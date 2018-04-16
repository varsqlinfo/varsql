/*
**
*ytkim
*varsql ui js
 */
;(function($, window, document, VARSQL) {
"use strict";

VARSQL.ui = VARSQL.ui||{};

var _ui = {};

_ui.base ={
	mimetype : ''	// editor mime type
	,sqlHints :{}	// sql hints
};

var _defaultOptions = {
	tablesConfig :{
			configVal :{search :{			// 검색
				field : ''		// 검색 필드
				,val : ''		// 검색어
			}
			,speed :-1			// scroll speed
		}
	}
}

var _g_options={
	dbtype:''
	,urlPrefix:''
	,param:{}
	,hiddenArea : '#dbHiddenArea'
	,downloadForm : '#downloadForm'
	,getUriPrefix:function (uri){
		return '/'+this.urlPrefix+(uri?uri:'');
	}
	,_opts :{}
};

VARSQL.ui.create = function (_opts){
	
	VARSQLCont.init(_opts.dbtype , _ui.base);
	
	_g_options._opts =_opts;
	_ui.initContextMenu();
	_ui.headerMenu.init(_opts);
	_ui.leftDbObject.create(_opts);
	_ui.layout.init(_opts);
	
}

//context menu 초기화
_ui.initContextMenu  = function (){
	
	if (document.addEventListener) { // IE >= 9; other browsers
        document.addEventListener('contextmenu', function(e) {
            e.preventDefault();
        }, false);
    } else { // IE < 9
        document.attachEvent('oncontextmenu', function(e) {
            window.event.returnValue = false;
        });
    }
	
	$(document).on('keydown',function (e) {
		var evt =window.event || e; 
		if(evt.ctrlKey){
			var returnFlag = true; 
			switch (evt.keyCode) {
				case 83: // keyCode 83 is s
					$('.sql_save_btn').trigger('click');
					returnFlag = false;
					break;
				case 70: // 80 is f
					returnFlag = false;
					break; 
				case 80: // 70 is f
					returnFlag = false;
					break; 
				default:
					break;
			}
			return returnFlag; 
		}
		return true; 
	});
}

// 환경 설정 관련
_ui.preferences= {
	save : function (prefInfo){
		
		prefInfo = VARSQL.util.objectMerge(_g_options._opts.screenSetting,prefInfo);
		
		var param = {
			conuid : _g_options.param.conuid
			,prefKey : 'main.database.setting'
			,prefVal : JSON.stringify(prefInfo)
		}
		VARSQL.req.ajax({      
			url:{gubun:VARSQL.uri.database, url:'/preferences/save.vsql'}
			,data: param
			,success:function (resData){
				//console.log(resData);
			}
		});
	}
}

// header 메뉴 처리.
_ui.headerMenu ={
	preferencesDialog : ''
	,init : function(){
		var _self = this;
		
		_self.initEvt();
	}
	,initEvt : function (){
		var _self = this;
		
		// header menu dropdown  init
		$('.header-menu-top-label').headerDropdown();
		
		$('.db-header-menu-wrapper').on('click', '.header-menu-item', function (e){
			var dataMenuItem = $(this).attr('data-menu-item');
			var menuArr = dataMenuItem.split('_');
			
			var depth1 =menuArr[0]
				,menu_mode2 =menuArr[1]
				,menu_mode3 = menuArr.length > 2 ?menuArr[2] :''; 
			
			$(document).trigger('click.bs.header-dropdown.data-api');
			
			switch (depth1) {
				case 'file': {
					
					switch (menu_mode2) {
						case 'new':	// 새파일
							$('.sql_new_file').trigger('click');
							break;
						case 'save': // 저장
							$('.sql_save_btn').trigger('click');
							break;
						case 'close': // 닫기
							var isInIFrame = (window.location != window.parent.location);
							if(isInIFrame==true){
								parent.userMain.activeClose();
							}else {
								if(confirm('창을 닫으시겠습니까?')){
									window.close();
								}
							}
							
							break;
						default:
							break;
					}
					
					break;
				}case 'edit':{
					switch (menu_mode2) {
						case 'undo':	// 취소
							_ui.SQL.getTextAreaObj().undo();
							break;
						case 'redo':	// 살리기
							_ui.SQL.getTextAreaObj().redo();
							break;
						case 'compare': //비교
							alert('['+menu_mode2+'] 준비중입니다.');
							break;
						default:
							break;
					}
					break;
				}case 'tool':{           
					switch (menu_mode2) {
						case 'import':	//가져오기
							alert('['+menu_mode2+'] 준비중입니다.');
							break;
						case 'export':	//내보내기
							_self.exportInfo(menu_mode3);
							break;
						case 'setting':	//설정.
							_self.openPreferences('설정',VARSQL.getContextPathUrl('/database/preferences/main.vsql?conuid='+_g_options.param.conuid));
							break;
						default:
							break;
					}
					break;
				}case 'help':{           
					switch (menu_mode2) {
						case 'help':	//도움말
							alert('['+menu_mode2+'] 준비중입니다.');
							break;
						case 'info':	//정보 보기.
							alert('['+menu_mode2+'] 준비중입니다.');
							break;
						default:
							break;
					}
					break;
				}
				default:
					break;
			}
		})
	}
	//header 메뉴 환경설정처리.
	,openPreferences : function (title , loadUrl){
		var _self = this; 
		
		if(_self.preferencesDialog ==''){
			_self.preferencesDialog = $('#preferencesTemplate').dialog({
				height: 420
				,width: 700
				,modal: true
				,buttons: {
					Ok:function (){
						_self.preferencesDialog.dialog( "close" );
					}
					,Cancel: function() {
						_self.preferencesDialog.dialog( "close" );
					}
				}
				,close: function() {
					_self.preferencesDialog.dialog( "close" );
				}
			});
		}
		
		var iframeEle =$($('#preferencesTemplate').find('iframe')); 
		
		if(iframeEle.attr('src') != loadUrl){
			iframeEle.attr('src', loadUrl);
		}
		
		_self.preferencesDialog.dialog("open").parent().find('.ui-dialog-title').html(title);
	}
	// 데이타 내보내기.
	,exportInfo :function (type){
		var _self = this; 
		
		if(type=='spec'){
			_self.openPreferences('명세서 내보내기',VARSQL.getContextPathUrl('/database/tools/export/main?conuid='+_g_options.param.conuid));
		}else{
			
		}
	}
}

// main layout 처리.
_ui.layout = {
	layoutObj :{
		mainLayout :false
		, leftLayout : false
		, rightLayout : false
	}
	,init : function(_opts){
		var _self = this; 
		_self.setLayout();
		_self.initEvt();
	}	
	,initEvt : function (){
		
	}
	,setLayout: function (){
		var _self = this; 
		_self.layoutObj.mainLayout = $('body').layout({
			inset: {
				top:	0
			,	bottom:	0
			,	left:	1
			,	right:	1
			}
			,center__paneSelector:	".ui-layout-center-area"
			, west__paneSelector:	".ui-layout-left-area"
			, north__paneSelector: ".ui-layout-header-area"
			, west__size:	300 
			, spacing_open:			5 // ALL panes
			, spacing_closed:   8 // ALL panes
			, north__spacing_open: 0
			, resizerDblClickToggle: false
			, center__maskIframesOnResize: true	
			, center__onresize: function (obj1, obj2 ,obj3 ,obj4 ,obj5){
				_self.layoutObj.rightLayout.resizeAll(obj1, obj2 ,obj3 ,obj4 ,obj5);
			} 
			, west__onresize: function (obj1, obj2 ,obj3 ,obj4 ,obj5){
				_self.layoutObj.leftLayout.resizeAll(obj1, obj2 ,obj3 ,obj4 ,obj5);
			}
		});
		
		_self.layoutObj.leftLayout = $('div.ui-layout-left-area').layout({
			north__paneSelector: ".ui-layout-left-top-area"
			, center__paneSelector: ".ui-layout-left-middle-area"
			, south__paneSelector: ".ui-layout-left-bottom-area"
			, north__size:    38
			, north__resizable: false
			, north__spacing_open: 0
			, south__size:    150
			, spacing_open:   5  // ALL panes  //0 일경우 버튼 사라짐.
			, spacing_closed:   8  // ALL panes
			, resizerDblClickToggle: false
			, center__onresize_end:  function (obj1, obj2 ,obj3 ,obj4 ,obj5){
				try{
					if($('.db-metadata-area.show-display').length > 0){
						
						// tab 리지이즈						
						$.pubTab(_ui.leftDbObjectServiceMenu.options.selector).setWidth(obj3.layoutWidth);
						
						$.pubGrid(_ui.leftDbObjectServiceMenu.options.left_service_menu_contentId+'>#'+$('.db-metadata-area.show-display').attr('id')).resizeDraw({width:obj3.layoutWidth,height:obj3.layoutHeight-25});
					}
				}catch(e){
					console.log(arguments)
				}
			}
			,south__onresize_end :  function (obj1, obj2 ,obj3 ,obj4 ,obj5){
				try{
					if($('.varsql-meta-cont-ele.on').length > 0){
						$.pubGrid('#'+$('.varsql-meta-cont-ele.on').attr('id')).resizeDraw({width:obj3.resizerLength,height:obj3.layoutHeight});
					}
				}catch(e){
					console.log(arguments)
				}
			}
		});

		_self.layoutObj.rightLayout = $('div.ui-layout-center-area').layout({
			north__paneSelector: ".inner-layout-toolbar-area"
			, center__paneSelector: ".inner-layout-sql-editor-area"
			, south__paneSelector: ".inner-layout-result-area"
			, north__size:    65
			, north__resizable: false
			, south__size:    100 
			, spacing_open:   5  // ALL panes  //0 일경우 버튼 사라짐.
			, spacing_closed:   8  // ALL panes
			, north__spacing_open: 0
			, south__maskIframesOnResize: true	
			, resizerDblClickToggle: false
			, center__onresize:  function (obj1, obj2 ,obj3 ,obj4 ,obj5){
				setSqlEditorHeight (obj3.layoutHeight)
			}
			,south__onresize_end :  function (obj1, obj2 ,obj3 ,obj4 ,obj5){
				try{
					var containerH = obj3.css.height-25;  
				
					if($('#dataGridArea .pubGrid-body-container').length > 0){
						$.pubGrid(_ui.SQL.options.dataGridSelector).resizeDraw({width:obj3.resizerLength,height:containerH});
						
						if(typeof $.pubGrid(_ui.SQL.options.dataColumnTypeSelector)!=='undefined' && $.isFunction($.pubGrid(_ui.SQL.options.dataColumnTypeSelector).resizeDraw)){
							$.pubGrid(_ui.SQL.options.dataColumnTypeSelector).resizeDraw({width:obj3.resizerLength,height:containerH});
						}
					}
				}catch(e){
					console.log(e)
				}
			}
		});
		
		function setSqlEditorHeight (_h){
			_ui.SQL.sqlEditorEle.css('height' ,_h);
		}
		setSqlEditorHeight($('#editorAreaTable').height())
		_ui.SQL.sqlTextAreaObj.refresh();
	}
}

// 왼쪽 영역 처리.
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
		
		$.extend(true,_g_options, _opts);
		$.extend(true,_self.options, _opts);
		
		_self._grid();
		_ui.SQL.init({dbtype:_opts.dbtype});
		
		_self.initEvt();
		
		_self._userSettingInfo();
	}
	// init left event 
	,initEvt : function (){
		var _self = this;
		
		// schema refresh button 
		$('.refresh-schema-btn').on('click', function (e){
			if(_self.options.active){
				_self._click(_self.options.active, true);
			}
		})
	}
	// db schema 그리기
	,_grid:function (){
		var _self = this;
		
		var data = _self.options.db_object_list;
		var len = data.length; 
	
		if(len < 1) return ; 
	
		var strHtm = [];
		var item; 
		for (var i=0; i<len ; i++ ){
			item = data[i];
			strHtm.push('<li><a href=\"javascript:;\" class=\"db-list-group-item\" obj_nm='+item+'>'+item+'</a></li>');
		}
									
		$(_self.options.selector).html(strHtm.join(''));
		
		if(len > 1){
			$('.db-schema-list-btn').show();
		}
		
		$(_self.options.selector+' .db-list-group-item').on('click', function (){
			if(_self.options.active) _self.options.active.removeClass('active');
			_self.options.active =$(this);
			_self.options.active.addClass('active');
			_g_options.param.schema =_self.options.active.attr('obj_nm');
			$('#varsql_schema_name').html(_g_options.param.schema);
			_self._click(this);
		});
		
		$(_self.options.selector+' .db-list-group-item[obj_nm="'+_g_options.schema+'"]').trigger('click');
		
	}
	// 사용자 셋팅 정보 가져오기.
	,_userSettingInfo : function (){
		var _self = this;
		var params = _g_options.param;
		
		VARSQL.req.ajax({      
		    loadSelector : '#db-page-wrapper'
		    ,url:{gubun:VARSQL.uri.sql, url:'/base/userSettingInfo.varsql'}
		    ,data:params 
		    ,success:function (res){
		    	var sqlInfo = res.item;
		    	if(sqlInfo){
		    		_ui.SQL.setQueryInfo(sqlInfo);
		    	}else{
		    		$('#saveSqlTitle').val(VARSQL.util.dateFormat(new Date(), 'yyyymmdd')+'query');
		    	}
			}
		});  
	}
	// 스키마 클릭. 
	,_click:function (obj , refreshFlag){
		var _self = this;
		var tmpParam = _self.options.param;
		tmpParam.schema = $(obj).attr('obj_nm');
		
		if(refreshFlag){
			_ui.leftDbObjectServiceMenu.create({param:tmpParam});
			return ; 
		}
		
		VARSQL.req.ajax({      
		    loadSelector : _ui.leftDbObjectServiceMenu.options.left_service_menu_contentId
		    ,url:{gubun:VARSQL.uri.database, url:'/serviceMenu.varsql'}
		    ,data:tmpParam
		    ,success:function (resData){
		    	_ui.leftDbObjectServiceMenu.create(
		    		$.extend({},{param:tmpParam} ,{menuData: resData.items})
		    	);
			}
		});
	}
};

/*
 * 왼족 메뉴 셋팅
 * 테이블 , 스키마 , view 등등 
 */
_ui.leftDbObjectServiceMenu ={
	initFlag : false
	,metadataCache:false
	,metaGridHeight :150
	,options :{
		selector:'#leftServiceMenu'
		,menuData:[]
		,param:{}
		,left_service_menu_contentId:'#left_service_menu_content'
		,metadata_content_area_wrapId:'#metadata_content_area_wrap'
		,metadata_content_area_wrapEle:null
		,metadata_content_areaId:'#metadata_content_area'
		,metadata_content_areaIdEle:null
	}
	,_metaCacheGubun : ''
	// 왼쪽 메뉴 생성 . 
	,create: function (options){
		var _self = this; 
		_self._initCacheObject();
		
		_self.options = VARSQL.util.objectMerge(_self.options, options);
		
		if(_self.initFlag ===false){
			_self._tabs();
			$($('.service_menu_tab')[0]).trigger('click');
		}else{
			$($('.service_menu_tab')[0]).attr('refresh','Y').trigger('click');
		}
		
		_self.initElement();
		_self.initFlag = true; 
	}
	,_initCacheObject : function (){
		this.metadataCache = {
			'table':{}
			,'view':{}
			,'procedure':{}
			,'function':{}
		}
		
		this.selectMetadata = {}; // 선택한 메뉴 
	}
	,initElement :function (){
		var _self = this;
		_self.options.metadata_content_area_wrapEle = $(_self.options.metadata_content_area_wrapId);
	}
	,getMetaContentWrapEle:function (){
		return this.options.metadata_content_area_wrapEle; 
	}
	// 왼쪽 상단 텝 메뉴 그리기
	,_tabs : function (){
		var _self = this; 
	
		var data = _self.options.menuData;
		var len = data.length; 
	
		if(len < 1) return ; 
		var item; 
		
		$(_self.options.left_service_menu_contentId).empty();
		$(_self.options.selector).empty();
		
		$.pubTab(_self.options.selector,{
			items :data
			,width:'auto'
			,addClass :'service_menu_tab'
			,click : function (item){
				var sObj = $(this);
				
				var refresh = sObj.attr('refresh')=='Y'?true:false; 
				sObj.attr('refresh','N');
				_self._dbObjectList(item, refresh);
			}
		})
		
		_self._serviceMenuContextMenu();
	}
	,_serviceMenuContextMenu : function (){
		var _self = this;
		
		$.pubContextMenu($('.service_menu_tab'), {
			items:[
			       {key:'refresh' ,name: '새로고침'},
    		]
			,callback:function (key){
	    		var sObj = this.element;
	    		
	        	if(key=='refresh'){
	        		_self._removeMetaCache();
	        		sObj.attr('refresh','Y').trigger('click');
	        	}
	    	}
		});
	}
	// 메타 데이타 케쉬된값 꺼내기
	,_getMetaCache:function (gubun, key){
		var t =this.metadataCache[gubun][key]; 
		return t?t:null;
	}
	// 메타 데이타 셋팅하기.
	,_setMetaCache:function (gubun, key ,data){
		this.metadataCache[gubun][key]= data;  
	}
	,_removeMetaCache:function (gubun, key){
		
		if(typeof gubun !='undefined' && typeof key != 'undefined'){
			delete this.metadataCache[gubun][key];  
		}else if(typeof gubun !='undefined'){
			this.metadataCache[gubun] ={};
		}else{
			this._initCacheObject();
		}
	}
	// 클릭시 텝메뉴에 해당하는 메뉴 그리기
	,_dbObjectList:function(selObj,refresh){
		var _self = this;
		var $contentId = selObj.contentid;
		
		var activeObj = $(_self.options.left_service_menu_contentId+'>#'+$contentId);
		
		$(_self.options.left_service_menu_contentId+'>'+' .show-display').removeClass('show-display');
		
		// 현재 창 사이즈 구하기.
		var dimension = {width:$(_self.options.left_service_menu_contentId).width() , height:$(_self.options.left_service_menu_contentId).height()};
		
		if(activeObj.length > 0){
			activeObj.addClass('show-display');
			
			if(refresh){
				//activeObj.empty();
			}else{
				$.pubGrid(_self.options.left_service_menu_contentId+'>#'+$contentId).resizeDraw(dimension);
				return ; 
			}
		}else{
			$(_self.options.left_service_menu_contentId).append('<div id="'+$contentId+'" class="db-metadata-area show-display"></div>');
		}
		
		VARSQL.req.ajax({      
			loadSelector : '.ui-layout-left-middle-area' //_self.options.left_service_menu_contentId
			,url:{gubun:VARSQL.uri.database, url:'/dbObjectList.varsql'}
			,data:$.extend(true,_self.options.param,{'gubun':$contentId}) 
			,success:function (resData){
				_self['_'+$contentId].call(_self,resData);
			}
		});
	}
	// 클릭시 텝메뉴에 해당하는 메뉴 그리기
	,_dbObjectMetadataList:function(param,callMethod,refresh){
		var _self = this;
		
		if(!refresh){
			var cacheData = _self._getMetaCache(param.gubun,param.objectName);
		
			if(cacheData){
				_self[callMethod].call(_self,cacheData, param);
				return ; 
			}
		}
		
		VARSQL.req.ajax({
			loadSelector : _self.options.metadata_content_area_wrapId
			,url:{gubun:VARSQL.uri.database, url:'/dbObjectMetadataList.varsql'}
			,async:false
			,data:param
			,success:function (resData){
				_self._setMetaCache(param.gubun,param.objectName, resData); // data cache
				_self[callMethod].call(_self,resData, param);
			}
		});
	}
	// 컨텍스트 메뉴 sql 생성 부분 처리 .
	,_createScriptSql :function (scriptObj){
		_ui.SQL.addCreateScriptSql(scriptObj);
	}
	,_createJavaProgram: function (scriptObj){
		_ui.JAVA.createJavaProgram(scriptObj);
	}
	/**
	 * @method _createDDL
	 * @param name 
	 * @param val 
	 * @param options 
	 * @description create ddl
	 */	
	,_createDDL :function (sObj){
		var _self = this; 
		
		var param =$.extend({},_self.options.param,{'gubun':sObj.gubun ,objectName:sObj.objName})
		
		VARSQL.req.ajax({
			url:{gubun:VARSQL.uri.database, url:'/createDDL.varsql'}
			,data:param
			,success:function (resData){
				if(sObj.gubunKey=='ddl_copy'){
					_ui.text.copy(resData.item);
				}else{
					_ui.SQL.addSqlEditContent(resData.item);
				}
			}
		});
	}
	// 데이타 내보내기
	,_dataExport : function (exportObj){
		_ui.SQL.exportDataDownload(exportObj);
	}
	// 테이블 정보보기.
	,_table : function (resData, reqParam){
		var _self = this;
		try{
    		var len = resData.items?resData.items.length:0;
    		var strHtm = [];
    		
			var itemArr = resData.items;
			var item;
			
			var tableHint = {};
			$.each(itemArr , function (_idx, _item){
				var tblName =_item.name;
				var colList = _item.colList; 
				
				var colArr = [];
				$.each(colList , function (j , colItem){
					colArr.push(colItem.name);
				});
				
				tableHint[tblName] = {
					columns:colArr
					,text :tblName
				};
				
				_self._setMetaCache('table',tblName, {items:colList});
				
			})
						
			// 테이블 hint;
			VARSQLHints.setTableInfo(tableHint);
			
			var tableObj = $.pubGrid(_self.options.left_service_menu_contentId+'>#table',{
				height:'auto'
				,autoResize :false
				,headerOptions:{}
				,setting : {
					enable : true
					,click : false
					,enableSearch : true
					,enableSpeed : true
					,callback : function (data){
						_ui.preferences.save({tablesConfig : data.item});
						_g_options._opts.screenSetting.tablesConfig = data.item;
					}
					,configVal : _g_options._opts.screenSetting.tablesConfig
				}
				,asideOptions :{
					lineNumber : {enable : true	,width : 30	,styleCss : 'text-align:right;padding-right:3px;'}				
				}
				,page :false
				,tColItem : [
					{key :'name', label:'Table', width:200, sort:true}
					,{key :'remarks', label:'설명', sort:true}
				]
				,tbodyItem :itemArr
				,rowOptions :{
					click : function (idx, item){
						var sObj = $(this);
						
		    			var refresh = sObj.attr('refresh')=='Y'?true:false; 
		    			sObj.attr('refresh','N');
		    			
		    			$('.table-list-item.active').removeClass('active');
		    			sObj.addClass('active');
		    			
		    			_self._dbObjectMetadataList($.extend({},_self.options.param,{'gubun':'table','objectName':item.name}), '_tableMetadata', refresh);
					}
					,contextMenu :{
						beforeSelect :function (){
							$(this).trigger('click');
						}
						,callback: function(key,sObj) {
							var ele = this.element, sItem = this.gridItem;
							var gubun='table'
								,tmpName = sItem.name;
							
							if(key=='dataview'){
								_ui.SQL._sqlData('select * from '+tmpName,false);
								return ; 
							}
							
							if(key=='refresh'){
								_self._removeMetaCache(gubun,tmpName);
								ele.attr('refresh','Y');
								ele.trigger('click.pubgridrow');
								return ; 
							}
							
							if(key=='settingBtn'){
								tableObj.toggleSettingArea();
								return ; 
							}
							
							if(key =='copy'){
								tableObj.copyData();
								return ; 
							}
							
							var cacheData = _self._getMetaCache(gubun,tmpName);
							
							if(key=='ddl_copy' || key=='ddl_paste'){
								_self._createDDL({
									gubunKey : key
									,gubun : 'table'
									,objName :  tmpName 
									,item : cacheData
								});
								return ;
							}
							
							if(key=='export_data'){
								_self._dataExport({
									gubun:gubun
									,gubunKey :key
									,objName :  tmpName 
									,item : cacheData
								});
								return ;
							}
							
							if(key=='java_camel_case_naming'|| key=='java_json' || key =='java_valid'){
								_self._createJavaProgram({
									gubun:gubun
									,gubunKey :key
									,objName :  tmpName 
									,item : cacheData
								});
								return ;
							}
							
							key = sObj.mode;
							
							_self._createScriptSql({
								gubunKey : key
								,gubun : 'table'
								,objName :  tmpName 
								,item : cacheData
								,param_yn: sObj.param_yn
							});
						},
						items: [
							
							{key : "dataview" , "name": "데이타 보기"}
							,{key : "copy" , "name": "복사"}
							,{divider:true}
							,{key : "sql_create", "name": "sql생성" 
								,subMenu: [
									{ key : "selectStar","name": "select *" , mode: "selectStar"}
									,{ key : "select","name": "select column" ,mode:"select"}
									,{ key : "insert","name": "insert" , mode:"insert"}
									,{ key : "update","name": "update" ,mode:"update"}
									,{ key : "delete","name": "delete" ,mode:"delete"}
									,{ key : "drop","name": "drop" , mode:"drop"}
								]
							}
							,{key : "create_java","name": "java 모델생성" 
								,subMenu:[
									{key : "java_camel_case_naming","name": "Camel case naming"}
									,{key : "java_json","name": "json형식"}
									,{key : "java_valid","name": "우효성 체크 Bean"}
									]
							}
							,{key : "mybatis-sql_create","name": "mybatis Sql생성" 
								,subMenu : [
									{ key : "mybatis_insert","name": "insert" ,mode:"insert" ,param_yn:'Y'}
									,{ key : "mybatis_update","name": "update" ,mode:"update" ,param_yn:'Y'}
									,{ key : "mybatis_delete","name": "delete" ,mode:"delete",param_yn:'Y'}
									,{ key : "mybatis_insert_camel_case","name": "insertCamelCase" ,mode:"insert|camel" ,param_yn:'Y'}
									,{ key : "mybatis_update_camel_case","name": "updateCamelCase" ,mode:"update|camel" ,param_yn:'Y'}
									,{ key : "mybatis_delete_camel_case","name": "deleteCamelCase" ,mode:"delete|camel",param_yn:'Y'}
								]
							}
							,{divider:true}
							,{key : "create_ddl_top","name": "DDL 보기" 
								,subMenu:[
									{key : "ddl_copy","name": "복사하기"}
									,{key : "ddl_paste","name": "edit 영역에보기"}
								]
							}
							,{key :'export', "name": "내보내기" 
								,subMenu:[
									{key : "export_data","name": "데이타 내보내기"}
								]
							}
							,{divider:true}
							,{key : "refresh" , "name": "새로고침"}
							,{divider:true}
							,{key : "settingBtn" , "name": "설정(활성/비활성)"}
						]
					}
				}
			});
 		}catch(e){
			VARSQL.log.info(e);
		}
	}
	//테이블에 대한 메타 정보 보기 .
	,_tableMetadata :function (colData ,reqParam){
		var _self = this;
		
		try{
			var items = colData.items;
			
			var colArr = [];
			$.each(items , function (i , item){
				colArr.push(item.name);
			});
			
			VARSQLHints.setTableColumns(reqParam.objectName ,colArr);
			
    		var gridObj = {
    			data:items
    			,column : [
					{ label: '컬럼명', key: 'name',width:80 },
					{ label: '데이타타입', key: 'typeAndLength' },
					{ label: '널여부', key: 'nullable',width:45},
					{ label: 'Key', key: 'constraints',width:45},
					{ label: '설명', key: 'comment',width:45}
				]
    		};
			
    		_self.selectMetadata['table'] = reqParam.objectName;
    		_self.setMetadataGrid(gridObj, 'table');
 		}catch(e){
			VARSQL.log.info(e);
		}
	}
	//view 정보 보기.
	,_view:function (resData ,reqParam){
		var _self = this;
		try{

			var len = resData.items?resData.items.length:0;
    		var strHtm = [];
    		
			var itemArr = resData.items;
			var item;
			
			var tableHint = {};
			$.each(itemArr , function (_idx, _item){
				var tblName =_item.name;
				var colList = _item.colList; 
				
				var colArr = [];
				$.each(colList , function (j , colItem){
					colArr.push(colItem.name);
				});
				
				tableHint[tblName] = {
					columns:colArr
					,text :tblName
				};
				
				_self._setMetaCache('view',tblName, {items:colList});
				
			})
						
			// 테이블 hint;
			VARSQLHints.setTableInfo(tableHint);
			
			$.pubGrid(_self.options.left_service_menu_contentId+'>#view',{
				headerView:true
				,height:'auto'
				,asideOptions :{
					lineNumber : {enable : true	,width : 30	,styleCss : 'text-align:right;padding-right:3px;'}				
				}
				,autoResize :false
				,page :false
				,tColItem : [
					{key :'name', label:'Table', width:200, sort:true}
					,{key :'remarks', label:'설명', sort:true}
				]
				,tbodyItem :itemArr
				,rowOptions : {
					click : function (idx, item){
						var sObj = $(this);
		    			$('.view-list-item.active').removeClass('active');
		    			sObj.addClass('active');
		    			
		    			_self._dbObjectMetadataList($.extend({},_self.options.param,{'gubun':'view','objectName':item.name}), '_viewMetadata');
					}
					,contextMenu :{
						beforeSelect :function (){
							$(this).trigger('click');
						}
						,callback: function(key,sObj) {
							var ele = this.element, sItem = this.gridItem;
							var gubun='view'
								,tmpName = sItem.name;
							
							var cacheData = _self._getMetaCache(gubun,tmpName);
							
							if(key=='ddl_copy' || key=='ddl_paste'){
								_self._createDDL({
									gubunKey : key
									,gubun : 'view'
									,objName :  tmpName 
								});
								return ;
							}
						},
						items: [
							{key : "sql_create", "name": "sql생성" 
								,subMenu: [
									{ key : "selectStar","name": "select *" , mode: "selectStar"}
									,{ key : "select","name": "select column" ,mode:"select"}
								]
							}
							,{divider:true}
							,{key : "create_ddl_top","name": "DDL 보기" 
								,subMenu:[
									{key : "ddl_copy","name": "복사하기"}
									,{key : "ddl_paste","name": "edit 영역에보기"}
								]
							}
						]
					}
				}
			});
 		}catch(e){
			VARSQL.log.info(e);
		}
	}
	// view 메타 데이타 보기.
	,_viewMetadata :function (colData ,reqParam){
		var _self = this;
		
		try{
    		var gridObj = {
    			data:colData.items
    			,column : [
    				{ label: '컬럼명', key: 'name',width:80 },
					{ label: '데이타타입', key: 'typeName' },
					{ label: '널여부', key: 'nullable',width:45},
					{ label: 'Key', key: 'constraints',width:45},
					{ label: '설명', key: 'comment',width:45}
				]
    		};
			
    		_self.setMetadataGrid(gridObj, 'view');
 		}catch(e){
			VARSQL.log.info(e);
		}
	}
	,_procedure:function (resData ,reqParam){
		var _self = this;
		try{
			var len = resData.items?resData.items.length:0;
    		var strHtm = [];
    		var $$gubun = 'procedure'
    		
			var itemArr = resData.items;
			
			$.each(itemArr , function (_idx, _item){
				var _name =_item.name;
				var colList = _item.colList; 
				_self._setMetaCache($$gubun,_name, {items:colList});
			})
			
			var procedureObj = $.pubGrid(_self.options.left_service_menu_contentId+'>#procedure',{
				headerView:true
				,height:'auto'
				,asideOptions :{
					lineNumber : {enable : true	,width : 30	,styleCss : 'text-align:right;padding-right:3px;'}				
				}
				,autoResize :false
				,page :false
				,tColItem : [
					{key :'name', label:'Procedure',width:200, sort:true}
					,{key :'status', label:'상태'}
					,{key :'remarks', label:'설명'}
				]
				,tbodyItem :itemArr
				,rowOptions :{
					click : function (idx, item){
						var sObj = $(this);
		    			
		    			$('.procedure-list-item.active').removeClass('active');
		    			sObj.addClass('active');
		    			
		    			_self._dbObjectMetadataList($.extend({},_self.options.param,{'gubun':$$gubun,'objectName':item.name}), '_procedureMetadata');
					}
					,contextMenu :{
						beforeSelect :function (){
							$(this).trigger('click');
						}
						,callback: function(key,sObj) {
							var ele = this.element, sItem = this.gridItem;
							var gubun=$$gubun
								,tmpName = sItem.name;
							
							var cacheData = _self._getMetaCache(gubun,tmpName);
							
							if(key =='copy'){
								procedureObj.copyData();
								return ; 
							}
							
							if(key=='ddl_copy' || key=='ddl_paste'){
								_self._createDDL({
									gubunKey : key
									,gubun : $$gubun
									,objName :  tmpName 
								});
								return ;
							}
						},
						items: [
							{key : "copy" , "name": "복사"}
							,{divider:true}
							,{key : "create_ddl_top","name": "DDL 보기" 
								,subMenu:[
									{key : "ddl_copy","name": "복사하기"}
									,{key : "ddl_paste","name": "edit 영역에보기"}
								]
							}
						]
					}
				}
			});
 		}catch(e){
			VARSQL.log.info(e);
		}
	}
	//테이블에 대한 메타 정보 보기 .
	,_procedureMetadata :function (colData ,reqParam){
		var _self = this;
		
		try{
    		var gridObj = {
    			data:colData.items
    			,column : [
					{ label: '파라미터명', key: 'name',width:80 },
					{ label: '데이타타입', key: 'dataType' },
					{ label: 'IN, OUT', key: 'columnType',width:45},
					{ label: '설명', key: 'comment',width:45},
				]
    		};
			
    		_self.setMetadataGrid(gridObj, 'procedure');
 		}catch(e){
			VARSQL.log.info(e);
		}
	}
	,_function : function (resData ,reqParam){
		var _self = this;
		try{
			var len = resData.items?resData.items.length:0;
    		var strHtm = [];
    		var $$gubun = 'function'
    		
			var itemArr = resData.items;
			
			$.each(itemArr , function (_idx, _item){
				var _name =_item.name;
				var colList = _item.colList; 
				_self._setMetaCache($$gubun,_name, {items:colList});
			})
		
			var itemArr = resData.items;
    				
			$.pubGrid(_self.options.left_service_menu_contentId+'>#function',{
				headerView:true
				,asideOptions :{
					lineNumber : {enable : true	,width : 30	,styleCss : 'text-align:right;padding-right:3px;'}				
				}
				,height: 'auto'
				,page :false
				,tColItem : [
					{key :'name', label:'Function',width:200, sort:true}
					,{key :'status', label:'상태'}
					,{key :'remarks', label:'설명'}
				]
				,tbodyItem :itemArr
				,rowOptions :{
					click : function (idx, item){
						var sObj = $(this);
		    			
		    			$('.function-list-item.active').removeClass('active');
		    			sObj.addClass('active');
		    			
		    			_self._dbObjectMetadataList($.extend({},_self.options.param,{'gubun':$$gubun,'objectName':item.name}), '_functionMetadata');
					}
					,contextMenu :{
						beforeSelect :function (){
							$(this).trigger('click');
						}
						,callback: function(key,sObj) {
							var ele = this.element, sItem = this.gridItem;
							var gubun=$$gubun
								,tmpName = sItem.name;
							
							var cacheData = _self._getMetaCache(gubun,tmpName);
							
							if(key =='copy'){
								procedureObj.copyData();
								return ; 
							}
							
							if(key=='ddl_copy' || key=='ddl_paste'){
								_self._createDDL({
									gubunKey : key
									,gubun : $$gubun
									,objName :  tmpName 
								});
								return ;
							}
						},
						items: [
							{key : "copy" , "name": "복사"}
							,{divider:true}
							,{key : "create_ddl_top","name": "DDL 보기" 
								,subMenu:[
									{key : "ddl_copy","name": "복사하기"}
									,{key : "ddl_paste","name": "edit 영역에보기"}
								]
							}
						]
					}
				}
			});
 		}catch(e){
			VARSQL.log.info(e);
		}
	}
	,_functionMetadata : function (colData ,reqParam){
		var _self = this;
		
		try{
    		var gridObj = {
    			data:colData.items
    			,column : [
					{ label: '파라미터명', key: 'name',width:80 },
					{ label: '데이타타입', key: 'dataType' },
					{ label: 'IN, OUT', key: 'columnType',width:45},
					{ label: '설명', key: 'comment',width:45},
				]
    		};
			
    		_self.setMetadataGrid(gridObj, 'function');
 		}catch(e){
			VARSQL.log.info(e);
		}
	}
	// 메타 데이타 그리기.
	,setMetadataGrid :function (gridData, type){
		var _self = this;
		//_self.getMetaContentWrapEle().empty();
		//_self.getMetaContentWrapEle().html('<div id="'+_self.options.metadata_content_areaId.replace('#', '')+'"></div>');
		
		var tmpEle = $(_self.options.metadata_content_area_wrapId+type);
		
		if(!tmpEle.hasClass('on')){
			$('.varsql-meta-cont-ele.on').removeClass('on');
			tmpEle.addClass('on');
		}
		
		if(tmpEle.length < 1){
			_self.getMetaContentWrapEle().append('<div id="'+ (_self.options.metadata_content_area_wrapId+type).replace('#', '') +'" class="varsql-meta-cont-ele on"></div>');
		}
		
		var gridObj = $.pubGrid(_self.options.metadata_content_area_wrapId+type);
		
		if(gridObj){
			gridObj.setData(gridData.data,'reDraw');
			return ; 
		}
		
		var contextItem = [
			{key : "copy" , "name": "복사", hotkey :'Ctrl+C'}
		];
		
		if(type == 'table'){
			contextItem = [
				{key : "copy" , "name": "복사", hotkey :'Ctrl+C'}
				,{divider:true}
				,{key : "sql_create", "name": "sql생성" 
					,subMenu: [
						{ key : "select","name": "select" ,mode:"select"}
						,{ key : "insert","name": "insert" , mode:"insert"}
						,{ key : "update","name": "update" ,mode:"update"}
					]
				}
				,{key : "mybatis-sql_create","name": "mybatis Sql생성" 
					,subMenu : [
						{ key : "mybatis_insert","name": "insert" ,mode:"insert" ,param_yn:'Y'}
						,{ key : "mybatis_update","name": "update" ,mode:"update" ,param_yn:'Y'}
						,{ key : "mybatis_insert_camel_case","name": "insertCamelCase" ,mode:"insert|camel" ,param_yn:'Y'}
						,{ key : "mybatis_update_camel_case","name": "updateCamelCase" ,mode:"update|camel" ,param_yn:'Y'}
					]
				}
			]
		}
		
		var gridObj = $.pubGrid(_self.options.metadata_content_area_wrapId+type, {
			headerOptions : {
				redraw : false
			}
			,asideOptions :{
				lineNumber : {enable : true	,width : 30	,styleCss : 'text-align:right;padding-right:3px;'}				
			}
			,page :false
			,height:'auto'
			,autoResize :false
			,tColItem : gridData.column
			,tbodyItem :gridData.data
			,rowOptions :{
				contextMenu : {
					beforeSelect :function (){
						$(this).trigger('click');
					}
					,disableItemKey : function (items){
						if(gridObj.getSelectItem(['name']).length < 1){
							return [
								{key :'sql_create' , depth :0	}
								,{key :'mybatis-sql_create' , depth :0}	
							]; 
						}
						
						return [];
						
					}
					,callback: function(key,sObj) {
						
						if(key =='copy'){
							gridObj.copyData();
							return ; 
						}
						var cacheData = gridObj.getSelectItem(['name']);
						key = sObj.mode;
						
						_self._createScriptSql({
							gubunKey : key
							,gubun : 'table'
							,objName :  _self.selectMetadata['table']
							,item : {
								items:cacheData
							}
							,param_yn: sObj.param_yn
						});
						
					},
					items: contextItem
				}
			}
		});
	}
	//db url call 할때 앞에 uri 뭍이기
	,_getPrefixUri:function (uri){
		return _g_options.getUriPrefix(uri);
	}
};

/**
 * sql 데이타 그리드
 */
_ui.SQL = {
	sqlTextAreaObj:null
	,sqlEditorEle:null
	,sqlEditorSearchCursor : null
	,resultMsgAreaObj:null
	,dataGridSelectorWrapObj:null
	,memoDialog : null
	,currentSqlData :''
	,_currnetQueryReusltData :{}
	,options :{
		selector:'#sqlExecuteArea'
		,dataGridSelector:'#dataGridArea'
		,dataColumnTypeSelector:'#dataColumnTypeArea'
		,dataGridSelectorWrap:'#dataGridAreaWrap'
		,resultMsgAreaWrap:'#resultMsgAreaWrap'
		,dataGridResultTabWrap:'#data_grid_result_tab_wrap'
		,preloaderArea :'#sqlEditerPreloaderArea'
		,limitCnt:'#limitRowCnt'
		,conuidObj:'#conuid'
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
		_self._createHtml();
		_self._initEditor();
		_self._initEvent();
	}
	,_createHtml :function(){
		var _self = this;
		
		var resultTabHtm = [],resultGridHtm=[];
		
		// data grid result tab
		resultTabHtm.push('<ul id="data_grid_result_tab" class="sql-result-tab">');
		resultTabHtm.push('	<li tab_gubun="result" class="on"><a href="javascript:;">결과</a></li>');
		resultTabHtm.push('	<li tab_gubun="columnType"><a href="javascript:;">컬럼타입</a></li>');
		resultTabHtm.push('	<li tab_gubun="msg"><a href="javascript:;">메시지</a></li>');
		resultTabHtm.push('</ul>');
		
		$(_self.options.dataGridResultTabWrap).html(resultTabHtm.join(''));
		
		// data grid araea
		resultGridHtm.push('<div id="dataGridArea" class="sql-result-area on" tab_gubun="result"></div>');
		resultGridHtm.push('<div id="dataColumnTypeArea" class="sql-result-area on" tab_gubun="columnType"></div>');
		resultGridHtm.push('<iframe id="resultMsgAreaWrap" frameborder="0" class="sql-result-area" tab_gubun="msg" src="" style="width:100%;bottom:0px;left:0px;top:0px;right:0px;"></iframe>');
		$(_self.options.dataGridSelectorWrap).html(resultGridHtm.join(''));
	}
	,_initEditor : function (){
		var _self = this;
		
		var tableHint = {};
		$.each(_ui.base.sqlHints , function (_idx, _item){
			tableHint[_item] = {
				colums:[]
				,text :_item
			};
		})
		
		VARSQL.ui.ddd = _self.sqlTextAreaObj = CodeMirror.fromTextArea(document.getElementById(_self.options.selector.replace('#', '')), {
			mode: _ui.base.mimetype,
			indentWithTabs: true,
			smartIndent: true,
			lineNumbers: true,
			height:'auto',
			lineWrapping: false,
			matchBrackets : true,
			autofocus: true,
			extraKeys: {"Ctrl-Space": "autocomplete"},
			hintOptions: {tables:tableHint}
		});
		
		_self.sqlEditorEle = $('.CodeMirror.cm-s-default');
	}
	//이벤트 초기화 
	,_initEvent :function (){
		var _self = this;
		
		function strUpperCase(){
			var selArr = _self.sqlTextAreaObj.getSelections(); 
			
			for(var i =0 ; i< selArr.length;i++){
				selArr[i] = toUpperCase(selArr[i]);
			}
			var selPosArr = _self.sqlTextAreaObj.listSelections(); 
			_self.sqlTextAreaObj.replaceSelections(selArr,selPosArr);
			_self.sqlTextAreaObj.setSelections(selPosArr);
		}
		
		function strLowerCase(){
			var selArr = _self.sqlTextAreaObj.getSelections(); 
			
			for(var i =0 ; i< selArr.length;i++){
				selArr[i] = toLowerCase(selArr[i]);
			}
			var selPosArr = _self.sqlTextAreaObj.listSelections(); 
			_self.sqlTextAreaObj.replaceSelections(selArr,selPosArr);
			_self.sqlTextAreaObj.setSelections(selPosArr);
		}
		function strCamelCase(){
			var selArr = _self.sqlTextAreaObj.getSelections(); 
			
			for(var i =0 ; i< selArr.length;i++){
				selArr[i] = convertCamel(selArr[i]);
			}
			var selPosArr = _self.sqlTextAreaObj.listSelections(); 
			_self.sqlTextAreaObj.replaceSelections(selArr,selPosArr);
			_self.sqlTextAreaObj.setSelections(selPosArr);
		}
		
		$.pubContextMenu(_self.sqlEditorEle, {
			items:[
				{key : "run" , "name": "실행" , hotkey :'Ctrl+Enter'}
				,{divider:true}
				,{key : "copy" , "name": "복사", hotkey :'Ctrl+C'}
				,{key : "cut" , "name": "잘라내기", hotkey :'Ctrl+X'}
				//,{key : "paste" , "name": "뭍여넣기"}
				,{key : "delete" , "name": "지우기"}
				,{divider:true}
				,{key : "sqlFormat" , "name": "쿼리 정렬"
					,subMenu: [
						{ key : "formatVarsql","name": "줄바꿈 정렬",hotkey :'Ctrl+Alt+F'}
						,{ key : "formatUtil","name": "정렬" , hotkey :'Ctrl+Shift+F'}
					]
				}
				,{key : "upperLowerCase", "name": "대소문자변환" 
					,subMenu: [
						{ key : "upper","name": "대문자변환",hotkey :'Ctrl+Shift+X'}
						,{ key : "lower","name": "소문자" , hotkey :'Ctrl+Shift+Y'}
						,{ key : "camel","name": "Camel Case"}
					]
				}
				,{key : "msgSend" , "name": "메시지 보내기"}
    		]
			,callback:function (key, item , evt){
	    		var sObj = this.element;
	    		
	    		switch (key) {
		    		case 'run':
		    			$('.sql_execue_btn').trigger('click');
		    			break;
					case 'undo':
						_self.sqlTextAreaObj.undo();
						break;
					case 'redo':
						_self.sqlTextAreaObj.redo();
						break;
					case 'copy':
						copyStringToClipboard('varsqleditor',_self.getSql());
						break;
					case 'cut':
						var startCursor = _self.sqlTextAreaObj.getCursor(true);
						copyStringToClipboard('varsqleditor',_self.getSql());
						_self.sqlTextAreaObj.replaceSelection('');
						
						_self.sqlTextAreaObj.focus();
						_self.sqlTextAreaObj.setCursor({line: startCursor.line, ch: startCursor.ch})
						break;
					case 'paste':
						
						console.log('paste')
						var startCursor = _self.sqlTextAreaObj.getCursor(true);
						_self.sqlTextAreaObj.focus();
						_self.sqlTextAreaObj.setCursor({line: startCursor.line, ch: startCursor.ch});
						try{
							document.execCommand('paste');
						}catch(e){
							console.log(e);
						}
						break;
					case 'delete':
						var startCursor = _self.sqlTextAreaObj.getCursor(true);
						_self.sqlTextAreaObj.replaceSelection('');
						
						_self.sqlTextAreaObj.focus();
						_self.sqlTextAreaObj.setCursor({line: startCursor.line, ch: startCursor.ch})
						
						break;
					case 'msgSend':
						$('.sql_send_btn').trigger('click');
						break;
					case 'formatVarsql':
						_self.sqlFormatData('varsql');
						break;
					case 'formatUtil':
						$('.sql_format_btn').trigger('click');
						break;
					case 'upper':
						strUpperCase();
						break;
					case 'lower':
						strLowerCase();
						break;
					case 'camel':
						strCamelCase();
						break;
					default:
						break;
				}
	    	}
		});
	
		_self.sqlEditorEle.on('keydown',function (e) {
			var evt =window.event || e; 
			
			if(evt.ctrlKey){
				var returnFlag = true; 
				if (evt.altKey) { // keyCode 78 is n
					switch (evt.keyCode) {
						case 78:
							$('.sql_new_file').trigger('click');
							returnFlag = false; 
							break;
						case 70: // 70 is f
							_self.sqlFormatData('varsql');
							returnFlag = false; 
							break;
						default:
							break;
					}
				}else if (evt.shiftKey) { 
					switch (evt.keyCode) {
						case 70: // keyCode 70 is f
							$('.sql_format_btn').trigger('click');
							returnFlag = false; 
							break;
						case 83: // keyCode 83 is s
							$('.sql_save_btn').trigger('click');
							returnFlag = false; 
							break;
						case 88: // keycode 88 is x  toUpperCase
							strUpperCase();
							returnFlag = false; 
							break;
						case 89: //keycode 89 is y  toLowerCase
							strLowerCase();
							returnFlag = false; 
							break;
						default:
							break;
					}
				}else{
					switch (evt.keyCode) {
						case 83: // keyCode 83 is s
							$('.sql_save_btn').trigger('click');
							returnFlag = false; 
							break;
						case 13: // keyCode 13 is Enter
							$('.sql_execue_btn').trigger('click');
							returnFlag = false; 
							break;
						default:
							break;
					}
				}
				return returnFlag;
			}
		});
		
		_self.sqlEditorEle.on('click', function(e){
			$('#sql_parameter_area').removeClass('on');
		})
		
		// sql 실행
		$('.sql_execue_btn').on('click',function (evt){
			_self.sqlData(evt);
		});
		
		// 실행취소
		$('.sql_undo_btn').on('click',function (evt){
			_self.sqlTextAreaObj.undo();
		});
		
		// 다시실행.
		$('.sql_redo_btn').on('click',function (evt){
			_self.sqlTextAreaObj.redo();
		});
		
		// sql 보내기
		$('.sql_send_btn').on('click',function (evt){
			_self.sqlSend(evt);
		});
		
		// sql 보내기
		$('.sql_find_btn').on('click',function (evt){
			var sqlFindText = $('#sqlFindText').val();
			_self.searchFindText(sqlFindText);
		});
		
		// 자동 줄바꿈.
		$('.sql_linewrapper_btn').on('click',function (evt){
			var lineWrapping = _self.sqlTextAreaObj.getOption('lineWrapping');
			
			lineWrapping = !lineWrapping;
			if(lineWrapping){
				$(this).addClass('sql-btn-success');
			}else{
				$(this).removeClass('sql-btn-success');
			}
			_self.sqlTextAreaObj.setOption('lineWrapping',lineWrapping);
		});
		
		// sql 포멧 정리.
		$('.sql_format_btn').on('click',function (){
			_self.sqlFormatData();
		});
		
		// 메개변수 처리. 
		$('#sql_parameter_toggle_btn').on('click',function (){
			if($('#sql_parameter_area').hasClass('on')){
				$('#sql_parameter_area').removeClass('on');
			}else{
				$('#sql_parameter_area').addClass('on');
			}
		});
		
		// sql 파라미터 삭제. 
		$('#sql_parameter_area').on('click','.sql-param-del-btn',function (e){
			//if(confirm('삭제 하시겠습니까?')){
			$(this).closest('.sql-param-row').remove();
			//}
		});
		
		// param add
		$('.sql-param-add-btn').on('click',function (e){
			_self.addParamTemplate('add');
			
		});
		
		// sql 정보 저장. 
		$('.sql_save_btn').on('click',function (e){
			_self.saveSql();
		});
		
		$('#sql-save-list-no').keydown(function(e) {
			if (e.keyCode == '13') {
				_self.sqlSaveList($('#sql-save-list-no').val());
			}
		});
		
		$('#saveSqlSearch').keydown(function(e) {
			if (e.keyCode == '13') {
				_self.sqlSaveList(1);
			}
		});
		
		$.pubAutocomplete('#recv_user_search' , {
			minLength : 0
			,itemkey : 'UID'
			,addSelector:'#recv_autocomplete_area'
			,autoClose:false
			,autocompleteTemplate : function (baseHtml){
				return '<div class="">'+baseHtml+'</div>';
			}
			,source : function (request, response){
				var params = { searchVal : request };
				
				VARSQL.req.ajax({      
				    url:{gubun:VARSQL.uri.user, url:'/searchUserList.varsql'}
				    ,data: params
				    ,success:function (data){
				    	//서버에서 json 데이터 response 후 목록에 뿌려주기 위함 VIEWID,UID,UNAME
				    	response(data.items);
					}
				});  
			}
			,select: function( event, item ) {
				var strHtm = [];
				
				this.selectorElement.val('');
				
				if($('.recv_id_item[_recvid="'+item.VIEWID+'"]').length > 0 ) {
					return false;
				}
				
				strHtm.push('<div class="recv_id_item" _recvid="'+item.VIEWID+'">'+item.UNAME+'('+item.UID+')');
				strHtm.push('<a href="javascript:;" class="pull-right">X</a></div>');
				$('#recvIdArr').append(strHtm.join(''));
				
				$('.recv_id_item[_recvid="'+item.VIEWID+'"] a').on('click', function (){
					$(this).closest('[_recvid]').remove();
				})
				
				return false; 
			}
			,renderItem : function (matchData,item){
				return item.UNAME+'('+matchData+')';
			}
		});
		
		// sql 정보 목록 이동. 
		$('.sql-list-move-btn').on('click',function (e){
			var mode = $(this).attr('_mode');
			
			var pageNo = $('#sql-save-list-no').val();
			var lastPage = $('#sql-save-list-pagecnt').html(); 
			
			pageNo = parseInt(pageNo, 10);
			if(mode=='p'){
				pageNo = pageNo -1; 
			}else{
				pageNo = pageNo +1;
			}
			
			if(pageNo > 0 && pageNo <= lastPage){
				_self.sqlSaveList(pageNo);
				return ; 
			}
		});
		
		$('.sql_save_list_btn').dropdown();
		$('.sql_save_list_btn').on('click',function (){
			
			if($('.sql-save-list-layer').attr('loadFlag') != 'Y'){
				$('.varsql-dropdown').addClass('on');
				_self.sqlSaveList();
			}
		});
		
		$('.sql_new_file').on('click',function (){
			_self.setQueryInfo('clear');
		});
		
		$(_self.options.dataGridResultTabWrap+' [tab_gubun]').on('click',function (){
			var sObj = $(this);
			var tab_gubun = sObj.attr('tab_gubun');
			
			if(sObj.hasClass('on')){
				return ;
			}
			
			$(_self.options.dataGridResultTabWrap+' [tab_gubun]').removeClass('on');
			sObj.addClass('on');
			
			// data grid araea
			$(_self.options.dataGridSelectorWrap +' [tab_gubun]').removeClass('on');
			$(_self.options.dataGridSelectorWrap +' [tab_gubun='+tab_gubun+']').addClass('on');
			
			if(tab_gubun == 'columnType'){
				_self.viewResultColumnType();
			}
		});
	}
	// 검색.
	,searchFindText : function (schTxt){
		var _self = this; 
		
		var endPos = _self.sqlTextAreaObj.listSelections()[0].head;
		
		var cursor =_self.sqlTextAreaObj.getSearchCursor(schTxt, endPos);
		_self.sqlEditorSearchCursor = cursor; 
		
		if(cursor.findNext()){
			_self.sqlTextAreaObj.setSelection(cursor.from(), cursor.to());
		}
	}
	// sql result column typ
	,viewResultColumnType : function (){
		var _self = this; 
		var columnTypeArr = _self._currnetQueryReusltData.column; 
		if(_self._currnetQueryReusltData.viewType != 'grid'){
			columnTypeArr = [];
		}
		
		var gridObj= $.pubGrid(_self.options.dataColumnTypeSelector,{
			height:'auto'
			,autoResize : false
			,page :false
			,headerOptions:{
				view:true
				,sort : true
				,resize:{
					enabled : true
				}
			}
			,asideOptions :{
				lineNumber : {enable : true	,width : 30	,styleCss : 'text-align:right;padding-right:3px;'}				
			}
			,tColItem : [
				{label: "NAME", key: "key"}
				,{label: "TYPE", key: "dbType"}
			]
			,tbodyItem :columnTypeArr
			,rowOptions :{
				contextMenu : {
					beforeSelect :function (){
						$(this).trigger('click');
					}
					,callback: function(key,sObj) {
						if(key =='copy'){
							gridObj.copyData();
							return ; 
						}
						
					},
					items: [
						{key : "copy" , "name": "복사"}
					]
				}
			}
		});
	}
	// sql 파라미터 셋팅. 
	,addParamTemplate : function (mode, data){
		if('data' == mode || 'init_data' == mode){
			var paramHtm = [];
			
			var dataLen = Object.keys(data||{}).length;
			if(dataLen < 1) data = {'' :''};
			
			if('data' == mode){
				$('.param-field-key').each(function (){
					if($.trim($(this).val())==''){
						$(this).closest('.input-field-row').remove();
					}
				})
			}
			
			for(var key in data){
				var tmpHtm='<tr class="sql-param-row">'
					+'	<td><div><input type="text" class="sql-param-key" value="{{key}}" /></div></td>'
					+'	<td><div><input type="text" class="sql-param-value" value="{{val}}"/></div></td>'
					+'	<td><span><button type="button" class="sql-param-del-btn fa fa-minus btn btn-sm btn-default"></button></span></td>'
					+'</tr>';
				
				paramHtm.push(Mustache.render(tmpHtm, {key: key , val : data[key]}));
			}
			
			if('init_data' ==mode){
				$('#sql_parameter_row_area').empty().html(paramHtm.join(''));	
			}else{
				$('#sql_parameter_row_area').append(paramHtm.join(''));
			}
		}else{
			var paramHtm='<tr class="sql-param-row">'
				+'	<td><div><input type="text" class="sql-param-key" /></div></td>'
				+'	<td><div><input type="text" class="sql-param-value"/></div></td>'
				+'	<td><span><button type="button" class="sql-param-del-btn fa fa-minus btn btn-sm btn-default"></button></span></td>'
				+'</tr>';
			
			if(mode =='init'){
				$('#sql_parameter_row_area').empty().html(paramHtm);	
			}else{
				$('#sql_parameter_row_area').append(paramHtm);
			}
		}
		
	}
	// save sql
	,saveSql : function (){
		var _self = this; 
		
		var params =VARSQL.util.objectMerge (_g_options.param,{
			'sql' :_self.getTextAreaObj().getValue()
			,'sqlTitle' : $('#saveSqlTitle').val()
			,'sqlId' : $('#sql_id').val()
			,'sqlParam' : JSON.stringify(_self.getSqlParam())
		});
		
		VARSQL.req.ajax({      
		    loadSelector : '#editorAreaTable'
		    ,url:{gubun:VARSQL.uri.sql, url:'/base/saveQuery.varsql'}
		    ,data:params 
		    ,success:function (res){
		    	$('#sql_id').val(res.item);
		    	_self.sqlSaveList();
		    	_self.currentSqlData = params.sql;
		    	//$(_self.options.preloaderArea +' .preloader-msg').html('저장되었습니다.');
		    	
			}
		});  
	}
	// sql 보내기.
	,sqlSend :function (){
		var _self = this;
		var sqlVal = _self.getSql();
		
		sqlVal=$.trim(sqlVal);
		
		$('#memoTitle').val(VARSQL.util.dateFormat(new Date(), 'yyyy-mm-dd HH:MM')+'_제목');
		$('#memoContent').val(sqlVal);
		
		if(_self.memoDialog==null){
			_self.memoDialog = $('#memoTemplate').dialog({
				height: 350
				,width: 640
				,modal: true
				,buttons: {
					"보내기":function (){
						var recvEle = $('.recv_id_item[_recvid]');
						
						if(recvEle.length < 1) {
							alert('보낼 사람을 선택하세요.');
							return ; 
						}
						
						if(!confirm('보내기 시겠습니까?')) return ; 
						
						var recv_id = [];
						$.each(recvEle,function (i , item ){
							recv_id.push($(item).attr('_recvid'));
						});

						var params = {
							'memo_title' : $('#memoTitle').val()
							,'memo_cont' : $('#memoContent').val()
							,'recv_id' : recv_id.join(';;')
						};
						
						VARSQL.req.ajax({      
						    loadSelector : '#editorAreaTable'
						    ,url:{gubun:VARSQL.uri.user, url:'/sendSql.varsql'}
						    ,data:params 
						    ,success:function (resData){
						    	_self.memoDialog.dialog( "close" );
							}
						});
					}
					,Cancel: function() {
						_self.memoDialog.dialog( "close" );
					}
				}
				,close: function() {
					_self.memoDialog.dialog( "close" );
				}
			});
		}
		
		_self.memoDialog.dialog("open");
		
		$('#recvIdArr').html('');
		
	}
	// set queryInfo
	,setQueryInfo : function (sItem){
		if(sItem=='clear'){
			if(this.currentSqlData != this.getTextAreaObj().getValue()){
				if(confirm('현재 쿼리를 저장 하시겠습니까?')){
					$('.sql_save_btn').trigger('click');
				}
				
			}
			sItem = {
				SQL_ID:''
				, GUERY_TITLE:(VARSQL.util.dateFormat(new Date(), 'yyyy-mm-dd HH:MM')+'_query')
				,QUERY_CONT:'',SQL_PARAM :''
			}
		}
		$('#sql_id').val(sItem.SQL_ID);
		$('#saveSqlTitle').val(sItem.GUERY_TITLE);
		this.getTextAreaObj().setValue(sItem.QUERY_CONT);
		this.getTextAreaObj().setHistory({done:[],undone:[]});
		try{
			this.addParamTemplate('init_data',$.parseJSON(sItem.SQL_PARAM));
		}catch(e){
			this.addParamTemplate('init_data',{'':''});
		}
		
		this.currentSqlData =  sItem.QUERY_CONT;
	}
	// 저장된 sql 목록 보기.
	,sqlSaveList : function (pageNo){
		var _self = this; 
		
		var params =VARSQL.util.objectMerge (_g_options.param,{
			searchVal : $('#saveSqlSearch').val()
			,page : pageNo ? pageNo : $('#sql-save-list-no').val()
			,countPerPage : 5
		});
		
		VARSQL.req.ajax({
		    loadSelector : '#editorAreaTable'
		    ,url:{gubun:VARSQL.uri.sql, url:'/base/sqlList.varsql'}
		    ,data:params 
		    ,success:function (res){
		    	var items = res.items;
		    	var paging = res.page;
		    	var strHtm = []
		    		,len = items.length;
		    	
		    	if(items.length > 0){
		    		for(var i =0 ;i <len; i++){
		    			var item = items[i];
		    			strHtm.push('<li _idx="'+i+'"><a href="javascript:;" class="save-list-item" _mode="view">'+item.GUERY_TITLE+'&nbsp;</a>');
		    			strHtm.push('<a href="javascript:;" class="pull-right save-list-item" _mode="del">삭제</a></li>');
		    		}
		    	}else{
		    		strHtm.push('<li>no data</li>')
		    	}
		    	
		    	$('#saveSqlList').empty().html(strHtm.join(''));
		    	
		    	$('.sql-save-list-layer').attr('loadFlag' ,'Y');
		    	$('#sql-save-list-no').val(paging.currPage); 
		    	$('#sql-save-list-pagecnt').html(paging.totalPage); 
		    	$('#sql-save-list-totalcnt').html(paging.totalCount); 
		    	
		    	$('#saveSqlList .save-list-item').on('click', function (e){
		    		var sObj = $(this)
		    			, mode = sObj.attr('_mode')
		    			, idx = sObj.closest('[_idx]').attr('_idx');
		    		
		    		var sItem =items[idx]; 
		    		
		    		if(mode=='view'){
		    			_self.setQueryInfo(sItem);
		    			$('.sql_save_list_btn').trigger('click');
		    		}else{
		    			if(!confirm('['+sItem.GUERY_TITLE + '] 삭제하시겠습니까?')){
		    				return ; 
		    			}
		    			
		    			if(sItem.SQL_ID == $('#sql_id').val()){
		    				_self.setQueryInfo('clear');
		    			}
		    			
		    			params['sqlId'] = sItem.SQL_ID;
		    			VARSQL.req.ajax({
		    			    type:"POST"
		    			    ,loadSelector : '#editorAreaTable'
		    			    ,url:{gubun:VARSQL.uri.sql, url:'/base/delSqlSaveInfo.varsql'}
		    			    ,dataType:'json'
		    			    ,data:params 
		    			    ,success:function (res){
		    			    	_self.sqlSaveList();
		    			    }
		    			});
		    		}
		    	})
			}
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
			var msgEle = $(_self.options.resultMsgAreaWrap).contents();
			var msgStyle = '<style>body{margin:0px;}.error-log-message{font-size:12px;color:#dd4b39;padding-bottom: 3px;border-bottom: 1px solid #f1e4e4;}'
				+'.success-log-message{	font-size:12px;color:#000099;padding-bottom: 3px;border-bottom: 1px solid #f1e4e4;}</style>';
			
			msgEle.find('head').html(msgStyle);
			
			_self.resultMsgAreaObj = msgEle.find('body');
			_self.resultMsgAreaObj.attr('oncontextmenu', "return false");
		}
		return _self.resultMsgAreaObj; 
	}
	,getSql: function (mode){
		var _self = this;
		var textObj = _self.getTextAreaObj(); 
		
		return textObj.getSelection()
	}
	// sql 실행시 셋팅 파라미터 구하기.
	,getSqlParam : function (){
		var sqlParam ={};
	
		$('.sql-param-row').each(function(i ,item){
			var k = $(this).find('.sql-param-key').val()
				,v=$(this).find('.sql-param-value').val();
			
			if($.trim(k) != ''){
				sqlParam[k] = v;
			}
		})
		return sqlParam; 
	}
	// sql 실행시 파라미터 체크. 
	,sqlParamCheck : function (sqlVal, sqlParam){
		var _self =this; 
		var matchArr = sqlVal.match(/[#|$]{(.+?)}/gi);
		if(matchArr){
			var addParam = {};
			var flag = true;
			for(var i =0 ;i < matchArr.length;i++){
		    	var tmpKey = matchArr[i].replace(/[$|#|{|}]/gi,''); 
		    	
		    	if(typeof sqlParam[tmpKey]==='undefined'){
		    		addParam[tmpKey] = '';
		    		flag = false; 
		    	}
			}
			
			if(flag == false){
				_self.addParamTemplate('data',addParam);
				$('#sql_parameter_area').addClass('on');
				
				var loopCnt = 0; 
				var loopInter = setInterval(function (){
					if((loopCnt+1)%2==1){
						$('#sql_parameter_area').css('background-color','#ffb3b3');
					}else{
						$('#sql_parameter_area').css('background-color','');
					}
					++loopCnt;
					if(loopCnt > 3){
						clearInterval(loopInter);
					}
				}, 500);	
			}
			
			return flag; 
			
		}
		
		return true; 
	}
	// sql 데이타 보기 
	,sqlData :function (evt){
		var _self = this;
		var sqlVal = _self.getSql();
		
		_self._sqlData(sqlVal,true);
	}
	,getSelectionStartPos : function(){
		var std = this.sqlTextAreaObj.listSelections()[0].anchor
		,end = this.sqlTextAreaObj.listSelections()[0].head;
	
		if(std.line < end.line){
			std  = end;
		}else if(std.line == end.line && std.ch > end.ch){
			std  = end;
		}
		return std;
	}
	// sql 데이타 보기 
	,_sqlData :function (sqlVal, paramFlag){
		var _self = this;
		
		sqlVal=$.trim(sqlVal);
		if('' == sqlVal){
			return ; 
		}
		
		var sqlParam = {};
		
		if(paramFlag===true){
			sqlParam = _self.getSqlParam();
			if(!_self.sqlParamCheck(sqlVal, sqlParam)){
				return '';
			}
		}
		
		var params =VARSQL.util.objectMerge (_g_options.param,{
			'sql' :sqlVal
			,'limit' : $(_self.options.limitCnt).val()
			,sqlParam : JSON.stringify(sqlParam)
		});
		
		VARSQL.req.ajax({      
		    loadSelector : '#editorAreaTable'
		    ,url:{gubun:VARSQL.uri.sql, url:'/base/sqlData.varsql'}
		    ,data:params 
		    ,success:function (responseData){
		    	try{
		    		var msgViewFlag =false,gridViewFlag = false;
		    		
		    		var resultMsg = [];
		    		
		    		if(responseData.resultCode ==500){
		    			
		    			var errQuery = responseData.item.query; 
		    			msgViewFlag =true; 
		    			resultMsg.push('<div class="error-log-message">#resultMsg#</div>'.replace('#resultMsg#' , responseData.message+'<br/>sql line : ['+responseData.customs.errorQuery+'] query: '+errQuery));
		    			
		    			var stdPos = _self.getSelectionStartPos();
		    			
		    			var cursor =_self.sqlTextAreaObj.getSearchCursor(errQuery, stdPos);
		    			
		    			if(cursor.findNext()){
		    				_self.sqlTextAreaObj.setSelection(cursor.from(), cursor.to());
		    			}
		    		}else{
		    			var resData = responseData.items; 
			    		var resultLen = resData.length;
			    		
			    		if(resultLen < 1 ){
			    			resData.data = [{result:"데이타가 없습니다."}];
			    			resData.column =[{label:'result',key:'result', align:'center'}];
			    		}
			    		
			    		var item; 
			    		var resultClass , tmpMsg;
			    		
		    			for(var i=resultLen-1; i>=0; i--){
		    				resultClass = 'success-log-message';
		    				item = resData[i];
		    				
		    				tmpMsg= item.resultMessage;
		    				if(item.resultType=='FAIL' || item.viewType=='msg'){
		    					msgViewFlag = true;
		    					
		    					if(item.resultType=='FAIL'){
			    					resultClass = 'error-log-message'; 
		    					}
		    				}
		    				
		    				if(item.viewType=='grid'){
		    					gridViewFlag = true;
		    					_self._currnetQueryReusltData =item;
		    				}
		    				    				
		    				resultMsg.push('<div class="'+resultClass+'">#resultMsg#</div>'.replace('#resultMsg#' , tmpMsg));
		    			}
		    		}
	    			if(msgViewFlag){
		    			$(_self.options.dataGridResultTabWrap+" [tab_gubun=msg]").trigger('click');
    				}else{
	    				$(_self.options.dataGridResultTabWrap+" [tab_gubun=result]").trigger('click');
	    			}
	    			
	    			if(gridViewFlag){
	    				_self.setGridData(_self._currnetQueryReusltData);
	    			}
	    			_self.getResultMsgAreaObj().prepend(resultMsg.join(''));
    				_self.getResultMsgAreaObj().animate({scrollTop: 0},'fast');
    				
		 		}catch(e){
					VARSQL.log.info(e);
				}		             
			}
		});  
	}
	// sql format
	,sqlFormatData :function (formatType){
		var _self = this;
		var sqlVal = _self.getSql('pos');
		var tmpEditor =_self.getTextAreaObj(); 
		sqlVal=$.trim(sqlVal);
		
		var startSelection;
		
		if('' == sqlVal){
			startSelection = {line:0,ch:0};
			tmpEditor.setSelection(startSelection, {line:10000,ch:0});
			sqlVal  = tmpEditor.getValue();
		}else{
			startSelection = _self.getSelectionStartPos();
		}
		
		if(''== sqlVal) return ; 
		
		var params =VARSQL.util.objectMerge (_g_options.param,{
			'sql' :sqlVal
		});
		
		params.formatType =formatType; 
		
		VARSQL.req.ajax({      
		    loadSelector : '#editorAreaTable'
		    ,url:{gubun:VARSQL.uri.sql, url:'/base/sqlFormat.varsql'}
		    ,dataType:'text'
		    ,data:params 
		    ,success:function (res){
		    	var linecnt = VARSQL.matchCount(res,VARSQLCont.constants.newline);
	    		tmpEditor.replaceSelection(res);
	    		tmpEditor.setSelection(startSelection, {line:startSelection.line+linecnt,ch:0});
			}
		});  
	}
	// export data download
	,exportDataDownload : function (exportInfo){
		var key = exportInfo.downloadType
		,gubun = exportInfo.gubun
		,tmpName = exportInfo.objName
		,data = exportInfo.item;
		
		var dataArr = data.items;
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
		strHtm.push("							<th tabindex=\"0\" rowspan=\"1\" colspan=\"1\" style=\"width: 40px;\"><input type=\"checkbox\" name=\"columnCheck\" value=\"all\">all</th>");
		strHtm.push("							<th tabindex=\"0\" rowspan=\"1\" colspan=\"1\" style=\"width: 100px;\">Column</th>");
		strHtm.push("							<th tabindex=\"0\" rowspan=\"1\" colspan=\"1\" style=\"width: 100px;\">Desc</th>");
		strHtm.push("						</tr>");
		strHtm.push("					</thead>");
		strHtm.push("					<tbody class=\"dataTableContent1\">");
		var item;
		for(var i=0; i < len; i++){
			item = dataArr[i];
			strHtm.push("						<tr class=\"gradeA add\">	");
			strHtm.push("							<td class=\"\"><input type=\"checkbox\" name=\"columnCheck\" value=\""+item[VARSQLCont.tableColKey.NAME]+"\"></td>	");
			strHtm.push("							<td class=\"\">"+item[VARSQLCont.tableColKey.NAME]+"</td>	");
			strHtm.push("							<td class=\"\">"+(item[VARSQLCont.tableColKey.COMMENT]||'')+"</td>	");
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
		strHtm.push("					<li><span><input type=\"radio\" name=\"exportType\" value=\"excel\"></span>Excel</li>");
		strHtm.push("				</ul>");
		strHtm.push("			</td>");
		strHtm.push("		</tr>");
		strHtm.push("	</table>");
		
		var modalEle = $('#data-export-modal'); 
		if(modalEle.length > 0){
			modalEle.empty();
			modalEle.html(strHtm.join(''));
		}else{
			$(_g_options.hiddenArea).append('<div id=\"data-export-modal\" title="데이타 내보내기">'+strHtm.join('')+'</div>');
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

					var params =VARSQL.util.objectMerge (_g_options.param,{
						exportType : VARSQL.check.radio('input:radio[name="exportType"]')
						,columnInfo : VARSQL.check.getCheckVal("input:checkbox[name='columnCheck']:not([value='all'])").join(',')
						,objectName : tmpName
						,limit: $('#exportCount').val()
					});

					VARSQL.req.download({
						type: 'post'
						,url: {gubun:VARSQL.uri.sql, url:'/base/dataExport.varsql'}
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
		var gubunKey = scriptInfo.gubunKey
			,gubun = scriptInfo.gubun
			,tmpName = scriptInfo.objName
			,data = scriptInfo.item
			,param_yn  = scriptInfo.param_yn;
		
		
		if(_g_options.schema != _g_options.param.schema){
			tmpName = _g_options.param.schema+'.'+tmpName;
		}
				
		gubunKey =gubunKey.split('|');
		
		var key =gubunKey[0]
			,keyMode = gubunKey[1];
		
		param_yn = param_yn?param_yn:'N';
		
		var reval =[];
		
		var dataArr = data.items, tmpval , item;
		
		var len = dataArr.length;
		
		reval.push(VARSQLCont.constants.newline); // 첫라인 줄바꿈으로 시작.
		// select 모든것.
		if(key=='selectStar'){
			reval.push('select * from '+tmpName);
			
		}
		// select 컬럼 값
		else if(key=='select'){
			reval.push('select ');
			for(var i=0; i < len; i++){
				item = dataArr[i];
				reval.push((i==0?'':',')+item[VARSQLCont.tableColKey.NAME]);
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
				reval.push(item[VARSQLCont.tableColKey.NAME]);
				
				valuesStr.push(queryParameter(param_yn, item , keyMode));
				
			}
			reval.push(' )'+VARSQLCont.constants.newline +'values( '+ valuesStr.join('')+' )');
			
		}
		// update 문
		else if(key=='update'){
			reval.push('update '+tmpName+VARSQLCont.constants.newline+' set ');
			
			var keyStr = [];
			var firstFlag = true; 
			
			for(var i=0; i < len; i++){
				item = dataArr[i];
				
				tmpval = queryParameter(param_yn, item, keyMode);
				
				if(item[VARSQLCont.tableColKey.PRIMAY_KEY] =='YES'){
					keyStr.push(item[VARSQLCont.tableColKey.NAME]+ ' = '+ tmpval);
				}else{
					if(!firstFlag){
						reval.push(',');
					}
					reval.push(item[VARSQLCont.tableColKey.NAME]+ ' = '+ tmpval);
					firstFlag = false; 
				}
			}
			
			if(keyStr.length > 0) reval.push(VARSQLCont.constants.newline+'where '+keyStr.join(' and '));
			
		}
		// delete 문
		else if(key=='delete'){
			reval.push('delete from '+tmpName);
			
			var item;
			var keyStr = [];
			var firstFlag = true; 
			
			for(var i=0; i < len; i++){
				item = dataArr[i];
				
				if(item[VARSQLCont.tableColKey.PRIMAY_KEY] == 'YES'){
					tmpval = queryParameter(param_yn, item, keyMode);
					
					keyStr.push(item[VARSQLCont.tableColKey.NAME]+ ' = '+ tmpval);
				}
			}
			
			if(keyStr.length > 0) reval.push(VARSQLCont.constants.newline+'where '+keyStr.join(' and '));
			
		}
		// drop 문
		else if(key=='drop'){
			reval.push('drop table '+tmpName);
		}
		
		_self.addSqlEditContent(reval.join(''));
	}
	// 에디터 영역에 값 넣기.
	,addSqlEditContent :function (cont){
	
		var _self = this
			,insVal = cont +VARSQLCont.constants.querySuffix;
		
		var editObj =_self.getTextAreaObj()
			,insLine = editObj.lastLine(); 
		editObj.replaceRange(insVal, CodeMirror.Pos(insLine));
		editObj.setSelection({line:insLine+1,ch:0}, {line:editObj.lastLine()+1,ch:0});
		editObj.focus();
		
	}
	// sql data grid
	,setGridData: function (pGridData){
		var _self = this; 
		
		var gridObj = $.pubGrid(_self.options.dataGridSelector,{
			height:'auto'
			,autoResize : false
			,page :false
			,headerOptions:{
				view:true
				,sort : true
				,resize:{
					enabled : true
				}
			}
			,asideOptions :{
				lineNumber : {enable : true}				
			}
			,bodyOptions :{
				cellDblClick : function (rowItem){
					var startCursor = _self.getTextAreaObj().getCursor(true);
					
					var cellVal = rowItem.item[rowItem.keyItem.key];
					
					var addLineArr = cellVal.split(VARSQLCont.constants.newline)
						,addLineCnt =addLineArr.length;
					
					_self.getTextAreaObj().replaceSelection(cellVal);
					_self.getTextAreaObj().focus();
					
					if(addLineCnt > 1){
						_self.getTextAreaObj().setCursor({line: startCursor.line+addLineCnt-1, ch:addLineArr[addLineCnt-1].length})
					}else{
						_self.getTextAreaObj().setCursor({line: startCursor.line, ch: startCursor.ch +cellVal.length})
					}
				}
			}
			,tColItem : pGridData.column
			,tbodyItem :pGridData.data
			,rowOptions :{
				contextMenu : {
					beforeSelect :function (){
						$(this).trigger('click');
					}
					,callback: function(key,sObj) {
						if(key =='copy'){
							gridObj.copyData();
							return ; 
						}
						
					},
					items: [
						{key : "copy" , "name": "복사"}
					]
				}
			}
		});
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


/**
 * textCopy 띄우기
 */
_ui.text={
	clipboardObj :false
	,clipBoardEle : false
	,modalEle :false
	,copy :function (copyString){
		var _this = this; 
		
		var strHtm = [];
		
		if(_this.modalEle === false){
			var modalEle = $('#data-copy-modal');
			$(_g_options.hiddenArea).append('<div id=\"data-copy-modal\" title="복사"><div><pre id="data-copy-area"></pre></div></div>');
			modalEle = $('#data-copy-modal'); 
			
			_this.modalEle = modalEle.dialog({
				height: 350
				,width: 640
				,modal: true
				,buttons: {
					"복사":function (){
						$( this ).dialog( "close" );
					}
					,Cancel: function() {
						$( this ).dialog( "close" );
					}
				}
				,close: function() {
				  $( this ).dialog( "close" );
				}
			});
			
			$($('[aria-describedby="data-copy-modal"] .ui-dialog-buttonset button.ui-button')[0]).addClass('varsql-copy-btn');
			new Clipboard('.varsql-copy-btn', {
			  text: function(trigger) {
			    return $('#data-copy-area').html();
			  }
			});
		}else{
			_this.modalEle.dialog( "open" );
		}
		
		$('#data-copy-area').empty().html(copyString);
	}
}
_ui.JAVA = {
	createJavaProgram : function (scriptInfo){
		var _self = this;
		var key = scriptInfo.gubunKey
			,gubun = scriptInfo.gubun
			,tmpName = scriptInfo.objName
			,data = scriptInfo.item
			
		var dataArr = data.items, tmpval , item;
		
		var len = dataArr.length;
		
		var newLine = VARSQLCont.constants.newline
			,tabStr = VARSQLCont.constants.tab;
		
		function javaCreate (createType){
			var codeStr =[];
			
			if(createType =='valid'){
				codeStr.push('import javax.validation.constraints.Max;' +newLine);
				codeStr.push('import javax.validation.constraints.Min;' +newLine);
				codeStr.push('import javax.validation.constraints.NotNull;' +newLine);
				codeStr.push('import javax.validation.constraints.Past;' +newLine);
				codeStr.push('import javax.validation.constraints.Size;' +newLine);
			}
			
			codeStr.push(newLine);
			codeStr.push('//@author'+newLine);
			
			codeStr.push('public class '+capitalizeFirstLetter(convertCamel(tmpName))+'{' +newLine);
			var methodStr = [];
			for(var i=0; i < len; i++){
				item = dataArr[i];
				var tmpDbType = VARSQLCont.dataType.getDbType(item[VARSQLCont.tableColKey.DATA_TYPE])
					,tmpJavaType=tmpDbType.javaType
					,tmpColumnNm = convertCamel(item[VARSQLCont.tableColKey.NAME])
					,tmpMethodNm = capitalizeFirstLetter(tmpColumnNm);
				
				if(createType =='json'){
					codeStr.push(tabStr+'@JsonProperty("'+tmpColumnNm +'")'+newLine);
				}
				
				if(createType =='valid'){
					if(item.IS_NULLABLE =='NO'){
						codeStr.push(tabStr+'@NotNull '+newLine); 
					}
					var columnSize = item[VARSQLCont.tableColKey.SIZE];
					if($.isFunction (tmpDbType.getLen)){
						columnSize = tmpDbType.getLen(columnSize);
					}
					codeStr.push(tabStr+'@Size(max='+columnSize+')'+newLine);
				}
				
				var colComment = item[VARSQLCont.tableColKey.COMMENT];
				colComment = colComment!='' ?' //'+colComment :'';
				
				codeStr.push(tabStr+'private '+tmpJavaType+' ' +tmpColumnNm +';'+colComment+newLine+newLine);
				
				methodStr.push(tabStr+'public '+tmpJavaType+' ' + 'get' +tmpMethodNm +'(){'+newLine);
				methodStr.push(tabStr+tabStr+'return this.'+tmpColumnNm+';'+newLine);
				methodStr.push(tabStr+'}'+newLine);
				
				methodStr.push(tabStr+'public void ' + 'set' +tmpMethodNm +'('+tmpJavaType+' '+tmpColumnNm+'){'+newLine);
				methodStr.push(tabStr+tabStr+'this.'+tmpColumnNm+'='+tmpColumnNm+';'+newLine);
				methodStr.push(tabStr+'}'+newLine);
			}
			
			codeStr.push(methodStr.join('')+newLine);
			codeStr.push('}');
			
			return codeStr.join('');
		}
		var reval = '';
		// java camel case
		if(key=='java_camel_case_naming'){
			reval = javaCreate('default');
		}
		// java_json
		else if(key=='java_json'){
			reval = javaCreate('json');
		}
		// java valid
		else if(key=='java_valid'){
			reval = javaCreate('valid');
		}
		_ui.text.copy(reval);
	}
}
	
function queryParameter(flag, columnInfo , colNameCase){
	var colName = columnInfo[VARSQLCont.tableColKey.NAME]
		, dataType = columnInfo[VARSQLCont.tableColKey.DATA_TYPE]; 
	
	if(flag=='Y'){
		if(colNameCase=='camel'){
			colName = convertCamel(colName);
		}
		return VARSQLCont.constants.queryParameterPrefix+colName+VARSQLCont.constants.queryParameterSuffix;
	}else{
		var tmpType = VARSQLCont.dataType.getDataTypeInfo(dataType);
		
		if(tmpType.isNum===true){
			return 1;
		}else{
			var defaultVal =tmpType.val;
			return defaultVal==""?"'"+toLowerCase(colName)+"'" :defaultVal; 
		}
	}
}

function randomString(strLen) {
	var chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXTZabcdefghiklmnopqrstuvwxyz";
	var randomstring = '';
	for (var i=0; i<strLen; i++) {
	var rnum = Math.floor(Math.random() * chars.length);
	randomstring += chars.substring(rnum,rnum+1);
	}
	
	return randomstring;
}

// camel 변환
function convertCamel(camelStr){
	
    if(camelStr == '') {
        return camelStr;
    }
    camelStr = toLowerCase(camelStr);
    // conversion
    var returnStr = camelStr.replace(/_(\w)/g, function(word) {
        return toUpperCase(word);
    });
    returnStr = returnStr.replace(/_/g, "");
    
    return returnStr; 
}

function toLowerCase(str){
	return (str || '').toLowerCase()
}

function toUpperCase(str){
	return (str || '').toUpperCase()
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

function capitalizeFirstLetter(str) {
    return toUpperCase(str.charAt(0)) + str.slice(1);
}

}(jQuery, window, document,VARSQL));