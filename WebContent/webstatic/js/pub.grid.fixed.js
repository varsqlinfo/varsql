/**
 * pubGrid v0.0.1
 * ========================================================================
 * Copyright 2016 ytkim
 * Licensed under MIT
 * http://www.opensource.org/licenses/mit-license.php
 * url : https://github.com/ytechinfo/pub
 * demo : http://pub.moaview.com/
*/

;(function($, window, document) {
"use strict";

var _initialized = false
,_$doc = $(document)
,_datastore = {}
,_defaults = {
	fixed:false
	,drag:false
	,scrollWidth : 18	// 스크롤바 넓이
	,minWidth : 38
	,rowOptions:{
		height: 22	// cell 높이
		,click : false //row(tr) click event
		,contextMenu : false // row(tr) contextmenu event
	}
	,useDefaultFormatter :false
	,formatter :{
		money :{prefix :'$', suffix :'원' , fixed : 0}	// money 설정 prefix 앞에 붙일 문구 , suffix : 마지막에 뭍일것 , fixed : 소수점 
		,number : {prefix :'$', suffix :'원' , fixed : 0}
	}
	,bigData : {
		enabled: true
		,gridCount : 30		// 화면에 한꺼번에 그리드 할 데이타 gridcount * 3 이 한꺼번에 그려진다.  auto 일 경우 화면 height 체크 해서 처리 한다. 
		,spaceUnitHeight : 100000	// 그리드 공백 높이 지정
		,horizontalEnableCount : 20	// 컬럼 view 카운트. 20개 이상일경우 처리. 
	}
	,autoResize : {
		enabled:true
		,responsive : false // 리사이즈시 그리드 리사이즈 여부.
		,threshold :150
	}
	,resizeGridWidthFixed : true	// 리사이즈시 그리드 리사이즈 여부.
	,headerOptions : {
		view : true	// header 보기 여부
		,height: 23
		,sort : false	// 초기에 정렬할 값
		,redraw : true	// 초기에 옵션 들어오면 새로 그릴지 여부.
		,resize:{	// resize 여부
			enabled : true
			,cursor : 'col-resize'
		}
		,displayLineNumber : false	 // 라인 넘버 보기.
		,displayRowSelector : false	 // row selecotr checkbox 보기
		,displayModifyInfo : false	 // 수정여부 보기
		,colFixedIndex : 0	// 고정 컬럼 
		,colWidthFixed : false  // 넓이 고정 여부.
		,colMinWidth : 50  // 컬럼 최소 넓이
		,viewAllLabel : true
		,contextMenu : false // header contextmenu event

	}
	,scroll :{
		lazyLoad : false // scroll 실시간으로 로드할지 여부 (속도에 영향으줌. )
		,lazyLoadTime : 30 // scroll 로드 타임. 
		,verticalWidth : 12
		,horizontalHeight: 12
	}
	,height: 200
	,tColItem : [] //head item
 	,theadGroup : [] // head group 
	,tbodyItem : []  // body item
	,tbodyGroup : [] // body group 
	,tfootItem : []  // foot item
	,page : false	// paging info
	,message : {
		empty : 'no data'
		,pageStatus : function (status){
			return status.currStart +' - ' + status.currEnd+' of '+ status.total;
		}
	}
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
		//_defaults.scrollWidth = 21;
		return 7;
	}
    if (!doc.addEventListener){
		//_defaults.scrollWidth = 18;
		return 8;
	}
    if (!win.atob){
		//_defaults.scrollWidth = 18;
		return 9;
	}

    if (!input.dataset){
		//_defaults.scrollWidth = 18;
		return 10;
	}
    return 11;
})());

var util= {
	formatter : {
		'money' : function (num , fixedNum , prefix , suffix){
			return (prefix||'')+ util.formatter.number(num, fixedNum) +(suffix||'');
		}
		,'number': function (num, fixedNum){
			
			if(!isNaN(num)){
				return num; 
			}
			
			fixedNum = fixedNum || 0; 
			
			if (!isFinite(num)) {
				return num;
			}
			if(typeof num === 'string'){
				num = num* 1;
			}
			
			var a = num.toFixed(fixedNum).split('.');
			a[0] = a[0].replace(/\d(?=(\d{3})+$)/g, '$&,');
			return a.join('.');
		}
		,'string' : function (val){
			return val ; 
		}
	}
}

function getHashCode (str){
    var hash = 0;
    if (str.length == 0) return hash;
    for (var i = 0; i < str.length; i++) {
        var tmpChar = str.charCodeAt(i);
        hash = ((hash<<5)-hash)+tmpChar;
        hash = hash & hash; 
    }
    return ''+hash+'99';
}
function copyStringToClipboard (prefix , copyText) {
	var isRTL = document.documentElement.getAttribute('dir') == 'rtl';

	if (typeof window.clipboardData !== "undefined" &&
	  typeof window.clipboardData.setData !== "undefined") {
		window.clipboardData.setData("Text", copyText);
		return ; 
	}

	var _id = prefix+'copyTextId'; 
	var copyArea = document.getElementById(_id); 
	if(!copyArea){
		var fakeElem = document.createElement('textarea');
		var yPosition = window.pageYOffset || document.documentElement.scrollTop;
		fakeElem.id =_id;
		fakeElem.style = 'top:'+yPosition+'px;font-size : 12pt;border:0;padding:0;margin:0;position:absolute;' +(isRTL ? 'right' : 'left')+':-9999px';
		fakeElem.setAttribute('readonly', '');

		document.body.appendChild(fakeElem);
		copyArea = document.getElementById(_id);
	}

	copyArea.value = copyText;
	copyArea.select();
	
	function handler (event){
		document.removeEventListener('copy', handler);
		copyArea = null; 
	}
	document.addEventListener('copy', handler);

	document.execCommand('copy');
}


function Plugin(element, options) {
	this._initialize(element, options);
	return this; 
}

function $pubSelect(selector){
	return document.querySelector(selector);
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

Plugin.prototype ={
	/**
     * @method _initialize
     * @description 그리드 초기화.
     */
	_initialize :function(selector,options){
		// scroll size 
		var _this = this; 
		_this.selector = selector;

		_this.prefix = 'pub'+getHashCode(_this.selector);
		_this.gridElement = $(selector);
		
		_this.element = {};
		_this.config = {
			gridWidth :{aside : 0,left : 0, main:0, total:0} 
			, body :{height : 0,width : 0}
			, header :{height : 0, width : 0}
			, footer :{height : 0, width : 0}
			, navi :{height : 0, width : 0}
			, scroll : {before:{},top :0 , left:0, startCol:0, endCol : 0,startRow : 0, endRow :0, viewIdx : 0, vBarPosition :0 , hBarPosition :0 , maxViewCount:0, viewCount : 0, verticalHeight:0,horizontalWidth:0}
			,aside :{items :[]}
			,select :{startIdx : 0,endIdx : 0, startRow : 0 ,startCol : 0, endRow:0, endCol :0}
		};
		
		_this.setOptions(options, true);

		_this.drag ={};
		_this.addStyleTag();

		_this._setThead();
		_this.setData(_this.options.tbodyItem , 'init');
		
		_this.config.gridXScrollFlag = false;
		_this._windowResize();

		return this;
	}
	/**
     * @method _setGridWidth
     * @description grid 넓이 구하기
     */
	,_setGridWidth : function (mode){
		var _this = this;
		
		_this.config.body.width = _this.gridElement.innerWidth()-1; // border 값 빼주기.			
	}
	/**
     * @method setOptions
     * @description 옵션 셋팅.
     */
	,setOptions : function(options , firstFlag){
		var _this = this; 
		
		_this.options =objectMerge($.extend(true , {}, _defaults), options);
					
		_this.options.tbodyItem = options.tbodyItem ? options.tbodyItem : _this.options.tbodyItem;

		//_this.config.rowHeight = _this.options.rowOptions.height+1;	// border-box 수정. 2017-08-11
		_this.config.rowHeight = _this.options.rowOptions.height;

		_this.config.drawBeforeData = {}; // 이전 값을 가지고 있기 위한 객체

		if(_this.options.rowOptions.contextMenu !== false && typeof _this.options.rowOptions.contextMenu == 'object'){
			var _cb = _this.options.rowOptions.contextMenu.callback; 
			
			if(_cb){
				_this.options.rowOptions.contextMenu.callback = function(key,sObj) {
					this.gridItem = _this.getItems(_this.config.scroll.viewIdx + parseInt(this.element.attr('rowInfo'),10));
					_cb.call(this,key,sObj);
				}
			}
		}else{
			_this.options.rowOptions.contextMenu =false; 
		}

		if(_this.options.headerOptions.contextMenu !== false && typeof _this.options.headerOptions.contextMenu == 'object'){
			var _hcb = _this.options.headerOptions.contextMenu.callback; 
			
			if(_hcb){
				_this.options.headerOptions.contextMenu.callback = function(key,sObj) {
					var headerInfo = this.element.attr('data-header-info'); 
					headerInfo = headerInfo.split(',');
					var selHeaderInfo = _this.config.headerInfo[headerInfo[0]][headerInfo[1]];
					this.gridItem = selHeaderInfo;
					_hcb.call(this,key,sObj);
				}
			}
		}else{
			_this.options.headerOptions.contextMenu =false; 
		}
		var asideItem = [];
		if(_this.options.headerOptions.displayLineNumber ===true){
			asideItem.push({
				key : 'number'
				,name : ''
				,width : 40
			});
		}

		if(_this.options.headerOptions.displayRowSelector ===true){
			asideItem.push({
				key : 'chceck'
				,name : 'check'
				,width : 25
			});
		}
		
		if(_this.options.headerOptions.displayModifyInfo ===true){
			asideItem.push({
				key : 'modify'
				,name : ''
				,width : 10
			});
		}
		
		_this.config.aside.items = asideItem; 
		for(var i =0 ; i < asideItem.length ;i++){
			_this.config.gridWidth.aside += asideItem[i].width; 
		}
		
		_this._setGridWidth();
	}
	,initScrollData : function (gridCount){
		this.config.scroll.endCol = this.options.tColItem.length-1;
		this.config.scroll.endRow = this.options.tbodyItem.length;
		this.config.scroll.totalHeight = this.options.tbodyItem.length * this.config.rowHeight; 

		return this.config.scroll; 
	}
	/**
     * @method addStyleTag
	 * @param options {Object} - 데이타 .
     * @description  add style tab
     */
	,addStyleTag : function (){
		var _this = this
			,_d = document; 
		
		var cssStr = [];
		
		var rowOptHeight = _this.options.rowOptions.height; 

		if(!isNaN(rowOptHeight)){
			cssStr.push('#'+_this.prefix+'_pubGrid .pub-body-td, #'+_this.prefix+'_pubGrid .pub-body-aside-td{height:'+rowOptHeight+'px;}');
		}

		var headerHeight = _this.options.headerOptions.height; 

		if(!isNaN(headerHeight)){
			cssStr.push('#'+_this.prefix+'_pubGrid .pubGrid-header-container th{height:'+headerHeight+'px;}');
		}
		var styleId = _this.prefix+'_pubGridStyle'; 
		var styleTag = document.getElementById(styleId);

		if(styleTag){
			if (styleTag.styleSheet) {
				styleTag.styleSheet.cssText = cssStr.join('');
			} else {
				styleTag.innerHTML = cssStr.join('');
			}
		}else{
			styleTag = _d.createElement('style');
		
			_d.getElementsByTagName('head')[0].appendChild(styleTag);
			styleTag.setAttribute('id', styleId);
			styleTag.setAttribute('type', 'text/css');

			if (styleTag.styleSheet) {
				styleTag.styleSheet.cssText = cssStr.join('');
			} else {
				styleTag.appendChild(document.createTextNode(cssStr.join('')));
			}
		}
		styleTag = null; 
	}
	/**
     * @method _setThead
     * @description 헤더 label 셋팅.
     */
	,_setThead : function (){
		var _this = this
			,opt = _this.options;
			
		var tci = opt.tColItem
			,thg = opt.theadGroup
			,fixedColIdx = opt.headerOptions.colFixedIndex
			,gridElementWidth =_this.config.body.width-(_this.config.gridWidth.aside+opt.scroll.verticalWidth)
			,tciItem,thgItem, rowItem, headItem
			,headGroupInfo = [] ,groupInfo = [], rowSpanNum = {}, colSpanNum = {}
			,leftHeaderGroupInfo = [] ,leftGroupInfo = [], rowSpanNum = {}, colSpanNum = {};
		
		if(thg.length < 1){
			thg.push(tci);
		}
		
		var tmpThgIdx=0,tmpColIdx=0,tmpThgItem , currentColSpanIdx=0  , beforeColSpanIdx=0 ;
		var sortHeaderInfo = {};
		for(var i=0,j=0 ;i <thg.length; i++ ){
			thgItem = thg[i];
			groupInfo = [];
			leftGroupInfo = [];
			tmpColIdx = 0;
			tmpThgIdx = 0;
			currentColSpanIdx=0
			colSpanNum[i] = {};
			beforeColSpanIdx = -1 ; 
			
			for(j=0; j<tci.length; j++) {
				tciItem = tci[j];
				
				if(i != 0) currentColSpanIdx = colSpanNum[i-1][j]||currentColSpanIdx; 

				//console.log('====================currentColSpanIdx : ', currentColSpanIdx)
				
				if(tmpColIdx > j || tmpThgIdx >= thgItem.length){
					headItem = {r:i,c:j,view:false};
				}else{
					headItem=thgItem[tmpThgIdx];

					tmpColIdx +=(headItem['colspan'] || 1);
					headItem['r'] = i;
					headItem['c'] = j;
					headItem['view'] = true;
					headItem['sort'] = tciItem.sort===true ? true : opt.headerOptions.sort;
					headItem['colSpanIdx'] = beforeColSpanIdx+1;
					headItem['colspanhtm'] = 'scope="col"';
					headItem['rowspanhtm'] ='';
					headItem['label'] = headItem.label ? headItem.label : tciItem.label;
					
					if(headItem.colspan){
						headItem['colSpanIdx'] = headItem['colSpanIdx']+headItem.colspan-1;
						headItem['colspanhtm'] = ' scope="colgroup" colspan="'+headItem.colspan+'" ';
						
						colSpanNum[i][j]= j+headItem.colspan; 
					}

					if(currentColSpanIdx > j){
						headItem['view'] = true;	
						tmpThgIdx +=1;
					}else{
						if(rowSpanNum[j] && rowSpanNum[j] >= i){
							headItem['view'] = false;
						}else{
							tmpThgIdx +=1;		
						}
					}

					if(headItem.rowspan){
						headItem['rowspanhtm'] += ' scope="col" rowspan="'+headItem.rowspan+'" ';
						rowSpanNum[j] = i+ headItem.rowspan -1;
					}
					beforeColSpanIdx = headItem['colSpanIdx'];
				}
				//console.log(i, 'headItem', headItem)
				//console.log(j+' ;; '+rowSpanNum[j] +' : '+headItem.view, headItem);

				if(headItem.view==true){
					sortHeaderInfo[j] = {r:i,key:tciItem.key}
					headItem['resizeIdx'] =headItem.colSpanIdx; 

					if(headItem.c < fixedColIdx){
						var leftHeadItem = objectMerge({},headItem);
						if(headItem.colspan > 0  && headItem.colSpanIdx+1 >fixedColIdx){
							headItem['colspanhtm'] = ' scope="colgroup" colspan="'+((headItem.colSpanIdx+1)-fixedColIdx)+'" ';
							groupInfo.push(headItem);
							leftHeadItem['resizeIdx'] = fixedColIdx-1;
							leftHeadItem['colspanhtm'] = ' scope="colgroup" colspan="'+(fixedColIdx -leftHeadItem.c)+'" ';
						}
						
						leftGroupInfo.push(leftHeadItem);	
					}else{
						groupInfo.push(headItem)
					}
				}
			}
			headGroupInfo.push(groupInfo);
			leftHeaderGroupInfo.push(leftGroupInfo)
		}

		for(var _ikey in sortHeaderInfo){
			var tmpHgi = headGroupInfo[sortHeaderInfo[_ikey].r][_ikey]
			if(typeof tmpHgi !=='undefined'){
				tmpHgi['isSort'] =(tmpHgi.sort===true?true:false); 
				headGroupInfo[sortHeaderInfo[_ikey].r][_ikey] = tmpHgi;
			}

			tmpHgi = leftHeaderGroupInfo[sortHeaderInfo[_ikey].r][_ikey]
			if(typeof tmpHgi !=='undefined'){
				tmpHgi['isSort'] =(tmpHgi.sort===true?true:false); 
				leftHeaderGroupInfo[sortHeaderInfo[_ikey].r][_ikey] = tmpHgi;
			}
		}

		_this.config.headerInfo = headGroupInfo;
		_this.config.headerLeftInfo = leftHeaderGroupInfo;

		var colWidth = Math.floor((gridElementWidth)/tci.length);
		
		var viewAllLabel= (opt.headerOptions.viewAllLabel ===true ?true :false); 

		for(var j=0; j<tci.length; j++){
			var tciItem = opt.tColItem[j];

			if(viewAllLabel){
				tciItem.width = tciItem.label.length* 10 + 1; 
			}else{
				tciItem.width = isNaN(tciItem.width) ? 0 :tciItem.width; 
			}
			
			tciItem.width = Math.max(tciItem.width, opt.headerOptions.colMinWidth);
			
			tciItem['_alignClass'] = tciItem.align=='right' ? 'ar' : (tciItem.align=='center'?'ac':'al');
			opt.tColItem[j] = tciItem;

			if(_this._isFixedPostion(j)){
				_this.config.gridWidth.left +=tciItem.width;
			}else{
				_this.config.gridWidth.main +=tciItem.width;
			}
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
			,opt = _this.options
			,_gw = _this.config.body.width
			,tci = opt.tColItem
			,tciLen = tci.length;

		//console.log(_this.config.gridWidth.main)
		
		var _totW = _this.config.gridWidth.aside+_this.config.gridWidth.left+_this.config.gridWidth.main+opt.scroll.verticalWidth;

			
		if(opt.headerOptions.colWidthFixed !== true){
			var resizeFlag = _totW  < _gw ? true : false;
			var remainderWidth = (_gw -_totW)/tciLen
				, lastSpaceW = (_gw -_totW)-remainderWidth *tciLen;

			if(opt.autoResize.responsive ===true){
				resizeFlag = true; 

				if(_this.config.body.width != 0){
					var resizeW = (_gw-(_this.config.drawBeforeData.bodyWidth||0)); 
					remainderWidth  = resizeW/tciLen;
					lastSpaceW =resizeW - remainderWidth*tciLen;
				}
			}

			if(resizeFlag){
				var leftGridWidth = 0, mainGridWidth=0;
				for(var j=0; j<tciLen; j++){
					
					opt.tColItem[j].width += remainderWidth;
					opt.tColItem[j].width = Math.max(opt.tColItem[j].width, opt.headerOptions.colMinWidth);

					if(_this._isFixedPostion(j)){
						leftGridWidth +=opt.tColItem[j].width;
					}else{
						mainGridWidth +=opt.tColItem[j].width;
					}
				}
				opt.tColItem[tciLen-1].width +=lastSpaceW;
				_this.config.gridWidth.left = leftGridWidth
				_this.config.gridWidth.main =mainGridWidth+lastSpaceW; 
			}
		}

		_this.config.body.height = opt.height;
		
		//console.log(_this.config.gridWidth, gridElementWidth, _w );
	}
	/**
     * @method _setTfoot
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
	,_getColGroup :function (id , type){
		var _this = this
			,opt = _this.options
			,tci = opt.tColItem
			,thiItem;
		var strHtm = [];

		var startCol =0, endCol = tci.length; 

		if(type=='left'){
			endCol = _this.options.headerOptions.colFixedIndex;
		}else if(type=='cont'){
			startCol = _this.options.headerOptions.colFixedIndex;
		}
		
		strHtm.push('<colgroup>');
		
		for(var i=startCol ;i <endCol; i++){
			thiItem = tci[i];
			var tmpStyle = [];
			tmpStyle.push('width:'+thiItem.width+'px;');
			if(thiItem.hidden===true){
				tmpStyle.push('display:none;');
			}
			
			strHtm.push('<col id="'+id+i+'" style="'+tmpStyle.join('')+'" />');
		}

		strHtm.push('</colgroup>');
		
		return strHtm.join('');	
	}
	/**
     * @method setData
	 * @param data {Array} - 데이타
	 * @param gridMode {String} - 그리드 모드 
     * @description 데이타 그리기
     */
	,setData :function (pdata, gridMode){
		var _this = this
			,opt = _this.options
			,tci = opt.tColItem;
		var data = pdata;
		var pageInfo = opt.page;

		gridMode = gridMode||'reDraw';
		if(!$.isArray(pdata)){
			data = pdata.items;
			pageInfo = pdata.page; 
		}

		if(data){
			_this.options.tbodyItem = data
		}

		if(gridMode =='init'){
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
		}

		if(gridMode=='reDraw'){
			_this.config.select = {isSelect :false, isMouseDown:false,startIdx : 0,endIdx : 0, startRow : 0 ,startCol : 0, endRow:0, endCol :0};
			_this.calcDimension('reDraw');
			_this.config.drawBeforeData = {}; // 이전 값을 가지고 있기 위한 객체
		}

		_this.drawGrid(gridMode,true);
		_this.setPage(pageInfo);
		
	}
	,setPage : function (pageInfo){
		var _this =this;

		if(pageInfo === false){
			$('#'+_this.prefix+'pubGrid-footer-wrapper').hide();
			return ; 
		}

		if(typeof pageInfo ==='object'){
			_this.pageNav(pageInfo);
		}
	}
	,initStyle : function (){
	
		var _this = this
			,opt = _this.options
			,tci = opt.tColItem
			,thiItem;

		var strCss = [];
		for(var i=0 ;i <tci.length; i++){
			thiItem = tci[i];
			var tmpStyle = [];
			tmpStyle.push('width:'+thiItem.width+'px;');
			if(thiItem.hidden===true){
				tmpStyle.push('display:none;');
			}

			strCss.push('#'+_this.prefix+'_pubGrid .table-column-'+i+'{'+tmpStyle.join('')+'}');
		}

		var d = document;
        var tag = d.createElement('style');

        d.getElementsByTagName('head')[0].appendChild(tag);
        tag.setAttribute('type', 'text/css');

        if (tag.styleSheet) {
            tag.styleSheet.cssText = strCss.join('');
        } else {
            tag.appendChild(document.createTextNode(strCss.join('')));
        }
	}
	/**
     * @method getTemplateHtml
     * @description header html 
     */
	,getTemplateHtml : function (){
		var _this = this;

		return '<div id="'+_this.prefix+'_pubGrid" class="pubGrid pubGrid-noselect"  style="overflow:hidden;width:'+_this.config.body.width+'px;">'
			+' 	<div id="'+_this.prefix+'_container" class="pubGrid-container" style="overflow:hidden;">'
			+' 		<div class="pubGrid-header-container-warpper">'
			+' 		  <div id="'+_this.prefix+'_headerContainer" class="pubGrid-header-container">'
			+' 			<div class="pubGrid-header-aside"><table class="pubGrid-header-aside-cont" style="width:'+_this.config.gridWidth.aside+'px;">#theaderAsideHtmlArea#</table></div>'
			+' 			<div class="pubGrid-header-left"><table class="pubGrid-header-left-cont" style="width:'+_this.config.gridWidth.left+'px;">#theaderLeftHtmlArea#</table></div>'
			+' 			<div class="pubGrid-header">'
			+'				<div class="pubGrid-header-cont-wrapper" style="position:relative;"><table style="width:'+_this.config.gridWidth.main+'px;" class="pubGrid-header-cont" onselectstart="return false">#theaderHtmlArea#</table></div>'
			+' 			</div>'
			+' 		  </div>'
			+' 		</div>'		

			+' 		<div id="'+_this.prefix+'_bodyContainer" class="pubGrid-body-container-warpper">'
			+' 			<div class="pubGrid-body-container">'
			+' 				<div class="pubGrid-body-aside"><table style="width:'+_this.config.gridWidth.aside+'px;" class="pubGrid-body-aside-cont"></table></div>'
			+' 				<div class="pubGrid-body-left"><table style="width:'+_this.config.gridWidth.left+'px;" class="pubGrid-body-left-cont"></table></div>'
			+' 				<div class="pubGrid-body">'
			+'					<div class="pubGrid-body-cont-wrapper" style="position:relative;"><table style="width:'+_this.config.gridWidth.main+'px;" class="pubGrid-body-cont"></table></div>'
			+'				</div>'
			+' 			</div>'
			+'			<div class="pubGrid-empty-msg-area"><span class="pubGrid-empty-msg"><i class="pubGrid-icon-info"></i><span class="empty-text">no-data</span></span></div>'
			+' 		</div>'		
			+' 		<div id="'+_this.prefix+'_footerContainer" class="pubGrid-footer-container">'
			+' 			<div class="pubGrid-footer-left"></div>'
			+' 			<div class="pubGrid-footer">'
			+' 				<div class="pubGrid-footer-cont-wrapper" style="position:relative;"><table style="width:'+_this.config.gridWidth.main+'px;" class="pubGrid-footer-cont"></table></div>'
			+' 			</div>'
			+' 		</div>'
			+' 		<div id="'+_this.prefix+'_vscroll" class="pubGrid-vscroll">'
			
			+' 			<div class="pubGrid-vscroll-bar-area"><div class="pubGrid-vscroll-bar"></div></div>'
			//+' 			<div class="pubGrid-vscroll-up">^</div>'
			//+' 			<div class="pubGrid-vscroll-down">V</div>'
			+' 		</div>'
			+' 		<div id="'+_this.prefix+'_hscroll" class="pubGrid-hscroll">'
			+'			<div class="pubGrid-scroll-aside-area" style="width:41px;"></div>'
			+' 			<div class="pubGrid-hscroll-bar-area"><div class="pubGrid-hscroll-bar"></div></div>'
			//+' 			<div class="pubGrid-hscroll-left"><</div>'
			//+' 			<div class="pubGrid-hscroll-right">></div>'
			+' 		</div> '
			+' 	</div>'
			+' 	<div id="'+_this.prefix+'_navigation" class="pubGrid-navigation"><div class="pubGrid-page-navigation"></div><div id="'+_this.prefix+'_status" class="pubgGrid-count-info"></div>'

			+' 	</div>'
			+' </div>';

	}
	,getTbodyAsideHtml : function (mode){
		var _this =this; 

		var firstFlag = true; 
		
		var strHtm = [], colGroupHtm=[];
		
		var items =_this.config.aside.items;


		var tmpeElementBody = _this.element.body.find('.pubGrid-body-aside-cont')
		
		for(var i =0 ; i < _this.config.scroll.maxViewCount; i++){
			if(tmpeElementBody.find('[rowinfo="'+i+'"]').length > 0){
				if(i > _this.config.scroll.viewCount){
					tmpeElementBody.find('[rowinfo="'+i+'"]').hide();
				}else{
					tmpeElementBody.find('[rowinfo="'+i+'"]').show();
				}
			}else{
				strHtm.push('<tr class="pub-body-tr" rowinfo="'+i+'">');

				for(var j=0 ;j < items.length; j++){
					var item = items[j];
					if(firstFlag){
						colGroupHtm.push('<col style="width:'+item.width+'px;" />');
					}
					
					strHtm.push('<td scope="col" class="pub-body-aside-td" data-aside-position="'+i+','+item.key+'"><div class="aside-content"></div></td>');
				}
				strHtm.push('</tr>');
				firstFlag = false; 
			}
		}
		
		if(mode=='init'){
			var bodyHtm = '';
			bodyHtm += '<colgroup>'+colGroupHtm.join('')+'</colgroup>';
			bodyHtm += '<tbody class="pubGrid-body-tbody" data-start-idx="0">'+strHtm.join('')+'</tbody>';
			tmpeElementBody.empty().html(bodyHtm);
		}else{
			strHtm = strHtm.join('');
			if(strHtm != ''){
				tmpeElementBody.append(strHtm);
			}
		}
	}
	/**
     * @method getTbodyHtml
	 * @description body html  만들기
     */
	,getTbodyHtml : function(mode){
		this.getTbodyAsideHtml(mode);

		function bodyHtm (_this , mode , type){
			var clickFlag = false
				,tci = _this.options.tColItem
				,colLen = tci.length
				,thiItem;
			
			var strHtm = [];
			
			var contTypeClass = type=='left'?'.pubGrid-body-left-cont':'.pubGrid-body-cont';
			//console.log(mode, _this.config.scroll.maxViewCount);
			var tmpeElementBody =  _this.element.body.find(contTypeClass); 
			
			var startCol =0, endCol = tci.length; 

			if(type=='left'){
				endCol = _this.options.headerOptions.colFixedIndex;
			}else if(type=='cont'){
				startCol = _this.options.headerOptions.colFixedIndex;
			}
			
			for(var i =0 ; i < _this.config.scroll.maxViewCount; i++){

				if(tmpeElementBody.find('[rowinfo="'+i+'"]').length > 0){
					if(i >= _this.config.scroll.viewCount){
						tmpeElementBody.find('[rowinfo="'+i+'"]').hide();
					}else{
						tmpeElementBody.find('[rowinfo="'+i+'"]').show();
					}
				}else{
					strHtm.push('<tr class="pub-body-tr '+((i%2==0)?'tr0':'tr1')+'" rowinfo="'+i+'">');

					for(var j=startCol ;j < endCol; j++){
						thiItem = tci[j];
						clickFlag = thiItem.colClick;
						
						strHtm.push('<td scope="col" class="pub-body-td '+(thiItem.hidden===true ? 'pubGrid-disoff':'')+'" data-grid-position="'+i+','+j+'"><div class="pub-content pub-content-ellipsis '+ (clickFlag?'pub-body-td-click':'') +'"></div></td>');
					}
					strHtm.push('</tr>');
				}
			}
			
			if(mode=='init'){
				var bodyHtm = '';
				bodyHtm +=_this._getColGroup(_this.prefix+'colbody', type);
				bodyHtm += '<tbody class="pubGrid-body-tbody" data-start-idx="0">'+strHtm.join('')+'</tbody>';

				tmpeElementBody.empty().html(bodyHtm);
				
			}else{
				strHtm = strHtm.join('');
				if(strHtm != ''){
					tmpeElementBody.append(strHtm);
				}
				return true; 
			}
		}

		if(this.options.tbodyItem.length > 0){
			bodyHtm (this,mode,'left');
			return bodyHtm (this,mode, 'cont');	
		}

		return false; 
		
	}
	/**
     * @method valueFormatter
	 * @param  thiItem {Object} header col info
	 * @param  item {Object} row 값
     * @description foot 데이타 셋팅
     */
	,valueFormatter : function (_idx ,thiItem, rowItem, addEle, returnFlag){
		
		var type = thiItem.type || 'string';
		
		var itemVal = rowItem[thiItem.key];
		var tmpFormatter={}; 		
		if(type == 'money' || type == 'number'){
			tmpFormatter = this.options.formatter[type];
		}

		if($.isFunction(thiItem.formatter)){
			itemVal = thiItem.formatter.call(null,{idx : _idx , colInfo:thiItem, item: rowItem , formatter : function (val, fixed , prefix , suffix){
				fixed = typeof fixed ==='undefined'?tmpFormatter.fixed :fixed;
				prefix = typeof prefix ==='undefined'?tmpFormatter.prefix :prefix;
				suffix = typeof suffix ==='undefined'?tmpFormatter.suffix :suffix;
				return util.formatter[type](val, fixed ,prefix, suffix); 
			}});
		}else{
			if(this.options.useDefaultFormatter===true){
				if(type == 'money'){
					itemVal = util.formatter[type](itemVal, tmpFormatter.fixed , tmpFormatter.prefix ,tmpFormatter.suffix);
				}else if(type == 'number'){
					itemVal = util.formatter[type](itemVal , tmpFormatter.fixed , tmpFormatter.prefix ,tmpFormatter.suffix);
				}
			}
		}
		if(returnFlag){
			return itemVal;
		}else{
			if(thiItem.render=='html'){
				addEle.innerHTML = itemVal;
			}else{
				addEle.textContent = itemVal;	
			}
		}
	}
	/**
     * @method _setTbodyAppend
     * @description tbody 추가 , 삭제 .
     */
	,_setTbodyAppend : function (mode){
		 return ; 
	}
	,theadHtml : function(type){
		var _this = this
			,cfg = _this.config
			,tci = _this.options.tColItem
			,hederOpt=_this.options.headerOptions;

		var strHtm = [];

		var headerInfo = cfg.headerInfo

		if(type=='left'){
			headerInfo =  cfg.headerLeftInfo
		}
		
		strHtm.push(_this._getColGroup(_this.prefix+'colHeader' , type));
		
		strHtm.push('<thead>');
		if(headerInfo.length > 0 && hederOpt.view){
			var ghArr, ghItem;
				
			for(var i =0 ; i < headerInfo.length; i++){
				ghArr = headerInfo[i];
				strHtm.push('<tr class="pub-header-tr">');
				for(var j=0 ; j <ghArr.length; j++){
					ghItem = ghArr[j];
					if(ghItem.view){
						strHtm.push('	<th '+ghItem.colspanhtm+' '+ghItem.rowspanhtm+' data-header-info="'+i+','+j+'" class="pubGrid-header-th" '+(ghItem.style?' style="'+ghItem.style+'" ':'')+'>');
						strHtm.push('		<div class="label-wrapper">');
						strHtm.push('			<div class="pub-header-cont outer '+(ghItem.isSort===true?'sort-header':'')+'" col_idx="'+j+'"><div class="inner"><div class="centered">'+ghItem.label+'</div></div></div>');
						strHtm.push('			<div class="pub-header-resizer" data-resize-idx="'+ghItem.resizeIdx+'"></div>');
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
	,_isFixedPostion : function (idx, position){
		position = position || 'l';

		return idx < this.options.headerOptions.colFixedIndex ? true : false ; 
	}
	,theadAsideHtml : function (){
		var _this = this
		
		var strHtm = [], colGroupHtm=[];
		
		var items =_this.config.aside.items;

		strHtm.push('<thead>');
		strHtm.push('<tr class="pub-header-tr">');
		for(var j=0 ;j < items.length; j++){
			var item = items[j];
			colGroupHtm.push('<col style="width:'+item.width+'px;" />');

			strHtm.push('	<th>');
			strHtm.push('		<div class="aside-label-wrapper">'+item.name+'</div>');
			strHtm.push('	</th>');					
		}
		strHtm.push('</tr>');
		strHtm.push('</thead>');
		return '<colgroup>'+colGroupHtm.join('')+'</colgroup>' +strHtm.join('');
	}
	/**
     * @method drawGrid
	 * @param  type {String} 그리드 타입.
     * @description foot 데이타 셋팅
     */
	,drawGrid : function (drawMode, unconditionallyFlag){
		var _this = this
			,tci = _this.options.tColItem
			,tbi = _this.options.tbodyItem
			,hederOpt=_this.options.headerOptions;

		if(drawMode =='init'){

			// header html 만들기
			var templateHtm = _this.getTemplateHtml();
			templateHtm = templateHtm.replace('#theaderAsideHtmlArea#',_this.theadAsideHtml('aside'));
			templateHtm = templateHtm.replace('#theaderHtmlArea#',_this.theadHtml('cont'));
			templateHtm = templateHtm.replace('#theaderLeftHtmlArea#',_this.theadHtml('left'));

			_this.gridElement.empty().html(templateHtm);
			_this.element.pubGrid = $('#'+_this.prefix +'_pubGrid');
			_this.element.hidden = $('#'+_this.prefix +'_hiddenArea');
			
			_this.element.container = $('#'+_this.prefix+'_container');
			_this.element.left = $('#'+_this.prefix+'_left');
			_this.element.header= $('#'+_this.prefix+'_headerContainer');
			_this.element.body = $('#'+_this.prefix +'_bodyContainer');
			_this.element.footer = $('#'+_this.prefix +'_footerContainer');

			_this.element.navi = $('#'+_this.prefix+'_navigation');
			_this.element.status = $('#'+_this.prefix+'_status');
			_this.element.vScrollBar = $('#'+_this.prefix+'_vscroll .pubGrid-vscroll-bar');
			_this.element.hScrollBar = $('#'+_this.prefix+'_hscroll .pubGrid-hscroll-bar');
			
			_this.setElementDimensionAndMessage();
			_this.calcDimension('init');

			// resize 설정
			_this._initHeaderEvent();
			_this._headerResize(hederOpt.resize.enabled);
			_this.scroll();
			_this._initBodyEvent();
			_this._setBodyEvent();

			_this.getTbodyHtml('init');
		}

		if(tbi.length < 1){
			_this.element.body.find('.pubGrid-body-cont-wrapper').hide();
			_this.element.body.find('.pubGrid-empty-msg-area').show();
			return ; 
		}else{
			_this.element.body.find('.pubGrid-body-cont-wrapper').show();
			_this.element.body.find('.pubGrid-empty-msg-area').hide();
		}
		
		var itemIdx = _this.config.scroll.viewIdx;
		var viewCount = _this.config.scroll.viewCount;

		var startCol=this.config.scroll.startCol 
			, endCol=this.config.scroll.endCol;
		
		var tbiItem , thiItem;
		var viewCount = _this.config.scroll.viewCount; 
		
		var asideItem =_this.config.aside.items; 

		var selectCell = _this.getSelectCellInfo(_this.config.select);

		var colFixedIndex = this.options.headerOptions.colFixedIndex;

		startCol = startCol < colFixedIndex ? colFixedIndex : startCol;

		//var selectFlag = (drawMode != 'init'&&selectCell.startIdx <= itemIdx && itemIdx <= selectCell.endIdx) ?true :false;
		var selectFlag = (_this.config.select.isSelect ===true) ?true :false;

		function setSelectCell(row , col, addEle){
			addEle.parentElement.classList.remove( 'col-active' );
			if(selectFlag){
				if(selectCell.startIdx <=row && row <= selectCell.endIdx
					&& selectCell.startCol <=col && col <= selectCell.endCol ){
					addEle.parentElement.classList.add( 'col-active' );
				}
			}

			return false; 
		}

		for(var i =0 ; i < viewCount; i++){
			tbiItem = tbi[itemIdx];
		
			var addEle;
			for(var j =0 ; j < asideItem.length ;j++){
				var tmpItem = asideItem[j]; 
				var rowCol = i+','+tmpItem.key;

				addEle =$pubSelect('#'+_this.prefix+'_bodyContainer .pubGrid-body-aside-cont').querySelector('[data-aside-position="'+rowCol+'"]>.aside-content');

				if(tmpItem.key == 'number'){
					addEle.textContent = (itemIdx+1);	
				}else if(tmpItem.key == 'chceck'){
					addEle.innerHTML = '<input type="checkbox" class="row-check"/>';	
				}else if(tmpItem.key == 'modify'){
					addEle.innerHTML = 'V';
				}
			};
			
			if(drawMode != 'hscroll'){
				for(var j=0; j < colFixedIndex ; j++){
					
					addEle =$pubSelect('#'+_this.prefix+'_bodyContainer .pubGrid-body-left-cont').querySelector('[data-grid-position="'+(i+','+j)+'"]>.pub-content');
					this.valueFormatter( i, tci[j],tbiItem , addEle); 
					setSelectCell(itemIdx , j ,  addEle);
					
					addEle = null; 
				}
			}
			
			for(var j=startCol ;j <= endCol; j++){
				//var addEle = _this.element.tdEle[rowCol] = $pubSelect('#'+_this.prefix+'_bodyContainer .pubGrid-body-tbody').querySelector('[data-grid-position="'+rowCol+'"]>.pub-content')
				
				addEle =$pubSelect('#'+_this.prefix+'_bodyContainer .pubGrid-body-cont').querySelector('[data-grid-position="'+(i+','+j)+'"]>.pub-content');
				this.valueFormatter( i, tci[j],tbiItem , addEle);
				setSelectCell(itemIdx , j ,  addEle);
				addEle = null; 
			}
			itemIdx++;
		}

		_this._statusMessage(viewCount);
		
	}
	/**
     * @method setElementDimensionAndMessage
	 * @description 그리드 element 수치 및 메시지 처리.
     */
	,setElementDimensionAndMessage : function (){
		this.config.header.height = this.element.header.outerHeight();

		this.config.navi.height = 0;

		if(this.options.page !== false){
			this.element.navi.show();
			this.config.navi.height = this.element.navi.outerHeight();
		}
		
		if(false){ //todo footer 구현시 처리. 
			this.config.footer.height = this.element.footer.outerHeight();
			this.element.footer.addClass('on');
		}
		var pubGridClass = '';
		if(this.config.aside.items.length > 0){
			pubGridClass +=' aside-cont-on';
		}

		if(this._isFixedPostion(0)){
			pubGridClass +=' left-cont-on';
		}


		if(this.options.tbodyItem.length < 1){
			pubGridClass +=' pubGrid-no-item';
		}

		this.element.pubGrid.addClass(pubGridClass)
		this.element.header.find('.pubGrid-header-aside-cont').css('line-height',this.config.header.height+'px').css('height',this.config.header.height+'px')
		this.element.header.find('.aside-label-wrapper').css('height',this.config.header.height-1+'px');
			
		$('#'+this.prefix+'_vscroll').css('width', this.options.scroll.verticalWidth);
		$('#'+this.prefix+'_hscroll').css('height', this.options.scroll.horizontalHeight).css('padding-left', (this.options.headerOptions.displayLineNumber ===true? 40 :0));
		
		// empty message
		if($.isFunction(this.options.message.empty)){
			this.element.body.find('.pubGrid-empty-msg').empty().html(this.options.message.empty());	
		}else{
			this.element.body.find('.pubGrid-empty-msg .empty-text').empty().html(this.options.message.empty);
		}
		
		this.calcViewCol(0);

	}
	/**
     * @method calcDimension
	 * @param  type {String}  타입.
	 * @param  opt {Object}  옵션.
     * @description 그리드 수치 계산 . 
     */
	, calcDimension : function (type , opt){
		var _this = this; 
		
		_this.config.gridWidth.total = _this.config.gridWidth.aside+_this.config.gridWidth.left+ _this.config.gridWidth.main;

		_this.config.drawBeforeData.bodyHeight = _this.config.body.height; 
		
		opt = opt||{height : (_this.options.height =='auto' ? _this.gridElement.parent().height() : _this.config.body.height )}; 
		opt = $.extend(true, {width : _this.gridElement.innerWidth(), height : _this.gridElement.parent().height()},opt);
		
		if(type =='init'  ||  type =='resize'){
			_this.element.pubGrid.css('height',opt.height+'px');
			_this.element.pubGrid.css('width',opt.width+'px');
		}

		_this.config.body.width = opt.width;
		_this.config.body.height = opt.height;
		
		var mainHeight = opt.height - this.config.navi.height;
		_this.element.container.css('height',mainHeight);

		var bodyH = mainHeight - this.config.header.height - this.config.footer.height - (hScrollFlag?this.options.scroll.horizontalHeight:0)
			, itemTotHeight = _this.options.tbodyItem.length * _this.config.rowHeight
			, vScrollFlag = itemTotHeight > bodyH ? true :false
			, bodyW = (_this.config.body.width- (vScrollFlag?this.options.scroll.verticalWidth :0))
			, hScrollFlag = _this.config.gridWidth.total > bodyW  ? true : false;
			
		_this.element.header.find('.pubGrid-header-cont').css('width',(_this.config.gridWidth.main)+'px');
		_this.element.header.find('.pubGrid-header-left-cont').css('width',(_this.config.gridWidth.left)+'px');
		
		_this.element.body.find('.pubGrid-body-cont').css('width',(_this.config.gridWidth.main)+'px');
		_this.element.body.find('.pubGrid-body-left-cont').css('width',(_this.config.gridWidth.left)+'px');
		_this.element.body.find('.pubGrid-body-left').css('height',(opt.height)+'px');
		_this.element.body.find('.pubGrid-empty-msg-area').css('line-height',(bodyH)+'px');

		//console.log(vScrollFlag,mainHeight , this.config.header.height , this.config.footer.height ,hScrollFlag, (hScrollFlag?this.options.scroll.horizontalHeight:0))
		
		var beforeViewCount = _this.config.scroll.viewCount ; 
		_this.config.scroll.viewCount = itemTotHeight > bodyH ? Math.ceil(bodyH / this.config.rowHeight) : _this.options.tbodyItem.length;
		_this.config.scroll.overflowVal = bodyH % this.config.rowHeight; 
		
		var topVal = 0 ; 
		if(vScrollFlag){
			_this.config.scroll.vUse = true;
			$('#'+_this.prefix+'_vscroll').css('padding-bottom',(hScrollFlag?(_this.options.scroll.horizontalHeight-1):0));
			$('#'+_this.prefix+'_vscroll').show();
			
			var scrollH = $('#'+_this.prefix+'_vscroll').find('.pubGrid-vscroll-bar-area').height();
			var barHeight = (bodyH*(bodyH/(itemTotHeight)*100))/100; 
			barHeight = barHeight < 25 ? 25 :barHeight;	
			_this.config.scroll.vBarHeight = barHeight; 
			_this.config.scroll.verticalHeight = scrollH - barHeight;
			_this.config.scroll.oneRowMove = _this.config.scroll.verticalHeight/(_this.options.tbodyItem.length-this.config.scroll.viewCount);
						
			topVal = _this.config.scroll.verticalHeight* _this.config.scroll.vBarPosition/100;
			_this.element.vScrollBar.css('height',barHeight);

		}else{
			_this.config.scroll.vUse = false; 
			$('#'+_this.prefix+'_vscroll').hide();
		}

		var leftVal =0;
		if(hScrollFlag){
			_this.config.scroll.hUse = true; 
			$('#'+_this.prefix+'_hscroll').css('padding-right',(vScrollFlag?_this.options.scroll.verticalWidth:0));
			$('#'+_this.prefix+'_hscroll').show();
			var hscrollW = bodyW-_this.config.gridWidth.aside; 
			var barWidth = (hscrollW*(hscrollW/_this.config.gridWidth.total*100))/100; 
			barWidth = barWidth < 25 ? 25 :barWidth;
			_this.config.scroll.hBarWidth = barWidth; 
			_this.config.scroll.horizontalWidth =$('#'+_this.prefix+'_hscroll').find('.pubGrid-hscroll-bar-area').width() - barWidth;
			_this.config.scroll.oneColMove = _this.config.gridWidth.total/_this.config.scroll.horizontalWidth;
			leftVal = _this.config.scroll.horizontalWidth* _this.config.scroll.hBarPosition/100;
			_this.element.hScrollBar.css('width',barWidth)
		}else{
			_this.config.scroll.hUse= false; 
			$('#'+_this.prefix+'_hscroll').hide();
		}

		//_this.calcViewCol(leftVal);
		if(_this.config.scroll.maxViewCount <_this.config.scroll.viewCount  ) _this.config.scroll.maxViewCount = _this.config.scroll.viewCount;
		
		var drawFlag =false; 

		if(type !='init' && ( beforeViewCount != _this.config.scroll.viewCount )){
			drawFlag = _this.getTbodyHtml(); 
		}

		if(this.config.scroll.startCol != this.config.scroll.before.startCol || this.config.scroll.before.endCol != this.config.scroll.endCol ){
			drawFlag = true; 
		}

		_this.moveHScroll(leftVal, false);

		if(beforeViewCount !=0 ){
			_this.moveVScroll(topVal, false);
			
			if(type !='reDraw' && drawFlag){
				_this.drawGrid();
			}
		}
	}
	/**
     * @method scroll event 처리.
     * @description 스크롤 컨트롤.
     */
	,scroll : function (){
		var _this = this
			,_conf = _this.config;
			
		_this.element.body.off('mousewheel DOMMouseScroll');
		_this.element.body.on('mousewheel DOMMouseScroll', function(e) {
			e.preventDefault();
			var oe = e.originalEvent;
			var delta = 0;
		
			if (oe.detail) {
				delta = oe.detail * -40;
			}else{
				delta = oe.wheelDelta;
			};
		
			//delta > 0--up
			if(_this.config.scroll.vUse){
				_this.moveVScroll(_this.config.scroll.top+((delta > 0?-1:1) * _this.config.scroll.oneRowMove));
			}else{
				if(_this.config.scroll.hUse){
					_this.moveHScroll(_this.config.scroll.left+((delta > 0?-1:1) * _this.config.scroll.oneColMove));
				}
			}
		});
		
		var scrollTimer , mouseDown = false;
		
		$('#'+_this.prefix+'_vscroll .pubGrid-vscroll-bar-area').off('mousedown touchstart mouseup touchend');
		$('#'+_this.prefix+'_vscroll .pubGrid-vscroll-bar-area').on('mousedown touchstart',function(e) {
			mouseDown = true;
			var evtY = e.offsetY;
			var upFlag =evtY> _this.config.scroll.top ? false :true;
			var loopcount =5;
			
			function scrollMove(pEvtY, pTop, vBarHeight, oneRowMove){
				clearTimeout(scrollTimer);
				
				pTop= pTop+((upFlag?-1:1) * (oneRowMove *loopcount));

				if(upFlag){
					if( pEvtY >= pTop ){
						mouseDown = false
						pTop = pEvtY;
					}
				}else{
					if(pEvtY <= (pTop +vBarHeight)){
						mouseDown = false
						pTop = pEvtY-vBarHeight;
					}
				}

				_this.moveVScroll(pTop);

				if(mouseDown){
					scrollTimer = setTimeout(function() {
						scrollMove(pEvtY, pTop, vBarHeight, oneRowMove);
					}, 200);
				}
			}scrollMove(evtY, _this.config.scroll.top, _this.config.scroll.vBarHeight, _this.config.scroll.oneRowMove);
			
		}).on('mouseup touchend',function(e) {
			mouseDown = false;
			clearTimeout(scrollTimer);
		});
		
		$('#'+_this.prefix+'_hscroll .pubGrid-hscroll-bar-area').off('mousedown touchstart mouseup touchend');
		$('#'+_this.prefix+'_hscroll .pubGrid-hscroll-bar-area').on('mousedown touchstart',function(e) {
			mouseDown = true;
			var evtX = e.offsetX;
			var leftFlag =evtX > _this.config.scroll.left ? false :true;
			var loopcount =10;
			
			function scrollMove(pEvtX, pLeft, hBarWidth, oneColMove){
				clearTimeout(scrollTimer);

				pLeft = pLeft+((leftFlag?-1:1) * (oneColMove*loopcount)); 

				if(leftFlag){
					if( pEvtX >= pLeft ){
						mouseDown = false
						pLeft = pEvtX;
					}
				}else{
					if(pEvtX <= (pLeft +hBarWidth)){
						mouseDown = false
						pLeft = pEvtX-hBarWidth;
					}
				}
				
				_this.moveHScroll(pLeft);
				
				if(mouseDown){
					scrollTimer = setTimeout(function() {
						scrollMove(pEvtX, pLeft, hBarWidth, oneColMove);
					}, 200);
				}
			}scrollMove(evtX,_this.config.scroll.left , _this.config.scroll.hBarWidth, _this.config.scroll.oneColMove);
			
		}).on('mouseup touchend',function(e) {
			mouseDown = false;
			clearTimeout(scrollTimer);
		});
				
		_this.element.hScrollBar.off('touchstart.pubhscroll mousedown.pubhscroll');
		_this.element.hScrollBar.on('touchstart.pubhscroll mousedown.pubhscroll',function (e){
			e.stopPropagation();

			var oe = e.originalEvent.touches;
			var ele = $(this); 
			var data = {};

			data.left = _this.config.scroll.left
			data.pageX = oe ? oe[0].pageX : e.pageX; 

			ele.addClass('active');

			$(document).on('touchmove.pubhscroll mousemove.pubhscroll', function (e){
				_this.horizontalScroll(data, e, 'move');
			}).on('touchend.pubhscroll mouseup.pubhscroll mouseleave.pubhscroll', function (e){
				ele.removeClass('active');
				_this.horizontalScroll(data,e, 'end');
			});

			return true; 
		});
		
		_this.element.vScrollBar.off('touchstart.pubvscroll mousedown.pubvscroll');
		_this.element.vScrollBar.on('touchstart.pubvscroll mousedown.pubvscroll',function (e){
			e.stopPropagation();
			var oe = e.originalEvent.touches;
			var ele = $(this); 
			var data = {};
			data.top= _this.config.scroll.top; 
			data.pageY = oe ? oe[0].pageY : e.pageY; 

			ele.addClass('active');

			$(document).on('touchmove.pubvscroll mousemove.pubvscroll', function (e){
				_this.verticalScroll( data,e , 'move');
			}).on('touchend.pubvscroll mouseup.pubvscroll mouseleave.pubvscroll', function (e){
				ele.removeClass('active');
				_this.verticalScroll(data, e , 'end');
			});

			return true; 
		})
	}
	/**
	* 세로 스크롤
	*/
	,verticalScroll : function (data,e, type){
		var oe = e.originalEvent.touches
		,oy = oe ? oe[0].pageY : e.pageY;

		oy = data.top+(oy - data.pageY);
		
		this.moveVScroll(oy);
		if(type=='end'){
			$(document).off('touchmove.pubvscroll mousemove.pubvscroll').off('touchend.pubvscroll mouseup.pubvscroll mouseleave.pubvscroll');
		}
	}
	/**
	* 세로 스크롤 이동.
	*/
	,moveVScroll : function (topVal, drawFlag){
		
		var beforeEndFlag = this.config.scroll.endFlag; 
		var _topVal = topVal; 
		this.config.scroll.endFlag = false; 
		var barPos = 0 ; 

		if( topVal> 0){
			if(topVal >= this.config.scroll.verticalHeight ){
				topVal = this.config.scroll.verticalHeight;
				this.config.scroll.endFlag = true; 
			}
			barPos = topVal/this.config.scroll.verticalHeight*100; 
		}else {
			topVal = 0;
			barPos = 0 ; 
		}
		
		this._setScrollBarTopPosition(topVal);
		
		var itemIdx =0;

		if(topVal > 0){
			var tmpRowHeight = this.config.rowHeight; 
			itemIdx = topVal/(this.config.scroll.verticalHeight / (this.options.tbodyItem.length-this.config.scroll.viewCount));
			itemIdx  = Math.round(itemIdx); 
		}
		
		//console.log('asdfasdf : ',this.config.scroll.overflowVal, this.config.scroll.verticalHeight, this.config.scroll.endFlag,_topVal,' : ', itemIdx, this.options.tbodyItem.length , tmpRowHeight,this.config.scroll.viewCount )
			
		if(this.config.scroll.endFlag){
			if(this.config.scroll.overflowVal > 0){
				var vHeight = (this.config.scroll.hUse?this.options.scroll.horizontalHeight:0);
				this.element.body.css('margin-top',(tmpRowHeight-this.config.scroll.overflowVal+2+vHeight)*-1); // 아래 조금 띄우기 위에서 +3 해줌. 
			}
		}else{
			if(beforeEndFlag && !this.config.scroll.endFlag){
				//itemIdx+=1;
				this.element.body.css('margin-top',0);
			}
		}
		
		this.config.scroll.vBarPosition = barPos;

		if(drawFlag === false){
			this.config.scroll.viewIdx = itemIdx; 
			return ; 
		}
		
		if(this.config.scroll.viewIdx ==itemIdx) return ;

		this.config.scroll.viewIdx = itemIdx; 

		this.drawGrid('vscroll');
	}
	/**
	* vertical scroll top postion
	*/
	,_setScrollBarTopPosition : function (topVal){
		this.config.scroll.top = topVal; 
		this.element.vScrollBar.css('top', topVal);
	}
	/**
	* 가로 스크롤
	*/
	, horizontalScroll : function (data ,e, type){
		var oe = e.originalEvent.touches
		,ox = oe ? oe[0].pageX : e.pageX;
		ox = data.left+(ox - data.pageX);
		
		this.moveHScroll(ox);

		if(type=='end'){
			$(document).off('touchmove.pubhscroll mousemove.pubhscroll').off('touchend.pubhscroll mouseup.pubhscroll mouseleave.pubhscroll');
		}
	}
	/**
	* 가로 스크롤 이동.
	*/
	,moveHScroll : function (leftVal, drawFlag){
		var vWidth =(this.config.scroll.vUse ? this.options.scroll.verticalWidth :0) +1
			,hw = this.config.scroll.horizontalWidth;
		leftVal = leftVal > 0 ? (leftVal >= hw ? hw : leftVal) : 0 ; 

		var headerLeft  = ((this.config.gridWidth.total - this.config.body.width+vWidth)*(leftVal/hw*100))/100; 

		this._setScrollBarLeftPosition(leftVal);
		this.config.scroll.hBarPosition = leftVal/hw*100; 
		
		this.calcViewCol(headerLeft);

		this.element.header.find('.pubGrid-header-cont-wrapper').css('left','-'+headerLeft+'px');
		this.element.body.find('.pubGrid-body-cont-wrapper').css('left','-'+headerLeft+'px');
		
		if(drawFlag !== false){
			this.drawGrid('hscroll');
		}
		
	}
	/**
	* 가로 스크롤 바 이동 
	*/
	, _setScrollBarLeftPosition : function (leftVal){
		this.config.scroll.left = leftVal; 
		this.element.hScrollBar.css('left',leftVal);
	}
	/**
	* view col 위치 구하기.
	*/
	,calcViewCol : function (leftVal){
		var tci = this.options.tColItem; 
		var gridW = leftVal+this.config.body.width; 
		var itemLeftVal=0;
		var startCol = 0, endCol =tci.length-1;
		var startFlag = true; 
		for(var i =0 ;i <tci.length ;i++){
			var thiItem = tci[i];

			itemLeftVal +=thiItem.width; 
						
			if(startFlag && itemLeftVal > leftVal){
				startCol = i; 
				startFlag = false; 
				continue; 
			}

			if( itemLeftVal >=gridW){
				endCol = i; 
				break; 
			}
		}

		this.config.scroll.before.startCol = this.config.scroll.startCol; // 이전데이타 
		this.config.scroll.before.endCol = this.config.scroll.endCol;

		this.config.scroll.startCol = ( startCol > 0? startCol:0 ); 
		this.config.scroll.endCol = ( endCol >= tci.length? tci.length:endCol );

	}
	,_statusMessage : function (viewCnt){
		var startVal = this.config.scroll.viewIdx +1
			,endVal = isNaN(viewCnt)? (startVal+ this.config.scroll.viewCount) : (startVal+ viewCnt);
		this.element.status.empty().html(this.options.message.pageStatus({
			currStart :startVal
			,currEnd : (endVal-1)
			,total : this.options.tbodyItem.length
		}))
	}
	,_getScrollOverHeight : function (idx , updown){
		return idx* (this.options.bigData.gridCount)* this.config.rowHeight;
	}
	/**
     * @method resizeDraw
     * @description resize 하기
     */
	,resizeDraw :function (opt){
		this.calcDimension('resize',opt);
		
		return ; 
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
		
		if(_this.options.autoResize ===false || _this.options.autoResize.enabled === false) return false; 
		
		var _evt = $.event,
			_special,
			resizeTimeout,
			eventName =  _this.prefix+"pubgridResize"; 

		_special = _evt.special[eventName] = {
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
						event.type = eventName;
						_evt.dispatch.apply( context, args );
					};

				if ( resizeTimeout ) {
					clearTimeout( resizeTimeout );
				}

				execAsap ?
					dispatch() :
					resizeTimeout = setTimeout( dispatch, _special.threshold );
			},
			threshold: _this.options.autoResize.threshold
		};
		$(window).off(eventName);
		$(window).on(eventName, function( event ) {
			_this.resizeDraw();
		});
	}
	/**
     * @method getItems
	 * @param  idx {Integer} item index
     * @description item 값 얻기.
     */
	,getItems:function (idx){
		if(isNaN(idx)){
			return this.options.tbodyItem;
		}else{
			return this.options.tbodyItem[idx]
		}
	}
	/**
     * @method _initHeaderEvent
     * @description 바디 이벤트 초기화.
     */
	,_initHeaderEvent : function (){
		var _this = this
			 ,headerCol = _this.element.header.find('.pub-header-cont.sort-header');
		
		var beforeClickObj; 
		//headerCol.off('click.pubGridHeader.sort');
		headerCol.on('click.pubGridHeader.sort',function (e){
			var selEle = $(this)
				,col_idx = selEle.attr('col_idx')
				,sortType = selEle.attr('sort_type');
			
			if(beforeClickObj) beforeClickObj.closest('.label-wrapper').removeClass('sortasc sortdesc');

			//.removeClass('sortasc sortdesc');
			sortType = sortType =='asc' ? 'desc' : (sortType =='desc'?'asc':'asc');
			
			// col select background col setting
			if($('#'+_this.prefix+'colbody'+col_idx).attr('data-sort-flag') != 'Y'){
				$(_this.element.body.find('col[data-sort-flag]')).css('background-color','inherit').removeAttr('data-sort-flag');
				$('#'+_this.prefix+'colbody'+col_idx).attr('data-sort-flag','Y');
				$('#'+_this.prefix+'colbody'+col_idx).css('background-color','#b9dfdc !important');
			}
			
			selEle.attr('sort_type', sortType);
			
			selEle.closest('.label-wrapper').removeClass('sortasc sortdesc').addClass('sort'+sortType);

			beforeClickObj = selEle;
		
			_this.setData(_this.getSortList(col_idx, sortType) ,'sort');
		});
		
		if(this.options.headerOptions.contextMenu !== false){
			$.pubContextMenu(_this.element.header.find('.pubGrid-header-th'),this.options.headerOptions.contextMenu);			
		}
	}
	/**
     * @method getSelectCellInfo
     * @description 선택된 cell 영역 구하기.
     */
	,getSelectCellInfo :function(selectInfo, flag){
		var  startIdx = selectInfo.startIdx
			,endIdx = selectInfo.endIdx	
			,startRow = parseInt(selectInfo.startRow,10)
			,endRow = parseInt(selectInfo.endRow,10)
			,startCol = parseInt(selectInfo.startCol,10)
			,endCol = parseInt(selectInfo.endCol,10);
		
		if(startIdx > endIdx){
			var tmp = endIdx;
			endIdx = startIdx;
			startIdx = tmp; 
		}
		
		if(startCol > endCol){
			var tmp = endCol;
			endCol = startCol;
			startCol = tmp; 
		}
		
		if(startRow > endRow){
			var tmp = endRow;
			endRow = startRow;
			startRow = tmp; 
		}
		
		if(this.config.scroll.viewIdx >= startIdx){
			startRow = 0;
		}else{
			startRow = startIdx-this.config.scroll.viewIdx;
		}
		
		if(this.config.scroll.viewIdx+this.config.scroll.maxViewCount <= endIdx){
			endRow = this.config.scroll.viewCount-1;
		}else{
			endRow = endIdx-this.config.scroll.viewIdx;
		}

		return {
			startIdx : startIdx
			,endIdx : endIdx
			,startRow : startRow
			,endRow : endRow
			,startCol : startCol
			,endCol : endCol
		}
	}
	/**
     * @method _initBodyEvent
     * @description 바디 이벤트 초기화.
     */
	,_initBodyEvent : function (){
		var _this = this
			 ,rowClickFlag =false; 
		
		_this.config.select.isMouseDown = false; 		
		
		_this.element.body.on('mousedown.pubgridcol','.pub-body-td',function (e){

			if(e.which ===3){
				return true; 
			}

			var sEle = $(this)
				,selCol = sEle.attr('data-grid-position').split(',')
				,selRow = selCol[0]
				,colIdx = selCol[1];

			var selIdx = _this.config.scroll.viewIdx+parseInt(selRow,10);
				
			var selItem = _this.options.tbodyItem[selRow];
			
			if (e.shiftKey) {
				_this.config.select.endIdx=selIdx;
				_this.config.select.endRow=selRow;
				_this.config.select.endCol=colIdx;
				selectTo();
			} else {
				_this.config.select = {isSelect :true, startIdx : selIdx, endIdx : selIdx, startRow : selRow ,endRow:selRow, startCol : colIdx,  endCol :colIdx};
				_this.element.body.find('.pub-body-td.col-active').removeClass('col-active');
				sEle.addClass('col-active');
			}
			
			window.getSelection().removeAllRanges();
		
			//_this.element.body.attr("onselectstart", "return false");
			_this.config.select.isMouseDown = true; 

			if($.isFunction(_this.options.tColItem[colIdx].colClick)){
				_this.options.tColItem[colIdx].colClick.call(this,colIdx,{
					r:selIdx
					,c:colIdx
					,item:selItem
				});
				return true; 
			}
			
			return true;

		}).on('mouseover.pubgridcol','.pub-body-td',function (e) {
			
			if (!_this.config.select.isMouseDown) return;

			var sEle = $(this)
				,selCol = sEle.attr('data-grid-position').split(',')
				,selRow = selCol[0]
				,colIdx = selCol[1];

			_this.config.select.endRow=selRow;
			_this.config.select.endCol=colIdx;
			_this.config.select.endIdx=_this.config.scroll.viewIdx+parseInt(selRow,10);

			selectTo();
		})
				
		$(document).on('mouseup.'+_this.prefix,function () {
			//_this.element.body.removeClass('pubGrid-noselect');
			_this.config.select.isMouseDown = false;
		});
	
		// col select
		function selectTo() {
			var colInfo = _this.getSelectCellInfo(_this.config.select);
			
			var  sRow= colInfo.startRow
				,eRow =  colInfo.endRow
				,sCol= colInfo.startCol
				,eCol =  colInfo.endCol; 
			
			_this.element.body.find('.pub-body-td.col-active').removeClass('col-active');
			
			for(var i = sRow ; i <= eRow ; i++){
				for(var j=sCol ;j <= eCol; j++){
					var rowCol = i+','+j; 
					var addEle;
					
					if(_this._isFixedPostion(j)){
						addEle =$pubSelect('#'+_this.prefix+'_bodyContainer .pubGrid-body-left-cont').querySelector('[data-grid-position="'+rowCol+'"]');
					}else{
						addEle =$pubSelect('#'+_this.prefix+'_bodyContainer .pubGrid-body-cont').querySelector('[data-grid-position="'+rowCol+'"]');
					}
					addEle.classList.add( 'col-active' );
					
					addEle = null; 
				}
			}
		}

		if(_this.options.rowOptions.click !== false && typeof _this.options.rowOptions.click == 'function'){
			rowClickFlag =true; 

			var beforeRow; 
			_this.element.body.on('click.pubgridrow','.pub-body-tr',function (e){
				var selRow = $(this)
					,rowinfo=selRow.attr('rowinfo');
				
				rowinfo = _this.config.scroll.viewIdx+parseInt(rowinfo,10);

				var selItem = _this.options.tbodyItem[rowinfo];
				
				if(beforeRow) beforeRow.removeClass('active');

				selRow.addClass('active');
				beforeRow = selRow; 
				
				_this.options.rowOptions.click.call(selRow ,rowinfo , selItem);							
			});
		}
				
		$(window).on("keydown." + _this.prefix, function (e) {
				if(!_this.config.focus) return ; 

			if (e.metaKey || e.ctrlKey) {
				
				if (e.which == 67) {
					
					var copyData = _this.selectData();
					try{
						copyStringToClipboard(_this.prefix, copyData);
					}catch(e){
						console.log('Unable to copy', e);					
					}					
				}
			}
		});

		$(document).on('mousedown.'+_this.prefix,'#'+_this.prefix+'_pubGrid',function (e){
			_this.config.focus = true; 
		})

		$(document).on('mousedown.'+_this.prefix, 'html', function (e) {
			if(e.which !==2 && $(e.target).closest('#'+_this.prefix+'_pubGrid').length < 1){
				_this.config.focus = false; 
			}
		});
	}
	/**
     * @method copyData
     * @description 데이타 복사.
     */
	,copyData : function (){
		var copyData = this.selectData();
		try{
			copyStringToClipboard(this.prefix, copyData);
		}catch(e){
			console.log('Unable to copy', e);					
		}			
	}
	/**
     * @method selectData
     * @description select data 구하기.
     */
	,selectData : function () {
		var _this = this; 
		var colInfo = _this.getSelectCellInfo(_this.config.select);
			
		var sCol= colInfo.startCol
			,eCol =  colInfo.endCol
			,sIdx= colInfo.startIdx
			,eIdx =  colInfo.endIdx;
					
		var textArr = [];
		for(var i = sIdx ; i <= eIdx ; i++){
			var item = _this.options.tbodyItem[i];
			var rowText = [];
			for(var j=sCol ;j <= eCol; j++){
				rowText.push(_this.valueFormatter( i, _this.options.tColItem[j],item,null,true)); 
			}
			
			textArr.push(rowText.join('\t'));
		}

		return textArr.join('\n');
	}
	/**
     * @method _setBodyEvent
     * @description body event setting
     */
	,_setBodyEvent : function (){
		if(this.options.rowOptions.contextMenu !== false){
			$.pubContextMenu($('#'+this.prefix+'_bodyContainer .pub-body-tr'),this.options.rowOptions.contextMenu);			
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
     * @method colResize
	 * @param  flag {Boolean} resize 여부
     * @description header resize 설정
     */
	,_headerResize :function (flag){
		var _this = this
			,resizeEle = _this.element.header.find('.pub-header-resizer');
		if(flag===true){
			resizeEle.css('cursor',_this.options.headerOptions.resize.cursor);
			
			resizeEle.on('touchstart.pubresizer mousedown.pubresizer',function (e){
				var oe = e.originalEvent.touches;

				_this.drag = {};
				_this.drag.pageX = oe ? oe[0].pageX : e.pageX;
				_this.drag.ele = $(this);
				_this.drag.ele.addClass('pubGrid-move-header')
				_this.drag.resizeIdx = _this.drag.ele.attr('data-resize-idx');
				_this.drag.isLeftContent  = _this._isFixedPostion(_this.drag.resizeIdx);
				_this.drag.colHeader= $('#'+_this.prefix+'colHeader'+_this.drag.resizeIdx);
				
				_this.drag.totColW = _this.drag.ele.closest('[data-header-info]').width();
				
				_this.drag.colW = _this.options.tColItem[_this.drag.resizeIdx].width;
				if(_this.drag.isLeftContent){
					_this.drag.gridW = _this.config.gridWidth.left - _this.drag.colW;
				}else{
					_this.drag.gridW = _this.config.gridWidth.main - _this.drag.colW;
				}
				_this.drag.gridBodyW = _this.config.body.width - _this.drag.colW;
								
				// resize시 select안되게 처리 . cursor처리 
				_$doc.attr("onselectstart", "return false");
				_this.element.hidden.append("<style type='text/css'>*{cursor:" + _this.options.headerOptions.resize.cursor + "!important}</style>");

				_$doc.on('touchmove.colheaderresize mousemove.colheaderresize', function (e){
					_this.onGripDrag(e,_this);
				}).on('touchend.colheaderresize mouseup.colheaderresize mouseleave.colheaderresize', function (e){
					_this.drag.ele.removeClass('pubGrid-move-header');
					_this.onGripDragEnd(e,_this);
				});

				return true; 
			})
		}else{
			resizeEle.css('cursor','auto');
			resizeEle.off('touchstart.pubresizer mousedown.pubresizer');
		}
	}
	/**
     * @method onGripDrag
	 * @param  e {Event} 이벤트
	 * @param  _this {Object} pub그리드 this
     * @description reisze 드래그 처리.
     */
	,onGripDrag : function(e, _this) { 
		_this._setHeaderResize(e,_this, 'move');	
			
		return false
	}
	/**
     * @method onGripDragOver
	 * @param  e {Event} 이벤트
	 * @param  _this {Object} pub그리드 this
     * @description reisze 드래그 end
     */
	,onGripDragEnd : function(e,_this) {
				
		_$doc.removeAttr("onselectstart");_$doc.off('touchend.colheaderresize mouseup.colheaderresize').off('touchmove.colheaderresize mousemove.colheaderresize mouseleave.colheaderresize');
		_this.element.hidden.empty();
		
		_this._setHeaderResize(e,_this, 'end');
		
		_this.drag=false;

		return false; 
	}
	/**
     * @method _setHeaderResize
	 * @param  e {Event} 이벤트
	 * @param  _this {Object} pub그리드 this
	 * @param  mode {String} 그리드 모드
     * @description reisze 드래그 end
     */
	,_setHeaderResize : function (e,_this , mode){

		if (!_this.drag) return false;

		var drag = _this.drag; 
		
		var oe = e.originalEvent.touches
			,ox = oe ? oe[0].pageX : e.pageX;
		
		var w = drag.colW + (ox - drag.pageX);
		
		var minFlag = false; 
		if(mode=='end'){
			if(w <= _this.options.headerOptions.colMinWidth){
				w =_this.options.headerOptions.colMinWidth;
				minFlag =true; 
			}
			
			var totalWidth = drag.gridW+w;
			
			if(_this.drag.isLeftContent){
				_this.config.gridWidth.left = totalWidth;
			}else{
				_this.config.gridWidth.main = totalWidth;
			}
			
			_this.config.body.width = drag.gridBodyW+w; 
			_this.options.tColItem[drag.resizeIdx].width = w; 
			
			drag.colHeader.css('width',w+'px');
			drag.colHeader.attr('_width',w);
			$('#'+_this.prefix+'colbody'+drag.resizeIdx).css('width',w+'px');
			
			drag.ele.removeAttr('style');

			_this.calcDimension('headerResize');
			
		}else{
			var w = drag.totColW + (ox - drag.pageX);

			if(w > _this.options.headerOptions.colMinWidth){
				drag.ele.css('left',w);
			}
		}
	}
	/**
     * @method pageNav
	 * @param  options {Object} 옵션
     * @description 페이징 하기.
     */
	,pageNav : function(options) {
		var _this =this; 

		var pagingInfo = _this.getPageInfo(options.totalCount , options.currPage , options.countPerPage, options.unitPage);
	
		_this.config.pageNo = options.currPage;

		var currP = pagingInfo.currPage;
		if (currP == "0") currP = 1;
		var preP_is = pagingInfo.prePage_is;
		var nextP_is = pagingInfo.nextPage_is;
		var currS = pagingInfo.currStartPage;
		var currE = pagingInfo.currEndPage;
		if (currE == "0") currE = 1;
		var nextO = 1 * currP + 1;
		var preO = currP - 1;
		var strHTML = new Array();
		strHTML.push('<ul>');
		if (new Boolean(preP_is) == true) {
			strHTML.push(' <li><a href="javascript:" class="page-click" pageno="'+preO+'">&laquo;</a></li>');
		} else {
			if (currP <= 1) {
				strHTML.push(' <li class="disabled"><a href="javascript:">&laquo;</a></li>');
			} else {
				strHTML.push(' <li><a href="javascript:" class="page-click" pageno="'+preO+'">&laquo;</a></li>');
			}
		}
		var no = 0;
		for (no = currS * 1; no <= currE * 1; no++) {
			if (no == currP) {
				strHTML.push(' <li class="active"><a href="javascript:">'+ no + '</a></li>');
			} else {
				strHTML.push(' <li class="page-click" pageno="'+no+'"><a href="javascript:" >'+ no + '</a></li>');
			}
		}

		if (new Boolean(nextP_is) == true) {
			strHTML.push(' <li class="page-click" pageno="'+nextO+'"><a href="javascript:" >&raquo;</a></li>');
		} else {
			if (currP == currE) {
				strHTML.push(' <li class="disabled"><a href="javascript:">&raquo;</a></li>');
			} else {
				strHTML.push(' <li class="page-click" pageno="'+nextO+'"><a href="javascript:" >&raquo;</a></li>');
			}
		}
		strHTML.push('</ul>');
		
		$('#'+_this.prefix+'pubGrid-pageNav').addClass('page-'+(options.position || 'center'))
		$('#'+_this.prefix+'pubGrid-pageNav').empty().html(strHTML.join(''));
		
		$('#'+_this.prefix+'pubGrid-pageNav .page-click').on('click', function() {
			var pageno = $(this).attr('pageno');
			
			$('#'+_this.prefix+'pubGrid-pageNav').find('li.active').removeClass('active');
			$('#'+_this.prefix+'pubGrid-pageNav').find('[pageno="'+pageno+'"]').addClass('active');

			if (typeof options.callback == 'function') {
				_this.config.pageNo = pageno;
				options.callback(pageno);
			}
		});
		
		return this; 
	}
	,getPageNo : function (){
		return this.config.pageNo;
	}
	/**
     * @method getPageInfo
	 * @param  totalCount {int} 총카운트
	 * @param  currPage {int} 현재 페이지
	 * @param  countPerPage {int} 한페이지에 나올 row수
	 * @param  unitPage {int} 한페이지에 나올 페이번호 갯수
     * @description 페이징 하기.
     */
	,getPageInfo : function (totalCount, currPage, countPerPage, unitPage) {
		var unitCount = 100;
		countPerPage = countPerPage || 10;
		unitPage = unitPage || 10;

		if (totalCount == 0) {
			countPerPage = unitCount;
		} else if (totalCount < countPerPage) {
			countPerPage = totalCount / unitCount * unitCount;
			if (totalCount % unitCount > 0) {
				countPerPage += unitCount;
			}
		}

		function getMaxNum( allPage, list_num) {
			if (allPage % list_num == 0) {
				return allPage / list_num;
			}
			return allPage / list_num + 1;
		}

		var totalPage = getMaxNum(totalCount, countPerPage);

		if (totalPage < currPage)
			currPage = totalPage;
		var currEndCount;
		if (currPage != 1) {
			currEndCount = currPage * countPerPage;
		} else {
			currEndCount = countPerPage;
		}

		if (currEndCount > totalCount)
			currEndCount = totalCount;
		var currStartPage;
		var currEndPage;
		
		if (totalPage <= unitPage) {
			currEndPage = totalPage;
			currStartPage = 1;
		} else {
			if(currPage < (unitPage /2)){
				currEndPage = (currPage - 1) / unitPage * unitPage + unitPage;
				currStartPage = currEndPage - unitPage + 1;
			}else{
				currEndPage = (currPage + unitPage /2);
				
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
			,'totalCount' : totalCount ,'totalPage' : totalPage
		};
	}
	/**
     * @method excelExport
	 * @param  opt {object} excel export 타입 ()
     * @description 해제.
     */
	,excelExport : function (opt){

		var downloadInfo =this.config.headerContainerElement.html();
		
		var cssText = '<style type="text/css">';
		cssText += opt.style || '';
        cssText += 'td {border:thin   solid #524848;border-collapse: collapse;}';
        cssText += '</style>';
		
		downloadInfo = downloadInfo.replace('<tbody></tbody>', this.getTbodyHtml('all'));

		console.log(downloadInfo);
		
		downloadInfo = cssText+downloadInfo;
		if(typeof opt !=='undefined'){
			if(opt.type=='download'){
				var fileName = opt.fileName || 'pubgrid-excel-data.xls',
					charset = opt.charset||"utf-8";

				if (navigator.msSaveOrOpenBlob) {
					var _blob = new Blob([downloadInfo], { type: "text/html" });
					window.navigator.msSaveOrOpenBlob(_blob, fileName);
				} else {
					if (_broswer=='msie' && typeof Blob === "undefined") {
						
						var downloadFrame = $('<iframe id="' + this.prefix+ '-excel-export" style="display:none"></iframe>');
						$(document.body).append(downloadFrame);

						 var frmTarget =downloadFrame.get(0).contentWindow.document ; // 해당 아이프레임의 문서에 접근
				 
						frmTarget.open("text/html", "replace");
						frmTarget.write(downloadInfo);
						frmTarget.execCommand("SaveAs", true, fileName);
						frmTarget.close();
						frmTarget.charset = "utf-8";
						frmTarget.focus();
					} else {
						var uri = "data:application/vnd.ms-excel;base64,"+window.btoa(unescape(encodeURIComponent(downloadInfo)))
							,anchor = document.body.appendChild(document.createElement("a"));
						
						anchor.download = fileName;
						//anchor.href = URL.createObjectURL( blob );
						anchor.href = uri;
						anchor.click();
						document.body.removeChild(anchor);
					}
				}
			}
		}else{
			return downloadInfo; 
		}
	}
	/**
     * @method destory
     * @description 해제.
     */
	,destory:function (){
		try{
			if($.isPlainObject (this.options.headerOptions.contextMenu)){
				$.pubContextMenu(this.element.header.find('.pubGrid-header-th')).destory();		
			}
			if($.isPlainObject (this.options.rowOptions.contextMenu)){
				$.pubContextMenu($('#'+this.prefix+'_bodyContainer .pub-body-tr')).destory();
			}
		}catch(e){};

		this.element.body.off('mousedown.pubgridcol mouseover.pubgridcol');
		$(document).off('mouseup.'+this.prefix).off('mousedown.'+this.prefix);
		$(window).off(this.prefix+"pubgridResize").off("keydown." + this.prefix);

		this.gridElement.find('*').off();
		$._removeData(this.gridElement)
		delete _datastore[this.selector];
		$(this.selector).empty(); 
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
	
	var _cacheObject = _datastore[selector]; 

	if(typeof options === 'undefined'){
		return _cacheObject; 
	}
	
	if(typeof _cacheObject === 'undefined'){
		_cacheObject = new Plugin(selector, options);
		_datastore[selector] = _cacheObject;
		return _cacheObject; 
	}else if(typeof options==='object'){
		var headerOpt = options.headerOptions ?options.headerOptions :{}
			,reDrawFlag = typeof headerOpt.redraw==='boolean' ? headerOpt.redraw : _cacheObject.options.headerOptions.redraw; 

		if(reDrawFlag===true){
			_cacheObject.destory();
			_cacheObject = new Plugin(selector, options);
			_datastore[selector] = _cacheObject;
		}else{
			
			_cacheObject.setOptions(options);
			_cacheObject.setData(_this.options.tbodyItem , 'reDraw');
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


