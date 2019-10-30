<!DOCTYPE html>
<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/include/tagLib.jspf"%>
<html>
<head>
<title><spring:message code="database.tools" /></title>
<%@ include file="/WEB-INF/include/database-dialog-head.jsp"%>

</head>
<BODY class="preferences">
	<div id="specMain" class="wh100">
		<div class="preferences-nav col-xs-3">
			<ul>
				<li :class="step==1?'active':''" @click="selectStep(1)">
					<a href="javascript:;"><spring:message code="msg.export.spec.step1" /></a>
				</li>
				<li :class="step==2?'active':''" @click="selectStep(2)">
					<a href="javascript:;"><spring:message code="msg.export.spec.step2" /></a>
				</li>
				<li :class="step==3?'active':''" @click="selectStep(3)">
					<a href="javascript:;"><spring:message code="msg.export.spec.step3" /></a>
				</li>
			</ul>
		</div>
		<div class="preferences-body process-step-area col-xs-9 scroll-y">
			<div class="process-step" :class="step==1?'active':''">
				<div class="col-xs-12">
					<div class="process-title"><spring:message code="msg.table.export.info" /></div>
				</div>
				<div class="col-xs-12">
					<form id="firstConfigForm" name="firstConfigForm" class="form-horizontal bv-form eportalForm">
						<div class="field-group">
							<label class="col-xs-3 control-label"><spring:message code="file_name" /></label>
							<div class="col-xs-9 padding0">
								<input class="form-control text required input-sm" name="exportName" v-model="userSetting.exportName" value="table_spec">
							</div>
						</div>
						<div class="field-group">
							<label class="col-xs-3 control-label"><spring:message code="msg.export.spec.table.definition" /></label>
							<div class="col-xs-9">
								<label class="checkbox-inline"> 
									<input type="checkbox" name="addTableDefinitionFlag" v-model="userSetting.addTableDefinitionFlag" value="true"><spring:message code="addflag" /> 
								</label>
							</div>
						</div>
						<div class="field-group">
							<label class="col-xs-3 control-label"><spring:message code="msg.export.spec.column.multi.sheet" /></label>
							<div class="col-xs-9">
								<label class="radio-inline"> 
									<input type="radio" name="sheetFlag" v-model="userSetting.sheetFlag" value="false" checked><spring:message code="single" /> 
								</label>
								<label class="radio-inline"> 
									<input type="radio" name="sheetFlag" v-model="userSetting.sheetFlag" value="true"> <spring:message code="multiple" />
								</label>
							</div>
						</div>
					</form>
				</div>
			</div>
			
			<div class="process-step" :class="step==2?'active':''">
				<div class="col-xs-12">
					<div class="process-title"><spring:message code="msg.export.spec.step2" /></div>
					
					<c:if test="${schemaInfo ne ''}">
						<div style="padding: 5px 0px 0px;">
							<label class="control-label" style="font-weight: bold;margin-right:5px;"><spring:message code="label.schema" /> : </label>
							<select v-model="selectSchema" @change="getTableList()" style="width: calc(100% - 55px);">
								<c:forEach var="item" items="${schemaList}" begin="0" varStatus="status">
									<option value="${item}">${item}</option>    
								</c:forEach>
							</select>
						</div>
					</c:if>
					<div class="process-desc" style="padding: 5px 0px 5px;"><spring:message code="msg.table.dbclick.move" /></div>
				</div>
				<div class="wh100-relative table-select-area" style="float: left;">
					<div class="col-xs-5">
						<div class="top-select mbottom-10 fb tl mRight-20"><spring:message code="label.table" /></div>
						<div>
							<ul id="source" class="pub-select-source pub-multiselect-area" style="height: 200px;width: 100%;">
							</ul>
						</div>
					</div>
					<div class="col-xs-2 text-center">
						<div style="position:relative;top:100px;">
							<a href="javascript:;" @click="selectTableObj.sourceMove()"><span class="fa fa-forward"></span></a>
							<br/>
							<a href="javascript:;" @click="selectTableObj.targetMove()"><span class="fa fa-backward"></span></a>
						</div>
					</div>
					<div class="col-xs-5">
						<div class="top-select mbottom-10 fb tl mRight-20"><spring:message code="msg.select.value" /></div>
						<div>
							<ul id="target" class="pub-select-target pub-multiselect-area" style="height: 200px;width: 100%;">
							
							</ul>
						</div>
						<div class="pull-right">
							<a href="javascript:;" class="fa fa-arrow-up"  @click="selectTableObj.move('up')"><spring:message code="up" /></a>
							<span style="padding-left:10px;"></span>
							<a href="javascript:;" class="fa fa-arrow-down"  @click="selectTableObj.move('down')"><spring:message code="down" /></a>
							<span style="padding-right:10px;"></span>
						</div>
					</div>
				</div>
			</div>
				
			<div class="process-step" :class="step==3?'active':''">
				<div class="col-xs-12">
					<div class="process-title"><spring:message code="msg.export.spec.column.title" /></div>
					<div class="process-desc"><spring:message code="msg.column.dbclick.move" /></div>
				</div>
				<div style="height:245px;;border:1px solid #ddd;margin:3px;overflow:hidden;">
					<div class="col-xs-3">
						<div class="top-select mbottom-10 fb tl mRight-20"><spring:message code="column" /></div>
						<div>
							<ul id="columnSource" class="pub-select-source pub-multiselect-area" style="height: 200px;width: 100%;">
							</ul>
						</div>
					</div>
					<div class="col-xs-1 text-center">
						<div style="position:relative;top:100px;">
							<a href="javascript:;" @click="selectColumnObj.sourceMove()"><span class="fa fa-forward"></span></a>
							<br/>
							<a href="javascript:;" @click="selectColumnObj.targetMove()"><span class="fa fa-backward"></span></a>
						</div>
					</div>
					<div class="col-xs-3">
						<div class="top-select mbottom-10 fb tl mRight-20"><spring:message code="msg.select.value" /></div>
						<div>
							<ul id="columnTarget" class="pub-select-target pub-multiselect-area" style="height: 200px;width: 100%;">
							
							</ul>
						</div>
						<div class="pull-right">
							<a href="javascript:;" class="fa fa-arrow-up" @click="selectColumnObj.move('up')"><spring:message code="up" /></a>
							<span style="padding-left:10px;"></span>
							<a href="javascript:;" class="fa fa-arrow-down" @click="selectColumnObj.move('down')"><spring:message code="down" /></a>
							<span style="padding-right:10px;"></span>
						</div>
					</div>
					<div class="col-xs-5">
						<form id="mappingForm" name="mappingForm" class="form-horizontal bv-form eportalForm" onsubmit="return false;">
							<div class="col-xs-12" style="padding:0px 0px 5px 0px;">
								<spring:message code="msg.export.spec.column.detail" />
							</div>
							<div class="field-group">
								<label class="col-xs-3 control-label padding0"><spring:message code="column" /></label>
								<div class="col-xs-9">
									<input class="form-control text required input-sm" v-model="detailItem.code" disabled="disabled">
								</div>
							</div>
							<div class="field-group">
								<label class="col-xs-3 control-label padding0"><spring:message code="column_name" /></label>
								<div class="col-xs-9">
									<input class="form-control text required input-sm" v-model="detailItem.title" >
								</div>
							</div>
							<div class="field-group">
								<label class="col-xs-3 control-label padding0"><spring:message code="size" /></label>
								<div class="col-xs-9">
									<input class="form-control text required input-sm" v-model="detailItem.width">
								</div>
							</div>
						</form>
					</div>
				</div>
			</div>
			<div class="process-step-btn-area">
				<button type="button" class="btn-md"  :class="step == 1 ? 'disabled' :''" @click="moveStep('prev')"><spring:message code="label.prev" /></button>
				<button type="button" class="btn-md"  :class="step == endStep ? 'disabled' :''" @click="moveStep('next')"><spring:message code="label.next" /></button>
				<button type="button" class="btn-md"  :class="step != endStep ? 'disabled' :''" @click="complete()"><spring:message code="label.complete" /></button>
			</div>
		</div>
	</div>
</BODY>
</html>

<script>
VarsqlAPP.vueServiceBean({
	el: '#specMain'
	,data: {
		step : 1
		,endStep : 3
		,selectSchema : ''
		,selectTableObj : {}
		,selectColumnObj : {}
		,userSetting : VARSQL.util.objectMerge({exportName:'table_spec',sheetFlag:'false', schema:'${schemaInfo}'},${userSettingInfo})
		,detailItem :{}
	}
	,methods:{
		init : function (){
			this.selectSchema = this.userSetting.schema; 
			this.setUserConfigInfo();
		}
		//이전 , 다음
		,moveStep : function (mode){
			if(mode == 'prev'){
				if(this.step > 1){
					this.step -= 1;
				}
			}else if(mode == 'next'){
				if(this.step < this.endStep){
					this.step += 1;
				}
			}
		}
		// step 선택
		,selectStep : function (step){
			this.step = step;
		}
		// 완료
		,complete : function (){
			var _self = this; 
			if(_self.selectTableObj.getTargetItem().length < 1){
				this.step=2;
    			alert('<spring:message code="msg.table.select" />');
    			return ; 
    		}
			
			if(_self.selectColumnObj.getTargetItem().length < 1){
    			alert('<spring:message code="msg.column.select" />');
    			return ; 
    		}
			
			_self.exportInfo();
		}
		,exportInfo : function (){
			var _self = this; 
			
			var info = $("#firstConfigForm").serializeJSON();
			
			var prefVal = {
				exportName : _self.userSetting.exportName
				,schema : _self.selectSchema
				,sheetFlag : _self.userSetting.sheetFlag
				,addTableDefinitionFlag : _self.userSetting.addTableDefinitionFlag
				,tables : _self.selectTableObj.getTargetItem()
				,columns: _self.selectColumnObj.getTargetItem()
			};
			
			var param = {
				prefVal : JSON.stringify(prefVal)
				,schema : _self.selectSchema
				,conuid : '${param.conuid}'
			};
			
			VARSQL.req.download({
				type: 'post'
				,url: {type:VARSQL.uri.database, url:'/tools/export/spec/tableExport.vsql'}
				,params : param
			});
		}
		,setUserConfigInfo : function (){
			var _self = this; 
			
			_self.setTableSelect();
			_self.getTableList();
			_self.setColumnSelect();
			
		}
		,getTableList : function(){
			var _self = this; 
			
			var param = {
				conuid : '${param.conuid}'
				,schema : this.selectSchema
			}
			
			VARSQL.req.ajax({
				url : {type:VARSQL.uri.database, url:'/tools/export/specMain/tableList.vsql'}
				,data: param
				,loadSelector : '.table-select-area'
				,success:function (resData){
					var list = resData.items;
					
					_self.selectTableObj.setItem('source',list);
					
					var beforeTableList = [];
					if(_self.selectSchema == _self.userSetting.schema){
						var beforeTables = _self.userSetting.tables;
						var beforeTablelen = beforeTables.length; 
						if(beforeTablelen > 0){
							var tableNameObj ={};
							for(var i =0; i <beforeTablelen; i++){
								var item = beforeTables[i];
								tableNameObj[item.name] = i; 
							}
							
							for(var i =0,len = list.length; i <len; i++){
								var item = list[i];
								var beforeTableIdx = tableNameObj[item.name]; 
								if(!VARSQL.isUndefined(beforeTableIdx)){
									beforeTableList.push(beforeTables[beforeTableIdx]);
								}
							}
						}
						_self.selectTableObj.setItem('target',beforeTableList);
					}else{
						_self.selectTableObj.setItem('target',[]);
					}
					
					
				}
			});
		}
		// table select info
		,setTableSelect: function (){
			var _self = this; 
			
			var tmpSourceItem = [] , paramSourceItem=[]; 

			_self.selectTableObj= $.pubMultiselect('#source', {
				targetSelector : '#target'
				,addItemClass:'text_selected'
				,useMultiSelect : true
				,duplicateCheck : true
				,useDragMove :false
				,sourceItem : {
					optVal : 'name'
					,optTxt : 'name'
					,emptyMessage : '<spring:message code="msg.export.spec.schema.select" />'
					,items : paramSourceItem
				}
				,targetItem : {
					optVal : 'name'
					,optTxt : 'name'
					,items : []
					,click: function (e, sItem){
						//console.log(e,sEle);
					}
				}
			}); 
		}
		//컬럼 select
		,setColumnSelect: function (){
			var _self = this; 
			
			var tmpSourceItem = [] , paramSourceItem=${varsqlfn:objectToJson(columnInfo)}; 
			
			var userSettingColumn = _self.userSetting.columns;
			userSettingColumn = $.isArray(userSettingColumn) &&  userSettingColumn.length > 0 ? userSettingColumn : paramSourceItem;

			_self.selectColumnObj= $.pubMultiselect('#columnSource', {
				targetSelector : '#columnTarget'
				,addItemClass:'text_selected'
				,useMultiSelect : true
				,duplicateCheck : true
				,useDragMove :false
				,sourceItem : {
					optVal : 'code'
					,optTxt : 'code'
					,items : paramSourceItem
				}
				,targetItem : {
					optVal : 'code'
					,optTxt : 'code'
					,items : userSettingColumn
					,click: function (e, sItem){
						sItem.width = (sItem.width ||sItem.custom.width);
						_self.detailItem = sItem;
					}
				}
			});
		}
	}
});
</script>



