<%@ tag language="java" pageEncoding="utf-8" body-content="empty" %><%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %><%@ 
	attribute name="resoures" type="java.lang.String" required="false" %><%@ 
	attribute name="editorHeight" type="java.lang.String" required="false" %><%@ 
	attribute name="diffMode" type="java.lang.Boolean" required="false" %><%

	String resoures = (String)jspContext.getAttribute("resoures");
	resoures = com.vartech.common.utils.StringUtils.allTrim(resoures)+",";
	jspContext.setAttribute("resouresList", java.util.Arrays.asList(resoures.split(",")));
%>
<c:if test="${fn:contains(resouresList, 'codeEditor')}">
	<%-- sql editor --%>
	<link href="${pageContextPath}/webstatic/js/plugins/sql/editor/sql.editor.min.css?v=${codeEditor_ver}" rel="stylesheet">
	<script src="${pageContextPath}/webstatic/js/plugins/sql/editor/sql.editor.js?v=${codeEditor_ver}"></script>
<style>
.sql-code-editor {
	width:100%;
    border: 1px solid #c5bbbb;
}
</style>
</c:if>

<c:if test="${fn:contains(resouresList, 'layout')}">
	<link href="${pageContextPath}/webstatic/css/plugins/layout/goldenlayout-base.css" rel="stylesheet" type="text/css">
	<link href="${pageContextPath}/webstatic/css/plugins/layout/goldenlayout-light-theme.css" rel="stylesheet" type="text/css">
	<script src="${pageContextPath}/webstatic/js/plugins/layout/goldenlayout.min.js"></script>
</c:if>
