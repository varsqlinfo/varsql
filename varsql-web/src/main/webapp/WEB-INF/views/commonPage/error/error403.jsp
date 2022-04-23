<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!doctype html>
<html>
<head>
<title>권한이 없습니다 403</title>
<style>
html, body{width:100%;height:100%;overflow:hidden;}
</style>
<script>
function fnRefresh(){
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
					<div><a href="javascript:fnRefresh()">돌아가기</a></div>
					<div class="var-load-frame" style="font-size: 3em;">
						<div>권한이 없습니다 </div>
						<div>관리자에게 문의 하세요.</div>
						<div>&nbsp;</div>
						<div>&nbsp;</div>
					</div>
				</td>
			</tr>
		</tbody>
	</table>
</body>
</html>
