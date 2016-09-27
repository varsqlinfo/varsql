<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/include/initvariable.jspf"%>

<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="description" content="">
<meta name="author" content="">

<!-- Bootstrap Core CSS -->
<link href="${pageContextPath}/webstatic/css/bootstrap.min.css" rel="stylesheet">

<link href="${pageContextPath}/webstatic/css/jquery-ui.css" rel="stylesheet">
<!-- Custom CSS -->
<link href="${pageContextPath}/webstatic/css/sb-admin-2.css" rel="stylesheet">
<!--  link href="${pageContextPath}/webstatic/css/ui.jqgrid.css" rel="stylesheet"-->

<!-- Morris Charts CSS -->
<link href="${pageContextPath}/webstatic/css/plugins/morris.css" rel="stylesheet">

<!-- Custom Fonts -->
<link href="${pageContextPath}/webstatic/font-awesome-4.1.0/css/font-awesome.min.css" rel="stylesheet" type="text/css">

<!-- context menu css
<link href="${pageContextPath}/webstatic/css/jquery.contextMenu.css" rel="stylesheet" type="text/css"-->

<link href="${pageContextPath}/webstatic/css/varsql.main.css" rel="stylesheet" type="text/css">

<link href="${pageContextPath}/webstatic/css/editor/codemirror.css" rel="stylesheet" type="text/css">
<link href="${pageContextPath}/webstatic/css/pub.grid.css" rel="stylesheet" type="text/css">


<!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
<!--[if lt IE 9]>
    <script src="http://html5shiv.googlecode.com/svn/trunk/html5.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/respond.js/1.4.2/respond.min.js"></script>
<![endif]-->
<%
long versionNum = java.lang.Math.round(java.lang.Math.random() * 20000); %>
<script src="${pageContextPath}/webstatic/js/jquery-1.10.2.min.js"></script>
<script src="${pageContextPath}/webstatic/js/jquery.form.min.js"></script>
<script src="${pageContextPath}/webstatic/js/jquery-ui-1.10.3.custom.min.js"></script>
<script src="${pageContextPath}/webstatic/js/jquery.jqGrid.src.js"></script>
<script src="${pageContextPath}/webstatic/js/jquery.serializeJSON.js"></script>
<script src="${pageContextPath}/webstatic/js/plugins/clipboard/clipboard.min.js"></script>
<!--  script src="${pageContextPath}/webstatic/js/plugins/contextMenu/jquery.contextMenu.js"></script -->

<script src="${pageContextPath}/webstatic/js/varsql.web.js?version=<%=versionNum%>"></script>
<script src="${pageContextPath}/webstatic/js/varsql.base.js?version=<%=versionNum%>"></script>
<script src="${pageContextPath}/webstatic/js/varsql.core.ui.js?version=<%=versionNum%>"></script>
<script src="${pageContextPath}/webstatic/js/varsql.lang.js?version=<%=versionNum%>"></script>

<script src="${pageContextPath}/webstatic/js/bootstrap.min.js" type="text/javascript"></script>
<script src="${pageContextPath}/webstatic/js/bootstrapValidator.js" type="text/javascript"></script>

<!-- Metis Menu Plugin JavaScript -->
<script src="${pageContextPath}/webstatic/js/plugins/metisMenu/metisMenu.min.js"></script>

<!-- sql editor -->
<script src="${pageContextPath}/webstatic/js/plugins/sqlEditor/codemirror.js"></script>
<script src="${pageContextPath}/webstatic/js/plugins/sqlEditor/sql.js"></script>

<script src="${pageContextPath}/webstatic/js/pub.grid.js?version=<%=versionNum%>"></script>

<script>
//VARSQL.loadResource([VARSQL.staticResource.get('jquery.contextMenu') ]);
VARSQL.loadResource([VARSQL.staticResource.get('contextMenu') ]);
//VARSQL.loadResource([VARSQL.staticResource.get('pubgrid') ]);\
VARSQL.unload();
</script>





