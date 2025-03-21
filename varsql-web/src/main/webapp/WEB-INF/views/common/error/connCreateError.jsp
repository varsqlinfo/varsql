<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/include/tagLib.jspf"%>
<!doctype html>
<html>
<head>
<title>Connection create error</title>
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
						<div><spring:message code="error.connection.create.error" text="DB connection 문제가 발생했습니다"/> </div>
						<div><spring:message code="error.default.admin" text="관리자에게 문의하세요."/></div>
						<div>&nbsp;</div>
						
						<div style="text-align: center; font-size: 0.7em"><a href="<c:url value="/board/${param.conuid}"/>" target="_blank"><spring:message code="board" /></a></div>
					</div>
				</td>
			</tr>
		</tbody>
	</table>
</body>
</html>
