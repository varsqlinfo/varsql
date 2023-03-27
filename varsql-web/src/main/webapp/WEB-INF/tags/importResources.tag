<%@ tag language="java" pageEncoding="utf-8" body-content="empty" %><%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %><%@ 
	attribute name="resoures" type="java.lang.String" required="false" %><%@ 
	attribute name="editorHeight" type="java.lang.String" required="false" %><%@ 
	attribute name="diffMode" type="java.lang.Boolean" required="false" %><%

	String resoures = (String)jspContext.getAttribute("resoures");
	resoures = com.vartech.common.utils.StringUtils.allTrim(resoures)+",";
	jspContext.setAttribute("resouresList", java.util.Arrays.asList(resoures.split(",")));
%>
<c:if test="${fn:contains(resouresList, 'codeEditor')}">
	<link href="${pageContextPath}/webstatic/css/editor/codemirror.css?version=${codemirror_ver}" rel="stylesheet" type="text/css">
	<link href="${pageContextPath}/webstatic/css/editor/show-hint.css?version=${codemirror_ver}" rel="stylesheet" type="text/css">
	<link href="${pageContextPath}/webstatic/css/editor/theme/eclipse.css?version=${codemirror_ver}" rel="stylesheet" type="text/css">
	
	<!-- sql editor -->
	<script src="${pageContextPath}/webstatic/js/plugins/sql/editor/codemirror.js?version=${codemirror_ver}"></script>
	<script src="${pageContextPath}/webstatic/js/plugins/sql/editor/sql.js?version=${codemirror_ver}"></script>
	<script src="${pageContextPath}/webstatic/js/plugins/sql/editor/show-hint.js?version=${codemirror_ver}"></script>
	<script src="${pageContextPath}/webstatic/js/plugins/sql/editor/var-sql-hint.js?version=${codemirror_ver}"></script>
	<script src="${pageContextPath}/webstatic/js/plugins/sql/editor/search/search.js?version=${codemirror_ver}"></script>
	<script src="${pageContextPath}/webstatic/js/plugins/sql/editor/search/searchcursor.js?version=${codemirror_ver}"></script>
	<script src="${pageContextPath}/webstatic/js/plugins/sql/editor/matchbrackets.js?version=${codemirror_ver}"></script>
	<script src="${pageContextPath}/webstatic/js/plugins/sql/editor/closebrackets.js?version=${codemirror_ver}"></script>
	<!-- diff -->
	<link href="${pageContextPath}/webstatic/css/editor/merge.css" rel="stylesheet" type="text/css">
	<script src="${pageContextPath}/webstatic/js/plugins/diff/diff_match_patch.js"></script>
	<script src="${pageContextPath}/webstatic/js/plugins/sql/editor/merge/merge.js"></script>
	<style>
	<c:if test='${editorHeight != null and editorHeight ne ""}'>
	.CodeMirror{
		border: 1px solid #d4d0d0;
	    height: ${editorHeight};
	}
	</c:if>
	
	<c:if test='${diffMode}'>
	.CodeMirror-merge, .CodeMirror-merge .CodeMirror {
	    height: ${editorHeight};
	}
	
	.CodeMirror-merge .CodeMirror-merge-pane {
	    height: ${editorHeight};
	}
	</c:if>
	</style>
</c:if>

<c:if test="${fn:contains(resouresList, 'layout')}">
	<link href="${pageContextPath}/webstatic/css/plugins/layout/goldenlayout-base.css" rel="stylesheet" type="text/css">
	<link href="${pageContextPath}/webstatic/css/plugins/layout/goldenlayout-light-theme.css" rel="stylesheet" type="text/css">
	<script src="${pageContextPath}/webstatic/js/plugins/layout/goldenlayout.min.js"></script>
</c:if>
