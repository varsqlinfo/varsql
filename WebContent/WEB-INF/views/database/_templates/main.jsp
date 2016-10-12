<!DOCTYPE html>
<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/include/tagLib.jspf"%>
<html>
<head>
<%@ include file="/WEB-INF/include/initvariable.jspf"%>
<title><spring:message code="screen.user" /></title>
<%@ include file="/WEB-INF/include/user-head.jsp"%>
</head>
<body>
	<div id="wrapper">
		<!-- Page Heading -->
		<div id="db-header">
			<div class="col-lg-12">
				<tiles:insertAttribute name="header" />
			</div>
		</div>
		<div id="db-page-wrapper">
			<div class="container-fluid fill row">
				<div class="row fill">
					<tiles:insertAttribute name="body" />
				</div>
			</div>
		</div>
	</div>
	<div id="dbHiddenArea"></div>
	
	<form name="downloadForm" id="downloadForm"  style="display:none;" target="hiddenIframe"></form>
	<iframe name="hiddenIframe" id="hiddenIframe"  style="width:0px;height:px;display:none;"></iframe>
</body>
</html>

<style>
.container-fluid {
  min-width:1024px;
  max-width: none !important;
}
</style>
