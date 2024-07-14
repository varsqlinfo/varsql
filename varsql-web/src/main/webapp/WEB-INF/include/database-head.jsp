<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/include/head-meta.jspf"%>
<%@ include file="/WEB-INF/include/databaseInitVariable.jspf"%>
<%@ taglib prefix="varsql" uri="http://varsql.vartech.com/varsql"%>

<%-- Custom Fonts --%>
<link href="${pageContextPath}/webstatic/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
<link href="${pageContextPath}/webstatic/css/jquery.toast.min.css" rel="stylesheet" type="text/css">
<link href="${pageContextPath}/webstatic/css/plugins/layout/goldenlayout-base.css" rel="stylesheet" type="text/css">
<link href="${pageContextPath}/webstatic/css/plugins/layout/goldenlayout-light-theme.css" rel="stylesheet" type="text/css">

<link href="${pageContextPath}/webstatic/css/pub.all.min.css?v=${css_ver}" rel="stylesheet" type="text/css">
<link href="${pageContextPath}/webstatic/css/varsql.db.main.min.css?v=${css_ver}" rel="stylesheet" type="text/css">

<link href="${pageContextPath}/webstatic/css/prettify/prettify.css?v=${prettify_ver}" rel="stylesheet" type="text/css">

<script src="<varsql:messageResourceUrl/>" type="text/javascript"></script>

<script src="${pageContextPath}/webstatic/js/plugins/polyfill/polyfill.min.js"></script>
<script src="${pageContextPath}/webstatic/js/plugins/filesaver/FileSaver.min.js"></script>

<script src="${pageContextPath}/webstatic/js/jquery-3.7.1.min.js"></script>
<script src="${pageContextPath}/webstatic/js/jquery-ui.min.js"></script>
<script src="${pageContextPath}/webstatic/js/plugins/sortable/Sortable.min.js"></script>
<script src="${pageContextPath}/webstatic/js/plugins/toast/jquery.toast.min.js"></script>
<script src="${pageContextPath}/webstatic/js/plugins/layout/goldenlayout.min.js"></script>

<script src="${pageContextPath}/webstatic/js/plugins/handlebars/handlebars-v4.7.6.js"></script>
<script src="${pageContextPath}/webstatic/js/vue.min.js"></script>

<%-- web socket js --%>
<script src="${pageContextPath}/webstatic/js/plugins/websocket/sockjs/sockjs.min.js"></script>
<script src="${pageContextPath}/webstatic/js/plugins/websocket/stomp/stomp.min.js"></script>

<script src="${pageContextPath}/webstatic/js/varsql.web.js?v=${pubjs_ver}"></script>
<script src="${pageContextPath}/webstatic/js/varsql.web.ui.js?v=${pubjs_ver}"></script>
<script src="${pageContextPath}/webstatic/js/varsql.db.conts.js?v=${pubjs_ver}"></script>
<script src="${pageContextPath}/webstatic/js/varsql.db.util.js?v=${pubjs_ver}"></script>
<script src="${pageContextPath}/webstatic/js/varsql.db.api.js?v=${pubjs_ver}"></script>
<script src="${pageContextPath}/webstatic/js/varsql.db.ui.js?v=${pubjs_ver}"></script>
<script src="${pageContextPath}/webstatic/js/varsql.db.vender.plugin.js?v=${pubjs_ver}"></script>

<%-- sql editor --%>
<link href="${pageContextPath}/webstatic/js/plugins/sql/editor/sql.editor.min.css?v=${codeEditor_ver}" rel="stylesheet">
<script src="${pageContextPath}/webstatic/js/plugins/sql/editor/sql.editor.js?v=${codeEditor_ver}"></script>

<script src="${pageContextPath}/webstatic/js/plugins/prettify/prettify.js?v=${prettify_ver}"></script>
<script src="${pageContextPath}/webstatic/js/plugins/prettify/lang-sql.js?v=${prettify_ver}"></script>

<script src="${pageContextPath}/webstatic/js/plugins/sql/formatter/sql-formatter.min.js"></script>
<script src="${pageContextPath}/webstatic/js/pub.multiselect2.js?v=${pubjs_ver}"></script>
<script src="${pageContextPath}/webstatic/js/pub.splitter.js?v=${pubjs_ver}"></script>
<script src="${pageContextPath}/webstatic/js/pub.grid.js?v=${pubjs_ver}"></script>
<script src="${pageContextPath}/webstatic/js/pub.tab.js?v=${pubjs_ver}"></script>
<script src="${pageContextPath}/webstatic/js/pub.context.js?v=${pubjs_ver}"></script>
<script src="${pageContextPath}/webstatic/js/pub.autocomplete.js?v=${pubjs_ver}"></script>

<script src="${pageContextPath}/webstatic/js/plugins/handlebars/handlebars.varsql.js?v=${pubjs_ver}"></script>
<script src="${pageContextPath}/webstatic/js/vue.varsql.js?v=${pubjs_ver}"></script>

<script src="${pageContextPath}/webstatic/js/varsql.plugin.defaults.js?v=${pubjs_ver}"></script>

<script>
VARSQL.unload();
</script>





