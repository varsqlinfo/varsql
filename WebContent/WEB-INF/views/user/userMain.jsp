<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/include/tagLib.jspf"%>
<%@ include file="/WEB-INF/include/initvariable.jspf"%>

<script>
$(document).ready(function (){
	userMain.init();
})
var userMain = {
	_userConnectionInfo :'#user_connection_info'
	,_connectionTab :'#user_connection_info_tab'
	,_connectionIframe :'#user_connection_info_iframe'
	,init : function (){
		var _self = this; 
		_self.evtInit();
	}
	,evtInit : function (){
		var _self = this; 
		$(_self._userConnectionInfo).on('change',function (){
			var selectObj = $(this);
			
			if(selectObj.val()=='') return ;
			
			_self.tabCntl($(_self._userConnectionInfo+" option:selected"));
		});
	}
	// 탭정보 컨트롤
	,tabCntl:function (selectObj){
		var _self = this;
		var sconid = selectObj.val();
		var tabs = $('.tabs_'+sconid);
		
		$( _self._connectionTab+'> .ui-state-active').removeClass('ui-state-active');
		tabs.addClass('ui-state-active');
		$('.db_sql_view_area').hide();
		
		if(tabs.length > 0){
			$('.iframe_'+sconid).show();
			return ; 
		}
		
		var strHtm='<li class="db-info-tab ui-state-default ui-corner-top connection-ui-tabs-active ui-state-active tabs_'+sconid+'">';
		strHtm+='	<span class="ui-paddingl5-r5 " vconnid="'+sconid+'" vname="'+escape(selectObj.attr('vname'))+'">';
		strHtm+='		<a href="javascript:" class="tab-ui-name-'+sconid+'">'+selectObj.attr('vname')+'</a>&nbsp;';
		strHtm+='		<a href="javascript:" class="tab-ui-close-'+sconid+'">X</a>&nbsp;';
		strHtm+='	</span>';
		strHtm+='</li>';

		$(_self._connectionTab).append(strHtm);
		
		$('.tab-ui-name-'+sconid).on('click',function (){
			var tmpid = $(this).parent().attr('vconnid');
			$(_self._userConnectionInfo).val(tmpid);
			$(_self._userConnectionInfo).trigger( "change" );
		});
		
		$('.tab-ui-close-'+sconid).on('click',function (){
			var pObj = $(this).parent(); 
			var tmpid = pObj.attr('vconnid');
			if(!confirm(unescape(pObj.attr('vname'))+' db를 닫으시겠습니까?')) return ; 
			
			$('.tabs_'+tmpid).remove();
			$('.iframe_'+tmpid).remove();
			
			if($('.db_sql_view_area').length < 1){
				$('#connection_select_msg_wrapper').show();
			}
		});
		var _url = VARSQL.url(VARSQL.uri.database, '/'+selectObj.attr('dbtype'))+'/?vconnid='+sconid; 
		var strHtm = '<iframe class="db_sql_view_area iframe_'+sconid+'" src="'+_url+'" style="width:100%;height:100%;" width="100%" height="100%" frameborder="0"></iframe>';
		
		$(_self._connectionIframe).append(strHtm);
		$('#connection_select_msg_wrapper').hide();
	}
}
</script>
<!-- Page Heading -->

<div class="col-lg-12 fill">
	<div class="row fill" id="user_connection_info_iframe">
		<table id="connection_select_msg_wrapper" style="width: 100%; height: 100%;">
			<tbody>
				<tr>
					<td style="text-align: center; font-size: 3em;">
							접속할 <img src="${pageContextPath}/webstatic/imgs/Database.gif">db를 선택하시오.
					</td>
				</tr>
			</tbody>
		</table>
	</div>
</div>