/**
 * pubSplitter: v0.0.1
 * ========================================================================
 * Copyright 2021-2021 ytkim
 * Licensed under MIT
 * http://www.opensource.org/licenses/mit-license.php
 * url : https://github.com/ytechinfo/pub
 * demo : http://pub.moaview.com/
*/
;(function ($) {

var pluginName = "pubSplitter"
	,_datastore = {}
	,_splitterSeq = 0
	,_defaults = {
		mode : 'simple'			// wrapper
		,orientation: 'vertical'	// splitter 방향  default vertical
		,border: false				// splitter border
		,initAutoSize :false			// 최초 size 계산 여부.
		,button : {					// button option
			enabled : false			// enabled 활성화 여부 default true
			,toggle : false			// button min , max button 토글로 하나 보일지 두개 보일지 여부.
			,click : function (mode){	// click callback

			}
		}
		,windowResize :{
			enabled : true
			,responsive : true		//size responsive 사용여부 
			,delay : 10				// resize s
		}
		,minSize:{				// 최소 사이즈 설정
			size : 5			// 최소 값
			,unit : '%'			// 최소 값 단위 (%,px)
		} 				
		,useHelper: true		// 위치 조정시 helper 사용여부.
		,useOverray: true		// 위치 조정시 helper 사용여부.
		,resizable : true		// 크기 조절 가능 여부.
		,theme: 'light'			// 테마  light , dark
		,handleSize: 6			// handle size
		,start: function (splitterEle, splitterConf, moveData){}	// start event callback
		,move: function (splitterEle, splitterConf, moveData){}	// move event callback
		,stop: function (splitterEle, splitterConf, moveData){}	// stop event callback
	};

function isUndefined(obj){
	return typeof obj==='undefined';
}

function isEmpty(obj){
	return obj ==null || typeof obj==='undefined';
}

function percentage(val,tot){
	return val*tot/100;
}

function percent(val,tot){
	return val/tot*100;
}

function objectMerge() {
	var objMergeRecursive = function (dst, src) {

		for (var p in src) {
			if (!src.hasOwnProperty(p)) {continue;}

			var srcItem = src[p] ;
			if (srcItem=== undefined) {continue;}

			if ( typeof srcItem!== 'object' || srcItem=== null) {
				dst[p] = srcItem;
			} else if (typeof dst[p]!=='object' || dst[p] === null) {
				dst[p] = objMergeRecursive(srcItem.constructor===Array ? [] : {}, srcItem);
			} else {
				objMergeRecursive(dst[p], srcItem);
			}
		}
		return dst;
	}

	var reval = arguments[0];
	if (typeof reval !== 'object' || reval === null) {	return reval;}
	for (var i = 1, il = arguments.length; i < il; i++) {
		objMergeRecursive(reval, arguments[i]);
	}

	return reval;
}

function Plugin(selector, options) {
	this.selector = selector;
	this.prefix = 'pubSplitter_'+(_splitterSeq++);
	this.setOptions(options);

	this.config = {
		splitterConf: {}
		,moveInfo :{}
		,changeSize : {prevSize : -1, nextSize : -1}
	}
	this.selectorElement = $(this.selector);

	this.init();

	return this;
}

Plugin.prototype ={
	init :function(){
		this.initPanelSize();
		this.initEvt();
		this._windowResize();
	}
	,setOptions : function (opts){
		this.options =objectMerge({}, _defaults, opts);
	}
	/**
	 * @method initEvt
	 * @description init splitter event
	 */
	,initEvt : function (){
		var _this = this;
		var element =this.selectorElement;

		element.off('touchstart.pubsplitter mousedown.pubsplitter');
		element.on('touchstart.pubsplitter mousedown.pubsplitter',function (e){
			var ele = $(this);

			if($(e.target).closest('.pub-toggle-btn').length > 0){
				return ;
			}
			var splitterConf = _this.config.splitterConf[_$util.getPubsplitterId(ele)];

			if(splitterConf.resizable===false) return false;

			_this._dragSpliiter(e, ele, splitterConf);

			return false;
		});

		if(_this.options.button.enabled===true){

			var isFunction = $.isFunction(_this.options.button.click);

			element.off('click.toggle.btn');
			element.on('click.toggle.btn', '.pub-toggle-btn', function (e){
				var ele = $(this);
				var mode = ele.data('mode');

				ele.closest('.pub-splitter-button').attr('mode', mode);

				var splitterEle = ele.closest('.pub-splitter');
				var splitterConf = _this.config.splitterConf[_$util.getPubsplitterId(splitterEle)];

				_this.setLimitSize(splitterEle, splitterConf, mode);

				if(isFunction){
					_this.options.button.click.call(null, mode, ele);
				}

				return false;
			});
		}
	}
	,initPanelSize : function (){
		var _this = this;

		var splitterConf = {};
		var defaultMinSize = this.options.minSize.size;
		var defaultMinUnit = this.options.minSize.unit;
		var helperFlag = this.options.useHelper;
		var overrayFlag= this.options.useOverray;

		// html element attribute
		// orientation = vertical , horizontal
		// prev-min-size = pixel
		// next-min-size = pixel
		// button-toggle= true, false
		// resizable = true, false
		var parentIdx =0 ;
		var beforeNextEle;
		var parentEleInfo = {};
		var beforeParentEle;
		this.selectorElement.each(function (i){
			var sEle = $(this);

			var orientationInfo = sEle.data('orientation') || _this.options.orientation;
			orientationInfo = orientationInfo == 'horizontal' ? orientationInfo :'vertical';

			var resizableFlag = sEle.data('resizable');

			if(typeof resizableFlag !== 'boolean'){
				resizableFlag = _this.options.resizable;
			}

			if(orientationInfo == 'horizontal'){
				sEle.css({
					'height': _this.options.handleSize+'px'
					,'margin-top': '-'+_this.options.handleSize+'px'
					,'line-height' :_this.options.handleSize+'px'
				});
			}else{
				sEle.css({
					'width': _this.options.handleSize+'px'
					,'margin-left': '-'+_this.options.handleSize+'px'
				});
			}

			sEle.addClass('pub-splitter').addClass(orientationInfo +' ' + (_this.options.border==true?'pub-border':'pub-border-none') + ' ' + (resizableFlag ?'':'resizeable-none' ));
			var prefixIdx= _this.prefix+(i);
			sEle.attr('data-pubsplitter', prefixIdx);

			if(helperFlag===true){
				sEle.html($('<div class="pub-splitter-helper"/>').addClass(orientationInfo));
			}

			if(overrayFlag===true){
				sEle.append('<div class="pub-splitter-overray"/>');
			}

			if(_this.options.button.enabled===true){
				var btnToggleFlag = sEle.data('button-toggle');

				if(typeof btnToggleFlag !== 'boolean'){
					btnToggleFlag = _this.options.button.toggle;
				}

				var startX = _this.options.handleSize-2;

				var strHtm = '<div class="pub-splitter-button '+orientationInfo +(btnToggleFlag ? ' pub-splitter-button-toggle' :'')+'" mode="">';
				if(orientationInfo == 'vertical'){

					strHtm +='<div class="pub-toggle-btn" data-mode="prev" style="position:relative;margin-bottom:2px;margin-right:0px;height:'+(_this.options.handleSize+5)+'px"><svg style="position: absolute;right: 0.5px;" height="'+(_this.options.handleSize+5)+'px" viewBox="0 0 '+(startX*2/2-1)+' '+startX*2+'"><g><polygon points="'+startX+',0 '+startX+','+startX*2+' 1,'+startX+'"></polygon></g></svg></div>'
					+'<div class="pub-toggle-btn" data-mode="next" style="height:'+(_this.options.handleSize+5)+'px;"><svg style="position: absolute;left: -0.5px;" height="'+(_this.options.handleSize+5)+'px" viewBox="0 0 '+(startX*2/2-1)+' '+startX*2+'"><g><polygon points="0,0 0,'+startX*2+' '+(startX-1)+','+startX+'"></polygon></g></svg></div>';
					
				}else{
					strHtm 	+='<span class="pub-toggle-btn" data-mode="prev" style="position:relative;width:'+(startX*2)+'px;height:100%;margin-right:5px;"><svg style="position: absolute;left:0px;bottom: 0.5px;" height="'+(_this.options.handleSize-2)+'px" viewBox="0 0 '+startX*2+' '+(startX*2/2-1)+'"><g><polygon points="'+startX+',1 0,'+startX+' '+startX*2+','+startX+'"></polygon></g></svg></span>'
					+'<span class="pub-toggle-btn" data-mode="next" style="position:relative;width:'+((startX*2))+'px;height:100%;"><svg style="left:0px;position: absolute;top: -0.5px;" height="'+(_this.options.handleSize-2)+'px" viewBox="0 0 '+startX*2+' '+(startX*2/2-1)+'"><g><polygon points="0,0 '+startX*2+',0 '+(startX)+','+(startX-1)+'"></polygon></g></svg></span>';
				}
				strHtm+='</div>';

				sEle.append(strHtm);
			}

			var prevMinSize = sEle.data('prev-min-size')
				,nextMinSize = sEle.data('next-min-size');
			
			var minSizeUnit = sEle.data('unit') || defaultMinUnit;

			prevMinSize= parseInt(isNaN(prevMinSize) ? defaultMinSize : prevMinSize, 10);
			nextMinSize= parseInt(isNaN(nextMinSize) ? defaultMinSize : nextMinSize, 10);

			prevMinSize = prevMinSize < 0 ? 0 : prevMinSize;
			nextMinSize = nextMinSize < 0 ? 0 : nextMinSize;

			var prevEle = sEle.prev()
				,nextEle = sEle.next();

			if(orientationInfo == 'vertical'){
				prevEle.css('padding-right', _this.options.handleSize);
			}else{
				prevEle.css('padding-bottom', _this.options.handleSize);
			}

			var currentParentEle =sEle.parent()[0];

			if(beforeParentEle != currentParentEle){
				++parentIdx;

				var currentParentJQEle = $(currentParentEle);

				currentParentJQEle.addClass('pub-splitter-wrapper ' +orientationInfo);

				if(currentParentJQEle.data('responsive') === false){
					currentParentJQEle.addClass('fixed');
				}
				
				parentEleInfo[parentIdx]= {
					parentEle : currentParentEle
					,children :[]
					,childTotSize : 0
					,orientation: orientationInfo
				};
			}

			if(beforeNextEle != prevEle[0]){
				var eleSize = _$util.getSize(prevEle, orientationInfo);
				parentEleInfo[parentIdx].children.push({
					ele : prevEle
					,eleSize : eleSize
				});
				parentEleInfo[parentIdx].childTotSize += eleSize;

				prevEle.addClass('pub-splitter-item');
			}

			nextEle.addClass('pub-splitter-item');

			var eleSize = _$util.getSize(nextEle, orientationInfo);

			parentEleInfo[parentIdx].children.push({
				ele : nextEle
				,eleSize : eleSize
			});
			parentEleInfo[parentIdx].childTotSize += eleSize;

			splitterConf[prefixIdx] = {
				prevMinSize: (prevMinSize || 0)
				,nextMinSize: (nextMinSize ||0)
				,minUnit : minSizeUnit
				,orientation: orientationInfo
				,resizable : resizableFlag
			};

			beforeNextEle= nextEle[0];
			beforeParentEle = currentParentEle;
		})

		_this.config.splitterConf = splitterConf;

		if(this.options.initAutoSize){
			_this._setInitPanelSize(parentEleInfo);
		}
	}
	/**
	 * @method _setInitPanelSize
	 * @param parentEleInfo {Object} - splitter element
	 * @description set 초기 panel size
	 */
	,_setInitPanelSize : function (parentEleInfo){
		for(var key in parentEleInfo){
			var parentItem = parentEleInfo[key];
			var children = parentItem.children;
			var orientation = parentItem.orientation;
			var parentSize = _$util.getSize($(parentItem.parentEle), parentItem.orientation, 0);

			children.forEach(function (item){
				var childEle = $(item.ele);

				if(orientation =='horizontal'){
					var eleHPercent = percent(item.eleSize, parentItem.childTotSize);
					childEle.css('height', percentage(eleHPercent, parentSize) +'px' );
				}else{
					var eleWPercent = percent(item.eleSize, parentItem.childTotSize);
					childEle.css('width', percentage(eleWPercent, parentSize) +'px' );
				}
			});
		}
	}
	/**
	 * @method setSize
	 * @param prevSize {Integer} - prev size
	 * @param nextSize {Integer} - next size
	 * @param splitter {Integer or Ele} - splitter index or splitter ele
	 * @description move vertial
	 */
	,setSize : function (prevSize, nextSize, splitter){

		if(isUndefined(prevSize) || isNaN(prevSize)){
			throw new Error('not valid prevSize : '+ prevSize );
		}

		var ele;
		if(isUndefined(splitter)){
			ele = this.selectorElement.get(0);
		}else{
			if(!isNaN(splitter)){
				if(splitter < 0 || splitter > this.selectorElement.length){
					throw new Error( 'arrayIndexOutOfBoundsException : '+ splitter );
				}
				ele = this.selectorElement.get(splitter);
			}else{
				ele = splitter;
			}
		}

		ele = $(ele);

		var splitterConf = this.config.splitterConf[_$util.getPubsplitterId(splitterEle)];

		var sizeInfo =_$util.getSizeInfo(this, ele, splitterConf);

		this._startResize(ele, splitterConf);
		_$util.setPanelSize(this, sizeInfo, splitterConf, sizeInfo.prevSize + sizeInfo.nextSize, prevSize, nextSize);
		this._stopResize(ele, splitterConf);
	}
	,setLimitSize : function (sEle, splitterConf, mode){

		var sizeInfo =_$util.getSizeInfo(this, sEle, splitterConf);

		var eleTotSize = sizeInfo.prevSize + sizeInfo.nextSize;

		var prevSize=0, nextSize=0;

		if(mode == 'prev'){
			prevSize = sizeInfo.prevMinSize;
			nextSize = eleTotSize - prevSize;
		}else{
			nextSize = sizeInfo.nextMinSize;
			prevSize = eleTotSize - nextSize;
		}

		this._startResize(sEle, splitterConf);
		_$util.setPanelSize(this, sizeInfo, splitterConf, eleTotSize, prevSize, nextSize);
		this._stopResize(sEle, splitterConf);
	}
	/*
	 * @method _dragSpliiter
	 * @description move vertial
	 */
	,_dragSpliiter : function (e, ele, splitterConf){
		var _this = this;

		var orientation = splitterConf.orientation;

		var data = _$util.getStartPosition(e, orientation);

		var sizeInfo = _$util.getSizeInfo(this, ele, splitterConf);

		var prevSize = sizeInfo.prevSize
			,nextSize = sizeInfo.nextSize;

		var prevMinSize = sizeInfo.prevMinSize, nextMinSize = sizeInfo.nextMinSize;

		this._startResize(ele, splitterConf, data);
		this._showHelper(ele);
		$(document).on('touchmove.pubsplitter mousemove.pubsplitter', function (e){
			var movePosition = _$util.getMovePosition(e, data, orientation);
			var absPos= Math.abs(movePosition);

			sizeInfo.isMovePrev = movePosition < 0;
			

			if(sizeInfo.isMovePrev){
				if(prevMinSize > prevSize-absPos){
					absPos = prevSize- prevMinSize;
				}
			}else{
				if(nextMinSize> nextSize-absPos){
					absPos = nextSize- nextMinSize;
				}
			}

			sizeInfo.absPos = absPos;

			if(_this.options.useHelper){
				if(orientation =='horizontal'){
					_this.config.helperEle.css('top', (sizeInfo.isMovePrev?'-':'')+absPos+'px');
				}else{
					_this.config.helperEle.css('left', (sizeInfo.isMovePrev?'-':'')+absPos+'px');
				}
			}else{
				_this._setPanelMovePosition(splitterConf, sizeInfo);
			}
			data.move = absPos;
			_this.options.move.call(null, ele, splitterConf, data); // move
		}).on('touchend.pubsplitter mouseup.pubsplitter mouseleave.pubsplitter', function (e){
			if(_this.options.useHelper){
				_this._hideHelper(ele);
				_this._setPanelMovePosition(splitterConf, sizeInfo);
			}
			_this._stopResize(ele, splitterConf, data);

			$(document).off('touchmove.pubsplitter mousemove.pubsplitter').off('touchend.pubsplitter mouseup.pubsplitter mouseleave.pubsplitter');
		});
	}
	,_startResize : function (splitterEle, splitterConf, moveData){
		if(this.options.useOverray===true){
			splitterEle.find('.pub-splitter-overray').show();
		}

		this.options.start.call(null, splitterEle, splitterConf, moveData);
	}
	,_stopResize : function (splitterEle, splitterConf, moveData){
		var _this = this;
		if(this.options.useOverray===true){
			splitterEle.find('.pub-splitter-overray').hide();
		}
		
		this.options.stop.call(null, splitterEle, splitterConf, moveData);
	}
	/**
	 * @method _setPanelWidth
	 * @description set panel width
	 */
	,_setPanelMovePosition : function (splitterConf, sizeInfo){
		var absPos = sizeInfo.absPos;
		var isMovePrev = sizeInfo.isMovePrev;

		var prevSize = (sizeInfo.prevSize + (absPos * (isMovePrev?-1:1)))
			,nextSize = (sizeInfo.nextSize + (absPos * (isMovePrev?1:-1)));

		_$util.setPanelSize(this, sizeInfo, splitterConf, sizeInfo.prevSize + sizeInfo.nextSize, prevSize, nextSize);
	}
	/**
	 * @method _showHelper
	 * @description show helper
	 */
	,_showHelper : function (ele){
		this.config.helperEle = ele.find('.pub-splitter-helper');
		this.config.helperEle.css({
			'left': '0px'
			,'top': '0px'
		});

		if(this.options.useHelper)	ele.addClass('helper');
	}
	/**
	 * @method _windowResize
	 * @description window resize
	 */
	,_windowResize :function (){
		var _this = this;

		if(_this.options.windowResize.enabled === false) return false;

		var _evt = $.event,
			_special,
			resizeTimeout,
			eventName =  _this.prefix+"pubSplitterResize";

		_special = _evt.special[eventName] = {
			setup: function() {
				$( this ).on( "resize.pubSplitter", _special.handler );
			},
			teardown: function() {
				$( this ).off( "resize.pubSplitter", _special.handler );
			},
			handler: function( event, execAsap ) {
				// Save the context
				var context = this,
					args = arguments,
					dispatch = function() {
						// set correct event type
						event.type = eventName;
						_evt.dispatch.apply( context, args );
					};

				if ( resizeTimeout ) {
					clearTimeout( resizeTimeout );
				}

				execAsap ?
					dispatch() :
					resizeTimeout = setTimeout( dispatch, _special.delay );
			},
			threshold: _this.options.windowResize.delay
		};
		
		$(window).off(eventName);
		$(window).on(eventName, function( event ) {
			_this.resizeDraw();
		});
	}
	/**
	 * @method resizeDraw
	 * @description resize draw
	 */
	,resizeDraw : function (mode){
		var _this = this; 
		var beforeParentEle; 
		mode = mode ||'all';
		this.selectorElement.each(function (i){
			var sEle = $(this);
			
			var currentParentEle =sEle.parent()[0];

			var currentParentJQEle = $(currentParentEle);

			if(beforeParentEle != currentParentEle){
				if((mode=='all' || mode=='vertical') && currentParentJQEle.hasClass('vertical')){
					_this._resizeVertical(currentParentJQEle);
				}else if((mode=='all' || mode=='horizontal') && currentParentJQEle.hasClass('horizontal')){
					_this._resizeHorizontal(currentParentJQEle);
				}
			}

			beforeParentEle = currentParentEle;	
		})		
	}
	/**
	 * @method _resizeHorizontal
	 * @description resize horizontal
	 */
	,_resizeHorizontal : function (currentParentJQEle){
		var splitItemArr = [];
		var totH = 0;
		var parentH = currentParentJQEle.height(); 
		currentParentJQEle.children().each(function (){
			var splitItemEle = $(this);

			if(splitItemEle.hasClass('pub-splitter-item')){
				var splitItemH = Math.ceil(splitItemEle.outerHeight()); 

				if(splitItemEle.height() == 0){
					parentH -= splitItemH;
				}else{
					totH += splitItemH;
				}

				splitItemArr.push({
					ele : splitItemEle
					,w :  splitItemH
					,minFlag : (splitItemEle.height()==0)
				})
			}
		})

		var len = splitItemArr.length; 
		var addItemW = 0; 
		for(var i =0 ;i < len; i++){
			var item = splitItemArr[i];

			if(!item.minFlag){
				var percentagVal = percentage(percent(item.w, totH),parentH); 
				percentagVal = Math.round(percentagVal);

				addItemW += percentagVal

				if(i == len -1){
					var modVal = parentH - addItemW;
					item.ele.css('height', (percentagVal + modVal) + 'px');							
				}else{
					item.ele.css('height', percentagVal + 'px');
				}
			}
		}
	}
	/**
	 * @method _resizeVertical
	 * @description resize vertical
	 */
	,_resizeVertical : function (currentParentJQEle){
		var splitItemArr = [];
		var totW = 0;
		var parentW = currentParentJQEle.width(); 
		currentParentJQEle.children().each(function (){
			var splitItemEle = $(this);

			if(splitItemEle.hasClass('pub-splitter-item')){
				var splitItemW = Math.ceil(splitItemEle.outerWidth()); 

				if(splitItemEle.width() == 0){
					parentW -= splitItemW;
				}else{
					totW += splitItemW;
				}

				splitItemArr.push({
					ele : splitItemEle
					,w :  splitItemW
					,minFlag : (splitItemEle.width()==0)
				})
			}
		})

		var len = splitItemArr.length; 
		var addItemW = 0; 
		for(var i =0 ;i < len; i++){
			var item = splitItemArr[i];

			if(!item.minFlag){
				var percentagVal = percentage(percent(item.w, totW),parentW); 
				percentagVal = Math.round(percentagVal);

				addItemW += percentagVal

				if(i == len -1){
					var modVal = parentW - addItemW;
					item.ele.css('width', (percentagVal + modVal) + 'px');							
				}else{
					item.ele.css('width', percentagVal + 'px');
				}
			}
		}
	}
	/**
	 * @method _hideHelper
	 * @description hide helper
	 */
	,_hideHelper : function (ele){
		ele.removeClass('helper');
	}
	,destroy:function (){

		this.selectorElement.find('*').off();

		this.selectorElement.each(function (){
			var sEle = $(this);
			sEle.removeClass('pub-splitter vertical horizontal pub-border');
			sEle.removeAttr('style').removeAttr('data-pubsplitter');
			sEle.parent().removeClass('pub-splitter-wrapper vertical horizontal');
			sEle.prev().removeClass('pub-splitter-item')
			sEle.next().removeClass('pub-splitter-item')
			
			sEle.empty();
		})

		$._removeData(this.selector)
		delete _datastore[this.selector];
	}
};

var _$util = {
	setPanelSize : function (gridCtx, sizeInfo, splitterConf, parentSize, prevSize, nextSize){
		var prevElement = sizeInfo.prevElement
			,nextElement = sizeInfo.nextElement;

		var prevMinSize = sizeInfo.prevMinSize;

		if(prevSize <= prevMinSize){
			prevSize = prevMinSize;
			nextSize = parentSize - prevMinSize;
		}

		var nextMinSize =sizeInfo.nextMinSize;

		if(nextSize <= nextMinSize){
			nextSize = nextMinSize;
			prevSize = parentSize - nextMinSize;
		}

		prevSize = prevSize.toFixed(1);
		nextSize = nextSize.toFixed(1);

		if(splitterConf.orientation == 'horizontal'){
			prevElement.css('height', prevSize+'px');
			nextElement.css('height', nextSize+'px');
		}else{
			prevElement.css('width', prevSize+'px');
			nextElement.css('width', nextSize+'px');
		}

		gridCtx.config.changeSize = {
			prevSize : parseFloat(prevSize)
			, nextSize : parseFloat(nextSize)
			, prevOverSize : sizeInfo.prevOverSize
			, nextOverSize : sizeInfo.nextOverSize
			, isPrevMinSize : (prevSize == sizeInfo.prevOverSize)
			, isNextMinSize : (nextSize == sizeInfo.nextOverSize)
		};
	}
	,getPubsplitterId : function (ele){
		return ele.attr('data-pubsplitter');
	}
	/**
	 * @method getSize
	 * @param ele {element} - prev size
	 * @param orientationInfo {String} - next size
	 * @param sizeType {Integer} - 0=width, 1= inner , 2 outer , 3 outer(true)
	 * @description 사이즈 구하기.
	 */
	,getSize : function (ele, orientationInfo, sizeType){
		if(sizeType==0){
			return orientationInfo == 'horizontal' ? ele.height() : ele.width();
		}
		return orientationInfo == 'horizontal' ? ele.outerHeight() : ele.outerWidth();
	}
	/**
	 * @method getSizeInfo
	 * @description move info
	 */
	,getSizeInfo : function (gridCtx, ele, splitterConf){
		var prevElement = ele.prev()
			,nextElement = ele.next();
		var sizeInfo = {};
		if(splitterConf.orientation == 'vertical'){
			sizeInfo={
				 prevSize : prevElement.outerWidth()
				, nextSize : nextElement.outerWidth()

				, prevOverSize : prevElement.outerWidth() - prevElement.width()
				, nextOverSize : nextElement.outerWidth() - nextElement.width()

				, parentSize : ele.parent().width()
			}
		}else{
			sizeInfo={
				 prevSize : prevElement.outerHeight()
				, nextSize : nextElement.outerHeight()

				, prevOverSize : prevElement.outerHeight() - prevElement.height()
				, nextOverSize : nextElement.outerHeight() - nextElement.height()

				, parentSize : ele.parent().height()
			}
		}

		if(splitterConf.minUnit == '%'){
			sizeInfo.prevMinSize = Math.max(percentage(splitterConf.prevMinSize, sizeInfo.parentSize), sizeInfo.prevOverSize);
			sizeInfo.nextMinSize = Math.max(percentage(splitterConf.nextMinSize, sizeInfo.parentSize), sizeInfo.nextOverSize);
		}else{
			sizeInfo.prevMinSize = Math.max(splitterConf.prevMinSize, sizeInfo.prevOverSize);
			sizeInfo.nextMinSize = Math.max(splitterConf.nextMinSize, sizeInfo.nextOverSize);
		}
		
		sizeInfo.prevElement = prevElement;
		sizeInfo.nextElement = nextElement;

		//this.config.sizeInfo=sizeInfo;
		return sizeInfo;
	}
	,getStartPosition : function (evt, orientation){
		var oe = evt.originalEvent.touches;
		if(orientation =='horizontal'){
			return {startPos : (oe && oe[0]? oe[0].pageY : evt.pageY)};
		}else{
			return {startPos : (oe && oe[0]? oe[0].pageX : evt.pageX)};
		}
	}
	,getMovePosition : function(evt, data, orientation){
		var oe = evt.originalEvent.touches;
		if(orientation =='horizontal'){
			return (oe && oe[0]? oe[0].pageY : evt.pageY) - data.startPos;
		}else{
			return (oe && oe[0]? oe[0].pageX : evt.pageX) - data.startPos;
		}
	}
}

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
