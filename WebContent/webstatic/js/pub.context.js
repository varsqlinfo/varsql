/**
 * pubContextMenu: v0.0.1
 * ========================================================================
 * Copyright 2016 ytkim
 * Licensed under MIT
 * http://www.opensource.org/licenses/mit-license.php
 * url : https://github.com/ytechinfo/pub
 * demo : http://pub.moaview.com/
*/
;(function ($, window, document, undefined) {

var pluginName = "pubContextMenu"
	,initialized = false
	,_datastore = {}
	,pubContextElement= false
	,isContextView = false
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
	
function Plugin(element, options) {
	this.selector = (typeof element=='object') ? element.selector : element;

	this.contextId = 'dropdown-'+getHashCode(this.selector);
	this.options = $.extend({}, defaults, options);
	this.contextData = {};
	this.selectElement = $('');

	if(pubContextElement ===false){
		$('body').append('<div id="pub-context-area"></div>');
		pubContextElement = $('#pub-context-area');
	}
	
	if(initialized===false){
		this.initEvt(); 
		initialized = true; 
	}
		
	this.addContext();
	this.context = $('#'+this.contextId);

	this.loadAfterEvt();

	return this; 
}

Plugin.prototype ={
	init :function(){
		var _this = this;
		defaults = this.options;
		
	}
	,initEvt : function (){
		$(document).on('mousedown.pubcontext', 'html', function (e) {
			if(e.which !==2 && $(e.target).closest('#pub-context-area').length < 1){
				$('#pub-context-area .pub-context').fadeOut(defaults.fadeSpeed, function(){
					var sEle = $(this);
					sEle.css({display:''}).find('.pub-context-sub.drop-left').css('left','').removeClass('drop-left');
				});
			}
		});
	}
	,loadAfterEvt : function(){
		var _this = this;
		var id=_this.contextId;

		if(defaults.preventDoubleContext){
			$(document).on('contextmenu.pubcontext'+_this.contextId, '#'+id+'_wrap .pub-context', function (e) {
				e.preventDefault();
			});
		}

		$(document).on('mouseenter.pubcontext'+this.contextId, '#'+id+'_wrap .pub-context-submenu', function(){
			var sEle = $(this); 
			var $sub = sEle.find('.pub-context-sub:first'),
				subWidth = $sub.width(),
				subLeft = $sub.offset().left,
				collision = (subWidth+subLeft) > window.innerWidth;
	
			if(collision){
				$sub.css('left', '-'+(subWidth/sEle.width()*100)+'%');
				$sub.addClass('drop-left');
			}
		});
	}
	,updatedefaults:function (opts){
		defaults = $.extend({}, defaults, opts);
	}
	,buildMenu : function (data, id, subMenu, depth){
		var _self = this; 
		var subClass = (subMenu) ? ' pub-context-sub' : 'pub-context pub-context-top',
			compressed = defaults.compress ? ' compressed-context' : '',
			$menuHtm = [];
		
		$menuHtm.push('<ul class="pub-context-menu ' + subClass + compressed+'" id="' + id + '">');
 
		var item ,linkTarget = '',itemKey , styleClass;
		for(var i = 0; i<data.length; i++) {
			item = data[i];
			
			styleClass = item.styleClass?item.styleClass:'';
			
			if (typeof item.divider !== 'undefined') {
				$menuHtm.push('<li class="divider '+styleClass+'" context-key="divider"></li>');
				continue;
			}
			
			if (typeof item.header !== 'undefined') {
				$menuHtm.push('<li class="pub-context-header '+styleClass+'" context-key="header">' + item.header + '</li>');
				continue; 
			}
			
			itemKey = depth+'_'+item.key; 
			_self.contextData[itemKey] = item;
		
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
				var subMenuData = _self.buildMenu(item.subMenu, id, true,depth+1);
				$menuHtm.push(subMenuData);
			}
			
			$menuHtm.push('</li>');
		}
		$menuHtm.push('</ul>');

		return $menuHtm.join('');
	}
	,closeContextMenu : function (){
		$('#pub-context-area .pub-context-top').hide();
		isContextView= false; 
	}
	/**
	*
	* 컨텍스트 메뉴 이벤트 처리. 
	*/
	,contextEvent : function (){
		var _self = this,opt = _self.options;
		
		$('#'+_self.contextId+' .pub-context-item').off('click.'+this.contextId);
		$('#'+_self.contextId+' .pub-context-item').on('click.'+this.contextId,function (e){
			var clickEle=$(this);
			
			if(clickEle.hasClass('pub-context-submenu')){
				return ; 
			}else{
				skey = clickEle.attr('context-key');
				var sobj = {
					key : skey
					,item : _self.contextData[skey]
					,list : _self.contextData
					,element : _self.selectElement
					,evt : e
				}
			
				if(jQuery.isFunction(opt.callback)){
					opt.callback.call(sobj, sobj.item.key, sobj.item , sobj.evt);
				}else{
					alert(skey);
				}
				_self.closeContextMenu();
			}
		});
	}
	,addContext : function (){
		var _self = this; 
		var id = _self.contextId
			,opt = _self.options
			,$menu =_self.buildMenu(opt.items, id, false, 0)
			,selector = _self.selector;
		
		$menu = '<div id="'+id+'_wrap" onselectstart="return false" draggable="false">'+$menu+'</div>';
		
		var contextMenu  = $('#'+id+'_wrap'); 

		if(contextMenu.length > 0){
			contextMenu.remove();
		}
		
		pubContextElement.append($menu);
		
		_self.contextEvent();
		var $win = $(window);

		$(document).off('contextmenu.pubcontext'+this.contextId, _self.selector);
		$(document).on('contextmenu.pubcontext'+this.contextId, _self.selector, function (e) {
			e.preventDefault();
			e.stopPropagation();
			
			_self.closeContextMenu();

			isContextView = true;
			//  이전 선택한 클래스 삭제 . 
			_self.selectElement.removeClass(opt.selectCls);

			var clickObj = $(this); //$( e.target ).closest( e.data.selector );
			clickObj.addClass(opt.selectCls);
			_self.selectElement = clickObj; 
			
			var $dd = $('#'+ id);
			
			if(jQuery.isFunction(opt.beforeSelect))	{
				opt.beforeSelect.call(this);
			}

			var eleH = $dd.height()
				,eleW = $dd.width()
				,evtX = e.pageX
				,evtY=e.pageY;
			
			var bottom = $win.scrollTop() + $win.height(),
				right = $win.scrollLeft() + $win.width(),
				height =eleH,
				width = eleW;
			
			var offset = {top : evtY , left: evtX};
			if (offset.top + height > bottom) {
				offset.top -= height;
			}

			if (offset.top < 0) {
				offset.top = 0;
			}

			if (offset.left + width > right) {
				offset.left -= width;
			}

			if (offset.left < 0) {
				offset.left = 0;
			}

			$dd.css(offset).fadeIn(defaults.fadeSpeed);
		});
	}
	,destory:function (){
		
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

	if(!selector){
		return ; 
	}
	var jSelctor = (typeof selector=='object') ? selector.selector : selector;

	var _cacheObject = _datastore[jSelctor];
		
	if(typeof options === 'undefined'){
		return _cacheObject||{}; 
	}
	
	if(typeof _cacheObject === 'undefined'){
		_cacheObject = new Plugin(selector, options);
		_datastore[jSelctor] = _cacheObject;
		return _cacheObject; 
	}else if(typeof options==='object'){
		_cacheObject.destory();
		_cacheObject = new Plugin(selector, options);
		_datastore[jSelctor] = _cacheObject;
		return _cacheObject; 
	}

	if(typeof options === 'string'){
		var callObj =_cacheObject[options]; 
		if(typeof callObj ==='undefined'){
			return options+' not found';
		}else if(typeof callObj==='function'){
			return _cacheObject[options].apply(_cacheObject,args);
		}else {
			return typeof callObj==='function'; 
		}
	}

	return _cacheObject;	
};

})(jQuery, window, document);
