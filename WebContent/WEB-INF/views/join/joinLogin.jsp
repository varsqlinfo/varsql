<%@ page contentType="text/html; charset=utf-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/include/initvariable.jspf"%>
<div style="display:none;">
	<form name="f" action="${varsqlLoginUrl}" method="post" onsubmit="return false;">
			<input type="text" id="id" name="id" value="${userjoinid}"> 
			<input type="password" id="password" name="password" value="${userjoinpassword}">
	</form>
</div>
<script>
	document.f.submit();
</script>