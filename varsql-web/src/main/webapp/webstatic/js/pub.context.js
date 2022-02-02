/**
 * pubContextMenu: v1.0.2
 * ========================================================================
 * Copyright 2016-2021 ytkim
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
	,_$win = $(window)
	,_defaults = {
		fadeSpeed: 100				// 숨김 속도
		,filter: function ($obj) {		// 필터
			// Modify $obj, Do not return
		}
		,theme : 'light'			// 테마  light , dark
		,isStopPropagation: true	// 이벤트 전파 차단 여부
		,selectCls : 'item_select'	// item select class
		,callback:function (key){	// item click callback
			alert(key)
		}
		,beforeSelect :function (item){	// 선택전 이벤트.

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
	this.options = $.extend({}, _defaults, options);
	this.contextData = {};
	this.selectElement;
	this.targetInfo;

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

$(document).on('mousedown.pubcontext', 'html', function (e) {
	if(e.which !==2 && $(e.target).closest('#pub-context-area').length < 1){
		$('#pub-context-area .pub-context-top').css({display:''}).find('.pub-context-sub.drop-left').css('left','').removeClass('drop-left');
	}
});

Plugin.prototype ={
	init :function(){
		var _this = this;

	}
	,initEvt : function (){
		var _this = this;

	}
	,loadAfterEvt : function(){
		var _this = this;
		var _opt = this.options;
		var id=_this.contextId;

		if(_opt.isStopPropagation){
			$('#'+id+'_wrap').on('contextmenu.pubcontext'+_this.contextId, '.pub-context-top', function (e) {
				e.preventDefault();
				e.stopPropagation();
			});
		}

		// sub menu click
		$('#'+id+'_wrap').on('mouseenter.pubcontext'+this.contextId, '.pub-context-submenu', function(e){
			var sEle = $(this);

			//sEle.closest('.pub-context-menu').find('.pub-context-submenu.on').removeClass('on');
			sEle.addClass('on');

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
		}).on('mouseleave.pubcontext'+this.contextId, '.pub-context-submenu', function(e){
			if(headerClickFlag){
				headerClickFlag = false;
				return ;
			}
			var sEle = $(this);
			sEle.removeClass('on');
		});

		var headerClickFlag = false; // header click check flag
		$('#'+id+'_wrap').on('click.item.'+_this.contextId ,'.pub-context-header',function (e){
			headerClickFlag =true;
			setTimeout(function(){ headerClickFlag = false}, 200)
		});

		// click event
		$('#'+id+'_wrap').on('click.item.'+_this.contextId ,'.pub-context-item',function (e){
			var clickEle=$(this);

			if(clickEle.hasClass('disabled')){
				return ;
			}else if(clickEle.hasClass('pub-context-submenu')){
				return ;
			}else{
				skey = clickEle.attr('context-key');

				var contentItem = _this.contextData[skey]; 
				var sobj = {
					key : skey
					,item : contentItem
					,list : _this.contextData
					,element : _this.selectElement
					,evt : e
				}

				if(jQuery.isFunction(contentItem.callback)){
					contentItem.callback.call(sobj, sobj.item.key, sobj.item , sobj.evt);
				}else if(jQuery.isFunction(_opt.callback)){
					_opt.callback.call(sobj, sobj.item.key, sobj.item , sobj.evt);
				}else{
					alert(skey);
				}
				
				_this.closeContextMenu();
			}
		});
	}
	,updatedefaults:function (opts){
		_defaults = $.extend({}, _defaults, opts);
	}
	,buildMenu : function (data, id, subMenu, depth){
		var _this = this;
		var subClass = (subMenu) ? ' pub-context-sub' : ' pub-context-top',
			$menuHtm = [];

		$menuHtm.push('<ul class="pub-context-menu ' + subClass +'" id="' + id + '">');

		var dateLen =data.length,item ,linkTarget = '',itemKey , styleClass;
		for(var i = 0; i< dateLen ; i++) {
			item = data[i];

			if(isUndefined(item)) continue ;

			styleClass = (item.styleClass?item.styleClass:'') + (item.disabled===true ?' disabled' :'');

			itemKey = depth+'_'+(item.key||'');

			if (typeof item.divider !== 'undefined') {
				$menuHtm.push('<li class="divider '+styleClass+'" context-key="divider"></li>');
				continue;
			}

			if (typeof item.header !== 'undefined') {
				$menuHtm.push('<li class="pub-context-header '+styleClass+'" context-key="'+itemKey+'_header">' + item.header + '</li>');
				continue;
			}

			if(item.checkbox ===true){
				$menuHtm.push('<li class="pub-context-header '+styleClass+'" context-key="checkbox"><label for="pubContext_'+item.key+'"><input type="checkbox" id="pubContext_'+item.key+'" /> <span>'+item.name+'</span></label></li>');
				continue;
			}

			_this.contextData[itemKey] = item;

			if (typeof item.target !== 'undefined') {
				linkTarget = ' target="'+item.target+'"';
			}

			if (typeof item.subMenu !== 'undefined') {
				$menuHtm.push('<li class="pub-context-submenu '+styleClass+'" context-key="'+itemKey+'"><a tabindex="-1"><span class="pub-context-item-title">' + item.name +'</span><span class="pub-context-hotkey-empty"></span></a>');
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
	}
	/**
     * @method contextEvent
     * @description context item event
     */
	,contextEvent : function (){

	}
	/**
     * @method disableItem
     * @description disabled item
     */
	,disableItem : function (itemKey , depth){
		this.contextElement.find('[context-key="'+depth+'_'+itemKey+'"]').addClass('disabled')
	}
	/**
     * @method changeName
     * @description change name
     */
	,changeName : function (itemKey ,depth, name){
		this.contextElement.find('[context-key="'+depth+'_'+itemKey+'"] .pub-context-item-title').text(name);
	}
	/**
     * @method changeHeader
     * @description change header item name
     */
	,changeHeader : function (itemKey ,depth, name){
		this.contextElement.find('[context-key="'+(depth+'_'+(itemKey||''))+'_header"]').text(name);
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
     * @method setTheme
     * @description set theme
     */
	,setTheme : function (themeName){
		this.options.theme = themeName;
		$('#'+this.contextId+'_wrap').attr('pub-theme', themeName);
	}
	/**
     * @method getTheme
     * @description get theme
     */
	,getTheme : function (){
		return this.options.theme;
	}
	/**
     * @method setTargetInfo
     * @description set context target item info
     */
	,setTargetInfo : function (targetInfo){
		this.targetInfo = targetInfo;
	}
	/**
     * @method setTargetInfo
     * @description get context target item info
     */
	,getTargetInfo : function (){
		return this.targetInfo;
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

		$menu = '<div id="'+id+'_wrap" class="'+opt.theme+'" onselectstart="return false" draggable="false">'+$menu+'</div>';

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

			//  이전 선택한 클래스 삭제 .
			if(_this.selectElement){
				_this.selectElement.removeClass(opt.selectCls);
			}

			var clickObj = $(this); //$( e.target ).closest( e.data.selector );
			clickObj.addClass(opt.selectCls);
			_this.selectElement = clickObj;

			var $dd = $('#'+ id);

			if(beforeSelectFlag){
				opt.beforeSelect.call(this, {evt : e, element : $(this)});
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

$[ pluginName ].setDefaults = function (defaultValue){
	_defaults = objectMerge(_defaults, defaultValue);
}

})(jQuery);
