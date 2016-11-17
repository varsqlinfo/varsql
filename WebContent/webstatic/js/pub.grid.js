/**
 * pubGrid v0.0.1
 * ========================================================================
 * Copyright 2016 ytkim
 * Licensed under MIT
 * http://www.opensource.org/licenses/mit-license.php
*/

;(function($, window, document) {
"use strict";

var _initialized = false
,_doc = $(document)
,_datastore = {}
,_defaults = {
	fixed:false
	,drag:false
	,scrollWidth : 18
	,minWidth : 30
	,autoResize : true
	,resizeGridWidthFixed : false	// 리사이즈시 그리드 리사이즈 여부.
	,headerOptions : {
		view : true	// header 보기 여부
		,sort : false	// 초기에 정렬할 값
		,redraw : true	// 초기에 옵션 들어오면 새로 그릴지 여부.
		,resize:{	// resize 여부
			enabled : true
		}
		,colWidthFixed:false  // 넓이 고정 여부.
		,colMinWidth : 50  // 컬럼 최소 넓이
		,resizeCursor : 'col-resize'
	}
	,height: 200
	,tColItem : [] //head item
 	,theadGroup : [] // head group 
	,tbodyItem : []  // body item
	,tbodyGroup : [] // body group 
	,tfootItem : []  // foot item
	,rowClick : false //row(tr) click event
	,rowContextMenu : false // row(tr) contextmenu event
}
,agt = navigator.userAgent.toLowerCase()
,_broswer = ((function (){
	if (agt.indexOf("msie") != -1) return 'msie'; 
	if (agt.indexOf("chrome") != -1) return 'chrome'; 
	if (agt.indexOf("firefox") != -1) return 'firefox'; 
	if (agt.indexOf("safari") != -1) return 'safari'; 
	if (agt.indexOf("opera") != -1) return 'opera'; 
	if (agt.indexOf("mozilla/5.0") != -1) return ',ozilla';
	if (agt.indexOf("staroffice") != -1) return 'starOffice'; 
	if (agt.indexOf("webtv") != -1) return 'WebTV'; 
	if (agt.indexOf("beonex") != -1) return 'beonex'; 
	if (agt.indexOf("chimera") != -1) return 'chimera'; 
	if (agt.indexOf("netpositive") != -1) return 'netPositive'; 
	if (agt.indexOf("phoenix") != -1) return 'phoenix'; 
	if (agt.indexOf("skipstone") != -1) return 'skipStone'; 
	if (agt.indexOf("netscape") != -1) return 'netscape'; 
})())
,_broswerVersion = ((function (){
	if(_broswer != 'msie') return -1; 
	var win = window;
	var doc = win.document;
	var input = doc.createElement ("input");
  
    if (win.ActiveXObject === undefined) return null;
    if (!win.XMLHttpRequest) return 6;
    if (!doc.querySelector){
		_defaults.scrollWidth = 21;
		return 7;
	}
    if (!doc.addEventListener){
		_defaults.scrollWidth = 18;
		return 8;
	}
    if (!win.atob){
		_defaults.scrollWidth = 18;
		return 9;
	}

    if (!input.dataset){
		_defaults.scrollWidth = 18;
		return 10;
	}
    return 11;
})());


function Plugin(element, options) {
	this._initialize(element, options);
	return this; 
}

Plugin.prototype ={
	/**
     * @method _initialize
     * @description 그리드 초기화.
     */
	_initialize :function(element,options){
		var _this = this; 
		_this.selector = element;
		_this.prefix = 'pub'+new Date().getTime();
		_this.element = $(element);
		_this.config = {totGridWidth : 0};
		_this.options =$.extend(true, {}, _defaults);
		_this.setOptions(options);
		_this.drag ={};
		_this._setGridWidth('init');
		
		_this.config.gridXScrollFlag = false;
		_this._setThead();
		
		
		_this.setData(_this.options.tbodyItem , 'init');

		_this._windowResize();

		return this;
	}
	/**
     * @method _setGridWidth
     * @description grid 넓이 구하기
     */
	,_setGridWidth : function (mode){
		var _this = this,pubGridWrapper;
		
		if(mode != 'init'){
			pubGridWrapper = $(_this.selector + '>.pubGrid-wrapper'); 
			pubGridWrapper.hide();
		}
			
		_this.config.gridSelectEleWidth = _this.element.innerWidth(); // border 값 빼주기.
		_this.config.gridElementWidth = _this.config.gridSelectEleWidth-3; // border 값 빼주기.
		_this.config.gridWidth = _this.config.gridElementWidth;
			
		if(mode != 'init') pubGridWrapper.show();
	}
	/**
     * @method setOptions
     * @description 옵션 셋팅.
     */
	,setOptions : function(options){
		var _this = this; 
		
		if($.isArray(options.tbodyItem)){
			delete _this.options.tbodyItem;
		}

		$.extend(true, _this.options, options);
		this.options.tbodyItem = options.tbodyItem ? options.tbodyItem : _this.options.tbodyItem;

		var _cb = _this.options.rowContextMenu.callback; 

		if(_this.options.rowContextMenu !== false && typeof _this.options.rowContextMenu == 'object'){
			var _cb = _this.options.rowContextMenu.callback; 
			
			if(_cb){
				_this.options.rowContextMenu.callback = function(key,sObj) {
					this.gridItem = _this.getItems(this.element.attr('rowInfo'));
					_cb.call(this,key,sObj);
				}
			}
		}else{
			_this.options.rowContextMenu =false; 
		}
	}
	/**
     * @method _setThead
	 * @param pItem {Object} - 데이타 .
	 * @param drawFlag {Boolean} - 새로 그리기 여부.
     * @description 헤더 label 셋팅.
     */
	,_setThead : function (){
		var _this = this
			,opt = _this.options;
			
		var tci = opt.tColItem
			,thg = opt.theadGroup
			,gridElementWidth =_this.config.gridElementWidth
			,tciItem,thgItem, rowItem, headItem
			,headGroupInfo = [],groupInfo = [], rowSpanNum = {};
		
		if(thg.length < 1){
			thg.push(tci);
		}
		
		var tmpThgIdx=0,tmpColIdx=0,tmpThgItem;
		var sortHeaderInfo = {};
		for(var i=0,j=0 ;i <thg.length; i++ ){
			thgItem = thg[i];
			groupInfo = [];
			tmpColIdx = 0;
			tmpThgIdx = 0;

			for(j=0; j<tci.length; j++) {
				tciItem = tci[j];
				if(tmpColIdx > j || tmpThgIdx >= thgItem.length){
					headItem = {r:i,c:j,view:false};
				}else{
					headItem=thgItem[tmpThgIdx];

					tmpColIdx +=(headItem['colspan'] || 0);
					headItem['r'] = i;
					headItem['c'] = j;
					headItem['view'] = true;
					headItem['sort'] = tciItem.sort===false?false:true;
					headItem['colSpanIdx'] = j;
					headItem['span'] = 'scope="col"';
					headItem['label'] = headItem.label ? headItem.label : tciItem.label;
					
					if(headItem.colspan){
						headItem['colSpanIdx'] = j+headItem.colspan-1;
						headItem['span'] = ' scope="colgroup" colspan="'+headItem.colspan+'" ';
					}

					if(rowSpanNum[j] && rowSpanNum[j] >= i){
						headItem['view'] = false;
					}
					
					if(headItem.rowspan){
						headItem['span'] = ' scope="col" rowspan="'+headItem.rowspan+'" ';
						rowSpanNum[j] = i+ headItem.rowspan -1;
					}
					
					tmpThgIdx +=1;
				}
				if(headItem.view==true){
					sortHeaderInfo[j] = {r:i,key:tciItem.key}
				}
				groupInfo.push(headItem);
			}
			headGroupInfo.push(groupInfo);
		}
		
		for(var _ikey in sortHeaderInfo){
			var tmpHgi = headGroupInfo[sortHeaderInfo[_ikey].r][_ikey]; 
			tmpHgi['isSort'] =(tmpHgi.sort===true?true:false); 
			headGroupInfo[sortHeaderInfo[_ikey].r][_ikey] = tmpHgi;
		}

		_this.config.headerInfo = headGroupInfo;

		var colWidth = Math.floor(gridElementWidth/tci.length);
		
		for(var j=0; j<tci.length; j++){
			var tciItem = opt.tColItem[j];
			tciItem.width = isNaN(tciItem.width) ? 0 :tciItem.width; 
			tciItem.width = Math.max(tciItem.width, opt.headerOptions.colMinWidth0);
			
			tciItem['_alignClass'] = tciItem.align=='right' ? 'ar' : (tciItem.align=='center'?'ac':'al');
			opt.tColItem[j] = tciItem;
			_this.config.totGridWidth +=tciItem.width;
		}
		
		_this._calcElementWidth();
	}
	/**
     * @method _calcElementWidth
	 * @description width 계산.
     */
	,_calcElementWidth : function (mode){

		var _this = this
			,_containerWidth ,_w
			,gridElementWidth = _this.config.gridElementWidth
			,opt = _this.options
			,tci = opt.tColItem
			,tciLen = tci.length;
		
		_w = _this.config.totGridWidth;
		_containerWidth = (_w+opt.scrollWidth);
		tciLen = tci.length;

		if( _containerWidth > gridElementWidth){
			_this.config.gridXScrollFlag = true;

			if(mode=='resize'){				
				_this.config.gridWidth = gridElementWidth - opt.scrollWidth;
 				var remainderWidth = Math.floor((_containerWidth-gridElementWidth)/tciLen);

				for(var j=0; j<tciLen; j++){
					opt.tColItem[j].width -= remainderWidth;
				}
				opt.tColItem[tciLen-1].width -=( (_containerWidth-gridElementWidth)%tciLen);
			}else{
				_this.config.gridWidth = _w; 
			}
		}else{
			if(opt.headerOptions.colWidthFixed !== true){
				_this.config.gridWidth = gridElementWidth - opt.scrollWidth;
				
				// 동적으로 width 계산할 경우 colwidth 처리.
				var _gw = _this.config.gridWidth; 
				var remainderWidth = Math.floor((_gw -_w)/tciLen);

				for(var j=0; j<tciLen; j++){
					opt.tColItem[j].width += remainderWidth;
				}
				opt.tColItem[tciLen-1].width +=( (_gw -_w)%tciLen);
			}else{

			}
		}
		_this.config.totGridWidth = _this.config.gridWidth;
		_this.config.height = opt.height;
		if(opt.height=='auto'){
			_this.config.height = _this.element.height();
		}
		//console.log(_this.config.gridWidth, gridElementWidth, _w, _sw );
	}
	/**
     * @method _setTbody
     * @param pItem {Object} - 데이타 .
	 * @param drawFlag {Boolean} - 새로 그리기 여부.
	 * @description 바디 데이타 셋팅
     */
	,_setTbody : function(){
		var _this = this; 
		this.options.tbodyItem = pItem;
	}
	/**
     * @method _setTfoot
	 * @param pItem {Object} - 데이타 .
	 * @param drawFlag {Boolean} - 새로 그리기 여부. 
     * @description foot 데이타 셋팅
     */
	,_setTfoot : function(){
		var _this = this; 
		this.options.tfootItem = pItem;

	}
	/**
     * @method _getColGroup
	 * @param type {String} - colgroup 타입
     * @description colgroup 구하기.
     */
	,_getColGroup :function (type){
		var _this = this
			,opt = _this.options
			,tci = opt.tColItem
			,thiItem;
		var strHtm = [];
		for(var i=0 ;i <tci.length; i++){
			thiItem = tci[i];
			var tmpStyle = [];
			tmpStyle.push('width:'+thiItem.width+'px;');
			if(thiItem.hidden===true){
				tmpStyle.push('display:none;');
			}
			strHtm.push('<col id="'+type+i+'" style="'+tmpStyle.join('')+'" />');
		}

		return strHtm.join('');	
	}
	/**
     * @method setData
	 * @param data {Array} - 데이타
	 * @param gridMode {String} - 그리드 모드 
     * @description 데이타 그리기
     */
	,setData :function (data, gridMode){
		var _this = this
			,opt = _this.options
			,tci = opt.tColItem;

		if(data){
			_this.options.tbodyItem = data
		}

		// sort 값이 있으면 초기 데이타 정렬
		if(opt.headerOptions.sort !==false){
			var _key ='', _sortType='asc', _idx = -1;
			if(typeof opt.headerOptions.sort ==='object'){
				_key = opt.headerOptions.sort.key;
				_sortType = opt.headerOptions.sort.type=='desc'?'desc':'asc';
			}else{
				_key = opt.headerOptions.sort;
			}

			for(var i=0 ;i < tci.length ; i++){
				if(tci[i].key == _key){
					_idx = i; 
					break; 
				}
			}
			
			if(_idx != -1) _this.getSortList(_idx, _sortType);
		}

		if(gridMode=='init'){
			_this.drawGrid();
		}else{
			_this.drawGrid('tbody', gridMode);
		}
	}
	/**
     * @method drawGrid
	 * @param  type {String} 그리드 타입.
     * @description foot 데이타 셋팅
     */
	,drawGrid : function (type, gridMode){
		var _this = this
			,opt = _this.options
			,ci = _this.config
			,tci = opt.tColItem
			,tbi = opt.tbodyItem
			,hederOpt=opt.headerOptions
			,thiItem;

		type = type ? type :'all';

		if(gridMode != 'sort'){
			$('.label-wrapper.sortasc,.label-wrapper.sortdesc').removeClass('sortasc sortdesc')
		}
		
		// header html 만들기
		function theadHtml(){
			var strHtm = [];

			strHtm.push('<colgroup id="'+_this.prefix+'colgroup_head">'+_this._getColGroup(_this.prefix+'colHeader')+'</colgroup>');

			strHtm.push('<thead>');
			if(ci.headerInfo.length > 0 && hederOpt.view){
				var ghArr, ghItem;
			
				for(var i =0,j=0 ; i <ci.headerInfo.length; i++){
					ghArr = ci.headerInfo[i];
					strHtm.push('<tr class="pub-header-tr">');
					for(j=0 ; j <ghArr.length; j++){
						ghItem = ghArr[j];
						if(ghItem.view){
							strHtm.push('	<th '+ghItem.span+' class="'+(_this.prefix+'-htd-'+(i+'_'+j))+'" '+(ghItem.style?' style="'+ghItem.style+'" ':'')+'>');
							strHtm.push('		<div class="label-wrapper">');
							strHtm.push('			<div class="pub-header-cont outer '+(ghItem.isSort===true?'sort-header':'')+'" col_idx="'+j+'"><div class="inner"><div class="centered">'+ghItem.label+'</div></div></div>');
							strHtm.push('			<div class="pub-header-resizer" colspanidx="'+ghItem.colSpanIdx+'"></div>');
							strHtm.push('		</div>');
							strHtm.push('	</th>');
						}
					}
					strHtm.push('</tr>');
				}
			}
			strHtm.push("</thead>");
			return strHtm.join('');
		}
		
		//body html  만들기
		function tbodyHtml(){
			var strHtm = [];
			
			if(tbi.length > 0){
				var tbiItem, clickFlag = false;
				
				for(var i =0 ; i <tbi.length ; i++){
					tbiItem = tbi[i];
					strHtm.push('<tr class="pub-body-tr" rowinfo="'+i+'">');

					for(var j=0 ;j <tci.length; j++){
						thiItem = tci[j];
						clickFlag = thiItem.colClick;
						
						strHtm.push('<td class="pub-body-td '+(thiItem.hidden===true ? 'pubGrid-disoff':'')+'"><div class="pub-content '+thiItem._alignClass+'"><a href="javascript:;" class="'+ (clickFlag?'pub-body-td-click':'') +'" colinfo="'+i+','+j+'">'+tbiItem[thiItem.key]+'</a></div></td>');
					}
				}
			}else{
				strHtm.push('<tr><td colspan="'+tci.length+'"><div class="text-center">NO DATA</div></td></tr>');
			}
			
			return strHtm.join('');
		}
		
		// foot html 만들기
		function tfootHtml(){
			var strHtm = [];
			strHtm.push("				<tfoot>");
			if(opt.tfootItem.length > 0){
				strHtm.push("					<tr class=\"pub-foot-tr\">");
				strHtm.push("						<td>1.9</td>");
				strHtm.push("						<td>0.003</td>");
				strHtm.push("						<td>40%</td>");
				strHtm.push("					</tr>");				
			}
			strHtm.push("				</tfoot>");
			return strHtm.join('');
		}

		if(type =='all'){
			
			var strHtm = [];
			strHtm.push('<div class="pubGrid-width-size" style="width:100%;height:0px;"></div>');
			strHtm.push('<div class="pubGrid-wrapper" style="width:'+_this.config.gridElementWidth+'px;">');
			strHtm.push('	<div id="'+_this.prefix+'pubGrid-container" class="pubGrid-container">');
			strHtm.push('		<div id="'+_this.prefix+'pubGrid-header-wrapper" class="pubGrid-header-wrapper">');
			strHtm.push('			<div id="'+_this.prefix+'pubGrid-header-container" class="pubGrid-header-container" style="width: '+(_this.config.totGridWidth+_this.options.scrollWidth)+'px;">');
			strHtm.push('				<table id="'+_this.prefix+'pubGrid-header" class="pubGrid-header" style="width:'+_this.config.gridWidth+'px" onselectstart="return false">');
			strHtm.push(theadHtml());
			strHtm.push('				</table>');
			strHtm.push('			</div>');	
			strHtm.push('		</div>');
			strHtm.push('		<div class="pubGrid-body-wrapper">');
			strHtm.push('			<div id="'+_this.prefix+'pubGrid-body-container" class="pubGrid-body-container" style="height:'+opt.height+'px;">');
			strHtm.push('				<table id="'+_this.prefix+'pubGrid-body" class="pubGrid-body" style="width:'+_this.config.gridWidth+'px">');
			strHtm.push('					<colgroup id="'+_this.prefix+'colgroup_body">'+_this._getColGroup(_this.prefix+'colbody')+'</colgroup>');
			strHtm.push('					<tbody class="pub-cont-tbody">');
			strHtm.push('					</tbody>');
			strHtm.push('				</table>');	
			strHtm.push('			</div>');
			strHtm.push('		</div>');
			strHtm.push('		<div id="'+_this.prefix+'hiddenArea" style="display:none;"></div>');
			strHtm.push('	</div>');

			strHtm.push('</div>');

			_this.element.empty();
			_this.element.html(strHtm.join(''));
			
			_this.config.headerWrapElement = $('#'+_this.prefix +'pubGrid-header-wrapper');
			_this.config.headerContainerElement = $('#'+_this.prefix +'pubGrid-header-container');
			_this.config.headerElement = $('#'+_this.prefix +'pubGrid-header');
			_this.config.bodyElement = $('#'+_this.prefix +'pubGrid-body');
			_this.config.bodyContainerElement = $('#'+_this.prefix +'pubGrid-body-container');
			_this.config.hiddenArea = $('#'+_this.prefix +'hiddenArea');
			
			if(opt.height =='auto'){
				var bodyH = _this.config.height-_this.config.headerWrapElement.height(); 
				bodyH = bodyH > 0?bodyH : _this.config.headerWrapElement.height+5 ; 
				_this.config.bodyContainerElement.css('height',(bodyH)+'px');
			}
			
			$(_this.selector+' .pubGrid-body-container').scroll(function (e){
				_this.config.headerWrapElement.scrollLeft($(this).scrollLeft());
			});

			// resize 설정
			_this._initHeaderEvent();
			_this._headerResize(hederOpt.resize.enabled);
		}
		
		if(type =='all' || type =='tbody'){
			$(_this.selector +' .pub-cont-tbody').empty().html(tbodyHtml());
			_this._initBodyEvent();
		}
	}
	/**
     * @method resizeDraw
     * @description resize 하기
     */
	,resizeDraw :function (opt){
		var _this = this;
		
		var isOpt =typeof opt==='undefined'; 
		
		opt = $.extend(true, {width : _this.element.innerWidth(), height : _this.element.height()}, (isOpt ? {} :opt));
		
		if(_this.config.gridSelectEleWidth == opt.width && _this.config.height == opt.height){
			return  false; 
		}
		
		if(!isOpt){
			_this.element.css('width',opt.width);
			_this.element.css('height',opt.height);
		}
		
		_this._setGridWidth();
		_this._calcElementWidth('resize');
		
		$(_this.selector+'>.pubGrid-wrapper').css('width',(_this.config.gridElementWidth)+'px');

		if(_this.options.resizeGridWidthFixed !== true){
			_this.config.headerContainerElement.css('width',(_this.config.gridWidth+_this.options.scrollWidth)+'px');
			_this.config.headerElement.css('width',(_this.config.gridWidth)+'px');
			_this.config.bodyElement.css('width',(_this.config.gridWidth)+'px');
		}
		
		if(_this.options.height =='auto'){
			var bodyH = opt.height-_this.config.headerWrapElement.height(); 
			bodyH = bodyH > 0?bodyH : _this.config.headerWrapElement.height()+5; 
			_this.config.bodyContainerElement.css('height',(bodyH)+'px');
		}

		$('#'+_this.prefix+"colgroup_head").empty().html(_this._getColGroup(_this.prefix+'colHeader'));
		$('#'+_this.prefix+"colgroup_body").empty().html(_this._getColGroup(_this.prefix+'colbody'));
		//_this._headerResize(_this.options.headerOptions.resize.enabled);

	}
	/**
     * @method resizeEnable
     * @description resize 사용
     */
	,resizeEnable :function (){
		this._headerResize(true);
	}
	/**
     * @method resizeDisable
     * @description risize 비활성.
     */
	,resizeDisable :function (){
		this._headerResize(false);
	}
	,_windowResize :function (){
		var _this = this; 
		if(_this.options.autoResize !== true) return false; 

		var _evt = $.event,
			_special,
			resizeTimeout;

		_special = _evt.special.pubgridResize = {
			setup: function() {
				$( this ).on( "resize.pubGrid", _special.handler );
			},
			teardown: function() {
				$( this ).off( "resize.pubGrid", _special.handler );
			},
			handler: function( event, execAsap ) {
				// Save the context
				var context = this,
					args = arguments,
					dispatch = function() {
						// set correct event type
						event.type = "pubgridResize";
						_evt.dispatch.apply( context, args );
					};

				if ( resizeTimeout ) {
					clearTimeout( resizeTimeout );
				}

				execAsap ?
					dispatch() :
					resizeTimeout = setTimeout( dispatch, _special.threshold );
			},
			threshold: 150
		};

		$(window).on("pubgridResize", function( event ) {
			_this.resizeDraw();
		});
	}
	/**
     * @method colResize
	 * @param  flag {Boolean} resize 여부
     * @description header resize 설정
     */
	,_headerResize :function (flag){
		var _this = this
			,resizeEle = $('#'+_this.prefix+'pubGrid-header .pub-header-resizer');
		if(flag===true){
			resizeEle.css('cursor','e-resize');
			
			resizeEle.on('touchstart.pubresizer mousedown.pubresizer',function (e){
				var oe = e.originalEvent.touches;

				_this.drag = {};
				_this.drag.pageX = oe ? oe[0].pageX : e.pageX;
				_this.drag.ele = $(this);
				_this.drag.colspanidx = _this.drag.ele.attr('colspanidx');
				_this.drag.colHeader= $('#'+_this.prefix+'colHeader'+_this.drag.colspanidx);
				_this.drag.colBody= $('#'+_this.prefix+'colbody'+_this.drag.colspanidx);
				_this.drag.colW = _this.drag.colHeader.attr('_width')?parseInt(_this.drag.colHeader.attr('_width'),10):_this.drag.colHeader.width();
				_this.drag.gridW = _this.config.headerElement.width();
				
				// resize시 select안되게 처리 . cursor처리 
				_doc.attr("onselectstart", "return false");
				_this.config.hiddenArea.append("<style type='text/css'>*{cursor:" + _this.options.headerOptions.resizeCursor + "!important}</style>");

				_doc.on('touchmove.colheaderresize mousemove.colheaderresize', function (e){
					_this.onGripDrag(e,_this);
				}).on('touchend.colheaderresize mouseup.colheaderresize mouseleave.colheaderresize', function (e){
					_this.onGripDragEnd(e,_this);
				});

				return false; 
			})
		}else{
			resizeEle.css('cursor','auto');
			resizeEle.off('touchstart.pubresizer mousedown.pubresizer');
		}
	}
	/**
     * @method getItems
	 * @param  idx {Integer} item index
     * @description item 값 얻기.
     */
	,getItems:function (idx){
		if(idx){
			return this.options.tbodyItem[idx]
		}else{
			return this.options.tbodyItem;
		}
	}
	/**
     * @method _initBodyEvent
     * @description 바디 이벤트 초기화.
     */
	,_initHeaderEvent : function (){
		var _this = this
			 ,headerCol =$('#'+_this.prefix+'pubGrid-container .pub-header-cont.sort-header');
		
		var beforeClickObj; 
		//headerCol.off('click.pubGridHeader.sort');
		headerCol.on('click.pubGridHeader.sort',function (e){
			var selEle = $(this)
				,col_idx = selEle.attr('col_idx')
				,sortType = selEle.attr('sort_type');
			
			if(beforeClickObj) beforeClickObj.closest('.label-wrapper').removeClass('sortasc sortdesc');

			//.removeClass('sortasc sortdesc');
			sortType = sortType =='asc' ? 'desc' : (sortType =='desc'?'asc':'asc');
			
			selEle.attr('sort_type', sortType);
			
			selEle.closest('.label-wrapper').removeClass('sortasc sortdesc').addClass('sort'+sortType);

			beforeClickObj = selEle;
		
			_this.setData(_this.getSortList(col_idx, sortType) ,'sort');
		});
	}
	/**
     * @method _initBodyEvent
     * @description 바디 이벤트 초기화.
     */
	,_initBodyEvent : function (){
		var _this = this
			 ,_rowTr =$('#'+_this.prefix+'pubGrid-container .pub-body-tr')
			 ,rowClickFlag =false; 
		if(_this.options.rowClick !== false && typeof _this.options.rowClick == 'function'){
			rowClickFlag =true; 

			var beforeRow; 
			_rowTr.off('click.pubgridrow');
			_rowTr.on('click.pubgridrow',function (e){
				var selRow = $(this)
					,rowinfo=selRow.attr('rowinfo')
					,selItem = _this.options.tbodyItem[rowinfo];
				
				if(beforeRow) beforeRow.removeClass('active');

				selRow.addClass('active');
				beforeRow = selRow; 
				
				_this.options.rowClick.call(selRow ,rowinfo , selItem);							
			});
		}

		if(_this.options.rowContextMenu !== false){
			$.pubContextMenu(_rowTr,_this.options.rowContextMenu);
		}
		
		if(!rowClickFlag){
			$('#'+_this.prefix+'pubGrid-container .pub-body-td-click').off('click.pub.gridcol');
			$('#'+_this.prefix+'pubGrid-container .pub-body-td-click').on('click.pub.gridcol',function (e){
				var selCol = $(this).attr('colinfo').split(',')
					,selRow = selCol[0]
					,colIdx = selCol[1]
					,selItem = _this.options.tbodyItem[selRow];

				_this.options.tColItem[colIdx].colClick.call(this,colIdx,{
					r:selRow
					,c:colIdx
					,item:selItem
				});
				
			});
		}
	}
	/**
     * @method getSortList
	 * @param  idx {Integer} item index
	 * @param  sortType {String} 정렬 타입 ex(asc,desc)
     * @description data sorting 처리.
     */
	,getSortList :function (idx, sortType){
		var _this = this
			,opt = _this.options
			,tci = opt.tColItem
			,tbi = opt.tbodyItem;
		
		if(idx < 0 || tbi.length < 1 || idx >= tci.length){
			return [];
		}
			
		var _key = tci[idx].key;

		function getItemVal(itemObj){
			return itemObj[_key];
		}
		
		if(sortType=='asc'){  // 오름차순
			tbi.sort(function (a,b){
				var v1 = getItemVal(a)
					,v2 = getItemVal(b);
				return v1 < v2 ? -1 : v1 > v2 ? 1 : 0;
			});
		}else{
			tbi.sort(function (a,b){ // 내림차순
				var v1 = getItemVal(a)
					,v2 = getItemVal(b);
				return v1 > v2 ? -1 : v1 < v2 ? 1 : 0;
			});
		}

		return tbi; 
	}
	/**
     * @method onGripDrag
	 * @param  e {Event} 이벤트
	 * @param  _this {Object} pub그리드 this
     * @description reisze 드래그 처리.
     */
	,onGripDrag : function(e, _this) { 
		
		if (!_this.drag) return false;

		var drag = _this.drag; 
		
		var t = drag.ele
			,oe = e.originalEvent.touches
			,ox = oe ? oe[0].pageX : e.pageX;
		
		var w = drag.colW + (ox - drag.pageX) ;
		
		//console.log(w , ox , drag.pageX, (ox - drag.pageX) )
		if(w > _this.options.minWidth){
			drag.changeColW = w;
			_this.config.gridElementWidth = drag.gridW+(ox - drag.pageX);
			_this.config.headerContainerElement.css('width',(_this.config.gridElementWidth+_this.options.scrollWidth)+'px');
			_this.config.headerElement.css('width',(_this.config.gridElementWidth)+'px');
			_this.config.bodyElement.css('width',(_this.config.gridElementWidth)+'px');

			drag.colHeader.css('width',w+'px');
			drag.colHeader.attr('_width',w);
			drag.colBody.css('width',w+'px');
		}		
			
		return false
	}
	/**
     * @method onGripDragOver
	 * @param  e {Event} 이벤트
	 * @param  _this {Object} pub그리드 this
     * @description reisze 드래그 end
     */
	,onGripDragEnd : function(e,_this) {
		_doc.off('touchend.colheaderresize mouseup.colheaderresize').off('touchmove.colheaderresize mousemove.colheaderresize mouseleave.colheaderresize');
		_doc.removeAttr("onselectstart");
		_this.config.hiddenArea.empty();
		
		if(!_this.drag) return false;
		
		_this.drag=false;

		return false; 
	}
	/**
     * @method destory
     * @description 해제.
     */
	,destory:function (){
		delete _datastore[this.selector];
		//this = {};
	}
	,getDataStore :function (){
		return _datastore; 
	}
};

$.pubGrid = function (selector,options, args) {
	
	if(!selector || $(selector).length < 1){
		return '['+selector + '] selector  not found '; 
	}

	if(typeof options === undefined){
		return _datastore[selector]; 
	}

	var _cacheObject = _datastore[selector]; 
	
	if(!_cacheObject){
		_cacheObject = new Plugin(selector, options);
		_datastore[selector] = _cacheObject;
		return _cacheObject; 
	}else if(typeof options==='object'){
		var headerOpt = options.headerOptions ?options.headerOptions :{}
			,reDrawFlag = typeof headerOpt.redraw==='boolean' ? headerOpt.redraw : _cacheObject.options.headerOptions.redraw; 

		if(reDrawFlag===true){
			_cacheObject = new Plugin(selector, options);
			_datastore[selector] = _cacheObject;
		}else{
			_cacheObject.setOptions(options);
			_cacheObject.drawGrid('tbody');
		}
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

}(jQuery, window, document));