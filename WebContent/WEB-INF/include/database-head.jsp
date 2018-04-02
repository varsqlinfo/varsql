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

<link href="${pageContextPath}/webstatic/css/editor/codemirror.css?version=${codemirror_ver}" rel="stylesheet" type="text/css">
<link href="${pageContextPath}/webstatic/css/editor/show-hint.css?version=${codemirror_ver}" rel="stylesheet" type="text/css">
<link href="${pageContextPath}/webstatic/css/pub.ep.css?version=${css_ver}" rel="stylesheet" type="text/css">
<link href="${pageContextPath}/webstatic/css/pub.tab.css?version=${css_ver}" rel="stylesheet" type="text/css">
<link href="${pageContextPath}/webstatic/css/pub.grid.fixed.css?version=${css_ver}" rel="stylesheet" type="text/css">
<link href="${pageContextPath}/webstatic/css/varsql.common.css?version=${css_ver}" rel="stylesheet">
<link href="${pageContextPath}/webstatic/css/varsql.main.css?version=${css_ver}" rel="stylesheet" type="text/css">
<link href="${pageContextPath}/webstatic/css/varsql.main.layout.css?version=${css_ver}" rel="stylesheet" type="text/css">


<!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
<!--[if lt IE 9]>
    <script src="http://html5shiv.googlecode.com/svn/trunk/html5.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/respond.js/1.4.2/respond.min.js"></script>
<![endif]-->

<script src="${pageContextPath}/webstatic/js/jquery-1.10.2.min.js"></script>
<script src="${pageContextPath}/webstatic/js/jquery-ui.min.js"></script>
<script src="${pageContextPath}/webstatic/js/plugins/layout/jquery.layout.min.js"></script>
<script src="${pageContextPath}/webstatic/js/plugins/clipboard/clipboard.min.js"></script>

<script src="${pageContextPath}/webstatic/js/varsql.custom.plugin.js?version=${pubjs_ver}"></script>
<script src="${pageContextPath}/webstatic/js/varsql.web.js?version=${pubjs_ver}"></script>
<script src="${pageContextPath}/webstatic/js/varsql.base.js?version=${pubjs_ver}"></script>
<script src="${pageContextPath}/webstatic/js/varsql.core.ui.js?version=${pubjs_ver}"></script>
<script src="${pageContextPath}/webstatic/js/varsql.lang.js?version=${pubjs_ver}"></script>

<!-- sql editor -->
<script src="${pageContextPath}/webstatic/js/plugins/sqlEditor/codemirror.js?version=${codemirror_ver}"></script>
<script src="${pageContextPath}/webstatic/js/plugins/sqlEditor/sql.js?version=${codemirror_ver}"></script>
<script src="${pageContextPath}/webstatic/js/plugins/sqlEditor/show-hint.js?version=${codemirror_ver}"></script>
<script src="${pageContextPath}/webstatic/js/plugins/sqlEditor/var-sql-hint.js?version=${codemirror_ver}"></script>
<script src="${pageContextPath}/webstatic/js/plugins/sqlEditor/search/search.js?version=${codemirror_ver}"></script>
<script src="${pageContextPath}/webstatic/js/plugins/sqlEditor/search/searchcursor.js?version=${codemirror_ver}"></script>

<script src="${pageContextPath}/webstatic/js/pub.grid.fixed.js?version=${pubjs_ver}"></script>
<script src="${pageContextPath}/webstatic/js/pub.tab.js?version=${pubjs_ver}"></script>
<script src="${pageContextPath}/webstatic/js/pub.autocomplete.js?version=${pubjs_ver}"></script>

<script>
VARSQL.loadResource([VARSQL.staticResource.get('contextMenu') ]);
//VARSQL.loadResource([VARSQL.staticResource.get('pubgrid') ]);\
VARSQL.unload();
</script>





