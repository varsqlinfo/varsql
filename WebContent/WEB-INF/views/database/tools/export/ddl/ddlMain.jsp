<!DOCTYPE html>
<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/include/tagLib.jspf"%>
<html>
<head>
<title><spring:message code="database.tools" /></title>
<%@ include file="/WEB-INF/include/database-dialog-head.jsp"%>

</head>

<BODY class="preferences">
	<div id="ddlMain" class="wh100">
		<div class="preferences-nav col-xs-3">
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
		
		<div class="preferences-body process-step-area col-xs-9 scroll-y">
			<div class="process-step" :class="step==1?'active':''">
				<div class="col-xs-12">
					<div class="process-title"><spring:message code="msg.export.ddl.info" /></div>
				</div>
				<div class="col-xs-12">
					<form id="firstConfigForm" name="firstConfigForm" class="form-horizontal bv-form eportalForm">
						<div class="field-group">
							<label class="col-xs-3"><spring:message code="file_name" /></label>
							<div class="col-xs-9 padding0">
								<input class="form-control text required input-sm" name="export_name" v-model="downloadConfig.exportName">
							</div>
						</div>
					</form>
				</div>
			</div>
			
			<div class="process-step" :class="step==2?'active':''">
				<div class="col-xs-12">
					<div class="process-title"><spring:message code="msg.export.ddl.step2" /></div>
					<div class="process-desc"><spring:message code="msg.export.ddl.object.desc" /></div>
				</div>
				<div class="col-xs-12">
					<ul>
						<template v-for="(objInfo,index) in exportObject">
						   <li>
						   		<input type="checkbox" :id="'check'+objInfo.contentid" @click="selectItem(objInfo)">
								<label :for="'check'+objInfo.contentid" :title="objInfo.name">{{ objInfo.name }}</label>
						   	</li> 
						</template>
					</ul>
				</div>
			</div>
				
			<div class="process-step" :class="step==3?'active':''">
				<div class="col-xs-12">
					<div class="process-title"><spring:message code="msg.export.ddl.object.select.title" /></div>
					<div class="process-desc"><spring:message code="msg.table.dbclick.move" /></div>
				</div>
				<div class="col-xs-3">
					<div class="top-select mbottom-10 fb tl mRight-20"><spring:message code="msg.select.object" /></div>
					<ul class="ddl-object-select-area">
						<template v-for="(objInfo,key) in selectObjectItems">
						   <li class="ddl-object-info" :class="objInfo.isActive===true?'active' :''" @click="setSelectObject(objInfo)">
						   		<a href="javascript:;" :title="objInfo.name">{{ objInfo.name }}</a>
						   	</li> 
						</template>
						<li v-if="Object.keys(selectObjectItems).length == 0">
							<p class="no-data"><spring:message code="msg.export.ddl.object.nodata" /></p>
						</li>
					</ul>
				</div>
				
				<div class="col-xs-4 padding0">
					<div class="top-select mbottom-10 fb tl mRight-20"><spring:message code="object" /></div>
					<div>
						<ul id="source" class="pub-select-source pub-multiselect-area" style="height: 200px;width: 100%;">
						</ul>
					</div>
				</div>
				<div class="col-xs-1 text-center padding0">
					<div style="position:relative;top:100px;">
						<a href="javascript:;" @click="selectDbObjectInfo.sourceMove()"><span class="fa fa-forward"></span></a>
						<br/>
						<a href="javascript:;" @click="selectDbObjectInfo.targetMove()"><span class="fa fa-backward"></span></a>
					</div>
				</div>
				<div class="col-xs-4 padding0">
					<div class="top-select mbottom-10 fb tl mRight-20"><spring:message code="msg.select.value" /></div>
					<div>
						<ul id="target" class="pub-select-target pub-multiselect-area" style="height: 200px;width: 100%;">
						
						</ul>
					</div>
					<div class="pull-right">
						<a href="javascript:;" class="fa fa-arrow-up"  @click="selectDbObjectInfo.move('up')"><spring:message code="up" /></a>
						<span style="padding-left:10px;"></span>
						<a href="javascript:;" class="fa fa-arrow-down"  @click="selectDbObjectInfo.move('down')"><spring:message code="down" /></a>
						<span style="padding-right:10px;"></span>
					</div>
				</div>
			</div>
			<div class="process-step-btn-area">
				<button type="button" class="btn-md" :class="step == 1 ? 'disabled' :''" @click="moveStep('prev')"><spring:message code="label.prev" /></button>
				<button type="button" class="btn-md" :class="step == endStep ? 'disabled' :''" @click="moveStep('next')"><spring:message code="label.next" /></button>
				<button type="button" class="btn-md" :class="step != endStep ? 'disabled' :''" @click="complete()"><spring:message code="label.complete" /></button>
			</div>
		</div>
	</div>
</BODY>
</html>
<style>
/*todo css파일로 옮길것.*/
.ddl-object-info.active{
	background-color: #e6e5e5;
}
</style>

<script>

VarsqlAPP.vueServiceBean({
	el: '#ddlMain'
	,data: {
		step : 1
		,endStep : 3
		,downloadConfig :{
			exportName : 'ddlInfo'
		}
		,selectExportObject : ''
		,selectDbObjectInfo : []
		,selectObjectItems :{}
		,objExportInfo : {}
		,selectExportInfo : {}
		,detailItem :{}
		,exportObject : []
	}
	,mounted : function (){
		var exportInfoArr = ${varsqlfn:objectToJson( exportServiceMenu)};
		
		for(var i =0, len = exportInfoArr.length; i<len; i++){
			var item = exportInfoArr[i];
			
			item._isSelect = false; 
			item.isActive = false; 
		}
		
		this.exportObject = exportInfoArr;
	}
	,methods:{
		init : function (){
			
		}
		//이전 , 다음
		,moveStep : function (mode){
			if(mode == 'prev'){
				if(this.step > 1){
					this.selectStep(this.step-1);
				}
			}else if(mode == 'next'){
				if(this.step < this.endStep){
					this.selectStep(this.step+1);
				}
			}
		}
		// step 선택
		,selectStep : function (step){
			var _self = this; 
			
			this.step = step;
			
			if(this.step != 3){
				return ; 
			}
			
			var objItem = _self.selectObjectItems;
			
			var dbObjType = [];
			for(var key in objItem){
				
				if(typeof _self.objExportInfo[key] ==='undefined'){
					dbObjType.push(key);
				} 
			}
			
			if(dbObjType.length < 1){
				var item = _self.selectObjectItems[Object.keys(_self.selectObjectItems)[0]];
				
				if(!VARSQL.isUndefined(item)){
					_self.setSelectObject(item);	
				}
				
				return ; 
			}
				
			var param = {
				conuid : '${param.conuid}'
				,ddlObjInfo : dbObjType.join(',')
			}
			
			VARSQL.req.ajax({
				url : {type:VARSQL.uri.database, url:'/tools/export/ddl/objInfo.vsql'}
				,data: param
				,loadSelector : '.preferences-body'
				,success:function (resData){
					var customItem = resData.customs;
					
					var firstFlag = true; 
					
					var selectKeyInfo; 
					
					for(var key in objItem){
						
						if(VARSQL.isUndefined(customItem[key])){
							continue ; 
						}
						
						_self.objExportInfo[key] = $.isArray(customItem[key])?customItem[key] : [];
						if(firstFlag){
							selectKeyInfo =key;
						}
						
						firstFlag = false; 
					}
					
					_self.setSelectObject(_self.selectObjectItems[selectKeyInfo]);
				}
			});
		}
		// 완료
		,complete : function (){
			var _self = this; 
			
			_self.selectExportInfo[_self.selectExportObject.contentid]  = _self.selectDbObjectInfo.getTargetItem();
			
			var exportInfo = {};
			for(var key in _self.selectObjectItems){
				exportInfo[key] = _self.selectExportInfo[key];
			}
			
			var prefVal = {
				"exportName" : _self.downloadConfig.exportName
				,"exportInfo" : exportInfo
			};
			
			var param = {
				prefVal : JSON.stringify(prefVal)
				,conuid : '${param.conuid}'
			};
			
			VARSQL.req.download({
				type: 'post'
				,url: {type:VARSQL.uri.database, url:'/tools/export/ddl/export.vsql'}
				,params : param
			});
			
			//_self.exportInfo();
		}
		// object 선택.
		,selectItem : function (objInfo){
			objInfo._isSelect = objInfo._isSelect ? false :true; 
			
			objInfo.isActive = false; 
			
			if(objInfo._isSelect){
				this.selectObjectItems[objInfo.contentid] =objInfo
			}else{
				delete this.selectObjectItems[objInfo.contentid];
			}
		}
		//object list
		,setSelectObject : function (sObj){
			
			var _self =this; 
			
			sObj.isActive = true; 
			
			var objName = sObj.contentid; 
			
			if(_self.selectExportObject != ''){
				_self.selectExportObject.isActive = false; 
				_self.selectExportInfo[_self.selectExportObject.contentid] = _self.selectDbObjectInfo.getTargetItem();
			}
			
			_self.selectExportObject = sObj;
			
			var sObjInfo = _self.objExportInfo[objName];
			
			var targetInfo = _self.selectExportInfo[objName] || sObjInfo;
			
			var tmpSourceItem = [] , paramSourceItem=sObjInfo; 

			_self.selectDbObjectInfo= $.pubMultiselect('#source', {
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
					,items : targetInfo
				}
				,pageInfo : {
					emptyMessage : 'no_data'
				}
			}); 
		}
	}
});

</script>


