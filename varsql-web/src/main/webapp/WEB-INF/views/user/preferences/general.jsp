<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/include/tagLib.jspf"%>
<div class="row">
    <div class="col-lg-12">
        <h1 class="page-header"><spring:message code="user.prefernces.menu.general" /></h1>
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
								<i class="fa fa-save"></i><spring:message code="btn.save" text="저장"/>
							</button>
						</div>
					</div>
				</div>

				<div>
					<form id="writeForm" name="writeForm" role="form" class="form-horizontal">
						<div class="form-group">
							<label class="col-lg-2 control-label" for="inputError"><spring:message code="label.id" text="ID"/></label>
							<div class="col-lg-10">
								<input type="text"  id=uid name="uid" value="${detailInfo.uid}" class="form-control text required" disabled>
							</div>
						</div>
						<div class="form-group">
							<label class="col-lg-2 control-label" for="inputError"><spring:message code="label.email" text="email"/></label>
							<div class="col-lg-10">
								<input type="text"  id=uemail name="uemail" value="${detailInfo.uemail}" class="form-control text required" disabled>
							</div>
						</div>
						<div class="form-group">
							<label class="col-lg-2 control-label" for="inputError"><spring:message code="join.form.name" text="URL"/></label>
							<div class="col-lg-10">
								 <input type="text" class="form-control" id="uname" name="uname"  value="${detailInfo.uname}" placeholder="<spring:message code="join.form.name"/>"/>
							</div>
						</div>
						<div class="form-group">
							<label class="col-lg-2 control-label"><spring:message code="join.form.organization"/></label>

				            <div class="col-lg-10">
				                <input type="text" class="form-control" id="deptNm" name="deptNm" value="${detailInfo.orgNm}" placeholder="<spring:message code="join.form.organization"/>"/>
				            </div>
						</div>
						<div class="form-group">
							<label class="col-lg-2 control-label"><spring:message code="join.form.locale"/></label>

				            <div class="col-lg-10">
				            	<select class="form-control" id="lang" name="lang">
				            		<option value=""><spring:message code="join.form.locale"/></option>
				            		<c:forEach var="item" items="${localeInfo}" begin="0" varStatus="status">
										<option value="${item.locale}" ${item.locale == detailInfo.lang ? 'selected="selected"' : '' }><spring:message code="${item.i18n}"/></option>
									</c:forEach>
				            	</select>
				            </div>
						</div>
						<div class="form-group">
							<label class="col-lg-2 control-label"><spring:message code="join.form.desc"/></label>

				            <div class="col-lg-10">
				                <textarea class="form-control" rows="3" id="description" name="description" placeholder="<spring:message code="join.form.desc"/>" >${detailInfo.description}</textarea>
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
	var generalMain = {
		init:function(){
			var _self = this;
			_self.initEvt();
		}
		,initEvt : function (){
			var _self = this;

			$('.save-btn').on('click',function (){
				$('#writeForm').submit();
			});

			$('#writeForm').bootstrapValidator({
				message: 'This value is not valid',
				feedbackIcons: {
					valid: 'glyphicon glyphicon-ok',
					invalid: 'glyphicon glyphicon-remove',
					validating: 'glyphicon glyphicon-refresh'
				},
				fields: {
					uname : {
						validators: {
							notEmpty: { message: VARSQL.messageFormat('varsql.form.0001') }
							,stringLength: { min: 3, max: 100, message: VARSQL.messageFormat('varsql.form.0004',{range : '3~100'}) }
						}
				  	}
					,uemail : {
						validators: {
							notEmpty: { message: VARSQL.messageFormat('varsql.form.0001')}
							,stringLength: { min: 0, max: 500, message: VARSQL.messageFormat('varsql.form.0004',{range : '0~250'}) }
							,emailAddress: {
								message: 'The input is not a valid email address'
							}
					  }
				  	}
					,udept : {
						validators: {
							stringLength: { min: 0, max: 120, message: VARSQL.messageFormat('varsql.form.0004',{range : '0~120'}) }
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

			var params  =$('#writeForm').serializeJSON();

			VARSQL.req.ajax({
				url: {type:VARSQL.uri.user, url:'/preferences/userInfoSave'},
				data:params,
				success: function(resData) {
					if(!VARSQL.req.validationCheck(resData)){
						return ;
					}else{
						if(resData.resultCode != 200){
							alert(resData.message);
							return ;
						}
					}

					location.href= location.href;
				}
			});
		}
	}

	$(document).ready(function (){
		generalMain.init();
	});
})()

</script>