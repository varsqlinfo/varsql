/**
 * pubResizer 0.0.1
 * ========================================================================
 * Copyright 2021-2021 ytkim
 * Licensed under MIT
 * http://www.opensource.org/licenses/mit-license.php
 * url : https://github.com/ytechinfo/pub
 * demo : http://pub.moaview.com/
*/

;(function ($, window, document) {
"use strict";
var pluginName = "pubResizer"
,_g_instance_idx =0
,_datastore = {}
,_defaults = {
	minSize : {
		width : 100
		,height : 100
	}
	,maxSize : {
		width : 1024
		,height : 700
	}
	// resize start
	,start :function (){}
	// resize
	,resize : function (){}
	//resize stop
	,stop :function (){}
};

function getHashCode (str){
	var hash = 0;
	if (str.length == 0) return hash;
	for (var i = 0; i < str.length; i++) {
		var tmpChar = str.charCodeAt(i);
		hash = ((hash<<5)-hash)+tmpChar;
		hash = hash & hash;
	}
	return ''+hash+''+(++_g_instance_idx);
}

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

function Plugin(selector, options) {
	this.selector = selector;
	this.prefix = pluginName + getHashCode(selector);
	this.element = $(selector);

	this.options = objectMerge({}, _defaults, options);
	
	this.init();

	return this;
}

Plugin.prototype ={
	init :function(){
		var _this =this;

		this.addResizeHandle();
		this.resizeEvent();
	
	}
	,addResizeHandle : function (){
		this.element.each(function (){
			var sEle = $(this);
			sEle.addClass('pubResizer-container');

			sEle.append('<div class="pubResizer-handler">..</div>')
		})
	}
	,resizeEvent : function (){
		var _this = this; 

		var hendlerEle = this.element.find('.pubResizer-handler'); 

		var minW = _this.options.minSize.width
			,minH = _this.options.minSize.height
			,maxW = _this.options.maxSize.width
			,maxH = _this.options.maxSize.height;

		var fnStart = _this.options.start
			,fnResize = _this.options.resize
			,fnStop = _this.options.stop;

		var moveX = 0, moveY = 0;	
								
		hendlerEle.off('touchstart.pubresizer mousedown.pubresizer');
		hendlerEle.on('touchstart.pubresizer mousedown.pubresizer',function (e){
			var oe = e.originalEvent.touches;

			var startX=  (oe && oe[0]? oe[0].pageX : e.pageX)
				,startY= (oe && oe[0]? oe[0].pageY : e.pageY);
						
			var containerEle = $(this).closest('.pubResizer-container');

			if(fnStart.call(null, startX, startY)===false){
				return false; 
			}

			var width = containerEle.width()
				,height = containerEle.height();

			$(document).on('touchmove.pubresizer mousemove.pubresizer', function (e1){
				var oe1 = e1.originalEvent.touches;
				
				moveX=	(oe1 && oe1[0]? oe1[0].pageX : e1.pageX) - startX;
				moveY= (oe1 && oe1[0]? oe1[0].pageY : e1.pageY) - startY;

				var w = width+moveX
					,h = height+moveY;
				
				if(fnResize.call(null, moveX, moveY)===false){
					return false; 
				};

				containerEle.css({
					width : (minW > w ? minW: (maxW < w ? maxW :  w))+'px'
					,height :(minH> h ? minH: (maxH < h ? maxH :  h)) +'px'
				});
					
			}).on('touchend.pubresizer mouseup.pubresizer mouseleave.pubresizer', function (e1){

				if(fnStop.call(null, moveX, moveY)===false){
					return false; 
				};

				$(document).off('touchmove.pubresizer mousemove.pubresizer').off('touchend.pubresizer mouseup.pubresizer mouseleave.pubresizer');
			});
		});
	}
	,getStartPosition : function (evt){
		var oe = evt.originalEvent.touches;
		
		return {
			y : (oe && oe[0]? oe[0].pageY : evt.pageY)
			,x : (oe && oe[0]? oe[0].pageX : evt.pageX)
		};
	
	}
	,destroy:function (){
		this.element.find('*').off();
		$._removeData(this.element)
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