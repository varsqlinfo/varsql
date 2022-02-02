/**
 * pubMultiselect v0.0.1
 * ========================================================================
 * Copyright 2021 ytkim
 * Licensed under MIT
 * http://www.opensource.org/licenses/mit-license.php
 * url : https://github.com/ytechinfo/pub
 * demo : http://pub.moaview.com/
*/
;(function ($, window, document) {
if (!Object.keys) {
  Object.keys = function(obj) {
    var keys = [];

    for (var i in obj) {
      if (obj.hasOwnProperty(i)) {
        keys.push(i);
      }
    }

    return keys;
  };
}

var pluginName = "pubMultiselect"
,initialized = false
,_datastore = {}
,_defaults = {
	mode : 'double'	// single, double
	,orientation : 'x'	// x = 가로보기 , y = 세로보기
	,header : {
		enableSourceLabel : false 	// header label 보일지 여부
		,enableTargetLabel : false 	// header label 보일지 여부
	}
	,body : {
		enableMoveBtn : true	// 이동 버튼 보이기 여부
		,moveBtnSize : 50	// item move button 영역 width 값
		,enableItemEvtBtn : false // 추가,삭제 버튼 보이기
	}
	,width : 'auto'
	,height : 'auto'
	,maxSize : -1	// 추가 가능한 max size
	,maxSizeMsg : '개 까지 등록 가능합니다.'	// 추가 가능한 max size가 넘었을경우 메시지 String, Function
	,useMultiSelect : true	// ctrl , shift key 이용해서 다중 선택하기 여부
	,containment : ''		// 경계 영역
	,useDragMove : false	// drag해서 이동할지 여부.
	,useDragSort : false // target drag 해서 정렬할지 여부.
	,addPosition : 'last'	// 추가 되는 방향키로 추가시 어디를 추가할지. ex(source, last)
	,duplicateCheck : true	// 중복 추가 여부.
	,pageInfo :{	// 다중으로 관리할경우 처리.
		max : 1		// max page 값
		,currPage : 1	// 현재 값
		,pageNumKey : 'pageNo'	// page number key 
		,emptyMessage : '더블클릭해서 추가하세요'	// item 없을때 메시지
	}
	,itemSelector :'.pub-select-item'	// select item selector
	,addItemCheckStyle :'text-selected'	// add item class
	,selectStyleClass : 'selected'	// select item class
	,items :[]					// item
	,source : {	// source item
		idKey : 'CODE_ID' 	// opt id key
		,nameKey : 'CODE_NM' // opt value key
		,searchAttrName : '_name'	// search item name attribute
		,searchAttrKey : '' // search item key attribute
		,emptyMessage:''	// message
		,items: []			// item
		,click : false	// 클릭시 이벤트
		,render: function (item){	// 아이템 추가될 템플릿.
			return '<span>'+item.text+'</span>'
		}
	}
	,target : {
		idKey : 'CODE_ID' // opt id key
		,nameKey : 'CODE_NM' // opt value key
		,items: []			// item
		,emptyMessage:'' 	// message
		,click : false	// 클릭시 이벤트
		,dblclick : false
		,render: function (item){	// 아이템 추가될 템플릿.
			return '<span>'+item.text+'</span>'
		}
	}
	,message : { // 방향키 있을때 메시지
		addEmpty : false
		,delEmpty : false
		,duplicate :false
	}
	,beforeMove : false		// 이동전 이벤트
	,beforeItemMove : false	 // 이동전 이벤트
	,afterSourceMove : false	// source 이동후 이벤트
	,compleateSourceMove : false	// source 이동 완료 이벤트
	,beforeTargetMove : false	// target 이동전  이벤트
	,afterTargetMove : false	// target 이동후  이벤트
	,compleateTargetMove : false	// target 이동 완료  이벤트
	,footer : {
		enable : false
	}
	,i18 : {
		helpMessage : '* 목록을 마우스로 드래그앤 드롭하거나 더블클릭 하세요.'
		,sourceLabel : 'Source'
		,targetLabel : 'Target'
		,upLabel : 'Up'
		,downLabel : 'Down'
		,add : 'Add'
		,allAdd : 'All add'
		,remove : 'Remove'
		,allRemove : 'All Remove'
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

function getHashCode (str){
	var hash = 0;
	if (str.length == 0) return hash;
	for (var i = 0; i < str.length; i++) {
		var tmpChar = str.charCodeAt(i);
		hash = ((hash<<5)-hash)+tmpChar;
		hash = hash & hash;
	}
	return ''+hash+'98';
}

function Plugin(selector, options) {

	this.selector = selector;
	this.prefix = 'pubMultiselect'+getHashCode(selector);

	this.options = objectMerge({}, _defaults, options);

	this.config ={
		currPage : this.options.pageInfo.currPage
		, pageNumInfo:{}
		, itemKey :{
			sourceIdx :{}
		}
		, focus : false
		, focusType : ''
	};

	this.element = {
		container : $(selector)
	}

	this.init();

	return this;
}

Plugin.prototype ={
	/**
	 * @method init
	 * @description 자동완성 초기화
	 */
	init :function(){
		var _this = this;

		_this.addItemList ={'1':{}};

		_this.render();

		_this._initItem();
		
		_this.initEvt();
	}
	/**
	 * @method initEvt
	 * @description 이벤트 초기화
	 */
	,initEvt : function (){
		var _this = this
			,opts = _this.options;

		_this.initSourceEvt();
		_this.initTargetEvt();

		//추가, 삭제
		this.element.container.on('click.pub-btn', '.pub-multiselect-btn', function (e){
			var sEle = $(this);
			var mode = sEle.data('mode');

			if(mode == 'add'){
				_this.sourceMove();
			}else if(mode == 'del'){
				_this.targetMove();
			}else if(mode=='item-add'){
				_this.sourceMove({
					items : [sEle.closest('.pub-select-item')[0]]
				});
				return false; 
			}else if(mode=='item-del'){
				_this.targetMove({
					items : [sEle.closest('.pub-select-item')[0]]
				});
				return false; 
			}else{
				_this.move(mode);
			}
		});
		
		$(document).off('keydown.'+_this.prefix);
		$(document).on("keydown." + _this.prefix, function (e) {

			if(!_this.config.focus) return ;

			var evtKey = window.event ? e.keyCode : e.which;

			if (e.metaKey || e.ctrlKey) { // copy

				 if(evtKey==65){ // ctrl + a
					_this.setItemSelection(e, _this.config.focusType, {mode : 'all'})
					return false;
				}
			}
		});

		$(document).off('mousedown.'+_this.prefix);
		$(document).on('mousedown.'+_this.prefix, 'html', function (e) {
			if(!_this.config.focus){
				return true;
			}

			if(e.which !==2 && $(e.target).closest('#'+_this.prefix+' .pub-multiselect-area').length < 1){
				_this.config.focus = false;
			}
		});

		if(opts.pageInfo.max > 1){
			
			$('#'+_this.prefix+' .pub-multiselect-page-num').on('click',function (e){
				var currEle = $(this);

				var beforeEle = $('#'+_this.prefix+' .pub-multiselect-page-num.selected');

				if(beforeEle.length > 0){
					_this.config.pageNumInfo[beforeEle.attr('data-page')] =[];
					_this.config.pageNumInfo[beforeEle.attr('data-page')].push(_this.element.target.clone().html());

					if(currEle.hasClass('selected')){
						return false;
					}
				}

				beforeEle.removeClass('selected');
				currEle.addClass('selected');

				var currPageNo = currEle.attr('data-page');

				_this.config.currPage = currPageNo;

				if(opts.pageInfo.emptyMessage !== false && _this.config.pageNumInfo[currPageNo].length < 1){
					_this.element.target.empty().html(_this.getEmptyMessage());
				}else{
					_this.element.target.empty().html(_this.config.pageNumInfo[currPageNo].join(''));
				}
			})

			$('#'+_this.prefix+' .pub-multiselect-page-num[data-page="'+_this.config.currPage+'"]').addClass('selected');
		}
	}
	/**
	 * @method _initItem
	 * @description selectbox 정보 초기화
	 */
	,_initItem : function (){
		var _this = this

		_this.setTargetItem(_this.options.target.items);
		_this.setSourceItem(_this.options.source.items);
	}
	/**
	 * @method setSourceItem
	 * @param items {Array} items array
	 * @description item 그리기.
	 */
	,setSourceItem : function (items){
		var _this = this
			,_opts = _this.options
			,sourceOpt = _opts.source
			,strHtm = []
			,tmpItem;

		var len = items.length
			,valKey = sourceOpt.idKey;

		var pageMaxVal = _opts.pageInfo.max;

		if(len > 0){
			for(var i=0 ;i < len; i++){
				tmpItem = items[i];
				var tmpSelctOptVal = tmpItem[valKey];
				var selectFlag = false;
				for(var j = 1 ;j <=pageMaxVal; j++){
					if(typeof _this.addItemList[j][tmpSelctOptVal] !=='undefined') {
						selectFlag = true;
						continue;
					}
				}

				strHtm.push(_this.getItemHtml('source', tmpSelctOptVal, tmpItem, selectFlag));
				_this.config.itemKey.sourceIdx[tmpSelctOptVal] = tmpItem;
			}
		}else{
			strHtm.push(_this.getEmptyMessage(sourceOpt.emptyMessage));
		}
		_this.element.source.empty().html(strHtm.join(''));
	}
	/**
	 * @method setSourceItem
	 * @param items {Array} items array
	 * @description item 그리기.
	 */
	,setTargetItem : function (items){
		var _this = this
			,_opts = _this.options
			,tmpItem;

		var targetOpt= _opts.target;

		var len = items.length
			,valKey = targetOpt.idKey;

		var pageNumKey = _opts.pageInfo.pageNumKey;
		var maxPageNo = _opts.pageInfo.max; 
		var currPageNo = _this.config.currPage; 

		_this.addItemList[currPageNo] ={};
		_this.config.pageNumInfo[currPageNo] = [];

		if(len > 0){
			for(var i=0 ;i < len; i++){
				tmpItem = items[i];

				var tmpSelctOptVal = tmpItem[valKey];
				var pageNo = tmpItem[pageNumKey] || currPageNo; 

				if(pageNo > maxPageNo){
					continue; 
				}

				if(typeof _this.config.pageNumInfo[pageNo] ==='undefined'){
					_this.config.pageNumInfo[pageNo] = [];
				}

				if(typeof _this.addItemList[pageNo] === 'undefined'){
					throw 'pageInfo undefined '+ pageNumKey+' :'+pageNo;
				}

				tmpItem['_CU'] = 'U';
				_this.addItemList[pageNo][tmpSelctOptVal] = tmpItem;

				_this.config.pageNumInfo[pageNo].push(_this.getItemHtml('target', tmpSelctOptVal, tmpItem))

				_this.addSourceItemSelecClass(_opts, tmpSelctOptVal);
			}

			_this.sourceItemSelectCheck(_opts);

			if(_this.config.pageNumInfo[currPageNo].length > 0){
				_this.element.target.empty().html(_this.config.pageNumInfo[currPageNo].join(''));
			}else{
				_this.element.target.empty().html(_this.getEmptyMessage(targetOpt.emptyMessage));
			}
		}else{
			_this.element.target.empty().html(_this.getEmptyMessage(targetOpt.emptyMessage));
			_this.element.source.find(_opts.itemSelector).removeClass(_opts.addItemCheckStyle);
		}
	}
	// sources select item add class
	,addSourceItemSelecClass : function (_opts, tmpSelctOptVal ){
		this.element.source.find(_opts.itemSelector+'[data-val="'+tmpSelctOptVal +'"]').addClass(_opts.addItemCheckStyle+ ' target-check');
	}
	// source 체크 된 item unselect
	,sourceItemSelectCheck: function (_opts){
		this.element.source.find(_opts.itemSelector).each(function (){
			var sEle = $(this);
			if(!sEle.hasClass('target-check')){
				sEle.removeClass(_opts.addItemCheckStyle);
			}

			sEle.removeClass('target-check');
		})
	}
	,_setFocus : function (selectType){
		this.config.focus = true;
		this.config.focusType = selectType;
	}
	/**
	 * @method initSourceEvt
	 * @description source 소스 이벤트 초기화
	 */
	,initSourceEvt : function (){
		var _this = this
			,opts = _this.options;

		_this.element.source.on('click.pub-multiselect', opts.itemSelector, function (e){
			_this._setClickEvent(e, 'source', $(this));
		})

		_this.element.source.on('dblclick.pub-multiselect', opts.itemSelector, function (e){
			_this._setFocus('source');
			_this.sourceMove();
			return ;
		})

		_this.element.source.on('selectstart',function(){ return false; });

		if(opts.useDragMove !== false){

			_this.element.source.find(opts.itemSelector).draggable({
				appendTo: "body"
				,containment : (opts.containment|| 'parent')
				,scroll : false
				,connectToSortable: '#'+_this.prefix+' [data-type="target"]' 
				,classes: {
					"ui-draggable": 'pub-multiselect-drag-handle'
				}
				,helper: function (event){
					var selectItem = _this.getSelectElement(_this.element.source);

					if(selectItem.length  < 1){
						return '<div></div>';
					}

					var strHtm = [];
					$.each(selectItem, function (i ,item ){
						strHtm.push('<div class="pub-multiselect-add-item">'+$(item).html()+'</div>')
					});

					return '<div class="pub-multiselect-add-helper-wrapper">'+strHtm.join('')+'</div>';
				}
				,start:function(e){
					var selectItem = _this.getSelectElement(_this.element.source);

					if( $.inArray(opts.selectStyleClass,e.currentTarget.classList) < 0 || selectItem.length < 1) {
						e.preventDefault();
						e.stopPropagation();
						return false;
					}
				}
				,stop : function (e,ui){
					return true;
				}
			});
		}
	}
	/**
	 * @method initTargetEvt
	 * @description target 소스 이벤트 초기화
	 */
	,initTargetEvt : function (){
		var _this = this
			,opts = _this.options;

		_this.element.target.on('click.pub-multiselect', opts.itemSelector, function (e){
			_this._setClickEvent(e, 'target', $(this));
		})

		_this.element.target.on('dblclick.pub-multiselect', opts.itemSelector, function (e){
			if($.isFunction(_this.options.target.dblclick)){

				if(_this.options.target.dblclick.call($(this),e, _this.addItemList[_this.config.currPage][_this.getItemVal($(this))]) ===false){
					return false;
				};
			}
			_this.element.target.find(opts.itemSelector).removeClass(opts.addItemCheckStyle);
			$(this).addClass(opts.addItemCheckStyle);
			_this.targetMove();
		})

		_this.element.target.on('selectstart',function(){ return false; });

		_this.element.target.sortable({
			scroll: true
		   ,containment : "parent"
		   ,cancel: ((opts.useDragSort !== false) ?'li:not(.'+opts.selectStyleClass+')' :'li')
		   ,start:function(e,ui){

			   try{
				   var uiItem = $(ui.item);
				   if(!uiItem.hasClass('ui-draggable')){
					   var selectItem = _this.getSelectElement(_this.element.target);

					   if( $.inArray(opts.selectStyleClass,e.currentTarget.classList) < 0 || selectItem.length < 1) {
						   //return false;
					   }
				   }
			   }catch(e){
				   console.log(e);
			   }
		   }
		   ,update : function (e, ui){
			   var uiItem = $(ui.item);
			   if(uiItem.hasClass('pub-multiselect-add-helper-wrapper')){
				   var addHtm = _this.sourceMove({
						returnFlag : true
				   });

				   uiItem.replaceWith(addHtm);
			   }else{
				   // 정렬에 대한 item 순서 처리.
			   }
		   }
		   ,change : function (e,ui){
			   var uiItem = $(ui.position.top);
		   }
	   })
	}
	/**
	 * @method _setClickEvent
	 * @param e {Event} 이벤트
	 * @param selectType {String} source, target
	 * @param sEle {Element} 선택된 element
	 * @description target 소스 이벤트 초기화
	 */
	,_setClickEvent : function (e, selectType, sEle){
		var _this = this
			,opts = _this.options
			,evtElement
			,selectItem;

		_this._setFocus(selectType);

		if(selectType =='source'){
			evtElement = _this.element.source;
			selectItem = opts.source;
		}else{
			evtElement = _this.element.target;
			selectItem = opts.target;
		}

		var evt = window.event || e;

		var lastClickEle = evtElement.find(opts.itemSelector+'[data-last-click="Y"]');
		var onlyClickFlag = false;
		if(opts.useMultiSelect ===true){
			if (evt.shiftKey){
				var allItem = evtElement.find(opts.itemSelector);
				var beforeIdx = allItem.index(lastClickEle)
					,currIdx = allItem.index(sEle);

				var source = Math.min(beforeIdx, currIdx)
					,last = Math.max(beforeIdx, currIdx);

				this.setItemSelection(e, selectType, {mode : 'selection', start : source, end : last});

			}else{
				sEle.attr('data-last-click','Y');
				lastClickEle.removeAttr('data-last-click');
				if(evt.ctrlKey){
					if(sEle.hasClass(opts.selectStyleClass)){
						sEle.removeClass(opts.selectStyleClass);
					}else{
						sEle.addClass(opts.selectStyleClass);
					}
				}else{
					onlyClickFlag=true;
				}
			}
		}else{
			sEle.attr('data-last-click','Y');
			onlyClickFlag = true;
		}

		if(onlyClickFlag){
			if($.isFunction(selectItem.click)){
				selectItem.click.call(sEle , e, _this.addItemList[_this.config.currPage][_this.getItemVal(sEle)]);
			}
			evtElement.find(opts.itemSelector+'.'+ opts.selectStyleClass).removeClass(opts.selectStyleClass);
			sEle.addClass(opts.selectStyleClass);
		}
	}
	,setItemSelection : function (e, selectType, opt){
		var _this = this
			,opts = _this.options
			,evtElement

		if(selectType =='source'){
			evtElement = _this.element.source;
		}else{
			evtElement = _this.element.target;
		}

		var mode = opt.mode;
		var start = opt.start
			,end =  opt.end; 
				
		if(mode=='all'){
			evtElement.find(opts.itemSelector).addClass(opts.selectStyleClass);
		}else if(mode=='selection'){

			evtElement.find(opts.itemSelector+'.'+ opts.selectStyleClass).removeClass(opts.selectStyleClass);

			var allItem = evtElement.find(opts.itemSelector);
			for(var i=end; i >= start; i--){
				$(allItem[i]).addClass(opts.selectStyleClass);
			}
		}
	}
	/**
	 * @method getTargetItem
	 * @description 추가된 아이템 구하기.
	 */
	,getTargetItem : function (itemKey, pageNum){
		var  _this = this;

		if(itemKey){
			if(typeof pageNum ==='undefined'){
				return _this.addItemList[_this.config.currPage][itemKey];
			}else{
				return _this.addItemList[pageNum][itemKey];
			}
		}else{
			var reInfo =[];

			var currEle = $('#'+_this.prefix+' .pub-multiselect-page-num.selected');
			_this.config.pageNumInfo[currEle.attr('data-page')||_this.config.currPage] =[];
			_this.config.pageNumInfo[currEle.attr('data-page')||_this.config.currPage].push(_this.element.target.clone().html());

			var pageNumHtm = _this.config.pageNumInfo;

			for(var pageHtmKey in pageNumHtm){

				var tmpHtmInfo = pageNumHtm[pageHtmKey].join('');

				var tmpHtmInfoEle = $(tmpHtmInfo);

				if(tmpHtmInfoEle.hasClass("empty-message")) continue;

				tmpHtmInfoEle.each(function (i ,item){

					var tmpPageNo = $(item).attr('data-pageno');
					var addItem = _this.addItemList[tmpPageNo][$(item).attr('data-val')];
					addItem['_pageNum'] = tmpPageNo;
					reInfo.push(addItem);
				})
			}
			return reInfo;
		}
	}
	/**
	 * @method getSelectElement
	 * @description 선택된 html eleement 얻기.
	 */
	,getSelectElement : function (evtElement){
		return 	evtElement ? evtElement.find(this.options.itemSelector+'.'+ this.options.selectStyleClass) : this.element.target.find(this.options.itemSelector+'.'+ this.options.selectStyleClass);
	}
	,getElement : function (key){
		return 	this.element.target.find('[data-val="'+key+'"]');
	}
	/**
	 * @method addItemStausUpdate
	 * @param itemKey {String} 아이템 key
	 * @description 등록된 아이템 상태 업데이트.
	 */
	,addItemStausUpdate : function (itemKey){
		var tmpItem = this.addItemList[this.config.currPage][itemKey];
		if(typeof tmpItem !=='undefined'){
			tmpItem['_CU'] = 'CU';
			this.getElement(itemKey).replaceWith(this.getItemHtml('target',itemKey,tmpItem));
		}
	}
	/**
	 * @method getItemVal
	 * @param itemEle {Element} value를 구할 element
	 * @description value 구하기.
	 */
	,getItemVal : function (itemEle){
		return itemEle.attr('data-val');
	}
	/**
	 * @method move
	 * @param type {String} up or down
	 * @description 아래위 이동
	 */
	,move :function (type){
		var _this = this;
		var selectElement =_this.getSelectElement(_this.element.target);
		var selectLen = selectElement.length;

		if(selectLen < 1) return ;

		var selectStyleClass =this.options.selectStyleClass;
		if(type=='up'){
			for(var i =0 ;i <selectLen ;i++){
				var currItem = $(selectElement[i])
					,prevItem = $(currItem.prev());

				if(!prevItem.hasClass(selectStyleClass)) {
					currItem.after(prevItem);
				}
			}
		}else{
			for(var i =selectLen-1 ;i >=0 ;i--){
				var currItem = $(selectElement[i])
					,nextItem = $(currItem.next());

				if(!nextItem.hasClass(selectStyleClass)) {
					currItem.before(nextItem);
				}
			}
		}
		
		$(selectElement[0]).attr('tabindex','-1').focus().removeAttr('tabindex');
	}
	/**
	 * @method sourceMove
	 * @param opt {Object} true or false
	 * 			
	 * @description source -> target 이동.
	 */
	,sourceMove : function (opt){
		var _this = this
			,opts = _this.options;
		
		opt = opt||{};

		var returnFlag = opt.returnFlag;
		var selectVal = opt.items || _this.getSelectElement(_this.element.source);

		if($.isFunction(opts.beforeMove)){
			if(opts.beforeMove('source') === false){
				return ;
			};
		}

		if(selectVal.length > 0){
			var tmpVal = '',tmpObj;
			var	strHtm = [];

			var addItemCount = _this.element.target.find(opts.itemSelector+':not(.ui-draggable)').length;

			var addItemKey = [];
			var addElements = [];
			var addItemMap = {};
			var dupChkFlag = true;
			var firstKey = '';

			for(var i =0; i <selectVal.length; i++){
				var item = selectVal[i];

				tmpObj = $(item);
				tmpVal=_this.getItemVal(tmpObj);

				var addChkFlag = typeof _this.addItemList[_this.config.currPage][tmpVal] ==='undefined';
				if(dupChkFlag && addChkFlag){
					dupChkFlag = false;
				}

				if(!addChkFlag) continue;

				if($.isFunction(opts.beforeItemMove)){
					if(opts.beforeItemMove(tmpObj) === false){
						return false;
					};
				}

				if(opts.maxSize != -1  && addItemCount >= opts.maxSize){

					if($.isFunction(opts.maxSizeMsg)){
						opts.maxSizeMsg.call();
					}else{
						alert(opts.maxSize+' '+opts.maxSizeMsg);
					}
					if(returnFlag===true){
						return false;
					}

					return false;
				}
				addItemCount+=1;

				var selectItem = _this.config.itemKey.sourceIdx[tmpVal];
				var _addItem = objectMerge({}, selectItem);
				_addItem['_CU'] = 'C';

				addItemKey.push(tmpVal);
				firstKey = tmpVal;

				addItemMap[tmpVal] =_addItem;

				strHtm.push(_this.getItemHtml('target',tmpVal ,selectItem ));

				addElements.push(tmpObj);

				if($.isFunction(opts.afterSourceMove)){
					opts.afterSourceMove(tmpObj);
				}
			}

			if(addItemKey.length  < 1){
				return ;
			}

			if(opts.duplicateCheck===true && dupChkFlag){
			  if($.isFunction(opts.message.duplicate)){
					opts.message.duplicate.call();
				}else if(opts.message.duplicate !== false){
					alert(opts.message.duplicate);
				}

				return false;
			}

			if($.isFunction(opts.compleateSourceMove)){
				if(opts.compleateSourceMove(addItemKey)===false) return false;
			}

			_this.element.target.find('.empty-message').remove();

			for(var i =0; i <addElements.length; i++){
				addElements[i].addClass(opts.addItemCheckStyle);
			}

			for(var key in addItemMap){
				_this.addItemList[_this.config.currPage][key] =addItemMap[key];
			}

			if(returnFlag===true){
				return strHtm.join('');
			}else{
				if(opts.addPosition=='first'){
					_this.element.target.prepend(strHtm.join(''));
					
				}else{
					_this.element.target.append(strHtm.join(''));
				}

				_this.element.target.find('[data-val="'+firstKey+'"]').attr('tabindex','-1').focus().removeAttr('tabindex')
			}
		}else{
			if($.isFunction(opts.message.addEmpty)){
				opts.message.addEmpty.call();
			}else if(opts.message.addEmpty !== false){
				alert(opts.message.addEmpty);
			}
			return ;
		}
	}
	/**
	 * @method getItemVal
	 * @param opt {Object} value를 구할 element
	 * @description value 구하기.
	 */
	,targetMove : function (opt){
		var _this = this;

		opt = opt||{};

		var selectVal = opt.items || _this.getSelectElement(_this.element.source);

		var selectVal = _this.getSelectElement(_this.element.target);

		if($.isFunction(_this.options.beforeMove)){
			if(_this.options.beforeMove('target') === false){
				return ;
			};
		}

		if(selectVal.length >0){
			var removeItem;
			var deleteItemKey = [];

			for(var i =0; i <selectVal.length; i++){
				var item = selectVal[i];

				var tmpKey = $(item).attr('data-val');
				removeItem = _this.config.itemKey.sourceIdx[tmpKey];

				if($.isFunction(_this.options.beforeTargetMove)){
					if(_this.options.beforeTargetMove($(item))===false) return false;
				}

				var removeFlag = false;

				for(var tmpPageNo in _this.addItemList){
					if(_this.config.currPage != tmpPageNo){
						if(typeof _this.addItemList[tmpPageNo][tmpKey] !=='undefined'){
							removeFlag = true ;
							break;
						}
					}
				}
				if(removeItem){
					if(removeFlag !== true){
						_this.element.source.find(_this.options.itemSelector+'[data-val="'+tmpKey+'"]').removeClass(_this.options.addItemCheckStyle);
					}
				}
				$(item).remove();

				deleteItemKey.push(tmpKey);

				delete _this.addItemList[_this.config.currPage][tmpKey];

				if($.isFunction(_this.options.afterTargetMove)){
					_this.options.afterTargetMove(removeItem);
				}
			}

			if(Object.keys(_this.addItemList[_this.config.currPage]).length < 1){
				_this.element.target.empty().html(_this.getEmptyMessage(_this.options.target.emptyMessage));
			}

			if($.isFunction(_this.options.compleateTargetMove)){
				_this.options.compleateTargetMove(deleteItemKey);
			}
		}else{
			if(_this.options.message.delEmpty !== false){
				alert(_this.options.message.delEmpty);
			}
			return ;
		}
	}
	,render : function (){
		var strHtm = [];
	
		if(this.options.orientation =='y'){
			strHtm =this.getVerticalTemplate();
		}else{
			strHtm =this.getHorizontalTemplate();
		}

		this.element.container.empty('').html(strHtm.join(''));
		
		this.element.source = this.element.container.find('[data-type="source"]');
		this.element.target = this.element.container.find('[data-type="target"]');
	}
	,getVerticalTemplate : function (){
		var strHtm = [];

		var opts = this.options;
		
		var width = isNaN(opts.width) ? '100%' : opts.width+'px'
			,height = (isNaN(opts.height) ? this.element.container.height() : opts.height)
			,moveBtnSize = isNaN(opts.body.moveBtnSize) ? _defaults.body.moveBtnSize :opts.body.moveBtnSize;
		
		var enableMoveBtn = opts.body.enableMoveBtn === false ? false :true
			,enablePage =  opts.pageInfo.max > 1 ? true :false;
			
		var bodyHeight = height -(opts.footer.enable ? 30 : 0);		
		bodyHeight = bodyHeight - (enablePage ? 22 : 0);
		bodyHeight = bodyHeight - (enableMoveBtn ? moveBtnSize : 0);
		
		var labelHalfHeight =(opts.header.enableSourceLabel ===false) ? 36/2 : 0;
		strHtm.push('<div id="'+this.prefix+'" style="width:'+width+';height:'+height+'px;" class="pub-multiselect">');
		strHtm.push('	<div class="pub-multiselect-body vertical '+(opts.body.enableItemEvtBtn ?'show-row-item-btn' : '' )+'">'); // body start

		if(opts.mode !='single'){
			
			
			strHtm.push('  <div style="height:'+(bodyHeight/2 - labelHalfHeight)+'px;">');
			
			if(opts.header.enableSourceLabel===true){
				strHtm.push('	<div class="pub-multiselect-label">'+this.options.i18.sourceLabel+'</div>');
			}

			strHtm.push('	<div class="pub-multiselect-area '+(opts.header.enableSourceLabel?'':'header-hide')+'"><ul class="pub-multiselect-items" data-type="source"></ul></div>');
			strHtm.push(' </div>');
			
			if(enableMoveBtn){
				strHtm.push('<div class="pub-multiselect-move-btn" style="height:'+moveBtnSize+'px;line-height:'+moveBtnSize+'px;">');
				strHtm.push('	<button type="button" style="margin-bottom:5px;" class="pub-multiselect-btn" data-mode="add" title="'+this.options.i18.add+'">'+this.options.i18.add+'</button>');
				strHtm.push('	<button type="button" class="pub-multiselect-btn" data-mode="del" title="'+this.options.i18.remove+'">'+this.options.i18.remove+'</button>');
				strHtm.push('</div>');
			}	
		}
		
		strHtm.push(' <div style="height:'+(bodyHeight/2+labelHalfHeight)+'px;">');

		if(opts.header.enableTargetLabel===true){
			strHtm.push('  <div class="pub-multiselect-label">'+this.options.i18.targetLabel+'</div>');
		}
		
		strHtm.push('  <div class="pub-multiselect-area '+(opts.header.enableTargetLabel?'':'header-hide')+'"><ul class="pub-multiselect-items" data-type="target"></ul></div>');
		strHtm.push(' </div>');

		// 페이지 정보
		if(enablePage){
			strHtm.push('				   <div class="pub-multiselect-paging">');
			for(var i = 1 ;i <=opts.pageInfo.max; i++){
				this.config.pageNumInfo[i+''] =[];
				this.addItemList[i+''] ={};
				strHtm.push('<a href="javascript:;" class="pub-multiselect-page-num" data-page="'+i+'" title="'+i+'"><span class="page-no-text">'+i+'</span></a>');
			}
			strHtm.push('					</div>');
		}

		strHtm.push('	</div>'); // body end

		// footer
		if(this.options.footer.enable===true){
			strHtm.push('	<div class="pubMultiselect-footer">');
			strHtm.push('		<button type="button" class="pub-multiselect-btn" data-mode="up" style="margin-right:5px;">'+this.options.i18.upLabel+'</button>');
			strHtm.push('		<button type="button" class="pub-multiselect-btn" data-mode="down">'+this.options.i18.downLabel+'</button>');
			strHtm.push('	</div>');	
		}
		
		strHtm.push('</div>');

		return strHtm;
	}
	,getHorizontalTemplate : function (){
		var strHtm = [];

		var opts = this.options;
		
		var width = isNaN(opts.width) ? '100%' : opts.width+'px'
			,height = (isNaN(opts.height) ? this.element.container.height() : opts.height)
			
		var bodyHeight = height -(opts.footer.enable ? 30 : 0) - (enablePage ? 22 : 0) - 2; // border 2 
							
		var enableMoveBtn = opts.body.enableMoveBtn === false ? false :true
			,enablePage =  opts.pageInfo.max > 1 ? true :false;

		strHtm.push('<div id="'+this.prefix+'" style="width:'+width+';height:'+height+'px;" class="pub-multiselect">');
		strHtm.push('	<div class="pub-multiselect-body horizontal '+(opts.body.enableItemEvtBtn ?'show-row-item-btn' : '' )+'">');
		strHtm.push('		<table>');
		strHtm.push('		<colgroup>');
		if(opts.mode =='single'){
			strHtm.push('<col style="width:100%">');
		}else{
			if(enableMoveBtn){
				var moveBtnSize = isNaN(opts.body.moveBtnSize) ? _defaults.body.moveBtnSize :opts.body.moveBtnSize;
				strHtm.push('<col style="width:calc(50% - '+(moveBtnSize/2)+'px)">');
				strHtm.push('<col style="width:'+moveBtnSize+'px">');
				strHtm.push('<col style="width:calc(50% - '+(moveBtnSize/2)+'px)">');	
			}else{
				strHtm.push('<col style="width:50%">');
				strHtm.push('<col style="width:50%">');
			}
		}

		strHtm.push('		</colgroup>');					
		strHtm.push('			<tbody>');
		strHtm.push('				<tr>');
		if(opts.mode !='single'){
			strHtm.push('					<td>');
			strHtm.push('					  <div style="height:'+bodyHeight+'px;">');
			
			if(opts.header.enableSourceLabel===true){
				strHtm.push('						<div class="pub-multiselect-label">'+this.options.i18.sourceLabel+'</div>');
			}

			strHtm.push('						<div class="pub-multiselect-area '+(opts.header.enableSourceLabel?'':'header-hide')+'"><ul class="pub-multiselect-items" data-type="source"></ul></div>');
			strHtm.push('					  </div>');
			strHtm.push('					</td>');

			if(enableMoveBtn){
				strHtm.push('					<td class="pub-multiselect-move-btn">');
				strHtm.push('						<button type="button" style="margin-bottom:5px;" class="pub-multiselect-btn" data-mode="add" title="'+this.options.i18.add+'"></button><br/>');
				strHtm.push('						<button type="button" class="pub-multiselect-btn" data-mode="del" title="'+this.options.i18.remove+'"></button>');
				strHtm.push('					</td>');
			}
		}
		
		strHtm.push('					<td>');
		strHtm.push('					  <div style="height:'+bodyHeight+'px;">');

		if(opts.header.enableTargetLabel===true){
			strHtm.push('						<div class="pub-multiselect-label">'+this.options.i18.targetLabel+'</div>');
		}
		
		strHtm.push('						<div class="pub-multiselect-area '+(opts.header.enableTargetLabel?'':'header-hide')+'"><ul class="pub-multiselect-items" data-type="target"></ul></div>');
		strHtm.push('					  </div>');

		// 페이지 정보
		if(enablePage){
			strHtm.push('				   <div class="pub-multiselect-paging">');
			for(var i = 1 ;i <=opts.pageInfo.max; i++){
				this.config.pageNumInfo[i+''] =[];
				this.addItemList[i+''] ={};
				strHtm.push('<a href="javascript:;" class="pub-multiselect-page-num" data-page="'+i+'" title="'+i+'"><span class="page-no-text">'+i+'</span></a>');
			}
			strHtm.push('					</div>');
		}

		strHtm.push('					</td>');
		strHtm.push('				</tr>');
		strHtm.push('			</tbody>');
		strHtm.push('		</table>');
		strHtm.push('	</div>');

		// footer
		if(this.options.footer.enable===true){
			strHtm.push('	<div class="pubMultiselect-footer">');
			strHtm.push('		<button type="button" class="pub-multiselect-btn" data-mode="up" style="margin-right:5px;">'+this.options.i18.upLabel+'</button>');
			strHtm.push('		<button type="button" class="pub-multiselect-btn" data-mode="down">'+this.options.i18.downLabel+'</button>');
			strHtm.push('	</div>');	
		}
		
		strHtm.push('</div>');

		return strHtm;
	}
	/**
	 * @method getEmptyMessage
	 * @description empty item message
	 */
	,getEmptyMessage : function (msg){
		return '<li class="empty-message">'+(msg||this.options.pageInfo.emptyMessage)+'</li>';
	}
	/**
	 * @method getItemHtml
	 * @param type {String} source , target
	 * @param seletVal {String} source , target
	 * @param tmpItem {Object} select item
	 * @description 선택된 html 얻기.
	 */
	,getItemHtml: function (type  , seletVal , tmpItem, selectFlag){
		var _this = this
			, _opts = _this.options
			, sourceItem = _opts.source
			, txtKey = sourceItem.nameKey
			, searchAttrName = sourceItem.searchAttrName
			, searchAttrKey = sourceItem.searchAttrKey == '' ? txtKey : sourceItem.searchAttrKey
			, renderTemplate = '';

		var nameVal = tmpItem[searchAttrKey]+''; 

		if(type=='source'){
			var styleClass ='';
			if(_this.addItemList[_this.config.currPage][seletVal]){
				styleClass += ' '+_opts.addItemCheckStyle;
			}

			if($.isFunction(sourceItem.render)){
				renderTemplate = sourceItem.render.call(sourceItem,{text : tmpItem[txtKey] , item : tmpItem});
			}else{
				renderTemplate = tmpItem[txtKey];
			}

			if(_opts.body.enableItemEvtBtn){
				renderTemplate += '<button type="button" class="pub-multiselect-btn" data-mode="item-add">'+this.options.i18.add+'</button>';
			}

			return '<li data-val="'+seletVal+'" '+searchAttrName+'="'+escape(nameVal)+'" class="pub-select-item '+(selectFlag==true?_opts.addItemCheckStyle+' ' :'')+styleClass+'" title="'+nameVal.replace(/["']/g,'')+'">'+renderTemplate+'</li>';
		}else{

			if($.isFunction(_opts.target.render)){
				renderTemplate += _opts.target.render.call(sourceItem,{text : tmpItem[txtKey] , item : tmpItem});
			}else{
				renderTemplate += tmpItem[txtKey];
			}

			if(_opts.body.enableItemEvtBtn){
				renderTemplate += '<button type="button" class="pub-multiselect-btn" data-mode="item-del">'+this.options.i18.remove+'</button>';
			}
			
			return '<li data-pageno="'+(tmpItem[_opts.pageInfo.pageNumKey]||_this.config.currPage)+'" data-val="'+seletVal+'" '+searchAttrName+'="'+escape(nameVal)+'" class="pub-select-item" title="'+nameVal.replace(/["']/g,'')+'">'+renderTemplate+'</li>';
		}
	}
	/**
	 * @method lSearch
	 * @param type {String} 검색할 문자열
	 * @description 왼쪽 아이템 조회.
	 */
	,lSearch : function (val){
		var _this = this
			,_opts = _this.options;

		var tmpVal = val;
		
		var len = _opts.source.items.length
			,valKey = _opts.source.idKey
			,txtKey = _opts.source.nameKey
			,searchAttrKey = (_opts.source.searchAttrKey == '' ? txtKey : _opts.source.searchAttrKey)
			,tmpItem;
			
		if( len> 0){
			_this.element.source.find(_opts.itemSelector).removeClass(_opts.selectStyleClass);
			for(var i=0 ;i < len; i++){
				tmpItem = _opts.source.items[i];

				if(tmpVal=='' || tmpItem[searchAttrKey].indexOf(tmpVal) > -1){
					$('#'+_this.prefix+' [data-type="source"]>[data-val='+tmpItem[valKey]+']').show();
				}else{
					$('#'+_this.prefix+' [data-type="source"]>[data-val='+tmpItem[valKey]+']').hide();
				}
			}
			//_this.element.source.empty().html(strHtm.join(''));
		}
	}
	,destroy:function (){
		//$(document).off('contextmenu.pubcontext', this.element).off('click', '.context-event');
	}
	/**
	 * @method getHilightTemplateData:
	 * @param re {RegExp} hilight 할 정규식
	 * @param item {Object} item
	 * @param hilightTemplate {String} hilight template
	 * @description hilight template 정보 얻기.
	 */

};

$[ pluginName ] = function (selector,options) {

	if(!selector){
		return ;
	}

	var _cacheObject = _datastore[selector];

	if(typeof options === 'undefined'){
		return _cacheObject||{};
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

$[ pluginName ].setDefaults = function (defaultValue){
	_defaults = objectMerge(_defaults, defaultValue);
}

})(jQuery, window, document);