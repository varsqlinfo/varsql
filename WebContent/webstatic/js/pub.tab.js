/**
 * pubTab v0.0.1
 * ========================================================================
 * Copyright 2016 ytkim
 * Licensed under MIT
 * http://www.opensource.org/licenses/mit-license.php
 * url : https://github.com/ytechinfo/pub
 * demo : http://pub.moaview.com/
*/

;(function ($, window, document) {
	"use strict";
    var pluginName = "pubTab"
		,_datastore = {}
		,defaults = {
			speed : 150
			,width:'auto'
			,itemMaxWidth : -1
			,autoMove : false
			,itemPadding: 5
			,height : '22px'
			,leftMargin : 30	// 왼쪽에 item 있을경우 더 이동할 space
			,overItemViewMode : 'drop'
			,dropItemHeight :'auto'		//drop item height
			,dropItemWidth :'auto'		//drop item width
			,moveZIndex : 9				// move 영역 z- index
			,filter: function ($obj) {
				// Modify $obj, Do not return
			}
			,titleIcon :{
				left :{
					overView : true		// mouseover icon view  여부
					,visible :true		// 기본 보이기 여부.
					,html : ''				// 활성시 추가할 html
					,click: false			// 클릭 이벤트.
				}
				,right : {
					overView : true		// mouseover icon view  여부
					,visible :true
					,html : ''				// 활성시 추가할 html
					,click: false			// 클릭 이벤트.
				}
			}
			,addClass : 'service_menu_tab'	// tab li 추가 클래스
			,items:[]							// tab item
			,click :function (item){			// tab click 옵션
				
			}
			,removeItem : false	// remove callback 옵션
			,itemKey :{							// item key mapping
				title :'name'
				,id :'_tabid'
			}
		};

	function objectMerge() {
		var dst = {},src ,p ,args = [].splice.call(arguments, 0);
		
		while (args.length > 0) {
			src = args.splice(0, 1)[0];
			if (Object.prototype.toString.call(src) == '[object Object]') {
				for (p in src) {
					if (src.hasOwnProperty(p)) {
						if (Object.prototype.toString.call(src[p]) == '[object Object]') {
							dst[p] = objectMerge(dst[p] || {}, src[p]);
						} else {
							dst[p] = src[p];
						}
					}
				}
			}
		}
		return dst;
	}
        
    function Plugin(element, options) {
        this.selector = (typeof element=='object') ? element.selector : element;
		this.contextId = 'pubTab-'+new Date().getTime();
		this.tabElement = $(element);

        options.width= isNaN(options.width) ?  this.tabElement.width() : options.width;
        this.options = objectMerge({}, defaults, options);
	
		this.init();
	
		return this; 
    }

	function arrayRemove(array, value){
		var index = null;
		while ((index = array.indexOf(value)) !== -1){
			array.splice(index, 1);
		}
		return array;
	}

	$(document).on('mousedown.pubtab', 'html', function (e) {
		if(e.which !==2 && $(e.target).closest('.pubTab-drop-item-wrapper').length < 1){
			$('.pubTab-move-area.pubTab-open').removeClass('pubTab-open');
		}
	});

	Plugin.prototype ={
		init :function(){
			var _this =this; 

			_this._setConfigInfo();
			_this.draw();

			_this.initEvt();
		}
		,_setConfigInfo : function (){
			this.config = {tabWidth :[] , tabHistory : [] , tabIdx : 0};
			this.element = {}

			var _opts = this.options; 

			var titleIcon =_opts.titleIcon; 
			
			var addHtml = '';

			var iconInfo ={left :{} , right:{}};

			if(titleIcon){
				if(titleIcon.left && titleIcon.left.html != ''){
					iconInfo.left.html =  '<span class="pubTab-icon-area '+(titleIcon.left.visible === false ? 'hide' : '')+' '+(titleIcon.left.overView === false ? 'pubTab-icon-hover-hide' : '')+'"><span class="pubTab-icon" data-posistion="left">'+titleIcon.left.html+'</span></span>';
				}
				
				if(titleIcon.right && titleIcon.right.html != ''){
					iconInfo.right.html =  '<span class="pubTab-icon-area '+(titleIcon.right.visible === false ? 'hide' : '')+' '+(titleIcon.right.overView === false ? 'pubTab-icon-hover-hide' : '')+'"><span class="pubTab-icon" data-posistion="right">'+titleIcon.right.html+'</span></span>';
				}
			}

			this.config.icon = iconInfo;
		}
		,initEvt : function (){
			var _this = this
				, opts = _this.options
				, titleIcon =opts.titleIcon; 

			_this.tabElement.on('click.pubtab.item', '.pubTab-item-cont',function (e){
				var sEle = $(this)
					,itemEle = sEle.closest('.pubTab-item');
				
				var tabIdx = itemEle.index();

				_this.setActive(opts.items[tabIdx]);

				if($.isFunction(opts.click)){
					opts.click.call(itemEle,opts.items[tabIdx])
				}
			})

			_this.tabElement.on('click.pubtab.icon', '.pubTab-icon',function (e){
				var sEle = $(this)
					,itemEle = sEle.closest('.pubTab-item');

				e.preventDefault();
				e.stopPropagation();
				
				var callfn;
				var iconPos = sEle.attr('data-posistion'); 
				if(iconPos=='left'){
					callfn = titleIcon.left.click;
				}else if(iconPos=='right'){
					callfn = titleIcon.right.click;
				}

				if($.isFunction(callfn)){
					var itemIdx = itemEle.index();
					callfn.call(itemEle,opts.items[itemIdx], itemIdx);
				}
				return false; 
			})
			
			_this.tabElement.on('mousewheel DOMMouseScroll','.pubTab-scroll', function(e) {
				var oe = e.originalEvent;
				var delta = 0;
			
				if (oe.detail) {
					delta = oe.detail * -40;
				}else{
					delta = oe.wheelDelta;
				};

				//delta > 0--up
				_this._moveContainerPos( _this.element.tabScrollElement.scrollLeft() + (delta > 0?-10 :10) );
			});
						
			if(opts.overItemViewMode =='drop'){

				_this.tabElement.on('click.pubtab.drop.btn','.pubTab-drop-open-btn', function (e){
					e.preventDefault();
					e.stopPropagation();

					var sEle = $(this)
						,tabArea=sEle.closest('.pubTab-move-area')
					
					if(tabArea.hasClass('pubTab-open')){
						tabArea.removeClass('pubTab-open');
					}else{
						tabArea.addClass('pubTab-open');
					}
				});

				_this.tabElement.on('click.pubtab.drop.item', '.pubTab-drop-item',function (e){
					e.preventDefault();
					e.stopPropagation();

					var sEle = $(this);

					$(_this.tabElement.find('.pubTab-move-area')).removeClass('pubTab-open');

					var tabIdx = opts.items.length - sEle.index()-1;

					$(_this.tabElement.find('.pubTab-item').get(tabIdx)).find('.pubTab-item-cont').trigger('click');	
				})
			}
		}
		,_setHistory : function (tabid){
			var idx = this.config.tabHistory.indexOf(tabid);
			this.config.tabHistory = arrayRemove(this.config.tabHistory, tabid);
			this.config.tabHistory.push(tabid);
		}
		,itemClick : function (item, addAttr){
			var idx  = item;
			if(typeof item ==='object'){
				idx = this.tabElement.find('.pubTab-item[data-tab-id="'+item[this.options.itemKey.id]+'"]').index();
			}

			idx = isNaN(idx) ? 0 :idx; 

			addAttr = addAttr || {};
			var clickEle = $(this.tabElement.find('.pubTab-item').get(idx));
			
			for(var key in addAttr){
				clickEle.attr(key , addAttr[key]);
			}
			
			$(this.tabElement.find('.pubTab-item').get(idx)).find('.pubTab-item-cont').trigger('click');
		}
		/**
		 * @method isActive
		 * @description item active 체크.
		 */
		,isActive : function (item){
			var tabEle;
			if(typeof item ==='object'){
				tabEle= this.tabElement.find('.pubTab-item[data-tab-id="'+item[this.options.itemKey.id]+'"].active');
			}else{
				tabEle= this.tabElement.find('.pubTab-item[data-tab-id="'+item+'"].active');
			}

			return tabEle.length > 0 ? true : false; 
		}
		/**
		 * @method setActive
		 * @description set item active 
		 */
		,setActive: function (item){
			var tabEle= this.tabElement.find('.pubTab-item[data-tab-id="'+item[this.options.itemKey.id]+'"]');
			
			if(tabEle.hasClass('active')){
				return ; 
			}

			this.tabElement.find('.pubTab-item.active').removeClass('active');
			this._setHistory(tabEle.attr('data-tab-id'));
			tabEle.addClass('active');
			this.movePosition(tabEle.index());
		}
		/**
		 * @method movePosition
		 * @description item move
		 */
		,movePosition : function(dataIdx){
			var _this = this;

			var tabWidthItem =_this.config.tabWidth[dataIdx]; 

			var selItem = _this.options.items[dataIdx];

			var itemEndPoint = tabWidthItem.leftLast+_this.config.moveAreaWidth; 
						
			var schLeft = _this.element.tabScrollElement.scrollLeft();
			var leftVal =0;

			if(schLeft >= tabWidthItem.leftFront){
				leftVal = tabWidthItem.leftFront-_this.options.leftMargin;
			}else if(itemEndPoint <= (schLeft +_this.config.width)){
				return ;
			}else{
				if(itemEndPoint > _this.config.width){
					leftVal = itemEndPoint - _this.config.width; 
				}
			}
			
			_this._moveContainerPos(leftVal);			
		}
		/**
		* @method _moveContainerPos
		* @description move container left position;
		*/
		,_moveContainerPos : function (val){
			val = (val>0?val:0);
			this.element.tabScrollElement.scrollLeft(val);
		}
		/**
		 * @method setItems
		 * @description set items
		 */
		,setItems : function (items){
			this.options.items = items;
			this.draw();
		}
		/**
		 * @method isItem
		 * @description item check
		 */
		,isItem : function (item){
			var tabEle;
			if(typeof item ==='object'){
				tabEle= this.tabElement.find('.pubTab-item[data-tab-id="'+item[this.options.itemKey.id]+'"]');
			}else{
				tabEle= this.tabElement.find('.pubTab-item[data-tab-id="'+item+'"]');
			}

			return tabEle.length > 0 ? true : false; 
		}
		/**
		 * @method addItem
		 * @description item add
		 */
		,addItem : function(addInfo){
			var item = addInfo.item
				,idx = addInfo.idx 
				,enabled =addInfo.enabled; 

			var tabEle= this.tabElement.find('.pubTab-item[data-tab-id="'+item[this.options.itemKey.id]+'"]');

			if(tabEle.length > 0){
				if(enabled !== false){
					tabEle.find('.pubTab-item-cont').trigger('click');
				}
				
				return false; 
			}

			if(isNaN(idx)){
				idx = this.options.items.length;
				this.options.items.push(item);
			}else{
				if(idx > this.options.items.length){
					idx = this.options.items.length;
				}
				this.options.items.splice(idx, 0, item);
			}
			
			var itemHtm = this._getTabItemHtml(item)
				,dropHtm = this._getDropItemHtml(item); 

			var tabItem = this.tabElement.find('.pubTab-item'); 
			if(tabItem.length < 1){
				this.element.tabContainerElement.prepend(itemHtm);
				this.tabElement.find('.pubTab-drop-item-area').prepend(dropHtm)
			}else{
				if(idx < 1){
					$(tabItem.get(0)).before(itemHtm);
				}else {
					$(tabItem.get(idx-1)).after(itemHtm);
				}

				$(this.tabElement.find('.pubTab-drop-item').get(0)).before(dropHtm);
			}
			
			this.calcItemWidth();

			if(enabled !== false){
				//this.draw();
				this.movePosition(idx);
				$(this.tabElement.find('.pubTab-item').get(idx)).find('.pubTab-item-cont').trigger('click');
			}

			return true;
		}
		/**
		 * @method updateItem
		 * @description item update
		 */
		,updateItem : function (modInfo){
			var item = modInfo.item
				,enabled =modInfo.enabled; 

			var tabEle= this.tabElement.find('.pubTab-item[data-tab-id="'+item[this.options.itemKey.id]+'"]');

			if(tabEle.length > 0){

				this.options.items[tabEle.index()] = objectMerge(this.options.items[tabEle.index()], item);
				
				tabEle.find('.pubTab-item-title').empty().html(item[this.options.itemKey.title]);
				
				if(enabled !== false){
					tabEle.find('.pubTab-item-cont').trigger('click');
				}
				this.calcItemWidth();
				
				return; 
			}else{
				this.addItem(modInfo);
			}
		}
		/**
		 * @method getFirstItem
		 * @description get item
		 */
		,getFirstItem : function (){
			if(this.options.items.length > 0){
				return this.options.items[0];
			}
			return {};
		}

		/**
		 * @method getLastItem
		 * @description get item
		 */
		,getLastItem : function (){
			if(this.options.items.length > 0){
				return this.options.items[this.options.items.length -1];
			}
			return {};
		}
		/**
		 * @method getItem
		 * @description get item
		 */
		,getItem : function(itemId , mode){
			var tabEle= this.tabElement.find('.pubTab-item[data-tab-id="'+itemId+'"]');
			var itemEle;
			if(mode == 'prev'){
				itemEle = tabEle.prev('[data-tab-id]');
			}else if(mode == 'next'){
				itemEle = tabEle.next('[data-tab-id]');
			}else{
				itemEle = tabEle; 
			}
			
			if(itemEle.length > 0){
				return this.options.items[itemEle.index()];
			}

			return {};
		}
		/**
		 * @method getItemLength
		 * @description tab item length
		 */
		,getItemLength : function (){
			return this.options.items.length;
		}
		/**
		 * @method removeItem
		 * @description item remove
		 */
		,removeItem : function(item){
			var idx = item;
			if(typeof item ==='object'){
				var removeEle = this.tabElement.find('.pubTab-item[data-tab-id="'+item[this.options.itemKey.id]+'"]'); 

				if(removeEle.length < 1){
					return ; 
				}
				idx = this.tabElement.find('.pubTab-item[data-tab-id="'+item[this.options.itemKey.id]+'"]').index();
			}

			if(isNaN(idx) || idx < 0 || idx > this.options.items.length){
				return ; 
			}

			var reval = this.options.items.splice(idx, 1);

			if(reval && reval.length < 1){
				return ;
			}
			reval = reval[0];
				
			this.tabElement.find('.pubTab-item[data-tab-id="'+reval._tabid+'"]').remove();
			this.tabElement.find('.pubTab-drop-item[data-tab-id="'+reval._tabid+'"]').remove();

			this.config.tabHistory = arrayRemove(this.config.tabHistory, reval._tabid);

			var viewTabId;
			if(this.config.tabHistory.length > 0){
				viewTabId =	this.config.tabHistory[this.config.tabHistory.length -1];
			}else{
				viewTabId =	(this.options.items[0]||{})._tabid;
			}
			
			if(viewTabId){
				this.tabElement.find('.pubTab-item[data-tab-id="'+viewTabId+'"]').find('.pubTab-item-cont').trigger('click');
			}

			this.calcItemWidth();
			
			if($.isFunction(this.options.removeItem)){
				this.options.removeItem.call(null,reval);
			}

			return reval;
		}
		,refresh : function (){
			var _this = this; 
			var eleW = _this.tabElement.width();

			_this.config.width = eleW;

			if(_this.config.totalWidth > eleW){
				$('#'+_this.contextId+'pubTab-move-space').show();
				_this.tabElement.find('.pubTab-move-area').show();
			}else{
				_this.tabElement.find('.pubTab-item').removeClass('pubTab-hide');
				$('#'+_this.contextId+'pubTab-move-space').hide();
				_this.tabElement.find('.pubTab-move-area').hide();
				_this.element.tabContainerElement.css('left', '0px');
			}

			return this; 
		}
		/**
		* set drop item height 
		*/
		,setDropHeight : function (h){
			if(isNaN(h)){
				return this;
			}
			this.element.dropItemAreaElement.css('max-height',h+'px');
			return this; 
		}
		/**
		* set tab width 
		*/
		,setWidth : function (val){
			this.refresh();
			return this; 
		}
		,getSelectItem : function(){
			var sEle = this.tabElement.find('.pubTab-item.active'); 
			return this.options.items[sEle.index()];
		}
		// tab  item template
		,_getTabItemHtml : function (item){
			
			var cfgIcon = this.config.icon; 
			var _opts = this.options;

			if(item[_opts.itemKey.id]){
				item._tabid = item[_opts.itemKey.id];
			}else{
				this.config.tabIdx++;
				item._tabid = 'tab_'+this.config.tabIdx;
			}
		
			var title = item[_opts.itemKey.title];
			
			var titleTag = '<span class="pubTab-item-title">'+title+'</span>';
			if(!isNaN(_opts.itemMaxWidth) && _opts.itemMaxWidth > 0){
				titleTag = '<span class="pubTab-item-title pub-title-ellipsis" style="max-width:'+(_opts.itemMaxWidth)+'px">'+title+'</span>';
				//titleTag = '<span class="pubTab-item-title">'+title+'</span>';
			}

			var itemHtm ='';
			if(cfgIcon.left && cfgIcon.left.html){
				itemHtm += cfgIcon.left.html;
			}

			itemHtm += titleTag;

			if(cfgIcon.right && cfgIcon.right.html){
				itemHtm += cfgIcon.right.html;
			}
			
			return '<li class="pubTab-item" data-tab-id="'+item._tabid+'" title="'+title+'"> <div class="pubTab-item-cont-wrapper"><div class="pubTab-item-cont '+_opts.addClass+'" >'+itemHtm+'</div></div></li>';
		}
		//drop item template
		,_getDropItemHtml : function (item){
			var title = item[this.options.itemKey.title]; 
			return '<li class="pubTab-drop-item" data-tab-id="'+item._tabid+'" title="'+title+'">'+title+'</li>'
		}
		,draw : function (){
			var _this = this
				,_opts = _this.options
				,items = _opts.items
				,itemLen = items.length; 
			
			function tabItemHtml (){
				var tabHtm = [];
					
				var item;
				var itemHtm;
				for(var i = 0 ;i < itemLen ;i++){
					tabHtm.push(_this._getTabItemHtml(items[i]));
				}
				return tabHtm.join('');
			}

			function dropItemHtml (){
				var dropHtml = [];
				
				for(var i = itemLen-1 ;i >= 0  ;i--){
					dropHtml.push(_this._getDropItemHtml(items[i]));
				}
				return dropHtml.join('');
			}

			var strHtm = [];
			strHtm.push('<div class="pubTab-wrapper">');
			strHtm.push('	<div id="'+_this.contextId+'pubTab" class="pubTab">');
			strHtm.push('		<div id="'+_this.contextId+'pubTab-scroll" class="pubTab-scroll">');
			strHtm.push('			<ul id="'+_this.contextId+'pubTab-container" class="pubTab-container" style="height:'+_opts.height+'">');
			strHtm.push(tabItemHtml());
			strHtm.push('			<li><div id="'+_this.contextId+'pubTab-move-space"  style="display:none;">&nbsp;</div></li>');
			strHtm.push('			</ul>');
			strHtm.push('		</div> ');
			strHtm.push('		<div class="pubTab-move-area" style="z-index:'+_opts.moveZIndex+';">');

			strHtm.push('		<span class="pubTab-drop-open-btn">');
			strHtm.push('			<div class="pubTab-move-dim"></div>');
			strHtm.push('			<i class="pubTab-more-button"></i>');
			if(_opts.overItemViewMode =='drop'){
				strHtm.push('		<div id="'+_this.contextId+'DropItem" style="width:'+_opts.dropItemWidth+'" class="pubTab-drop-item-wrapper"><ul class="pubTab-drop-item-area">'+dropItemHtml()+'</ul></div>');
			}

			strHtm.push('</span>');
			strHtm.push('		</div>');
			strHtm.push('	</div>');
			strHtm.push('</div>');

			_this.tabElement.empty().html(strHtm.join(''));
					
			_this.element.tabContainerElement =  $('#'+_this.contextId+'pubTab-container');
			_this.element.tabScrollElement = $('#'+_this.contextId+'pubTab-scroll');
			_this.element.dropItemAreaElement = $('#'+_this.contextId+'DropItem');

			_this.config.moveAreaWidth  = this.tabElement.find('.pubTab-move-area').width();
			$('#'+_this.contextId+'pubTab-move-space').css('width',_this.config.moveAreaWidth);

			_this.calcItemWidth();
			_this.setWidth(_opts.width);
			_this.setDropHeight(_opts.dropItemHeight)
		}
		,calcItemWidth :function (){
			var _this =this;
			var containerW = 0; 
			_this.element.tabContainerElement.find('.pubTab-item').each(function(i , item){
				var itemW =$(item).outerWidth() + _this.options.itemPadding;
				containerW +=itemW;
				_this.config.tabWidth[i] = {
					itemW : itemW
					,leftFront : (containerW-itemW)
					,leftLast : containerW
				}
			});

			_this.config.totalWidth = containerW;

			this.refresh();
		}
		,setScrollInfo : function (){
			this.config.scroll = {
				width : this.element.tabContainerElement.width() - _this.element.tabScrollElement.width()	
			} 
		}
		/**
		 * @method setTheme
		 * @description set theme
		 */
		,setTheme : function (themeName){
			this.tabElement.removeClass('pub-theme-'+this.options.theme);
			this.options.theme = themeName;
			this.tabElement.addClass('pub-theme-'+themeName);
		}
		/**
		 * @method getTheme
		 * @description get theme
		 */
		,getTheme : function (){
			return this.options.theme;
		}
		,destroy:function (){
			
		}
	};

    $[ pluginName ] = function (selector,options) {

		if(!selector){
			return ; 
		}

		var _cacheObject = _datastore[selector];

		if(typeof options === 'undefined'){
			return _cacheObject; 
		}
		
		if(!_cacheObject){
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