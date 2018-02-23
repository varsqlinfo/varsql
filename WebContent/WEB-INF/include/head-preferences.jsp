<%@ page language="java" pageEncoding="UTF-8" %>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<%@ include file="/WEB-INF/include/initvariable.jspf"%>
<link rel="shortcut icon" href="${pageContextPath}/webstatic/favicon.ico" type="image/x-icon">
<link rel="icon" href="${pageContextPath}/webstatic/favicon.ico" type="image/x-icon">

<%
long versionNum = java.lang.Math.round(java.lang.Math.random() * 20000); %>

<!-- Bootstrap Core CSS -->
<link href="${pageContextPath}/webstatic/css/bootstrap.min.css" rel="stylesheet">
<!-- Custom Fonts -->
<link href="${pageContextPath}/webstatic/font-awesome-4.1.0/css/font-awesome.min.css" rel="stylesheet" type="text/css">

<!-- Custom CSS -->
<link href="${pageContextPath}/webstatic/css/varsql.common.css" rel="stylesheet">
<link href="${pageContextPath}/webstatic/css/varsql.main.css" rel="stylesheet" type="text/css">
<link href="${pageContextPath}/webstatic/css/varsql.preferences.css?version=<%=versionNum%>" rel="stylesheet" type="text/css">
<link href="${pageContextPath}/webstatic/css/pub.multiselect.css?version=<%=versionNum%>" rel="stylesheet" type="text/css">
<link href="${pageContextPath}/webstatic/css/pub.tree.css?version=<%=versionNum%>" rel="stylesheet" type="text/css">


<!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
<!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
    <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
<![endif]-->

<script src="${pageContextPath}/webstatic/js/jquery-1.10.2.min.js"></script>
<script src="${pageContextPath}/webstatic/js/jquery-ui.min.js"></script>
<script src="${pageContextPath}/webstatic/js/jquery.serializeJSON.js"></script>
<script src="${pageContextPath}/webstatic/js/varsql.web.js"></script>
<script src="${pageContextPath}/webstatic/js/pub.tree.js"></script>
<script src="${pageContextPath}/webstatic/js/pub.multiselect.js"></script>

<script src="${pageContextPath}/webstatic/js/vue.min.js"></script>
<script src="${pageContextPath}/webstatic/js/vue.varsql.js"></script>

<script src="${pageContextPath}/webstatic/js/bootstrap.min.js" type="text/javascript"></script>
<script src="${pageContextPath}/webstatic/js/bootstrapValidator.js" type="text/javascript"></script>

