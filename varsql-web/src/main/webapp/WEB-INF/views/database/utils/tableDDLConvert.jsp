<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/include/tagLib.jspf"%>

<div id="specMain" class="wh100">
	<div class="menu-tools-body col-xs-12 scroll-y">
		<div v-show="step==1">
			<div class="col-xs-12">
				<div class="process-title"><spring:message code="msg.export.spec.step2" /></div>

				<c:if test="${schemaInfo ne ''}">
					<div style="padding: 5px 0px 0px;">
						<span class="control-label" style="width:100px;font-weight: bold;margin-right:5px;"><spring:message code="db.schema" /> : </span>
						<select v-model="selectSchema" @change="getTableList()" style="width: calc(100% - 100px);">
							<c:forEach var="item" items="${schemaList}" begin="0" varStatus="status">
								<option value="${item}">${item}</option>
							</c:forEach>
						</select>
					</div>
				</c:if>
				
				<div style="padding: 5px 0px 0px;">
					<span class="control-label" style="width:100px;font-weight: bold;margin-right:5px;"><spring:message code="database" /> : </span>
					<select v-model="selectConvertDB" style="width: calc(100% - 100px);">
						<c:forEach var="item" items="${dbTypeList}" begin="0" varStatus="status">
							<c:if test="${item.dbVenderName ne 'other'}">
								<option value="${item.name}" >${item.name}</option>
							</c:if>
						</c:forEach>
					</select>
				</div>
				
				<div class="process-desc" style="padding: 5px 0px 5px;"><spring:message code="msg.table.dbclick.move" /></div>
			</div>
			<div class="wh100-relative table-select-area" style="float: left;">
				<div class="col-xs-12">
					<div class="top-select mbottom-10 fb tl mRight-20"><spring:message code="db.table" /></div>
					<div id="source" style="height: 200px;width: 100%;"></div>
				</div>
			</div>
			<div style="clear:both;"></div>
		</div>
		
		<div v-show="step==2">
			<div class="process-title"><spring:message code="result" /></div>
			<div style="height:300px">
				<div class="wh100 sql-code-editor" id="tableDDLInfo"></div>
			</div>
		</div>
		
		<div class="process-step-area" style="margin-top: 20px;text-align: center;">
			<div class="process-step-btn-area">
				<template v-if="step==1">
					<button type="button" class="btn-md" @click="convert()"><spring:message code="convert" /></button>
				</template>
				<template v-else>
					<button type="button" class="btn-md" @click="download()"><spring:message code="download" /></button>
					<button type="button" class="btn-md" @click="step=1"><spring:message code="step.prev" /></button>
				</template>
			</div>
		</div>
	</div>
</div>

<varsql:importResources resoures="codeEditor" editorHeight="100%"/>

<script>
VarsqlAPP.vueServiceBean({
	el: '#specMain'
	,data: {
		step : 1
		,endStep : 3
		,selectSchema : ''
		,selectConvertDB : ''
		,selectTableObj : {}
		,ddl:''
	}
	,methods:{
		init : function (){
			this.selectSchema = '${currentSchemaName}';
			this.setUserConfigInfo();
			
			this.tableDDLEditor = new codeEditor(document.getElementById('tableDDLInfo'), {
				schema: '',
				editorOptions: { 
					theme: 'vs-light'
					,minimap: {enabled: true} 
					,contextmenu :false
				}
			})
		}
		
		// 완료
		,convert : function (){
			var _self = this;
			if(_self.selectTableObj.getTargetItem().length < 1){
				VARSQL.alertMessage('msg.select.param', VARSQL.message('table'));
    			return ;
    		}
			
			if(VARSQL.isBlank(_self.selectConvertDB)){
				VARSQL.alertMessage('msg.select.param', VARSQL.message('db'));
    			return ;
    		}

			_self.exportInfo();
		}
		,exportInfo : function (){
			var _self = this;

			var tableList =[];
			_self.selectTableObj.getTargetItem().forEach(item=>{
				tableList.push({name : item.name});
			}) 
			
			var prefVal = {
				schema : _self.selectSchema
				,tables : tableList
			};
			
			this.step =2;

			var param = {
				prefVal : JSON.stringify(prefVal)
				,schema : _self.selectSchema
				,conuid : '${param.conuid}'
				,convertDb : _self.selectConvertDB
			};
			
			VARSQL.req.ajax({
				url: {type:VARSQL.uri.database, url:'/utils/gen/convertTableDDL'}
				,data: param
				,loadSelector : 'body'
				,success:function (resData){
					var list = resData.list||[];
					
					var scriptArr = [];
					
					list.forEach(item=>{
						scriptArr.push(item.createScript);
					})
					
					_self.tableDDLEditor.setValue(scriptArr.join('\n\n'));
					
				}
			});
		}
		,download:function (){
			var params ={
   				exportType :'sql'
   				,fileName :'table-convert-ddl'+VARSQL.util.dateFormat(new Date(),'yyyy-mm-dd')
   				,content : this.tableDDLEditor.getValue()
   			};
    		
   			VARSQL.req.download({
   				type: 'post'
   				,url: '/download'
   				,params: params
   			});
		}
		,setUserConfigInfo : function (){
			var _self = this;

			_self.setTableSelect();
			_self.getTableList();

		}
		,getTableList : function(){
			var _self = this;

			var param = {
				conuid : '${param.conuid}'
				,schema : this.selectSchema
			}

			VARSQL.req.ajax({
				url : {type:VARSQL.uri.database, url:'/tools/export/specMain/tableList'}
				,data: param
				,loadSelector : '.table-select-area'
				,success:function (resData){
					var list = resData.list;

					_self.selectTableObj.setSourceItem(list);
					_self.selectTableObj.setTargetItem([]);
				}
			});
		}
		// table select info
		,setTableSelect: function (){
			var _self = this;

			var tmpSourceItem = [] , paramSourceItem=[];

			_self.selectTableObj= $.pubMultiselect('#source', {
				duplicateCheck : true
				,header : {
					enableSourceLabel : true 	// source header label 보일지 여부
					,enableTargetLabel : false 	// target header label 보일지 여부
				}
				,valueKey : 'name'	
				,labelKey : 'name'
				,source : {
					emptyMessage : '<spring:message code="msg.export.spec.schema.select" />'
					,items : paramSourceItem
					,search :{
						enable : true
					}
				}
				,target : {
					items : []
				}
				,footer : {
					enable : true
				}
			});
		}
	}
});
</script>



