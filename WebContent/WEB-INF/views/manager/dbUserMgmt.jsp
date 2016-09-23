<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/include/initvariable.jspf"%>
<script>
$(document).ready(function (){
	dbUserMgmt.init();
});

var dbUserMgmt ={
	init:function (){
		var _self = this; 
		_self.search();
		_self.initEvt();
	}
	,initEvt:function (){
		var _self = this; 
		$('.searchBtn').click(function() {
			_self.search();
		});
		
		$('#searchval').keydown(function() {
			if(event.keyCode =='13') _self.search();
		});
		
		$('.btnSave').click(function (){
			_self.addDbUser();
		});
		
	}
	,search:function (no){
		var _self = this; 
		var param = {
			page:no?no:1
			,'searchval':$('#searchval').val()
		};
		
		VARSQL.req.ajax({
			type:'POST'
			,data:param
			,url : {gubun:VARSQL.uri.manager, url:'/dbnuser/dbList.do'}
			,dataType:'JSON'
			,success:function (response){
				try{
					
		    		var resultLen = response.result?response.result.length:0;
		    		
		    		if(resultLen==0){
		    			$('#dbinfolist').html('<div class="text-center"><spring:message code="msg.nodata"/></div>');
		    			$('#pageNavigation').pagingNav();
		    			return ; 
		    		}
		    		var result = response.result;
		    		
		    		var strHtm = new Array();
		    		var item; 
		    		for(var i = 0 ;i < resultLen; i ++){
		    			item = result[i];
		    			strHtm.push('<a href="javascript:;" class="list-group-item db-list-item" conid="'+item.VCONNID+'">'+item.VNAME);
		    			strHtm.push('<span class="pull-right text-muted small"><!--em>4 minutes ago</em--></span></a>');
		    		}
		    		
		    		$('#dbinfolist').html(strHtm.join(''));
		    		
		    		$('.db-list-item').on('click', function (){
		    			_self.dbUserList(this);
		    		});
		    		
		    		$('#pageNavigation').pagingNav(response.paging,fnSearch);
		    		
				}catch(e){
					$('#dataViewAreaTd').attr('color','red');
					$("#dataViewAreaTd").val("errorMsg : "+e+"\nargs : " + e.message);  
				}
			}
		});
	}
	,dbUserList:function (sObj){
		sObj=$(sObj);
		$('#vconnid').val(sObj.attr('conid'));
		
		VARSQL.req.ajax({
			type:'POST'
			,data:{
				vconnid:$('#vconnid').val()
			}
			,url : {gubun:VARSQL.uri.manager, url:'/dbnuser/dbnUserMappingList.do'}
			,dataType:'JSON'
			,success:function (response){
				var resultLen = response.result?response.result.length:0;
				
				if(resultLen==0){
	    			$('.dataTableContent1').html('<option><spring:message code="msg.nodata" /></option>');
	    			$('.dataTableContent2').html('<option><spring:message code="msg.nodata" /></option>');
	    			return ; 
	    		}
	    		var result = response.result;
	    		
	    		var strHtm1 = new Array(), strHtm2 = new Array();
	    		var item;
	    		var clazz;
	    		for(var i = 0 ;i < resultLen; i ++){
	    			item = result[i];
	    			if(item.VCONNID){
	    				strHtm1.push('	<option value="'+item.VIEWID+'">'+item.UNAME+' / '+item.DEPT_NM+' / '+item.UEMAIL+'</option>');	
	    			}else{
	    				strHtm2.push('	<option value="'+item.VIEWID+'">'+item.UNAME+' / '+item.DEPT_NM+' / '+item.UEMAIL+'</option>');
	    			}
	    		}
	    		
	    		$('.dataTableContent1').html(strHtm1.join(''));
	    		$('.dataTableContent2').html(strHtm2.join(''));
	    		
	    		var aaa = new selectBoxMove('.dataTableContent2','.dataTableContent1');
	    		aaa.init();
			}
		});
	}
	,addDbUser:function (){
		if($('#vconnid').val()==''){
			alert('<spring:message code="msg.warning.select" />');
			return ; 
		}
		
		var reInfo = new Array();
		$('.dataTableContent1').children().each(function (i, item){
			reInfo.push($(item).val())
		});

		
		var param ={
			selectItem:reInfo.join(',')
			,vconnid:$('#vconnid').val()
		};
		
		VARSQL.req.ajax({
			type:'POST'
			,data:param
			,url : {gubun:VARSQL.uri.manager, url:'/dbnuser/addDbUser.do'}
			,dataType:'JSON'
			,success:function (response){
				
			}
		});
	}
};

</script>
<!-- Page Heading -->
<div class="row">
    <div class="col-lg-12">
        <h1 class="page-header"><spring:message code="manage.menu.dbnuser" /></h1>
    </div>
    <!-- /.col-lg-12 -->
</div>
<div class="row">
	<div class="col-lg-3">
		<div class="panel panel-default">
			<div class="panel-heading">
				<div class="input-group">
					<input type="text" id="searchval" name="searchval" class="form-control">
						<span class="input-group-btn">
						<button class="btn btn-default searchBtn" type="button">
							<span class="glyphicon glyphicon-search"></span>
						</button>
					</span>
				</div>
			</div>
			<!-- /.panel-heading -->
			<div class="panel-body">
				<input type="hidden" id="vconnid" name="vconnid" value="">
				<div class="list-group" id="dbinfolist"></div>
				<!-- /.list-group -->
				<div id="pageNavigation"></div>
			</div>
			<!-- /.panel-body -->
		</div>
		<!-- /.panel -->
	</div>
	<!-- /.col-lg-4 -->
	<div class="col-lg-9">
		<div class="panel panel-default">
			<div class="panel-heading">
				<spring:message code="manage.dbnuser.dbuseuser" />
				<div class="pull-right">
					<button type="button" class="btn btn-outline btn-primary btn-xs btnSave">
						<spring:message code="btn.save" />
					</button>
				</div>
			</div>
			<!-- /.panel-heading -->
			<div class="panel-body">
				<div class="col-sm-12">
					<select multiple class="form-control dataTableContent1" size="10">
					  <option><spring:message code="msg.nodata" /></option>
					</select>
				</div>
				<div class="col-sm-12">&nbsp;</div>
				<div class="col-sm-12">
					<select multiple class="form-control dataTableContent2" size="10">
					  <option><spring:message code="msg.nodata" /></option>
					</select>
				</div>
			</div>
			<!-- /.panel-body -->
		</div>
		<!-- /.panel -->
	</div>
	<!-- /.col-lg-8 -->
</div>
<!-- /.row -->
