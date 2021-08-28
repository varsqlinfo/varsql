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
	,defaults = {
		fadeSpeed: 100				// 숨김 속도 
		,mode : 'simple'			// wrapper
		,orientation: 'vertical'	// splitter 방향  default vertical
		,border: false				// splitter border 
		,initAutoSize :true			// panel width fix
		,useButton : false			// click 으로 이동. 
		,minSize : 0				// default pixel
		,percent: true			// position % 여부 true , false , {vertical : false, horizontal : false}
		,useHelper: true		// 위치 조정시 helper 사용여부.
		,useOverray: true		// 위치 조정시 helper 사용여부.
		,theme: 'light'			// 테마  light , dark
		,handleSize: 6
		,autoResize :true
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
	
function Plugin(selector, options) {
	this.selector = selector;
	this.prefix = 'pubSplitter_'+(_splitterSeq++);
	this.setOptions(options);

	this.config = {
		splitterConf: {}
		,moveInfo :{}
	}
	this.element;
	this.selectorElement = $(this.selector);
	
	this.init();

	return this; 
}

Plugin.prototype ={
	init :function(){
		this.initPanelSize();
		this.initEvt();
	}
	,setOptions : function (opts){
		var options = $.extend({}, defaults, opts);

		if(options.percent===true){
			options.percent = {vertical: true, horizontal: false};
		}else if(opts.percent===false){
			options.percent = {vertical: false, horizontal: false};
		}else{
			options.percent = $.extend({},  {vertical: false, horizontal: false}, options.percent);
		}

		this.options =options; 
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
			var splitterConf = _this.config.splitterConf[ele.data('pubsplitter')];

			_this._dragSpliiter(e, ele, splitterConf);
			
			return false; 
		});

		if(_this.options.useButton){
			element.on('click.toggle.btn', '.pub-toggle-btn', function (e){
				var ele = $(this);
				var mode = ele.data('mode');
									
				var splitterEle = ele.closest('.pub-splitter');

				var splitterConf = _this.config.splitterConf[splitterEle.data('pubsplitter')];

				_this.setLimitSize(splitterEle, splitterConf, mode);
							
				return false; 
			});
		}
	}
	,initPanelSize : function (){
		var _this = this; 
		
		var splitterConf = {};
		var defaultMinSize = this.options.minSize;
		var helperFlag = this.options.useHelper;
		var overrayFlag= this.options.useOverray;
			
		// html element attribute
		// orientation = vertical , horizontal
		// prev-min-size = pixel 
		// next-min-size = pixel 
		// percent = true, false; 
		var parentIdx =0 ; 
		var beforeNextEle;
		var parentEleInfo = {};
		var beforeParentEle; 
		this.selectorElement.each(function (i){
			var sEle = $(this); 

			var orientationInfo = sEle.data('orientation') || _this.options.orientation;
			orientationInfo = orientationInfo == 'horizontal' ? orientationInfo :'vertical';

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
			
			sEle.addClass('pub-splitter').addClass(orientationInfo +' ' + (_this.options.border==true?'pub-border':'pub-border-none'));
			var prefixIdx= _this.prefix+(i); 
			sEle.attr('data-pubsplitter', prefixIdx);

			if(helperFlag===true){
				sEle.html($('<div class="pub-splitter-helper"/>').addClass(orientationInfo));
			}

			if(overrayFlag===true){
				sEle.append('<div class="pub-splitter-overray"/>');
			}

			if(_this.options.useButton===true){
				var strHtm = '<div class="pub-splitter-button '+orientationInfo+'" >';
				if(orientationInfo == 'vertical'){
					strHtm +='<span class="pub-toggle-btn" data-mode="prev" style="width:100%;height:16px;margin-bottom:5px;"><svg viewBox="0 0 6 12"><path d="M0 6 L6 12 L6 0 Z"></path></svg></span>'
					+'<span class="pub-toggle-btn" data-mode="next" style="width:100%;height:16px;"><svg viewBox="0 0 6 12"><g><path d="M0 0 L0 12 L6 6 Z"></path></g></svg></span>';
				}else{
					strHtm 	+='<span class="pub-toggle-btn" data-mode="prev" style="width:16px;height:100%;margin-right:5px;"><svg viewBox="0 0 12 6" style="position: relative;"><g><path d="M6 0 L0 6 L12 6 Z"></path></g></svg></span>'
					+'<span class="pub-toggle-btn" data-mode="next" style="width:16px;height:100%;"><svg viewBox="0 0 12 6" style="position: relative;"><g><path d="M0 0 L6 6 L12 0 Z"></path></g></svg></span>'					
				}
				strHtm+='</div>';

				sEle.append(strHtm);
			}
						
			var prevMinSize = sEle.data('prev-min-size')
				,nextMinSize = sEle.data('next-min-size');

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

			var confPercent = String(sEle.data('percent'));
			confPercent = confPercent == 'false' ? false : (confPercent == 'true' ? true : _this.options.percent[orientationInfo]);

			var currentParentEle =sEle.parent()[0]; 

			if(beforeParentEle != currentParentEle){
				++parentIdx;
				
				$(currentParentEle).addClass('pub-splitter-wrapper ' +orientationInfo);

				parentEleInfo[parentIdx]= {
					parentEle : currentParentEle
					,parentSize : _$util.getSize($(currentParentEle), orientationInfo, 0)
					,children :[]
					,childTotSize : 0
					,orientation: orientationInfo
				};
			}

			if(beforeNextEle != prevEle[0]){
				parentEleInfo[parentIdx].children.push({
					ele : prevEle
					,percent : confPercent
				});
				parentEleInfo[parentIdx].childTotSize += _$util.getSize(prevEle, orientationInfo);	
			}

			parentEleInfo[parentIdx].children.push({
				ele : nextEle
				,percent : confPercent
			});
			parentEleInfo[parentIdx].childTotSize += _$util.getSize(nextEle, orientationInfo);

			splitterConf[prefixIdx] = {
				prevMinSize: prevMinSize
				,nextMinSize: nextMinSize
				,percent: confPercent
				,orientation: orientationInfo
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
			var parentSize = parentItem.parentSize;
			
			children.forEach(function (item){
				var childEle = $(item.ele);
				
				if(orientation =='horizontal'){
					var eleH = percentage(percent(childEle.outerHeight(), parentItem.childTotSize), parentSize);
					childEle.css('height', (item.percent ? percent(eleH, parentSize)+'%' : eleH +'px') );
				}else{
					var eleW = percentage(percent(childEle.outerWidth(), parentItem.childTotSize), parentSize);
					childEle.css('width', (item.percent ? percent(eleW, parentSize)+'%' : eleW +'px') );
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

		var splitterConf = this.config.splitterConf[ele.data('pubsplitter')]; 

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
		if(this.options.useOverray===true){
			splitterEle.find('.pub-splitter-overray').hide();
		}
		if(splitterConf.percent){
			var prevEle = splitterEle.prev()
				,nextEle = splitterEle.next();

			if(splitterConf.orientation == 'horizontal'){
				var parentH = splitterEle.parent().height();
				
				prevEle.css('height', percent(prevEle.outerHeight(), parentH)+'%');					
				nextEle.css('height', percent(nextEle.outerHeight(), parentH)+'%')
			}else{	
				var parentW = splitterEle.parent().width();
							
				prevEle.css('width', percent(prevEle.outerWidth(), parentW)+'%');					
				nextEle.css('width', percent(nextEle.outerWidth(), parentW)+'%')
			}
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
			sEle.removeAttr('style')
			sEle.parent().removeClass('pub-splitter-wrapper vertical horizontal');
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
		
		if(splitterConf.orientation == 'horizontal'){
			prevElement.css('height', prevSize+'px');
			nextElement.css('height', nextSize+'px');
		}else{
			prevElement.css('width', prevSize+'px');
			nextElement.css('width', nextSize+'px');	
		}
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

		if(splitterConf.percent){
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

})(jQuery);
