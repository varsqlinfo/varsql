/*
 * pubContextMenu: v0.0.1
 * ========================================================================
 * Copyright 2015 ytkim
 * Licensed under MIT
 * http://www.opensource.org/licenses/mit-license.php
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
        
    function Plugin(element, options) {
        this.selector = (typeof element=='object') ? element.selector : element;
		this.contextId = 'dropdown-'+new Date().getTime();
        this.options = $.extend({}, defaults, options);
		this.contextData = {};
		this.selectElement = $('');

		if(pubContextElement ===false){
			$('body').append('<div id="pub-context-area"></div>');
			pubContextElement = $('#pub-context-area');

		}
		
		if(initialized===false){
			this.init(); 
			//initialized = true; 
		}
		
		this.addContext();
		this.context = $('#'+this.contextId);
		return element; 
    }

	Plugin.prototype ={
		init :function(){
			var _this = this;
			defaults = this.options;
			id=_this.contextId;
			
			$(document).on('mousedown', 'html', function (e) {
				if(e.which !==2 && $(e.target).closest('#'+id+'_wrap .pub-context').length < 1){
					$('#'+id+'_wrap .pub-context').fadeOut(defaults.fadeSpeed, function(){
						$('#'+id+'_wrap .pub-context').css({display:''}).find('.drop-left').removeClass('drop-left');
					});
				}
			});

			if(defaults.preventDoubleContext){
				$(document).on('contextmenu.pubcontext', '#'+id+'_wrap .pub-context', function (e) {
					e.preventDefault();
				});
			}

			$(document).on('mouseenter', '#'+id+'_wrap .pub-context-submenu', function(){
				var $sub = $(this).find('.pub-context-sub:first'),
					subWidth = $sub.width(),
					subLeft = $sub.offset().left,
					collision = (subWidth+subLeft) > window.innerWidth;
				if(collision){
					$sub.addClass('drop-left');
				}
			});
		}
		,updatedefaults:function (opts){
			defaults = $.extend({}, defaults, opts);
		}
		,buildMenu : function (data, id, subMenu, depth){
			var _self = this; 
			var subClass = (subMenu) ? ' pub-context-sub' : ' pub-context-top',
				compressed = defaults.compress ? ' compressed-context' : '',
				$menuHtm = [];
			
			$menuHtm.push('<ul class="pub-context-menu pub-context' + subClass + compressed+'" id="' + id + '">');
	 
			var item ,linkTarget = '',itemKey;;
			for(var i = 0; i<data.length; i++) {
				item = data[i]; 
				
				if (typeof item.divider !== 'undefined') {
					$menuHtm.push('<li class="divider" context-key="divider"></li>');
					continue;
				}
				
				if (typeof item.header !== 'undefined') {
					$menuHtm.push('<li class="pub-context-header" context-key="header">' + item.header + '</li>');
					continue; 
				}
				
				itemKey = depth+'_'+item.key; 
				_self.contextData[itemKey] = item;
			
				if (typeof item.target !== 'undefined') {
					linkTarget = ' target="'+item.target+'"';
				}

				if (typeof item.subMenu !== 'undefined') {
					$menuHtm.push('<li class="pub-context-submenu ui-context-item" context-key="'+itemKey+'"><a tabindex="-1">' + item.name + '</a>');
				} else {
					$menuHtm.push('<li class="ui-context-item" context-key="'+itemKey+'"><a tabindex="-1">' + item.name + '</a>');
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
		,destory:function (){
			$(document).off('contextmenu.pubcontext', this.element).off('click', '.context-event');
		}
		/**
		*
		* 컨텍스트 메뉴 이벤트 처리. 
		*/
		,contextEvent : function (){
			var _self = this,opt = _self.options;
			
			$('#'+_self.contextId+' .ui-context-item').on('click',function (){
				var clickEle=$(this);
				
				if(clickEle.hasClass('pub-context-submenu')){
					
				}else{
					skey = clickEle.attr('context-key');
					var sobj = {
						key : skey
						,item : _self.contextData[skey]
						,list : _self.contextData
						,element : _self.selectElement
					}
				
					if(jQuery.isFunction(opt.callback)){
						opt.callback.call(sobj, sobj.item.key, sobj.item);
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
			
			pubContextElement.append($menu);
			
			_self.contextEvent();

			$(document).off('contextmenu.pubcontext', _self.selector);
			$(document).on('contextmenu.pubcontext', _self.selector, function (e) {
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
					,evtX = e.pageX
					,evtY=e.pageY;
				
				//console.log('eleH : ' + eleH,'evtX : ' + evtX,'evtY : ' + evtY,  $(document).height());

				if ( evtY > eleH+5 && (evtY + eleH+5) > $(document).height()) {
					$dd.css({
						top: evtY - (eleH+5),
						left: evtX +5
					}).fadeIn(defaults.fadeSpeed);
				} else {
					$dd.css({
						top: evtY + 5,
						left: evtX + 5
					}).fadeIn(defaults.fadeSpeed);
				}
			});
		}
	};

    $[ pluginName ] = function (selector,options) {

		if(!selector){
			return ; 
		}

		var _cacheObject = _datastore[selector];

		if(typeof options === undefined){
			return _cacheObject; 
		}
		
		if(!_cacheObject){
			_cacheObject = new Plugin(selector, options);
			_datastore[selector] = _cacheObject;
			return _cacheObject; 
		}else if(typeof options==='object'){
			_cacheObject = new Plugin(selector, options);
			_datastore[selector] = _cacheObject;
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
