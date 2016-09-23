<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/include/initvariable.jspf"%>
<script>
$(document).ready(function (){
	manageMain.init();
});

var manageMain ={
	init:function (){
		var _self = this; 
		_self.initEvt();
		_self.search();
	}
	,initEvt:function (){
		var _self = this; 
		$('#allcheck').click(function() {
			$('input:checkbox[class="itemCheck"]').prop( 'checked', $(this).is(':checked'));
		});
		
		//수락 거부 버튼 처리 
		$('.btnAcceptYN').click(function() {
			_self.acceptYn($(this).val());
		});
		
		//검색 버튼 처리
		$('.searchBtn').click(function() {
			_self.search();
		});
		
		// 검색 input 처리
		$('#searchval').keydown(function() {
			if(event.keyCode =='13') _self.search();
		});
		
		// 갯수 셋팅시 이벤트 처리
		$('#rowDataSize').change(function() {
			_self.search();
		});
	}
	,acceptYn : function(obj){
		var selectItem = VARSQL.check.getCheckVal('input:checkbox[class="itemCheck"]');
		
		if(VARSQL.isDataEmpty(selectItem)){
			VARSQL.alert('<spring:message code="msg.data.select" />');
			return ; 
		}
		
		if(!confirm(obj=='Y'?'<spring:message code="msg.accept.msg" />':'<spring:message code="msg.denial.msg" />')){
			return ; 
		}
		
		var param = {
			acceptyn:obj
			,selectItem:selectItem.join(',')
		};
		
		VARSQL.req.ajax({
			type:'POST'
			,data:param
			,url : {gubun:VARSQL.uri.manager, url:'/acceptYn.do'}
			,dataType:'JSON'
			,success:function (response){
				$('#allcheck').prop('checked', false);
				fnSearch();
			}
		});
	}
	,search : function(no){
		var _self = this; 
		
		var param = {
			page:no?no:1
			,rows:$('#rowDataSize').val()
			,'searchval':$('#searchval').val()
		};
		
		VARSQL.req.ajax({
			type:'POST'
			,data:param
			,url : {gubun:VARSQL.uri.manager, url:'/userList.do'}
			,dataType:'JSON'
			,success:function (response){
	    		var resultLen = response.result?response.result.length:0;
	    		
	    		if(resultLen==0){
	    			$('.dataTableContent').html('<tr><td colspan="10"><div class="text-center"><spring:message code="msg.nodata"/></div></td></tr>');
	    			$('.pageNavigation').pagingNav();
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
	    			strHtm.push('	<td><div class="text-center"><input type="checkbox" class="itemCheck" value="'+item.VIEWID+'"></div></td>');
	    			strHtm.push('	<td class="">'+item.UNAME+'</td>');
	    			strHtm.push('	<td class="">'+item.UID+'</td>');
	    			strHtm.push('	<td class="center">'+item.CHAR_CRE_DT+'</td>');
	    			strHtm.push('	<td class="center">'+item.ACCEPT_YN+'</td>');
	    			strHtm.push('</tr>');
	    		}
	    		
	    		$('.dataTableContent').html(strHtm.join(''));
	    		
	    		$('.pageNavigation').pagingNav(response.paging,manageMain.search);
			}
		});
	}
}


</script>
<!-- Page Heading -->
<div class="row">
    <div class="col-lg-12">
        <h1 class="page-header"><spring:message code="manage.menu.usermgmt" /></h1>
    </div>
    <!-- /.col-lg-12 -->
</div>
<div class="row">
	<div class="col-lg-12">
		<div class="panel panel-default">
			<!-- /.panel-heading -->
			<div class="panel-body">
				<div class="row">
					<div class="col-sm-6">
						<label><select name="rowDataSize" id="rowDataSize"
							aria-controls="dataTables-example" class="form-control input-sm"><option
									value="10">10</option>
								<option value="25">25</option>
								<option value="50">50</option>
								<option value="100">100</option></select>
						</label>
						<label> 
							<button type="button" class="btn btn-xs btn-primary btnAcceptYN" value="Y"><spring:message code="btn.accept" /></button>
						</label>
						<label> 
							<button type="button" class="btn btn-xs btn-danger btnAcceptYN" value="N"><spring:message code="btn.denial" /></button>
						</label>
					</div>
					<div class="col-sm-6">
						<div class="dataTables_filter">
							<div class="input-group floatright">
								<input type="text" value="" id="searchval" name="searchval"
									class="form-control" onkeydown=""> <span
									class="input-group-btn">
									<button class="btn btn-default searchBtn" type="button">
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
									<th tabindex="0" rowspan="1" colspan="1" style="width: 10px;"><div
											class="text-center">
											<input type="checkbox" id="allcheck" name="allcheck">
										</div></th>
									<th tabindex="0" rowspan="1" colspan="1" style="width: 195px;"><spring:message
											code="manage.userlist.name" /></th>
									<th tabindex="0" rowspan="1" colspan="1" style="width: 150px;"><spring:message
											code="manage.userlist.id" /></th>
									<th tabindex="0" rowspan="1" colspan="1" style="width: 179px;"><spring:message
											code="manage.userlist.date" /></th>
									<th tabindex="0" rowspan="1" colspan="1" style="width: 50px;"><spring:message
											code="manage.userlist.access" /></th>
								</tr>
							</thead>
							<tbody class="dataTableContent">
							</tbody>
						</table>
						<div class="pageNavigation"></div>
					</div>
				</div>
			</div>
			<!-- /.panel-body -->
		</div>
		<!-- /.panel -->
	</div>
	<!-- /.col-lg-12 -->
</div>
<!-- /.row -->
