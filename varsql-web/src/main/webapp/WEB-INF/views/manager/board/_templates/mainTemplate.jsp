<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/include/tagLib.jspf"%>

<!doctype html>
<html>
<head>
<title>- <spring:message code="screen.user" /> -</title>
<%@ include file="/WEB-INF/include/appHead.jsp"%>
</head>

<body style="padding:10px 10px;">
	<div id="wrapper" class="wh100">
		<!--Start Content-->
		<div id="main-content" class="wh100">
			<tiles:insertAttribute name="body" />
		</div>
		<!--End Content-->
	</div>
	<div style="clear:both;"></div>
</body>
</html>

<script>
VARSQL.unload('refresh');
</script>