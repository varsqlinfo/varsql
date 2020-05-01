/**
 * pubMultiselect v0.0.1
 * ========================================================================
 * Copyright 2016 ytkim
 * Licensed under MIT
 * http://www.opensource.org/licenses/mit-license.php
 * url : https://github.com/ytechinfo/pub
 * demo : http://pub.moaview.com/
*/
;(function ($, window, document, undefined) {
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
,defaults = {
	targetSelector : false	//	타켓 selector
	,maxSize : -1	// 추가 가능한 max size
	,maxSizeMsg : false	// 추가 가능한 max size가 넘었을경우 메시지
	,useMultiSelect : false	// ctrl , shift key 이용해서 다중 선택하기 여부
	,containment : ''
	,useDragMove : true	// drag해서 이동할지 여부.
	,useDragSort : true // target drag 해서 정렬할지 여부.
	,addPosition : 'last'	// 추가 되는 방향키로 추가시 어디를 추가할지. ex(source, last)
	,duplicateCheck : true	// 중복 추가 여부.
	,pageInfo :{	// 다중으로 관리할경우 처리.
		max : 1
		,currPage : 1
		,selector : '#page_area'
		,pageNumKey : 'pageNo'
		,emptyMessage : '더블클릭해서 추가하세요'
	}
	,itemSelector :'.pub-select-item'
	,addItemClass :'selected'	// add item class
	,selectClass : 'selected'
	,handleClass : 'pub-handle'	// item 선택 클래스.
	,items :[]
	,sourceItem : {
		optVal : 'CODE_ID'
		,optTxt : 'CODE_NM'
		,useHtmlData : false // html element 를 직접사용할경우.
		,searchAttrName : '_name'
		,searchAttrKey : ''
		,emptyMessage:''
		,items: []
		,click : false	// 클릭시 이벤트
		,render: function (item){	// 아이템 추가될 템플릿.
			return '<span>'+item.text+'</span>'
		}
	}
	,targetItem : {
		optVal : 'CODE_ID'
		,optTxt : 'CODE_NM'
		,useHtmlData : false // html element 를 직접사용할경우.
		,items: []
		,emptyMessage:''
		,click : false
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
	,beforeMove : false
	,beforeItemMove : false
	,afterSourceMove : false
	,compleateSourceMove : false
	,beforeTargetMove : false
	,afterTargetMove : false
	,compleateTargetMove : false
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

function Plugin(sourceSelector, options) {

	this.elePrefix = pluginName+'-'+new Date().getTime();

	this.options = objectMerge({}, defaults, options);

	this.sourceSelector = (typeof sourceSelector=='object') ? sourceSelector.selector : sourceSelector;
	this.targetSelector = (typeof options.targetSelector=='object') ? options.targetSelector.selector : options.targetSelector;

	this.sourceElement = $(this.sourceSelector);
	this.targetElement = $(this.targetSelector);

	this.config ={
		currPage : this.options.pageInfo.currPage
		,pageNumInfo:{}
		,itemKey :{
			sourceIdx :{}
		}
	};

	this.init();

	return sourceSelector;
}

Plugin.prototype ={
	/**
	 * @method init
	 * @description 자동완성 초기화
	 */
	init :function(){
		var _this = this;

		_this.addItemList ={'1':{}};
		_this._initPageInfo();
		_this._initItem();
		_this.initEvt();
	}
	/**
	 * @method initEvt
	 * @description 이벤트 초기화
	 */
	,initEvt : function (){
		var _this = this;

		_this.initSourceEvt();
		_this.initTargetEvt();

	}/**
	 * @method _initPageInfo
	 * @description 페이지 정보 초기화
	 */
	,_initPageInfo : function (){
		var _this =this
			,opts = _this.options;

		if(opts.pageInfo.max > 1){
			var pageHtm = [];

			for(var i = 1 ;i <=opts.pageInfo.max; i++){
				_this.config.pageNumInfo[i+''] =[];
				_this.addItemList[i+''] ={};
				pageHtm.push('<a href="javascript:;" class="page-num" data-page="'+i+'"><span class="page-no-text">'+i+'</span></a>');
			}

			$(opts.pageInfo.selector).addClass(_this.elePrefix).empty().html(pageHtm.join(''));

			$('.'+_this.elePrefix+' .page-num').on('click',function (e){
				var currEle = $(this);
				var beforeEle = $('.'+_this.elePrefix+' .page-num.selected');

				if(beforeEle.length > 0){
					_this.config.pageNumInfo[beforeEle.attr('data-page')] =[];
					_this.config.pageNumInfo[beforeEle.attr('data-page')].push(selectObj.targetElement.clone().html());

					if(currEle.hasClass('selected')){
						return false;
					}
				}

				beforeEle.removeClass('selected');
				currEle.addClass('selected');

				var currPageNo = currEle.attr('data-page');

				_this.config.currPage = currPageNo;

				if(opts.pageInfo.emptyMessage !== false && _this.config.pageNumInfo[currPageNo].length < 1){
					selectObj.targetElement.empty().html(_this.getEmptyMessage());
				}else{
					selectObj.targetElement.empty().html(_this.config.pageNumInfo[currPageNo].join(''));
				}
			})
			$('.'+_this.elePrefix+' .page-num[data-page="'+_this.config.currPage+'"]').addClass('selected');
		}
	}
	/**
	 * @method _initItem
	 * @description selectbox 정보 초기화
	 */
	,_initItem : function (){
		var _this = this

		_this.setItem('target',_this.options.targetItem.items);
		_this.setItem('source',_this.options.sourceItem.items);
	}
	/**
	 * @method setItem
	 * @param type {String} selectbox 타입(source or target)
	 * @param items {Array} items array
	 * @description item 그리기.
	 */
	,setItem :  function (type , items){
		var _this = this
			,_opts = _this.options
			,tmpSourceItem = _opts.sourceItem
			,strHtm = []
			,tmpItem;

		var len ,valKey ,txtKey
			,searchAttrName = tmpSourceItem.searchAttrName
			,searchAttrKey = tmpSourceItem.searchAttrKey == '' ? txtKey : tmpSourceItem.searchAttrKey;

		if(type=='source'){
			valKey = tmpSourceItem.optVal;
			txtKey = tmpSourceItem.optTxt;

			if(tmpSourceItem.useHtmlData===true){
				tmpSourceItem.items=[];
				_this.sourceElement.find(_opts.itemSelector).each(function (i ,item){
					var sObj = $(this);
					var addItem = {};

					addItem[valKey] = sObj.attr('data-val');
					addItem[txtKey] = sObj.attr('data-text')||sObj.text();
					addItem[searchAttrName] = sObj.attr(searchAttrName);

					if(_this.addItemList[_this.config.currPage][addItem[valKey]]){
						sObj.addClass(_opts.addItemClass);
					}

					tmpSourceItem.items.push(addItem);
					_this.config.itemKey.sourceIdx[addItem[valKey]] = i;
				});

				items = tmpSourceItem.items;
			}

			tmpSourceItem.items = items;
			len = tmpSourceItem.items.length;
			var pageMaxVal = _opts.pageInfo.max;

			if(len > 0){
				for(var i=0 ;i < len; i++){
					tmpItem = tmpSourceItem.items[i];
					var tmpSelctOptVal = tmpItem[valKey];
					var selectFlag = false;
					for(var j = 1 ;j <=pageMaxVal; j++){
						if(typeof _this.addItemList[j][tmpSelctOptVal] !=='undefined') {
							selectFlag = true;
							continue;
						}
					}

					strHtm.push(_this.getItemHtml(type,tmpSelctOptVal, tmpItem , selectFlag));
					_this.config.itemKey.sourceIdx[tmpSelctOptVal] = i;
				}
			}else{
				strHtm.push(_this.getEmptyMessage(tmpSourceItem.emptyMessage));
			}
			_this.sourceElement.empty().html(strHtm.join(''));

			_this._setDragOpt();
		}else{
			var tmpTargetItem= _opts.targetItem;

			tmpTargetItem.items = items;
			len = tmpTargetItem.items.length;
			valKey = tmpTargetItem.optVal;
			txtKey = tmpTargetItem.optTxt;

			if(tmpTargetItem.useHtmlData===true){
				tmpTargetItem.items=[];
				var idx = 0;

				_this.targetElement.find(_opts.itemSelector).each(function (i ,item){
					var sObj = $(this);
					var addItem = {};

					addItem[valKey] = sObj.val();
					addItem[txtKey] = sObj.attr('data-text')||sObj.text();
					addItem[searchAttrName] = sObj.attr(searchAttrName);

					var _key = addItem[valKey];
					addItem['_CU'] = 'U';

					_this.addItemList[_this.config.currPage][_key]=addItem;
					tmpTargetItem.items.push(addItem);

					_this.addSourceItemSelecClass(_opts, addItem[valKey] );
					++idx;
				});

				_this.sourceItemSelectCheck(_opts);

				len = idx;

				items = tmpTargetItem.items;
			}

			tmpTargetItem.items = items;
			len = tmpTargetItem.items.length;
			valKey = tmpTargetItem.optVal;
			txtKey = tmpTargetItem.optTxt;

			var pageNumKey = _opts.pageInfo.pageNumKey;

			_this.addItemList[_this.config.currPage] ={};
			_this.config.pageNumInfo[_this.config.currPage] = [];

			if(len > 0){
				for(var i=0 ;i < len; i++){
					tmpItem = tmpTargetItem.items[i];

					var tmpSelctOptVal = tmpItem[valKey];

					if(typeof _this.config.pageNumInfo[tmpItem[pageNumKey]||_this.config.currPage] ==='undefined'){
						_this.config.pageNumInfo[tmpItem[pageNumKey]||_this.config.currPage] = [];
					}

					if(typeof _this.addItemList[tmpItem[pageNumKey]||_this.config.currPage] ==='undefined'){
						throw 'pageInfo undefined '+ pageNumKey+' :'+tmpItem[pageNumKey]+' ';
					}

					tmpItem['_CU'] = 'U';
					_this.addItemList[tmpItem[pageNumKey]||_this.config.currPage][tmpSelctOptVal] =tmpItem;

					_this.config.pageNumInfo[tmpItem[pageNumKey]||_this.config.currPage].push(_this.getItemHtml(type,tmpSelctOptVal, tmpItem))

					_this.addSourceItemSelecClass(_opts, tmpSelctOptVal);
				}

				_this.sourceItemSelectCheck(_opts);

				if(_this.config.pageNumInfo[_this.config.currPage].length > 0){
					_this.targetElement.empty().html(_this.config.pageNumInfo[_this.config.currPage].join(''));
				}else{
					_this.targetElement.empty().html(_this.getEmptyMessage());
				}
			}else{
				_this.targetElement.empty().html(_this.getEmptyMessage());
				_this.sourceElement.find(_opts.itemSelector).removeClass(_opts.addItemClass);
			}
		}
	}
	// sources select item add class
	,addSourceItemSelecClass : function (_opts, tmpSelctOptVal ){
		this.sourceElement.find(_opts.itemSelector+'[data-val="'+tmpSelctOptVal +'"]').addClass(_opts.addItemClass+ ' target-check');
	}
	// source 체크 된 item unselect
	,sourceItemSelectCheck: function (_opts){
		this.sourceElement.find(_opts.itemSelector).each(function (){
			var sEle = $(this);
			if(!sEle.hasClass('target-check')){
				sEle.removeClass(_opts.addItemClass);
			}

			sEle.removeClass('target-check')
		})
	}

	/**
	 * @method initSourceEvt
	 * @description source 소스 이벤트 초기화
	 */
	,initSourceEvt : function (){
		var _this = this
			,opts = _this.options;

		_this.sourceElement.addClass(_this.elePrefix+'source pub-multiselect-area');

		_this.sourceElement.on('click.pub-multiselect',opts.itemSelector, function (e){
			_this._setClickEvent(e, 'source' , $(this));
		})

		_this.sourceElement.on('dblclick.pub-multiselect',opts.itemSelector, function (e){
			_this.sourceMove();
			return ;
		})

		_this.sourceElement.on('selectstart',function(){ return false; });

		_this.targetElement.sortable({
			 scroll: true
			,containment : "parent"
			,cancel: ((opts.useDragSort !== false) ?'li:not(.'+opts.selectClass+')' :'li')
			,start:function(e,ui){

				try{
					var uiItem = $(ui.item);
					if(!uiItem.hasClass('ui-draggable')){
						var selectItem = _this.getSelectElement(_this.targetElement);

						if( $.inArray(opts.selectClass,e.currentTarget.classList) < 0 || selectItem.length < 1) {
							//return false;
						}
					}
				}catch(e){
					console.log(e);
				}
			}
			,update : function (e, ui){
				var uiItem = $(ui.item);
				if(uiItem.hasClass('pub-multi-add-helper-wrapper')){
					var addHtm = _this.sourceMove(true);

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
	,_setDragOpt : function (){
		var _this = this
			,opts = _this.options;

		if(opts.useDragMove !== false){

			_this.sourceElement.find(opts.itemSelector).draggable({
				appendTo: "body"
				,containment : (opts.containment|| 'parent')
				,scroll : false
				,connectToSortable: _this.targetSelector
				,classes: {
					"ui-draggable": opts.handleClass
				}
				,helper: function (event){
					var selectItem = _this.getSelectElement(_this.sourceElement);

					if(selectItem.length  < 1){
						return '<div></div>';
					}

					var strHtm = [];
					$.each(selectItem, function (i ,item ){
						strHtm.push('<div class="pub-multi-add-item">'+$(item).html()+'</div>')
					});

					return '<div class="pub-multi-add-helper-wrapper">'+strHtm.join('')+'</div>';
				}
				,start:function(e,info){
					var selectItem = _this.getSelectElement(_this.sourceElement);

					if( $.inArray(opts.selectClass,e.currentTarget.classList) < 0 || selectItem.length < 1) {
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

		_this.targetElement.addClass(_this.elePrefix+'target pub-multiselect-area');

		_this.targetElement.on('click',opts.itemSelector, function (e){
			_this._setClickEvent(e, 'target', $(this));
		})

		_this.targetElement.on('dblclick.pub-multiselect',opts.itemSelector, function (e){
			if($.isFunction(_this.options.targetItem.dblclick)){

				if(_this.options.targetItem.dblclick.call($(this),e, _this.addItemList[_this.config.currPage][_this.getItemVal($(this))]) ===false){
					return false;
				};
			}
			_this.targetElement.find(opts.itemSelector).removeClass(opts.addItemClass);
			$(this).addClass(opts.addItemClass);
			_this.targetMove();
		})
		//_this.targetElement.on('selectstart',function(){ return false; });
	}
	/**
	 * @method _setClickEvent
	 * @param e {Event} 이벤트
	 * @param selectType {String} source, target
	 * @param sEle {Element} 선택된 element
	 * @description target 소스 이벤트 초기화
	 */
	,_setClickEvent : function (e,selectType, sEle){
		var _this = this
			,opts = _this.options
			,evtElement
			,selectItem;

		if(selectType =='source'){
			evtElement = _this.sourceElement;
			selectItem = opts.sourceItem;
		}else{
			evtElement = _this.targetElement;
			selectItem = opts.targetItem;
		}

		var evt =window.event || e;

		var lastClickEle = evtElement.find(opts.itemSelector+'[data-last-click="Y"]');
		var onlyClickFlag = false;
		if(opts.useMultiSelect ===true){
			if (evt.shiftKey){
				var allItem = evtElement.find(opts.itemSelector);
				var beforeIdx = allItem.index(lastClickEle)
					,currIdx = allItem.index(sEle);

				var source = Math.min(beforeIdx, currIdx)
					,last = Math.max(beforeIdx, currIdx);

				evtElement.find(opts.itemSelector+'.'+ opts.selectClass).removeClass(opts.selectClass);

				for(var i=last; i >= source; i--){
					$(allItem[i]).addClass(opts.selectClass);
				}
			}else{
				lastClickEle.removeAttr('data-last-click');
				if(evt.ctrlKey){
					if(sEle.hasClass(opts.selectClass)){
						sEle.removeClass(opts.selectClass);
					}else{
						sEle.addClass(opts.selectClass);
					}
				}else{
					onlyClickFlag=true;
				}
			}
		}else{
			onlyClickFlag = true;
		}

		if(onlyClickFlag){
			if($.isFunction(selectItem.click)){
				selectItem.click.call(sEle , e, _this.addItemList[_this.config.currPage][_this.getItemVal(sEle)]);
			}
			evtElement.find(opts.itemSelector+'.'+ opts.selectClass).removeClass(opts.selectClass);
			sEle.addClass(opts.selectClass);
		}

		sEle.attr('data-last-click','Y');
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

			var currEle = $('.'+_this.elePrefix+' .page-num.selected');
			_this.config.pageNumInfo[currEle.attr('data-page')||_this.config.currPage] =[];
			_this.config.pageNumInfo[currEle.attr('data-page')||_this.config.currPage].push(_this.targetElement.clone().html());

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
		return 	evtElement ? evtElement.find(this.options.itemSelector+'.'+ this.options.selectClass) : this.targetElement.find(this.options.itemSelector+'.'+ this.options.selectClass);
	}
	,getElement : function (key){
		return 	this.targetElement.find('[data-val="'+key+'"]');
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
		var selectElement =_this.getSelectElement(_this.targetElement);
		var selectLen = selectElement.length;

		if(selectLen < 1) return ;

		var selectClass =this.options.selectClass;
		if(type=='up'){
			for(var i =0 ;i <selectLen ;i++){
				var currItem = $(selectElement[i])
					,prevItem = $(currItem.prev());

				if(!prevItem.hasClass(selectClass)) {
					currItem.after(prevItem);
				}
			}
		}else{
			for(var i =selectLen-1 ;i >=0 ;i--){
				var currItem = $(selectElement[i])
					,nextItem = $(currItem.next());

				if(!nextItem.hasClass(selectClass)) {
					currItem.before(nextItem);
				}
			}
		}
	}
	/**
	 * @method sourceMove
	 * @param type {Boolean} true or false
	 * @description source -> target 이동.
	 */
	,sourceMove : function (returnFlag){
		var _this = this
			,opts = _this.options;
		var selectVal =_this.getSelectElement(_this.sourceElement);

		if($.isFunction(opts.beforeMove)){
			if(opts.beforeMove('source') === false){
				return ;
			};
		}

		if(selectVal.length >0){
			var tmpVal = '',tmpObj;
			var	strHtm = [];

			var addItemCount = _this.targetElement.find(opts.itemSelector+':not(.ui-draggable)').length;

			var addItemKey = [];
			var addElements = [];
			var addItemMap = {};
			var dupChkFlag = true;

			selectVal.each(function (i, item){
				tmpObj = $(item);
				tmpVal=_this.getItemVal(tmpObj);
				
				var addChkFlag = typeof _this.addItemList[_this.config.currPage][tmpVal] ==='undefined'; 
				if(dupChkFlag && addChkFlag){
					dupChkFlag = false; 
				}

				if(!addChkFlag) return true; 

				if($.isFunction(opts.beforeItemMove)){
					if(opts.beforeItemMove(tmpObj) === false){
						return false;
					};
				}

				if(opts.maxSize != -1  && addItemCount >= opts.maxSize){

					if($.isFunction(opts.maxSizeMsg)){
						opts.maxSizeMsg.call();
					}else{
						alert(opts.maxSizeMsg);
					}
					if(returnFlag===true){
						return false;
					}

					return false;
				}
				addItemCount+=1;

				var selectItem = opts.sourceItem.items[_this.config.itemKey.sourceIdx[tmpVal]];
				var _addItem = $.extend(true , {}, selectItem);
				_addItem['_CU'] = 'C';


				addItemKey.push(tmpVal);

				addItemMap[tmpVal] =_addItem;

				strHtm.push(_this.getItemHtml('target',tmpVal ,selectItem ));

				addElements.push(tmpObj);

				if($.isFunction(opts.afterSourceMove)){
					opts.afterSourceMove(tmpObj);
				}
			});

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

			_this.targetElement.find('.empty-message').remove();

			for(var i =0; i <addElements.length; i++){
				addElements[i].addClass(opts.addItemClass);
			}

			for(var key in addItemMap){
				_this.addItemList[_this.config.currPage][key] =addItemMap[key];
			}
			
			if(returnFlag===true){
				return strHtm.join('');
			}else{
				if(opts.addPosition=='first'){
					_this.targetElement.prepend(strHtm.join(''));
				}else{
					_this.targetElement.append(strHtm.join(''));
				}
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
	 * @param itemEle {Element} value를 구할 element
	 * @description value 구하기.
	 */
	,targetMove : function (){
		var _this = this;
		var selectVal = _this.getSelectElement(_this.targetElement);

		if($.isFunction(_this.options.beforeMove)){
			if(_this.options.beforeMove('target') === false){
				return ;
			};
		}

		if(selectVal.length >0){
			var removeItem;
			var deleteItemKey = [];
			selectVal.each(function (i, item){
				var tmpKey = $(item).attr('data-val');
				removeItem = _this.options.sourceItem.items[_this.config.itemKey.sourceIdx[tmpKey]];

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
						_this.sourceElement.find(_this.options.itemSelector+'[data-val="'+tmpKey+'"]').removeClass(_this.options.addItemClass);
					}
				}
				$(item).remove();

				deleteItemKey.push(tmpKey);

				delete _this.addItemList[_this.config.currPage][tmpKey];

				if($.isFunction(_this.options.afterTargetMove)){
					_this.options.afterTargetMove(removeItem);
				}
			});
			
			if(Object.keys(_this.addItemList[_this.config.currPage]).length < 1){
				_this.targetElement.empty().html(_this.getEmptyMessage());
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
			, sourceItem = _opts.sourceItem
			, txtKey = sourceItem.optTxt
			, searchAttrName = sourceItem.searchAttrName
			, searchAttrKey = sourceItem.searchAttrKey == '' ? txtKey : sourceItem.searchAttrKey
			, renderTemplate = '';

		if(type=='source'){
			var styleClass ='';
			if(_this.addItemList[_this.config.currPage][seletVal]){
				styleClass += ' '+_opts.addItemClass;
			}

			if($.isFunction(sourceItem.render)){
				renderTemplate = sourceItem.render.call(sourceItem,{text : tmpItem[txtKey] , item : tmpItem});
			}else{
				renderTemplate = tmpItem[txtKey];
			}

			return '<li data-val="'+seletVal+'" '+searchAttrName+'="'+escape(tmpItem[searchAttrKey])+'" class="pub-select-item '+(selectFlag==true?_opts.addItemClass+' ' :'')+styleClass+'">'+renderTemplate+'</li>';
		}else{
			if($.isFunction(_opts.targetItem.render)){
				renderTemplate = _opts.targetItem.render.call(sourceItem,{text : tmpItem[txtKey] , item : tmpItem});
			}else{
				renderTemplate = tmpItem[txtKey];
			}

			return '<li data-pageno="'+(tmpItem[_opts.pageInfo.pageNumKey]||_this.config.currPage)+'" data-val="'+seletVal+'" '+searchAttrName+'="'+escape(tmpItem[searchAttrKey])+'" class="pub-select-item">'+renderTemplate+'</li>';
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
		var searchAttr = _opts.sourceItem.searchAttrName;

		var len = _opts.sourceItem.items.length
			,valKey = _opts.sourceItem.optVal
			,txtKey = _opts.sourceItem.optTxt
			,searchAttrName = _opts.sourceItem.searchAttrName
			,searchAttrKey = (_opts.sourceItem.searchAttrKey == '' ? txtKey : _opts.sourceItem.searchAttrKey)
			,tmpItem
			,strHtm = [];

		if( len> 0){
			_this.sourceElement.find(_opts.itemSelector).removeClass(_opts.selectClass);
			for(var i=0 ;i < len; i++){
				tmpItem = _opts.sourceItem.items[i];

				if(tmpVal=='' || tmpItem[searchAttrKey].indexOf(tmpVal) > -1){
					$('.'+_this.elePrefix+'source [data-val='+tmpItem[valKey]+']').show();
				}else{
					$('.'+_this.elePrefix+'source [data-val='+tmpItem[valKey]+']').hide();
				}
			}
			//_this.sourceElement.empty().html(strHtm.join(''));
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

})(jQuery, window, document);