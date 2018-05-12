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
			,width:300
			,overItemViewMode : 'drop'
			,moveZIndex : 9				// move 영역 z- index
			,filter: function ($obj) {
				// Modify $obj, Do not return
			}
			,icon :{
				prev :'pubTab-left-arrow'
				,next : 'pubTab-right-arrow'
			}
			,addClass : 'service_menu_tab'	// tab li 추가 클래스
			,items:[]							// tab item
			,click :function (item){			// tab click 옵션
				
			}
			,itemKey :{							// item key mapping
				title :'name'
			}
		};
        
    function Plugin(element, options) {
        this.selector = (typeof element=='object') ? element.selector : element;
		this.contextId = 'pubTab-'+new Date().getTime();
		this.element = $(element);

        options.width= isNaN(options.width) ?  this.element.width() : options.width;
        this.options = $.extend({}, defaults, options);
		
		this.init();
	
		return this; 
    }

	Plugin.prototype ={
		init :function(){
			var _this =this; 

			_this.config = {tabWidth :[]};
			_this.draw();

			_this.initEvt();

		}
		,initEvt : function (){
			var _this = this; 

			_this.element.on('click', '.pubTab-item-cont',function (e){
				var sEle = $(this); 

				_this.element.find('.pubTab-item-cont.active').removeClass('active');
				sEle.addClass('active')

				if($.isFunction(_this.options.click)){
					var tabIdx = sEle.attr('data-tab-idx');

					
					_this.options.click.call(this,_this.options.items[tabIdx])
				}
			})
			
			var prevElement = _this.element.find('.pubTab-prev')
				, nextElement = this.element.find('.pubTab-next');
			
			var prevTimerObj = null;
			prevElement.on( "mouseenter", function (e){
				var scrollLeft = _this.config.tabScrollElement.scrollLeft();
				function movePrev(){
					_this.config.tabScrollElement.scrollLeft(scrollLeft-10);
					prevTimerObj = setTimeout(function(){
						scrollLeft = _this.config.tabScrollElement.scrollLeft();
						movePrev()
					}, _this.options.speed);
				}

				movePrev();	
			}).on( "mouseleave", function (e){
				clearTimeout(prevTimerObj);
			});
			
			var nextTimerObj = null;
			nextElement.on( "mouseenter", function (e){
				var scrollLeft = _this.config.tabScrollElement.scrollLeft();
				function moveNext(){
					_this.config.tabScrollElement.scrollLeft(scrollLeft+10);
					nextTimerObj = setTimeout(function(){
						scrollLeft = _this.config.tabScrollElement.scrollLeft();
						moveNext()
					}, _this.options.speed);
				}
				
				prevElement.show();
				
				moveNext();	
			}).on( "mouseleave", function (e){
				clearTimeout(nextTimerObj);
			});


			if(_this.options.overItemViewMode =='drop'){

				_this.element.find('.pubTab-drop-open-btn').on('click', function (e){
					var sEle = $(this)
						,tabArea=sEle.closest('.pubTab-move-area')
					
					if(tabArea.hasClass('pubTab-open')){
						tabArea.removeClass('pubTab-open');
					}else{
						tabArea.addClass('pubTab-open');
					}
				});

				_this.element.on('click', '.pubTab-drop-item',function (e){
					var sEle = $(this)
						,dataIdx = sEle.data('tab-idx')
						,selItem =_this.config.tabWidth[dataIdx]; 

					_this.element.find('.pubTab-item-cont.active').removeClass('active');
					_this.element.find('.pubTab-item-cont[data-tab-idx="'+dataIdx+'"]').addClass('active');
					var itemEndPoint = selItem.leftLast+_this.config.moveAreaWidth+2; 

					var leftVal =0; 
					if(itemEndPoint > _this.config.width){
						leftVal = itemEndPoint - _this.config.width; 
					}else{

						var schLeft = _this.config.tabScrollElement.scrollLeft();
						if(schLeft > schLeft.leftFront){
							leftVal = schLeft.leftFront;
						}
					}
					_this.config.tabScrollElement.scrollLeft(leftVal);

					if($.isFunction(_this.options.click)){
						_this.options.click.call(this,_this.options.items[dataIdx])
					}

					_this.element.find('.pubTab-move-area').removeClass('pubTab-open');
				})
			}

			/*
			prevElement.on('click', function (e){
				var scrollLeft = _this.config.tabScrollElement.scrollLeft();
				
				_this.config.tabScrollElement.scrollLeft(scrollLeft-10);

				if(scrollLeft-20 < 0){
					prevElement.hide();
				}

			})

			nextElement.on('click', function (e){
				var scrollLeft = _this.config.tabScrollElement.scrollLeft();
				_this.config.tabScrollElement.scrollLeft(scrollLeft+10);

				if(scrollLeft > 0){
					prevElement.show();
				}
			})
			*/
		}
		,setWidth : function (val){
			var _this = this; 
			val = isNaN(val) ? _this.config.width :val;

			$('#'+_this.contextId+'pubTab').css('width',val);
			_this.config.tabScrollElement.css('width',val);
			
			_this.config.width = val; 

			if(_this.config.totalWidth > val){
				$('#'+_this.contextId+'pubTab-move-space').show();
				_this.element.find('.pubTab-move-area').show();
				/*
				var tabWidthArr = _this.config.tabWidth;
				var tmpIdx =tabWidthArr.length - 1;
				for(; tmpIdx > 0;tmpIdx--){
					if(tabWidthArr[tmpIdx].leftFront +_this.config.moveAreaWidth > val){
						_this.element.find('.pubTab-item-cont[data-tab-idx="'+tmpIdx+'"]').addClass('pubTab-hide')
					}else{
						_this.element.find('.pubTab-item-cont[data-tab-idx="'+tmpIdx+'"]').removeClass('pubTab-hide');
					}
				}
				*/
			}else{
				_this.element.find('.pubTab-item-cont').removeClass('pubTab-hide');
				$('#'+_this.contextId+'pubTab-move-space').hide();
				_this.element.find('.pubTab-move-area').hide();
				_this.config.tabContainerElement.css('left', '0px');
			}
		}
		,destory:function (){
			
		}
		,draw : function (){
			var _this = this
				,_opts = _this.options
				,items = _opts.items
				,itemLen = items.length; 
			
			function tabItemHtml (){
				var tabHtm = [];
				var titleKey = _opts.itemKey.title;
				for(var i = 0 ;i < itemLen ;i++){
					var item = items[i];
					tabHtm.push('<li class="pubTab-item '+(i+1==itemLen ? 'last':'')+'"><span class="pubTab-item-cont '+_opts.addClass+'" data-tab-idx="'+i+'">'+item[titleKey]+'</span></li>');
				}
				return tabHtm.join('');
			}

			function dropItemHtml (){
				var tabHtm = [];
				var titleKey = _opts.itemKey.title;
				for(var i = itemLen-1 ;i >= 0  ;i--){
					var item = items[i];
					tabHtm.push('<li class="pubTab-drop-item" data-tab-idx="'+i+'">'+item[titleKey]+'</li>');
				}
				return tabHtm.join('');
			}

			var strHtm = [];
			strHtm.push('<div class="pubTab-wrapper">');
			strHtm.push('	<div id="'+_this.contextId+'pubTab" class="pubTab">');
			strHtm.push('		<div id="'+_this.contextId+'pubTab-scroll" class="pubTab-scroll">');
			strHtm.push('			<ul id="'+_this.contextId+'pubTab-container" class="pubTab-container">');
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
				strHtm.push('		<ul id="'+_this.contextId+'DropItem" class="pubTab-drop-item-area">'+dropItemHtml()+'</ul>');
			}

			strHtm.push('</span>');
				
			
			
			strHtm.push('		</div>');
			strHtm.push('	</div>');
			strHtm.push('</div>');

			_this.element.empty().html(strHtm.join(''));
					
			_this.config.tabContainerElement =  $('#'+_this.contextId+'pubTab-container');
			_this.config.tabScrollElement = $('#'+_this.contextId+'pubTab-scroll');

			_this.config.moveAreaWidth  = this.element.find('.pubTab-move-area').width();
			$('#'+_this.contextId+'pubTab-move-space').css('width',_this.config.moveAreaWidth);

			_this.calcItemWidth();
			_this.setWidth(_opts.width);
		}
		,calcItemWidth :function (){
			var _this =this;
			var containerW = 0; 
			_this.config.tabContainerElement.find('.pubTab-item').each(function(i , item){
				var itemW =$(item).outerWidth();
				containerW +=itemW;
				_this.config.tabWidth[i] = {
					itemW : itemW
					,leftFront : (containerW-itemW)
					,leftLast : containerW
				}
			});

			_this.config.totalWidth = containerW;
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

		if(typeof options === undefined){
			return _cacheObject; 
		}
		
		if(!_cacheObject){
			_cacheObject = new Plugin(selector, options);
			_datastore[selector] = _cacheObject;
			return _cacheObject; 
		}else if(typeof options==='object'){
			_cacheObject.destory();
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
