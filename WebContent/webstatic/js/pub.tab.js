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
			,icon :{
				prev :'pubTab-left-arrow'
				,next : 'pubTab-right-arrow'
			}
			,activeIcon :{
				overView : false		// mouseover icon view  여부
				,position : 'prev'		//  활성시 html 추가 위치
				,html : ''				// 활성시 추가할 html
				,click: false			// 클릭 이벤트.
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
		this.element = $(element);

        options.width= isNaN(options.width) ?  this.element.width() : options.width;
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

			var _opts = this.options; 

			var activeIcon =_opts.activeIcon; 
			var prevFlag = false;
			var addHtml = '';
			if(activeIcon && activeIcon.html != '') {
				prevFlag = activeIcon.position =='prev' ?true :false; 
				addHtml = '<span class="pubTab-icon-area"><span class="pubTab-icon">'+activeIcon.html+'</span></span>';
			}

			this.config.icon ={
				prevFlag : prevFlag
				,html : addHtml
				,hoverHideCls : (activeIcon.overView === false ? 'pubTab-icon-hover-hide' : '')
			}
		}
		,initEvt : function (){
			var _this = this
				,opts = _this.options;

			_this.element.on('click', '.pubTab-item-cont ',function (e){
				var sEle = $(this)
					,itemEle = sEle.closest('.pubTab-item');
				
				var tabIdx = itemEle.index();

				_this.setActive(opts.items[tabIdx]);

				if($.isFunction(opts.click)){
					opts.click.call(itemEle,opts.items[tabIdx])
				}
			})

			_this.element.on('click', '.pubTab-icon',function (e){
				var sEle = $(this)
					,itemEle = sEle.closest('.pubTab-item');

				e.preventDefault();
				e.stopPropagation();

				if($.isFunction(opts.activeIcon.click)){
					var itemIdx = itemEle.index();

					opts.activeIcon.click.call(itemEle,opts.items[itemIdx], itemIdx);
				}
			})
			
			if(opts.autoMove) {
				var prevTimerObj = null;
				_this.element.on( "mouseenter", '.pubTab-prev' ,function (e){
					var scrollLeft = _this.config.tabScrollElement.scrollLeft();
					function movePrev(){
						_this.config.tabScrollElement.scrollLeft(scrollLeft-10);
						prevTimerObj = setTimeout(function(){
							scrollLeft = _this.config.tabScrollElement.scrollLeft();
							movePrev()
						}, opts.speed);
					}

					movePrev();	
				}).on( "mouseleave", function (e){
					clearTimeout(prevTimerObj);
				});
				
				var nextTimerObj = null;
				_this.element.on( "mouseenter", '.pubTab-next' ,function (e){
					var scrollLeft = _this.config.tabScrollElement.scrollLeft();
					function moveNext(){
						_this.config.tabScrollElement.scrollLeft(scrollLeft+10);
						nextTimerObj = setTimeout(function(){
							scrollLeft = _this.config.tabScrollElement.scrollLeft();
							moveNext()
						}, opts.speed);
					}
					
					prevElement.show();
					
					moveNext();	
				}).on( "mouseleave", function (e){
					clearTimeout(nextTimerObj);
				});
			}

			if(opts.overItemViewMode =='drop'){

				_this.element.on('click.pubtab.drop.btn','.pubTab-drop-open-btn', function (e){
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

				_this.element.on('click.pubtab.drop.item', '.pubTab-drop-item',function (e){
					e.preventDefault();
					e.stopPropagation();

					var sEle = $(this);

					$(_this.element.find('.pubTab-move-area')).removeClass('pubTab-open');

					var tabIdx = opts.items.length - sEle.index()-1;

					$(_this.element.find('.pubTab-item').get(tabIdx)).find('.pubTab-item-cont ').trigger('click');	
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
				idx = this.element.find('.pubTab-item[data-tab-id="'+item[this.options.itemKey.id]+'"]').index();
			}

			idx = isNaN(idx) ? 0 :idx; 

			addAttr = addAttr || {};
			var clickEle = $(this.element.find('.pubTab-item').get(idx));
			
			for(var key in addAttr){
				clickEle.attr(key , addAttr[key]);
			}
			
			$(this.element.find('.pubTab-item').get(idx)).find('.pubTab-item-cont ').trigger('click');
		}
		,setActive: function (item){

			var tabEle= this.element.find('.pubTab-item[data-tab-id="'+item[this.options.itemKey.id]+'"]');
			
			if(tabEle.hasClass('active')){
				return ; 
			}

			this.element.find('.pubTab-item.active').removeClass('active');

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

			var itemEndPoint = tabWidthItem.leftLast+_this.config.moveAreaWidth+30; 
						
			var schLeft = _this.config.tabScrollElement.scrollLeft();
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

			_this.config.tabScrollElement.scrollLeft((leftVal>0?leftVal:0));
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
				tabEle= this.element.find('.pubTab-item[data-tab-id="'+item[this.options.itemKey.id]+'"]');
			}else{
				tabEle= this.element.find('.pubTab-item[data-tab-id="'+item+'"]');
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

			var tabEle= this.element.find('.pubTab-item[data-tab-id="'+item[this.options.itemKey.id]+'"]');

			if(tabEle.length > 0){
				if(enabled !== false){
					tabEle.find('.pubTab-item-cont ').trigger('click');
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

			var tabItem = this.element.find('.pubTab-item'); 
			if(tabItem.length < 1){
				this.config.tabContainerElement.prepend(itemHtm);
				this.element.find('.pubTab-drop-item-area').prepend(dropHtm)
			}else{
				if(idx < 1){
					$(tabItem.get(0)).before(itemHtm);
				}else {
					$(tabItem.get(idx-1)).after(itemHtm);
				}

				$(this.element.find('.pubTab-drop-item').get(0)).before(dropHtm);
			}
			
			this.calcItemWidth();

			if(enabled !== false){
				//this.draw();
				this.movePosition(idx);
				$(this.element.find('.pubTab-item').get(idx)).find('.pubTab-item-cont ').trigger('click');
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

			var tabEle= this.element.find('.pubTab-item[data-tab-id="'+item[this.options.itemKey.id]+'"]');

			if(tabEle.length > 0){

				this.options.items[tabEle.index()] = objectMerge(this.options.items[tabEle.index()], item);
				
				tabEle.find('.pubTab-item-cont ').empty().html(item[this.options.itemKey.title]);
				
				if(enabled !== false){
					tabEle.find('.pubTab-item-cont ').trigger('click');
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
			var tabEle= this.element.find('.pubTab-item[data-tab-id="'+itemId+'"]');
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
				var removeEle = this.element.find('.pubTab-item[data-tab-id="'+item[this.options.itemKey.id]+'"]'); 

				if(removeEle.length < 1){
					return ; 
				}
				idx = this.element.find('.pubTab-item[data-tab-id="'+item[this.options.itemKey.id]+'"]').index();
			}

			if(isNaN(idx) || idx < 0 || idx > this.options.items.length){
				return ; 
			}

			var reval = this.options.items.splice(idx, 1);

			if(reval && reval.length < 1){
				return ;
			}
			reval = reval[0];
				
			this.element.find('.pubTab-item[data-tab-id="'+reval._tabid+'"]').remove();
			this.element.find('.pubTab-drop-item[data-tab-id="'+reval._tabid+'"]').remove();

			this.config.tabHistory = arrayRemove(this.config.tabHistory, reval._tabid);

			var viewTabId;
			if(this.config.tabHistory.length > 0){
				viewTabId =	this.config.tabHistory[this.config.tabHistory.length -1];
			}else{
				viewTabId =	(this.options.items[0]||{})._tabid;
			}
			
			if(viewTabId){
				this.element.find('.pubTab-item[data-tab-id="'+viewTabId+'"]').find('.pubTab-item-cont ').trigger('click');
			}

			this.calcItemWidth();
			
			if($.isFunction(this.options.removeItem)){
				this.options.removeItem.call(null,reval);
			}

			return reval;
		}
		,refresh : function (){
			var _this = this; 
			var eleW = _this.element.width();

			_this.config.width = eleW;

			if(_this.config.totalWidth > eleW){
				$('#'+_this.contextId+'pubTab-move-space').show();
				_this.element.find('.pubTab-move-area').show();
			}else{
				_this.element.find('.pubTab-item').removeClass('pubTab-hide');
				$('#'+_this.contextId+'pubTab-move-space').hide();
				_this.element.find('.pubTab-move-area').hide();
				_this.config.tabContainerElement.css('left', '0px');
			}

			return this; 
		}
		/**
		* set drop item height 
		*/
		,setDropHeight : function (h){
			if(isNaN(h)){
				return this
			}
			this.config.dropItemAreaElement.css('max-height',h+'px');
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
			var sEle = this.element.find('.pubTab-item.active'); 
			return this.options.items[sEle.index()];
		}
		,destroy:function (){
			
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
			if(cfgIcon.prevFlag===true){
				itemHtm = cfgIcon.html + titleTag;
			}else{
				itemHtm = titleTag+cfgIcon.html;
			}
			
			return '<li class="pubTab-item" data-tab-id="'+item._tabid+'" title="'+title+'"> <div class="pubTab-item-cont-wrapper"><div class="pubTab-item-cont '+cfgIcon.hoverHideCls+' '+_opts.addClass+'" >'+itemHtm+'</div></div></li>';
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
			strHtm.push('			<i class="pubTab-prev '+_opts.icon.prev+'"></i>');
			strHtm.push('			<i class="pubTab-next '+_opts.icon.next+'"></i>');
			if(_opts.overItemViewMode =='drop'){
				strHtm.push('		<div id="'+_this.contextId+'DropItem" style="width:'+_opts.dropItemWidth+'" class="pubTab-drop-item-wrapper"><ul class="pubTab-drop-item-area">'+dropItemHtml()+'</ul></div>');
			}

			strHtm.push('</span>');
			strHtm.push('		</div>');
			strHtm.push('	</div>');
			strHtm.push('</div>');

			_this.element.empty().html(strHtm.join(''));
					
			_this.config.tabContainerElement =  $('#'+_this.contextId+'pubTab-container');
			_this.config.tabScrollElement = $('#'+_this.contextId+'pubTab-scroll');
			_this.config.dropItemAreaElement = $('#'+_this.contextId+'DropItem');;
			_this.config.moveAreaWidth  = this.element.find('.pubTab-move-area').width();
			$('#'+_this.contextId+'pubTab-move-space').css('width',_this.config.moveAreaWidth);

			_this.calcItemWidth();
			_this.setWidth(_opts.width);
			_this.setDropHeight(_opts.dropItemHeight)
		}
		,calcItemWidth :function (){
			var _this =this;
			var containerW = 0; 
			_this.config.tabContainerElement.find('.pubTab-item').each(function(i , item){
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
				width : this.config.tabContainerElement.width() - _this.config.tabScrollElement.width()	
			} 
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