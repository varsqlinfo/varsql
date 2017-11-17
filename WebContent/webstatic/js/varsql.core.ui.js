/*
**
*ytkim
*varsql ui js
 */
;(function($, window, document, VARSQL) {
"use strict";

var _ui=VARSQL.ui||{};

_ui.base ={
	mimetype : ''	// editor mime type
	,sqlHints :{}	// sql hints
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

_ui.create = function (_opts){
	var _self = this; 
	
	_ui.base.sqlHints = VARSQLCont.dataType.sqlHints(_opts.dbtype);
	_ui.base.mimetype = VARSQLCont.dataType.getMimeType(_opts.dbtype);
	
	_self.initContextMenu();
	_self.headerMenu.init(_opts);
	_self.leftDbObject.create(_opts);
	_self.layout.init(_opts);
}

//context menu 초기화
_ui.initContextMenu  = function (){
	if (document.addEventListener) { // IE >= 9; other browsers
        document.addEventListener('contextmenu', function(e) {
            e.preventDefault();
        }, false);
    } else { // IE < 9
        document.attachEvent('oncontextmenu', function() {
            window.event.returnValue = false;
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
		
		$('body').on('keydown',function (e) {
			var evt =window.event || e; 
			
			if(evt.ctrlKey){
				if (evt.shiftKey) {
					switch (evt.keyCode) {
						case 83: // keyCode 83 is s
							$('.sql-save-btn').trigger('click');
							break;
						default:
							break;
					}
				}
			}
		});
		
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
							$('.sql-new-file').trigger('click');
							break;
						case 'save': // 저장
							$('.sql-save-btn').trigger('click');
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
							_self.openPreferences();
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
				}default:
						break;
				}
		})
	}
	//header 메뉴 환경설정처리.
	,openPreferences : function (){
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
			
			var iframeEle =$($('#preferencesTemplate').find('iframe')); 
			
			var loadUrl = iframeEle.attr('data-load-url');
			iframeEle.attr('src', loadUrl);
		}
		
		_self.preferencesDialog.dialog("open");
	}
	// 데이타 내보내기.
	,exportInfo :function (type){
		alert(type);
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
			$('.CodeMirror.cm-s-default').css('height' ,_h);
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
		
		$.extend(true,_ui.options, _opts);
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
			_ui.options.param.schema =_self.options.active.attr('obj_nm');
			$('#varsql_schema_name').html(_ui.options.param.schema);
			_self._click(this);
		});
		
		$(_self.options.selector+' .db-list-group-item[obj_nm="'+_ui.options.connInfo.schema+'"]').trigger('click');
		
	}
	// 사용자 셋팅 정보 가져오기.
	,_userSettingInfo : function (){
		var _self = this;
		var params = _ui.options.param;
		
		VARSQL.req.ajax({      
		    type:"POST"
		    ,loadSelector : '#db-page-wrapper'
		    ,url:{gubun:VARSQL.uri.sql, url:'/base/userSettingInfo.varsql'}
		    ,dataType:'json'
		    ,data:params 
		    ,success:function (res){
		    	var sqlInfo = res.item;
		    	if(sqlInfo){
		    		_ui.SQL.setQueryInfo(sqlInfo);
		    	}else{
		    		$('#saveSqlTitle').val(VARSQL.util.dateFormat(new Date(), 'yyyymmdd')+'query');
		    	}
			}
			,error :function (data, status, err){
				VARSQL.log.error(data, status, err);
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
		    type:"POST"
		    ,loadSelector : _ui.leftDbObjectServiceMenu.options.left_service_menu_contentId
		    ,url:{gubun:VARSQL.uri.database, url:'/serviceMenu.varsql'}
		    ,dataType:'json'
		    ,data:tmpParam
		    ,success:function (resData){
		    	_ui.leftDbObjectServiceMenu.create(
		    		$.extend({},{param:tmpParam} ,{menuData: resData.items})
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
			delete this.metadataCache[gubun];
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
		    type:"POST"  
		    ,loadSelector : _self.options.left_service_menu_contentId
		    ,url:{gubun:VARSQL.uri.database, url:'/dbObjectList.varsql'}
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
			var cacheData = _self._getMetaCache(param.gubun,param.objectName);
		
			if(cacheData){
				_self[callback].call(_self,cacheData, param);
				return ; 
			}
		}
		
		VARSQL.req.ajax({
		    type:"POST"
		    ,loadSelector : _self.options.metadata_content_area_wrapId
		    ,url:{gubun:VARSQL.uri.database, url:'/dbObjectMetadataList.varsql'}
		    ,dataType:'json'
		    ,async:false
		    ,data:param
		    ,success:function (resData){
		    	_self._setMetaCache(param.gubun,param.objectName, resData); // data cache
		    	_self[callback].call(_self,resData, param);
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
		
		var param =$.extend({},_self.options.param,{'gubun':'table',objectName:sObj.objName})
		
		VARSQL.req.ajax({
		    type:"POST"
		    ,url:{gubun:VARSQL.uri.database, url:'/createDDL.varsql'}
		    ,dataType:'json'
		    ,data:param
		    ,success:function (resData){
		    	if(sObj.gubunKey=='ddl_copy'){
		    		_ui.text.copy(resData.item);
		    	}else{
		    		_ui.SQL.addSqlEditContent(resData.item);
		    	}
			}
			,error :function (data, status, err){
				VARSQL.log.error(data, status, err);
			}
			,beforeSend: _self.loadBeforeSend
			,complete: _self.loadComplete
		});
	}
	// 데이타 내보내기
	,_dataExport : function (exportObj){
		_ui.SQL.exportDataDownload(exportObj);
	}
	// 테이블 정보보기.
	,_tables:function (resData, reqParam){
		var _self = this;
		try{
    		var len = resData.items?resData.items.length:0;
    		var strHtm = [];
    		
			var itemArr = resData.items;
			var item;
			
			var tableHint = {};
			$.each(itemArr , function (_idx, _item){
				tableHint[_item.TABLE_NAME] = {
					columns:[]
					,text :_item.TABLE_NAME
				};
			})
			
			// 테이블 hint;
			VARSQLHints.setTableInfo( tableHint);
			
			$.pubGrid(_self.options.left_service_menu_contentId+'>#tables',{
				height:'auto'
				,autoResize :false
				,page :false
				,tColItem : [
					{key :'TABLE_NAME', label:'Table', width:200, sort:true}
					,{key :'REMARKS', label:'설명'}
				]
				,tbodyItem :itemArr
				,rowOptions :{
					click : function (idx, item){
						var sObj = $(this);
						
		    			var refresh = sObj.attr('refresh')=='Y'?true:false; 
		    			sObj.attr('refresh','N');
		    			
		    			$('.table-list-item.active').removeClass('active');
		    			sObj.addClass('active');
		    			
		    			_self._dbObjectMetadataList($.extend({},_self.options.param,{'gubun':'table','objectName':item.TABLE_NAME}), '_tableMetadata', refresh);
					}
					,contextMenu :{
						beforeSelect :function (){
							$(this).trigger('click');
						}
						,callback: function(key,sObj) {
							var ele = this.element, sItem = this.gridItem;
							var gubun='table'
								,tmpName = sItem.TABLE_NAME;
							
							if(key=='refresh'){
								_self._removeMetaCache(gubun,tmpName);
								ele.attr('refresh','Y');
								ele.trigger('click.pubgridrow');
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
							
							if(key=='export_data'||key=='export_column'){
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
							{key : "refresh" , "name": "새로고침"}
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
							,{key : "create_ddl_top","name": "DDL 보기" 
								,subMenu:[
									{key : "ddl_copy","name": "복사하기"}
									,{key : "ddl_paste","name": "edit 영역에보기"}
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
							,{key :'export', "name": "내보내기" 
								,subMenu:[
									{key : "export_data","name": "데이타 내보내기"}
									,{key : "export_column","name": "컬럼정보 내보내기"}
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
	,_tableMetadata :function (colData ,reqParam){
		var _self = this;
		
		try{
			var colArr = [];
			$.each(colData.items , function (i , item){
				colArr.push(item.COLUMN_NAME);
			});
			
			VARSQLHints.setTableColumns(reqParam.objectName ,colArr);
			
    		var gridObj = {
    			data:colData.items
    			,column : [
					{ label: '컬럼명', key: 'name',width:80 },
					{ label: '데이타타입', key: 'typeName' },
					{ label: '널여부', key: 'nullable',width:45},
					{ label: 'PK', key: 'primayKey',width:45},
					{ label: '설명', key: 'comment',width:45}
				]
    		};
			
    		_self.setMetadataGrid(gridObj, 'table');
 		}catch(e){
			VARSQL.log.info(e);
		}
	}
	//view 정보 보기.
	,_views:function (resData ,reqParam){
		var _self = this;
		try{
			var itemArr = resData.items;
			
			$.pubGrid(_self.options.left_service_menu_contentId+'>#views',{
				headerView:true
				,height:'auto'
				,bigData : {enabled :false}
				,autoResize :false
				,page :false
				,tColItem : [
					{key :'TABLE_NAME', label:'View', width:200, sort:true}
					,{key :'REMARKS', label:'설명'}
				]
				,tbodyItem :itemArr
				,rowOptions : {
					click : function (idx, item){
						var sObj = $(this);
		    			$('.view-list-item.active').removeClass('active');
		    			sObj.addClass('active');
		    			
		    			_self._dbObjectMetadataList($.extend({},_self.options.param,{'gubun':'view','objectName':item.TABLE_NAME}), '_viewMetadata');
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
					{ label: 'PK', key: 'primayKey',width:45},
					{ label: '설명', key: 'comment',width:45}
				]
    		};
			
    		_self.setMetadataGrid(gridObj, 'view');
 		}catch(e){
			VARSQL.log.info(e);
		}
	}
	,_procedures:function (resData ,reqParam){
		var _self = this;
		try{
			var itemArr = resData.items;
			
			$.pubGrid(_self.options.left_service_menu_contentId+'>#procedures',{
				headerView:true
				,height:'auto'
				,bigData : {enabled :false}
				,autoResize :false
				,page :false
				,tColItem : [
					{key :'PROCEDURE_NAME', label:'Procedure',width:200, sort:true}
					,{key :'REMARKS', label:'설명'}
				]
				,tbodyItem :itemArr
				,rowOptions :{
					click : function (idx, item){
						var sObj = $(this);
		    			
		    			$('.procedure-list-item.active').removeClass('active');
		    			sObj.addClass('active');
		    			
		    			_self._dbObjectMetadataList($.extend({},_self.options.param,{'gubun':'procedure','objectName':item.PROCEDURE_NAME}), '_procedureMetadata');
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
					{ label: '컬럼명', key: 'COLUMN_NAME',width:80 },
					{ label: '데이타타입', key: 'TYPE_NAME_SIZE' },
					{ label: '널여부', key: 'NULLABLE',width:45},
					{ label: '설명', key: 'REMARKS',width:45},
				]
    		};
			
    		_self.setMetadataGrid(gridObj, 'procedure');
 		}catch(e){
			VARSQL.log.info(e);
		}
	}
	,_functions:function (resData ,reqParam){
		var _self = this;
		try{
			var itemArr = resData.items;
    				
			$.pubGrid(_self.options.left_service_menu_contentId+'>#functions',{
				headerView:true
				,height: 'auto'
				,bigData : {enabled :false}
				,page :false
				,tColItem : [
					{key :'FUNCTION_NAME', label:'Function',width:200, sort:true}
					,{key :'FUNCTION_TYPE', label:'설명'}
				]
				,tbodyItem :itemArr
				,rowOptions :{
					click : function (idx, item){
						var sObj = $(this);
		    			
		    			$('.function-list-item.active').removeClass('active');
		    			sObj.addClass('active');
		    			
		    			_self._dbObjectMetadataList($.extend({},_self.options.param,{'gubun':'function','objectName':sObj.attr('function_nm')}), '_functionMetadata');
					}
				}
			});
 		}catch(e){
			VARSQL.log.info(e);
		}
	}
	,_functionMetadata : function (colData ,reqParam){
		
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
		}else{
			$.pubGrid(_self.options.metadata_content_area_wrapId+type, {
				headerOptions : {
					redraw : false
				}
				,page :false
				,height:'auto'
				,autoResize :false
				,tColItem : gridData.column
				,tbodyItem :gridData.data
			});
		}
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
		
		_self.sqlTextAreaObj = CodeMirror.fromTextArea(document.getElementById(_self.options.selector.replace('#', '')), {
			mode: _ui.base.mimetype,
			indentWithTabs: true,
			smartIndent: true,
			lineNumbers: true,
			height:'auto',
			 lineWrapping: true,
			matchBrackets : true,
			autofocus: true,
			extraKeys: {"Ctrl-Space": "autocomplete"},
			hintOptions: {tables:tableHint}
		});
	}
	//이벤트 초기화 
	,_initEvent :function (){
		var _self = this; 
	
		var textareaObj = $('.CodeMirror.cm-s-default');
		
		textareaObj.on('keydown',function (e) {
			var evt =window.event || e; 
			
			if(evt.ctrlKey){
				if (evt.altKey) { // keyCode 78 is n
					switch (evt.keyCode) {
						case 78:
							$('.sql-new-file').trigger('click');
							break;
						default:
							break;
					}
				}
				
				if (evt.shiftKey) { // keyCode 70 is f
					switch (evt.keyCode) {
						case 70:
							$('.sql-format-btn').trigger('click');
							break;
						case 83: // keyCode 83 is s
							$('.sql-save-btn').trigger('click');
							break;
						case 88: // toUpperCase
							var sCursor = _self.getTextAreaObj().getCursor(true)
							,eCursor = _self.getTextAreaObj().getCursor(false);
						
							_self.getTextAreaObj().replaceSelection(_self.getSql().toUpperCase());
							_self.getTextAreaObj().setSelection(sCursor, eCursor);
							break;
						case 89: // toLowerCase
							var sCursor = _self.getTextAreaObj().getCursor(true)
							,eCursor = _self.getTextAreaObj().getCursor(false);
						
							_self.getTextAreaObj().replaceSelection(_self.getSql().toLowerCase());
							_self.getTextAreaObj().setSelection(sCursor, eCursor);
							break;
						default:
							break;
					}
					
					return false; 
				}
				
				if (evt.keyCode == 13) { // keyCode 13 is Enter
					$('.sql-execue-btn').trigger('click');
					return false; // preventing default action
				}
			}
		});
		
		textareaObj.on('click', function(e){
			$('#sql_parameter_area').removeClass('on');
		})
		
		// sql 실행
		$('.sql-execue-btn').on('click',function (evt){
			_self.sqlData(evt);
		});
		
		// sql 보내기
		$('.sql-send-btn').on('click',function (evt){
			_self.sqlSend(evt);
		});
		
		// sql 포멧 정리.
		$('.sql-format-btn').on('click',function (){
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
		$('.sql-save-btn').on('click',function (e){
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
				    type:"POST"
				    ,url:{gubun:VARSQL.uri.user, url:'/searchUserList.varsql'}
				    ,dataType:'json'
				    ,data: params
				    ,success:function (data){
				    	//서버에서 json 데이터 response 후 목록에 뿌려주기 위함 VIEWID,UID,UNAME
				    	response(data.items);
					}
					,error :function (data, status, err){
						VARSQL.log.error(data, status, err);
					}
				});  
			}
			,select: function( event, item ) {
				var strHtm = [];
				
				if($('.recv_id_item[_recvid="'+item.VIEWID+'"]').length > 0 ) return false;
				
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
		
		$('.sql-save-list-btn').dropdown();
		$('.sql-save-list-btn').on('click',function (){
			
			if($('.sql-save-list-layer').attr('loadFlag') != 'Y'){
				$('.varsql-dropdown').addClass('on');
				_self.sqlSaveList();
			}
		});
		
		$('.sql-new-file').on('click',function (){
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
	,viewResultColumnType : function (){
		var _self = this; 
		var columnTypeArr = _self._currnetQueryReusltData.column; 
		if(_self._currnetQueryReusltData.viewType != 'grid'){
			columnTypeArr = [];
		}
		
		$.pubGrid(_self.options.dataColumnTypeSelector,{
			height:'auto'
			,autoResize : false
			,page :false
			,headerOptions:{
				view:true
				,displayLineNumber : true	 // 라인 넘버 보기.
				,sort : true
				,resize:{
					enabled : true
				}
			}
			,tColItem : [
				{label: "NAME", key: "key"}
				,{label: "TYPE", key: "dbType"}
			]
			,tbodyItem :columnTypeArr
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
					+'	<td>'
					+'		<div><input type="text" class="sql-param-key" value="{{key}}" /></div>'
					+'	</td>'
					+'	<td>'
					+'		<div><input type="text" class="sql-param-value" value="{{val}}"/></div>'
					+'	</td>'
					+'	<td>'
					+'		<span><button type="button" class="sql-param-del-btn fa fa-minus btn btn-sm btn-default"></button></span>'
					+'	</td>'
					+'	</tr>';
				paramHtm.push(Mustache.render(tmpHtm, {key: key , val : data[key]}));
			}
			
			if('init_data' ==mode){
				$('#sql_parameter_row_area').empty().html(paramHtm.join(''));	
			}else{
				$('#sql_parameter_row_area').append(paramHtm.join(''));
			}
		}else{
			var paramHtm='<tr class="sql-param-row">'
				+'	<td>'
				+'		<div><input type="text" class="sql-param-key" /></div>'
				+'	</td>'
				+'	<td>'
				+'		<div><input type="text" class="sql-param-value"/></div>'
				+'	</td>'
				+'	<td>'
				+'		<span><button type="button" class="sql-param-del-btn fa fa-minus btn btn-sm btn-default"></button></span>'
				+'	</td>'
				+'	</tr>';
			
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
		
		var params =VARSQL.util.objectMerge (_ui.options.param,{
			'sql' :_self.getTextAreaObj().getValue()
			,'sqlTitle' : $('#saveSqlTitle').val()
			,'sqlId' : $('#sql_id').val()
			,'sqlParam' : JSON.stringify(_self.getSqlParam())
		});
		
		VARSQL.req.ajax({      
		    type:"POST"
		    ,loadSelector : '#editorAreaTable'
		    ,url:{gubun:VARSQL.uri.sql, url:'/base/saveQuery.varsql'}
		    ,dataType:'json'
		    ,data:params 
		    ,success:function (res){
		    	$('#sql_id').val(res.item);
		    	_self.sqlSaveList();
		    	_self.currentSqlData = params.sql;
		    	//$(_self.options.preloaderArea +' .preloader-msg').html('저장되었습니다.');
		    	
			}
			,error :function (data, status, err){
				VARSQL.log.error(data, status, err);
			}
			,beforeSend: _self.loadBeforeSend
			,complete: _self.loadComplete
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
						    type:"POST" 
						    ,loadSelector : '#editorAreaTable'
						    ,url:{gubun:VARSQL.uri.user, url:'/sendSql.varsql'}
						    ,dataType:'json'
						    ,data:params 
						    ,success:function (resData){
						    	_self.memoDialog.dialog( "close" );
							}
							,error :function (data, status, err){
								VARSQL.log.error(data, status, err);
							}
							,beforeSend: _self.loadBeforeSend
							,complete: _self.loadComplete
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
					$('.sql-save-btn').trigger('click');
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
		
		var params =VARSQL.util.objectMerge (_ui.options.param,{
			searchVal : $('#saveSqlSearch').val()
			,page : pageNo ? pageNo : $('#sql-save-list-no').val()
			,countPerPage : 5
		});
		
		VARSQL.req.ajax({
		    type:"POST"
		    ,loadSelector : '#editorAreaTable'
		    ,url:{gubun:VARSQL.uri.sql, url:'/base/sqlList.varsql'}
		    ,dataType:'json'
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
		    			$('.sql-save-list-btn').trigger('click');
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
			,error :function (data, status, err){
				VARSQL.log.error(data, status, err);
			}
			,beforeSend: _self.loadBeforeSend
			,complete: _self.loadComplete
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
			var msgStyle = '<style>body{margin:0px;}.error-log-message{font-size:12px;color:#dd4b39}'
				+'.success-log-message{	font-size:12px;color:#000099}</style>';
			
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
			sqlParam[k] = v;
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
		
		sqlVal=$.trim(sqlVal);
		if('' == sqlVal){
			sqlVal  = _self.getTextAreaObj().getValue();
		}
		
		if(''== sqlVal) return ;
		
		var sqlParam = _self.getSqlParam();
		if(!_self.sqlParamCheck(sqlVal, sqlParam)){
			
			return '';
		}
		
		var params =VARSQL.util.objectMerge (_ui.options.param,{
			'sql' :sqlVal
			,'limit' : $(_self.options.limitCnt).val()
			,sqlParam : JSON.stringify(sqlParam)
		});
		
		VARSQL.req.ajax({      
		    type:"POST" 
		    ,loadSelector : '#editorAreaTable'
		    ,url:{gubun:VARSQL.uri.sql, url:'/base/sqlData.varsql'}
		    ,dataType:'json'
		    ,data:params 
		    ,success:function (resData){
		    	try{
		    		var resultLen = resData.length;
		    		
		    		if(resultLen < 1 ){
		    			resData.data = [{result:"데이타가 없습니다."}];
		    			resData.column =[{label:'result',key:'result', align:'center'}];
		    		}
		    		
		    		var item = resData[0];
		    		_self._currnetQueryReusltData =item;
		    		
		    		if(item.resultType=='FAIL' || item.viewType=='msg'){
		    			$(_self.options.dataGridResultTabWrap+" [tab_gubun=msg]").trigger('click');
    				}else{
	    				$(_self.options.dataGridResultTabWrap+" [tab_gubun=result]").trigger('click');
	    			}
	    			
		    		var resultMsg = [];
	    			for(var i=0; i < resultLen; i++){
	    				item = resData[i];
	    				
	    				if(item.viewType=='grid'){
	    					_self.setGridData(item);
	    				}
	    				resultMsg.push('<div class="'+(item.resultType=='FAIL'?'error-log-message':'success-log-message')+'">#resultMsg#</div>'.replace('#resultMsg#' , VARSQL.util.escapeHTML(item.resultMessage)));
	    			}
	    			_self.getResultMsgAreaObj().prepend(resultMsg.join(''));
    				_self.getResultMsgAreaObj().animate({scrollTop: 0},'fast');
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
		
		var params =VARSQL.util.objectMerge (_ui.options.param,{
			'sql' :sqlVal
		});
		
		VARSQL.req.ajax({      
		    type:"POST"
		    ,loadSelector : '#editorAreaTable'
		    ,url:{gubun:VARSQL.uri.sql, url:'/base/sqlFormat.varsql'}
		    ,dataType:'text'
		    ,data:params 
		    ,success:function (res){
		    	var linecnt = VARSQL.matchCount(res,VARSQLCont.constants.newline);
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

					var params =VARSQL.util.objectMerge (_ui.options.param,{
						exportType : VARSQL.check.radio('input:radio[name="exportType"]')
						,columnInfo : VARSQL.check.getCheckVal("input:checkbox[name='columnCheck']:not([value='all'])").join(',')
						,objectName : tmpName
						,limit: $('#exportCount').val()
					});

					VARSQL.req.download(_ui.options.downloadForm, {
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
	/*
	,loadBeforeSend :function (){
		
	}
	,loadComplete :function (){
		
	}
	*/
	// sql data grid
	,setGridData: function (pGridData){
		var _self = this; 
		
		$.pubGrid(_self.options.dataGridSelector,{
			height:'auto'
			,autoResize : false
			,page :false
			,bigData : {
				gridCount : 20		// 화면에 한꺼번에 그리드 할 데이타 gridcount * 3 이 한꺼번에 그려진다. 
				,spaceUnitHeight : 100000	// 그리드 공백 높이 지정
				,horizontalEnableCount : 50
			}
			,headerOptions:{
				view:true
				,displayLineNumber : true	 // 라인 넘버 보기.
				,sort : true
				,resize:{
					enabled : true
				}
			}
			,tColItem : pGridData.column
			,tbodyItem :pGridData.data
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
			$(_ui.options.hiddenArea).append('<div id=\"data-copy-modal\" title="복사"><div><pre id="data-copy-area"></pre></div></div>');
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
				
				codeStr.push(tabStr+'private '+tmpJavaType+' ' +tmpColumnNm +';'+newLine+newLine);
				
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
		var tmpType = VARSQLCont.dataType.dto[dataType];
		
		if(tmpType.isNum===true){
			return 1;
		}else{
			//var colLen  = columnInfo.COLUMN_SIZE;
			return tmpType.val; 
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
    camelStr = camelStr.toLowerCase();
    // conversion
    var returnStr = camelStr.replace(/_(\w)/g, function(word) {
        return word.toUpperCase();
    });
    returnStr = returnStr.replace(/_/g, "");
    
    return returnStr; 
}

function capitalizeFirstLetter(str) {
    return str.charAt(0).toUpperCase() + str.slice(1);
}

VARSQL.ui = _ui;
}(jQuery, window, document,VARSQL));