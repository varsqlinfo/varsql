<!DOCTYPE html>
<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ include file="/WEB-INF/include/tagLib.jspf"%>

<html>
<head>
<title><spring:message code="varsql.title"/></title>
<%@ include file="/WEB-INF/include/head-user.jsp"%>
</head>
<body class="user-main-body">
  	<div id="user-header">
	    <tiles:insertAttribute name="header" />
	</div>
  	<div id="user-page-wrapper">
		<tiles:insertAttribute name="body" />
    </div>

    <div class="user-main-footer">
    	<tiles:insertAttribute name="footer" />
    </div>
</body>
</html>

