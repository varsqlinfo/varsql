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
   <form name="resetForm" id="resetForm" class="form-horizontal well" role="form" onsubmit="return false;">
		<input type="hidden" name="token" value="<c:out value='${param.token}' />">
        <div class="form-group">
            <label class="col-sm-3 control-label"><spring:message code="join.form.password"/></label>

            <div class="col-sm-6 col-md-6">
                <input type="password" class="form-control" id="upw" name="upw" placeholder="<spring:message code="join.form.password"/>" />
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-3 control-label"><spring:message code="join.form.password.confirm"/></label>

            <div class="col-sm-6 col-md-6">
                <input type="password" class="form-control" id="confirmUpw" name="confirmUpw" placeholder="<spring:message code="join.form.password.confirm" />" />
            </div>
        </div>
    </form>
    <div class="form-group">
          <div class="col-sm-12 text-center">
              <button type="button" class="btn btn-info btn-reset"><spring:message code="btn.confirm"/></button>
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

		$('.btn-reset').on('click',function (){
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
				upw: {
	                validators: {
	                    notEmpty: {
	                        message: '필수 입력사항입니다.'
	                    }
	                    ,identical: {
	                        field: 'confirmUpw',
	                        message: '비밀번호가 같아야합니다.'
	                    }
	                    ,stringLength: {
	                        min: 4,
	                        max: 500,
	                        message: '최소 4글자 이상 이여야 합니다.'
	                    }
	                }
	            }
	            ,confirmUpw: {
	                validators: {
	                    notEmpty: {
	                        message: '필수 입력사항입니다.'
	                    }
	                    ,identical: {
	                        field: 'upw',
	                        message: '비밀번호가 같아야합니다.'
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
			url: {type:VARSQL.uri.ignore, url:'/resetPassword'},
			data:params,
			success: function(resData) {
				var item = resData.item; 
				
				if(item != 'success'){
					if(item=='token'){
						alert('유효하지 않은 토근입니다.');
					}else if(item=='password'){
						alert('패스워드를 정확히 입력해주세요.');
					}else{
						alert(item);
					}
					
					return ;
				}else{
					alert('변경되었습니다.');
					location.href='${pageContext.request.contextPath}';
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