/**
 * pubContextMenu: v0.0.1
 * ========================================================================
 * Copyright 2016 ytkim
 * Licensed under MIT
 * http://www.opensource.org/licenses/mit-license.php
 * url : https://github.com/ytechinfo/pub
 * demo : http://pub.moaview.com/
*/
;(function ($) {

var pluginName = "pubContextMenu"
	,_pubContextUid = 0
	,initialized = false
	,_datastore = {}
	,pubContextElement= false
	,isContextView = false
	,_$win = $(window)
	,defaults = {
		fadeSpeed: 100			
		,filter: function ($obj) {
			// Modify $obj, Do not return
		}
		,bgiframe:true
		,above: 'auto'
		,preventDoubleContext: true
		,compress: true
		,selectCls : 'item_select'
		,callback:function (key){
			alert(key)
		}
		,beforeSelect :function (item){
			
		}
	};

function isUndefined(obj){
	return typeof obj==='undefined';
}

function getUid(){
	return pluginName +'_'+ (++_pubContextUid); 
}

function getHashCode (str){
    var hash = 0;
    if (str.length == 0) return hash;
    for (var i = 0; i < str.length; i++) {
        var tmpChar = str.charCodeAt(i);
        hash = ((hash<<5)-hash)+tmpChar;
        hash = hash & hash; 
    }
    return (hash+'').replace(/-/gi,'1_');
}
	
function Plugin(selector, options , uuid) {
	this.selector = selector;
	this.contextId = 'dropdown-pubcontext-'+getHashCode(selector);
	this.options = $.extend({}, defaults, options);
	this.contextData = {};
	this.selectElement;

	if(pubContextElement ===false){
		$('body').append('<div id="pub-context-area"></div>');
		pubContextElement = $('#pub-context-area');
	}
	
	if(initialized===false){
		this.initEvt(); 
		initialized = true; 
	}

	this.addContext();
	this.contextElement = $('#'+this.contextId);

	this.loadAfterEvt();

	return this; 
}

Plugin.prototype ={
	init :function(){
		var _this = this;

	}
	,initEvt : function (){
		var _this = this; 
		$(document).on('mousedown.pubcontext', 'html', function (e) {
			if(e.which !==2 && $(e.target).closest('#pub-context-area').length < 1){
				$('#pub-context-area .pub-context').fadeOut(_this.options.fadeSpeed, function(){
					var sEle = $(this);
					sEle.css({display:''}).find('.pub-context-sub.drop-left').css('left','').removeClass('drop-left');
				});
			}
		});
	}
	,loadAfterEvt : function(){
		var _this = this;
		var id=_this.contextId;

		if(_this.options.preventDoubleContext){
			$(document).on('contextmenu.pubcontext'+_this.contextId, '#'+id+'_wrap .pub-context', function (e) {
				e.preventDefault();
			});
		}

		$(document).on('mouseenter.pubcontext'+this.contextId, '#'+id+'_wrap .pub-context-submenu', function(e){
			var sEle = $(this); 
			var $sub = sEle.find('.pub-context-sub:first')
				, subWidth = $sub.width()
				, subLeft = $sub.offset().left
				, collision = (subWidth+subLeft) > _$win.width();
				
			if(collision){
				$sub.css('left' , '-'+(subWidth/sEle.width()*100)+'%');
				$sub.addClass('drop-left');
			}
			
			var offTop = sEle.offset().top
				, subHeight =$sub.height()
				, screenBottom = (_$win.scrollTop() + _$win.height())

			if(offTop + subHeight+10> screenBottom){
				offTop = ((offTop + subHeight +10) -screenBottom);
				offTop = offTop < 0 ? 0 : offTop;
				$sub.css('top' ,'-'+ offTop+'px');
			}else{
				$sub.css('top' ,'');
			}
		});
	}
	,updatedefaults:function (opts){
		defaults = $.extend({}, defaults, opts);
	}
	,buildMenu : function (data, id, subMenu, depth){
		var _this = this; 
		var subClass = (subMenu) ? ' pub-context-sub' : 'pub-context pub-context-top',
			compressed = _this.options.compress ? ' compressed-context' : '',
			$menuHtm = [];
		
		$menuHtm.push('<ul class="pub-context-menu ' + subClass + compressed+'" id="' + id + '">');
 
		var dateLen =data.length,item ,linkTarget = '',itemKey , styleClass;
		for(var i = 0; i< dateLen ; i++) {
			item = data[i];
			
			styleClass = (item.styleClass?item.styleClass:'') + (item.disabled===true ?' disabled' :'');
			
			if (typeof item.divider !== 'undefined') {
				$menuHtm.push('<li class="divider '+styleClass+'" context-key="divider"></li>');
				continue;
			}
			
			if (typeof item.header !== 'undefined') {
				$menuHtm.push('<li class="pub-context-header '+styleClass+'" context-key="header">' + item.header + '</li>');
				continue; 
			}

			if(item.checkbox ===true){
				$menuHtm.push('<li class="pub-context-header '+styleClass+'" context-key="checkbox"><label for="pubContext_'+item.key+'"><input type="checkbox" id="pubContext_'+item.key+'" /> <span>'+item.name+'</span></label></li>');
				continue; 
			}
			
			itemKey = depth+'_'+item.key; 
			_this.contextData[itemKey] = item;
		
			if (typeof item.target !== 'undefined') {
				linkTarget = ' target="'+item.target+'"';
			}

			if (typeof item.subMenu !== 'undefined') {
				$menuHtm.push('<li class="pub-context-submenu pub-context-item '+styleClass+'" context-key="'+itemKey+'"><a tabindex="-1"><span class="pub-context-item-title">' + item.name +'</span><span class="pub-context-hotkey-empty"></span></a>');
			} else {
				var hotkeyHtm = item.hotkey||'';
				hotkeyHtm = hotkeyHtm !='' ?'<span class="pub-context-hotkey">'+ item.hotkey +'</span>':'';
				$menuHtm.push('<li class="pub-context-item '+styleClass+'" context-key="'+itemKey+'"><a tabindex="-1"><span class="pub-context-item-title">' + item.name +'</span>'+hotkeyHtm+'</a>');
			}

			if (typeof item.subMenu != 'undefined') {
				var subMenuData = _this.buildMenu(item.subMenu, id, true,depth+1);
				$menuHtm.push(subMenuData);
			}
			
			$menuHtm.push('</li>');
		}
		$menuHtm.push('</ul>');

		return $menuHtm.join('');
	}
	/**
     * @method closeContextMenu
     * @description close context menu
     */
	,closeContextMenu : function (){
		$('#pub-context-area .pub-context-top').hide();
		isContextView= false; 
	}
	/**
     * @method contextEvent
     * @description context item event 
     */
	,contextEvent : function (){
		var _this = this,opt = _this.options;
		
		$('#'+_this.contextId+' .pub-context-item').off('click.'+this.contextId);
		$('#'+_this.contextId+' .pub-context-item').on('click.'+this.contextId,function (e){
			var clickEle=$(this);

			if(clickEle.hasClass('disabled')){
				return ; 
			}else if(clickEle.hasClass('pub-context-submenu')){
				return ; 
			}else{
				skey = clickEle.attr('context-key');
				var sobj = {
					key : skey
					,item : _this.contextData[skey]
					,list : _this.contextData
					,element : _this.selectElement
					,evt : e
				}
			
				if(jQuery.isFunction(opt.callback)){
					opt.callback.call(sobj, sobj.item.key, sobj.item , sobj.evt);
				}else{
					alert(skey);
				}
				_this.closeContextMenu();
			}
		});
	}
	/**
     * @method disableItem
     * @description disabled item 
     */
	,disableItem : function (itemKey , depth){
		this.contextElement.find('[context-key="'+depth+'_'+itemKey+'"]').addClass('disabled')
	}
	/**
     * @method enableItem
     * @description enabled item
     */
	,enableItem : function (itemKey , depth){
		if(typeof itemKey !== 'undefined'){
			this.contextElement.find('[context-key="'+depth+'_'+itemKey+'"]').removeClass('disabled');
		}else{
			this.contextElement.find("[context-key].disabled").removeClass('disabled');
		}
	}
	/**
     * @method getCheckBoxId
     * @description get checkbox id
     */
	,getCheckBoxId : function (chkid){
		return 'pubContext_'+chkid;
	}
	/**
     * @method addContext
     * @description context menu 이벤트 처리.
     */
	,addContext : function (){
		var _this = this; 
		var id = _this.contextId
			,opt = _this.options
			,$menu =_this.buildMenu(opt.items, id, false, 0)
			,selector = _this.selector;
		
		$menu = '<div id="'+id+'_wrap" onselectstart="return false" draggable="false">'+$menu+'</div>';
		
		var contextMenu  = $('#'+id+'_wrap'); 

		if(contextMenu.length > 0){
			contextMenu.remove();
		}
		
		pubContextElement.append($menu);
		
		_this.contextEvent();

		var disableFlag = $.isFunction(opt.disableItemKey)
			,beforeSelectFlag = $.isFunction(opt.beforeSelect);

		$(document).off('contextmenu.pubcontext'+this.contextId, _this.selector);
		$(document).on('contextmenu.pubcontext'+this.contextId, _this.selector, function (e) {
			e.preventDefault();
			e.stopPropagation();

			if(disableFlag){
				var disableItem = opt.disableItemKey.call(this , opt.items);
				_this.enableItem();

				var disableItemLen = disableItem.length , tmpItem; 
				if(disableItemLen > 0){
					for(var i =0 ; i < disableItemLen ;i++){
						tmpItem = disableItem[i];
						_this.disableItem(tmpItem.key , tmpItem.depth);
					}
				}
			}

			_this.closeContextMenu();

			isContextView = true;
			//  이전 선택한 클래스 삭제 . 
			if(_this.selectElement){
				_this.selectElement.removeClass(opt.selectCls);
			}

			var clickObj = $(this); //$( e.target ).closest( e.data.selector );
			clickObj.addClass(opt.selectCls);
			_this.selectElement = clickObj; 
			
			var $dd = $('#'+ id);
			
			if(beforeSelectFlag){
				opt.beforeSelect.call(this);
			}

			var eleH = $dd.height()
				,eleW = $dd.width()
				,evtX = e.pageX
				,evtY=e.pageY;
			
			var bottom = _$win.scrollTop() + _$win.height(),
				right = _$win.scrollLeft() + _$win.width(),
				height =eleH,
				width = eleW;
			
			var offTop = evtY
				,offLeft = evtX;
			
			offTop = ((offTop + height +20 > bottom )? (bottom- (height+20)) : offTop);
			offTop = offTop < 0 ? 0 : offTop;
			
			offLeft = (offLeft + width > right)? (offLeft-width) : offLeft;
			offLeft = offLeft < 0 ? 0 : offLeft;

			//var log  = {evtX : evtX, evtY : evtY, bottom : bottom, right : right, height : height, width : offTop, offTop : offTop,offLeft: offLeft}
			//console.log(JSON.stringify(log))

			$dd.css({top : offTop , left: offLeft}).fadeIn(opt.fadeSpeed);
		});
	}
	,destroy:function (){
		
		$('#'+this.contextId+'_wrap').find('*').off();
		
		var contextMenu  = $('#'+this.contextId+'_wrap'); 

		if(contextMenu.length > 0){
			contextMenu.remove();
		}
		
		$(document).off('mousedown.'+this.contextId).off('mouseenter.pubcontext'+this.contextId);
		$(document).off('contextmenu.pubcontext'+this.contextId, this.element).off('click.'+this.contextId, '.context-event');

		$._removeData(this.selector)
		delete _datastore[this.selector];
	}
};


$[ pluginName ] = function (selector,options) {
	var _cacheObject = _datastore[selector]; 

	if(isUndefined(_cacheObject)){
	
		_cacheObject = new Plugin(selector, options);
		_datastore[selector] = _cacheObject;
		
		return _cacheObject; 
	}else if(typeof options==='object'){
		
		_cacheObject.destroy();
		_cacheObject = new Plugin(selector, options);
		_datastore[selector] = _cacheObject;
		return _cacheObject; 
	}

	if(typeof options === 'string'){
		var callObj =_cacheObject[options]; 
		if(isUndefined(callObj)){
			return options+' not found';
		}else if(typeof callObj==='function'){
			return _cacheObject[options].apply(_cacheObject,args);
		}else {
			return typeof callObj==='function'; 
		}
	}

	return _cacheObject;
};

})(jQuery);
