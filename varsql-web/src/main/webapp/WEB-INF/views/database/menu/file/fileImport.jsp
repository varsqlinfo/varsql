<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/include/tagLib.jspf"%>

<div id="<varsql:namespace/>" class="menu-popup-layer wh100">

	<div class="process-body wh100" style="position:relative;">
		<div class="process-step" :class="step==1?'active':''">
			<div class="col-xs-12">
				<div class="field-group">
					<h2 class="process-header"><spring:message code="select.format" text="형식 선택"/></h2>
					<div class="process-header-desc"><spring:message code="msg.file.import.001" text="가져오기할 작업의 형식을 아래에서 선택해주세요."/></div>
					<ul class="process-type-select">
						<li>
							<label class="checkbox-container">JSON
							  <input type="radio" v-model="importType" value="json">
							  <span class="radiomark"></span>
							</label>
							<div class="checkbox-container-desc"><spring:message code="msg.file.import.003" text="JSON 파일을 이용하여 데이터를  입력합니다"/></div>
						</li>
						<li>
							<label class="checkbox-container">XML
							  <input type="radio" v-model="importType" value="xml">
							  <span class="radiomark"></span>
							</label>
							<div class="checkbox-container-desc"><spring:message code="msg.file.import.003" text="XML 파일을 이용하여 데이터를  입력합니다"/></div>
						</li>
						<li>
							<label class="checkbox-container">SQL
							  <input type="radio" v-model="importType" value="sql" checked="checked">
							  <span class="radiomark"></span>
							</label>
							<div class="checkbox-container-desc"><spring:message code="msg.file.import.002" text="SQL 파일을 실행해서 입력합니다"/></div>
						</li>						
						<!--  li>
							<label class="checkbox-container">CSV
							  <input type="radio" v-model="importType" value="csv">
							  <span class="radiomark"></span>
							</label>
							<div class="checkbox-container-desc"> CSV 파일을 이용하여 데이터를  입력합니다</div>
						</li-->
					</ul>
				</div>
			</div>
		</div>

		<div class="process-step" :class="step==2?'active':''">
			<div class="col-xs-12">
				<file-upload id="fileUploadCp" :options="fileUploadOpt" :file-list.sync="fileList" :accept.sync="fileExtensions" :callback="callback"></file-upload>

				<div style="padding-top:10px;">
					<div>
						<div>
							<spring:message code="uploaded.file" text="업로드된 파일"/>
							<button @click="selectImport()"><spring:message code="import" text="가져오기"/></button>
						</div>
					</div>
					<div class="row import-file-area">
						<div class="col-xs-6 import-file-list">
							<template v-for="(item,index) in fileList">
								<ul class="import-file-item" v-if="item.fileExt==importType">
									<li class="text-ellipsis float-left" style="width:calc(100% - 148px);" :title="item.fileName">
										<input type="checkbox"	:id="item.fileId" v-model="item.isCheck"> <label :for="item.fileId">{{item.fileName}}</label>
									</li>
									<li class="float-left text-right" style="width:75px;">
										{{item.displaySize}}
									</li>
									<li class="float-left text-right" style="width:70px;">
										<button class="btn btn-default" @click="fileItemImport(item)">Import</button>
									</li>
								</ul>
							</template>
						</div>
						<div id="importFileResult" class="col-xs-6 import-file-result">
							<div v-for="(item, index) in importResult" v-bind:key="item._uid">
								<div class="file-import-result-msg user-select-on">
									<div :class="(item.resultCode == 200 ? 'success' :'error')">
										<span> {{item.fileName}}</span> <span v-if="item.resultCode == 200">count : {{item.resultCount}}</span>
									</div>
					    			<div v-if="item.resultCode > 200" class="error">{{item.message}}</div>
				    			</div>
			    			</div>
						</div>
					</div>
				</div>
			</div>
		</div>

		<div class="process-step" :class="step==3?'active':''">
			<div class="col-xs-12">
				<template v-else-if="importType=='csv'">
					2333
				</template>
			</div>
		</div>

		<step-button :step.sync="step" :end-step.sync="endStep" :buttons.sync="buttons" ref="stepButton"></step-button>
	</div>
</div>
<style>
.import-file-area{
	border:1px solid #ddd;
	height: 250px;
}
.import-file-item{
	margin: 7px 0px;
    height: 20px;
}
.import-file-list{
	height: 100%;
	overflow-x : hidden;
	overflow-y : auto;
}
.file-import-result-msg{
	border-bottom:1px solid #ddd;
	padding: 3px;
}
.import-file-result{
	border-left : 1px solid #ddd;
	height: 100%;
	overflow-x : hidden;
	overflow-y : auto;
}
</style>
<script>
VARSQL.loadResource(['fileupload']);
VARSQL.undrop();
VarsqlAPP.vueServiceBean({
	el: '#<varsql:namespace/>'
	,data: {
		step : 1
		,importType : 'json'
		,fileExtensions : ''
		,importExtInfo :{
			sql : {ext : 'sql', endStep : 2}
			,xml : {ext : 'xml', endStep : 2}
			,json : {ext : 'json', endStep : 2}
			,csv : {ext : 'csv', endStep : 3}
		}
		,endStep : 2
		,conuid : '${conuid}'
		,fileUploadOpt :{
			url :VARSQL.url(VARSQL.uri.database, '/file/upload')
			,params :  {
				div :'import'
				,fileExtensions : 'sql'
				,conuid : '${conuid}'
			}
		}
		,buttons:{
		}
		,fileList : []
		,callback : {}
		,importResult :[]
		,logSeq: 0
	}
	,mounted : function (){
		this.setFileTypeInfo(this.importType);

		this.fileUploadOpt.params['conuid'] =this.conuid;
		this.callback = {
			success : function (file, resp){
				//console.log(file, resp);
			}
			,successmultiple : function (file, resp){
				//console.log(file, resp);
			}
		}
	}
	,watch :{
		importType : function (newVal){
			this.setFileTypeInfo(newVal);
		}
	}
	,methods:{
		// menu 선택
		moveStep : function (step){
			/*
			var _self = this;

			if(this.step == 2){
				this.fileImport();
			}
			*/
		}
		// file type 정보 셋팅.
		,setFileTypeInfo : function (fileType){
			var extInfo = this.importExtInfo[fileType];
			this.fileExtensions = extInfo.ext;
			this.endStep = extInfo.endStep;
			this.buttons = VARSQL.util.objectMerge(this.buttons, extInfo.buttons);

			this.fileUploadOpt.params['fileExtensions'] =this.fileExtensions;
		}
		,fileImport : function (mode, selectFile){

			var importFileIds=[];
			if(mode=='all'){
				for(var i = 0;i < this.fileList.length; i++){
					var fileInfo = this.fileList[i];
					if(fileInfo.isCheck===true){
						importFileIds.push(fileInfo.fileId);
					}
				}
				if(importFileIds.length < 1){
					VARSQL.toastMessage('msg.file.uploadandexecute');
					return false;
				}

				if(!VARSQL.confirmMessage('msg.select.file.import.confirm','선택한 파일을 import 하시겠습니까?')){
					return ;
				}

			}else{
				importFileIds.push(selectFile.fileId);
			}
			
			var param = {
				importType : this.importType,
				conuid : this.conuid,
				fileIds : importFileIds.join(',')
			}
			
			param.progressUid = VARSQL.generateUUID();
			
			var _this =this;

			this.$ajax({
				url : {type:VARSQL.uri.database, url:'/file/import'}
				,loadSelector : 'body'
				,data : param
				,success: function(resData) {
					var items = resData.list.reverse().map(function(item){
						item._uid = _this.logSeq++;
						return item; 
					})
					_this.importResult.unshift(...items); //console.log(resData);
					
					document.getElementById('importFileResult').scrollTop =0;
				}
			})
			
			var progressEle = $('body .center-loading-centent'); 
			progressEle.append('<div id="progressItemImport"></div><div id="progressItemContent"></div>');
			
			var progressItemImportEle = $('#progressItemImport');
			var progressItemContentEle = $('#progressItemContent');
			VARSQL.req.progressInfo({
				progressUid : param.progressUid
				,callback : function (resData){
					var item = resData.item; 
					
					if(item == 'fail'){
						progressEle.text('fail');
					}else if(item == 'complete'){
						progressEle.text('complete');
					}else{
						if(item != null){
							
							var resultItems = item.items ||[];
							
							var progressText = VARSQL.util.numberFormat(item.progressContentLength)+'';
							
							progressItemImportEle.text(item.itemIdx+'/'+item.totalItemSize);
							
							if(item.totalContentLength && item.totalContentLength > 0){
								progressText += ' / ' + VARSQL.util.numberFormat(item.totalContentLength);
							}
							
							if(item.name){
								progressText = item.name + '('+progressText + ')';
							}
							
							progressItemContentEle.html(progressText);
						}
					}
				} 
			});
		}
		// 선택된 파일 import
		,selectImport : function (){
			this.fileImport('all');
		}
		// file 단위 import
		,fileItemImport : function (item){
			this.fileImport('file', item);
		}
		,complete : function (){
			window.close();
		}
	}
});

</script>
