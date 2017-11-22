<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/include/tagLib.jspf"%>
<script>
$(document).ready(function (){
	fnInit();
});

function fnInit(){
	$('.searchBtn1').click(function() {
		fnSearch1();
	});
	
	$('#searchVal1').keydown(function() {
		if(event.keyCode =='13') fnSearch1();
	});
	
	$('.searchBtn2').click(function() {
		fnSearch2();
	});
	
	$('#searchVal2').keydown(function() {
		if(event.keyCode =='13') fnSearch2();
	});
	
	fnSearch1();
	fnSearch2();
};

function fnSearch1(no){
	var param = {
		page:no?no:1
		,'searchVal':$('#searchVal1').val()
	};
	
	VARSQL.req.ajax({
		type:'POST'
		,data:param
		,url : {gubun:VARSQL.uri.admin, url:'/managerMgmt/userList'}
		,dataType:'JSON'
		,success:function (response){
				
    		var resultLen = response.result?response.result.length:0;
    		
    		if(resultLen==0){
    			$('.dataTableContent1').html('<tr><td colspan="10"><div class="text-center"><spring:message code="msg.nodata"/></div></td></tr>');
    			$('.pageNavigation1').pagingNav();
    			return ; 
    		}
    		var result = response.result;
    		
    		var strHtm = new Array();
    		var item;
    		var clazz;
    		for(var i = 0 ;i < resultLen; i ++){
    			item = result[i];
    			clazz= i%2==0?'add':'even';
    			strHtm.push('<tr class="gradeA '+clazz+'">');
    			strHtm.push('	<td class=""><a href="javascript:;" viewid="'+item.VIEWID+'" mode="add" class="addManagerRole">'+item.UNAME+'</a></td>');
    			strHtm.push('	<td class="">'+item.UID+'</td>');
    			strHtm.push('	<td class="center">'+item.DEPT_NM+'</td>');
    			strHtm.push('</tr>');
    		}
    		
    		$('.dataTableContent1').html(strHtm.join(''));
    		
    		fnManagerRoleEvent('add');
    		
    		$('.pageNavigation1').pagingNav(response.paging,fnSearch1);
			
		}
		,error :function (data, status, err){
			$('.dataTableContent1').html("<font color='red'> errorMsg : "+ err +" resultMsg: "+ status+"</font>");
			VARSQL.progress.end('.dataTableContent1');
		}
		,complete: function() { 
			VARSQL.progress.end('.dataTableContent1');
		}
		,beforeSend: function() {
			VARSQL.progress.start ('.dataTableContent1');
		}
	});
}

function fnSearch2(no){
	var param = {
		page:no?no:1
		,'searchVal':$('#searchVal2').val()
	};
	
	VARSQL.req.ajax({
		type:'POST'
		,data:param
		,url : {gubun:VARSQL.uri.admin, url:'/managerMgmt/managerList'}
		,dataType:'JSON'
		,success:function (response){
				
    		var resultLen = response.result?response.result.length:0;
    		
    		if(resultLen==0){
    			$('.dataTableContent2').html('<tr><td colspan="10"><div class="text-center"><spring:message code="msg.nodata"/></div></td></tr>');
    			$('.pageNavigation2').pagingNav();
    			return ; 
    		}
    		var result = response.result;
    		
    		var strHtm = new Array();
    		var item;
    		var clazz;
    		for(var i = 0 ;i < resultLen; i ++){
    			item = result[i];
    			clazz= i%2==0?'add':'even';
    			strHtm.push('<tr class="gradeA '+clazz+'">');
    			strHtm.push('	<td class=""><a href="javascript:;" viewid="'+item.VIEWID+'" mode="del" class="delManagerRole">'+item.UNAME+'</a></td>');
    			strHtm.push('	<td class="">'+item.UID+'</td>');
    			strHtm.push('	<td class="center">'+item.DEPT_NM+'</td>');
    			strHtm.push('</tr>');
    		}
    		
    		$('.dataTableContent2').html(strHtm.join(''));
    		
    		fnManagerRoleEvent('del');
    		
    		$('.pageNavigation2').pagingNav(response.paging,fnSearch2);
		
		}
		,error :function (data, status, err){
			$('.dataTableContent2').html("<font color='red'> errorMsg : "+ err +" resultMsg: "+ status+"</font>");
			VARSQL.progress.end('.dataTableContent2');
		}
		,complete: function() { 
			VARSQL.progress.end('.dataTableContent2');
		}
		,beforeSend: function() {
			VARSQL.progress.start ('.dataTableContent2');
		}
	});
}

function fnManagerRoleEvent(pType){
	
	$('.'+pType+'ManagerRole').click(function (){
		var type= $(this).attr('mode');
		if(!confirm(type=='add'?'<spring:message code="msg.add.manager.confirm"/>':'<spring:message code="msg.del.manager.confirm"/>')){
			return ; 
		}
		
		var param ={
			viewid:$(this).attr('viewid')
			,mode:type
		};
		VARSQL.req.ajax({
			type:'POST'
			,data:param
			,url : {gubun:VARSQL.uri.admin, url:'/managerMgmt/managerRoleMgmt'}
			,dataType:'JSON'
			,success:function (response){
				fnSearch1();	
				fnSearch2();
			}
			,error :function (data, status, err){
				$('#dataViewAreaTd').html("<font color='red'> errorMsg : "+ err +" resultMsg: "+ status+"</font>");
				VARSQL.progress.end('dataViewAreaTd');
			}
			,complete: function() { 
				VARSQL.progress.end('dataViewAreaTd');
			}
			,beforeSend: function() {
				VARSQL.progress.start ('dataViewAreaTd');
			}
		});
	});
}
</script>
<!-- Page Heading -->
<div class="row">
    <div class="col-lg-12">
        <h1 class="page-header"><spring:message code="admin.menu.managermgmt" /></h1>
    </div>
    <!-- /.col-lg-12 -->
</div>
<div class="row">
	<div class="col-sm-6">
		<div class="panel panel-default">
			<div class="panel-heading"><spring:message code="admin.userlist.search.head" /></div>
			<!-- /.panel-heading -->
			<div class="panel-body">
				<div class="row">
					<div class="col-sm-12">
						<div class="dataTables_filter">
							<div class="input-group floatright">
								<input type="text" value="" id="searchVal1" name="searchVal1"
									class="form-control"> <span
									class="input-group-btn">
									<button class="btn btn-default searchBtn1" type="button">
										<span class="glyphicon glyphicon-search"></span>
									</button>
								</span>
							</div>
						</div>
					</div>
				</div>
				<div class="table-responsive">
					<div id="dataTables-example_wrapper"
						class="dataTables_wrapper form-inline" role="grid">
						<table
							class="table table-striped table-bordered table-hover dataTable no-footer"
							id="dataTables-example">
							<thead>
								<tr role="row">
									<th tabindex="0" rowspan="1" colspan="1" style="width: 195px;"><spring:message
											code="manage.userlist.name" /></th>
									<th tabindex="0" rowspan="1" colspan="1" style="width: 150px;"><spring:message
											code="manage.userlist.id" /></th>
									<th tabindex="0" rowspan="1" colspan="1" style="width: 179px;"><spring:message
											code="manage.userlist.dept" /></th>
								</tr>
							</thead>
							<tbody class="dataTableContent1">
							</tbody>
						</table>
						<div class="pageNavigation1"></div>
					</div>
				</div>
			</div>
			<!-- /.panel-body -->
		</div>
		<!-- /.panel -->
	</div>
	
	<div class="col-sm-6">
		<div class="panel panel-default">
			<div class="panel-heading"><spring:message code="admin.managerlist.head" /></div>
			<!-- /.panel-heading -->
			<div class="panel-body">
				<div class="row">
					<div class="col-sm-12">
						<div class="dataTables_filter">
							<div class="input-group floatright">
								<input type="text" value="" id="searchVal2" name="searchVal2"
									class="form-control"> <span
									class="input-group-btn">
									<button class="btn btn-default searchBtn2" type="button">
										<span class="glyphicon glyphicon-search"></span>
									</button>
								</span>
							</div>
						</div>
					</div>
				</div>
				<div class="table-responsive">
					<div id="dataTables-example_wrapper"
						class="dataTables_wrapper form-inline" role="grid">
						<table
							class="table table-striped table-bordered table-hover dataTable no-footer"
							id="dataTables-example">
							<thead>
								<tr role="row">
									<th tabindex="0" rowspan="1" colspan="1" style="width: 195px;"><spring:message
											code="manage.userlist.name" /></th>
									<th tabindex="0" rowspan="1" colspan="1" style="width: 150px;"><spring:message
											code="manage.userlist.id" /></th>
									<th tabindex="0" rowspan="1" colspan="1" style="width: 179px;"><spring:message
											code="manage.userlist.dept" /></th>
								</tr>
							</thead>
							<tbody class="dataTableContent2">
							</tbody>
						</table>
						<div class="pageNavigation2"></div>
					</div>
				</div>
			</div>
			<!-- /.panel-body -->
		</div>
		<!-- /.panel -->
	</div>
</div>
<!-- /.row -->
