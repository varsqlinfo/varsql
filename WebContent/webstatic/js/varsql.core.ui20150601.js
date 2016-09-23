/*
**
*ytkim
*varsql ui js
 */

;(function($, window, document, VARSQL) {
"use strict";

var _ui=VARSQL.ui||{};

_ui.base ={
	dto:$.extend({}, VARSQL.datatype , {})
	,constants:$.extend({}, VARSQL.constants , {})
};

_ui.options={
	dbtype:''
	,urlPrefix:''
	,param:{}
	,hiddenArea : '#dbHiddenArea'
	,downloadForm : '#downloadForm'
	,getUriPrefix:function (uri){
		return '/'+this.urlPrefix+(uri?uri:'');
	}
};

_ui.leftDbObject ={
	options :{
		selector:'#leftDbObjectWrap'
		,active: null
		,db_object_list:[]
		,param:{}
	}
	,create:function (_opts){
		var _self = this;
		
		if(!_opts.dbtype) {
			alert('dbtype empty');
			return ;
		}
		
		$.extend(true,_ui.options, _opts);
		$.extend(true,_self.options, _opts);
		
		_self._grid();
		
		_ui.SQL.init({dbtype:_opts.dbtype});
	}
	,_grid:function (){
		var _self = this;
		
		var data = _self.options.db_object_list;
		var len = data.length; 
	
		if(len < 1) return ; 
	
		var strHtm = [];
		var item; 
		strHtm.push("<div class=\"db-list-group\">");
		for (var i=0; i<len ; i++ ){
			item = data[i];
			strHtm.push('<a href=\"javascript:;\" class=\"db-list-group-item\" obj_nm='+item+'>'+item+'</a>');
		}
		strHtm.push("</div>");
									
		$(_self.options.selector).html(strHtm.join(''));
		
		$(_self.options.selector+' .db-list-group-item').on('click', function (){
			if(_self.options.active) _self.options.active.removeClass('active');
			_self.options.active =$(this);
			_self.options.active.addClass('active');
			_ui.options.param.schema =_self.options.active.attr('obj_nm');
			_self._click(this);
		});
	}
	,_click:function (obj){
		var _self = this;
		var tmpParam = _self.options.param;
		tmpParam.schema = $(obj).attr('obj_nm');
		
		VARSQL.req.ajax({      
		    type:"POST"  
		    ,url:{gubun:VARSQL.uri.database, url:_ui.options.getUriPrefix('/serviceMenu.do')}
		    ,dataType:'json'
		    ,data:tmpParam
		    ,success:function (resData){
		    	_ui.leftDbObjectServiceMenu.create(
		    		$.extend({},{param:tmpParam} , resData)
		    	);
			}
			,error :function (data, status, err){
				VARSQL.log.error(data, status, err);
			}
			,beforeSend: _self.loadBeforeSend
			,complete: _self.loadComplete
		});
	}
};

/*
 * 왼족 메뉴 셋팅
 * 테이블 , 스키마 , view 등등 
 */
_ui.leftDbObjectServiceMenu ={
	metadataCache:{
		tableCache:{}
		,viewCache:{}
		,procedureCache:{}
		,functionCache:{}
	}
	,cacheSuffix:'Cache'
	,options :{
		selector:'#leftServiceMenu'
		,menuData:[]
		,param:{}
		,contentAreaId:'#leftServiceMenuContent'
		,metadataContentAreaWrapId:'#metadataContentAreaWrap'
		,metadataContentAreaWrapEle:null
		,metadataContentAreaId:'#metadataContentArea'
		,metadataContentAreaIdEle:null
	}
	// 왼쪽 메뉴 생성 . 
	,create: function (options){
		var _self = this; 
		
		$.extend(true,_self.options, options);
		
		_self.initElement();
	
		_self._tabs();
	}
	,initElement :function (){
		var _self = this;
		_self.options.metadataContentAreaWrapEle = $(_self.options.metadataContentAreaWrapId);
	}
	,getMetaContentWrapEle:function (){
		return this.options.metadataContentAreaWrapEle; 
	}
	// 왼쪽 상단 텝 메뉴 그리기
	,_tabs : function (){
		var _self = this; 
	
		var data = _self.options.menuData;
		var len = data.length; 
	
		if(len < 1) return ; 
		var item; 
	
		var htmStr = new Array();
		for (var i=0; i<len ; i++ ){
			item = data[i];
			htmStr.push('<li class="service_menu_tab ui-state-default ui-corner-top" contentid="'+item.contentid+'" contenturl="'+item.contentid+'"><a href="#tabs-3" class="left-menu-ui-tabs-anchor" role="presentation" tabindex="-1" id="ui-id-3">'+item.name+'</a></li>');
		}
		
		var beforeSelectContentId = _self.options.active?_self.options.active.attr('contentid'):'';
		$(_self.options.contentAreaId).empty();
		$(_self.options.selector).empty();
		$(_self.options.selector).html(htmStr.join(''));
		beforeSelectContentId=beforeSelectContentId?beforeSelectContentId:$($('.service_menu_tab')[0]).attr('contentid');
		
		$(_self.options.contentAreaId).unbind("contextmenu");
		$(_self.options.contentAreaId).bind("contextmenu", function(e){
		    return false;
		});
		
		$(_self.options.selector+' .service_menu_tab').on('click', function (){
			var sObj = $(this);
			_self._off();
			_self.options.active =sObj;
			_self._on();
			var refresh = sObj.attr('refresh')=='Y'?true:false; 
			sObj.attr('refresh','N');
			
			_self._dbObjectList(sObj, refresh);
		});
		
		$($('.service_menu_tab[contentid='+beforeSelectContentId+']')).trigger('click');
		
		_self._serviceMenuContextMenu();
	}
	,_serviceMenuContextMenu : function (){
		var _self = this; 
		context.init({preventDoubleContext: false});
		
		context.attach('.service_menu_tab', [
    		{header: 'Options'},
    		{text: 'Open', href: '#'},
    		{text: 'Open in new Window', href: '#'},
    		{divider: true},
    		{text: 'Copy', href: '#'},
    		{text: 'Dafuq!?', href: '#'}
    	]);
		
		$.contextMenu({
	        selector: '.service_menu_tab', 
	        callback: function(key, options) {
	        	var sObj = $(this); 
	        	if(key=='refresh'){
	        		sObj.attr('refresh','Y').trigger('click');
	        	}
	        },
	        items: {
	            "refresh": {"name": "새로고침"}
	        }
	    });
	}
	// 텝 메뉴 활성 지우기
	,_off : function (){
		var _self = this; 
		if(_self.options.active) _self.options.active.removeClass('ui-state-active');
	}
	// 텝메뉴 활성 시키기
	,_on : function (){
		var _self = this; 
		if(_self.options.active) _self.options.active.addClass('ui-state-active');
	}
	// 메타 데이타 케쉬된값 꺼내기
	,_getMetaCache:function (gubun, key){
		gubun =gubun+this.cacheSuffix; 
		var t =this.metadataCache[gubun][key]; 
		return t?t:null;
	}
	// 메타 데이타 셋팅하기.
	,_setMetaCache:function (gubun, key ,data){
		gubun =gubun+this.cacheSuffix; 
		this.metadataCache[gubun][key]= data;  
	}
	// 클릭시 테메뉴에 해당하는 메뉴 그리기
	,_dbObjectList:function(selObj,refresh){
		var _self = this;
		var $contentId = selObj.attr('contentid');
		
		var activeObj = $(_self.options.contentAreaId+' > #'+$contentId);
		
		$(_self.options.contentAreaId+'>'+' .show-display').removeClass('show-display');
		
		if(refresh){
			activeObj.remove();
		}else{
			if(activeObj.length > 0){
				activeObj.addClass('show-display');
				return ; 
			}
		}
		
		VARSQL.req.ajax({      
		    type:"POST"  
		    ,url:{gubun:VARSQL.uri.database, url:_self._getPrefixUri('/dbObjectList.do')}
		    ,dataType:'json'
		    ,data:$.extend(true,_self.options.param,{'gubun':$contentId}) 
		    ,success:function (resData){
		    	_self['_'+$contentId].call(_self,resData);
			}
			,error :function (data, status, err){
				VARSQL.log.error(data, status, err);
			}
			,beforeSend: _self.loadBeforeSend
			,complete: _self.loadComplete
		});
	}
	// 클릭시 텝메뉴에 해당하는 메뉴 그리기
	,_dbObjectMetadataList:function(param,callback,refresh){
		var _self = this;
		
		if(!refresh){
			var cacheData = _self._getMetaCache(param.gubun,param.name);
		
			if(cacheData){
				_self[callback].call(_self,cacheData);
				return ; 
			}
		}
		
		VARSQL.req.ajax({
		    type:"POST"
		    ,url:{gubun:VARSQL.uri.database, url:_self._getPrefixUri('/dbObjectMetadataList.do')}
		    ,dataType:'json'
		    ,data:param
		    ,success:function (resData){
		    	_self._setMetaCache(param.gubun,param.name, resData); // data cache
		    	_self[callback].call(_self,resData);
			}
			,error :function (data, status, err){
				VARSQL.log.error(data, status, err);
			}
			,beforeSend: _self.loadBeforeSend
			,complete: _self.loadComplete
		});
	}
	// 컨텍스트 메뉴 sql 생성 부분 처리 .
	,_createScriptSql :function (scriptObj){
		_ui.SQL.addCreateScriptSql(scriptObj);
	}
	// 데이타 내보내기
	,_dataExport : function (exportObj){
		_ui.SQL.exportDataDownload(exportObj);
	}
	// 테이블 정보보기.
	,_tables:function (resData){
		var _self = this;
		try{
    		var len = resData.result?resData.result.length:0;
    		var strHtm = [];
    		
    		strHtm.push('<div id="tables" class="db-metadata-area show-display">');
    		if(len >0){
    			var itemArr = resData.result;
    			var item;
    			
    			for (var i=0; i<len ; i++ ){
        			item = itemArr[i];
        			strHtm.push('<div class="db-metadata-area-item"><a href=\"javascript:;\" class="table-list-item" tbl_nm="'+item.TABLE_NAME+'">'+item.TABLE_NAME);
        			strHtm.push('<span class="pull-right text-muted small"><em>'+item.REMARKS+'</em></span>');
        			strHtm.push('</a></div>');
        		}
    		}else{
    			strHtm.push('<div class="text-center no-data">no_data</div>');
    		}
    		strHtm.push("</div>");
    		
    		$(_self.options.contentAreaId).append(strHtm.join(''));
    		
    		_self.tableContextMenu();
    		
    		$('.table-list-item').on('mousedown',function(e){
    			var sObj = $(this);
    			var refresh = sObj.attr('refresh')=='Y'?true:false; 
    			sObj.attr('refresh','N');
    			
    			$('.table-list-item.active').removeClass('active');
    			sObj.addClass('active');
    			
    			_self._dbObjectMetadataList($.extend({},_self.options.param,{'gubun':'table','name':sObj.attr('tbl_nm')}), '_tableMetadata', refresh);
    			
    			if(e.which==3){ // 1:좌클릭, 2:휠클릭, 3:우클릭
    				$(_self.options.contentAreaId).unbind("contextmenu");
    				sObj.contextMenu();
    				$(_self.options.contentAreaId).bind("contextmenu",function (){return false;});
    			}
    		});
 		}catch(e){
			VARSQL.log.info(e);
		}
	}
	// 테이블 마우스 오른쪽 버튼 처리.
	,tableContextMenu : function (){
		var _self = this; 
		$.contextMenu( 'destroy', '.table-list-item' );
		$.contextMenu({
	        selector: '.table-list-item', 
	        trigger: 'none',
	        callback: function(key, options) {
	        	
	        	var gubun='table'
	        		,tmpName = $(this).attr('tbl_nm');
	        	var cacheData = _self._getMetaCache(gubun,$(this).attr('tbl_nm'));
	        	
	        	if(key=='refresh'){
	        		$(this).attr('refresh','Y');
	        		$(this).trigger('mousedown');
	        		return ; 
	        	}
	        	
	        	if(key=='export_data'||key=='export_column'){
	        		_self._dataExport({
		        		gubun:gubun
		        		,downloadType:key
	        			,objName :  tmpName 
		        		,item : cacheData
		        	});
	        		return ; 
	        	}
	        	
	        	var sObj = options.commands[key];
	        	key = sObj.mode;
	        	
	        	_self._createScriptSql({
	        		scriptType : key
	        		,gubun : 'table'
	        		,objName :  tmpName 
	        		,item : cacheData
	        		,param_yn: sObj.param_yn
	        	});
	        },
	        items: {
	            "refresh": {"name": "새로고침"}
	            ,"sql_create": {
	                "name": "sql생성" 
	                ,"items": {
	                	"selectStar": {"name": "select *" , mode: "selectStar"}
	                    ,"select": {"name": "select column" ,mode:"select"}
	                    ,"insert": {"name": "insert" , mode:"insert"}
	                    ,"update": {"name": "update" ,mode:"update"}
	                    ,"delete": {"name": "delete" ,mode:"delete"}
	                    ,"drop": {"name": "drop" ,mode:"drop"}
	                }
	            }
	            ,"mybatis-sql_create": {
	                "name": "mybatis Sql생성" 
	                ,"items": {
	                    "mybatis_insert": {"name": "insert" ,mode:"insert" ,param_yn:'Y'}
	                    ,"mybatis_update": {"name": "update" ,mode:"update" ,param_yn:'Y'}
	                    ,"mybatis_delete": {"name": "delete" ,mode:"delete",param_yn:'Y'}
	                }
	            }
	            ,'export':{
	            	"name": "내보내기" 
	                ,"items": {
	                	"export_data": {"name": "데이타 내보내기"}
	                    ,"export_column": {"name": "컬럼정보 내보내기"}
	                }
	            }
	        }
	    });
	}
	//테이블에 대한 메타 정보 보기 .
	,_tableMetadata :function (colData){
		var _self = this;
		
		try{
    		var gridObj = {
    			data:colData.result
    			,column : [
					{ label: '컬럼명', name: 'COLUMN_NAME',width:80 },
					{ label: '데이타타입', name: 'TYPE_NAME' },
					{ label: '널여부', name: 'IS_NULLABLE',width:45},
					{ label: '키여부', name: 'KEY_SEQ',width:45},
					{ label: '설명', name: 'REMARKS',width:45}
				]
    		};
			
    		_self.setMetadataGrid(gridObj);
 		}catch(e){
			VARSQL.log.info(e);
		}
	}
	//view 정보 보기.
	,_views:function (resData){
		var _self = this;
		try{
    		var len = resData.result?resData.result.length:0;
    		var strHtm = [];
    		
    		strHtm.push('<div id="views" class="db-metadata-area show-display">');
    		if(len >0){
    			var itemArr = resData.result;
    			var item;
    			
    			for (var i=0; i<len ; i++ ){
        			item = itemArr[i];
        			strHtm.push('<a href=\"javascript:;\" class="view-list-item db-metadata-area-item" view_nm="'+item.TABLE_NAME+'">'+item.TABLE_NAME);
        			strHtm.push('<span class="pull-right text-muted small"><em>'+item.REMARKS+'</em></span>');
        			strHtm.push('</a>');
        		}
    		}else{
    			strHtm.push('<div class="text-center no-data">no_data</div>');
    		}
    		strHtm.push("</div>");
    		
    		$(_self.options.contentAreaId).append(strHtm.join(''));
    		
    		$('.view-list-item').on('click',function(){
    			var sObj = $(this);
    			$('.view-list-item.active').removeClass('active');
    			sObj.addClass('active');
    			
    			_self._dbObjectMetadataList($.extend({},_self.options.param,{'gubun':'view','name':sObj.attr('view_nm')}), '_viewMetadata');
    		});
 		}catch(e){
			VARSQL.log.info(e);
		}
	}
	// view 메타 데이타 보기.
	,_viewMetadata :function (colData){
		var _self = this;
		
		try{
    		var gridObj = {
    			data:colData.result
    			,column : [
					{ label: '컬럼명', name: 'COLUMN_NAME',width:80 },
					{ label: '데이타타입', name: 'TYPE_NAME' },
					{ label: '널여부', name: 'IS_NULLABLE',width:45},
					{ label: '키여부', name: 'KEY_SEQ',width:45},
					{ label: '설명', name: 'REMARKS',width:45}
				]
    		};
			
    		_self.setMetadataGrid(gridObj);
 		}catch(e){
			VARSQL.log.info(e);
		}
	}
	,_procedures:function (resData){
		var _self = this;
		try{
    		var len = resData.result?resData.result.length:0;
    		var strHtm = [];
    		
    		strHtm.push('<div id="procedures" class="db-metadata-area show-display">');
    		if(len >0){
    			var itemArr = resData.result;
    			var item;
    			
    			for (var i=0; i<len ; i++ ){
        			item = itemArr[i];
        			strHtm.push('<a href=\"javascript:;\" class="procedure-list-item db-metadata-area-item" procedure_nm="'+item.PROCEDURE_NAME+'">'+item.PROCEDURE_NAME);
        			strHtm.push('<span class="pull-right text-muted small"><em>'+item.REMARKS+'</em></span>');
        			strHtm.push('</a>');
        		}
    		}else{
    			strHtm.push('<div class="text-center no-data">no_data</div>');
    		}
    		strHtm.push("</div>");
    		
    		$(_self.options.contentAreaId).append(strHtm.join(''));
    		
    		$('.procedure-list-item').on('click',function(){
    			var sObj = $(this);
    			
    			$('.procedure-list-item.active').removeClass('active');
    			sObj.addClass('active');
    			
    			_self._dbObjectMetadataList($.extend({},_self.options.param,{'gubun':'procedure','name':sObj.attr('procedure_nm')}), '_procedureMetadata');
    		});
 		}catch(e){
			VARSQL.log.info(e);
		}
	}
	//테이블에 대한 메타 정보 보기 .
	,_procedureMetadata :function (colData){
		var _self = this;
		
		try{
    		var gridObj = {
    			data:colData.result
    			,column : [
					{ label: '컬럼명', name: 'COLUMN_NAME',width:80 },
					{ label: '데이타타입', name: 'TYPE_NAME' },
					{ label: '널여부', name: 'NULLABLE',width:45},
					{ label: '설명', name: 'REMARKS',width:45},
				]
    		};
			
    		_self.setMetadataGrid(gridObj);
 		}catch(e){
			VARSQL.log.info(e);
		}
	}
	,_functions:function (resData){
		var _self = this;
		try{
    		var len = resData.result?resData.result.length:0;
    		var strHtm = [];
    		
    		strHtm.push('<div id="functions" class="db-metadata-area show-display">');
    		if(len >0){
    			var itemArr = resData.result;
    			var item;
    			
    			for (var i=0; i<len ; i++ ){
        			item = itemArr[i];
        			strHtm.push('<a href=\"javascript:;\" class="function-list-item db-metadata-area-item" function_nm="'+item+'">'+item);
        			strHtm.push('<span class="pull-right text-muted small"><em></em></span>');
        			strHtm.push('</a>');
        		}
    		}else{
    			strHtm.push('<div class="text-center no-data">no_data</div>');
    		}
    		strHtm.push("</div>");
    		
    		$(_self.options.contentAreaId).append(strHtm.join(''));
    		
    		$('.function-list-item').on('click',function(){
    			var sObj = $(this);
    			
    			$('.function-list-item.active').removeClass('active');
    			sObj.addClass('active');
    			
    			//_self._dbObjectMetadataList($.extend({},_self.options.param,{'gubun':'function','name':sObj.attr('function_nm')}), '_functionMetadata');
    		});
 		}catch(e){
			VARSQL.log.info(e);
		}
	}
	// 메타 데이타 그리기.
	,setMetadataGrid :function (gridObj){
		var _self = this;
		_self.getMetaContentWrapEle().empty();
		_self.getMetaContentWrapEle().html('<table id="'+_self.options.metadataContentAreaId.replace('#', '')+'" width="100%"></table>');
		
		$(_self.options.metadataContentAreaId).jqGrid({
			data: gridObj.data,
			datatype: "local",
			width:'100%',
			height: 230,
			rowNum: 100,
			loadonce: true,
			colModel:gridObj.column,
			emptyrecords: "No records to view",
			scroll:1,
			sortorder: "asc",
			shrinkToFit:false,
			autowidth:true,
			loadtext: "Loading...",
			viewrecords: true,
			caption: ""
		});
	}
	//tab 종료 값 얻기
	,_getContentId:function (){
		return this.options.active.attr('contentid');
	}
	//db url call 할때 앞에 uri 뭍이기
	,_getPrefixUri:function (uri){
		return _ui.options.getUriPrefix(uri);
	}
};


/**
 * sql 데이타 그리드
 */
_ui.SQL = {
	sqlTextAreaObj:null
	,resultMsgAreaObj:null
	,dataGridSelectorWrapObj:null
	,options :{
		selector:'#sqlExecuteArea'
		,dataGridSelector:'#dataGridArea'
		,dataGridSelectorWrap:'#dataGridAreaWrap'
		,resultMsgAreaWrap:'#resultMsgAreaWrap'
		,limitCnt:'#limitRowCnt'
		,vconnidObj:'#vconnid'
		,active: null
		,dbtype:'db2'
		,cancel: "input,textarea,button,select,option"
		,distance: 1
		,delay: 0
	}
	//SQL ui 초기화 
	,init:function (options){
		var _self = this; 
		if(options && !options.dbtype) {
			alert('dbtype empty');
			return ;
		}
		$.extend(true,_self.options, options);
		
		_self._initEditor('text/x-sql');
		_self._initEvent();
	}
	,_initEditor : function (mime){
		var _self = this;
		var _w = $('#sqlEditorWrap').outerWidth()-5; 
		$( window ).resize(function() {
			_w = $('#sqlEditorWrap').outerWidth()-5; 
			$('#editorAreaTable').css('width', _w+'px');
			
			var gridObj = $(_self.options.dataGridSelector); 
			gridObj.setGridWidth(width, false);
	        // 그리드의 width를 div 에 맞춰서 적용
			gridObj.setGridWidth(_w , false); //Resized to new width as per windo
		});
		
		$('#editorAreaTable').css('width',_w);
		
		_self.sqlTextAreaObj = CodeMirror.fromTextArea(document.getElementById(_self.options.selector.replace('#', '')), {
			mode: mime,
			indentWithTabs: true,
			smartIndent: true,
			lineNumbers: true,
			matchBrackets : true,
			autofocus: true,
			extraKeys: {"Ctrl-Space": "autocomplete"},
			hintOptions: {tables: {
				users: {name: null, score: null, birthDate: null},
				countries: {name: null, population: null, size: null}
			}}
		});
	}
	//이벤트 초기화 
	,_initEvent :function (){
		var _self = this; 
	
		var textareaObj = $('.CodeMirror.cm-s-default');
		textareaObj.resizable({
			handles: "s"
			,minHeight:50
		});
		
		textareaObj.on('keydown',function (e) {
			var evt =window.event || e; 
			
			if(evt.ctrlKey){
				if (evt.keyCode == 13) { // keyCode 13 is Enter
					$('.sql-execue-btn').trigger('click');
					return false; // preventing default action
				}
				
				if (evt.keyCode == 70 && evt.shiftKey) { // keyCode 13 is Enter
					$('.sql-format-btn').trigger('click');
					return false; // preventing default action
				}
			}
		});
		
		$('.sql-execue-btn').on('click',function (evt){
			_self.sqlData(evt);
		});
		
		$('.sql-format-btn').on('click',function (){
			_self.sqlFormatData();
		});
	}
	//텍스트 박스 object
	,getTextAreaObj:function(){
		return this.sqlTextAreaObj; 
	}
	//data grid area
	,getDataGridWrapObj:function(){
		var _self = this; 
		
		if(_self.dataGridSelectorWrapObj==null){
			_self.dataGridSelectorWrapObj = $(_self.options.dataGridSelectorWrap );
		}
		return _self.dataGridSelectorWrapObj;
	}
	//result message area
	,getResultMsgAreaObj:function(){
		var _self = this; 
		
		if(_self.resultMsgAreaObj==null){
			_self.resultMsgAreaObj = $(_self.options.resultMsgAreaWrap);
		}
		return _self.resultMsgAreaObj; 
	}
	,getSql: function (mode){
		var _self = this;
		var textObj = _self.getTextAreaObj(); 
		
		return textObj.getSelection()
	}
	// sql 데이타 보기 
	,sqlData :function (evt){
		var _self = this;
		var sqlVal = _self.getSql();
		
		sqlVal=$.trim(sqlVal);
		if('' == sqlVal){
			sqlVal  = _self.getTextAreaObj().getValue();
		}
		
		if(''== sqlVal) return ; 
		
		var params = $.extend({},_ui.options.param , {
			'sql' :sqlVal
			,'limit' : $(_self.options.limitCnt).val()
		});
		
		VARSQL.req.ajax({      
		    type:"POST"  
		    ,url:{gubun:VARSQL.uri.database, url:'/sqlData.do'}
		    ,dataType:'json'
		    ,data:params 
		    ,success:function (resData){
		    	try{
		    				    		
		    		var resultLen = resData.length;
		    		
		    		if(resultLen < 1 ){
		    			resData.data = [{result:"데이타가 없습니다."}];
		    			resData.column =[{name:'result',index:'result', align:'center'}];
		    			_self.setGridData(resData);
		    		}else{
		    			var item;
		    			
		    			for(var i=0; i < resultLen; i++){
		    				item = resData[i];
		    				if(item.viewType=='grid'){
		    					_self.getDataGridWrapObj().empty();
		    					_self.getDataGridWrapObj().html('<table id="'+_self.options.dataGridSelector.replace('#', '')+'" width="100%"></table>');
		    					_self.setGridData(item);
		    				}
		    				
		    				_self.getResultMsgAreaObj().prepend('<div class="'+(item.resultType=='FAIL'?'error-log-message':'success-log-message')+'">'+item.resultMessage+'</div>');
		    				
		    				_self.getResultMsgAreaObj().animate({scrollTop: 0},'fast');
		    			}
		    		}
		 		}catch(e){
					VARSQL.log.info(e);
				}		             
			}
			,error :function (data, status, err){
				VARSQL.log.error(data, status, err);
			}
			,beforeSend: _self.loadBeforeSend
			,complete: _self.loadComplete
		});  
	}
	,sqlFormatData :function (){
		var _self = this;
		var sqlVal = _self.getSql('pos');
		var tmpEditor =_self.getTextAreaObj(); 
		sqlVal=$.trim(sqlVal);
		
		var startSelection , endSelection;
		
		if('' == sqlVal){
			startSelection = {line:0,ch:0};
			tmpEditor.setSelection(startSelection, {line:10000,ch:0});
			sqlVal  = tmpEditor.getValue();
		}else{
			startSelection = tmpEditor.listSelections()[0].anchor;
			endSelection = tmpEditor.listSelections()[0].head;
			
			if(startSelection.line > endSelection.line){
				startSelection  = endSelection;
			}else if(startSelection.line == endSelection.line && startSelection.ch > endSelection.ch){
				startSelection  = endSelection;
			}
		}
		
		if(''== sqlVal) return ; 
		
		var params = $.extend({},_ui.options.param , {
			'sql' :sqlVal
		});
		
		VARSQL.req.ajax({      
		    type:"POST"  
		    ,url:{gubun:VARSQL.uri.database, url:'/sqlFormat.do'}
		    ,dataType:'text'
		    ,data:params 
		    ,success:function (res){
		    	var linecnt = VARSQL.matchCount(res,_ui.base.constants.newline);
	    		tmpEditor.replaceSelection(res);
	    		tmpEditor.setSelection(startSelection, {line:startSelection.line+linecnt,ch:0});
			}
			,error :function (data, status, err){
				VARSQL.log.error(data, status, err);
			}
			,beforeSend: _self.loadBeforeSend
			,complete: _self.loadComplete
		});  
	}
	// export data download
	,exportDataDownload : function (exportInfo){
		var key = exportInfo.downloadType
		,gubun = exportInfo.gubun
		,tmpName = exportInfo.objName
		,data = exportInfo.item;
		
		var dataArr = data.result;
		var len = dataArr.length;
		
		var strHtm = [];
		strHtm.push("	<table style=\"100%\" >");
		strHtm.push("		<colgroup>");
		strHtm.push("			<col width=\"310px\" />");
		strHtm.push("			<col width=\"20px\" />");
		strHtm.push("			<col width=\"*\" />");
		strHtm.push("		</colgroup>");
		strHtm.push("		<tr>");
		strHtm.push("			<td>");
		strHtm.push("			  <div style=\"height:225px;overflow-x:hidden;overflow-y:auto;\">");
		strHtm.push("				<table style=\"vertical-align: text-top;\" class=\"table table-striped table-bordered table-hover dataTable no-footer\" id=\"dataTables-example\">");
		strHtm.push("					<thead>");
		strHtm.push("						<tr role=\"row\">");
		strHtm.push("							<th tabindex=\"0\" rowspan=\"1\" colspan=\"1\" style=\"width: 20px;\"><input type=\"checkbox\" name=\"columnCheck\" value=\"all\">all</th>");
		strHtm.push("							<th tabindex=\"0\" rowspan=\"1\" colspan=\"1\" style=\"width: 150px;\">Column</th>");
		strHtm.push("						</tr>");
		strHtm.push("					</thead>");
		strHtm.push("					<tbody class=\"dataTableContent1\">");
		var item;
		for(var i=0; i < len; i++){
			item = dataArr[i];
			strHtm.push("						<tr class=\"gradeA add\">	");
			strHtm.push("							<td class=\"\"><input type=\"checkbox\" name=\"columnCheck\" value=\""+item.COLUMN_NAME+"\"></td>	");
			strHtm.push("							<td class=\"\">"+item.COLUMN_NAME+"</td>	");
			strHtm.push("						</tr>");
		}
		strHtm.push("					</tbody>");
		strHtm.push("				</table>");
		strHtm.push("			  </div>");
		strHtm.push("			</td>");
		strHtm.push("			<td></td>");
		strHtm.push("			<td style=\"vertical-align: text-top;\">");
		strHtm.push("				<div>");
		strHtm.push("					<label class=\"control-label\">LIMIT COUNT</label>");
		strHtm.push("					<input class=\"\" id=\"exportCount\" name=\"exportCount\" value=\"1000\">");
		strHtm.push("				</div>");
		strHtm.push("				<ul>");
		strHtm.push("					<li><span><input type=\"radio\" name=\"exportType\" value=\"csv\" checked></span>CSV</li>");
		strHtm.push("					<li><span><input type=\"radio\" name=\"exportType\" value=\"json\"></span>JSON</li>");
		strHtm.push("					<li><span><input type=\"radio\" name=\"exportType\" value=\"insert\"></span>INSERT문</li>");
		strHtm.push("					<li><span><input type=\"radio\" name=\"exportType\" value=\"xml\"></span>XML</li>");
		strHtm.push("				</ul>");
		strHtm.push("			</td>");
		strHtm.push("		</tr>");
		strHtm.push("	</table>");
		
		var modalEle = $('#data-export-modal'); 
		if(modalEle.length > 0){
			modalEle.empty();
			modalEle.html(strHtm.join(''));
		}else{
			$(_ui.options.hiddenArea).append('<div id=\"data-export-modal\" title="데이타 내보내기">'+strHtm.join('')+'</div>');
			modalEle = $('#data-export-modal'); 
		}
		
		var checkAllObj = $("input:checkbox[name='columnCheck'][value='all']"); 
		checkAllObj.on('click',function (){
			VARSQL.check.allCheck($(this),"input:checkbox[name='columnCheck']");
		});
		
		checkAllObj.trigger('click');
		
		modalEle.dialog({
			height: 350
			,width: 640
			,modal: true
			,buttons: {
				"내보내기":function (){
					if(!confirm('내보내기 하시겠습니까?')) return ; 

					var params = $.extend({},_ui.options.param , {
						exportType : VARSQL.check.radio('input:radio[name="exportType"]')
						,columnInfo : VARSQL.check.getCheckVal("input:checkbox[name='columnCheck']:not([value='all'])").join(',')
						,name: tmpName
						,limit: $('#exportCount').val()
					});

					VARSQL.req.download(_ui.options.downloadForm, {
						type: 'post'
						,url: {gubun:VARSQL.uri.database, url:'/dataExport.do'}
						,params:params
					});
				}
				,Cancel: function() {
					$( this ).dialog( "close" );
				}
			}
			,close: function() {
			  $( this ).dialog( "close" );
			}
		});
	}
	// 스크립트 내보내기
	,addCreateScriptSql :function (scriptInfo){
		var _self = this;
		var key = scriptInfo.scriptType
			,gubun = scriptInfo.gubun
			,tmpName = scriptInfo.objName
			,data = scriptInfo.item
			,param_yn  = scriptInfo.param_yn;
		
		param_yn = param_yn?param_yn:'N';
		
		var reval =[];
		
		var dataArr = data.result, tmpval , item;
		
		var len = dataArr.length;
		
		reval.push(_ui.base.constants.newline); // 첫라인 줄바꿈으로 시작.
		// select 모든것.
		if(key=='selectStar'){
			reval.push('select * from '+tmpName);
			
		}
		// select 컬럼 값
		else if(key=='select'){
			reval.push('select ');
			for(var i=0; i < len; i++){
				item = dataArr[i];
				reval.push((i==0?'':',')+item.COLUMN_NAME);
			}
			
			reval.push(' from '+tmpName);

		}
		// insert 문
		else if(key=='insert'){
			reval.push('insert into '+tmpName+' (');
			var valuesStr = [];
			for(var i=0; i < len; i++){
				item = dataArr[i];
				if(i!=0){
					reval.push(',');
					valuesStr.push(',');
				}
				reval.push(item.COLUMN_NAME);
				
				valuesStr.push(queryParameter(param_yn, item.COLUMN_NAME , item.DATA_TYPE));
				
			}
			reval.push(' )'+_ui.base.constants.newline +'values( '+ valuesStr.join('')+' )');
			
		}
		// update 문
		else if(key=='update'){
			reval.push('update '+tmpName+_ui.base.constants.newline+' set ');
			
			var keyStr = [];
			var firstFlag = true; 
			
			for(var i=0; i < len; i++){
				item = dataArr[i];
				if(!firstFlag){
					reval.push(',');
				}
				
				tmpval = queryParameter(param_yn, item.COLUMN_NAME , item.DATA_TYPE);
				
				if(item.KEY_SEQ =='YES'){
					keyStr.push(item.COLUMN_NAME+ ' = '+ tmpval);
				}else{
					reval.push(item.COLUMN_NAME+ ' = '+ tmpval);
					firstFlag = false; 
				}
			}
			
			if(keyStr.length > 0) reval.push(_ui.base.constants.newline+'where '+keyStr.join(' and '));
			
		}
		// delete 문
		else if(key=='delete'){
			reval.push('delete from '+tmpName);
			
			var item;
			var keyStr = [];
			var firstFlag = true; 
			
			for(var i=0; i < len; i++){
				item = dataArr[i];
				if(!firstFlag){
					reval.push(',');
				}
				
				tmpval = queryParameter(param_yn, item.COLUMN_NAME , item.DATA_TYPE);
				
				if(item.KEY_SEQ =='YES'){
					keyStr.push(item.COLUMN_NAME+ ' = '+ tmpval);
				}
			}
			
			if(keyStr.length > 0) reval.push(_ui.base.constants.newline+'where '+keyStr.join(' and '));
			
		}
		// drop 문
		else if(key=='drop'){
			reval.push('drop table '+tmpName);
		}
		
		var insVal = reval.join('') +_ui.base.constants.querySuffix; 
		var editObj =_self.getTextAreaObj()
			,insLine = editObj.lastLine(); 
		editObj.replaceRange(insVal, CodeMirror.Pos(insLine));
		editObj.setSelection({line:insLine+1,ch:0}, {line:editObj.lastLine()+1,ch:0});
		editObj.focus();
	}
	,loadBeforeSend :function (){
		
	}
	,loadComplete :function (){
		
	}
	// 왼쪽 메뉴 생성 .
	,setGridData: function (pGridData){
		var _self = this; 
		$(_self.options.dataGridSelector).jqGrid({
			data: pGridData.data,
			datatype: "local",
			width:'100%',
			height: 230,
			rowNum: 100,
			loadonce: true,
			rowList: [100,300,500],
			colModel: pGridData.column,
			emptyrecords: "No records to view",
			scroll:1,
			sortorder: "asc",
			shrinkToFit:false,
			autowidth:true,
			loadtext: "Loading...",
			viewrecords: true,
		});
		
		var labelClick= function (){
			var sObj = $(this);
			sObj.unbind('click');
			
			try{
				$($('.ui-grid-col-header-select').parent()).html($('.ui-grid-col-header-select').val());
			}catch(e){}

			var labelHtm = '<input type="text" class="ui-grid-col-header-select" value="'+sObj.text()+'"/>';
			var labelObj = $(labelHtm);
						
			sObj.html(labelObj);

			labelObj.blur(function (){
				sObj.text(labelObj.val());
				sObj.bind('click',labelClick);
			});

			labelObj.select();
		};
		$('.ui-grid-cols-select').click(labelClick);
	}
};

_ui.progress = {
	start:function (divObj){
		try{
			var obj = $(divObj);
			
			var modalcls = divObj.replace(/^[.#]/, '');
			
			$(divObj).prepend('<div class="'+modalcls+'dialog-modal transbg" style="position:absolute;z-index:100000;text-align:center;border:1px solid;background: #CCC; filter:alpha(opacity=50); -moz-opacity:0.5; opacity: 0.5;display:table-cell;vertical-align:middle"><span><span style="font-weight:bold;background: #fff;">기다리시오....인내심을 가지고..</span></span></div>');
			
			$("."+modalcls +'dialog-modal > span').css('line-height',obj.outerHeight() +'px');
			$("."+modalcls +'dialog-modal').css('width',obj.outerWidth());
			$("."+modalcls +'dialog-modal').css('height',obj.outerHeight());
			$("."+modalcls +'dialog-modal').show();
		}catch(e){
			alert(e);
		}
	},
	end :function (divObj){
		try{
			$('.'+divObj.replace(/^[.#]/, '') +'dialog-modal').hide();
		}catch(e){
			alert(e);
		}
	}
};

function queryParameter(flag, colName, dataType){
	if(flag=='Y'){
		return _ui.base.constants.queryParameterPrefix+colName+_ui.base.constants.queryParameterSuffix;
	}else{
		return _ui.base.dto[dataType].val; 
	}
}

VARSQL.ui = _ui;
}(jQuery, window, document,VARSQL));