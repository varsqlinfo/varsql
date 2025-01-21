<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/include/tagLib.jspf"%>
<!doctype html>
<html>
<head>
<title>Blocking user</title>
<style>
html, body{width:100%;height:100%;overflow:hidden;}
</style>
<script>
if(top != window){
	top.location.href = location.href;
}
</script>

</head>
<body>
	<table data-init-msg="Y" style="width: 100%; height: 100%;">
		<tbody>
			<tr>
				<td style="text-align: center;">
					<div><a href="${pageContext.request.contextPath}/logout">로그인 화면 가기</a></div>
					<div class="var-load-frame" style="font-size: 3em;">
						<div><spring:message code="error.blocking.user" text="접근 금지 사용자 입니다"/></div>
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
