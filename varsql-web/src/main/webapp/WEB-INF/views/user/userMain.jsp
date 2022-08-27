<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/include/tagLib.jspf"%>
<style>


</style>
<script>
(function() {

VARSQL.unload('top');

var $userMain = {
	_userConnectionInfo :'#user_connection_info'
	,_connectionTab :'#user_connection_info_tab'
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
				,'name' : sEle.text()
			};

			_self.addTabInfo(sItemInfo);
		});
	}
	,initTab : function (){
		var _self =this;

		_self.tabObj = $.pubTab('#connection_tab',{
			items :_self.tabInfo
			,height : 20
			,dropdownWidth : 150
			,dropdownHeight : 200
			,drag: {
				enabled :true
				,dragDrop : function (moveItem){
					_self.saveConnTabInfo(moveItem , 'moveTab');
				}
			}
			,activeFirstItem : false
			,enableClickEventChange : true
			,contentViewSelector : '#dbBrowserContainer'
			,contentStyleClass : 'db-browser-content'
			,contentRender : function (item, addContentElement){
				
				if(item.blockDb ===true){
					callback(_self.userDbBlockTemplate);
					return ; 
				}
				
				var sconid = item.conuid;

				item.url= VARSQL.url(VARSQL.uri.database)+'/?conuid='+sconid;
				
				addContentElement.empty().html(VARSQL.util.renderHtml(_self.iframeLoadTemplate, item));
				
				item.isContentLoad = false;
				addContentElement.find('iframe').on('load',function(){
					addContentElement.find('.browser-loading-msg').remove();
					item.isContentLoad = true; 
				});
			}
			,itemKey :{							// item key mapping
				title :'name'
				,id :'conuid'
			}
			,titleIcon :{
				left :{
					onlyActiveView : true
					,html :  '<i class="fa fa-refresh" style="cursor:pointer;"></i>'
					,click : function (item, idx){
						_self.viewDb(item , true);
					}
				}
				,right :{
					html :  '<i class="fa fa-close" style="cursor:pointer;"></i>'
					,click : function (item, idx){
						var tmpid = item.conuid;

						if(item.blockDb==true){
							_self.tabObj.removeItem(item);
							return ;
						}

						if(!confirm(VARSQL.messageFormat('varsql.0033', {name : item.name}))) return ;

						_self.tabObj.removeItem(item);

						_self.saveConnTabInfo(item , 'del');

						$('#wrapper_'+tmpid).remove();

						if(_self.tabObj.getItemLength() < 1){
							$(_self._userConnectionInfo).val('');
						}
					}
				}
			}
			,click : function (item, customInfo){
				var sconid = item.conuid;
				
				$(_self._userConnectionInfo).val(sconid);

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
		}
	}
	,viewDb : function (item , refreshFlag){
		if(item.isContentLoad !== true) {
			alert('loading...');
			return ;
		}
		
		if(refreshFlag){
			if(!confirm(VARSQL.messageFormat('msg.refresh'))){
				return ;
			}
		}
		
		this.tabObj.reloadContent(item);
	}
	// add tab
	,addTabInfo : function (sItem){
		var _self =this;

		if(_self.tabObj.isItem(sItem.conuid)){
			_self.tabObj.itemClick(sItem , {view:true});
			return ;
		}

		_self.saveConnTabInfo(sItem , 'add'); // conn tab 정보 저장.

		_self.tabObj.addItem({item:sItem} , {mode:'add'});

	}
	// connection tab info save
	,saveConnTabInfo : function (item, mode){
		var _self =this;

		var params ={};
		params.mode = mode;

		if(mode=='moveTab'){
			params.conuid = item.moveItem.conuid;
			params.prevConuid = (item.afterPrevItem ||{}).conuid ||'';
			params.firstConuid = _self.tabObj.getFirstItem().conuid;
		}else {
			params.conuid = item.conuid;
			
			if(mode=='add'){
				params = VARSQL.util.objectMerge(params,item);
				params.prevConuid = (_self.tabObj.getLastItem().conuid ||'');
			}
		}	
		
		VARSQL.req.ajax({
		    url:{type:VARSQL.uri.database, url:'/connTabInfo'}
		    ,data:params
		    ,success:function (res){
		    	var item = res.item;

			}
		});
	}
	,activeClose : function (){
		this.tabObj.rightIconClick(this.tabObj.getSelectItem());
	}
	,blockTab : function (tabInfo){
		var sconid = tabInfo.vconuid;
		
		if(this.tabObj.isItem(sconid)){
			this.tabObj.updateItem({item:{"conuid": sconid, blockDb: true}});
			this.tabObj.reloadContent(this.tabObj.getItem(sconid));
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
			$userMain.viewDb(activeItem.item);
		}else{
			location.reload();
		}
	}
	,isDbActive: function (conuid){
		return $userMain.tabObj.isActive(conuid);
	}
}

}());

</script>
<!-- Page Heading -->

<script id="iframeLoadTemplate" type="text/varsql">
	<iframe src="{{url}}" style="width:100%;height:calc(100% - 2px);" frameborder="0"></iframe>

	<div class="browser-loading-msg">
		<div style="display:table-cell;text-align:center;vertical-align: middle;font-size: 3em;">
			<img src="${pageContext.request.contextPath}/webstatic/css/images/loading.gif">
			<div>[{{name}}] <spring:message code="msg.dbdata.load" text="db loading" /> </div>
		</div>
	</div>
</script>

<div class="user-main-body wh100">
	<div class="wh100" id="dbBrowserContainer">
		<div class="db-browser-content" style="z-index:0;display:table;">
			<div class="var-db-select-text" style="display:table-cell;text-align:center;vertical-align: middle;font-size: 3em;">
				<spring:message code="msg.connect.db.select" arguments="${pageContext.request.contextPath}" text="Select database" />
			</div>
		</div>
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

