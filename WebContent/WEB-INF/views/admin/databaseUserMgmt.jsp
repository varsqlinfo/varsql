<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/include/tagLib.jspf"%>

<script>
$(document).ready(function (){
	databaseUserMgmt.init();
});

var databaseUserMgmt = {
	selectObj :{}
	,init :function (){
		var _self = this; 
		_self.initEvt();
		_self.search();
	}
	,initEvt:function(){
		var _self = this; 
		
		$('.searchBtn').on('click',function (){
			_self.search();
		});
		
		$('#searchVal').on('keydown',function (){
			if(window.event.keyCode == 13){
				$('.searchBtn').trigger('click');
			}
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
					_self.addDbManager('down', moveItem);
				}
			}
			,compleateTargetMove : function (moveItem){
				if($.isArray(moveItem)){
					_self.addDbManager('up', moveItem);
				}
			}
		}); 
	}
	,search:function (no){
		var _self = this; 
		
		var param = {
			pageNo : (no?no:1)
			,'searchVal':$('#searchVal').val()
		};
		
		VARSQL.req.ajax({
			data:param
			,url : {type:VARSQL.uri.admin, url:'/main/dblist'}
			,success:function (resData){
					
				var result = resData.items;
	    		var resultLen = result.length;
	    		
	    		if(resultLen==0){
	    			$('.dbinfolist').html('<div class="text-center"><spring:message code="msg.nodata"/></div>');
	    			$('.pageNavigation').pagingNav();
	    			return ; 
	    		}
	    		var strHtm = new Array();
	    		var item; 
	    		for(var i = 0 ;i < resultLen; i ++){
	    			item = result[i];
	    			strHtm.push('<a href="javascript:;" class="list-group-item db-list-item" data-conid="'+item.VCONNID+'">'+item.VNAME +'</a>');
	    		}
	    		
	    		$('.dbinfolist').empty().html(strHtm.join(''));
	    		
	    		$('.db-list-item').on('click', function (){
	    			var sEle = $(this);
	    			$('.db-list-item.active').removeClass('active');	
	    			sEle.addClass('active');
	    			
					var vconnID = sEle.data('conid');	
	    			$('#vconnid').val(vconnID);
	    			
	    			_self.managerList(vconnID);
	    		});
	    		
	    		$('.pageNavigation').pagingNav(resData.page,$.proxy( _self.search, _self ));
		    		
			}
		});
	}
	,addDbManager : function (mode, moveItem){
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
			,url : {type:VARSQL.uri.admin, url:'/managerMgmt/addDbManager'}
			,success:function (resData){
				_self.managerList(param.vconnid);
			}
		});
	}
	,managerList : function (vconnID){
		var _self = this; 
		
		VARSQL.req.ajax({
			data:{
				vconnid: vconnID
			}
			,loadSelector: '.manage-user-detail'
			,url : {type:VARSQL.uri.admin, url:'/managerMgmt/dbManagerList'}
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
	<div class="col-xs-6">
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
				<input type="hidden" id="vconnid" name="vconnid" value="">
				<div class="list-group dbinfolist" ></div>
				<!-- /.list-group -->
				<div class="pageNavigation"></div>
			</div>
			<!-- /.panel-body -->
		</div>
		<!-- /.panel -->
	</div>
	
	<div class="col-xs-6">
		<div class="panel panel-default">
			<div class="panel-heading">
				<spring:message code="admin.managerlist.dbuser" />
			</div>
			<!-- /.panel-heading -->
			<div class="panel-body manage-user-detail">
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
</div>
<!-- /.row -->
