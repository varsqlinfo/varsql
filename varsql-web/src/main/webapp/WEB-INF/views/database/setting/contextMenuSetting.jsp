<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/include/tagLib.jspf"%>

<div id="varsqlVueArea" class="context-menu-config display-off">
	<div class="wh100-abs">
		<div class="col-xs-12"  style="height:140px;">
			<div id="contentTreeMenu" class="col-xs-5 wh100 border" style="overflow:auto;" >
				<ul>
					<li v-for="(item,index) in contextItems">
						<a :class="deteilItem == item?'active':''">
							<span @click="viewItem(item,'parent')">{{item.name}}</span>

							<span class="pull-right">
								<button class="btn btn-sm btn-default" @click="addChildItem(item)"><spring:message code="sub.add"/></button>
								<button class="btn btn-sm btn-default" @click="removeItem(contextItems,index,item)"><spring:message code="delete"/></button>
							</span>
						</a>
						<template v-if="item.templateInfos.length > 0">
							<ul class="sub-list">
								<li v-for="(childItem,index2) in item.templateInfos">
									<a :class="deteilItem == childItem ? 'active':''">
										<span @click="viewItem(childItem,'child')">{{childItem.name}}</span>
										<span class="pull-right">
											<button class="btn btn-sm btn-default" @click="removeItem(item.templateInfos, index2, childItem)"><spring:message code="delete"/></button>
										</span>
									</a>
								</li>
							</ul>
						</template>
					</li>
				</ul>
			</div>
			<div class="col-xs-7">
				<div class="col-sm-12 padding0">
					<div class="pull-right">
						<button type="button" class="btn btn-md btn-default" @click="newItem()"><spring:message code="new"/></button>
						<button type="button" class="btn btn-md btn-default" @click="save()" v-show="viewItemType=='new'"><spring:message code="save"/></button>
					</div>
				</div>
				<div class="col-xs-12 padding0">
					<div class="field-group">
						<label class="col-xs-4 control-label"><spring:message code="contextmenu.name" text="컨텍스트명"/></label>
						<div class="col-xs-8">
							<input v-model="deteilItem.name" class="form-control text required input-sm">
						</div>
					</div>
					<div class="field-group" v-show="viewItemType=='child'">
						<label class="col-xs-4 control-label"><spring:message code="view.mode" text="보기 방식"/></label>
						<div class="col-xs-8">
							<label class="checkbox-container display-inline"><spring:message code="view.mode.editor" text="에디터보기"/>
							  <input type="radio" v-model="deteilItem.viewMode" value="editor" checked="checked">
							  <span class="radiomark"></span>
							</label>
							<label class="checkbox-container display-inline"><spring:message code="view.mode.dialog" text="다이얼로그"/>
							  <input type="radio" v-model="deteilItem.viewMode" value="dialog" checked="checked">
							  <span class="radiomark"></span>
							</label>
						</div>
					</div>
				</div>
			</div>
		</div>

		<div class="col-xs-12 padding0" style="height:calc(100% - 175px);min-height:300px;padding-bottom: 5px;">
			<div id="subCodeDisabled" class="wh100-abs overlay-bg" style="z-index: 10;position: absolute;"></div>
			<div class="col-xs-12" style="height: 220px;">
				<div class="sub-cont-title">Template</div>
				<div style="height: calc(100% - 30px);">
					<div id="mainTemplate" class="wh100 sql-code-editor"></div>
				</div>
			</div>
			<div class="col-xs-12"  style="height:calc(100% - 220px) ;">
				<div class="sub-cont-title" style="padding-top: 3px;"><button @click="generate()">Check source</button></div>
				<textarea v-model="resultCode" class="wh100" style="height:calc(100% - 30px);" readonly></textarea>
			</div>
		</div>

		<div class="col-xs-12" style="padding-right: 10px;">
	    	<div class="pull-right">
	    		<button type="button" class="btn btn-md btn-default" @click="restoreDefault()"><spring:message code="restore.default"/></button>
	    		<button type="button" class="btn btn-md btn-default" @click="importInfo()"><spring:message code="import"/></button>
				<button type="button" class="btn btn-md btn-default" @click="exportInfo()"><spring:message code="export"/></button>
	    		<button type="button" class="btn btn-md btn-info" @click="contextSave()"><spring:message code="apply"/></button>
	    	</div>
	    </div>
	</div>
</div>

<varsql:importResources resoures="codeEditor" editorHeight="100%"/>

<script>
(function (){
	var allContextInfo =  ${settingInfo};

	var defaultTableColumnInfo = {
        'table' : {"name":"test_table"  ,"remarks":"test table"}
        ,'columns' : [
    	    {"no":0,"name":"test_id","dataType":"STRING","typeName":"VARCHAR","typeAndLength":"VARCHAR(32)","length":32,"nullable":"N","comment":"","constraints":"PK","autoincrement":null,"defaultVal":""},{"no":0,"name":"link_id","dataType":"STRING","typeName":"VARCHAR","typeAndLength":"VARCHAR(32)","length":32,"nullable":"N","comment":"","constraints":"PK","autoincrement":null,"defaultVal":""},{"no":0,"name":"open_type","dataType":"CHAR","typeName":"CHAR","typeAndLength":"CHAR(1)","length":1,"nullable":"Y","comment":"","constraints":"","autoincrement":null,"defaultVal":""},{"no":0,"name":"open_option","dataType":"STRING","typeName":"VARCHAR","typeAndLength":"VARCHAR(1000)","length":1000,"nullable":"Y","comment":"","constraints":"","autoincrement":null,"defaultVal":""},{"no":0,"name":"img_src","dataType":"STRING","typeName":"VARCHAR","typeAndLength":"VARCHAR(500)","length":500,"nullable":"Y","comment":"","constraints":"","autoincrement":null,"defaultVal":""},{"no":0,"name":"sort_order","dataType":"NUMERIC","typeName":"NUMERIC","typeAndLength":"NUMERIC(6)","length":6,"nullable":"Y","comment":"","constraints":"","autoincrement":null,"defaultVal":""},{"no":0,"name":"reg_id","dataType":"STRING","typeName":"VARCHAR","typeAndLength":"VARCHAR(50)","length":50,"nullable":"N","comment":"","constraints":"","autoincrement":null,"defaultVal":""},{"no":0,"name":"reg_dt","dataType":"TIMESTAMP","typeName":"TIMESTAMP","typeAndLength":"TIMESTAMP","length":19,"nullable":"N","comment":"","constraints":"","autoincrement":null,"defaultVal":"CURRENT_TIMESTAMP"}
    	]
    };

	VarsqlAPP.vueServiceBean( {
	    el: '#varsqlVueArea'
	    ,data: {
	        viewItemType : 'new'
	        ,resultCode : ''
	        ,mainTemplateEditor : {}
	        ,subCodeTemplateEditor : {}
	        ,deteilItem :{}
	        ,contextItems : []
	        ,templateInfo : {}
	        ,codeDetailInfo: {}
	        ,codeDetailCompileResult :''
	        ,conuid: '<c:out value="${param.conuid}" escapeXml="true"></c:out>'
	    }
	    ,created : function (){
	    	this.templateInfo = this.createSubItem();

	    	try{
	    		this.contextItems = allContextInfo;

	    		if(!VARSQL.isArray(this.contextItems)){
	    			if(VARSQL.confirmMessage('msg.setting.fail.restore')){
	    				this.restoreDefault(false);
	    			}
	    		}

	    	}catch(e){
	    		VARSQLUI.toast.open(e);
	    		this.contextItems = [];
	    	}
	    }
	    ,methods:{

	        init : function(){
	        	
	        	$.pubSplitter('.splitter',{
					handleSize : 5
				});
	        	
	        	VARSQLCont.init('${dbtype}');
	        	this.initEditor();
	        	this.newItem();
	        }
	    	,initEditor : function (){
	    		var _this = this;
				// main code
	    		this.mainTemplateEditor = new codeEditor(document.getElementById('mainTemplate'), {
					schema: '',
					editorOptions: { 
						theme: 'vs-'+VARSQLUI.theme()
						,minimap: {enabled: false} 
						,contextmenu :false
					},
					change : ()=>{
						_this.templateInfo.main = _this.mainTemplateEditor.getValue();
						_this.generate();
					}
				})
				
	    	}
	    	,getSaveItem : function (){
	    		var contextItems = this.contextItems;

	    		for(var i =0; i < contextItems.length;i++){
	    			var item =contextItems[i];
	    			var templateInfos = item.templateInfos;
	    			for(var j =0; j < templateInfos.length;j++){
	    				var templateInfo = templateInfos[j];

	    				var result =VARSQLTemplate.render.generateSource(templateInfo, defaultTableColumnInfo);

	    				if(result.isError){
	    	    			VARSQL.toastMessage('msg.setting.invalid');
	    	    			this.viewItem(templateInfo, 'child');
	    	    			return null;
	    	    		}
	    			}
	    		}
	    		
	    		return contextItems; 
	    		
	    	}
	    	// 정보 저장 및 적용.
	    	,contextSave :function (){
	    		var contextItems = this.getSaveItem();
				if(contextItems == null){
					return ;
				}

	    		if(!VARSQL.confirmMessage('msg.saveandreload')){
	    			return ;
	    		}

	    		VARSQLApi.preferences.save({
	    			conuid : this.conuid
	    			,prefKey : 'main.contextmenu.serviceobject'
	    			,prefVal : JSON.stringify(contextItems)
	    		});
	    	}
	    	,exportInfo : function (){
	    		
	    		var contextItems = this.getSaveItem();
				if(contextItems == null){
					return ;
				}
				
				
	    		var params ={
    				exportType :'text'
    				,fileName :'contextmenu-template.json'
    				,content : JSON.stringify(contextItems, null, 2)
    			};
	    		

    			VARSQL.req.download({
    				type: 'post'
    				,url: '/download'
    				,params: params
    			});
	    	}
	    	// 초기화
	    	,restoreDefault :function (messageView){

	    		if(messageView !== false){
		    		if(!VARSQL.confirmMessage('msg.init.restore.confirm')){
		    			return ;
		    		}
	    		}

	    		VARSQLApi.preferences.save({
	    			conuid : this.conuid
	    			,prefKey : 'main.contextmenu.serviceobject'
	    			,prefVal : ''
	    		}, function (){
	    			location.href = location.href;
	    		});
	    	}
	    	// item 보기
	    	,viewItem : function (item, type){

	    		this.viewItemType = type;
	    		this.deteilItem = item;
	    		this.resultCode = '';

	    		var codeDetailInfo = {};
	    		if(type == 'parent'){
	    			this.templateInfo = this.createSubItem();
	    		}else{
	    			this.templateInfo = item;
	    		}

	    		this.setSubArea();
	    	}
	    	// 신규
	    	,newItem : function (){
	    		this.viewItemType = 'new';
				this.deteilItem = {
					name : ''
					,templateInfos :[]
				}
				this.setSubArea();
	    	}
	    	,setSubArea : function (){
	    		
	    		if(VARSQL.isUndefined(this.deteilItem.main)){
	    			$('#subCodeDisabled').show();
	    		}else{
	    			$('#subCodeDisabled').hide();
	    		}
	    		this.mainTemplateEditor.setValue(VARSQL.str.trim(this.templateInfo.main||''), 'handlebars');
	    		
	    	}
	    	,createSubItem : function (pItem){
	    		return {
					name : 'sub-menu'
					,viewMode : 'editor'
					,main : ''
				};
	    	}
	    	// 하위 메뉴 추가.
	    	,addChildItem : function (pItem) {
	    		var item = this.createSubItem(pItem);
	    		pItem.templateInfos.push(item);
	    		this.viewItem(item, 'child');
	    	}
	    	// 신규  item 정보 저장
	    	,save : function (){
				this.contextItems.push(this.deteilItem);

				this.newItem();
  
				this.$nextTick(function() {
					$('#contentTreeMenu').scrollTop($('#contentTreeMenu').prop('scrollHeight'));
			    });
	    	}
	    	// item 삭제.
	    	,removeItem : function (items,index,item){
	    		if(!VARSQL.confirmMessage('msg.delete.confirm')){
					return ;
				}

				items.splice(index,1);
	    	}
	    	//  generate source
	        , generate : function(){
	        	
	        	if(VARSQL.str.trim(this.templateInfo.main||'') == ''){
	        		this.resultCode = '';
	        		return ; 
	    		}

	        	var result =VARSQLTemplate.render.generateSource(this.templateInfo, defaultTableColumnInfo, false);
	        	
				if(result.isError){
	    			VARSQLUI.toast.open(result.errorInfo.msg);
	    			return ;
	    		}else{
	    			this.resultCode =result.value;
	    		}
	        }
	        ,errorHandler : function (errorType , err){
	        	var msg = err.message;
	        	if('main' == errorType){
					this.resultCode = msg;
	        	}else {
	        		this.codeDetailCompileResult = msg;
	        	}
	        }
	    }
	});
})();

</script>
