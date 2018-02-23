<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/include/tagLib.jspf"%>
<script>
$(document).ready(function (){
	userMain.init();
})

var userMain = {
	_userConnectionInfo :'#user_connection_info'
	,_connectionTab :'#user_connection_info_tab'
	,_connectionIframe :'#user_connection_info_iframe'
	,iframeLoadTemplate : ''
	,tabItemTemplate : ''
	,preferencesUrl : '<c:url value="/user/preferences?header=N" />'
	,init : function (){
		var _self = this; 
		_self.iframeLoadTemplate = $('#iframeLoadTemplate').html(); 
		_self.tabItemTemplate = $('#tabItemTemplate').html(); 
		_self.evtInit();
	}
	,evtInit : function (){
		var _self = this; 
		$(_self._userConnectionInfo).on('change',function (){
			var selectObj = $(this);
			
			if(selectObj.val()=='') return ;
			
			var sEle =$(_self._userConnectionInfo+" option:selected");
			_self.tabCntl({
				id : sEle.val()
				,name : sEle.attr('vname')
			});
		});
		
		$(_self._connectionTab).on('click','.tab-ui-name',function (){
			var tmpid = $(this).closest('[conuid]').attr('conuid');
			
			if(tmpid=='preferences'){
				userMain.tabCntl({
					id : 'preferences'
					,name : '환경설정'
				});
				return ; 
				
			}
			$(_self._userConnectionInfo).val(tmpid);
			$(_self._userConnectionInfo).trigger( "change" );
		});
		
		$(_self._connectionTab).on('click','.tab-ui-close',function (){
			var pObj = $(this).closest('[conuid]'); 
			var tmpid = pObj.attr('conuid');
			if(!confirm(pObj.attr('vname')+' db를 닫으시겠습니까?')) return ; 
			
			var tmpDbTabInfo = $(this).closest('.db-info-tab'); 
			
			var viewElement = $(tmpDbTabInfo.prev());
			
			if(viewElement.length < 1){
				viewElement= $(tmpDbTabInfo.next());
			}
			
			$('.tabs_'+tmpid).remove();
			$('#wrapper_'+tmpid).remove();
			
			if(viewElement.length > 0){
				$(viewElement.find('.db-info-tab-item')).trigger('click');	
			}
			
			if($('.db_sql_view_area').length < 1){
				$(_self._userConnectionInfo).val('');
				$('#connectionMsg').show();
			}
		});
		
		$(_self._connectionTab).on('click','.db-info-tab-ui-refresh',function (){
			var pObj = $(this).closest('[conuid]')
				,sconid = pObj.attr('conuid');
			
			if($('#wrapper_'+sconid+'> .connection_select_msg_wrapper').length) {
				alert('로드중입니다.');
				return ;
			}
			
			if(!confirm('새로고침 하시겠습니까?')) return ;
			
			pObj.find('.tab-ui-name').trigger('click');
			
			$('.iframe_'+sconid).attr('src', _self.getDbClientUrl(sconid));
		});
	}
	// 탭정보 컨트롤
	,tabCntl:function (sItem){
		var _self = this;
		var sconid = sItem.id;
		var tabs = $('.tabs_'+sconid);
		
		$('#connectionMsg').hide();
		
		$( _self._connectionTab+'> .ui-tab-item-active').removeClass('ui-tab-item-active');
		tabs.addClass('ui-tab-item-active');
		$('.db_sql_view_area').css('z-index',1);
		
		if(tabs.length > 0){
			$('#wrapper_'+sconid).css('z-index',100);
			return ; 
		}
		
		$('#connection_select_msg_wrapper').show();
		
		$(_self._connectionTab).append(VARSQL.util.renderHtml(_self.tabItemTemplate, sItem));
		
		sItem.url= _self.getDbClientUrl(sconid);
		 
		$(_self._connectionIframe).append(VARSQL.util.renderHtml(_self.iframeLoadTemplate, sItem));
		
		$('.iframe_'+sconid).load( function(){
			$('#wrapper_'+sconid+'> .connection_select_msg_wrapper').remove();
		});
	}
	,activeClose : function (){
		$(this._connectionTab+'> .ui-tab-item-active .tab-ui-close').trigger('click');
	}
	,getDbClientUrl : function (sconid){
		if(sconid =='preferences'){
			return this.preferencesUrl; 
		}else{
			return VARSQL.url(VARSQL.uri.database)+'/?conuid='+sconid;
		}
	} 
}
</script>
<!-- Page Heading -->
<script id="tabItemTemplate" type="text/varsql">
<li class="db-info-tab ui-tab-item ui-tab-item-active tabs_{{id}}">
	<span class="ui-paddingl5-r5 " conuid="{{id}}" vname="{{name}}">
		<a href="javascript:" class="db-info-tab-ui-refresh" title="refresh"><span class="fa fa-refresh"></span></a>&nbsp
		<a href="javascript:" class="db-info-tab-item tab-ui-name">{{name}}</a>&nbsp
		<a href="javascript:" class="db-info-tab-item-close tab-ui-close">X</a>&nbsp
	</span>
</li>
</script>
<script id="iframeLoadTemplate" type="text/varsql">
<div id="wrapper_{{id}}" class="db_sql_view_area" style="height:100%;width:100%;z-index:100;position:absolute;background-color:#ddd;">
	<iframe class="iframe_{{id}}" src="{{url}}" style="width:100%;height:100%;" frameborder="0"></iframe>

	<table class="connection_select_msg_wrapper"  style="width: 100%; height: 100%;position:absolute;z-index:200;top:0px;">
		<tbody>
			<tr>
				<td style="text-align: center; font-size: 3em;">
					<div class="var-load-frame">
						<img src="${pageContext.request.contextPath}/webstatic/css/images/loading.gif">
						<div>[{{name}}] db 정보를 로드중입니다.</div>
					</div>
				</td>
			</tr>
		</tbody>
	</table>
</div>
</script>

<div class="col-lg-12 fill" style="height:100%;">
	<div class="row fill" id="user_connection_info_iframe" style="height:100%;">
		<table id="connectionMsg"  style="width: 100%; height: 100%;position:absolute;z-index:200;top:0px;">
			<tbody>
				<tr>
					<td style="text-align: center; font-size: 3em;">
						<div class="var-db-select-text">접속할 <img src="${pageContext.request.contextPath}/webstatic/imgs/Database.gif">db를 선택하시오.</div>	
					</td>
				</tr>
			</tbody>
		</table>
	</div>
</div>
<div id="memoTemplate_view_dialog" style="display:none;" title="메시지">
	<div style="margin: 0px -10px 0px -10px;">
		<textarea id="memo_content" name="memo_content" class="form-control" rows="12" placeholder="내용"></textarea>
	</div>
</div>