<!DOCTYPE html>
<%@ page contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/include/initvariable.jspf"%>
<%@ page import=" java.util.*, java.io.*" %>

<HTML>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><spring:message code="page.title.varsql"/></title>
<head>
<script src="${pageContextPath}/webstatic/js/jquery-1.10.2.min.js"></script>
<script src="${pageContextPath}/webstatic/js/bootstrap.min.js" type="text/javascript"></script>
<script src="${pageContextPath}/webstatic/js/bootstrapValidator.js" type="text/javascript"></script>

<!-- Bootstrap Core CSS -->
<link href="${pageContextPath}/webstatic/css/bootstrap.min.css" rel="stylesheet">

<link href="${pageContextPath}/webstatic/font-awesome-4.1.0/css/font-awesome.min.css" rel="stylesheet" type="text/css">

<link href="${pageContextPath}/webstatic/css/varsql.main.css" rel="stylesheet" type="text/css">
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


</style>
</head>
<body>
	<div class="container">
		<form name="f" action="${varsqlLoginUrl}" method="post"
			class="form-signin" role="form">
			<h2 class="form-signin-heading"><spring:message code="msg.please.sign.in" /></h2>
			<div>
				<button type="button" class="btn btn-default"
					onclick="$('#id').val('varsqladmin');$('#password').val('varsqladmin');">Admin</button>
				<button type="button" class="btn btn-default"
					onclick="$('#id').val('manage');$('#password').val('manage');">Manage</button>
				<button type="button" class="btn btn-default"
					onclick="$('#id').val('abcde10@naver.com');$('#password').val('asdfas');">User</button>
			</div>
			<input class="form-control" id="id" name="id" value="manage"
				type="text" placeholder="<spring:message code="login.form.id"/>"
				autofocus> <input class="form-control" id="password"
				name="password" value="manage" type="password"
				placeholder="<spring:message code="login.form.pw"/>" value="">
			<div class="checkbox">
				<label> <input type="checkbox" value="remember-me">
					Remember me
				</label>
			</div>
			<c:if test="${login=='fail'}">
				<div class="error">
					<p>
						<spring:message code="msg.login.fail" />
					<p>
				</div>
			</c:if>
			<button class="btn btn-lg btn-primary btn-block" type="submit">
				<spring:message code="btn.login" />
			</button>
			<div class="text-center panel-footer">
				<a href="javascript:;" class="">아이디찾기</a>
				<a href="javascript:;" class="">비밀번호찾기</a>
				<a href="./join/" class="">회원가입</a>
			</div>
		</form>
	</div>
	<!-- /container -->


</body>

</html>
