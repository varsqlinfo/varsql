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
    <h3 class="page-header" style="text-align:center;"><spring:message code="varsql.title"/> <spring:message code="password.reset"/></h3>
    <!-- form start -->
    <form name="resetForm" id="resetForm" method="POST" action="<c:url value='/join/join' />"  class="form-horizontal well" role="form" onsubmit="return false;">
		
		<div  style="text-align: center;">
	        <ul style="list-style: none;font-weight: bold;">
	        	<li v-if="msgView =='error'" style="color: #ff2b2b;">
	        		<spring:message code="msg.send.mail.password.fail" text="메일 발송에 실패했습니다. <br/> 잘못된 비밀번호 재 설정 요청입니다."/>
	        	</li>
	        	<li v-else-if="msgView =='success'" style="color: #1eaef7;"><spring:message code="msg.send.mail" text="메일을 발송하였습니다."/></li>
	        </ul>
		</div>
		
        <div class="form-group">
            <label for="inputEmail3" class="col-sm-3 control-label"><spring:message code="user.id"/></label>

            <div class="col-sm-6 col-md-6">
                <input type="text" class="form-control required" id="uid" name="uid" placeholder="<spring:message code="user.id" />" />
            </div>
        </div>
        
        <div class="form-group">
            <label class="col-sm-3 control-label"><spring:message code="email"/></label>

            <div class="col-sm-6 col-md-6">
                <input type="email" class="form-control" id="uemail" name="uemail" placeholder="<spring:message code="email" />"/>
            </div>
        </div>
    </form>
    <div class="form-group">
          <div class="col-sm-12 text-center">
              <button type="button" @click="submit()" class="btn btn-info"><spring:message code="btn.confirm"/></button>
              <button type="button" @click="goCancel()" class="btn btn-default"><spring:message code="cancel"/></button>
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
		msgView : false
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
					uid: {
						validators: {
							notEmpty: { message: VARSQL.message('msg.valid.required','필수 입력사항입니다.')}
							,stringLength: { min: 3, max: 100, message: VARSQL.message('msg.valid.size.param',{size:'3~100'})}
						}
				  	}
					,uemail: {
						validators: {
							notEmpty: { message: VARSQL.message('msg.valid.required','필수 입력사항입니다.')}
							,stringLength: { min: 0, max: 250, message: VARSQL.message('msg.valid.size.param',{size:'0~250'})}
							,emailAddress: {
								message: VARSQL.message('msg.valid.invalid.param', VARSQL.message('email'))
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
				url: {type:VARSQL.uri.ignore, url:'/lostPassword'},
				data:params,
				success: function(resData) {
					if(!VARSQL.req.validationCheck(resData)){
						return ;
					}
					
					if(resData.resultCode == 404 || !VARSQL.isEmpty(resData.message)){
						_this.msgView = 'error'; 
						return ; 
					}
					
					_this.msgView = 'success';
				},
				error: function(xhr, status, e) {
					VARSQL.log(status + " : " + e + xhr.responseText);
				}
			});
		}
	}
});
</script>
