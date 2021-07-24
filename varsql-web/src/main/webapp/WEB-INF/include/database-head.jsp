<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/include/head-meta.jspf"%>
<%@ include file="/WEB-INF/include/initvariable.jspf"%>
<%@ taglib prefix="varsql" uri="http://varsql.vartech.com/varsql"%>

<%-- Custom Fonts --%>
<link href="${pageContextPath}/webstatic/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
<link href="${pageContextPath}/webstatic/css/jquery.toast.min.css" rel="stylesheet" type="text/css">
<link href="${pageContextPath}/webstatic/css/plugins/layout/goldenlayout-base.css" rel="stylesheet" type="text/css">
<link href="${pageContextPath}/webstatic/css/plugins/layout/goldenlayout-light-theme.css" rel="stylesheet" type="text/css">

<link href="${pageContextPath}/webstatic/css/editor/codemirror.css?version=${codemirror_ver}" rel="stylesheet" type="text/css">
<link href="${pageContextPath}/webstatic/css/editor/show-hint.css?version=${codemirror_ver}" rel="stylesheet" type="text/css">
<link href="${pageContextPath}/webstatic/css/pub.all.min.css?version=${css_ver}" rel="stylesheet" type="text/css">
<link href="${pageContextPath}/webstatic/css/varsql.db.main.min.css?version=${css_ver}" rel="stylesheet" type="text/css">

<link href="${pageContextPath}/webstatic/css/prettify/prettify.css?version=${prettify_ver}" rel="stylesheet" type="text/css">

<script src="${pageContextPath}/webstatic/i18n/<varsql:resourceLocaleName name="varsql.lang"/>.js?version=${pubjs_ver}"></script>

<script src="${pageContextPath}/webstatic/js/plugins/polyfill/polyfill.min.js"></script>

<script src="${pageContextPath}/webstatic/js/jquery-3.3.1.min.js"></script>
<script src="${pageContextPath}/webstatic/js/jquery-ui.min.js"></script>
<script src="${pageContextPath}/webstatic/js/plugins/toast/jquery.toast.min.js"></script>
<script src="${pageContextPath}/webstatic/js/plugins/layout/goldenlayout.min.js"></script>

<script src="${pageContextPath}/webstatic/js/plugins/handlebars/handlebars-v4.7.6.js"></script>
<script src="${pageContextPath}/webstatic/js/vue.min.js"></script>

<%-- web socket js --%>
<script src="${pageContextPath}/webstatic/js/plugins/websocket/sockjs/sockjs.min.js"></script>
<script src="${pageContextPath}/webstatic/js/plugins/websocket/stomp/stomp.min.js"></script>

<script src="${pageContextPath}/webstatic/js/varsql.web.custom.plugin.js?version=${pubjs_ver}"></script>
<script src="${pageContextPath}/webstatic/js/varsql.web.js?version=${pubjs_ver}"></script>
<script src="${pageContextPath}/webstatic/js/varsql.web.ui.js?version=${pubjs_ver}"></script>
<script src="${pageContextPath}/webstatic/js/varsql.db.conts.js?version=${pubjs_ver}"></script>
<script src="${pageContextPath}/webstatic/js/varsql.db.api.js?version=${pubjs_ver}"></script>
<script src="${pageContextPath}/webstatic/js/varsql.db.ui.js?version=${pubjs_ver}"></script>
<script src="${pageContextPath}/webstatic/js/varsql.db.vender.plugin.js?version=${pubjs_ver}"></script>

<%-- sql editor --%>
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

<script src="${pageContextPath}/webstatic/js/plugins/handlebars/handlebars.varsql.js?version=${pubjs_ver}"></script>
<script src="${pageContextPath}/webstatic/js/vue.varsql.js?version=${pubjs_ver}"></script>

<script>
VARSQL.unload();
</script>





