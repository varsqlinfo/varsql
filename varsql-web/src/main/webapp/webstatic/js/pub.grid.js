/**
 * pubGrid v1.0.3
 * ========================================================================
 * Copyright 2016-2022 ytkim
 * Licensed under MIT
 * http://www.opensource.org/licenses/mit-license.php
 * url : https://github.com/ytechinfo/pub
 * demo : http://pub.moaview.com/
*/

;(function($, window, document) {
"use strict";
/**
af :  add function
ap  : add parameter
*/
var _initialized = false
,_$win = $(window)
,_$doc = $(document)
,pubGridLayoutElement = false
,_datastore = {}
,_defaults = {
	blankSpaceWidth : 1		// 오른쪽 끝 공백 값
	,copyMode : 'single'		// copy mode	single, multiple, none
	,colFixedIndex : 0	// 고정 컬럼
	,widthFixed : false  // 넓이 고정 여부.
	,useDefaultFormatter: true // 기본 포멧터 사용여부
	,editable :false	// 편집 모드 활성화
	,selectionMode : 'multiple-cell'	//cell 선택 모드 row, cell, multiple-row, multiple-cell
	,showTooltip : false			// tooltip flag
	,theme : 'light'			// 테마 값
	,height: 'auto'				// 높이 값
	,width: 'auto'				// 넓이값
	,itemMaxCount : -1	// add시 item max로 유지할 카운트
	,valueFilter : false		// value filter function (colItem, objectValue)
	,rowOptions:{	// 로우 옵션.
		height: 22	// cell 높이
		,click : false //row(tr) click event
		,contextMenu : false // row(tr) contextmenu event
		,addStyle : false	// 추가할 style method
		,dblClick : false	// row dblclick event
		,dblClickCheck : false	// double click row checkbox checked true 여부.
		,pasteBefore :false		// 붙여 넣기 전 호출 메소드
		,pasteAfter :false		// 붙여 넣기 후 호출 메소드
	}
	,formatter :{
		money :{prefix :'$', suffix :'원' , fixed : 0}	// money 설정 prefix : 앞에 붙일 문구 , suffix : 마지막에 붙일 문구 , fixed : 소수점
		,number : {prefix :'', suffix :'' , fixed : 0}	// number 값 설정
	}
	,autoResize : {	//리사이즈 설정
		enabled:true	// 리사이즈시 그리드 리사이즈 여부.
		,responsive : true	// 반응형 여부
		,threshold :150		// resize 반응 시간 
	}
	,headerOptions : {
		view : true	// header 보기 여부
		,height: 25	// header 높이
		,sort : true	// 초기에 정렬할 값
		,redraw : true	// 초기에 옵션 들어오면 새로 그릴지 여부.
		,resize:{	// resize 설정
			enabled : true			// 활성화여부
			,cursor : 'col-resize'	// 커서 모양
			,update : false			// 변경시 콜랙 함수
			,minWidth : 50  // 컬럼 최소 넓이
			,maxWidth : 1500  // 컬럼 최대 넓이
		}
		,isColSelectAll : true	// 전체 선택 여부.
		,scrollEnabled : true	// 마우스 휠로 가로 스크롤 이동할지 여부.
		,oneCharWidth: 7		// char 의 넓이값
		,viewAllLabel : true	
		,contextMenu : false // header contextmenu event
		,helpBtn:{			//	header help btn 설정
			enabled : false	// header help btn 활성 여부.
			,title : ''		// tooltip
			,click :  function (clickInfo){}	// click event
			,dblclick : function (clickInfo){} // double click event
		}
		,drag :{
			enabled : false			// 활성화여부
			,dropSelector : ''		// drop selector
			,dropCallback : function (colItem){ // drop 전에 이벤트
				return true; 
			}
			
		}
	}
	,setting : {					// 그리드 설정
		mode :'simple'				// simple (search , fixed) , full(column config , filter) , full-center(screen center)
		,enabled : false			// 활성여부
		,enableColumnFix : false	// 고정 컬럼 활성여부
		,click : false				// 직접 처리 할경우. function 으로 처리.
		,btnClose : false			// button 으로만 닫기 여부
		,useRememberValue : false	// 검색어 local storage에 저장 여부
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
			searchFilter : function (item, key, schRegExp){ // 검색 필터
				var itemVal = (item[key]||'')+'';

				if(schRegExp.test(itemVal)){
					return true;
				}
				return false;
			}
		}
	}
	,asideOptions :{	// aside 옵션
		lineNumber : {	// 번호
			enabled :false	// 활성화 여부
			,key : 'lineNumber'	// key
			,charWidth : 9		// char width
			,name : ''			//  컬럼명
			,width : 40			// 넓이
			,styleCss : ''	//css
			,isSelectRow:true	// 선택 여부
		}
		,rowSelector :{	// 체크 박스
			enabled :false		// 활성화 여부
			,key : 'checkbox'	// key
			,name : 'V'			// name
			,width : 25			// 넓이값
			,click : function (rowInfo){ // click event , return false 일경우 체크 안함.

			}
		}
		,modifyInfo :{	// 수정 여부
			enabled :false	// 활성화 여부
			,key : 'modify'	// key
			,name : 'modify'	// name
			,width : 10		// 넓이값
		}
	}
	,bodyOptions : {	// body option
		cellDblClick : false	// body td click
		,keyNavHandler : false // arrows key handler function
	}
	,scroll :{	// 스크롤 옵션
		isStopPropagation : false	// 이벤트 전파 여부.
		,vertical : {
			width : 14			// 세로 스크롤
			,bgDelay : 100		// 스크롤 빈공간 mousedown delay
			,btnDelay : 100		// 방향키 mousedown delay
			,dragDelay : 5		// 스크롤 bar drag delay
			,speed : 2			// 스크롤 스피드 row 1
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
			,enableWheel : true 	//  wheel 로 스크롤 이동. 

		}
	}
	/*
	, tColItem  : {
		"key": ""	// key
		,"label": ""	// label
		,"width": 100		// width
		,"sort": true		// sort flag
		,"align": ""	// align
		,"type": ""		// value type
		,"renderer ": { // render mode (html or text default text)
			type : "text" // button, image, checkbox, radio, select, link, html 
			,item : {
				key : ""
			}
			,click : function (){}
			,template : function (){}
			,validator : function (){}
		}		
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
		
	}
	*/
	,tColItem : [] //head item
	,theadGroup : [] // head group
	,tbodyItem : []  // body item
	,tbodyGroup : [] // body group
	,tfootItem : []  // foot item
	,navigation :{
		usePaging : false	// 페이지 사용여부
		,status : false
		,statusFormatter : '{{currStart}} - {{currEnd}} of {{total}}'
		,height : 32		// 높이 값
		,position : 'center'	// 위치 값
		,callback : function (no){}	// 페이지 콜백
	}
	,page : false	// paging info
	,message : {
		empty : 'no data'
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
	,operators : {} // setting condition operator
};

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

function arrayCopy(orginArray){
	return $.isArray(orginArray) ? orginArray.slice():null;
}

function eventKeyCode (e){
	return window.event ? e.keyCode : e.which; 
}

function createArray(m,n,initial){
	var reval = [];
	for(var j=0; j < m; j++){
		reval[j] =[];
		for(var k =0 ; k < n; k++){
			reval[j][k] = initial;
		}
	}

	return reval; 
}

function replaceMesasgeFormat(logicCode, param){
	return logicCode.replace(/{{(.+?)}}/gi, function (word){
		var key = word.replace(/[\{\}]/g,'');
		return  param[key];
	});
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

function evtPos(e){
	var oe = e.originalEvent
	var evt; 
	if(oe){
		if(oe.changedTouches){
			evt =oe.changedTouches[0];
		}else{
			if( oe.touches){
				evt = oet[0] 
			}
		}
	}

	evt = evt || e; 
	
	return {x : evt.pageX, y : evt.pageY};
}

function stopPreventCancel(e){
	e.preventDefault();
	e.stopPropagation();
}

function isInputField(tagName){
	return tagName.search(/(input|select|textarea)/i) > -1;
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

function $pubSelector(selector){
	return document.querySelector(selector);
}

function objMergeRecursive(dst, src) {

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

function objectMerge() {
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

function getMeasureTextWidth(gridCtx, str, type){
	
	var w = gridCtx.element.measureEl.text(str).width()+(type=='header' ? 22 : 14);
	gridCtx.element.measureEl.text('');
	return w;
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

function Plugin(element, options) {
	if(pubGridLayoutElement ===false){
		$('body').append('<div class="pubGrid-body-hidden-area"></div>');
		pubGridLayoutElement = $('.pubGrid-body-hidden-area');
	}
	
	this._initialize(element, options);

	return this;
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
			, dataInfo : {colLen : 0, allColLen : 0, rowLen : 0, lastRowIdx : 0, orginTColItem:[], orginTColIdxKeyMap : {}}
			, rowOpt :{}
			, sort : {current :''}
			, pagingInfo : false
			, selection :{
				startCell :{}
			}
			, searchOn : false
			, isResize : false
			, focus : false
			, isBodyDragging: false
			, mouseEnter :false
			, currentClickInfo :{}
			, allCheck :false
			, currentHeaderResizeFlag : true
			, initHeaderResizer :false
			, settingConfig :{
				viewInitFlag :true
				,changeInfos: {}	// change info
				,filterTemplate: {}	// filter html template
				,currentClickItem: {}	// current click item
				,filterInfo : false // filter info {checkFn, check condition}
			}
		};

		_this.eleCache = {};
		_this._initScrollData();
		_$util.setSelectionRangeInfo(_this, {}, true);

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
		this.config.scroll = {containerLeft :0, before:{},top :0 , left:0, startCol:0, endCol : 0, viewIdx : 0, vBarPosition :0 , hBarPosition :0 , maxViewCount:0, viewCount : 0, vTrackHeight:0, hTrackWidth:0, verticalScrollTimer: -1, horizontalScrollTimer: -1, mouseDown: false};
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

		if(options.setting.mode != 'full'){
			options.setting.mode = 'simple';	
			options.setting.btnClose = false;	
		}
		
		options.setting.configVal = objectMerge({},_defaults.setting.configVal ,options.setting.configVal);

		_this.options =objectMerge({}, _defaults, options)
		
		_this.options.tbodyItem = $.isArray(options.tbodyItem) ? options.tbodyItem : (_this.options.tbodyItem ||[]);

		_this.config.isValueFilter = isFunction(_this.options.valueFilter);

		_this.config.dataInfo.orginTColItem = arrayCopy(_this.options.tColItem);

		_this.config.dataInfo.orginTColItem.forEach(function (item, idx){
			_this.config.dataInfo.orginTColIdxKeyMap[item.key] = item; 
		})
		
		_this.config.rowHeight = _this.options.rowOptions.height;

		_this.config.isKeyNavHandler = isFunction(_this.options.bodyOptions.keyNavHandler);

		if(_this.options.rowOptions.contextMenu !== false && typeof _this.options.rowOptions.contextMenu == 'object'){
			var _cb = _this.options.rowOptions.contextMenu.callback;

			if(_cb){
				_this.options.rowOptions.contextMenu.callback = function(key,sObj) {
					var rowInfo = _this.getRowItemToElement(this.element);
					this.gridItem = rowInfo.item;
					this.rowIdx = rowInfo.rowIdx;
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
			this.config.header.height = _this.options.headerOptions.height * (this.options.theadGroup.length > 0 ?this.options.theadGroup.length+1 : 1)
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
	 * @param options {Object} - 데이터 .
	 * @description  add style tab
	 */
	,addStyleTag : function (){
		var _this = this
			,_d = document;

		var cssStr = [];

		var rowOptHeight = _this.options.rowOptions.height;

		if(!isNaN(rowOptHeight)){
			cssStr.push('#'+_this.prefix+'_pubGrid .pub-body-td, #'+_this.prefix+'_pubGrid .pub-body-aside-td{max-height:'+rowOptHeight+'px;height:'+rowOptHeight+'px;line-height:'+(rowOptHeight-4)+'px;}');
			cssStr.push('#'+_this.prefix+'_pubGrid .pub-body-td>.pub-content, #'+_this.prefix+'_pubGrid .pub-body-aside-td > .aside-content{margin:1px 0px 1px 0px;max-height:'+(rowOptHeight-3)+'px; }');
			//cssStr.push('#'+_this.prefix+'_pubGrid .pub-body-td>.pub-content, #'+_this.prefix+'_pubGrid .pub-body-aside-td > .aside-content{margin:1px 0px 1px 0px;height:'+(rowOptHeight-3)+'px; line-height:'+(rowOptHeight-5)+'px;}');
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
			,tciItem, headItem
			,headGroupInfo = [] ,groupInfo = []
			,leftHeaderGroupInfo = [] ,leftGroupInfo = [];

		var gridTci = [];
		for(var  i =0 ;i < tci.length;i++){
			var tmpTci = tci[i];
			if(tmpTci.visible !== false){
				gridTci.push(tmpTci);
			}
		}

		cfg.tColItem = gridTci;

		tci = gridTci;

		var thg = isUndefined(opt.theadGroup)? [] : arrayCopy(opt.theadGroup);
		thg.push(tci);
		
		var theadGrpRow; 

		var tciLen = tci.length
			,thgLen = thg.length; 

		var colSpanInfo = createArray(thgLen, tciLen)
			,rowSpanInfo = createArray(thgLen, tciLen);
		
		for(var i=0; i <thgLen; i++){
			theadGrpRow = thg[i];

			groupInfo = [];
			leftGroupInfo = [];

			var colSpanArr = colSpanInfo[i];
			var rowSpanArr = rowSpanInfo[i];
							
			var tmpColIdx=0;
			for(var j=0; j < theadGrpRow.length; j++){
				var headItem = objectMerge({},theadGrpRow[j]);

				var colspanNum = headItem['colspan']||1;
				var rowspanNum = headItem['rowspan']||0;
				
				var k = tmpColIdx;
				tmpColIdx += colspanNum;
						
				var continueFlag = false; 
				for(; k < tmpColIdx; k++){
					if(i != 0){
						var beforeSpanRowNum = rowSpanInfo[i-1][k];
						beforeSpanRowNum = beforeSpanRowNum > 1 ? beforeSpanRowNum-1 : 0;	

						if(beforeSpanRowNum > 0){
							rowSpanInfo[i][k] = beforeSpanRowNum;
							colSpanInfo[i][k] = 0;
							if(i+1 != thgLen){
								j--;
							}
							
							continueFlag = true; 
							break;
						}
					}

					if(rowspanNum > 0){ // rowspan 일때 아래 i+1의 row 값 추가
						colSpanArr[k] = 0;
						rowSpanArr[k] = rowspanNum;	
					}else{
						rowSpanArr[k] = 0;
						if(colspanNum == 1){
							colSpanArr[k] = 1;
						}else{
							colSpanArr[k] = 2;
						}
					}
				}

				if(continueFlag){
					continue ; 
				}

				tciItem = tci[tmpColIdx-1] ||{};
				
				headItem['colspan'] = colspanNum;
				headItem['rowspan'] = rowspanNum;
				headItem['view'] = true;
				headItem['sort'] = tciItem.sort===false ? false : opt.headerOptions.sort !== false;
				headItem['colspanhtm'] = headItem.colspan > 1 ? ' scope="colgroup" colspan="'+headItem.colspan+'" ' :'scope="col"';
				headItem['rowspanhtm'] = '';
				headItem['resizeIdx'] =tmpColIdx-1;

				if(headItem.rowspan > 1){
					headItem['rowspanhtm'] = ' scope="col" rowspan="'+headItem.rowspan+'" style="height:'+(_this.options.headerOptions.height*headItem.rowspan)+'px" '; 
				}
							
				if(i+rowspanNum >= thgLen || i+1 == thgLen){
					headItem.isSort = (headItem.sort===true?true:false); 
				}

				if(tmpColIdx <= fixedColIdx){
					leftGroupInfo.push(headItem);
				}else if(tmpColIdx - colspanNum < fixedColIdx ){
					headItem['colspan'] = (tmpColIdx - fixedColIdx);
					headItem['colspanhtm'] = ' scope="colgroup" colspan="'+headItem['colspan']+'" ';
					groupInfo.push(headItem);

					var leftHeadItem = objectMerge({},headItem);
					leftHeadItem['colspan'] = (fixedColIdx - (tmpColIdx - colspanNum))
					leftHeadItem['resizeIdx'] = fixedColIdx;
					leftHeadItem['colspanhtm'] = ' scope="colgroup" colspan="'+leftHeadItem['colspan']+'" ';
					
					leftGroupInfo.push(leftHeadItem);
				}else{
					groupInfo.push(headItem)
				}
			}

			headGroupInfo.push(groupInfo);
			leftHeaderGroupInfo.push(leftGroupInfo)
		}

		cfg.headerInfo = headGroupInfo;
		cfg.headerLeftInfo = leftHeaderGroupInfo;

		var colWidth = Math.floor((gridElementWidth)/tci.length);

		colWidth = colWidth-10;

		var viewAllLabel = calcFlag===false ? false : (opt.headerOptions.viewAllLabel ===true ?true :false);

		var leftWidth=0, mainWidth=0 , viewColCount= 0;
		for(var j=0; j<tci.length; j++){
			var tciItem = tci[j];
			tciItem.maxWidth = -1;	// max width

			if(tciItem.visible===false) continue;

			tciItem.renderer = tciItem.renderer || {type : 'text'};

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
				tciItem.width = isNaN(tciItem.width) ? opt.headerOptions.resize.minWidth :tciItem.width;
			}

			tciItem.width = Math.max(tciItem.width, opt.headerOptions.resize.minWidth);

			tciItem['_alignClass'] = tciItem.align=='right' ? 'ar' : (tciItem.align=='center'?'ac':'al');
			cfg.tColItem[j] = tciItem;

			if(_this._isFixedPostion(j)){
				leftWidth +=tciItem.width;
			}else{
				mainWidth +=tciItem.width;
			}
		}
		cfg.gridWidth.left = leftWidth;
		cfg.gridWidth.main = mainWidth;

		cfg.dataInfo.colLen = viewColCount;
		cfg.dataInfo.allColLen= tci.length;

		if(calcFlag === false){
			return ;
		}

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
				item.width = Math.max(item.width, opt.headerOptions.resize.minWidth);

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
	 * @description foot 데이터 셋팅
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
	 * @method addRow
	 * @param data {Object , Array} - 데이터
	 * @param opt {Object} - add option { index : index ,'first','last' ,focus : 스크롤 이동 여부 (true or false) }
	 * @description 데이터 add
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
	 * @description 데이터 그리기
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
	 * @description 데이터 그리기
	 */
	,updateRow : function (idx ,rowItem, clickFlag){

		var updItem = this.options.tbodyItem[idx];

		if(updItem){
			updItem= objectMerge(updItem , rowItem)
			this.options.tbodyItem[idx] =updItem;
			this.setData(this.options.tbodyItem,'reDraw_update');

			if(clickFlag===true){
				var rowClickFn = this.options.rowOptions.click;
				if(isFunction(rowClickFn)){
					rowClickFn.call(null ,  {r : idx, item : updItem});
				}
			}
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
			_$util.setSelectionRangeInfo(this, {}, true);
		}

		if(idxs.length < 1){
			return ;
		}

		for(var i =0 ;i < idxs.length;i++){
			var rowIdx = idxs[i];
			var rangeKey = 'row'+rowIdx;

			_$util.setSelectionRangeInfo(this, {
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

			_$util.setSelectionRangeInfo(this, {
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
	,_setSearchData: function (mode, drawFlag){
		var settingOpt = this.options.setting;

		if(mode != 'search'){
			this.config.orginData = this.options.tbodyItem;
		}

		if(settingOpt.enabled ===true){
			var schArr = [];
			var orginData = this.config.orginData;
			var filterInfo = this.config.settingConfig.filterInfo; 

			var schField = settingOpt.configVal.search.field ||''
				,schVal = settingOpt.configVal.search.val ||'';
						
			schVal = _$util.trim(schVal); 

			if((schField != '' && schVal !='') || filterInfo !== false){
				var schArr =[];
			
				var chkFn =settingOpt.util.searchFilter;

				var schRegExp = true; 
				if(schVal != ''){
					if(schField == '$all'){
						chkFn = new Function('item','searchField','chkRegExp', _$util.genAllColumnSearch(this));		
					}
					schRegExp = _$util.getSearchRegExp(schVal);
				}			

				for(var i = 0, len = orginData.length; i < len; i++){
					var tmpItem =orginData[i]; 

					if(schRegExp ===true || chkFn(tmpItem, schField, schRegExp)){
						if(filterInfo === false){
							schArr.push(tmpItem);
						}else{
							if(filterInfo.fn(tmpItem, filterInfo.chkOpts)){
								schArr.push(tmpItem);
							}
						}
					}
				}

				this.config.searchOn = true; 
			}else{
				this.config.searchOn = false; 
				schArr = this.config.orginData;
			}

			if(mode =='search' && drawFlag !==false){ 
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
	 * @param data {Array} - 데이터
	 * @param gridMode {String} - 그리드 모드
	 * @param addOpt {Object} - add opt
	 * @description 데이터 그리기
	 */
	,setData :function (pdata, mode, addOpt){
		var _this = this
			,opt = _this.options;
		var data = pdata;

		mode = mode||'reDraw';

		var modeInfo = mode.split('_');
		var gridMode = modeInfo[0]
			,subMode = modeInfo[1] ||'';

		gridMode = gridMode||'reDraw';

		if(!$.isArray(pdata)){
			data = pdata.items;
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
			// sort 값이 있으면 초기 데이터 정렬
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

			if(subMode !='paste' && subMode != 'update'){
				_$util.setSelectionRangeInfo(_this, {}, true);
			}
			if(subMode != 'update'){
				_this.calcDimension(gridMode);
			}
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
		}else if(subMode =='update'){
			this.drawGrid('vscroll');
		}else{
			_this.drawGrid(mode,true);
		}

		if(_this.options.navigation.usePaging === true) {
			_this.setPaging(mode=='init' ? opt.paging : (pdata.paging ||{}));
		}

		_this._setStatusMessage();

		if(_this.config.searchOn===true){
			_this.gridElement.find('.pubGrid-setting-btn').addClass('search-on');
		}else{
			_this.gridElement.find('.pubGrid-setting-btn').removeClass('search-on');
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
	,setPaging : function (_paging){
		var _this =this;

		_paging = _paging||{};

		if(_this.options.navigation.usePaging !== true) {
			throw 'usePaging not enabled';
		}

		var pagingInfo = _this.getPagingInfo(_paging.totalCount||0, _paging.currPage, _paging.countPerPage, _paging.unitPage);

		if(pagingInfo.totalCount < 1) {
			$('#'+_this.prefix+'_page').empty();
			return ; 
		}

		_this.config.pageNo = pagingInfo.currPage;
		_this.config.pagingInfo = pagingInfo;

		var currP = pagingInfo.currPage;
		if (currP == "0") currP = 1;
		var preP_is = pagingInfo.prePage_is;
		var currS = pagingInfo.currStartPage;
		var currE = pagingInfo.currEndPage;
		if (currE == "0") currE = 1;
		var nextO = 1 * currP + 1;
		var preO = currP - 1;
		var strHTML = [];

		strHTML.push('<ul>');

		if (currP <= 1) {
			strHTML.push(' <li class="disabled page-icon"><a href="javascript:">&laquo;</a></li>');
		} else {
			strHTML.push(' <li><a href="javascript:" class="page-num page-icon" pageno="'+preO+'">&laquo;</a></li>');
		}
		
		if(preP_is && (currE - pagingInfo.unitPage >= 0)){
			strHTML.push(' <li class="page-num" pageno="1"><a href="javascript:" >1...</a></li>');
		}
		
		var no = 0;
		for (no = currS * 1; no <= currE * 1; no++) {
			if (no == currP) {
				strHTML.push(' <li class="active"><a href="javascript:">'+ no + '</a></li>');
			} else {
				strHTML.push(' <li class="page-num" pageno="'+no+'"><a href="javascript:" >'+ no + '</a></li>');
			}
		}
	
		if(currS + pagingInfo.unitPage < pagingInfo.totalPage){
			strHTML.push(' <li class="page-num" pageno="'+pagingInfo.totalPage+'"><a href="javascript:" >...'+pagingInfo.totalPage+'</a></li>');
		}
			
		if (currP == currE) {
			strHTML.push(' <li class="disabled"><a href="javascript:">&raquo;</a></li>');
		} else {
			strHTML.push(' <li><a href="javascript:" class="page-num page-icon" pageno="'+nextO+'">&raquo;</a></li>');
		}
				
		strHTML.push('</ul>');

		var pageNaviEle = $('#'+_this.prefix+'_page');
		pageNaviEle.addClass('page-'+_this.options.navigation.position);
		pageNaviEle.empty().html(strHTML.join(''));

		return this;
	}
	,initStyle : function (){

		var _this = this
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

		var prefix =_this.prefix; 

		var templateHtml =  '<div class="pubGrid-wrapper"><div id="'+prefix+'_pubGrid" class="pubGrid pubGrid-noselect" tabindex="-1"  style="outline:none !important;overflow:hidden;width:'+_this.config.container.width+'px;">'
			+' 	<div id="'+prefix+'_container" class="pubGrid-container '+(_this.options.colFixedIndex >0 ? 'pubGrid-col-fixed':'')+'" style="overflow:hidden;">'
			+'    <div class="pubGrid-setting-btn"><svg version="1.1" width="'+vArrowWidth+'px" height="'+vArrowWidth+'px" viewBox="0 0 54 54" style="enable-background:new 0 0 54 54;">	'
			+'<g><path id="'+prefix+'_settingBtn" d="M51.22,21h-5.052c-0.812,0-1.481-0.447-1.792-1.197s-0.153-1.54,0.42-2.114l3.572-3.571	'
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
			+'</svg>'
			+'		</div>'
			+' 		<div class="pubGrid-header-container-warpper">'
			+' 		  <div id="'+prefix+'_headerContainer" class="pubGrid-header-container">';
			
			if(_this.options.headerOptions.view !== false){
				templateHtml+=' 			<div class="pubGrid-header-aside"><table class="pubGrid-header-aside-cont" style="width:'+_this.config.gridWidth.aside+'px;">'+_this.theadAsideHtml('aside') +'</table></div>'
				+' 			<div class="pubGrid-header-left"><table class="pubGrid-header-left-cont" style="width:'+_this.config.gridWidth.left+'px;">'+_this.theadHtml('left')+'</table></div>'
				+' 			<div class="pubGrid-header-left-cont-border"><div class="pubGrid-border"></div></div>'
				+' 			<div class="pubGrid-header">'
				+'				<div class="pubGrid-header-cont-wrapper" style="position:relative;"><table class="pubGrid-header-cont" onselectstart="return false">'+_this.theadHtml('cont')+'</table></div>'
				+' 			</div>'
			};

			templateHtml+=' 		  </div>'
			+' 		</div>'

			+' 		<div id="'+prefix+'_bodyContainer" class="pubGrid-body-container-warpper">'
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
			+' 		<div id="'+prefix+'_footerContainer" class="pubGrid-footer-container">'
			+' 			<div class="pubGrid-footer-left"></div>'
			+' 			<div class="pubGrid-footer">'
			+' 				<div class="pubGrid-footer-cont-wrapper" style="position:relative;"><table class="pubGrid-footer-cont"></table></div>'
			+' 			</div>'
			+' 		</div>'
			+' 		<div id="'+prefix+'_vscroll" class="pubGrid-vscroll">'
			+'			<div class="pubGrid-scroll-top-area" style="height:23px;"></div>'
			+' 			<div class="pubGrid-vscroll-bar-area">'
			+'			  <div class="pubGrid-vscroll-bar-bg"></div>'
			+' 			  <div class="pubGrid-vscroll-up pubGrid-vscroll-btn" data-pubgrid-btn="U"><svg width="'+vArrowWidth+'px" height="8px" viewBox="0 0 110 110" style="enable-background:new 0 0 100 100;"><g><polygon points="50,0 0,100 100,100" fill="#737171"/></g></svg></div>'
			+'			  <div class="pubGrid-vscroll-bar"><div class="pubGrid-vscroll-bar-tip" style="right:'+(this.options.scroll.vertical.width)+'px"></div></div>'
			+' 			  <div class="pubGrid-vscroll-down pubGrid-vscroll-btn" data-pubgrid-btn="D"><svg width="'+vArrowWidth+'px" height="8px" viewBox="0 0 110 110" style="enable-background:new 0 0 100 100;"><g><polygon points="0,0 100,0 50,90" fill="#737171"/></g></svg></div>'
			+' 			</div>'
			+' 		</div>'
			+' 		<div id="'+prefix+'_hscroll" class="pubGrid-hscroll">'
			+'			<div class="pubGrid-scroll-aside-area"></div>'
			+' 			<div class="pubGrid-hscroll-bar-area">'
			+'			  <div class="pubGrid-hscroll-bar-bg"></div>'
			+' 			  <div class="pubGrid-hscroll-left pubGrid-hscroll-btn" data-pubgrid-btn="L"><svg width="8px" height="'+hArrowWidth+'px" viewBox="0 0 110 110" style="enable-background:new 0 0 100 100;"><g><polygon points="10,50 100,0 100,100" fill="#737171"/></g></svg></div>'
			+'			  <div class="pubGrid-hscroll-bar"></div>'
			+' 			  <div class="pubGrid-hscroll-right pubGrid-hscroll-btn" data-pubgrid-btn="R"><svg width="8px" height="'+hArrowWidth+'px" viewBox="0 0 110 110" style="enable-background:new 0 0 100 100;"><g><polygon points="0,0 0,100 90,50" fill="#737171"/></g></svg></div>'
			+'			</div>'
			+'			<div class="pubGrid-hscroll-edge" style="right:'+(_this.options.scroll.vertical.width-1)+'px;"></div>'
			+' 		</div>'
			+' 		<div id="'+prefix+'_resizeHelper" class="pubGrid-resize-helper"></div>'
			+' 	</div>'
			+' </div>'
			+' <div style="top:-9999px;left:-9999px;position:fixed;z-index:999999;"><textarea id="'+prefix+'_pubGridPasteArea"></textarea>' // copy 하기위한 textarea 꼭 위치해야함.
			+' <textarea id="'+prefix+'_pubGridCopyArea"></textarea>' // copy 하기위한 textarea 꼭 위치해야함.
			+' <pre id="'+prefix+'Measurer" style="display:inline-block;white-space: pre;position: relative;padding:0px;margin:0px;"></pre></div>'
			+' <div id="'+prefix+'_navigation" class="pubGrid-navigation" style="height:'+(_this.options.navigation.height) +'px">'
			+'	 <div class="pubGrid-paging"><div class="pubGrid-paging-box"><div id="'+prefix+'_page"></div></div></div><div class="pubgGrid-message-info"><div id="'+prefix+'_status"></div></div>'
			+' </div>'
			+' </div>';

			return templateHtml; 
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

			var bodyHtmTemplate = '';
			bodyHtmTemplate += '<colgroup>'+colGroupHtm.join('')+'</colgroup>';
			bodyHtmTemplate += '<tbody class="pubGrid-body-tbody">'+strHtm.join('')+'</tbody>';
			tmpeElementBody.empty().html(bodyHtmTemplate);
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

		if(mode=='init' || this.config.dataInfo.rowLen > 0){
			_$template.bodyHtm (this, mode, 'left');
			return _$template.bodyHtm (this, mode, 'cont');
		}

		return false;
	}
	/**
	 * @method getRenderValue
	 * @param  thiItem {Object} header col info
	 * @param  item {Object} row 값
	 * @description foot 데이터 셋팅
	 */
	,getRenderValue : function (thiItem, rowItem, mode, addEle){

		var rendererType = thiItem.renderer.type; 

		if(mode == 'data' && rendererType != 'text'){
			return rowItem[thiItem.key];
		}
		
		var itemVal = (_$renderer[ rendererType ] || _$renderer.text)(this, thiItem, rowItem, mode);

		if(addEle){
			itemVal = itemVal||'';
			if(rendererType !='text'){
				addEle.innerHTML = itemVal;
				itemVal = '';
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
	,_setCellStyle : function (cellEle, _idx, thiItem, rowItem){
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
			,headerOpt=_this.options.headerOptions;

		var strHtm = [];

		var headerInfo = cfg.headerInfo;

		if(type=='left'){
			headerInfo =  cfg.headerLeftInfo;
		}

		var headerDragEnabled = headerOpt.drag.enabled;

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
							strHtm.push('  <div class="pub-header-help-wrapper" title="'+_this.options.headerOptions.helpBtn.title+'"><svg class="pub-header-help" viewBox="0 0 100 100"><g><polygon class="pub-header-help-btn" points="0 0,0 100,100 0"></polygon></g></svg> </div>');
						}
						strHtm.push('  <div class="label-wrapper">');
						strHtm.push('   <div class="pub-header-cont outer '+(ghItem.isSort===true?'sort-header':'')+'" col_idx="'+ghItem.resizeIdx+'"><div class="pub-inner"><div class="centered" '+(headerDragEnabled?' draggable="true" ':'')+'>'+ghItem.label+'</div></div>');
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
	,setColFixedIndex : function (idx, headRedrawFlag){
		if(this.options.colFixedIndex > 0  && idx < 1){
			this.element.container.removeClass('pubGrid-col-fixed');
		}else if(this.options.colFixedIndex < 1  && idx > 0){
			this.element.container.addClass('pubGrid-col-fixed')
		}
		this.options.colFixedIndex = idx;
		this._setThead(headRedrawFlag===true?true :false);
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
		var _this = this;

		var strHtm = [], colGroupHtm=[];

		var strHtm = [];

		var headerInfo = this.config.headerInfo;


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
	 * @description foot 데이터 셋팅
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
				_this.element.hScrollEdge = $('#'+_this.prefix+'_hscroll .pubGrid-hscroll-edge');
				_this.element.resizeHelper = $('#'+_this.prefix+'_resizeHelper');	// resize helper

				// query selector
				_this.element.leftContent = $pubSelector('#'+_this.prefix+'_bodyContainer .pubGrid-body-left-cont');
				_this.element.bodyContent = $pubSelector('#'+_this.prefix+'_bodyContainer .pubGrid-body-cont');

				//hidden area element
				_this.element.pasteArea = $('#'+_this.prefix +'_pubGridPasteArea');

				// measure element
				_this.element.measureEl = $('#'+_this.prefix+'Measurer');

				// header html 만들기
				if(headerOpt.view===false){
					_this.element.header.height(0).hide()
					$('#'+_this.prefix+'_vscroll .pubGrid-scroll-top-area').hide();
				}else{
					// 실제 header height 값 넣기.
					_this.config.header.height = _this.element.header.height();
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

		viewCount = drawMode =='init' ?  _this.config.scroll.bodyViewCount : viewCount

		// remove edit area
		if(_this.config.isCellEdit ===true){
			_$renderer.editAreaClose(_this);
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
			tbiItem = tbi[itemIdx];

			if(tbiItem){
				if(_this.config.rowOpt.isAddStyle){
					$pubSelector('#'+_this.prefix+'_bodyContainer .pubGrid-body-cont [rowinfo="'+i+'"]').setAttribute('style', (fnAddStyle.call(null,tbiItem)||''));
				}

				var overRowFlag = (itemIdx >= this.config.dataInfo.orginRowLen);
				var addEle ,tdEle;

				for(var j =0 ; j < asideItem.length ;j++){
					var tmpItem = asideItem[j];
					var cellPosition = i+','+tmpItem.key;

					addEle =$pubSelector('#'+_this.prefix+'_bodyContainer .pubGrid-body-aside-cont').querySelector('[data-aside-position="'+cellPosition+'"]>.aside-content');

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
						tdEle =_this.element.leftContent.querySelector('[data-cell-position="'+(i+','+j)+'"]');
						addEle =tdEle.querySelector('.pub-content');

						colItem = tci[j];
						_this._setCellStyle(tdEle, i, colItem, tbiItem)

						if(overRowFlag){
							addEle.textContent='';
						}else{
							var val = this.getRenderValue(colItem, tbiItem, 'view', addEle);
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
					//var addEle = _this.element.tdEle[cellPosition] = $pubSelector('#'+_this.prefix+'_bodyContainer .pubGrid-body-tbody').querySelector('[data-cell-position="'+cellPosition+'"]>.pub-content')

					tdEle =_this.element.bodyContent.querySelector('[data-cell-position="'+(i+','+j)+'"]');

					if(tdEle){
						addEle =tdEle.querySelector('.pub-content');

						colItem = tci[j];

						_this._setCellStyle(tdEle, i ,colItem , tbiItem)

						if(overRowFlag){
							addEle.textContent='';
						}else{
							var val = this.getRenderValue(colItem, tbiItem, 'view', addEle);
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
			}
						
			itemIdx++;

			if(itemIdx > this.config.dataInfo.orginRowLen){
				_this.element.container.find('[rowinfo="'+i+'"]').hide();
			}else{
				_this.element.container.find('[rowinfo="'+i+'"]').show();
			}
		}
	}
	/**
	 * @method setElementDimensionAndMessage
	 * @description 그리드 element 수치 및 메시지 처리.
	 */
	,setElementDimensionAndMessage : function (){
		var header_height = this.config.header.height;

		this.config.navi.height = 0;

		if(this.options.navigation.usePaging === true || this.options.navigation.status === true){
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

		var  bodyH = mainHeight - cfg.header.height - cfg.footer.height -2  // -2 body border 2px
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
			if(vScrollFlag){
				$('#'+_this.prefix+'_hscroll').css('padding-right',_this.options.scroll.vertical.width);
				_this.element.hScrollEdge.show();
			}else{
				$('#'+_this.prefix+'_hscroll').css('padding-right',0);
				_this.element.hScrollEdge.hide();
			}
			$('#'+_this.prefix+'_hscroll').show();

			var hscrollW = $('#'+_this.prefix+'_hscroll').find('.pubGrid-hscroll-bar-area').width();

			var barWidth = (hscrollW*(bodyW/gridContTotW*100))/100;
			barWidth = barWidth < 25 ? 25 :( barWidth > hscrollW ?hscrollW :barWidth);

			cfg.scroll.hThumbWidth = barWidth;
			cfg.scroll.hTrackWidth =hscrollW - barWidth;
			cfg.scroll.oneColMove = gridContTotW/cfg.tColItem.length ; //gridContTotW/cfg.scroll.hTrackWidth;
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

		_this.element.container.off('mousewheel DOMMouseScroll');
		_this.element.container.on('mousewheel DOMMouseScroll', function(e) {
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

				if(_this.options.scroll.isStopPropagation === true){
					stopPreventCancel(e)
				}else if(_this.config.scroll.top != 0 && _this.config.scroll.top != _this.config.scroll.vTrackHeight){
					stopPreventCancel(e)
				}
			}else{
				if(_this.config.scroll.hUse && _this.options.scroll.horizontal.enableWheel===true){
					_this.moveHorizontalScroll({pos :(delta > 0?'L':'R') , speed : _this.options.scroll.horizontal.speed});

					if(_this.options.scroll.isStopPropagation===true){
						stopPreventCancel(e)
					}else{
						if(_this.config.scroll.left != 0 && _this.config.scroll.left != _this.config.scroll.hTrackWidth){
							stopPreventCancel(e)
						}
					}
				}
			}
		});

		$('#'+_this.prefix+'_vscroll .pubGrid-vscroll-bar-bg').off('mousedown touchstart mouseup touchend mouseleave');
		$('#'+_this.prefix+'_vscroll .pubGrid-vscroll-bar-bg').on('mousedown touchstart',function(e) {
			_$scroll.verticalMove(_this, e.offsetY, _this.config.scroll.top, _this.config.scroll.vThumbHeight, _this.config.scroll.oneRowMove *_this.options.scroll.horizontal.speed);
		}).on('mouseup touchend mouseleave',function(e) {
			_this.config.scroll.mouseDown = false;
			clearTimeout(_this.config.scroll.verticalScrollTimer);
		});

		$('#'+_this.prefix+'_hscroll .pubGrid-hscroll-bar-bg').off('mousedown touchstart mouseup touchend mouseleave');
		$('#'+_this.prefix+'_hscroll .pubGrid-hscroll-bar-bg').on('mousedown touchstart',function(e) {
			_$scroll.horizontalMove(_this, e.offsetX, _this.config.scroll.left, _this.config.scroll.hThumbWidth, _this.config.scroll.oneColMove);
		}).on('mouseup touchend mouseleave',function(e) {
			_this.config.scroll.mouseDown = false;
			clearTimeout(_this.config.scroll.horizontalScrollTimer);
		});

		var scrollbarDragTimeer;
		var startTime = '';
		var hDragDelay = _this.options.scroll.horizontal.dragDelay;
		// 가로 스크롤 bar drag
		_this.element.hScrollBar.off('touchstart.pubhscroll mousedown.pubhscroll');
		_this.element.hScrollBar.on('touchstart.pubhscroll mousedown.pubhscroll',function (e){
			e.stopPropagation();

			var ele = $(this);
			var data = {};

			data.left = _this.config.scroll.left
			data.pageX = evtPos(e).x;

			ele.addClass('active');

			$(document).on('touchmove.pubhscroll mousemove.pubhscroll', function (e1){

				if(startTime==''){
					startTime = new Date().getTime();
				}

				if(new Date().getTime()-hDragDelay <= startTime){
					clearTimeout(scrollbarDragTimeer);
				}

				scrollbarDragTimeer = setTimeout(function() {
					startTime='';
					_this.horizontalScroll(data, e1, 'move');
				}, hDragDelay);
			}).on('touchend.pubhscroll mouseup.pubhscroll mouseleave.pubhscroll', function (e1){
				ele.removeClass('active');
				clearTimeout(scrollbarDragTimeer);
				startTime='';
				_this.horizontalScroll(data, e1, 'end');
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

			var ele = $(this);
			var data = {};
			data.top= _this.config.scroll.top;
			data.pageY = evtPos(e).y;

			ele.addClass('active');

			$(document).on('touchmove.pubvscroll mousemove.pubvscroll', function (e1){
				if(startTime==''){
					startTime = new Date().getTime();
				}

				if(new Date().getTime()-vDragDelay <= startTime){
					clearTimeout(scrollbarDragTimeer);
				}

				scrollbarDragTimeer = setTimeout(function() {
					startTime='';
					_this.verticalScroll( data, e1, 'move');

					if(tooltipFlag){
						tooltipEle.text(_this.config.scroll.viewIdx+1);
						tooltipEle.show();
					}
				}, vDragDelay);
			}).on('touchend.pubvscroll mouseup.pubvscroll mouseleave.pubvscroll', function (e1){
				ele.removeClass('active');
				clearTimeout(scrollbarDragTimeer);
				_this.verticalScroll(data, e1, 'end');
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
	,verticalScroll : function (data, e, type){
		var oy = data.top+(evtPos(e).y - data.pageY);

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
	,horizontalScroll : function (data, e, type){
		var ox = data.left+(evtPos(e).x - data.pageX);

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
		this.config.scroll.before.startCol = this.config.scroll.startCol; // 이전데이터
		this.config.scroll.before.endCol = this.config.scroll.endCol;

		this.config.scroll.startCol = ( startCol > 0? startCol:0 );
		this.config.scroll.endCol = ( endCol >= tci.length? tci.length:endCol );

		// 화면에 다 보이는 col size
		this.config.scroll.insideEndCol = this.config.scroll.endCol + ( itemLeftVal != gridW ? -1 : 0 );

		return containerLeft;
	}
	,_setStatusMessage : function (){

		if(this.options.navigation.status !== true){
			return ; 
		}

		var totCnt = 0; 
		var startVal = 0;
		var endVal = 0;
		if(this.options.navigation.usePaging === true) {
			if(this.config.pagingInfo !== false){
				var pagingInfo =this.config.pagingInfo; 
				totCnt = pagingInfo.totalCount;
				var first =pagingInfo.currPage-1; 
				startVal = first * pagingInfo.countPerPage +1 ;
				endVal = (first+1) * pagingInfo.countPerPage;
			}
		}else{
			totCnt = this.config.dataInfo.orginRowLen;
			startVal = this.config.scroll.viewIdx +1;
			endVal = startVal+ this.config.scroll.insideViewCount;
		}

		if(totCnt > 0){
			this.element.status.empty().html(replaceMesasgeFormat(this.options.navigation.statusFormatter||'', {
				currStart :startVal
				,currEnd : endVal >= totCnt? totCnt: endVal
				,total : totCnt
			}))
		}else{
			this.element.status.empty().html('');
		}
	}
	/**
	 * @method isVisible
	 * @description display visible
	 */
	,isVisible : function (){
		return this.gridElement.is(':visible');
	}
	/**
	 * @method resizeDraw
	 * @description resize 하기
	 */
	,resizeDraw :function (opt){
		if(this.isVisible()===false){
			return ;
		}

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

		var eventName =  'resize.' + _this.prefix;
		var threshold = _this.options.autoResize.threshold;

		var beforeResizeTime = -1;
		
		$(window).off(eventName);
		$(window).on(eventName, function( event ) {
			
			if(threshold < 1){
				_this.resizeDraw();
				return ; 
			}

			if(beforeResizeTime !=-1 && (beforeResizeTime + threshold > new Date().getTime())){
				return ; 
			}

			setTimeout(function (){
				_this.resizeDraw();
			}, threshold);

			beforeResizeTime = new Date().getTime();
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

				_$util.setSelectionRangeInfo(_this, {
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
					var currLeftVal = _this.config.scroll.left; 
					_this.moveHorizontalScroll({pos :(delta > 0?'L':'R') , speed : _this.options.scroll.horizontal.speed});

					if(_this.options.scroll.isStopPropagation===true){
						stopPreventCancel(e)
					}else{
						if(currLeftVal != 0 && currLeftVal != _this.config.scroll.hTrackWidth){
							stopPreventCancel(e)
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
			_$setting.init(_this, true);
		}

		if(headerOpt.drag.enabled === true){
				// drag event start
			_this.element.header.on('dragstart.pubGrid.dragitem', '.pub-header-cont', function (e){
				
				var selEle = $(this)
					,col_idx = selEle.attr('col_idx');
				
				var dt = e.originalEvent.dataTransfer;
				dt.effectAllowed = 'copyMove';
				dt.setData('text/plain',_this.config.tColItem[col_idx].label);
							
			}).on('drag.pubGrid.dragitem', '.label-wrapper', function (e){
				//e.preventDefault();
				//console.log('111');
								
				//console.log('111111111');
			}).on('dragenter.pubtab.dragitem', '.pubTab-item', function (e){
				
				return false; // mouse  cursor 이상 현상 제거 하기 위해서 처리함. 
				
			}).on('dragend.pubtab.dragitem',  function (e){
				//var dt = e.originalEvent.dataTransfer;
				e.originalEvent.dataTransfer.clearData()
				
			}).on('drop.pubGrid.dragitem', '.label-wrapper', function (e){
				//var dt = e.originalEvent.dataTransfer;
				// column move 
				// console.log('drop',dt)
			}).on('dragover.pubGrid.dragitem', function (e){
				e.preventDefault();
			})
			var dropCallback = headerOpt.drag.dropCallback; 

			$(document).off('drop.'+_this.prefix);
			$(document).on('drop.'+_this.prefix, headerOpt.dropSelector, function (e){

				if(!_this.config.focus){
					return true;
				}
				var dt = e.originalEvent.dataTransfer;
				var data = dt.getData('text/plain');

				var reval = dropCallback(e, data); 
				
				if(reval === false){
					return false; 
				}
				
			})

		}
		
	}
	,_initFooterEvent : function (){
		var _this = this;

		var pageCallback = _this.options.navigation.callback;
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
		_$setting.init(this, this.options.setting.enabled);
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
				var initFlag=true;
				if(e.ctrlKey){
					initFlag  = false;
				}
				var rowItemIdx = _this.config.scroll.viewIdx+intValue($(this).closest('tr').attr('rowinfo'));

				_this.addRowSelections(rowItemIdx, initFlag);
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

		_$util.setSelectionRangeInfo(_this, {isMouseDown : false});

		var bodyDragTimer;
		var bodyDragDelay = 150;
		var multipleFlag = _$util.isMultipleSelection(selectionMode);

		function dragScrollMove(ctx){
			var cfg = ctx.config
				, rangeInfo = cfg.selection.range; 
			bodyDragTimer = setInterval(function() {
				if(cfg.mouseDragDirectionY !==false){
					var endIdx = -1;
					if(cfg.mouseDragDirectionY =='D'){
						endIdx = rangeInfo.maxIdx+1;
					}else{
						endIdx = rangeInfo.startIdx > rangeInfo.minIdx ? rangeInfo.minIdx -1 : rangeInfo.maxIdx-1;
					}
					
					_$util.setSelectionRangeInfo(ctx, {
						rangeInfo :  {endIdx : endIdx}
					},false, false);

					ctx.moveVerticalScroll({pos :cfg.mouseDragDirectionY});
				}

				if(ctx.config.mouseScrollDirectionX !==false){
					var endCol = -1;

					if(cfg.mouseScrollDirectionX =='R'){
						endCol = cfg.scroll.insideEndCol+1;
					}else{
						endCol = ctx.config.scroll.insideStartCol-1;
					}

					var reGridFlag = endCol < 0 || endCol >= cfg.tColItem.length ? true :false ;

					_$util.setSelectionRangeInfo(ctx, {
						rangeInfo :  {endCol : endCol}
					},false, reGridFlag);
					
					ctx.moveHorizontalScroll({pos :cfg.mouseScrollDirectionX});
				}
			}, bodyDragDelay);
		}

		var rowClickFn = _this.options.rowOptions.click;
		var rowClickFlag = isFunction(rowClickFn);

		// click event 하나로 합치기. 

		var clickTimer;
		var currentCellPosition;

		var clickCnt = 0, clickDelay = 400;
		
		var resetClick = function () {
			clickCnt = 0;
			currentCellPosition = null;
		}

		// Function to wait for the next click
		function conserveClick(cellPosition) {
			currentCellPosition = cellPosition;
			clearTimeout(clickTimer);
			clickTimer = setTimeout(resetClick, clickDelay);
		}

		// row cell double click event
		var dblCheckFlag = _this.options.rowOptions.dblClickCheck===true;
		var editable = _this.options.editable;
		var dobleClickEventFlag = editable || dblCheckFlag || isFunction(_this.options.rowOptions.dblClick) || isFunction(_this.options.bodyOptions.cellDblClick); 
		var fnDblClick = _this.options.rowOptions.dblClick || _this.options.bodyOptions.cellDblClick ||(function (){});

		// body  selection 처리.
		_this.element.body.on('mousedown.pubgrid.col','.pub-body-td',function (e){

			if(e.which ===3){
				return true;
			}

			if(isInputField(e.target.tagName)){
				return true; 
			}

			var position  = _this.element.body.offset();

			var _l  = position.left
				,_r = _l +_this.config.container.width - _this.options.scroll.vertical.width;
			var _t = position.top,
				_b = _t+_this.config.container.bodyHeight;

			if(multipleFlag){
				// mouse darg scroll
				$(document).on('touchmove.pubgrid.body.drag mousemove.pubgrid.body.drag', function (e1){
					_this.config.isBodyDragging = true; 

					var evtInfo1 = evtPos(e1);

					var movePageX = evtInfo1.x
						, movePageY = evtInfo1.y;

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

					if(!bodyDragTimer) dragScrollMove(_this);

				}).on('touchend.pubgrid.body.drag mouseup.pubgrid.body.drag mouseleave.pubgrid.body.drag', function (e1){
					_this.config.isBodyDragging = false; 
					$(document).off('touchmove.pubgrid.body.drag mousemove.pubgrid.body.drag').off('touchend.pubgrid.body.drag mouseup.pubgrid.body.drag mouseleave.pubgrid.body.drag');
					clearInterval(bodyDragTimer);
					bodyDragTimer = false;
				});
			}

			var sEle = $(this);
			
			var cellInfo = _$util.getCellInfo(_this, sEle);

			var currViewIdx = _this.config.scroll.viewIdx;
			
			_this.setCellClick(e, cellInfo, multipleFlag, selectionMode);

			var newViewIdx = _this.config.scroll.viewIdx;

			if(currViewIdx != newViewIdx){
				cellInfo.r = cellInfo.r-1;
			}

			var colIdx = cellInfo.c;
			var rowItemIdx = cellInfo.rowItemIdx;

			var positionInfo = {
				position : sEle.attr('data-cell-position')
				,rowItemIdx : rowItemIdx
			};

			if(editable === true){
				if(cellInfo.colInfo.renderer.type =='dropdown'){
					resetClick();
					_$renderer.editCell(_this, cellInfo, e);
					return false;
				}
			
				if(clickCnt == 0){
					_$renderer.editAreaClose(_this); // 이전 에디트창 닫기
				}
			}

			if (clickCnt > 0 && (currentCellPosition.position == positionInfo.position && currentCellPosition.rowItemIdx == positionInfo.rowItemIdx) ) { // double click 처리.
				conserveClick(positionInfo);
				resetClick();
				
				if(dobleClickEventFlag){
					if(editable ===true){
						_$renderer.editCell(_this, cellInfo, e);
						return false; 
					}

					var clickRowItem = cellInfo.rowItem; 
					if(dblCheckFlag){
						_this.options.tbodyItem[rowItemIdx] = _this.getRowCheckValue(clickRowItem, clickRowItem['_pubcheckbox']===true?false:true);
	
						var addEle =$pubSelector('#'+_this.prefix+'_bodyContainer .pubGrid-body-aside-cont').querySelector('[data-aside-position="'+cellInfo.r+',checkbox"]>.aside-content');
	
						_$util.setCheckBoxCheck(addEle, clickRowItem);
					}
	
					fnDblClick.call(sEle, {item : clickRowItem, r : rowItemIdx, c : colIdx, keyItem : cellInfo.colInfo});
				}
			}else{
				++clickCnt;
				conserveClick(positionInfo);
			}

			if(!editable && $(e.target).closest('.pub-render-element').length > 0){ // render item click 처리.
				if(isFunction(cellInfo.colInfo.renderer.click)){
					cellInfo.colInfo.renderer.click.call(null, cellInfo);
					return false; 
				}
			}

			if(isFunction(cellInfo.colInfo.colClick)){
				cellInfo.colInfo.colClick.call(this, colIdx, {
					r: rowItemIdx
					,c: colIdx
					,item: cellInfo.rowItem
				});
				return true;
			}
			// row click event
			if(rowClickFlag){
				if(sEle.closest('.pubGrid-body-aside-cont').length > 0){
					return true;
				}
				var clickInfo = _this.getCurrentClickInfo();
				rowClickFn.call(null , {rowItemIdx : clickInfo.rowItemIdx, item : clickInfo.item });
			}

			return true;

		}).on('mouseover.pubgrid.col','.pub-body-td',function (e) {

			if (!_this.config.isBodyDragging) return;

			if(!(selectionMode =='multiple-row' || selectionMode =='multiple-cell')){
				return ;
			}

			var cellInfo = _$util.getCellInfo(_this, $(this));

			var selectRangeInfo = _$util.getSelectionModeColInfo( selectionMode, cellInfo.c, _this.config.dataInfo);

			_$util.setSelectionRangeInfo(_this, {
				rangeInfo : {
					endIdx : cellInfo.rowItemIdx
					,endCol : selectRangeInfo.endCol
				}
			},false , true);

		})
	
		_this.element.pubGrid.on('mouseup.'+_this.prefix,function (e) {
			//_this.element.body.removeClass('pubGrid-noselect');
			_$util.setSelectionRangeInfo(_this, {isMouseDown : false});
		}).on('mousedown.'+_this.prefix,function (e){ // focus in
			_this._setGridFocusIn(e);
		}).on('blur.'+_this.prefix,function (e){ //blur focus out
			_this._setGridFocusOut(e);
		})

		// focus out
		$(document).off('mousedown.'+_this.prefix);
		$(document).on('mousedown.'+_this.prefix, 'html', function (e) {
			if(!_this.config.focus){
				return true;
			}
			
			_this._setGridFocusOut(e);
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

				if(content != ''){
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

					if(startCellInfo.startIdx+iLen > tbodyLen){ // 붙여 넣기 데이터가 더 많으면 추가 row 생성.
						tbodyItems = tbodyItems.concat(_$util.newItems(tColItems, startCellInfo.startIdx+iLen -tbodyLen));
						tbodyLen =tbodyItems.length;
					}

					for(var i =0; i<iLen; i++){
						var addCont = contentArr[i];

						var addRowIdx = startIdx+i;

						if(addRowIdx >= tbodyLen){
							break;
						}

						var rowItem = tbodyItems[addRowIdx];

						var addContArr = addCont.split(/\t/);
						var jLen=addContArr.length;

						_$util.setChangeValue(_this, 'new', rowItem);

						for(var j =0; j <jLen; j++){
							var addColIdx = startCol+j;

							if(addColIdx < tColLen){
								maxCol = Math.max(maxCol,addColIdx);
								rowItem[tColItems[addColIdx].key] = addContArr[j];
							}
						}
					}

					_$util.setSelectionRangeInfo(_this, {
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
		$(document).off('keydown.'+_this.prefix);
		$(document).on("keydown." + _this.prefix, function (e) {

			if(!_this.config.focus) return ;

			if(isInputField(e.target.tagName)){
				return true; 
			}

			// 설정 영역 keydown 처리
			if($(e.target).closest('.pubGrid-setting-area').length > 0) return true;

			var evtKey = eventKeyCode(e);
			
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
				}else if(evtKey==70){ // ctrl+f
					stopPreventCancel(e)
					
					_$setting.settingBtnToggle(_this);
					return true;
				}
			}

			if(_this.options.editable===true){
				var clickInfo = _this.getCurrentClickInfo();

				var cellInfo = _$util.getCellInfo(_this, _$util.getCellElement(_this, clickInfo.r, clickInfo.c));

				if((65 <= evtKey && evtKey <= 90) || (48 <= evtKey && evtKey <= 57)){
					_$renderer.editCell(_this, cellInfo, e);
					return false;
				}
			}

			if( (32 < evtKey && evtKey < 41) || evtKey == 13 || evtKey == 9){
				stopPreventCancel(e)

				_this.gridKeyCtrl(e, evtKey);
			}
		});
	}
	// grid focus out
	,_setGridFocusOut : function (e){
		if(e.which !==2 && $(e.target).closest('#'+this.prefix+'_pubGrid').length < 1  && $(e.target).closest('[data-pubgrid-layer="'+this.prefix+'"]').length < 1){
			this.config.focus = false;
			_$renderer.editAreaClose(this);
		}
	}
	// grid focus in
	,_setGridFocusIn : function (e){
		this.config.focus = true;

		if(!isInputField(e.target.tagName)){
			_$renderer.editAreaClose(this);
		}
	}
	// cell click
	,setCellClick : function (e, cellInfo, multipleFlag, selectionMode){
		var _this =this; 

		_this._setGridFocusIn(e)
		
		var rowItemIdx = cellInfo.rowItemIdx
			,colIdx = cellInfo.c;
		
		var selItem = cellInfo.rowItem;

		if(!_this._isFixedPostion(colIdx)){
			if(colIdx < _this.config.scroll.insideStartCol){
				_this.moveHorizontalScroll({pos: 'L', colIdx: colIdx});
			}else if(colIdx > _this.config.scroll.insideEndCol){
				_this.moveHorizontalScroll({pos: 'R', colIdx: colIdx});
			}
		}
		
		var selectRangeInfo = _$util.getSelectionModeColInfo(selectionMode, colIdx, this.config.dataInfo, this.config.selection.isMouseDown);
		
		if(multipleFlag && e.shiftKey) {	// shift key
			var rangeInfo = {endIdx: rowItemIdx, endCol: selectRangeInfo.endCol};

			if(selectRangeInfo.startCol > -1){
				rangeInfo.srartCol = selectRangeInfo.startCol;
			}

			_$util.setSelectionRangeInfo(_this, {
				rangeInfo : rangeInfo
				,isMouseDown : true
			},false , true);

		}else if(multipleFlag && e.ctrlKey){ // ctrl key

			_$util.setSelectionRangeInfo(_this, {
				rangeInfo : {startIdx : rowItemIdx, endIdx : rowItemIdx, startCol : selectRangeInfo.startCol, endCol: selectRangeInfo.endCol}
				,isSelect : true
				,curr : (_this.config.selection.isSelect?'add':'')
				,isMouseDown : true
				,startCell : {startIdx : rowItemIdx, startCol : selectRangeInfo.startCol}
			}, false, true);

		}else{
			_$util.setSelectionRangeInfo(_this, {
				rangeInfo : {startIdx : rowItemIdx, endIdx : rowItemIdx, startCol : selectRangeInfo.startCol, endCol: selectRangeInfo.endCol}
				,isSelect : true
				,allSelect : false
				,isMouseDown : true
				,startCell : {startIdx : rowItemIdx, startCol : colIdx}
			}, true, true);
		}
		var _r = cellInfo.r 
		// hidden row up
		if(cellInfo.r+1 > _this.config.scroll.insideViewCount){
			_this.moveVerticalScroll({pos: 'D'});
			_r = cellInfo.r -1; 
		}

		_this.config.currentClickInfo ={
			column : cellInfo.colInfo
			,item : selItem
			,rowItemIdx : rowItemIdx
			,c : colIdx
			,r : _r
		};

		window.getSelection().removeAllRanges();
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

				if(_$util.insideScrollCheck(_this, evtKey, evt, endIdx, endCol, scrollInfo, moveRowIdx, endCol)){ // 스크롤 밖에 있을때
					return ;
				}

				if((moveRowIdx -scrollInfo.viewIdx) >= scrollInfo.insideViewCount){
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

				if(_$util.insideScrollCheck(_this, evtKey, evt, endIdx, endCol, scrollInfo, moveRowIdx, endCol)){ // 스크롤 밖에 있을때
					return ;
				}

				if(moveRowIdx < scrollInfo.viewIdx){
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

				if(_$util.insideScrollCheck(_this, evtKey, evt, endIdx, endCol, scrollInfo, endIdx, moveColIdx)){ // 스크롤 밖에 있을때
					return ;
				}

				if(!_this._isFixedPostion(moveColIdx) && moveColIdx <= scrollInfo.startCol){
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

				if(_$util.insideScrollCheck(_this, evtKey, evt, endIdx, endCol, scrollInfo, endIdx, moveColIdx)){ // 스크롤 밖에 있을때
					return ;
				}

				if(moveColIdx >= scrollInfo.insideEndCol){
					_this.moveHorizontalScroll({pos:'R' ,colIdx :moveColIdx});
				}

				break;
			}

			default:{
				break;
			}
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
		var colInfo = _$util.getSelectionRangeInfo(this);
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
					var cellPosition = i+','+j;
					var currIdx = currViewIdx+i;

					var addEle;

					if(_this._isFixedPostion(j)){
						addEle =_this.element.leftContent.querySelector('[data-cell-position="'+cellPosition+'"]');
					}else{
						addEle =_this.element.bodyContent.querySelector('[data-cell-position="'+cellPosition+'"]');
					}
					if(addEle==null) continue;

					_this.config.selection.unSelectPosition[cellPosition]='';

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
				var posInfo = _$util.getCellPosition(sEle);
				if(_this.isSelectPosition(currViewIdx+posInfo.r , posInfo.c)){

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
				var cellPosition = i+','+j;
				var currIdx = currViewIdx+i;

				if(isRowSelect || isColSelect){
					 delete _this.config.selection.unSelectPosition[cellPosition];
				}

				if(!_this.isSelectPosition(currIdx ,j, true)){
					continue;
				}

				var addEle;

				if(_this._isFixedPostion(j)){
					addEle =_this.element.leftContent.querySelector('[data-cell-position="'+cellPosition+'"]');
				}else{
					addEle =_this.element.bodyContent.querySelector('[data-cell-position="'+cellPosition+'"]');
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
	 * @method isSelectPosition
	 * @description cell 선택 여부
	 */
	,isSelectPosition: function (row , col, currFlag){

		if(hasProperty(this.config.selection.unSelectPosition, row+','+col)){
			return false;
		}else{

			if(currFlag){
				return _$util.isSelRange(this.config.selection.range, row , col);
			}else {

				var allRange = this.config.selection.allRange;

				for(var key in allRange){
					var tmpRange = allRange[key];

					if(_$util.isSelRange(tmpRange, row , col)){
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
	 * @description 데이터 복사.
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
						tmpVal = _this.getRenderValue(colItem, item, 'data');
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

		var sCol,eCol,sIdx,eIdx;

		var allSelectFlag = _this._isAllSelect();

		if(allSelectFlag){
			sCol = 0;
			eCol = _this.config.tColItem.length-1;
			sIdx = 0;
			eIdx = _this.config.dataInfo.lastRowIdx;
		}else{
			var colInfo = _this.config.selection;
			sCol = colInfo.minCol;
			eCol = colInfo.maxCol;
			sIdx = colInfo.minIdx;
			eIdx = colInfo.maxIdx;
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

				if((allSelectFlag && !_this.isAllSelectUnSelectPosition(i, j)) || _this.isSelectPosition(i, j)) {
					addRowFlag = true;

					tmpVal = _this.getRenderValue(colItem, item, 'data');
					
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
	 * @method getSelectionColumnInfo
	 * @description selection column info
	 */
	,getSelectionColInfos : function(){

		var sCol,eCol,sIdx,eIdx;

		var allSelectFlag = this._isAllSelect();

		if(allSelectFlag){
			sCol = 0;
			eCol = this.config.tColItem.length-1;
			sIdx = 0;
			eIdx = 1;
		}else{
			var colInfo = this.config.selection;
			sCol = colInfo.minCol;
			eCol = colInfo.maxCol;
			sIdx = colInfo.minIdx;
			eIdx = colInfo.maxIdx;
		}

		if(sIdx < 0 || eIdx < 0)
			return [];

		var tColItem = this.config.tColItem;

		var keyInfo ={};

		for(var i=sIdx; i<=eIdx; i++){
			for(var j=sCol; j<=eCol; j++){
				var colItem = tColItem[j];

				if(colItem.visible===false) continue;

				if((allSelectFlag && !this.isAllSelectUnSelectPosition(i, j)) || this.isSelectPosition(i, j)) {
					if(!hasProperty(keyInfo ,j)){
						keyInfo[j] = colItem;
					}
				}
			}
		}

		var sortKeyArr = Object.keys(keyInfo).sort(function(a, b) {
			return (parseInt(a, 10) > parseInt(b, 10)? 1 : 0);
		});

		var sortResult = [];

		for (var i=0; i < sortKeyArr.length; i++) {
			sortResult.push(keyInfo[sortKeyArr[i]]);
		}

		return sortResult;

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
	,getSelectionItem : function (itemKeys, allColumnFlag) {
		var _this = this;

		var sIdx, eIdx;

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
			if(addRowFlag){
				if(allColumnFlag===true){
					selectItem.push(objectMerge(item));
				}else{
					selectItem.push(sItem);
				}
			} 	
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
			_this.config.sort.orginData = arrayCopy(tbi);
		}

		_this.config.sort.current = _key;

		var reversed = (sortType=='asc') ? 1 : -1;

		if(sortType=='asc' || sortType=='desc'){  // 오름차순

			tbi.sort(function (a,b){
				var v1 = a[_key]
					,v2 = b[_key];

				if(v1 == v2) return 0;

				if(val != ''){
					return (v1==val ? reversed * -1 : (v2==val ? reversed : 0));
				}else{
					return (v1 < v2 ? reversed * -1 : (v1 > v2 ? reversed : 0));
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
			,cfg = _this.config; 
				
		if(cfg.initHeaderResizer === true && cfg.currentHeaderResizeFlag == flag){
			return ;
		}

		cfg.currentHeaderResizeFlag = flag;

		var resizeEle =_this.element.header.find('.pub-header-resizer');
		if(flag){
			resizeEle.css('cursor',_this.options.headerOptions.resize.cursor);
		}else{
			resizeEle.css('cursor','auto');
		}

		if(cfg.initHeaderResizer===true) return ; 
		
		cfg.initHeaderResizer = true; 

		var maxColWidth = _this.options.headerOptions.resize.maxWidth;

		var valueFilterFn = _this.options.valueFilter;
		var filterFnFlag = isFunction(valueFilterFn);

		_this.element.header.on('dblclick.pubgrid.headerResizer', '.pub-header-resizer',function (e){

			if(cfg.currentHeaderResizeFlag ===false) return false; 

			_$util.colResize(_this, $(this));

			var selColItem = cfg.tColItem[_this.drag.resizeIdx];

			var resizeW = selColItem.maxWidth || -1;

			if(resizeW < 1){
				var tbodyItem = _this.options.tbodyItem
					,beforeLen = -1
					,tmpVal, currLen;

				for(var i =0, len = cfg.dataInfo.orginRowLen ;i <len;i++){
					
					tmpVal = _this.getRenderValue(selColItem, tbodyItem[i], 'view');

					if(tmpVal == null || isUndefined(tmpVal)) continue; 
											
					currLen = tmpVal.length;
					if(currLen > beforeLen){
						resizeW = Math.max(getMeasureTextWidth(_this, tmpVal||''), resizeW);
					}

					if(maxColWidth > 0 && resizeW >= maxColWidth){
						break; 
					}
					
					beforeLen= currLen;
				}

				resizeW = Math.max(resizeW, getMeasureTextWidth(_this, selColItem.label, 'header'));
			}

			_this._setHeaderResize(e, _this, 'end', resizeW);
		})

		_this.element.header.on('touchstart.pubresizer mousedown.pubresizer', '.pub-header-resizer', function (e){
			if(cfg.currentHeaderResizeFlag ===false) return false; 
			
			_$util.colResize(_this, $(this));

			_this.element.resizeHelper.show().css('left', (_this.drag.positionLeft+_this.drag.totColW)+'px');

			var startX = evtPos(e).x;
			_this.drag.pageX = startX;
			_this.drag.ele.addClass('pubGrid-move-header')

			// resize시 select안되게 처리 . cursor처리
			_$doc.attr("onselectstart", "return false");

			var moveStart = false;
			_$doc.on('touchmove.colheaderresize mousemove.colheaderresize', function (e1){
				if(!moveStart){
					var moveX = evtPos(e1).x;
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

			return false;
		})
	
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
	,_setHeaderResize : function (e, _this, mode, resizeW){

		if (!_this.drag) return false;

		var drag = _this.drag;

		var w = resizeW, ox = 0;

		if(isUndefined(resizeW)){
			ox = evtPos(e).x;
			w = resizeW ||(drag.colW + (ox - drag.pageX));
		}


		if(mode=='end'){
			this.config.isResize = true;
			var minWidth =_this.options.headerOptions.resize.minWidth
				, maxWidth = _this.options.headerOptions.resize.maxWidth;

			if(minWidth != -1 && w < minWidth){
				w = minWidth;
			}else if(maxWidth != -1 && w > maxWidth){
				w = maxWidth;
			}

			_this.setColumnWidth(drag.resizeIdx, w, drag.colHeader);

			if(isFunction(_this.options.headerOptions.resize.update)){
				_this.options.headerOptions.resize.update.call(null , {index:drag.resizeIdx , width: w});
			}
			drag.ele.removeAttr('style');

		}else{
			var w = drag.totColW + (ox - drag.pageX);
			_this.element.resizeHelper.css('left', _this.drag.positionLeft+w);
			//drag.ele.css('left', w > minWidth? w : minWidth);
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

		if(w <= this.options.headerOptions.resize.minWidth){
			w =this.options.headerOptions.resize.minWidth;
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
	 * @method getHeaderItems
	 * @param  get header items
	 * @description header items
	 */
	,getHeaderItems : function (){
		return this.config.tColItem;
	}
	,getPageNo : function (){
		return this.config.pageNo;
	}
	/**
	 * @method getPagingInfo
	 * @param  totalCount {int} 총카운트
	 * @param  currPage {int} 현재 페이지
	 * @param  countPerPage {int} 한페이지에 나올 row수
	 * @param  unitPage {int} 한페이지에 나올 페이번호 갯수
	 * @description 페이징 하기.
	 */
	,getPagingInfo : function (totalCount, currPage, countPerPage, unitPage) {
		countPerPage = countPerPage || 10;
		unitPage = unitPage || 10;

		if (totalCount < 1) {
			return {
				'currPage' :0 ,'unitPage' : 0
				,'totalCount' : 0 ,'totalPage' : 0
			}
		} 
		
		if (totalCount < countPerPage) {
			countPerPage = totalCount;
		}
		
		var totalPage = (totalCount % countPerPage == 0) ? (totalCount/countPerPage) : (Math.floor(totalCount/countPerPage) + 1)

		if (totalPage < currPage){
			currPage = totalPage;
		}
		
		var currEndCount = countPerPage;
		
		if (currPage != 1) {
			currEndCount = currPage * countPerPage;
		}

		if (currEndCount > totalCount){
			currEndCount = totalCount;
		}
		
		var currStartPage;
		var currEndPage;

		if (totalPage <= unitPage) {
			currEndPage = totalPage;
			currStartPage = 1;
		} else {
			var halfUnitPage = unitPage;

			if(currPage == unitPage || (currPage > unitPage && (totalPage - (currPage-1) >= unitPage)) ){
				halfUnitPage = Math.floor(unitPage /2); 
			}

			if(currPage <= halfUnitPage){
				currEndPage = unitPage;
				currStartPage = 1; 
			}else if(currPage+halfUnitPage < totalPage){
				currEndPage = currPage + halfUnitPage;
				currStartPage = currEndPage - unitPage + 1;
			}else{
				currEndPage = (currPage + halfUnitPage);

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
			,'countPerPage' : countPerPage, 'totalCount' : totalCount ,'totalPage' : totalPage
		};
	}
	/**
	 * @method setTheme
	 * @description set theme
	 */
	,setTheme : function (themeName){
		this.options.theme = themeName;
		this.gridElement.attr('pub-theme', themeName);
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
	 * @param  rowEle {Object} row element
	 * @description get row item
	 */
	,getRowItemToElement : function (rowEle){
		var rowinfo = rowEle.attr('rowinfo');
		var rowIdx = this.config.scroll.viewIdx+intValue(rowinfo);
		return {rowIdx : rowIdx, item : this.getItems(rowIdx)};
		;
	}
	/**
	 * @method closeSettingLayer
	 * @description setting layer close
	 */
	,closeSettingLayer : function (){
		$('[data-pubgrid-layer="'+this.prefix+'"]').removeClass('open'); 
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
		
		var settingEle = $('#'+this.prefix+'_pubGridSettingArea'); 
		settingEle.off();
		settingEle.find('.pubGrid-setting-panel').empty();

		this.element.body.off('mousedown.pubgrid.col mouseover.pubgrid.col');
		$(document).off('mouseup.'+this.prefix).off('mousedown.'+this.prefix);
		$(window).off(this.prefix+"pubgridResize").off("keydown." + this.prefix);
		this.gridElement.removeAttr('pub-theme');
		this.gridElement.find('*').off();
		
		$('#'+this.prefix+'Measurer').remove(); 
		$._removeData(this.gridElement)
		delete _datastore[this.selector];
		$(this.selector).empty();
		//this = {};
	}
	,getDataStore :function (){
		return _datastore;
	}
};

// scroll
var _$scroll = {
	verticalMove : function(ctx, pEvtY, pTop, vThumbHeight, oneRowMove){
		ctx.config.scroll.mouseDown = true;

		clearTimeout(ctx.config.scroll.verticalScrollTimer);

		var upFlag =pEvtY> ctx.config.scroll.top ? false :true;
		var loopcount =5;

		pTop= pTop+((upFlag?-1:1) * (oneRowMove *loopcount));

		if(upFlag){
			if( pEvtY >= pTop ){
				ctx.config.scroll.mouseDown = false
				pTop = pEvtY;
			}
		}else{
			if(pEvtY <= (pTop +vThumbHeight)){
				ctx.config.scroll.mouseDown = false
				pTop = pEvtY-vThumbHeight;
			}
		}

		ctx.moveVerticalScroll({pos :pTop});

		if(ctx.config.scroll.mouseDown){
			ctx.config.scroll.verticalScrollTimer = setTimeout(function() {
				_$scroll.verticalMove(ctx, pEvtY, pTop, vThumbHeight, oneRowMove*ctx.options.scroll.horizontal.speed);
			}, ctx.options.scroll.vertical.bgDelay);
		}
	}
	,horizontalMove : function (ctx, pEvtX, pLeft, hThumbWidth, oneColMove){
		ctx.config.scroll.mouseDown = true;

		clearTimeout(ctx.config.scroll.horizontalScrollTimer);

		var leftFlag =pEvtX > ctx.config.scroll.left ? false :true;
		var loopcount =10;

		pLeft = pLeft+((leftFlag?-1:1) * (oneColMove*loopcount));

		if(leftFlag){
			if( pEvtX >= pLeft ){
				ctx.config.scroll.mouseDown = false
				pLeft = pEvtX;
			}
		}else{
			if(pEvtX <= (pLeft +hThumbWidth)){
				ctx.config.scroll.mouseDown = false
				pLeft = pEvtX-hThumbWidth;
			}
		}

		ctx.moveHorizontalScroll({pos : pLeft});

		if(ctx.config.scroll.mouseDown){
			ctx.config.scroll.horizontalScrollTimer = setTimeout(function() {
				_$scroll.horizontalMove(ctx, pEvtX, pLeft, hThumbWidth, oneColMove);
			}, ctx.options.scroll.horizontal.bgDelay);
		}
	}
}

var _$template = {
	/**
	 * @method bodyHtm
	 * @description body template
	 */
	bodyHtm : function(_this, mode, type){

		var clickFlag = false
			,tci = _this.config.tColItem
			,thiItem;

		var strHtm = [];

		var contTypeClass = type=='left'?'.pubGrid-body-left-cont':'.pubGrid-body-cont';
		//console.log(mode, _this.config.scroll.maxViewCount);
		var tmpeElementBody=_this.element.body.find(contTypeClass);

		var startCol=0, endCol=tci.length;

		if(type=='left'){
			endCol = _this.options.colFixedIndex;
		}else if(type=='cont'){
			startCol = _this.options.colFixedIndex;
		}

		for(var i =0 ; i < _this.config.scroll.maxViewCount; i++){
			var trEle = tmpeElementBody.find('[rowinfo="'+i+'"]'); 
			if(mode != 'init' && trEle.length > 0){
				if(i >= _this.config.scroll.viewCount){
					trEle.hide();
				}else{
					trEle.show();
				}
			}else{
				strHtm.push('<tr class="pub-body-tr '+((i%2==0)?'tr0':'tr1')+'" rowinfo="'+i+'">');

				for(var j=startCol ;j < endCol; j++){
					thiItem = tci[j];
					clickFlag = thiItem.colClick;

					strHtm.push('<td scope="col" class="pub-body-td" data-cell-position="'+i+','+j+'"><div class="pub-content pub-content-ellipsis ' +thiItem['_alignClass']+' '+ (clickFlag?'pub-body-td-click':'') +'"></div></td>');
				}
				strHtm.push('</tr>');
			}
		}

		if(mode=='init'){
			var bodyHtmTemplate = '';
			bodyHtmTemplate +=_this._getColGroup(_this.prefix+'colbody', type);
			bodyHtmTemplate += '<tbody class="pubGrid-body-tbody">'+strHtm.join('')+'</tbody>';

			tmpeElementBody.empty().html(bodyHtmTemplate);

		}else{
			strHtm = strHtm.join('');
			if(strHtm != ''){
				tmpeElementBody.append(strHtm);
			}
			return true;
		}
	}
}

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
	,getCellInfo : function (ctx, cellEle){
		var posInfo = this.getCellPosition(cellEle);
		var rowItemIdx = ctx.config.scroll.viewIdx+posInfo.r;
		return {
			r : posInfo.r
			,c : posInfo.c
			,rowItemIdx : rowItemIdx
			,rowItem : ctx.options.tbodyItem[rowItemIdx]
			,colInfo : ctx.config.tColItem[posInfo.c] 
		}
	}
	// grid position 
	,getCellPosition : function (cellEle){
		var posInfo = cellEle.data('cell-position').split(',');
			
		return {
			r : intValue(posInfo[0])
			,c : intValue(posInfo[1])
		}
	}
	// grid position 
	,getCellElement : function (ctx, r, c){
		return ctx.element.body.find('.pub-body-td[data-cell-position="'+r+','+c+'"]');
	}
	// new add
	,genAllColumnSearch : function (gridCtx){
		var tcolItems = gridCtx.options.tColItem; 

		var logicStr =[]

		var searchLogic = defaultCondition['searchIndex'].code; 
		
		for(var i=0; i<tcolItems.length; i++){
			var tcolItem = tcolItems[i];
			logicStr.push(i != 0 ? defaultLogicalOp.getCode('or') : '');
			logicStr.push(replaceMesasgeFormat(searchLogic , {key : tcolItem.key}));
		}
		
		return this.genSearchLogic(logicStr.join(''));
	}
	// new add
	,genSearchLogic : function (checkLogicStr){
		return 'if(#logic#){return true;} return false;'.replace('#logic#', checkLogicStr);
	}
	// new add
	/**
	 * @method getSearchRegExp
	 * @param schVal {String} - search text.
	 * @description get search RegExp
	 */	
	,getSearchRegExp : function(schVal, regType) { 
		if(regType == 'all'){
			schVal = schVal.replace(/([.?*+^$[\]\\()|{}-])/g, "\\$1");	
		}else{
			schVal = schVal.replace(/([.?*+^$[\]\\(){}-])/g, "\\$1");	
		}
		
		return new RegExp('('+schVal + ')','i');
	}
	/**
	 * @method trim
	 * @description trim
	 */	
	,trim : function(str) { 
		return str.replace(/^\s+|\s+$/g,""); 
	}
	,numval : function (str){
		return str.replace(/[^0-9|.]/g,''); 
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
	 * @method isSelRange
	 * @description 선택된 cell check
	 */
	,isSelRange: function (range,row,col){
		if(col =='row'){
			return range.minIdx <=row && row <= range.maxIdx;
		}else{
			return range.minIdx <=row && row <= range.maxIdx && range.minCol <=col && col <= range.maxCol;
		}
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
	,getSelectionModeColInfo : function (selectionMode, colIdx, dataInfo, isMouseDown){
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
	 * @method setChangeValue
	 * @description CUD모드 변경. (c = create , u = update , d =delete)
	 */
	,setChangeValue : function (ctx, mode, rowItem, colInfo, newValue){
		if(mode == 'new'){
			rowItem['_pubCUD'] = 'C';
		}else if(mode == 'modify'){
			rowItem['_pubCUD'] = rowItem['_pubCUD']=='_C'?'C':(rowItem['_pubCUD']=='C'?'CU':'U');
			colInfo.maxWidth = Math.max(getCharLength(newValue||'', ctx.options.headerOptions.oneCharWidth), colInfo.maxWidth);
			rowItem[colInfo.key] = newValue;

			var editRowInfo = ctx.config.editRowInfo;
			var tdEle = ctx.element.body.find('.pub-body-td[data-cell-position="'+editRowInfo.r+','+editRowInfo.c+'"]').get(0);

			// 공통으로 처리. 
			ctx._setCellStyle(tdEle, editRowInfo.rowItemIdx ,colInfo, rowItem);
			ctx.getRenderValue(colInfo, rowItem, 'view', tdEle.querySelector('.pub-content'));
			
		}else if(mode == 'remove'){
			rowItem['_pubCUD'] = 'D';
		}

		return rowItem;
	}
	/**
	*
	*/
	,setCheckBoxCheck : function (checkEle, item){
		checkEle.innerHTML = '<input type="checkbox" class="pub-row-check" '+(item['_pubcheckbox']?'checked':'')+'/>';
	}
	/**
	 * @method setRangeInfo
	 * @description
	 */
	,setRangeInfo : function(ctx, evtKey, evt, endIdx, moveColIdx){

		var startCol=moveColIdx, endCol=moveColIdx;

		if(ctx.options.selectionMode =='multiple-row' || ctx.options.selectionMode =='row'){
			startCol = 0;
			endCol = ctx.config.dataInfo.colLen-1;
		}

		var multipleFlag = this.isMultipleSelection(ctx.options.selectionMode);

		if(multipleFlag && (evtKey != 9 && evt.shiftKey)){
			this.setSelectionRangeInfo(ctx, {
				rangeInfo :  {endIdx : endIdx, endCol : endCol}
				,startCell : {startIdx : endIdx, startCol : moveColIdx}
			}, false,true);
		}else{
			this.setSelectionRangeInfo(ctx, {
				rangeInfo :  {startIdx : endIdx,endIdx : endIdx, startCol:startCol,endCol :endCol}
				,startCell : {startIdx : endIdx, startCol : moveColIdx}
			},true, true);
		}
	}
	/**
	 * @method isRangeKey
	 * @description header , col 선택 여부 확인
	 */
	 ,isRangeKey : function (ctx, key){
		return ctx.config.selection.allRange[key] ?true :false;
	}
	/**
	 * @method setSelectionInfo
	 * @description set selection info
	 */
	,setSelectionInfo : function (ctx, cfgSelect, attrInfo, rangeInfo){

		for(var key in attrInfo){
			if(key =='rangeInfo'){
				;
			}else if(key =='curr'){
				if(attrInfo[key]=='add'){
					cfgSelect.curr+=1;
					cfgSelect.range = rangeInfo;
					var rangeKey = rangeInfo._key ?  rangeInfo._key : cfgSelect.curr;

					if(this.isRangeKey(ctx, rangeKey)){
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
	/**
	 * @method setSelectionRangeInfo
	 * @param  initFlag {boolean} init flag
	 * @param  tdSelectFlag {boolean} select flag
	 * @description 선택 영역 정보 셋팅.
	 */
	 ,setSelectionRangeInfo : function(ctx, selectionInfo, initFlag, tdSelectFlag){
		var _this =this;
		var cfgSelect = ctx.config.selection;

		if(initFlag !==true  && ctx._isAllSelect()){
			return ;
		}

		var	rangeInfo = selectionInfo.rangeInfo;

		if(initFlag === true){

			var initOpt = {
				curr: 0
				,range: {startIdx: -1, endIdx : -1, startCol: -1, endCol: -1}
				,allRange: {}
				,isSelect: false
				,isMouseDown: false
				,unSelectPosition: {}
				,allSelect: false
				,minIdx: -1, maxIdx: -1
				,minCol: -1, maxCol: -1
				,startCell: {startIdx : -1, startCol: -1}
			};
			_this.setSelectionInfo(ctx, initOpt, selectionInfo, rangeInfo);
			cfgSelect = ctx.config.selection = initOpt;
			cfgSelect.allRange[initOpt.curr] =cfgSelect.range;
		}else{
			_this.setSelectionInfo(ctx, cfgSelect, selectionInfo, rangeInfo);
		}

		var currInfo = cfgSelect.range;

		for(var key in rangeInfo){
			currInfo[key] = rangeInfo[key];
		}

		cfgSelect.minIdx = ( cfgSelect.minIdx ==-1 ? Math.min(currInfo.startIdx, currInfo.endIdx) : Math.min(cfgSelect.minIdx, currInfo.startIdx, currInfo.endIdx) );
		cfgSelect.maxIdx = Math.max(cfgSelect.maxIdx, currInfo.endIdx, currInfo.startIdx);
		cfgSelect.minIdx = cfgSelect.minIdx < -1 ? 0 : cfgSelect.minIdx;
		cfgSelect.maxIdx = cfgSelect.maxIdx >= ctx.config.dataInfo.lastRowIdx ? ctx.config.dataInfo.lastRowIdx : cfgSelect.maxIdx;

		if(initFlag !==true){
			cfgSelect.minCol = (cfgSelect.minCol == -1 ? Math.min(currInfo.endCol, currInfo.startCol): Math.min(cfgSelect.minCol, currInfo.endCol, currInfo.startCol) );
			cfgSelect.maxCol = Math.max(cfgSelect.maxCol, currInfo.endCol, currInfo.startCol);

			cfgSelect.minCol = cfgSelect.minCol < -1 ? 0 : cfgSelect.minCol;
			cfgSelect.maxCol = cfgSelect.maxCol >= ctx.config.tColItem.length ? ctx.config.tColItem.length-1 : cfgSelect.maxCol;
		}

		if(isUndefined(rangeInfo)) return ;

		currInfo.minIdx = Math.min(currInfo.startIdx, currInfo.endIdx)
		currInfo.maxIdx = Math.max(currInfo.endIdx, currInfo.startIdx);
		currInfo.minCol = Math.min(currInfo.endCol, currInfo.startCol);
		currInfo.maxCol = Math.max(currInfo.endCol, currInfo.startCol);

		if(initFlag !==true){
			currInfo.minCol = currInfo.minCol < -1 ? 0 : currInfo.minCol;
			currInfo.maxCol = currInfo.maxCol >= ctx.config.tColItem.length ? ctx.config.tColItem.length-1 : currInfo.maxCol;

			currInfo.minIdx = currInfo.minIdx < -1 ? 0 : currInfo.minIdx;
			currInfo.maxIdx = currInfo.maxIdx >= ctx.config.dataInfo.lastRowIdx ? ctx.config.dataInfo.lastRowIdx : currInfo.maxIdx;
		}

		if(tdSelectFlag){
			ctx._setCellSelect(initFlag);
		}
	}
	/**
	 * @method getSelectionRangeInfo
	 * @description 선택된 cell 영역 구하기.
	 */
	 ,getSelectionRangeInfo :function(ctx){
		var selectionInfo = ctx.config.selection.range;

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

		if(ctx.config.scroll.viewIdx >= startIdx){
			startRow = 0;
		}else{
			startRow = startIdx-ctx.config.scroll.viewIdx;
		}

		if(ctx.config.scroll.viewIdx+ctx.config.scroll.maxViewCount <= endIdx){
			endRow = ctx.config.scroll.viewCount-1;
		}else{
			endRow = endIdx-ctx.config.scroll.viewIdx;
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
	 * @method insideScrollCheck
	 * @description  cursor scroll inside check
	 */
	,insideScrollCheck : function(ctx, evtKey, evt, endIdx, endCol, scrollInfo, moveRowIdx, moveColIdx){

		if(ctx.config.isKeyNavHandler){
			if(ctx.options.bodyOptions.keyNavHandler.call(evt,{key : evtKey, moveCol: moveColIdx, moveRow : moveRowIdx, item : ctx.getItems(moveRowIdx)})===false){
				return false;
			}
		}

		this.setRangeInfo(ctx, evtKey, evt, moveRowIdx, moveColIdx);

		var reFlag = false;
		if(endIdx < scrollInfo.viewIdx || (endIdx > scrollInfo.viewIdx+scrollInfo.insideViewCount)){ // 스크롤 밖에 있을때
			ctx.moveVerticalScroll({pos: 'M',rowIdx :moveRowIdx });
			reFlag = true;
		}

		if(!ctx._isFixedPostion(moveColIdx)){
			if(endCol < scrollInfo.startCol){ // 스크롤 밖에 있을때
				ctx.moveHorizontalScroll({pos: 'L',colIdx :moveColIdx});
				reFlag = true;
			}else if(endCol > scrollInfo.endCol){
				ctx.moveHorizontalScroll({pos: 'R',colIdx :moveColIdx});
				reFlag = true;
			}
		}
		return reFlag;
	}
	,colResize : function (_this , sEle){
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
}

// background click check
$(document).on('mousedown.pubgrid.background', function (e){
	if(e.which !==2){
		var targetEle = $(e.target);
		var pubGridLayterEle = targetEle.closest('.pubGrid-layer');
		if(pubGridLayterEle.length < 1 ){
			

			$('.pubGrid-layer.open').each(function (){
				var sEle = $(this); 
				if(!sEle.data('pubgrid-close-btn')){
					sEle.removeClass('open');
					return true; 
				}

				setTimeout(function (){
					var pEle = $('#'+sEle.data('pubgrid-layer')+'_pubGrid').get(0).parentNode.parentNode;
	
					var isVisible = true; 
					var idx = 0;
					while(pEle != null && idx < 10 && pEle != document.body){
	
						var pComputedStyle = getComputedStyle(pEle);
						
						if(pComputedStyle.visibility =='hidden' || pComputedStyle.display == 'none' || parseFloat(pComputedStyle.opacity) <=0){
							isVisible = false; 
							break; 
						}
						idx++;
						pEle = pEle.parentNode;
					}
					
					if(isVisible ===false){
						sEle.removeClass('open');
					}
				}, 300);
			})
		}else{
			var layerid = pubGridLayterEle.data('pubgrid-layer');
			$('.pubGrid-layer.open').each(function (){
				var sEle = $(this);

				if(layerid != sEle.data('pubgrid-layer')){
					if(!sEle.data('pubgrid-close-btn')){
						sEle.removeClass('open');
					}
				}
			})
		}
	}
})

var defaultCondition ={
	'==': {nm: '같다', code: 'item["{{key}}"] == chkOpts[{{idx}}].chkVal'}
	,'!=' :{nm: '같지않다', code: 'item["{{key}}"] != chkOpts[{{idx}}].chkVal'}	
	,'index': {nm: '포함한다', isRegExp: true, code: 'chkOpts[{{idx}}].chkRegExp.test(item["{{key}}"])'}
	,'notIndex' : {nm: '포함하지 않는다', isRegExp: true, code : '!chkOpts[{{idx}}].chkRegExp.test(item["{{key}}"])'}
	,'startsWith':{nm: '시작한다', code: 'item["{{key}}"].startsWith(chkOpts[{{idx}}].chkVal)'}
	,'endsWith':{nm: '끝난다', code: 'item["{{key}}"].endsWith(chkOpts[{{idx}}].chkVal)'}
	,'>': {nm: '크다', code: 'item["{{key}}"] > chkOpts[{{idx}}].chkVal'}
	,'>=': {nm: '크거나같다', code: 'item["{{key}}"] >= chkOpts[{{idx}}].chkVal'}
	,'<': {nm: '작다', code: 'item["{{key}}"] < chkOpts[{{idx}}].chkVal'}
	,'<=': {nm: '작거나같다', code: 'item["{{key}}"] <= chkOpts[{{idx}}].chkVal'}
	,'searchIndex': {nm: '포함한다', isRegExp: true, code: 'chkRegExp.test(item["{{key}}"])'}
};

var defaultLogicalOp ={
	'and': {nm:'AND',code : ' && '}
	,'or': {nm:'OR',code : ' || '}
	,getCode : function (key){
		return this[key].code;
	}
};

var gridOperators = {
	'string' : [
		defaultCondition['index']
		,defaultCondition['notIndex']
		,defaultCondition['==']
		,defaultCondition['!=']
		,defaultCondition['startsWith']
		,defaultCondition['endsWith']
	]
	,'number' : [
		defaultCondition['==']
		,defaultCondition['!=']
		,defaultCondition['>']
		,defaultCondition['>=']
		,defaultCondition['<']
		,defaultCondition['<=']
	]
};


var _$renderer = {
	button : function (gridCtx, thiItem, rowItem, mode){
		var renderer = thiItem.renderer;
		return replaceMesasgeFormat('<span class="pub-render-element button">{{label}}</span>', {
			label: renderer.label
		})
	}
	, image : function (gridCtx, thiItem, rowItem, mode){
		var renderer = thiItem.renderer;

		var imgSrc;
		if(isFunction(renderer.url)){
			imgSrc = renderer.src(rowItem);
		}else{
			imgSrc = replaceMesasgeFormat(renderer.src, rowItem);
		}

		return replaceMesasgeFormat('<img class="pub-render-element img" src="{{src}}">', {
			src: imgSrc
		})
	}
	, checkbox : function (gridCtx, thiItem, rowItem, mode){
		var renderer = thiItem.renderer;
		
		return replaceMesasgeFormat('<input type="checkbox" class="pub-render-element check">{{label}}', {
			label : rowItem[thiItem.key]
		})
	}
	, dropdown : function (gridCtx, thiItem, rowItem, mode){

		var renderer = thiItem.renderer;

		var strHtm = [];

		strHtm.push('<span class="pub-render-element dropdown">');
		strHtm.push(replaceMesasgeFormat('<span class="pub-content ">{{text}}</span>',{text : rowItem[thiItem.key]}));
		strHtm.push('<span class="pub-icon"><svg width="12px" height="8px" viewBox="0 0 110 110" style="enable-background:new 0 0 100 100;"><g><polygon points="0,0 100,0 50,90" fill="#737171"></polygon></g></svg></span>');
		strHtm.push('</span>');

		return strHtm.join('');
	}
	, link : function (gridCtx, thiItem, rowItem, mode){
		return replaceMesasgeFormat('<span class="pub-render-element link">{{value}}</span>',{value : rowItem[thiItem.key]});
	}
	, html : function (gridCtx, thiItem, rowItem, mode){
		return thiItem.template(thiItem, rowItem);
	}
	, text : function (gridCtx, thiItem, rowItem, mode){
		var type = thiItem.type || 'string';

		var itemVal;

		if(gridCtx.config.isValueFilter && mode =='view'){
			itemVal = gridCtx.options.valueFilter(thiItem, rowItem);
		}else{
			itemVal = rowItem[thiItem.key];
		}

		var tmpFormatter={};
		if(type == 'money' || type == 'number'){
			tmpFormatter = gridCtx.options.formatter[type];
		}

		if(isFunction(thiItem.formatter)){
			itemVal = thiItem.formatter.call(null,{colInfo:thiItem, item: rowItem, formatInfo : tmpFormatter});
		}else{
			if(gridCtx.options.useDefaultFormatter===true){
				if(type == 'money'){
					itemVal = formatter[type](itemVal, tmpFormatter.fixed, tmpFormatter.prefix, tmpFormatter.suffix);
				}else if(type == 'number'){
					itemVal = formatter[type](itemVal, tmpFormatter.fixed, tmpFormatter.prefix, tmpFormatter.suffix);
				}
			}
		}

		return itemVal; 
	}
	/**
	 * @method editCell
	 * @description edit form
	 */
	,editCell : function (gridCtx, cellInfo, e){

		if(gridCtx.config.isCellEdit){
			_$renderer.editAreaClose(gridCtx, false); // 이전 에디트창 닫기

			if((cellInfo.r == gridCtx.config.editRowInfo.r && cellInfo.c == gridCtx.config.editRowInfo.c)){
				gridCtx.config.isCellEdit = false;
				gridCtx.config.editRowInfo = {};
				return ;
			}
		}

		gridCtx.config.isCellEdit = true;

		gridCtx.config.editRowInfo = {
			rowItemIdx : cellInfo.rowItemIdx
			,r : cellInfo.r
			,c : cellInfo.c
			,colInfo : cellInfo.colInfo
			,rowItem : cellInfo.rowItem
		};

		var selEl = gridCtx.element.body.find('.pub-body-td[data-cell-position="'+cellInfo.r+','+cellInfo.c+'"]');

		var editEleOffset = selEl.offset();
		
		var position = {
			left : editEleOffset.left
			,top : editEleOffset.top
		}

		var colInfo = cellInfo.colInfo
		var rowItem = cellInfo.rowItem;

		var renderInfo = colInfo.renderer||{};
		var renderType = renderInfo.type; 
		var reForm =[];

		var editAreaEle = $('#'+gridCtx.prefix+'_pubGridEditArea')
				
		if(editAreaEle.length < 1){
			pubGridLayoutElement.append('<div id="'+gridCtx.prefix+'_pubGridEditArea" data-pubgrid-layer="'+gridCtx.prefix+'" class="pubGrid-edit-layer pubGrid-noselect pubGrid-layer"></div>');
			editAreaEle = $('#'+gridCtx.prefix+'_pubGridEditArea');
		}

		reForm.push( '<div class="pubGrid-edit" data-edit-type="'+renderType+'">');
		if(renderType =='dropdown'){
			reForm.push( '<ul class="pubGrid-edit-field dropdown">');
			var items = renderInfo.list||[];

			var labelField = renderInfo.labelField
				,valueField = renderInfo.valueField;
			
			for(var i =0, len = items.length;i < len; i++){
				
				var item = items[i];

				var param = {
					label : labelField ? item[labelField] : item
					,value : valueField ? item[valueField] : item
				}

				param.selected = (param.value == rowItem[colInfo.key] ? 'selected' : '');

				reForm.push(replaceMesasgeFormat('<li class="pubGrid-select-item {{selected}}" data-val="{{value}}" >{{label}}</li>',param))				
			}
			reForm.push( '</ul>');

			editAreaEle.empty().html(reForm.join(''));
			editAreaEle.addClass('open');

			editAreaEle.find('.pubGrid-select-item').on('click', function (){// item select
				var clickElement = $(this);

				clickElement.closest('.pubGrid-edit-field').find('.pubGrid-select-item.selected').removeClass('selected');

				clickElement.addClass('selected');
				_$renderer.editAreaClose(gridCtx);
			});

			editAreaEle.css('height','auto');

			var positionTop = position.top
				, editAreaHeight =editAreaEle.height()
				, screenBottom = (_$win.scrollTop() + _$win.height());
			if(positionTop + (editAreaHeight+10) > screenBottom){
				positionTop = positionTop - (editAreaHeight+2); // border 2 
				if(positionTop < 0){
					editAreaHeight = editAreaHeight - Math.abs(positionTop);
					positionTop = 0;	
				}

				editAreaHeight = editAreaHeight+ 'px';
			}else{
				editAreaHeight = 'auto';
				positionTop = positionTop + gridCtx.config.rowHeight;
			}
			
			editAreaEle.css({
				top : positionTop+'px'
				,left: position.left +'px'
				,width :selEl.outerWidth() +'px'
				,height: editAreaHeight
			})

			return ; 

		}else if(renderType =='textarea'){
			reForm.push( '<textarea class="pubGrid-edit-field"></textarea>');
		}else if(renderType =='number'){
			reForm.push( '<input type="number" class="pubGrid-edit-field">');
		}else{
			reForm.push( '<input type="text" class="pubGrid-edit-field">');
		}
		reForm.push( '</div>');

		selEl.append(reForm.join(''));
		var editEl = selEl.find('.pubGrid-edit-field');
		editEl.focus();
		editEl.val(rowItem[colInfo.key]);
		
	}
	// edit 창 닫기
	,editAreaClose: function (gridCtx, editInfoInitFlag){

		if(gridCtx.config.isCellEdit === true){
			var editRowInfo = gridCtx.config.editRowInfo;
			var renderer = editRowInfo.colInfo.renderer;

			var newValue = editRowInfo.rowItem[editRowInfo.colInfo.key];
			if( renderer && renderer.type == 'dropdown'){
				var selectElements = $('#'+gridCtx.prefix+'_pubGridEditArea .pubGrid-select-item.selected');

				if(selectElements.length > 0){
					newValue = selectElements.attr('data-val');
				}
				
				$('#'+gridCtx.prefix+'_pubGridEditArea').removeClass('open');
			}else{
				var beforeEditEle = gridCtx.element.body.find('.pub-body-td[data-cell-position="'+gridCtx.config.editRowInfo.r+','+gridCtx.config.editRowInfo.c+'"] .pubGrid-edit-field');

				if(beforeEditEle.length > 0){
					newValue = beforeEditEle.val();
					beforeEditEle.remove();
				}
			}

			if(newValue != editRowInfo.rowItem[editRowInfo.colInfo.key]){
				_$util.setChangeValue(gridCtx, 'modify', editRowInfo.rowItem, editRowInfo.colInfo, newValue);
			}

			if(editInfoInitFlag !== false){
				gridCtx.config.isCellEdit = false;
				gridCtx.config.editRowInfo = {};
			}
			
		}		
	}
}


var _$setting = {
	filterCheckboxIdx : 0
	/**
	 * @method init
	 * @description grid setting info
	 */
	,init : function (gridCtx, btnEnabledFlag){
		var _this = this; 
		var settingOpt = gridCtx.options.setting;

		var settingBtn = gridCtx.element.container.find('.pubGrid-setting-btn'); 
		
		if(btnEnabledFlag ===true){
			settingBtn.show();
		}else{
			settingBtn.hide();
		}

		if(gridCtx.config.initSettingFlag ===true){
			return ; 
		}

		var settingAreaEle = $('#'+gridCtx.prefix+'_pubGridSettingArea')
				
		if(settingAreaEle.length < 1){
			var template = '<div id="'+gridCtx.prefix+'_pubGridSettingArea" data-pubgrid-layer="'+gridCtx.prefix+'" data-pubgrid-close-btn="'+settingOpt.btnClose+'" class="pubGrid-preferences-wrapper pubGrid-noselect pubGrid-layer '+(settingOpt.mode =='full-center' ? 'full-center' : '')+'">'

			if(settingOpt.mode =='full-center'){
				template +='	<div class="full-setting-overlay"></div>';
			}
			template+='	<div class="pubGrid-setting-panel '+settingOpt.mode+'">'
			template+='	</div>';
			template+=+'</div>';

			pubGridLayoutElement.append(template);
			settingAreaEle = $('#'+gridCtx.prefix+'_pubGridSettingArea')
		}else{
			if(settingAreaEle.find('.pubGrid-setting-header').length > 0){
				return ; 
			}
		}

		settingAreaEle.find('.pubGrid-setting-panel').empty().html(_this.templateHtml(gridCtx, settingOpt));
		
		var isCustomSetting = isFunction(settingOpt.click); 
	
		// 한번만 초기화 하기 위해서 처리.
		gridCtx.config.initSettingFlag = true; 
			
		this.initEvent(gridCtx, settingOpt, settingAreaEle);
		this.initResizeEvent(gridCtx, settingOpt, settingAreaEle);

		// 초기 값 셋팅
		gridCtx.config.settingConfig.currentTColItems = arrayCopy(gridCtx.config.tColItem);
					
		if(gridCtx.config.settingConfig.viewInitFlag===true){
			_this.setViewColTemplate(gridCtx);
			gridCtx.config.settingConfig.changeInfos = {};
			gridCtx.config.settingConfig.currentClickItem = {};
			gridCtx.config.settingConfig.viewInitFlag = false; 
		}
		
		settingBtn.on('mousedown.pubgrid.setting', function (e){
			_$renderer.editAreaClose(gridCtx);
			if(isCustomSetting){
				settingOpt.click.call(null,{evt :e , item :{}});
				return ; 
			}
			
			if(!isUndefined(e.which) && e.which !== 1){
				return ; 
			}

			stopPreventCancel(e)
			
			if(settingAreaEle.hasClass('open')){
				settingAreaEle.removeClass('open');
			}else{
				_this.closeOtherSettingLayer(gridCtx.prefix);
				settingAreaEle.addClass('open');

				gridCtx.config.settingConfig.currentTColItems = arrayCopy(gridCtx.config.tColItem); // cancel 시 적용 하기 위해서 복사

				// current click item setting
				if(hasProperty(gridCtx.config.settingConfig.currentClickItem,'key')){
					settingAreaEle.find('.tcol-view-list [data-key="'+gridCtx.config.settingConfig.currentClickItem.key+'"] .view-col-item').trigger('click.viewitem');
				}
				
				if(settingOpt.mode != 'full-center'){
					var evtPosVal = evtPos(e);					
					
					var height = settingAreaEle.find('.pubGrid-setting-panel').height()
						,evtX = evtPosVal.x
						,evtY = evtPosVal.y;
						
					if(isUndefined(evtX)){  // ctrl + f 로 오픈시
						var offsetVal = settingBtn.offset();
						evtX = offsetVal.left + settingBtn.width();
						evtY = offsetVal.top;
					}
					
					if(settingAreaEle.hasClass("pub-grid-setting-move")){
						var wScrollTop = _$win.scrollTop()
							,wScrollLeft = _$win.scrollLeft();
						
						var pos = settingAreaEle.position();

						if(wScrollTop > pos.top){
							settingAreaEle.css({top : wScrollTop});
						}

						if(wScrollLeft > pos.left){
							settingAreaEle.css({left : wScrollLeft});
						}

						return ;
					}
			
					var bottom = _$win.scrollTop() + _$win.height();
				
					var offTop = evtY;
				
					offTop = ((offTop+height > bottom )? (bottom- (height)) : offTop);
					offTop = offTop < 3 ? 3 : offTop;
											
					settingAreaEle.css({top : offTop, left : evtX+10});
				}
			}
			
			return true;
		});
		
		settingAreaEle.find('.pubGrid-setting-header').on('mousedown touchstart',function(e) {

			var tagName = e.target.tagName;
			if(tagName.search(/(input|textarea|select|button)/gi) > -1){
				return true;
			}

			var pos = settingAreaEle.position();

			var evtPosVal = evtPos(e);
		
			var panelEle = settingAreaEle.find('.pubGrid-setting-panel'); 

			var startLeft = pos.left
				, startTop = pos.top
				, startX = evtPosVal.x
				, startY = evtPosVal.y
				, height = panelEle.outerHeight()
				, width = panelEle.outerWidth();
			
			$(document).on('touchmove.pubsettingheader mousemove.pubsettingheader', function (e1){
				var wScrollTop = _$win.scrollTop()
					,wScrollLeft = _$win.scrollLeft()
				var bottom = wScrollTop + _$win.height()
					,right = wScrollLeft + _$win.width();

				var evtPosVal = evtPos(e1);
				var moveX = evtPosVal.x
					, moveY= evtPosVal.y;

				var moveLeft = startLeft - (startX -moveX)
					,moveTop = startTop - (startY -moveY);

				moveTop = ((moveTop+height > bottom )? (bottom- (height)) : moveTop);
				moveTop = moveTop < wScrollTop ? wScrollTop : moveTop;

				moveLeft = (moveLeft + width > right)? (right-width) : moveLeft;
				moveLeft = moveLeft < 0 ? 0 : moveLeft;
				
				settingAreaEle.css({top : moveTop, left : moveLeft});
			}).on('touchend.pubsettingheader mouseup.pubsettingheader mouseleave.pubsettingheader', function (e1){
				settingAreaEle.addClass("pub-grid-setting-move");
				$(document).off('touctouchmove.pubsettingheader mousemove.pubsettingheader').off('touchend.pubsettingheader mouseup.pubsettingheader mouseleave.pubsettingheader');
			});
		})
		
		// 컬럼 고정. 
		settingAreaEle.find('[name="pubgrid_col_fixed"]').on('change.colfixed.setting', function (e){
			var sEle = $(this);
			var sIndex = sEle.prop('selectedIndex');
	
			gridCtx.setColFixedIndex(sIndex);
	
			settingAreaEle.removeClass('open');
		})
	}
	,closeOtherSettingLayer : function (layerid){
		$('.pubGrid-layer.open').not('[data-pubgrid-layer="'+layerid+'"]').each(function (){
			$(this).removeClass('open'); 
		})
	}
	// setting 버튼 토글. 
	,settingBtnToggle : function (gridCtx){
		gridCtx.element.container.find('.pubGrid-setting-btn').trigger('mousedown.pubgrid.setting');
	}
	,initEvent : function (gridCtx, settingOpt, settingAreaEle){
		var _this = this; 
		
		var orginTColIdxKeyMap = gridCtx.config.dataInfo.orginTColIdxKeyMap;
		// data search 
		var dataSearchFieldEle = settingAreaEle.find('[name="dataSearchField"]')
			,dataSearchEle = settingAreaEle.find('[name="dataSearch"]')
			,dataSearchBtn = settingAreaEle.find('.data-search-btn');

		if(settingOpt.configVal.search.field != ''){
			dataSearchFieldEle.val(settingOpt.configVal.search.field);
		}

		settingAreaEle.find('.header-close-btn').on('click.close.btn', function (){
			settingAreaEle.removeClass('open');
		})

		dataSearchEle.val(settingOpt.configVal.search.val);	

		if(localStorage && settingOpt.configVal.search.val =='' && settingOpt.useRememberValue){
			dataSearchEle.val(localStorage.getItem(gridCtx.prefix+'SearchValue'));
		}
		
		dataSearchBtn.on('click.data.search',  function (e){
			var searchVal = dataSearchEle.val(); 

			gridCtx.options.setting.configVal.search = {
				field : settingOpt.mode=='simple' ? dataSearchFieldEle.val(): '$all'
				,val : searchVal
			};

			if(localStorage){
				localStorage.setItem(gridCtx.prefix+'SearchValue',searchVal);
			}

			gridCtx._setSearchData('search');		
			
			if(isFunction(settingOpt.callback)){
				settingOpt.callback.call(null,{evt :e , item : {search : settingOpt.configVal.search}})
			}
		});

		dataSearchEle.on('keydown.data.search',  function (e){
			var keycode = eventKeyCode(e);

			if (keycode == 13) {
				dataSearchBtn.trigger('click.data.search');
				return false; 
			}else if(keycode == 27){
				settingAreaEle.removeClass('open');
				return false; 
			}
		});
	
		// all column click
		settingAreaEle.on('click.add.colItem', '.add-col-item', function (e){
			var sEle = $(this);
	
			var chkIdx= sEle.attr('data-chk-idx');
	
			var isChecked = sEle.hasClass('on'); 
	
			if(chkIdx == 'all'){
				if(isChecked){
					_this.setViewColTemplate(gridCtx, []);
					settingAreaEle.find('.add-col-item').removeClass('on');
				}else{
					_this.setViewColTemplate(gridCtx, gridCtx.config.dataInfo.orginTColItem);
					settingAreaEle.find('.add-col-item').addClass('on');
				}
			}else{
				var clickItem = gridCtx.config.dataInfo.orginTColItem[parseInt(chkIdx,10)]; 
	
				var chkEle = settingAreaEle.find('.tcol-view-list [data-key="'+clickItem.key+'"]');
	
				if(sEle.hasClass('on')){
					chkEle.remove();
					sEle.removeClass('on');
				}else{
					if(chkEle.length < 1){
						_this.addViewColTemplate(gridCtx, clickItem);
					}
					sEle.addClass('on');
				}
			}
		})
	
		// select column click
		settingAreaEle.on('click.remove.colItem', '.remove-btn', function (e){
			var parentEle = $(this).closest('[data-key]');
			var dataKey =parentEle.attr('data-key'); 

			delete gridCtx.config.settingConfig.changeInfos[dataKey];

			settingAreaEle.find('.add-col-item[data-key="'+dataKey+'"]').removeClass('on');
			parentEle.remove();
		});
		
		// setting btn
		settingAreaEle.on('click.setting.btn','.pubGrid-btn[data-mode]', function (e){
			var sEle = $(this);
			var mode = sEle.attr('data-mode');
	
			if(mode=='apply' || mode=='a&c'){	// apply , a&c = apply and close
				var viewColumnKey = [];
				var fixedColIdx= -1;
				settingAreaEle.find('.tcol-view-list>li').each(function (idx){
					var ele = $(this);
					viewColumnKey.push(ele.attr('data-key'));

					if(ele.find('.column-fix-icon.on').length > 0){
						fixedColIdx = idx;
					}
				});

				if(viewColumnKey.length  < 1){
					settingAreaEle.find('.setting-message').show();
					return ; 
				}else{
					settingAreaEle.find('.setting-message').hide();
				}

				_this.setFilterChangeInfo(gridCtx, settingAreaEle, gridCtx.config.settingConfig.currentClickItem);

				var newViewCols = [];
				var headRedraw = gridCtx.options.tColItem.length == viewColumnKey.length ? false : true;
				var changeWidthInfo={};
				viewColumnKey.forEach(function (itemKey, idx){
					if(!headRedraw && itemKey != gridCtx.options.tColItem[idx].key){
						headRedraw = true; 
					}
					var item =orginTColIdxKeyMap[itemKey]; 
					var changeInfo = gridCtx.config.settingConfig.changeInfos[itemKey];
					
					if(!isUndefined(changeInfo)){
						changeInfo.width = isNaN(changeInfo.width)? item.width : Number(changeInfo.width);
						if(changeInfo.width != item.width){
							changeWidthInfo[idx] = changeInfo.width;
						}
						item.filterInfos=[];
						item = objectMerge(item, changeInfo);
					}

					newViewCols.push(item);
				});

				gridCtx.options.tColItem =newViewCols;

				_this.getFilterCheckLogic(gridCtx, newViewCols);

				fixedColIdx = fixedColIdx+1;

				if(headRedraw || gridCtx.options.colFixedIndex != fixedColIdx){
					gridCtx._setSearchData('search', false); // search
					gridCtx.setColFixedIndex(fixedColIdx, true);
				}else{
					for(var key in changeWidthInfo){	// 변경된 width 적용.
						gridCtx.setColumnWidth(key, changeWidthInfo[key], null, false);
					}
					gridCtx._setSearchData('search'); // search
				}

				gridCtx.config.settingConfig.currentTColItems = arrayCopy(gridCtx.config.tColItem);
				
				if(mode =='apply')	return ; 
								
			}else if(mode=='default'){
				gridCtx.config.dataInfo.orginTColItem.forEach(function (item){
					item.filterInfos = [];
				});
				gridCtx.config.settingConfig.viewInitFlag = true; 
				gridCtx.config.settingConfig.filterInfo = false; 
				gridCtx.config.settingConfig.changeInfos = {};
				_this.viewFilterIcon(settingAreaEle, '$all', false);
				
			
				_this.setViewColumnInfo(gridCtx, settingAreaEle, {}, {filterInfos:[]});
				gridCtx.options.tColItem = gridCtx.config.dataInfo.orginTColItem;
				_this.setViewColTemplate(gridCtx, gridCtx.config.dataInfo.orginTColItem);

				dataSearchEle.val('');
				gridCtx.options.setting.configVal.search = {
					field : ''
					,val : ''
				};
				
				gridCtx._setSearchData('search', false); 
				gridCtx.setColFixedIndex(0, true);
				return ; 
			}else{	 // cancel
				var changeInfos = gridCtx.config.settingConfig.changeInfos;
				for(var key in changeInfos){
					var item = changeInfos[key];

					if(item.filterInfos.length > 0 && !hasProperty(orginTColIdxKeyMap[key],'filterInfos')){
						_this.viewFilterIcon(settingAreaEle, key, false);
					}
				}
				
				gridCtx.config.settingConfig.changeInfos = {};
				gridCtx.config.settingConfig.currentClickItem = {};
				gridCtx.config.tColItem = gridCtx.config.settingConfig.currentTColItems;
				_this.setViewColTemplate(gridCtx);
				_this.setViewColumnInfo(gridCtx, settingAreaEle, {}, {filterInfos:[]});
			}
	
			settingAreaEle.removeClass('open');
		})

		// view item click
		var shiftStartIdx = -1;
		settingAreaEle.on('click.viewitem','.view-col-item', function (e){
			var sEle = $(this);
			var dataKeyEle = sEle.closest('[data-key]');
			var dataKey = sEle.closest('[data-key]').attr('data-key');
			var selectItem = orginTColIdxKeyMap[dataKey];

			var beforeClickItem = gridCtx.config.settingConfig.currentClickItem;
			
			shiftStartIdx = e.shiftKey ? shiftStartIdx : -1;
			
			if(e.ctrlKey){
				if(sEle.hasClass('on')){
					sEle.removeClass('on');
				}else{
					sEle.addClass('on');
				}
			}else if(e.shiftKey){
				var beforeIdx = settingAreaEle.find('.tcol-view-list [data-key="'+beforeClickItem.key+'"]').index();

				if(beforeIdx != -1){
					shiftStartIdx = shiftStartIdx ==-1 ? beforeIdx : shiftStartIdx;
					var currentIdx = dataKeyEle.index();
					var allViewEle = dataKeyEle.closest('.tcol-view-list').children();

					var maxIdx = Math.max(currentIdx, beforeIdx, shiftStartIdx)
						,minIdx = Math.min(currentIdx, beforeIdx, shiftStartIdx);
					
					var downFlag = shiftStartIdx < currentIdx;

					for(var i = minIdx; i <= maxIdx; i++){
						var item = $(allViewEle[i]); 
						if(item.is(':visible')){

							if((downFlag && shiftStartIdx <= i && i <= currentIdx)  // down
								|| (!downFlag && currentIdx <= i && i <= shiftStartIdx) // up
							){
								item.find('.view-col-item').addClass('on')
							}else{
								item.find('.view-col-item').removeClass('on')	
							}	
						}
					}
				}else{
					shiftStartIdx = -1;
					sEle.addClass('on');
				}
			}else{
				settingAreaEle.find('.view-col-item.on').removeClass('on');
				sEle.addClass('on');
			}
				
			if(beforeClickItem.key){
				_this.setFilterChangeInfo(gridCtx, settingAreaEle, beforeClickItem);
			}

			var changeInfo = _this.getViewItemChangeInfo(gridCtx, dataKey, selectItem);
			gridCtx.config.settingConfig.changeInfos[dataKey] = changeInfo;

			_this.setViewColumnInfo(gridCtx, settingAreaEle, selectItem, changeInfo);
		})
		
		// add filter item
		settingAreaEle.on('click.addop.item', '.add-op-btn', function (e){
			var detailViewCol = gridCtx.config.settingConfig.currentClickItem; 

			if(hasProperty(detailViewCol,'key')){
				var viewType = _this.getOpType(gridCtx, detailViewCol.type);
							
				settingAreaEle.find('.filter-item-list').append(_this.getReplaceCheckId(gridCtx.config.settingConfig.filterTemplate[viewType]));
				settingAreaEle.find('.filter-area').scrollTop(function (){
					return this.scrollHeight;
				});
			}
		})

		// remove filter item 
		settingAreaEle.on('click.filter.remove','.filter-check-status ',function (e){
			var sEle = $(this);
			var filterItemEle = sEle.closest('li');
			var dataKey = filterItemEle.attr('data-key');

			var selectItem = orginTColIdxKeyMap[dataKey];
			var changeInfo = _this.getViewItemChangeInfo(gridCtx, dataKey, selectItem);
			changeInfo.filterInfos = [];  // filter 정보 초기화
			gridCtx.config.settingConfig.changeInfos[dataKey] = changeInfo;

			if(gridCtx.config.settingConfig.currentClickItem.key == dataKey){
				_this.setViewColumnInfo(gridCtx, settingAreaEle, selectItem, changeInfo);
			}
			
			sEle.removeClass('on');
		})

		// remove filter row item 
		settingAreaEle.on('click.filterrow.remove','.filter-row-remove',function (e){
			var sEle = $(this);
			var filterItemEle = sEle.closest('li');
			filterItemEle.remove();		
		})

		// 숫자 필드 숫자만 들어가게 처리. 
		settingAreaEle.on('keypress.number.field','input[type="number"]',function (e){
			var sEle = $(this);
			var maxlen = parseInt(sEle.attr('maxlength'),10);
			var keyCode = e.keyCode; 

			if((48 <= keyCode && keyCode <=57)  && sEle.val().length < maxlen){
				return true; 
			}else{
				return false; 
			}
		})

		// 숫자 컬럼 처리.
		settingAreaEle.on('input.number.field','[data-type="number"]',function (e){
			var sEle = $(this);
			var numVal = sEle.val();

			if(isNaN(numVal)){
				sEle.val(_$util.numval(numVal));
			}
		})

		// 전체 컬럼 검색
		settingAreaEle.find('[name="allTcolSearch"]').on('input.allcol.field', function (e){
			var schRegExp = _$util.getSearchRegExp($(this).val());
			
			gridCtx.config.dataInfo.orginTColItem.forEach(function (item){
				var liEle =settingAreaEle.find('.add-col-item[data-key="'+item.key+'"]').closest('li'); 

				if(schRegExp.test(item.label)){
					liEle.show();
				}else{
					liEle.hide();
				}
			});
		})

		// 선택 컬럼 검색
		settingAreaEle.find('[name="viewTcolSearch"]').on('input.viewcol.field', function (e){
			var schRegExp = _$util.getSearchRegExp($(this).val());
			
			settingAreaEle.find('.tcol-view-list [data-key]').each(function (item, idx){
				var sEle = $(this);
				var dataKey = sEle.attr('data-key');

				var item = gridCtx.config.dataInfo.orginTColIdxKeyMap[dataKey];

				if(schRegExp.test(item.label)){
					sEle.show();
				}else{
					sEle.hide();
				}
			})
		})

		var tColViewListEle = settingAreaEle.find('.view-item-panel .item-list'); 
		// 선택 컬럼 검색
		tColViewListEle.on('click.viewcol.fixicon', '.column-fix-icon', function (e){
			var sEle = $(this);
			if(sEle.hasClass('on')){
				sEle.removeClass('on');
			}else{
				tColViewListEle.find('.column-fix-icon.on').removeClass('on');
				sEle.addClass('on');
			}
		})
	
		// column up down btn
		settingAreaEle.find('.arrow-btn [data-arrow]').on('click', function (e){
			var sEle =$(this); 
			
			var upFlag = sEle.attr('data-arrow')=='up';
			var selectEles = settingAreaEle.find('.view-col-item.on'); 

			if(selectEles.length < 1) return ; 

			if(upFlag){
				selectEles.each(function (item, idx){
					var itemEle = $(this).closest('li');
					var prevEle = itemEle.prevAll(':visible').first();
					var prevIdx= prevEle.index()
						,curridx = itemEle.index();

					if(prevIdx == -1 && curridx != 0){
						itemEle.closest('.tcol-view-list').prepend(itemEle);
					}else{
						if(prevEle.length > 0){
							var prevOnFlag = prevEle.find('.view-col-item.on').length > 0;

							if(prevOnFlag){
								if(prevIdx+1 != curridx){
									prevEle.after(itemEle);
								}
							}else{
								prevEle.before(itemEle);
							}
						}
					}
				})

				var firstEle= $(selectEles[0]);

				if(firstEle.position().top < 0){
					tColViewListEle.scrollTop(tColViewListEle.scrollTop() + firstEle.position().top)
				}
			}else{
				var firstEle=false;
				for(var i =selectEles.length-1; i>=0; i--){
					var itemEle = $(selectEles[i]).closest('li');

					if(firstEle===false){
						firstEle = itemEle; 
					}

					var nextEle = itemEle.nextAll(':visible').first();

					if(nextEle.length > 0){
						if(nextEle.find('.view-col-item.on').length < 1){
							nextEle.after(itemEle);
						}
					}
				}

				var scrollTopVal = (firstEle.outerHeight() + firstEle.position().top + tColViewListEle.scrollTop()) - tColViewListEle.height(); 

				if(scrollTopVal > 0){
					tColViewListEle.scrollTop(scrollTopVal);
				}
			}			
		})
	}
	,initResizeEvent: function (gridCtx, settingOpt, settingAreaEle){
		
		var settingPanelEle = settingAreaEle.find('.pubGrid-setting-panel');
		settingAreaEle.find('.pubGrid-resize-handler').on('touchstart.pubrgrid.resizer mousedown.pubrgrid.resizer',function (e){
			var oe = e.originalEvent.touches;

			var startX=  (oe && oe[0]? oe[0].pageX : e.pageX)
				,startY= (oe && oe[0]? oe[0].pageY : e.pageY);
						
			var width = settingPanelEle.width()
				,height = settingPanelEle.height();

			$(document).on('touchmove.pubrgrid.resizer mousemove.pubrgrid.resizer', function (e1){
				var oe1 = e1.originalEvent.touches;
				
				var moveX=	(oe1 && oe1[0]? oe1[0].pageX : e1.pageX) - startX
					,moveY= (oe1 && oe1[0]? oe1[0].pageY : e1.pageY) - startY;
				
				settingPanelEle.css({
					width : (width+moveX)+'px'
					,height : (height+moveY)+'px'
				});
					
			}).on('touchend.pubrgrid.resizer mouseup.pubrgrid.resizer mouseleave.pubrgrid.resizer', function (e1){
				$(document).off('touchmove.pubrgrid.resizer mousemove.pubrgrid.resizer').off('touchend.pubrgrid.resizer mouseup.pubrgrid.resizer mouseleave.pubrgrid.resizer');
			});
		});
	}
	,getViewItemChangeInfo : function(gridCtx, dataKey, selectItem) {
		
		var changeInfo = gridCtx.config.settingConfig.changeInfos[dataKey]; 
		if(isUndefined(changeInfo)){
			changeInfo ={
				filterInfos : selectItem.filterInfos ||[]
				, width : selectItem.width
			}
		}else{
			changeInfo.width = selectItem.width;
		}

		return changeInfo; 
	}
	// filter check logic
	,getFilterCheckLogic : function (gridCtx, tColItemArr){
		var allChkVal = [];
		var _this = this; 		

		var filterChkFlag = false; 
		var chkLogicStr = [];
		
		tColItemArr.forEach(function (item, tColIdx){
			var filterInfos = item.filterInfos;
			
			if($.isArray(filterInfos) && filterInfos.length > 0){
				var viewType = _this.getOpType(gridCtx, item.type);
				var opArr = gridCtx.config.settingConfig.operators[viewType];

				if(filterChkFlag) chkLogicStr.push(defaultLogicalOp.getCode('and'));

				chkLogicStr.push('(');
				filterInfos.forEach(function (filterItem, idx){
					var opItem = opArr[filterItem.op];

					allChkVal.push({
						chkVal : filterItem.text
						,chkRegExp : opItem.isRegExp?_$util.getSearchRegExp(filterItem.text, 'all') : false
					});

					if(idx > 0){
						chkLogicStr.push(( filterItem.logicOp ? defaultLogicalOp.getCode('and') : defaultLogicalOp.getCode('or')));
					}
					
					chkLogicStr.push(replaceMesasgeFormat(opItem.code , {
						key : item.key
						,idx : allChkVal.length -1
					}));
				});
				chkLogicStr.push(')');

				filterChkFlag = true; 
			}
		})

		if(filterChkFlag){
			var logicStr = _$util.genSearchLogic(chkLogicStr.join(''));

			gridCtx.config.settingConfig.filterInfo = {
				fn : new Function('item','chkOpts', logicStr)
				,chkOpts : allChkVal
			};
		}else{
			gridCtx.config.settingConfig.filterInfo = false; 
		}
	}
	,viewFilterIcon : function (settingAreaEle, key, visible){
		var filterIconSelector = (key=='$all'?'':'[data-key="'+key+'"]') +' .filter-check-status';
		
		if(visible===true){
			settingAreaEle.find(filterIconSelector).addClass('on');
		}else{
			settingAreaEle.find(filterIconSelector).removeClass('on');
		}
	}
	// get operator type
	,getOpType : function (gridCtx, type){
		type = (type||'').toLowerCase();
		return hasProperty(gridCtx.config.settingConfig.operators, type) ? type : 'string';
	}
	// defail info 
	,setViewColumnInfo : function (gridCtx, settingAreaEle, selectItem, changeInfo){
		var _this =this; 

		if(changeInfo.filterInfos){
			var viewType = hasProperty(gridCtx.config.settingConfig.operators, selectItem.type) ?selectItem.type : 'string';
			var filterRowHtml = gridCtx.config.settingConfig.filterTemplate[viewType]; 

			var filterItemListEle = settingAreaEle.find('.filter-item-list'); 
			filterItemListEle.empty();
			if(changeInfo.filterInfos.length > 0){
				
				changeInfo.filterInfos.forEach(function (item, idx){
					var filterRowEle =$(_this.getReplaceCheckId(filterRowHtml)); 
					
					filterRowEle.find('[name="filter-op-logical"]').attr('checked',item.logicOp);
					filterRowEle.find('[name="filter-op"]').val(item.op);
					filterRowEle.find('[name="filter-text"]').val(item.text);
					
					filterItemListEle.append(filterRowEle);
				})	
			}else{
				filterItemListEle.append(_this.getReplaceCheckId(filterRowHtml));
			}
		}
		
		gridCtx.config.settingConfig.currentClickItem = selectItem;
		gridCtx.config.settingConfig.detailViewCol = changeInfo; 
		settingAreaEle.find('.select-item-name').val(selectItem.label);
		settingAreaEle.find('[name="setting-width-field"]').val(changeInfo.width);

		if(isUndefined(selectItem.key)){
			settingAreaEle.find('.view-col-item.on').removeClass('on');
		}
	}
	,getReplaceCheckId : function (replaceStr){
		return replaceStr.replace(/#checkboxid#/g, 'fc_'+ (++this.filterCheckboxIdx));
	}
	// filter change Info
	,setFilterChangeInfo : function (gridCtx, settingAreaEle, changeInfo){
		if(hasProperty(changeInfo,'key')){
			
			var beforeChangeInfo = gridCtx.config.settingConfig.changeInfos[changeInfo.key]; 

			if(!isUndefined(beforeChangeInfo)){
				var widthVal = parseInt(settingAreaEle.find('[name="setting-width-field"]').val(),10);

				if(!isNaN(widthVal) && widthVal > 0){
					beforeChangeInfo.width = widthVal;
				}

				beforeChangeInfo.filterInfos = [];
				
				settingAreaEle.find('.filter-item-list').find('li').each(function (){
					var sEle = $(this);
					var filterText = sEle.find('[name="filter-text"]').val(); 
					if(_$util.trim(filterText)){
						
						beforeChangeInfo.filterInfos.push({
							op : sEle.find('[name="filter-op"]').val()
							,text : filterText
							,logicOp : sEle.find('[name="filter-op-logical"]').is(':checked')
						});
					}
				})

				if(beforeChangeInfo.filterInfos.length > 0){
					this.viewFilterIcon(settingAreaEle, changeInfo.key, true);
				}else{
					this.viewFilterIcon(settingAreaEle, changeInfo.key, false);
				}
			}
		}
	}
	// set view column template
	,setViewColTemplate : function (gridCtx, tColItems){
		var _this = this; 
		if(isUndefined(tColItems)){
			tColItems= gridCtx.config.tColItem;
		}

		var settingAreaEle = $('#'+gridCtx.prefix+'_pubGridSettingArea');
		var strHtm = [];
		tColItems.forEach(function (item){
			settingAreaEle.find('.add-col-item[data-key="'+item.key+'"]').addClass('on');
			strHtm.push(_this.viewColTemplateHtml(item));
		})

		settingAreaEle.find('.tcol-view-list').empty().html(strHtm.join(''));
	}
	,addViewColTemplate : function (gridCtx, addItems){
		var _this = this; 
		var tColItems = []
		if($.isArray(addItems)){
			tColItems = addItems; 
		}else{
			tColItems.push(addItems);
		}

		var strHtm = [];
		tColItems.forEach(function (item){
			strHtm.push(_this.viewColTemplateHtml(item));
		})
		
		$('#'+gridCtx.prefix+'_pubGridSettingArea .tcol-view-list').append(strHtm.join(''));
	}
	,viewColTemplateHtml : function (item){
		return '<li data-key="'+item.key+'"><span class="remove-btn"></span>'
			+'<span class="view-col-item">'+item.label+'</span>'
			+'<span class="filter-check-status"></span><span class="column-fix-icon" title="Column fixed"></span></li>';
	}
	,initFilterItemTemplateHtml: function (gridCtx){

		var operators = gridCtx.config.settingConfig.operators;

		for(var key in operators){
			var  condHtm =[];
			condHtm.push('<li>');
			condHtm.push('<span class="filter-op-logical"><input type="checkbox" name="filter-op-logical" id="#checkboxid#"><label for="#checkboxid#"></label></span>');
			condHtm.push('<select name="filter-op" class="filter-op">');
			operators[key].forEach(function(item, idx){
				condHtm.push('<option value="'+idx+'">'+item.nm+'</option>');	
			});
			
			condHtm.push('</select>');
			condHtm.push('<input type="text" name="filter-text" class="filter-text" autocomplete="off" data-type="'+key+'">');
			condHtm.push('<button type="button" class="filter-row-remove">-</button>');
			condHtm.push('</li>');	

			gridCtx.config.settingConfig.filterTemplate[key] = condHtm.join('');
		}
		
	}
	,templateHtml : function (gridCtx, settingOpt){
		var strHtm = [];
		
		if(settingOpt.mode =='simple'){
			var optHtm = [];

			gridCtx.config.tColItem.forEach(function (item){
				optHtm.push('<option value="'+item.key+'">'+item.label+'</option>');
			})
			var optHtmStr = optHtm.join('');

			strHtm.push('<div class="pubGrid-setting-simple-body">');
			strHtm.push(' <div class="pubGrid-search-area">');
			strHtm.push('  <select name="dataSearchField"><option value="$all">All</option>'+optHtmStr+'</select>');
			strHtm.push('  <input type="text" name="dataSearch" class="pubGrid-search-field">');
			strHtm.push('  <button type="button" class="pubgrid-btn data-search-btn">'+gridCtx.options.i18n['search.button']+'</button>');
			strHtm.push(' </div>');
			
			if(settingOpt.enableColumnFix ===true){
				strHtm.push(' <div class="pubGrid-colfixed-area"><span>'+gridCtx.options.i18n['setting.column.fixed.label']+'</span>');
				strHtm.push('  <select name="pubgrid_col_fixed"><option value="0">사용안함</option>'+optHtmStr+'</select>');
				strHtm.push(' </div>');
			}
						
			strHtm.push('</div>');
		}else{

			gridCtx.config.settingConfig.operators = objectMerge(gridOperators, gridCtx.options.operators);
			this.initFilterItemTemplateHtml(gridCtx);

			strHtm.push('	<div class="pubGrid-setting-header">All Search : <input class="input-sch" type="text" name="dataSearch" />');
			strHtm.push('	<button type="button" class="data-search-btn">Search</button>');
			strHtm.push('   <button type="button" class="pubgrid-btn header-close-btn"></button></div>');
			strHtm.push('	<div class="pubGrid-setting-body">');
			strHtm.push('		<div class="tcol all-item-panel">');
			strHtm.push('			<div class="label">All Column</div>');
			strHtm.push('			<span class="add-col-item" data-chk-idx="all"></span>');
			strHtm.push('			<input class="input-sch" type="text" name="allTcolSearch" />');
			strHtm.push('			<div class="item-list">');
			strHtm.push('				<ul class="tcol-all-list"> ');

			var allColItems = gridCtx.config.dataInfo.orginTColItem;
			allColItems.forEach(function (item, idx){
				strHtm.push('<li><span class="add-col-item" data-chk-idx="'+idx+'" data-key="'+item.key+'">'+item.label+'</span></li>');
			})
			
			strHtm.push('				</ul>');
			strHtm.push('			</div>');
			strHtm.push('		</div>');
			strHtm.push('		<div class="tcol view-item-panel">');
			strHtm.push('			<div class="label">Select Column</div>');
			strHtm.push('			<input class="input-sch" type="text" name="viewTcolSearch"> ');
			strHtm.push('			<span class="arrow-btn"><a href="javascript:;" data-arrow="up"></a><a href="javascript:;" data-arrow="down"></a></span>');
			strHtm.push('			<div class="item-list">');
			strHtm.push('				<ul class="tcol-view-list"> ');	
			strHtm.push('				</ul>');
			strHtm.push('			</div>');
			strHtm.push('		</div>');
			strHtm.push('		<div class="tcol setting-panel">');
			strHtm.push('			<div>name : <input type="text" class="select-item-name" readonly disabled> width : <input type="number" max="99999" min="0" maxlength="5" name="setting-width-field" class="setting-width-field"/>px</div>');
			strHtm.push('			<div>filter :</div>');
			strHtm.push('			<div  class="filter-area">');
			strHtm.push('			  <ul class="filter-item-list"></ul>');
			strHtm.push('			  <button type="button" class="add-op-btn">+</button>');
			strHtm.push('			</div>');
			strHtm.push('			<div class="btn-area">');
			strHtm.push('			  <button type="button" class="pubGrid-btn" data-mode="default">Restore Defaults</button>');
			strHtm.push('			  <button type="button" class="pubGrid-btn" data-mode="apply">Apply</button>');
			strHtm.push('		  	</div> ');
			strHtm.push('		</div>');
			strHtm.push('	</div>');
			strHtm.push('	<div class="pubGrid-setting-footer">');
			strHtm.push('		<div class="btn-area">');
			strHtm.push('			<button type="button" class="pubGrid-btn" data-mode="a&c">Apply & close</button>');
			strHtm.push('			<button type="button" class="pubGrid-btn" data-mode="cancel">Cancel</button>');
			strHtm.push('			<span class="setting-message">Select Column</span>');
			strHtm.push('		</div>	');
			strHtm.push('	</div>');
			strHtm.push('<div class="pubGrid-resize-handler"></div>')
		}

		return strHtm.join('');
	}
}


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

$.pubGrid.setDefaults = function (defaultValue){
	_defaults = objectMerge(_defaults, defaultValue);
}

}(jQuery, window, document));