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
		,selectStep : function (step){
			this.$refs.stepButton.move(step);
		}
		,moveStep : function (step){
			var _self = this;

			if(step < 3){
				return true;
			}

			var objItem = _self.selectObjectItems;
			
			var dbObjType = [];
			var activeObjItem = {};
			var firstFlag = true; 
			for(var key in objItem){
				
				if(firstFlag){
					activeObjItem = objItem[key];
				}
				
				if(objItem[key].isActive){
					activeObjItem = objItem[key];
				}
				
				if(typeof _self.objExportInfo[key] ==='undefined'){
					dbObjType.push(key);
				}
				
				firstFlag = false; 
			}
			
			if(VARSQL.getLength(_self.selectObjectItems) < 1){
				VARSQLUI.toast.open(VARSQL.messageFormat('varsql.0006'));
				this.selectStep(2);
				return false;
			}
			
			if(dbObjType.length < 1){
				this.setSelectObject(activeObjItem);
				return ;
			}
			
			var param = {
				conuid : '${param.conuid}'
				,ddlObjInfo : dbObjType.join(',')
			}

			VARSQL.req.ajax({
				url : {type:VARSQL.uri.database, url:'/tools/export/ddl/objInfo'}
				,data: param
				,loadSelector : '.menu-tools-body'
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
						}else{
							_self.selectExportInfo[key] = customItem[key];
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
				,url: {type:VARSQL.uri.database, url:'/tools/export/ddl/export'}
				,params : param
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
		,setSelectObject : function (sObj){

			var _self =this;

			var objName = sObj.contentid;
			
			for(var key in _self.selectObjectItems){
				_self.selectObjectItems[key].isActive = false; 
			}
			
			sObj.isActive = true;
			
			if(_self.selectExportObject != ''){
				//_self.selectExportObject.isActive = false;
				_self.selectExportInfo[_self.selectExportObject.contentid] = _self.selectDbObjectInfo.getTargetItem();
			}

			_self.selectExportObject = sObj;

			var sObjInfo = _self.objExportInfo[objName];

			var targetInfo = _self.selectExportInfo[objName] || sObjInfo;

			var tmpSourceItem = [] , paramSourceItem=sObjInfo;

			_self.selectDbObjectInfo= $.pubMultiselect('#source', {
				duplicateCheck : true
				,valueKey : 'name'	
				,labelKey : 'name'
				,source : {
					items : paramSourceItem
				}
				,target : {
					items : targetInfo
				}
				,footer : {
					enable : true
				}
			});
		}
	}
});

</script>


