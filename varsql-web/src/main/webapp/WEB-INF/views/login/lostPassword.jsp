<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/include/tagLib.jspf"%>
<!doctype html>
<HTML>

<head>
<title><spring:message code="page.title.varsql"/></title>
<%@ include file="/WEB-INF/include/head-meta.jspf"%>
<%@ include file="/WEB-INF/include/headInitvariable.jspf"%>

<link rel="shortcut icon" href="${pageContextPath}/webstatic/favicon.ico" type="image/x-icon">
<link rel="icon" href="${pageContextPath}/webstatic/favicon.ico" type="image/x-icon">

<link href="${pageContextPath}/webstatic/css/bootstrap.min.css" rel="stylesheet">
<link href="${pageContextPath}/webstatic/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">

<script src="${pageContextPath}/webstatic/js/jquery-3.3.1.min.js"></script>
<script src="${pageContextPath}/webstatic/js/bootstrap.min.js" type="text/javascript"></script>
<script src="${pageContextPath}/webstatic/js/bootstrapValidator.js" type="text/javascript"></script>
<script src="${pageContextPath}/webstatic/js/jquery.serializeJSON.js"></script>
<script src="${pageContextPath}/webstatic/js/varsql.web.js"></script>
<!-- Bootstrap Core CSS -->

</head>
<body>

<div class="container">
    <h3 class="page-header" style="text-align:center;"><spring:message code="page.title.varsql"/> <spring:message code="password.reset"/></h3>
    <!-- form start -->
    <form name="resetForm" id="resetForm" method="POST" action="<c:url value='/join/join' />"  class="form-horizontal well" role="form" onsubmit="return false;">

        <div class="form-group">
            <label for="inputEmail3" class="col-sm-3 control-label"><spring:message code="join.form.uid"/></label>

            <div class="col-sm-6 col-md-6">
                <input type="text" class="form-control required" id="uid" name="uid" placeholder="<spring:message code="join.form.uid" />" />
            </div>
        </div>
        
        <div class="form-group">
            <label class="col-sm-3 control-label"><spring:message code="join.form.email"/></label>

            <div class="col-sm-6 col-md-6">
                <input type="email" class="form-control" id="uemail" name="uemail" placeholder="<spring:message code="join.form.email" />"/>
            </div>
        </div>
    </form>
    <div class="form-group">
          <div class="col-sm-12 text-center">
              <button type="button" class="btn btn-info btn-join"><spring:message code="btn.confirm"/></button>
              <button type="button" class="btn btn-default btnMain"><spring:message code="label.cancel"/></button>
          </div>
      </div>
    <!--/form-->
</div>

</body>
</html>

<script>
$(function (){
var resetForm = {
	init : function (){
		var _this = this;

		_this.initEvt();
	}
	,initEvt : function (){
		var _this = this;

		$('.btnMain').click(function (e){
			location.href ='<c:url value="/" />';
		});

		$('.btn-join').on('click',function (){
			$('#resetForm').submit();
		});

		$('#resetForm').bootstrapValidator({
			message: 'This value is not valid',
			feedbackIcons: {
				valid: 'glyphicon glyphicon-ok',
				invalid: 'glyphicon glyphicon-remove',
				validating: 'glyphicon glyphicon-refresh'
			}
			,fields: {
				uid: {
					validators: {
						notEmpty: { message: '필수 입력사항입니다.'}
						,stringLength: { min: 3, max: 100, message: '사이즈는 3~100 사이여야 합니다'}
					}
			  	}
				,uemail: {
					validators: {
						notEmpty: { message: '필수 입력사항입니다.'}
						,stringLength: { min: 0, max: 500, message: '크기는 3~100 사이여야 합니다'}
						,emailAddress: {
							message: 'The input is not a valid email address'
						}
				  }
			  	}
			}
		}).on('success.form.bv', function(e) {
			// Prevent form submission
			e.preventDefault();

			_this.resetPassword();
		});
	}
	,resetPassword: function (){
		var params  =$('#resetForm').serializeJSON();

		VARSQL.req.ajax({
			url: {type:VARSQL.uri.ignore, url:'/lostPassword'},
			data:params,
			success: function(resData) {
				console.log(resData);
				if(!VARSQL.req.validationCheck(resData)){
					return ;
				}else{
					
				}

			},
			error: function(xhr, status, e) {
				VARSQL.log(status + " : " + e + xhr.responseText);
			}
		});
	}
}

resetForm.init();
}());

</script>