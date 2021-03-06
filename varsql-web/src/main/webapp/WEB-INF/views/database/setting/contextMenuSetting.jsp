<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/include/tagLib.jspf"%>

<div id="varsqlVueArea" class="context-menu-config display-off">
	<div class="wh100-abs">
		<div class="col-xs-12"  style="height:140px;">
			<div class="col-xs-5 wh100 border" style="overflow:auto;" >
				<ul>
					<li v-for="(item,index) in contextItems">
						<a :class="deteilItem == item?'active':''">
							<span @click="viewItem(item,'catg')">{{item.name}}</span>

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
						<button type="button" class="btn btn-md btn-default" @click="save()" v-show="this.viewMode=='new'"><spring:message code="btn.save"/></button>
					</div>
				</div>
				<div class="col-xs-12 padding0">
					<div class="field-group">
						<label class="col-xs-2 control-label">컨텍스트명</label>
						<div class="col-xs-10">
							<input v-model="deteilItem.name" class="form-control text required input-sm">
						</div>
					</div>
					<div class="field-group">
						<label class="col-xs-2 control-label">정렬순서</label>
						<div class="col-xs-10">
							<input v-model="deteilItem.sortOrder" class="form-control text required input-sm">
						</div>
					</div>
				</div>
			</div>
		</div>

		<div class="col-xs-12 padding0" style="height:calc(100% - 170px);min-height:300px;padding-bottom: 10px;">
			<div id="subCodeDisabled" class="wh100-abs" style="z-index: 10;position: absolute;background: #efefef;opacity: 0.7;"></div>
			<div class="col-xs-12" style="height: 220px;">
				<div class="col-xs-7 padding0 h100">
					<div class="sub-cont-title">Template</div>
					<div style="height: calc(100% - 30px);">
						<textarea id="mainTemplate" class="wh100"></textarea>
					</div>
				</div>
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

<varsql:editorResource editorHeight="100%"/>

<script>

Handlebars.registerHelper("camelCase", function(text, options) {
    return VARSQL.util.convertCamel(text);
});

Handlebars.registerHelper("upperCase", function(text, options) {
    return VARSQL.util.toUpperCase(text);
});

Handlebars.registerHelper("lowerCase", function(text, options) {
    return VARSQL.util.capitalize(text);
});

Handlebars.registerHelper("capitalize", function(text, options) {
    return  VARSQL.util.capitalize(text);;
});

Handlebars.registerHelper("addChar", function(idx,ch) {
	return idx > 0 ? ch : '';
});

Handlebars.registerHelper("addPreSuffix", function(prefix, suffix , text) {
	return prefix + text + suffix;
});

Handlebars.registerHelper("isPk", function(item) {

	var constraintVal = item[VARSQLCont.tableColKey.CONSTRAINTS];
	if(constraintVal =='PK' || constraintVal.indexOf('PRIMARY') > -1 ){
		return true;
	}
	return false;
});

Handlebars.registerHelper("pkColumns", function(items) {
	var reval = [];

	var constraintsKey = VARSQLCont.tableColKey.CONSTRAINTS;
	for(var i =0 ; i<items.length;i++){
		var item = items[i];
		var constraintVal = item[constraintsKey];
		if(constraintVal =='PK' || constraintVal.indexOf('PRIMARY') > -1 ){
			reval.push(item);
		}
	}

	return reval;
});

Handlebars.registerHelper('xif', function (v1,o1,v2,mainOperator,v3,o2,v4,options) {
    var operators = {
         '==': function(a, b){ return a==b},
         '===': function(a, b){ return a===b},
         '!=': function(a, b){ return a!=b},
         '!==': function(a, b){ return a!==b},
         '<': function(a, b){ return a<b},
         '<=': function(a, b){ return a<=b},
         '>': function(a, b){ return a>b},
         '>=': function(a, b){ return a>=b},
         '&&': function(a, b){ return a&&b},
         '||': function(a, b){ return a||b},
      }
    var a1 = operators[o1](v1,v2);
    var a2 = operators[o2](v3,v4);
    var isTrue = operators[mainOperator](a1, a2);
    return isTrue ? options.fn(this) : options.inverse(this);
});

(function (){
	var tableInfo = {"name":"test_table"  ,"remarks":"test table"};

	var columns = [
	    {"no":0,"name":"test_id","dataType":"STRING","typeName":"VARCHAR","typeAndLength":"VARCHAR(32)","length":32,"nullable":"N","comment":"","constraints":"PK","autoincrement":null,"defaultVal":""},{"no":0,"name":"link_id","dataType":"STRING","typeName":"VARCHAR","typeAndLength":"VARCHAR(32)","length":32,"nullable":"N","comment":"","constraints":"PK","autoincrement":null,"defaultVal":""},{"no":0,"name":"open_type","dataType":"CHAR","typeName":"CHAR","typeAndLength":"CHAR(1)","length":1,"nullable":"Y","comment":"","constraints":"","autoincrement":null,"defaultVal":""},{"no":0,"name":"open_option","dataType":"STRING","typeName":"VARCHAR","typeAndLength":"VARCHAR(1000)","length":1000,"nullable":"Y","comment":"","constraints":"","autoincrement":null,"defaultVal":""},{"no":0,"name":"img_src","dataType":"STRING","typeName":"VARCHAR","typeAndLength":"VARCHAR(500)","length":500,"nullable":"Y","comment":"","constraints":"","autoincrement":null,"defaultVal":""},{"no":0,"name":"sort_order","dataType":"NUMERIC","typeName":"NUMERIC","typeAndLength":"NUMERIC(6)","length":6,"nullable":"Y","comment":"","constraints":"","autoincrement":null,"defaultVal":""},{"no":0,"name":"reg_id","dataType":"STRING","typeName":"VARCHAR","typeAndLength":"VARCHAR(50)","length":50,"nullable":"N","comment":"","constraints":"","autoincrement":null,"defaultVal":""},{"no":0,"name":"reg_dt","dataType":"TIMESTAMP","typeName":"TIMESTAMP","typeAndLength":"TIMESTAMP","length":19,"nullable":"N","comment":"","constraints":"","autoincrement":null,"defaultVal":"CURRENT_TIMESTAMP"}
	];
	var allContextInfo =  ${settingInfo};

	VarsqlAPP.vueServiceBean( {
	    el: '#varsqlVueArea'
	    ,data: {
	        viewMode : 'new'
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
	    		this.contextItems = VARSQL.isArray(allContextInfo)?allContextInfo : allContextInfo.items

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
	        	VARSQLCont.init('other' , {});
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
	    	// 정보 저장 및 적용.
	    	,contextSave :function (){
	    		if(!confirm(VARSQL.messageFormat('varsql.0020'))){
	    			return ;
	    		}

	    		VARSQLApi.preferences.save({
	    			conuid : this.conuid
	    			,prefKey : 'main.contextmenu.serviceobject'
	    			,prefVal : JSON.stringify(this.contextItems)
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

	    		this.viewMode = 'view';
	    		this.deteilItem = item;
	    		this.resultCode = '';

	    		var codeDetailInfo = {};
	    		if(type =='catg'){
	    			this.templateInfo = this.createSubItem();
	    		}else{
	    			this.templateInfo = item;
	    			codeDetailInfo = item.propItems[0]||{};
	    		}

	    		this.setSubArea();
	    		this.setGenCode(codeDetailInfo);
	    	}
	    	// 신규
	    	,newItem : function (){
	    		this.viewMode = 'new';
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
					,main : ''
					,propItems : []
				};
	    	}
	    	,addChildItem : function (pItem) {
				this.viewMode = 'child';
	    		var item = this.createSubItem(pItem);

	    		pItem.templateInfos.push(item);

	    		this.deteilItem = item;
	    	}
	    	// 신규  item 정보 저장
	    	,save : function (){
				this.contextItems.push(this.deteilItem);
				this.newItem();
	    	}
	    	// item 삭제.
	    	,removeItem : function (items,index,item){
	    		if(!confirm(VARSQL.messageFormat('varsql.0016'))){
					return ;
				}

				items.splice(index,1);
	    	}
	        , generate : function(){

	        	var template='';
	            try{
	            	template = Handlebars.compile(this.templateInfo.main);
	            }catch(e){
	            	this.errorHandler('main',e);
	            	return ;
	            }
	            var allPropItems = this.templateInfo.propItems;

	            var allParam = {
	                'table' : tableInfo
	                ,'columns' : columns
	            };

	            for(var i=0; i< allPropItems.length; i++){
	                var item = allPropItems[i];
	                this.compilePropCode(item);
	                allParam[item.key] = VARSQL.str.rtrim(item.compileValue);
	            }

	            var html = template(allParam);
	            this.resultCode = html;
	        }
	        , compilePropCode : function (item){
	            var propItem = item || this.codeDetailInfo;

	            try{
	            	if(propItem.code){
	            		var template = Handlebars.compile(propItem.code);
	                    propItem.compileValue = template({
	                        'table' : tableInfo
	                        ,'columns' : columns
	                    });
	            	}
	            }catch(e){
	            	this.errorHandler('prop',e);
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
	            var parser;
	            try{
	            	parser = Handlebars.parse(source);
	            	this.generate();
	            }catch(e){
	            	this.errorHandler('main',e);
	            	return ;
	            }

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
