<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/include/tagLib.jspf"%>

<div id="<varsql:namespace/>" class="menu-popup-layer wh100">
	<div class="preferences-nav col-xs-3">
		<ul>
			<li :class="step==1?'active':''" @click="selectMenu(1)">
				<a href="javascript:;"><spring:message code="export" /></a>
			</li>
			<li :class="step==2?'active':''" @click="selectMenu(2)">
				<a href="javascript:;"><spring:message code="import" /></a>
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

<script>

VarsqlAPP.vueServiceBean({
	el: '#<varsql:namespace/>'
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
		
	}
	,methods:{
		init : function (){
			
		}
		// menu 선택
		,selectMenu : function (step){
			var _self = this; 
			
			this.step = step;

		}
		
	}
});

</script>