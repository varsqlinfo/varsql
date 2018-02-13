<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/include/tagLib.jspf"%>
<script>
$(document).ready(function (){
	databaseOptMgmt.init();
});

var databaseOptMgmt = {
	init : function (){
		var _this = this; 
		_this.search();
		_this.add();
		_this.initEvt();
	}
	,initEvt : function (){
		var _this = this; 
		//add btn
		$('.addBtn').click(function() {
			$('#addForm').data('bootstrapValidator').resetForm(true);
			_this.add();
		});
		
		//add btn
		$('.saveAndPoolInitBtn').click(function() {
			if(!confirm('<spring:message code="msg.saveAndpoolInit.confirm"/>')) return ; 
			
			$('#pollinit').val('Y');
			$('.saveBtn').trigger('click');
		});
		// connection check
		$('.connCheckBtn').click(function() {
			_this.connectionCheck();
		});
		//delete btn
		$('.deleteBtn').click(function() {
			_this.deleteInfo();
		});
		//search
		$('.searchBtn').click(function() {
			_this.search();
		});
		
		$('#searchVal').keydown(function() {
			if(event.keyCode =='13') _this.search();
		});
		
		$('#vtype').on('change',function() {
			_this.dbDriverLoad(this.value);
		});
		
		$('#addForm').bootstrapValidator({
			message: 'This value is not valid',
			feedbackIcons: {
				valid: 'glyphicon glyphicon-ok',
				invalid: 'glyphicon glyphicon-remove',
				validating: 'glyphicon glyphicon-refresh'
			},
			fields: {
			  vname: {
				  validators: {
					  notEmpty: {
						  message: 'The name is required and cannot be empty'
					  }
				  }
			  },
			  vdbschema: {
				  validators: {
					  notEmpty: {
						  message: 'The schema is required and cannot be empty'
					  }
				  }
			  },
			  vurl: {
				  validators: {
					  notEmpty: {
						  message: 'The url is required and cannot be empty'
					  }
				  }
			  }
			  /*
			  vid: {
				  validators: {
					  notEmpty: {
						  message: 'The id is required and cannot be empty'
					  }
				  }
			  },
			  vpw: {
				  validators: {
					  notEmpty: {
						  message: 'The password is required and cannot be empty'
					  }
				  }
			  }
			  */
			}
		}).on('success.form.bv', function(e) {
			// Prevent form submission
			e.preventDefault();

			_this.save();
		});
	}
	// 추가
	,add : function (){
		$('#vconnid').val('');
		$('#vname').val('');
		$('#vdbschema').val('');
		$('#vurl').val('');
		$('#vdriver').val('');
		$('#vtype').val('');
		$('#vid').val('');
		$('#vpw').val('');
		$('#vconnopt').val('');
		$('#vpoolopt').val('');
		$('#vquery').val('');
		$('#pollinit').val('N');
	}
	,clickDbInfo : function (sObj){
		sObj = $(sObj);
		
		$('#vconnid').val(sObj.attr('conid'));
		VARSQL.req.ajax({
			data:{
				vconnid:$('#vconnid').val()
			}
			,url : '/admin/main/dbDetail'
			,success:function (resData){
				var item = resData.item;
				
				$('#vname').val(item.VNAME);
				$('#vurl').val(item.VURL);
				$('#vdriver').val(item.VDRIVER);
				$('#vdbschema').val(item.VDBSCHEMA);
				$('#vtype').val(item.VTYPE);
				$('#vid').val(item.VID);
				$('#vpw').val(item.VPW);
				$('#vconnopt').val(item.VCONNOPT);
				$('#vpoolopt').val(item.VPOOLOPT);
				$('#vquery').val(item.VQUERY);
				
				$('#vtype').trigger('change');
			}
		});
	}
	,search : function (no){
		var _this = this;
		
		var param = {
			pageNo : (no?no:1)
			,'searchVal':$('#searchVal').val()
		};
		
		VARSQL.req.ajax({
			data:param
			,url : '/admin/main/dblist'
			,success:function (resData){
					
				var result = resData.items;
	    		var resultLen = result.length;
	    		
	    		if(resultLen==0){
	    			$('#dbinfolist').html('<div class="text-center"><spring:message code="msg.nodata"/></div>');
	    			$('#pageNavigation').pagingNav();
	    			return ; 
	    		}
	    		
	    		var strHtm = new Array();
	    		var item; 
	    		for(var i = 0 ;i < resultLen; i ++){
	    			item = result[i];
	    			strHtm.push('<a href="javascript:;" class="list-group-item" conid="'+item.VCONNID+'">'+item.VNAME);
	    			strHtm.push('<span class="pull-right text-muted small"><!--em>4 minutes ago</em--></span></a>');
	    		}
	    		
	    		$('#dbinfolist').html(strHtm.join(''));
	    		
	    		$('#dbinfolist .list-group-item').on('click',function (){
	    			databaseOptMgmt.clickDbInfo($(this));
	    		});
	    		
	    		$('#pageNavigation').pagingNav(resData.page,databaseOptMgmt.search);
			}
		});
	}
	,save : function (){
		var _this = this; 
		VARSQL.req.ajax({
			data:$("#addForm").serialize()
			,url : '/admin/main/dbSave'
			,success:function (resData){
				if(VARSQL.req.validationCheck(resData)){
					_this.search();
					$('.addBtn').trigger("click");
					
				}
			}
		});
	}
	// 정보 삭제 .
	,deleteInfo : function (){
		var _this = this; 
		
		if($('#vconnid').val()==''){
			$('#warningMsgDiv').html('<spring:message code="msg.warning.select" />');
			return ; 
		}
		
		if(!confirm('<spring:message code="msg.delete.confirm"/>')){
			return ; 
		}
		
		VARSQL.req.ajax({
			type:'POST'
			,data: {
				vconnid : $('#vconnid').val() 
			}
			,url : '/admin/main/dbDelete'
			,dataType:'JSON'
			,success:function (resData){
				_this.search();
				$('#addBtn').trigger("click");
			}
		});
	}
	,connectionCheck : function (){
		/*
		if($('#vconnid').val()==''){
			$('#warningMsgDiv').html('<spring:message code="msg.warning.select" />');
			return ; 
		}
		*/
		var param =  $("#addForm").serializeJSON();
		
		param.vdriver = $('#vdriver option:selected').attr('data-driver');
		
		VARSQL.req.ajax({
			type:'POST'
			,data:param
			,url : '/admin/main/dbConnectionCheck'
			,dataType:'JSON'
			,success:function (resData){
				if(VARSQL.req.validationCheck(resData)){
					if(resData.messageCode =='success'){
						alert('<spring:message code="msg.success" />');
						return 
					}else{
						alert(resData.messageCode  +'\n'+ resData.message);
					}	
				}
				
			}
		});
	}
	// db driver list
	,dbDriverLoad : function (val){
		VARSQL.req.ajax({
			data: {
				dbtype :val
			}
			,url : '/admin/main/dbDriver'
			,success:function (resData){
				
				var result = resData.items;
	    		var resultLen = result.length;
	    		
	    		if(resultLen==0){
	    			$('#vdriver').empty();
	    			return ; 
	    		}
	    		
	    		var strHtm = [];
	    		var item;
	    		for(var i = 0 ;i < resultLen; i ++){
	    			item = result[i];
	    			strHtm.push('<option value="'+item.DRIVER_ID+'" data-driver="'+item.DBDRIVER+'">'+item.DRIVER_DESC+'('+item.DBDRIVER+')</option>');
	    		}
	    		
	    		$('#vdriver').empty().html(strHtm.join(''));
			}
		});
	}
}

</script>
<!-- Page Heading -->
<div class="row">
    <div class="col-lg-12">
        <h1 class="page-header"><spring:message code="admin.menu.databaseoptmgmt" /></h1>
    </div>
    <!-- /.col-lg-12 -->
</div>
<div class="row">
	<div class="col-lg-5">
		<div class="panel panel-default">
			<div class="panel-heading">
				<div class="input-group">
					<input type="text" value="" id="searchVal" name="searchVal" class="form-control">
					<span	class="input-group-btn">
						<button class="btn btn-default searchBtn" type="button"> <span class="glyphicon glyphicon-search"></span></button>
					</span>
				</div>
			</div>
			<!-- /.panel-heading -->
			<div class="panel-body">
				<div class="list-group" id="dbinfolist"></div>
				<!-- /.list-group -->
				<div id="pageNavigation"></div>
			</div>
			<!-- /.panel-body -->
		</div>
		<!-- /.panel -->
	</div>
	<!-- /.col-lg-4 -->
	<div class="col-lg-7">
		<div class="panel panel-default">
			<div class="panel-heading"><spring:message code="admin.form.header" /></div>
			<!-- /.panel-heading -->
			<div class="panel-body">
				<form id="addForm" name="addForm" class="form-horizontal">
					<input type="hidden" id="vconnid" name="vconnid">
					<input type="hidden" id="pollinit" name="pollinit" value="N">
					<div id="warningMsgDiv"></div>
					<div class="form-group">
						<label class="col-sm-4 control-label"><spring:message code="admin.form.db.vname" /></label>
						<div class="col-sm-8">
							<input class="form-control text required" id="vname" name="vname" value="">
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-4 control-label"><spring:message code="admin.form.db.databasename" /></label>
						<div class="col-sm-8">
							<input class="form-control text required" id="vdbschema" name="vdbschema" value="">
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-4 control-label"><spring:message code="admin.form.db.vurl" /></label>
						<div class="col-sm-8">
							<input class="form-control text required" id="vurl" name="vurl" value="">
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-4 control-label"><spring:message code="admin.form.db.vtype" /></label>
						<div class="col-sm-8">
							<select class="form-control text required" id="vtype" name="vtype">
								<c:forEach items="${dbtype}" var="tmpInfo" varStatus="status">
									<option value="${tmpInfo.URLPREFIX}" i18n="${tmpInfo.LANGKEY}">${tmpInfo.NAME}</option>
								</c:forEach>
							</select>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-4 control-label"><spring:message code="admin.form.db.vdriver" /></label>
						<div class="col-sm-8">
							<select class="form-control text required" id="vdriver" name="vdriver">
							</select>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-4 control-label"><spring:message code="admin.form.db.vid" /></label>
						<div class="col-sm-8">
							<input class="form-control text required" id="vid" name="vid" value="">
						</div>
					</div>
					
					<div class="form-group">
						<label class="col-sm-4 control-label"><spring:message code="admin.form.db.vpw" /></label>
						<div class="col-sm-8">
							<input class="form-control text required" type="password" id="vpw" name="vpw" value="">
						</div>
					</div>
					
					<div class="form-group">
						<label class="col-sm-4 control-label"><spring:message code="admin.form.db.vquery" /></label>
						<div class="col-sm-8">
							<input class="form-control text" type="text" id="vquery" name="vquery" value="">
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-4 control-label"><spring:message code="admin.form.db.vconnopt" /></label>
						<div class="col-sm-8">
							<input class="form-control text" type="text" id="vconnopt" name="vconnopt" value="">
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-4 control-label"><spring:message code="admin.form.db.vpoolopt" /></label>
						<div class="col-sm-8">
							<input class="form-control text" type="text" id="vpoolopt" name="vpoolopt" value="">
						</div>
					</div>

					<div class="form-group">
						<div class="col-sm-offset-4 col-sm-8">
							<button type="button" class="btn btn-default addBtn"><spring:message code="btn.add"/></button>
							<button type="submit" class="btn btn-default saveBtn"><spring:message code="btn.save"/></button>
							<button type="button" class="btn btn-default saveAndPoolInitBtn"><spring:message code="btn.save.andpoolnit"/></button>
							<button type="button" class="btn btn-primary connCheckBtn"><spring:message code="btn.connnection.check"/></button>
							<button type="button" class="btn btn-danger deleteBtn"><spring:message code="btn.delete"/></button>
						</div>
					</div>
				</form>
			</div>
			<!-- /.panel-body -->
		</div>
		<!-- /.panel -->
	</div>
	<!-- /.col-lg-8 -->
</div>
<!-- /.row -->
