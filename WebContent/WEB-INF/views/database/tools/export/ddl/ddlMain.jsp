<!DOCTYPE html>
<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/include/tagLib.jspf"%>
<html>
<head>
<title><spring:message code="database.tools" /></title>
<%@ include file="/WEB-INF/include/head-preferences.jsp"%>

</head>

<BODY class="preferences-body">
	<div class="preferences">
		<div class="navi-area col">
			<div class="preferences-left-body">
				<div id="preferences_area"></div>
			</div>
		</div>
		<div class="content-area-frame col scroll-y">
			<iframe id="view_iframe" name="view_iframe"	class="content-view-iframe"></iframe>
		</div>
	</div>
</BODY>
</html>

<script>
$('.pub-main-body').on('selectstart', function (e){
	return false;
})

var toolMain = {
	init : function (){
		var treeItem = [];
		treeItem.push({id:'top'	,pid:''	,name:'preferences-top'});
		treeItem.push({id:'1',pid:'top',name:'<spring:message code="label.table" />',url:'<c:url value="/database/tools/export/ddl/table.vsql?conuid=${param.conuid}" />'});
		treeItem.push({id:'2',pid:'top',name:'<spring:message code="label.view" />',url:'<c:url value="/database/tools/export/ddl/view.vsql?conuid=${param.conuid}" />'});
		//treeItem.push({id:'2',pid:'top',name:'<spring:message code="label.view" />',url:'<c:url value="/tools/preferences/sqlFormatSetting.vsql?conuid=${param.conuid}" />'});	

		$.pubTree("#preferences_area", {
			source : treeItem
			,useIcon :{icon : false}
			,click : function (sObj){
				$('#view_iframe').attr('src',sObj.item.url);
			}
		}).nodeClick('1'); // 트리 객체 네임  div명	
	}
}

$(document).ready(function (){
	toolMain.init();
})
</script>


