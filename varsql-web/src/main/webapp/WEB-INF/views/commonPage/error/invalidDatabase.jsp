<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!doctype html>
<html>
<head>
<title>invalid database error</title>
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
						<div>유효하지 않은 데이터 베이스 입니다.</div>
						<div>페이지를 <a href="javascript:;" onclick="fnFresh();">새로고침</a> 하세요.</div>
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