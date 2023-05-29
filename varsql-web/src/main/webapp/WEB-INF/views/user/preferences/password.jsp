<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/include/tagLib.jspf"%>
<%
response.setHeader("Cache-Control","no-cache");
response.setHeader("Pragma","no-cache");
response.setDateHeader ("Expires", -1);
%>
<div class="row">
    <div class="col-lg-12">
        <h1 class="page-header"><spring:message code="user.prefernces.menu.pasword" /></h1>
    </div>
    <!-- /.col-lg-12 -->
</div>
<div class="page-cont row">
	<div class="col-xs-12 fill">
		<div class="panel panel-default">
			<div class="panel-body">
				<div class="row">
					<div class="col-sm-12">
						<div class="pull-right margin-bottom5">
							<button type="button" class="btn btn-default save-btn">
								<i class="fa fa-save"></i><spring:message code="btn.save"/>
							</button>
						</div>
					</div>
				</div>

				<div>
					<form id="passwordResetForm" class="form-horizontal required-validate" method="post" onsubmit="return false;">
						<div class="form-group">
							<label class="col-lg-2 control-label"><spring:message code="user.form.password.current"/></label>
							<div class="col-lg-10">
								<input type="password"  id="currPw" name="currPw" value="" class="form-control text">
							</div>
						</div>
						<div class="form-group">
							<label class="col-lg-2 control-label"><spring:message code="user.form.password.new" /></label>
							<div class="col-lg-10">
								 <input type="password" id="upw" name="upw" value="" class="form-control"/>
							</div>
						</div>
						<div class="form-group">
							<label class="col-lg-2 control-label"><spring:message code="user.form.password.confirm"/></label>

				            <div class="col-lg-10">
				                <input type="password" id="confirmUpw" name="confirmUpw" value="" class="form-control"/>
				            </div>
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>
</div>

<script>

(function (){
	$(document).ready(function (){
		passwordMain.init();
	});

	var passwordMain = {
		init:function(){
			var _self = this;
			_self.initEvt();
		}
		,initEvt : function (){
			var _self = this;

			$('.save-btn').on('click',function (){
				$('#passwordResetForm').submit();
			});

			$('#passwordResetForm').bootstrapValidator({
				message: 'This value is not valid',
				feedbackIcons: {
					valid: 'glyphicon glyphicon-ok',
					invalid: 'glyphicon glyphicon-remove',
					validating: 'glyphicon glyphicon-refresh'
				},
				fields: {
					currPw: {
		                validators: {
		                    notEmpty: { message: VARSQL.message('varsql.form.0001') }
		                }
		            }
					,upw : {
		                validators: {
		                	 notEmpty: { message: VARSQL.message('varsql.form.0001')}
	                    	,stringLength: {min: 4, max: 500, message:  VARSQL.message('varsql.form.0002' , {len:4}) }
		                    ,identical: {
		                        field: 'confirmUpw',
		                        message: VARSQL.message('varsql.form.0003')
		                    }
		                }
		            }
		            ,confirmUpw : {
		                validators: {
		                    notEmpty: {
		                        message: VARSQL.message('varsql.form.0001')
		                    }
		                    ,identical: {
		                        field: 'upw',
		                        message: VARSQL.message('varsql.form.0003')
		                    }
		                }
		            }
				}
			}).on('success.form.bv', function(e) {
				// Prevent form submission
				e.preventDefault();

				_self.saveInfo();
			});
		}
		// 정보 저장.
		,saveInfo : function (){
			var _self = this;

			var params  =$('#passwordResetForm').serializeJSON();

			VARSQL.req.ajax({
				url: {type:VARSQL.uri.user, url:'/preferences/passwordSave'},
				data:params,
				success: function(resData) {
					if(!VARSQL.req.validationCheck(resData)){
						return ;
					}

					if(resData.resultCode == 50001){
						VARSQL.alertMessage('varsql.m.0007');
						return ;
					}else{
						if(resData.resultCode != 200){
							VARSQL.alertMessage(resData.message);
							return ;
						}
					}

					VARSQL.alertMessage('varsql.m.0008');

					location.href= location.href;
				}
			});
		}
	}
})();

</script>
