<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/include/initvariable.jspf"%>

<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="description" content="">
<meta name="author" content="">

<link rel="shortcut icon" href="${pageContextPath}/webstatic/favicon.ico" type="image/x-icon">
<link rel="icon" href="${pageContextPath}/webstatic/favicon.ico" type="image/x-icon">

<!-- Bootstrap Core CSS -->
<link href="${pageContextPath}/webstatic/css/bootstrap.min.css" rel="stylesheet">

<link href="${pageContextPath}/webstatic/css/jquery-ui.css" rel="stylesheet">
<!-- Custom CSS -->
<!--  link href="${pageContextPath}/webstatic/css/ui.jqgrid.css" rel="stylesheet"-->

<!-- Custom Fonts -->
<link href="${pageContextPath}/webstatic/font-awesome-4.1.0/css/font-awesome.min.css" rel="stylesheet" type="text/css">

<link href="${pageContextPath}/webstatic/css/varsql.main.css" rel="stylesheet" type="text/css">



<!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
<!--[if lt IE 9]>
    <script src="http://html5shiv.googlecode.com/svn/trunk/html5.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/respond.js/1.4.2/respond.min.js"></script>
<![endif]-->
<%
long versionNum = java.lang.Math.round(java.lang.Math.random() * 20000); %>
<script src="${pageContextPath}/webstatic/js/plugins/mustache/mustache.min.js" type="text/javascript"></script>
<script src="${pageContextPath}/webstatic/js/jquery-1.10.2.min.js"></script>
<script src="${pageContextPath}/webstatic/js/jquery-ui.min.js"></script>

<script src="${pageContextPath}/webstatic/js/varsql.web.js?version=<%=versionNum%>"></script>

<script src="${pageContextPath}/webstatic/js/bootstrap.min.js" type="text/javascript"></script>
