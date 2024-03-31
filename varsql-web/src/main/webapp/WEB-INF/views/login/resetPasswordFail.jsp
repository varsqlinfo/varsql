<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/include/tagLib.jspf"%>

<!doctype html>
<html>
<head>
<title><spring:message code="varsql.title"/></title>
<style>
html, body{width:100%;height:100%;overflow:hidden;}
</style>
</head>
<body>
	<table data-init-msg="Y" style="width: 100%; height: 100%;">
		<tbody>
			<tr>
				<td style="text-align: center;">
					<div><a href="${pageContext.request.contextPath}/login"><spring:message code="login.page.view" text="로그인 화면 이동"/></a></div>
					<div class="var-load-frame" style="font-size: 3em;">
						<div><spring:message code="invalid.token"/></div>
						<div>&nbsp;</div>
						<div>&nbsp;</div>
					</div>
				</td>
			</tr>
		</tbody>
	</table>
</body>
</html>
