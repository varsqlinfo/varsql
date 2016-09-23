<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/include/initvariable.jspf"%>
<%@ page import=" java.util.*, java.io.*" %>
<!doctype html>
<HTML>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><spring:message code="page.title.varsql"/></title>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="">
<meta name="author" content="">

<script src="${pageContextPath}/webstatic/js/jquery-1.10.2.min.js"></script>
<script src="${pageContextPath}/webstatic/js/bootstrap.min.js" type="text/javascript"></script>
<script src="${pageContextPath}/webstatic/js/bootstrapValidator.js" type="text/javascript"></script>

<!-- Bootstrap Core CSS -->
<link href="${pageContextPath}/webstatic/css/bootstrap.min.css" rel="stylesheet">

<link href="${pageContextPath}/webstatic/font-awesome-4.1.0/css/font-awesome.min.css" rel="stylesheet" type="text/css">

<link href="${pageContextPath}/webstatic/css/varsql.main.css" rel="stylesheet" type="text/css">

<script>
$(document).ready(function (){
	fnInit();
});

function fnInit(){
	
	$('.btnMain').click(function (){
		location.href ='<c:url value="/" />';
	});
	
	$('#joinForm').bootstrapValidator({
		message: 'This value is not valid',
		feedbackIcons: {
			valid: 'glyphicon glyphicon-ok',
			invalid: 'glyphicon glyphicon-remove',
			validating: 'glyphicon glyphicon-refresh'
		}
	});
}
</script>	
</head>
<body>


<header class="navbar navbar-default">
    <div class="container">
        <div class="navbar-header">
            <a class="navbar-brand" href="#"><spring:message code="page.title.varsql"/></a>
        </div>
    </div>
</header>
<!--// header -->

<div class="container">
    <h3 class="page-header"><spring:message code="join.form.title"/></h3>
    <!-- form start -->
    <form name="joinForm" id="joinForm" method="POST" action="<c:url value='/join/join.do' />"  class="form-horizontal well" role="form">

        <div class="form-group">
            <label for="inputEmail3" class="col-sm-3 control-label"><spring:message code="join.form.email"/></label>

            <div class="col-sm-6 col-md-6">
                <input type="email" class="form-control" id="uid" name="uid" placeholder="<spring:message code="join.form.email" />" required data-bv-notempty-message="<spring:message code="msg.email.empty" />"/>
            </div>
        </div>
        <div class="form-group">
            <label for="nickname" class="col-sm-3 control-label"><spring:message code="join.form.name"/></label>

            <div class="col-sm-6 col-md-6">
                <input type="text" class="form-control" id="uname" name="uname" placeholder="<spring:message code="join.form.name"/>" required data-bv-notempty-message="<spring:message code="msg.name.empty" />"/>
            </div>
        </div>
        <div class="form-group">
            <label for="nickname" class="col-sm-3 control-label"><spring:message code="join.form.dept"/></label>

            <div class="col-sm-6 col-md-6">
                <input type="text" class="form-control" id="udept" name="udept" placeholder="<spring:message code="join.form.dept"/>"/>
            </div>
        </div>
        <div class="form-group">
            <label for="inputPassword1" class="col-sm-3 control-label"><spring:message code="join.form.password"/></label>

            <div class="col-sm-6 col-md-6">
                <input type="password" class="form-control" id="upw" name="upw" placeholder="<spring:message code="join.form.password"/>" 
                required data-bv-notempty-message="<spring:message code="msg.password.empty" />"
                data-bv-stringlength="true" data-bv-stringlength-min="6"
                data-bv-stringlength-message="<spring:message code="msg.password.length.check"/>" />
            </div>
        </div>
        <div class="form-group">
            <label for="inputPassword2" class="col-sm-3 control-label"><spring:message code="join.form.password.confirm"/></label>

            <div class="col-sm-6 col-md-6">
                <input type="password" class="form-control" id="upw2" name="upw2" placeholder="<spring:message code="join.form.password.confirm" />" 
                data-bv-identical="true" data-bv-identical-field="upw" required data-bv-notempty-message="<spring:message code="msg.password.empty"/>"
                data-bv-identical-message="<spring:message code="msg.password.differrent"/>" />
            </div>
        </div>

        <div class="form-group">
            <div class="col-sm-12 text-center">
                <button type="submit" class="btn btn-info"><spring:message code="btn.join"/></button>
                <button type="button" class="btn btn-default btnMain"><spring:message code="btn.main"/></button>
            </div>
        </div>
    </form>
    <!--/form-->
</div>

</body>
</html>
