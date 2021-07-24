<%@ tag language="java" pageEncoding="utf-8" body-content="empty" %><%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><%@ 
	attribute name="editorHeight" type="java.lang.String" required="false" %><%@ 
	attribute name="diffMode" type="java.lang.Boolean" required="false" %>
<link href="${pageContextPath}/webstatic/css/editor/codemirror.css?version=${codemirror_ver}" rel="stylesheet" type="text/css">
<link href="${pageContextPath}/webstatic/css/editor/show-hint.css?version=${codemirror_ver}" rel="stylesheet" type="text/css">
<link href="${pageContextPath}/webstatic/css/editor/theme/eclipse.css?version=${codemirror_ver}" rel="stylesheet" type="text/css">

<!-- sql editor -->
<script src="${pageContextPath}/webstatic/js/plugins/sqlEditor/codemirror.js?version=${codemirror_ver}"></script>
<script src="${pageContextPath}/webstatic/js/plugins/sqlEditor/sql.js?version=${codemirror_ver}"></script>
<script src="${pageContextPath}/webstatic/js/plugins/sqlEditor/show-hint.js?version=${codemirror_ver}"></script>
<script src="${pageContextPath}/webstatic/js/plugins/sqlEditor/var-sql-hint.js?version=${codemirror_ver}"></script>
<script src="${pageContextPath}/webstatic/js/plugins/sqlEditor/search/search.js?version=${codemirror_ver}"></script>
<script src="${pageContextPath}/webstatic/js/plugins/sqlEditor/search/searchcursor.js?version=${codemirror_ver}"></script>
<script src="${pageContextPath}/webstatic/js/plugins/sqlEditor/matchbrackets.js?version=${codemirror_ver}"></script>
<script src="${pageContextPath}/webstatic/js/plugins/sqlEditor/closebrackets.js?version=${codemirror_ver}"></script>

<c:if test='${diffMode}'>
<link href="${pageContextPath}/webstatic/css/editor/merge.css" rel="stylesheet" type="text/css">
<script src="${pageContextPath}/webstatic/js/plugins/diff/diff_match_patch.js"></script>
<script src="${pageContextPath}/webstatic/js/plugins/sqlEditor/merge/merge.js"></script>
</c:if>

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