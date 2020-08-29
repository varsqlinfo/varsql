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
"use stric1t";
/**
af :  add function
ap  : add parameter
*/
var _initialized = false
,_$doc = $(document)
,_datastore = {}
,_defaults = {
	blankSpaceWidth : 1		// 오른쪽 끝 공백 값
	,copyMode : 'single'		// copy mode	single, multiple, none
	,colFixedIndex : 0	// 고정 컬럼
	,widthFixed : false  // 넓이 고정 여부.
	,useDefaultFormatter: true // 기본 포멧터 사용여부
	,editable :false	// 편집 모드 활성화
	,selectionMode : 'multiple-cell'	// row , cell , multiple-row , multiple-cell	// 선택 방법.
	,showTooltip : false			// tooltip flag
	,theme : 'light'
	,height: 'auto'
	,width: 'auto'
	,itemMaxCount : -1	// add시 item max로 유지할 카운트
	,colOptions : {	// 컬럼 옵션
		minWidth : 50  // 컬럼 최소 넓이
		,maxWidth : -1  // 컬럼 최대 넓이
	}
	,rowOptions:{	// 로우 옵션.
		height: 22	// cell 높이
		,click : false //row(tr) click event
		,contextMenu : false // row(tr) contextmenu event
		,addStyle : false	// 추가할 style method
		,dblClick : false	// row dblclick event
		,dblClickCheck : false	// double click row checkbox checked true 여부.
		,pasteBefore :false
		,pasteAfter :false
	}
	,formatter :{
		money :{prefix :'$', suffix :'원' , fixed : 0}	// money 설정 prefix 앞에 붙일 문구 , suffix : 마지막에 뭍일것 , fixed : 소수점
		,number : {prefix :'', suffix :'' , fixed : 0}
	}
	,autoResize : {	//리사이즈 설정
		enabled:true	// 리사이즈시 그리드 리사이즈 여부.
		,responsive : true
		,threshold :150
	}
	,headerOptions : {
		view : true	// header 보기 여부
		,height: 25
		,sort : true	// 초기에 정렬할 값
		,redraw : true	// 초기에 옵션 들어오면 새로 그릴지 여부.
		,resize:{	// resize 설정
			enabled : true			// 활성화여부
			,cursor : 'col-resize'	// 커서 모양
			,update : false			// 변경시 콜랙 함수
		}
		,isColSelectAll : true	// 전체 선택 여부.
		,scrollEnabled : true	// 마우스 휠로 가로 스크롤 이동할지 여부.
		,oneCharWidth: 7
		,viewAllLabel : true
		,contextMenu : false // header contextmenu event
		,helpBtn:{			//	header help btn 설정
			enabled : false	// header help btn 활성 여부.
			,click :  function (clickInfo){}	// click event
			,dblclick : function (clickInfo){} // double click event
		}
	}
	,setting : {					// 그리드 설정
		enabled : false				// 활성여부
		,enableSpeed : false		// 스크롤 스피트 사용여부
		,enableSearch : true		// 검색 활성 여부
		,enableColumnFix : false	// 고정 컬럼 활성여부
		,click : false				// 직접 처리 할경우. function 으로 처리.
		,speedMaxVal :10			// 스크롤 스피드
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
				var itemVal = (item[key]||'')+'';

				if(itemVal.toLowerCase().indexOf(searchVal) > -1){
					return true;
				}
				return false;
			}
		}
	}
	,asideOptions :{	// aside 옵션
		lineNumber : {	// 번호
			enabled :false
			,key : 'lineNumber'
			,charWidth : 9
			,name : ''
			,width : 40
			,styleCss : ''	//css
			,isSelectRow:true
		}
		,rowSelector :{	// 체크 박스
			enabled :false
			,key : 'checkbox'
			,name : 'V'
			,width : 25
			,click : function (rowInfo){ // click event , return false 일경우 체크 안함.

			}
		}
		,modifyInfo :{	// 수정 여부
			enabled :false
			,key : 'modify'
			,name : 'modify'
			,width : 10
		}
	}
	,bodyOptions : {	// body option
		cellDblClick : false	// body td click
	}
	,scroll :{	// 스크롤 옵션
		isPreventDefault : true	// 이벤트 전파 여부.
		,vertical : {
			width : 14			// 세로 스크롤
			,bgDelay : 100		// 스크롤 빈공간 mousedown delay
			,btnDelay : 100		// 방향키 mousedown delay
			,dragDelay : 5		// 스크롤 bar drag delay
			,speed : 1			// 스크롤 스피드 row 1
			,onUpdate : function (item){	// 스크롤 업데이트.
				return true;
			}
			,tooltip : false		// item count tooltip

		}
		,horizontal :{
			height: 14			// 가로 스크롤 높이
			,bgDelay : 100
			,btnDelay : 100		// 방향키 버튼 속도.
			,dragDelay : 7		// 스크롤 bar drag delay
			,speed : 1			// 스크롤 스피드

		}
	}
	/*
	tColItem  object info
	{
	  "key": "b"	// key
	  ,"label": "비"	// label
	  ,"width": 100		// width
	  ,"sort": true		// sort flag
	  ,"align": "center"	// align
	  ,"type": "money"		// value type
	  ,"render": "html"		// render mode (html or text default text)
	  ,formatter : function (obj){	// 보여질 값을 처리.
			return obj.item.STATE;
	  }
	  ,defaultValue : ''	// add item default value
	  ,colClick :function (idx,item){ console.log(idx, item)}		// cell click event
	  ,styleClass : function (idx, item){return 'pub-bg-private';}	// cell add class
	  ,tooltip : {
		 show : true	// 툴팁 보일지 여부.
		 ,formatter : function (obj){	// 툴팁 내용
			return obj.val;
		 }
	  }
	  ,editor : {
		type : "text", "text, select, textarea, number, custom"
		editorBtn : false,		// 버튼 보일지 여부.
		editorBtnOver : false, // 오버시 버튼 보이기
		items : [],
		validator : function (){}
	  }

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

function getCharLength(s , charW){
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

	if(isNaN(charW)){
		return (w_1 + (w_15*1.3) + (w_2*2) + (w_3*2.1));
	}else{
		return (w_1 + (w_15*1.3) + (w_2*2) + (w_3*2.1) ) * charW +10;
	}
}

var formatter= {
	'money' : function (num , fixedNum , prefix , suffix){
		return (prefix||'')+ formatter.number(num, fixedNum) +(suffix||'');
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

	var _id = prefix+'_pubGridCopyArea';
	var copyArea = document.getElementById(_id);

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
			gridWidth :{aside : 0,left : 0, main:0, total:0, mainOverWidth:0 ,mainInsideWidth:0}
			, container :{height : 0,width : 0, bodyHeight:0}
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
			, sort : {current :''}
			, selection :{
				startCell :{}
			}
			, searchOn : false
			, isResize : false
			, focus : false
			, mouseEnter :false
			, currentClickInfo :{}
			, allCheck :false
		};
		_this.eleCache = {};
		_this._initScrollData();
		_this._setSelectionRangeInfo({},true);

		_this.setOptions(options, true);

		_this.drag ={};
		_this.addStyleTag();

		_this._setSearchData('init');

		_this._setThead();
		_this.setData(_this.options.tbodyItem , 'init');

		_this.config.gridXScrollFlag = false;
		_this._windowResize();

		return this;
	}
	// 스크롤 데이터 초기화
	,_initScrollData : function (){
		this.config.scroll = {containerLeft :0, before:{},top :0 , left:0, startCol:0, endCol : 0, viewIdx : 0, vBarPosition :0 , hBarPosition :0 , maxViewCount:0, viewCount : 0, vTrackHeight:0,hTrackWidth:0};
	}
	/**
	 * @method _setGridContainerWidth
	 * @description grid 넓이 구하기
	 */
	,_setGridContainerWidth : function (width, mode){
		if(mode=='headerResize'){
			this.config.container.width = width;
		}else{
			this.config.container.width = width- this.config.blankSpaceWidth;		// border 값 빼주기.
		}
	}
	/**
	 * @method getGridWidth
	 * @description grid width
	 */
	,getGridWidth : function(){
		return this.options.width =='auto' ? this.gridElement.width() : this.options.width;
	}
	/**
	 * @method getGridHeight
	 * @description grid height
	 */
	,getGridHeight : function (){
		return this.options.height =='auto' ? this.gridElement.height() : this.options.height;
	}
	/**
	 * @method setOptions
	 * @description 옵션 셋팅.
	 */
	,setOptions : function(options , firstFlag){
		var _this = this;
		options.setting = options.setting ||{};
		options.setting.configVal = objectMerge({},_defaults.setting.configVal ,options.setting.configVal);

		_this.options =objectMerge({}, _defaults, options)
		_this.options.tbodyItem = options.tbodyItem ? options.tbodyItem : _this.options.tbodyItem;

		_this.config.rowHeight = _this.options.rowOptions.height;

		if(_this.options.rowOptions.contextMenu !== false && typeof _this.options.rowOptions.contextMenu == 'object'){
			var _cb = _this.options.rowOptions.contextMenu.callback;

			if(_cb){
				_this.options.rowOptions.contextMenu.callback = function(key,sObj) {
					this.gridItem = _this.getRowItemToElement(this.element);
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

		// space width
		this.config.blankSpaceWidth = 2+(this.options.blankSpaceWidth>0?this.options.blankSpaceWidth :0);

		// header element height
		if(_this.options.headerOptions.view !== false){
			this.config.header.height = _this.options.headerOptions.height * (this.options.theadGroup.length > 0 ?this.options.theadGroup.length : 1);
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

		_this._setGridContainerWidth(_this.getGridWidth());
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
			,fixedColIdx = opt.colFixedIndex
			,cfg = _this.config
			,gridElementWidth =cfg.container.width
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
			tciItem.maxWidth = -1;	// max width

			if(tciItem.visible===false) continue;

			if(!isUndefined(tciItem.tooltip) && isFunction(tciItem.tooltip.formatter)){
				tciItem.afTooltipFormatter = tciItem.tooltip.formatter;
			}else {
				tciItem.afTooltipFormatter = false;
			}

			++viewColCount;

			if(viewAllLabel){
				var labelWidth = tciItem.label.length * 5;
				tciItem.width = isNaN(tciItem.width) ? labelWidth : (labelWidth > tciItem.width ? labelWidth: tciItem.width);
			}else{
				tciItem.width = isNaN(tciItem.width) ? opt.colOptions.minWidth :tciItem.width;
			}

			tciItem.width = Math.max(tciItem.width, opt.colOptions.minWidth);

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

		cfg.dataInfo.colLen = viewColCount;
		cfg.dataInfo.allColLen= tci.length;

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

		if(this.options.widthFixed === true){
			return ;
		}

		var _this = this
			,_containerWidth ,_w
			,opt = _this.options
			,_gw = _this.config.container.width
			,tci = _this.config.tColItem
			,tciLen = _this.config.dataInfo.colLen;

		var verticalW = 0;

		if(_this.options.tbodyItem.length > 0){
			if( (_this.options.tbodyItem.length * _this.config.rowHeight) > (_this.getGridHeight() - this.config.header.height -this.config.footer.height)){
				verticalW = opt.scroll.vertical.width;
			}
		}

		var _totW = _this.config.gridWidth.aside+_this.config.gridWidth.left+_this.config.gridWidth.main+verticalW;

		var resizeFlag = _totW  < _gw ? true : false;
		var remainderWidth = Math.floor((_gw -_totW)/tciLen)
			, lastSpaceW = (_gw -_totW)-remainderWidth *tciLen;

		if(resizeFlag){
			var leftGridWidth = 0, mainGridWidth=0;
			for(var j=0; j<tciLen; j++){
				var item = tci[j];
				item.width += remainderWidth;
				item.width = Math.max(item.width, opt.colOptions.minWidth);

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
			endCol = _this.options.colFixedIndex;
		}else if(type=='cont'){
			startCol = _this.options.colFixedIndex;
		}

		strHtm.push('<colgroup>');

		for(var i=startCol ;i <endCol; i++){
			thiItem = tci[i];
			var tmpStyle = [];
			tmpStyle.push('width:'+thiItem.width+'px;');
			if(thiItem.visible===false){
				tmpStyle.push('display:none;visibility: hidden;');
			}else{
				strHtm.push('<col id="'+id+i+'" style="'+tmpStyle.join('')+'" />');
			}
		}

		strHtm.push('</colgroup>');

		return strHtm.join('');
	}
	/**
	 * @method addData
	 * @param data {Object , Array} - 데이타
	 * @param opt {Object} - add option { index : index ,'first','last' ,focus : 스크롤 이동 여부 (true or false) }
	 * @description 데이타 add
	 */
	,addRow : function (pData, opt){
		if(!$.isArray(pData)){
			pData = [pData];
		}
		this.setData(pData, 'addData', opt||{});
	}
	/**
	 * @method removeRow
	 * @param idx {Integer,String} //number index ,'first' , 'last','checked'  - remove row item
	 * @description 데이타 그리기
	 */
	,removeRow : function (idxs){
		var removeRowInfo=[];
		var removeIdxs =[];

		if(idxs =='checked'){
			removeIdxs = this.getCheckItems('index');
		}else{
			if($.isArray(idxs)){
				removeIdxs = idxs;
			}else{
				if(idxs =='first'){
					removeIdxs.push(0);
				}else if(idxs =='last'){
					removeIdxs.push(this.options.tbodyItem.length -1);
				}else if(!isNaN(idxs)){
					removeIdxs.push(parseInt(idxs,10));
				}
			}
		}

		removeIdxs.sort(function (a,b){
			return a < b;
		});

		for(var i =removeIdxs.length-1; i >= 0 ;i--){
			var removeIdx = removeIdxs[i];
			var removeItem = this.options.tbodyItem.splice(removeIdx, 1)[0];
			if(removeItem){
				removeItem['$removeIndex'] = removeIdx
				removeRowInfo.push(removeItem);
			}

		}

		this.setData(this.options.tbodyItem,'reDraw');
		return removeRowInfo;
	}
	,removeSelectionRow : function (idx){
		var removeIdxs;
		if(idx){
			removeIdxs = idx
		}else{
			removeIdxs = this.getRowSelectionIdx();
		}

		return this.removeRow(removeIdxs);
	}
	/**
	 * @method updateRow
	 * @param idx {Integer} - update position index
	 * @param rowItem {Object} - update item
	 * @description 데이타 그리기
	 */
	,updateRow : function (idx ,rowItem){

		var updItem = this.options.tbodyItem[idx];

		if(updItem){
			this.options.tbodyItem[idx] = objectMerge(this.options.tbodyItem[idx] , rowItem);
			this.setData(this.options.tbodyItem,'reDraw');
		}

		return updItem;
	}
	/**
	 * @method addRowSelections
	 * @param idxs {Array} - select position index
	 * @description row 선택
	 */
	,addRowSelections : function (idxs, beforeSelectionClearFlag){

		if(!$.isArray(idxs)){
			idxs = [idxs];
		}

		if(beforeSelectionClearFlag ===true){
			_$util.clearActiveColumn(this.element);
			this._setSelectionRangeInfo({}, true);
		}

		if(idxs.length < 1){
			return ;
		}

		for(var i =0 ;i < idxs.length;i++){
			var rowIdx = idxs[i];
			var rangeKey = 'row'+rowIdx;

			this._setSelectionRangeInfo({
				rangeInfo : {_key : rangeKey, startIdx : rowIdx, endIdx : rowIdx, startCol : 0,  endCol :this.config.dataInfo.colLen-1}
				,isSelect : true
				,curr : 'add'
			}, false, true);
		}
	}
	/**
	 * @method clearRowSelections
	 * @param idxs {Array} - select position index
	 * @description row 선택 clear.
	 */
	,clearRowSelections : function (idxs){

		if(!$.isArray(idxs)){
			idxs = [idxs];
		}

		if(idxs.length < 1){
			return ;
		}

		for(var i =0 ;i < idxs.length;i++){
			var rowIdx = idxs[i];
			var rangeKey = 'row'+rowIdx;

			this._setSelectionRangeInfo({
				rangeInfo : {_key : rangeKey, startIdx : rowIdx, endIdx : rowIdx, startCol : 0,  endCol :this.config.dataInfo.colLen-1}
				,isSelect : false
				,curr : 'remove'
			}, false, true);
		}
	}
	/**
	 * @method _setSearchData
	 * @description 검색 정보 셋팅
	 */
	,_setSearchData: function (mode){
		var settingOpt = this.options.setting;

		if(mode != 'search'){
			this.config.orginData = this.options.tbodyItem;
		}

		if(settingOpt.enabled ===true  && settingOpt.enableSearch ===true){
			var schArr = [];
			var orginData = this.config.orginData;

			var schField = settingOpt.configVal.search.field ||''
				,schVal = settingOpt.configVal.search.val ||'';

			schVal = _$util.trim(schVal);

			if(schField != '' && schVal !=''){
				var schArr =[];

				schVal =schVal.toLowerCase();

				for(var i =0 , len  = orginData.length; i < len;i++){
					var tmpItem =orginData[i];

					if(settingOpt.util.searchFilter(tmpItem,schField,schVal)){
						schArr.push(tmpItem);
					}
				}

				this.config.searchOn = true;
			}else{
				this.config.searchOn = false;
				schArr = this.config.orginData;
			}

			if(mode =='search'){
				var currSortKey = this.config.sort.current;

				// 검색 시 정렬 필드 체크 해서 검색 결과 정렬하기
				if(currSortKey !=''){
					this.options.tbodyItem = schArr;
					schArr = this._getSortList(currSortKey,this.config.sort[currSortKey].sortType);
				}

				this.setData(schArr,'search');
			}else{
				this.options.tbodyItem = schArr;
			}
		}
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

		var modeInfo = mode.split('_');
		var gridMode = modeInfo[0]
			,subMode = modeInfo[1] ||'';

		gridMode = gridMode||'reDraw';

		if(!$.isArray(pdata)){
			data = pdata.items;
			pageInfo = pdata.page;
		}

		var resizeFlag = false;
		if((_this.options.tbodyItem.length == 0 && data.length > 0)
			||_this.options.tbodyItem.length > 0 && data.length == 0){
			resizeFlag = true;
		}

		if(data){
			if(gridMode == 'addData'){
				var rowIdx =0;

				var addIdx = addOpt.index;

				if(addIdx =='first'){
					Array.prototype.unshift.apply(_this.options.tbodyItem, data);
					//_this.options.tbodyItem = _this.options.tbodyItem.unshift(data);
				}else if(!isNaN(addIdx)){
					addIdx = parseInt(addIdx,10);
					Array.prototype.splice.apply(_this.options.tbodyItem, [addIdx,0].concat(data));
				}else{
					_this.options.tbodyItem = _this.options.tbodyItem.concat(data);
				}

				if(opt.itemMaxCount > 0 &&_this.options.tbodyItem.length > opt.itemMaxCount){
					var removeCount = _this.options.tbodyItem.length-opt.itemMaxCount;
					if(addIdx =='first'){
						_this.options.tbodyItem.splice(opt.itemMaxCount,removeCount)
					}else{
						_this.options.tbodyItem.splice(0,removeCount)
					}
				}
			}else{
				if(gridMode != 'init' && subMode !='keepCheck'){
					_this.setCheckItems('all', false, false);
				}

				_this.options.tbodyItem = data;
			}
		}

		if(gridMode =='init'){
			// sort 값이 있으면 초기 데이타 정렬
			if(typeof opt.headerOptions.sort ==='object'){
				var _key = opt.headerOptions.sort.key
					,_sortType = opt.headerOptions.sort.type=='desc'?'desc':'asc';

				_this.options.tbodyItem = _this._getSortList(_key, _sortType);
			}
		}

		if(gridMode == 'search'){
			gridMode = 'reDraw';
		}else{
			if(gridMode == 'reDraw' || gridMode =='addData'){
				_this._setSearchData(gridMode);
			}
		}

		_this.config.dataInfo.orginRowLen = _this.options.tbodyItem.length;

		if(_this.config.dataInfo.orginRowLen > 0){
			_this.config.dataInfo.rowLen= _this.config.dataInfo.orginRowLen+1;
			_this.config.dataInfo.lastRowIdx= _this.config.dataInfo.orginRowLen-1;
		}else{
			_this.config.dataInfo.rowLen= 0;
			_this.config.dataInfo.lastRowIdx= 0;
		}

		if(gridMode=='reDraw' || gridMode == 'addData'){
			_this._setHeaderInitInfo();

			if(subMode !='paste'){
				_this._setSelectionRangeInfo({}, true);
			}

			_this.calcDimension(gridMode);
		}

		if(gridMode == 'addData' || subMode =='paste'){

			var rowIdx =_this.config.scroll.viewIdx;

			var addIdx = addOpt.index;

			if(addOpt.focus ===true){
				if(addIdx =='first'){
					rowIdx =0;
				}else if(!isNaN(addIdx)){
					rowIdx = parseInt(addOpt.index,10);
				}else{
					rowIdx = _this.config.dataInfo.orginRowLen;
				}
			}

			if(_this.config.scroll.viewIdx <= rowIdx && rowIdx < _this.config.scroll.viewIdx +_this.config.scroll.viewCount){
				_this.drawGrid(mode,true);
			}else if(addOpt.focus===true){
				_this.moveVerticalScroll({rowIdx : rowIdx});
			}
		}else{
			_this.drawGrid(mode,true);
		}

		_this.setPage(pageInfo);

		if(_this.config.searchOn===true){
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
			val = 1;
		}
		this.options.scroll.vertical.speed =  1;
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

		return '<div class="pubGrid-wrapper"><div id="'+_this.prefix+'_pubGrid" class="pubGrid pubGrid-noselect" tabindex="-1"  style="outline:none !important;overflow:hidden;width:'+_this.config.container.width+'px;">'
			+' 	<div id="'+_this.prefix+'_container" class="pubGrid-container '+(_this.options.colFixedIndex >0 ? 'pubGrid-col-fixed':'')+'" style="overflow:hidden;">'
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
			+' 			<div class="pubGrid-header-left-cont-border"><div class="pubGrid-border"></div></div>'
			+' 			<div class="pubGrid-header">'
			+'				<div class="pubGrid-header-cont-wrapper" style="position:relative;"><table class="pubGrid-header-cont" onselectstart="return false">#theaderHtmlArea#</table></div>'
			+' 			</div>'
			+' 		  </div>'
			+' 		</div>'

			+' 		<div id="'+_this.prefix+'_bodyContainer" class="pubGrid-body-container-warpper">'
			+' 			<div class="pubGrid-body-container">'
			+' 				<div class="pubGrid-body-aside"><table style="width:'+_this.config.gridWidth.aside+'px;" class="pubGrid-body-aside-cont"></table></div>'
			+' 				<div class="pubGrid-body-left"><table style="width:'+_this.config.gridWidth.left+'px;" class="pubGrid-body-left-cont"></table></div>'
			+' 				<div class="pubGrid-body-left-cont-border"><div class="pubGrid-border"></div></div>'
			+' 				<div class="pubGrid-body">'
			+'					<div class="pubGrid-body-cont-wrapper" style="position:relative;"><table  class="pubGrid-body-cont"></table></div>'
			+'				</div>'
			+'				<div class="pubGrid-body-selection-cell"></div>'
			+' 			</div>'
			+'			<div class="pubGrid-empty-msg-area"><span class="pubGrid-empty-msg"><i class="pubGrid-icon-info"></i><span class="empty-text">no-data</span></span></div>'
			+' 		</div>'
			+' 		<div id="'+_this.prefix+'_footerContainer" class="pubGrid-footer-container">'
			+' 			<div class="pubGrid-footer-left"></div>'
			+' 			<div class="pubGrid-footer">'
			+' 				<div class="pubGrid-footer-cont-wrapper" style="position:relative;"><table class="pubGrid-footer-cont"></table></div>'
			+' 			</div>'
			+' 		</div>'
			+' 		<div id="'+_this.prefix+'_vscroll" class="pubGrid-vscroll">'
			+'			<div class="pubGrid-scroll-top-area" style="height:23px;"></div>'
			+' 			<div class="pubGrid-vscroll-bar-area">'
			+'			  <div class="pubGrid-vscroll-bar-bg"></div>'
			+' 			  <div class="pubGrid-vscroll-up pubGrid-vscroll-btn" data-pubgrid-btn="U"><svg width="'+vArrowWidth+'px" height="8px" viewBox="0 0 110 110" style="enable-background:new 0 0 100 100;"><g><polygon points="50,0 0,100 100,100" fill="#737171"/></g></svg></div>'
			+'			  <div class="pubGrid-vscroll-bar"><div class="pubGrid-vscroll-bar-tip" style="right:'+(this.options.scroll.vertical.width)+'px"></div></div>'
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
			+' 		</div>'
			+' 		<div id="'+_this.prefix+'_resizeHelper" class="pubGrid-resize-helper"></div>'
			+' 	</div>'
			+' </div>'
			+' <div style="top:-9999px;left:-9999px;position:fixed;z-index:999999;"><textarea id="'+_this.prefix+'_pubGridPasteArea"></textarea>' // copy 하기위한 textarea 꼭 위치해야함.
			+' <textarea id="'+_this.prefix+'_pubGridCopyArea"></textarea></div>' // copy 하기위한 textarea 꼭 위치해야함.
			+' <div id="'+_this.prefix+'_navigation" class="pubGrid-navigation"><div class="pubGrid-page-navigation"></div><div id="'+_this.prefix+'_status" class="pubgGrid-count-info"></div>'
			+' </div>'
			+' </div>';

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
	* @method getViewRow
	* @description view row count
	*/
	,getViewRow: function (){
		return this.config.scroll.bodyViewCount;
	}
	/**
	* @method getRowSelectionIdx
	* @description row selection idx
	*/
	,getRowSelectionIdx: function (){
		var _this = this;

		var sIdx,eIdx;

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

		var selectItem = [], addRowFlag= false;

		for(var i = sIdx ; i <= eIdx ; i++){
			var item = _this.options.tbodyItem[i];

			addRowFlag = false;

			if((allSelectFlag && !_this.isAllSelectUnSelectPosition( i,'row')) || _this.isSelectPosition(i,'row')) {
				addRowFlag = true;

			}

			if(addRowFlag) 	selectItem.push(i);
		}

		return selectItem;
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
				endCol = _this.options.colFixedIndex;
			}else if(type=='cont'){
				startCol = _this.options.colFixedIndex;
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
	,valueFormatter : function (_idx ,thiItem, rowItem, addEle){

		var type = thiItem.type || 'string';
		var itemVal = rowItem[thiItem.key];

		var tmpFormatter={};
		if(type == 'money' || type == 'number'){
			tmpFormatter = this.options.formatter[type];
		}

		if(isFunction(thiItem.formatter)){
			itemVal = thiItem.formatter.call(null,{idx : _idx , colInfo:thiItem, item: rowItem , formatInfo : tmpFormatter});
		}else{
			if(this.options.useDefaultFormatter===true){
				if(type == 'money'){
					itemVal = formatter[type](itemVal, tmpFormatter.fixed , tmpFormatter.prefix ,tmpFormatter.suffix);
				}else if(type == 'number'){
					itemVal = formatter[type](itemVal , tmpFormatter.fixed , tmpFormatter.prefix ,tmpFormatter.suffix);
				}
			}
		}

		if(addEle){
			if(thiItem.render=='html'){
				addEle.innerHTML = itemVal||'';
			}else{
				addEle.textContent = itemVal;
			}
		}
		return itemVal;
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
		var addClass;
		if(isFunction(thiItem.styleClass)){
			addClass = thiItem.styleClass.call(null,{idx : _idx, colInfo:thiItem, item: rowItem})
		}else{
			addClass=(thiItem.cellClass||'');
		}
		cellEle.setAttribute('class','pub-body-td '+addClass );
	}
	/**
	 * @method theadHtml
	 * @description get thead html template
	 */
	,theadHtml : function(type){
		var _this = this
			,cfg = _this.config
			,tci = _this.config.tColItem
			,headerOpt=_this.options.headerOptions;

		var strHtm = [];

		var headerInfo = cfg.headerInfo;

		if(type=='left'){
			headerInfo =  cfg.headerLeftInfo;
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
						strHtm.push(' <th '+ghItem.colspanhtm+' '+ghItem.rowspanhtm+' data-header-info="'+i+','+ghItem.resizeIdx+'" class="pubGrid-header-th" '+(ghItem.style?' style="'+ghItem.style+'" ':'')+'>');
						if(_this.options.headerOptions.helpBtn.enabled === true){
							strHtm.push('  <div class="pub-header-help-wrapper"><svg class="pub-header-help" viewBox="0 0 100 100"><g><polygon class="pub-header-help-btn" points="0 0,0 100,100 0"></polygon></g></svg> </div>');
						}
						strHtm.push('  <div class="label-wrapper">');
						strHtm.push('   <div class="pub-header-cont outer '+(ghItem.isSort===true?'sort-header':'')+'" col_idx="'+ghItem.resizeIdx+'"><div class="pub-inner"><div class="centered">'+ghItem.label+'</div></div>');
						if(ghItem.isSort ===true){
							strHtm.push('<div class="pub-sort-icon pubGrid-sort-up">'+_this.options.icon.sortup+'</div><div class="pub-sort-icon pubGrid-sort-down">'+ _this.options.icon.sortdown+'</div>');
						}
						strHtm.push('   </div>');
						strHtm.push('  </div>');
						strHtm.push('   <div class="pub-header-resizer" data-resize-idx="'+ghItem.resizeIdx+'"></div>');
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

		return idx < this.options.colFixedIndex ? true : false ;
	}
	/**
	 * @method setColFixedIndex
	 * @param  idx {Number} header column index
	 * @description 고정 컬럼
	 */
	,setColFixedIndex : function (idx){
		if(this.options.colFixedIndex > 0  && idx < 1){
			this.element.container.removeClass('pubGrid-col-fixed');
		}else if(this.options.colFixedIndex < 1  && idx > 0){
			this.element.container.addClass('pubGrid-col-fixed')
		}
		this.options.colFixedIndex = idx;
		this._setThead(false);
		this.setData(this.options.tbodyItem, 'init_colfixed')
	}
	/**
	 * @method getColFixedIndex
	 * @description get column fixed index
	 */
	,getColFixedIndex : function (){
		return this.options.colFixedIndex;
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
				_this.element.resizeHelper = $('#'+_this.prefix+'_resizeHelper');	// resize helper

				// query selector
				_this.element.leftContent = $pubSelector('#'+_this.prefix+'_bodyContainer .pubGrid-body-left-cont');
				_this.element.bodyContent = $pubSelector('#'+_this.prefix+'_bodyContainer .pubGrid-body-cont');

				//hidden area element
				_this.element.pasteArea = $('#'+_this.prefix +'_pubGridPasteArea');

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
				_this.setTheme(_this.options.theme);
				_this._initFooterEvent();
			}else{
				_this.element.header.find('.pubGrid-header-left-cont').empty().html(_this.theadHtml('left'));
				_this.element.header.find('.pubGrid-header-cont').empty().html(_this.theadHtml('cont'));

				_this._headerResize(headerOpt.resize.enabled);
				_this.getTbodyHtml('init_colfixed');

				_this.calcDimension('colfixed');
				_this.calcViewCol(_this.config.scroll.left);
			}
		}

		if(tbi.length < 1){
			_this.element.body.addClass('pubGrid-data-empty');
			return ;
		}else{
			_this.element.body.removeClass('pubGrid-data-empty');
		}

		var startCol=this.config.scroll.startCol
			, endCol=this.config.scroll.endCol;

		var startCellInfo = _this.config.selection.startCell;

		var fnAddStyle = _this.options.rowOptions.addStyle;

		var itemIdx = _this.config.scroll.viewIdx;
		var viewCount = _this.config.scroll.viewCount;

		// aside number size check
		if(_this.options.asideOptions.lineNumber.enabled ===true){
			var itemViewMaxCnt = itemIdx+viewCount;

			if((itemViewMaxCnt) > 999){
				var idxCharLen = (itemViewMaxCnt+'').length;

				if(_this.config.aside.lineNumberCharLength != idxCharLen){

					_this.config.aside.lineNumberCharLength = idxCharLen;

					var asideWidth = _this.options.asideOptions.lineNumber.width + ((idxCharLen-3) * _this.options.asideOptions.lineNumber.charWidth);

					$('#'+_this.prefix+'colhead'+_this.options.asideOptions.lineNumber.key).css('width',  asideWidth+'px');
					$('#'+_this.prefix+'colbody'+_this.options.asideOptions.lineNumber.key).css('width',  asideWidth+'px');

					_this.config.gridWidth.aside = _this.config.aside.initWidth - _this.options.asideOptions.lineNumber.width + asideWidth;
					_this.calcDimension('resize_aside');

				}
			}else{
				if(_this.config.aside.lineNumberCharLength != 0){
					_this.config.gridWidth.aside = _this.config.aside.initWidth;

					$('#'+_this.prefix+'colhead'+_this.options.asideOptions.lineNumber.key).css('width',  _this.options.asideOptions.lineNumber.width+'px');
					$('#'+_this.prefix+'colbody'+_this.options.asideOptions.lineNumber.key).css('width',  _this.options.asideOptions.lineNumber.width+'px');

					_this.config.aside.lineNumberCharLength= 0;
					_this.calcDimension('resize_aside');

				}
			}
			itemIdx = _this.config.scroll.viewIdx;
			viewCount = _this.config.scroll.viewCount;
		}

		// remove edit area
		if(_this.config.isCellEdit ===true){
			_this._setEditAreaData();
		}

		// row color change
		if(itemIdx%2==0){
			_this.element.body.removeClass('even');
		}else{
			_this.element.body.addClass('even');
		}

		var tbiItem , colItem;
		var tooltipFlag = _this.options.showTooltip===true?true:false;
		var asideItem =_this.config.aside.items;

		var colFixedIndex = this.options.colFixedIndex;

		for(var i =0 ; i < viewCount; i++){
			tbiItem = tbi[itemIdx] ||{};

			if(_this.config.rowOpt.isAddStyle){
				$pubSelector('#'+_this.prefix+'_bodyContainer .pubGrid-body-cont [rowinfo="'+i+'"]').setAttribute('style', (fnAddStyle.call(null,tbiItem)||''));
			}

			var overRowFlag = (itemIdx >= this.config.dataInfo.orginRowLen);
			var addEle ,tdEle;

			for(var j =0 ; j < asideItem.length ;j++){
				var tmpItem = asideItem[j];
				var rowCol = i+','+tmpItem.key;

				addEle =$pubSelector('#'+_this.prefix+'_bodyContainer .pubGrid-body-aside-cont').querySelector('[data-aside-position="'+rowCol+'"]>.aside-content');

				if(tmpItem.key == 'lineNumber'){
					addEle.textContent = (itemIdx+1);
				}else if(tmpItem.key == 'checkbox'){
					_$util.setCheckBoxCheck(addEle , tbiItem);
				}else if(tmpItem.key == 'modify'){
					addEle.textContent = 'V';
				}
			};

			if(drawMode != 'hscroll'){
				for(var j=0; j < colFixedIndex ; j++){
					tdEle =_this.element.leftContent.querySelector('[data-grid-position="'+(i+','+j)+'"]');
					addEle =tdEle.querySelector('.pub-content');

					colItem = tci[j];
					_this._setCellStyle(tdEle, i ,colItem , tbiItem)

					if(overRowFlag){
						addEle.textContent='';
					}else{
						var val = this.valueFormatter( i, colItem, tbiItem, addEle);
						_$util.setSelectCell(_this, startCellInfo, itemIdx, j, addEle);

						if(tooltipFlag){
							if(colItem.afTooltipFormatter){
								val = colItem.afTooltipFormatter({item : tbiItem ,r: i ,c: j , keyItem : colItem});
							}
							tdEle.title = val;
						}
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

					colItem = tci[j];

					_this._setCellStyle(tdEle, i ,colItem , tbiItem)

					if(overRowFlag){
						addEle.textContent='';
					}else{
						var val = this.valueFormatter( i, colItem, tbiItem, addEle);
						_$util.setSelectCell(_this, startCellInfo, itemIdx, j, addEle);

						if(tooltipFlag){

							if(colItem.afTooltipFormatter){
								val = colItem.afTooltipFormatter({item : tbiItem ,r: i ,c: j , keyItem : colItem});
							}
							tdEle.title = val;

						}
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

		if(this.options.page !== false && this.options.page.status === true){
			_this._statusMessage(viewCount);
		}
	}
	/**
	 * @method setElementDimensionAndMessage
	 * @description 그리드 element 수치 및 메시지 처리.
	 */
	,setElementDimensionAndMessage : function (){
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

		this.element.pubGrid.addClass(pubGridClass);
		this.element.header.css({'height' : header_height});
		this.element.header.find('.pubGrid-header-aside-cont').css({'line-height': (header_height)+'px' , 'height' : (header_height)+'px'})
		//this.element.header.find('.aside-label-wrapper').css('height',header_height-1+'px'); // 20190311

		$('#'+this.prefix+'_vscroll .pubGrid-scroll-top-area').css({'line-height': header_height+'px' , 'height' : header_height+'px'});

		$('#'+this.prefix+'_vscroll').css({'width' : this.options.scroll.vertical.width , 'padding-top' :  header_height});
		$('#'+this.prefix+'_hscroll').css({'height' : this.options.scroll.horizontal.height});

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
	, calcDimension : function (pType, opt){

		var typeInfo =pType.split('_');
		var type = typeInfo[0];
		var subType = typeInfo[1] ||'';

		var _this = this
			,cfg = _this.config;

		var dimension;

		if(type =='headerResize'){
			dimension = {width : cfg.container.width, height : cfg.container.height};
		}else{
			dimension = {width : _this.getGridWidth(), height :_this.getGridHeight()};
		}

		opt = objectMerge(dimension ,opt);
		cfg.container.height = opt.height;
		var mainHeight = opt.height - cfg.navi.height;

		if(type =='init' || type =='resize'){

			_this.element.pubGrid.css('height',mainHeight+'px');
			_this.element.pubGrid.css('width',opt.width+'px');

			var currentContainerWidth = cfg.container.width // border 값
				, resizeWidth = opt.width-cfg.blankSpaceWidth;

			if(currentContainerWidth != resizeWidth && type=='resize' && _this.options.autoResize !==false && _this.options.autoResize.responsive ===true){

				var _totW = cfg.gridWidth.total;

				var _overW = resizeWidth - currentContainerWidth;
				var _currGridMain = cfg.gridWidth.main;
				var _addW = 0;
				var _totColWidth =0;

				var colItems = cfg.tColItem;

				for(var i=0 ,colLen  = colItems.length; i<colLen; i++){
					var colItem = colItems[i];
					_addW = (_overW*(colItem.width/_currGridMain*100)/100);
					_this.setColumnWidth(i, (colItem.width+_addW), null, false);

					_totColWidth+=colItem.width;
				}

				cfg.gridWidth.main = _totColWidth;
			}
		}

		cfg.gridWidth.total = cfg.gridWidth.aside+cfg.gridWidth.left+ cfg.gridWidth.main;

		_this._setGridContainerWidth(opt.width, type);

		var  bodyH = mainHeight - cfg.header.height - cfg.footer.height
			, itemTotHeight = (cfg.dataInfo.rowLen-1) * cfg.rowHeight
			, vScrollFlag = (itemTotHeight > bodyH)
			, bodyW = (cfg.container.width-(vScrollFlag?this.options.scroll.vertical.width:0))
			, hScrollFlag = (Math.floor(cfg.gridWidth.total) > bodyW);

		vScrollFlag = (!vScrollFlag && hScrollFlag) ? (itemTotHeight > bodyH-this.options.scroll.horizontal.height) : vScrollFlag;
		hScrollFlag = (!hScrollFlag && vScrollFlag) ? (Math.floor(cfg.gridWidth.total) > cfg.container.width-this.options.scroll.vertical.width) : hScrollFlag;

		bodyW = (cfg.container.width-(vScrollFlag?this.options.scroll.vertical.width:0));

		//if(cfg.isResize !== true && !cfg.scroll.hUse && type == 'reDraw' && cfg.scroll.vUse != vScrollFlag){
		if(cfg.isResize !== true && (type == 'reDraw'||type=='resize') && cfg.scroll.vUse != vScrollFlag){
			var colItems = cfg.tColItem;
			var colLen = colItems.length;

			var _w =0, lastSpaceW=0;

			if(vScrollFlag){
				_w =  (this.options.scroll.vertical.width/colLen)*-1;
				lastSpaceW = this.options.scroll.vertical.width+(_w *colLen);
			}else{
				if(cfg.container.width > cfg.gridWidth.total){
					var addW = cfg.container.width-cfg.gridWidth.total;
					if(addW > colLen){
						_w = (addW /colLen);
						lastSpaceW = addW - (_w *colLen);
					}else{
						lastSpaceW =addW;
					}
				}
			}

			if(_w != 0 || lastSpaceW != 0){
				var _totColWidth =0;

				for(var i=0; i<colLen; i++){
					var colItem = colItems[i];

					_this.setColumnWidth(i ,colItem.width + _w, null, false);
					_totColWidth+=colItem.width;
				}

				_this.setColumnWidth(colLen-1 ,colItems[colLen-1].width +lastSpaceW , null, false);

				cfg.gridWidth.main = _totColWidth+lastSpaceW;
				cfg.gridWidth.total = cfg.gridWidth.aside+cfg.gridWidth.left+ cfg.gridWidth.main;

				hScrollFlag = Math.floor(cfg.gridWidth.total) > bodyW  ? true : false;
			}
		}

		//console.log('bodyW', cfg.gridWidth.aside, cfg.gridWidth.left , cfg.gridWidth.main)

		bodyH -= hScrollFlag? this.options.scroll.horizontal.height :0;
		_this._setPanelElementWidth();

		_this.element.body.css('height',(bodyH)+'px');

		var beforeViewCount = cfg.scroll.viewCount ;
		cfg.scroll.viewCount = itemTotHeight > bodyH ? Math.ceil(bodyH / cfg.rowHeight) : cfg.dataInfo.rowLen;
		cfg.scroll.bodyViewCount = Math.ceil(bodyH/cfg.rowHeight);
		cfg.scroll.insideViewCount = Math.floor(bodyH/cfg.rowHeight);
		cfg.container.bodyHeight = bodyH;

		var topVal = 0 ;
		if(vScrollFlag && cfg.dataInfo.orginRowLen >= cfg.scroll.viewCount){
			cfg.scroll.vUse = true;
			$('#'+_this.prefix+'_vscroll').css('padding-bottom',(hScrollFlag?(_this.options.scroll.horizontal.height-1):0));
			$('#'+_this.prefix+'_vscroll').show();

			var scrollH = $('#'+_this.prefix+'_vscroll').find('.pubGrid-vscroll-bar-area').height();

			var barHeight = (scrollH*(bodyH/(itemTotHeight)*100))/100;
			if(scrollH <25){
				barHeight = 1;
			}else{
				barHeight = barHeight < 25 ? 25 : ( barHeight > scrollH ?scrollH :barHeight);
			}

			cfg.scroll.vHeight = scrollH;
			cfg.scroll.vThumbHeight = barHeight;
			cfg.scroll.vTrackHeight = scrollH - barHeight;
			cfg.scroll.oneRowMove = cfg.scroll.vTrackHeight/(cfg.dataInfo.rowLen-cfg.scroll.viewCount);

			topVal = cfg.scroll.vTrackHeight* cfg.scroll.vBarPosition/100;

			_this.element.vScrollBar.css('height',barHeight);
			if(scrollH < 25){
				_this.element.vScrollBar.hide();
			}else{
				_this.element.vScrollBar.show();
			}
		}else{
			cfg.scroll.vUse = false;
			$('#'+_this.prefix+'_vscroll').hide();
		}

		var leftVal =0;
		if(hScrollFlag){
			var gridContTotW = cfg.gridWidth.total;

			cfg.scroll.hUse = true;
			$('#'+_this.prefix+'_hscroll').css('padding-right',(vScrollFlag?_this.options.scroll.vertical.width:0));
			$('#'+_this.prefix+'_hscroll').show();

			var hscrollW = $('#'+_this.prefix+'_hscroll').find('.pubGrid-hscroll-bar-area').width();

			var barWidth = (hscrollW*(bodyW/gridContTotW*100))/100;
			barWidth = barWidth < 25 ? 25 :( barWidth > hscrollW ?hscrollW :barWidth);

			cfg.scroll.hThumbWidth = barWidth;
			cfg.scroll.hTrackWidth =hscrollW - barWidth;
			cfg.scroll.oneColMove = gridContTotW/cfg.scroll.hTrackWidth;
			leftVal = cfg.scroll.hTrackWidth* cfg.scroll.hBarPosition/100;
			_this.element.hScrollBar.css('width',barWidth)
		}else{
			cfg.scroll.hUse= false;
			$('#'+_this.prefix+'_hscroll').hide();
		}

		// main inside width
		cfg.gridWidth.mainInsideWidth = (cfg.container.width - cfg.gridWidth.aside - cfg.gridWidth.left-(cfg.scroll.vUse?_this.options.scroll.vertical.width:0));
		// main over width value
		cfg.gridWidth.mainOverWidth = cfg.gridWidth.main-cfg.gridWidth.mainInsideWidth;


		if(cfg.scroll.maxViewCount <cfg.scroll.viewCount  ) cfg.scroll.maxViewCount = cfg.scroll.viewCount;

		var drawFlag =false;



		if(type !='init' && ( beforeViewCount != cfg.scroll.viewCount )){
			drawFlag = _this.getTbodyHtml();
		}

		if(subType =='aside'){
			_this.moveVerticalScroll({rowIdx :cfg.scroll.viewIdx, drawFlag : false,resizeFlag:true});
			return ;
		}

		if(cfg.scroll.startCol != cfg.scroll.before.startCol || cfg.scroll.before.endCol != cfg.scroll.endCol ){
			drawFlag = true;
		}

		if(type =='reDraw'){
			topVal=0;
			leftVal=0;
		}else if(type == 'addData'){
			if(cfg.scroll.vUse){
				topVal = cfg.scroll.viewIdx * cfg.scroll.oneRowMove;
			}
		}

		if(type=='resize' ||type =='headerResize'){
			_this.moveVerticalScroll({pos :topVal, drawFlag : false,resizeFlag:true});
			_this.moveHorizontalScroll({pos :leftVal, drawFlag : false,resizeFlag:true});

			this.calcViewCol(leftVal);
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
	,setColWidth : function (){

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
		_this.element.header.find('.pubGrid-header-cont').css('width',(_this.config.gridWidth.main)+'px');

		// body grid set width
		_this.element.body.find('.pubGrid-body-aside-cont').css('width',(_this.config.gridWidth.aside)+'px');
		_this.element.body.find('.pubGrid-body-left-cont').css('width',(_this.config.gridWidth.left)+'px');
		_this.element.body.find('.pubGrid-body-cont').css('width',(_this.config.gridWidth.main)+'px');

		// set horizontal scroll padding
		//$('#'+_this.prefix+'_hscroll').css('padding-left' , _this.config.gridWidth.aside);
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
		var startTime = '';
		var hDragDelay = _this.options.scroll.horizontal.dragDelay;
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

				if(startTime==''){
					startTime = new Date().getTime();
				}

				if(new Date().getTime()-hDragDelay <= startTime){
					clearTimeout(scrollbarDragTimeer);
				}

				scrollbarDragTimeer = setTimeout(function() {
					startTime='';
					_this.horizontalScroll(data, e, 'move');
				}, hDragDelay);
			}).on('touchend.pubhscroll mouseup.pubhscroll mouseleave.pubhscroll', function (e){
				ele.removeClass('active');
				clearTimeout(scrollbarDragTimeer);
				startTime='';
				_this.horizontalScroll(data,e, 'end');
			});

			return true;
		});


		var tooltipFlag = _this.options.scroll.vertical.tooltip;
		var vDragDelay = _this.options.scroll.vertical.dragDelay;
		var tooltipEle = _this.element.vScrollBar.find('.pubGrid-vscroll-bar-tip');
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
				if(startTime==''){
					startTime = new Date().getTime();
				}

				if(new Date().getTime()-vDragDelay <= startTime){
					clearTimeout(scrollbarDragTimeer);
				}

				scrollbarDragTimeer = setTimeout(function() {
					startTime='';
					_this.verticalScroll( data,e , 'move');

					if(tooltipFlag){
						tooltipEle.text(_this.config.scroll.viewIdx+1);
						tooltipEle.show();
					}
				}, vDragDelay);
			}).on('touchend.pubvscroll mouseup.pubvscroll mouseleave.pubvscroll', function (e){
				ele.removeClass('active');
				clearTimeout(scrollbarDragTimeer);
				_this.verticalScroll(data, e , 'end');
				startTime='';

				if(tooltipFlag){
					tooltipEle.hide();
				}
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
				if(onUpdateFn.call(null, {scrollTop : topVal, height :  this.config.scroll.vTrackHeight, barPosition : barPos }) === false){
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

		if(!_this.config.scroll.hUse){
			if(this.config.scroll.left > 0){
				this.moveHScrollPosition(0, moveObj.drawFlag);
			}

			if(moveObj.resizeFlag !== true){
				return ;
			}
		}

		var posVal = moveObj.pos;

		var leftVal= posVal;

		if(isNaN(posVal)){

			if(isUndefined(moveObj.colIdx)){
				leftVal =_this.config.scroll.left+((posVal=='L'?-1:1) * _this.config.scroll.oneColMove);
			}else{
				var lastHeaderIdx = _this.config.headerInfo.length-1;

				var colIdxHeaderEle = _this.element.header.find('[data-header-info="'+lastHeaderIdx+','+moveObj.colIdx+'"]');
				var headerPos = colIdxHeaderEle.position();

				if(posVal =='L'){
					leftVal = headerPos.left;
				}else{
					leftVal = headerPos.left + colIdxHeaderEle.outerWidth();
					leftVal = leftVal - (_this.config.gridWidth.mainInsideWidth);
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
		return leftVal < 1? 0:(((this.config.gridWidth.mainOverWidth)*(leftVal/this.config.scroll.hTrackWidth*100))/100);
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

		var contLeftVal  = this.calcViewCol(leftVal);

		this.config.scroll.left = leftVal;
		this.config.scroll.hBarPosition = leftVal/hw*100;

		if(updateChkFlag !== false){
			var onUpdateFn = this.options.scroll.horizontal.onUpdate;
			if(drawFlag !== false && isFunction(onUpdateFn)){
				if(onUpdateFn.call(null, {scrollLeft : leftVal , width : this.config.scroll.hTrackWidth, barPosition : this.config.scroll.hBarPosition}) === false){
					return ;
				}
			}
		}

		this.element.hScrollBar.css('left',this.config.scroll.left+'px');
		this.element.header.find('.pubGrid-header-cont-wrapper').css('left','-'+contLeftVal+'px');
		this.element.body.find('.pubGrid-body-cont-wrapper').css('left','-'+contLeftVal+'px');

		if(drawFlag !== false){
			this.drawGrid('hscroll');
		}
	}

	/**
	 * @method calcViewCol
	 * @param leftVal {Integer} body left position
	 * @description view col 위치 구하기.
	 */
	,calcViewCol : function (leftVal){

		var containerLeft  = this._getBodyContainerLeft(leftVal);

		var tci = this.config.tColItem;
		var gridW = containerLeft+this.config.gridWidth.mainInsideWidth;
		var itemLeftVal=0;
		var startCol = 0, endCol =tci.length-1;
		var startFlag = true, inSideStartFlag = true;

		for(var i = this.options.colFixedIndex ;i <tci.length ;i++){

			if(inSideStartFlag && itemLeftVal >= containerLeft){
				this.config.scroll.insideStartCol = i;
				inSideStartFlag = false;
			}

			itemLeftVal +=tci[i].width;

			if(startFlag && itemLeftVal > containerLeft){
				startCol = i;
				startFlag = false;
				continue;
			}

			if( itemLeftVal >=gridW){
				endCol = i;
				break;
			}
		}
		this.config.scroll.containerLeft = containerLeft;
		this.config.scroll.before.startCol = this.config.scroll.startCol; // 이전데이타
		this.config.scroll.before.endCol = this.config.scroll.endCol;

		this.config.scroll.startCol = ( startCol > 0? startCol:0 );
		this.config.scroll.endCol = ( endCol >= tci.length? tci.length:endCol );

		// 화면에 다 보이는 col size
		this.config.scroll.insideEndCol = this.config.scroll.endCol + ( itemLeftVal != gridW ? -1 : 0 );

		return containerLeft;
	}
	,_statusMessage : function (viewCnt){
		var startVal = this.config.scroll.viewIdx +1
			,endVal = startVal+ this.config.scroll.insideViewCount;

		endVal = endVal >= this.config.dataInfo.orginRowLen? this.config.dataInfo.orginRowLen: endVal;

		this.element.status.empty().html(this.options.message.pageStatus({
			currStart :startVal
			,currEnd : endVal
			,total : this.config.dataInfo.orginRowLen
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
	 * @method getFieldValues
	 * @param  key {String} item key
	 * @description filed 값 얻기.
	 */
	,getFieldValues:function (key){
		if(isUndefined(key)) return [];
		else {
			var tbodyItem =this.options.tbodyItem;
			var reval = [];
			for(var i =0, len=tbodyItem.length;i < len; i++){
				tbodyItem[i];
				reval.push(tbodyItem[i][key]);
			}
			return reval;
		}
	}
	/**
	 * @method getCheckItems
	 * @param  type {String} // default = 'item', 'index'  return type
	 * @description check item 값 얻기.
	 */
	,getCheckItems: function (type){
		var tbodyItem =this.options.tbodyItem;
		var reval = [];
		for(var i =0, len=tbodyItem.length;i < len; i++){
			var item = tbodyItem[i];
			if(item['_pubcheckbox']===true){
				if(type=='index'){
					reval.push(i);
				}else{
					reval.push(item);
				}
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
	,setCheckItems: function (idxArr, checkFlag, drawFlag){
		var tbodyItem =this.options.tbodyItem;
		var item;
		checkFlag = isUndefined(checkFlag)?true:checkFlag;
		if(idxArr=='all'){

			this.config.allCheck = checkFlag;
			var allChkEle =this.element.header.find('.pub-header-checkbox');

			if(checkFlag){
				allChkEle.addClass('pub-check-all');
			}else{
				allChkEle.removeClass('pub-check-all');
			}

			for(var i =0, len=tbodyItem.length;i < len; i++){
				tbodyItem[i]['_pubcheckbox']= checkFlag;
			}
		}else{
			if(!$.isArray(idxArr)){
				idxArr = [idxArr];
			}

			for(var i =0, len=idxArr.length;i < len; i++){
				item = tbodyItem[idxArr[i]];
				if(item) item['_pubcheckbox'] = checkFlag;
			}
		}
		if(drawFlag !== false){
			this.drawGrid();
		}
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
					_$util.clearActiveColumn(_this.element);
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

		// header mouse scroll
		if(headerOpt.scrollEnabled ===true){
			_this.element.header.off('mousewheel DOMMouseScroll');
			_this.element.header.on('mousewheel DOMMouseScroll', function(e) {
				var oe = e.originalEvent;
				var delta = 0;

				if (oe.detail) {
					delta = oe.detail * -40;
				}else{
					delta = oe.wheelDelta;
				};

				//delta > 0--up
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
			});
		}

		if(_this.options.asideOptions.rowSelector.enabled === true){
			// checkbox click
			_this.element.header.on('click.pubGrid.allcheck','.pub-header-checkbox',function (e){
				_this.setCheckItems('all', !_this.config.allCheck);
			});
		}

		if(_this.options.headerOptions.helpBtn.enabled ===true){
			var fnHelpClick = _this.options.headerOptions.helpBtn.click;
			if(isFunction(fnHelpClick)){
				_this.element.header.on('click.pubGrid.helpbtn','.pub-header-help-btn',function (e){
					var selEle = $(this);
					var headerInfo = selEle.closest('[data-header-info]').attr('data-header-info').split(',');

					fnHelpClick.call(selEle, {item :_this.config.headerInfo[headerInfo[0]][headerInfo[1]], c :headerInfo[1] })
				});
			}

			var fnHelpDblclick = _this.options.headerOptions.helpBtn.dblclick;
			if(isFunction(fnHelpDblclick)){
				_this.element.header.on('dblclick.pubGrid.helpbtn','.pub-header-help-btn',function (e){
					var selEle = $(this);
					var headerInfo = selEle.closest('[data-header-info]').attr('data-header-info').split(',');

					fnHelpDblclick.call(selEle, {item :_this.config.headerInfo[headerInfo[0]][headerInfo[1]], c :headerInfo[1] })
				});
			}
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

			_this.setSorting({key : _this.config.tColItem[col_idx].key, sortType : sortType});
		});

		if(headerOpt.contextMenu !== false){
			_this.config.headerContext = $.pubContextMenu('#'+_this.prefix+'_headerContainer .pubGrid-header-th',headerOpt.contextMenu);
		}
		if(_this.options.setting.enabled ===true){
			_this.setGridSettingInfo();
		}
	}
	,_initFooterEvent : function (){
		var _this = this;

		var pageCallback = _this.options.page.callback;
		_this.element.navi.on('click', '.page-num',function() {
			var sEle = $(this);
			var pageno =sEle.attr('pageno');

			_this.element.navi.find('li.active').removeClass('active');
			_this.element.navi.find('li[pageno="'+pageno+'"]').addClass('active');

			if (isFunction(pageCallback)) {
				_this.config.pageNo = pageno;
				pageCallback(pageno);
			}
		});
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
			schSelEle.on('keydown.pubGrid.search',function(e) {
				if (e.keyCode == '13') {
					settingWrapper.find('[data-setting-mode="search"]').trigger('click');
					return false;
				}
			});

			//버튼 클릭.
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

					settingOpt.configVal.search = settingVal.search;

					_this._setSearchData('search');
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

				var initFlag=true;
				if(e.ctrlKey){
					initFlag  = false;
				}
				var rowIdx = _this.config.scroll.viewIdx+intValue(row_idx);

				_this.addRowSelections(rowIdx, initFlag);
			});
		}

		if(asideOpt.rowSelector.enabled === true){

			var fnRowChkClick = asideOpt.rowSelector.click;

			// checkbox click
			_this.element.body.find('.pubGrid-body-aside').on('click.pubGrid.check','.pub-row-check',function (e){
				var selEle = $(this)
					,rowinfo = selEle.closest('tr').attr('rowinfo');

				rowinfo = _this.config.scroll.viewIdx+intValue(rowinfo);

				var selItem = _this.options.tbodyItem[rowinfo];

				if(isFunction(fnRowChkClick) && fnRowChkClick.call(selEle ,{item : selItem ,r: rowinfo})===false){
					selEle.prop('checked',false);
					selItem['_pubcheckbox'] =false;
					return ;
				}

				selItem['_pubcheckbox'] = selItem['_pubcheckbox'] === true ? false :true;
			});
		}

		// row cell double click event
		var dblCheckFlag = _this.options.rowOptions.dblClickCheck===true;

		var editable = _this.options.editable;
		if(editable || dblCheckFlag || isFunction(_this.options.rowOptions.dblClick) || isFunction(_this.options.bodyOptions.cellDblClick)){
			var fnDblClick = _this.options.rowOptions.dblClick || _this.options.bodyOptions.cellDblClick ||(function (){});

			_this.element.body.on('dblclick.pubgrid.td','.pub-body-td',function (e){
				var selRow = $(this)
					,tdInfo=selRow.data('grid-position')
					,rowColArr  = tdInfo.split(',');

				var rowIdx = _this.config.scroll.viewIdx+intValue(rowColArr[0])
					,colIdx = intValue(rowColArr[1]);

				var rowItem = _this.options.tbodyItem[rowIdx]
					colItem = _this.config.tColItem[colIdx];

				if(editable ===true){
					if(colItem.editor===false) return ;

					_this.config.isCellEdit = true;

					_this.config.editRowInfo = {
						idx : rowIdx
						,colItem : colItem
						,rowItem : rowItem
					};

					_$util.getEditForm(selRow,colItem ,rowItem);
					return ;
				}

				if(dblCheckFlag){
					_this.options.tbodyItem[rowIdx] = _this.getRowCheckValue(rowItem,rowItem['_pubcheckbox']===true?false:true);

					var addEle =$pubSelector('#'+_this.prefix+'_bodyContainer .pubGrid-body-aside-cont').querySelector('[data-aside-position="'+rowColArr[0]+',checkbox"]>.aside-content');

					_$util.setCheckBoxCheck(addEle , rowItem);
				}

				fnDblClick.call(selRow ,{item : rowItem ,r: rowIdx ,c:colIdx , keyItem : colItem} );
			});
		}

		// edit focusout event
		_this.element.body.on('focusout.pubgrid.edit','.pubGrid-edit-area',function (e){
			e.preventDefault();
			e.stopPropagation();

			if(_this.config.isCellEdit===false) return ;

			_this._setEditAreaData();
		});

		_this._setSelectionRangeInfo({isMouseDown : false});

		var bodyDragTimer;
		var bodyDragDelay = 150;
		var multipleFlag = _$util.isMultipleSelection(selectionMode);

		function dragScrollMove(){
			bodyDragTimer = setInterval(function() {
				if(_this.config.mouseDragDirectionY !==false){
					_this.moveVerticalScroll({pos :_this.config.mouseDragDirectionY});
				}

				if(_this.config.mouseScrollDirectionX !==false){
					_this.moveHorizontalScroll({pos :_this.config.mouseScrollDirectionX});
				}
			}, bodyDragDelay);
		}

		var rowClickFn = _this.options.rowOptions.click;
		var rowClickFlag = isFunction(rowClickFn);

		// body  selection 처리.
		_this.element.body.on('mousedown.pubgrid.col','.pub-body-td',function (e){

			if(e.which ===3){
				return true;
			}

			var oe = e.originalEvent.touches;
			var startPageX = oe ? oe[0].pageX : e.pageX;
			var startPageY = oe ? oe[0].pageY : e.pageY;

			var position  = _this.element.body.offset();

			var _l  = position.left
				,_r = _l +_this.config.container.width - _this.options.scroll.vertical.width;
			var _t = position.top,
				_b = _t+_this.config.container.bodyHeight;

			if(multipleFlag){
				// mouse darg scroll
				$(document).on('touchmove.pubgrid.body.drag mousemove.pubgrid.body.drag', function (e1){

					var oe1 = e1.originalEvent.touches;
					var movePageX = oe1 ? oe1[0].pageX : e1.pageX;
					var movePageY = oe1 ? oe1[0].pageY : e1.pageY;

					_this.config.mouseScrollDirectionX =false;
					if(movePageX < _l){
						_this.config.mouseScrollDirectionX = 'L';
					}else if(movePageX > _r){
						_this.config.mouseScrollDirectionX = 'R';
					}

					_this.config.mouseDragDirectionY = false;
					if(movePageY < _t){
						_this.config.mouseDragDirectionY = 'U';
					}else if(movePageY > _b){
						_this.config.mouseDragDirectionY = 'D';
					}

					if(!bodyDragTimer)dragScrollMove();

				}).on('touchend.pubgrid.body.drag mouseup.pubgrid.body.drag mouseleave.pubgrid.body.drag', function (e1){
					$(document).off('touchmove.pubgrid.body.drag mousemove.pubgrid.body.drag').off('touchend.pubgrid.body.drag mouseup.pubgrid.body.drag mouseleave.pubgrid.body.drag');
					clearInterval(bodyDragTimer);
					bodyDragTimer = false;
				});
			}

			var sEle = $(this)
				,gridTdPos = sEle.attr('data-grid-position')
				,selCol = gridTdPos.split(',')
				,selRow = intValue(selCol[0])
				,colIdx = intValue(selCol[1]);

			var selIdx = _this.config.scroll.viewIdx+intValue(selRow);

			if(!_this._isFixedPostion(colIdx)){
				if(colIdx < _this.config.scroll.insideStartCol){
					_this.moveHorizontalScroll({pos:'L' ,colIdx :colIdx});
				}else if(colIdx > _this.config.scroll.insideEndCol){
					_this.moveHorizontalScroll({pos:'R' ,colIdx :colIdx});
				}
			}
			var selectRangeInfo = _$util.getSelectionModeColInfo( selectionMode ,colIdx , _this.config.dataInfo ,_this.config.selection.isMouseDown);

			var selItem = _this.options.tbodyItem[selIdx];
			var colItem = _this.config.tColItem[colIdx];

			_this.config.currentClickInfo ={
				column : colItem
				,item : selItem
				,r : selIdx
				,c : colIdx
			};

			if(multipleFlag && e.shiftKey) {	// shift key
				var rangeInfo = {endIdx: selIdx, endCol: selectRangeInfo.endCol};

				if(selectRangeInfo.startCol > -1){
					rangeInfo.srartCol = selectRangeInfo.startCol;
				}

				_this._setSelectionRangeInfo({
					rangeInfo : rangeInfo
					,isMouseDown : true
				},false , true);

			}else if(multipleFlag && e.ctrlKey){ // ctrl key

				_this._setSelectionRangeInfo({
					rangeInfo : {startIdx : selIdx, endIdx : selIdx, startCol : selectRangeInfo.startCol, endCol: selectRangeInfo.endCol}
					,isSelect : true
					,curr : (_this.config.selection.isSelect?'add':'')
					,isMouseDown : true
					,startCell : {startIdx : selIdx, startCol : selectRangeInfo.startCol}
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

			if(isFunction(colItem.colClick)){
				colItem.colClick.call(this,colIdx,{
					r:selIdx
					,c:colIdx
					,item:selItem
				});
				return true;
			}
			// row click event
			if(rowClickFlag){
				if(sEle.closest('.pubGrid-body-aside-cont').length > 0){
					return true;
				}
				rowClickFn.call(null , _this.getCurrentClickInfo());
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

			var selectRangeInfo = _$util.getSelectionModeColInfo( selectionMode ,colIdx , _this.config.dataInfo);

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
		}).on('mousedown.'+_this.prefix,function (e){ // focus in
			_this.config.focus = true;
		}).on('blur.'+_this.prefix,function (e){ //blur focus out
			_this.config.focus = false;
		})

		// focus out
		$(document).on('mousedown.'+_this.prefix, 'html', function (e) {
			if(!_this.config.focus){
				return true;
			}

			if(e.which !==2 && $(e.target).closest('#'+_this.prefix+'_pubGrid').length < 1){
				_this.config.focus = false;
			}
		});

		if(_this.options.editable===true){

			var pasteBeforeFn = _this.options.rowOptions.pasteBefore;
			var pasteBeforeFnFlag = isFunction(pasteBeforeFn);

			var pasteAfterFn = _this.options.rowOptions.pasteAfter;
			var pasteAfterFnFlag = isFunction(pasteAfterFn);

			// paste event
			_this.element.pasteArea.on('paste.pubGrid', function (e){

				var orginEvt = e.originalEvent;
				var content ='';
				if( orginEvt.clipboardData && orginEvt.clipboardData.getData ){
					content = orginEvt.clipboardData.getData('text');
				}else if( e.clipboardData && e.clipboardData.getData ){
					content = e.clipboardData.getData('text/plain');
				}else if( window.clipboardData  && window.clipboardData.getData ){
					content = window.clipboardData.getData('Text');
				}

				if(pasteBeforeFnFlag){
					content = pasteBeforeFn.call(null , content);
				}

				if(content !=''){
					var contentArr = content.split(/\r\n|\r|\n/);

					var startCellInfo = _this.config.selection.startCell;

					var tColItems = _this.config.tColItem
						,tbodyItems = _this.options.tbodyItem;

					var startIdx = startCellInfo.startIdx
						,startCol = startCellInfo.startCol
						,tColLen =tColItems.length
						,tbodyLen =tbodyItems.length;

					var maxCol=0
						,iLen = contentArr.length;

					if(startCellInfo.startIdx+iLen > tbodyLen){ // 붙여 넣기가 row가 더 많으면 추가 item 생성.
						tbodyItems = tbodyItems.concat(_$util.newItems(tColItems, startCellInfo.startIdx+iLen -tbodyLen));
						tbodyLen =tbodyItems.length;
					}

					for(var i =0; i<iLen; i++){
						var addCont = contentArr[i];

						var addRowIdx = startIdx+i;

						if(addRowIdx >= tbodyLen){
							break;
						}

						var selItem = tbodyItems[addRowIdx];

						var addContArr = addCont.split(/\t/);
						var jLen=addContArr.length;

						_$util.setCUD(selItem);

						for(var j =0; j <jLen; j++){
							var addColIdx = startCol+j;

							if(addColIdx < tColLen){
								maxCol = Math.max(maxCol,addColIdx);
								selItem[tColItems[addColIdx].key] = addContArr[j];
							}
						}
					}

					_this._setSelectionRangeInfo({
						rangeInfo :  {startIdx : startCellInfo.startIdx,endIdx : startCellInfo.startIdx+iLen-1, startCol:startCellInfo.startCol,endCol :maxCol}
						,startCell : startCellInfo
					},true, false);

					_this.setData(tbodyItems,'reDraw_paste' ,{focus:true,index: _this.config.scroll.viewIdx});

					if(pasteAfterFnFlag){
						pasteAfterFn.call(null , content);
					}
				}
			})
		}

		var copyMode = _this.options.copyMode;
		var selectionMode = _this.options.selectionMode;
		// window keydown 처리.  tabindex 처리 확인 해볼것.
		$(window).on("keydown." + _this.prefix, function (e) {

			if(!_this.config.focus) return ;

			// 설정 영역 keydown 처리
			if($(e.target).closest('.pubGrid-setting-area').length > 0) return true;

			var evtKey = window.event ? e.keyCode : e.which;

			if (e.metaKey || e.ctrlKey) { // copy

				if (evtKey == 67) { // ctrl+ c
					if(copyMode =='none'){
						return ;
					}

					var copyData = '';

					if(selectionMode =='row' && copyMode =='single' && _this.config.selection.allSelect !== true ){
						var startCellInfo = _this.config.selection.startCell;
						var selItem = _this.options.tbodyItem[startCellInfo.startIdx];

						if(isUndefined(selItem)){
							return ;
						}

						copyData = _this.options.tbodyItem[startCellInfo.startIdx][_this.config.tColItem[startCellInfo.startCol].key];
					}else{
						copyData = _this.selectionData();
					}

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
				}else if(evtKey==86){ // ctrl + v
					_this.element.pasteArea.focus();
					return true;
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
	 * @method _setEditAreaData
	 * @description remove edit area element
	 */
	,_setEditAreaData :function (){
		if(this.config.isCellEdit===true){

			var selRow = this.element.body.find('.pubGrid-edit-area');

			var editRowInfo = this.config.editRowInfo
				rowIdx = editRowInfo.idx
				,rowItem = editRowInfo.rowItem
				,colItem = editRowInfo.colItem;

			var newVal = selRow.find('.pubGrid-edit-field').val();

			if(newVal != rowItem[colItem.key]){

				colItem.maxWidth = Math.max(getCharLength(newVal||'', this.options.headerOptions.oneCharWidth),colItem.maxWidth);

				rowItem[colItem.key] = newVal;
				_$util.setCUD(rowItem);
			}

			var tdEle = selRow.closest('.pub-body-td').get(0);

			var addEle =tdEle.querySelector('.pub-content');

			this._setCellStyle(tdEle, rowIdx ,colItem, rowItem);
			this.valueFormatter( rowIdx, colItem,rowItem , addEle);

			this.config.isCellEdit = false;
			this.config.editRowInfo = {};
			selRow.remove();
		}
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
				}else if(!_this._isFixedPostion(moveColIdx) && moveColIdx <= scrollInfo.startCol){
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

			var multipleFlag = _$util.isMultipleSelection(_this.options.selectionMode);

			if(multipleFlag && (evtKey != 9 && evt.shiftKey)){
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

			if(!_this._isFixedPostion(moveColIdx)){
				if(endCol < scrollInfo.startCol){ // 스크롤 밖에 있을때
					_this.moveHorizontalScroll({pos: 'L',colIdx :moveColIdx});
					reFlag = true;
				}else if(endCol > scrollInfo.endCol){
					_this.moveHorizontalScroll({pos: 'R',colIdx :moveColIdx});
					reFlag = true;
				}
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
	 * @param  initFlag {boolean} init flag
	 * @param  tdSelectFlag {boolean} select flag
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
					}else if(attrInfo[key]=='remove'){
						cfgSelect.curr+=1;
						cfgSelect.range = rangeInfo;
						var rangeKey = rangeInfo._key ?  rangeInfo._key : cfgSelect.curr;
						rangeInfo.mode = 'remove';
						delete cfgSelect.allRange[rangeKey];
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
			_$util.clearActiveColumn(_this.element);
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
			if(col =='row'){
				return range.minIdx <=row && row <= range.maxIdx;
			}else{
				return range.minIdx <=row && row <= range.maxIdx && range.minCol <=col && col <= range.maxCol;
			}
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

		opt = objectMerge({isSelect :false,dataType : 'text', isFormatValue: false} ,opt);

		var dataType =opt.dataType
			,isDataTypeJson = (dataType=='json')
			,isFormatValue = opt.isFormatValue===true;

		if(opt.isSelect===true){
			return _this.selectionData(dataType);
		}else{

			var tbodyItem = _this.options.tbodyItem;
			var tColItem = _this.config.tColItem;

			var tbodyLen = tbodyItem.length
				,tColLen = tColItem.length;

			if(tbodyLen < 1 ) {
				if(isDataTypeJson){
					return {
						header : tColItem
						,data : []
					}
				}else{
					return '';
				}
			}

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

					var tmpVal = '';

					if(isFormatValue){
						if(colItem.render=='html'){
							tmpVal = item[tmpKey];
						}else{
							tmpVal = _this.valueFormatter( i, colItem,item);
						}
					}else{
						tmpVal = item[tmpKey];
					}

					if(isDataTypeJson){
						rowItem[tmpKey] = tmpVal;
					}else{
						rowText.push(tmpVal);
					}
				}

				if(isDataTypeJson){
					returnVal.push(rowItem);
				}else{
					returnVal.push(rowText.join('\t'));
				}
			}

			if(isDataTypeJson){
				return {
					header : tColItem
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

		if(sIdx < 0 || eIdx < 0)
			return dataType == 'json'? {} :'';

		var returnVal = [];
		var addRowFlag;

		var tbodyItem = _this.options.tbodyItem;
		var tColItem = _this.config.tColItem;

		var keyInfo ={};
		var tmpVal = '';
		for(var i = sIdx ; i <= eIdx ; i++){
			var item = tbodyItem[i];

			var rowText = [], rowItem = {'_pubIdx' : i};
			addRowFlag = false;

			for(var j=sCol ;j <= eCol; j++){
				var colItem = tColItem[j];

				if(colItem.visible===false) continue;

				var tmpKey = colItem.key;

				if((allSelectFlag && !_this.isAllSelectUnSelectPosition( i,j)) || _this.isSelectPosition(i,j)) {
					addRowFlag = true;

					if(colItem.render=='html'){
						tmpVal = item[tmpKey];
					}else{
						tmpVal = _this.valueFormatter( i, colItem,item);
					}

					if(dataType=='json'){
						keyInfo[j] = colItem;
						rowItem[tmpKey] = tmpVal;
					}else{
						rowText.push(tmpVal);
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
	 * @method getCurrentClickInfo
	 * @description current click info
	 */
	,getCurrentClickInfo : function (){
		return this.config.currentClickInfo;
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

		if(!$.isArray(itemKeys)){
			itemKeys = [itemKeys];
		}
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
			this.config.rowContext = $.pubContextMenu('#'+this.prefix+'_bodyContainer .pub-body-tr',this.options.rowOptions.contextMenu);
		}
	}
	/**
	 * @method setSorting
	 * @param  sortInfo {Object} [{key, val, sortType}]column key , value ,sortType ="a" or "d"
	 * @description data sorting
	 */
	,setSorting : function (sortInfoArr){

		if(!$.isArray(sortInfoArr)){
			sortInfoArr = [sortInfoArr];
		}

		for(var i =0, len = sortInfoArr.length;i < len; i++){
			var sortInfo = sortInfoArr[i];
			this.setData(this._getSortList(sortInfo.key, sortInfo.sortType, sortInfo.val) ,'sort');
		}
	}
	/**
	 * @method _getSortList
	 * @param  idx {Integer} item index
	 * @param  sortType {String} 정렬 타입 ex(asc,desc)
	 * @param  val {String,Integer} 정렬값
	 * @description data sorting 처리.
	 */
	,_getSortList :function (key, sortType, val){
		var _this = this
			,tbi = _this.options.tbodyItem
			,val  = val ||'';

		var _key = key;

		if(isUndefined(_this.config.sort[_key])){
			_this.config.sort[_key]= {sortType : sortType};
		}else{
			_this.config.sort[_key].sortType=sortType;
		}

		if(_this.config.sort.current != _key){
			_this.config.sort.orginData = tbi.slice(0);
		}

		_this.config.sort.current = _key;

		function getItemVal(itemObj){
			return itemObj[_key];
		}

		if(sortType=='asc'){  // 오름차순

			tbi.sort(function (a,b){
				var v1 = getItemVal(a)
					,v2 = getItemVal(b);

				if(v1 == v2) return 0;

				if(val != ''){
					return (v1==val?-1:(v2==val)?1:0);
				}else{
					return v1 < v2 ? -1 : v1 > v2 ? 1 : 0;
				}
			});
		}else if(sortType=='desc'){
			tbi.sort(function (a,b){ // 내림차순
				var v1 = getItemVal(a)
					,v2 = getItemVal(b);

				if(v1 == v2) return 0;

				if(val != ''){
					return (v1==val?1:(v2==val)?-1:0);
				}else{
					return v1 > v2 ? -1 : v1 < v2 ? 1 : 0;
				}
			});
		}else{
			_this.config.sort.current = '';
			tbi = _this.config.sort.orginData;
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

			_this.drag.resizeIdx = parseInt(_this.drag.ele.attr('data-resize-idx'),10);
			_this.drag.isLeftContent  = _this._isFixedPostion(_this.drag.resizeIdx);
			_this.drag.colHeader= $('#'+_this.prefix+'colHeader'+_this.drag.resizeIdx);

			// get absolute left position
			var posLeft = _this.config.gridWidth.aside;
			for(var i = 0;i < _this.drag.resizeIdx; i++ ){
				posLeft+=$('#'+_this.prefix+'colHeader'+i).width();
			}
			_this.drag.positionLeft = posLeft;

			_this.drag.totColW = _this.drag.colHeader.width();

			_this.drag.colW = _this.config.tColItem[_this.drag.resizeIdx].width;
			if(_this.drag.isLeftContent){
				_this.drag.gridW = _this.config.gridWidth.left - _this.drag.colW;
			}else{
				_this.drag.positionLeft -= _this.config.scroll.containerLeft;
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
							resizeW = Math.max(getCharLength(tmpVal||'',_this.options.headerOptions.oneCharWidth),resizeW);
						}
						beforeLen= currLen;
					}

					selColItem.maxWidth=resizeW;
				}

				if(_this.options.colOptions.maxWidth != -1){
					resizeW = Math.min(resizeW,_this.options.colOptions.maxWidth );
				}

				_this._setHeaderResize(e,_this, 'end' , resizeW);
			})

			resizeEle.css('cursor',_this.options.headerOptions.resize.cursor);
			resizeEle.on('touchstart.pubresizer mousedown.pubresizer',function (e){
				var oe = e.originalEvent.touches;
				var moveStart = false;
				colResize(_this, $(this));

				_this.element.resizeHelper.show().css('left', (_this.drag.positionLeft+_this.drag.totColW)+'px');

				var startX = oe ? oe[0].pageX : e.pageX;
				_this.drag.pageX = startX;
				_this.drag.ele.addClass('pubGrid-move-header')

				// resize시 select안되게 처리 . cursor처리
				_$doc.attr("onselectstart", "return false");
				_this.element.hidden.append("<style type='text/css'>*{cursor:" + _this.options.headerOptions.resize.cursor + "!important}</style>");

				_$doc.on('touchmove.colheaderresize mousemove.colheaderresize', function (e1){
					if(!moveStart){
						var oe1 = e1.originalEvent.touches;
						var moveX = oe1 ? oe1[0].pageX : e1.pageX;
						if( moveX > startX+10 || moveX < startX-10){
							_this.element.container.addClass('pubGrid-resize-mode');
							moveStart =true;
						}
					}
					_this.onGripDrag(e1,_this);
				}).on('touchend.colheaderresize mouseup.colheaderresize mouseleave.colheaderresize', function (e1){
					_this.drag.ele.removeClass('pubGrid-move-header');
					_this.onGripDragEnd(e1,_this);
					_this.element.container.removeClass('pubGrid-resize-mode');
					_this.element.resizeHelper.hide();
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
			this.config.isResize = true;

			w = (w > _this.options.colOptions.minWidth? w : _this.options.colOptions.minWidth);

			_this.setColumnWidth(drag.resizeIdx, w, drag.colHeader);

			if(isFunction(_this.options.headerOptions.resize.update)){
				_this.options.headerOptions.resize.update.call(null , {index:drag.resizeIdx , width: w});
			}
			drag.ele.removeAttr('style');

		}else{
			var w = drag.totColW + (ox - drag.pageX);
			_this.element.resizeHelper.css('left', _this.drag.positionLeft+w);
			//drag.ele.css('left', w > _this.options.colOptions.minWidth? w : _this.options.colOptions.minWidth);
		}
	}
	/**
	 * @method setColumnWidth
	 * @param  idx : heder index
	 * @param  w : width
	 * @param  colHeaderEle , header element
	 * @param  calcFlag , calcDimension call flag
	 * @description set header width
	 */
	,setColumnWidth : function (idx,w, colHeaderEle , calcFlag){

		if(w <= this.options.colOptions.minWidth){
			w =this.options.colOptions.minWidth;
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

		if(calcFlag !== false){
			this.calcDimension('headerResize');
		}
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
		var strHTML = [];
		strHTML.push('<ul>');
		if (new Boolean(preP_is) == true) {
			strHTML.push(' <li><a href="javascript:" class="page-num page-icon" pageno="'+preO+'">&laquo;</a></li>');
		} else {
			if (currP <= 1) {
				strHTML.push(' <li class="disabled page-icon"><a href="javascript:">&laquo;</a></li>');
			} else {
				strHTML.push(' <li><a href="javascript:" class="page-num page-icon" pageno="'+preO+'">&laquo;</a></li>');
			}
		}
		var no = 0;
		for (no = currS * 1; no <= currE * 1; no++) {
			if (no == currP) {
				strHTML.push(' <li class="active"><a href="javascript:">'+ no + '</a></li>');
			} else {
				strHTML.push(' <li class="page-num" pageno="'+no+'"><a href="javascript:" >'+ no + '</a></li>');
			}
		}

		if (new Boolean(nextP_is) == true) {
			strHTML.push(' <li><a href="javascript:" class="page-num page-icon" pageno="'+nextO+'">&raquo;</a></li>');
		} else {
			if (currP == currE) {
				strHTML.push(' <li class="disabled"><a href="javascript:">&raquo;</a></li>');
			} else {
				strHTML.push(' <li><a href="javascript:" class="page-num page-icon" pageno="'+nextO+'">&raquo;</a></li>');
			}
		}
		strHTML.push('</ul>');

		var pageNaviEle = _this.element.navi.find('.pubGrid-page-navigation');
		pageNaviEle.addClass('page-'+(options.position || 'center'));
		pageNaviEle.empty().html(strHTML.join(''));

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
	 * @method getRowCheckValue
	 * @param  rowItem {object} row item
	 * @param  checkFlag {Boolean} check 여부
	 * @description get rowitem check value
	 */
	,getRowCheckValue : function (rowItem,checkFlag){
		rowItem['_pubcheckbox']	= (checkFlag===false?false: true);
		return rowItem;
	}
	/**
	 * @method getTheme
	 * @description get theme
	 */
	,getTheme : function (){
		return this.options.theme;
	}
	/**
	 * @method getRowItemToElement
	 * @param  rowEle {Object} row elemnt
	 * @description get row item
	 */
	,getRowItemToElement : function (rowEle){
		var row_idx = rowEle.attr('rowinfo');
		return this.getItems(this.config.scroll.viewIdx+intValue(row_idx));
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

var _$util = {
	/**
	 * @method newItems
	 * @description get new item default object
	 */
	newItems : function (tColItems, newCount){
		var len = tColItems.length;

		newCount=newCount||1;

		var reArr=[];
		for(var i=0; i <newCount; i++){
			var addItem = {'_pubCUD':'_C'};
			for(var j= 0; j <len; j++){
				var tColItem = tColItems[j];
				addItem[tColItem.key]  = tColItem.defaultValue ||'';
			}

			reArr.push(addItem);
		}

		return reArr;
	}
	/**
	 * @method trim
	 * @description trim
	 */
	,trim : function(str) {
		return str.replace(/^\s+|\s+$/g,"");
	}
	/**
	 * @method setSelectCell
	 * @description cell 선택
	 */
	,setSelectCell : function(gridObj,startCellInfo, row , col, addEle){
		var tdEle = addEle.parentElement;
		if(startCellInfo.startIdx == row  && startCellInfo.startCol == col ){
			tdEle.classList.add('col-active');
			tdEle.classList.add('selection-start-col');
			return ;
		}

		if(gridObj._isAllSelect()){
			if(gridObj.isAllSelectUnSelectPosition(row , col)){
				tdEle.classList.remove('col-active' );
			}else{
				tdEle.classList.add('col-active' );
			}
		}else{
			tdEle.classList.remove('col-active' );

			if(gridObj.isSelectPosition(row , col)){
				tdEle.classList.add('col-active' );
			}
		}

		return false;
	}
	/**
	 * @method getEditForm
	 * @description get edit form
	 */
	,getEditForm : function (selEl, colItem, rowItem){

		var reForm =[];

		var editor = colItem.editor||{};;

		reForm.push( '<div class="pubGrid-edit-area pubGrid-edit-type-'+editor.type+'">');
		if(editor.type =='select'){
			reForm.push( '<select class="pubGrid-edit-field">');
			var items = editor.items||[];
			for(var i =0, len = items.length;i < len; i++){
				var item = items[i];
				reForm.push( '<option value="'+item.CODE+'">'+item.LABEL+'</option>');
			}
			reForm.push( '</select>');
		}else if(editor.type =='textarea'){
			reForm.push( '<textarea class="pubGrid-edit-field"></textarea>');
		}else if(editor.type =='number'){
			reForm.push( '<input type="number" class="pubGrid-edit-field">');
		}else{
			reForm.push( '<input type="text" class="pubGrid-edit-field">');
		}
		reForm.push( '</div>');

		selEl.append(reForm.join(''));

		var editEl = selEl.find('.pubGrid-edit-field');

		editEl.val(rowItem[colItem.key]);
		editEl.focus();
	}
	/**
	 * @method clearActiveColumn
	 * @description clear active column
	 */
	,clearActiveColumn : function (element){
		element.body.find('.pub-body-td.col-active').removeClass('col-active');
	}
	/**
	 * @method _isMultipleSelection
	 * @description is multiple selection mode
	 */
	,isMultipleSelection : function (selectionMode){
		return (selectionMode=='multiple-row' || selectionMode =='multiple-cell')
	}
	/**
	 * @method getSelectionModeColInfo
	 * @description selection mode col info
	 */
	,getSelectionModeColInfo : function (selectionMode ,colIdx ,dataInfo ,isMouseDown){
		var _startCol=0 , _endCol =0;

		if(selectionMode =='multiple-row'){
			_startCol = 0;
			_endCol = dataInfo.colLen-1;
		}else if(selectionMode =='multiple-cell'){
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

		return {startCol : _startCol, endCol : _endCol};
	}
	/**
	 * @method setCUD
	 * @description CUD모드 변경. (c = create , u = update , d =delete)
	 */
	,setCUD : function (selItem){
		selItem['_pubCUD'] = selItem['_pubCUD']=='_C'?'C':(selItem['_pubCUD']=='C'?'CU':'U');
		return selItem;
	}
	/**
	*
	*/
	,setCheckBoxCheck : function (checkEle, item){
		checkEle.innerHTML = '<input type="checkbox" class="pub-row-check" '+(item['_pubcheckbox']?'checked':'')+'/>';
	}
}


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