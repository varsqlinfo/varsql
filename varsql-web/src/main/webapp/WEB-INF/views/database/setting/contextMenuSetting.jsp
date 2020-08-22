<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/include/tagLib.jspf"%>
<style>
ul >li{
    clear: both;
}
</style>
<div id="varsqlVueArea" class="display-off">
	<div class="col-xs-12"  style="height:140px;overflow:auto;">
		<div class="col-xs-5">
			<ul>
				<li v-for="(item,index) in contextItems">
					<a>
						<span>{{item.name}}</span>

						<span class="pull-right">
							<button class="btn btn-sm btn-default" @click="addChildItem(item)"><spring:message code="btn.sub.add"/></button>
							<button class="btn btn-sm btn-default" @click="removeItem(contextItems,index,item)"><spring:message code="btn.delete"/></button>
						</span>
					</a>
					<template v-if="item.child.length > 0">
						<ul class="sub-list">
							<li v-for="(childItem,index2) in item.child">
								<a>
									<span>{{childItem.name}}</span>
									<span class="pull-right">
										<button class="btn btn-sm btn-default" @click="removeItem(item.child, index2, childItem)"><spring:message code="btn.delete"/></button>
									</span>
								</a>
							</li>
						</ul>
					</template>
				</li>
			</ul>
		</div>
		<div class="col-xs-7">
			<div class="col-sm-12">
				<div class="pull-right">
					<button type="button" class="btn btn-md btn-default" @click="newItem()"><spring:message code="btn.new"/></button>
					<button type="button" class="btn btn-md btn-default" @click="save()" v-show="this.addMode=='new'"><spring:message code="btn.save"/></button>
				</div>
			</div>
			<div class="col-xs-12">
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

	<div class="col-xs-12" style="height:300px;">
		<div class="col-xs-8 h100">
			<div class="col-xs-5 h100">
				template :
				<textarea id="genSourceCode" v-model="template" class="wh100" @blur="setProperty()"></textarea>
			</div>
			<div class="col-xs-7 h100">
				<div>
					<table class="w100">
					    <colgroup style="width:130px;"></colgroup>
					    <colgroup style="width:*;"></colgroup>
					    <tr>
					        <td style="vertical-align: top;">
					        	key :
					            <ul>
					                <template v-for="(item,index) in allTemplateProp">
					                    <li><a @click="setGenCode(item)">{{item.key}}</a> <a v-if="item.isRemove===true" @click="removeTemplateProp(index);">삭제</a></li>
					                </template>
					            </ul>
					        </td>
					        <td>
					        	code :
					            <textarea v-model="propItem.code"  style="width:100%;height: 150px"></textarea>
					        </td>
					    </tr>
					</table>
				</div>
				<div style="height:calc(100% - 150px);">
					 <pre class="wh100">{{propItem.compileValue}}</pre>
				</div>
			</div>
		</div>
		<div class="col-xs-4 h100">
			<div><button @click="generate()">gensource</button></div>
			<textarea id="resultCode" class="wh100">{{resultCode}}</textarea>
		</div>
	</div>
</div>

<script>
var tableInfo = {"name":"t_banner_link_mapping"
    ,"remarks":""
};

var columns = [
    {"no":0,"name":"code_id","dataType":"STRING","typeName":"VARCHAR","typeAndLength":"VARCHAR(32)","length":32,"nullable":"N","comment":"","constraints":"PK","autoincrement":null,"defaultVal":""},{"no":0,"name":"link_id","dataType":"STRING","typeName":"VARCHAR","typeAndLength":"VARCHAR(32)","length":32,"nullable":"N","comment":"","constraints":"PK","autoincrement":null,"defaultVal":""},{"no":0,"name":"open_type","dataType":"CHAR","typeName":"CHAR","typeAndLength":"CHAR(1)","length":1,"nullable":"Y","comment":"","constraints":"","autoincrement":null,"defaultVal":""},{"no":0,"name":"open_option","dataType":"STRING","typeName":"VARCHAR","typeAndLength":"VARCHAR(1000)","length":1000,"nullable":"Y","comment":"","constraints":"","autoincrement":null,"defaultVal":""},{"no":0,"name":"img_src","dataType":"STRING","typeName":"VARCHAR","typeAndLength":"VARCHAR(500)","length":500,"nullable":"Y","comment":"","constraints":"","autoincrement":null,"defaultVal":""},{"no":0,"name":"sort_order","dataType":"NUMERIC","typeName":"NUMERIC","typeAndLength":"NUMERIC(6)","length":6,"nullable":"Y","comment":"","constraints":"","autoincrement":null,"defaultVal":""},{"no":0,"name":"reg_id","dataType":"STRING","typeName":"VARCHAR","typeAndLength":"VARCHAR(50)","length":50,"nullable":"N","comment":"","constraints":"","autoincrement":null,"defaultVal":""},{"no":0,"name":"reg_dt","dataType":"TIMESTAMP","typeName":"TIMESTAMP","typeAndLength":"TIMESTAMP","length":19,"nullable":"N","comment":"","constraints":"","autoincrement":null,"defaultVal":"CURRENT_TIMESTAMP"}
];
</script>

<script id="template" type="text/varsql-template">
@Entity
@Table(name = {{table.name}}._TB_NAME)
public class {{table.name}}{
    public final static String _TB_NAME="{{camelCase table.name}}";

    {{classProperty}}
    {{get_set}}
}
</script>

<script id="classPropertyTemplate" type="text/varsql-template">
{{#columns}}
    private {{typeName}} {{camelCase name}}; {{comment}}
{{/columns}}
</script>

<script id="get_setPropertyTemplate" type="text/varsql-template">
{{#columns}}
    public {{typeName}} get{{capitalize (camelCase name)}}(){
        return this.{{camelCase name}};
    }
{{/columns}}
</script>


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

var allPropArr = [
    {
        key : 'classProperty'
        ,code : document.getElementById('classPropertyTemplate').innerHTML
        ,compileValue : ''
    }
    ,{
        key : 'get_set'
        ,code : document.getElementById('get_setPropertyTemplate').innerHTML
        ,compileValue : ''
    }
]

var contextItems = [
	{
		name : '일반'
		,sortOrder : 1
		,child :[]
	}
	,{
		name : 'mybatis'
		,sortOrder : 2
		,child :[]
	}
	,{
		name : 'JAVA'
		,sortOrder : 3
		,child :[]
	}
]

VarsqlAPP.vueServiceBean( {
    el: '#varsqlVueArea'
    ,validateCheck : true
    ,data: {
        addMode : 'new'
    	,template : ''
        ,allTemplateProp : allPropArr
        ,resultCode : ''
        ,propItem : {
            key : ''
            ,code : ''
            ,compileValue :''
        }
        ,deteilItem :{}
        ,contextItems : contextItems
    }
    ,watch: {
        'propItem.code': {
            handler: function (after, before) {
                this.compilePropCode();
            },
            deep: true
        }
    }
    ,methods:{
        init : function(){
        	this.newItem();
        	this.template = document.getElementById('template').innerHTML;
        }
    	,newItem : function (){
    		this.addMode = 'new';
			this.deteilItem = {
				name : ''
				,sortOrder : this.contextItems.length +1
				,child :[]
			}
    	}
    	,addChildItem : function (pItem) {
			this.addMode = 'child';
    		var item = {
				name : 'sub-menu'
				,sortOrder : pItem.child.length +1
			}
    		pItem.child.push(item);

    		this.deteilItem = item;
    	}
    	,save : function (){
			this.contextItems.push(this.deteilItem);
    	}
    	,removeItem : function (items,index,item){
    		if(!confirm(VARSQL.messageFormat('varsql.0016'))){
				return ;
			}

			items.splice(index,1);

    	}
        , generate : function(){
            var template = Handlebars.compile(this.template);
            var allTemplateProp = this.allTemplateProp;

            var allParam = {
                'table' : tableInfo
                ,'columns' : columns
            };

            for(var i=0; i< allTemplateProp.length; i++){
                var item = allTemplateProp[i];
                this.compilePropCode(item);
                allParam[item.key] = item.compileValue;
            }

            var html = template(allParam);

            this.resultCode = html;
        }
        , compilePropCode : function (item){
            var propItem = item || this.propItem;
            try{
                var template = Handlebars.compile(propItem.code);
                propItem.compileValue = template({
                    'table' : tableInfo
                    ,'columns' : columns
                });

            }catch(e){
                console.log(e)
            }
        }
        , setGenCode : function(item){
            this.propItem = item;

            if(item.code !=''  && item.compileValue ==''){

            }
        }
        ,removeTemplateProp : function (idx){
            this.allTemplateProp.splice(idx,1);
        }
        , setProperty: function(){
            var source = this.template;
            var parser = Handlebars.parse(source);

            var len = parser.body.length;
            var allTemplateProp = this.allTemplateProp;

            if(len > 0){

                var allTEmplateObj = {};

                for(var i=0; i< allTemplateProp.length; i++){
                    var bodyItem = allTemplateProp[i];

                    allTEmplateObj[bodyItem.key]  = bodyItem;
                }

                var addPropItems = [];
                for(var i=0; i< len; i++){
                    var bodyItem = parser.body[i];

                    if(bodyItem.type=='MustacheStatement'){

                        var propKey = bodyItem.path.original;

                        if(propKey.indexOf('.') > -1 || bodyItem.params.length > 0){
                            continue;
                        }

                        var templateProp;

                        if(allTEmplateObj[propKey]){
                            templateProp = allTEmplateObj[propKey];
                            delete allTEmplateObj[propKey];
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

                for(var key in allTEmplateObj){
                    var bodyItem = allTEmplateObj[key];
                    bodyItem.isRemove = true;
                    addPropItems.push(bodyItem);
                }

                this.allTemplateProp =  addPropItems;
            }else{
                for(var i=0; i< allTemplateProp.length; i++){
                    var bodyItem = allTemplateProp[i];
                    bodyItem.isRemove = true;
                }
            }
        }
    }
});
</script>