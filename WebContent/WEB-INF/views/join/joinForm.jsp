<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/include/tagLib.jspf"%>
<%@ page import=" java.util.*, java.io.*" %>
<!doctype html>
<HTML>
<%@ include file="/WEB-INF/include/initvariable.jspf"%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><spring:message code="page.title.varsql"/></title>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="">
<meta name="author" content="">
<link rel="shortcut icon" href="${pageContextPath}/webstatic/favicon.ico" type="image/x-icon">
<link rel="icon" href="${pageContextPath}/webstatic/favicon.ico" type="image/x-icon">

<link href="${pageContextPath}/webstatic/css/bootstrap.min.css" rel="stylesheet">
<link href="${pageContextPath}/webstatic/font-awesome-4.1.0/css/font-awesome.min.css" rel="stylesheet" type="text/css">
<link href="${pageContextPath}/webstatic/css/varsql.main.css" rel="stylesheet" type="text/css">


<script src="${pageContextPath}/webstatic/js/jquery-1.10.2.min.js"></script>
<script src="${pageContextPath}/webstatic/js/bootstrap.min.js" type="text/javascript"></script>
<script src="${pageContextPath}/webstatic/js/bootstrapValidator.js" type="text/javascript"></script>
<script src="${pageContextPath}/webstatic/js/jquery.serializeJSON.js"></script>
<script src="${pageContextPath}/webstatic/js/varsql.web.js"></script>
<!-- Bootstrap Core CSS -->

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
    <form name="joinForm" id="joinForm" method="POST" action="<c:url value='/join/join' />"  class="form-horizontal well" role="form">

        <div class="form-group">
            <label for="inputEmail3" class="col-sm-3 control-label"><spring:message code="join.form.uid"/></label>

            <div class="col-sm-6 col-md-6">
                <input type="text" class="form-control required" id="uid" name="uid" placeholder="<spring:message code="join.form.uid" />" />
            </div>
        </div>
         <div class="form-group">
            <label class="col-sm-3 control-label"><spring:message code="join.form.name"/></label>

            <div class="col-sm-6 col-md-6">
                <input type="text" class="form-control" id="uname" name="uname" placeholder="<spring:message code="join.form.name"/>"/>
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-3 control-label"><spring:message code="join.form.email"/></label>

            <div class="col-sm-6 col-md-6">
                <input type="email" class="form-control" id="uemail" name="uemail" placeholder="<spring:message code="join.form.email" />"/>
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-3 control-label"><spring:message code="join.form.dept"/></label>

            <div class="col-sm-6 col-md-6">
                <input type="text" class="form-control" id="udept" name="udept" placeholder="<spring:message code="join.form.dept"/>"/>
            </div>
        </div>
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

        <div class="form-group">
            <div class="col-sm-12 text-center">
                <button type="submit" class="btn btn-info"><spring:message code="btn.join"/></button>
                <button type="button" class="btn btn-default btnMain"><spring:message code="btn.main"/></button>
            </div>
        </div>
    </form>
    
    <div style="display:none;">
		<form name="f" action="${varsqlLoginUrl}" method="post">
				<input type="text" id="id" name="id" value=""> 
				<input type="password" id="password" name="password" value="">
		</form>
	</div>
    <!--/form-->
</div>

</body>
</html>

<script>
var joinForm = {
	init : function (){
		var _this = this; 
		
		_this.initEvt();
	}
	,initEvt : function (){
		var _this = this;
	
		$('.btnMain').click(function (){
			location.href ='<c:url value="/" />';
		});
		
		var idChecVal = -1; 
		$('#uid').focusout(function(e) {
			
			var tmpVal = $(this).val(); 
			
			VARSQL.req.ajax({
				url: {gubun:VARSQL.uri.join, url:'/idCheck'},
				data:{
					uid : tmpVal
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
				},
				error: function(xhr, status, e) {
					GAINEP.log(status + " : " + e + xhr.responseText);
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
						notEmpty: { message: '필수 입력사항입니다.'}
						,stringLength: { min: 3, max: 100, message: '사이즈는 3~100 사이여야 합니다'}
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
						notEmpty: { message: '필수 입력사항입니다.'}
						,stringLength: { min: 3, max: 100, message: '크기는 3~100 사이여야 합니다'}
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
				,upw: {
	                validators: {
	                    notEmpty: {
	                        message: '필수 입력사항입니다.'
	                    }
	                    ,different: {
	                        field: 'uid',
	                        message: '아이디와 달라야 합니다.'
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
			
			_this.saveInfo();
		});
	}
	,saveInfo: function (){
		var params  =$('#joinForm').serializeJSON();
		
		VARSQL.req.ajax({
			url: {gubun:VARSQL.uri.join, url:'/save'},
			cache: false,
			type:"post",
			data:params,
			dataType: "json",
			success: function(resData) {
				if(resData.messageCode=='valid'){
					var items = resData.items;
					objLen = items.length;
					if(objLen >0){
						var item;
						for(var i=0; i <objLen; i++){
							item = items[i];
							alert(item.field + "\n"+ item.defaultMessage)
							return ; 
						}
					}
				}else{
					if(resData.code ==409){
						alert('아이디  중복 입니다.');
						return ;
					}
				}
				$('#id').val( $('#uid').val());
				$('#password').val($('#upw').val());
				
				document.f.submit();
			},
			error: function(xhr, status, e) {
				GAINEP.log(status + " : " + e + xhr.responseText);
			}
		});
	}
}

$(document).ready(function (){
	joinForm.init();
});
</script>	