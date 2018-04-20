/**
 * pubTab v0.0.1
 * ========================================================================
 * Copyright 2016 ytkim
 * Licensed under MIT
 * http://www.opensource.org/licenses/mit-license.php
 * url : https://github.com/ytechinfo/pub
 * demo : http://pub.moaview.com/
*/

;(function ($, window, document, undefined) {
"use strict";
    var pluginName = "pubTab"
		,_datastore = {}
		,defaults = {
			speed : 150
			,width:300
			,filter: function ($obj) {
				// Modify $obj, Do not return
			}
			,icon :{
				prev :'pub-tab-left-arrow'
				,next : 'pub-tab-right-arrow'
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

        options.width= isNaN(options.width) ?  this.element.width() : options.width
        this.options = $.extend({}, defaults, options);
		
		this.init();
	
		return this; 
    }

	Plugin.prototype ={
		init :function(){
			var _this =this; 

			_this.config = {};
			_this.draw();

			_this.initEvt();

		}
		,initEvt : function (){
			var _this = this; 

			_this.element.find('.pub-tab-item-cont').on('click', function (e){
				var sEle = $(this); 

				_this.element.find('.pub-tab-item-cont.active').removeClass('active');
				sEle.addClass('active')

				if($.isFunction(_this.options.click)){
					var tabIdx = sEle.attr('data-tab-idx');

					
					_this.options.click.call(this,_this.options.items[tabIdx])
				}
			})
			
			var prevElement = _this.element.find('.pub-tab-prev')
				, nextElement = this.element.find('.pub-tab-next');
			
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
			
			val = val || this.options.width;

			val = val =='auto' ? this.element.width():val;

			this.options.width = val; 
			$('#'+this.contextId+'pub-tab').css('width',val);
			this.config.tabScrollElement.css('width',val);

			if(this.config.tabContainerElement.width() >= this.options.width){
				$('#'+this.contextId+'pub-tab-move-space').show();
				this.element.find('.pub-tab-move-area').show();
			}else{
				$('#'+this.contextId+'pub-tab-move-space').hide();
				this.element.find('.pub-tab-move-area').hide();
			}
		}
		,destory:function (){
			
		}
		,draw : function (){
			var _this = this
				,items = _this.options.items
				,itemLen = items.length; 
			
			function tabItemHtml (){
				var tabHtm = [];
				var titleKey = _this.options.itemKey.title;
				for(var i = 0 ;i < itemLen ;i++){
					var item = items[i];
					tabHtm.push('<li class="pub-tab-item '+(i+1==itemLen ? 'last':'')+'"><span class="pub-tab-item-cont '+_this.options.addClass+'" data-tab-idx="'+i+'">'+item[titleKey]+'</span></li>');
				}
				return tabHtm.join('');
			}

			var strHtm = [];
			strHtm.push('<div class="pub-tab-wrapper">');
			strHtm.push('	<div id="'+_this.contextId+'pub-tab" class="pub-tab">');
			strHtm.push('		<div id="'+_this.contextId+'pub-tab-scroll" class="pub-tab-scroll">');
			strHtm.push('			<ul id="'+_this.contextId+'pub-tab-container" class="pub-tab-container">');
			strHtm.push(tabItemHtml());
			strHtm.push('			<li><div id="'+_this.contextId+'pub-tab-move-space"  style="display:none;">&nbsp;</div></li>');
			strHtm.push('			</ul>');
			strHtm.push('		</div> ');
			strHtm.push('		<div class="pub-tab-move-area">');
			strHtm.push('			<div class="pub-tab-move-dim"></div>');
			strHtm.push('			<i class="pub-tab-prev '+_this.options.icon.prev+'"></i>');
			strHtm.push('			<i class="pub-tab-next '+_this.options.icon.next+'"></i>');
			strHtm.push('		</div>');
			strHtm.push('	</div>');
			strHtm.push('</div>');

			_this.element.empty().html(strHtm.join(''));
					
			_this.config.tabContainerElement =  $('#'+_this.contextId+'pub-tab-container');
			_this.config.tabScrollElement = $('#'+_this.contextId+'pub-tab-scroll');


			$('#'+_this.contextId+'pub-tab-move-space').css('width',this.element.find('.pub-tab-move-area').width());
			_this.setWidth();
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
