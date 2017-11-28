<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/include/tagLib.jspf"%>
<%@ include file="/WEB-INF/include/initvariable.jspf"%>

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
				<div class="col-sm-12">
					<form id="writeForm" name="writeForm" role="form" class="form-horizontal">
						<div class="form-group">
							<label class="col-lg-2 control-label" for="inputError"><spring:message code="label.email" text="email"/></label>
							<div class="col-lg-10">
								<input type="text"  id=uemail name="uemail" value="" class="form-control text required">
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label" for="inputError"><spring:message code="join.form.name" text="URL"/></label>
							<div class="col-lg-10">
								 <input type="text" class="form-control" id="uname" name="uname" placeholder="<spring:message code="join.form.name"/>" required data-bv-notempty-message="<spring:message code="msg.name.empty" />"/>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label"><spring:message code="join.form.dept"/></label>

				            <div class="col-sm-10">
				                <input type="text" class="form-control" id="udept" name="udept" placeholder="<spring:message code="join.form.dept"/>"/>
				            </div>
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>
</div>
<script>
var virtualMain = {
	URL:{
		saveUri:'<c:url value="/portal/${gainPortalVirtual.virtualContextPath}/manage//portal/savePortalInfo" />'
		,urlCheckUri : '<c:url value="/portal/${gainPortalVirtual.virtualContextPath}/manage/portal/urlCheck" />'
	}
	,virtualPortalItems : false
	,init:function(){
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
				portalName: {
				  validators: {
					  notEmpty: {
						  message: '그룹명은 필수 입력사항입니다.'
					  }
				  }
			  	},	
			  	portalUrl: {
				  validators: {
					  notEmpty: {
						  message: 'url 필수 입력사항입니다.'
					  }
			          ,stringLength: {
					      min: 3,
					      max: 100,
					      message: '사이즈는 3~32 사이여야 합니다'
					  }
			          ,callback: {
	                     message: '이미 있는 URL 입니다.',
	                     callback: function (value, validator, $field) {
	                   	  return $('#url_check').val() < 1;
	                     }
	                 }
				  }
			  	},	
			  	sortOrder: {
				  validators: {
					  integer: {
	                     message: '숫자만 가능합니다.'
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
		
		var params  =$('#writeForm').serializeJSON();
		
		GAINEP.req.ajax({
			url: _self.URL.saveUri,
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
						alert('url 중복 입니다.');
						return ;
					}
				}
				
				location.href = location.href;
			},
			error: function(xhr, status, e) {
				GAINEP.log(status + " : " + e + xhr.responseText);
			}
		});
	}
}

$(document).ready(function (){
	generalMain.init();
});
</script>