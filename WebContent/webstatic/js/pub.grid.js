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
,_$doc = $(document)
,_datastore = {}
,_defaults = {
	fixed:false
	,drag:false
	,scrollWidth : 18	// 스크롤바 넓이
	,minWidth : 38
	,rowOptions:{
		height:20	// cell 높이
	}
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
		,threshold :150
	}
	,resizeGridWidthFixed : true	// 리사이즈시 그리드 리사이즈 여부.
	,headerOptions : {
		view : true	// header 보기 여부
		,sort : false	// 초기에 정렬할 값
		,redraw : true	// 초기에 옵션 들어오면 새로 그릴지 여부.
		,resize:{	// resize 여부
			enabled : true
			,cursor : 'col-resize'
		}
		,colWidthFixed : false  // 넓이 고정 여부.
		,colMinWidth : 50  // 컬럼 최소 넓이
	}
	,height: 200
	,tColItem : [] //head item
 	,theadGroup : [] // head group 
	,tbodyItem : []  // body item
	,tbodyGroup : [] // body group 
	,tfootItem : []  // foot item
	,rowClick : false //row(tr) click event
	,rowContextMenu : false // row(tr) contextmenu event
	,page : false	// paging info
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


function scrollBarSize (ele) {
	var scrollInfo = {};
    	var html =
	    '<div id="_pubGrid_scrollbar_width" style="position: absolute; top: -300px; width: 100px; height: 100px; overflow-y: scroll;">'+
	    '    <div style="height: 120px">1</div>'+
	    '</div>';
	$(ele).append(html);
	var schBarW= 100 - $('#_pubGrid_scrollbar_width > div').width();
	ele.find('#_pubGrid_scrollbar_width').remove();
	//if (_broswer == 'msie') schBarW  = schBarW / 2; // need this for IE9+
	return schBarW;
} 

var util= {
	formatter : {
		'money' : function (num , fixedNum , prefix , suffix){
			return (prefix||'')+ util.formatter.number(num, fixedNum) +(suffix||'');
		}
		,'number': function (num, fixedNum){
			fixedNum = fixedNum || 0; 
			
			if (!isFinite(num)) {
				return num;
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
		// scroll size 
		var _this = this; 
		_this.selector = element;
		_this.prefix = 'pub'+new Date().getTime();
		_this.element = $(element);
		
		_this.config = {totGridWidth : 0, scrollWidth :(scrollBarSize(_this.element)+1)};
		
		_this.options =$.extend(true, {}, _defaults);
		_this.setOptions(options, true);
		
		_this.drag ={};
		_this.addStyleTag();

		_this._setThead();
		_this.setData(_this.options.tbodyItem , 'init');
		
		_this.config.gridXScrollFlag = false;
		_this.config.page = _this.options.page;

		_this._windowResize();

		return this;
	}
	/**
     * @method _setGridWidth
     * @description grid 넓이 구하기
     */
	,_setGridWidth : function (mode){
		var _this = this;
		
		_this.config.gridElementWidth = _this.element.innerWidth()-1; // border 값 빼주기.			
	}
	/**
     * @method setOptions
     * @description 옵션 셋팅.
     */
	,setOptions : function(options , firstFlag){
		var _this = this; 
		
		if($.isArray(options.tbodyItem)){
			delete _this.options.tbodyItem;
		}

		$.extend(true, _this.options, options);
		
		this.options.tbodyItem = options.tbodyItem ? options.tbodyItem : _this.options.tbodyItem;

		//_this.config.rowHeight = _this.options.rowOptions.height+1;	// border-box 수정. 2017-08-11
		_this.config.rowHeight = _this.options.rowOptions.height+1;

		_this.config.scroll = {top :0 , left:0, startCol:0, endCol:this.options.tbodyItem.length,startRow :0
			, endRow :0, updown:'', viewItemIdx : 1, height:0, hScrollMoveFlag: false,viewArea :{area0 :true , area1:false , area2:false}};
		_this.config.drawBeforeData = {}; // 이전 값을 가지고 있기 위한 객체
		
		if(_this.options.bigData === false || _this.options.bigData.enabled === false){
			_this.options.bigData ={
				enabled :false
				,gridCount : 1000
				,spaceUnitHeight : 100000
				,horizontalEnableCount : 50 
			};
		}else{
			if(_this.options.bigData.gridCount=='auto'){
				var gc = parseInt((_this.element.height() / _this.options.rowOptions.height), 10 ); 
				_this._setBigDataCount(gc + parseInt(gc/2, 10));				
			}else{
				_this._setBigDataCount(_this.options.bigData.gridCount);
			}
		}
				
		this.config.horizontalEnabled = this.options.tColItem.length > _this.options.bigData.horizontalEnableCount ? true : false; 

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
			cssStr.push('#'+_this.prefix+'pubGrid .pub-body-td{height:'+rowOptHeight+'px;padding: 0px;margin:0px;}');
			cssStr.push('#'+_this.prefix+'pubGrid .pub-content-ellipsis{height:'+(rowOptHeight-4)+'px;padding: 0px;margin:0px;}');
			cssStr.push('#'+_this.prefix+'pubGrid .pub-body-td> .pub-content{height:'+rowOptHeight+'px;}');
		}

		var styleTag = _d.createElement('style');
		
		_d.getElementsByTagName('head')[0].appendChild(styleTag);
		styleTag.setAttribute('type', 'text/css');

		if (styleTag.styleSheet) {
			styleTag.styleSheet.cssText = cssStr.join('');
		} else {
			styleTag.appendChild(document.createTextNode(cssStr.join('')));
		}
		
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
			,gridElementWidth =_this.config.gridElementWidth
			,tciItem,thgItem, rowItem, headItem
			,headGroupInfo = [],groupInfo = [], rowSpanNum = {}, colSpanNum = {};
		
		if(thg.length < 1){
			thg.push(tci);
		}
		
		var tmpThgIdx=0,tmpColIdx=0,tmpThgItem , currentColSpanIdx=0  , beforeColSpanIdx=0 ;
		var sortHeaderInfo = {};
		for(var i=0,j=0 ;i <thg.length; i++ ){
			thgItem = thg[i];
			groupInfo = [];
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
					headItem['span'] = 'scope="col"';
					headItem['label'] = headItem.label ? headItem.label : tciItem.label;
					
					if(headItem.colspan){
						headItem['colSpanIdx'] = headItem['colSpanIdx']+headItem.colspan-1;
						headItem['span'] = ' scope="colgroup" colspan="'+headItem.colspan+'" ';
						
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
						headItem['span'] = ' scope="col" rowspan="'+headItem.rowspan+'" ';
						rowSpanNum[j] = i+ headItem.rowspan -1;
					}
					beforeColSpanIdx = headItem['colSpanIdx'];
					
				}
				
				//console.log(j+' ;; '+rowSpanNum[j] +' : '+headItem.view, headItem);

				if(headItem.view==true){
					sortHeaderInfo[j] = {r:i,key:tciItem.key}
					groupInfo.push(headItem);
				}
			}
			headGroupInfo.push(groupInfo);
		}
		
		for(var _ikey in sortHeaderInfo){
			var tmpHgi = headGroupInfo[sortHeaderInfo[_ikey].r][_ikey]; 
			if(typeof tmpHgi ==='undefined') continue; 

			tmpHgi['isSort'] =(tmpHgi.sort===true?true:false); 
			headGroupInfo[sortHeaderInfo[_ikey].r][_ikey] = tmpHgi;
		}

		_this.config.headerInfo = headGroupInfo;

		var colWidth = Math.floor(gridElementWidth/tci.length);
		
		for(var j=0; j<tci.length; j++){
			var tciItem = opt.tColItem[j];

			//console.log(tciItem.width);

			tciItem.width = isNaN(tciItem.width) ? 0 :tciItem.width; 
			tciItem.width = Math.max(tciItem.width, opt.headerOptions.colMinWidth);
			
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

		//console.log(_this.config.totGridWidth)
		
		_w = _this.config.totGridWidth;
		_containerWidth = (_w+_this.config.scrollWidth);
		tciLen = tci.length;
		var totGridWidth = 0; 
		if( _containerWidth > gridElementWidth){
			_this.config.gridXScrollFlag = true;

			if(mode=='resize'){				
 				var remainderWidth = Math.floor((_containerWidth-gridElementWidth)/tciLen);

				for(var j=0; j<tciLen; j++){
					opt.tColItem[j].width -= remainderWidth;
					totGridWidth +=opt.tColItem[j].width;
				}
				totGridWidth =totGridWidth-opt.tColItem[tciLen-1].width;
				opt.tColItem[tciLen-1].width -=( (_containerWidth-gridElementWidth)%tciLen);
				totGridWidth +=opt.tColItem[tciLen-1].width;
			}else{
				totGridWidth = _w; 
			}
		}else{
			if(opt.headerOptions.colWidthFixed !== true){
				// 동적으로 width 계산할 경우 colwidth 처리.
				var _gw = gridElementWidth- _this.config.scrollWidth; 
				var remainderWidth = Math.floor((_gw -_w)/tciLen);

				for(var j=0; j<tciLen; j++){
					opt.tColItem[j].width += remainderWidth;
					totGridWidth +=opt.tColItem[j].width;
				}

				totGridWidth = totGridWidth-opt.tColItem[tciLen-1].width;
				opt.tColItem[tciLen-1].width +=( (_gw -_w)%tciLen);
				totGridWidth +=opt.tColItem[tciLen-1].width;
			}
		}

		_this.config.totGridWidth = totGridWidth;
		_this.config.height = opt.height;
		if(opt.height=='auto'){
			_this.config.height = _this.element.height();
		}
		//console.log(_this.config.gridWidth, gridElementWidth, _w );
	}
	/**
     * @method _setTbody
	 * @description 바디 데이타 셋팅
     */
	,_setTbody : function(){
		var _this = this; 
		this.options.tbodyItem = pItem;
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
		var bodyFlag= (type == 'body'); 
		if(bodyFlag){
			_this.config.gridBodyWidth = 0; 

			if(_this._isHorizontalCheck()){
				startCol=0;
				endCol=tci.length;
			}else{
				startCol =_this.config.scroll.startCol;
				endCol =_this.config.scroll.endCol+1;
			}
		}
		
		//console.log(this.config.horizontalEnabled , this.config.scroll.hScrollMoveFlag , startCol, endCol )
		
		for(var i=startCol ;i <endCol; i++){
			thiItem = tci[i];
			var tmpStyle = [];
			tmpStyle.push('width:'+thiItem.width+'px;');
			if(thiItem.hidden===true){
				tmpStyle.push('display:none;');
			}
			if(bodyFlag){
				_this.config.gridBodyWidth += thiItem.width;
			}
			strHtm.push('<col id="'+id+i+'" style="'+tmpStyle.join('')+'" />');
		}
		
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
		var pageInfo = {};
		if(!$.isArray(pdata)){
			data = pdata.items;
			pageInfo = pdata.page; 

		}

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
			_this.drawGrid('init',true);
		}else{
			_this.drawGrid('tbody',true);
		}

		if(gridMode != 'sort'){
			if(_this.config.page){
				_this.setPage(pageInfo);
			}
		}

		var itemHeight = _this.options.tbodyItem.length* _this.config.rowHeight
			,unitHeight = _this.options.bigData.spaceUnitHeight
			,loopCnt = Math.floor(itemHeight/unitHeight);
		
		var itemHeightHtm = [], topHeightHtm = [];

		for(var i =0 ;i <loopCnt ;i++ ){
			itemHeightHtm.push('<div _idx="'+i+'" style="height:'+unitHeight+'px;"></div>');
			topHeightHtm.push('<div data-top-idx="'+i+'" style="height:0px;"></div>');
		}

		if(itemHeight%unitHeight != 0){
			itemHeightHtm.push('<div style="height:'+itemHeight%unitHeight+'px;"></div>');
			topHeightHtm.push('<div data-top-idx="'+(loopCnt)+'" style="height:0px;"></div>');
			loopCnt+=1;
		}

		_this.config.scroll.spaceCount = loopCnt;
		
		_this.config.pubGridTopSpaceElement.empty().html(topHeightHtm.join(''));
		_this.config.pubGridBodyHeightElement.empty().html(itemHeightHtm.join(''));
		

	}
	,setPage : function (pageInfo){
		var _this =this; 
		_this.pageNav(pageInfo);
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

			strCss.push('#'+_this.prefix+'pubGrid .table-column-'+i+'{'+tmpStyle.join('')+'}');
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
     * @method getHeaderHtml
     * @description header html 
     */
	,getHeaderHtml : function (){
		var _this = this; 

		return '<div id="'+_this.prefix+'pubGrid-width-size" class="pubGrid-width-size" style="width:100%;height:0px;"></div>'
			+' <div class="pubGrid-wrapper">'
			+'  <div id="'+_this.prefix+'pubGrid" class="pubGrid" style="width:'+_this.config.gridElementWidth+'px;">'
			+'	<div id="'+_this.prefix+'pubGrid-container" class="pubGrid-container">'
			+'		<div id="'+_this.prefix+'pubGrid-header-wrapper" class="pubGrid-header-wrapper">'
			+'			<div id="'+_this.prefix+'pubGrid-header-container" class="pubGrid-header-container" style="display: table-row;">'
			+'				<div style="display:table-cell;"><table id="'+_this.prefix+'pubGrid-header" style="width:'+_this.config.totGridWidth+'px;" class="pubGrid-header" onselectstart="return false">'
			+'				#theaderHtmlArea#</table></div>'
			+'				<div style="width:'+(_this.config.scrollWidth+1)+'px;height:10px;display:table-cell;float:left;"></div>'
			+'			</div>'	
			+'		</div>'
			+'		<div id="pubGrid-body-wrapper" class="pubGrid-body-wrapper">'
			+'			<div id="'+_this.prefix+'pubGrid-body-scroll" class="pubGrid-body-scroll" style="height:'+_this.options.height+'px;">'
			+'			  <div id="'+_this.prefix+'pubGrid-body-container" class="pubGrid-body-container" style="style="display: table-row;"">'
			
			+'				<div id="'+_this.prefix+'pubGrid-body-height" class="pubGrid-body-height" style="width: 0px;padding:0px;display: table-cell;float:left;"></div>'
			+'				<div style="display:table-cell;float:left;position:relative;">'
			+'					<div id="'+_this.prefix+'pubGrid-body-top-space" class="pubGrid-body-top-space" style="width: 1px; padding:0px;height:100%;"></div>'
			+'					<div style="display: table-row;">'
			+'						<div id="'+_this.prefix+'pubGrid-body-left-space" style="float:left;display: table-cell;"></div>'
			+'						<div style="display: table-cell;"><table id="'+_this.prefix+'pubGrid-body" class="pubGrid-body">'
			+'							<colgroup id="'+_this.prefix+'colgroup_body"></colgroup>'
			+'						</table></div>'	
			+'					</div>'
			+'				</div>'
			+'				<div id="'+_this.prefix+'pubGrid-body-width" class="pubGrid-body-width" style="position:absolute;z-index:-1;height:1px;top:0px;padding:0px;width:'+_this.config.totGridWidth+'px;"></div>'
			+'			  </div>'
			+'			</div>'
			+'		</div>'
			+'		<div id="'+_this.prefix+'pubGrid-pageNav"></div>'
			+'		<div id="'+_this.prefix+'hiddenArea" style="display:none;"></div>'
			+'	</div>'
			+'  </div>'
			+'</div>'; 

	}
	//body html  만들기
	,getTbodyHtml : function(tbi, tci , itemIdx, tbodyIdx){
		var strHtm = [], thiItem;
		
		if(tbi.length > 0){
			var tbiItem, clickFlag = false;
			var startRow = 0
				,endRow = tbi.length
				, startCol=this.config.scroll.startCol 
				, endCol=this.config.scroll.endCol
				, itemVal;
			
			if(itemIdx !='all'){
				var itemLen = endRow; 
				startRow = (itemIdx-1) * this.options.bigData.gridCount
				endRow = itemLen> startRow ? ( itemLen > (startRow + this.options.bigData.gridCount) ? (startRow + this.options.bigData.gridCount) : itemLen) : 0;
			}

			if(this._isHorizontalCheck()){
				startCol=0;
				endCol=tci.length-1;
			}
			
			//console.log('startRow : '+startRow, 'endRow : '+endRow , 'startCol : '+startCol, 'endCol : '+endCol, 'itemIdx: '+itemIdx)
			
			var tmpVal;
						
			for(var i =startRow ; i < endRow; i++){
				tbiItem = tbi[i];
				strHtm.push('<tr class="pub-body-tr '+((i%2==0)?'tr0':'tr1')+'" rowinfo="'+i+'">');

				for(var j=startCol ;j <=endCol; j++){
					thiItem = tci[j];
					clickFlag = thiItem.colClick;
					tmpVal = this.valueFormatter( i, thiItem,tbiItem); 
					strHtm.push('<td scope="col" class="pub-body-td '+(thiItem.hidden===true ? 'pubGrid-disoff':'')+'" data-colinfo="'+i+','+j+'"><div class="pub-content '+thiItem._alignClass+'"><div class="pub-content-cell"><div class="pub-content-ellipsis '+ (clickFlag?'pub-body-td-click':'') +'" title="'+tmpVal+'" >'+tmpVal+'</div></div></div></td>');
				}
			}
		}else{
			strHtm.push('<tr><td colspan="'+tci.length+'"><div class="text-center">NO DATA</div></td></tr>');
		}

		if(!isNaN(tbodyIdx)){
			return '<tbody class="pub-cont-tbody-'+tbodyIdx+'">' +strHtm.join('')+'</tbody>';
		}else{
			return strHtm.join('');
		}

	}
	/**
     * @method valueFormatter
	 * @param  thiItem {Object} header col info
	 * @param  item {Object} row 값
     * @description foot 데이타 셋팅
     */
	,valueFormatter : function (_idx ,thiItem, rowItem){
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
			if(type == 'money'){
				itemVal = util.formatter[type](itemVal, tmpFormatter.fixed , tmpFormatter.prefix ,tmpFormatter.suffix);
			}else if(type == 'number'){
				itemVal = util.formatter[type](itemVal , tmpFormatter.fixed , tmpFormatter.prefix ,tmpFormatter.suffix);
			}
		}

		return itemVal; 
	}
	,_isHorizontalCheck : function (){
		return this.config.horizontalEnabled===true ?false : (this.config.horizontalEnabled===false && this.config.scroll.hScrollMoveFlag ===true);
	}
	,_setTbodyAppend : function (){
		
		this.config.bodyCount= this.config.bodyCount||-1;

		var viewItemCount = parseInt(this.config.gridBodyHeight/ this.options.rowOptions.height, 10 );
		var bodyCount = 2 + parseInt(viewItemCount/this.options.bigData.gridCount,10); 
		bodyCount = bodyCount < 2 ? 2 :bodyCount;
				
		if(this.config.bodyCount != bodyCount){
			
			if(this.config.bodyCount > bodyCount){
				for(var i =bodyCount+1; i<= this.config.bodyCount; i++){
					this.config.bodyElement.find('.pub-cont-tbody-'+(i)).remove();
				}
			}else{
				var bodyHtm ='';
				for(var i =this.config.bodyCount+1; i<= bodyCount; i++){
					bodyHtm += '<tbody class="pub-cont-tbody-'+i+'"></tbody>';
				}
				this.config.bodyElement.append(bodyHtm);

				
			}
			this.config.bodyCount = bodyCount; 
		}
	}
	/**
     * @method drawGrid
	 * @param  type {String} 그리드 타입.
     * @description foot 데이타 셋팅
     */
	,drawGrid : function (drawMode, unconditionallyFlag){
		var _this = this
			,opt = _this.options
			,ci = _this.config
			,tci = opt.tColItem
			,tbi = opt.tbodyItem
			,hederOpt=opt.headerOptions;
		
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

		function tbodyHtml(itemIdx, tbodyIdx){
			return _this.getTbodyHtml(tbi, tci, itemIdx, tbodyIdx);
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

		if(drawMode =='init'){
			
			_this.element.empty().html(_this.getHeaderHtml().replace('#theaderHtmlArea#',theadHtml()));
			
			_this.config.pubGridElement = $('#'+_this.prefix +'pubGrid');
			_this.config.pubGridTopSpaceElement = $('#'+_this.prefix +'pubGrid-body-top-space');
			_this.config.pubGridBodyHeightElement = $('#'+_this.prefix +'pubGrid-body-height');
			_this.config.pubGridBodyWidthElement = $('#'+_this.prefix +'pubGrid-body-width');
			_this.config.pubGridLeftSpaceElement = $('#'+_this.prefix +'pubGrid-body-left-space');
			
			_this.config.headerWrapElement = $('#'+_this.prefix +'pubGrid-header-wrapper');
			_this.config.headerContainerElement = $('#'+_this.prefix +'pubGrid-header-container');
			_this.config.headerElement = $('#'+_this.prefix +'pubGrid-header');
			_this.config.bodyElement = $('#'+_this.prefix +'pubGrid-body');
			_this.config.bodyScroll = $('#'+_this.prefix +'pubGrid-body-scroll');
			_this.config.hiddenArea = $('#'+_this.prefix +'hiddenArea');
			
			if(opt.height =='auto'){
				var bodyH = _this.config.height-_this.config.headerWrapElement.height(); 
				bodyH = bodyH > 0?bodyH : _this.config.headerWrapElement.height+5 ; 
				_this.config.bodyScroll.css('height',(bodyH)+'px');
			}
			// resize 설정
			_this._initHeaderEvent();
			_this._headerResize(hederOpt.resize.enabled);
			_this.scroll();
			_this._initBodyEvent();
			_this._setBodyEvent();
			_this.config.gridBodyHeight = _this.config.bodyScroll.height();

			_this._setTbodyAppend();
			_this.scrollColumnPosition(0,0);

			$('#'+_this.prefix+"colgroup_body").empty().html(_this._getColGroup(_this.prefix+'colbody', 'body'));
		}
	
		var scrollData = _this.config.scroll
			,viewItemIdx=scrollData.viewItemIdx
			,updown = scrollData.updown
			,bigDataGridCount = _this.options.bigData.gridCount;
		
		if(unconditionallyFlag != true && _this.config.drawBeforeData.viewItemIdx==viewItemIdx
			&& _this.config.drawBeforeData.startCol==scrollData.startCol
			&& _this.config.drawBeforeData.endCol==scrollData.endCol	
		){
			return ;
		}else{
			_this.config.drawBeforeData = {
				viewItemIdx :viewItemIdx	
				,startCol : scrollData.startCol
				,endCol : scrollData.endCol	
			}

			$('#'+_this.prefix+"colgroup_body").empty().html(_this._getColGroup(_this.prefix+'colbody', 'body'));
		}

		if(_this._isHorizontalCheck()){
			_this.config.pubGridLeftSpaceElement.css('width','0px');	
		}else{
			_this.config.pubGridLeftSpaceElement.css('width',(scrollData.leftWidth[scrollData.startCol]||0)+'px');
		}
	
		if(drawMode=='scroll'){
			if(updown =='down'){
				var topEle = _this.config.bodyElement.find('.pub-cont-tbody-0').addClass('pub-cont-tbody-temp').removeClass('pub-cont-tbody-0');

				for(var i =1 ; i <= _this.config.bodyCount; i++){
					_this.config.bodyElement.find('.pub-cont-tbody-'+(i)).addClass('pub-cont-tbody-'+(i-1)).removeClass('pub-cont-tbody-'+(i));
				}
								
				topEle.addClass('pub-cont-tbody-'+(_this.config.bodyCount)).removeClass('pub-cont-tbody-temp');
				_this.config.bodyElement.find('.pub-cont-tbody-'+(_this.config.bodyCount-1)).after(topEle);

				topEle.empty().html(tbodyHtml(viewItemIdx+_this.config.bodyCount));
			}else if(updown =='up'){

				var bottomEle = _this.config.bodyElement.find('.pub-cont-tbody-'+_this.config.bodyCount).addClass('pub-cont-tbody-temp').removeClass('pub-cont-tbody-'+_this.config.bodyCount);

				for(var i =_this.config.bodyCount-1 ; i >=0; i--){
					_this.config.bodyElement.find('.pub-cont-tbody-'+(i)).addClass('pub-cont-tbody-'+(i+1)).removeClass('pub-cont-tbody-'+(i));
				}
				
				bottomEle.addClass('pub-cont-tbody-0').removeClass('pub-cont-tbody-temp');
				_this.config.bodyElement.find('.pub-cont-tbody-1').before(bottomEle);
				bottomEle.empty().html(tbodyHtml(viewItemIdx));
			}
			
		}else if(drawMode == 'scrollH_draw' ||drawMode == 'scrollV_draw'){
			for(var i =0 ; i <= _this.config.bodyCount; i++){
				var tmpArea = 'area'+i;
				if( drawMode != 'scrollH_draw'||(drawMode=='scrollH_draw' && scrollData.viewArea[tmpArea])){
					_this.config.bodyElement.find('.pub-cont-tbody-'+i).empty().html(tbodyHtml(viewItemIdx+i));
				}
			}
		}else{
			for(var i =0 ; i <= _this.config.bodyCount; i++){
				_this.config.bodyElement.find('.pub-cont-tbody-'+i).empty().html(tbodyHtml(viewItemIdx+i));
			}
		}

		var topSpaceHeight = (viewItemIdx -1)*scrollData.itemGroupTotalHeight; 

		var spaceItemCount = Math.floor(topSpaceHeight /_this.options.bigData.spaceUnitHeight)
			,overHeight = topSpaceHeight % _this.options.bigData.spaceUnitHeight; 
				
		for(var i =0 ;i < scrollData.spaceCount; i++){
			if(i < spaceItemCount){
				_this.config.pubGridTopSpaceElement.find('[data-top-idx="'+i+'"]').css('height', _this.options.bigData.spaceUnitHeight);
			}else{
				_this.config.pubGridTopSpaceElement.find('[data-top-idx="'+i+'"]').css('height', 0);
			}
		}
		
		//console.log($('.pubGrid-body-scroll').scrollTop(), scrollData.itemGroupTotalHeight, spaceItemCount , topSpaceHeight,_this.options.bigData.spaceUnitHeight, overHeight, _this.options.bigData.spaceUnitHeight )
		
		_this.config.bodyElement.css('width',_this.config.gridBodyWidth+'px');
		_this.config.pubGridTopSpaceElement.find('[data-top-idx="'+spaceItemCount+'"]').css('height', overHeight +'px');

	}
	,scrollColumnPosition : function (sTop, sLeft, pType){
		var tci = this.options.tColItem; 
		
		var gridW = sLeft+this.config.gridElementWidth; 
		var itemLeftVal=0;
		var startCol = 0, endCol =tci.length;
		var leftWidth ={};
		if(pType === 'vscroll'){
			leftWidth = this.config.scroll.leftWidth;
			startCol = this.config.scroll.startCol;
			endCol = this.config.scroll.endCol;
		}else{
			
			for(var i =0 ;i <tci.length ;i++){
				var thiItem = tci[i];

				leftWidth[i] =itemLeftVal; 
				itemLeftVal +=thiItem.width; 
				
				//console.log(thiItem.width, itemLeftVal)
				if(itemLeftVal < sLeft){
					startCol = i-1; 
					continue; 
				}

				if( itemLeftVal >gridW){
					endCol = i+1; 
					break; 
				}
			}
		}

		var viewIdx = this.config.scroll.viewItemIdx
			,beforeHeight = this._getScrollOverHeight(viewIdx);
			
		this.config.scroll.viewArea['area0'] = sTop < beforeHeight?true:false; 

		for(var i =1 ; i <= this.config.bodyCount; i++){
			var tmpArea = 'area'+i; 
			var currHeight = this._getScrollOverHeight(viewIdx+i);
			this.config.scroll.viewArea[tmpArea] = (beforeHeight > sTop + this.config.gridBodyHeight) || (sTop > currHeight)?false : true; 
			beforeHeight = currHeight; 	
		}

		this.config.scroll.beforeTop = sTop; 
		this.config.scroll.leftWidth = leftWidth; 
		this.config.scroll.startCol = ( startCol > 0? startCol:0 ); 
		this.config.scroll.endCol = ( endCol >= tci.length? tci.length-1:endCol );
	}
	/**
     * @method scroll
     * @description 스크롤 컨트롤.
     */
	,scroll : function (){
		var _this = this
			,_conf = _this.config;
			
		_conf.bodyScroll.on("scroll", function(event) {
			event.preventDefault();
			var scrollEle = $(this);

			var scrollData = _conf.scroll
				,sTop  = scrollEle.scrollTop()
				,sLeft = scrollEle.scrollLeft();

			var updown = '';
			if(sTop > scrollData.top){
				updown = 'down';
			}else if(sTop < scrollData.top){
				updown = 'up';
			}

			var lr = '';
			if(sLeft < scrollData.left){
				lr = 'left';
			}else if(sLeft > scrollData.left){
				lr = 'right';
			}
			
			scrollData.top= sTop;
			scrollData.left= sLeft;
			scrollData.updown= updown;
			scrollData.leftright= lr;

			var currentStartCol = scrollData.startCol, currentEndCol = scrollData.endCol;
						
			if(updown == ''){
				_conf.headerWrapElement.scrollLeft(sLeft);

				_this.scrollColumnPosition(sTop , sLeft , 'hscroll');
												
				if(scrollData.startCol != currentStartCol || scrollData.endCol != currentEndCol){
					if(!_this._isHorizontalCheck() || scrollData.hScrollMoveFlag !== true){
						scrollData.hScrollMoveFlag = true;
						_this.drawGrid('scrollH_draw', true);
					}					 
				}
			}else{
				var scrollRedrawFlag = false;
				if(scrollData.hScrollMoveFlag===true){
					scrollData.hScrollMoveFlag = false; 
					_this.scrollColumnPosition(sTop , sLeft , 'vscroll');
					
					_this.drawGrid('scrollV_draw', true);
					return ; 
				}

				var viewIdx = _conf.scroll.viewItemIdx
					,topViewHeight = _this._getScrollOverHeight(viewIdx)
					,midViewHeight = _this._getScrollOverHeight(viewIdx+1)

				if(updown =='up'){
					scrollRedrawFlag = sTop < topViewHeight ?true:false;
				}else if(updown =='down'){
					scrollRedrawFlag = sTop > midViewHeight ?true:false;
				}
							
				if(scrollRedrawFlag){
					viewIdx = viewIdx + (updown=='down' ? 1 : -1);
					viewIdx = viewIdx < 1 ? 1 : viewIdx;
				
					var jumpFlag = false, scrIdx = 1; 
					var scrIdxVal = sTop/scrollData.itemGroupTotalHeight; 
					if(updown=='down'){
						
						scrIdx = Math.ceil(scrIdxVal);

						if(scrIdx - viewIdx > 1){
							scrIdx =  scrIdx > scrollData.maxViewItemIdx ? scrollData.maxViewItemIdx : scrIdx;
							jumpFlag = true; 
						}
					}else if( updown=='up'){
						scrIdx = Math.floor(scrIdxVal);
						
						if(viewIdx-scrIdx > 1){
							jumpFlag = true; 	
						}else{
							scrIdx = scrIdx -1; 
						}
					}

					//console.log('-------------	##################-------------------------------------')
					
					//if(scrIdx < 1) return ; 

					if( !jumpFlag  &&  (_conf.scroll.viewItemIdx==viewIdx || viewIdx==scrIdx)) return ; 
					
					if(jumpFlag){
						_conf.scroll.viewItemIdx = scrIdx < 1 ? 1 :scrIdx;

						_this.scrollColumnPosition(sTop , sLeft , 'vscroll');
						_this.drawGrid('scrollV_draw');
					}else{
						_conf.scroll.viewItemIdx = viewIdx;

						_this.scrollColumnPosition(sTop , sLeft , 'vscroll');
						_this.drawGrid('scroll');																	
					}
						
				}
			}

			return true; 
		});
	}
	,_setBigDataCount: function (gridCount){
		// item total height 값
		this.config.scroll.itemGroupTotalHeight = this.options.bigData.gridCount* this.config.rowHeight; 
		this.config.scroll.maxViewItemIdx = Math.ceil(this.options.tbodyItem.length / this.options.bigData.gridCount)-2;
	
	}
	,_getScrollOverHeight : function (idx , updown){
		return idx* (this.options.bigData.gridCount)* this.config.rowHeight;
	}
	/**
     * @method resizeDraw
     * @description resize 하기
     */
	,resizeDraw :function (opt){
		var _this = this;
		
		var isOpt =typeof opt==='undefined'; 
		
		_this.config.drawBeforeData.bodyHeight = _this.config.gridBodyHeight; 

		opt = $.extend(true, {width : _this.element.innerWidth(), height : _this.element.parent().height()}, (isOpt ? {} :opt));
		
		if(!isOpt){
			_this.element.css('width',opt.width);
		}

		_this._setGridWidth();
		_this.config.pubGridElement.css('width',(_this.config.gridElementWidth)+'px');
		
		if(_this.options.resizeGridWidthFixed === false){
			_this._calcElementWidth('resize');
			_this.config.headerElement.css('width',(_this.config.totGridWidth)+'px');
			_this.config.bodyElement.css('width',(_this.config.gridBodyWidth)+'px');

			_this.config.pubGridBodyWidthElement.css('width',(_this.config.gridElementWidth)+'px');

			$('#'+_this.prefix+"colgroup_head").empty().html(_this._getColGroup(_this.prefix+'colHeader'));
			
		}else{
			if(_this.options.height =='auto'){
				_this.element.css('height',opt.height);

				var bodyH = opt.height-_this.config.headerWrapElement.height(); 
				bodyH = bodyH > 0?bodyH : bodyH+5;
				_this.config.bodyScroll.css('height',(bodyH)+'px');
				_this.config.gridBodyHeight = bodyH;
				_this._setTbodyAppend();
		
				if(_this.options.bigData.enabled === true){
					setTimeout(function (){
						_this.scrollColumnPosition(_this.config.bodyScroll.scrollTop(),_this.config.bodyScroll.scrollLeft());
						_this.drawGrid('scrollV_draw');
					}, 100);
				}
			}
		}
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
		
		if(_this.config.windowResize == 'init'){
			return false; 
		}
		if(_this.options.autoResize ===false || _this.options.autoResize.enabled === false) return false; 
		
		_this.config.windowResize = 'init';
		
		var _evt = $.event,
			_special,
			resizeTimeout,
			eventName = _this.prefix+"pubgridResize"; 

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
		if(idx){
			return this.options.tbodyItem[idx]
		}else{
			return this.options.tbodyItem;
		}
	}
	/**
     * @method _initHeaderEvent
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
			
			// col select background col setting
			if($('#'+_this.prefix+'colbody'+col_idx).attr('data-sort-flag') != 'Y'){
				$(_this.config.bodyElement.find('col[data-sort-flag]')).css('background-color','inherit').removeAttr('data-sort-flag');
				$('#'+_this.prefix+'colbody'+col_idx).attr('data-sort-flag','Y');
				$('#'+_this.prefix+'colbody'+col_idx).css('background-color','#b9dfdc !important');
			}
			
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
			 ,rowClickFlag =false; 
		
		var beforeCol; 
		_this.config.bodyElement.on('click.pubgridcol','.pub-body-td',function (e){
			var sEle = $(this)
				,selCol = sEle.attr('data-colinfo').split(',')
				,selRow = selCol[0]
				,colIdx = selCol[1]
				,selItem = _this.options.tbodyItem[selRow];
			
			if(beforeCol) beforeCol.removeClass('col-active');
			sEle.addClass('col-active');

			beforeCol = sEle; 

			if($.isFunction(_this.options.tColItem[colIdx].colClick)){
				_this.options.tColItem[colIdx].colClick.call(this,colIdx,{
					r:selRow
					,c:colIdx
					,item:selItem
				});
				return false; 
			}
		});
		

		if(_this.options.rowClick !== false && typeof _this.options.rowClick == 'function'){
			rowClickFlag =true; 

			var beforeRow; 
			_this.config.bodyElement.on('click.pubgridrow','.pub-body-tr',function (e){
				var selRow = $(this)
					,rowinfo=selRow.attr('rowinfo')
					,selItem = _this.options.tbodyItem[rowinfo];
				
				if(beforeRow) beforeRow.removeClass('active');

				selRow.addClass('active');
				beforeRow = selRow; 
				
				_this.options.rowClick.call(selRow ,rowinfo , selItem);							
			});
		}
	}
	/**
     * @method _setBodyEvent
     * @description body event setting
     */
	,_setBodyEvent : function (){
		if(this.options.rowContextMenu !== false){
			$.pubContextMenu($('#'+this.prefix+'pubGrid-container .pub-body-tr'),this.options.rowContextMenu);
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
			,resizeEle = $('#'+_this.prefix+'pubGrid-header .pub-header-resizer');
		if(flag===true){
			resizeEle.css('cursor',_this.options.headerOptions.resize.cursor);
			
			resizeEle.on('touchstart.pubresizer mousedown.pubresizer',function (e){
				var oe = e.originalEvent.touches;

				_this.drag = {};
				_this.drag.pageX = oe ? oe[0].pageX : e.pageX;
				_this.drag.ele = $(this);
				_this.drag.ele.addClass('pubGrid-move-header')
				_this.drag.colspanidx = _this.drag.ele.attr('colspanidx');
				_this.drag.colHeader= $('#'+_this.prefix+'colHeader'+_this.drag.colspanidx);
				
				_this.drag.colW = _this.drag.colHeader.attr('_width')?parseInt(_this.drag.colHeader.attr('_width'),10):_this.drag.colHeader.width();
				_this.drag.gridW = _this.config.totGridWidth - _this.options.tColItem[_this.drag.colspanidx].width;
				_this.drag.gridBodyW = _this.config.gridBodyWidth - _this.options.tColItem[_this.drag.colspanidx].width;
								
				// resize시 select안되게 처리 . cursor처리 
				_$doc.attr("onselectstart", "return false");
				_this.config.hiddenArea.append("<style type='text/css'>*{cursor:" + _this.options.headerOptions.resize.cursor + "!important}</style>");

				_$doc.on('touchmove.colheaderresize mousemove.colheaderresize', function (e){
					_this.onGripDrag(e,_this);
				}).on('touchend.colheaderresize mouseup.colheaderresize mouseleave.colheaderresize', function (e){
					_this.drag.ele.removeClass('pubGrid-move-header');
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
		
		_$doc.off('touchend.colheaderresize mouseup.colheaderresize').off('touchmove.colheaderresize mousemove.colheaderresize mouseleave.colheaderresize');
		_$doc.removeAttr("onselectstart");
		_this.config.hiddenArea.empty();
		
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
			
			_this.config.totGridWidth = totalWidth; 
			_this.config.gridBodyWidth = drag.gridBodyW+w; 
			_this.options.tColItem[drag.colspanidx].width = w; 
			
			drag.colHeader.css('width',w+'px');
			drag.colHeader.attr('_width',w);
			$('#'+_this.prefix+'colbody'+drag.colspanidx).css('width',w+'px');
			
			_this.config.headerElement.css('width',(totalWidth)+'px');
			_this.config.bodyElement.css('width',(drag.gridBodyW+w)+'px');
			
			_this.config.pubGridBodyWidthElement.css('width',(totalWidth)+'px');
			drag.ele.removeAttr('style');
			
		}else{
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

		var navPosition = _this.config.page.position || 'left';
		
		var pagingInfo = _this.getPageInfo(options.totalCount , options.currPage , options.countPerPage, options.unitPage);
		
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
		strHTML.push('<div class="pubGrid-page-navigation page-'+navPosition+'"><ul>');
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
				strHTML.push(' <li><a href="javascript:" class="page-click" pageno="'+no+'">'+ no + '</a></li>');
			}
		}

		if (new Boolean(nextP_is) == true) {
			strHTML.push(' <li><a href="javascript:" class="page-click" pageno="'+nextO+'">&raquo;</a></li>');
		} else {
			if (currP == currE) {
				strHTML.push(' <li class="disabled"><a href="javascript:">&raquo;</a></li>');
			} else {
				strHTML.push(' <li><a href="javascript:" class="page-click" pageno="'+nextO+'">&raquo;</a></li>');
			}
		}
		strHTML.push('</ul></div>');

		$('#'+_this.prefix+'pubGrid-pageNav').empty().html(strHTML.join(''));
		
		$('#'+_this.prefix+'pubGrid-pageNav .page-click').on('click', function() {
			var pageno = $(this).attr('pageno');
			if (typeof _this.config.page.callback == 'function') {
				_this.config.page.callback(pageno);
			}
		});
		
		return this; 
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
		
		downloadInfo = downloadInfo.replace('<tbody></tbody>', this.getTbodyHtml(this.options.tbodyItem, this.options.tColItem,'all'));
		
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
		$(window).off(this.prefix+"pubgridResize")
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
			_cacheObject.setData(_this.options.tbodyItem , 'init');
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
