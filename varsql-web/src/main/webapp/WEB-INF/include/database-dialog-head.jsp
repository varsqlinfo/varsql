<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/include/head-meta.jspf"%>
<%@ include file="/WEB-INF/include/initvariable.jspf"%>
<%@ taglib prefix="varsql" uri="http://varsql.vartech.com/varsql"%>

<%
long versionNum = java.lang.Math.round(java.lang.Math.random() * 20000); %>


<!-- Custom Fonts -->
<link href="${pageContextPath}/webstatic/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">

<link href="${pageContextPath}/webstatic/css/pub.all.min.css?version=${css_ver}" rel="stylesheet" type="text/css">
<link href="${pageContextPath}/webstatic/css/varsql.db.preferences.min.css?version=<%=versionNum%>" rel="stylesheet" type="text/css">
<link href="${pageContextPath}/webstatic/css/prettify/prettify.css?version=${prettify_ver}" rel="stylesheet" type="text/css">
<link href="${pageContextPath}/webstatic/css/jquery.toast.min.css" rel="stylesheet" type="text/css">
<!-- Custom CSS -->
<link href="${pageContextPath}/webstatic/css/pub.multiselect.css?version=<%=versionNum%>" rel="stylesheet" type="text/css">
<link href="${pageContextPath}/webstatic/css/pub.tree.css?version=<%=versionNum%>" rel="stylesheet" type="text/css">


<script src="${pageContextPath}/webstatic/i18n/<varsql:resourceLocaleName name="varsql.lang"/>.js?version=${pubjs_ver}"></script>
<script src="${pageContextPath}/webstatic/js/jquery-3.3.1.min.js"></script>
<script src="${pageContextPath}/webstatic/js/jquery-ui.min.js"></script>
<script src="${pageContextPath}/webstatic/js/jquery.serializeJSON.js"></script>
<script src="${pageContextPath}/webstatic/js/plugins/toast/jquery.toast.min.js"></script>
<script src="${pageContextPath}/webstatic/js/varsql.web.js?version=${pubjs_ver}"></script>
<script src="${pageContextPath}/webstatic/js/varsql.web.ui.js?version=${pubjs_ver}"></script>
<script src="${pageContextPath}/webstatic/js/pub.tree.js"></script>
<script src="${pageContextPath}/webstatic/js/pub.multiselect.js"></script>
<script src="${pageContextPath}/webstatic/js/vue.min.js"></script>
<script src="${pageContextPath}/webstatic/js/vue.varsql.js?version=${pubjs_ver}"></script>

<script src="${pageContextPath}/webstatic/js/bootstrap.min.js" type="text/javascript"></script>
<script src="${pageContextPath}/webstatic/js/bootstrapValidator.js" type="text/javascript"></script>

