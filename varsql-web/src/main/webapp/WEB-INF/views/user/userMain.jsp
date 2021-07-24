<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/include/tagLib.jspf"%>
<script>
(function() {

VARSQL.unload('top');

var $userMain = {
	_userConnectionInfo :'#user_connection_info'
	,_connectionTab :'#user_connection_info_tab'
	,_connectionIframe :'#user_connection_info_iframe'
	,iframeLoadTemplate : ''
	,userDbBlockTemplate : ''
	,tabItemTemplate : ''
	,tabObj : ''
	,tabInfo :( ${conTabInfo} ||[])
	,init : function (){
		var _self = this;
		_self.iframeLoadTemplate = $('#iframeLoadTemplate').html();
		_self.userDbBlockTemplate = $('#userDbBlockTemplate').html();
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
			items :_self.tabInfo
			,height : 20
			,dropItemHeight : 'auto'
			,activeFirstItem :false
			,enableClickEventChange : true
			,itemKey :{							// item key mapping
				title :'name'
				,id :'conuid'
			}
			,titleIcon :{
				left :{
					visible:false
					,overview :false
					,html :  '<i class="fa fa-refresh" style="cursor:pointer;"></i>'
					,click : function (item, idx){
						var sconid = item.conuid;

						_self.viewDb(item , true);
					}
				}
				,right :{
					html :  '<i class="fa fa-close" style="cursor:pointer;"></i>'
					,click : function (item, idx){
						var tmpid = item.conuid;

						if(!confirm(item.name+' db를 닫으시겠습니까?')) return ;

						_self.tabObj.removeItem(item);

						_self.saveConnTabInfo(item , 'del');

						$('.tabs_'+tmpid).remove();
						$('#wrapper_'+tmpid).remove();

						if(_self.tabObj.getItemLength() < 1){
							$(_self._userConnectionInfo).val('');
							$('#connectionMsg').show();
						}
					}
				}
			}
			,click : function (item, customInfo){
				var sconid = item.conuid;

				_self.loadDbPage(item);

				if(customInfo && customInfo.mode != 'add'){
					_self.saveConnTabInfo(item , 'view');
				}

				return ;
			}
		})

		var viewItem= _self.tabInfo.length > 0?_self.tabInfo[0] : false;

		for(var i =0 ;i <_self.tabInfo.length;i++){
			var item = _self.tabInfo[i];

			if(item.viewYn ===true){
				viewItem = item;
				break;
			}
		}

		if(viewItem !== false){
			_self.tabObj.setActive(viewItem);
			_self.loadDbPage(viewItem);
		}
	}
	,viewDb : function (item , refreshFlag){
		var sconid = item.conuid;
		
		if($('#wrapper_'+sconid+'> .connection_select_msg_wrapper').length > 0) {
			alert('loading...');
			return ;
		}
		
		if(refreshFlag){
			if(!confirm('새로고침 하시겠습니까?')){
				return ;
			}
			
			this.viewLoadMessage();
		}
		
		$('.iframe_'+sconid).attr('src', this.getDbClientUrl(sconid));
	}
	// add tab
	,addTabInfo : function (sItem){
		var _self =this;

		var sconid = sItem.conuid;

		if(_self.tabObj.isItem(sconid)){
			_self.tabObj.itemClick(sItem , {view:true});
			return ;
		}

		_self.saveConnTabInfo(sItem , 'add'); // conn tab 정보 저장.

		_self.tabObj.addItem({item:sItem} , {mode:'add'});

		_self.loadDbPage(sItem);

	}
	// load db page
	,loadDbPage : function (sItem){
		var _self =this;

		var sconid = sItem.conuid;

		$(_self._userConnectionInfo).val(sconid);

		if($('#wrapper_'+sconid).length > 0){
			_self.dbShowHide(sconid);
			return ;
		}

		$('#connection_select_msg_wrapper').show();

		sItem.url= _self.getDbClientUrl(sconid);

		$(_self._connectionIframe).append(VARSQL.util.renderHtml(_self.iframeLoadTemplate, sItem));

		$('.iframe_'+sconid).on('load',function(){
			$('#wrapper_'+sconid+'> .connection_select_msg_wrapper').remove();
			_self.viewLoadMessage(true);
		});

		_self.dbShowHide(sconid);

		$('#connectionMsg').hide();
	}
	// db page show hide
	,dbShowHide : function (sconid){
		$('.db_sql_view_area').css('z-index',1);
		$('#wrapper_'+sconid).css('z-index',100);
	}
	// connection tab info save
	,saveConnTabInfo : function (item , mode){
		var _self =this;

		var params;

		params = VARSQL.util.objectMerge ({},item);
		params.mode = mode;

		if(mode=='add'){
			params.prevVconnid = (_self.tabObj.getLastItem().conuid ||'');
		}

		VARSQL.req.ajax({
		    url:{type:VARSQL.uri.database, url:'/connTabInfo'}
		    ,data:params
		    ,success:function (res){
		    	var item = res.item;

			}
		});
	}
	,getDbClientUrl : function (sconid){
		return VARSQL.url(VARSQL.uri.database)+'/?conuid='+sconid;
	}
	,activeClose : function (){
		this.tabObj.rightIconClick(this.tabObj.getSelectItem());
	}
	,blockTab : function (tabInfo){
		var sconid = tabInfo.vconuid;
		$('#wrapper_'+sconid).empty().html(this.userDbBlockTemplate);
	}
	,viewLoadMessage : function (hideFlag){
		if(hideFlag ===true){
			$('#varsql_page_load_msg_wrapper').hide();
		}else{
			$('#varsql_page_load_msg_wrapper').show();
		}
	}
}

$(function (){
	$userMain.init();
})

window.userMain = {
	activeClose : function (){
		$userMain.activeClose();
	}
	,blockTab : function (tabInfo){
		$userMain.blockTab(tabInfo);
	}
	,pageRefresh : function (){
		var activeItem = $userMain.tabObj.getActive();
		if(activeItem.idx > -1){
			this.viewLoadMessage();
			$userMain.viewDb(activeItem.item);
		}else{
			location.reload();
		}
	}
	,viewLoadMessage : function (hideFlag){
		$userMain.viewLoadMessage(hideFlag);
	}
}

}());

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
						<div>[{{name}}] <spring:message code="msg.dbdata.load" text="db loading" /> </div>
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
						<div class="var-db-select-text"><spring:message code="msg.connect.db.select" arguments="${pageContext.request.contextPath}" text="Select database" /></div>
					</td>
				</tr>
			</tbody>
		</table>
	</div>
</div>


<script id="userDbBlockTemplate" type="text/varsql">
<table class="wh100-absolute">
	<tbody>
		<tr>
			<td style="text-align: center; font-size: 3em;">
				<div>DB가 차단되었습니다.</div>
				<div>관리자에게 문의하세요.</div>
			</td>
		</tr>
	</tbody>
</table>
</script>

<table id="varsql_page_load_msg_wrapper" class="wh100-absolute page-reload-msg">
	<tbody>
		<tr>
			<td style="text-align: center; font-size: 3em;">
				<img src="${pageContext.request.contextPath}/webstatic/css/images/loading.gif">
				<div>페이지를 로드중입니다.</div>
			</td>
		</tr>
	</tbody>
</table>