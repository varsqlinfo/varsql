<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/include/tagLib.jspf"%>
<!doctype html>
<HTML>

<head>
<title><spring:message code="varsql.title"/></title>
<%@ include file="/WEB-INF/include/head-meta.jspf"%>
<%@ include file="/WEB-INF/include/headInitvariable.jspf"%>

<link rel="shortcut icon" href="${pageContextPath}/webstatic/favicon.ico" type="image/x-icon">
<link rel="icon" href="${pageContextPath}/webstatic/favicon.ico" type="image/x-icon">

<link href="${pageContextPath}/webstatic/css/bootstrap.min.css" rel="stylesheet">
<link href="${pageContextPath}/webstatic/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">

<script src="${pageContextPath}/webstatic/js/jquery-3.7.1.min.js"></script>
<script src="<varsql:messageResourceUrl/>" type="text/javascript"></script>
<script src="${pageContextPath}/webstatic/js/bootstrap.min.js" type="text/javascript"></script>
<script src="${pageContextPath}/webstatic/js/bootstrapValidator.js" type="text/javascript"></script>
<script src="${pageContextPath}/webstatic/js/jquery.serializeJSON.js"></script>
<script src="${pageContextPath}/webstatic/js/varsql.web.js"></script>
<!-- Bootstrap Core CSS -->

</head>
<body>

<div class="container">
    <h3 class="page-header"><spring:message code="varsql.title"/> <spring:message code="user.join"/></h3>
    <!-- form start -->
    <form name="joinForm" id="joinForm" method="POST" action="<c:url value='/join/join' />"  class="form-horizontal well" role="form" onsubmit="return false;">

        <div class="form-group">
            <label for="inputEmail3" class="col-sm-3 control-label"><spring:message code="user.id"/></label>

            <div class="col-sm-6 col-md-6">
                <input type="text" class="form-control required" id="uid" name="uid" placeholder="<spring:message code="user.id" />" />
            </div>
        </div>
         <div class="form-group">
            <label class="col-sm-3 control-label"><spring:message code="user.name"/></label>

            <div class="col-sm-6 col-md-6">
                <input type="text" class="form-control" id="uname" name="uname" placeholder="<spring:message code="user.name"/>"/>
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-3 control-label"><spring:message code="email"/></label>

            <div class="col-sm-6 col-md-6">
                <input type="email" class="form-control" id="uemail" name="uemail" placeholder="<spring:message code="email" />"/>
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-3 control-label"><spring:message code="user.orgnm"/></label>

            <div class="col-sm-6 col-md-6">
                <input type="text" class="form-control" id="orgNm" name="orgNm" placeholder="<spring:message code="user.orgnm"/>"/>
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-3 control-label"><spring:message code="user.password"/></label>

            <div class="col-sm-6 col-md-6">
                <input type="password" class="form-control" id="upw" name="upw" placeholder="<spring:message code="user.password"/>" />
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-3 control-label"><spring:message code="user.password.confirm"/></label>

            <div class="col-sm-6 col-md-6">
                <input type="password" class="form-control" id="confirmUpw" name="confirmUpw" placeholder="<spring:message code="user.password.confirm" />" />
            </div>
        </div>
    </form>
    <div class="form-group">
          <div class="col-sm-12 text-center">
              <button type="button" class="btn btn-info btn-join"><spring:message code="join"/></button>
              <button type="button" class="btn btn-default btnMain"><spring:message code="screen.login"/></button>
          </div>
      </div>

    <div style="display:none;">
		<form name="f" action="${varsqlfn:loginUrl(pageContext.request)}" method="post" onsubmit="return false;">
			<input type="text" id="vsql_login_id" name="vsql_login_id" value="">
			<input type="password" id="vsql_login_password" name="vsql_login_password" value="">
		</form>
	</div>
    <!--/form-->
</div>

</body>
</html>

<script>
$(function (){
var joinForm = {
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
			$('#joinForm').submit();
		});

		var idChecVal = -1;
		$('#uid').focusout(function(e) {
			
			var tmpVal =  $.trim($(this).val());

			$(this).val(tmpVal);

			VARSQL.req.ajax({
				url: {type:VARSQL.uri.join, url:'/idCheck'},
				data:{
					uid : $.trim(tmpVal)
				},
				success: function(resData) {
					if(resData.item  > 0){
						idChecVal = 1;
					}else{
						idChecVal = 0;
					}
					$('#joinForm')
				    .data('bootstrapValidator')
				    .updateStatus('uid', 'VALIDATING')
				    .validateField('uid');
				}
			});
		});

		var emailChecVal = -1;

		$('#uemail').focusout(function(e) {

			var tmpVal =  $.trim($(this).val());

			$(this).val(tmpVal);

			VARSQL.req.ajax({
				url: {type:VARSQL.uri.join, url:'/emailCheck'},
				data:{
					uemail : $.trim(tmpVal)
				},
				success: function(resData) {
					if(resData.item  > 0){
						emailChecVal = 1;
					}else{
						emailChecVal = 0;
					}
					$('#joinForm')
				    .data('bootstrapValidator')
				    .updateStatus('uemail', 'VALIDATING')
				    .validateField('uemail');
				}
			});
		});

		$('#joinForm').bootstrapValidator({
			message: 'This value is not valid',
			feedbackIcons: {
				valid: 'glyphicon glyphicon-ok',
				invalid: 'glyphicon glyphicon-remove',
				validating: 'glyphicon glyphicon-refresh'
			}
			,fields: {
				uid: {
					validators: {
						notEmpty: { message: VARSQL.message('msg.valid.required','필수 입력사항입니다.')}
						,stringLength: { min: 3, max: 100, message: VARSQL.message('msg.valid.size.param',{size:'3~100'})}
						,callback: {
		                     message: '이미 존재하는 아이디 입니다.',
		                     callback: function (value, validator, $field) {
		                   	  	return idChecVal < 1;
		                     }
		                }
					}
			  	}
				,uname: {
					validators: {
						notEmpty: { message: VARSQL.message('msg.valid.required','필수 입력사항입니다.')}
						,callback: {
		                     message: VARSQL.message('msg.valid.size.param',{size:'2~100'}),
		                     callback: function (value, validator, $field) {
		                   	  	return $.trim(value).length > 1;
		                     }
		                 }
					}
			  	}
				,uemail: {
					validators: {
						notEmpty: { message: VARSQL.message('msg.valid.required','필수 입력사항입니다.')}
						,stringLength: { min: 0, max: 250, message: VARSQL.message('msg.valid.size.param',{size:'3~100'})}
						,emailAddress: {
							message: 'The input is not a valid email address'
						}
						,callback: {
		                     message: VARSQL.message('msg.valid.duplicated.param', VARSQL.message('email')),
		                     callback: function (value, validator, $field) {
		                   	  	return emailChecVal < 1;
		                     }
		                }
				  }
			  	}
				,upw: {
	                validators: {
	                    notEmpty: {
	                        message: VARSQL.message('msg.valid.required','필수 입력사항입니다.')
	                    }
	                    ,different: {
	                        field: 'uid',
	                        message: VARSQL.message('msg.valid.id.diffrent','아이디와 달라야 합니다.')
	                    }
	                    ,identical: {
	                        field: 'confirmUpw',
	                        message: VARSQL.message('msg.valid.password.identical','비밀번호가 같아야합니다.')
	                    }
	                    ,stringLength: {
	                        min: 4,
	                        max: 200,
	                        message: VARSQL.message('msg.valid.min.size.param', {size : 4})
	                    }
	                }
	            }
	            ,confirmUpw: {
	                validators: {
	                    notEmpty: {
	                        message: VARSQL.message('msg.valid.required','필수 입력사항입니다.')
	                    }
	                    ,identical: {
	                        field: 'upw',
	                        message: VARSQL.message('msg.valid.password.identical','비밀번호가 같아야합니다.')
	                    }
	                }
	            }

			}
		}).on('success.form.bv', function(e) {
			// Prevent form submission
			e.preventDefault();

			_this.saveInfo();
		});
	}
	,saveInfo: function (){
		var params  =$('#joinForm').serializeJSON();

		VARSQL.req.ajax({
			url: {type:VARSQL.uri.join, url:'/save'},
			data:params,
			success: function(resData) {
				if(!VARSQL.req.validationCheck(resData)){
					return ;
				}else{
					if(resData.code ==409){
						VARSQL.alertMessage('msg.valid.duplicated.param', VARSQL.message('id'));
						return ;
					}
				}
				$('#vsql_login_id').val( $('#uid').val());
				$('#vsql_login_password').val($('#upw').val());

				document.f.submit();
			},
			error: function(xhr, status, e) {
				VARSQL.log(status + " : " + e + xhr.responseText);
			}
		});
	}
}

joinForm.init();
}());

</script>
