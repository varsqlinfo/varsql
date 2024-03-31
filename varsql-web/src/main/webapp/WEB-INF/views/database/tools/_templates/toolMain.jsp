<!DOCTYPE html>
<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/include/tagLib.jspf"%>
<html>
<head>
<title><spring:message code="tool" /></title>
<%@ include file="/WEB-INF/include/database-dialog-head.jsp"%>

</head>

<BODY class="menu-tools">
	<tiles:insertAttribute name="body" />
	<iframe style="display:none;width:0px;height:0px;" id="downloadForm" name="downloadForm"></iframe>
</BODY>
</html>


