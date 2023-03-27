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

<script src="<varsql:messageResourceUrl/>" type="text/javascript"></script>
<script src="${pageContextPath}/webstatic/js/jquery-3.3.1.min.js"></script>
<script src="${pageContextPath}/webstatic/js/bootstrap.min.js" type="text/javascript"></script>
<script src="${pageContextPath}/webstatic/js/bootstrapValidator.js" type="text/javascript"></script>
<script src="${pageContextPath}/webstatic/js/jquery.serializeJSON.js"></script>
<script src="${pageContextPath}/webstatic/js/varsql.web.js"></script>
<script src="${pageContextPath}/webstatic/js/vue.min.js"></script>
<script src="${pageContextPath}/webstatic/js/vue.varsql.js?version=${pubjs_ver}"></script>

</head>
<body>

<div id="vueArea" class="container">
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
              <button type="button" @click="submit()" class="btn btn-info btn-reset"><spring:message code="btn.confirm"/></button>
              <button type="button" @click="goCancel()" class="btn btn-default btnMain"><spring:message code="label.cancel"/></button>
          </div>
      </div>
    <!--/form-->
</div>

</body>
</html>

<script>

VarsqlAPP.vueServiceBean({
	el: '#vueArea'
	,data: {
		msgView : ''
	}
	,methods:{
		init : function (){
			var _this =this;
			
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
		                        message: VARSQL.messageFormat('varsql.0042','필수 입력사항입니다.')
		                    }
		                    ,identical: {
		                        field: 'confirmUpw',
		                        message: VARSQL.messageFormat('varsql.0046','비밀번호가 같아야합니다.')
		                    }
		                    ,stringLength: {
		                        min: 4,
		                        max: 200,
		                        message: VARSQL.messageFormat('varsql.0047', {size : 4})
		                    }
		                }
		            }
		            ,confirmUpw: {
		                validators: {
		                    notEmpty: {
		                        message: VARSQL.messageFormat('varsql.0042','필수 입력사항입니다.')
		                    }
		                    ,identical: {
		                        field: 'upw',
		                        message: VARSQL.messageFormat('varsql.0046','비밀번호가 같아야합니다.')
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
		,submit : function (){
			$('#resetForm').submit();
		}
		,goCancel : function (){
			location.href ='<c:url value="/" />';
		}
		,resetPassword: function (){
			var _this = this; 
			var params  =$('#resetForm').serializeJSON();
			
			this.$ajax({
				url: {type:VARSQL.uri.ignore, url:'/resetPassword'},
				data:params,
				success: function(resData) {
					
					 var item = resData.item; 
	                    
                    if(item != 'success'){
                        if(item=='token'){
                            alert(VARSQL.messageFormat('varsql.0052','유효하지 않은 토근입니다.'));
                        }else if(item=='password'){
                            alert(VARSQL.messageFormat('varsql.0053', '패스워드를 정확히 입력해주세요.'));
                        }else{
                            alert(item);
                        }
                        
                        return ;
                    }else{
                        alert(VARSQL.messageFormat('varsql.0054', '변경되었습니다.'));
                        location.href='<c:url value="/" />';
                    }
				},
				error: function(xhr, status, e) {
					VARSQL.log(status + " : " + e + xhr.responseText);
				}
			});
		}
	}
});

</script>