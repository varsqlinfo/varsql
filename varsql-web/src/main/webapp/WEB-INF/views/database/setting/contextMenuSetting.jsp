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
								<button class="btn btn-sm btn-default" @click="addChildItem(item)"><spring:message code="btn.sub.add"/></button>
								<button class="btn btn-sm btn-default" @click="removeItem(contextItems,index,item)"><spring:message code="btn.delete"/></button>
							</span>
						</a>
						<template v-if="item.templateInfos.length > 0">
							<ul class="sub-list">
								<li v-for="(childItem,index2) in item.templateInfos">
									<a :class="deteilItem == childItem ? 'active':''">
										<span @click="viewItem(childItem,'child')">{{childItem.name}}</span>
										<span class="pull-right">
											<button class="btn btn-sm btn-default" @click="removeItem(item.templateInfos, index2, childItem)"><spring:message code="btn.delete"/></button>
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
						<button type="button" class="btn btn-md btn-default" @click="newItem()"><spring:message code="btn.new"/></button>
						<button type="button" class="btn btn-md btn-default" @click="save()" v-show="viewItemType=='new'"><spring:message code="btn.save"/></button>
					</div>
				</div>
				<div class="col-xs-12 padding0">
					<div class="field-group">
						<label class="col-xs-2 control-label"><spring:message code="label.contextmenu.name" text="컨텍스트명"/></label>
						<div class="col-xs-10">
							<input v-model="deteilItem.name" class="form-control text required input-sm">
						</div>
					</div>
					<div class="field-group" v-show="viewItemType=='child'">
						<label class="col-xs-2 control-label"><spring:message code="label.view.mode" text="보기 방식"/></label>
						<div class="col-xs-10">
							<label class="checkbox-container display-inline"><spring:message code="label.view.mode.editor" text="에디터보기"/>
							  <input type="radio" v-model="deteilItem.viewMode" value="editor" checked="checked">
							  <span class="radiomark"></span>
							</label>
							<label class="checkbox-container display-inline"><spring:message code="label.view.mode.dialog" text="다이얼로그"/>
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
				<div class="col-xs-7 padding0 h100">
					<div class="sub-cont-title">Template</div>
					<div style="height: calc(100% - 30px);">
						<textarea id="mainTemplate" class="wh100"></textarea>
					</div>
				</div>
				<div class="splitter" data-orientation="vertical"></div>
				<div class="col-xs-5 h100">
					<div class="sub-cont-title" style="padding-top: 3px;"><button @click="generate()">Check source</button></div>
					<textarea id="resultCode" class="wh100" style="height:calc(100% - 30px);">{{resultCode}}</textarea>
				</div>
			</div>
			<div class="col-xs-12"  style="height:calc(100% - 220px) ;">
				<div class="col-xs-2 h100 padding0">
					<div class="h100">
				      	<div class="sub-cont-title">Template key</div>

				      	<div class="border" style="height: calc(100% - 30px);overflow:auto;">
				            <ul>
				                <template v-for="(item,index) in templateInfo.propItems">
				                    <li><a :class="item == codeDetailInfo ? 'active':''" @click="setGenCode(item)">{{item.key}}</a> <a v-if="item.isRemove===true" @click="removeTemplateProp(index);">삭제</a></li>
				                </template>
				           </ul>
			           </div>
					</div>
				</div>
				<div class="col-xs-10 h100 padding0">
					<div class="sub-cont-title">Template code</div>
					<div style="height: calc(100% - 30px);">
						<div class="col-xs-8 h100">
							<textarea id="subCodeTemplate" class="wh100"></textarea>
						</div>
						<div class="splitter" data-orientation="vertical"></div>
						<div class="col-xs-4 h100">
							<pre class="wh100">{{codeDetailInfo.compileValue}}</pre>
						</div>
					</div>
				</div>
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
	        ,conuid: '<c:out value="${param.conuid}" escapeXml="true"></c:out>'
	    }
	    ,created : function (){
	    	this.templateInfo = this.createSubItem();

	    	try{
	    		this.contextItems = allContextInfo;

	    		if(!VARSQL.isArray(this.contextItems)){
	    			if(confirm(VARSQL.messageFormat('varsql.0022'))){
	    				this.restoreDefault(false);
	    			}
	    		}

	    	}catch(e){
	    		VARSQLUI.toast.open(e);
	    		this.contextItems = [];
	    	}
	    }
	    ,watch: {
	        'codeDetailInfo.code': {
	            handler: function (after, before) {
	                this.compilePropCode();
	            },
	            deep: true
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
	    		this.mainTemplateEditor = CodeMirror.fromTextArea(document.getElementById('mainTemplate'), {
					mode: 'text/x-handlebars-template',
					indentWithTabs: true,
					smartIndent: true,
					indentUnit : 4,
					lineNumbers: true,
					lineWrapping: false,
					matchBrackets : true,
					theme: "eclipse"
				})
				this.mainTemplateEditor.on('change', function(cm) {
					_this.templateInfo.main = cm.getValue();
			    })

			    this.mainTemplateEditor.on('blur', function(cm) {
					_this.templateInfo.main = cm.getValue();
					_this.setProperty();
			    })

			    // sub code
	    		this.subCodeTemplateEditor = CodeMirror.fromTextArea(document.getElementById('subCodeTemplate'), {
					mode: 'text/x-handlebars-template'
				})
				this.subCodeTemplateEditor.on('change', function(cm) {
					_this.codeDetailInfo.code = cm.getValue();
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
	    	    			VARSQLUI.toast.open(VARSQL.messageFormat('varsql.0026'));
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

	    		if(!confirm(VARSQL.messageFormat('varsql.0024'))){
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
		    		if(!confirm(VARSQL.messageFormat('varsql.0021'))){
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
	    			item.propItems = item.propItems ||[]; 
	    			this.templateInfo = item;
	    			codeDetailInfo = item.propItems[0]||{};
	    		}

	    		this.setSubArea();
	    		this.setGenCode(codeDetailInfo);
	    		this.generate();
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

	    		if(VARSQL.isArray(this.deteilItem.propItems)){
	    			$('#subCodeDisabled').hide();
	    		}else{
	    			$('#subCodeDisabled').show();
	    		}
	    		this.mainTemplateEditor.setValue(VARSQL.str.trim(this.templateInfo.main||''));
				this.mainTemplateEditor.setHistory({done:[],undone:[]});
	    	}
	    	,createSubItem : function (pItem){
	    		return {
					name : 'sub-menu'
					,viewMode : 'editor'
					,main : ''
					,propItems : []
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
	    		if(!confirm(VARSQL.messageFormat('varsql.0016'))){
					return ;
				}

				items.splice(index,1);
	    	}
	    	//  generate source
	        , generate : function(){

	        	var result =VARSQLTemplate.render.generateSource(this.templateInfo, defaultTableColumnInfo, false);

				if(result.isError){
	    			VARSQLUI.toast.open(result.errorInfo.msg);
	    			return ;
	    		}else{
	    			this.resultCode =result.value;
	    		}
	        }
	    	// property template code render
	        , compilePropCode : function (item){
	            var propItem = item || this.codeDetailInfo;
	            var _this =this;

            	if(propItem.code){
                    propItem.compileValue = VARSQLTemplate.render.text(propItem.code, defaultTableColumnInfo, function (e){
                    	_this.errorHandler('prop',e);
                    });
            	}
	        }
	        , setGenCode : function(item){

				this.codeDetailInfo = item;

				this.subCodeTemplateEditor.setValue(VARSQL.str.trim(this.codeDetailInfo.code||''));
				this.subCodeTemplateEditor.setHistory({done:[],undone:[]});
	        }
	        ,removeTemplateProp : function (idx){
	        	this.templateInfo.propItems.splice(idx,1);
	        }
	        , setProperty: function(){
	            var source = this.templateInfo.main;
	            var _this =this;
            	var	parser = VARSQLTemplate.parse(source ,function (e){
					_this.errorHandler('main',e);
				});
            	if(parser ===false) return false;

            	this.generate();

	            var len = parser.body.length;
	            var currentPropItems = this.templateInfo.propItems;

	            if(len > 0){

	                var addPropObj = {};

	                for(var i=0; i< currentPropItems.length; i++){
	                    var bodyItem = currentPropItems[i];

	                    addPropObj[bodyItem.key]  = bodyItem;
	                }

	                var addPropItems = [];
	                for(var i=0; i< len; i++){
	                    var bodyItem = parser.body[i];

	                    if(bodyItem.type=='MustacheStatement'){

	                        var propKey = bodyItem.path.original;

	                        propKey = propKey +'';

	                        if(propKey.indexOf('.') > -1 || bodyItem.params.length > 0){
	                            continue;
	                        }

	                        var templateProp;

	                        if(addPropObj[propKey]){
	                            templateProp = addPropObj[propKey];
	                            templateProp.isRemove =false;
	                            delete addPropObj[propKey];
	                        }else{
	                            templateProp = {
	                                key : propKey
	                                ,code : ''
	                                ,compileValue : ''
	                            };
	                        }
	                        addPropItems.push(templateProp);
	                    }
	                }

	                for(var key in addPropObj){
	                    var bodyItem = addPropObj[key];
	                    bodyItem.isRemove = true;
	                    addPropItems.push(bodyItem);
	                }

	                this.templateInfo.propItems =  addPropItems;
	            }else{
	                for(var i=0; i< currentPropItems.length; i++){
	                    var bodyItem = currentPropItems[i];
	                    bodyItem.isRemove = true;
	                }
	            }
	        }
	        ,errorHandler : function (errorType , err){
	        	var msg = err.message;
	        	if('main' == errorType){
					this.resultCode = msg;
	        	}else {
					this.codeDetailInfo.compileValue = msg;
	        	}
	        }
	    }
	});
})();

</script>
