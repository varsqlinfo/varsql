<!DOCTYPE html>
<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ include file="/WEB-INF/include/tagLib.jspf"%>

<html>
<head>
<title><spring:message code="user.home.title"/></title>
<%@ include file="/WEB-INF/include/database-top-head.jsp"%>
</head>
<body>
  <div>
  <!-- Navigation -->
  	<div id="user-header">
	    <tiles:insertAttribute name="header" />
	</div>
  	<div id="user-page-wrapper">
		<tiles:insertAttribute name="body" />
    </div>
    
    <div class="user-main-footer">
    	<tiles:insertAttribute name="footer" />
    </div>
    <!-- /#page-wrapper -->
    <%--
    <div id="footer">
      <tiles:insertAttribute name="footer" />
    </div>
     --%>
  </div>
</body>
</html>

