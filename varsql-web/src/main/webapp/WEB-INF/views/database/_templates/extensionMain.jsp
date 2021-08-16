<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%><%@ include file="/WEB-INF/include/tagLib.jspf"%>
<!DOCTYPE html>

<html>
<head>
<title>${vname}::<spring:message code="screen.user" /></title>
<%@ include file="/WEB-INF/include/database-extension.jsp"%>

</head>
<body class="extension-body">
	<tiles:insertAttribute name="body" />
</body>
</html>
