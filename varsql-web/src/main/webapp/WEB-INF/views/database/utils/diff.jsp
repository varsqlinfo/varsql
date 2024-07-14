<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/include/tagLib.jspf"%>
<!-- Page Heading -->
<div class="row page-header-area">
    <div class="col-lg-12">
        <h1 class="page-header">
        	<spring:message code="header.menu.tool.utils.diff" />
        </h1>
    </div>
    <!-- /.col-lg-12 -->
</div>

<div class="wh100" id=epViewArea style="padding-top: 5px;height:calc(100% - 35px)">
	<div style="height:30px;">
		<template v-if="!isViewer">
			<button type="button" @click="diffMove('previous')" class="btn btn-default">이전</button>
			<button type="button" @click="diffMove('next')" class="btn btn-default">다음</button>
			<button type="button" @click="viewerToggle(true)" class="btn btn-default">뷰어보기</button>
		</template>
		<template v-else>
			<button type="button" @click="viewerMove('previous')" class="btn btn-default">이전</button>
			<button type="button" @click="viewerMove('next')" class="btn btn-default">다음</button>
			<button type="button" @click="viewerToggle(false)" class="btn btn-default">닫기</button>
		</template>
	</div>
	<div id="diffEditor" class="wh100" style="height:calc(100% - 30px);">
	</div>
	
	<pre id="orginCont" style="display:none;"><c:out value="${param.orgin}" escapeXml="true"/></pre>
	<pre id="modifedCont" style="display:none;"><c:out value="${param.modified}" escapeXml="true"/></pre>
</div>

<varsql:importResources resoures="codeEditor" editorHeight="100%"/>

<script>

VarsqlAPP.vueServiceBean( {
	el: '#epViewArea'
	,data: {
		diffEditor : {}
		,isViewer :false
	}
	,methods:{
		init : function(){
			this.diffEditor = codeEditor.diffEditor(document.getElementById('diffEditor'), {
				useLineDiff: true,
				editorOptions: {theme: 'vs-'+VARSQLUI.theme()}
			})
			
			this.diffEditor.diff(document.getElementById('orginCont').innerText, document.getElementById('modifedCont').innerText);
		}
		,diffMove(mode) {
			if (mode == 'next') {
				this.diffEditor.next();
			} else {
				this.diffEditor.prev();
			}
		}
		// 
		,viewerMove(mode) {
			if (mode == 'next') {
				this.diffEditor.viewerPrev();
			} else {
				this.diffEditor.viewerNext();
			}
		}
		// 뷰어 토글	
		,viewerToggle(flag) {
			this.isViewer = flag; 
			if(flag){
				this.diffEditor.viewerOpen()	
			}else{
				this.diffEditor.viewerClose()	
			}
		}
	}
});



</script>
