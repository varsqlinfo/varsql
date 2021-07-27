<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/include/tagLib.jspf"%>

<div id="<varsql:namespace/>" class="menu-popup-layer wh100">

	<div class="process-body wh100" style="position:relative;">
		<div class="process-step" :class="step==1?'active':''">
			<div class="col-xs-12">
				<div class="field-group">
					<h2 class="process-header">가져오기 유형 선택</h2>
					<div class="process-header-desc">가져오기할 작업의 유형을 아래에서 선택해주세요.</div>
					<ul class="process-type-select">
						<li>
							<label class="checkbox-container">SQL
							  <input type="radio" v-model="importType" value="1" checked="checked">
							  <span class="radiomark"></span>
							</label>
							<div class="checkbox-container-desc"> SQL 파일을 실행해서 입력합니다</div>
						</li>
						<li>
							<label class="checkbox-container">XML,JSON
							  <input type="radio" v-model="importType" value="2">
							  <span class="radiomark"></span>
							</label>
							<div class="checkbox-container-desc"> XML , JSON 파일을 이용하여 데이터를  입력합니다</div>
						</li>
						<li>
							<label class="checkbox-container">CSV,XLS
							  <input type="radio" v-model="importType" value="3">
							  <span class="radiomark"></span>
							</label>
							<div class="checkbox-container-desc"> XML , JSON 파일을 이용하여 데이터를  입력합니다</div>
						</li>
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
							업로드된 파일
							<button @click="selectImport()">Import</button>
						</div>
					</div>
					<div class="row import-file-area">
						<div class="col-xs-6 import-file-list">
							<template v-for="(item,index) in fileList">
								<ul class="import-file-item">
									<li class="text-ellipsis float-left" style="width:calc(100% - 148px);" :title="item.fileName">
										<input type="checkbox"	:id="item.fileId" v-model="item.isCheck"> <label :for="item.fileId">{{item.fileName}}</label>
									</li>
									<li class="float-left text-right" style="width:75px;">
										{{item.displaySize}}
									</li>
									<li class="float-left text-right" style="width:70px;">
										<button class="btn btn-default" @click="fileImport(item)">Import</button>
									</li>
								</ul>
							</template>
						</div>
						<div class="col-xs-6 import-file-result">
							<template v-for="(item,index) in importResult">
								<div class="file-import-result-msg">
									<div :class="(item.resultCode == 200 ? 'success' :'error')">
										<span> {{item.fileName}}</span> <span>count : {{item.resultCount}}</span>
									</div>
					    			<div v-if="item.resultCode > 200" class="error">{{item.message}}</div>
				    			</div>
			    			</template>
						</div>
					</div>
				</div>
			</div>
		</div>

		<div class="process-step" :class="step==3?'active':''">
			<div class="col-xs-12">
				Import Result
				<template v-if="importType=='1'">
					<template v-for="(item,index) in importResult">
						<div class="list-group-item row">
							<span>{{item.resultCode == 200 ? 'success' :'fail'}}</span> <span> {{item.fileName}}</span> <span>count : {{item.resultCount}}</span>
		    			</div>
		    			<div v-if="item.resultCode > 200" class="error">{{item.message}}</div>
	    			</template>
	    			<div class="text-center" v-if="importResult.length === 0"><spring:message code="msg.nodata"/></div>
				</template>
				<template v-else-if="importType=='2'">
					222
				</template>
				<template v-else-if="importType=='3'">
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
VarsqlAPP.vueServiceBean({
	el: '#<varsql:namespace/>'
	,data: {
		step : 1
		,importType : '1'
		,fileExtensions : 'sql'
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
	}
	,mounted : function (){
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
			if(newVal=='1'){
				this.fileExtensions ='sql';

				this.buttons={
					2:'Import'
				};
			}else if(newVal=='2'){
				this.fileExtensions ='xml,json';

				this.buttons = {
					2:'Import'
				};
			}else if(newVal=='3'){
				this.fileExtensions ='csv,xls';
			}
			this.fileUploadOpt.params['fileExtensions'] =this.fileExtensions;
		}
	}
	,methods:{
		// menu 선택
		moveStep : function (step){
			var _self = this;

			if(this.step == 2){
				this.sqlFileImport();
			}
		}
		,sqlFileImport : function (mode, selectFile){

			var importFileIds=[];
			if(mode=='all'){
				for(var i = 0;i < this.fileList.length; i++){
					var fileInfo = this.fileList[i];
					if(fileInfo.isCheck===true){
						importFileIds.push(fileInfo.fileId);
					}
				}
				if(importFileIds.length < 1){
					VARSQLUI.toast.open(VARSQL.messageFormat('varsql.0029'));
					return false;
				}

				if(!confirm('선택한 파일을 import 하시겠습니까?')){
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
			var _this =this;

			this.$ajax({
				url : {type:VARSQL.uri.database, url:'/file/import'}
				,loadSelector : 'body'
				,data : param
				,success: function(resData) {
					_this.importResult = _this.importResult.concat(resData.items); //console.log(resData);
				}
			})
		}
		// 선택된 파일 import
		,selectImport : function (){
			this.sqlFileImport('all');
		}
		// file 단위 import
		,fileImport : function (item){
			this.sqlFileImport('file', item);
		}
		,complete : function (){
			window.close();
		}
	}
});

</script>