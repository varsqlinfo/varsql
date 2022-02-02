<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/include/head-meta.jspf"%>
<%@ include file="/WEB-INF/include/headInitvariable.jspf"%>
<%@ taglib prefix="varsql" uri="http://varsql.vartech.com/varsql"%>

<!-- Bootstrap Core CSS -->
<link href="${pageContextPath}/webstatic/css/bootstrap.min.css" rel="stylesheet">

<link href="${pageContextPath}/webstatic/css/jquery-ui.min.css" rel="stylesheet">

<!-- Custom Fonts -->
<link href="${pageContextPath}/webstatic/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">

<link href="${pageContextPath}/webstatic/css/jquery.toast.min.css" rel="stylesheet" type="text/css">

<link href="${pageContextPath}/webstatic/css/pub.all.min.css?version=${css_ver}" rel="stylesheet" type="text/css">
<link href="${pageContextPath}/webstatic/css/varsql.mgmt.min.css?v=${varsqlfn:staticResourceVersion('')}" rel="stylesheet">

<script src="${pageContextPath}/webstatic/js/plugins/polyfill/polyfill.min.js"></script>

<script src="${pageContextPath}/webstatic/i18n/<varsql:resourceLocaleName name="varsql.lang"/>.js?version=${pubjs_ver}"></script>
<script src="${pageContextPath}/webstatic/js/jquery-3.3.1.min.js"></script>
<script src="${pageContextPath}/webstatic/js/plugins/toast/jquery.toast.min.js"></script>
<script src="${pageContextPath}/webstatic/js/bootstrap.min.js" type="text/javascript"></script>
<script src="${pageContextPath}/webstatic/js/bootstrapValidator.js" type="text/javascript"></script>

<script src="${pageContextPath}/webstatic/js/jquery-ui.min.js"></script>
<script src="${pageContextPath}/webstatic/js/jquery.serializeJSON.js"></script>

<script src="${pageContextPath}/webstatic/js/pub.multiselect2.js"></script>

<script src="${pageContextPath}/webstatic/js/plugins/handlebars/handlebars-v4.7.6.js"></script>
<script src="${pageContextPath}/webstatic/js/vue.min.js"></script>
<script src="${pageContextPath}/webstatic/js/plugins/vue/vee-validate.min.js"></script>
<script src="${pageContextPath}/webstatic/js/varsql.web.js?version=${pubjs_ver}"></script>
<script src="${pageContextPath}/webstatic/js/varsql.web.ui.js?version=${pubjs_ver}"></script>
<script src="${pageContextPath}/webstatic/js/vue.varsql.js?version=${pubjs_ver}"></script>
<script src="${pageContextPath}/webstatic/js/varsql.web.plugin.ui.js?version=${pubjs_ver}"></script>
<script src="${pageContextPath}/webstatic/js/plugins/handlebars/handlebars.varsql.js"></script>

<script src="${pageContextPath}/webstatic/js/varsql.plugin.defaults.js?version=${pubjs_ver}"></script>
