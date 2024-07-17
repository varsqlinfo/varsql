/**
*ytkim
*varsql db ui js
 */
;(function($, window, document, VARSQL) {
"use strict";

// 전역 변수
var _g_options={
	dbtype:''
	,param:{}
	,hiddenArea : '#dbHiddenArea'
	,downloadForm : '#downloadForm'
	,_opts :{}
	,serviceObject : []
	,screenSetting : {}
	,autoSave :{
		enabled : true
		,delay : (10 *1000) // 10 초.
	}
};

// cache
var _g_all_schema_object={}
	,_g_cache_obj_meta_store ={};
var _g_cache = {
	initSOMetaCacheObject : function (){
		var serviceObject = _g_options.serviceObject;

		for(var i =0; i < serviceObject.length; i++){
			var serviceObj = serviceObject[i];
			var contentid =serviceObj.contentid;

			_g_cache_obj_meta_store[this.getObjectTypeKey(contentid)] = {initFlag:false}; // 초기화 여부
		}
	}
	,getObjectTypeKey : function (objectType){
		return _g_options.param.schema +'.'+ objectType;
	}
	// object cache check
	,isSOMetaInitCache : function (objectType){
		var objectCacheKey = this.getObjectTypeKey(objectType);

		return (_g_cache_obj_meta_store[objectCacheKey]||{}).initFlag===true ? true :false;
	}
	//set  object initflag
	,setSOMetaInitFlag : function (objectType){
		_g_cache_obj_meta_store[this.getObjectTypeKey(objectType)].initFlag=true;
	}
	// 메타 데이터 케쉬된값 꺼내기
	,getSOMetaCache:function (objectType, objecName, tabKey){
		tabKey =tabKey||'column';

		var objectCacheKey = this.getObjectTypeKey(objectType);
		
		var cacheObj = _g_cache_obj_meta_store[objectCacheKey][objecName];

		if(VARSQL.isUndefined(cacheObj)){
			return null;
		}

		var t =cacheObj[tabKey];
		return t?t:null;
	}
	// scheam 별  object cache
	,setCacheSchemaObject : function (objectType , objInfo){
		var cacheSchemaObject = _g_all_schema_object[_g_options.param.schema];

		if(VARSQL.isUndefined(cacheSchemaObject)){
			 cacheSchemaObject ={};
			_g_all_schema_object[_g_options.param.schema] = cacheSchemaObject;
		}
		cacheSchemaObject[objectType] = objInfo;
	}
	// get cache schema object
	,getCacheSchemaObject : function (objectType){
		return (_g_all_schema_object[_g_options.param.schema] ||{})[objectType];

	}
	// 메타 데이터 셋팅하기.
	,setSOMetaCache:function (objectType, objecName, tabKey, data){
		var objectCacheKey = this.getObjectTypeKey(objectType);

		if(VARSQL.isUndefined(_g_cache_obj_meta_store[objectCacheKey][objecName])){
			var objData = {};
			objData[tabKey] = data;
			_g_cache_obj_meta_store[objectCacheKey][objecName] =objData;
		}else{
			_g_cache_obj_meta_store[objectCacheKey][objecName][tabKey]= data;
		}
	}
	// remove service object meta cache
	,removeSOMetaCache:function (objectType, objecName){

		var objectCacheKey = this.getObjectTypeKey(objectType);

		if(typeof objectType !='undefined' && typeof objecName != 'undefined'){
			delete _g_cache_obj_meta_store[objectCacheKey][objecName];
		} else if (typeof objectType !='undefined'){
			_g_cache_obj_meta_store[objectCacheKey] ={};
		} else {
			this.initSOMetaCacheObject();
		}
	}
}
var ALL_COMPONENT_INFO = {
	dbObject : {
		type : 'dbObjectComponent'
		,selector :'#pluginSchemaObject'
		,template : '#dbObjectComponentTemplate'
		,init : function (componentState){
			_ui.dbSchemaObject.init();
		}
		,resize : function (componentState, opt){
			_ui.dbSchemaObject.resizeObjectArea(opt);
		}
	}
	,dbMetadata : {
		type : 'dbMetadataComponent'
		,selector :'#pluginSchemaObject'
		,template : '#dbMetadataComponentTemplate'
		,init : function (componentState){
			_ui.dbObjectMetadata.init();
		}
		,resize : function (componentState, opt){
			_ui.dbObjectMetadata.resizeMetaArea(opt);
		}
	}
	,sqlEditor :{
		type : 'sqlEditorComponent'
		,selector :'#pluginSchemaObject'
		,template : '#sqlEditorComponentTemplate'
		,init : function (componentState){
			_ui.SQL.init();
		}
		,resize : function (componentState, opt){
			_ui.SQL.resize(opt);
		}
	}
	,sqlData : {
		type : 'sqlDataComponent'
		,selector :'#pluginSchemaObject'
		,template : '#sqlDataComponentTemplate'
		,init : function (componentState){
			_ui.sqlDataArea.init();
		}
		,resize : function (componentState, opt){
			_ui.sqlDataArea.resize(opt);
		}
	}
	,plugin : {
		type : 'pluginComponent'
		,template : 'function'
		,init : function (componentState){
			_ui.pluginComponent.init(componentState);
		}
		,resize : function (componentState, opt){
			var componentObj = _ui.component[componentState.key];

			var resizeFn = componentObj.resize;
			if(VARSQL.isFunction(resizeFn)){
				resizeFn.call(componentObj, opt);
			}
		}
	}
}

// sql editor z-index 변경. 
function sqlEditorStyleChange(sqlContainer){
	sqlContainer = sqlContainer.closest('.lm_item.lm_stack');
	sqlContainer.find('>.lm_header').css('z-index',2);
	sqlContainer.find('>.lm_items').css('z-index',2);
}

function registerFn(componentInfo){

	if(_$utils.isEqComponentName('pluginComponent', componentInfo)){
		return function( container, componentState ){

			var componentObj = _ui.component[componentState.key];
			container.getElement().html(componentObj.template());

			var initResize = true;
			container.on('resize',function() {
				if(initResize === true){
					initResize = false;
					return ;
				}
				var resizeFn = componentObj.resize;
				if(VARSQL.isFunction(resizeFn)){

					var componentInfo = container._config.componentState;

					if(componentInfo.initFlag !== true){
				    	return ;
			    	}
					try{
						resizeFn.call(componentObj, componentState, {width : container.width, height : container.height});
					}catch(e){};
				}
			})
		}
	}

	return function( container, componentState ){
	    container.getElement().html($(componentInfo.template).html());
	    container.$isVarComponentRemove = true;

		var initResize = true;
		container.on('resize',function() {
			if(initResize === true){
				initResize = false;
				return ;
			}
			componentInfo.resize(componentState, {width : container.width,height : container.height});
		});
	};
}

var _ui = {
	base :{
		mimetype: ''	// editor mime type
	}
	,extension: {}		// db object 추가 extension
	,component: {} 	//추가 component
};

var _$userMain= top != window ? top.userMain : {isDbActive:function (){return true; }}; // 부모 userMain;

var _$utils = {
	copy : function( target, source ) {
		for( var key in source ) {
			target[ key ] = source[ key ];
		}
		return target;
	}
	,isEqComponentName : function (componentName, componentInfo){
		return componentName == componentInfo.type;
	}
	,getComponentName : function (componentInfo){
		return componentInfo.type;
	}
}

_ui.pluginProxy = {
	createScriptSql :function (scriptObj){
		_ui.SQL.addCreateScriptSql(scriptObj);
	}
	,getActiveObjectMenu : function (){
		return _ui.dbSchemaObject.selectObjectMenu;
	}
	,setMetaTabDataCache : function (objType, cacheItem){
		var metaTabId = _ui.dbObjectMetadata.getActiveTabId(objType);

		if(_g_cache.getSOMetaCache(objType, cacheItem.name, metaTabId)) return ;

		if(metaTabId == 'column'){
			_g_cache.setSOMetaCache(objType,cacheItem.name, 'column', {items:cacheItem.colList});
		}else if(metaTabId == 'info'){

			var colList = [];

			for(var key in cacheItem){
				var val = cacheItem[key];
				if(key=='customField'){
					for(var key2 in val)
					colList.push({
						name : key2
						,val : val[key2]
					});
				}else{
					colList.push({
						name : convertUnderscoreCase(key)
						,val : val
					});
				}
			}

			_g_cache.setSOMetaCache(objType,cacheItem.name, 'info', {items:colList});
		}else{
			_g_cache.setSOMetaCache(objType,cacheItem.name, 'column', {items:cacheItem.colList});
		}
	}
	,refreshObjectInfo : function (objInfo){
		_ui.dbSchemaObject.getObjectTypeData({
			contentid : objInfo.type
			,objectName : objInfo.objectName
			,objectIdx : objInfo.objectIdx
		} ,true)
	}
}

//컨텍스트 메뉴 sql 생성 부분 처리 .
_ui.addDbServiceObject = function(objectInfo){
	_$utils.copy(_ui.dbSchemaObject, objectInfo);
}

_ui.addODbServiceObjectMetadata = function(objectInfo){
	_$utils.copy(_ui.dbObjectMetadata, objectInfo);
}

var _selectorSuffix ={
	TAB :'tab'
	,TAB_CONT :'tabCont'
	,CONT :'cont'
};

var _selector = {
	plugin :{
		'schemaObject' : '#pluginSchemaObject'
		,'objectMeta' : '#pluginObjectMeta'
		,'sqlEditor' : '#pluginSqlEditor'
		,'sqlResult' : '#pluginSqlResult'
		,'glossary' : '#pluginGlossary'
		,'history' : '#pluginHistory'
	}
	,subffix :{
		'tab' : 'Tab'
		,'tabCont' : 'TabContent'
		,'cont' : 'Content'
	}
}

// background click check
$(document).on('mousedown.varsql.background', function (e){
	if(e.which !==2){
		var targetEle = $(e.target);
		if(targetEle.closest('.varsql-menu').length < 1 ){
			$('.varsql-menu .open').removeClass('open');
		}

		if(targetEle.closest('.varsql-widget-layer').length < 1 ){
			$('.varsql-widget-layer.open').removeClass('open');
		}
	}
})

function varsqlLayerClear(){
	$(document).trigger('mousedown.varsql.background');
}

/**
 * 파라미터 얻기.
 * @returns
 */
function _getParam(){
	var arr = [{},_g_options.param];
	for(var i = 0 ; i<arguments.length; i++){
		arr.push(arguments[i]);
	}

	return VARSQL.util.objectMerge.apply(null ,arr);
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

	$(document).on('keydown.main.screen',function (e) {
		var evt =window.event || e;
		if(evt.ctrlKey){
			var returnFlag = true;
			switch (evt.keyCode) {
				case 83: // keyCode 83 is s
					$('.sql_save_btn').trigger('click');
					returnFlag = false;
					break;
				case 70: // 70 is f
					returnFlag = false;
					break;
				case 80: // 80 is p
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

//header 메뉴 처리.
_ui.headerMenu ={
	preferencesDialog : ''
	,dialogObj : {}
	,init : function(){
		var _self = this;
		// theme 설정.
		_self.setThemeInfo(_g_options.screenSetting.mainTheme, true);

		_self.initEvt();
	}
	,initEvt : function (){
		var _self = this;

		// header menu dropdown  init
		$('.varsql-top-menu-label').on('click.header.menu', function (){
			var sEle = $(this);
			var dropDownEle = sEle.closest('.top-menu-button');

			if(dropDownEle.hasClass('open')){
				dropDownEle.removeClass('open');
			}else{
				$('.header-dropdown-submenu.open').removeClass('open');
				dropDownEle.addClass('open');
			}
		}).on('mouseenter.header.menu',function (e){
			var sEle = $(this);
			var eleOpen = $('.varsql-menu>li.open');
			if(eleOpen.length > 0){
				if(eleOpen != sEle){
					eleOpen.removeClass('open');
				}
				sEle.closest('li').addClass('open');
			}
		})
		// submenu view
		$('.header-dropdown-submenu').on('mouseenter.sub.menu', function (){
			var sEle = $(this);
			var dropDownEle = sEle.closest('.header-dropdown-submenu');
			if(!dropDownEle.hasClass('open')){
				$('.header-dropdown-submenu.open').removeClass('open');
				dropDownEle.addClass('open');
			}
		});
		// top menu mouseover submenu hide
		$('.varsql-top-menu >.varsql-menu-item').on('mouseenter.menu.item', function (){
			$('.header-dropdown-submenu.open').removeClass('open');
		});

		$('.varsql-menu').on('click', '.varsql-menu-item', function (e){
			var sEle = $(this);
			var dataMenuItem = sEle.attr('data-menu-item');
			var menuArr = dataMenuItem.split('_');

			var depth1 =menuArr[0]
				,menu_mode2 =menuArr[1]
				,menu_mode3 = menuArr.length > 2 ?menuArr[2] :'';

			if(sEle.hasClass('disable')){
				return ;
			}

			varsqlLayerClear();

			switch (depth1) {
				case 'file': {

					switch (menu_mode2) {
						case 'new':	// 새파일
							$('.sql_toolbar_new_file').trigger('click');
							break;
						case 'save': // 저장
							$('.sql_toolbar_save_btn').trigger('click');
							break;
						case 'allsave': // 모두 저장
							$('.sql_toolbar_allsave_btn').trigger('click');
							break;
						case 'import': // 가져오기
							_self.openPreferences('Table Data Import',VARSQL.url(VARSQL.uri.database, '/menu/fileImport?conuid='+_g_options.param.conuid),{width:800, height:560});
							break;
						case 'export': // 내보내기

							//openMenuDialog : function (title,type ,loadUrl, dialogOpt){

							_self.openMenuDialog(VARSQL.message('export'),'fileExport',{type:VARSQL.uri.database, url:'/menu/fileExport'}, {'width':600,'height' : 400});
							break;
						case 'newwin': // 새창 보기.
							var dimension = VARSQL.util.browserSize();
							var popt = 'width='+(dimension.width)+',height='+(dimension.height)+',scrollbars=1,resizable=1,status=0,toolbar=0,menubar=0,location=0';

							VARSQLUI.popup.open(location.href, {
								name : 'new_win'+_g_options.conuid
								,viewOption : popt
							});

							break;
						case 'close': // 닫기
							var isInIFrame = (window.location != window.parent.location);
							if(isInIFrame==true){
								parent.userMain.activeClose();
							}else {
								if(VARSQL.confirmMessage('msg.close.window')){
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
							_ui.SQL.undo();
							break;
						case 'redo':	// 살리기
							_ui.SQL.redo();
							break;
						case 'compare': //비교
							VARSQLUI.alert.open('['+menu_mode2+'] 준비중입니다.');
							break;
						default:
							break;
					}
					break;
				}case 'search':{
					switch (menu_mode2) {
						case 'find':	// 찾기
							_ui.SQL.findTextOpen();
							break;
						default:
							break;
					}
					break;
				}case 'tool':{
					switch (menu_mode2) {
						case 'import':	//가져오기
							VARSQLUI.alert.open('['+menu_mode2+'] 준비중입니다.');
							break;
						case 'export':	//내보내기
							_self.exportInfo(menu_mode3);
							break;
						case 'setting':	//설정.
							//_self.openPreferences('설정',VARSQL.getContextPathUrl('/database/preferences/main?conuid='+_g_options.param.conuid));

							VARSQLUI.popup.open(VARSQL.getContextPathUrl('/database/preferences/main?conuid='+_g_options.param.conuid), {
								name : 'preferencesMain'+_g_options.param.conuid
								,viewOption : 'width=1000,height=710,scrollbars=1,resizable=1,status=0,toolbar=0,menubar=0,location=0'
							});

							break;
						case 'show':	//추가 항목 보기.

							var componentInfo;
							if(menu_mode3 =='glossary'){
								componentInfo ={
									nm : 'glossary'
									,key : 'glossary'
								};
							}

							if(menu_mode3 =='history'){
								componentInfo ={
									nm : 'history'
									,key : 'history'
								};
							}

							if(!VARSQL.isUndefined(componentInfo)) {
								_ui.layout.addComponent(componentInfo);
							}

							break;
						case 'theme':	//테마 설정
							_self.setThemeInfo(menu_mode3);

							break;
						case 'diff':	//테마 설정
							_self.textDiff();

							break;
						case 'layout':	//레이아웃 초기화
							if(VARSQL.confirmMessage('msg.layout.restore.confirm')){
								_ui.preferences.save('init', function (){
									location.href = location.href;
									return ;
								});
							}
							break;
						case 'utils':	//유틸리티
							var componentInfo;
							if(menu_mode3 =='gentable'){
								var popt = 'width=1000,height=600,scrollbars=1,resizable=1,status=0,toolbar=0,menubar=0,location=0';

								VARSQLUI.popup.open(VARSQL.getContextPathUrl('/database/utils/genTable?conuid='+_g_options.param.conuid), {
									name : 'gentable'+_g_options.param.conuid
									,viewOption : popt
								});

								return ;
							}
							
							if(menu_mode3 =='multidbsqlexecute'){
								var popt = 'width=1280,height=700,scrollbars=1,resizable=1,status=0,toolbar=0,menubar=0,location=0';

								VARSQLUI.popup.open(VARSQL.getContextPathUrl('/database/utils/multiDbSqlExecute?conuid='+_g_options.param.conuid), {
									name : 'multidbsqlexecute'+_g_options.param.conuid
									,viewOption : popt
								});

								return ;
							}
							
							if(menu_mode3 =='tableddlconvert'){
								_self.openPreferences(VARSQL.message('header.menu.tool.utils.tableddlconvert')
								,VARSQL.getContextPathUrl('/database/utils/tableDDLConvert?conuid='+_g_options.param.conuid)
								,{
									width:800,height:470
								});
								return ;
							}
							
							if(menu_mode3 =='tablecolumnsearch'){
								_self.openPreferences(VARSQL.message('header.menu.tool.utils.tablecolumnsearch'),
								VARSQL.getContextPathUrl('/database/utils/tableColumnSearch?conuid='+_g_options.param.conuid)
								,{
									width:800,height:500
								});
								return ;
							}

							break;
						default:
							break;
					}
					break;
				}case 'help':{
					switch (menu_mode2) {
						case 'help':	//도움말
							VARSQLUI.alert.open('['+menu_mode2+'] 준비중입니다.');
							break;
						case 'dbinfo':	//정보 보기.

							if(_self.dialogObj['dbInfo']){
								_self.dialogObj['dbInfo'].dialog( "open" );
								return ;
							}

							var param = {
								conuid : _g_options.param.conuid
							}
							VARSQL.req.ajax({
								url:{type:VARSQL.uri.database, url:'/dbInfo'}
								,data: param
								,success:function (resData){
									var list = resData.list;

									var strHtm =[];

									var itemName = '', itemVal='', etc='';
									for(var i =0 , len = list.length; i < len ;i++){
										var item = list [i];

										itemName = '', itemVal='', etc='';

										for(var key in item){
											var keyLowerVal = key.toLowerCase();
											if(keyLowerVal.indexOf('name') >0){
												itemName = item[key]||'';
											}else if(keyLowerVal.indexOf('val') >0){
												itemVal = item[key]||'';
											}else{
												etc +=item[key]||'';
											}
										}

										strHtm.push(itemName != '' ? itemName+' : ' : '');
										strHtm.push(itemVal != '' ? itemVal+'  ' : '');
										strHtm.push(etc != '' ? etc:'');
										strHtm.push('<br/>');
									}

									$('#epHeaderDialogDbInfo').html(strHtm.join(''));

									_self.dialogObj['dbInfo'] = $('#aboutDbInfoDialog').dialog({
										height: 300
										,width: 500
										,modal: true
										,buttons: {
											Ok:function (){
												_self.dialogObj['dbInfo'].dialog( "close" );
											}
										}
										,close: function() {
											_self.dialogObj['dbInfo'].dialog( "close" );
										}
									});
								}
							});

							break;
						case 'info':	//정보 보기.
							var aboutDialog = $('#aboutVarsqlDialog').dialog({
								height: 200
								,width: 400
								,modal: true
								,buttons: {
									Ok:function (){
										aboutDialog.dialog( "close" );
									}
								}
								,close: function() {
									aboutDialog.dialog( "close" );
								}
							});
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
	,openMenuDialog : function (title,type ,loadUrl, dialogOpt){
		var _self = this;
		var dialogEleObj = _self.dialogObj[type];
		if(!VARSQL.isUndefined(dialogEleObj)){
			dialogEleObj.dialog( "open" );
			return ;
		}

		var param = {
			conuid : _g_options.param.conuid
			,prefKey : type
		}

		VARSQL.req.ajax({
			url:loadUrl
			,data: param
			,dataType : 'text'
			,success:function (resData){
				var eleId = 'openMenuDialog_'+type;

				$('#varsqlExtensionsElementArea').append('<div id="'+eleId+'"></div>');
				eleId = '#'+eleId;

				var ele =$(eleId);
				ele.html(resData);
				ele.attr('title',title);

				var menuDialog =VARSQLUI.dialog.open(eleId, VARSQL.util.objectMerge({
					width :  450
					,height : 400
					,modal: true
					,resizable: false
					,buttons: {
						Close:function (){
							menuDialog.dialog( "close" );
						}
					}
					,close: function() {
						menuDialog.dialog( "close" );
					}
				},dialogOpt));

				menuDialog.dialog("open");

				_self.dialogObj[type] = menuDialog;
			}
		});
	}
	// 데이터 내보내기.
	,exportInfo :function (type){
		var _self = this;

		if(type=='spec'){
			_self.openPreferences('Table Spec Export',VARSQL.getContextPathUrl('/database/tools/export/specMain?conuid='+_g_options.param.conuid),{width:800});
		}else if(type=='ddl'){
			_self.openPreferences('DDL Export',VARSQL.getContextPathUrl('/database/tools/export/ddlMain?conuid='+_g_options.param.conuid), {width:800,height:500});
		}else if(type=='tableData'){
			_self.openPreferences('Table Data Export',VARSQL.getContextPathUrl('/database/tools/export/tableDataExport?conuid='+_g_options.param.conuid),{width:800});
		}
	}
	//header 메뉴 환경설정처리.
	,openPreferences : function (title, loadUrl, opt){
		var _self = this;
		
		const opts = VARSQL.util.objectMerge({width:700,height:470}, opt??{});
		
		_self.preferencesDialog = $('#preferencesTemplate').dialog({
			height: opts.height
			,width: opts.width
			,modal: true
			,buttons: {
				Close:function (){
					_self.preferencesDialog.dialog( "close" );
				}
			}
			,close: function() {
				_self.preferencesDialog.dialog( "close" );
			}
		});

		VARSQLUI.frame.open('#preferencesTemplate iframe', loadUrl)

		_self.preferencesDialog.dialog("open").parent().find('.ui-dialog-title').html(title);
	}
	// 테마 설정.
	,setThemeInfo : function (themeName, initFlag){

		VARSQLUI.theme(_g_options.param.conuid, themeName);

		if(initFlag !== true){
			_ui.SQL.sqlMainEditor.updateEditorOptions({ theme: 'vs-'+ themeName});
			
			_g_options.screenSetting.mainTheme = themeName;
			_ui.preferences.save({mainTheme : themeName});
		}
	}
	// text diff
	,textDiff : function (original, modified){
		VARSQLUI.popup.open(VARSQL.getContextPathUrl('/database/utils/diff?conuid='+_g_options.param.conuid), {
			name : 'textDiff'+_g_options.param.conuid
			,viewOption : 'width=1000,height=710,scrollbars=1,resizable=1,status=0,toolbar=0,menubar=0,location=0'
			,method :'post'
			,isNewYn : 'Y'
			,param :{
				orgin: (original||''),
				modified: (modified||'')
			}
		});
	}
}


// 환경 설정 관련
_ui.preferences= {
	save : function (prefInfo , callback){

		if(!_$userMain.isDbActive(_g_options.param.conuid)){
			return ;
		}

		if(prefInfo =='init'){
			_g_options.screenSetting = {} ;
		}else{
			_g_options.screenSetting = VARSQL.util.objectMerge(_g_options.screenSetting, prefInfo);
		}

		VARSQLApi.preferences.save({
			conuid : _g_options.param.conuid
			,prefKey : 'main.database.setting'
			,prefVal : JSON.stringify(_g_options.screenSetting)
		}, callback);
	}
}

// layoutObject
_ui.layout = {
	layoutObj :false
	,mainObj :{} //main layout 처리.
	,init : function(_opts){
		var _self = this;
		_self.initEvt();

		_self.setLayout();
	}
	,initEvt : function (){
		var _self = this;
		var splitterInitFlag = false;
		
		var boardEle = $('#varsqlBoardWrapper'); 
		// board toggle btn
		$('.database-board-view-btn').on('click.viewbtn', function (e){
			
			if(splitterInitFlag === false){
				splitterInitFlag = true;
				$('#mainArticleFrame').attr('src', _g_options.boardUrl);
			}

			if(boardEle.hasClass('on')){
				boardEle.removeClass('on');
			}else{
				boardEle.addClass('on');
			}
		})
		// close btn
		$('.database-board-close-btn').on('click', function (){
			$('.database-board-view-btn').trigger('click.viewbtn');
		})
	}
	,layoutResize : function (){
		this.mainObj.updateSize();
	}
	,setLayout: function (){
		var _self = this;

		var config = {
		  settings: {
		    hasHeaders: true,
		    constrainDragToContainer: true,
		    reorderEnabled: true,
		    selectionEnabled: false,
		    popoutWholeStack: false,
		    blockedPopoutsThrowError: true,
		    closePopoutsOnUnload: true,
		    showPopoutIcon: false,
		    showMaximiseIcon: true,
		    showCloseIcon: false
		  },
		  dimensions: {
		    borderWidth: 5,
		    minItemHeight: 10,
		    minItemWidth: 10,
		    headerHeight: 20,
		    dragProxyWidth: 300,
		    dragProxyHeight: 200
		  },
		  labels: {
		    close: 'close',
		    maximise: 'maximise',
		    minimise: 'minimise',
		  },
		  content: [{
			type:'row'
			,content : [
				{
				type: 'row',
				content: [
				{
				  type: 'column',
				  width:30 ,
				  content: [{
					type: 'component',
					id : 'dbObject',
					height :60,
					componentName: 'dbObjectComponent',
					componentState:{},
					title:'serviceObject',
					isClosable :false
				  }, {
					type: 'component',
					height: 40,
					id : 'dbMetadata',
					componentName: 'dbMetadataComponent',
					componentState:{},
					title: 'Meta',
					isClosable :false
				  }]
				},
				{
				  type: 'column',
				  width: 70,
				  content: [{
					type: 'component',
					id : 'sqlEditor',
					componentName: 'sqlEditorComponent',
					componentState:{},
					title: 'Editor',
					isClosable :false
				  }, {
					type: 'component',
					id : 'sqlData',
					componentName: 'sqlDataComponent',
					componentState:{},
					title: 'sql result',
					isClosable :false
				  }]
				}]
			  }]
		  }]
		};

		try{
			if(!VARSQL.isBlank(_g_options.screenSetting.layoutConfig)){
				var currConfig = JSON.parse(_g_options.screenSetting.layoutConfig);
				config = !VARSQL.isBlank(currConfig) ? currConfig : config;
			}
		}catch(e){}

		var varsqlLayout = new GoldenLayout(config, $('#varsqlBodyWrapper'));

		for(var key in ALL_COMPONENT_INFO){
			var componentInfo =ALL_COMPONENT_INFO[key];
			var componentName = _$utils.getComponentName(componentInfo);

			varsqlLayout.registerComponent(componentName, registerFn(componentInfo));
		}

		// component create
		varsqlLayout.on('componentCreated', function( component ){

			if(component.container.$isVarComponentRemove ===true){
				component.container.tab.closeElement.remove();
			}

			var componentInfo = component.config.componentState;

			var componentName = component.componentName;

			if(componentInfo.isDynamicAdd == true){
				delete componentInfo.isDynamicAdd;
				return ;
			}

			if(component.tab.isActive){
				_self.initComponent(componentName, componentInfo);
			}
		});

		// title click
		varsqlLayout.on('tabCreated', function( tab ){
			tab.titleElement.on('dblclick', function (e){
				tab.contentItem.parent.toggleMaximise();
			})
		});

		// item destroy
		varsqlLayout.on('itemDestroyed', function( component ){
			if(component.type=='component' && _$utils.isEqComponentName(component.componentName, ALL_COMPONENT_INFO.plugin)){
				var componentInfo = component.config.componentState;

				if(componentInfo.initFlag !== true) return ;

				var componentObj = _ui.component[componentInfo.key];
				var destroyFn = componentObj.destroy;

				if(VARSQL.isFunction(destroyFn)){
					destroyFn.call(componentObj);
				}
			}
		});

		varsqlLayout.on('stackCreated', function( stack ){

			var items = stack.contentItems;

			for(var i =0 ;i < items.length; i++){
				items[i].config.componentState.initFlag = false;
			}
		});

		// layout chage event
		varsqlLayout.on('activeContentItemChanged', function( contentItem ){

	    	var componentName = contentItem.componentName;
	    	var componentInfo = contentItem.config.componentState;

	    	if(componentInfo.initFlag !== true && contentItem.isInitialised ===true){
		    	_self.initComponent(componentName, componentInfo);
	    	}
	    	
			if(contentItem.config.id=='sqlEditor'){
				sqlEditorStyleChange($(contentItem.element));
			}
	    });

		// layout ready
		varsqlLayout.on('initialised', function( contentItem ){
			var layoutSaveTimer;
			var firstFlag = true;

			varsqlLayout.on('stateChanged', function(a1){

				if(firstFlag){
					firstFlag = false;
					return;
				}

				if(!a1 || varsqlLayout._maximisedItem) return ;
				
				clearTimeout(layoutSaveTimer);

				layoutSaveTimer = setTimeout(function() {
					_ui.preferences.save({layoutConfig : JSON.stringify( varsqlLayout.toConfig())});
				}, 300);
			});
	    });

		varsqlLayout.init();

		var windowResizeTimer;
		$(window).resize(function() {
			clearTimeout(windowResizeTimer);
			windowResizeTimer = setTimeout(function() {
				varsqlLayout.updateSize();
			}, 20);
		})

		_self.mainObj = varsqlLayout;
	}
	,initComponent : function (componentName, componentInfo){
		componentInfo.initFlag = true;

		var componentKey = componentName.replace(new RegExp('Component$'), '');

		ALL_COMPONENT_INFO[componentKey].init(componentInfo);
	}
	// tab active
	,setActiveTab : function (tabKey){
		var varsqlLayout =this.mainObj;

		var items = varsqlLayout.root.getItemsById(tabKey);

		if(items.length > 0){
			var contentItem= items[0];

			if(!contentItem.tab.isActive){
				contentItem.tab.header.parent.setActiveContentItem(contentItem);
				contentItem.setSize();
			}
			return true;
		}

		return false;
	}
	// add custom component
	,addComponent : function (addItemInfo){

		var varsqlLayout =this.mainObj;

		if(this.setActiveTab(addItemInfo.key)){
			return ;
		}

		var pluginComponentName = _$utils.getComponentName(ALL_COMPONENT_INFO.plugin);
		var pluginItem = varsqlLayout.root._$getItemsByProperty('componentName', pluginComponentName);

		var pluginLen = pluginItem.length;

		addItemInfo.isDynamicAdd = true;

		if(pluginLen > 0){
			(pluginItem[pluginLen-1].parent).addChild({
				title: addItemInfo.nm
			    ,type: 'component'
			    ,id : addItemInfo.key
			    ,componentName: pluginComponentName
			    ,componentState: addItemInfo
			})
		}else{
			varsqlLayout.root.contentItems[0].addChild({
				title: addItemInfo.nm
			    ,type: 'component'
			    ,id : addItemInfo.key
			    ,componentName: pluginComponentName
			    ,componentState: addItemInfo
			})
		}

		if(pluginLen < 1){
			varsqlLayout.root.getItemsById(addItemInfo.key)[0].container.setSize(250);
		}

		this.initComponent(pluginComponentName, addItemInfo);
	}
}

// plugin add
_ui.registerPlugin = function ( regItem){
	_$utils.copy(_ui.component,regItem);
}

// plugin
_ui.pluginComponent ={
	// plugin component 초기화
	init :  function (itemInfo){
		itemInfo.initFlag = true;
		var componentObj = _ui.component[itemInfo.key];
		var initFn = componentObj.init;

		if(VARSQL.isFunction(initFn)){
			initFn.call(componentObj);
		}
	}
}

// db schema object 처리.
_ui.dbSchemaObject ={
	initObjectMenu : false
	,selectObjectMenu : {}
	,options :{}
	,selector : {
		schemaObject : _selector.plugin.schemaObject
		,objectTypeTab : '#pluginSchemaObjectTab'
	}
	,objectTypeTab : false
	,init :function (){
		var _self = this;

		var _opts = _g_options;

		if(!_g_options.dbtype) {
			VARSQLUI.alert.open('dbtype empty');
			return ;
		}

		_g_cache.initSOMetaCacheObject();

		VARSQL.util.objectMerge(_self.options, _opts);
		
		_self.initSchema();
		_self._initObjectTypeTab();
		_self.initEvt();

	}
	// 사용자가 선택한 스키마 셋팅
	,initSchema : function (){
		var selectSchema =_g_options.screenSetting.selectSchema; 
		
		var schemaEle = $(this.selector.schemaObject+' .db-schema-item[obj_nm="'+selectSchema+'"]');
		 
		if(schemaEle.length > 0){
			if(!schemaEle.hasClass('active')){
				$(this.selector.schemaObject+' .db-schema-item.active').removeClass('active');
				schemaEle.addClass('active');	
				$('#varsqlSchemaName').val(selectSchema);
			}
			
			_g_options.param.schema = selectSchema;
		}
	}
	// init left event
	,initEvt : function (){
		var _self = this;

		$(_self.selector.schemaObject +' .db-schema-list-btn').on('click', function (){
			var viewArea = $(this).closest('.schema-view-btn');

			if(viewArea.hasClass('open')){
				viewArea.removeClass('open');
			}else{
				viewArea.addClass('open');
			}
		})

		// db default schema click
		$(_self.selector.schemaObject +' .default_db_view_btn').on('click', function (){
			$(_self.selector.schemaObject+' .db-schema-item.db-schema-default').trigger('click');
		})

		// 스키마 클릭.
		$(_self.selector.schemaObject +' .db-schema-item').on('click', function (){

			var sEle = $(this);

			if(sEle.hasClass('active')){
				return ;
			}else{
				$(_self.selector.schemaObject+' .db-schema-item.active').removeClass('active');
				sEle.addClass('active');
			}

			varsqlLayerClear();
			var objNm =sEle.attr('obj_nm'); 
			_g_options.param.schema = objNm;

			$('#varsqlSchemaName').val(_g_options.param.schema);
			
			_ui.preferences.save({selectSchema :  objNm});

			_g_cache.initSOMetaCacheObject();
			_self.selectObjectMenu = {};

			_self.objectTypeTab.itemClick(0);

		});
	}
	// db object type tab
	,_initObjectTypeTab : function (){
		var _self = this;
		
		_self.objectTypeTab = $.pubTab(_self.selector.objectTypeTab, {
			items : _g_options.serviceObject
			,dropdown : {
				width : 100
				,heightResponsive : true
			}
			,itemKey :{							// item key mapping
				title :'name'
				,id: 'contentid'
			}
			,titleIcon :{
				left : {
					html : '<i class="fa fa-refresh" style="cursor:pointer;"></i>'
					,onlyActiveView : true
					,click : function (item, idx){
						if(VARSQL.confirmMessage('msg.refresh')){
							_self.getObjectTypeData(item, true);
						}
					}
				}
			}
			,click : function (item){
				_self.selectObjectMenu = item;
				_self.getObjectTypeData(item);
			}
		});
	}
	// 클릭시 텝메뉴에 해당하는 메뉴 그리기
	,getObjectTypeData : function(selItem, refresh){
		var _self = this;
		var $contentId = selItem.contentid;

		if(_self.objectTypeTab.isActive(selItem)){

			var serviceGridObj = $.pubGrid(_self.objectTypeTab.getTabContentSelector(selItem));

			if(serviceGridObj){
				serviceGridObj.resizeDraw();
			}
		}

		_self.getObjectMetadata({'objectType':$contentId, 'visible' :true});

		var objectInitFlag = _g_cache.isSOMetaInitCache($contentId);

		if(refresh !== true && objectInitFlag){
			return ;
		}

		var callMethod = this['_'+$contentId];

		var param =_getParam({'objectType':$contentId, 'objectNames' : selItem.objectName , 'objectIdx' : selItem.objectIdx});

		if(refresh !== true){

			var cacheObj = _g_cache.getCacheSchemaObject($contentId);

			if(!VARSQL.isUndefined(cacheObj)){
				callMethod.call(_self, cacheObj, param);
				return ;
			}
		}

		_g_cache.removeSOMetaCache($contentId, selItem.objectName);

		param.refresh = refresh;
		
		VARSQL.req.ajax({
			loadSelector : _self.selector.schemaObject 
			,url:{type:VARSQL.uri.database, url:'/dbObjectList'}
			,data : param
			,success:function (resData){
				_g_cache.setSOMetaInitFlag($contentId, true);
				_g_cache.setCacheSchemaObject($contentId, resData); // object cache
				callMethod.call(_self, resData, param);
				
				if(VARSQL.isBlank(selItem.objectName)){
					_self.getObjectMetadata({'objectType':$contentId, 'initFlag' :true});
				}
			}
		});

		if(_g_options.lazyload === true){
			param.custom = {allMetadata : "Y"};

			VARSQL.req.ajax({
				url:{type:VARSQL.uri.database, url:'/dbObjectList'}
				,data : param
				,success:function (resData){
					resData.refreshFlag = false;
					callMethod.call(_self,resData);
					_g_cache.setCacheSchemaObject($contentId, resData); // object cache
				}
			});
		}
	}
	// resize object area
	,resizeObjectArea : function (dimension){
		// tab resize
		if(this.objectTypeTab){
			this.objectTypeTab.resize();
			var gridObj = $.pubGrid(this.objectTypeTab.getTabContentSelector(this.selectObjectMenu));

			if(gridObj){
				gridObj.resizeDraw();
			}
		}
	}
	// 데이터 내보내기
	,_dataExport : function (exportObj){
		_ui.SQL.exportDataDownload(exportObj);
	}
	,getObjectMetadata : function (param, refresh){
		if(param.objectInfo){
			_ui.pluginProxy.setMetaTabDataCache(param.objectType, param.objectInfo);
		}
		
		_ui.dbObjectMetadata.getServiceObjectMetadata(param, refresh);
	}
};

//table
_ui.addDbServiceObject({
	objectGridObj : {},
	_table : function (resData, reqParam){

		var _self = this;
		try{
			var $$objectType = 'table';
			var itemArr = resData.list;

			_ui.SQL.addEditorHint(_g_options.param.schema,'table',itemArr);
			
			if(reqParam.refresh ==true  && !VARSQL.isBlank(reqParam.objectNames)){
				
				var refreshTableName = reqParam.objectNames;
				
				for(var item of itemArr){
					if(item.name == refreshTableName){
						_self.objectGridObj.updateRow(reqParam.objectIdx, item, true);
						break; 
					}
				}
				return ;
			}

			if(resData.refreshFlag===false) return ;

			var contextItems = [
				{header: "title" , "key": "contextTitle"}
				,{divider:true}
				,{key : "dataview" , "name": VARSQL.message('dataview')
					,subMenu: [
						{ key : "dataview_all","name": VARSQL.message('data') , mode: "selectStar"}
						,{ key : "dataview_count","name": VARSQL.message('count') ,mode:"selectCount"}
					]
				}
				,{key : "copy" , "name": VARSQL.message('copy')}
				,{divider:true}
				,{key : "sql_create", "name": "sql"
					,subMenu: [
						{ key : "selectStar","name": "select *" , mode: "selectStar"}
						,{ key : "select","name": "select column" ,mode:"select"}
						,{ key : "selectCount","name": "select count" ,mode:"selectCount"}
						,{ key : "insert","name": "insert" , mode:"insert"}
						,{ key : "update","name": "update" ,mode:"update"}
						,{ key : "delete","name": "delete" ,mode:"delete"}
						,{ key : "drop","name": "drop" , mode:"drop"}
					]
				}
			];

			var settingItems = _g_options.userSettingInfo['main.contextmenu.serviceobject'];

			settingItems = VARSQL.isArray(settingItems) ? settingItems : [];

			var userContextItems = [];
			for(var i =0 ;i <settingItems.length;i++){
				var userItem = settingItems[i];

				var pItemKey = 'uCustomItem_'+i;

				var addItem = {key : pItemKey	,name : userItem.name ,subMenu : []};

				var templateInfos = userItem.templateInfos;

				for(var j =0 ;j < templateInfos.length; j++){
					var templateInfo = templateInfos[j];
					templateInfo.key = pItemKey +'_'+ j;
					templateInfo.isTemplate =true;

					addItem.subMenu.push(templateInfo);
				}
				userContextItems.push(addItem);
			}
			contextItems = contextItems.concat(userContextItems);

			contextItems = contextItems.concat([
				{divider:true}
				,{key :'export', "name": VARSQL.message('export')
					,subMenu:[
						{key : "export_data","name": VARSQL.message('data.export')}
					]
				}
				,{divider:true}
				,{key : "refresh" , "name": VARSQL.message('refresh')}
			]);
			
			var tableObj = $.pubGrid(_self.objectTypeTab.getTabContentSelector({contentid: $$objectType}),{
				setting : {
					enabled : true
					,click : false
					,enableSearch : true
					,callback : function (data){
						_ui.preferences.save({tablesConfig : data.item});
						_g_options.screenSetting.tablesConfig = data.item;
					}
					,configVal : _g_options.screenSetting.tablesConfig
				}
				,asideOptions :{
					lineNumber : {enabled : true, width : 30, align: 'right'}
				}
				,tColItem : VARSQLCont.getMainObjectServiceHeader($$objectType)
				,scroll :{	// 스크롤 옵션
					vertical : {
						speed : 2			// 스크롤 스피드 row 1
					}
				}
				,tbodyItem :itemArr
				,bodyOptions :{
					cellDblClick : function (rowItem){
						var selKey =rowItem.keyItem.key;

						if(selKey == 'name' ){
							var item  = rowItem.item;

							_ui.SQL._sqlExecute('select * from '+ getTableName(item),false);
						}
					}
					,keyNavHandler : function(moveInfo){

						if(moveInfo.key == 13){
							return false;
						}else{
							_self.getObjectMetadata({'objectType':$$objectType, 'objectName':moveInfo.item.name, 'objectInfo' : moveInfo.item});
						}
					}
				}
				,rowOptions :{
					click : function (rowInfo){
						var item = rowInfo.item;

		    			_self.getObjectMetadata({'objectType':$$objectType, 'objectName':item.name, 'objectInfo' : item}, true);
					}
					,contextMenu :{
						beforeSelect :function (){
							var itemInfo = tableObj.getRowItemToElement($(this));
							tableObj.config.rowContext.changeHeader('contextTitle',0,itemInfo.item.name);
						}
						//editor 없을대 처리, 경고창으로 대체 함.
						,disableItemKey : function (items){
							if(!_ui.SQL.getSqlEditorObj()){
								return [
									{key :'sql_create' , depth :0	}
								];
							}
							return [];
						}
						,callback: function(key,sObj) {
							var sItem = this.gridItem;
							var tmpName = sItem.name;
							
							if(key=='dataview_all'){
								_ui.SQL._sqlExecute('select * from '+getTableName(sItem),false);
								return ;
							}else if(key=='dataview_count'){
								_ui.SQL._sqlExecute('select count(1) CNT from '+getTableName(sItem),false);
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

							if(key == 'refresh'){
								_ui.pluginProxy.refreshObjectInfo({type : $$objectType , objectName : tmpName, objectIdx : this.rowIdx});
								return ;
							}

							var cacheData = _g_cache.getSOMetaCache($$objectType,tmpName, 'column');
							
							if(VARSQL.isBlank(cacheData) && sItem.colList){
								_g_cache.setSOMetaCache($$objectType, tmpName, 'column', sItem.colList);
								cacheData = {items:sItem.colList};
							}
							
							var params ={
								objectType : $$objectType
								,gubunKey :key
								,objName : tmpName
								,objInfo : sItem
								,item : cacheData
							};

							if(key=='export_data'){
								_self._dataExport(params);
								return ;
							}

							if(sObj.isTemplate===true){

								var result =VARSQLTemplate.render.generateSource(sObj, {
					                'table' : sItem
					                ,'columns' : cacheData.items
					            });

								if(result.isError){
									VARSQL.toastMessage('msg.setting.fail.check');
					            	return ;
					    		}

								var resultValue = result.value;
								
								if(!_ui.SQL.getSqlEditorObj() || sObj.viewMode != 'editor'){
									_ui.text.copy(resultValue, 'java');
									return ;
								}else{
									_ui.SQL.addSqlEditContent(resultValue, false);	
								}
					            
								return ;
							}

							params.sqlGenType = sObj.mode;
							params.param_yn = sObj.param_yn;
							_ui.pluginProxy.createScriptSql(params);
						},
						items: contextItems
					}
				}
			});

			_self.objectGridObj = tableObj;
		}catch(e){
			VARSQL.log.info(e);
		}
	}
})

// view object grid
_ui.addDbServiceObject({
	_view:function (resData ,reqParam){
		var _self = this;
		try{
			var $$objectType = 'view';
			var itemArr = resData.list;
			
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
			})
			
			_ui.SQL.addEditorHint(_g_options.param.schema,'table',itemArr);

			var viewObj = $.pubGrid(_self.objectTypeTab.getTabContentSelector({contentid: $$objectType}),{
				asideOptions :{
					lineNumber : {enabled : true, width : 30, align: 'right'}
				}
				,tColItem : VARSQLCont.getMainObjectServiceHeader($$objectType)
				,tbodyItem : itemArr
				,setting : {
					enabled : true
					,enableSearch : true
				}
				,bodyOptions :{
					cellDblClick : function (rowItem){
						var selKey =rowItem.keyItem.key;

						if(selKey == 'name' ){
							var item  = rowItem.item;
							_ui.SQL._sqlExecute('select * from '+ getTableName(item),false);
						}
					}
					,keyNavHandler : function(moveInfo){

						if(moveInfo.key == 13){
							return false;
						}else{
							_self.getObjectMetadata({'objectType':$$objectType, 'objectName':moveInfo.item.name, 'objectInfo' : moveInfo.item});
						}
					}
				}
				,rowOptions : {
					click : function (rowInfo){
						var item = rowInfo.item;

		    			_self.getObjectMetadata({'objectType':$$objectType, 'objectName':item.name, 'objectInfo' : item});
					}
					,contextMenu :{
						beforeSelect :function (){
							var itemObj = viewObj.getRowItemToElement($(this));
							viewObj.config.rowContext.changeHeader('contextTitle',0,itemObj.item.name);
						}
						,callback: function(key, sObj) {
							var sItem = this.gridItem;
							var tmpName = sItem.name;

							if(key =='copy'){
								viewObj.copyData();
								return ;
							}

							var cacheData = _g_cache.getSOMetaCache($$objectType, tmpName);

							_ui.pluginProxy.createScriptSql({
								gubunKey : key
								,sqlGenType : sObj.mode
								,objectType : $$objectType
								,objInfo : sItem
								,objName :  tmpName
								,item : {
									items : cacheData.items
								}
							});
						},
						items: [
							{header: "title", "key": "contextTitle"}
							,{divider:true}
							,{key : "copy" , "name": "Copy", hotkey :'Ctrl+C'}
							,{key : "sql_create", "name": "sql생성"
								,subMenu: [
									{ key : "selectStar","name": "select *" , mode: "selectStar"}
									,{ key : "select","name": "select column" ,mode:"select"}
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
});

// procedure object grid
_ui.addDbServiceObject({
	_procedure:function (resData ,reqParam){
		var _self = this;
		try{
    		var $$objectType = 'procedure';

			var gridObj = $.pubGrid(_self.objectTypeTab.getTabContentSelector({contentid: $$objectType}),{
				asideOptions :{
					lineNumber : {enabled : true, width : 30, align: 'right'}
				}
				,tColItem : VARSQLCont.getMainObjectServiceHeader($$objectType) 
				,tbodyItem : resData.list
				,setting : {
					enabled : true
					,enableSearch : true
				}
				,bodyOptions :{
					keyNavHandler : function(moveInfo){

						if(moveInfo.key == 13){
							return false;
						}else{
							_self.getObjectMetadata({'objectType':$$objectType, 'objectName':moveInfo.item.name, 'objectInfo' : moveInfo.item});
						}
					}
				}
				,rowOptions :{
					click : function (rowInfo){
						var item = rowInfo.item;

		    			_self.getObjectMetadata({'objectType':$$objectType, 'objectName':item.name, 'objectInfo' : item});
					}
					,contextMenu :{
						beforeSelect :function (){
							var itemInfo = gridObj.getRowItemToElement($(this));
							gridObj.config.rowContext.changeHeader('contextTitle',0,itemInfo.item.name);
						}
						,callback: function(key,sObj) {
							if(key =='copy'){
								gridObj.copyData();
								return ;
							}
						},
						items: [
							{header: "title", "key": "contextTitle"}
							, {divider:true}
							, {key : "copy" , "name": "Copy"}
						]
					}
				}
			});
 		}catch(e){
			VARSQL.log.info(e);
		}
	}
})

_ui.addDbServiceObject({
	_function : function (resData ,reqParam){
		var _self = this;
		try{
    		var $$objectType = 'function';

			var gridObj = $.pubGrid(_self.objectTypeTab.getTabContentSelector({contentid: $$objectType}),{
				asideOptions :{
					lineNumber : {enabled : true	,width : 30	,styleCss : 'text-align:right;padding-right:3px;'}
				}
				,tColItem : VARSQLCont.getMainObjectServiceHeader($$objectType)
				,tbodyItem : resData.list
				,setting : {
					enabled : true
					,enableSearch : true
				}
				,bodyOptions :{
					keyNavHandler : function(moveInfo){

						if(moveInfo.key == 13){
							return false;
						}else{
							_self.getObjectMetadata({'objectType':$$objectType, 'objectName':moveInfo.item.name, 'objectInfo' : moveInfo.item});
						}
					}
				}
				,rowOptions :{
					click : function (rowInfo){
						var item = rowInfo.item;

		    			_self.getObjectMetadata({'objectType':$$objectType, 'objectName':item.name, 'objectInfo' : item});
					}
					,contextMenu :{
						beforeSelect :function (){
							var itemInfo = gridObj.getRowItemToElement($(this));
							gridObj.config.rowContext.changeHeader('contextTitle',0,itemInfo.item.name);
						}
						,callback: function(key,sObj) {
							if(key =='copy'){
								gridObj.copyData();
								return ;
							}
						},
						items: [
							{header: "title", "key": "contextTitle"}
							,{divider:true}
							,{key : "copy" , "name": "Copy"}
						]
					}
				}
			});
 		}catch(e){
			VARSQL.log.info(e);
		}
	}
});

_ui.addDbServiceObject({
	_index : function (resData ,reqParam){
		var _self = this;
		try{
    		var $$objectType = 'index';

			var gridObj = $.pubGrid(_self.objectTypeTab.getTabContentSelector({contentid: $$objectType}),{
				asideOptions :{
					lineNumber : {enabled : true	,width : 30	,styleCss : 'text-align:right;padding-right:3px;'}
				}
				,tColItem :VARSQLCont.getMainObjectServiceHeader($$objectType)
				,tbodyItem : resData.list
				,setting : {
					enabled : true
					,enableSearch : true
				}
				,bodyOptions :{
					keyNavHandler : function(moveInfo){

						if(moveInfo.key == 13){
							return false;
						}else{
							_self.getObjectMetadata({'objectType':$$objectType, 'objectName':moveInfo.item.name, 'objectInfo' : moveInfo.item});
						}
					}
				}
				,rowOptions :{
					click : function (rowInfo){
						var item = rowInfo.item;

		    			_self.getObjectMetadata({'objectType':$$objectType, 'objectName':item.name, 'objectInfo' : item});
					}
					,contextMenu :{
						beforeSelect :function (){
							var itemInfo = gridObj.getRowItemToElement($(this));
							gridObj.config.rowContext.changeHeader('contextTitle',0,itemInfo.item.name);
						}
						,callback: function(key,sObj) {
							if(key =='copy'){
								gridObj.copyData();
								return ;
							}
						},
						items: [
							{header: "title", "key": "contextTitle"}
							,{divider:true}
							,{key : "copy" , "name": "Copy"}
						]
					}
				}
			});
 		}catch(e){
			VARSQL.log.info(e);
		}
	}
})

_ui.addDbServiceObject({
	_trigger : function (resData, reqParam){
		var _self = this;
		try{
			var $$objectType = 'trigger';

			var gridObj = $.pubGrid(_self.objectTypeTab.getTabContentSelector({contentid: $$objectType}),{
				asideOptions :{
					lineNumber : {enabled : true, width : 30, styleCss : 'text-align:right;padding-right:3px;'}
				}
				,tColItem : VARSQLCont.getMainObjectServiceHeader($$objectType) 
				,tbodyItem : resData.list
				,setting : {
					enabled : true
					,enableSearch : true
				}
				,bodyOptions :{
					keyNavHandler : function(moveInfo){

						if(moveInfo.key == 13){
							return false;
						}else{
							_self.getObjectMetadata({'objectType':$$objectType, 'objectName':moveInfo.item.name, 'objectInfo' : moveInfo.item});
						}
					}
				}
				,rowOptions :{
					click : function (rowInfo){
						var item = rowInfo.item;

		    			_self.getObjectMetadata({'objectType':$$objectType, 'objectName':item.name, 'objectInfo' : item});
					}
					,contextMenu :{
						beforeSelect :function (){
							var itemInfo = gridObj.getRowItemToElement($(this));
							gridObj.config.rowContext.changeHeader('contextTitle',0,itemInfo.item.name);
						}
						,callback: function(key,sObj) {
							
							if(key =='copy'){
								gridObj.copyData();
								return ;
							}
						},
						items: [
							{header: "title", "key": "contextTitle"}
							,{divider:true}
							,{key : "copy" , "name": "Copy"}
						]
					}
				}
			});
 		}catch(e){
			VARSQL.log.info(e);
		}
	}
})

_ui.addDbServiceObject({
	_sequence : function (resData, reqParam){
		var _self = this;
		try{
			var $$objectType = 'sequence';

			var gridObj = $.pubGrid(_self.objectTypeTab.getTabContentSelector({contentid: $$objectType}),{
				asideOptions :{
					lineNumber : {enabled : true	,width : 30	,styleCss : 'text-align:right;padding-right:3px;'}
				}
				,tColItem : VARSQLCont.getMainObjectServiceHeader($$objectType)
				,tbodyItem : resData.list
				,setting : {
					enabled : true
					,enableSearch : true
				}
				,bodyOptions :{
					keyNavHandler : function(moveInfo){

						if(moveInfo.key == 13){
							return false;
						}else{
							_self.getObjectMetadata({'objectType':$$objectType, 'objectName':moveInfo.item.name, 'objectInfo' : moveInfo.item});
						}
					}
				}
				,rowOptions :{
					click : function (rowInfo){
						var item = rowInfo.item;

						_self.getObjectMetadata({'objectType':$$objectType, 'objectName':item.name, 'objectInfo' : item});
					}
					,contextMenu :{
						beforeSelect :function (){
							var itemInfo = gridObj.getRowItemToElement($(this));
							gridObj.config.rowContext.changeHeader('contextTitle',0,itemInfo.item.name);
						}
						,callback: function(key, sObj) {
							if(key =='copy'){
								gridObj.copyData();
								return ;
							}
						},
						items: [
							{header: "title", "key": "contextTitle"}
							,{divider:true}
							,{key : "copy" , "name": "Copy"}
						]
					}
				}
			});
		}catch(e){
			VARSQL.log.info(e);
		}
	}
})
/**
 * db object metadata
 */
_ui.dbObjectMetadata= {
	initFlag : false
	,metaInfoLoadComplete : true
	,selectMetadata : {}
	,metadataTabInfo:{}
	,activeTabInfo : {}
	,selector : {
		contEleId : _selector.plugin.objectMeta
	}
	// 왼쪽 메뉴 생성 .
	,init : function (options){
		this.createTemplate();
		this.initEvt();
	}
	,initEvt : function (){
		var _self = this;

		// ddl copy
		$(_self.selector.contEleId).on('click','.ddl-copy', function (){
			var sEle = $(this)
				,mode = sEle.data('ddl-copy-mode');

			var copyTxt = sEle.closest('.pretty-view-area').find('textarea').val();

			if('copy'==mode){
				copyStringToClipboard(copyTxt, 'ddlcopy');
			}else{
				_ui.SQL.addSqlEditContent(copyTxt , false);
			}
		})
	}
	/**
     * @method getServiceObjectMetadata
	 * @param objectType {String} - db service object
	 * @param objectName {String} - object type name
	 * @param visible {Boolean} - 보이기 여부.
     * @description 클릭시 텝메뉴에 해당하는 메뉴 그리기
     */
	,getServiceObjectMetadata : function(param, refresh){

		var _self = this
			,objType = param.objectType
			,objName = param.objectName;

		var metaTabSelector = _self.getTabGroupEleId(objType); 
		var selObjectMetaEle = $(metaTabSelector);

		if(!selObjectMetaEle.hasClass('on')){
			$(_self.getTabGroupEleId('on')).removeClass('on');
			selObjectMetaEle.addClass('on');
		}

		if(_self.metaInfoLoadComplete===false){
			VARSQLUI.alert.open({key:'msg.loading'});
			return ;
		}

		if(refresh !== true && (_self.selectMetadata[objType]||{}).name == objName){
			return ;
		}

		_ui.layout.setActiveTab('dbMetadata');

		if(param.visible===true){	// object 선택 이 아닌  object tab 선택.
			_self.resizeMetaArea();
			return ;
		}
		_self.selectMetadata[objType].objectInfo = param.objectInfo ||{};	// 선택한 object 정보.
		_self.selectMetadata[objType].name = objName||''; // 선택한 오브젝트 캐쉬
		
		var metaTabObj = _self.selectMetadata[objType].metaTab;
	
		if(VARSQL.isBlank(metaTabObj)){
			
			_self.selectMetadata[objType].metaTab = $.pubTab(metaTabSelector, {
				items : _self.metadataTabInfo[objType]
				,itemKey :{title :'name', id : 'tabid'}
				,dropdown : {
					heightResponsive : true
				}
				,click : function (item){

					var objectName = _self.selectMetadata[objType].name;
					var selectSchema = _self.selectMetadata[objType].schema;

					var metaTabKey = item.tabid;

					var tabParam = {
						metaTabKey : metaTabKey
						, objectType : objType
						, objectName : objectName
					};

					_self.activeTabInfo[objType] = metaTabKey;

					var cacheData;
					if(objectName !=''){
						cacheData = _g_cache.getSOMetaCache(objType, objectName, metaTabKey);
					}else{
						cacheData = {items:[]};
					}

					_self.resizeMetaArea();

					if('ddl' == metaTabKey){
						if(cacheData){
							_self.metadataDDLView(objType, metaTabKey, cacheData);
							return ;
						}

						_self._createDDL(tabParam, function (data){
							_self.metadataDDLView(objType, metaTabKey, data);
						});
						
					}else {
						var callMethodStr = '_'+convertCamel(objType+'_'+metaTabKey);

						if(cacheData && VARSQL.isArray(cacheData.items)){
							_self[callMethodStr](cacheData, tabParam, metaTabKey, false);
							return ;
						}
						
						_self._getMetadataInfo(tabParam, function (resData, param){
							_self[callMethodStr](resData, tabParam, metaTabKey, true);
						})
					}
				}
			})
		}else{
			var activeItem = metaTabObj.getActive();
			metaTabObj.itemClick(activeItem.idx);
		}
	}
	// meta data 가져오기.
	,_getMetadataInfo : function (param , callbackFn){
		var _self =this;

		_self.metaInfoLoadComplete = false;
		
		VARSQL.req.ajax({
			loadSelector : _self.selector.contEleId
			,url:{type:VARSQL.uri.database, url:'/dbObjectMetadataList'}
			,data : _getParam(param)
			,success:function (resData){

				_self.metaInfoLoadComplete = true;

				var result = resData.list;

				if(result.length > 0){
					var callData=result;
					var objectType = param.objectType;

					if(param.metaTabKey =='column'){
						if(result.length > 0){
							callData = result[0].colList || [];
						}
					}
					_g_cache.setSOMetaCache(objectType, param.objectName, param.metaTabKey, {items:callData});
					callbackFn.call(_self, {items:callData}, param);
				}
			}
			,error: function (jqXHR, exception) {
				_self.metaInfoLoadComplete = true;
			}
		});
	}
	// meta tab content id
	,getTabContEleId : function(objType, metaKey){
		return this.selector.contEleId +' [data-so-meta-tab-content="'+objType+'_'+ metaKey+'"]';
	}
	// active tab id
	,getActiveTabId : function (objType){
		return this.activeTabInfo[objType] || this.metadataTabInfo[objType][0].tabid;
	}
	// meta content group id
	,getTabGroupEleId : function(objType){
		if(objType == 'on'){
			return this.selector.contEleId +' .on[data-so-meta-tab-group]';
		}
		return this.selector.contEleId +' [data-so-meta-tab-group="'+objType+'"]';
	}
	/**
	 * @method _createDDL
	 * @param name
	 * @param val
	 * @param options
	 * @description create ddl
	 */
	,_createDDL :function (sObj, callbackFn){
		var _self = this;

		var param =_getParam({'objectType':sObj.objectType, objectName:sObj.objectName});

		VARSQL.req.ajax({
			url:{type:VARSQL.uri.database, url:'/createDDL'}
			,loadSelector : _self.selector.contEleId
			,data:param
			,success:function (resData){

				var item = resData.item||{};

				if(sObj.changeFormat){
					item.createScript = getFormatSql(item.createScript, _g_options.dbtype, 'ddl');
				}

				_g_cache.setSOMetaCache(param.objectType, param.objectName, 'ddl', item.createScript);

				if(VARSQL.isFunction(callbackFn)){
					callbackFn.call(_self, item.createScript);
				}else{
					if(sObj.gubunKey=='ddl_copy'){
						_ui.text.copy(item.createScript);
					}else{
						_ui.SQL.addSqlEditContent(item.createScript, false);
					}
				}
				
			}
		});
	}
	
	// ddl source view
	,metadataDDLView : function (objectType, tabkey, ddlSource){
		var addHtml = $('#ddlViewTemplate').html();
		
		var ele = $(this.selectMetadata[objectType].metaTab.getTabContentSelector({'tabid' : tabkey}));

		var prettyEle = ele.find('.prettyprint'); 
		if(prettyEle.length < 1){
			ele.empty().html(addHtml);
			prettyEle = ele.find('.prettyprint'); 
		}

		prettyEle.empty().html(ddlSource).removeClass('prettyprinted');
		ele.find('textarea').val(ddlSource);
		prettyEle.scrollTop(0);
		PR.prettyPrint();

	}
	// create meta element template
	,createTemplate : function (){
		var _self = this;

		var data = _g_options.serviceObject;
		var len = data.length;

		if(len < 1) return ;

		var metaStrHtm = [];
		var item;
		for(var i=0; i<len; i++){
			item = data[i];
			var contentid = item.contentid;

			_self.metadataTabInfo[contentid] = item.tabList;
			_self.selectMetadata[contentid] = {name : '', metaTab : ''};

			metaStrHtm.push('<div data-so-meta-tab-group="'+contentid+'" class="object-meta-content '+(i==0?'on':'')+'"> </div>');
		}
		
		$(_self.selector.contEleId).empty().html(metaStrHtm.join(''));
	}
	// meta 영역 resize
	,resizeMetaArea : function (dimension){
		var resizeMethod = this['_'+_ui.pluginProxy.getActiveObjectMenu().contentid+'MetaResize'];
		if(resizeMethod) resizeMethod.call(this, dimension);

		if(this.selectMetadata[_ui.pluginProxy.getActiveObjectMenu().contentid].metaTab){
			this.selectMetadata[_ui.pluginProxy.getActiveObjectMenu().contentid].metaTab.resize();
		}
	}
}

//table tab control
_ui.addODbServiceObjectMetadata({
	//테이블에 대한 메타 정보 보기 .
	_tableColumn:function (colData, callParam, tabKey, reloadFlag){
		var _self = this;
		var $$objectType = 'table';

		var items = colData.items;

		if(reloadFlag===true){ // 데이터 세로 로드시 cache에 추가.
			_ui.SQL.addEditorHint(_g_options.param.schema,'table',{name : callParam.objectName , colList :items });
		}

		var metaEleId = _self.selectMetadata[$$objectType].metaTab.getTabContentSelector({'tabid' :tabKey});

		var gridObj = $.pubGrid(metaEleId);

		if(gridObj){
			gridObj.setData(items, 'reDraw');
			return ;
		}
		
		var contextItems =[
			{key : "copy" , "name": "Copy", hotkey :'Ctrl+C'}
			,{ key : "add_editor","name": "Ediotr 컬럼명 넣기"}
			,{divider:true}
			,{key : "sql_create", "name": "sql생성"
				,subMenu: [
					{ key : "create_select","name": "select" ,mode:"select"}
					,{ key : "create_selectWhere","name": "selectWhere" ,mode:"selectWhere"}
					,{ key : "create_insert","name": "insert" , mode:"insert"}
					,{ key : "create_update","name": "update" ,mode:"update"}
				]
			}
		];
		
		var settingItems = _g_options.userSettingInfo['main.contextmenu.serviceobject'];

		settingItems = VARSQL.isArray(settingItems) ? settingItems : [];

		var userContextItems = [];
		for(var i =0 ;i <settingItems.length;i++){
			var userItem = settingItems[i];

			var pItemKey = 'uCustomItem_'+i;

			var addItem = {key : pItemKey	,name : userItem.name ,subMenu : []};

			var templateInfos = userItem.templateInfos;

			for(var j =0 ;j < templateInfos.length; j++){
				var templateInfo = templateInfos[j];
				templateInfo.key = pItemKey +'_'+ j;
				templateInfo.isTemplate =true;

				addItem.subMenu.push(templateInfo);
			}
			userContextItems.push(addItem);
		}
		contextItems = contextItems.concat(userContextItems);

		gridObj = $.pubGrid(metaEleId, {
			headerOptions : {redraw : false}
			,asideOptions :{lineNumber : {enabled : true	,width : 30}}
			,setting : {
				enabled : true
				,enableSearch : true
			}
			,tColItem : VARSQLCont.getMainObjectMetaHeader('tableColumn')
			,scroll :{	// 스크롤 옵션
				vertical : {
					speed : 2			// 스크롤 스피드 row 1
				}
			}
			,tbodyItem :items
			,message : {
				empty : ''
			}
			,rowOptions :{
				contextMenu : {
					beforeSelect :function (){
						$(this).trigger('click');
					}
					,disableItemKey : function (items){
						if(gridObj.getSelectionItem(['name']).length < 1){
							return [
								{key :'sql_create' , depth :0	}
								,{key :'add_editor' , depth :0}
							];
						}
						return [];
					}
					,callback: function(key,sObj) {

						if(key =='copy'){
							gridObj.copyData();
							return ;
						}

						if(key.indexOf('create_') ==0){
							var cacheData = gridObj.getSelectionItem(['name']);

							_ui.pluginProxy.createScriptSql({
								gubunKey : key
								,sqlGenType : sObj.mode
								,objectType : $$objectType
								,objName : _self.selectMetadata[$$objectType].name
								,objInfo : _self.selectMetadata[$$objectType].objectInfo
								,item : {
									items:cacheData
								}
								,param_yn: sObj.param_yn
							});

							return ;
						}

						if(key =='add_editor'){
							var selectionItems = gridObj.getSelectionItem(['name'], true);
							
							if(selectionItems.length > 0){
								var colInfo = [];
								for(var i =0; i < selectionItems.length; i++){
									colInfo.push({
										label : selectionItems[i].name
									});
								}

								_ui.SQL.addTextToEditorArea('',{type:'column' , header : colInfo})
							}
							return ;
						}
						
						if(sObj.isTemplate===true){

							var result =VARSQLTemplate.render.generateSource(sObj, {
				                'table' : {
				                	name : _self.selectMetadata[$$objectType].name
				                }
				                ,'columns' : gridObj.getSelectionItem(['name'], true)
				            });

							if(result.isError){
								VARSQL.toastMessage('msg.setting.fail.check');
				            	return ;
				    		}

							var resultValue = result.value;
							
							if(!_ui.SQL.getSqlEditorObj() || sObj.viewMode != 'editor'){
								_ui.text.copy(resultValue, 'java');
								return ;
							}else{
								_ui.SQL.addSqlEditContent(resultValue, false);	
							}
								
							return ;
						}
					}
					,items: contextItems
				}
			}
		});
	}
	,_tableMetaResize : function (dimension){
		if(this.selectMetadata['table'].metaTab){
			var gridObj = $.pubGrid(this.selectMetadata['table'].metaTab.getTabContentSelector({'tabid' : 'column'}));

			if(gridObj){
				gridObj.resizeDraw();
			}
		}
	}
})

//view 정보 보기.
_ui.addODbServiceObjectMetadata({
	// view 메타 데이터 보기.
	_viewColumn :function (colData ,callParam, tabKey, reloadFlag){
		var _self = this;
		var $$objectType = 'view';

		var items = colData.items;

		if(reloadFlag===true){ // 데이터 세로 로드시 cache에 추가.
			var colArr = [];
			$.each(items , function (i , item){
				colArr.push(item.name);
			});
			_ui.SQL.addEditorHint(_g_options.param.schema,'table',{name : callParam.objectName , colList :items });
		}

		var metaEleId = _self.selectMetadata[$$objectType].metaTab.getTabContentSelector({'tabid': tabKey});

		var gridObj = $.pubGrid(metaEleId);

		if(gridObj){
			gridObj.setData(items, 'reDraw');
			return ;
		}

		gridObj = $.pubGrid(metaEleId, {
			headerOptions : {redraw : false}
			,setting : {
				enabled : true
				,enableSearch : true
			}
			,asideOptions :{lineNumber : {enabled : true	,width : 30}}
			,tColItem : VARSQLCont.getMainObjectMetaHeader('viewColumn')
			,tbodyItem :items
			,message : {
				empty : ''
			}
			,rowOptions :{
				contextMenu : {
					beforeSelect :function (){
						$(this).trigger('click');
					}
					,disableItemKey : function (items){
						if(gridObj.getSelectionItem(['name']).length < 1){
							return [
								{key :'select' , depth :0	}
							];
						}
						return [];
					}
					,callback: function(key,sObj) {

						if(key =='copy'){
							gridObj.copyData();
							return ;
						}

						var cacheData = gridObj.getSelectionItem(['name']);

						_ui.pluginProxy.createScriptSql({
							gubunKey: key
							,sqlGenType: sObj.mode
							,objectType: $$objectType
							,objName: _self.selectMetadata[$$objectType].name
							,objInfo: _self.selectMetadata[$$objectType].objectInfo
							,item : {
								items:cacheData
							}
							,param_yn: sObj.param_yn
						});

					},
					items: [
						{key: "copy", "name": "Copy", hotkey: 'Ctrl+C'}
						,{key: "select", "name": "Select query", mode: "select"}
					]
				}
			}
		});
	}
	,_viewMetaResize : function (dimension){

		if(this.selectMetadata['view'].metaTab){
			var gridObj = $.pubGrid(this.selectMetadata['view'].metaTab.getTabContentSelector({'tabid' : 'column'}));

			if(gridObj){
				gridObj.resizeDraw();
			}
		}
	}
});

// 프로시저 처리.
_ui.addODbServiceObjectMetadata({
	//procedure 대한 메타 정보 보기 .
	_procedureColumn :function (colData, callParam, tabKey, reloadFlag){
		var _self = this;

 		var $$objectType = 'procedure';

 		var metaEleId = _self.selectMetadata[$$objectType].metaTab.getTabContentSelector({'tabid': tabKey});

		var gridObj = $.pubGrid(metaEleId);

		var items = colData.items;

		if(gridObj){
			gridObj.setData(items,'reDraw');
			return ;
		}

		gridObj = $.pubGrid(metaEleId, {
			headerOptions : {redraw : false}
			,setting : {
				enabled : true
				,enableSearch : true
			}
			,asideOptions :{lineNumber : {enabled : true	,width : 30}}
			,tColItem : VARSQLCont.getMainObjectMetaHeader('procedureColumn')
			,tbodyItem :items
			,message : {
				empty : ''
			}
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
						{key : "copy" , "name": "Copy", hotkey :'Ctrl+C'}
					]
				}
			}
		});
	}
	,_procedureMetaResize : function (dimension){
		if(this.selectMetadata['procedure'].metaTab){
			var gridObj = $.pubGrid(this.selectMetadata['procedure'].metaTab.getTabContentSelector({'tabid' : 'column'}));

			if(gridObj){
				gridObj.resizeDraw();
			}
		}
	}
})

// function 정보 처리.
_ui.addODbServiceObjectMetadata({
	//function 대한 메타 정보 보기 .
	_functionColumn :function (colData, callParam, tabKey, reloadFlag){
		var _self = this;

 		var $$objectType = 'function';

 		var metaEleId = _self.selectMetadata[$$objectType].metaTab.getTabContentSelector({'tabid': tabKey});

		var items = colData.items;

		var gridObj = $.pubGrid(metaEleId);

		if(gridObj){
			gridObj.setData(items, 'reDraw');
			return ;
		}

		gridObj = $.pubGrid(metaEleId, {
			headerOptions : {redraw : false}
			,setting : {
				enabled : true
				,enableSearch : true
			}
			,asideOptions :{lineNumber : {enabled : true	,width : 30}}
			,tColItem : VARSQLCont.getMainObjectMetaHeader('functionColumn')
			,tbodyItem :items
			,message : {
				empty : ''
			}
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
						{key : "copy" , "name": "Copy", hotkey :'Ctrl+C'}
					]
				}
			}
		});
	}
	// function 대한 메타 정보 보기 .
	,_functionInfo :function (colData, callParam, tabKey, reloadFlag){
		var _self = this;

		var $$objectType = 'function';

		var metaEleId = _self.selectMetadata[$$objectType].metaTab.getTabContentSelector({'tabid': tabKey});

		var items = colData.items;

		var gridObj = $.pubGrid(metaEleId);

		if(gridObj){
			gridObj.setData(items,'reDraw');
			return ;
		}

		gridObj = $.pubGrid(metaEleId, {
			headerOptions : {redraw : false}
			,asideOptions :{lineNumber : {enabled : true	,width : 30}}
			,setting : {
				enabled : true
				,enableSearch : true
			}
			,tColItem : [
				{ label: 'Name', key: 'name'},
				{ label: 'Value', key: 'val',width:80 },
			]
			,tbodyItem :items
			,message : {
				empty : ''
			}
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
						{key : "copy" , "name": "Copy", hotkey :'Ctrl+C'}
					]
				}
			}
		});
	}
	,_functionMetaResize : function (dimension){
		if(this.selectMetadata['function'].metaTab){
			var gridObj = $.pubGrid(this.selectMetadata['function'].metaTab.getTabContentSelector({'tabid' : 'info'}));

			if(gridObj){
				gridObj.resizeDraw();
			}
		}
	}
})

// index 처리.
_ui.addODbServiceObjectMetadata({
	// index column 정보.
	_indexColumn : function (colData, callParam, tabKey, reloadFlag){
		var _self = this;

 		var $$objectType = 'index';

 		var metaEleId = _self.selectMetadata[$$objectType].metaTab.getTabContentSelector({'tabid': tabKey});

		var gridObj = $.pubGrid(metaEleId);

		var items = colData.items;

		if(gridObj){
			gridObj.setData(items,'reDraw');
			return ;
		}

		gridObj = $.pubGrid(metaEleId, {
			headerOptions : {redraw : false}
			,setting : {
				enabled : true
				,enableSearch : true
			}
			,asideOptions :{lineNumber : {enabled : true	,width : 30}}
			,tColItem : VARSQLCont.getMainObjectMetaHeader('indexColumn')
			,tbodyItem :items
			,message : {
				empty : ''
			}
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
						{key : "copy" , "name": "Copy", hotkey :'Ctrl+C'}
					]
				}
			}
		});
	}
	,_indexMetaResize : function (dimension){
		if(this.selectMetadata['index'].metaTab){
			var gridObj = $.pubGrid(this.selectMetadata['index'].metaTab.getTabContentSelector({'tabid' : 'column'}));

			if(gridObj){
				gridObj.resizeDraw();
			}
		}
	}
})

// trigger 처리.
_ui.addODbServiceObjectMetadata({
	_triggerInfo : function (colData, callParam, tabKey, reloadFlag){
		var _self = this;

 		var $$objectType = 'trigger';

 		var metaEleId = _self.selectMetadata[$$objectType].metaTab.getTabContentSelector({'tabid': tabKey});

		var gridObj = $.pubGrid(metaEleId);

		var items = colData.items;

		if(gridObj){
			gridObj.setData(items,'reDraw');
			return ;
		}

		gridObj = $.pubGrid(metaEleId, {
			headerOptions : {redraw : false}
			,asideOptions :{lineNumber : {enabled : true	,width : 30}}
			,setting : {
				enabled : true
				,enableSearch : true
			}
			,tColItem : VARSQLCont.getMainObjectMetaHeader('triggerInfo')
			,tbodyItem :items
			,message : {
				empty : ''
			}
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
						{key : "copy" , "name": "Copy", hotkey :'Ctrl+C'}
					]
				}
			}
		});
	}
	,_triggerMetaResize : function (dimension){
		if(this.selectMetadata['trigger'].metaTab){
			var gridObj = $.pubGrid(this.selectMetadata['trigger'].metaTab.getTabContentSelector({'tabid' : 'info'}));

			if(gridObj){
				gridObj.resizeDraw();
			}
		}
	}
})

// sequence 처리.
_ui.addODbServiceObjectMetadata({
	// sequence 정보보기.
	_sequenceInfo : function (colData, callParam, tabKey, reloadFlag){
		var _self = this;

 		var $$objectType = 'sequence';

 		var metaEleId = _self.selectMetadata[$$objectType].metaTab.getTabContentSelector({'tabid': tabKey});

		var gridObj = $.pubGrid(metaEleId);

		var items = colData.items;

		if(gridObj){
			gridObj.setData(items,'reDraw');
			return ;
		}

		gridObj = $.pubGrid(metaEleId, {
			headerOptions : {redraw : false}
			,asideOptions :{lineNumber : {enabled : true	,width : 30}}
			,setting : {
				enabled : true
				,enableSearch : true
			}
			,tColItem : VARSQLCont.getMainObjectMetaHeader('sequenceInfo')
			,tbodyItem :items
			,message : {
				empty : ''
			}
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
						{key : "copy" , "name": "Copy", hotkey :'Ctrl+C'}
					]
				}
			}
		});
	}
	,_sequenceMetaResize : function (dimension){
		if(this.selectMetadata['sequence'].metaTab){
			var gridObj = $.pubGrid(this.selectMetadata['sequence'].metaTab.getTabContentSelector({'tabid' : 'info'}));

			if(gridObj){
				gridObj.resizeDraw();
			}
		}
	}
})

/**
 * sql 데이터 그리드
 */
_ui.SQL = {
	currentSqlEditorInfo : null
	,sqlMainEditor:{}
	,sqlFileTab : null	// sql file tab list
	,sqlFileNameDialogEle : null // sql file name dialog
	,allTabSqlEditorObj : {}	 // sql editor  object
	,sqlEditorSelector : '#varsql_main_editor'
	,sqlParameterEle : null
	,noteDialog : null
	,recvIdMultiSelectObj : null
	,findTextDialog : null
	,currentSqlData :''
	,findTextEle : null
	,convertTextDialog : null
	,editorHistory :{
		back:[]
		,forward :[]
	}
	,editorHistoryIdx : 0
	,options :{
		selector:'#sqlExecuteArea'
		,limitCnt:'#limitRowCnt'
		,conuidObj:'#conuid'
		,active: null
		,cancel: "input,textarea,button,select,option"
		,distance: 1
		,delay: 0
	}
	//SQL ui 초기화
	,init:function (options){
		var _self = this;

		var options ={dbtype:_g_options.dbtype};
		if(!options.dbtype) {
			VARSQLUI.alert.open('dbtype empty');
			return ;
		}

		$.extend(true,_self.options, options);

		_self._initTab();
		_self._initEditor();
		_self._initEvent();
		_self.sqlFileTabList();
		_self._initDialog();

	}
	// init editor tab
	,_initTab : function (){
		var _self = this;

		// tab-item
		_self.sqlFileTab = $.pubTab('#varsqlSqlFileTab',{
			items : []
			,width:'auto'
			,itemMaxWidth: 100
			,useContentContainer :false
			,dropdown: {
				width: 100
				,heightResponsive: true
			}
			,drag: {
				enabled :true
				,dragDrop : function (moveItem){
					_self.saveSqlFile(moveItem, 'moveTab');
				}
			}
			,contextMenu :  {
				callback: function(key,sObj) {
					var item = sObj.tabItem;

					var param = {sqlId : item.sqlId};
					if(key =='close'){
						_self.deleteEditorInfo('del', item);
						param.len = _self.sqlFileTab.getItemLength();
						_self.saveSqlFile(param ,'delTab');
						return ;
					}else if(key =='close_other'){
						_self.deleteEditorInfo('other', item);
						_self.saveSqlFile(param ,'delTab-other');
						_self.setEditorHistory('removeOther');
					}else if(key =='close_all'){
						_self.deleteEditorInfo('all');
						_self.saveSqlFile(param ,'delTab-all');
						_self.setEditorHistory('removeAll');
					}
				},
				items: [
					{key : "close" , "name": "Close"}
					,{key : "close_other" , "name": "Close Others"}
					,{key : "close_all" , "name": "Close All"}
				]
			}
			,titleIcon :{
				right :{
					html : '<i class="fa fa-remove"></i>'
					,click : function (item, idx){
						var sqlId =item.sqlId;

						var editorObj= _self.allTabSqlEditorObj[sqlId];
						item = editorObj.item;

						var param = {sqlId : sqlId};

						if(item._isChange===true){
							var dialogObj = VARSQLUI.dialog.open('#confirmTemplateTemplate',{
								height: 150
								,width: 300
								,modal: true
								,autoOpen:true
								,buttons: {
									"Save and close":function (){
										param.sqlCont = _self.sqlMainEditor.getValue(sqlId);
										param.len =_self.sqlFileTab.getItemLength();
										_self.saveSqlFile(param ,'query_del');
										_self.deleteEditorInfo('del', item);
										dialogObj.dialog( "close" );
										_self.setEditorHistory('remove',item)
									}
									,"Close":function (){
										param.len =_self.sqlFileTab.getItemLength();
										_self.saveSqlFile(param ,'delTab');
										_self.deleteEditorInfo('del', item);
										dialogObj.dialog( "close" );
										_self.setEditorHistory('remove',item)
									}
									,"Cancel": function() {
										dialogObj.dialog( "close" );
									}
								}
								,close: function() {
									dialogObj.dialog( "close" );
								}
							})
						}else{
							_self.deleteEditorInfo('del', item);
							param.len =_self.sqlFileTab.getItemLength();
							_self.saveSqlFile(param ,'delTab');

							_self.setEditorHistory('remove',item)
						}
					}
				}
			}
			,click : function (item){
				_self.loadEditor(item);
			}
			,itemKey :{							// item key mapping
				title :'sqlTitle'
				,id : 'sqlId'
			}
		})
		_self.sqlFileTab.resize({height: $(_self.sqlEditorSelector).height()});
	}
	,_initEditor : function (){
		var _self = this;

		_self.sqlParameterEle = $('#sql_parameter_area');
		
		var theme = VARSQLUI.theme(_g_options.param.conuid);
		
		var codeEditorObj = new codeEditor(document.getElementById('varsql_main_editor'), {
			schema: _g_options.schema,
			mimeType : _g_options.dbtype,
			editorOptions: { 
				theme: 'vs-'+ (theme =='dark'?'dark':'light')
				,minimap: {enabled: false} 
				,fixedOverflowWidgets : true
			},
			change:()=>{
				var currentEditorInfo = _self.currentSqlEditorInfo;
				
				if(VARSQL.isBlank(currentEditorInfo)) return ; 
				
				if(currentEditorInfo.item._isChange ===false){
					currentEditorInfo.item._isChange = true;
					var activeItem = _self.sqlFileTab.getActive();
					_self.sqlFileTab.updateTitle(activeItem.item.sqlId ,'*' + activeItem.item.sqlTitle);
				}
	
				if(_g_options.autoSave.enabled !== false){
					clearTimeout(changeTimer);
					autoSave(currentEditorInfo);
				}
			}
			,contextItems:[
				{
					label : 'Diff'
					,hotKey : ''
					,action : function (){
						_ui.headerMenu.textDiff(_self.getSql(),'')
					}
				}
			]
			,onContextMenu:()=>{
				//sqlEditorStyleChange($('#pluginSqlEditor'));
			}
			,keyEvents: {
				save: () => {
					_self.saveSqlFile();
				},
				history: (activeItem, mode) => {
		            _self.sqlFileTab.setActive(activeItem);
		        },
		        executeSql: () => {
		            _self.sqlExecute();
		        },
		        sqlFormat: () => {
		            _self.sqlFormatData();
		        }
			},
			message: {
		        execute: VARSQL.message('toolbar.execute'),
		        format: VARSQL.message('toolbar.format'),
		        cut: VARSQL.message('toolbar.cut'),
		        paste: VARSQL.message('toolbar.paste'),
		        copy: VARSQL.message('toolbar.copy'),
		    }
		})

		// 자동 저장 처리.
		var changeTimer;
		function autoSave(editerInfo){
			changeTimer = setTimeout(function() {
				editerInfo.item._isChange = false;
				_self.saveSqlFile();
			},_g_options.autoSave.delay );
		}
		
		

		_self.sqlMainEditor = codeEditorObj;
	}
	,_initDialog : function (){
		var _self = this;

		// sql 파일명 생성 수정 dialog
		if(_self.sqlFileNameDialogEle==null){
			_self.sqlFileNameDialogEle = $('#editorNewSqlFileDialog').dialog({
				height: 200
				,width: 280
				,resizable: false
				,modal: true
				,autoOpen : false
				,close: function() {
					_self.sqlFileNameDialogEle.dialog( "close" );
				}
				,buttons: {
					"Save":function (){
						_self.sqlFileNameSave();
					}
					,Cancel: function() {
						_self.sqlFileNameDialogEle.dialog( "close" );
					}
				}
			});

			// sql file 생성
			$('#editorSqlFileNameText').keydown(function(e) {
				if (e.keyCode == '13') {
					e.preventDefault();
					_self.sqlFileNameSave();
					return false;
				}
			});
		}
	}
	// file name 저장.
	,sqlFileNameSave : function (){
		var nameTxt = $('#editorSqlFileNameText').val();
		if(VARSQL.str.trim(nameTxt)==''){
			$('#editorSqlFileNameText').focus();
			VARSQL.alertMessage('msg.content.enter.param',VARSQL.message('sql.file.name'));
			return ;
		}

		var sqlFileId = $('#editorSqlFileId').val();

		this.saveSqlFile({
			'sqlId' : sqlFileId
			,'sqlTitle' : nameTxt
		}, (sqlFileId =='' ? 'newfile' :'title'));

		this.sqlFileNameDialogEle.dialog( "close" );
	}
	// add editor hint 
	,addEditorHint: function (schema, type, objectInfo){
		this.sqlMainEditor.addSuggestionInfo(_g_options.schema == schema?"":schema, type, objectInfo);		
	}
	// 실행 취소
	,undo :function (){
		this.getSqlEditorObj().undo();
	}
	// 되살리기
	,redo :function (){
		this.getSqlEditorObj().redo();
	}
	// editor tab & editor element 삭제.
	,deleteEditorInfo : function (mode, item){
		if(mode == 'all' || mode == 'other'){

			if(mode == 'all'){
				this.sqlFileTab.removeItem('all');
				item = {};
			}else{
				this.sqlFileTab.removeItem('other', item.sqlId);
			}

			for(var key in this.allTabSqlEditorObj){
				if(key != item.sqlId){
					var removeIem = this.allTabSqlEditorObj[key].item;

					delete this.allTabSqlEditorObj[key];
					_ui.sqlDataArea.removeDataGridEle(removeIem);
				}
			};
		}else{
			this.sqlFileTab.removeItem(item);
			var sqlId = item.sqlId
			if(this.allTabSqlEditorObj[sqlId]){
				delete this.allTabSqlEditorObj[sqlId];
			}
			_ui.sqlDataArea.removeDataGridEle(item);
		}

		if(this.sqlFileTab.getItemLength() < 1){
			this.currentSqlEditorInfo= null;
			$('[data-editor-id="empty"]').addClass('active'); // 빈파일 화면 오픈.
			this.addParamTemplate('init', {});
			this.sqlParameterEle.attr('data-paramter-id','');
			this.setSqlEditorBtnDisable();
		}
	}
	,setSqlEditorBtnDisable :  function(){
		$('[data-sql-editor-menu="y"]').attr('disabled',true).addClass('disable');
		_ui.sqlDataArea.setGridSelector();
	}
	,setSqlEditorBtnEnable :  function(){
		$('[data-sql-editor-menu="y"]').removeAttr('disabled').removeClass('disable');
	}
	,resize : function (dimension){
		if(dimension){
			dimension.height = dimension.height-60; // editor button area  +  editor sql tab area
		}

		var _self = this;

		var editorObj = _self.getSqlEditorObj();
		if(editorObj){
			editorObj.resize();
		}

		try{
			_self.sqlFileTab.resize({height: dimension.height-10});
		}catch(e){
			console.log('editor refresh error')
		}
	}
	//이벤트 초기화
	,_initEvent :function (){
		var _self = this;
		
		// sql 실행
		$('.sql_toolbar_execute_btn').on('click',function (evt){
			_self.sqlExecute();
		});

		// 새파일
		$('.sql_toolbar_new_file, .sql_new_file').on('click',function (){
			 $('#editorSqlFileNameText').val('');
			 $('#editorSqlFileId').val('');

			_self.sqlFileNameDialogEle.dialog("open");
		});

		// 저장
		$('.sql_toolbar_save_btn').on('click',function (e){
			_self.saveSqlFile();
		});

		// 모두 저장
		$('.sql_toolbar_allsave_btn').on('click',function (e){
			_self.saveSqlAllFile();
		});

		// minimap
		$('.sql_toolbar_minimap_btn').on('click',function (evt){
			var minimap = _self.sqlMainEditor.getEditorOption('minimap');
			
			_self.sqlMainEditor.updateEditorOptions({minimap: {enabled: minimap.enabled?false :true }});
			
			if(!minimap.enabled){
				$(this).addClass('on');
			}else{
				$(this).removeClass('on');
			}
		});

		// 자동 줄바꿈.
		$('.sql_toolbar_linewrapper_btn').on('click',function (evt){
			var wordWrap = _self.sqlMainEditor.getEditorOption('wordWrap')=='on'?'off':'on';
			
			_self.sqlMainEditor.updateEditorOptions({wordWrap: wordWrap})
			
			if(wordWrap=='on'){
				$(this).addClass('on');
			}else{
				$(this).removeClass('on');
			}
		});

		// 실행취소
		$('.sql_toolbar_undo_btn').on('click',function (evt){
			_self.sqlMainEditor.undo();
		});

		// 다시실행.
		$('.sql_toolbar_redo_btn').on('click',function (evt){
			_self.sqlMainEditor.redo();
		});

		// sql 보내기
		$('.sql_toolbar_send_btn').on('click',function (evt){
			_self.sqlSend(evt);
		});

		// sql 포멧 정리.
		$('.sql_toolbar_format_btn').on('click',function (){
			_self.sqlFormatData();
		});

		// 메개변수 처리.
		$('#sql_parameter_toggle_btn').on('click',function (){
			if($('#sql_parameter_wrapper').hasClass('on')){
				$('#sql_parameter_wrapper').removeClass('on');
			}else{
				$('#sql_parameter_wrapper').addClass('on');
			}
		});

		// sql 파라미터 삭제.
		$('#sql_parameter_wrapper').on('click','.sql-param-del-btn',function (e){
			//if(confirm('삭제 하시겠습니까?')){
			$(this).closest('.sql-param-row').remove();
			//}
		});

		// param add
		$('.sql-param-add-btn').on('click',function (e){
			_self.addParamTemplate('add');
		});

		// sql file search
		$('#sqlFileSearchTxt').keydown(function(e) {
			if (e.keyCode == '13') {
				_self.sqlFileList();
			}
		});
		
		// sql text convert
		$('.sql_toolbar_convertext_btn').on('click',function (e){
			_self.sqlConvertText();
		});

		if(VARSQL.isUndefined(_g_options.screenSetting.sqlFileConfig)){
			VARSQL.util.objectMerge (_g_options.screenSetting,{sqlFileConfig:{enable :false}});
		}

		// sql file list view
		$('#sql_filelist_view_btn').on('click', function (){
			var sEle = $(this);
			var sEditorWrapperEle = $('#sql_editor_wrapper');

			var sqlFileConfig = {enable :false};
			if(sEditorWrapperEle.hasClass('sql-flielist-active')){
				sqlFileConfig.enable = false;
				sEditorWrapperEle.removeClass('sql-flielist-active');
				sEle.removeClass('on');
			}else{
				sqlFileConfig.enable = true;
				sEditorWrapperEle.addClass('sql-flielist-active');
				sEle.addClass('on');
			}

			if(_g_options.screenSetting.sqlFileConfig.enable !==sqlFileConfig.enable){
				_ui.preferences.save({sqlFileConfig :  sqlFileConfig});
			}

			if(sEle.attr('loadFlag') != 'Y'){
				_self.sqlFileList();
				sEle.attr('loadFlag','Y');
			}
		});

		if(_g_options.screenSetting.sqlFileConfig.enable ===true){
			$('#sql_filelist_view_btn').trigger('click');
		}
	}
	// editor selection text copy
	,selectionTextCopy: function (){
		copyStringToClipboard(this.getSql(), 'varsqleditor');
	}
	,findTextOpen : function(){
		this.sqlMainEditor.find();
	}
	// sql text convert change
	,sqlConvertText : function (){
		var _self = this; 
		
		if(this.convertTextDialog==null){
			_self.convertTextDialog = $('#queryConvertDialog').dialog({
				height: 370
				,width: 700
				,modal: true
				,buttons: {
					"Apply":function (){
						var sqlResult =convertTextToTemplate();
						_ui.SQL.addTextToEditorArea(sqlResult);
						_self.convertTextDialog.dialog( "close" );
					}
					,"Copy":function (){
						var sqlResult = convertTextToTemplate();
						_self.convertTextDialog.dialog( "close" );
						copyStringToClipboard(sqlResult);
					}
					,Cancel: function() {
						_self.convertTextDialog.dialog( "close" );
					}
				}
				,close: function() {
					_self.convertTextDialog.dialog( "close" );
				}
			});
			
			// sql convert text button
			$('.query_convert_text_btn').on('click',function (e){
				convertTextToTemplate();
			});
		}
		var sql = _self.getSql();
		
		if(sql != ''){
			$('#convertSqlText').val(sql);
		}
		
		VARSQL.req.ajax({
		    url: {type:VARSQL.uri.database, url:'/preferences/convertTextSetting'}
		    ,method :'get'
		    ,data : VARSQL.util.objectMerge ({},_g_options.param)
		    ,success:function (res){
		    	var convertTypeList = VARSQL.parseJSON(res.item) ||[];
			
		    	$('#queryConvertType').empty().html(VARSQLTemplate.render.text('{{#each datas}}<option value="{{key}}" {{addChar @first "selected" ""}}>{{name}}</option>{{/each }}',{
					datas : convertTypeList
				}));
				
				$('#queryConvertType').off('change');
				$('#queryConvertType').on('change', function (){
					var val = $(this).val();
					
					for(var i=0; i<convertTypeList.length;i++ ){
						var item = convertTypeList[i]; 
			    		if(item.key == val){
			    			$('#convertSqlTemplate').val(item.code);
			    			break; 
			    		};
			    	}
				})
				
				$('#convertSqlTemplate').val(convertTypeList[0].code);

		    	_self.convertTextDialog.dialog("open");
			}
		});
	}
	// editor 에 텍스트 추가.
	,addTextToEditorArea : function(addText, addOpts){
		
		if(!VARSQL.isUndefined(addOpts)){
			if(addOpts.type=='column'){
				addText = addTextChangeFormat(addOpts);
			}
		}

		addText = addText+'';
		this.sqlMainEditor.insertText(addText,true)
		
	}
	// sql 파라미터 셋팅.
	,addParamTemplate : function (mode, data){
		var _self =this;

		var rowTemplate = '<div class="sql-param-row">'
		+'	<span class="key"><input type="text" class="sql-param-key" name="sql-param-key" value="" /></span>'
		+'	<span class="val"><textarea class="sql-param-value" name="sql-param-value" rows="2"></textarea></span>'
		+'	<span class="remove"><button type="button" class="sql-param-del-btn"><i class="fa fa-minus"></i></button></span>'
		+'</div>';

		var currentParamEle = _self.sqlParameterEle;

		if('init' == mode){
			currentParamEle.empty();
		}

		var dataLen = Object.keys(data||{}).length;
		if(dataLen < 1) data = {'' :''};

		var focusFlag = ('data' == mode);
		for(var key in data){
			var rowEle = $(rowTemplate);

			rowEle.find('[name="sql-param-key"]').val(key);
			rowEle.find('[name="sql-param-value"]').val(data[key]);

			currentParamEle.append(rowEle);

			if(focusFlag){
				focusFlag =false;
				rowEle.find('[name="sql-param-value"]').focus();
			}
		}
	}
	// save sql
	,saveSqlFile : function (item, mode){
		var _self = this;

		var params;
		mode = mode || 'query';
		var saveInfo;
		if(mode=='query'){
			if(_self.getSqlEditorObj() ===false){
				return ;
			}
			_self.setSqlParamemter(_self.currentSqlEditorInfo.sqlId);

			saveInfo = {
				'sqlCont' :_self.getSqlEditorObj().getValue()
				,'sqlId' : _self.currentSqlEditorInfo.sqlId
				,'sqlParam' : JSON.stringify(_self.getSqlParamemter())
				,'editorCursor' : JSON.stringify(_self.getSqlEditorObj().getCursorPosition())
			};
		}else if(mode =='query_del'){
			saveInfo = {
				'sqlCont' : item.sqlCont
				,'sqlId' : item.sqlId
				,'sqlParam' : JSON.stringify(_self.getSqlParamemter(item.sqlId))
			};
		}else if(mode =='moveTab'){
			saveInfo = {
				'sqlId' : item.moveItem.sqlId
				,'firstSqlId' : _self.sqlFileTab.getFirstItem().sqlId
				,'prevSqlId' : (item.afterPrevItem||{}).sqlId
			};
		}else{
			saveInfo = VARSQL.util.objectMerge ({}, item);

			if (mode=='title'){
				if(_self.sqlFileTab.isItem(item.sqlId)){
					_self.sqlFileTab.updateItem({item:{
						"sqlId":item.sqlId
			    		,"sqlTitle":item.sqlTitle
					}, enabled:false});
				};
			}else if('newfile' == mode){
				saveInfo.prevSqlId = (_self.sqlFileTab.getLastItem().sqlId ||'');
			}
		}

		params =VARSQL.util.objectMerge ({}, _g_options.param, saveInfo);
		params.mode = mode;

		VARSQL.req.ajax({
		    url:{type:VARSQL.uri.sql, url:'/file/saveSql'}
		    ,data:params
		    ,success:function (res){
		    	var item = res.item;

		    	if(mode=='title' || 'newfile' == mode){
		    		_self.sqlFileList();
		    		if('newfile' == mode){
		    			var  newfileItem = {
		    				"sqlId":item.sqlId
		    				,"sqlTitle":params.sqlTitle
		    				,"sqlCont": ''
		    			}
		    			_self.addTabSqlEditorInfo(newfileItem);
		    			_self.loadEditor(newfileItem);
		    		}
		    	}else if(mode=='query'){
		    		var currentEditorInfo = _self.currentSqlEditorInfo;
					currentEditorInfo.item._isChange = false;

					_self.sqlFileTab.updateItem({item:{
						"sqlId":currentEditorInfo.sqlId
					}, enabled:false});
		    	}
			}
		});
	}
	// save all
	,saveSqlAllFile : function (){
		var _self =this;
		var allEditorObj = this.allTabSqlEditorObj;

		var queryCont = {};
		var sqlIdArr = [];

		for(var key in allEditorObj){
			var editorObj =allEditorObj[key];

			if(editorObj.item._isChange===true){
				sqlIdArr.push(key);

				var contValue = _self.sqlMainEditor.getValue(key);

				queryCont[key] = contValue;
				queryCont[key+'_param'] = JSON.stringify(_self.getSqlParamemter(key));
				queryCont[key+'_cursor'] =JSON.stringify(_self.sqlMainEditor.getCursorPosition(key));
			}
		}

		if(sqlIdArr.length > 0){
			queryCont['sqlIdArr'] =sqlIdArr.join(";");

			var params =VARSQL.util.objectMerge ({},_g_options.param,queryCont);

			VARSQL.req.ajax({
				url:{type:VARSQL.uri.sql, url:'/file/saveAllSql'}
			    ,data : params
			    ,success:function (resData){

			    	for(var i = 0 ;i < sqlIdArr.length;i++){
			    		var sqlId = sqlIdArr[i];

			    		var editorObj = allEditorObj[sqlId];
			    		editorObj.item._isChange = false;

			    		_self.sqlFileTab.updateItem({item:{
							"sqlId":sqlId
						}, enabled:false});
			    	}
				}
			});
		}
	}
	// sql 보내기.
	,sqlSend :function (){
		var _self = this;

		var sqlVal=''

		if(_self.getSqlEditorObj() !==false){
			sqlVal= _self.getSql();
			sqlVal=VARSQL.str.trim(sqlVal);
		}

		$('#noteTitle').val(VARSQL.util.dateFormat(new Date(), 'yyyy-mm-dd HH:MM')+'_title');
		$('#noteContent').val(sqlVal);

		if(_self.noteDialog==null){
			_self.noteDialog = $('#noteTemplate').dialog({
				height: 370
				,width: 700
				,modal: true
				,buttons: {
					"Send":function (){
						var recvList = _self.recvIdMultiSelectObj.getTargetItem();

						if(recvList.length < 1) {
							VARSQL.alertMessage('msg.add.param', VARSQL.message('recipient'));
							return ;
						}

						if(!VARSQL.confirmMessage('msg.send.confirm')) return ;

						var recv_id = [];
						
						$.each(recvList,function (i , item ){
							recv_id.push(item.viewid);
						});

						var params = {
							'noteTitle' : $('#noteTitle').val()
							,'noteCont' : $('#noteContent').val()
							,'recvId' : recv_id.join(';;')
						};
						
						VARSQL.req.ajax({
						    url:{type:VARSQL.uri.user, url:'/sendNote'}
						    ,data:params
						    ,success:function (resData){
						    	_self.noteDialog.dialog( "close" );
							}
						});
					}
					,Cancel: function() {
						_self.noteDialog.dialog( "close" );
					}
				}
				,close: function() {
					_self.noteDialog.dialog( "close" );
				}
			});
			
			_self.recvIdMultiSelectObj = $.pubMultiselect('#recvIdArr', {
				orientation : 'y'
				,duplicateCheck : true
				,height:235
				,valueKey : 'viewid'	
				,labelKey : 'uname'
				,header : {
					enableTargetLabel : true 	// target header label 보일지 여부
				}
				,body : {
					enableMoveBtn : false
					,enableItemEvtBtn : true 
				}
				,message :{
					duplicate: VARSQL.message('msg.item.added')
				}
				,source : {
					items : []
					,emptyMessage : VARSQL.message('msg.valid.search.param',{item : VARSQL.message('user')})
				}
				,target : {
					label : VARSQL.message('recipient')
					,items : []
					,emptyMessage : ' '
				}
			});
			
			// 메뉴 검색 input 
			$('#recv_user_search').on('keyup', function (e){
				if (e.keyCode == '13') {
					var params = { searchVal : $(this).val()};
	
					VARSQL.req.ajax({
					    url:{type:VARSQL.uri.user, url:'/searchUserList'}
					    ,data: params
					    ,success:function (resData){
					    	_self.recvIdMultiSelectObj.setSourceItem(resData.list);
						}
					});
				}
			});
		}

		_self.noteDialog.dialog("open");

		_self.recvIdMultiSelectObj.setTargetItem([]);

	}
	// sql file tab list
	,sqlFileTabList : function (){
		var _self = this;

		VARSQL.req.ajax({
		    loadSelector : '#sql_editor_wrapper'
		    ,url:{type:VARSQL.uri.sql, url:'/file/sqlFileTab'}
		    ,data:_g_options.param
		    ,success:function (res){
		    	var items = res.list
		    		,len = items.length;

		    	if(len > 0){
		    		_self.sqlFileTab.setItems(items);
		    		var enableItem;
		    		for(var i =0 ;i <len; i++){
		    			var sItem = items[i];
		    			sItem._isChange = false;
		    			_self.addTabSqlEditorInfo(sItem);

		    			if(sItem.viewYn){
		    				enableItem = sItem ;
		    			}
		    		}
		    		enableItem = enableItem ? enableItem : items[0];
		    		_self.loadEditor(enableItem, {saveFlag:false});
		    		_self.setSqlEditorBtnEnable();
		    	}else{
		    		_self.setSqlEditorBtnDisable();
		    	}
			}
		});
	}
	// 저장된 sql 목록 보기.
	,sqlFileList : function (){
		var _self = this;

		var params =VARSQL.util.objectMerge ({}, _g_options.param,{
			searchVal : $('#sqlFileSearchTxt').val()
		});

		VARSQL.req.ajax({
		    url:{type:VARSQL.uri.sql, url:'/file/sqlList'}
		    ,data:params
		    ,success:function (res){
		    	var items = res.list;
		    	var paging = res.page;
		    	var strHtm = []
		    		,len = items.length;

		    	if(items.length > 0){
		    		for(var i =0 ;i <len; i++){
		    			var item = items[i];
		    			strHtm.push('<li class="sql-flie-item-area" _idx="'+i+'"><a href="javascript:;" class="sql-flielist-item text-ellipsis" _mode="view" title="'+item.sqlTitle+'">'+item.sqlTitle+'</a>');
		    			strHtm.push('<a href="javascript:;" class="pull-right sql-flielist-item" _mode="del" title="삭제"><i class="fa fa-remove"></i></a>');
		    			strHtm.push('<a href="javascript:;" class="pull-right sql-flielist-item" _mode="setting" title="설정" style="margin-right:5px;"><i class="fa fa-gear"></i></a></li>');
		    		}
		    	}else{
		    		strHtm.push('<li>no data</li>')
		    	}

		    	$('#sql_filelist_area').empty().html(strHtm.join(''));

		    	$('#sql_filelist_area .sql-flielist-item').on('click', function (e){
		    		var sEle = $(this)
		    			, mode = sEle.attr('_mode')
		    			, itemArea = sEle.closest('.sql-flie-item-area')
		    			, idx = itemArea.attr('_idx');

		    		var sItem =items[idx];

		    		var sqlId = sItem.sqlId;

		    		if(mode=='view'){

		    			if(_self.sqlFileTab.isItem(sqlId)){
		    				_self.loadEditor(sItem);
		    			}else{
		    				params['sqlId'] = sqlId;
		    				VARSQL.req.ajax({
			    			    loadSelector : '#sql_editor_wrapper'
			    			    ,url:{type:VARSQL.uri.sql, url:'/file/sqlFileDetailInfo'}
			    			    ,data:params
			    			    ,success:function (res){
			    			    	_self.addTabSqlEditorInfo(res.item);
			    			    	_self.loadEditor(res.item);
			    			    }
			    			});
		    			}
		    		}else if(mode=='setting'){
		    			$('#editorSqlFileId').val(sqlId);
						$('#editorSqlFileNameText').val(sItem.sqlTitle);

						_self.sqlFileNameDialogEle.dialog("open");
		    		}else{
		    			if(!VARSQL.confirmMessage('msg.delete.confirm.param', {item : sItem.sqlTitle})){
		    				return ;
		    			}

		    			params['sqlId'] = sqlId;
		    			VARSQL.req.ajax({
		    				url:{type:VARSQL.uri.sql, url:'/file/delSqlSaveInfo'}
		    			    ,data:params
		    			    ,success:function (res){
		    			    	itemArea.remove();
		    			    	_self.deleteEditorInfo('del', sItem);
		    			    	_self.sqlFileList();
		    			    }
		    			});
		    		}
		    	})
			}
		});
	}
	,setEditorHistory :function (mode, historyItem){
		this.sqlMainEditor.history(mode, historyItem);
	}
	,loadEditor : function (sItem, opt){
		var _self = this;
		opt = opt||{};

		var sqlId = sItem.sqlId;

		sItem = (_self.allTabSqlEditorObj[sqlId] ? _self.allTabSqlEditorObj[sqlId].item : sItem);

		if(!_self.sqlMainEditor.existsContent(sqlId)){
			_ui.sqlDataArea.addDataGridEle(sItem)
		}else{
			if(this.currentSqlEditorInfo.sqlId ==sqlId){
				return ;
			}
		}

		_self.sqlMainEditor.viewContent(sItem, opt.isHistory);

		var isTabItem =_self.sqlFileTab.isItem(sqlId);

		if(!isTabItem){
			var lastItem = _self.sqlFileTab.getLastItem();

			_self.sqlFileTab.addItem({item:{
				sqlId : sqlId
				,sqlTitle : sItem.sqlTitle
			},enabled:false});

			_self.saveSqlFile({
				sqlId : sqlId
				, prevSqlId : (lastItem.sqlId ||'')
			},'addTab');// tab 정보 추가.

			_self.setSqlEditorBtnEnable();
		}else{
			if(opt.saveFlag !== false){
				_self.saveSqlFile({
					sqlId : sqlId
				},'viewTab');
			}
		}

		_self.sqlFileTab.setActive(sItem);
		_ui.sqlDataArea.setGridSelector(sItem);

		// set sql parameter start
		var beforeSqlId = _self.sqlParameterEle.attr('data-paramter-id');
		if(!VARSQL.isBlank(beforeSqlId) && VARSQL.hasProperty(_self.allTabSqlEditorObj, beforeSqlId)){
			_self.allTabSqlEditorObj[beforeSqlId].parameter = _self.getSqlParamemter();
		}

		if(_self.allTabSqlEditorObj[sqlId].parameter === false){
			var sqlParam = {'':''};
			try{
				sqlParam = VARSQL.parseJSON(sItem.sqlParam);
			}catch(e){}

			_self.allTabSqlEditorObj[sqlId].parameter = sqlParam;
		}

		_self.sqlParameterEle.attr('data-paramter-id',sqlId);
		_self.addParamTemplate('init', _self.allTabSqlEditorObj[sqlId].parameter);
		// set sql parameter end

		if($('[data-editor-id="empty"]').hasClass('active')) $('[data-editor-id="empty"]').removeClass('active');

		_self.setSelectSqlEditorInfo(sItem);
	}
	// set editor
	,setSelectSqlEditorInfo : function(item){
		if(!VARSQL.isBlank(this.currentSqlEditorInfo) && this.currentSqlEditorInfo.sqlId != item.sqlId){
			//close result grid layer 
			var gridObj = $.pubGrid(_ui.sqlDataArea.getSqlResultSelector(this.currentSqlEditorInfo , 'result')); 
			if(gridObj) gridObj.closeSettingLayer();
		}
	
		if(VARSQL.isUndefined(item._isChange)){
			item._isChange = false;
		}

		this.currentSqlEditorInfo = {
			sqlId : item.sqlId
			,item : item
		};
	}
	// tab 정보 추가.
	,addTabSqlEditorInfo : function(item){
		if(VARSQL.isUndefined(this.allTabSqlEditorObj[item.sqlId])){
			this.allTabSqlEditorObj[item.sqlId] = {item : item , parameter : false};
		}
	}
	//텍스트 박스 object
	,getSqlEditorObj:function(){

		if(this.sqlMainEditor && this.currentSqlEditorInfo){
			return this.sqlMainEditor;
		}else{
			return false;
		}
	}
	,getSql: function (executeSqlFlag){
		var _self = this;
		var executeSql = _self.sqlMainEditor.getSelectionValue(); 
		
		if(executeSql.trim() =='' && executeSqlFlag===true){
			var pos = _self.sqlMainEditor.getCursorPosition();
			var result = VARSQLUtils.split(_self.sqlMainEditor.getValue() ,{findLine : pos.lineNumber, findCharPos : pos.column});
			
			if(result.length >0 ){
				var item = result[0]; 
				_self.sqlMainEditor.setSelection({startLineNumber :item.startLine ,startColumn :item.startCharPos-1
					,endLineNumber :item.endLine ,endColumn : item.endCharPos
				});
				executeSql = item.statement;
			}
		}
		return executeSql;
	}
	// sql parameter editer
	,setSqlParamemter: function (sqlId){
		this.allTabSqlEditorObj[sqlId].parameter = this.getSqlParamemter();
	}
	// sql 실행시 셋팅 파라미터 구하기.
	,getSqlParamemter: function (sqlId){
		if(VARSQL.isUndefined(sqlId)){
			var sqlParam ={};
			this.sqlParameterEle.find('.sql-param-row').each(function(i, item){
				var sEle =$(this);
				var k = VARSQL.str.trim(sEle.find('[name="sql-param-key"]').val())
					,v= sEle.find('[name="sql-param-value"]').val();

				if(k != '' || VARSQL.str.trim(v) != '' ){
					sqlParam[(k==''?i:k)] = v;
				}
			})

			return sqlParam;
		}else{
			return this.allTabSqlEditorObj[sqlId].parameter ||{};
		}
	}
	// sql 실행시 파라미터 체크.
	,sqlParamCheck : function (sqlVal, sqlParam){
		var _self =this;
		
		var sqlAllParam =VARSQLUtils.getSqlParam(sqlVal);

		if(VARSQL.getLength(sqlAllParam) > 0){
			var addParam = {};
			var flag = true;
			
			for(var key in sqlAllParam){
				if(!VARSQL.hasProperty(sqlParam, key)){
					addParam[key] = '';
					flag = false;
				}
			}

			if(flag == false){
				$('#sql_parameter_wrapper').addClass('on');
				_self.addParamTemplate('data', addParam);

				var loopCnt = 0;
				var loopInter = setInterval(function (){
					if((loopCnt+1)%2==1){
						$('#sql_parameter_wrapper').css('background-color','#ffb3b3');
					}else{
						$('#sql_parameter_wrapper').css('background-color','');
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
	// sql 데이터 보기
	,sqlExecute :function (evt){
		var _self = this;
		var sqlVal = _self.getSql(true);

		_self._sqlExecute(sqlVal,true);
	}
	// sql 데이터 보기
	,_sqlExecute :function (sqlVal, paramFlag){
		var _self = this;

		if(top.mainSocketConnect) top.mainSocketConnect(); // main socket check

		sqlVal=VARSQL.str.trim(sqlVal);
		if('' == sqlVal){
			return ;
		}

		var sqlParam = {};

		if(paramFlag===true){
			sqlParam = _self.getSqlParamemter();
			if(!_self.sqlParamCheck(sqlVal, sqlParam)){
				return '';
			}
		}

		var params =VARSQL.util.objectMerge ({}, _g_options.param,{
			'sql' :sqlVal
			,'limit' : $(_self.options.limitCnt).val()
			,sqlParam : JSON.stringify(sqlParam)
		});
		
		VARSQL.req.ajax({
		    loadSelector : '.varsql-body-wrapper'
		    ,disableResultCheck : true
		    ,enableLoadSelectorBtn : true 
		    ,url:{type:VARSQL.uri.sql, url:'/base/execute'}
		    ,data:params
		    ,success:function (resData){
		    	_ui.sqlDataArea.viewResult(resData)
			}
		});
	}
	// sql format
	,sqlFormatData :function (formatType){
		var _self = this;
		var sqlVal = _self.getSql();
		sqlVal=VARSQL.str.trim(sqlVal);
		
		var allFormatFlag = false;

		if('' == sqlVal){
			allFormatFlag = true;
			sqlVal  = this.sqlMainEditor.getValue();
		}
		// script 모듈로 처리.
		try{
			var formatSql = getFormatSql(sqlVal, _g_options.dbtype, 'sql');

			if(allFormatFlag){
	    		this.sqlMainEditor.replaceAllContent(formatSql);
	    	}else{
				this.sqlMainEditor.insertText(formatSql,true);
	    	}
			return ;
		}catch(e){
			console.log(e.message);
		}

		// 쿼리로 처리.
		var params =VARSQL.util.objectMerge ({}, _g_options.param,{
			'sql' :sqlVal
		});

		params.formatType =formatType;

		VARSQL.req.ajax({
		    loadSelector : '#sql_editor_wrapper'
		    ,url:{type:VARSQL.uri.sql, url:'/base/format'}
		    ,data:params
		    ,success:function (res){
		    	var formatSql = res.item;
		    	formatSql = VARSQL.str.trim(formatSql);

		    	if(allFormatFlag){
		    		_self.sqlMainEditor.replaceAllContent(formatSql);
		    	}else{
					_self.sqlMainEditor.insertText(formatSql,true);
		    	}
			}
		});
	}
	// export data download
	,exportDataDownload : function (exportInfo){
		var key = exportInfo.downloadType
			,tmpName = exportInfo.objName
			,data = exportInfo.item;

		var exportItems = data.items;
		
		var items = [];
		for(var i =0; i<exportItems.length; i++){
			var exportItem = exportItems[i];
			items.push({
				name : exportItem[VARSQLCont.tableColKey.NAME] 
				,comment : exportItem[VARSQLCont.tableColKey.COMMENT] 
				,alias : exportItem[VARSQLCont.tableColKey.NAME] 
			});
			
		}
		
		var dataExportModealElId = '#data-export-modal';
	
		var modalEle = $(dataExportModealElId);
		
		if(modalEle.length > 0){
			modalEle.dialog( "open" );
			$('#exportObjectName').val(tmpName);
			$('#exportOrginObjectName').val(tmpName);
			$('#exportConditionQueryArea').addClass('display-off');
			$('#exportConditionQuery').val('');
			$('#exportAliasType').val('default');
			$.pubGrid('#data-export-column-list').setData(items);
			$.pubGrid('#data-export-column-list').setCheckItems('all');

			return ;
		}else{
			$(_g_options.hiddenArea).append($('#dataExportTemplate').html());
			modalEle = $(dataExportModealElId);
			$('#exportObjectName').val(tmpName);
			$('#exportOrginObjectName').val(tmpName);

			var exportConditionArea = $('#exportConditionQueryArea');
			$('#exportAdvancedBtn').on('click',function (e){

				if(exportConditionArea.hasClass('display-off')){
					exportConditionArea.removeClass('display-off');
				}else{
					exportConditionArea.addClass('display-off');
				}
			})
		}

		function exportData(){
			var chkItems =$.pubGrid('#data-export-column-list').getCheckItems();
			var chkItemLen = chkItems.length;
			if(chkItemLen < 1){
				VARSQLUI.alert.open({key:'msg.item.select'});
				return ;
			}

			if(!VARSQL.confirmMessage('msg.export.confirm')) return false;

			var columnNameArr = [];
			for(var i =0 ;i < chkItemLen;i++){
				var item = chkItems[i];
				columnNameArr.push({
					name : item.name
					,alias : item.alias
				});
			}

			var params =VARSQL.util.objectMerge ({}, _g_options.param,{
				exportType : VARSQL.check.radio('input:radio[name="exportType"]')
				,columnInfo : JSON.stringify(columnNameArr)
				,objectName : $('#exportOrginObjectName').val()
				,exportObjectName : $('#exportObjectName').val()
				,charset: $('#exportCharset').val()
				,limit: $('#exportCount').val()
				,conditionQuery: (!$('#exportConditionQueryArea').hasClass('display-off') ? $('#exportConditionQuery').val():'')
			});

			VARSQL.req.download({
				type: 'post'
				,loadSelector : dataExportModealElId
				,url: {type:VARSQL.uri.sql, url:'/base/dataExport'}
				,progressBar : true
				,mode : 2
				,params:params
				,enableLoadSelectorBtn : true 
			});
		}

		modalEle.dialog({
			height: 500
			,width: 730
			,modal: true
			,show : false
			,buttons: {
				"Export":function (){
					exportData();
				}
				,Cancel: function() {
					$( this ).dialog( "close" );
				}
			}
			,close: function() {
			  $( this ).dialog( "close" );
			}
		});
		
		$('#exportAliasType').on('change',function (){
			var val = $(this).val(); 
			
			var gridItems = $.pubGrid('#data-export-column-list').getItems();
			
			for(var i =0; i <gridItems.length; i++){
				var gridItem = gridItems[i];
				
				if(val=='upper'){
					gridItem.alias = toUpperCase(gridItem.alias); 
				}else if(val == 'lower'){
					gridItem.alias = toLowerCase(gridItem.alias);
				}else if(val == 'camel'){
					gridItem.alias = convertCamel(gridItem.alias);
				}else{
					gridItem.alias = gridItem.name;
				}
				$.pubGrid('#data-export-column-list').updateRow(i, gridItem);
			}
		})

		if(VARSQL.isUndefined($.pubGrid('#data-export-column-list'))){
			$.pubGrid('#data-export-column-list',{
				autoResize :false
				,editable:true 
				,asideOptions :{
					lineNumber : {enabled : true,width : 30}
					,rowSelector :{
						enabled : true
						,key : 'checkbox'
						,name : 'V'
						,width : 25
					}
				}
				,tColItem : [
					{key:'name' ,label :'Column', editable:false}
					,{key:'alias' ,label :'Alias', editable:true}
					,{key:'comment' ,label :'Desc', editable:false}
				]
				,tbodyItem :items
			});
			$.pubGrid('#data-export-column-list').setCheckItems('all');
		}
	}
	// 스크립트 내보내기
	,addCreateScriptSql :function (scriptInfo){
		var _self = this;
		_self.addSqlEditContent(generateSQL(scriptInfo), false);
	}
	// 에디터 영역에 값 넣기.
	,addSqlEditContent :function (cont, suffixAddFlag){
		var _self = this;

		cont = VARSQL.str.trim(cont);

		var insVal = VARSQLCont.constants.newline+cont;

		if(suffixAddFlag !== false){
			insVal = insVal +VARSQLCont.constants.querySuffix;
		}

		insVal =insVal+VARSQLCont.constants.newline;

		var editObj =_self.getSqlEditorObj();

		if(!editObj){
			VARSQLUI.alert.open({key:'msg.editor.empty.warning'});
			return ;
		}
		editObj.insertText(insVal);
	}
};

/**
 * sql data area
 */
_ui.sqlDataArea =  {
	_currnetQueryReusltData :{}
	,_cacheResultInfo : {}	// key= sqlid , value = sql result data
	,curentSqlEditorId : ''
	,currnetDataGridSelector : false
	,currnetDataGridColumnSelector :false
	,resultTab :{}	// sql result tab object
	,options :{
	}
	,init : function (){
		this.initTab();
		this.setGridSelector();
		this.addDataGridEle({sqlId : 'empty'});
		this.initResultDataContentMenu();
	}
	,initTab : function (){
		var _self = this;

		_self.resultTab = $.pubTab(_selector.plugin.sqlResult, {
			items : [
				{id :'queryData', name : VARSQL.message('result')}
				,{id :'queryColumn', name : VARSQL.message('column')}
				,{id :'queryLog', name : VARSQL.message('log')}
			]
			,itemKey :{							// item key mapping
				title :'name'
				,id: 'id'
			}
			,dropdown : {
				heightResponsive : true
			}
			,contentStyleClass : function(item){
				if(item.id == 'queryLog'){
					return 'sql-result-log';
				}
			}
			,titleIcon :{
				right : {
					html : '<i class="fa fa-eraser" style="cursor:pointer;"></i>'
					,onlyActiveView : true
					,visible : function (item){
						return item.id =='queryLog' ? true : false; 
					}
					,click : function (item, idx){
						_self.resultTab.clearTabContent(item);
					}
				}
			}
			,click : function (item){
				var itemId = item.id; 
				if(itemId =='queryData' || itemId=='queryColumn'){
					_self.resize();
					if(itemId=='queryColumn'){
						_self.viewResultColumnType();
					}
				}
				
			}
		});

		return ;
	}
	// add data grid element
	,addDataGridEle : function(item){
		$(this.resultTab.getTabContentSelector('queryData')).append('<div class="sql-result-tab-cont on" data-sqlresult-grid="'+item.sqlId+'"></div></div>');
		$(this.resultTab.getTabContentSelector('queryColumn')).append('<div class="sql-result-tab-cont on" data-sqlresult-column="'+item.sqlId+'"></div>');
	}
	//remove data grid element
	,removeDataGridEle : function(item){
		// result grid
		var resultSelector = this.getSqlResultSelector(item, 'result'); 
		var sqlResultObj = $.pubGrid(resultSelector);
		if(sqlResultObj) sqlResultObj.destroy();
		$(resultSelector).remove();

		//grid column
		var columnSelector = this.getSqlResultSelector(item, 'columnType'); 
		var sqlColmnuTypeObj = $.pubGrid(columnSelector);
		if(sqlColmnuTypeObj) sqlColmnuTypeObj.destroy();
		$(columnSelector).remove();

	}
	,setGridSelector :  function (item){

		if(VARSQL.isUndefined(item)){
			item = {sqlId : 'empty'}; 
		}

		this.curentSqlEditorId = item.sqlId;

		// grid
		$(this.resultTab.getTabContentSelector('queryData')+'>.on').removeClass('on')
		this.currnetDataGridSelector = this.getSqlResultSelector(item, 'result');
		$(this.currnetDataGridSelector).addClass('on');

		//grid column
		$(this.resultTab.getTabContentSelector('queryColumn')+'>.on').removeClass('on')
		this.currnetDataGridColumnSelector = this.getSqlResultSelector(item, 'columnType');
		$(this.currnetDataGridColumnSelector).addClass('on');

		if(this.resultTab.isActive('queryColumn')){
			this.viewResultColumnType();
		}

		this.resize();
	}
	// sql result select
	,getSqlResultSelector : function (item, type){

		if(type == 'result'){
			return '[data-sqlresult-grid="'+item.sqlId+'"]';
		}

		if(type == 'columnType'){
			return '[data-sqlresult-column="'+item.sqlId+'"]';
		}

	}
	// 결과 보기.
	,viewResult : function (resultData){
		var _self = this;

		_ui.layout.setActiveTab('sqlData');

		var msgViewFlag = false, gridViewFlag = false;

		var resultInfo = {};
		var resultMsg = [];
		var resultCode = resultData.resultCode;
		if(resultCode == 200){
			var resData = resultData.list;
    		var resultLen = resData.length;

    		var item;
			for(var i=resultLen-1; i>=0; i--){
				item = resData[i];
				
				if(item.resultType=='fail' || item.viewType=='msg'){
					msgViewFlag = true;
				}else if(item.viewType=='grid'){
					gridViewFlag = true;
					resultInfo =item;
				}

				resultMsg.push('<div class="'+(item.resultType =='fail' ? 'error' :'success')+'"><span class="log-end-time">'+VARSQLUtils.millitimeToFormat(item.endtime, VARSQLCont.timestampFormat)+' </span>#resultMsg#</div>'.replace('#resultMsg#', item.resultMessage));
			}
		}else{
			msgViewFlag = true;
			var errorMessage;
			var errQuery = '';
			var resItem = resultData.item || {};
			var msgItemResult = resItem.result || {};
			var errQuery = resItem.query;
			
			if(resultCode == 10002){
				resultInfo = msgItemResult;
				errorMessage = msgItemResult.resultMessage;
				
				_self.setGridData(resultInfo);
			}else{
				errorMessage = resultData.message;
			}
			
			var logValEle = $('<div><div class="error"><span class="log-end-time">'+VARSQLUtils.millitimeToFormat(msgItemResult.endtime,VARSQLCont.timestampFormat)+' </span>#resultMsg#</div></div>'.replace('#resultMsg#' , '<span class="error-message">'+errorMessage+'</span><br/>sql line : <span class="error-line">['+resultData.customMap.errorLine+']</span> query: <span class="log-query"></span>'));
			logValEle.find('.log-query').text(errQuery);

			resultMsg.push(logValEle.html());
			logValEle.empty();
			logValEle= null;
		}

		if(msgViewFlag){
			this.resultTab.itemClick({id :'queryLog'});
		}else{
			this.resultTab.itemClick({id :'queryData'});
		}

		if(gridViewFlag){
			_self.setGridData(resultInfo);
		}

		var msgAreaEle = $(this.resultTab.getTabContentSelector('queryLog'));

		msgAreaEle.prepend(resultMsg.join(''));
		msgAreaEle.animate({scrollTop: 0},'fast');
	}
	// sql data grid
	,setGridData: function (pGridData){
		var _self = this;

		this._cacheResultInfo[this.curentSqlEditorId] = {
			isColumnView : false
			,resultData : pGridData
		};
		
		$.pubGrid(_self.currnetDataGridSelector,{
			setting : {
				enabled : true
				,mode :'full'
				,btnClose : true
				,width:700
				,height:230
				,util : {
					isTypeNumber : function (hederInfo){ 
						return hederInfo.number;
					}
				}
			}
			,autoResize : false
			,asideOptions :{
				lineNumber : {enabled : true}
			}
			,scroll :{	// 스크롤 옵션
				vertical : {
					speed : 3			// 스크롤 스피드 row 1
				}
			}
			,headerOptions:{
        		helpBtn:{
					enabled : true
					,title : 'Add Column Name'
					,click :  function (clickInfo){
						var item = clickInfo.item;
						_ui.SQL.addTextToEditorArea('',{type:'column' , header : item});
					}
				}
				,drag:{
					enabled : true
				}
			}
			,valueFilter : function (colInfo, objValue){
				var val = objValue[colInfo.key];
				if((colInfo.type =='string' || colInfo.lob) && !VARSQL.isBlank(val) && val.length >1000){
						return val.substring(0, 1000)+'...';
				}else{
					return val;
				}
			}
			,bodyOptions :{
				cellDblClick : function (rowItem){
					_ui.SQL.addTextToEditorArea(rowItem.item[rowItem.keyItem.key]);
				}
			}
			,navigation : {
				status :true
				,height :20
				,enableSelectionInfo : true
			}
			,tColItem : pGridData.column
			,tbodyItem :pGridData.data
		});
	}
	,initResultDataContentMenu : function (){
		var _self = this;
		var queryColumnSelector = this.resultTab.getTabContentSelector('queryData'); 
		
		var dataSelectorEle =$(queryColumnSelector);
		var gridContextObj = $.pubContextMenu(queryColumnSelector, {
			items: [
				{key : "copy" , "name": "Copy"}
				,{divider : true}
				,{key : "select_col" , "name": "컬럼명"
					,subMenu : [
						{ key : "select_col_copy","name": "컬럼명 Copy"}
						,{ key : "select_col_editor","name": "컬럼명 넣기"}
						,{ key : "select_col_val_editor","name": "컬럼명 & 값 넣기"}
					]
				}
				,{divider : true}
				,{key : "detail" , "name": "상세보기"}
				,{key : "download" , "name": "다운로드"
					,subMenu : [
						{checkbox : true , name:'selet data' , key:'sqlGridResultSelect'}
						,{divider : true}
						,{ key : "download_excel","name": "EXCEL" ,mode:"excel"}
						,{ key : "download_csv","name": "CSV" ,mode:"csv" }
						,{ key : "download_xml","name": "XML" ,mode:"xml"}
						,{ key : "download_json","name": "json" ,mode:"json"}
					]
				}
			]
			,isEnabled : function (){
				return $(_self.resultTab.getTabContentSelector('queryData')+'>.on').find('.pubGrid').length > 0;
			}
			,beforeSelect :function (contextInfo){
				var trEle = $(contextInfo.evt.target).closest('.pub-body-tr', dataSelectorEle);
				if(trEle.length > 0){
					gridContextObj.enableItem('detail',0);
					var itemObj = $.pubGrid(_self.currnetDataGridSelector).getRowItemToElement(trEle);
					gridContextObj.setTargetInfo(itemObj.item);
				}else{
					gridContextObj.disableItem('detail',0);
				}

			}
			,disableItemKey : function (items){
				
				if($.pubGrid(_self.currnetDataGridSelector).getSelectionColInfos().length < 1){
					return [
						{key :'select_col' , depth :0	}
					];
				}
				return [];
			}
			,callback: function(key,sObj) {
				if(key =='copy'){
					$.pubGrid(_self.currnetDataGridSelector).copyData();
					return ;
				}

				if(key =='select_col_editor' || key =='select_col_copy'){
					var colInfos= $.pubGrid(_self.currnetDataGridSelector).getSelectionColInfos();

					if(colInfos.length >0){
						if(key =='select_col_copy'){
							var colLabel  = [];
							for(var i=0; i<colInfos.length; i++){
								colLabel.push(colInfos[i].label);
							}
							copyStringToClipboard(colLabel.join('\t'));
						}else{
							_ui.SQL.addTextToEditorArea('',{type:'column', header: colInfos});
						}
					}
					return ;
				}

				if(key == 'select_col_val_editor'){
					var colInfo= $.pubGrid(_self.currnetDataGridSelector).selectionData('json');

					if(colInfo.header && colInfo.header.length >0){
						_ui.SQL.addTextToEditorArea('',{type:'column', header: colInfo.header, data : colInfo.data});
					}
					return ;
				}

				if(key =='detail'){
					var popupId = 'pop_'+_g_options.param.conuid

					var popupObj = VARSQLUI.popup.open(VARSQL.getContextPathUrl('/database/extension/detailView?conuid='+_g_options.param.conuid), {
						name : 'detailView' +_g_options.param.conuid
						,viewOption : 'width=1000,height=710,scrollbars=1,resizable=1,status=0,toolbar=0,menubar=0,location=0'
					});

					try{
						if(VARSQL.isUndefined(popupObj.viewItem)){
							window[popupId] = function (){
								popupObj.viewItem( $.pubGrid(_self.currnetDataGridSelector).getHeaderItems() , $.pubGrid(_self.currnetDataGridSelector).getItems());
							}
						}else{
							popupObj.viewItem( $.pubGrid(_self.currnetDataGridSelector).getHeaderItems() , $.pubGrid(_self.currnetDataGridSelector).getItems());
						}
					}catch(e){}

					return ;
				}

				if(key.indexOf('download_') > -1){
					var sqlGridResultSelect = gridContextObj.getCheckBoxId('sqlGridResultSelect');
					var isSelect = $("#"+sqlGridResultSelect).is(":checked");

					var selData = $.pubGrid(_self.currnetDataGridSelector).getData({isSelect:isSelect, dataType:'json'});
					var mode = sObj.mode;

					var params =VARSQL.util.objectMerge ({}, _g_options.param,{
						exportType :mode
						,headerInfo : JSON.stringify(selData.header)
						,gridData : JSON.stringify(selData.data)
					});

					VARSQL.req.download({
						type: 'post'
						,url: {type:VARSQL.uri.sql, url:'/base/gridDownload'}
						,params: params
					});

					return
				}
			}
		});
	}
	// sql result column typ
	,viewResultColumnType : function (){
		var _self = this;

		var cacheResultInfo = this._cacheResultInfo[this.curentSqlEditorId];

		if(VARSQL.isUndefined(cacheResultInfo) || cacheResultInfo.isColumnView===true){
			return ; 
		}

		cacheResultInfo.isColumnView = true; 

		var resultData = cacheResultInfo.resultData;

		var columnTypeArr = resultData.column;

		var gridObj = $.pubGrid(_self.currnetDataGridColumnSelector);

		if(gridObj){
			gridObj.setData(columnTypeArr,'reDraw');
			return ;
		}

		$.pubGrid(_self.currnetDataGridColumnSelector,{
			height:'auto'
			,page :false
			,setting : {
				enabled : true
				,enableSearch : true
			}
			,asideOptions: {
				lineNumber : {enabled : true, width : 30, styleCss : 'text-align:right;padding-right:3px;'}
			}
			,tColItem: [
				{label: "NAME", key: "label"}
				,{label: "TYPE", key: "dbType"}
			]
			,tbodyItem: columnTypeArr
			,bodyOptions: {
				cellDblClick : function (rowItem){
					_ui.SQL.addTextToEditorArea('', {type:'column', header : rowItem.item});
				}
			}
			,rowOptions :{
				contextMenu : {
					beforeSelect :function (){
						$(this).trigger('click');
					}
					,callback: function(key,sObj) {
						if(key =='copy'){
							$.pubGrid(_self.currnetDataGridColumnSelector).copyData();
							return ;
						}

					},
					items: [
						{key : "copy" , "name": "Copy"}
					]
				}
			}
		});
	}
	,resize : function (dimension){
		if(this.resultTab) this.resultTab.resize()
		
		try{
			$.pubGrid(this.currnetDataGridSelector).resizeDraw();
		}catch(e){
			//console.log(e)
		}
		try{
			$.pubGrid(this.currnetDataGridColumnSelector).resizeDraw();
		}catch(e){
			//console.log(e)
		}
	}
}


//glossary component
_ui.registerPlugin({
	'glossary' : {
		selector :'#pluginGlossary'
		,gridObj : false
		,init : function (){
			var _self = this;

			_self.initEvt();

			_self.gridObj = $.pubGrid('#glossaryResultArea', {
				asideOptions :{lineNumber : {enabled : true	,width : 30}}
				,tColItem : [
					{ label: 'WORD', key: 'word',width:80 },
					{ label: 'EN', key: 'wordEn' },
					{ label: 'ABBR', key: 'wordAbbr', width:45},
					{ label: 'DESC', key: 'wordDesc',width:45},
				]
				,tbodyItem :[]
				,bodyOptions : {
					cellDblClick : function (cellInfo){

						var selKey =cellInfo.keyItem.key;

						if(selKey !='wordDesc'){

							var variableText = $(_self.selector+' #glossaryConvertTxt').val();

							var val =cellInfo.item[selKey];

							val = val.split(' ').join('_');

							if(VARSQL.str.trim(variableText)==''){
								variableText = val;
							}else{
								variableText = variableText+'_'+val;
							}

							$(_self.selector+' #glossaryConvertTxt').val(variableText);
						}
					}
				}
			});
		}
		,initEvt : function (){
			var _self = this;

			// enter 검색.
			$(_self.selector+' #glossarySearchTxt').on('keydown', function (e){
				if (e.keyCode == '13') {
					_self.search();
				}
			})

			// 검색
			$(_self.selector+' .glossary-search-btn').on('click', function (e){
				_self.search();
			})

			// 변환
			$(_self.selector+' .glossary-convert-camelcase').on('click', function (e){
				$(_self.selector+' #glossaryConvertTxt').val(convertCamel($(_self.selector+' #glossaryConvertTxt').val()));
			})
			// 지우기
			$(_self.selector+' .glossary-convert-clear').on('click', function (e){
				$(_self.selector+' #glossaryConvertTxt').val('');
			})
		}
		,search :  function (){
			var _self = this;
			var schVal = $(_self.selector+' #glossarySearchTxt').val();

			schVal = VARSQL.str.trim(schVal);

			if(schVal.length < 1){
				return ;
			}

			var params ={
				searchVal : schVal
			}

			VARSQL.req.ajax({
			    loadSelector : _self.selector
			    ,url:{type:VARSQL.uri.plugin, url:'/glossary/search'}
			    ,data : params
			    ,success:function (res){
			    	_self.gridObj.setData(res.list,'reDraw');
				}
			});
		}
		,template : function (){
			return $('#glossaryComponentTemplate').html();
		}
		,resize : function (dimension){
			this.gridObj.resizeDraw();
		}
		,destroy: function (){
			this.gridObj.destroy();
		}
	}
})

//history component
_ui.registerPlugin({
	'history' : {
		selector :'#pluginHistory'
		,gridObj :false
		,pageNo :1
		,scrollEndFlag : true
		,init : function (){
			var _self = this;
			_self.initEvt();
		}
		,initEvt : function (){
			var _self = this;
			// enter 검색.
			$(_self.selector+' #historySearchTxt').on('keydown', function (e){
				if (e.keyCode == '13') {
					_self.search();
				}
			})
			// 검색
			$(_self.selector+' .history-search-btn').on('click', function (e){
				_self.search();
			})

			_self.gridObj = $.pubGrid('#historyResultArea', {
				asideOptions :{lineNumber : {enabled : true	,width : 30}}
				,tColItem : [
					{ label: 'SQL', key: 'logSql'},
					{ label: 'Start Time', key: 'startTime' },
					{ label: 'End Time', key: 'endTime', width:45},
					{ label: 'Delay Time', key: 'delayTime',width:45},
				]
				,tbodyItem :[]
				,bodyOptions : {
					cellDblClick : function (cellInfo){

						var selKey =cellInfo.keyItem.key;

						if(selKey == 'logSql'){
							var val =cellInfo.item[selKey];
							_ui.SQL.addSqlEditContent(val , false);
						}
					}
				}
				,scroll :{
					vertical : {
						onUpdate : function (item){	// 스크롤 업데이트.
							if(_self.scrollEndFlag !==true && item.barPosition > 80){
								_self.pageNo = _self.pageNo+1;
								_self.search('scroll');
							}
						}
					}
				}
			});
		}
		,search :  function (mode){
			var _self = this;
			var schVal = $(_self.selector+' #historySearchTxt').val();

			schVal = VARSQL.str.trim(schVal);

			if(mode != 'scroll'){
				_self.pageNo = 1;
			}

			var params ={
				pageNo: _self.pageNo
				,countPerPage : _self.gridObj.getViewRow() * 2
				,'searchVal':schVal
				,conuid : _g_options.param.conuid
			}

			VARSQL.req.ajax({
			    loadSelector : _self.selector
			    ,url:{type:VARSQL.uri.plugin, url:'/history/search'}
			    ,data : params
			    ,success:function (res){

			    	var items = res.list ||[];

			    	var itemLen =items.length;

			    	if(_self.pageNo ==1){
			    		_self.gridObj.setData(items);
			    	}else{
			    		_self.gridObj.addRow(items);
			    	}

			    	if(itemLen> 0){
			    		_self.scrollEndFlag = false;
			    	}else{
			    		_self.scrollEndFlag = true;
			    	}
				}
			});
		}
		,template : function (){
			return $('#historyPluginAreaTemplate').html();
		}
		,resize : function (dimension){
			this.gridObj.resizeDraw();
		}
		,destroy: function (){
		}
	}
})

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
			VARSQLUI.alert.open(e);
		}
	},
	end :function (divObj){
		try{
			$('.'+divObj.replace(/^[.#]/, '') +'dialog-modal').hide();
		}catch(e){
			VARSQLUI.alert.open(e);
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
	,copy :function (copyString, copyType){
		var _this = this;

		if(_this.modalEle === false){
			var modalEle = $('#data-copy-modal');
			//$(_g_options.hiddenArea).append('<div id=\"data-copy-modal\" title="Copy" style="overflow:hidden"><textarea id="data-copy-area" class="wh100"></textarea></div>');
			$(_g_options.hiddenArea).append('<div id=\"data-copy-modal\" title="Copy" style="overflow:hidden" class="pretty-view-area"><pre id="data-copy-area" class="user-select-on prettyprint lang-sql" contenteditable="true"></pre><textarea id="data-orgin-area" style="display:none;"></textarea></div>');
			modalEle = $('#data-copy-modal');

			_this.modalEle = modalEle.dialog({
				height: 350
				,width: 640
				,modal: true
				,buttons: {
					'Copy':function (){
						_this.modalEle.dialog( "close" );
						copyStringToClipboard($('#data-orgin-area').val(), 'programCodeCopy');
					}
					,'Cancel': function() {
						_this.modalEle.dialog( "close" );
					}
				}
				,close: function() {
					_this.modalEle.dialog( "close" );
				}
			});
		}else{
			_this.modalEle.dialog( "open" );
		}

		var dataCopyArea = $('#data-copy-area');
		dataCopyArea.empty().html(copyString).removeClass('prettyprinted');

		dataCopyArea.scrollTop(0);

		if(copyType=='java'){
			dataCopyArea.removeClass('lang-sql').addClass('language-java');
		}else{
			dataCopyArea.removeClass('language-java').addClass('lang-sql');
		}

		$('#data-orgin-area').val(copyString);
		PR.prettyPrint();
	}
}

/**
 * getTableName
 * @param tblName {String}
 */
function getTableName(tblName){

	if(typeof tblName ==='object'){
		if(!VARSQL.isBlank(tblName.schema)){
			return tblName.schema+'.'+tblName.name; 
		}
		
		tblName = tblName.name;
	}
	
	if(_g_options.schema != _g_options.param.schema){
		tblName = _g_options.param.schema+'.'+tblName;
	}

	return tblName;
}

function getFormatSql(sql, dbtype, sqlType){
	try{
		
		if(toLowerCase(dbtype) == 'sqlserver' && sqlType=='ddl'){
			return sql; 	
		}
		
		var formatType = VARSQLCont.formatType(_g_options.dbtype);
		
		var indentStyle = 'tabularLeft'; 
		if(sql =='ddl'){
			indentStyle = 'standard';
		}
		
		if(/\s?create|alter/gi.test(sql)){
			indentStyle = 'standard';
		}
		
		return sqlFormatter.format(sql, {
			language: formatType,
	        lineBetweenQueries : 2,
	        indentStyle : indentStyle,
		  	paramTypes: { custom: [{ regex: String.raw`#\{\w+\}` }, { regex: String.raw`\$\{\w+\}` }] },
		})
	}catch(e){
		if(sqlType == 'sql'){
			throw e;
		}
		return sql;
	}
}

/**
 * sql gen
 * @param scriptInfo
 * @returns
 */
function generateSQL(scriptInfo){
	var sqlGenType = scriptInfo.sqlGenType
		,data = scriptInfo.item
		,param_yn  = scriptInfo.param_yn;
		
	var serviceObjName = getTableName( (scriptInfo.objInfo && scriptInfo.objInfo.name ? scriptInfo.objInfo : scriptInfo.objName));

	sqlGenType =sqlGenType.split('|');

	var key =sqlGenType[0]
		,keyMode = sqlGenType[1];

	param_yn = param_yn?param_yn:'N';

	var reval =[];

	var dataArr = data.items, tmpval , item;

	var len = dataArr.length;

	if(key=='selectStar'){ // select 모든것.
		reval.push('select * from '+serviceObjName);

	}else if(key=='selectCount'){// count query
		reval.push('select count(1) from '+serviceObjName);
	}
	else if(key=='select'){ // select 컬럼 값
		reval.push('select ');
		for(var i=0; i < len; i++){
			item = dataArr[i];
			reval.push((i==0?'':', ')+item[VARSQLCont.tableColKey.NAME]);
		}

		reval.push(' from '+serviceObjName);

	}
	else if(key=='selectWhere'){ // select 컬럼 값
		reval.push('select ');
		var whereClause = [];
		for(var i=0; i < len; i++){
			item = dataArr[i];
			reval.push((i==0?'':', ')+item[VARSQLCont.tableColKey.NAME]);
			whereClause.push((i==0?'where ':'and ')+item[VARSQLCont.tableColKey.NAME]+" = ''");
		}

		reval.push('\nfrom '+serviceObjName);
		reval.push('\n'+whereClause.join('\n'));
	}
	else if(key=='insert'){ // insert 문
		reval.push('insert into '+serviceObjName+' (');
		var valuesStr = [];
		for(var i=0; i < len; i++){
			item = dataArr[i];
			if(i!=0){
				reval.push(', ');
				valuesStr.push(', ');
			}
			reval.push(item[VARSQLCont.tableColKey.NAME]);

			valuesStr.push(queryParameter(param_yn, item, keyMode));

		}
		reval.push(' )'+VARSQLCont.constants.newline +'values( '+ valuesStr.join('')+' )');

	}
	else if(key=='update'){ // update 문
		reval.push('update '+serviceObjName+VARSQLCont.constants.newline+' set ');

		var keyStr = [];
		var firstFlag = true;

		for(var i=0; i < len; i++){
			item = dataArr[i];

			tmpval = queryParameter(param_yn, item, keyMode);

			var constraintVal = item[VARSQLCont.tableColKey.CONSTRAINTS] ||'';
			if(constraintVal =='PK' || constraintVal.indexOf('PRIMARY') > -1 ){
				keyStr.push(item[VARSQLCont.tableColKey.NAME]+ ' = '+ tmpval);
			}else{
				if(!firstFlag){
					reval.push(', ');
				}
				reval.push(item[VARSQLCont.tableColKey.NAME]+ ' = '+ tmpval);
				firstFlag = false;
			}
		}

		if(keyStr.length > 0) reval.push(VARSQLCont.constants.newline+'where '+keyStr.join(' and '));

	}
	else if(key=='delete'){ // delete 문
		reval.push('delete from '+serviceObjName);

		var item;
		var keyStr = [];
		
		for(var i=0; i < len; i++){
			item = dataArr[i];
			var constraintVal = item[VARSQLCont.tableColKey.CONSTRAINTS] ||'';
			if(constraintVal =='PK' || constraintVal.indexOf('PRIMARY') > -1 ){
				tmpval = queryParameter(param_yn, item, keyMode);

				keyStr.push(item[VARSQLCont.tableColKey.NAME]+ ' = '+ tmpval);
			}
		}
		
		console.log(keyStr.length)

		if(keyStr.length > 0) reval.push(VARSQLCont.constants.newline+'where '+keyStr.join(' and '));

	}
	else if(key=='drop'){ // drop 문
		reval.push('drop table '+serviceObjName);
	}

	return reval.join('');
}

function queryParameter(flag, columnInfo, colNameCase){
	var colName = columnInfo[VARSQLCont.tableColKey.NAME];

	if(flag=='Y'){
		if(colNameCase=='camel'){
			colName = convertCamel(colName);
		}
		return VARSQLCont.constants.queryParameterPrefix+colName+VARSQLCont.constants.queryParameterSuffix;
	}else{
		return VARSQLCont.getDefaultValue(columnInfo);
	}
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
//camel case -> underscorecase 변환
function convertUnderscoreCase(str){
	if(str == '') {
		return str;
	}
	return str.split(/(?=[A-Z])/).join('_').toUpperCase();
}

function toLowerCase(str){
	return (str || '').toLowerCase()
}

function toUpperCase(str){
	return (str || '').toUpperCase()
}

function copyStringToClipboard (copyText, prefix) {

	var isRTL = document.documentElement.getAttribute('dir') == 'rtl';

	if (typeof window.clipboardData !== "undefined" &&
	  typeof window.clipboardData.setData !== "undefined") {
		window.clipboardData.setData("Text", copyText);
		return ;
	}

	prefix = prefix||'varsqlMain';

	var _id = prefix+'CopyTextId';
	var copyArea = document.getElementById(_id);
	if(!copyArea){
		var fakeElem = document.createElement('textarea');
		var yPosition = window.pageYOffset || document.documentElement.scrollTop;
		fakeElem.id =_id;
		fakeElem.style = 'top:'+yPosition+'px;z-index:9999999;font-size : 12pt;border:0;padding:0;margin:0;position:absolute;' +(isRTL ? 'right' : 'left')+':-9999px';
		fakeElem.setAttribute('readonly', '');

		document.body.appendChild(fakeElem);
		copyArea = document.getElementById(_id);
	}

	copyArea.value = copyText;
	copyArea.select();

	function handler (){
		document.removeEventListener('copy', handler);
		copyArea = null;
	}
	document.addEventListener('copy', handler);

	document.execCommand('copy');
}

// change format
function addTextChangeFormat(colInfo){
	
	var templateItem ={
		separator : ', '
		,format : '{col}'
	};
	
	if(VARSQL.isArray(colInfo.data)){
		templateItem = {
			separator : 'and '
			,format : '{col}={val}'
			,multipleFormat : '{col} in ({val})'
			,conditionTemplate : true
		};
	}

	var headerArr = VARSQL.isArray(colInfo.header) ?colInfo.header : [colInfo.header];
	var dataArr = VARSQL.isArray(colInfo.data) ? colInfo.data : (VARSQL.isUndefined(colInfo.data) ? [] : [colInfo.data]);
	var revalStr = [];

	for(var i =0; i < headerArr.length; i++){
		var headerItem = headerArr[i];

		var formatStr ='';
		
		var addVal = null;
		if(templateItem.addValueFlag !== false && headerItem.lob !== true){
			var addValue = [];
			var dupChkVal = [];
			for (var j = 0; j < dataArr.length; j++) {
				var dataItem = dataArr[j];
				var val = dataItem[headerItem.key];
				if(val && VARSQL.inArray(dupChkVal, val)==-1){
					if(headerItem.number){
						addValue.push(val);
					}else {
						addValue.push('\''+val +'\'');
					}
					dupChkVal.push(val);
				}
			}

			if(addValue.length > 1 && templateItem.multipleFormat){
				addVal = addValue.join(',');
				formatStr += templateItem.multipleFormat;
			}else{
				addVal = addValue[0];
				formatStr += templateItem.format;
			}
		}else {
			formatStr += templateItem.format;
		}

		if(VARSQL.isBlank(addVal)){
			if(headerItem.number){
				addVal = 0;
			}else {
				addVal = '\'\'';
			}
		}
		
		if(i != 0 ){
			 var sep = templateItem.secondSeparator || templateItem.separator;
			 formatStr = sep + formatStr;
			 if(VARSQL.str.trim(sep) !=','){
				formatStr = '\n' + formatStr;
			 }
		}

		revalStr.push(VARSQL.message(formatStr,{
			col : (headerItem.column||headerItem.label)
			,val : addVal
		})+' ');
	}

	return revalStr.join('');
}

// text -> template로 변경.
function convertTextToTemplate(val){

	var splitCharObj = {
		newline : '\n'
		,tab : '\t'
		,comma : ','
		,space : ' '
	};

	var type = $('#queryConvertType').val();
	var splitChar = $('#queryConvertSplitChar').val();

	var text = $('#convertSqlText').val();
	var template = $('#convertSqlTemplate').val();
	
	if(type == 'single' || type=='javascript'){
		text = text.replace(/\\/gm,'\\\\');
		text = text.replace(/\'/gm,'\\\'')
	}else if(type == 'double' || type=='java'){
		text = text.replace(/\\/gm,'\\\\');
		text = text.replace(/\"/gm,'\\\"');
	}

	var textArr = text.split(splitCharObj[splitChar]);

	var convertData = [];
	for(var i =0 ;i < textArr.length; i++){
		convertData.push({
			text : textArr[i]
		});
	}
	
	var result = VARSQLTemplate.render.text(template,{
		datas : convertData
	});
	
	$('#convertSqlResult').val(result);
	
	return result; 
}

VARSQL.ui = VARSQL.ui||{};
VARSQL.ui.create = function (_opts){

	VARSQLCont.init(_opts.dbtype);
	_ui.base.mimetype = VARSQLCont.editorMimetype(); 

	_opts.screenSetting = _opts.userSettingInfo['main.database.setting'];

	delete _opts.userSettingInfo['main.database.setting'];

	_g_options = VARSQL.util.objectMerge(_g_options, _opts);

	_ui.initContextMenu();
	_ui.headerMenu.init();

	_ui.layout.init(_opts);

	// add extension service object
	var extensionServiceObject = VARSQL.vender[_opts.dbtype]||{};
	_ui.addDbServiceObject(extensionServiceObject.serviceObject)
	_ui.addODbServiceObjectMetadata(extensionServiceObject.objectMetadata)
}

VARSQL.ui.layoutResize  = function (){
	_ui.layout.layoutResize();
}

}(jQuery, window, document,VARSQL));
