<%@ page contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/include/tagLib.jspf"%>
<!DOCTYPE html>
<HTML>
<head>
<%@ include file="/WEB-INF/include/head-meta.jspf"%>
<title><spring:message code="varsql.title"/></title>
<%@ include file="/WEB-INF/include/headInitvariable.jspf"%>
<link rel="shortcut icon" href="${pageContextPath}/webstatic/favicon.ico" type="image/x-icon">
<link rel="icon" href="${pageContextPath}/webstatic/favicon.ico" type="image/x-icon">
<script src="${pageContextPath}/webstatic/js/jquery-3.3.1.min.js"></script>
<script src="<varsql:messageResourceUrl/>" type="text/javascript"></script>
<script src="${pageContextPath}/webstatic/js/bootstrap.min.js" type="text/javascript"></script>
<script src="${pageContextPath}/webstatic/js/bootstrapValidator.js" type="text/javascript"></script>
<script src="${pageContextPath}/webstatic/js/varsql.web.js"></script>

<!-- Bootstrap Core CSS -->
<link href="${pageContextPath}/webstatic/css/bootstrap.min.css" rel="stylesheet">

<link href="${pageContextPath}/webstatic/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">

<link href="${pageContextPath}/webstatic/css/varsql.common.min.css" rel="stylesheet" type="text/css">
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
	
	$('#vsql_login_id,#vsql_login_password').on('keypress' , function (e){
		var keyCode = 0;
		var shiftKey=false;
		keyCode=e.keyCode;
		shiftKey=e.shiftKey;

		if (((keyCode >= 65 && keyCode <= 90)&& !shiftKey)||((keyCode >= 97 && keyCode <= 122)&& shiftKey)){
			$('.error-msg').empty().html(VARSQL.message('msg.capsLock', 'CapsLock이 켜져 있습니다'));
		}else{
			$('.error-msg').empty().html("");
		}
	})

	if(localStorage.getItem('varsqlLoginID') && localStorage.getItem('varsqlLoginID') !=''){
		$('#varsqlRememberMe').prop('checked',true);
		$('#vsql_login_id').val(localStorage.getItem('varsqlLoginID'));
	}

	$('#vsql_login_password').keydown(function(event) {
		if(event.keyCode =='13'){
			$('.btn-login').trigger('click');
		}
	});

	$('.btn-login').on('click', function (){
		var loginID = $.trim($('#vsql_login_id').val());

		$('#vsql_login_id').val(loginID);
		$('#vsql_login_password').val($.trim($('#vsql_login_password').val()))

		if($('#varsqlRememberMe').is(':checked')){
			localStorage.setItem('varsqlLoginID', loginID);
		}else{
			localStorage.removeItem('varsqlLoginID');
		}
		document.f.submit();
	});
	
	$('#lang').on('change',function (){
		var lang = $(this).val();
		var localeParam = '';
		if(!VARSQL.isBlank(lang)){
			localeParam = '?locale='+lang;
		}
		location.href=VARSQL.getContextPathUrl()+"/login"+localeParam;
	})
})
</script>
</head>
<body>
	<div class="container">
		<form name="f" action="${varsqlfn:loginUrl(pageContext.request)}" method="post"
			class="form-signin" role="form" onsubmit="return false;">
			<h2 class="form-signin-heading" style="text-align:center;"><spring:message code="sign.in.header" /></h2>
			<sec:csrfInput/>

			<input class="form-control" id="vsql_login_id" name="vsql_login_id" type="text" placeholder="<spring:message code="user.id"/>" style="margin-bottom:5px;"	autofocus autocomplete="off">
			<input class="form-control" id="vsql_login_password" name="vsql_login_password" type="password" placeholder="<spring:message code="user.password"/>" value="">
			<div class="checkbox" style="margin-bottom:13px;">
				<label style="padding-top:5px;">
					<input type="checkbox" id="varsqlRememberMe" name="varsqlRememberMe" value="on"> Remember me
				</label>
				<div class="pull-right">
					<varsql:supportLocale var="localeInfo"/>
					<c:set var="requestLocaleCode" value="${varsqlfn:requestLocaleCode(pageContext.request) }"/>
					<select class="input-sm" id="lang" name="lang" style="padding:3px;" >
	            		<option value=""><spring:message code="language.select" text="언어선택"/></option>
	            		<c:forEach var="item" items="${localeInfo}" begin="0" varStatus="status">
							<option value="${item.localeCode}" ${item.localeCode == requestLocaleCode ? 'selected="selected"' : '' }><spring:message code="${item.i18n}"/></option>
						</c:forEach>
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
				<a href="./join/" class="btn btn-block btn-success"><spring:message code="btn.signup" /></a>
				
				<c:if test="${varsqlfn:isPasswordResetModeEmail()}">
					<span style="float:right;margin-top:10px;">
						<a href="<c:url value="/lostPassword" />"><spring:message code="msg.password.lost" /></a>
					</span>
				</c:if>
			</div>
		</form>
	</div>
	<!-- /container -->


</body>

</html>
