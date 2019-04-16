<%@ page contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/include/tagLib.jspf"%>
<!DOCTYPE html>
<HTML>
<head>
<%@ include file="/WEB-INF/include/head-meta.jspf"%>
<title><spring:message code="page.title.varsql"/></title>
<%@ include file="/WEB-INF/include/initvariable.jspf"%>
<link rel="shortcut icon" href="${pageContextPath}/webstatic/favicon.ico" type="image/x-icon">
<link rel="icon" href="${pageContextPath}/webstatic/favicon.ico" type="image/x-icon">
<script src="${pageContextPath}/webstatic/js/jquery-1.10.2.min.js"></script>
<script src="${pageContextPath}/webstatic/js/bootstrap.min.js" type="text/javascript"></script>
<script src="${pageContextPath}/webstatic/js/bootstrapValidator.js" type="text/javascript"></script>

<!-- Bootstrap Core CSS -->
<link href="${pageContextPath}/webstatic/css/bootstrap.min.css" rel="stylesheet">

<link href="${pageContextPath}/webstatic/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">

<link href="${pageContextPath}/webstatic/css/varsql.common.css" rel="stylesheet" type="text/css">
<style>
body {
  padding-top: 40px;
  padding-bottom: 40px;
  background-color: #eee;
}

.form-signin {
  max-width: 330px;
  padding: 15px;
  margin: 0 auto;
}
.form-signin .form-signin-heading,
.form-signin .checkbox {
  margin-bottom: 10px;
}
.form-signin .checkbox {
  font-weight: normal;
}
.form-signin .form-control {
  position: relative;
  height: auto;
  -webkit-box-sizing: border-box;
     -moz-box-sizing: border-box;
          box-sizing: border-box;
  padding: 10px;
  font-size: 16px;
}
.form-signin .form-control:focus {
  z-index: 2;
}
.form-signin input[type="email"] {
  margin-bottom: -1px;
  border-bottom-right-radius: 0;
  border-bottom-left-radius: 0;
}
.form-signin input[type="password"] {
  margin-bottom: 10px;
  border-top-left-radius: 0;
  border-top-right-radius: 0;
}
.error-msg{
	color: #f5172c;
}
</style>
<script>
if(top != window){
	top.location.href = location.href; 
}

$(document).ready(function (){
	
	$('#id,#password').on('keypress' , function (e){
		var keyCode = 0;
		var shiftKey=false;
		keyCode=e.keyCode;
		shiftKey=e.shiftKey;
		
		if (((keyCode >= 65 && keyCode <= 90)&& !shiftKey)||((keyCode >= 97 && keyCode <= 122)&& shiftKey)){
			$('.error-msg').empty().html("CapsLock이 켜져 있습니다");
		}else{
			$('.error-msg').empty().html("");
		}
	})
	 
	if(localStorage.getItem('varsqlLoginID') && localStorage.getItem('varsqlLoginID') !=''){
		$('#rememberMe').prop('checked',true);
		$('#id').val(localStorage.getItem('varsqlLoginID'));
	}
	
	$('#password').keydown(function(event) {
		if(event.keyCode =='13'){
			$('.btn-login').trigger('click');
		}
	});
	
	$('.btn-login').on('click', function (){
		var loginID = $.trim($('#id').val()); 
	
		$('#id').val(loginID);
		$('#password').val($.trim($('#password').val()))
		
		if($('#rememberMe').is(':checked')){
			localStorage.setItem('varsqlLoginID', loginID);
		}else{
			localStorage.removeItem('varsqlLoginID');
		}
		document.f.submit();
	});
})
</script>
</head>
<body>
	<div class="container">
		<form name="f" action="${varsqlLoginUrl}" method="post"
			class="form-signin" role="form" onsubmit="return false;">
			<h2 class="form-signin-heading"><spring:message code="msg.please.sign.in" /></h2>
			<sec:csrfInput/>
			
			<input class="form-control" id="id" name="id" type="text" placeholder="<spring:message code="login.form.id"/>" style="margin-bottom:5px;"	autofocus autocomplete="off"> 
			<input class="form-control" id="password" name="password" type="password" placeholder="<spring:message code="login.form.pw"/>" value="">
			<div class="checkbox" style="margin-bottom:13px;">
				<label style="padding-top:5px;">
					<input type="checkbox" id="rememberMe" value="remember-me"> Remember me
				</label>
				<div class="pull-right">
					<select name="lang" style="padding:3px;">
						<option value="">언어 선택</option>
						<option value="ko">한국어</option>
						<option value="en">English</option>
					</select>
				</div>
			</div>
			<div class="error">
				<c:if test="${param.mode eq 'fail'}">
					<p><spring:message code="msg.login.fail" /><p>
				</c:if>
				<div class="error-msg"></div>
			</div>
			<div style="padding-bottom:10px;">
				<button class="btn btn-lg btn-primary btn-block btn-login" type="button">
					<spring:message code="btn.login" />
				</button>
			</div>
			<div>
				<a href="./join/" class=""><button class="btn  btn-block btn-success" type="button">
					<spring:message code="btn.signup" />
				</button></a>
			</div>
		</form>
	</div>
	<!-- /container -->


</body>

</html>
