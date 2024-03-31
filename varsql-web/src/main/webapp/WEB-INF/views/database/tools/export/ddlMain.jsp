<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/include/tagLib.jspf"%>

<div id="ddlMain" class="wh100">
	<div class="menu-tools-nav col-xs-3">
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

	<div class="menu-tools-body process-step-area col-xs-9 scroll-y">
		<div class="process-step" :class="step==1?'active':''">
			<div class="col-xs-12">
				<div class="process-title"><spring:message code="export.info" /></div>
			</div>
			<div class="col-xs-12">
				<form id="firstConfigForm" name="firstConfigForm" class="form-horizontal bv-form eportalForm">
					<div class="field-group">
						<label class="col-xs-3"><spring:message code="file.name" /></label>
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
				
				<c:if test="${schemaInfo ne ''}">
					<div style="padding: 5px 0px 0px;">
						<label class="control-label" style="font-weight: bold;margin-right:5px;"><spring:message code="db.schema" /> : </label>
						<select v-model="selectSchema" @change="loadObjectInfo()" style="width: calc(100% - 80px);">
							<c:forEach var="item" items="${schemaList}" begin="0" varStatus="status">
								<option value="${item}">${item}</option>
							</c:forEach>
						</select>
					</div>
				</c:if>
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
					<li v-if="VARSQL.getLength(selectObjectItems) < 1">
						<p class="no-data"><spring:message code="msg.export.ddl.object.nodata" /></p>
					</li>
				</ul>
			</div>

			<div class="col-xs-9 padding0">
				<div id="source" style="height: 250px;"></div>
			</div>
		</div>
		
		<step-button :step.sync="step" :end-step="endStep" ref="stepButton"></step-button>
		
	</div>
</div>

<script>

VarsqlAPP.vueServiceBean({
	el: '#ddlMain'
	,data: {
		step : 1
		,endStep : 3
		,downloadConfig :{
			exportName : 'ddlInfo'
		}
		,selectSchema : '${currentSchemaName}'
		,objectLoadSchema : ''
		,selectExportObject : ''
		,selectDbObjectInfo : null
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
		,selectStep : function (step){
			this.$refs.stepButton.move(step);
		}
		,moveStep : function (step){
			var _self = this;
			
			if(step < 3){
				return true;
			}
			
			if(VARSQL.getLength(_self.selectObjectItems) < 1){
				VARSQL.toastMessage('msg.item.select');
				this.selectStep(2);
				return false;
			}
			
			this.loadObjectInfo();
			
		}
		,loadObjectInfo: function (){
			var _self = this;
			
			var selectObjectItems = _self.selectObjectItems;
			
			if(_self.objectLoadSchema != _self.selectSchema){
				_self.objExportInfo= {};
				_self.selectExportInfo = {};
			}
			
			var dbObjType = [];
			var activeObjItem = {};
			var firstFlag = true; 
			for(var key in selectObjectItems){
				
				if(firstFlag){
					activeObjItem = selectObjectItems[key];
				}
				
				if(selectObjectItems[key].isActive){
					activeObjItem = selectObjectItems[key];
				}
				
				if(_self.objectLoadSchema != _self.selectSchema || typeof _self.objExportInfo[key] ==='undefined'){
					dbObjType.push(key);
				}
				
				firstFlag = false; 
			}
			
			if(dbObjType.length < 1){
				this.setSelectObject(activeObjItem);
				return ;
			}
			
			var param = {
				conuid : '${param.conuid}'
				,ddlObjInfo : dbObjType.join(',')
				,schema : this.selectSchema
			}

			VARSQL.req.ajax({
				url : {type:VARSQL.uri.database, url:'/tools/export/ddl/objInfo'}
				,data: param
				,loadSelector : '.menu-tools-body'
				,success:function (resData){
					
					var customItem = resData.customMap;

					var firstFlag = true;

					var viewObjectKey;

					for(var key in selectObjectItems){

						if(VARSQL.isUndefined(customItem[key])){
							continue ;
						}
						
						if(firstFlag){
							viewObjectKey =key;
						}

						_self.objExportInfo[key] = $.isArray(customItem[key])?customItem[key] : [];
						_self.selectExportInfo[key] = _self.objExportInfo[key];

						firstFlag = false;
					}
					
					_self.objectLoadSchema = param.schema;
					_self.setSelectObject(_self.selectObjectItems[viewObjectKey], true);
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
				,schema : this.selectSchema
			};

			VARSQL.req.download({
				type: 'post'
				,url: {type:VARSQL.uri.database, url:'/tools/export/ddl/export'}
				,mode :true
				,params : param
				,mode : 3
			});

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
		//object list
		,setSelectObject : function (sObj, initFlag){

			var _self =this;

			var objName = sObj.contentid;
			
			for(var key in _self.selectObjectItems){
				_self.selectObjectItems[key].isActive = false; 
			}
			
			sObj.isActive = true;
			
			if(initFlag !== true && _self.selectExportObject != ''){
				//_self.selectExportObject.isActive = false;
				_self.selectExportInfo[_self.selectExportObject.contentid] = _self.selectDbObjectInfo.getTargetItem();
			}

			_self.selectExportObject = sObj;

			var sourceItems = _self.objExportInfo[objName];
			var targetItems = _self.selectExportInfo[objName] || sObjInfo;
			
			if(_self.selectDbObjectInfo != null){
				_self.selectDbObjectInfo.setSourceItem(sourceItems);
    			_self.selectDbObjectInfo.setTargetItem(targetItems);
			}
			
			_self.selectDbObjectInfo= $.pubMultiselect('#source', {
				duplicateCheck : true
				,header : {
					enableSourceLabel : true 	// source header label 보일지 여부
					,enableTargetLabel : true 	// target header label 보일지 여부
				}
				,valueKey : 'name'	
				,labelKey : 'name'
				,source : {
					items : sourceItems
					,search :{
						enable : true
					}
				}
				,target : {
					items : targetItems
					,search :{
						enable : true
					}
				}
				,footer : {
					enable : true
				}
			});
		}
	}
});

</script>
