<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/include/tagLib.jspf"%>
<!doctype html>
<html>
<head>
<title>System error 404</title>
<style>
html, body{width:100%;height:100%;overflow:hidden;}
</style>
<script>
function fnBack(){
	if(document.referrer ==''){
		location.href= '${pageContext.request.contextPath}';
	}else{
		history.go(-1);
	}
}
</script>
</head>
<body>
	<table data-init-msg="Y" style="width: 100%; height: 100%;">
		<tbody>
			<tr>
				<td style="text-align: center;">
					<div class="var-load-frame" style="font-size: 3em;">
						<div>
							<spring:message code="error.404" /><br/>
						</div>
						<div>&nbsp;</div>
					</div>
					
					<div><a href="javascript:fnBack()"><spring:message code="back.page" /></a></div>
					<div><a href="${pageContext.request.contextPath}"><spring:message code="main.page" /></a></div>
				</td>
			</tr>
		</tbody>
	</table>
</body>
</html>
