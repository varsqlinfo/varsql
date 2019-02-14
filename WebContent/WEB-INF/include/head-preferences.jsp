<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/include/head-meta.jspf"%>
<%@ include file="/WEB-INF/include/initvariable.jspf"%>

<%
long versionNum = java.lang.Math.round(java.lang.Math.random() * 20000); %>

<!-- Bootstrap Core CSS -->
<link href="${pageContextPath}/webstatic/css/bootstrap.min.css" rel="stylesheet">
<!-- Custom Fonts -->
<link href="${pageContextPath}/webstatic/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">

<!-- Custom CSS -->
<link href="${pageContextPath}/webstatic/css/varsql.common.css" rel="stylesheet">
<link href="${pageContextPath}/webstatic/css/varsql.preferences.css?version=<%=versionNum%>" rel="stylesheet" type="text/css">
<link href="${pageContextPath}/webstatic/css/pub.multiselect.css?version=<%=versionNum%>" rel="stylesheet" type="text/css">
<link href="${pageContextPath}/webstatic/css/pub.tree.css?version=<%=versionNum%>" rel="stylesheet" type="text/css">


<script src="${pageContextPath}/webstatic/js/jquery-1.10.2.min.js"></script>
<script src="${pageContextPath}/webstatic/js/jquery-ui.min.js"></script>
<script src="${pageContextPath}/webstatic/js/jquery.serializeJSON.js"></script>
<script src="${pageContextPath}/webstatic/js/varsql.web.js"></script>
<script src="${pageContextPath}/webstatic/js/varsql.ui.js"></script>
<script src="${pageContextPath}/webstatic/js/pub.tree.js"></script>
<script src="${pageContextPath}/webstatic/js/pub.multiselect.js"></script>

<script src="${pageContextPath}/webstatic/js/vue.min.js"></script>
<script src="${pageContextPath}/webstatic/js/vue.varsql.js"></script>

<script src="${pageContextPath}/webstatic/js/bootstrap.min.js" type="text/javascript"></script>
<script src="${pageContextPath}/webstatic/js/bootstrapValidator.js" type="text/javascript"></script>

