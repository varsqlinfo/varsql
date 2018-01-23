<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/include/tagLib.jspf"%>
<script>
$(document).ready(function (){
	dbUserMgmt.init();
});

var dbUserMgmt ={
	selectObj :{}
	,init:function (){
		var _self = this; 
		_self.search();
		_self.initEvt();
	}
	,initEvt:function (){
		var _self = this; 
		$('.searchBtn').click(function() {
			_self.search();
		});
		
		$('#searchVal').keydown(function(event) {
			if(event.keyCode =='13') _self.search();
		});
		
		$('.item-move').on('click',function (){
			var moveItem = [];
			var mode = $(this).attr('mode'); 
			if(mode =='up'){
				moveItem = _self.selectObj.targetMove()
			}else{
				moveItem = _self.selectObj.sourceMove();
			}
		});
		
		_self.selectObj= $.pubMultiselect('#source', {
			targetSelector : '#target'
			,addItemClass:'text_selected'
			,useMultiSelect : true
			,useDragMove : false
			,useDragSort : false
			,sourceItem : {
				optVal : 'VIEWID'
				,optTxt : 'UNAME'
				,items : []
			}
			,targetItem : {
				optVal : 'VIEWID'
				,optTxt : 'UNAME'
				,items : []
			}
			,compleateSourceMove : function (moveItem){
				if($.isArray(moveItem)){
					_self.addDbUser('down', moveItem);
				}
			}
			,compleateTargetMove : function (moveItem){
				if($.isArray(moveItem)){
					_self.addDbUser('up', moveItem);
				}
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
			data:param
			,url : {gubun:VARSQL.uri.manager, url:'/dbnuser/dbList'}
			,success:function (resData){
					
				var result = resData.items;
	    		var resultLen = result.length;
	    		
	    		if(resultLen==0){
	    			$('#dbinfolist').html('<div class="text-center"><spring:message code="msg.nodata"/></div>');
	    			$('#pageNavigation').pagingNav();
	    			return ; 
	    		}
	    		
	    		var strHtm = [];
	    		var item; 
	    		for(var i = 0 ;i < resultLen; i ++){
	    			item = result[i];
	    			strHtm.push('<a href="javascript:;" class="list-group-item db-list-item" data-conid="'+item.VCONNID+'">'+item.VNAME);
	    			strHtm.push('<span class="pull-right text-muted small"><!--em>4 minutes ago</em--></span></a>');
	    		}
	    		
	    		$('#dbinfolist').html(strHtm.join(''));
	    		
	    		$('.db-list-item').on('click', function (){
	    			var vconnID = $(this).data('conid');		
	    			$('#vconnid').val(vconnID);
	    			_self.dbUserList(vconnID);
	    		});
	    		
	    		$('#pageNavigation').pagingNav(resData.page,$.proxy( _self.search, _self ));
			}
		});
	}
	,dbUserList : function (vconnID){
		var _self = this; 
		
		VARSQL.req.ajax({
			data:{
				vconnid : vconnID
			}
			,url : {gubun:VARSQL.uri.manager, url:'/dbnuser/dbnUserMappingList'}
			,success:function (resData){
				var result = resData.items;
	    		var resultLen = result.length;
				
				if(resultLen==0){
	    			$('#source').html('<spring:message code="msg.nodata" />');
	    			$('#target').html('<spring:message code="msg.nodata" />');
	    			return ; 
	    		}
	    		
	    		var sourceItem = [], targetItem = [];
	    		var item;
	    		for(var i = 0 ;i < resultLen; i ++){
	    			item = result[i];
	    			
	    			item.UNAME =item.UNAME+'('+item.UEMAIL +')';
	    			
	    			if(item.VCONNID){
	    				targetItem.push(item);	
	    			}else{
	    				sourceItem.push(item);
	    			}
	    		}
	    		
	    		_self.selectObj.setItem('source', sourceItem);
	    		_self.selectObj.setItem('target', targetItem);
			}
		});
	}
	,addDbUser : function (mode, moveItem){
		var _self = this; 
		
		if($('#vconnid').val()==''){
			alert('<spring:message code="msg.warning.select" />');
			return ; 
		}
		
		var param ={
			selectItem : moveItem.join(',')
			,vconnid:$('#vconnid').val()
			, mode : mode =='up'? 'del' : 'add'
		};
		
		VARSQL.req.ajax({
			data:param
			,url : {gubun:VARSQL.uri.manager, url:'/dbnuser/addDbUser'}
			,success:function (response){
				_self.dbUserList(param.vconnid);
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
	<div class="col-xs-3">
		<div class="panel panel-default">
			<div class="panel-heading">
				<div class="input-group">
					<input type="text" id="searchVal" name="searchVal" class="form-control">
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
	<div class="col-xs-9">
		<div class="panel panel-default">
			<div class="panel-heading">
				<spring:message code="manage.dbnuser.dbuseuser" />
			</div>
			<!-- /.panel-heading -->
			<div class="panel-body">
				<div class="col-sm-12">
					<ul id="source" class="form-control" style="width:100%;height:200px;">
					  <li><spring:message code="msg.nodata" /></li>
					</ul>
				</div>
				<div class="col-sm-12" style="text-align:center;padding:10px;">
					<a href="javascript:;" class="btn_m mb05 item-move" mode="down"><span class="glyphicon glyphicon-chevron-down"></span>추가</a>
					<a href="javascript:;" class="btn_m mb05 item-move" mode="up"><span class="glyphicon glyphicon-chevron-up"></span>삭제</a>
				</div>
				<div class="col-sm-12">
					<ul id="target"  class="form-control" style="width:100%;height:200px;">
					  <li><spring:message code="msg.nodata" /></li>
					</ul>
				</div>
			</div>
			<!-- /.panel-body -->
		</div>
		<!-- /.panel -->
	</div>
	<!-- /.col-lg-8 -->
</div>
<!-- /.row -->
