<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/include/tagLib.jspf"%>

<div id="<varsql:namespace/>" class="menu-popup-layer wh100">
	
	<div class="process-body col-xs-12 scroll-y" style="position:relative;">
		<div class="process-step" :class="selectMenu==1?'active':''">
			<div class="col-xs-12">
				<div class="field-group">
					<h2 class="process-header">가져오기 유형 선택</h2>
					<div class="process-header-desc">가져오기할 작업의 유형을 아래에서 선택해주세요.</div>
					<ul class="process-type-select">
						<li>
							<label class="checkbox-container">SQL
							  <input type="radio" v-model="<varsql:namespace/>importType" value="sql" checked="checked">
							  <span class="radiomark"></span>
							</label>
							<div class="checkbox-container-desc"> SQL 파일을 실행해서 입력합니다</div>
						</li>
						<li>
							<label class="checkbox-container">XML,JSON
							  <input type="radio" v-model="<varsql:namespace/>importType" value="xml-json">
							  <span class="radiomark"></span>
							</label>
							<div class="checkbox-container-desc"> XML , JSON 파일을 이용하여 데이터를  입력합니다</div>
						</li>
						<li>
							<label class="checkbox-container">CSV,XLS
							  <input type="radio" v-model="<varsql:namespace/>importType" value="csv-xls">
							  <span class="radiomark"></span>
							</label>
							<div class="checkbox-container-desc"> XML , JSON 파일을 이용하여 데이터를  입력합니다</div>
						</li>
					</ul>
				</div>
			</div>
		</div>
		
		<div class="process-step" :class="selectMenu==2?'active':''">
			<div class="col-xs-12">
				<form id="<varsql:namespace/>fileImportForm" name="<varsql:namespace/>fileImportForm" class="form-horizontal bv-form">
					<div class="field-group">
						<label class="col-xs-3"><spring:message code="file_name" /></label>
						<div class="col-xs-9 padding0">
							<input type="file" class="form-control text required input-sm" name="file">
						</div>
					</div>
				</form>
			</div>
			<div class="col-xs-12">
				<button type="button" class="btn-md" @click="upload()"><spring:message code="upload" /></button>
			</div>
		</div>
		
		<div class="col-xs-12">
			<div class="process-step-btn-area">
				<button type="button" class="btn-md"><spring:message code="import" /></button>
			</div>
		</div>
	</div>
</div>

<script>

VarsqlAPP.vueServiceBean({
	el: '#<varsql:namespace/>'
	,data: {
		selectMenu : 1
		,<varsql:namespace/>importType : ''
	}
	,mounted : function (){
		
	}
	,methods:{
		init : function (){
			
		}
		// menu 선택
		,selectMenu : function (selectMenu){
			var _self = this; 
			
			_self.selectMenu = selectMenu;
		}
		,upload : function (){
			
			
			VARSQL.req.uploadFile("#<varsql:namespace/>fileImportForm",{
				url : {type:VARSQL.uri.database, url:'/file/import'}
				,param :{div :'fileImport',read:true}
				,success: function (resData){
					console.log(resData)
				}
			})
		}
	}
});

</script>