<!DOCTYPE html>
<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ include file="/WEB-INF/include/tagLib.jspf"%>

<html>
<head>
	<title><spring:message code="user.preferences.title"/></title>
	<%@ include file="/WEB-INF/include/head-manager.jsp"%>
</head>
<body>
	<div id="wrapper" class="height100">
		<!-- Navigation -->
		<c:if test="${headerview ne 'N'}">
			<nav class="manager-top-wrap navbar-inverse navbar-fixed-top" role="navigation">
				<tiles:insertAttribute name="header" />
			</nav>
		</c:if>
		<!-- Sidebar Menu Items - These collapse to the responsive navigation menu on small screens -->
		<div id="main-wrap" class="container-fluid height100">
			<div class="row height100">
				<div id="sidebar-left" class="col-xs-2 col-sm-2">
					<tiles:insertAttribute name="left" />
				</div>

				<!--Start Content-->
				<div id="main-content" class="height100 col-xs-12 col-sm-10">
					<tiles:insertAttribute name="body" />
				</div>
				<!--End Content-->
			</div>
		</div>
	</div>
</body>
</html>
<script>
$(document).ready(function (){

	$('.show-sidebar').on('click', function (e) {
		e.preventDefault();
		$('div#main-wrap').toggleClass('sidebar-show');
	});
})
</script>