<!DOCTYPE html>
<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ include file="/WEB-INF/include/initvariable.jspf"%>

<html>
<head>
	<title><spring:message code="user.home.title"/></title>
	<%@ include file="/WEB-INF/include/user-head.jsp"%>
</head>
<body>
  <div id="wrapper">
  <!-- Navigation -->
  	<div id="user-header">
	    <nav class="user-navbar navbar-static-top" role="navigation" style="margin-bottom: 0">
	        <tiles:insertAttribute name="header" />
	    </nav>
	</div>
  	<div id="user-page-wrapper">
		<tiles:insertAttribute name="body" />
    </div>
    <!-- /#page-wrapper -->
    <div id="footer">
      <tiles:insertAttribute name="footer" />
    </div>
  </div>
</body>
</html>

