<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/include/tagLib.jspf"%>
<script>

VARSQL.unload();

var userMain = {
	_userConnectionInfo :'#user_connection_info'
	,_connectionTab :'#user_connection_info_tab'
	,_connectionIframe :'#user_connection_info_iframe'
	,iframeLoadTemplate : ''
	,tabItemTemplate : ''
	,tabObj : ''
	,preferencesUrl : '<c:url value="/user/preferences?header=N" />'
	,init : function (){
		var _self = this; 
		_self.iframeLoadTemplate = $('#iframeLoadTemplate').html(); 
		_self.initTab();
		_self.evtInit();
	}
	,evtInit : function (){
		var _self = this; 
		
		$(_self._userConnectionInfo).on('change',function (){
			var selectObj = $(this);
			
			if(selectObj.val()=='') return ;
			
			var sEle =$(_self._userConnectionInfo+" option:selected");
			
			var sItemInfo = {
				conuid : sEle.val()
				,'name' : sEle.attr('vname')
			};
			
			_self.addTabInfo(sItemInfo);
		});
	}
	,initTab : function (){
		var _self =this; 
		
		_self.tabObj = $.pubTab('#connection_tab',{
			items :[]
			,height : 20
			,dropItemHeight : 'auto'
			,itemKey :{							// item key mapping
				title :'name'
				,id :'conuid'
			}
			,titleIcon :{
				left :{
					html :  '<i class="fa fa-refresh" style="cursor:pointer;"></i>'
					,click : function (item, idx){
						var sconid = item.conuid;
					
						if($('#wrapper_'+sconid+'> .connection_select_msg_wrapper').length) {
							alert('로드중입니다.');
							return ;
						}
						
						if(!confirm('새로고침 하시겠습니까?')) return ;
						
						$('.iframe_'+sconid).attr('src', _self.getDbClientUrl(sconid));
					}
				}
				,right :{
					html :  '<i class="fa fa-close" style="cursor:pointer;"></i>'
					,click : function (item, idx){
						var tmpid = item.conuid;
						
						if(!confirm(item.name+' db를 닫으시겠습니까?')) return ; 
						
						_self.tabObj.removeItem(item);
						
						$('.tabs_'+tmpid).remove();
						$('#wrapper_'+tmpid).remove();
						
						if(_self.tabObj.getItemLength() < 1){
							$(_self._userConnectionInfo).val('');
							$('#connectionMsg').show();
						}
					}
				}
			}
			,click : function (item){
				var sconid = item.conuid; 
				
				$('.db_sql_view_area').css('z-index',1);
				$('#wrapper_'+sconid).css('z-index',100);
				
				if(sconid =='preferences'){
					$(_self._userConnectionInfo).val('');
					return ; 
				}
				
				$(_self._userConnectionInfo).val(sconid);
				
				return ; 
			}
		})
	}
	// add tab
	,addTabInfo : function (sItem){
		var _self =this;		
		
		var sconid = sItem.conuid; 
		
		if(_self.tabObj.isItem(sconid)){
			_self.tabObj.itemClick(sItem);
			return ; 
		}
		
		_self.tabObj.addItem({item:sItem});
		
		$('#connection_select_msg_wrapper').show();
		
		sItem.url= _self.getDbClientUrl(sconid);
		 
		$(_self._connectionIframe).append(VARSQL.util.renderHtml(_self.iframeLoadTemplate, sItem));
		
		$('.iframe_'+sconid).on('load',function(){
			$('#wrapper_'+sconid+'> .connection_select_msg_wrapper').remove();
		});
		
		$('#connectionMsg').hide();
	}
	,getDbClientUrl : function (sconid){
		if(sconid =='preferences'){
			return this.preferencesUrl; 
		}else{
			return VARSQL.url(VARSQL.uri.database)+'/?conuid='+sconid;
		}
	} 
}

$(function (){
	userMain.init();
})

</script>
<!-- Page Heading -->

<script id="iframeLoadTemplate" type="text/varsql">
<div id="wrapper_{{conuid}}" class="db_sql_view_area" style="height:100%;width:100%;z-index:100;position:absolute;background-color:#ddd;">
	<iframe class="iframe_{{conuid}}" src="{{url}}" style="width:100%;height:100%;" frameborder="0"></iframe>

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

<div class="user-main-body wh100">
	<div class="wh100" id="user_connection_info_iframe">
		<table id="connectionMsg" class="wh100-absolute" style="z-index:200;top:0px;">
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
<div id="memoTemplate_view_dialog" style="display:none;">
	<div class="memo-view-area">
		<textarea id="memo_content" name="memo_content"></textarea>
	</div>
</div>