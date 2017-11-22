<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/include/tagLib.jspf"%>
<script>
$(document).ready(function (){
	databaseUserMgmt.init();
});

var databaseUserMgmt = {
	init :function (){
		var _self = this; 
		_self.initEvt();
		_self.search();
	}
	,initEvt:function(){
		var _self = this; 
		$('.btnSave').on('click',function (){
			if(!confirm('<spring:message code="admin.databaseUserMgmt.adduser.confirm" />')) return ; 
			_self.addDbManager();
		});
		
		$('.searchBtn').on('click',function (){
			_self.search();
		});
		
		$('#searchVal').on('keydown',function (){
			if(window.event.keyCode == 13){
				$('.searchBtn').trigger('click');
			}
		});
	}
	,search:function (no){
		var _self = this; 
		
		var param = {
			page:no?no:1
			,'searchVal':$('#searchVal').val()
		};
		
		VARSQL.req.ajax({
			type:'POST'
			,data:param
			,url : {gubun:VARSQL.uri.admin, url:'/main/dblist'}
			,dataType:'JSON'
			,success:function (response){
				try{
					
		    		var resultLen = response.result?response.result.length:0;
		    		
		    		if(resultLen==0){
		    			$('.dbinfolist').html('<div class="text-center"><spring:message code="msg.nodata"/></div>');
		    			$('.pageNavigation').pagingNav();
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
		    		
		    		$('.dbinfolist').html(strHtm.join(''));
		    		
		    		$('.db-list-item').on('click', function (){
		    			_self.managerList(this);
		    		});
		    		
		    		$('.pageNavigation').pagingNav(response.paging,fnSearch);
		    		
				}catch(e){
					$('.dataViewAreaTd').attr('color','red');
					$(".dataViewAreaTd").val("errorMsg : "+e+"\nargs : " + e.message);  
				}
			}
		});
	}
	,addDbManager:function (){
		if($('#vconid').val()==''){
			alert('<spring:message code="msg.warning.select" />');
			return ; 
		}
		
		var reInfo = new Array();
		$('.dataTableContent1').children().each(function (i, item){
			reInfo.push($(item).val())
		});

		
		var param ={
			selectItem:reInfo.join(',')
			,vconid:$('#vconid').val()
		};
		
		VARSQL.req.ajax({
			type:'POST'
			,data:param
			,url : {gubun:VARSQL.uri.admin, url:'/managerMgmt/addDbManager'}
			,dataType:'JSON'
			,success:function (response){
				
			}
		});
	}
	,managerList:function (sObj){
		sObj = $(sObj);
		$('#vconid').val(sObj.attr('conid'));
		VARSQL.req.ajax({
			type:'POST'
			,data:{
				vconid:sObj.attr('conid')
			}
			,url : {gubun:VARSQL.uri.admin, url:'/managerMgmt/dbManagerList'}
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
	    		for(var i = 0 ;i < resultLen; i ++){
	    			item = result[i];
	    			if(item.VCONNID){
	    				strHtm1.push('	<option value="'+item.VIEWID+'">'+item.UNAME+' / '+item.DEPT_NM+'</option>');	
	    			}else{
	    				strHtm2.push('	<option value="'+item.VIEWID+'">'+item.UNAME+' / '+item.DEPT_NM+'</option>');
	    			}
	    		}
	    		
	    		$('.dataTableContent1').html(strHtm1.join(''));
	    		$('.dataTableContent2').html(strHtm2.join(''));
	    		
	    		var aaa = new selectBoxMove('.dataTableContent2','.dataTableContent1');
	    		aaa.init();
				
			}
		});
	}
}

</script>
<!-- Page Heading -->
<div class="row">
    <div class="col-lg-12">
        <h1 class="page-header"><spring:message code="admin.menu.databaseusermgmt" /></h1>
    </div>
    <!-- /.col-lg-12 -->
</div>
<div class="row">
	<div class="col-sm-6">
		<div class="panel panel-default">
			<div class="panel-heading">
				<div class="input-group">
					<input type="text" value="" id="searchVal" name="searchVal"	class="form-control">
					<span class="input-group-btn searchBtn">
						<button class="btn btn-default" type="button">
							<span class="glyphicon glyphicon-search"></span>
						</button>
					</span>
				</div>
			</div>
			<!-- /.panel-heading -->
			<div class="panel-body">
				<input type="hidden" id="vconid" name="vconid" value="">
				<div class="list-group dbinfolist" ></div>
				<!-- /.list-group -->
				<div class="pageNavigation"></div>
			</div>
			<!-- /.panel-body -->
		</div>
		<!-- /.panel -->
	</div>
	
	<div class="col-sm-6">
		<div class="panel panel-default">
			<div class="panel-heading">
				<spring:message code="admin.managerlist.dbuser" />
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
</div>
<!-- /.row -->
