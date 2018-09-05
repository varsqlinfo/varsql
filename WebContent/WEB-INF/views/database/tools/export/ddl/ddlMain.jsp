<!DOCTYPE html>
<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/include/tagLib.jspf"%>
<html>
<head>
<title><spring:message code="database.tools" /></title>
<%@ include file="/WEB-INF/include/head-preferences.jsp"%>

</head>

<BODY class="preferences-body">
	<div id="ddlMain" class="preferences">
		<div class="left-nav col-xs-3 padding0">
			<ul>
				<li :class="step==1?'active':''" @click="selectStep(1)">
					<a href="javascript:;"><spring:message code="msg.export.ddl.step1" /></a>
				</li>
				<li :class="step==2?'active':''" @click="selectStep(2)">
					<a href="javascript:;"><spring:message code="msg.export.ddl.step2" /></a>
				</li>
				<li :class="step==3?'active':''" @click="selectStep(3)">
					<a href="javascript:;"><spring:message code="msg.export.ddl.step3" /></a>
				</li>
			</ul>
		</div>
		
		<div class="process-step-area col-xs-9 padding0 scroll-y">
			<div class="process-step" :class="step==1?'active':''">
				<div class="col-xs-12">
					<label class="control-label"><spring:message code="msg.export.ddl.info" /></label>
				</div>
				<div class="col-xs-12">
					<form id="firstConfigForm" name="firstConfigForm" class="form-horizontal bv-form eportalForm">
						<div class="form-group">
							<label class="col-xs-3 control-label"><spring:message code="file_name" /></label>
							<div class="col-xs-9 padding0">
								<input class="form-control text required input-sm" id="export_name" name="export_name" value="table_spec" >
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-3 control-label"><spring:message code="msg.export.multi.file" /></label>
							<div class="col-xs-9">
								<label class="radio-inline"> 
									<input type="radio" name="sheet_flag" value="false" checked><spring:message code="msg.export.multi.file.single" /> 
								</label>
								<label class="radio-inline"> 
									<input type="radio" name="sheet_flag" value="true"> <spring:message code="msg.export.multi.file.multi" />
								</label>
							</div>
						</div>
					</form>
				</div>
			</div>
			
			<div class="process-step" :class="step==2?'active':''">
				<div class="col-xs-12">
					<label class="control-label"><spring:message code="msg.export.ddl.object.select" /></label>
				</div>
				<div class="col-xs-12">
					<ul>
						<template v-for="(objInfo,index) in exportObject">
						   <li>
						   		<a href="javascript:;" :title="objInfo.name" @click="selectItem(objInfo)"><input type="checkbox">{{ objInfo.name }}</a>
						   	</li> 
						</template>
					</ul>
				</div>
			</div>
				
			<div class="process-step" :class="step==3?'active':''">
				<div class="col-xs-12">
					<label class="control-label"><spring:message code="msg.table.dbclick.move" /></label>
				</div>
				<div class="col-xs-3">
					<div class="top-select mbottom-10 fb tl mRight-20"><spring:message code="msg.select.object" /></div>
					<ul class="ddl-object-select-area">
						<template v-for="(objInfo,key) in selectObjectItems">
						   <li class="ddl-object-info">
						   		<a href="javascript:;" :title="objInfo.name">{{ objInfo.name }}</a>
						   	</li> 
						</template>
						<li v-if="Object.keys(selectObjectItems).length == 0">
							<p class="no-data"><spring:message code="msg.export.ddl.object.nodata" /></p>
						</li>
					</ul>
				</div>
				
				<div class="col-xs-4 padding0">
					<div class="top-select mbottom-10 fb tl mRight-20"><spring:message code="label.table" /></div>
					<div>
						<ul id="source" class="pub-select-source pub-multiselect-area" style="height: 200px;width: 100%;">
						</ul>
					</div>
				</div>
				<div class="col-xs-1 text-center padding0">
					<div style="position:relative;top:100px;">
						<a href="javascript:;" @click="selectTableObj.sourceMove()"><span class="glyphicon glyphicon-forward"></span></a>
						<br/>
						<a href="javascript:;" @click="selectTableObj.targetMove()"><span class="glyphicon glyphicon-backward"></span></a>
					</div>
				</div>
				<div class="col-xs-4 padding0">
					<div class="top-select mbottom-10 fb tl mRight-20"><spring:message code="msg.select.value" /></div>
					<div>
						<ul id="target" class="pub-select-target pub-multiselect-area" style="height: 200px;width: 100%;">
						
						</ul>
					</div>
					<div class="pull-right">
						<a href="javascript:;" @click="selectTableObj.move('up')"><spring:message code="up" /></a>
						<span style="padding-left:10px;"></span>
						<a href="javascript:;" @click="selectTableObj.move('down')"><spring:message code="down" /></a>
						<span style="padding-right:10px;"></span>
					</div>
				</div>
			</div>
			<div class="process-step-btn-area">
				<button type="button" class="btn btn-default btn-sm" :class="step == 1 ? 'disabled' :''" @click="moveStep('prev')"><spring:message code="label.prev" /></button>
				<button type="button" class="btn btn-default btn-sm" :class="step == endStep ? 'disabled' :''" @click="moveStep('next')"><spring:message code="label.next" /></button>
				<button type="button" class="btn btn-default btn-sm" :class="step != endStep ? 'disabled' :''" @click="complete()"><spring:message code="label.complete" /></button>
			</div>
		</div>
	</div>
</BODY>
</html>
<script>

VarsqlAPP.vueServiceBean({
	el: '#ddlMain'
	,data: {
		step : 1
		,endStep : 3
		,selectTableObj : []
		,selectColumnObj : []
		,selectObjectItems :{} 
		,detailItem :{}
		,exportObject : ${varsqlfn:objectToJson( exportServiceMenu)}
	}
	,methods:{
		init : function (){
			
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
			
			//_self.exportInfo();
		}
		// object 선택.
		,selectItem : function (objInfo){
			objInfo._isSelect = objInfo._isSelect ? false :true; 
			
			if(objInfo._isSelect){
				this.selectObjectItems[objInfo.contentid] =objInfo
			}else{
				delete this.selectObjectItems[objInfo.contentid];
			}
		}
		,exportInfo : function (){
			var _self = this; 
			
			var info = $("#firstConfigForm").serializeJSON();
			
			var prefVal = {
				exportName : _self.export_name
				,sheetFlag : _self.sheet_flag
				,tables : _self.selectTableObj.getTargetItem()
				,columns: _self.selectColumnObj.getTargetItem()
			};
			
			var param = {
				prefVal : JSON.stringify(prefVal)
				,conuid : '${param.conuid}'
			};
			
			VARSQL.req.download({
				type: 'post'
				,url: {gubun:VARSQL.uri.database, url:'/tools/export/spec/tableExport.vsql'}
				,params : param
			});
		}
		// table select info
		,setTableSelect: function (){
			var _self = this; 
			
			var tmpSourceItem = [] , paramSourceItem=${varsqlfn:objectToJson(tableInfo)}; 

			_self.selectTableObj= $.pubMultiselect('#source', {
				targetSelector : '#target'
				,addItemClass:'text_selected'
				,useMultiSelect : true
				,duplicateCheck : true
				,useDragMove :false
				,sourceItem : {
					optVal : 'name'
					,optTxt : 'name'
					,items : paramSourceItem
				}
				,targetItem : {
					optVal : 'name'
					,optTxt : 'name'
					,items : (_self.userSetting.tables ||[])
					,click: function (e, sItem){
						//console.log(e,sEle);
					}
				}
			}); 
		}
	}
});

</script>


