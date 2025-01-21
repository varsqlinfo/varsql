<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/include/tagLib.jspf"%>
<!doctype html>
<html>
<head>
<title>Board error</title>
<style>
html, body{width:100%;height:100%;overflow:hidden;}
</style>
</head>
<body>
	<table data-init-msg="Y" style="width: 100%; height: 100%;">
		<tbody>
			<tr>
				<td style="text-align: center;">
					<div><a href="javascript:history.go(-1)"><spring:message code="back.page" text="돌아가기"/></a></div>
					<div class="var-load-frame">
						<div><spring:message code="error.500"/> </div>
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
