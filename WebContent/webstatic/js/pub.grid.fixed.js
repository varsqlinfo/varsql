/**
 * pubGrid v0.0.1
 * ========================================================================
 * Copyright 2016-2019 ytkim
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
	drag:false
	,borderSpace : 2 // width board space
	,rowOptions:{
		height: 22	// cell 높이
		,click : false //row(tr) click event
		,contextMenu : false // row(tr) contextmenu event
		,addStyle : false	// 추가할 style method
	}
	,useDefaultFormatter :false
	,formatter :{
		money :{prefix :'$', suffix :'원' , fixed : 0}	// money 설정 prefix 앞에 붙일 문구 , suffix : 마지막에 뭍일것 , fixed : 소수점 
		,number : {prefix :'$', suffix :'원' , fixed : 0}
	}
	,autoResize : {
		enabled:true
		,responsive : true // 리사이즈시 그리드 리사이즈 여부.
		,threshold :150
	}
	,headerOptions : {
		view : true	// header 보기 여부
		,height: 23
		,sort : false	// 초기에 정렬할 값
		,redraw : true	// 초기에 옵션 들어오면 새로 그릴지 여부.
		,resize:{	// resize 여부
			enabled : true
			,cursor : 'col-resize'
			,update : false	// 변경시 콜랙 함수
		}
		,isColSelectAll : true	// 전체 선택 여부.
		,colFixedIndex : 0	// 고정 컬럼 
		,colWidthFixed : false  // 넓이 고정 여부.
		,colMinWidth : 50  // 컬럼 최소 넓이
		,colMaxWidth : 500  // 컬럼 최대 넓이
		,oneCharWidth: 7
		,viewAllLabel : true
		,contextMenu : false // header contextmenu event
		
	}
	,setting : {
		enabled : false
		,enableSpeed : false
		,enableSearch : true
		,enableColumnFix : false
		,click : false		// 직접 처리 할경우. function 으로 처리.
		,speedMaxVal :10
		,callback : function (item){
			
		}
		,configVal :{
			search :{			// 검색
				field : ''		// 검색 필드
				,val : ''		// 검색어
			}
			,speed :-1			// scroll speed
			,fixColumnPosition : -1	// fixed col position
		}
		,util : {
			searchFilter : function (item, key,searchVal){
				var itemVal = (item[key]||'');
				if(itemVal.toLowerCase().indexOf(searchVal) > -1){
					return true; 
				}
				return false;
			}
		}
	}
	,asideOptions :{
		lineNumber : {
			enabled :false
			,key : 'lineNumber'
			,charWidth : 9
			,name : ''
			,width : 40
			,styleCss : ''	//css 
			,isSelectRow:true
		}
		,rowSelector :{
			enabled :false
			,key : 'checkbox'
			,name : 'V'
			,width : 25
		}
		,modifyInfo :{
			enabled :false
			,key : 'modify'
			,name : 'modify'
			,width : 10
		}
	}
	,bodyOptions : {
		cellDblClick : false	// body td click
		,valueFilter : false	// cell value filter
	}
	,scroll :{
		isPreventDefault : true	// 이벤트 전파 여부.	
		,vertical : {
			width : 12
			,bgDelay : 100		// 스크롤 빈공간 mousedown delay
			,btnDelay : 100		// 방향키 mousedown delay
			,speed :  'auto'	// 스크롤 스피드
			,onUpdate : function (item){	// 스크롤 업데이트. 
				return true; 
			}
		}
		,horizontal :{
			height: 12
			,bgDelay : 100		
			,btnDelay : 100		// 방향키 버튼 속도.
			,speed : 1			// 스크롤 스피드
		}
	}
	,selectionMode : 'multiple-cell'	// row , cell , multiple-row , multiple-cell	// 선택 방법. 
	,theme : 'light'
	,height: 'auto'
	,itemMaxCount : -1	// add시 item max로 유지할 카운트
	/*
	tColItem  object info
	{
	  "key": "b"	// key
	  ,"label": "비"	// label
	  ,"width": 100		// width
	  ,"sort": true		// sort flag
	  ,"align": "center"	// align
	  ,"type": "money"		// value type
	  ,"render": "html"		// render mode
	  ,colClick :function (idx,item){ console.log(idx, item)}		// cell click event
	  ,styleClass : function (idx, item){return 'pub-bg-private';}	// cell add class
	}
	*/
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
	,i18n :{
		'setting.label' : '설정'
		,'search.button' : '검색'
		,'setting.speed.label' : '스크롤속도'
		,'setting.column.fixed.label' : '고정컬럼'
	}
	,icon : {
		'sortup' : '<svg width="8px" height="8px" viewBox="0 0 110 110" style="enable-background:new 0 0 100 100;"><g><polygon points="50,0 0,100 100,100" fill="#737171"></polygon></g></svg>'
		,'sortdown' : '<svg width="8px" height="8px" viewBox="0 0 110 110" style="enable-background:new 0 0 100 100;"><g><polygon points="0,0 100,0 50,90" fill="#737171"></polygon></g></svg>'
	}
}
,agt = navigator.userAgent.toLowerCase()
,_broswer = ((function (){
	if (agt.indexOf("msie") != -1) return 'msie'; 
	if (agt.indexOf("chrome") != -1) return 'chrome'; 
	if (agt.indexOf("firefox") != -1) return 'firefox'; 
	if (agt.indexOf("safari") != -1) return 'safari'; 
	if (agt.indexOf("opera") != -1) return 'opera'; 
	if (agt.indexOf("mozilla/5.0") != -1) return 'mozilla';
	if (agt.indexOf("staroffice") != -1) return 'starOffice'; 
	if (agt.indexOf("webtv") != -1) return 'WebTV'; 
	if (agt.indexOf("beonex") != -1) return 'beonex'; 
	if (agt.indexOf("chimera") != -1) return 'chimera'; 
	if (agt.indexOf("netpositive") != -1) return 'netPositive'; 
	if (agt.indexOf("phoenix") != -1) return 'phoenix'; 
	if (agt.indexOf("skipstone") != -1) return 'skipStone'; 
	if (agt.indexOf("netscape") != -1) return 'netscape'; 
})());

var hasOwnProperty = Object.prototype.hasOwnProperty;

function hasProperty(obj , key){
	return hasOwnProperty.call(obj, key);
}

function isUndefined(obj){
	return typeof obj==='undefined';
}
function isFunction(obj){
	return typeof obj==='function';
}

function intValue(val){
	return parseInt(val , 10);
}

function getCharLength(s){
    var w_1 =0 , w_15 =0 , w_2=0, w_3 =0 ; // 글자 크기.

    for(var i=0,l=s.length; i<l; i++){
		var c=s.charCodeAt(i);

		if(c>>11){ // 3byte 처리
			++w_3;
		}else if(c>>7){	// 2byte 처리
			++w_2;
		}else{
			if(65 <=c && c <=90){ // 대문자 
				++w_15;
			}else{
				++w_1;
			}
		}
    }

    return w_1 + (w_15*1.3) + (w_2*2) + (w_3*2.1);
}

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

	if (!isUndefined(window.clipboardData) && !isUndefined(window.clipboardData.setData)) {
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

function $pubSelector(selector){
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

_$doc.on('mouseup.pubgrid', function (){
	for(var key in _datastore){
		_datastore[key]._setMouseDownFlag(false);
	}
})

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
			, container :{height : 0,width : 0}
			, header :{height : 0, width : 0}
			, footer :{height : 0, width : 0}
			, navi :{height : 0, width : 0}
			, initSettingFlag :false
			, aside :{items :[], lineNumberCharLength : 0, initWidth: 0}
			, select : {}
			, template: {}
			, orginData: []
			, dataInfo : {colLen : 0, allColLen : 0, rowLen : 0, lastRowIdx : 0}
			, rowOpt :{}
			, sort : {}
			, selection :{
				startCell :{}
			}
		};
		_this.eleCache = {};
		_this._initScrollData();
		_this._setSelectionRangeInfo({},true);
		
		_this.setOptions(options, true);

		_this.drag ={};
		_this.addStyleTag();

		_this._setThead();
		_this.setData(_this.options.tbodyItem , 'init');
		
		_this.config.gridXScrollFlag = false;
		_this._windowResize();

		return this;
	}

	,_initScrollData : function (){
		this.config.scroll = {before:{},top :0 , left:0, startCol:0, endCol : 0, viewIdx : 0, vBarPosition :0 , hBarPosition :0 , maxViewCount:0, viewCount : 0, vTrackHeight:0,hTrackWidth:0, bodyHeight:0}; 
	}
	/**
     * @method _setGridWidth
     * @description grid 넓이 구하기
     */
	,_setGridWidth : function (mode){
		var _this = this;
		
		_this.config.container.width = _this.gridElement.innerWidth()-1; // border 값 빼주기.			
	}
	/**
     * @method setOptions
     * @description 옵션 셋팅.
     */
	,setOptions : function(options , firstFlag){
		var _this = this; 
		options.setting = options.setting ||{};
		options.setting.configVal = objectMerge({},_defaults.setting.configVal ,options.setting.configVal);
		
		_this.options =objectMerge($.extend(true , {}, _defaults), options);
			
		_this.options.tbodyItem = options.tbodyItem ? options.tbodyItem : _this.options.tbodyItem;

		//_this.config.rowHeight = _this.options.rowOptions.height+1;	// border-box 수정. 2017-08-11
		_this.config.rowHeight = _this.options.rowOptions.height;

		if(_this.options.rowOptions.contextMenu !== false && typeof _this.options.rowOptions.contextMenu == 'object'){
			var _cb = _this.options.rowOptions.contextMenu.callback; 
			
			if(_cb){
				_this.options.rowOptions.contextMenu.callback = function(key,sObj) {
					this.gridItem = _this.getItems(_this.config.scroll.viewIdx + intValue(this.element.attr('rowInfo')));
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

		if(_this.options.asideOptions.lineNumber.enabled ===true){
			asideItem.push(_this.options.asideOptions.lineNumber);
		}

		if(_this.options.asideOptions.rowSelector.enabled ===true){
			asideItem.push(_this.options.asideOptions.rowSelector);
		}
		
		if(_this.options.asideOptions.modifyInfo.enabled ===true){
			asideItem.push(_this.options.asideOptions.modifyInfo);
		}
		
		_this.config.rowOpt.isAddStyle = isFunction(_this.options.rowOptions.addStyle);
		
		_this.config.aside.items = asideItem; 
		for(var i =0 ; i < asideItem.length ;i++){
			_this.config.gridWidth.aside += asideItem[i].width; 
		}
		_this.config.aside.initWidth = _this.config.gridWidth.aside;

		_this._setGridWidth();
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

		if(_this.options.asideOptions.lineNumber.isSelectRow ===true){
			cssStr.push('#'+_this.prefix+'_pubGrid .pubGrid-body-aside .pub-body-aside-td{cursor:pointer;}');
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
	,_setThead : function (calcFlag){
		var _this = this
			,opt = _this.options;
			
		var tci = opt.tColItem
			,thg = opt.theadGroup
			,fixedColIdx = opt.headerOptions.colFixedIndex
			,cfg = _this.config
			,gridElementWidth =cfg.container.width-(cfg.gridWidth.aside+opt.scroll.vertical.width)
			,tciItem,thgItem, rowItem, headItem
			,headGroupInfo = [] ,groupInfo = [], rowSpanNum = {}, colSpanNum = {}
			,leftHeaderGroupInfo = [] ,leftGroupInfo = [], rowSpanNum = {}, colSpanNum = {};

		var gridTci = [];
		for(var  i =0 ;i < tci.length;i++){
			var tmpTci = tci[i];
			if(tmpTci.visible !== false){
				gridTci.push(tmpTci);
			}
		}

		cfg.tColItem = gridTci;

		tci = gridTci; 
		
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

				if(tmpColIdx > j || tmpThgIdx >= thgItem.length){
					headItem = {r:i,c:j,view:false};
				}else{
					headItem=thgItem[tmpThgIdx];

					if(calcFlag !== false){
						tmpColIdx +=(headItem['colspan'] || 1);
						headItem['r'] = i;
						headItem['c'] = j;
						headItem['view'] = true;
						headItem['sort'] = tciItem.sort===false ? false : opt.headerOptions.sort;
						headItem['colSpanIdx'] = beforeColSpanIdx+1;
						headItem['colspanhtm'] = 'scope="col"';
						headItem['rowspanhtm'] ='';
						headItem['label'] = headItem.label ? headItem.label : tciItem.label;
						
						if(headItem.colspan){
							headItem['colSpanIdx'] = headItem['colSpanIdx']+headItem.colspan-1;
							headItem['colspanhtm'] = ' scope="colgroup" colspan="'+headItem.colspan+'" ';
							
							colSpanNum[i][j]= j+headItem.colspan; 
						}

						if(headItem.rowspan){
							headItem['rowspanhtm'] += ' scope="col" rowspan="'+headItem.rowspan+'" ';
							rowSpanNum[j] = i+ headItem.rowspan -1;
						}
						beforeColSpanIdx = headItem['colSpanIdx'];
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
				}

				if(headItem.view==true){
					sortHeaderInfo[j] = {r:i,key:tciItem.key};
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
			if(!isUndefined(tmpHgi)){
				tmpHgi['isSort'] =(tmpHgi.sort===true?true:false); 
				headGroupInfo[sortHeaderInfo[_ikey].r][_ikey] = tmpHgi;
			}

			tmpHgi = leftHeaderGroupInfo[sortHeaderInfo[_ikey].r][_ikey]
			if(!isUndefined(tmpHgi)){
				tmpHgi['isSort'] =(tmpHgi.sort===true?true:false); 
				leftHeaderGroupInfo[sortHeaderInfo[_ikey].r][_ikey] = tmpHgi;
			}
		}

		cfg.headerInfo = headGroupInfo;
		cfg.headerLeftInfo = leftHeaderGroupInfo;

		var colWidth = Math.floor((gridElementWidth)/tci.length);
		
		colWidth = colWidth-10;
				
		var viewAllLabel = calcFlag===false ? false : (opt.headerOptions.viewAllLabel ===true ?true :false); 

		var strHtm = [], leftWidth=0, mainWidth=0 , viewColCount= 0;
		for(var j=0; j<tci.length; j++){
			var tciItem = tci[j];

			if(tciItem.visible===false) continue; 
			
			++viewColCount; 

			if(viewAllLabel){
				tciItem.width = tciItem.label.length * 11; 
			}else{
				tciItem.width = isNaN(tciItem.width) ? 0 :tciItem.width; 
			}
			
			tciItem.width = Math.max(tciItem.width, opt.headerOptions.colMinWidth);
			tciItem['_alignClass'] = tciItem.align=='right' ? 'ar' : (tciItem.align=='center'?'ac':'al');
			cfg.tColItem[j] = tciItem;

			if(_this._isFixedPostion(j)){
				leftWidth +=tciItem.width;
			}else{
				mainWidth +=tciItem.width;
			}

			strHtm.push('<option value="'+tciItem.key+'">'+tciItem.label+'</option>');
		}
		cfg.gridWidth.left = leftWidth;
		cfg.gridWidth.main = mainWidth;
		
		cfg.itemColumnCount = viewColCount; 
		
		if(calcFlag === false){
			return ; 
		}
		
		cfg.template['searchField'] = strHtm.join('');
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
			,_gw = _this.config.container.width
			,tci = _this.config.tColItem
			,tciLen = _this.config.itemColumnCount;

		var _totW = _this.config.gridWidth.aside+_this.config.gridWidth.left+_this.config.gridWidth.main+opt.scroll.vertical.width;
					
		if(opt.headerOptions.colWidthFixed !== true){
			var resizeFlag = _totW  < _gw ? true : false;
			var remainderWidth = Math.floor((_gw -_totW)/tciLen)
				, lastSpaceW = (_gw -_totW)-remainderWidth *tciLen;

			lastSpaceW = lastSpaceW - _this.options.borderSpace; // border 겹치는 현상때문에 -2 빼줌.

			if(resizeFlag){
				var leftGridWidth = 0, mainGridWidth=0;
				for(var j=0; j<tciLen; j++){
					var item = tci[j];
					item.width += remainderWidth;
					item.width = Math.max(item.width, opt.headerOptions.colMinWidth);

					if(_this._isFixedPostion(j)){
						leftGridWidth +=item.width;
					}else{
						mainGridWidth +=item.width;
					}
				}
				_this.config.tColItem[tciLen-1].width +=lastSpaceW;
				_this.config.gridWidth.left = leftGridWidth
				_this.config.gridWidth.main =mainGridWidth+lastSpaceW; 
			}
		}
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
			,tci = _this.config.tColItem
			,thiItem;
		var strHtm = [];

		var startCol =0, endCol = tci.length; 

		if(type=='left'){
			endCol = _this.options.headerOptions.colFixedIndex;
		}else if(type=='cont'){
			startCol = _this.options.headerOptions.colFixedIndex;
		}
		
		strHtm.push('<colgroup>');
		
		var enableColCnt =0; 
		for(var i=startCol ;i <endCol; i++){
			thiItem = tci[i];
			var tmpStyle = [];
			tmpStyle.push('width:'+thiItem.width+'px;');
			if(thiItem.visible===false){
				tmpStyle.push('display:none;visibility: hidden;');
			}else{
				++enableColCnt;
				strHtm.push('<col id="'+id+i+'" style="'+tmpStyle.join('')+'" />');
			}
		}

		_this.config.dataInfo.colLen = enableColCnt;
		_this.config.dataInfo.allColLen= endCol;
		
		strHtm.push('</colgroup>');
		
		return strHtm.join('');	
	}
	/**
     * @method addData
	 * @param data {Array} - 데이타
	 * @param opt {Object} - add option { prepend : 앞에 넣을지 여부 (true or false) ,focus : 스크롤 이동 여부 (true or false) }
     * @description 데이타 add
     */
	,addData : function (pData, opt){
		this.setData(pData, 'addData', opt);
	}
	/**
     * @method setData
	 * @param data {Array} - 데이타
	 * @param gridMode {String} - 그리드 모드 
	 * @param addOpt {Object} - add opt
     * @description 데이타 그리기
     */
	,setData :function (pdata, mode, addOpt){
		var _this = this
			,opt = _this.options
			,tci = _this.config.tColItem;
		var data = pdata;
		var pageInfo = opt.page;

		mode = mode||'reDraw'; 

		var gridMode = mode.split('_')[0];

		gridMode = gridMode||'reDraw';
		if(!$.isArray(pdata)){
			data = pdata.items;
			pageInfo = pdata.page; 
		}

		if(data){
			if(gridMode == 'addData'){
				var rowIdx =0;
				
				if(addOpt.prepend ===true){
					Array.prototype.unshift.apply(_this.options.tbodyItem, data);
					//_this.options.tbodyItem = _this.options.tbodyItem.unshift(data);	
				}else{
					_this.options.tbodyItem = _this.options.tbodyItem.concat(data);	
				}
				
				if(_this.options.tbodyItem.length > opt.itemMaxCount){
					var removeCount = _this.options.tbodyItem.length-opt.itemMaxCount; 
					if(addOpt.prepend ===true){
						_this.options.tbodyItem.splice(opt.itemMaxCount,removeCount)
					}else{
						_this.options.tbodyItem.splice(0,removeCount)
					}	
				}
			}else{
				_this.options.tbodyItem = data;
			}
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
		
		if(gridMode == 'search'){
			gridMode = 'reDraw';
		}else{
			var settingOpt = _this.options.setting; 

			_this.config.orginData = _this.options.tbodyItem;
			
			if(settingOpt.enabled ===true  && settingOpt.enableSearch ===true){
				try{
					var schField = settingOpt.configVal.search.field ||''
						,schVal = settingOpt.configVal.search.val ||'';

					if(schField != '' && schVal !=''){
						var tbodyItem = _this.options.tbodyItem
							,schArr =[];

						schVal =schVal.toLowerCase();
						
						for(var i =0 , len  = tbodyItem.length; i < len;i++){
							var tmpItem =tbodyItem[i]; 

							if(settingOpt.util.searchFilter(tmpItem,schField,schVal)){
								schArr.push(tmpItem);
							}
						}
						_this.options.tbodyItem = schArr;
					}
				}catch(e){}
			}
		}
		
		_this.config.dataInfo.orginRowLen = _this.options.tbodyItem.length;

		if(_this.config.dataInfo.orginRowLen > 0){
			_this.config.dataInfo.rowLen= _this.config.dataInfo.orginRowLen+1;
			_this.config.dataInfo.lastRowIdx= _this.config.dataInfo.orginRowLen-1;
		}
		
		if(gridMode=='reDraw' || gridMode == 'addData'){
			_this._setHeaderInitInfo();
			_this._setSelectionRangeInfo({}, true);

			_this.calcDimension(gridMode);
		}
				
		_this.setScrollSpeed();

		if(gridMode != 'addData'){
			_this.drawGrid(mode,true);
		}else{
			if(addOpt.focus ===true){
				if(addOpt.prepend ===true){
					_this.moveVerticalScroll({rowIdx : 0})
				}else{
					_this.moveVerticalScroll({rowIdx : _this.config.dataInfo.orginRowLen})
				}	
			}
		}

		_this.setPage(pageInfo);

		if(_this.config.orginData != _this.options.tbodyItem){
			_this.gridElement.find('.pubGrid-setting').addClass('search-on');
		}else{
			_this.gridElement.find('.pubGrid-setting').removeClass('search-on');
		}
	}
	/**
     * @method setScrollSpeed
     * @description 스크롤 스피드 셋팅
     */
	,setScrollSpeed : function (val){
		if(!isUndefined(val) && !isNaN(val) && val > 0){
			val= parseInt(val,10);
		}else{
			//val = Math.ceil(this.config.dataInfo.rowLen /100);
		}
		this.options.scroll.vertical.speed =  val; 
		return val;
	}
	,_setHeaderInitInfo : function (){
		var tci  = this.config.tColItem;

		for(var i =0 ;i <tci.length; i++){
			tci[i].maxWidth = -1; 
		}
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
			,tci = _this.config.tColItem
			,thiItem;

		var strCss = [];
		for(var i=0 ;i <tci.length; i++){
			thiItem = tci[i];
			var tmpStyle = [];
			tmpStyle.push('width:'+thiItem.width+'px;');
			if(thiItem.visible===false){
				tmpStyle.push('display:none;visibility: hidden;');
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
		var _this = this
			,vArrowWidth = _this.options.scroll.vertical.width-2
			,hArrowWidth = _this.options.scroll.horizontal.height-2;
	
		return '<div class="pubGrid-wrapper"><div id="'+_this.prefix+'_pubGrid" class="pubGrid pubGrid-noselect"  style="overflow:hidden;width:'+_this.config.container.width+'px;">'
			+' 	<div id="'+_this.prefix+'_container" class="pubGrid-container" style="overflow:hidden;">'
			+'    <div class="pubGrid-setting-wrapper pubGrid-layer" data-pubgrid-layer="'+_this.prefix+'"><div class="pubGrid-setting"><svg version="1.1" width="'+vArrowWidth+'px" height="'+vArrowWidth+'px" viewBox="0 0 54 54" style="enable-background:new 0 0 54 54;">	'
			+'<g><path id="'+_this.prefix+'_settingBtn" d="M51.22,21h-5.052c-0.812,0-1.481-0.447-1.792-1.197s-0.153-1.54,0.42-2.114l3.572-3.571	'
			+'		c0.525-0.525,0.814-1.224,0.814-1.966c0-0.743-0.289-1.441-0.814-1.967l-4.553-4.553c-1.05-1.05-2.881-1.052-3.933,0l-3.571,3.571	'
			+'		c-0.574,0.573-1.366,0.733-2.114,0.421C33.447,9.313,33,8.644,33,7.832V2.78C33,1.247,31.753,0,30.22,0H23.78	'
			+'		C22.247,0,21,1.247,21,2.78v5.052c0,0.812-0.447,1.481-1.197,1.792c-0.748,0.313-1.54,0.152-2.114-0.421l-3.571-3.571	'
			+'		c-1.052-1.052-2.883-1.05-3.933,0l-4.553,4.553c-0.525,0.525-0.814,1.224-0.814,1.967c0,0.742,0.289,1.44,0.814,1.966l3.572,3.571	'
			+'		c0.573,0.574,0.73,1.364,0.42,2.114S8.644,21,7.832,21H2.78C1.247,21,0,22.247,0,23.78v6.439C0,31.753,1.247,33,2.78,33h5.052	'
			+'		c0.812,0,1.481,0.447,1.792,1.197s0.153,1.54-0.42,2.114l-3.572,3.571c-0.525,0.525-0.814,1.224-0.814,1.966	'
			+'		c0,0.743,0.289,1.441,0.814,1.967l4.553,4.553c1.051,1.051,2.881,1.053,3.933,0l3.571-3.572c0.574-0.573,1.363-0.731,2.114-0.42	'
			+'		c0.75,0.311,1.197,0.98,1.197,1.792v5.052c0,1.533,1.247,2.78,2.78,2.78h6.439c1.533,0,2.78-1.247,2.78-2.78v-5.052	'
			+'		c0-0.812,0.447-1.481,1.197-1.792c0.751-0.312,1.54-0.153,2.114,0.42l3.571,3.572c1.052,1.052,2.883,1.05,3.933,0l4.553-4.553	'
			+'		c0.525-0.525,0.814-1.224,0.814-1.967c0-0.742-0.289-1.44-0.814-1.966l-3.572-3.571c-0.573-0.574-0.73-1.364-0.42-2.114	'
			+'		S45.356,33,46.168,33h5.052c1.533,0,2.78-1.247,2.78-2.78V23.78C54,22.247,52.753,21,51.22,21z M52,30.22	'
			+'		C52,30.65,51.65,31,51.22,31h-5.052c-1.624,0-3.019,0.932-3.64,2.432c-0.622,1.5-0.295,3.146,0.854,4.294l3.572,3.571	'
			+'		c0.305,0.305,0.305,0.8,0,1.104l-4.553,4.553c-0.304,0.304-0.799,0.306-1.104,0l-3.571-3.572c-1.149-1.149-2.794-1.474-4.294-0.854	'
			+'		c-1.5,0.621-2.432,2.016-2.432,3.64v5.052C31,51.65,30.65,52,30.22,52H23.78C23.35,52,23,51.65,23,51.22v-5.052	'
			+'		c0-1.624-0.932-3.019-2.432-3.64c-0.503-0.209-1.021-0.311-1.533-0.311c-1.014,0-1.997,0.4-2.761,1.164l-3.571,3.572	'
			+'		c-0.306,0.306-0.801,0.304-1.104,0l-4.553-4.553c-0.305-0.305-0.305-0.8,0-1.104l3.572-3.571c1.148-1.148,1.476-2.794,0.854-4.294	'
			+'		C10.851,31.932,9.456,31,7.832,31H2.78C2.35,31,2,30.65,2,30.22V23.78C2,23.35,2.35,23,2.78,23h5.052	'
			+'		c1.624,0,3.019-0.932,3.64-2.432c0.622-1.5,0.295-3.146-0.854-4.294l-3.572-3.571c-0.305-0.305-0.305-0.8,0-1.104l4.553-4.553	'
			+'		c0.304-0.305,0.799-0.305,1.104,0l3.571,3.571c1.147,1.147,2.792,1.476,4.294,0.854C22.068,10.851,23,9.456,23,7.832V2.78	'
			+'		C23,2.35,23.35,2,23.78,2h6.439C30.65,2,31,2.35,31,2.78v5.052c0,1.624,0.932,3.019,2.432,3.64	'
			+'		c1.502,0.622,3.146,0.294,4.294-0.854l3.571-3.571c0.306-0.305,0.801-0.305,1.104,0l4.553,4.553c0.305,0.305,0.305,0.8,0,1.104 '
			+'		l-3.572,3.571c-1.148,1.148-1.476,2.794-0.854,4.294c0.621,1.5,2.016,2.432,3.64,2.432h5.052C51.65,23,52,23.35,52,23.78V30.22z"/>'
			+'	<path d="M27,18c-4.963,0-9,4.037-9,9s4.037,9,9,9s9-4.037,9-9S31.963,18,27,18z M27,34c-3.859,0-7-3.141-7-7s3.141-7,7-7'
			+'		s7,3.141,7,7S30.859,34,27,34z"/></g>'
			+'</svg></div>'
			+'		<div class="pubGrid-setting-area">'
			+'			<div class="pubGrid-search-area"><select name="pubgrid_srh_filed"><option>field</option></select><input type="text" name="pubgrid_srh_val" class="pubGrid-search-field"><button type="button" class="pubgrid-btn" data-setting-mode="search">'+this.options.i18n['search.button']+'</button></div>'
			+'			<div class="pubGrid-colfixed-area"><span>'+this.options.i18n['setting.column.fixed.label']+'</span><select name="pubgrid_col_fixed"></select></div>'
			+'			<div class="pubGrid-speed-area"><span>'+this.options.i18n['setting.speed.label']+'</span><select name="pubgrid_scr_speed"><option value="1">1</option></select></div>'
			+'		</div>'
			+'		</div>'
			+' 		<div class="pubGrid-header-container-warpper">'
			+' 		  <div id="'+_this.prefix+'_headerContainer" class="pubGrid-header-container">'
			+' 			<div class="pubGrid-header-aside"><table class="pubGrid-header-aside-cont" style="width:'+_this.config.gridWidth.aside+'px;">#theaderAsideHtmlArea#</table></div>'
			+' 			<div class="pubGrid-header-left"><table class="pubGrid-header-left-cont" style="width:'+_this.config.gridWidth.left+'px;">#theaderLeftHtmlArea#</table></div>'
			+' 			<div class="pubGrid-header">'
			+'				<div class="pubGrid-header-cont-wrapper" style="position:relative;"><table style="width:'+_this.config.gridWidth.main+'px;" class="pubGrid-header-cont" onselectstart="return false">#theaderHtmlArea#</table></div>'
			+' 			</div>'
			+' 		  </div>'
			+' 		</div>'		

			+' 		<div id="'+_this.prefix+'_bodyContainer" class="pubGrid-body-container-warpper" style="height:'+_this.config.container.height+'px">'
			+' 			<div class="pubGrid-body-container">'
			+' 				<div class="pubGrid-body-aside"><table style="width:'+_this.config.gridWidth.aside+'px;" class="pubGrid-body-aside-cont"></table></div>'
			+' 				<div class="pubGrid-body-left"><table style="width:'+_this.config.gridWidth.left+'px;" class="pubGrid-body-left-cont"></table></div>'
			+' 				<div class="pubGrid-body">'
			+'					<div class="pubGrid-body-cont-wrapper" style="position:relative;"><table style="width:'+_this.config.gridWidth.main+'px;" class="pubGrid-body-cont"></table></div>'
			+'				</div>'
			+'				<div class="pubGrid-body-selection-cell"></div>'
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
			+'			<div class="pubGrid-scroll-top-area" style="height:23px;"></div>'
			+' 			<div class="pubGrid-vscroll-bar-area">'
			+'			  <div class="pubGrid-vscroll-bar-bg"></div>'
			+' 			  <div class="pubGrid-vscroll-up pubGrid-vscroll-btn" data-pubgrid-btn="U"><svg width="'+vArrowWidth+'px" height="8px" viewBox="0 0 110 110" style="enable-background:new 0 0 100 100;"><g><polygon points="50,0 0,100 100,100" fill="#737171"/></g></svg></div>'
			+'			  <div class="pubGrid-vscroll-bar"></div>'
			+' 			  <div class="pubGrid-vscroll-down pubGrid-vscroll-btn" data-pubgrid-btn="D"><svg width="'+vArrowWidth+'px" height="8px" viewBox="0 0 110 110" style="enable-background:new 0 0 100 100;"><g><polygon points="0,0 100,0 50,90" fill="#737171"/></g></svg></div>'
			+' 			</div>'
			+' 		</div>'
			+' 		<div id="'+_this.prefix+'_hscroll" class="pubGrid-hscroll">'
			+'			<div class="pubGrid-scroll-aside-area"></div>'
			+' 			<div class="pubGrid-hscroll-bar-area">'
			+'			  <div class="pubGrid-hscroll-bar-bg"></div>'
			+' 			  <div class="pubGrid-hscroll-left pubGrid-hscroll-btn" data-pubgrid-btn="L"><svg width="8px" height="'+hArrowWidth+'px" viewBox="0 0 110 110" style="enable-background:new 0 0 100 100;"><g><polygon points="10,50 100,0 100,100" fill="#737171"/></g></svg></div>'
			+'			  <div class="pubGrid-hscroll-bar"></div>'
			+' 			  <div class="pubGrid-hscroll-right pubGrid-hscroll-btn" data-pubgrid-btn="R"><svg width="8px" height="'+hArrowWidth+'px" viewBox="0 0 110 110" style="enable-background:new 0 0 100 100;"><g><polygon points="0,0 0,100 90,50" fill="#737171"/></g></svg></div>'
			+'			</div>'
			+' 		</div> '
			+' 	</div>'
			+' 	<div id="'+_this.prefix+'_navigation" class="pubGrid-navigation"><div class="pubGrid-page-navigation"></div><div id="'+_this.prefix+'_status" class="pubgGrid-count-info"></div>'
			+' 	</div>'
			+' </div></div>';

	}
	,getTbodyAsideHtml : function (mode){
		var _this =this; 

		var strHtm = [];
		
		var items =_this.config.aside.items;
		
		var tmpeElementBody = _this.element.body.find('.pubGrid-body-aside-cont');
		
		for(var i =0 ; i < _this.config.scroll.maxViewCount; i++){
			if(tmpeElementBody.find('[rowinfo="'+i+'"]').length > 0){
				if(i >= _this.config.scroll.viewCount){
					tmpeElementBody.find('[rowinfo="'+i+'"]').hide();
				}else{
					tmpeElementBody.find('[rowinfo="'+i+'"]').show();
				}
			}else{
				strHtm.push('<tr class="pub-body-tr" rowinfo="'+i+'">');

				for(var j=0 ;j < items.length; j++){
					var item = items[j];
					item['_alignClass'] = item.align=='right' ? 'ar' : (item.align=='left'?'al':'ac');

					strHtm.push('<td scope="col" class="pub-body-aside-td pub-'+item.key+'" data-aside-position="'+i+','+item.key+'"><div class="aside-content '+item['_alignClass']+'" style="'+item.styleCss+'"></div></td>');
				}
				strHtm.push('</tr>');
			}
		}
		
		if(mode=='init'){
			var colGroupHtm=[];
			for(var j=0 ;j < items.length; j++){
				colGroupHtm.push('<col id="'+_this.prefix+'colbody'+items[j].key+'" style="width:'+items[j].width+'px;" />');
			}
							
			var bodyHtm = '';
			bodyHtm += '<colgroup>'+colGroupHtm.join('')+'</colgroup>';
			bodyHtm += '<tbody class="pubGrid-body-tbody">'+strHtm.join('')+'</tbody>';
			tmpeElementBody.empty().html(bodyHtm);
		}else{
			strHtm = strHtm.join('');
			if(strHtm != ''){
				tmpeElementBody.append(strHtm);
			}
		}
	}

	/**
	* @method getRowCount
	* @description view row count
	*/
	,getViewRow: function (){
		return this.config.scroll.viewCount;
	}
	/**
     * @method getTbodyHtml
	 * @description body html  만들기
     */
	,getTbodyHtml : function(pMode){

		pMode = pMode||'';
		var modeInfo =pMode.split('_');
		var mode = modeInfo[0];
		var subMode = modeInfo[1] ||'';
		
		if(subMode !='colfixed'){
			this.getTbodyAsideHtml(mode);
		}
		
		function bodyHtm (_this , mode , type){
			var clickFlag = false
				,tci = _this.config.tColItem
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

				if(mode != 'init' && tmpeElementBody.find('[rowinfo="'+i+'"]').length > 0){
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
						
						strHtm.push('<td scope="col" class="pub-body-td" data-grid-position="'+i+','+j+'"><div class="pub-content pub-content-ellipsis ' +thiItem['_alignClass']+' '+ (clickFlag?'pub-body-td-click':'') +'"></div></td>');
					}
					strHtm.push('</tr>');
				}
			}
			
			if(mode=='init'){
				var bodyHtm = '';
				bodyHtm +=_this._getColGroup(_this.prefix+'colbody', type);
				bodyHtm += '<tbody class="pubGrid-body-tbody">'+strHtm.join('')+'</tbody>';
				
				tmpeElementBody.empty().html(bodyHtm);
				
			}else{
				strHtm = strHtm.join('');
				if(strHtm != ''){
					tmpeElementBody.append(strHtm);
				}
				return true; 
			}
		}

		if(mode=='init' || this.config.dataInfo.rowLen > 0){
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
		var itemVal;
		if(this.options.bodyOptions.valueFilter ===false){
			itemVal = rowItem[thiItem.key];
		}else{
			var tmpVal = this.options.bodyOptions.valueFilter(thiItem, rowItem);
			itemVal = tmpVal !==false ? tmpVal : rowItem[thiItem.key];
		}
		
		var tmpFormatter={}; 		
		if(type == 'money' || type == 'number'){
			tmpFormatter = this.options.formatter[type];
		}

		if(isFunction(thiItem.formatter)){
			itemVal = thiItem.formatter.call(null,{idx : _idx , colInfo:thiItem, item: rowItem , formatter : function (val, fixed , prefix , suffix){
				fixed = isUndefined(fixed)?tmpFormatter.fixed :fixed;
				prefix = isUndefined(prefix)?tmpFormatter.prefix :prefix;
				suffix = isUndefined(suffix)?tmpFormatter.suffix :suffix;
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
				addEle.innerHTML = itemVal||'';
			}else{
				addEle.textContent = itemVal;	
			}
		}
	}
	/**
     * @method _setCellStyle
	 * @param  cellEle {Elements} cell dom element
	 * @param  _idx {Integer} header column index
	 * @param  thiItem {Object} header item
	 * @param  rowItem {Object} item
     * @description tbody 추가 , 삭제 .
     */
	,_setCellStyle : function (cellEle, _idx,thiItem,rowItem){
		// style 처리
		var addClass
		if(isFunction(thiItem.styleClass)){
			addClass = thiItem.styleClass.call(null,_idx, rowItem)
		}else{
			addClass=(thiItem.cellClass||'');
		}
		cellEle.setAttribute('class','pub-body-td '+addClass );
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
			,tci = _this.config.tColItem
			,headerOpt=_this.options.headerOptions;

		var strHtm = [];

		var headerInfo = cfg.headerInfo

		if(type=='left'){
			headerInfo =  cfg.headerLeftInfo
		}
		
		strHtm.push(_this._getColGroup(_this.prefix+'colHeader' , type));
		
		strHtm.push('<thead>');
		if(headerInfo.length > 0 && headerOpt.view){
			var ghArr, ghItem;
				
			for(var i =0, len=headerInfo.length ; i < len; i++){
				ghArr = headerInfo[i];
				strHtm.push('<tr class="pub-header-tr">');
				for(var j=0 ; j <ghArr.length; j++){
					ghItem = ghArr[j];
					if(ghItem.view){
						strHtm.push(' <th '+ghItem.colspanhtm+' '+ghItem.rowspanhtm+' data-header-info="'+i+','+j+'" class="pubGrid-header-th" '+(ghItem.style?' style="'+ghItem.style+'" ':'')+'>');
						strHtm.push('  <div class="label-wrapper">');
						strHtm.push('   <div class="pub-header-cont outer '+(ghItem.isSort===true?'sort-header':'')+'" col_idx="'+j+'"><div class="pub-inner"><div class="centered">'+ghItem.label+'</div></div>');
						if(ghItem.isSort ===true){
							strHtm.push('<div class="pub-sort-icon pubGrid-sort-up">'+_this.options.icon.sortup+'</div><div class="pub-sort-icon pubGrid-sort-down">'+ _this.options.icon.sortdown+'</div>');	
						}
						strHtm.push('   </div>');
						strHtm.push('   <div class="pub-header-resizer" data-resize-idx="'+ghItem.resizeIdx+'"></div>');
						strHtm.push('  </div>');
						strHtm.push(' </th>');					
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
	/**
     * @method setColFixedIndex
	 * @param  idx {Number} header column index
     * @description 고정 컬럼 
     */
	,setColFixedIndex : function (idx){
		this.options.headerOptions.colFixedIndex = idx; 
		this._setThead(false);
		this.setData(this.options.tbodyItem, 'init_colFixed')
	}
	/**
     * @method getColFixedIndex
     * @description get column fixed index 
     */
	,getColFixedIndex : function (){
		return this.options.headerOptions.colFixedIndex;
	}
	,theadAsideHtml : function (){
		var _this = this
		
		var strHtm = [], colGroupHtm=[];
		
		var items =_this.config.aside.items;

		strHtm.push('<thead>');
		strHtm.push('<tr class="pub-header-tr">');
		for(var j=0 ;j < items.length; j++){
			var item = items[j];
			colGroupHtm.push('<col id="'+_this.prefix+'colhead'+items[j].key+'" style="width:'+item.width+'px;" />');

			strHtm.push('	<th>');
			strHtm.push('		<div class="aside-label-wrapper pub-header-'+item.key+'">'+item.name+'</div>');
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
	,drawGrid : function (pMode, unconditionallyFlag){
		var _this = this
			,tci = _this.config.tColItem
			,tbi = _this.options.tbodyItem
			,headerOpt=_this.options.headerOptions;

		pMode = pMode||'';
		var drawModeInfo =pMode.split('_');

		var drawMode = drawModeInfo[0];
		var subMode = drawModeInfo[1]||'';

		if(drawMode =='init'){
						
			if(subMode ==''){
				var templateHtm = _this.getTemplateHtml();
				templateHtm = templateHtm.replace('#theaderAsideHtmlArea#',_this.theadAsideHtml('aside'));
				templateHtm = templateHtm.replace('#theaderLeftHtmlArea#',_this.theadHtml('left'));
				templateHtm = templateHtm.replace('#theaderHtmlArea#',_this.theadHtml('cont'));
			
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

				// query selector 
				_this.element.leftContent = $pubSelector('#'+_this.prefix+'_bodyContainer .pubGrid-body-left-cont');
				_this.element.bodyContent = $pubSelector('#'+_this.prefix+'_bodyContainer .pubGrid-body-cont');
				
				// header html 만들기
				if(headerOpt.view===false){
					_this.element.header.height(0).hide()
					$('#'+_this.prefix+'_vscroll .pubGrid-scroll-top-area').hide();
				}

				_this.setElementDimensionAndMessage();
				_this.calcDimension('init');
				
				_this.calcViewCol(0);

				_this._initHeaderEvent();
				_this._headerResize(headerOpt.resize.enabled);
				_this.scroll();

				_this._initBodyEvent();
				_this._setBodyEvent();
				_this.getTbodyHtml('init');
			}else{
				_this.element.header.find('.pubGrid-header-left-cont').empty().html(_this.theadHtml('left'));
				_this.element.header.find('.pubGrid-header-cont').empty().html(_this.theadHtml('cont'));
				
				_this._headerResize(headerOpt.resize.enabled);
				_this.getTbodyHtml('init_colfixed');
				
				_this.calcDimension('colfixed');
			}
		}

		if(tbi.length < 1){
			_this.element.body.find('.pubGrid-body-container').hide();
			_this.element.body.find('.pubGrid-empty-msg-area').show();
			return ; 
		}else{
			_this.element.body.find('.pubGrid-body-container').show()
			_this.element.body.find('.pubGrid-empty-msg-area').hide();
		}
		
		var itemIdx = _this.config.scroll.viewIdx;
		var viewCount = _this.config.scroll.viewCount;

		var startCol=this.config.scroll.startCol 
			, endCol=this.config.scroll.endCol;
		
		var tbiItem , thiItem;
		var viewCount = _this.config.scroll.viewCount; 
		
		var asideItem =_this.config.aside.items; 

		var colFixedIndex = this.options.headerOptions.colFixedIndex;
		var startCellInfo = _this.config.selection.startCell;

		function setSelectCell(row , col, addEle){
			var tdEle = addEle.parentElement; 
			if(startCellInfo.startIdx == row  && startCellInfo.startCol == col ){
				tdEle.classList.add('col-active');
				tdEle.classList.add('selection-start-col');
				return ; 
			}

			if(_this._isAllSelect()){
				if(_this.isAllSelectUnSelectPosition(row , col)){
					tdEle.classList.remove('col-active' );
				}else{
					tdEle.classList.add('col-active' );
				}
			}else{
				tdEle.classList.remove('col-active' );

				if(_this.isSelectPosition(row , col)){
					tdEle.classList.add('col-active' );
				}
			}

			return false; 
		}

		var fnAddStyle = _this.options.rowOptions.addStyle; 
		
		// aside number size check
		if(_this.options.asideOptions.lineNumber.enabled ===true){
			var itemViewMaxCnt = itemIdx+viewCount; 
			if((itemViewMaxCnt) >10000){
				var idxCharLen = (itemViewMaxCnt+'').length; 

				if(_this.config.aside.lineNumberCharLength != idxCharLen){
				
					_this.config.aside.lineNumberCharLength = idxCharLen;

					var asideWidth = idxCharLen * _this.options.asideOptions.lineNumber.charWidth; 

					$('#'+_this.prefix+'colhead'+_this.options.asideOptions.lineNumber.key).css('width',  asideWidth+'px');
					$('#'+_this.prefix+'colbody'+_this.options.asideOptions.lineNumber.key).css('width',  asideWidth+'px');

					_this.config.gridWidth.aside = _this.config.aside.initWidth - _this.options.asideOptions.lineNumber.width + asideWidth;
					_this.calcDimension('resize');
				}
			}else{
				if(_this.config.aside.lineNumberCharLength != 0){
					_this.config.gridWidth.aside = _this.config.aside.initWidth;

					$('#'+_this.prefix+'colhead'+_this.options.asideOptions.lineNumber.key).css('width',  _this.options.asideOptions.lineNumber.width+'px');
					$('#'+_this.prefix+'colbody'+_this.options.asideOptions.lineNumber.key).css('width',  _this.options.asideOptions.lineNumber.width+'px');

					_this.config.aside.lineNumberCharLength= 0; 
					_this.calcDimension('resize');
				}
			}
		}
		
		// row color change
		if(itemIdx%2==0){
			_this.element.body.find('.pubGrid-body-container').removeClass('even');
		}else{
			_this.element.body.find('.pubGrid-body-container').addClass('even');
		}
		
		for(var i =0 ; i < viewCount; i++){
			tbiItem = tbi[itemIdx] ||{};
			
			if(_this.config.rowOpt.isAddStyle){
				$pubSelector('#'+_this.prefix+'_bodyContainer .pubGrid-body-cont [rowinfo="'+i+'"]').setAttribute('style', (fnAddStyle.call(null,tbiItem)||''));
			}
			
			var overRowFlag = (itemIdx >= this.config.dataInfo.orginRowLen)
			var addEle ,tdEle;

			for(var j =0 ; j < asideItem.length ;j++){
				var tmpItem = asideItem[j]; 
				var rowCol = i+','+tmpItem.key;
				
				addEle =$pubSelector('#'+_this.prefix+'_bodyContainer .pubGrid-body-aside-cont').querySelector('[data-aside-position="'+rowCol+'"]>.aside-content');
				
				if(tmpItem.key == 'lineNumber'){
					addEle.textContent = (itemIdx+1);	
				}else if(tmpItem.key == 'checkbox'){
					addEle.innerHTML = '<input type="checkbox" class="pub-row-check" '+(tbiItem['_pubcheckbox']?'checked':'')+'/>';	
				}else if(tmpItem.key == 'modify'){
					addEle.innerHTML = 'V';
				}
			};
			
			if(drawMode != 'hscroll'){
				for(var j=0; j < colFixedIndex ; j++){
					tdEle =_this.element.leftContent.querySelector('[data-grid-position="'+(i+','+j)+'"]');
					addEle =tdEle.querySelector('.pub-content');
					
					_this._setCellStyle(tdEle, i ,tci[j] , tbiItem)

					if(overRowFlag){
						addEle.textContent='';
					}else{
						this.valueFormatter( i, tci[j],tbiItem , addEle); 
						setSelectCell(itemIdx , j ,  addEle);
					}
					
					tdEle = null; 
					addEle = null; 
				}
			}
			
			for(var j=startCol ;j <= endCol; j++){
				//var addEle = _this.element.tdEle[rowCol] = $pubSelector('#'+_this.prefix+'_bodyContainer .pubGrid-body-tbody').querySelector('[data-grid-position="'+rowCol+'"]>.pub-content')
				
				tdEle =_this.element.bodyContent.querySelector('[data-grid-position="'+(i+','+j)+'"]');
				
				if(tdEle){
					addEle =tdEle.querySelector('.pub-content');

					_this._setCellStyle(tdEle, i ,tci[j] , tbiItem)

					if(overRowFlag){
						addEle.textContent='';
					}else{
						this.valueFormatter( i, tci[j],tbiItem , addEle);
						setSelectCell(itemIdx , j ,  addEle);
					}
				}
				tdEle = null; 
				addEle = null; 
			}
			itemIdx++;
		}
		
		if(itemIdx > this.config.dataInfo.orginRowLen){
			_this.element.container.find('[rowinfo="'+(viewCount-1)+'"]').hide();
		}else{
			_this.element.container.find('[rowinfo="'+(viewCount-1)+'"]').show();
		}
		
		_this._statusMessage(viewCount);	
	}
	/**
     * @method setElementDimensionAndMessage
	 * @description 그리드 element 수치 및 메시지 처리.
     */
	,setElementDimensionAndMessage : function (){
		this.config.header.height = this.element.header.outerHeight();
		var header_height = this.config.header.height; 

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

		if(this.config.dataInfo.orginRowLen < 1){
			pubGridClass +=' pubGrid-no-item';
		}

		this.element.pubGrid.addClass(pubGridClass)
		this.element.header.find('.pubGrid-header-aside-cont').css({'line-height': (header_height-1)+'px' , 'height' : (header_height-1)+'px'})
		//this.element.header.find('.aside-label-wrapper').css('height',header_height-1+'px'); // 20190311

		$('#'+this.prefix+'_vscroll .pubGrid-scroll-top-area').css({'line-height': header_height+'px' , 'height' : header_height+'px'});
			
		$('#'+this.prefix+'_vscroll').css({'width' : this.options.scroll.vertical.width , 'padding-top' :  header_height});
		$('#'+this.prefix+'_hscroll').css({'height' : this.options.scroll.horizontal.height , 'padding-left' : this.config.gridWidth.aside});
		
		if(this.options.tbodyItem.length < 1){
			// empty message
			if(isFunction(this.options.message.empty)){
				this.element.body.find('.pubGrid-empty-msg').empty().html(this.options.message.empty());	
			}else{
				this.element.body.find('.pubGrid-empty-msg .empty-text').empty().html(this.options.message.empty);
			}
		}
	}
	/**
     * @method calcDimension
	 * @param  type {String}  타입.
	 * @param  opt {Object}  옵션.
     * @description 그리드 수치 계산 . 
     */
	, calcDimension : function (type, opt){

		var _this = this; 
		var dimension;
		
		if(type =='headerResize'){
			 
			dimension = {width : _this.config.container.width, height : _this.config.container.height}; 
		}else{
			dimension = {width : _this.gridElement.innerWidth(), height :(_this.options.height =='auto' ? _this.gridElement.height() : _this.options.height)};
		}
		
		opt = $.extend(true, dimension ,opt);

		if(type =='init'  ||  type =='resize'){
			_this.element.pubGrid.css('height',opt.height+'px');
			_this.element.pubGrid.css('width',opt.width+'px');
			
			if(_this.config.container.width != opt.width && type=='resize' && _this.options.autoResize !==false && _this.options.autoResize.responsive ===true){
			
				var _totW = _this.config.gridWidth.total; 
				var _overW = (_totW+_this.options.scroll.vertical.width) - _this.config.container.width;
	
				if(_overW > 0){
					_overW = (opt.width*(_overW/_this.config.container.width*100)/100);
				}else{
					_overW = 0; 
				}
				
				var _reiszeW = (opt.width+_overW-_this.options.scroll.vertical.width) -(_totW); 
				var _currGridMain = _this.config.gridWidth.main;
				var _addW = 0 ; 
				_overW = _overW > 0 ? _overW : 0;

				var colItems = _this.config.tColItem;

				for(var i=0 ,colLen  = colItems.length; i<colLen; i++){
					var colItem = colItems[i]; 
					_addW = (_reiszeW*(colItem.width/_currGridMain*100)/100);
					
					var colResizeW = colItem.width = colItem.width + _addW;

					if(i == colLen-1){
						$('#'+_this.prefix+'colHeader'+i).css('width', (colResizeW-2) +'px');
						$('#'+_this.prefix+'colbody'+i).css('width', (colResizeW-2) +'px');
					}else{
						$('#'+_this.prefix+'colHeader'+i).css('width',colResizeW+'px');
						$('#'+_this.prefix+'colbody'+i).css('width',colResizeW +'px');
					}				
				}

				_this.config.gridWidth.main = _currGridMain +(_reiszeW);
			}
		}

		_this.config.gridWidth.total = _this.config.gridWidth.aside+_this.config.gridWidth.left+ _this.config.gridWidth.main;
		
		_this.config.container.width = opt.width;
		_this.config.container.height = opt.height;
		
		var mainHeight = opt.height - this.config.navi.height;
		_this.element.container.css('height',mainHeight-2);

		var  bodyW = (_this.config.container.width-this.options.scroll.vertical.width)
			, hScrollFlag = _this.config.gridWidth.total > bodyW  ? true : false
			, bodyH = mainHeight - this.config.header.height - this.config.footer.height -(hScrollFlag?(_this.options.scroll.horizontal.height-1):0)
			, itemTotHeight = _this.config.dataInfo.rowLen * _this.config.rowHeight
			, vScrollFlag = itemTotHeight > bodyH ? true :false;

		
		_this._setPanelElementWidth();
		
		_this.element.body.find('.pubGrid-empty-msg-area').css('line-height',(bodyH)+'px');
		_this.element.body.css('height',(bodyH)+'px');

		//console.log(vScrollFlag,mainHeight , this.config.header.height , this.config.footer.height ,hScrollFlag, (hScrollFlag?this.options.scroll.horizontal.height:0))

		var beforeViewCount = _this.config.scroll.viewCount ; 
		_this.config.scroll.viewCount = itemTotHeight > bodyH ? Math.ceil(bodyH / this.config.rowHeight) : _this.config.dataInfo.rowLen;
		_this.config.scroll.bodyHeight = bodyH;
		_this.config.scroll.insideViewCount = Math.floor(bodyH/this.config.rowHeight);
		
		var topVal = 0 ; 
		if(vScrollFlag && _this.config.dataInfo.orginRowLen >= _this.config.scroll.viewCount){
			_this.config.scroll.vUse = true;
			$('#'+_this.prefix+'_vscroll').css('padding-bottom',(hScrollFlag?(_this.options.scroll.horizontal.height-1):0));
			$('#'+_this.prefix+'_vscroll').show();
			
			var scrollH = $('#'+_this.prefix+'_vscroll').find('.pubGrid-vscroll-bar-area').height();
			
			var barHeight = (scrollH*(bodyH/(itemTotHeight)*100))/100; 
			if(scrollH <25){
				barHeight = 1;
			}else{
				barHeight = barHeight < 25 ? 25 : ( barHeight > scrollH ?scrollH :barHeight);	
			}
			
			_this.config.scroll.vHeight = scrollH; 
			_this.config.scroll.vThumbHeight = barHeight; 
			_this.config.scroll.vTrackHeight = scrollH - barHeight;
			_this.config.scroll.oneRowMove = _this.config.scroll.vTrackHeight/(_this.config.dataInfo.rowLen-this.config.scroll.viewCount);
						
			topVal = _this.config.scroll.vTrackHeight* _this.config.scroll.vBarPosition/100;
			
			_this.element.vScrollBar.css('height',barHeight);
			if(scrollH < 25){
				_this.element.vScrollBar.hide();
			}else{
				_this.element.vScrollBar.show();
			}
		}else{
			_this.config.scroll.vUse = false; 
			$('#'+_this.prefix+'_vscroll').hide();
		}

		var leftVal =0;
		if(hScrollFlag){
			_this.config.scroll.hUse = true; 
			$('#'+_this.prefix+'_hscroll').css('padding-right',(vScrollFlag?_this.options.scroll.vertical.width:0));
			$('#'+_this.prefix+'_hscroll').show();

			var hscrollW = $('#'+_this.prefix+'_hscroll').find('.pubGrid-hscroll-bar-area').width();

			var barWidth = (hscrollW*(bodyW/_this.config.gridWidth.total*100))/100; 
			barWidth = barWidth < 25 ? 25 :( barWidth > hscrollW ?hscrollW :barWidth);
			
			_this.config.scroll.hThumbWidth = barWidth;
			_this.config.scroll.hTrackWidth =hscrollW - barWidth;
			_this.config.scroll.oneColMove = _this.config.gridWidth.total/_this.config.scroll.hTrackWidth;
			leftVal = _this.config.scroll.hTrackWidth* _this.config.scroll.hBarPosition/100;
			_this.element.hScrollBar.css('width',barWidth)
		}else{
			_this.config.scroll.hUse= false; 
			$('#'+_this.prefix+'_hscroll').hide();
		}

		// main inside width
		_this.config.gridWidth.mainInsideWidth = (_this.config.container.width - _this.config.gridWidth.aside - (_this.config.scroll.vUse?_this.options.scroll.vertical.width:0)); 
		// main over width value
		_this.config.gridWidth.mainOverWidth = _this.config.gridWidth.main-(_this.config.container.width - _this.config.gridWidth.aside); 


		if(_this.config.scroll.maxViewCount <_this.config.scroll.viewCount  ) _this.config.scroll.maxViewCount = _this.config.scroll.viewCount;
						
		var drawFlag =false; 

		if(type !='init' && ( beforeViewCount != _this.config.scroll.viewCount )){
			drawFlag = _this.getTbodyHtml(); 
		}

		if(this.config.scroll.startCol != this.config.scroll.before.startCol || this.config.scroll.before.endCol != this.config.scroll.endCol ){
			drawFlag = true; 
		}

		if(type =='reDraw'){
			topVal=0; 
			leftVal=0; 
		}else if(type == 'addData'){
			if(_this.config.scroll.vUse){
				topVal = _this.config.scroll.viewIdx * _this.config.scroll.oneRowMove;
			}
		}

		if(type=='resize' ||type =='headerResize'){
			_this.moveVerticalScroll({pos :topVal, drawFlag : false,resizeFlag:true});
			_this.moveHorizontalScroll({pos :leftVal, drawFlag : false,resizeFlag:true});

			this.calcViewCol(_this._getBodyContainerLeft(leftVal));
			_this.drawGrid();
		}else{
			_this.moveVerticalScroll({pos :topVal, drawFlag : false});
			_this.moveHorizontalScroll({pos :leftVal, drawFlag : false});
			if(beforeViewCount !=0 ){
				if(type !='reDraw' && drawFlag){
					_this.drawGrid();
				}
			}
		}
	}
	/**
     * @method _setPanelElementWidth
     * @description panel width 셋팅.
     */
	,_setPanelElementWidth : function (){
		var _this = this; 

		// header grid set width
		_this.element.header.find('.pubGrid-header-aside-cont').css('width',(_this.config.gridWidth.aside)+'px');
		_this.element.header.find('.pubGrid-header-left-cont').css('width',(_this.config.gridWidth.left)+'px');
		_this.element.header.find('.pubGrid-header-cont').css('width',(_this.config.gridWidth.main-_this.options.borderSpace)+'px');
		
		// body grid set width
		_this.element.body.find('.pubGrid-body-aside-cont').css('width',(_this.config.gridWidth.aside)+'px');
		_this.element.body.find('.pubGrid-body-left-cont').css('width',(_this.config.gridWidth.left)+'px');
		_this.element.body.find('.pubGrid-body-cont').css('width',(_this.config.gridWidth.main- _this.options.borderSpace)+'px');
		
		// set horizontal scroll padding 
		$('#'+_this.prefix+'_hscroll').css('padding-left' , _this.config.gridWidth.aside);
	}
	/**
     * @method scroll
     * @description 스크롤 컨트롤.
     */
	,scroll : function (){
		var _this = this
			,_conf = _this.config;
			
		_this.element.body.off('mousewheel DOMMouseScroll');
		_this.element.body.on('mousewheel DOMMouseScroll', function(e) {
			var oe = e.originalEvent;
			var delta = 0;
		
			if (oe.detail) {
				delta = oe.detail * -40;
			}else{
				delta = oe.wheelDelta;
			};

			//delta > 0--up
			if(_this.config.scroll.vUse){
				_this.moveVerticalScroll({pos :(delta > 0? 'U' :'D') , speed : _this.options.scroll.vertical.speed});

				if(_this.options.scroll.isPreventDefault === true){
					e.preventDefault();
				}else if(_this.config.scroll.top != 0 && _this.config.scroll.top != _this.config.scroll.vTrackHeight){
					e.preventDefault();
				}
			}else{
				if(_this.config.scroll.hUse){
					_this.moveHorizontalScroll({pos :(delta > 0?'L':'R') , speed : _this.options.scroll.horizontal.speed});
					
					if(_this.options.scroll.isPreventDefault===true){
						e.preventDefault();
					}else{
						if(_this.config.scroll.left != 0 && _this.config.scroll.left != _this.config.scroll.hTrackWidth){
							e.preventDefault();
						}
					}
				}
			}
		});
		
		var scrollTimer , mouseDown = false
			,vBgDelay = _this.options.scroll.vertical.bgDelay
			,hBgDelay = _this.options.scroll.horizontal.bgDelay;
		
		$('#'+_this.prefix+'_vscroll .pubGrid-vscroll-bar-bg').off('mousedown touchstart mouseup touchend mouseleave');
		$('#'+_this.prefix+'_vscroll .pubGrid-vscroll-bar-bg').on('mousedown touchstart',function(e) {
			mouseDown = true;
			var evtY = e.offsetY;
			var upFlag =evtY> _this.config.scroll.top ? false :true;
			var loopcount =5;
			
			function scrollMove(pEvtY, pTop, vThumbHeight, oneRowMove){
				clearTimeout(scrollTimer);
				
				pTop= pTop+((upFlag?-1:1) * (oneRowMove *loopcount));

				if(upFlag){
					if( pEvtY >= pTop ){
						mouseDown = false
						pTop = pEvtY;
					}
				}else{
					if(pEvtY <= (pTop +vThumbHeight)){
						mouseDown = false
						pTop = pEvtY-vThumbHeight;
					}
				}

				_this.moveVerticalScroll({pos :pTop});

				if(mouseDown){
					scrollTimer = setTimeout(function() {
						scrollMove(pEvtY, pTop, vThumbHeight, oneRowMove*_this.options.scroll.horizontal.speed);
					}, vBgDelay);
				}
			}scrollMove(evtY, _this.config.scroll.top, _this.config.scroll.vThumbHeight, _this.config.scroll.oneRowMove *_this.options.scroll.horizontal.speed);
			
		}).on('mouseup touchend mouseleave',function(e) {
			mouseDown = false;
			clearTimeout(scrollTimer);
		});
		
		$('#'+_this.prefix+'_hscroll .pubGrid-hscroll-bar-bg').off('mousedown touchstart mouseup touchend mouseleave');
		$('#'+_this.prefix+'_hscroll .pubGrid-hscroll-bar-bg').on('mousedown touchstart',function(e) {
			mouseDown = true;
			var evtX = e.offsetX;
			var leftFlag =evtX > _this.config.scroll.left ? false :true;
			var loopcount =10;
			
			function scrollMove(pEvtX, pLeft, hThumbWidth, oneColMove){
				clearTimeout(scrollTimer);

				pLeft = pLeft+((leftFlag?-1:1) * (oneColMove*loopcount)); 

				if(leftFlag){
					if( pEvtX >= pLeft ){
						mouseDown = false
						pLeft = pEvtX;
					}
				}else{
					if(pEvtX <= (pLeft +hThumbWidth)){
						mouseDown = false
						pLeft = pEvtX-hThumbWidth;
					}
				}
				
				_this.moveHorizontalScroll({pos : pLeft});
				
				if(mouseDown){
					scrollTimer = setTimeout(function() {
						scrollMove(pEvtX, pLeft, hThumbWidth, oneColMove);
					}, hBgDelay);
				}
			}scrollMove(evtX,_this.config.scroll.left , _this.config.scroll.hThumbWidth, _this.config.scroll.oneColMove);
			
		}).on('mouseup touchend mouseleave',function(e) {
			mouseDown = false;
			clearTimeout(scrollTimer);
		});
		
		var scrollbarDragTimeer; 
		// 가로 스크롤 bar drag
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
				clearTimeout(scrollbarDragTimeer)
				scrollbarDragTimeer = setTimeout(function() {
					_this.horizontalScroll(data, e, 'move');
				}, 5);
			}).on('touchend.pubhscroll mouseup.pubhscroll mouseleave.pubhscroll', function (e){
				ele.removeClass('active');
				_this.horizontalScroll(data,e, 'end');
			});

			return true; 
		});
		
		// 세로 스크롤 바 .
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
				clearTimeout(scrollbarDragTimeer)
				scrollbarDragTimeer = setTimeout(function() {
					_this.verticalScroll( data,e , 'move');
				}, 7);
			}).on('touchend.pubvscroll mouseup.pubvscroll mouseleave.pubvscroll', function (e){
				ele.removeClass('active');
				clearTimeout(scrollbarDragTimeer);
				_this.verticalScroll(data, e , 'end');
			});

			return true; 
		}); 

		// 가로 스크롤 방향키
		var scrollBtnTimer
			,vBtnDelay = _this.options.scroll.vertical.btnDelay
			,hBtnDelay = _this.options.scroll.horizontal.btnDelay;

		$('#'+_this.prefix+'_hscroll .pubGrid-hscroll-btn').off('mousedown touchstart mouseup touchend mouseleave');
		$('#'+_this.prefix+'_hscroll .pubGrid-hscroll-btn').on('mousedown touchstart',function(e) {
			var sEle = $(this)
				,mode = sEle.attr('data-pubgrid-btn');

			scrollBtnTimer = setInterval(function() {
				_this.moveHorizontalScroll({pos :mode});
			}, hBtnDelay);
		}).on('mouseup touchend mouseleave',function(e) {
			clearInterval(scrollBtnTimer);
		});
		
		//세로 스크롤 방향키
		$('#'+_this.prefix+'_vscroll .pubGrid-vscroll-btn').off('mousedown touchstart mouseup touchend mouseleave');
		$('#'+_this.prefix+'_vscroll .pubGrid-vscroll-btn').on('mousedown touchstart',function(e) {
			var sEle = $(this)
				,mode = sEle.attr('data-pubgrid-btn');
			
			scrollBtnTimer = setInterval(function() {
				_this.moveVerticalScroll({pos :mode});
			}, vBtnDelay);
		}).on('mouseup touchend mouseleave',function(e) {
			clearInterval(scrollBtnTimer);
		});
	}
	/**
	* 세로 스크롤 드래그 이동 
	*/
	,verticalScroll : function (data,e, type){
		var oe = e.originalEvent.touches
		,oy = oe ? oe[0].pageY : e.pageY;

		oy = data.top+(oy - data.pageY);
		
		this.moveVerticalScroll({pos :oy});
		if(type=='end'){
			$(document).off('touchmove.pubvscroll mousemove.pubvscroll').off('touchend.pubvscroll mouseup.pubvscroll mouseleave.pubvscroll');
		}
	}
	/**
	* @method moveVerticalScroll
	* @param  moveObj.pos {String ,Integer} 'U' or 'D' or top position
	* @param  moveObj.resizeFlag {boolean} resize flag
	* @param  moveObj.drawFlag {boolean} redraw flag
	* @param  moveObj.speed {Integer} row move count
	* @param  moveObj.rowIdx {Integer} move row idx
    * @description 세로 스크롤 이동.
	*/
	,moveVerticalScroll : function (moveObj){
		var _this =this
			,opt = _this.options; 

		if(!_this.config.scroll.vUse && moveObj.resizeFlag !== true){ 
			_this.config.scroll.viewIdx = 0;
			return ; 
		}

		var posVal=moveObj.pos 
			,speed = moveObj.speed || 1
			,drawFlag = moveObj.drawFlag
			,rowIdx = moveObj.rowIdx;
		
		var topVal = posVal; 

		if(!isNaN(rowIdx)){
			topVal =rowIdx * _this.config.scroll.oneRowMove;
		}else{
			if(isNaN(posVal)){
				topVal =_this.config.scroll.top+((topVal=='U'?-1:1)* speed * _this.config.scroll.oneRowMove);
			}
		}
		
		this.moveVScrollPosition(topVal, moveObj.drawFlag);
	}
	/**
	*세로 스크롤 위치 이동.
	*/
	,moveVScrollPosition : function (topVal, drawFlag, updateChkFlag){

		var barPos = 0 ; 

		if( topVal> 0){
			if(topVal >= this.config.scroll.vTrackHeight ){
				topVal = this.config.scroll.vTrackHeight;
			}
			barPos = topVal/this.config.scroll.vTrackHeight*100; 
		}else {
			topVal = 0;
			barPos = 0 ; 
		}
		
		if(this.config.scroll.top ==topVal){
			return ; 
		}
		
		if(updateChkFlag !== false){
			var onUpdateFn = this.options.scroll.vertical.onUpdate; 
			if(drawFlag !== false && isFunction(onUpdateFn)){
				if(onUpdateFn.call(null, {scrollTop : topVal, height :  this.config.scroll.vTrackHeight , barPosition : barPos }) === false){
					return ; 
				}
			}
		}

		this._setScrollBarTopPosition(topVal);
		
		var itemIdx =0;

		if(topVal > 0){
			var tmpRowHeight = this.config.rowHeight; 
			itemIdx = topVal/(this.config.scroll.vTrackHeight / (this.config.dataInfo.rowLen-this.config.scroll.viewCount));
			itemIdx  = Math.round(itemIdx); 
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
	* vertical scroll top postion setting
	*/
	,_setScrollBarTopPosition : function (topVal){
		this.config.scroll.top = topVal; 
		this.element.vScrollBar.css('top', topVal);
	}
	/**
	* 가로 스크롤 드래그 이동
	*/
	,horizontalScroll : function (data ,e, type){
		var oe = e.originalEvent.touches
		,ox = oe ? oe[0].pageX : e.pageX;
		ox = data.left+(ox - data.pageX);
		
		this.moveHorizontalScroll({pos : ox});

		if(type=='end'){
			$(document).off('touchmove.pubhscroll mousemove.pubhscroll').off('touchend.pubhscroll mouseup.pubhscroll mouseleave.pubhscroll');
		}
	}
	/**
	* @method moveHorizontalScroll
	* @param  moveObj.pos {String ,Integer} 'L' or 'R' or left position
	* @param  moveObj.resizeFlag {boolean} resize flag
	* @param  moveObj.drawFlag {boolean} redraw flag
	* @param  moveObj.speed {Integer} row move count
    * @description 가로 스크롤 이동.
	*/
	,moveHorizontalScroll : function (moveObj){
		var _this =this; 

		if(!_this.config.scroll.hUse && moveObj.resizeFlag !== true){ return ; }

		var posVal = moveObj.pos;

		var leftVal= posVal;
		
		if(isNaN(posVal)){

			if(isUndefined(moveObj.colIdx)){
				leftVal =_this.config.scroll.left+((posVal=='L'?-1:1) * _this.config.scroll.oneColMove);
			}else{
				var leafHeaderIdx = _this.config.headerInfo.length-1; 

				var colIdxHeaderEle = _this.element.header.find('.pubGrid-header-cont [data-header-info="'+leafHeaderIdx+','+moveObj.colIdx+'"]');
				var headerPos = colIdxHeaderEle.position();
				
				if(posVal =='L'){
					leftVal = headerPos.left-this.options.borderSpace;
				}else{
					leftVal = headerPos.left + colIdxHeaderEle.outerWidth();
					leftVal = leftVal - (_this.config.gridWidth.mainInsideWidth);

					leftVal = leftVal +this.options.borderSpace+2; // 여백 3 추가. 
				}
				
				leftVal = leftVal/(_this.config.gridWidth.mainOverWidth) *100;
				leftVal = (leftVal * _this.config.scroll.hTrackWidth/100);
			}
		}
		
		this.moveHScrollPosition(leftVal, moveObj.drawFlag);
	}
	/**
     * @method _getBodyContainerLeft
	 * @param leftVal {Integer} body left position
	 * @param drawFlag {Boolean} draw flag
	 * @param updateChkFlag {Boolean} 업데이트 여부.
     * @description 가로 스크롤바 위치 이동
     */
	,_getBodyContainerLeft : function (leftVal){
		return ((this.config.gridWidth.total - this.config.container.width)*(leftVal/this.config.scroll.hTrackWidth*100))/100; 
	}
	/**
     * @method moveHScrollPosition
	 * @param leftVal {Integer} body left position
	 * @param drawFlag {Boolean} draw flag
	 * @param updateChkFlag {Boolean} 업데이트 여부.
     * @description 가로 스크롤바 위치 이동
     */
	,moveHScrollPosition : function (leftVal, drawFlag, updateChkFlag){
		var hw = this.config.scroll.hTrackWidth;
		leftVal = leftVal > 0 ? (leftVal >= hw ? hw : leftVal) : 0 ; 

		if(this.config.scroll.left ==leftVal){
			return ; 
		}

		var headerLeft  = this._getBodyContainerLeft(leftVal); 

		if(leftVal == hw ){
			headerLeft= headerLeft +(this.config.scroll.vUse?this.options.scroll.vertical.width:0) +1; // 여백 3 추가. 
		}

		this._setScrollBarLeftPosition(leftVal);
		this.config.scroll.hBarPosition = leftVal/hw*100; 

		if(updateChkFlag !== false){
			var onUpdateFn = this.options.scroll.horizontal.onUpdate; 
			if(drawFlag !== false && isFunction(onUpdateFn)){
				if(onUpdateFn.call(null, {scrollLeft : leftVal , width : this.config.scroll.hTrackWidth, barPosition : this.config.scroll.hBarPosition}) === false){
					return ; 
				}
			}
		}
		
		this.calcViewCol(headerLeft);

		this.element.header.find('.pubGrid-header-cont-wrapper').css('left','-'+headerLeft+'px');
		this.element.body.find('.pubGrid-body-cont-wrapper').css('left','-'+headerLeft+'px');
		
		if(drawFlag !== false){
			this.drawGrid('hscroll');
		}
	}
	/**
	* 가로 스크롤 바 위치 셋팅
	*/
	, _setScrollBarLeftPosition : function (leftVal){
		this.config.scroll.left = leftVal; 
		this.element.hScrollBar.css('left',leftVal);
	}
	/**
     * @method calcViewCol
	 * @param leftVal {Integer} body left position
     * @description view col 위치 구하기.
     */
	,calcViewCol : function (leftVal){
		var tci = this.config.tColItem; 
		var gridW = leftVal+this.config.gridWidth.mainInsideWidth; 
		var itemLeftVal=0;
		var startCol = 0, endCol =tci.length-1;
		var startFlag = true, inSideStartFlag = true; 
		for(var i =0 ;i <tci.length ;i++){

			if(inSideStartFlag && itemLeftVal >= leftVal){
				this.config.scroll.insideStartCol = i; 
				inSideStartFlag = false;
			}

			itemLeftVal +=tci[i].width; 
						
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


		// 화면에 다 보이는 col size
		this.config.scroll.insideEndCol = this.config.scroll.endCol + ( itemLeftVal != gridW ? -1 : 0 );
	}
	,_statusMessage : function (viewCnt){
		var startVal = this.config.scroll.viewIdx +1
			,endVal = isNaN(viewCnt)? (startVal+ this.config.scroll.viewCount) : (startVal+ viewCnt);
		this.element.status.empty().html(this.options.message.pageStatus({
			currStart :startVal
			,currEnd : (endVal-1)
			,total : this.config.dataInfo.rowLen
		}))
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
     * @method getCheckItems
     * @description check item 값 얻기.
     */
	,getCheckItems: function (){
		var tbodyItem =this.options.tbodyItem; 
		var reval = [];
		for(var i =0, len=tbodyItem.length;i < len; i++){
			var item = tbodyItem[i]; 
			if(item['_pubcheckbox']===true){
				reval.push(item)
			}
		}
		return reval;
	}
	/**
     * @method setCheckItems
	 * @param  idxArr {Array} integer array
	 * @param  checkFlag {Bollean} check flag
     * @description set checkbox
     */
	,setCheckItems: function (idxArr, checkFlag){
		var tbodyItem =this.options.tbodyItem; 
		var item;
		if(idxArr=='all'){
			for(var i =0, len=tbodyItem.length;i < len; i++){
				tbodyItem[i]['_pubcheckbox']= checkFlag; 
			}
		}else{
			for(var i =0, len=idxArr.length;i < len; i++){
				item = tbodyItem[idxArr[i]];
				if(item) item['_pubcheckbox'] = true; 
			}
		}

		this.drawGrid();
	}
	/**
     * @method _initHeaderEvent
     * @description 바디 이벤트 초기화.
     */
	,_initHeaderEvent : function (){
		var _this = this
			,headerOpt = _this.options.headerOptions;
		
		//headerCol.off('click.pubGridHeader.sort');
		
		var dataSortEvent = 'dblclick.pubGridHeader.sort';

		if(headerOpt.isColSelectAll ===true){
			// column selection
			_this.element.header.on('click.pubGridHeader.selection','.pub-header-cont',function (e){
				var selEle = $(this)
					,col_idx = selEle.attr('col_idx');
				
				var curr ='' , initFlag = true ; 
				if(e.ctrlKey){
					curr = 'add';
					initFlag  = false; 
				}else{
					_this.element.body.find('.pub-body-td.col-active').removeClass('col-active');
				}

				var rangeKey = 'col'+col_idx;

				_this._setSelectionRangeInfo({
					rangeInfo : {_key : rangeKey, startIdx : 0, endIdx : _this.config.dataInfo.lastRowIdx, startCol : col_idx,  endCol :col_idx}
					,isSelect : true
					,curr : curr
				}, initFlag , true);
			});
		}else{
			dataSortEvent = 'click.pubGridHeader.sort';
		}
		
		if(_this.options.asideOptions.rowSelector.enabled === true){
			// checkbox click
			_this.element.header.on('click.pubGrid.allcheck','.pub-header-checkbox',function (e){
				var checkFlag; 
				if($(this).hasClass('pub-check-all')){
					$(this).removeClass('pub-check-all');
					checkFlag = false; 
				}else{
					$(this).addClass('pub-check-all');
					checkFlag = true; 
				}

				_this.setCheckItems('all',checkFlag);
			});
		}
		
		var beforeClickObj; 
		// sort
		_this.element.header.on(dataSortEvent,'.pub-header-cont.sort-header',function (e){
			var selEle = $(this)
				,col_idx = selEle.attr('col_idx')
				,sortType = selEle.attr('sort_type');
			
			if(beforeClickObj){
				beforeClickObj.closest('.label-wrapper').removeClass('sortasc sortdesc');
				beforeClickObj.removeAttr('sort_type');
			}

			sortType = sortType =='asc' ? 'desc' : (sortType =='desc'?'':'asc');
			
			// col select background col setting
			if($('#'+_this.prefix+'colbody'+col_idx).attr('data-sort-flag') != 'Y'){
				$(_this.element.body.find('col[data-sort-flag]')).css('background-color','inherit').removeAttr('data-sort-flag');
				$('#'+_this.prefix+'colbody'+col_idx).attr('data-sort-flag','Y');
				$('#'+_this.prefix+'colbody'+col_idx).css('background-color','#b9dfdc !important');
			}
			
			selEle.attr('sort_type', sortType);
			
			var labelWrapperEle = selEle.closest('.label-wrapper');
			labelWrapperEle.removeClass('sortasc sortdesc');

			if(sortType !='' ){
				labelWrapperEle.addClass('sort'+sortType);
			}

			beforeClickObj = selEle;
		
			_this.setData(_this.getSortList(col_idx, sortType) ,'sort');
		});
		
		if(headerOpt.contextMenu !== false){
			$.pubContextMenu('#'+_this.prefix+'_headerContainer .pubGrid-header-th',headerOpt.contextMenu);			
		}
		if(_this.options.setting.enabled ===true){
			_this.setGridSettingInfo();
		}
	}
	/**
     * @method toggleSettingArea
     * @description settring enable/disable
     */
	,toggleSettingArea : function (){
		this.options.setting.enabled = !this.options.setting.enabled;
		this.setGridSettingInfo(!this.options.setting.enabled);
	}
	/**
     * @method setGridSettingInfo
     * @description grid setting info
     */
	,setGridSettingInfo : function (btnDisableFlag,layerEnableFlag ){
		var _this = this
			,headerOpt = _this.options.headerOptions
			,settingOpt = _this.options.setting; 
		
		var settingWrapper = _this.element.container.find('.pubGrid-setting-wrapper'); 
		
		if(layerEnableFlag===true){
			settingWrapper.addClass('open');
		}else{
			settingWrapper.removeClass('open');
		}

		if(btnDisableFlag == 'enable'){
		}else{
			if(btnDisableFlag ===true){
				settingWrapper.hide();
			}else{
				settingWrapper.show();
			}
		}

		if(_this.config.initSettingFlag ===true){
			return ; 
		}

		// 한번만 초기화 하기 위해서 처리.
		_this.config.initSettingFlag = true; 
		// grid setting btn	
		if(isFunction(settingOpt.click)){
			settingBtn.on('click', function (e){
				settingOpt.click.call(null,{evt :e , item :{}});	
			});
		}else{
			
			settingWrapper.find('.pubGrid-setting').on('click', function (e){
				if(settingWrapper.hasClass('open')){
					settingWrapper.removeClass('open');
				}else{
					settingWrapper.addClass('open');
				}				
			});
			
			if(settingOpt.enableSearch ===true){
				settingWrapper.find('[name="pubgrid_srh_filed"]').empty().html(_this.config.template['searchField']);
				settingWrapper.find('.pubGrid-search-area').show(); 
			}

			if(settingOpt.enableColumnFix ===true){
				settingWrapper.find('[name="pubgrid_col_fixed"]').empty().html('<option value="0">사용안함</option>'+_this.config.template['searchField']);
				settingWrapper.find('.pubGrid-colfixed-area').show(); 
			}

			var settingVal = {};

			if(settingOpt.enableSpeed ===true){
				settingWrapper.find('.pubGrid-speed-area').show();
				
				var strHtm = [];
				strHtm.push('<option value="-1">auto</option>');
				for(var i =1 ;i <=settingOpt.speedMaxVal;i++){
					strHtm.push('<option value="'+i+'">'+i+'</option>');
				}
				
				settingWrapper.find('[name="pubgrid_scr_speed"]').empty().html(strHtm.join(''));
			}

			
			var schFieldEle = settingWrapper.find('[name="pubgrid_srh_filed"]')
				,schSelEle = settingWrapper.find('[name="pubgrid_srh_val"]');
			
			if(settingOpt.configVal.search.field!=''){
				schFieldEle.val(settingOpt.configVal.search.field);
			}
			schSelEle.val(settingOpt.configVal.search.val);
			
			// 검색
			schSelEle.on('keydown',function(e) {
				if (e.keyCode == '13') {
					settingWrapper.find('[data-setting-mode="search"]').trigger('click');
					return false; 
				}
			});
			
			//속도 . 
			settingWrapper.on('click','[data-setting-mode]',function (e){
				var sEle = $(this)
					,btnMode = sEle.data('setting-mode');

				if('search' == btnMode){
					var schField = schFieldEle.val()
						,schVal = schSelEle.val();

					settingVal.search = {
						field : schField
						,val : schVal
					}
					
					var schArr = [];
					var orgData = _this.config.orginData;
					
					if($.trim(schVal)!=''){
						schVal = schVal.toLowerCase();

						for(var i =0 , len  = orgData.length; i < len;i++){
							if(settingOpt.util.searchFilter(orgData[i],schField,schVal)){
								schArr.push(orgData[i]);
							}
						}
						_this.setData(schArr,'search');	
					}else{
						_this.setData(_this.config.orginData,'search');	
					}

					settingOpt.configVal.search = settingVal.search;						
				}

				if(isFunction(settingOpt.callback)){
					settingOpt.callback.call(null,{evt :e , item : settingVal})
				}
			})

			// 스크롤 스피드 셋팅 
			settingWrapper.find('[name="pubgrid_scr_speed"]').on('change.scroll.speed', function (e){
				settingVal.speed = _this.setScrollSpeed($(this).val());
				
				settingWrapper.removeClass('open');

				if(isFunction(settingOpt.callback)){
					settingOpt.callback.call(null,{evt :e , item : settingVal})
				}
			})
			
			// 컬럼 고정. 
			settingWrapper.find('[name="pubgrid_col_fixed"]').on('change.colfixed.setting', function (e){
				var sEle = $(this);
				var sIndex = sEle.prop('selectedIndex');

				_this.setColFixedIndex(sIndex);

				settingWrapper.removeClass('open');
			})
		}
	}
	/**
     * @method getSelectionCellInfo
     * @description 선택된 cell 영역 구하기.
     */
	,getSelectionCellInfo :function(curr, allDataFlag){
		var selectionInfo = this.config.selection.range;

		var  startIdx = selectionInfo.startIdx
			,endIdx = selectionInfo.endIdx	
			,startCol = selectionInfo.startCol
			,endCol = selectionInfo.endCol
			,startRow = -1
			,endRow = -1;
			
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
     * @method _getSelectionModeColInfo
     * @description select mode col info
     */
	,_getSelectionModeColInfo : function (selectionMode ,colIdx ,dataInfo ,isMouseDown){
		var multipleFlag=false; 
		var _startCol=0 , _endCol =0; 

		if(selectionMode =='multiple-row'){
			multipleFlag = true;
			_startCol = 0;
			_endCol = dataInfo.colLen-1;
		}else if(selectionMode =='multiple-cell'){
			multipleFlag = true;

			if(isMouseDown){
				_startCol = -1;
			}else{
				_startCol = colIdx;
			}

			_endCol = colIdx;
		}else if(selectionMode =='row'){
			_startCol = 0;
			_endCol = dataInfo.colLen-1;
		}else{
			_startCol = colIdx;
			_endCol = colIdx;
		}

		return {multipleFlag :multipleFlag, startCol : _startCol, endCol : _endCol};
	}
	/**
     * @method _initBodyEvent
     * @description 바디 이벤트 초기화.
     */
	,_initBodyEvent : function (){
		var _this = this
			,asideOpt = _this.options.asideOptions
			,selectionMode= _this.options.selectionMode;
		
		if(asideOpt.lineNumber.isSelectRow === true){
			// column selection
			_this.element.body.find('.pubGrid-body-aside').on('click.pubGridLine.selection','.pub-lineNumber',function (e){
				var selEle = $(this)
					,row_idx = selEle.closest('tr').attr('rowinfo');
				
				var curr ='' , initFlag = true ; 
				if(e.ctrlKey){
					curr = 'add';
					initFlag  = false; 
				}else{
					_this.element.body.find('.pub-body-td.col-active').removeClass('col-active');
				}

				var rowIdx = _this.config.scroll.viewIdx+intValue(row_idx); 

				var rangeKey = 'row'+rowIdx;

				_this._setSelectionRangeInfo({
					rangeInfo : {_key : rangeKey, startIdx : rowIdx, endIdx : rowIdx, startCol : 0,  endCol :_this.config.itemColumnCount-1}
					,isSelect : true
					,curr : curr
				}, initFlag , true);
			});
		}

		if(asideOpt.rowSelector.enabled === true){
			// checkbox click
			_this.element.body.find('.pubGrid-body-aside').on('click.pubGrid.check','.pub-row-check',function (e){
				var selEle = $(this)
					,rowinfo = selEle.closest('tr').attr('rowinfo');

				rowinfo = _this.config.scroll.viewIdx+intValue(rowinfo);

				var selItem = _this.options.tbodyItem[rowinfo];

				selItem['_pubcheckbox'] = selItem['_pubcheckbox'] === true ? false :true;
			});
		}
		
	
		if(isFunction(_this.options.rowOptions.click)){

			var beforeRow; 
			_this.element.body.find('.pubGrid-body').on('click.pubgrid.row','.pub-body-tr',function (e){
				var selRow = $(this)
					,rowinfo=selRow.attr('rowinfo');
				
				rowinfo = _this.config.scroll.viewIdx+intValue(rowinfo);

				var selItem = _this.options.tbodyItem[rowinfo];
				
				if(beforeRow) beforeRow.removeClass('active');

				selRow.addClass('active');
				beforeRow = selRow; 
				
				 _this.options.rowOptions.click.call(selRow ,rowinfo , selItem);							
			});
		}

		if(isFunction(_this.options.bodyOptions.cellDblClick)){
			
			_this.element.body.find('.pubGrid-body').on('dblclick.pubgrid.td','.pub-body-td',function (e){
				var selRow = $(this)
					,tdInfo=selRow.data('grid-position')
					,rowColArr  = tdInfo.split(',');
				
				var rowIdx = _this.config.scroll.viewIdx+intValue(rowColArr[0])
					,colIdx = intValue(rowColArr[1]); 

				var rowItem = _this.options.tbodyItem[rowIdx];
								
				_this.options.bodyOptions.cellDblClick.call(selRow ,{item : rowItem ,r: rowIdx ,c:colIdx , keyItem : _this.config.tColItem[colIdx] } );
			});
		}

		_this._setSelectionRangeInfo({isMouseDown : false});
		
		_this.element.body.find('.pubGrid-body').on('mousedown.pubgrid.col','.pub-body-td',function (e){
			
			if(e.which ===3){
				return true; 
			}

			var sEle = $(this)
				,gridTdPos = sEle.attr('data-grid-position')
				,selCol = gridTdPos.split(',')
				,selRow = intValue(selCol[0])
				,colIdx = intValue(selCol[1]);
			
			var selIdx = _this.config.scroll.viewIdx+intValue(selRow);

			if(colIdx < _this.config.scroll.insideStartCol){
				_this.moveHorizontalScroll({pos:'L' ,colIdx :colIdx});
			}else if(colIdx > _this.config.scroll.insideEndCol){
				_this.moveHorizontalScroll({pos:'R' ,colIdx :colIdx});
			}
			
			var selectRangeInfo = _this._getSelectionModeColInfo( selectionMode ,colIdx , _this.config.dataInfo ,_this.config.selection.isMouseDown);
			
			var selItem = _this.options.tbodyItem[selRow];
			
			if(selectRangeInfo.multipleFlag && e.shiftKey) {	// shift key
				var rangeInfo = {endIdx: selIdx, endCol: selectRangeInfo.endCol};

				if(selectRangeInfo.startCol > -1){
					rangeInfo.srartCol = selectRangeInfo.startCol;
				}

				_this._setSelectionRangeInfo({
					rangeInfo : rangeInfo
					,isMouseDown : true
				},false , true);

			}else if(selectRangeInfo.multipleFlag && e.ctrlKey){ // ctrl key

				_this._setSelectionRangeInfo({
					rangeInfo : {startIdx : selIdx, endIdx : selIdx, startCol : selectRangeInfo.startCol, endCol: selectRangeInfo.endCol}
					,isSelect : true
					,curr : (_this.config.selection.isSelect?'add':'')
					,isMouseDown : true
				}, false, true);
			
			}else{
				_this._setSelectionRangeInfo({
					rangeInfo : {startIdx : selIdx, endIdx : selIdx, startCol : selectRangeInfo.startCol, endCol: selectRangeInfo.endCol}
					,isSelect : true
					,allSelect : false
					,isMouseDown : true
					,startCell : {startIdx : selIdx, startCol : colIdx}
				}, true, true);
			}
			
			// hidden row up
			if(selRow+1 > _this.config.scroll.insideViewCount){
				_this.moveVerticalScroll({pos: 'D'});
			}
			
			window.getSelection().removeAllRanges();
		
			if(isFunction(_this.config.tColItem[colIdx].colClick)){
				_this.config.tColItem[colIdx].colClick.call(this,colIdx,{
					r:selIdx
					,c:colIdx
					,item:selItem
				});
				return true; 
			}
			
			return true;

		}).on('mouseover.pubgrid.col','.pub-body-td',function (e) {
			
			if (!_this.config.selection.isMouseDown) return;
			
			if(!(selectionMode =='multiple-row' || selectionMode =='multiple-cell')){
				return ; 
			}

			var sEle = $(this)
				,selCol = sEle.attr('data-grid-position').split(',')
				,selRow = intValue(selCol[0])
				,colIdx = intValue(selCol[1]);

			var selectRangeInfo = _this._getSelectionModeColInfo( selectionMode ,colIdx , _this.config.dataInfo);

			_this._setSelectionRangeInfo({
				rangeInfo : {
					endIdx : _this.config.scroll.viewIdx+intValue(selRow)
					,endCol : selectRangeInfo.endCol
				}
			},false , true);
			
		})
				
		_this.element.pubGrid.on('mouseup.'+_this.prefix,function (e) {
			//_this.element.body.removeClass('pubGrid-noselect');
			_this._setSelectionRangeInfo({isMouseDown : false});
		})

		// focus in
		_this.element.pubGrid.on('mousedown.'+_this.prefix,function (e){
			_this.config.focus = true;
		})
		
		// focus out
		$(document).on('mousedown.'+_this.prefix, 'html', function (e) {
			if(e.which !==2 && $(e.target).closest('#'+_this.prefix+'_pubGrid').length < 1){
				_this.config.focus = false;
			}
		});
		
		// window keydown 처리.  tabindex 처리 확인 해볼것.
		$(window).on("keydown." + _this.prefix, function (e) {

			if(!_this.config.focus) return ;
			
			// 설정 영역 keydown 처리
			if($(e.target).closest('.pubGrid-setting-area').length > 0) return true; ;

			var evtKey = window.event ? e.keyCode : e.which;

			if (e.metaKey || e.ctrlKey) { // copy 

				if (evtKey == 67) { // ctrl+ c
					
					var copyData = _this.selectionData();
					try{
						copyStringToClipboard(_this.prefix, copyData);
					}catch(e){
						console.log('Unable to copy', e);					
					}					
				}else if(evtKey==65){ // ctrl + a
					if(	$(e.target).closest('#'+_this.prefix+'_pubGrid .pubGrid-setting-wrapper').length > 0){
						return true; 	
					}
					_this.allItemSelect();
					return false; 
				}
			}
			
			if( (32 < evtKey && evtKey <41) || evtKey == 13 || evtKey == 9){
				e.preventDefault();
				e.stopPropagation();

				_this.gridKeyCtrl(e, evtKey);
			}
		});
	}
	/**
     * @method gridKeyCtrl
     * @description key ctrl
     */
	,gridKeyCtrl : function (evt, evtKey){
		var _this  =this;

		var rangeInfo = _this.config.selection.range
			,scrollInfo = _this.config.scroll
			,dataInfo =_this.config.dataInfo
			,startCell = _this.config.selection.startCell;

		var endIdx = startCell.startIdx
			,endCol = startCell.startCol;
		
		switch(evtKey){
			case 34 : // PageDown
			case 13 : // enter
			case 40 : { //down
				
				if(endIdx+1 >= dataInfo.orginRowLen){
					if((endIdx > scrollInfo.viewIdx+scrollInfo.insideViewCount)){
						_this.moveVerticalScroll({pos: 'M',rowIdx :endIdx });
					}
					return ; 
				}

				var moveRow = (evtKey ==34 ? scrollInfo.insideViewCount: 1);  
				var moveRowIdx = (endIdx +moveRow);
					
				moveRowIdx = moveRowIdx >= dataInfo.orginRowLen ? dataInfo.orginRowLen-1 : moveRowIdx;

				setRangeInfo(evtKey,evt ,moveRowIdx,endCol);

				if(insideScrollCheck(endIdx, endCol, scrollInfo, moveRowIdx, endCol)){ // 스크롤 밖에 있을때
					return ; 
				}else if((moveRowIdx -scrollInfo.viewIdx) >= scrollInfo.insideViewCount){
					_this.moveVerticalScroll({pos: 'D',speed :moveRow });
				}
			
				break;
			}
			case 33 :  //PageUp
			case 38 : { //up
				
				if(endIdx <= 0){
					if(endIdx < scrollInfo.viewIdx){
						_this.moveVerticalScroll({pos: 'M',rowIdx :endIdx });
					}			
					return ; 
				}

				var moveRow = (evtKey ==33 ? scrollInfo.insideViewCount: 1);  
				var moveRowIdx = (endIdx - moveRow);
				
				moveRowIdx = moveRowIdx > 0 ? moveRowIdx : 0;

				setRangeInfo(evtKey,evt ,moveRowIdx,endCol);

				if(insideScrollCheck(endIdx, endCol, scrollInfo, moveRowIdx, endCol)){ // 스크롤 밖에 있을때
					return ; 
				}else if(moveRowIdx < scrollInfo.viewIdx){
					_this.moveVerticalScroll({pos:'U',speed :moveRow});
				}

				break; 
			}
			case 36 : // Home
			case 37 : { //left

				if(endCol <= 0){
					if(endCol < scrollInfo.startCol){
						_this.moveHorizontalScroll({pos: 'L',colIdx :endCol});
					}
					return ; 
				}

				var moveColIdx =(evtKey ==36 ? 0: endCol-1);
				
				moveColIdx = moveColIdx > 0 ? moveColIdx : 0;

				setRangeInfo(evtKey,evt ,endIdx,moveColIdx);
				
				if(insideScrollCheck(endIdx, endCol, scrollInfo, endIdx, moveColIdx)){ // 스크롤 밖에 있을때
					return ; 
				}else if(moveColIdx <= scrollInfo.startCol){
					_this.moveHorizontalScroll({pos:'L', colIdx :moveColIdx });
				}

				break; 
			}
			case 35 : // End
			case 9 : // tab
			case 39 : { //right
				if(endCol+1 >= dataInfo.colLen){
					if(endCol > scrollInfo.endCol){
						_this.moveHorizontalScroll({pos: 'R',colIdx :endCol});
					}

					return; 
				}
				
				var moveColIdx =(evtKey ==35 ? dataInfo.colLen-1 : endCol+1);

				setRangeInfo(evtKey,evt ,endIdx,moveColIdx);
				
				if(insideScrollCheck(endIdx, endCol, scrollInfo, endIdx, moveColIdx)){ // 스크롤 밖에 있을때
					return ; 
				}else if(moveColIdx >= scrollInfo.insideEndCol){
					_this.moveHorizontalScroll({pos:'R' ,colIdx :moveColIdx});
				}
						
				break; 
			}
			
			default:{
				break; 
			}
		}

		function setRangeInfo (evtKey, evt, endIdx, moveColIdx){
			
			var startCol=moveColIdx, endCol=moveColIdx; 
			
			if(_this.options.selectionMode =='multiple-row' || _this.options.selectionMode =='row'){
				startCol = 0; 
				endCol = _this.config.dataInfo.colLen-1;
			}
			
			if(evtKey != 9 && evt.shiftKey){
				_this._setSelectionRangeInfo({
					rangeInfo :  {endIdx : endIdx, endCol : endCol}
					,startCell : {startIdx : endIdx, startCol : moveColIdx}
				}, false,true);
			}else{
				_this._setSelectionRangeInfo({
					rangeInfo :  {startIdx : endIdx,endIdx : endIdx, startCol:startCol,endCol :endCol}
					,startCell : {startIdx : endIdx, startCol : moveColIdx}
				},true, true);
			}
		}

		// cursor scroll inside check
		function insideScrollCheck(endIdx, endCol,scrollInfo, moveRowIdx, moveColIdx){
			var reFlag = false; 
			if(endIdx < scrollInfo.viewIdx || (endIdx > scrollInfo.viewIdx+scrollInfo.insideViewCount)){ // 스크롤 밖에 있을때
				_this.moveVerticalScroll({pos: 'M',rowIdx :moveRowIdx });
				reFlag = true; 
			}
			
			if(endCol < scrollInfo.startCol){ // 스크롤 밖에 있을때
				_this.moveHorizontalScroll({pos: 'L',colIdx :moveColIdx});
				reFlag = true; 
			}else if(endCol > scrollInfo.endCol){
				_this.moveHorizontalScroll({pos: 'R',colIdx :moveColIdx});
				reFlag = true; 
			}
			return reFlag;
		}
	}
	/**
     * @method _setMouseDownFlag
     * @description mousedown flag 처리.
     */
	,_setMouseDownFlag :function(flag){
		this.config.selection.isMouseDown = flag;
	}
	/**
     * @method allItemSelect
     * @description 전체 선택.
     */
	,allItemSelect : function (){
		this.config.selection.allSelect = true; 
		this.element.body.find('.pub-body-td').addClass('col-active');
		return ; 
	}
	/**
     * @method _setSelectionRangeInfo
     * @description 선택 영역 정보 셋팅.
     */
	,_setSelectionRangeInfo : function(selectionInfo, initFlag, tdSelectFlag){
		var _this =this; 
		var cfgSelect = this.config.selection;

		if(initFlag !==true  && _this._isAllSelect()){
			return ; 
		}

		var	rangeInfo = selectionInfo.rangeInfo;
		
		function setSelectionInfo (cfgSelect, attrInfo){

			for(var key in attrInfo){
				if(key =='rangeInfo'){
					;
				}else if(key =='curr'){
					if(attrInfo[key]=='add'){
						cfgSelect.curr+=1;
						cfgSelect.range = rangeInfo;
						var rangeKey = rangeInfo._key ?  rangeInfo._key : cfgSelect.curr;
						
						if(_this.isRangeKey(rangeKey)){
							rangeInfo.mode = 'remove';
							delete cfgSelect.allRange[rangeKey];
						}else{
							cfgSelect.allRange[rangeKey] = rangeInfo;
						}
					}
				}else{
					cfgSelect[key] = attrInfo[key];
				}
			}

			return cfgSelect; 
		}

		if(initFlag === true){
		
			var initOpt = {
				curr :0
				,range : {startIdx : -1,endIdx : -1, startCol : -1, endCol : -1}
				,allRange:  {}
				,isSelect : false
				,isMouseDown:false
				,unSelectPosition:{}
				,allSelect : false
				,minIdx : -1 ,maxIdx : -1
				,minCol : -1 ,maxCol : -1
				,startCell:{startIdx : -1, startCol : -1}
			};
			setSelectionInfo(initOpt, selectionInfo);
			cfgSelect = this.config.selection = initOpt;
			cfgSelect.allRange[initOpt.curr] =cfgSelect.range;
		}else{
			setSelectionInfo(cfgSelect, selectionInfo);
		}

		var currInfo = cfgSelect.range;

		for(var key in rangeInfo){
			currInfo[key] = rangeInfo[key];
		}

		cfgSelect.minIdx = ( cfgSelect.minIdx ==-1 ? Math.min(currInfo.startIdx, currInfo.endIdx) : Math.min(cfgSelect.minIdx, currInfo.startIdx, currInfo.endIdx) );
		cfgSelect.maxIdx = Math.max(cfgSelect.maxIdx ,currInfo.endIdx , currInfo.startIdx);

		cfgSelect.minCol = (cfgSelect.minCol == -1 ? Math.min( currInfo.endCol , currInfo.startCol): Math.min(cfgSelect.minCol ,currInfo.endCol , currInfo.startCol) );
		cfgSelect.maxCol = Math.max(cfgSelect.maxCol ,currInfo.endCol , currInfo.startCol); 

		if(isUndefined(rangeInfo)) return ; 

		currInfo.minIdx = Math.min(currInfo.startIdx, currInfo.endIdx)
		currInfo.maxIdx = Math.max(currInfo.endIdx ,currInfo.startIdx);
		currInfo.minCol = Math.min( currInfo.endCol , currInfo.startCol);
		currInfo.maxCol = Math.max( currInfo.endCol , currInfo.startCol);
		
		if(tdSelectFlag){
			_this._setCellSelect(initFlag);
		}
	}
	/**
     * @method _isAllSelect
     * @description cell select  
     */
	,_isAllSelect : function (){
		return this.config.selection.allSelect;
	}
	/**
     * @method _setCellSelect
     * @description cell select  
     */
	,_setCellSelect : function (initFlag) {
		var _this =this; 

		var tmpCurr = _this.config.selection.curr;
		var colInfo = _this.getSelectionCellInfo(tmpCurr, false);
		var startCellInfo = _this.config.selection.startCell; // start cell
		
		var sIdx = colInfo.startIdx 
			,eIdx = colInfo.endIdx 
			,sCol= colInfo.startCol
			,eCol =  colInfo.endCol
			,currViewIdx = _this.config.scroll.viewIdx;

		var	sRow= sIdx < currViewIdx ? 0 : (sIdx-currViewIdx)
			,eRow =  eIdx- currViewIdx;

		eRow = eRow > _this.config.scroll.viewCount ? _this.config.scroll.viewCount :eRow;

		if(_this.config.selection.range.mode == 'remove'){
			for(var i = sRow ; i <= eRow ; i++){
				for(var j=sCol ;j <= eCol; j++){
					var rowCol = i+','+j; 
					var currIdx = currViewIdx+i;
					
					var addEle;

					if(_this._isFixedPostion(j)){
						addEle =_this.element.leftContent.querySelector('[data-grid-position="'+rowCol+'"]');
					}else{
						addEle =_this.element.bodyContent.querySelector('[data-grid-position="'+rowCol+'"]');
					}
					if(addEle==null) continue; 

					_this.config.selection.unSelectPosition[rowCol]='';

					addEle.removeAttribute('data-select-idx');
					addEle.classList.remove('col-active');
					
					addEle = null; 
				}
			}
			return ; 
		}
		
		_this.element.body.find('.pub-body-td.selection-start-col').removeClass('selection-start-col');

		if(initFlag){
			_this.element.body.find('.pub-body-td.col-active').removeClass('col-active');
		}else{
			_this.element.body.find('.pub-body-td[data-select-idx="'+tmpCurr+'"].col-active').each(function (){

				var sEle = $(this);
				
				var gridTdPos = sEle.attr('data-grid-position')
					,selCol = gridTdPos.split(',');

				if(_this.isSelectPosition(currViewIdx+intValue(selCol[0]) , intValue(selCol[1]))){
					
				}else{
					sEle.removeClass('col-active');
				}
			})
		}
		
		var rangeKey = _this.config.selection.range._key; 
		var isRowSelect =false,isColSelect = false;  
		if(!isUndefined(rangeKey)){
			isRowSelect = _this.config.selection.range._key.indexOf('row') == 0;
			isColSelect = _this.config.selection.range._key.indexOf('col') == 0;
		}
		
		for(var i = sRow ; i <= eRow ; i++){

			for(var j=sCol ;j <= eCol; j++){
				var rowCol = i+','+j; 
				var currIdx = currViewIdx+i;

				if(isRowSelect || isColSelect){
					 delete _this.config.selection.unSelectPosition[rowCol];
				}

				if(!_this.isSelectPosition(currIdx ,j, true)){
					continue; 
				}
				
				var addEle;
				
				if(_this._isFixedPostion(j)){
					addEle =_this.element.leftContent.querySelector('[data-grid-position="'+rowCol+'"]');
				}else{
					addEle =_this.element.bodyContent.querySelector('[data-grid-position="'+rowCol+'"]');
				}
				if(addEle==null) continue; 

				addEle.setAttribute('data-select-idx',tmpCurr);
				
				if(startCellInfo.startIdx == currIdx  && startCellInfo.startCol == j ){
					addEle.classList.add('col-active');
					addEle.classList.add('selection-start-col');
				}else{
					addEle.classList.add('col-active');
				}
				
				addEle = null; 
			}
		}
	}
	/**
     * @method isRangeKey
     * @description header , col 선택 여부 확인
     */
	,isRangeKey : function (key){
		return this.config.selection.allRange[key] ?true :false;
	}
	/**
     * @method isSelectPosition
     * @description cell 선택 여부
     */
	,isSelectPosition: function (row , col, currFlag){
		
		function isSelRange(range,row,col){
			return range.minIdx <=row && row <= range.maxIdx && range.minCol <=col && col <= range.maxCol;
		}

		if(hasProperty(this.config.selection.unSelectPosition, row+','+col)){
			return false; 
		}else{

			if(currFlag){
				return isSelRange(this.config.selection.range, row , col);
			}else {

				var allRange = this.config.selection.allRange;

				for(var key in allRange){
					var tmpRange = allRange[key];
					
					if(isSelRange(tmpRange, row , col)){
						return true; 
					}
				}
			}
		}

		return false; 
	}
	/**
     * @method isAllSelectUnSelectPosition
     * @description cell all 선택시 선택 여부
     */
	,isAllSelectUnSelectPosition: function (row , col){
		return hasProperty(this.config.selection.unSelectPosition, row+','+col)
	}
	/**
     * @method copyData
     * @description 데이타 복사.
     */
	,copyData : function (){
		var copyData = this.selectionData();
		try{
			copyStringToClipboard(this.prefix, copyData);
		}catch(e){
			console.log('Unable to copy', e);					
		}			
	}
	/**
	* @method getData
    * @description data 구하기.
	*/
	,getData : function (opt){
		var _this = this; 
			
		opt = objectMerge({isSelect :false,dataType : 'text'} ,opt);
		
		var dataType =opt.dataType; 
		if(opt.isSelect===true){
			return _this.selectionData(dataType);
		}else{

			var tbodyItem = _this.options.tbodyItem;
			var tColItem = _this.config.tColItem;

			var tbodyLen = tbodyItem.length
				,tColLen = tColItem.length;
			
			var returnVal = [];
			var keyInfo ={};
			for(var i = 0; i < tbodyLen; i++){
				var item = tbodyItem[i];
				var rowText = [];
				var rowItem = {};

				for(var j=0 ;j < tColLen; j++){
					var colItem = tColItem[j];
					if(colItem.visible===false) continue;
				
					var tmpKey = colItem.key; 

					var tmpVal = _this.valueFormatter( i, colItem,item,null,true); 
					if(dataType=='json'){
						keyInfo[j] = colItem;
						rowItem[tmpKey] = tmpVal;
					}else{
						rowText.push(tmpVal);
					}
				}

				if(dataType=='json'){
					returnVal.push(rowItem);
				}else{
					returnVal.push(rowText.join('\t'));
				}
			}
		
			if(dataType=='json'){
				var reKeyInfo =[];
				for(var key in keyInfo){
					reKeyInfo.push(keyInfo[key]);
				}

				return {
					header : reKeyInfo
					,data : returnVal
				}
			}else{
				return returnVal.join('\n');
			}
		}
	}
	/**
     * @method selectionData
     * @description select data 구하기.
     */
	,selectionData : function (dataType) {
		var _this = this; 

		dataType = dataType||'text';
		
		var sCol,eCol,sIdx,eIdx , chkFn;

		var allSelectFlag = _this._isAllSelect(); 
		
		if(allSelectFlag){
			sCol= 0;
			eCol= _this.config.tColItem.length-1;
			sIdx= 0;
			eIdx = _this.config.dataInfo.lastRowIdx;
		}else{
			var colInfo = _this.config.selection;
			sCol= colInfo.minCol;
			eCol =  colInfo.maxCol;
			sIdx= colInfo.minIdx;
			eIdx =  colInfo.maxIdx;
		}

		if(sIdx < 0 || eIdx < 0) return ''; 

		var returnVal = [];
		var addRowFlag;

		var tbodyItem = _this.options.tbodyItem;
		var tColItem = _this.config.tColItem;

		var keyInfo ={};
		for(var i = sIdx ; i <= eIdx ; i++){
			var item = tbodyItem[i];

			var rowText = [], rowItem = {};
			addRowFlag = false; 

			for(var j=sCol ;j <= eCol; j++){
				var colItem = tColItem[j];

				if(colItem.visible===false) continue;
				
				var tmpKey = colItem.key; 

				if((allSelectFlag && !_this.isAllSelectUnSelectPosition( i,j)) || _this.isSelectPosition(i,j)) {
					addRowFlag = true;
					 
					if(dataType=='json'){
						keyInfo[j] = colItem;
						rowItem[tmpKey] = _this.valueFormatter( i, colItem,item,null,true);
					}else{
						rowText.push(_this.valueFormatter( i, colItem,item,null,true));
					}
				}else{
					rowText.push('');
					rowItem[tmpKey] = '';
				}
			}

			if(addRowFlag){
				if(dataType=='json'){
					returnVal.push(rowItem);
				}else{
					returnVal.push(rowText.join('\t'));
				}
			}
		}
		if(dataType=='json'){
			var reKeyInfo =[];

			for(var key in keyInfo){
				reKeyInfo.push(keyInfo[key]);
			}

			return {
				header : reKeyInfo
				,data : returnVal
			}
		}else{
			return returnVal.join('\n');
		}
		
	}
	/**
     * @method getSelectionItem
     * @description selection item 구하기.
     */
	,getSelectionItem : function (itemKeys) {
		var _this = this; 
		
		var sCol,eCol,sIdx,eIdx , chkFn;

		var allSelectFlag = _this._isAllSelect(); 
		
		if(allSelectFlag){
			sIdx= 0;
			eIdx = _this.config.dataInfo.orginRowLen;
		}else{
			var colInfo = _this.config.selection;
			sIdx= colInfo.minIdx;
			eIdx =  colInfo.maxIdx;
		}

		if(sIdx < 0 || eIdx < 0) return []; 

		var selectItem = [];
		var addRowFlag; 
		var keyLen = itemKeys.length; 
		
		var hi = _this.config.headerInfo[_this.config.headerInfo.length-1];
		var itemKeysIdx = [] , tmpKeyIdx={};
		for(var i = 0 ; i <hi.length ; i++){
			tmpKeyIdx[hi[i].key] = i; 
		}
		
		for(var k = 0 ; k <keyLen ;k++){
			itemKeysIdx.push(tmpKeyIdx[itemKeys[k]]);
		}
		
		var idxLen = itemKeysIdx.length; 
		for(var i = sIdx ; i <= eIdx ; i++){
			var item = _this.options.tbodyItem[i];

			var sItem = {};
			addRowFlag = false;
			for(var j=0 ;j < idxLen; j++){
				if((allSelectFlag && !_this.isAllSelectUnSelectPosition( i,itemKeysIdx[j]))  ||  _this.isSelectPosition( i,itemKeysIdx[j])){
					addRowFlag = true; 
					sItem[itemKeys[j]] = item[itemKeys[j]];
				}
			}
			if(addRowFlag) 	selectItem.push(sItem);
		}

		return selectItem;
	}
	/**
     * @method _setBodyEvent
     * @description body event setting
     */
	,_setBodyEvent : function (){
		if(this.options.rowOptions.contextMenu !== false){
			$.pubContextMenu('#'+this.prefix+'_bodyContainer .pub-body-tr',this.options.rowOptions.contextMenu);			
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
			,tci = _this.config.tColItem
			,tbi = opt.tbodyItem;
		
		if(idx < 0 || tbi.length < 1 || idx >= tci.length){
			return [];
		}

		var _key = tci[idx].key;

		if(isUndefined(_this.config.sort[_key])){
			_this.config.sort[_key]= {sortType : sortType};
		}else{
			_this.config.sort[_key].sortType=sortType;
		}
		
		function getItemVal(itemObj){
			return itemObj[_key];
		}
		
		if(sortType=='asc'){  // 오름차순
			_this.config.sort[_key].orginList = tbi.slice(0);

			tbi.sort(function (a,b){
				var v1 = getItemVal(a)
					,v2 = getItemVal(b);
				return v1 < v2 ? -1 : v1 > v2 ? 1 : 0;
			});
		}else if(sortType=='desc'){
			tbi.sort(function (a,b){ // 내림차순
				var v1 = getItemVal(a)
					,v2 = getItemVal(b);
				return v1 > v2 ? -1 : v1 < v2 ? 1 : 0;
			});
		}else{
			tbi = _this.config.sort[_key].orginList;
		}

		return tbi; 
	}
	/**
     * @method _headerResize
	 * @param  flag {Boolean} resize 여부
     * @description header resize 설정
     */
	,_headerResize :function (flag){
		var _this = this
			,resizeEle = _this.element.header.find('.pub-header-resizer');

		function colResize(_this , sEle){
			_this.drag = {};
			
			_this.drag.ele = sEle;
			
			_this.drag.resizeIdx = _this.drag.ele.attr('data-resize-idx');
			_this.drag.isLeftContent  = _this._isFixedPostion(_this.drag.resizeIdx);
			_this.drag.colHeader= $('#'+_this.prefix+'colHeader'+_this.drag.resizeIdx);
			
			_this.drag.totColW = _this.drag.ele.closest('[data-header-info]').width();
			
			_this.drag.colW = _this.config.tColItem[_this.drag.resizeIdx].width;
			if(_this.drag.isLeftContent){
				_this.drag.gridW = _this.config.gridWidth.left - _this.drag.colW;
			}else{
				_this.drag.gridW = _this.config.gridWidth.main - _this.drag.colW;
			}
			_this.drag.gridBodyW = _this.config.container.width - _this.drag.colW;
			return ; 
		}

		if(flag===true){

			resizeEle.on('dblclick', function (e){
				colResize(_this, $(this));

				var selColItem = _this.config.tColItem[_this.drag.resizeIdx]; 
				
				var resizeW = selColItem.maxWidth || -1; 

				if(resizeW < 1){
					var tbodyItem = _this.options.tbodyItem
						, beforeLen = -1
						,tmpVal ,currLen
						,selColKey  =selColItem.key; 
					
					for(var i =0, len = _this.config.dataInfo.orginRowLen ;i <len;i++){
						tmpVal = tbodyItem[i][selColKey]+'';

						currLen = tmpVal.length;
						if(currLen > beforeLen){
							resizeW = Math.max(getCharLength(tmpVal||''),resizeW);
						}

						beforeLen= currLen;
					}
					resizeW = resizeW*_this.options.headerOptions.oneCharWidth+10;
					selColItem.maxWidth=resizeW; 
				}

				_this._setHeaderResize(e,_this, 'end' , Math.min(resizeW,_this.options.headerOptions.colMaxWidth ));
			})

			resizeEle.css('cursor',_this.options.headerOptions.resize.cursor);
			resizeEle.on('touchstart.pubresizer mousedown.pubresizer',function (e){
				var oe = e.originalEvent.touches;
				
				colResize(_this, $(this));

				_this.drag.pageX = oe ? oe[0].pageX : e.pageX;
				_this.drag.ele.addClass('pubGrid-move-header')
								
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
	,_setHeaderResize : function (e,_this , mode, resizeW){

		if (!_this.drag) return false;

		var drag = _this.drag; 

		var w = resizeW, ox; 

		if(isUndefined(resizeW)){
			var oe = e.originalEvent.touches;
			
			ox = oe ? oe[0].pageX : e.pageX;
			
			w = resizeW ||(drag.colW + (ox - drag.pageX));
		}

		if(mode=='end'){
			_this.setHeaderWidth(drag.resizeIdx , w , drag.colHeader);

			if(isFunction(_this.options.headerOptions.resize.update)){
				_this.options.headerOptions.resize.update.call(null , {index:drag.resizeIdx , width: w});
			}
			drag.ele.removeAttr('style');

		}else{
			var w = drag.totColW + (ox - drag.pageX);
			drag.ele.css('left', w > _this.options.headerOptions.colMinWidth? w : _this.options.headerOptions.colMinWidth);	
		}
	}
	/**
     * @method setHeaderWidth
	 * @param  idx : heder index
	 * @param  w : width
	 * @param  colHeaderEle , header element
     * @description 페이징 하기.
     */
	,setHeaderWidth : function (idx,w, colHeaderEle){
		if(w <= this.options.headerOptions.colMinWidth){
			w =this.options.headerOptions.colMinWidth;
		}
		
		if(this.drag.isLeftContent){
			this.config.gridWidth.left = this.config.gridWidth.left-this.config.tColItem[idx].width + w;
		}else{
			this.config.gridWidth.main = this.config.gridWidth.main-this.config.tColItem[idx].width + w;
		}
		
		//_this.config.container.width = drag.gridBodyW+w; // 2018-06-06 불필요 삭제 .
		this.config.tColItem[idx].width = w; 
		
		if(colHeaderEle){
			colHeaderEle.css('width',w+'px');
		}else{
			$('#'+this.prefix+'colHeader'+idx).css('width',w+'px');
		}

		$('#'+this.prefix+'colbody'+idx).css('width',w+'px');

		this.calcDimension('headerResize');
	}
	/**
     * @method getHeaderWidth
	 * @param  idx : heder index
     * @description 페이징 하기.
     */
	,getHeaderWidth : function (idx){
		return _this.config.tColItem[idx].width;
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
     * @method setTheme
     * @description set theme
     */
	,setTheme : function (themeName){
		this.gridElement.removeClass('pub-theme-'+this.options.theme);
		this.options.theme = themeName;
		this.gridElement.addClass('pub-theme-'+themeName);
	}
	/**
     * @method getTheme
     * @description get theme
     */
	,getTheme : function (){
		return this.options.theme;
	}
	/**
     * @method excelExport
	 * @param  opt {object} excel export 타입 ()
     * @description 해제.
     */
	,excelExport : function (opt){
		
		var _this = this
			,cfg = _this.config
			,tci = _this.config.tColItem
			,tbi = _this.options.tbodyItem
			,headerOpt=_this.options.headerOptions;

		
		var downloadInfo = [];

		downloadInfo.push('<!doctype html><html xmlns:o="urn:schemas-microsoft-com:office:office" xmlns:x="urn:schemas-microsoft-com:office:excel" xmlns="http://www.w3.org/TR/REC-html40"><head>');
		downloadInfo.push('<meta http-equiv="Content-Type" content="text/html; charset=utf-8" /><meta charset="UTF-8" />');
		downloadInfo.push('<!--[if gte mso 9]><xml><x:ExcelWorkbook><x:ExcelWorksheets><x:ExcelWorksheet><x:Name>{worksheet}</x:Name><x:WorksheetOptions><x:DisplayGridlines/></x:WorksheetOptions></x:ExcelWorksheet></x:ExcelWorksheets></x:ExcelWorkbook></xml><![endif]-->');
	
			
		//style start	
		downloadInfo.push('<style type="text/css">');
		downloadInfo.push('th {border:thin solid #524848;border-collapse: collapse;background-color:#dedede;}');
        downloadInfo.push('td {border:thin   solid #524848;border-collapse: collapse;}');
		downloadInfo.push(opt.style || '');
        downloadInfo.push('</style></head>');
		//style end;

		var headerInfo = cfg.headerInfo; 
		
		downloadInfo.push('<body>');
		downloadInfo.push('<table>');

		if(headerInfo.length > 0 && headerOpt.view){
			var ghArr, ghItem;
				
			for(var i =0, len=headerInfo.length ; i < len; i++){
				ghArr = headerInfo[i];
				downloadInfo.push('<tr class="pub-header-tr">');
				for(var j=0 ; j <ghArr.length; j++){
					ghItem = ghArr[j];
					if(ghItem.view){
						downloadInfo.push('	<th '+ghItem.colspanhtm+' '+ghItem.rowspanhtm+'>');
						downloadInfo.push(ghItem.label);
						downloadInfo.push('	</th>');					
					}
				}
				downloadInfo.push('</tr>');
			}
		}
		
		for(var i =0 ; i < tbi.length; i++){
			downloadInfo.push("<tr>");
			var item = tbi[i];
			for(var j =0 ; j < tci.length; j++){
				var keyItem = tci[j];


				downloadInfo.push("<td>");
				downloadInfo.push(item[keyItem.key]);
				downloadInfo.push("</td>");
			}
			downloadInfo.push("</tr>");
		}
		downloadInfo.push('</table>');
		downloadInfo.push('</body></html>');
				
		downloadInfo = downloadInfo.join('');

		if(!isUndefined(opt)){
			if(opt.type=='download'){
				var fileName = opt.fileName || 'pubgrid-excel-data.xls',
					charset = opt.charset||"utf-8";

				if (navigator.msSaveOrOpenBlob) {
					var _blob = new Blob([downloadInfo], { type: "text/html; charset=UTF-8" });
					window.navigator.msSaveOrOpenBlob(_blob, fileName);
				} else {
					if (_broswer=='msie' && typeof isUndefined(Blob)) {
						
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

						var uri = "data:application/vnd.ms-excel;charset=UTF-8,%EF%BB%BF"+encodeURIComponent(downloadInfo)
							,anchor = document.body.appendChild(document.createElement("a"));
						
						anchor.download = fileName;
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
     * @method destroy
     * @description 해제.
     */
	,destroy:function (){
		try{
			if($.isPlainObject (this.options.headerOptions.contextMenu)){
				$.pubContextMenu('#'+_this.prefix+'_headerContainer .pubGrid-header-th').destroy();		
			}
			if($.isPlainObject (this.options.rowOptions.contextMenu)){
				$.pubContextMenu('#'+this.prefix+'_bodyContainer .pub-body-tr').destroy();
			}
		}catch(e){};

		this.element.body.off('mousedown.pubgrid.col mouseover.pubgrid.col');
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

// background click check 
$(document).on('mousedown.pubgrid.background', function (e){
	if(e.which !==2){
		var targetEle = $(e.target); 
		var pubGridLayterEle = targetEle.closest('.pubGrid-layer'); 
		if(pubGridLayterEle.length < 1 ){
			$('.pubGrid-layer.open').removeClass('open');
		}else{
			var layerid = pubGridLayterEle.data('pubgrid-layer');
			$('.pubGrid-layer.open').each(function (){
				var sEle = $(this); 
				
				if(layerid != sEle.data('pubgrid-layer')){
					sEle.removeClass('open');
				}
			})
		}
	}
})

$.pubGrid = function (selector,options, args) {
	var _cacheObject = _datastore[selector]; 

	if(isUndefined(options)){
		return _cacheObject; 
	}
	
	if(isUndefined(_cacheObject)){
		if(!selector || $(selector).length < 1){
			return '['+selector + '] selector  not found '; 
		}

		_cacheObject = new Plugin(selector, options);
		_datastore[selector] = _cacheObject;
		
		return _cacheObject;
	}else if(typeof options==='object'){
		var headerOpt = options.headerOptions ?options.headerOptions :{}
			,reDrawFlag = typeof headerOpt.redraw==='boolean' ? headerOpt.redraw : _cacheObject.options.headerOptions.redraw; 

		if(reDrawFlag===true){
			_cacheObject.destroy();
			_cacheObject = new Plugin(selector, options);
			_datastore[selector] = _cacheObject;
		}else{
			_cacheObject.setOptions(options);
			_cacheObject.setData(options.tbodyItem , 'reDraw');
		}
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

}(jQuery, window, document));