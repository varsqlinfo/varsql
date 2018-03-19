<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/include/head-meta.jspf"%>
<%@ include file="/WEB-INF/include/initvariable.jspf"%>

<link rel="shortcut icon" href="${pageContextPath}/webstatic/favicon.ico" type="image/x-icon">
<link rel="icon" href="${pageContextPath}/webstatic/favicon.ico" type="image/x-icon">

<link href="${pageContextPath}/webstatic/css/jquery-ui.css" rel="stylesheet">
<!-- Bootstrap Core CSS -->
<link href="${pageContextPath}/webstatic/css/bootstrap.min.css" rel="stylesheet">

<!-- Custom Fonts -->
<link href="${pageContextPath}/webstatic/font-awesome-4.1.0/css/font-awesome.min.css" rel="stylesheet" type="text/css">

<link href="${pageContextPath}/webstatic/css/varsql.common.css" rel="stylesheet">
<link href="${pageContextPath}/webstatic/css/varsql.main.css" rel="stylesheet" type="text/css">
<%
long versionNum = java.lang.Math.round(java.lang.Math.random() * 20000); %>
<script src="${pageContextPath}/webstatic/js/jquery-1.10.2.min.js"></script>
<script src="${pageContextPath}/webstatic/js/jquery-ui.min.js"></script>

<script src="${pageContextPath}/webstatic/js/varsql.custom.plugin.js?version=<%=versionNum%>"></script>
<script src="${pageContextPath}/webstatic/js/varsql.web.js?version=<%=versionNum%>"></script>
<script src="${pageContextPath}/webstatic/js/varsql.lang.js?version=<%=versionNum%>"></script>

