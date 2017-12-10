<!DOCTYPE html>
<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ include file="/WEB-INF/include/tagLib.jspf"%>

<html>
<head>
	<%@ include file="/WEB-INF/include/initvariable.jspf"%>
	<title><spring:message code="user.preferences.title"/></title>
	<%@ include file="/WEB-INF/include/head-manager.jsp"%>
</head>
<body>
	<div id="wrapper" class="height100">
		<!-- Navigation -->
		<c:if test="${headerview eq 'Y'}">
			<nav class="manager-top-wrap navbar-inverse navbar-fixed-top" role="navigation">
				<tiles:insertAttribute name="header" />
			</nav>
		</c:if>
		<!-- Sidebar Menu Items - These collapse to the responsive navigation menu on small screens -->
		<div id="main-wrap" style="padding-top:${headerview eq 'Y'?'50':'0'}px" class="container-fluid height100">
			<div class="row height100">
				<div class="col-xs-2">
					<tiles:insertAttribute name="left" />
				</div>
				
				<!--Start Content-->
				<div id="main-content" class="height100 col-xs-10">
					<tiles:insertAttribute name="body" />
				</div>
				<!--End Content-->
			</div>
		</div>
	</div>
</body>
</html>

