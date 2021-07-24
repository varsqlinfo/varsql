<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/include/tagLib.jspf"%>

<div id="<varsql:namespace/>" class="menu-popup-layer wh100">
	
	<div class="process-body col-xs-12 wh100" style="position:relative;">
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
			</div>
		</div>
		
		<div class="process-step" :class="step==3?'active':''">
			<div class="col-xs-12">
				<template v-if="importType=='1'">
				111
				</template>
				<template v-else-if="importType=='2'">
					222
				</template>
				<template v-else-if="importType=='3'">
					2333
				</template>
			</div>
		</div>
		
		<step-button :step.sync="step" :end-step="endStep" ref="stepButton"></step-button>
	</div>
</div>

<script>
VARSQL.loadResource(['fileupload']);
VarsqlAPP.vueServiceBean({
	el: '#<varsql:namespace/>'
	,data: {
		step : 1
		,importType : '1'
		,fileExtensions : 'sql'
		,endStep : 3
		,fileUploadOpt :{
			url :VARSQL.url(VARSQL.uri.database, '/file/import')
			,params :  {
				div :'import'
				,importType : 'sql'
			}
		}
		,fileList : []
		,callback : {}
	}
	,mounted : function (){
		
		this.callback = {
			success : function (file, resp){
				console.log(file, resp);
			}
			,successmultiple : function (file, resp){
				console.log(file, resp);
			}
		}
	}
	,watch :{
		importType : function (newVal){
			if(newVal=='1'){
				this.fileExtensions ='sql';
			}else if(newVal=='2'){
				this.fileExtensions ='xml,json';
			}else if(newVal=='3'){
				this.fileExtensions ='csv,xls';
			}
			this.fileUploadOpt.params['importType'] =this.fileExtensions;
		}
	}
	,methods:{
		// menu 선택
		moveStep : function (step){
			var _self = this; 
			
			if(this.step < 2){
				if(this.importType ==''){
					VARSQLUI.toast.open(VARSQL.messageFormat('varsql.0006'));
					return false;
				}
			}
		}
		,upload : function (){
			var param = {
				div :'fileImport',
				importType : this.importType,
			}
			VARSQL.req.uploadFile("#<varsql:namespace/>fileImportForm",{
				url : {type:VARSQL.uri.database, url:'/file/import'}
				,param : param
				,success: function (resData){
					console.log(resData)
				}
			})
		}
		,complete : function (){
			
		}
	}
});

</script>