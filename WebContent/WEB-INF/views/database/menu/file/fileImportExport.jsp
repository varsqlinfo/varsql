<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/include/tagLib.jspf"%>

<div id="<varsql:namespace/>" class="menu-popup-layer" style="width:450px;height:400px;">
	<div class="preferences-nav col-xs-3">
		<ul>
			<li :class="selectMenu==1?'active':''" @click="selectMenu(1)">
				<a href="javascript:;"><spring:message code="import" /></a>
			</li>
			<li :class="selectMenu==2?'active':''" @click="selectMenu(2)">
				<a href="javascript:;"><spring:message code="export" /></a>
			</li>
		</ul>
	</div>
	
	<div class="preferences-body col-xs-9 scroll-y">
		<div class="process-step" :class="selectMenu==1?'active':''">
			<div class="col-xs-12">
				<form id="<varsql:namespace/>fileImportForm" name="<varsql:namespace/>fileImportForm" class="form-horizontal bv-form">
					<div class="field-group">
						<label class="col-xs-3"><spring:message code="file_name" /></label>
						<div class="col-xs-9 padding0">
							<input type="file" class="form-control text required input-sm" name="export_name">
						</div>
					</div>
				</form>
			</div>
			<div class="col-xs-12">
				<button type="button" class="btn-md"><spring:message code="upload" /></button>
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
	}
});

</script>