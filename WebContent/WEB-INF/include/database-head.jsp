<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/include/head-meta.jspf"%>
<%@ include file="/WEB-INF/include/initvariable.jspf"%>


<!-- Custom Fonts -->
<link href="${pageContextPath}/webstatic/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
<link href="${pageContextPath}/webstatic/css/jquery.toast.min.css" rel="stylesheet" type="text/css">
<link href="${pageContextPath}/webstatic/css/plugins/layout/goldenlayout-base.css" rel="stylesheet" type="text/css">
<link href="${pageContextPath}/webstatic/css/plugins/layout/goldenlayout-light-theme.css" rel="stylesheet" type="text/css">

<link href="${pageContextPath}/webstatic/css/editor/codemirror.css?version=${codemirror_ver}" rel="stylesheet" type="text/css">
<link href="${pageContextPath}/webstatic/css/editor/show-hint.css?version=${codemirror_ver}" rel="stylesheet" type="text/css">
<link href="${pageContextPath}/webstatic/css/pub.all.min.css?version=${css_ver}" rel="stylesheet" type="text/css">
<link href="${pageContextPath}/webstatic/css/varsql.db.main.min.css?version=${css_ver}" rel="stylesheet" type="text/css">

<link href="${pageContextPath}/webstatic/css/prettify/prettify.css?version=${prettify_ver}" rel="stylesheet" type="text/css">

<script src="${pageContextPath}/webstatic/js/jquery-3.3.1.min.js"></script>
<script src="${pageContextPath}/webstatic/js/jquery-ui.min.js"></script>
<script src="${pageContextPath}/webstatic/js/plugins/toast/jquery.toast.min.js"></script>
<script src="${pageContextPath}/webstatic/js/plugins/layout/goldenlayout.min.js"></script>
<script src="${pageContextPath}/webstatic/js/plugins/clipboard/clipboard.min.js"></script>

<script src="${pageContextPath}/webstatic/js/varsql.custom.plugin.js?version=${pubjs_ver}"></script>
<script src="${pageContextPath}/webstatic/js/varsql.web.js?version=${pubjs_ver}"></script>
<script src="${pageContextPath}/webstatic/js/varsql.base.js?version=${pubjs_ver}"></script>
<script src="${pageContextPath}/webstatic/js/varsql.core.ui.js?version=${pubjs_ver}"></script>
<script src="${pageContextPath}/webstatic/js/varsql.lang.js?version=${pubjs_ver}"></script>
<script src="${pageContextPath}/webstatic/js/varsql.ui.js?version=${pubjs_ver}"></script>
<script src="${pageContextPath}/webstatic/js/varsql.vender.mode.js?version=${pubjs_ver}"></script>

<!-- sql editor -->
<script src="${pageContextPath}/webstatic/js/plugins/sqlEditor/codemirror.js?version=${codemirror_ver}"></script>
<script src="${pageContextPath}/webstatic/js/plugins/sqlEditor/sql.js?version=${codemirror_ver}"></script>
<script src="${pageContextPath}/webstatic/js/plugins/sqlEditor/show-hint.js?version=${codemirror_ver}"></script>
<script src="${pageContextPath}/webstatic/js/plugins/sqlEditor/var-sql-hint.js?version=${codemirror_ver}"></script>
<script src="${pageContextPath}/webstatic/js/plugins/sqlEditor/search/search.js?version=${codemirror_ver}"></script>
<script src="${pageContextPath}/webstatic/js/plugins/sqlEditor/search/searchcursor.js?version=${codemirror_ver}"></script>
<script src="${pageContextPath}/webstatic/js/plugins/sqlEditor/matchbrackets.js?version=${codemirror_ver}"></script>
<script src="${pageContextPath}/webstatic/js/plugins/sqlEditor/closebrackets.js?version=${codemirror_ver}"></script>

<script src="${pageContextPath}/webstatic/js/plugins/prettify/prettify.js?version=${prettify_ver}"></script>
<script src="${pageContextPath}/webstatic/js/plugins/prettify/lang-sql.js?version=${prettify_ver}"></script>

<script src="${pageContextPath}/webstatic/js/pub.grid.js?version=${pubjs_ver}"></script>
<script src="${pageContextPath}/webstatic/js/pub.tab.js?version=${pubjs_ver}"></script>
<script src="${pageContextPath}/webstatic/js/pub.context.js?version=${pubjs_ver}"></script>
<script src="${pageContextPath}/webstatic/js/pub.autocomplete.js?version=${pubjs_ver}"></script>

<script>
VARSQL.unload();
</script>





