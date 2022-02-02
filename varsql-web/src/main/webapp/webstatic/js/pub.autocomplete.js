/**
 * pubAutocomplete : v1.0.2
 * ========================================================================
 * Copyright 2016~2021 ytkim
 * Licensed under MIT
 * http://www.opensource.org/licenses/mit-license.php
 * url : https://github.com/ytechinfo/pub
 * demo : http://pub.moaview.com/
*/
;(function ($, window, document) {

    var pluginName = "pubAutocomplete"
		,initialized = false
		,_datastore = {}
		,pubElement= false
        ,_defaults = {
			_currMode : 'default'
			,viewAreaSelector : false	// 결과가 보여질 element
			,useFilter : true	// 필터 사용여부.
			,minLength: 1		// 최소 사이즈
			,autoClose : true	// 자동 닫힘 여부
			,itemkey : 'title'	// string , function
			,height : 200		// 높이 값
			,selectCls : 'selected'	// select css class name
			,emptyMessage : 'no data'	// 데이터 없을때 값
			,searchDelay : -1		// 검색 delay
			,items :[]				// items
			,charZeroConfig : false	// 글자 없을때 설정
			,filter : function (itemVal , searchVal) {	// 검색 데이터 처리 필터
				searchVal = searchVal.toLowerCase();
				return ~(itemVal).toLowerCase().indexOf(searchVal);
			}
			,source: function (request, response){	// data 호출
				response(this.items);
			}
			,select : function (event,item){	// select
				console.log('onSelect : ' + item);
			}
			,focus : function (e){	// foucs 시 이벤트

			}
			,renderItem : function (matchData,item){ // 표시 할 데이터 처리
				return matchData;
			}
			,hilightTemplate : '<b>$1</b>' // 하이라이트 처리
			,autocompleteTemplate : function (baseHtml){ // 자동 완성 템플릿
				return baseHtml;
			}
			,initTemplateElementEvent : function (){ // template event 처리

			}
		};

    function Plugin(element, options) {
        this.selector = (typeof element=='object') ? element.selector : element;
		this.selectorElement = $(this.selector);
		this.autocompleteEle = false;
		this.autocompleteEleId = pluginName+'-'+new Date().getTime();
        this.options = $.extend({}, _defaults, options);
		this.size = {eleH: 0,itemH : 0, itemAllH : 0};
		this.currentSearchVal='';
		this.config={
			initStyleFlag : false
		};
		this.layerMouseOver =true;
		if(pubElement ===false){
			$('body').append('<div class="pub-autocomplete-area"></div>');
			pubElement = $('.pub-autocomplete-area');
		}

		this.selectorElement.attr('autocomplete',"off");

		var itemKey = this.options.itemkey; 

		this.config.itemKeyFn = $.isFunction(itemKey)?itemKey : function (item){return item[itemKey]};

		this._addAutocompleteTemplate();

		this.init();

		return element;
    }

	Plugin.prototype ={
		/**
		 * @method init
		 * @description 자동완성 초기화
		 */
		init :function(){
			var _this = this;
			_this.initEvt();
		}
		/**
		 * @method initEvt
		 * @description 이벤트 초기화
		 */
		,initEvt : function (){
			var _this = this;

			_this.selectorElement.on('blur.pubAutocomplete' , function (e){
				if(!_this.layerMouseOver){
					_this.hide();
				}
			});

			_this.selectorElement.on('focus.pubAutocomplete' , function (e){
				if($.isFunction(_this.options.focus)){
					_this.options.focus.call(_this,e);
				}
				_this.setCssStyle();

				if(_this.selectorElement.val().length ==0){
					_this._charZeroEvent();
				}
			});

			_this.selectorElement.on('keydown.pubAutocomplete' , function (e){
				var key = window.event ? e.keyCode : e.which;

				switch(key){
					case 40 : { //down
						_this.moveItem('down');
						break;
					}
					case 38 : { //up
						_this.moveItem('up');
						break;
					}
					case 27 : { //esc
						_this.hide();
						break;
					}
					case 13 : // enter
					case 9 :{
						if(_this.getSelectElement().length > 0){
							_this.selectItem();
						}
						break;
					}
					default:{
						break;
					}
				}
			});

			var searchTimeout;
			var beforeSearchVal = '';
			_this.selectorElement.on('input.pubAutocomplete' , function (e){
				var searchVal = _this.selectorElement.val();

				if(beforeSearchVal == searchVal){
					return ; 
				}

				if(searchVal.length==0){
					_this._charZeroEvent();
					return ;
				}else if(searchVal.length <= _this.options.minLength){
					_this.hide();
					return ;
				}

				if(_this.options.searchDelay !== -1){
					if(searchTimeout) window.clearTimeout(searchTimeout);
					searchTimeout = window.setTimeout(function (){ // 검색어 완성시 검색 할수 있게 지연처리.
						_this.gridItems('default',searchVal);
					}, _this.options.searchDelay );
				}else{
					
					_this.gridItems('default',searchVal);
				}

			});

			_this.autocompleteEle.on('click','.pub-autocomplete-item',function (e){
				var beforeSelectItem = $('.pub-autocomplete-item.'+_this.options.selectCls);

				if(beforeSelectItem.length > 0){
					beforeSelectItem.removeClass(_this.options.selectCls);
				}

				$(this).addClass(_this.options.selectCls);
				_this.selectItem(e);
			})

			if($.isFunction(_this.options.initTemplateElementEvent)){
				_this.options.initTemplateElementEvent.call(this);
			}
		}
		,setCssStyle : function (cssStyle){
			var _this =this;
			if(typeof cssStyle==='undefined'){
				if(!_this.config.initStyleFlag){

					var position = _this.selectorElement.offset();

					var pubAutocompleteWrapper = _this.autocompleteEle.closest('.pub-autocomplete-wrapper');

					var _width = this.options.width ||_this.selectorElement.outerWidth();

					pubAutocompleteWrapper.css('width',_width+'px');

					if(_this.options.viewAreaSelector ===false){
						pubAutocompleteWrapper.css({ 'left': position.left+'px'
							,'top':(position.top+_this.selectorElement.outerHeight())+'px'
						})
					}

					_this.config.initStyleFlag = true;
				}
			}else{
				_this.autocompleteEle.closest('.pub-autocomplete-wrapper').css(cssStyle);
			}
		}
		/**
		 * @method _charZeroEvent
		 * @description 한글자도 입력되지 않았을때
		 */
		,_charZeroEvent : function (){
			if($.isFunction(this.options.charZeroConfig.init)){
				var reval = this.options.charZeroConfig.init.call(this);
				this.show();

				if(typeof reval=='string'){
					this.autocompleteEle.find('.pub-autocomplete-item-area').empty().html(reval);
				}else{
					this.calcSize();

					if(this.options.charZeroConfig.initEvt){
						this.options.charZeroConfig.initEvt.call(this);
					}
				}
			}else{
				this.hide();
			}
		}
		/**
		 * @method moveItem
		 * @param mode {String} up or down
		 * @description 자동완성 아래위  방향키 처리.
		 */
		,moveItem : function (mode){
			var _this = this;

			if(!_this.autocompleteEle.is(':visible')){
				_this.show();
				return ;
			}

			var selectItem;
			var upFlag = 'up'==mode?true:false;
			var beforeSelectItem = _this.getSelectElement();

			if(beforeSelectItem.length > 0){
				if(upFlag){
					selectItem = beforeSelectItem.removeClass(_this.options.selectCls).prev('.pub-autocomplete-item').addClass(_this.options.selectCls);
				}else{
					selectItem = beforeSelectItem.removeClass(_this.options.selectCls).next('.pub-autocomplete-item').addClass(_this.options.selectCls);
				}
				if(selectItem.length < 1){
					_this.autocompleteEle.scrollTop((upFlag ? _this.size.itemAllH :0));
					_this.selectorElement.val(_this.currentSearchVal);
				}else{
					var eleH = _this.size.eleH, itemH = _this.size.itemH
						,scrTop = _this.autocompleteEle.scrollTop()	,sItemTop= selectItem.position().top;

					if (sItemTop + itemH - eleH > 0){
						_this.autocompleteEle.scrollTop(sItemTop + itemH + scrTop - eleH -_this.config.position.top);
					}else if (sItemTop < 0){
						_this.autocompleteEle.scrollTop(sItemTop + scrTop - _this.config.position.top);
					}

					_this.selectorElement.val(unescape(selectItem.attr('data-val')));
				}

			}else{
				_this.autocompleteEle.scrollTop((upFlag ? _this.size.itemAllH :0));

				selectItem = _this.autocompleteEle.find('.pub-autocomplete-item:'+(upFlag?'last':'first')).addClass(_this.options.selectCls);

				if(selectItem.attr('data-val')){
					_this.selectorElement.val(unescape(selectItem.attr('data-val')));
				}
			}
		}
		/**
		 * @method _addAutocompleteTemplate
		 * @description 자동완성 기본 템플릿 넣기.
		 */
		,_addAutocompleteTemplate : function (){
			var _this = this;
			if($(_this.autocompleteEleId).length < 1){
				var autotemplate = '<div id='+_this.autocompleteEleId+' class="pub-autocomplete-item-wrapper" style="max-height:'+_this.options.height+'px;"><ul class="pub-autocomplete-item-area"></ul></div>';

				var allTemplateHtm = [];
				allTemplateHtm.push('<div class="pub-autocomplete-wrapper">');

				if($.isFunction(_this.options.autocompleteTemplate)){
					allTemplateHtm.push(_this.options.autocompleteTemplate(autotemplate));
				}else{
					allTemplateHtm.push(autotemplate);
				}

				allTemplateHtm.push('</div>');

				if(_this.options.viewAreaSelector !==false){
					$(_this.options.viewAreaSelector).empty().html(allTemplateHtm.join(''));
				}else{
					pubElement.append(allTemplateHtm.join(''));
				}

				_this.autocompleteEle =  $('#'+_this.autocompleteEleId);

				if(_this.options.autoClose){
					_this.autocompleteEle.closest('.pub-autocomplete-wrapper').on('mouseenter', function (e){
						_this.layerMouseOver = true;
					}).on('mouseleave', function (e){
						_this.layerMouseOver = false;
					});
				}
			}
		}
		/**
		 * @method getSelectElement
		 * @description 선택된 html eleement 얻기.
		 */
		,getSelectElement : function (){
			return this.autocompleteEle.find('.pub-autocomplete-item.'+ this.options.selectCls);
		}
		/**
		 * @method getSelectElement
		 * @description 선택된 html eleement 얻기.
		 */
		,getSelectItem : function (idx){
			if(typeof idx !=='undefined'){
				return this.getItems()[idx];
			}else{
				var idx = this.autocompleteEle.find('.pub-autocomplete-item.'+ this.options.selectCls).attr('data-idx');

				if(idx){
					return this.getItems()[idx];
				}
			}
			return {};
		}
		/**
		 * @method setEmptyMessage
		 * @param msg {String} msg
		 * @description empty message
		 */
		,setEmptyMessage : function (msg){
			this.options.emptyMessage = msg;
		}
		/**
		 * @method setItems
		 * @param items {Array} items
		 * @description 자동완성 item
		 */
		,setItems : function (items){
			this.options.items = items;
		}
		/**
		 * @method getItems
		 * @param mode {String} mode
		 * @description 자동완성 모드에 따른 아이템 얻기.
		 */
		,getItems : function (){
			return this._getOptionValue('items');
		}
		/**
		 * @method getOptions
		 * @param mode {String} mode
		 * @description 옵션 얻기.
		 */
		,getOptions : function (mode){
			if(typeof mode !== 'undefined'){
				return this.options[mode];
			}else{
				return this.options[this.options._currMode]||this.options ;
			}
		}
		,_getOptionValue : function (key){
			return this.getOptions()[key] || this.options[key];
		}
		/**
		 * @method hide
		 * @description 숨기기
		 */
		,hide : function (){
			this.autocompleteEle.closest('.pub-autocomplete-wrapper').hide();
		}
		/**
		 * @method show
		 * @description 보이기
		 */
		,show : function (){
			this.autocompleteEle.closest('.pub-autocomplete-wrapper').show();

			if(!this.config.position){
				this.config.position = this.autocompleteEle.position();
			}
		}
		/**
		 * @method updatedefaults
		 * @param opts {Object} 옵션
		 * @description 기본 옵션 업데이트.
		 */
		,updatedefaults:function (opts){
			_defaults = $.extend({}, _defaults, opts);
		}
		/**
		 * @method gridItems
		 * @param mode {String} 검색 모드
		 * @param searchVal {String} 검색어
		 * @description 자동완성 그리기.
		 */
		,gridItems : function (mode ,searchVal){
			var _this = this;

			_this.options._currMode = mode;
			_this.currentSearchVal = searchVal;

			if(mode=='default' && $.isFunction(_this._getOptionValue('source'))){
				_this._getOptionValue('source').call(this.options,searchVal, function (item){
					_this.setItems(item);
					_this._drawSearchData(mode,searchVal);
				});
			}else{
				return _this._drawSearchData(mode,searchVal);
			}
		}
		/**
		 * @method _drawSearchData
		 * @param mode {String} 검색 모드
		 * @param searchVal {String} 검색어
		 * @description 자동완성 그리기.
		 */
		,_drawSearchData : function (mode,searchVal){
			var _this = this;

			var opts =_this.getOptions()
				,items = _this.getItems()
				,len = items.length
				,emptyFlag = true;

			var strHtm = [];

			if(len > 0){
				var renderFn = _this._getOptionValue('renderItem')
					,filterFn = _this._getOptionValue('filter')
					,hilightTemplate = _this._getOptionValue('hilightTemplate')
					,useFilter = _this._getOptionValue('useFilter')
					,itemKeyFn = _this.config.itemKeyFn;
				
				var tmpSearchVal = searchVal.replace(/[-\/\\^$*+?.()|[\]{}]/g, '\\$&');
				var re = new RegExp("(" + tmpSearchVal.split(' ').join('|') + ")", "gi");

				for (var i=0; i<len; i++) {
					var item = items[i];
					var itemVal = (typeof item ==='string' ? item : itemKeyFn(item));
					if(searchVal==''){
						emptyFlag = false;
						strHtm.push('<li class="pub-autocomplete-item" data-idx="'+i+'" data-val="'+escape(itemVal)+'">'+renderFn(itemVal,item)+'</li>');
					}else if(useFilter===false){
						emptyFlag = false;
						strHtm.push('<li class="pub-autocomplete-item" data-idx="'+i+'" data-val="'+escape(itemVal)+'">'+renderFn(_this.getHilightTemplateData(re, itemVal,hilightTemplate),item)+'</li>');
					}else{
						if(filterFn(itemVal , searchVal)){
							emptyFlag = false;
							strHtm.push('<li class="pub-autocomplete-item" data-idx="'+i+'" data-val="'+escape(itemVal)+'">'+renderFn(_this.getHilightTemplateData(re, itemVal,hilightTemplate),item)+'</li>');
						}
					}
				}
			}
			if(emptyFlag){
				_this.viewEmptyMessage();
			}else{
				_this.autocompleteEle.find('.pub-autocomplete-item-area').empty().html(strHtm.join(''));
				_this.calcSize();
				_this.show();
			}
		}
		/**
		 * @method viewEmptyMessage
		 * @param msg {String} message
		 * @description 결과 없을때 보여줄 메소드
		 */
		,viewEmptyMessage : function (msg){
			if(this._getOptionValue('emptyMessage') ===false){
				this.hide();
			}else{
				if(msg){
					this.autocompleteEle.find('.pub-autocomplete-item-area').empty().html(msg);
				}else{
					if($.isFunction(this._getOptionValue('emptyMessage'))){
						this.autocompleteEle.find('.pub-autocomplete-item-area').empty().html('<li>'+this._getOptionValue('emptyMessage').call(this)+'</li>');
					}else{
						this.autocompleteEle.find('.pub-autocomplete-item-area').empty().html('<li>'+this._getOptionValue('emptyMessage')+'</li>');
					}
				}
			}
		}
		/**
		 * @method selectItem
		 * @description 아이템 선택
		 */
		,selectItem :  function (event){
			this.hide();

			this.selectorElement.val(unescape(this.getSelectElement().attr('data-val')));
			if($.isFunction(this.options.select)){
				this.options.select.call(this,event,this.getSelectItem());
			}
		}
		/**
		 * @method calcSize
		 * @description size 계산
		 */
		,calcSize : function (){
			if(this.options.items.length > 0){
				this.size = {
					eleH : this.autocompleteEle.height()
					,itemH : this.autocompleteEle.find('.pub-autocomplete-item:first').outerHeight()
					,itemAllH : this.autocompleteEle.find('.pub-autocomplete-item-area').outerHeight()
				};
			}else{
				this.size = {eleH: 0,itemH : 0, itemAllH : 0};
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
		,getHilightTemplateData: function (re,item, hilightTemplate){
			return item.replace(re,hilightTemplate )
		}
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