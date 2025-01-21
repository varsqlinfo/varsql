<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/include/tagLib.jspf"%>
<!doctype html>
<html>
<head>
<title>Database error</title>
<style>
html, body{width:100%;height:100%;overflow:hidden;}
</style>
</head>
<body>
	<table data-init-msg="Y" style="width: 100%; height: 100%;">
		<tbody>
			<tr>
				<td style="text-align: center; font-size: 3em;">
					<div class="var-load-frame">
						<div><spring:message code="error.blocking.database" text="차단된 데이터 베이스 입니다."/></div>
						<div><spring:message code="error.default.admin" text="관리자에게 문의하세요."/></div>
						<div>&nbsp;</div>
						<div>&nbsp;</div>
					</div>
				</td>
			</tr>
		</tbody>
	</table>
</body>
</html>
<script>
function fnFresh(){
	(top != window ? top :window).location.href='${pageContext.request.contextPath}/user';
}
</script>