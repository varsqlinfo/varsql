<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/include/tagLib.jspf"%>
<!-- Brand and toggle get grouped for better mobile display -->
<div class="navbar-header">
	<ul class="nav navbar-nav">
        <li class="dropdown">
        	<div class="form-horizontal" >
				<div class="ui-connection-list">
					<label>Connect to : </label> 
					
					<select id="user_connection_info">
						<option value="">----connection info---</option>
						<c:forEach items="${dblist}" var="tmpInfo" varStatus="status">
							<option value="${tmpInfo.vconnid}" dbtype="${tmpInfo.type}" vname="${tmpInfo.name}">${tmpInfo.name}</option>
						</c:forEach>
					</select>
				</div>
				
				<div class="ui-connection-tabs-wrap">
					<div class="connection-ui-tabs">
						<ul class="connection-ui-tabs-nav" id="user_connection_info_tab">
						</ul>
					</div>
				</div>
			</div>
        </li>
    </ul>
</div>
 <!-- Top Menu Items -->
<ul class="navbar-top-links navbar-right">
	<li class="dropdown">
        <a href="javascript:;" class="dropdown-toggle ui-memo-btn" data-toggle="dropdown" >
            <i class="fa fa-bell fa-fw"></i> <i class="fa fa-caret-down"></i>
        </a>
        <ul id="memo_alert_area" class="dropdown-menu dropdown-alerts">
        </ul>
    </li>
    <li class="dropdown">
        <a href="javascript:;" class="dropdown-toggle" data-toggle="dropdown"><spring:message code="screen"/><b class="caret"></b></a>
	     <ul class="dropdown-menu alert-dropdown">
	         <%@ include file="/WEB-INF/include/screen.jspf"%>
	     </ul>
	 </li>
	 <li class="dropdown">
	     <a href="#" class="dropdown-toggle" data-toggle="dropdown">
	     	<sec:authentication property="principal.username" /> <b class="caret"></b></a>
	     <ul class="dropdown-menu">
	         <li>
	             <a href="#"><i class="fa fa-fw fa-user"></i> <spring:message code="btn.profile"/></a>
	         </li>
	         <li>
	             <a href="#"><i class="fa fa-fw fa-gear"></i> <spring:message code="btn.setting"/></a>
	         </li>
	         <li class="divider"></li>
	         <li>
	             <a href="<c:url value="/logout" />"><i class="fa fa-fw fa-power-off"></i> <spring:message code="btn.logout"/></a>
	            </li>
	        </ul>
    </li>
</ul>
<script>

var userHeader = {
	memoDialog:false
	,init : function (){
		var _self = this; 
		_self.initEvt();
	}
	,initEvt :function (){
		var _self = this;
		$('.ui-memo-btn').on('click',function(e){
			_self.messageLoad();
		})
	}
	, messageLoad: function (){
		var _self = this; 
		VARSQL.req.ajax({
		    type:"POST"
		    ,url:{gubun:VARSQL.uri.user, url:'/message.vsql'}
		    ,dataType:'json'
		    ,data:{}
		    ,success:function (res){
		    	var items = res.items;
		    	var paging = res.paging;
		    	var strHtm = []
		    		,len = items.length;
		    	
		    	if(items.length > 0){
		    		for(var i =0 ;i <len; i++){
		    			var item = items[i];
		    			
		    			strHtm.push('<li>');
		    			strHtm.push('    <a href="javascript:;" class="memo-item" _idx="'+i+'">');
		    			strHtm.push('         <div>');
		    			strHtm.push('             <i class="fa fa-envelope fa-fw"></i>'+ item.MEMO_TITLE);
		    			strHtm.push('            <span class="pull-right text-muted small">'+item.REG_DT+'</span>');
		    			strHtm.push('		</div>');
		    			strHtm.push('     </a>');
		    			strHtm.push(' </li>');
		    		}
		    	}else{
		    		strHtm.push('<li>no data</li>')
		    	}
		    	
		    	$('#memo_alert_area').empty().html(strHtm.join(''));
		    	$('#memo_alert_area .memo-item').on('click', function (){
		    		var idx = $(this).attr('_idx');
		    		_self.memoDetail(items[idx]);
		    	})
		    	
		    	
			}
			,error :function (data, status, err){
				VARSQL.log.error(data, status, err);
			}
		});
	}
	,memoDetail : function (item){
		var _self = this;
		
		$('#memo_content').val(item.MEMO_CONT);
		
		
		if(item.UPD_DT==null){
			VARSQL.req.ajax({
			    type:"POST"
			    ,url:{gubun:VARSQL.uri.user, url:'/updMsgViewDt.vsql'}
			    ,dataType:'json'
			    ,data:{memo_id : item.MEMO_ID}
			    ,success:function (res){
			    	
				}
				,error :function (data, status, err){
					VARSQL.log.error(data, status, err);
				}
			});
		}
		if(_self.memoDialog===false){
			_self.memoDialog = $('#memoTemplate_view_dialog').dialog({
				height: 350
				,width: 640
				,autoOpen: false
				,modal: true
				,buttons: {
					"닫기":function (){
						_self.memoDialog.dialog( "close" );
					}
				}
				,close: function() {
					_self.memoDialog.dialog( "close" );
				}
			});
		}
		
		_self.memoDialog.dialog( "option" , "title" ,item.MEMO_TITLE+'('+item.REG_DT+')');
		_self.memoDialog.dialog( "open" );
	
	}
}

$(document).ready(function (){
	userHeader.init();
})
</script>