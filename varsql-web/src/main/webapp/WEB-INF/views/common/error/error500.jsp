<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/include/tagLib.jspf"%>
<!doctype html>
<html>
<head>
<title>System error 500</title>
<style>
html, body{width:100%;height:100%;overflow:hidden;}
</style>

</head>
<body>
	<table style="width: 100%; height: 100%;">
		<tbody>
			<tr>
				<td style="text-align: center;">
					<div class="var-load-frame" style="font-size: 3em;">
						<div>
							<spring:message code="error.default.error.page" /><br/>
						</div>
						<div>&nbsp;</div>
					</div>
				</td>
			</tr>
		</tbody>
	</table>
</body>
</html>
