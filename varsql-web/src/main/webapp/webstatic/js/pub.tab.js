/**
 * pubTab v1.0.2
 * ========================================================================
 * Copyright 2016-2021 ytkim
 * Licensed under MIT
 * http://www.opensource.org/licenses/mit-license.php
 * url : https://github.com/ytechinfo/pub
 * demo : http://pub.moaview.com/
*/

;(function ($, window, document) {
"use strict";
var pluginName = "pubTab"
,_datastore = {}
,_defaults = {
	width:'auto'			//tab width
	,itemMaxWidth : -1		// tab max width
	,activeFirstItem : true	//로드시 첫번째 item 활성화 여부
	,enableClickEventChange : false // click event item 변경시에만 활성화
	,itemPadding: 5			// item padding 값
	,tabHeight : 25		// tab height
	,leftMargin : 30		// 왼쪽에 item 있을경우 더 이동할 space
	,overItemViewMode : 'drop'	// over item  보여질 방법.
	,dropdownHeight :'auto'		//drop item height
	,dropdownWidth :'auto'		//drop item width
	,isMultipleContainer : true	 // 하나의 컨텐츠 영역만 사용.
	,useContentContainer : true	// 컨텐츠 영역 사용여부
	,titleIcon :{
		left :{
			onlyActiveView :true		// 기본 보이기 여부.
			,html : ''				// 활성시 추가할 html
			,click: false			// 클릭 이벤트.
		}
		,right : {
			onlyActiveView :false
			,html : ''				// 활성시 추가할 html
			,click: false			// 클릭 이벤트.
		}
	}
	,contextMenu :false 			// context menu
	,addClass : 'service_menu_tab'	// tab li 추가 클래스
	,items:[]							// tab item
	,click :function (item){			// tab click 옵션

	}
	,contentViewSelector : false 			// tab content view selector
	,contentStyleClass : ''
	,contentRender : function (item, addContentAreaElement){
		return ; 
	}
	,removeItem : false	// remove callback 옵션
	,blinkClass : 'blinkcss'
	,itemKey :{							// item key mapping
		title :'name'				// tab item name
		,id :'_tabid'				// tab item id
	}
	,beforeUpdate : function (){	// before update item callback
	}
	,afterUpdate : function (){		// after update item callback
	}
	,drag : {	// drag  이동
		enabled : false	// 활성 여부
		,dragStart : function (dragItem){} // drag start
		,dragEnter : function (dragItem){} // drag enter
		,dragDrop : function (dragItem){} // drag enter
		,dragEnd : function (dragItem){} // drag enter
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
	this.selector = element;
	this.prefix = pluginName + new Date().getTime();
	this.tabElement = $(element);

	if(options.width != 'auto'){
		$(this.selector).width(options.width);
	}

	options.width= isNaN(options.width) ?  this.tabElement.width() : options.width;
	this.options = objectMerge({}, _defaults, options);
	
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
	if(e.which !==2 && $(e.target).closest('.pubTab-dropdown-wrapper').length < 1){
		$('.pubTab-dropdown-wrapper.pubTab-open').removeClass('pubTab-open');
	}
});

Plugin.prototype ={
	init :function(){
		var _this =this;

		_this._setConfigInfo();
		_this.draw();

		_this.initEvt();

		if(this.options.activeFirstItem===true && this.getItemLength() > 0){
			setTimeout(function (){
				_this.itemClick();
			},100);
		}
	}
	,_setConfigInfo : function (){
		this.config = {tabWidth :[], tabHistory : [], tabIdx : 0, tabContextMenu : false};
		this.element = {}

		var _opts = this.options;

		var titleIcon =_opts.titleIcon;

		var iconInfo ={left :{} , right:{}};

		if(titleIcon){
			var leftIcon =titleIcon.left; 
			if(leftIcon && leftIcon.html != ''){
				iconInfo.left.html =  '<span class="pubTab-icon-area '+(leftIcon.onlyActiveView === true ? 'visible-hide' : '')+'"><span class="pubTab-icon" data-posistion="left">'+leftIcon.html+'</span></span>';
			}

			if(titleIcon.right && titleIcon.right.html != ''){
				iconInfo.right.html =  '<span class="pubTab-icon-area '+(titleIcon.right.onlyActiveView === true ? 'visible-hide' : '')+'"><span class="pubTab-icon" data-posistion="right">'+titleIcon.right.html+'</span></span>';
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

			_this.itemClick(tabIdx);
		})


		if(opts.contextMenu !== false){

			if(opts.contextMenu !== false && typeof opts.contextMenu == 'object'){
				var _cb = opts.contextMenu.callback;

				if(_cb){
					opts.contextMenu.callback = function(key,sObj) {
						var tabEle = this.element.closest('[data-tab-id]');
						var tabId = tabEle.attr('data-tab-id');
						sObj = {
							tabId : tabId
							,tabItem : _this.getItem(tabId)
						}
						_cb.call(this,key,sObj);
					}
				}
			}

			_this.config.tabContextMenu = $.pubContextMenu('#'+_this.prefix+'pubTab-container .pubTab-item-cont',opts.contextMenu);
		}

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
			_this._moveContainerPos( _this.element.tabScrollElement.scrollLeft() + (delta > 0?-10 :10));

			return false;
		});

		if(opts.overItemViewMode =='drop'){

			_this.tabElement.on('click.pubtab.drop.btn','.pubTab-more-button', function (e){
				e.preventDefault();
				e.stopPropagation();

				var sEle = $(this)
					,tabArea=sEle.closest('.pubTab-more-button')

				if(_this.element.dropdownAreaElement.hasClass('pubTab-open')){
					_this.element.dropdownAreaElement.removeClass('pubTab-open');
				}else{
					_this.element.dropdownAreaElement.addClass('pubTab-open');
				}
			});

			_this.tabElement.on('click.pubtab.drop.item', '.pubTab-drop-item',function (e){
				e.preventDefault();
				e.stopPropagation();

				var sEle = $(this);

				_this.element.dropdownAreaElement.removeClass('pubTab-open');

				var tabIdx = opts.items.length - sEle.index()-1;

				$(_this.tabElement.find('.pubTab-item').get(tabIdx)).find('.pubTab-item-cont').trigger('click');
			})
		}

		if(this.options.drag.enabled===true){
			this.dragMove();
		}
	}
	,dragMove : function (){
		var _this = this; 

		_this.drag = {};

		var dragElement;

		var startX;
		var dragItemIdx; 

		var dragFncs = this.options.drag; 

		this.tabElement.on('dragstart.pubtab.dragitem', '.pubTab-item', function (e){
			_this.element.tabContainerElement.addClass('drag-on');

			dragItemIdx = $(this).index();

			var dragItem = _this.options.items[dragItemIdx]; 

			if(_this.isActive(dragItem) ===false){
				_this.itemClick(dragItem);
			}

			startX = e.originalEvent.pageX;
			dragElement = $(this);
			
			e.originalEvent.dataTransfer.effectAllowed = 'copyMove';

			if(dragFncs['dragStart'].call($(this), dragItem) ===false){
				return false; 
			}

		}).on('drag.pubtab.dragitem', '.pubTab-item', function (e){
			//e.preventDefault();
		}).on('dragenter.pubtab.dragitem', '.pubTab-item', function (e){
			var enterEle = $(this); 
			dragFncs['dragEnter'].call(enterEle, _this.options.items[enterEle.index()])
			$(this).addClass('drag-over');

			return false; // mouse  cursor 이상 현상 제거 하기 위해서 처리함. 
			
		}).on('dragleave.pubtab.dragitem', '.pubTab-item', function (e){
			$(this).removeClass('drag-over');
		}).on('dragend.pubtab.dragitem',  function (e){
			_this.element.tabContainerElement.removeClass('drag-on');
			dragFncs['dragEnd'].call($(this));
		}).on('drop.pubtab.dragitem', '.pubTab-item', function (e){
			
			var dropEle = $(this); 
			dropEle.removeClass('drag-over');
			var dropIdx = dropEle.index();

			if(dragItemIdx == dropIdx) return ; 

			var endX = e.originalEvent.pageX; 
			var prevItem;
			if(startX <= endX){
				prevItem = _this.options.items[dropIdx]; 
				dropEle.after(dragElement);
			}else{
				prevItem = _this.options.items[dropIdx-1]; 
				dropEle.before(dragElement);
			}

			var dragItem = _this.options.items.splice(dragItemIdx, 1)[0];
			if(dragFncs['dragDrop'].call(dropEle, {beforePrevItem : _this.options.items[dragItemIdx-1], moveItem :dragItem, afterPrevItem :prevItem}) ===false){
				return false; 
			}
				
			_this.options.items.splice(dropIdx, 0, dragItem);

			var moveInfo ={nextTabId : '', moveTabId :dragItem._tabid, prevTabId:''};
			if(_this.options.items.length > dropIdx+1){
				moveInfo.nextTabId = _this.options.items[dropIdx+1]._tabid;
			}

			if(0 < dropIdx){
				moveInfo.prevTabId = _this.options.items[dropIdx-1]._tabid;
			}
		})

		this.tabElement.on('dragover.pubtab.dragitem', function (e){
			e.preventDefault();
		})
		
	}
	,_setHistory : function (tabid){
		var idx = this.config.tabHistory.indexOf(tabid);
		this.config.tabHistory = arrayRemove(this.config.tabHistory, tabid);
		this.config.tabHistory.push(tabid);
	}
	,itemClick : function (item, customInfo){
		var idx  = item;
		if(typeof item ==='object'){
			idx = this.tabElement.find('.pubTab-item[data-tab-id="'+item[this.options.itemKey.id]+'"]').index();
		}

		idx = isNaN(idx) ? 0 :idx;

		customInfo = customInfo || {};
		var clickEle = $(this.tabElement.find('.pubTab-item').get(idx));

		var opts = this.options;
		var tabItem = opts.items[idx];

		if(opts.enableClickEventChange ===true && this.isActive(tabItem)){ // 변경 되었을때만 event 처리.
			return ;
		}

		this.setActive(tabItem);

		if($.isFunction(opts.click)){
			opts.click.call(clickEle, tabItem, customInfo);
		}

		//$(this.tabElement.find('.pubTab-item').get(idx)).find('.pubTab-item-cont').trigger('click');
	}
	// right icon click
	,rightIconClick: function (item){
		var ele;
		if(typeof item ==='object'){
			ele = this.tabElement.find('.pubTab-item[data-tab-id="'+item[this.options.itemKey.id]+'"]');
			ele.find('.pubTab-icon[data-posistion="right"]').trigger('click.pubtab.icon');
		}
	}
	// left icon click
	,leftIconClick: function (item){
		var ele;
		if(typeof item ==='object'){
			ele = this.tabElement.find('.pubTab-item[data-tab-id="'+item[this.options.itemKey.id]+'"]');
			ele.find('.pubTab-icon[data-posistion="left"]').trigger('click.pubtab.icon');
		}
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
	 * @method setTabBlink
	 * @param item {Object or String} -tab item or tab id
	 * @description set tab blink
	 */
	,setTabBlink : function (item){
		var tabEle;
		if(typeof item ==='object'){
			tabEle= this.tabElement.find('.pubTab-item[data-tab-id="'+item[this.options.itemKey.id]+'"]');
		}else{
			tabEle= this.tabElement.find('.pubTab-item[data-tab-id="'+item+'"]');
		}

		if(tabEle.length > 0 && !tabEle.hasClass(this.options.blinkClass)){
			tabEle.find('.pubTab-item-cont').addClass(this.options.blinkClass);
		}
	}
	/**
	 * @method removeTabBlink
	 * @param item {Object or String} -tab item or tab id
	 * @description remove tab blink
	 */
	,removeTabBlink : function (item){
		var tabEle;
		if(typeof item ==='object'){
			tabEle= this.tabElement.find('.pubTab-item[data-tab-id="'+item[this.options.itemKey.id]+'"]');
		}else{
			tabEle= this.tabElement.find('.pubTab-item[data-tab-id="'+item+'"]');
		}

		if(tabEle.length > 0){
			tabEle.find('.pubTab-item-cont').removeClass(this.options.blinkClass);
		}
	}
	/**
	 * @method setActive
	 * @description set active item
	 */
	,setActive: function (item){
		var tabEle= this.tabElement.find('.pubTab-item[data-tab-id="'+item[this.options.itemKey.id]+'"]');

		this.removeTabBlink(item);

		if(this.options.isMultipleContainer === false){
			this._appendTabContent(item);
			return ; 
		}else{
			if(item._isInitialised !== true){
				this._appendTabContent(item);
				item._isInitialised = true; 
			}
		}

		if(tabEle.hasClass('active')){
			return ;
		}

		this.tabElement.find('.pubTab-item.active').removeClass('active');
		this.element.contentContainerElement.find('.pubTab-content.active').removeClass('active');
		this._setHistory(tabEle.attr('data-tab-id'));
		tabEle.addClass('active');
		this.element.contentContainerElement.find('[data-tab-cont-id="'+item[this.options.itemKey.id]+'"]').addClass('active');
		this.movePosition(tabEle.index());
	}
	,reloadContent : function (item){
		this._appendTabContent(item, true);
	}
	/**
	 * @method getActive
	 * @description get active item
	 */
	,getActive : function(){
		var sEle = this.tabElement.find('.pubTab-item.active');
		var idx = sEle.index();
		return {
			idx : idx
			,item : this.options.items[idx]
		};
	}
	/**
	 * @method movePosition
	 * @description item move
	 */
	,movePosition : function(dataIdx){
		var _this = this;

		var tabWidthItem =_this.config.tabWidth[dataIdx];

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
	,addItem : function(addInfo, customInfo){
		var item = addInfo.item
			,idx = addInfo.idx
			,enabled =addInfo.enabled;

		var tabEle= this.tabElement.find('.pubTab-item[data-tab-id="'+item[this.options.itemKey.id]+'"]');

		if(tabEle.length > 0){
			if(enabled !== false){
				this.itemClick(item, customInfo);
				//tabEle.find('.pubTab-item-cont').trigger('click');
			}

			return false;
		}

		item._isInitialised = false;

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
			,dropHtm = this._getDropdownHtml(item);

		var tabItem = this.tabElement.find('.pubTab-item');
		if(tabItem.length < 1){
			this.element.tabContainerElement.prepend(itemHtm);
			this.tabElement.find('.pubTab-dropdown-area').prepend(dropHtm)
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

			this.itemClick(item, customInfo);

			//$(this.tabElement.find('.pubTab-item').get(idx)).find('.pubTab-item-cont').trigger('click');
		}

		return true;
	}
	/**
	 * @method updateTitle
	 * @description update tab title
	 */
	,updateTitle : function (id ,tit , overwriteFlag){
		var tabEle= this.tabElement.find('.pubTab-item[data-tab-id="'+id+'"]');

		if(tabEle.length > 0){

			var titEle = tabEle.find('.pubTab-item-title');

			tabEle.attr('title', tit);
			titEle.empty().html(tit);

			if(overwriteFlag){
				this.options.items[tabEle.index()][this.options.itemKey.title] = tit;
			}

			return;
		}
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

			var titEle = tabEle.find('.pubTab-item-title');

			if($.isFunction(this.options.beforeUpdate)){
				if(this.options.beforeUpdate.call(null,{ele : tabEle , titleEle : titEle, item:item})===false){
					return ;
				}
			}

			this.options.items[tabEle.index()] = objectMerge(this.options.items[tabEle.index()], item);

			var tit = this.options.items[tabEle.index()][this.options.itemKey.title];

			tabEle.attr('title', tit);
			titEle.empty().html(tit);

			if(enabled !== false){
				tabEle.find('.pubTab-item-cont').trigger('click');
			}
			this.calcItemWidth();

			if($.isFunction(this.options.afterUpdate)){
				this.options.afterUpdate.call(null,{ele : tabEle , titleEle : titEle, item:item});
			}

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
	,removeItem : function(item, tabId){
		var idx = item;

		if(item=='all'){
			this.config.tabHistory =[];
			this.options.items = [];

			this.tabElement.find('.pubTab-item[data-tab-id]').remove();
			this.element.contentContainerElement.find('[data-tab-cont-id]').remove();
			this.calcItemWidth();

			if($.isFunction(this.options.removeItem)){
				this.options.removeItem.call(null,'all');
			}
			return ;
		}

		if(item =='other'){
			this.options.items = [this.getItem(tabId)];
			this.config.tabHistory =[tabId];

			this.tabElement.find('.pubTab-item[data-tab-id]').not( '[data-tab-id="'+tabId+'"]' ).remove();
			this.element.contentContainerElement.find('[data-tab-cont-id]').not( '[data-tab-cont-id="'+tabId+'"]' ).remove();
			this.tabElement.find('.pubTab-item[data-tab-id="'+tabId+'"]').find('.pubTab-item-cont').trigger('click');

			this.calcItemWidth();

			if($.isFunction(this.options.removeItem)){
				this.options.removeItem.call(null,'other');
			}
			return ;
		}

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
		this.element.contentContainerElement.find('[data-tab-cont-id="'+reval._tabid+'"]').remove();
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
	/**
	* tab view history
	*/
	,getHistory : function (){
		return this.config.tabHistory;
	}
	,refresh : function (){
		var _this = this;
		var eleW = _this.tabElement.width();

		_this.config.width = eleW;

		if(_this.config.totalWidth > eleW){
			$('#'+_this.prefix+'pubTab-move-space').show();
			_this.tabElement.find('.pubTab-more-button').show();
		}else{
			_this.tabElement.find('.pubTab-item').removeClass('pubTab-hide');
			$('#'+_this.prefix+'pubTab-move-space').hide();
			_this.tabElement.find('.pubTab-more-button').hide();
			_this.element.tabContainerElement.css('left', '0px');
		}

		return this;
	}
	/**
	* set drop item height
	*/
	,setDropdownHeight : function (h){
		if(isNaN(h)){
			return this;
		}
		this.element.dropdownAreaElement.css('max-height',h+'px');
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

		var titleIcon =_opts.titleIcon;

		var itemHtm ='';
		if(cfgIcon.left && cfgIcon.left.html){

			if(!$.isFunction(titleIcon.left.visible) || ($.isFunction(titleIcon.left.visible) && titleIcon.left.visible.call(null, item) !== false)){
				itemHtm += cfgIcon.left.html;
			}
		}

		itemHtm += titleTag;
		if(cfgIcon.right && cfgIcon.right.html){
			if(!$.isFunction(titleIcon.right.visible) || ($.isFunction(titleIcon.right.visible) && titleIcon.right.visible.call(null, item) !== false)){
				itemHtm += cfgIcon.right.html;
			}
		}

		return '<div class="pubTab-item" draggable="'+(_opts.drag.enabled ? true:false)+'" data-tab-id="'+item._tabid+'" title="'+title+'"><div class="pubTab-item-overlay" style=""></div><div class="pubTab-item-cont-wrapper"><div class="pubTab-item-cont '+_opts.addClass+'" >'+itemHtm+'</div></div></div>';
	}
	/**
	 * @method getItemLength
	 * @description tab item length
	 */
	,getTabContentSelector : function (item){
		if(this.options.isMultipleContainer === false){
			return '#'+this.prefix+'ContentContainer>[data-tab-cont-id="'+this.prefix+'"]';
		}

		return '#'+this.prefix+'ContentContainer>[data-tab-cont-id="'+(typeof item ==='string'? item : item[this.options.itemKey.id])+'"]';
		
	}
	/**
	 * @method clearTabContent
	 * @description clear tab content 
	 */
	,clearTabContent : function (item){
		$(this.getTabContentSelector(item)).empty();
	}
	,_appendTabContent : function (item, reloadFlag){
		var contentContainerElement = this.element.contentContainerElement; 

		var tabid = '';
		var contentLoadFlag = false; 
		if(this.options.isMultipleContainer === false){
			tabid = this.prefix;
			contentLoadFlag = true; 
		}else{
			tabid = item[this.options.itemKey.id];

			var contentEleLen = contentContainerElement.find('[data-tab-cont-id="'+tabid+'"]').length; 

			if(contentEleLen < 1){
				contentContainerElement.append(this._getTabContentHtml(item));
			}

			if(reloadFlag === true || item._isInitialised !== true){
				contentLoadFlag = true; 
			}
		}

		if(contentLoadFlag){
			this.options.contentRender(item, contentContainerElement.find('[data-tab-cont-id="'+tabid+'"]'));
		}
		
	}
	,_getTabContentHtml : function (item){
		var style='';
		if($.isFunction(this.options.contentStyleClass)){
			style = this.options.contentStyleClass(item) ||'';
		}else{
			style = this.options.contentStyleClass;
		}

		return '<div class="pubTab-content '+style + (this.options.isMultipleContainer === false ? ' active':'')+'" data-tab-cont-id="'+item._tabid+'"></div>';
	}
	//drop item template
	,_getDropdownHtml : function (item){
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
			for(var i = 0 ;i < itemLen ;i++){
				tabHtm.push(_this._getTabItemHtml(items[i]));
			}
			return tabHtm.join('');
		}

		function dropdownHtml (){
			var dropHtml = [];

			for(var i = itemLen-1 ;i >= 0  ;i--){
				dropHtml.push(_this._getDropdownHtml(items[i]));
			}
			return dropHtml.join('');
		}

		var strHtm = [];
		strHtm.push('<div class="pubTab-wrapper" role="presentation">');
		strHtm.push('	<div id="'+_this.prefix+'pubTab" class="pubTab" style="height:'+_opts.tabHeight+'px;">');
		strHtm.push('		<div id="'+_this.prefix+'pubTab-scroll" class="pubTab-scroll">');
		strHtm.push('			<div id="'+_this.prefix+'pubTab-container" class="pubTab-container" >');
		strHtm.push(tabItemHtml());
		strHtm.push('			<span><div id="'+_this.prefix+'pubTab-move-space" style="display:none;">&nbsp;</div></span>');
		strHtm.push('			</div>');
		strHtm.push('		</div> ');
		
		if(_opts.overItemViewMode =='drop'){
			strHtm.push(' 		<div class="pubTab-more-button"></div>');
		}
		
		strHtm.push('	</div>');

		if(_opts.contentViewSelector===false && _opts.useContentContainer !== false){
			strHtm.push('<div id="'+_this.prefix+'ContentContainer" class="pubTab-content-container" style="height:calc(100% - '+_opts.tabHeight+'px);">');
			
			if(_opts.isMultipleContainer === false){
				strHtm.push(this._getTabContentHtml({_tabid: this.prefix}));
			}else{
				for(var i = 0 ;i < itemLen ;i++){
					strHtm.push(this._getTabContentHtml(items[i]));
				}
			}		
			strHtm.push('</div>');
		}

		if(_opts.overItemViewMode =='drop'){
			var drw = _opts.dropdownWidth; 
			strHtm.push('<div id="'+_this.prefix+'Dropdown" style="width:'+(drw+(drw == 'auto'?'':'px'))+';" class="pubTab-dropdown-wrapper"><ul class="pubTab-dropdown-area">'+dropdownHtml()+'</ul></div>');
		}
		
		strHtm.push('</div>');

		_this.tabElement.empty().html(strHtm.join(''));

		_this.element.tabContainerElement =  $('#'+_this.prefix+'pubTab-container');
		_this.element.tabScrollElement = $('#'+_this.prefix+'pubTab-scroll');
		_this.element.dropdownAreaElement = $('#'+_this.prefix+'Dropdown');
		_this.element.contentContainerElement = (_opts.contentViewSelector === false ? $('#'+_this.prefix+'ContentContainer') : $(_opts.contentViewSelector));
		
		_this.config.moveAreaWidth  = this.tabElement.find('.pubTab-more-button').width();
		$('#'+_this.prefix+'pubTab-move-space').css('width',_this.config.moveAreaWidth);
		_this.element.dropdownAreaElement.css('top', (_this.element.tabContainerElement.height())+'px');

		_this.calcItemWidth();
		_this.setWidth(_opts.width);
		_this.setDropdownHeight(_opts.dropdownHeight)
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
		this.options.theme = themeName;
		this.tabElement.attr('pub-theme', themeName);
	}
	/**
	 * @method getTheme
	 * @description get theme
	 */
	,getTheme : function (){
		return this.options.theme;
	}
	,destroy:function (){
		this.tabElement.find('*').off();
		$._removeData(this.tabElement)
		delete _datastore[this.selector];
		$(this.selector).empty();
		//this = {};
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

$[ pluginName ].setDefaults = function (defaultValue){
	_defaults = objectMerge(_defaults, defaultValue);
}

})(jQuery, window, document);