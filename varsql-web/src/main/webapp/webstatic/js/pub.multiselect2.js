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
	,itemSelector :'.pub-select-item'	// select item selector
	,enableAddItemCheck : true			// 추가된 아이템 표시 여부.
	,selectStyleClass : 'selected'	// select item class
	,items :[]			// item
	,valueKey : 'code' 	// value key
	,labelKey : 'name' //  label key
	,render: function (item){	// 아이템 추가될 템플릿.
		return item.text
	}
	,source : {	// source item
		label : ''	// headerlabel
		,labelAlign : 'center'
		,emptyMessage:''	// message
		,items: []			// item
		,click : false	// 클릭시 이벤트
		,search :{
			enable : false
			,enableKeyPress : false 	// keypress event 활성화 여부
			,callback : function (searchWord, evtType){ // enter ,search button click callback 
				//console.log(searchWord)		
			}
		}
		,beforeMove : false	// 이동전  이벤트
		,afterMove : false	//  이동후  이벤트
		,completeMove : false	// 이동 완료  이벤트
	}
	,target : {
		label : ''	// header label
		,labelAlign : 'center'
		,items: []			// item
		,emptyMessage:'' 	// message
		,click : false	// 클릭시 이벤트
		,dblclick : false
		,search :{
			enable : false
			,enableKeyPress : false 	// keypress event 활성화 여부
			,callback : function (searchWord, evtType){ // enter ,search button click callback 
				//console.log(searchWord)		
			}
		}
		,beforeMove : false	// 이동전  이벤트
		,afterMove : false	//  이동후  이벤트
		,completeMove : false	// 이동 완료  이벤트
		,paging :{	// 다중으로 관리할경우 처리.
			unitPage : 1		// max page 값
			,currPage : 1	// 현재 값
			,pageNumKey : 'pageNo'	// page number key 
			,enableMultiple : true 	// 페이징 처리를 item 내부의 pageNo 값으로 처리.
		}
	}
	,message : { // 방향키 있을때 메시지
		addEmpty : false
		,delEmpty : false
		,duplicate :false
	}
	,footer : {
		enable : false
	}
	,i18 : {
		helpMessage : '* 목록을 마우스로 드래그앤 드롭하거나 더블클릭 하세요.'
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

function objectValues (obj){
	if(obj){
		if (Object.values){
			return Object.values(obj);
		} else{
			return Object.keys(obj).map(function(key) {
				return obj[key];
			})
		}
	};

	return [];
}



function Plugin(selector, options) {

	this.selector = selector;
	this.prefix = 'pubMultiselect'+getHashCode(selector);

	if( options.source.paging !== false){
		options.source.paging = objectMerge({unitPage : 10, currPage : 1}, options.source.paging);
	}

	if( options.target.paging !== false){
		options.target.paging = objectMerge({unitPage : 10, currPage : 1}, options.target.paging);
	}

	this.options = objectMerge({}, _defaults, options);

	this.config ={
		currPage : this.options.target.paging.currPage
		, itemKey :{
			sourceIdx :{}
		}
		, focus : false
		, focusType : ''
		, addCheckStyle : (this.options.enableAddItemCheck ===true ? 'text-selected' :'')
		, itemList : {'1':{}}
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

		// 검색
		this.element.container.on('click.search', '.search-button', function (e){
			var sEle = $(this);
			var labelWrapperEle = sEle.closest('[data-mode]'); 
			var mode = labelWrapperEle.attr('data-mode');

			if(mode=='source'){
				_this.options.source.search.callback.call(sEle, labelWrapperEle.find('.input-text').val(), e);
			}else{
				_this.options.target.search.callback.call(sEle, labelWrapperEle.find('.input-text').val(), e);
			}
		});

		// Page navigation click event
		this.element.container.on('click.pagigng.num', '.pub-multiselect-paging .page-num', function (e){
			var sEle = $(this);
			var pageno = sEle.attr('pageno');
			var modeArea = sEle.closest('[data-mode]'); 
			var mode = modeArea.attr('data-mode');

			var param = {no : pageno, searchword : modeArea.find('.input-text').val()||'' ,evt :e};

			if(mode=='source'){
				_this.options.source.paging.callback.call(sEle, param);
			}else{
				if(_this.options.target.paging.enableMultiple === true){
					_this.config.currPage = pageno;
					_this.options.target.paging.currPage = pageno; 
					_this.setTargetItem(objectValues(_this.config.itemList[pageno])||[]);
				}else{
					_this.options.target.paging.callback.call(sEle, param);
				}				
			}
		});

		// search box keyup event
		this.element.container.on('keyup.search', '.input-text', function (e){
			var sEle = $(this);
			var labelWrapperEle = sEle.closest('[data-mode]'); 
			var mode = labelWrapperEle.attr('data-mode');

			var inputText = sEle.val(); 

			if(mode=='source'){
				if(_this.options.source.search.enableKeyPress === true){
					_this.options.source.search.callback.call(sEle, inputText, e);
				}else if(e.keyCode === 13){
					_this.options.source.search.callback.call(sEle, inputText, e);
				}
			}else{
				if(_this.options.target.search.enableKeyPress === true){
					_this.options.target.search.callback.call(sEle, inputText, e);
				}else if(e.keyCode === 13){
					_this.options.target.search.callback.call(sEle, inputText, e);
				}
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
	 * @param items {Array} items array
	 * @description item 그리기.
	 */
	,setSourceItem : function (items, pagingInfo){
		var _this = this
			sourceOpt = _this.options.source
						
		var len = items.length;
		
		if(len > 0){
			var strHtm = [];
			var valKey = _this.options.valueKey;
			var pageMaxVal = _this.options.target.paging.unitPage;
			for(var i=0 ;i < len; i++){
				var tmpItem = items[i];
				var itemValue = tmpItem[valKey];
				var itemCheckFlag = false;
				for(var j = 1 ;j <=pageMaxVal; j++){
					if(_this.config.itemList[j] && typeof _this.config.itemList[j][itemValue] !=='undefined') {
						itemCheckFlag = true;
						break; 
					}
				}

				strHtm.push(_this.getItemHtml('source', itemValue, tmpItem, itemCheckFlag));
				_this.config.itemKey.sourceIdx[itemValue] = tmpItem;
			}

			_this.element.source.empty().html(strHtm.join(''));
		}else{
			_this.element.source.empty().html(_this.getEmptyMessage(sourceOpt.emptyMessage));
		}
		
		_$pagingUtil.setPaging(_this, 'source',pagingInfo || sourceOpt.paging);
		
	}
	/**
	 * @method setTargetItem
	 * @param items {Array} items array
	 * @description item 그리기.
	 */
	,setTargetItem : function (items, pagingInfo){
		var _this = this
			,_opts = _this.options
			,tmpItem;

		var targetOpt= _opts.target;

		var currPageNo = _this.config.currPage; 

		var len = items.length;
		
		this.config.itemList[currPageNo] ={};

		if(len > 0){
			var valKey = _opts.valueKey;
			var pageNumKey = targetOpt.paging.pageNumKey;
			var maxPageNo = targetOpt.paging.unitPage; 
			

			var strHtm = [];
			for(var i=0 ;i < len; i++){
				tmpItem = items[i];

				var itemValue = tmpItem[valKey];
				var pageNo = tmpItem[pageNumKey] || currPageNo; 

				if(pageNo > maxPageNo){
					continue; 
				}

				if(typeof _this.config.itemList[pageNo] ==='undefined'){
					_this.config.itemList[pageNo] = {}
				}

				if(typeof _this.config.itemList[pageNo][itemValue] !=='undefined'){
					continue; 
				}

				tmpItem['_CU'] = 'U';
				
				_this.config.itemList[pageNo][itemValue] = tmpItem;

				if(currPageNo == pageNo){
					strHtm.push(_this.getItemHtml('target', itemValue, tmpItem));
				}
				
				this.element.source.find(_opts.itemSelector+'[data-val="'+itemValue +'"]').addClass(_this.config.addCheckStyle+ ' target-check');
			}

			this.element.source.find(_opts.itemSelector).each(function (){
				var sEle = $(this);
				if(!sEle.hasClass('target-check')){
					sEle.removeClass(_this.config.addCheckStyle);
				}
	
				sEle.removeClass('target-check');
			})

			if(strHtm.length > 0){
				_this.element.target.empty().html(strHtm.join(''));
			}else{
				_this.element.target.empty().html(_this.getEmptyMessage(targetOpt.emptyMessage));
			}
		}else{
			_this.element.target.empty().html(_this.getEmptyMessage(targetOpt.emptyMessage));
			_this.element.source.find(_opts.itemSelector).removeClass(_this.config.addCheckStyle);
		}

		_$pagingUtil.setPaging(_this, 'target', pagingInfo || targetOpt.paging);
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

				if(_this.options.target.dblclick.call($(this),e, _this.config.itemList[_this.config.currPage][_this.getItemVal($(this))]) ===false){
					return false;
				};
			}
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
				selectItem.click.call(sEle , e, _this.config.itemList[_this.config.currPage][_this.getItemVal(sEle)]);
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
				return _this.config.itemList[_this.config.currPage][itemKey];
			}else{
				return _this.config.itemList[pageNum][itemKey];
			}
		}else{
			var reInfo =[];

			for(var no in _this.config.itemList){

				var pageItems=_this.config.itemList[no];

				for(var key in pageItems){
					var addItem = pageItems[key]
					addItem['_pageNum']  = no; 
					reInfo.push(addItem);
				}
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
		var tmpItem = this.config.itemList[this.config.currPage][itemKey];
		if(typeof tmpItem !=='undefined'){
			tmpItem['_CU'] = 'CU';
			this.getElement(itemKey).replaceWith(this.getItemHtml('target', itemKey, tmpItem));
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

				var addChkFlag = typeof _this.config.itemList[_this.config.currPage][tmpVal] ==='undefined';
				if(dupChkFlag && addChkFlag){
					dupChkFlag = false;
				}

				if(!addChkFlag) continue;

				if($.isFunction(opts.source.beforeMove)){
					if(opts.source.beforeMove(tmpObj) === false){
						continue;
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

				strHtm.push(_this.getItemHtml('target', tmpVal ,selectItem));

				addElements.push(tmpObj);

				if($.isFunction(opts.source.afterMove)){
					opts.source.afterMove(tmpObj);
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

			if($.isFunction(opts.source.completeMove)){
				if(opts.source.completeMove(addItemKey)===false) return false;
			}

			_this.element.target.find('.empty-message').remove();

			for(var i =0; i <addElements.length; i++){
				addElements[i].addClass(_this.config.addCheckStyle);
			}

			for(var key in addItemMap){
				_this.config.itemList[_this.config.currPage][key] =addItemMap[key];
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
		 var opts = _this.options;

		opt = opt||{};

		var selectVal = opt.items || _this.getSelectElement(_this.element.source);

		var selectVal = _this.getSelectElement(_this.element.target);

		if(selectVal.length >0){
			var removeItem;
			var deleteItemKey = [];

			var removeItems = [];

			for(var i =0; i <selectVal.length; i++){
				var item = selectVal[i];

				var tmpKey = $(item).attr('data-val');
				removeItem = _this.config.itemKey.sourceIdx[tmpKey];

				if($.isFunction(opts.target.beforeMove)){
					if(opts.target.beforeMove($(item))===false) {
						continue;
					};
				}

				var removeFlag = false;

				for(var tmpPageNo in _this.config.itemList){
					if(_this.config.currPage != tmpPageNo){
						if(typeof _this.config.itemList[tmpPageNo][tmpKey] !=='undefined'){
							removeFlag = true ;
							break;
						}
					}
				}
				if(removeItem){
					if(removeFlag !== true){
						_this.element.source.find(opts.itemSelector+'[data-val="'+tmpKey+'"]').removeClass(_this.config.addCheckStyle);
					}
				}
				
				removeItems.push($(item))
				deleteItemKey.push(tmpKey);

				if($.isFunction(opts.target.afterMove)){
					opts.target.afterMove(removeItem);
				}
			}

			var returnRemoveFlag = true; 
			if($.isFunction(opts.target.completeMove)){
				returnRemoveFlag = opts.target.completeMove(deleteItemKey);
			}
			
			if(returnRemoveFlag !== false){
				for(var i =0; i <removeItems.length; i++){
					removeItems[i].remove();
					delete this.config.itemList[this.config.currPage][deleteItemKey[i]];
				}

				if(Object.keys(_this.config.itemList[_this.config.currPage]).length < 1){
					_this.element.target.empty().html(_this.getEmptyMessage(opts.target.emptyMessage));
				}
			}

		}else{
			if(opts.message.delEmpty !== false){
				alert(opts.message.delEmpty);
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

		var sourceContainerEl = this.element.container.find('[data-mode="source"]');

		var labelH = 0;
		if(sourceContainerEl.find('.pub-multiselect-label').length > 0){
			labelH = sourceContainerEl.find('.pub-multiselect-label').outerHeight();
			labelH = labelH < 36 ? 36 : 0;
		}
		sourceContainerEl.find('.pub-multiselect-area').css('height' , 'calc(100% - '+labelH+'px)');

		var targetContainerEl = this.element.container.find('[data-mode="target"]');

		labelH = 0; 
		if(targetContainerEl.find('.pub-multiselect-label').length > 0){
			labelH = targetContainerEl.find('.pub-multiselect-label').outerHeight();
			labelH = labelH < 36 ? 36 : 0;
		}
		targetContainerEl.find('.pub-multiselect-area').css('height' , 'calc(100% - '+labelH+'px)');

		
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
			
		var bodyHeight = height -(opts.footer.enable ? 30 : 0);		
		bodyHeight = bodyHeight - (enableMoveBtn ? moveBtnSize : 0);
		
		var labelHalfHeight =(opts.header.enableSourceLabel ===false) ? 36/2 : 0;
		strHtm.push('<div id="'+this.prefix+'" style="width:'+width+';" class="pub-multiselect">');
		strHtm.push('	<div class="pub-multiselect-body vertical '+(opts.body.enableItemEvtBtn ?'show-row-item-btn' : '' )+'">'); // body start

		if(opts.mode !='single'){
			strHtm.push('  <div style="height:'+(bodyHeight/2 - labelHalfHeight)+'px;" data-mode="source">');
			
			if(opts.header.enableSourceLabel===true){
				strHtm.push(this.getLabelHtml('source'));
			}

			strHtm.push('	<div class="pub-multiselect-area '+(opts.header.enableSourceLabel?'':'header-hide')+'"><ul class="pub-multiselect-items" data-type="source"></ul></div>');
			strHtm.push(' </div>');
			
			if(opts.target.paging !== false){
				strHtm.push(' <div id="'+this.prefix+'source_paging" class="pub-multiselect-paging"></div>');
			}
			
			if(enableMoveBtn){
				strHtm.push('<div class="pub-multiselect-move-btn" style="height:'+moveBtnSize+'px;line-height:'+moveBtnSize+'px;">');
				strHtm.push('	<button type="button" style="margin-bottom:5px;" class="pub-multiselect-btn" data-mode="add" title="'+this.options.i18.add+'">'+this.options.i18.add+'</button>');
				strHtm.push('	<button type="button" class="pub-multiselect-btn" data-mode="del" title="'+this.options.i18.remove+'">'+this.options.i18.remove+'</button>');
				strHtm.push('</div>');
			}	
		}
		
		strHtm.push(' <div style="height:'+(bodyHeight/2+labelHalfHeight)+'px;" data-mode="target">');

		if(opts.header.enableTargetLabel===true){
			strHtm.push(this.getLabelHtml('target'));
		}
		
		strHtm.push('  <div class="pub-multiselect-area '+(opts.header.enableTargetLabel?'':'header-hide')+'"><ul class="pub-multiselect-items" data-type="target"></ul></div>');
		strHtm.push(' </div>');

		// 페이지 정보

		if(opts.target.paging !== false){
			strHtm.push('					<div id="'+this.prefix+'target_paging" class="pub-multiselect-paging"></div>');
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
			
		var bodyHeight = height -(opts.footer.enable ? 30 : 0) - 2; // border 2 
							
		var enableMoveBtn = opts.body.enableMoveBtn === false ? false :true;

		strHtm.push('<div id="'+this.prefix+'" style="width:'+width+';" class="pub-multiselect">');
		strHtm.push('	<div class="pub-multiselect-body horizontal '+(opts.body.enableItemEvtBtn ?'show-row-item-btn' : '' )+'">');
		strHtm.push('		<table>');
		strHtm.push('		<colgroup>');
		if(opts.mode =='single'){
			strHtm.push('<col style="width:100%">');
		}else{
			var moveBtnSize = 10;
			if(enableMoveBtn){
				moveBtnSize = isNaN(opts.body.moveBtnSize) ? _defaults.body.moveBtnSize :opts.body.moveBtnSize;
			}

			strHtm.push('<col style="width:calc(50% - '+(moveBtnSize/2)+'px)">');
			strHtm.push('<col style="width:'+moveBtnSize+'px">');
			strHtm.push('<col style="width:calc(50% - '+(moveBtnSize/2)+'px)">');	
		}

		strHtm.push('		</colgroup>');					
		strHtm.push('			<tbody>');
		strHtm.push('				<tr>');
		if(opts.mode !='single'){
			strHtm.push('					<td');
			strHtm.push('					 <div data-mode="source">');
			strHtm.push('					   <div style="height:'+bodyHeight+'px;">');
			
			if(opts.header.enableSourceLabel===true){
				strHtm.push(this.getLabelHtml('source'));
			}

			strHtm.push('						 <div class="pub-multiselect-area"><ul class="pub-multiselect-items" data-type="source"></ul></div>');

			strHtm.push('					   </div>');

			if(opts.target.paging !== false){
				strHtm.push('					<div id="'+this.prefix+'source_paging" class="pub-multiselect-paging"></div>');
			}
			strHtm.push('					 </div>');
			strHtm.push('					</td>');

			strHtm.push('					<td class="pub-multiselect-move-btn">');

			if(enableMoveBtn){
				strHtm.push('						<button type="button" style="margin-bottom:5px;" class="pub-multiselect-btn" data-mode="add" title="'+this.options.i18.add+'"></button><br/>');
				strHtm.push('						<button type="button" class="pub-multiselect-btn" data-mode="del" title="'+this.options.i18.remove+'"></button>');
			}
			
			strHtm.push('					</td>');
		}
		
		strHtm.push('					<td');
		strHtm.push('					 <div data-mode="target">');
		strHtm.push('					  <div style="height:'+bodyHeight+'px;">');

		if(opts.header.enableTargetLabel===true){
			strHtm.push(this.getLabelHtml('target'));
		}
		
		strHtm.push('						<div class="pub-multiselect-area"><ul class="pub-multiselect-items" data-type="target"></ul></div>');
		strHtm.push('					  </div>');

		if(opts.target.paging !== false){
			strHtm.push('					<div id="'+this.prefix+'target_paging" class="pub-multiselect-paging"></div>');
		}

		strHtm.push('					 </div>');
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
		return '<li class="empty-message">'+msg+'</li>';
	}

	,getLabelHtml : function (mode){
		
		var labelOpt = this.options.source;
		if(mode =='target'){
			labelOpt = this.options.target;
		}

		var strHtm =[];
		strHtm.push('<div class="pub-multiselect-label al-'+labelOpt.labelAlign+'">');

		if(labelOpt.search && labelOpt.search.enable === true){
			if(labelOpt.label != ''){
				strHtm.push('<span class="label-text">'+labelOpt.label+'</span>');
			}

			strHtm.push('<input type="text" class="input-text">');
			strHtm.push('<span class="search-button"><button type="button">Search</button></span>');
		}else{
			strHtm.push('<span class="label-text" style="width:100%;display:block;">'+labelOpt.label+'</span>');
		}

		strHtm.push('</div>');

		return strHtm.join('');
	}
	/**
	 * @method getItemHtml
	 * @param type {String} source , target
	 * @param seletVal {String} source , target
	 * @param tmpItem {Object} select item
	 * @description 선택된 html 얻기.
	 */
	,getItemHtml: function (type, seletVal, tmpItem, selectFlag){
		var _this = this
			, _opts = _this.options
			,labelKey = _opts.labelKey;

		var templateItem = type=='source'? _opts.source : _opts.target; 

		var renderTemplate = '';
		if($.isFunction(_opts.render)){
			renderTemplate = _opts.render.call(templateItem,{text : tmpItem[labelKey] , item : tmpItem}, 'source');
		}else{
			renderTemplate = tmpItem[labelKey];
		}
		renderTemplate = '<span>'+renderTemplate + '</span>';
		
		var itemText = $(renderTemplate).text();

		if(type=='source'){
			
			var styleClass = '';

			if(selectFlag === true || _this.config.itemList[_this.config.currPage][seletVal]){
				styleClass += ' '+_this.config.addCheckStyle;
			}

			if(_opts.body.enableItemEvtBtn){
				renderTemplate += '<button type="button" class="pub-multiselect-btn" data-mode="item-add">'+this.options.i18.add+'</button>';
			}

			return '<li data-val="'+seletVal+'" class="pub-select-item '+styleClass+'" title="'+itemText.replace(/["']/g,'')+'">'+renderTemplate+'</li>';
		}else{
			
			if(_opts.body.enableItemEvtBtn){
				renderTemplate += '<button type="button" class="pub-multiselect-btn" data-mode="item-del">'+this.options.i18.remove+'</button>';
			}
			
			return '<li data-pageno="'+(tmpItem[_opts.target.paging.pageNumKey]||_this.config.currPage)+'" data-val="'+seletVal+'" class="pub-select-item" title="'+itemText.replace(/["']/g,'')+'">'+renderTemplate+'</li>';
		}
	}
	,destroy:function (){
		$(document).off('keydown.'+this.prefix).off('mousedown.'+this.prefix);
	}
};

var _$pagingUtil = {
	setPaging : function (_ctx, type,_paging){
			
		_paging = _paging||{};

		var pagingInfo = this.getPagingInfo(_paging.totalCount||0, _paging.currPage, _paging.countPerPage, _paging.unitPage);

		if(pagingInfo.totalCount <1) {
			$('#'+_ctx.prefix+type+'_paging').empty();
			return ; 
		}
	
		_ctx.config.pageNo = pagingInfo.currPage;
		_ctx.config.pagingInfo = pagingInfo;
	
		var currP = pagingInfo.currPage;
		if (currP == "0") currP = 1;
		var preP_is = pagingInfo.prePage_is;
		var currS = pagingInfo.currStartPage;
		var currE = pagingInfo.currEndPage;
		if (currE == "0") currE = 1;
		var nextO = 1 * currP + 1;
		var preO = currP - 1;
		var strHTML = [];
		strHTML.push('<ul>');
		
		if (currP <= 1) {
			strHTML.push(' <li class="disabled page-icon"><a href="javascript:">&laquo;</a></li>');
		} else {
			strHTML.push(' <li><a href="javascript:" class="page-num page-icon" pageno="'+preO+'">&laquo;</a></li>');
		}
		
		if(preP_is && (currE - pagingInfo.unitPage >= 0)){
			strHTML.push(' <li class="page-num" pageno="1"><a href="javascript:" >1...</a></li>');
		}
		
		var no = 0;
		for (no = currS * 1; no <= currE * 1; no++) {
			if (no == currP) {
				strHTML.push(' <li class="active"><a href="javascript:">'+ no + '</a></li>');
			} else {
				strHTML.push(' <li class="page-num" pageno="'+no+'"><a href="javascript:" >'+ no + '</a></li>');
			}
		}
	
		if(currS + pagingInfo.unitPage < pagingInfo.totalPage){
			strHTML.push(' <li class="page-num" pageno="'+pagingInfo.totalPage+'"><a href="javascript:" >...'+pagingInfo.totalPage+'</a></li>');
		}
			
		if (currP == currE) {
			strHTML.push(' <li class="disabled"><a href="javascript:">&raquo;</a></li>');
		} else {
			strHTML.push(' <li><a href="javascript:" class="page-num page-icon" pageno="'+nextO+'">&raquo;</a></li>');
		}
		
		strHTML.push('</ul>');
	
		var pageNaviEle = $('#'+_ctx.prefix+type+'_paging');
		pageNaviEle.empty().html(strHTML.join(''));
	
		return this;
	}

	/**
	 * @method getPagingInfo
	 * @param  totalCount {int} 총카운트
	 * @param  currPage {int} 현재 페이지
	 * @param  countPerPage {int} 한페이지에 나올 row수
	 * @param  unitPage {int} 한페이지에 나올 페이번호 갯수
	 * @description 페이징 하기.
	 */
	 ,getPagingInfo : function (totalCount, currPage, countPerPage, unitPage) {
		countPerPage = countPerPage || 10;
		unitPage = unitPage || 10;

		if (totalCount < 1) {
			return {
				'currPage' :0 ,'unitPage' : 0
				,'totalCount' : 0 ,'totalPage' : 0
			}
		} 
		
		if (totalCount < countPerPage) {
			countPerPage = totalCount;
		}
		
		var totalPage = (totalCount % countPerPage == 0) ? (totalCount/countPerPage) : (Math.floor(totalCount/countPerPage) + 1)

		if (totalPage < currPage){
			currPage = totalPage;
		}
		
		var currEndCount = countPerPage;
		
		if (currPage != 1) {
			currEndCount = currPage * countPerPage;
		}

		if (currEndCount > totalCount){
			currEndCount = totalCount;
		}
		
		var currStartPage;
		var currEndPage;

		if (totalPage <= unitPage) {
			currEndPage = totalPage;
			currStartPage = 1;
		} else {
			var halfUnitPage = unitPage;

			if(currPage == unitPage || (currPage > unitPage && (totalPage - (currPage-1) >= unitPage)) ){
				halfUnitPage = Math.floor(unitPage /2); 
			}

			if(currPage <= halfUnitPage){
				currEndPage = unitPage;
				currStartPage = 1; 
			}else if(currPage+halfUnitPage < totalPage){
				currEndPage = currPage + halfUnitPage;
				currStartPage = currEndPage - unitPage + 1;
			}else{
				currEndPage = (currPage + halfUnitPage);

				if(currEndPage > totalPage){
					currEndPage =totalPage;
				}
				currStartPage = currEndPage - unitPage + 1;
			}
		}

		if (currEndPage > totalPage)
			currEndPage = totalPage;

		var prePage=0;
		var prePage_is=false;
		if (currStartPage != 1) {
			prePage_is = true;
			prePage = currStartPage - 1;
		}

		var nextPage=0;
		var nextPage_is =false;
		if (currEndPage != totalPage) {
			nextPage_is = true;
			nextPage = currEndPage + 1;
		}

		return  {
			'currPage' :currPage ,'unitPage' : unitPage
			,'prePage' : prePage ,'prePage_is' : prePage_is
			,'nextPage' : nextPage,'nextPage_is' : nextPage_is
			,'currStartPage' : currStartPage ,'currEndPage' : currEndPage
			,'countPerPage' : countPerPage, 'totalCount' : totalCount ,'totalPage' : totalPage
		};
	}
	
}

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