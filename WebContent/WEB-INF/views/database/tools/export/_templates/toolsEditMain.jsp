<!DOCTYPE html>
<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/include/tagLib.jspf"%>
<html>
<head>
<%@ include file="/WEB-INF/include/initvariable.jspf"%>
<title><spring:message code="database.tools" /></title>
<%@ include file="/WEB-INF/include/head-preferences.jsp"%>

</head>

<BODY class="setting-body">
	<tiles:insertAttribute name="body" />
	<iframe style="display:none;width:0px;height:0px;" id="downloadForm" name="downloadForm"></iframe>
</BODY>
</html>


