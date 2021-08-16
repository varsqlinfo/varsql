/**
*ytkim
*varsql db ui js
 */
;(function($, window, document, VARSQL) {
"use strict";

var _defaultOptions = {
	dateFormat :'yyyy-MM-dd hh:mm:ss'
}

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

		for(var i =0; i<serviceObject.length; i++){
			var serviceObj = serviceObject[i];
			var contentid =serviceObj.contentid;

			_g_cache_obj_meta_store[contentid] = {initFlag:false}; // 초기화 여부
		}
	}
	// object cache check
	,isSOMetaInitCache : function (objectType){
		return (_g_cache_obj_meta_store[objectType]||{}).initFlag===false ? false :true;
	}
	//set  object initflag
	,setSOMetaInitFlag : function (objectType){
		_g_cache_obj_meta_store[objectType].initFlag=true;
	}
	// 메타 데이타 케쉬된값 꺼내기
	,getSOMetaCache:function (objectType, objecName, tabKey){
		tabKey =tabKey||'column';

		var cacheObj = _g_cache_obj_meta_store[objectType][objecName];

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
	// 메타 데이타 셋팅하기.
	,setSOMetaCache:function (objectType, objecName, tabKey, data){
		if(VARSQL.isUndefined(_g_cache_obj_meta_store[objectType][objecName])){
			var objData = {};
			objData[tabKey] = data;
			_g_cache_obj_meta_store[objectType][objecName] =objData;
		}else{
			_g_cache_obj_meta_store[objectType][objecName][tabKey]= data;
		}
	}
	// remove service object meta cache
	,removeSOMetaCache:function (objectType, objecName){
		if(typeof objectType !='undefined' && typeof objecName != 'undefined'){
			delete _g_cache_obj_meta_store[objectType][objecName];
		} else if (typeof objectType !='undefined'){
			_g_cache_obj_meta_store[objectType] ={};
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


VARSQL.ui = VARSQL.ui||{};
VARSQL.ui.create = function (_opts){

	VARSQLCont.init(_opts.dbtype , _ui.base);

	_opts.screenSetting = _opts.userSettingInfo['main.database.setting'];

	delete _opts.userSettingInfo['main.database.setting'];

	_g_options = VARSQL.util.objectMerge(_g_options, _opts);

	_ui.initContextMenu();
	_ui.headerMenu.init();
	_ui.initEditorOpt();

	_ui.layout.init(_opts);
	_ui.extension = VARSQL.vender[_opts.dbtype] ||{};
}

var _ui = {
	base :{
		mimetype: ''	// editor mime type
		,sqlHints: {}	// sql hints
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
	,getActiveMetaTabInfo  : function (objType){
		return _ui.dbObjectMetadata.getActiveTabId(objType);
	}
	,setMetaTabDataCache : function (objType, cacheItem){
		var metaTabId = _ui.pluginProxy.getActiveMetaTabInfo(objType);

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
_ui.addDbServiceObject = function (objectKey, objectInfo){
	_$utils.copy(_ui.dbSchemaObject,objectInfo);
}

_ui.addODbServiceObjectMetadata = function (objectKey, objectInfo){
	_$utils.copy(_ui.dbObjectMetadata,objectInfo);
}

_ui.initEditorOpt = function (){
	CodeMirror.keyMap.default["Shift-Tab"] = "indentLess";
	CodeMirror.keyMap.default["Tab"] = "indentMore";
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

function _getSelector(key, suffixKey){
	return _selector['plugin'][key] + (suffixKey ? (_selector['subffix'][suffixKey]||suffixKey):'' );
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
						case 'import': // 가져오기 및 내보내기

							var dimension = VARSQL.util.browserSize();
							var popt = 'width=800px,height=500px,scrollbars=1,resizable=1,status=0,toolbar=0,menubar=0,location=0';

							VARSQLUI.popup.open(VARSQL.url(VARSQL.uri.database, '/menu/fileImport?conuid='+_g_options.param.conuid), {
								name : _g_options.conuid
								,viewOption : popt
							});

							break;
						case 'export': // 가져오기 및 내보내기

							//openMenuDialog : function (title,type ,loadUrl, dialogOpt){

							_self.openMenuDialog(VARSQL.messageFormat('menu.file.export'),'fileExport',{type:VARSQL.uri.database, url:'/menu/fileExport'}, {'width':600,'height' : 400});
							break;
						case 'newwin': // 새창 보기.
							var dimension = VARSQL.util.browserSize();
							var popt = 'width='+(dimension.width)+',height='+(dimension.height)+',scrollbars=1,resizable=1,status=0,toolbar=0,menubar=0,location=0';

							VARSQLUI.popup.open(location.href, {
								name : _g_options.conuid
								,viewOption : popt
							});

							break;
						case 'close': // 닫기
							var isInIFrame = (window.location != window.parent.location);
							if(isInIFrame==true){
								parent.userMain.activeClose();
							}else {
								if(confirm(VARSQL.messageFormat('msg.close.window'))){
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
								name : 'preferencesMain'
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
						case 'layout':	//레이아웃 초기화
							if(confirm(VARSQL.messageFormat('varsql.0013'))){
								_ui.preferences.save('init', function (){
									location.href = location.href;
									return ;
								});
							}
							break;
						case 'utils':	//유틸리티
							var componentInfo;
							if(menu_mode3 =='genexceltoddl'){
								var popt = 'width=1000,height=600,scrollbars=1,resizable=1,status=0,toolbar=0,menubar=0,location=0';

								VARSQLUI.popup.open(VARSQL.getContextPathUrl('/database/utils/genExcelToDDL?conuid='+_g_options.param.conuid), {
									name : 'genexceltoddl'
									,viewOption : popt
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
									var list = resData.items;

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
	// 데이타 내보내기.
	,exportInfo :function (type){
		var _self = this;

		if(type=='spec'){
			_self.openPreferences('명세서 내보내기',VARSQL.getContextPathUrl('/database/tools/export/specMain?conuid='+_g_options.param.conuid));
		}else if(type=='ddl'){
			_self.openPreferences('DDL 내보내기',VARSQL.getContextPathUrl('/database/tools/export/ddlMain?conuid='+_g_options.param.conuid));
		}
	}
	//header 메뉴 환경설정처리.
	,openPreferences : function (title , loadUrl){
		var _self = this;

		if(_self.preferencesDialog ==''){
			_self.preferencesDialog = $('#preferencesTemplate').dialog({
				height: 450
				,width: 700
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
		}

		VARSQLUI.frame.open('#preferencesTemplate iframe', loadUrl)

		_self.preferencesDialog.dialog("open").parent().find('.ui-dialog-title').html(title);
	}
	// 테마 설정.
	,setThemeInfo : function (themeName, initFlag){

		VARSQLUI.theme(_g_options.param.conuid, themeName);

		if(initFlag !== true){
			_g_options.screenSetting.mainTheme = themeName;
			_ui.preferences.save({mainTheme : themeName});
		}
	}
}


// 환경 설정 관련
_ui.preferences= {
	save : function (prefInfo , callback){

		if(!_$userMain.isDbActive(_g_options.param.conuid)){
			return ;
		}

		if(prefInfo =='init'){
			prefInfo = {} ;
		}else{
			prefInfo = VARSQL.util.objectMerge(_g_options.screenSetting, prefInfo);
		}

		VARSQLApi.preferences.save({
			conuid : _g_options.param.conuid
			,prefKey : 'main.database.setting'
			,prefVal : JSON.stringify(prefInfo)
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
			if(_$utils.isEqComponentName(component.componentName, ALL_COMPONENT_INFO.plugin)){
				var componentInfo = component.config.componentState;

				if(componentInfo.initFlag !==true) return ;

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
	    });

		// layout ready
		varsqlLayout.on('initialised', function( contentItem ){
			var firstFlag = true;
			var layoutSaveTimer;

			varsqlLayout.on('stateChanged', function(){

				if(firstFlag){
					firstFlag = false;
					return;
				}

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
	,selectObjectMenu : ''
	,options :{
		objectTypeTabEleId : _getSelector('schemaObject', _selectorSuffix.TAB)
		,objectTypeTabContentEleId : _getSelector('schemaObject', _selectorSuffix.TAB_CONT)
	}
	,init :function (){
		var _self = this;

		var _opts = _g_options;

		if(!_g_options.dbtype) {
			VARSQLUI.alert.open('dbtype empty');
			return ;
		}

		_g_cache.initSOMetaCacheObject();

		VARSQL.util.objectMerge(_self.options, _opts);

		_self._initObjectTypeTab();
		_self.initEvt();

	}
	// init left event
	,initEvt : function (){
		var _self = this;

		var schemaObjEleId = _getSelector('schemaObject');

		$(schemaObjEleId+' .db-schema-list-btn').on('click', function (){
			var viewArea = $(this).closest('.schema-view-btn');

			if(viewArea.hasClass('open')){
				viewArea.removeClass('open');
			}else{
				viewArea.addClass('open');
			}
		})

		// db default schema click
		$(schemaObjEleId+' .default_db_view_btn').on('click', function (){
			$(schemaObjEleId+' .db-schema-item.db-schema-default').trigger('click');
		})

		// 스키마 클릭.
		$(schemaObjEleId+' .db-schema-item').on('click', function (){

			var sEle = $(this);

			if(sEle.hasClass('active')){
				return ;
			}else{
				$(schemaObjEleId+' .db-schema-item.active').removeClass('active');
				sEle.addClass('active');
			}

			varsqlLayerClear();

			_g_options.param.schema =sEle.attr('obj_nm');

			$('#varsqlSschemaName').val(_g_options.param.schema);

			_g_cache.initSOMetaCacheObject();
			_self.selectObjectMenu = '';

			$.pubTab(_self.options.objectTypeTabEleId).itemClick(0);

		});
	}
	// db object type tab
	,_initObjectTypeTab : function (){
		var _self = this;

		_self.createTemplate();

		var objectTypeTab = $.pubTab(_self.options.objectTypeTabEleId ,{
			items : _g_options.serviceObject
			,dropItemHeight : $(_self.options.objectTypeTabContentEleId).height() -10
			,titleIcon :{
				left :{
					html :  '<i class="fa fa-refresh" style="cursor:pointer;"></i>'
					,visible : false
					,overview : false
					,click : function (item, idx){
						if(confirm(VARSQL.messageFormat('msg.refresh'))){
							_self.getObjectTypeData(item, true);
						}
					}
				}
			}
			,click : function (item){
				var sObj = $(this);

				_self.selectObjectMenu = item.contentid;
				_self.getObjectTypeData(item);
			}
		});
	}
	/**
	 * init object tab content element
	 */
	,createTemplate : function (){
		var _self = this;

		var data = _g_options.serviceObject;

		var len = data.length;

		if(len < 1) return ;

		var strHtm = [];
		var item;
		for(var i=0; i < len; i++){
			item = data[i];
			var contentid = item.contentid;
			strHtm.push('<div id="'+contentid+'" class="varsql-tab-content '+(i==0?'tab-on':'')+'"></div>'); // object element
		}

		$(_self.options.objectTypeTabContentEleId).empty().html(strHtm.join(''));
	}
	// 클릭시 텝메뉴에 해당하는 메뉴 그리기
	,getObjectTypeData : function(selObj,refresh){
		var _self = this;
		var $contentId = selObj.contentid;

		var schemaObjectEleId = _getSelector('schemaObject', _selectorSuffix.TAB_CONT);

		var activeObj = $(schemaObjectEleId+'>#'+$contentId);

		if(!activeObj.hasClass('tab-on')){
			// tab 전환.
			$(schemaObjectEleId+'> .tab-on').removeClass('tab-on');
			activeObj.addClass('tab-on');

			var serviceGridObj = $.pubGrid(schemaObjectEleId+'>#'+$contentId);

			if(serviceGridObj){
				serviceGridObj.resizeDraw();
			}
		}

		_self.getObjectMetadata({'objectType':$contentId, 'visible' :true});

		var objectInitFlag = _g_cache.isSOMetaInitCache($contentId);
		if(refresh !== true && objectInitFlag){
			return ;
		}

		var callMethod = _self.getCallMethod('_'+$contentId);

		var param =_getParam({'objectType':$contentId, 'objectNames' : selObj.objectName , 'objectIdx' : selObj.objectIdx});

		if(refresh !== true){

			var cacheObj = _g_cache.getCacheSchemaObject($contentId);

			if(!VARSQL.isUndefined(cacheObj)){
				callMethod.call(_self, cacheObj, param);
				return ;
			}
		}

		_g_cache.removeSOMetaCache($contentId, selObj.objectName);

		param.refresh = refresh;

		VARSQL.req.ajax({
			loadSelector : _getSelector('schemaObject')
			,url:{type:VARSQL.uri.database, url:'/dbObjectList'}
			,data : param
			,success:function (resData){
				_g_cache.setSOMetaInitFlag($contentId, true);
				_g_cache.setCacheSchemaObject($contentId, resData); // object cache
				callMethod.call(_self, resData, param);

				if(VARSQL.isBlank(selObj.objectName)){
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

		if(dimension){
			dimension.height = dimension.height - 52; // schema area +  tab area
		}

		// tab resize
		var tabObj =$.pubTab(this.options.objectTypeTabEleId);
		if(tabObj){
			tabObj.refresh().setDropHeight(dimension.height-10);
		}

		var gridObj = $.pubGrid(this.options.objectTypeTabContentEleId+'>#'+this.selectObjectMenu);
		if(gridObj){
			gridObj.resizeDraw();
		}
	}
	// 데이타 내보내기
	,_dataExport : function (exportObj){
		_ui.SQL.exportDataDownload(exportObj);
	}
	,getCallMethod : function (methodName){
		var callMethod  =_ui.extension[methodName];

		if(VARSQL.isUndefined(callMethod)){
			callMethod = this[methodName];
		}

		return callMethod;
	}
	,getObjectMetadata : function (param,refresh){
		_ui.dbObjectMetadata.getServiceObjectMetadata(param,refresh);
	}
};

//table
_ui.addDbServiceObject('table',{
	objectGridObj : {},
	_table : function (resData, reqParam){

		var _self = this;
		try{
			var $$objectType = 'table';
			var itemArr = resData.items;

			var len = itemArr.length;

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

				_g_cache.setSOMetaCache($$objectType, tblName, 'column', {items:colList});
			})

			// 테이블 hint;
			VARSQLHints.setTableInfo(tableHint);

			if(reqParam.refresh ==true  && !VARSQL.isBlank(reqParam.objectNames)){
				_self.objectGridObj.updateRow(reqParam.objectIdx, itemArr[0], true);
				return ;
			}

			if(resData.refreshFlag===false) return ;

			var contextItems = [
				{header: "title" , "key": "contextTitle"}
				,{divider:true}
				,{key : "dataview" , "name": VARSQL.messageFormat('dataview')
					,subMenu: [
						{ key : "dataview_all","name": VARSQL.messageFormat('data') , mode: "selectStar"}
						,{ key : "dataview_count","name": VARSQL.messageFormat('count') ,mode:"selectCount"}
					]
				}
				,{key : "copy" , "name": VARSQL.messageFormat('copy')}
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
				,{key :'export', "name": VARSQL.messageFormat('export')
					,subMenu:[
						{key : "export_data","name": VARSQL.messageFormat('data.export')}
					]
				}
				,{divider:true}
				,{key : "refresh" , "name": VARSQL.messageFormat('refresh')}
			]);

			var tableObj = $.pubGrid(_self.options.objectTypeTabContentEleId+'>#'+$$objectType,{
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
				,tColItem : [
					{key :'name', label:'Table', width:200}
					,{key :'remarks', label:'설명'}
				]
				,tbodyItem :itemArr
				,bodyOptions :{
					cellDblClick : function (rowItem){
						var selKey =rowItem.keyItem.key;

						if(selKey == 'name' ){
							_ui.SQL._sqlData('select * from '+ getTableName(rowItem.item[selKey]),false);
						}
					}
					,keyNavHandler : function(moveInfo){

						if(moveInfo.key == 13){
							return false;
						}else{
							_self.getObjectMetadata({'objectType':$$objectType,'objectName':moveInfo.item.name});
						}
					}
				}
				,rowOptions :{
					click : function (rowInfo){
						var item = rowInfo.item;

		    			_self.getObjectMetadata({'objectType':$$objectType,'objectName':item.name});
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
								_ui.SQL._sqlData('select * from '+getTableName(tmpName),false);
								return ;
							}else if(key=='dataview_count'){
								_ui.SQL._sqlData('select count(1) CNT from '+getTableName(tmpName),false);
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

							var params ={
								objectType : $$objectType
								,gubunKey :key
								,objName : tmpName
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
									VARSQLUI.toast.open(VARSQL.messageFormat('varsql.0025'));
					            	return ;
					    		}

								var resultCode = result.value;
					            if(sObj.viewMode=='editor'){
					            	_ui.SQL.addSqlEditContent(resultCode, false);
					            }else{
					            	_ui.text.copy(resultCode, 'java');
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
_ui.addDbServiceObject('view',{
	_view:function (resData ,reqParam){
		var _self = this;
		try{
			var $$objectType = 'view';
			var len = resData.items?resData.items.length:0;

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

				_g_cache.setSOMetaCache($$objectType,tblName,  'column', {items:colList});

			})

			// 테이블 hint;
			VARSQLHints.setTableInfo(tableHint);

			var viewObj = $.pubGrid(_self.options.objectTypeTabContentEleId+'>#'+$$objectType,{
				asideOptions :{
					lineNumber : {enabled : true, width : 30, align: 'right'}
				}
				,tColItem : [
					{key :'name', label:'View', width:200}
					,{key :'remarks', label:'설명'}
				]
				,tbodyItem :itemArr
				,setting : {
					enabled : true
					,enableSearch : true
				}
				,bodyOptions :{
					cellDblClick : function (rowItem){
						var selKey =rowItem.keyItem.key;

						if(selKey == 'name' ){
							_ui.SQL._sqlData('select * from '+getTableName(rowItem.item[selKey]),false);
						}
					}
					,keyNavHandler : function(moveInfo){

						if(moveInfo.key == 13){
							return false;
						}else{
							_self.getObjectMetadata({'objectType':$$objectType,'objectName':moveInfo.item.name});
						}
					}
				}
				,rowOptions : {
					click : function (rowInfo){
						var item = rowInfo.item;

		    			_self.getObjectMetadata({'objectType':$$objectType,'objectName':item.name});
					}
					,contextMenu :{
						beforeSelect :function (){
							var itemObj = viewObj.getRowItemToElement($(this));
							viewObj.config.rowContext.changeHeader('contextTitle',0,itemObj.item.name);
						}
						,callback: function(key,sObj) {
							var ele = this.element, sItem = this.gridItem;
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
								,objName :  tmpName
								,item : {
									items : cacheData.items
								}
							});
						},
						items: [
							{header: "title" , "key": "contextTitle"}
							,{divider:true}
							,{key : "copy" , "name": "복사", hotkey :'Ctrl+C'}
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
_ui.addDbServiceObject('procedure',{
	_procedure:function (resData ,reqParam){
		var _self = this;
		try{
			var len = resData.items?resData.items.length:0;
    		var $$objectType = 'procedure';

			var itemArr = resData.items;

			var procedureObj = $.pubGrid(_self.options.objectTypeTabContentEleId+'>#'+$$objectType,{
				asideOptions :{
					lineNumber : {enabled : true	,width : 30, align: 'right'}
				}
				,tColItem : [
					{key :'name', label:'Procedure',width:200}
					,{key :'status', label:'상태'}
					,{key :'remarks', label:'설명'}
				]
				,tbodyItem :itemArr
				,setting : {
					enabled : true
					,enableSearch : true
				}
				,bodyOptions :{
					keyNavHandler : function(moveInfo){

						if(moveInfo.key == 13){
							return false;
						}else{
							_self.getObjectMetadata({'objectType':$$objectType,'objectName':moveInfo.item.name});
						}
					}
				}
				,rowOptions :{
					click : function (rowInfo){
						var item = rowInfo.item;

						_ui.pluginProxy.setMetaTabDataCache($$objectType, item);

		    			_self.getObjectMetadata({'objectType':$$objectType,'objectName':item.name});
					}
					,contextMenu :{
						beforeSelect :function (){
							$(this).trigger('click');
						}
						,callback: function(key,sObj) {
							var ele = this.element, sItem = this.gridItem;
							var tmpName = sItem.name;

							var cacheData = _g_cache.getSOMetaCache($$objectType,tmpName);

							if(key =='copy'){
								procedureObj.copyData();
								return ;
							}
						},
						items: [
							{key : "copy" , "name": "복사"}
						]
					}
				}
			});
 		}catch(e){
			VARSQL.log.info(e);
		}
	}
})

_ui.addDbServiceObject('function',{
	_function : function (resData ,reqParam){
		var _self = this;
		try{
			var len = resData.items?resData.items.length:0;
    		var $$objectType = 'function';

			var itemArr = resData.items;

			var gridObj = $.pubGrid(_self.options.objectTypeTabContentEleId+'>#'+$$objectType,{
				asideOptions :{
					lineNumber : {enabled : true	,width : 30	,styleCss : 'text-align:right;padding-right:3px;'}
				}
				,tColItem : [
					{key :'name', label:'Function',width:200}
					,{key :'status', label:'상태'}
					,{key :'remarks', label:'설명'}
				]
				,tbodyItem :itemArr
				,setting : {
					enabled : true
					,enableSearch : true
				}
				,bodyOptions :{
					keyNavHandler : function(moveInfo){

						if(moveInfo.key == 13){
							return false;
						}else{
							_self.getObjectMetadata({'objectType':$$objectType,'objectName':moveInfo.item.name});
						}
					}
				}
				,rowOptions :{
					click : function (rowInfo){
						var item = rowInfo.item;

						_ui.pluginProxy.setMetaTabDataCache($$objectType, item);

		    			_self.getObjectMetadata({'objectType':$$objectType,'objectName':item.name});
					}
					,contextMenu :{
						beforeSelect :function (){
							$(this).trigger('click');
						}
						,callback: function(key,sObj) {
							var ele = this.element, sItem = this.gridItem;
							var tmpName = sItem.name;

							var cacheData = _g_cache.getSOMetaCache($$objectType,tmpName);

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
 		}catch(e){
			VARSQL.log.info(e);
		}
	}
});

_ui.addDbServiceObject('index',{
	_index : function (resData ,reqParam){
		var _self = this;
		try{
			var len = resData.items?resData.items.length:0;
    		var $$objectType = 'index';

			var itemArr = resData.items;

			var indexObj = $.pubGrid(_self.options.objectTypeTabContentEleId+'>#'+$$objectType,{
				asideOptions :{
					lineNumber : {enabled : true	,width : 30	,styleCss : 'text-align:right;padding-right:3px;'}
				}
				,tColItem : [
					{key :'name', label:'Index',width:200}
					,{key :'tblName', label:'테이블명'}
					,{key :'type', label:'타입'}
					,{key :'tableSpace', label:'Tablespace'}
					,{key :'bufferPool', label:'버퍼풀'}
					,{key :'status', label:'상태'}
				]
				,tbodyItem :itemArr
				,setting : {
					enabled : true
					,enableSearch : true
				}
				,bodyOptions :{
					keyNavHandler : function(moveInfo){

						if(moveInfo.key == 13){
							return false;
						}else{
							_self.getObjectMetadata({'objectType':$$objectType,'objectName':moveInfo.item.name});
						}
					}
				}
				,rowOptions :{
					click : function (rowInfo){
						var item = rowInfo.item;

						_ui.pluginProxy.setMetaTabDataCache($$objectType, item);

		    			_self.getObjectMetadata({'objectType':$$objectType,'objectName':item.name});
					}
					,contextMenu :{
						beforeSelect :function (){
							$(this).trigger('click');
						}
						,callback: function(key,sObj) {
							var ele = this.element, sItem = this.gridItem;
							var tmpName = sItem.name;

							var cacheData = _g_cache.getSOMetaCache($$objectType,tmpName);

							if(key =='copy'){
								indexObj.copyData();
								return ;
							}
						},
						items: [
							{key : "copy" , "name": "복사"}
						]
					}
				}
			});
 		}catch(e){
			VARSQL.log.info(e);
		}
	}
})

_ui.addDbServiceObject('trigger',{
	_trigger : function (resData ,reqParam){
		var _self = this;
		try{
			var len = resData.items?resData.items.length:0;
    		var $$objectType = 'trigger';

			var itemArr = resData.items;

			var triggerGridObj = $.pubGrid(_self.options.objectTypeTabContentEleId+'>#'+$$objectType,{
				asideOptions :{
					lineNumber : {enabled : true, width : 30, styleCss : 'text-align:right;padding-right:3px;'}
				}
				,tColItem : [
					{key :'name', label:'Trigger', width:120}
					,{key :'tblName', label:'테이블명', width:100}
					,{key :'eventType', label:'타입'}
					,{key :'timing', label:'timing'}
					,{key :'status', label:'상태'}
					,{key :'created', label:'CREATED'}
				]
				,tbodyItem :itemArr
				,setting : {
					enabled : true
					,enableSearch : true
				}
				,bodyOptions :{
					keyNavHandler : function(moveInfo){

						if(moveInfo.key == 13){
							return false;
						}else{
							_self.getObjectMetadata({'objectType':$$objectType,'objectName':moveInfo.item.name});
						}
					}
				}
				,rowOptions :{
					click : function (rowInfo){
						var item = rowInfo.item;

						_ui.pluginProxy.setMetaTabDataCache($$objectType, item);

		    			_self.getObjectMetadata({'objectType':$$objectType,'objectName':item.name});
					}
					,contextMenu :{
						beforeSelect :function (){
							$(this).trigger('click');
						}
						,callback: function(key,sObj) {
							var ele = this.element, sItem = this.gridItem;
							var tmpName = sItem.name;

							var cacheData = _g_cache.getSOMetaCache($$objectType,tmpName);

							if(key =='copy'){
								triggerGridObj.copyData();
								return ;
							}
						},
						items: [
							{key : "copy" , "name": "복사"}
						]
					}
				}
			});
 		}catch(e){
			VARSQL.log.info(e);
		}
	}
})

_ui.addDbServiceObject('sequence',{
	_sequence : function (resData ,reqParam){
		var _self = this;
		try{
			var len = resData.items?resData.items.length:0;
			var $$objectType = 'sequence';

			var itemArr = resData.items;

			var triggerGridObj = $.pubGrid(_self.options.objectTypeTabContentEleId+'>#'+$$objectType,{
				asideOptions :{
					lineNumber : {enabled : true	,width : 30	,styleCss : 'text-align:right;padding-right:3px;'}
				}
				,tColItem : [
					{key :'name', label:'Sequence',width:200}
					,{key :'status', label:'상태'}
					,{key :'created', label:'생성일자'}
					,{key :'lastDdlTime', label:'최종수정일'}
					]
				,tbodyItem :itemArr
				,setting : {
					enabled : true
					,enableSearch : true
				}
				,bodyOptions :{
					keyNavHandler : function(moveInfo){

						if(moveInfo.key == 13){
							return false;
						}else{
							_self.getObjectMetadata({'objectType':$$objectType,'objectName':moveInfo.item.name});
						}
					}
				}
				,rowOptions :{
					click : function (rowInfo){
						var item = rowInfo.item;

						_ui.pluginProxy.setMetaTabDataCache($$objectType, item);

						_self.getObjectMetadata({'objectType':$$objectType,'objectName':item.name});
					}
					,contextMenu :{
						beforeSelect :function (){
							$(this).trigger('click');
						}
						,callback: function(key,sObj) {
							var ele = this.element, sItem = this.gridItem;
							var tmpName = sItem.name;

							var cacheData = _g_cache.getSOMetaCache($$objectType,tmpName);

							if(key =='copy'){
								triggerGridObj.copyData();
								return ;
							}
						},
						items: [
							{key : "copy" , "name": "복사"}
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
		contEleId : _getSelector('objectMeta', _selectorSuffix.CONT)
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
	,getServiceObjectMetadata : function(param,refresh){

		var _self = this
			,objType = param.objectType
			,objName = param.objectName;

		var selObjectMetaEle = $(_self.getTabGroupEleId(objType));

		if(!selObjectMetaEle.hasClass('tab-on')){
			$(_self.getTabGroupEleId('tab-on')).removeClass('tab-on');
			selObjectMetaEle.addClass('tab-on');
		}

		if(_self.metaInfoLoadComplete===false){
			VARSQLUI.alert.open({key:'msg.loading'});
			return ;
		}

		if(refresh !== true && _self.selectMetadata[objType] == objName){
			return ;
		}

		_ui.layout.setActiveTab('dbMetadata');

		if(param.visible===true){	// object 선택 이 아닌  object tab 선택.
			_self.resizeMetaArea();
			return ;
		}

		_self.selectMetadata[objType] = objName||''; // 선택한 오브젝트 캐쉬

		var metaTabEleId = _self.selector.contEleId +' [data-so-meta-tab="'+objType+'"]';
		var metaTabObj = $.pubTab(metaTabEleId);

		if(VARSQL.isUndefined(metaTabObj)){
			$.pubTab(metaTabEleId, {
				items : _self.metadataTabInfo[objType]
				,click : function (item){
					var tabEle= $(this)
						,objectName = _self.selectMetadata[objType];

					var metaTabKey = item.tabid;

					var tabParam = {
						metaTabKey : metaTabKey
						, objectType : objType
						, objectName : objectName
					};

					_self.activeTabInfo[objType] = metaTabKey;

					var tabGroupContEleId = _self.getTabGroupEleId(objType);

					var sEle = $(_self.getTabContEleId(objType, metaTabKey));

					if(!sEle.hasClass('on')){
						$(tabGroupContEleId+' .on[data-so-meta-tab-content]').removeClass('on');
						sEle.addClass('on');
					}

					var cacheData;
					if(objectName !=''){
						cacheData = _g_cache.getSOMetaCache(objType, objectName, metaTabKey);
					}else{
						cacheData = {items:[]};
					}

					if('ddl' == metaTabKey){
						if(cacheData){
							_self.metadataDDLView(objType, metaTabKey, cacheData);
							return ;
						}else{
							_self._createDDL(tabParam, function (data){
								_self.metadataDDLView(objType, metaTabKey, data);
							});
						}
					}else {
						var callMethodStr = '_'+convertCamel(objType+'_'+metaTabKey);

						if(cacheData && VARSQL.isArray(cacheData.items)){
							_self[callMethodStr](cacheData, tabParam, metaTabKey, false);
							return ;
						}else{
							_self._getMetadataInfo(tabParam, function (resData, param){
								_self[callMethodStr](resData, tabParam, metaTabKey, true);
							})
						}
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

				var result = resData.items;

				if(result.length > 0){
					var callData=result;
					var objectType = param.objectType;

					if('table' == objectType || 'view' == param.objectType){
						if(result.length > 0){
							callData = result[0].colList;
						}
					}
					_g_cache.setSOMetaCache(objectType,param.objectName, param.cacheKey,{items:callData});
					callbackFn.call(_self,{items:callData}, param);
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
		if(objType == 'tab-on'){
			return this.selector.contEleId +' .tab-on[data-so-meta-tab-group]';
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

		var param =_getParam({'objectType':sObj.objectType ,objectName:sObj.objectName});

		VARSQL.req.ajax({
			url:{type:VARSQL.uri.database, url:'/createDDL'}
			,loadSelector : _self.selector.contEleId
			,data:param
			,success:function (resData){

				var item = resData.item||{};
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
	,getCallMethod : function (methodName){
		var callMethod  =_ui.extension[methodName];

		if(VARSQL.isUndefined(callMethod)){
			callMethod = this[methodName];
		}

		return callMethod;
	}
	// ddl source view
	,metadataDDLView : function (objectType, eleName, ddlSource){
		var addHtml = $('#ddlViewTemplate').html();
		var ddlEleId = this.getTabContEleId(objectType,eleName);

		var ele = $(ddlEleId);
		ele.empty().html(addHtml);

		ele.find('.prettyprint').empty().html(ddlSource).removeClass('prettyprinted');
		ele.find('textarea').val(ddlSource);
		ele.scrollTop(0);
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
		for(var i=0; i < len; i++){
			item = data[i];
			var contentid = item.contentid;

			metaStrHtm.push('<div data-so-meta-tab-group="'+contentid+'" class="varsql-tab-content '+(i==0?'tab-on':'')+'">');
			metaStrHtm.push('	<div data-so-meta-tab="'+contentid +'" class="object-meta-tab"></div>');
			metaStrHtm.push('	<div class="object-meta-tab-content">');

			var metaTabList = item.tabList;

			_self.metadataTabInfo[contentid] = metaTabList;

			for(var j =0 ;j <metaTabList.length; j++){
				var tabItem = metaTabList[j];
				metaStrHtm.push('	<div data-so-meta-tab-content="'+(contentid+'_' +tabItem.tabid)+'" class="varsql-tab-group '+(j==0?'on':'')+'"></div>');
			}
			metaStrHtm.push('	</div>');
			metaStrHtm.push('</div>	');
		}

		$(_getSelector('objectMeta', _selectorSuffix.CONT)).empty().html(metaStrHtm.join(''));
	}
	// meta 영역 resize
	,resizeMetaArea : function (dimension){

		if(dimension){
			dimension.height = dimension.height-25; // tab height
		}

		var resizeMethod = this.getCallMethod('_'+_ui.pluginProxy.getActiveObjectMenu()+'MetaResize');
		resizeMethod.call(this, dimension);
	}
}

//table tab control
_ui.addODbServiceObjectMetadata('table', {
	//테이블에 대한 메타 정보 보기 .
	_tableColumn:function (colData ,callParam, eleName, reloadFlag){
		var _self = this;

		var metaEleId = _self.getTabContEleId('table',callParam.metaTabKey);

		var items = colData.items;

		if(reloadFlag===true){ // 데이타 세로 로드시 cache에 추가.
			var colArr = [];
			$.each(items , function (i , item){
				colArr.push(item.name);
			});
			VARSQLHints.setTableColumns(callParam.objectName ,colArr);
		}

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
				{ label: '컬럼명', key: 'name',width:80 },
				{ label: '데이타타입', key: 'typeAndLength' },
				{ label: 'Key', key: 'constraints', align:'center', width:45},
				{ label: '기본값', key: 'defaultVal',width:45},
				{ label: '널여부', key: 'nullable',width:45},
				{ label: '설명', key: 'comment',width:45}
			]
			,tbodyItem :items
			,rowOptions :{
				contextMenu : {
					beforeSelect :function (){
						$(this).trigger('click');
					}
					,disableItemKey : function (items){
						if(gridObj.getSelectionItem(['name']).length < 1){
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

						var cacheData = gridObj.getSelectionItem(['name']);

						_ui.pluginProxy.createScriptSql({
							gubunKey : key
							,sqlGenType : sObj.mode
							,objectType : 'table'
							,objName :  _self.selectMetadata['table']
							,item : {
								items:cacheData
							}
							,param_yn: sObj.param_yn
						});

					},
					items: [
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
			}
		});
	}
	,_tableMetaResize : function (dimension){
		var gridObj = $.pubGrid(this.getTabContEleId('table',"column"));

		if(gridObj){
			gridObj.resizeDraw();
		}
	}
})

//view 정보 보기.
_ui.addODbServiceObjectMetadata('view', {
	// view 메타 데이타 보기.
	_viewColumn :function (colData ,callParam, eleName, reloadFlag){
		var _self = this;
		var $objType = 'view';

		var metaEleId = _self.getTabContEleId($objType,callParam.metaTabKey);

		var items = colData.items;

		if(reloadFlag===true){ // 데이타 세로 로드시 cache에 추가.
			var colArr = [];
			$.each(items , function (i , item){
				colArr.push(item.name);
			});
			VARSQLHints.setTableColumns(callParam.objectName ,colArr);
		}

		var gridObj = $.pubGrid(metaEleId);

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
			,tColItem : [
				{ label: '컬럼명', key: 'name',width:80 },
				{ label: '데이타타입', key: 'typeName' },
				{ label: '널여부', key: 'nullable',width:45},
				{ label: 'Key', key: 'constraints',width:45},
				{ label: '설명', key: 'comment',width:45}
			]
			,tbodyItem :items
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
							gubunKey : key
							,sqlGenType : sObj.mode
							,objectType : $objType
							,objName :  _self.selectMetadata[$objType]
							,item : {
								items:cacheData
							}
							,param_yn: sObj.param_yn
						});

					},
					items: [
						{key : "copy" , "name": "복사", hotkey :'Ctrl+C'}
						,{ key : "select","name": "select" ,mode:"select"}
					]
				}
			}
		});
	}
	,_viewMetaResize : function (dimension){
		var gridObj = $.pubGrid(this.getTabContEleId('view',"column"));

		if(gridObj){
			gridObj.resizeDraw();
		}
	}
});

// 프로시저 처리.
_ui.addODbServiceObjectMetadata('procedure', {
	//procedure 대한 메타 정보 보기 .
	_procedureColumn :function (colData ,callParam, eleName, reloadFlag){
		var _self = this;

 		var $objType = 'procedure';

 		var metaEleId = _self.getTabContEleId($objType,callParam.metaTabKey);

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
			,tColItem : [
				{ label: '파라미터명', key: 'name',width:80 },
				{ label: '데이타타입', key: 'dataType' },
				{ label: 'IN, OUT', key: 'columnType',width:45},
				{ label: '설명', key: 'comment',width:45},
			]
			,tbodyItem :items
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
						{key : "copy" , "name": "복사", hotkey :'Ctrl+C'}
					]
				}
			}
		});
	}
	,_procedureMetaResize : function (dimension){
		var gridObj = $.pubGrid(this.getTabContEleId('procedure',"column"));

		if(gridObj){
			gridObj.resizeDraw();
		}
	}
})

// function 정보 처리.
_ui.addODbServiceObjectMetadata('function', {
	//function 대한 메타 정보 보기 .
	_functionColumn :function (colData ,callParam, eleName, reloadFlag){
		var _self = this;

 		var $objType = 'function';

 		var metaEleId = _self.getTabContEleId($objType,callParam.metaTabKey);

		var items = colData.items;

		var gridObj = $.pubGrid(metaEleId);

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
			,tColItem : [
				{ label: '파라미터명', key: 'name',width:80 },
				{ label: '데이타타입', key: 'dataType' },
				{ label: 'IN, OUT', key: 'columnType',width:45},
				{ label: '설명', key: 'comment',width:45},
			]
			,tbodyItem :items
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
						{key : "copy" , "name": "복사", hotkey :'Ctrl+C'}
					]
				}
			}
		});
	}
	// function 대한 메타 정보 보기 .
	,_functionInfo :function (colData ,callParam, eleName, reloadFlag){
		var _self = this;

		var $objType = 'function';

		var metaEleId = _self.getTabContEleId($objType,callParam.metaTabKey);

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
						{key : "copy" , "name": "복사", hotkey :'Ctrl+C'}
					]
				}
			}
		});
	}
	,_functionMetaResize : function (dimension){
		var gridObj = $.pubGrid(this.getTabContEleId('function',"column"));

		if(gridObj){
			gridObj.resizeDraw();
		}
	}
})

// index 처리.
_ui.addODbServiceObjectMetadata('index', {
	// index column 정보.
	_indexColumn : function (colData ,callParam, eleName, reloadFlag){
		var _self = this;

 		var $objType = 'index';

 		var metaEleId = _self.getTabContEleId($objType,callParam.metaTabKey);

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
			,tColItem : [
				{ label: '컬럼명', key: 'name',width:80 },
				{ label: 'POSITION', key: 'no',width:80 },
				{ label: 'ASC OR DESC', key: 'ascOrdesc' },
			]
			,tbodyItem :items
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
						{key : "copy" , "name": "복사", hotkey :'Ctrl+C'}
					]
				}
			}
		});
	}
	,_indexMetaResize : function (dimension){
		var gridObj = $.pubGrid(this.getTabContEleId('index',"column"));

		if(gridObj){
			gridObj.resizeDraw();
		}
	}
})

// trigger 처리.
_ui.addODbServiceObjectMetadata('trigger', {
	_triggerInfo : function (colData ,callParam, eleName, reloadFlag){
		var _self = this;

 		var $objType = 'trigger';

 		var metaEleId = _self.getTabContEleId($objType,callParam.metaTabKey);

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
			,tColItem : [
				{ label: 'Name', key: 'name'},
				{ label: 'Value', key: 'val',width:80 },
			]
			,tbodyItem :items
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
						{key : "copy" , "name": "복사", hotkey :'Ctrl+C'}
					]
				}
			}
		});
	}
	,_triggerMetaResize : function (dimension){
		var gridObj = $.pubGrid(this.getTabContEleId('trigger',"column"));

		if(gridObj){
			gridObj.resizeDraw();
		}
	}
})

// sequence 처리.
_ui.addODbServiceObjectMetadata('sequence', {
	// sequence 정보보기.
	_sequenceInfo : function (colData ,callParam, eleName, reloadFlag){
		var _self = this;

 		var $objType = 'sequence';

 		var metaEleId = _self.getTabContEleId($objType,callParam.metaTabKey);

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
			,tColItem : [
				{ label: 'Name', key: 'name'},
				{ label: 'Value', key: 'val',width:80 },
			]
			,tbodyItem :items
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
						{key : "copy" , "name": "복사", hotkey :'Ctrl+C'}
					]
				}
			}
		});
	}
	,_sequenceMetaResize : function (dimension){
		var gridObj = $.pubGrid(this.getTabContEleId('sequence',"info"));

		if(gridObj){
			gridObj.resizeDraw();
		}
	}
})

/**
 * sql 데이타 그리드
 */
_ui.SQL = {
	currentSqlEditorInfo : null
	,sqlEditorBuffer : {}
	,sqlMainEditor:{}
	,sqlFileTabObj : null	// sql file tab list
	,sqlFileNameDialogEle : null // sql file name dialog
	,allTabSqlEditorObj : {}	 // sql editor  object
	,sqlEditorSelector : '#varsql_main_editor'
	,sqlParameterSelector : '#sql_parameter_area'
	,noteDialog : null
	,findTextDialog : null
	,currentSqlData :''
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

		_self._initEditor();
		_self._initEvent();
		_self._initTab();
		_self.sqlFileTabList();
		_self._initDialog();

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
			VARSQLUI.alert.open({key:'varsql.0010'});
			return ;
		}

		var sqlFileId = $('#editorSqlFileId').val();

		this.saveSqlFile({
			'sqlId' : sqlFileId
			,'sqlTitle' : nameTxt
		}, (sqlFileId =='' ? 'newfile' :'title'));

		this.sqlFileNameDialogEle.dialog( "close" );
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
	,deleteEditorInfo : function (item){
		this.sqlFileTabObj.removeItem(item);
		var sqlId = item.sqlId
		if(this.allTabSqlEditorObj[sqlId]){
			$('.sql-parameter-area[data-parameter-id="'+sqlId+'"]').remove();
			delete this.allTabSqlEditorObj[sqlId];
			delete this.sqlEditorBuffer[sqlId];
		}
		_ui.sqlDataArea.removeDataGridEle(item);
		if(this.sqlFileTabObj.getItemLength() < 1){
			this.currentSqlEditorInfo= null;

			$('[data-editor-id="empty"]').addClass('active'); // 빈파일 화면 오픈.
			this.setSqlEditorBtnDisable();
		}
	}
	,setSqlEditorBtnDisable :  function(){
		$('[data-sql-editor-menu="y"]').attr('disabled',true).addClass('disable');
		_ui.sqlDataArea.initGridSelector();
	}
	,setSqlEditorBtnEnable :  function(){
		$('[data-sql-editor-menu="y"]').removeAttr('disabled').removeClass('disable');
	}
	// init editor tab
	,_initTab : function (){
		var _self = this;

		// tab-item
		_self.sqlFileTabObj = $.pubTab('#varsqlSqlFileTab',{
			items : []
			,width:'auto'
			,itemMaxWidth: 100
			,dropItemWidth : '100px'
			,contextMenu :  {
				callback: function(key,sObj) {
					var item = sObj.tabItem;

					var param = {sqlId : item.sqlId};
					if(key =='close'){
						_self.deleteEditorInfo(item);
						param.len = _self.sqlFileTabObj.getItemLength();
						_self.saveSqlFile(param ,'delTab');
						return ;
					}else if(key =='close_other'){
						_self.sqlFileTabObj.removeItem('other', item.sqlId);
						_self.saveSqlFile(param ,'delTab-other');
					}else if(key =='close_all'){
						_self.sqlFileTabObj.removeItem('all');
						_self.saveSqlFile(param ,'delTab-all');
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
										param.sqlCont = editorObj.editor.getValue();
										_self.deleteEditorInfo(item);
										param.len =_self.sqlFileTabObj.getItemLength();
										_self.saveSqlFile(param ,'query_del');
										dialogObj.dialog( "close" );
									}
									,"Close":function (){
										_self.deleteEditorInfo(item);
										param.len =_self.sqlFileTabObj.getItemLength();
										_self.saveSqlFile(param ,'delTab');
										dialogObj.dialog( "close" );
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
							_self.deleteEditorInfo(item);
							param.len =_self.sqlFileTabObj.getItemLength();
							_self.saveSqlFile(param ,'delTab');
						}
					}
				}
			}
			,click : function (item){
				var sqlId =item.sqlId;

				_self.loadEditor(item);

			}
			,itemKey :{							// item key mapping
				title :'sqlTitle'
				,id : 'sqlId'
			}
		})
	}
	,_initEditor : function (){
		var _self = this;

		var editor= CodeMirror(document.getElementById('varsql_main_editor'), {
			mode: _ui.base.mimetype,
			indentWithTabs: true,
			smartIndent: true,
			autoCloseBrackets: true,
			indentUnit : 4,
			lineNumbers: true,
			height:'auto',
			lineWrapping: false,
			matchBrackets : true,
			autofocus: true,
			extraKeys: {
				"Ctrl-Space": "autocomplete"
				,"Ctrl-F": function (){
					// 검색 재정의
				}
				,"Shift-Ctrl-F" : function (){
					// 검색 재정의
				}
				,"Shift-Ctrl-R" : function (){
					// 검색 재정의
				}
				,"F11": function(cm) {
					_self.sqlData();
		        }
			},
			hintOptions: {tables:{}}
		});

		// 자동 저장 처리.
		var changeTimer;
		function autoSave(editerInfo){
			changeTimer = setTimeout(function() {
				editerInfo.item._isChange = false;
				_self.saveSqlFile();
			},_g_options.autoSave.delay );
		}

		editor.on("change", function (cm , changeObj){

			var currentEditorInfo = _self.currentSqlEditorInfo;

			if(currentEditorInfo.item._isChange ===false){
				currentEditorInfo.item._isChange = true;
				var activeItem = _self.sqlFileTabObj.getActive();
				_self.sqlFileTabObj.updateTitle(activeItem.item.sqlId ,'*' + activeItem.item.sqlTitle);
			}

			if(_g_options.autoSave.enabled !== false){
				clearTimeout(changeTimer);
				autoSave(currentEditorInfo);
			}
		})

		_self.sqlMainEditor = editor;
	}
	,resize : function (dimension){
		if(dimension){
			dimension.height = dimension.height-60; // editor button area  +  editor sql tab area
		}

		var _self = this;

		var editorObj = _self.getSqlEditorObj();
		if(editorObj){
			editorObj.refresh();
		}

		try{
			_self.sqlFileTabObj.refresh().setDropHeight(dimension.height-10);
		}catch(e){
			console.log('editor refresh error')
		}
	}

	//이벤트 초기화
	,_initEvent :function (){
		var _self = this;

		function strUpperCase(){
			var selArr = _self.getSqlEditorObj().getSelections();

			for(var i =0 ; i< selArr.length;i++){
				selArr[i] = toUpperCase(selArr[i]);
			}
			var selPosArr = _self.getSqlEditorObj().listSelections();
			_self.getSqlEditorObj().replaceSelections(selArr,selPosArr);
			_self.getSqlEditorObj().setSelections(selPosArr);
		}

		function strLowerCase(){
			var selArr = _self.getSqlEditorObj().getSelections();

			for(var i =0 ; i< selArr.length;i++){
				selArr[i] = toLowerCase(selArr[i]);
			}
			var selPosArr = _self.getSqlEditorObj().listSelections();
			_self.getSqlEditorObj().replaceSelections(selArr,selPosArr);
			_self.getSqlEditorObj().setSelections(selPosArr);
		}
		function strCamelCase(){
			var selArr = _self.getSqlEditorObj().getSelections();

			for(var i =0 ; i< selArr.length;i++){
				selArr[i] = convertCamel(selArr[i]);
			}
			var selPosArr = _self.getSqlEditorObj().listSelections();
			_self.getSqlEditorObj().replaceSelections(selArr,selPosArr);
			_self.getSqlEditorObj().setSelections(selPosArr);
		}

		// editor context menu
		$.pubContextMenu(_self.sqlEditorSelector, {
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
				,{key : "upperLowerCase", "name": "변환"
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
		    			$('.sql_toolbar_execute_btn').trigger('click');
		    			break;
					case 'undo':
						_self.undo();
						break;
					case 'redo':
						_self.redo();
						break;
					case 'copy':
						$('.sql_toolbar_copy_btn').trigger('click');
						break;
					case 'cut':
						$('.sql_toolbar_cut_btn').trigger('click');
						break;
					case 'paste':

						console.log('paste')
						var startCursor = _self.getSqlEditorObj().getCursor(true);
						_self.getSqlEditorObj().setCursor({line: startCursor.line, ch: startCursor.ch});
						try{
							document.execCommand('paste');
						}catch(e){
							console.log(e);
						}
						break;
					case 'delete':
						$('.sql_toolbar_delete_btn').trigger('click');
						break;
					case 'msgSend':
						$('.sql_toolbar_send_btn').trigger('click');
						break;
					case 'formatVarsql':
						_self.sqlFormatData('varsql');
						break;
					case 'formatUtil':
						$('.sql_toolbar_format_btn').trigger('click');
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
	    		_self.editorFocus();
	    	}
		});

		$(_self.sqlEditorSelector).on('keydown',function (e) {
			var evt =window.event || e;

			if(evt.ctrlKey){
				var returnFlag = true;
				if (evt.altKey) { // keyCode 78 is n
					switch (evt.keyCode) {
						case 78:
							$('.sql_toolbar_new_file').trigger('click');
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
							$('.sql_toolbar_format_btn').trigger('click');
							returnFlag = false;
							break;
						case 83: // keyCode 83 is s
							$('.sql_toolbar_allsave_btn').trigger('click');
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
							$('.sql_toolbar_save_btn').trigger('click');
							returnFlag = false;
							break;
						case 13: // keyCode 13 is Enter
							$('.sql_toolbar_execute_btn').trigger('click');
							returnFlag = false;
							break;
						case 70:
							_self.findTextOpen();
							returnFlag = false;
						default:
							break;
					}
				}
				return returnFlag;
			}
		});

		// sql 실행
		$('.sql_toolbar_execute_btn').on('click',function (evt){
			_self.sqlData();
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

		// sql 자르기
		$('.sql_toolbar_cut_btn').on('click',function (evt){
			_self.selectionTextCopy();
			$('.sql_toolbar_delete_btn').trigger('click');
		});

		// sql 복사
		$('.sql_toolbar_copy_btn').on('click',function (evt){
			_self.selectionTextCopy();
		});

		// sql 지우기
		$('.sql_toolbar_delete_btn').on('click',function (evt){
			var startCursor = _self.getSqlEditorObj().getCursor(true);
			_self.getSqlEditorObj().replaceSelection('');

			_self.editorFocus();
			_self.getSqlEditorObj().setCursor({line: startCursor.line, ch: startCursor.ch})
		});

		// 자동 줄바꿈.
		$('.sql_toolbar_linewrapper_btn').on('click',function (evt){
			var lineWrapping = _self.getSqlEditorObj().getOption('lineWrapping');

			lineWrapping = !lineWrapping;
			if(lineWrapping){
				$(this).addClass('on');
			}else{
				$(this).removeClass('on');
			}
			_self.getSqlEditorObj().setOption('lineWrapping',lineWrapping);
		});

		// 실행취소
		$('.sql_toolbar_undo_btn').on('click',function (evt){
			_self.undo();
		});

		// 다시실행.
		$('.sql_toolbar_redo_btn').on('click',function (evt){
			_self.redo();
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

		$.pubAutocomplete('#recv_user_search' , {
			minLength : 0
			,itemkey : function (item){
				return item.uname+'('+item.uid+')'
			}
			,viewAreaSelector:'#recv_autocomplete_area'
			,autoClose:false
			,useFilter :false
			,searchDelay : 100
			,autocompleteTemplate : function (baseHtml){
				return '<div class="">'+baseHtml+'</div>';
			}
			,source : function (request, response){
				var params = { searchVal : request };

				VARSQL.req.ajax({
				    url:{type:VARSQL.uri.user, url:'/searchUserList'}
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

				if($('.recv_id_item[_recvid="'+item.viewid+'"]').length > 0 ) {
					return false;
				}

				strHtm.push('<div class="recv_id_item" _recvid="'+item.viewid+'">'+item.uname+'('+item.uid+')');
				strHtm.push('<a href="javascript:;" class="pull-right">X</a></div>');
				$('#recvIdArr').append(strHtm.join(''));

				$('.recv_id_item[_recvid="'+item.viewid+'"] a').on('click', function (){
					$(this).closest('[_recvid]').remove();
				})

				return false;
			}
			,renderItem : function (matchData,item){
				return matchData;
			}
		});
	}
	,editorFocus : function (){
		this.getSqlEditorObj().focus();
	}
	// editor selection text copy
	,selectionTextCopy: function (){
		copyStringToClipboard(this.getSql(), 'varsqleditor');
	}
	,findTextOpen : function(){
		var _self = this;
		if(_self.findTextDialog==null){
			_self.findTextDialog = $('#editorFindTextDialog').dialog({
				height: 285
				,width: 280
				,resizable: false
				,modal: false
				,close: function() {
					_self.findTextDialog.dialog( "close" );
				}
			});

			$('#editorFindText').on('keydown',function(e) {
				if (e.keyCode == '13') {
					$('.find_text_btn').trigger('click');
				}
			});

			$('.find_text_btn').on('click',function (){
				var findText = $('#editorFindText').val();
				var replaceText = $('#editorReplaceText').val();
				_self.searchFindText(findText, replaceText,false);
			});
			$('.find_replace_btn').on('click',function (){
				var findText = $('#editorFindText').val();
				var replaceText = $('#editorReplaceText').val();

				_self.searchFindText(findText, replaceText ,true);
			});
			$('.find_all_replace_btn').on('click',function (){
				var findText = $('#editorFindText').val();
				var replaceText = $('#editorReplaceText').val();

				_self.searchFindText(findText, replaceText ,false, true);
			});
			$('.find_close_btn').on('click',function (){
				_self.findTextDialog.dialog( "close" );
			});
		}

		$('#editorFindText').val(_self.getSql());

		_self.findTextDialog.dialog("open");
	}
	// 검색.
	,searchFindText : function (orginTxt ,replaceTxt, replaceFlag, replaceAllFlag,wrapSearch){
		var _self = this;

		var directionValue = $("input:radio[name=find-text-direction]:checked").val();

		var findOpt={}

		$('input:checkbox[name=find-text-option]:checked').each(function() {
			findOpt[this.value] = true;
		});

		var isReverseFlag = directionValue =='down' ? false : true;

		var findPos;
		var wrapSearchPos;
		if(isReverseFlag){
			wrapSearchPos = {line: 100000, ch: 100000};
			findPos = _self.getSelectionPosition();
		}else{
			wrapSearchPos = {line: 0, ch: 0};
			findPos = _self.getSelectionPosition(true);
		}
		var schTxt = orginTxt;

		var caseSearchOpt = findOpt.caseSearch == true?'' :'i';

		if(findOpt.regularSearch===true){
			schTxt = new RegExp(schTxt,caseSearchOpt);
		}else{
			schTxt = new RegExp(schTxt.replace(/([.?*+^$[\]\\(){}|-])/g, "\\$1"),caseSearchOpt);
		}

		if(replaceAllFlag ===true){ //  모두 바꾸기
			findPos = {line: 0, ch: 0};
			isReverseFlag = false;
		}else{
			if(replaceFlag){
				if(_self.getSql().match(schTxt) != null){
					_self.getSqlEditorObj().replaceSelection(replaceTxt);
				}
			}
		}

		var cursor =_self.getSqlEditorObj().getSearchCursor(schTxt, findPos , {
			caseFold : !findOpt.caseSearch
		})

		var isNext;

		if(replaceAllFlag ===true){
			var replaceCount =0;

			while(cursor.findNext()){
				replaceCount++;
				_self.getSqlEditorObj().setSelection(cursor.from(), cursor.to());
				_self.getSqlEditorObj().replaceSelection(replaceTxt);
			}

			if(!isNext){
				VARSQLUI.alert.open({key:'varsql.0011' , count: replaceCount});
			}

			return ;
		}

		isNext = cursor.find(isReverseFlag);

		if(wrapSearch===true && isNext===false){
			VARSQLUI.alert.open({key:'varsql.0012' , findText: orginTxt});
			return ;
		}

		if(isNext){
			_self.getSqlEditorObj().setSelection(cursor.from(), cursor.to());
		}else{
			if(findOpt.wrapSearch===true){
				_self.getSqlEditorObj().setCursor(wrapSearchPos);
				_self.searchFindText(orginTxt,replaceTxt,replaceFlag, replaceAllFlag, true);
			}else{
				VARSQLUI.alert.open({key:'varsql.0012' , findText: orginTxt});
				return ;
			}
		}
	}
	// editor 에 텍스트 추가.
	,addTextToEditorArea : function(addText, addCriteria){
		var _self = this;
		var sqlEditorObj =_self.getSqlEditorObj();
		if(sqlEditorObj ==false){
			return ;
		}

		var currEditorCursor = sqlEditorObj.getCursor(true);

		if(!VARSQL.isUndefined(addCriteria)){
			if(addCriteria.type=='column'){

				var startCursor = {line:0 , ch:0};

				startCursor.line = currEditorCursor.line-10;
				startCursor.line = startCursor.line > 0 ?startCursor.line : 0;

				var chkQuery = sqlEditorObj.getRange(startCursor,currEditorCursor);

				var addTextFormat = addColumnPrefix(chkQuery);
				//addText = addColumnPrefix(chkQuery)+addText+' ';
				addText = (addTextFormat=='{key}' ? '' : ' ')+ VARSQL.messageFormat(addTextFormat ,{key:addText,val:''});
			}
		}

		addText = addText+'';

		var addLineArr = addText.split(VARSQLCont.constants.newline)
			,addLineCnt =addLineArr.length;

		sqlEditorObj.replaceSelection(addText);
		_self.editorFocus();

		if(addLineCnt > 1){
			sqlEditorObj.setCursor({line: currEditorCursor.line+addLineCnt-1, ch:addLineArr[addLineCnt-1].length})
		}else{
			sqlEditorObj.setCursor({line: currEditorCursor.line, ch: currEditorCursor.ch +addText.length})
		}
	}
	// 파라미터 html template
	,getParamTemplate : function (valFlag){
		return '<div class="sql-param-row">'
			+'	<span class="key"><input type="text" class="sql-param-key" value="'+(valFlag?'{{key}}':'')+'" /></span>'
			+'	<span class="val"><input type="text" class="sql-param-value" value="'+(valFlag?'{{val}}':'')+'"/></span>'
			+'	<span class="remove"><button type="button" class="sql-param-del-btn"><i class="fa fa-minus"></i></button></span>'
			+'</div>';
	}
	// sql 파라미터 셋팅.
	,addParamTemplate : function (mode, data){
		var _self =this;
		var currentParamEle = $('.sql-parameter-area.active');

		if('data' == mode || 'init_data' == mode){
			var paramHtm = [];

			var dataLen = Object.keys(data||{}).length;
			if(dataLen < 1) data = {'' :''};

			var template = VARSQLTemplate.compile(_self.getParamTemplate(true));

			for(var key in data){
				paramHtm.push(template({key: key , val : data[key]}));
			}

			if('init_data' ==mode){
				currentParamEle.empty().html(paramHtm.join(''));
			}else{
				currentParamEle.append(paramHtm.join(''));
			}
		}else{
			var paramHtm=_self.getParamTemplate();

			if(mode =='init'){
				currentParamEle.empty().html(paramHtm);
			}else{
				currentParamEle.append(paramHtm);
			}
		}
	}
	// save sql
	,saveSqlFile : function (item , mode){
		var _self = this;

		var params;
		mode = mode || 'query';

		if(mode=='query'){
			if(_self.getSqlEditorObj() ===false){
				return ;
			}

			params =VARSQL.util.objectMerge ({},_g_options.param,{
				'sqlCont' :_self.getSqlEditorObj().getValue()
				,'sqlId' : $('#sqlFileId').val()
				,'sqlParam' : JSON.stringify(_self.getSqlParam())
				,'mode' : mode
			});
		}else if(mode =='query_del'){
			params =VARSQL.util.objectMerge ({},_g_options.param,{
				'sqlCont' : item.sqlCont
				,'sqlId' : item.sqlId
				,'sqlParam' : JSON.stringify(_self.getSqlParam(item.sqlId))
				,'mode' : mode
			});
		}else{
			params = VARSQL.util.objectMerge ({},_g_options.param,item);
			params.mode = mode;

			if (mode=='title'){
				if(_self.sqlFileTabObj.isItem(item.sqlId)){
					_self.sqlFileTabObj.updateItem({item:{
						"sqlId":item.sqlId
			    		,"sqlTitle":item.sqlTitle
					}, enabled:false});
				};
			}else if('newfile' == mode){
				params.prevSqlId = (_self.sqlFileTabObj.getLastItem().sqlId ||'');
			}
		}

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

					_self.sqlFileTabObj.updateItem({item:{
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
				queryCont[key] = editorObj.editor.getValue();
				queryCont[key+'_param'] = JSON.stringify(_self.getSqlParam(key));
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

			    		_self.sqlFileTabObj.updateItem({item:{
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

		$('#noteTitle').val(VARSQL.util.dateFormat(new Date(), 'yyyy-mm-dd HH:MM')+'_제목');
		$('#noteContent').val(sqlVal);

		if(_self.noteDialog==null){
			_self.noteDialog = $('#noteTemplate').dialog({
				height: 350
				,width: 640
				,modal: true
				,buttons: {
					"Send":function (){
						var recvEle = $('.recv_id_item[_recvid]');

						if(recvEle.length < 1) {
							VARSQLUI.alert.open({key:'varsql.0007'});
							return ;
						}

						if(!confirm(VARSQL.messageFormat('varsql.0014'))) return ;

						var recv_id = [];
						$.each(recvEle,function (i , item ){
							recv_id.push($(item).attr('_recvid'));
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
		}

		_self.noteDialog.dialog("open");

		$('#recvIdArr').html('');

	}
	// sql file tab list
	,sqlFileTabList : function (){
		var _self = this;

		VARSQL.req.ajax({
		    loadSelector : '#sql_editor_wrapper'
		    ,url:{type:VARSQL.uri.sql, url:'/file/sqlFileTab'}
		    ,data:_g_options.param
		    ,success:function (res){
		    	var items = res.items
		    		,len = items.length;

		    	if(len > 0){
		    		_self.sqlFileTabObj.setItems(items);
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
		    		_self.loadEditor(enableItem, false);
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
		    	var items = res.items;
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

		    			if(_self.sqlFileTabObj.isItem(sqlId)){
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
		    			if(!confirm(VARSQL.messageFormat('varsql.0015', {itemText : sItem.sqlTitle}))){
		    				return ;
		    			}

		    			params['sqlId'] = sqlId;
		    			VARSQL.req.ajax({
		    				url:{type:VARSQL.uri.sql, url:'/file/delSqlSaveInfo'}
		    			    ,data:params
		    			    ,success:function (res){
		    			    	itemArea.remove();
		    			    	_self.deleteEditorInfo(sItem);
		    			    	_self.sqlFileList();
		    			    }
		    			});
		    		}
		    	})
			}
		});
	}
	,loadEditor : function (sItem, viewTabSaveFlag){
		var _self = this;

		var sqlId = sItem.sqlId;

		sItem = (_self.allTabSqlEditorObj[sqlId] ? _self.allTabSqlEditorObj[sqlId].item : sItem);

		if(VARSQL.isBlank(_self.sqlEditorBuffer[sqlId])){
			_self.sqlEditorBuffer[sqlId] = CodeMirror.Doc(VARSQL.isBlank(sItem.sqlCont)?"":sItem.sqlCont, _ui.base.mimetype);

			$(_self.sqlParameterSelector).append('<div class="sql-parameter-area" data-parameter-id="'+sqlId+'"></div>');
			_ui.sqlDataArea.addDataGridEle(sItem);
		}else{
			if(this.currentSqlEditorInfo.sqlId ==sqlId){
				return ;
			}
		}

		var buf = _self.sqlEditorBuffer[sqlId];

		if (buf.getEditor()) buf = buf.linkedDoc({sharedHist: true});
		var old = _self.sqlMainEditor.swapDoc(buf);
		var linked = old.iterLinkedDocs(function(doc) {linked = doc;});
		if (linked) {
		    // Make sure the document in buffers is the one the other view is looking at
		    for (var name in buffers) if (buffers[name] == old) buffers[name] = linked;
		    old.unlinkDoc(linked);
		}
		_self.sqlMainEditor.focus();

		$('#sqlFileId').val(sqlId);

		// parameter active
		$('.sql-parameter-area.active').removeClass('active');
		$('.sql-parameter-area[data-parameter-id="'+sqlId+'"]').addClass('active');

		var isTabItem =_self.sqlFileTabObj.isItem(sqlId);

		if(!isTabItem){
			var lastItem = _self.sqlFileTabObj.getLastItem();

			_self.sqlFileTabObj.addItem({item:{
				sqlId : sqlId
				,sqlTitle : sItem.sqlTitle
			},enabled:false});

			_self.saveSqlFile({
				sqlId : sqlId
				, prevSqlId : (lastItem.sqlId ||'')
			},'addTab');// tab 정보 추가.

			_self.setSqlEditorBtnEnable();
		}else{
			if(viewTabSaveFlag !== false){
				_self.saveSqlFile({
					sqlId : sqlId
				},'viewTab');
			}
		}

		_self.sqlFileTabObj.setActive(sItem);
		_ui.sqlDataArea.setGridSelector(sItem);

		if(buf.VARSQL_DATA_LOAD_FLAG===true){
			_self.setSelectSqlEditorInfo(sItem);
			return ;
		}

		if($('[data-editor-id="empty"]').hasClass('active')) $('[data-editor-id="empty"]').removeClass('active');

		buf.VARSQL_DATA_LOAD_FLAG = true;

		// tab item setting
		try{
			_self.addParamTemplate('init_data',$.parseJSON(sItem.sqlParam));
		}catch(e){
			_self.addParamTemplate('init_data',{'':''});
		}

		_self.allTabSqlEditorObj[sqlId].editor = buf.getEditor();
		_self.setSelectSqlEditorInfo(sItem);
	}
	// set editor
	,setSelectSqlEditorInfo : function(item){
		if(VARSQL.isUndefined(item._isChange)){
			item._isChange = false;
		}

		this.currentSqlEditorInfo = {
			sqlId : item.sqlId
			,item : item
			,editor : this.allTabSqlEditorObj[item.sqlId].editor
		};
	}
	// tab 정보 추가.
	,addTabSqlEditorInfo : function(item){
		if(VARSQL.isUndefined(this.allTabSqlEditorObj[item.sqlId])){
			this.allTabSqlEditorObj[item.sqlId] = {item : item , editor : false};
		}
	}
	//텍스트 박스 object
	,getSqlEditorObj:function(){

		if(this.currentSqlEditorInfo  && this.currentSqlEditorInfo.editor){
			return this.currentSqlEditorInfo.editor;
		}else{
			return false;
		}
	}
	,getSql: function (){
		var _self = this;
		var textObj = _self.getSqlEditorObj();

		return textObj.getSelection();
	}
	// sql 실행시 셋팅 파라미터 구하기.
	,getSqlParam : function (sqlId){
		var sqlParam ={};

		var sqlParaSelector = '.sql-parameter-area.active';

		if(!VARSQL.isUndefined(sqlId)){
			sqlParaSelector = '.sql-parameter-area[data-parameter-id="'+sqlId+'"]'
		}

		$(sqlParaSelector+' .sql-param-row').each(function(i ,item){
			var k = $(this).find('.sql-param-key').val()
				,v=$(this).find('.sql-param-value').val();

			if(VARSQL.str.trim(k) != ''){
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
				$('#sql_parameter_wrapper').addClass('on');

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
	// sql 데이타 보기
	,sqlData :function (evt){
		var _self = this;
		var sqlVal = _self.getSql();

		_self._sqlData(sqlVal,true);
	}
	,getSelectionPosition : function(endFlag){
		var std = this.getSqlEditorObj().listSelections()[0].anchor
		,end = this.getSqlEditorObj().listSelections()[0].head;

		var isChange = false;
		if(std.line > end.line){
			isChange = true;
		}else if(std.line == end.line && std.ch > end.ch){
			isChange = true;
		}

		return endFlag===true ? (isChange ? std :end ): (isChange ? end :std);
	}
	// sql 데이타 보기
	,_sqlData :function (sqlVal, paramFlag){
		var _self = this;

		sqlVal=VARSQL.str.trim(sqlVal);
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

		var params =VARSQL.util.objectMerge ({}, _g_options.param,{
			'sql' :sqlVal
			,'limit' : $(_self.options.limitCnt).val()
			,sqlParam : JSON.stringify(sqlParam)
		});

		VARSQL.req.ajax({
		    loadSelector : '#sql_editor_wrapper'
		    ,disableResultCheck : true
		    ,url:{type:VARSQL.uri.sql, url:'/base/sqlData'}
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
		var tmpEditor =_self.getSqlEditorObj();
		sqlVal=VARSQL.str.trim(sqlVal);

		var startSelection;

		if('' == sqlVal){
			startSelection = {line:0,ch:0};
			sqlVal  = tmpEditor.getValue();
		}else{
			startSelection = _self.getSelectionPosition();
		}

		if(''== sqlVal) return ;

		var params =VARSQL.util.objectMerge ({}, _g_options.param,{
			'sql' :sqlVal
		});

		params.formatType =formatType;

		VARSQL.req.ajax({
		    loadSelector : '#sql_editor_wrapper'
		    ,url:{type:VARSQL.uri.sql, url:'/base/sqlFormat'}
		    ,data:params
		    ,success:function (res){
		    	var formatSql = res.item;
		    	formatSql = VARSQL.str.trim(formatSql)

		    	var linecnt = VARSQL.matchCount(formatSql,VARSQLCont.constants.newline)+1;
	    		tmpEditor.replaceSelection(formatSql);
	    		tmpEditor.setSelection(startSelection, _self.getSqlEditorObj().getCursor(true));
			}
		});
	}
	// export data download
	,exportDataDownload : function (exportInfo){
		var key = exportInfo.downloadType
			,tmpName = exportInfo.objName
			,data = exportInfo.item;

		var items = data.items;

		var modalEle = $('#data-export-modal');

		if(modalEle.length > 0){
			modalEle.dialog( "open" );
			$('#exportFileName').val(tmpName);
			$('#exportObjectName').val(tmpName);
			$('#exportConditionQueryArea').addClass('display-off');
			$('#exportConditionQuery').val('');
			$.pubGrid('#data-export-column-list').setData(items);
			$.pubGrid('#data-export-column-list').setCheckItems('all');

			return ;
		}else{
			$(_g_options.hiddenArea).append($('#dataExportTemplate').html());
			modalEle = $('#data-export-modal');
			$('#exportFileName').val(tmpName);
			$('#exportObjectName').val(tmpName);

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
				VARSQLUI.alert.open({key:'varsql.0006'});
				return ;
			}

			if(!confirm(VARSQL.messageFormat('varsql.0008'))) return false;

			var columnNameArr = [];
			for(var i =0 ;i < chkItemLen;i++){
				var item = chkItems[i];
				columnNameArr.push(item[VARSQLCont.tableColKey.NAME]);
			}

			var params =VARSQL.util.objectMerge ({}, _g_options.param,{
				exportType : VARSQL.check.radio('input:radio[name="exportType"]')
				,columnInfo : columnNameArr.join(',')
				,objectName : $('#exportObjectName').val()
				,fileName: $('#exportFileName').val()
				,limit: $('#exportCount').val()
				,conditionQuery: (!$('#exportConditionQueryArea').hasClass('display-off') ? $('#exportConditionQuery').val():'')
			});

			VARSQL.req.download({
				type: 'post'
				,url: {type:VARSQL.uri.sql, url:'/base/dataExport'}
				,params:params
			});
		}

		modalEle.dialog({
			height: 430
			,width: 730
			,modal: true
			,show : false
			,buttons: {
				"Export & Close":function (){
					if(exportData() ===false) return ;

					$( this ).dialog( "close" );
				}
				,"Export":function (){
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

		if(VARSQL.isUndefined($.pubGrid('#data-export-column-list'))){
			$.pubGrid('#data-export-column-list',{
				autoResize :false
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
					{key:VARSQLCont.tableColKey.NAME ,label :'Column'}
					,{key:VARSQLCont.tableColKey.COMMENT ,label :'Desc'}
				]
				,tbodyItem :items
			});
			$.pubGrid('#data-export-column-list').setCheckItems('all');
		}
	}
	// 스크립트 내보내기
	,addCreateScriptSql :function (scriptInfo){
		var _self = this;
		_self.addSqlEditContent(generateSQL(scriptInfo));
	}
	// 에디터 영역에 값 넣기.
	,addSqlEditContent :function (cont , suffixAddFlag){
		var _self = this;

		cont = VARSQL.str.trim(cont);

		var insVal = VARSQLCont.constants.newline+cont;

		if(suffixAddFlag !== false){
			insVal = insVal +VARSQLCont.constants.querySuffix;
		}

		insVal =insVal+VARSQLCont.constants.newline;

		var editObj =_self.getSqlEditorObj();

		if(!editObj){
			VARSQLUI.alert.open({key:'varsql.0009'});
			return ;
		}

		var insLine = editObj.lastLine()+1;

		editObj.replaceRange(insVal, CodeMirror.Pos(insLine));
		editObj.setSelection({line:insLine,ch:0}, {line:editObj.lastLine(),ch:0});
		editObj.focus();

	}
};

/**
 * sql data area
 */
_ui.sqlDataArea =  {
	_currnetQueryReusltData :{}
	,resizeDimension : {} 	// resize 수치
	,currnetDataGridSelector : false
	,currnetDataGridColumnSelector :false
	,resultMsgAreaObj:null
	,initDataGridContextFlag : false // data grid context 초기화 여부
	,options :{
		dataGridSelector:'#dataGridArea'
		,resultMsgAreaWrap:'#resultMsgAreaWrap'
		,dataGridSelectorWrap : '#dataGridAreaWrap'
		,dataGridResultTabWrap:'#data_grid_result_tab_wrap'
		,active: null
		,delay: 0
	}
	,init : function (){
		this.initGridSelector();
		this.initEvt();
	}
	,initEvt : function (){
		var _self = this;

		// log 삭제.
		$(_self.options.dataGridResultTabWrap+' .log_clear_btn').on('click',function (){
			_self.getResultMsgAreaObj().empty();
			return false;
		});

		// sql result tab click
		$(_self.options.dataGridResultTabWrap+' [tab_gubun]').on('click',function (){
			var sObj = $(this);
			var tab_gubun = sObj.attr('tab_gubun');

			if(sObj.hasClass('on')){
				return ;
			}

			$(_self.options.dataGridResultTabWrap+' [tab_gubun]').removeClass('on');
			sObj.addClass('on');

			if(tab_gubun =='msg'){
				// data grid araea
				$(_self.options.dataGridSelectorWrap +' [tab_gubun="result"]').removeClass('tab-on');
				$(_self.options.dataGridSelectorWrap +' [tab_gubun="msg"]').addClass('tab-on');
			}else{
				$(_self.options.dataGridSelectorWrap +' [tab_gubun="result"]').addClass('tab-on');
				$(_self.options.dataGridSelectorWrap +' [tab_gubun="msg"]').removeClass('tab-on');

				$(_self.getSqlResultSelector('active')+' .sql-editor-result-grid.on').removeClass('on')
				$(_self.getSqlResultSelector('active',tab_gubun)).addClass('on');
			}
		});
	}
	// init grid selector
	,initGridSelector : function (){
		this.setGridSelector({sqlId : 'empty'});
	}
	// add data grid element
	,addDataGridEle : function(item){
		var addTemplateHtml = '<div class="sql-editor-result active" data-sql-result-id="'+item.sqlId+'"><div class="sql-editor-result-grid on" data-grid-type="result"></div><div class="sql-editor-result-grid" data-grid-type="columnType"></div></div>';
		$(this.options.dataGridSelector).append(addTemplateHtml);
		//this.setGridSelector(item);
	}
	//remove data grid element
	,removeDataGridEle : function(item){
		$(this.options.dataGridSelector).find(this.getSqlResultSelector(item)).remove();

		// result grid
		var sqlResultObj = $.pubGrid(this.getSqlResultSelector(item , 'result'));
		if(sqlResultObj) sqlResultObj.destroy();

		//grid column
		var sqlColmnuTypeObj = $.pubGrid(this.getSqlResultSelector(item , 'columnType'));
		if(sqlColmnuTypeObj) sqlColmnuTypeObj.destroy();

	}
	,setGridSelector :  function (item){
		// grid
		this.currnetDataGridSelector = this.getSqlResultSelector(item , 'result');
		//grid column
		this.currnetDataGridColumnSelector = this.getSqlResultSelector(item , 'columnType');

		$('.sql-editor-result[data-sql-result-id].active').removeClass('active');
		$('.sql-editor-result[data-sql-result-id="'+item.sqlId+'"]').addClass('active');

		this.resize(this.resizeDimension);
	}
	// sql result select
	,getSqlResultSelector : function (item, type){
		var sqlResultSelector;
		if(item =='active'){
			sqlResultSelector = '.sql-editor-result[data-sql-result-id].active';
		}else{
			sqlResultSelector = '.sql-editor-result[data-sql-result-id="'+item.sqlId+'"]';
		}

		if(type){
			return sqlResultSelector + ' [data-grid-type="'+type+'"]';
		}

		return sqlResultSelector;
	}
	// 결과 보기.
	,viewResult : function (resultData){
		var _self = this;

		_ui.layout.setActiveTab('sqlData');

		var msgViewFlag =false,gridViewFlag = false;

		var resultMsg = [];
		var resultCode = resultData.resultCode;
		if(resultCode == 200){
			var resData = resultData.items;
    		var resultLen = resData.length;

    		if(resultLen < 1 ){
    			resData.data = [{result:"데이타가 없습니다."}];
    			resData.column =[{label:'result',key:'result', align:'center'}];
    		}

    		var item;
    		var resultClass , tmpMsg;

			for(var i=resultLen-1; i>=0; i--){
				resultClass = 'success';
				item = resData[i];

				tmpMsg= item.resultMessage;
				if(item.resultType=='fail' || item.viewType=='msg'){
					msgViewFlag = true;

					if(item.resultType=='fail'){
    					resultClass = 'error';
					}
				}

				if(item.viewType=='grid'){
					gridViewFlag = true;
					_self._currnetQueryReusltData =item;
				}

				resultMsg.push('<div class="'+resultClass+'"><span class="log-end-time">'+milli2str(item.endtime,_defaultOptions.dateFormat)+'</span>#resultMsg#</div>'.replace('#resultMsg#' , tmpMsg));
			}
		}else{
			msgViewFlag =true;
			var errorMessage;
			var errQuery = '';
			var resItem = resultData.item || {};
			var msgItemResult = resItem.result || {};
			var errQuery = resItem.query;

			if(resultCode ==80000 || resultCode ==80001){
				errorMessage = resultData.message;
			}else{

				if(resultCode ==10002){
					_self._currnetQueryReusltData =msgItemResult;
					errorMessage = msgItemResult.resultMessage;
					// todo click 시 변경되게 수정할것.
					$(_self.options.dataGridResultTabWrap+" [tab_gubun=result]").trigger('click');
					_self.setGridData(_self._currnetQueryReusltData);
					_self.viewResultColumnType();
				}else{
					errorMessage = resultData.message;
				}
			}
			var logValEle = $('<div><div class="error"><span class="log-end-time">'+milli2str(msgItemResult.endtime,_defaultOptions.dateFormat)+'</span>#resultMsg#</div></div>'.replace('#resultMsg#' , '<span class="error-message">'+errorMessage+'</span><br/>sql line : <span class="error-line">['+resultData.customs.errorLine+']</span> query: <span class="log-query"></span>'));
			logValEle.find('.log-query').text(errQuery);

			resultMsg.push(logValEle.html());
			logValEle.empty();
			logValEle= null;

			if(_ui.SQL.getSqlEditorObj() !==false){
				var stdPos = _ui.SQL.getSelectionPosition();

				var cursor =_ui.SQL.getSqlEditorObj().getSearchCursor(errQuery, stdPos);

				if(cursor.findNext()){
					_ui.SQL.getSqlEditorObj().setSelection(cursor.from(), cursor.to());
				}
			}
		}

		if(msgViewFlag){
			$(_self.options.dataGridResultTabWrap+" [tab_gubun=msg]").trigger('click');
		}else{
			$(_self.options.dataGridResultTabWrap+" [tab_gubun=result]").trigger('click');
		}

		if(gridViewFlag){
			_self.setGridData(_self._currnetQueryReusltData);
			_self.viewResultColumnType();
		}

		_self.getResultMsgAreaObj().prepend(resultMsg.join(''));
		_self.getResultMsgAreaObj().animate({scrollTop: 0},'fast');
	}
	// sql data grid
	,setGridData: function (pGridData){
		var _self = this;

		$.pubGrid(_self.currnetDataGridSelector,{
			setting : {
				enabled : true
				,click : false
				,enableSearch : true
				,enableColumnFix : true
			}
			,autoResize : false
			,headerOptions:{
				helpBtn:{
					enabled : true
					,title : '컬럼명 editor에 넣기'
					,click :  function (clickInfo){
						var item = clickInfo.item;
						_ui.SQL.addTextToEditorArea(item.label+' ',{type:'column' , dataType : item.type});
					}
				}
			}
			,asideOptions :{
				lineNumber : {enabled : true}
			}
			,bodyOptions :{
				cellDblClick : function (rowItem){
					_ui.SQL.addTextToEditorArea(rowItem.item[rowItem.keyItem.key]);
				}
				,valueFilter : function (headerItem, bodyItem){
					if(headerItem.dbType=='CLOB'){
						var reval = bodyItem[headerItem.key];
						return (reval||'').substring(0,2000);
					}
					return false;
				}
			}
			,navigation : {
				status :true
				,height :20
			}
			,tColItem : pGridData.column
			,tbodyItem :pGridData.data
		});

		if(_self.initDataGridContextFlag===false) { // grid context menu 처리.
			_self.initDataGridContextFlag= true;
			var dataSelectorEle =$(_self.options.dataGridSelector);
			var gridContextObj = $.pubContextMenu(_self.options.dataGridSelector, {
				items: [
					{key : "copy" , "name": "복사"}
					,{divider : true}
					,{key : "select_column_editor" , "name": "컬럼명 넣기"}
					,{key : "select_column_copy" , "name": "컬럼명 복사"}
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
					return [];
				}
				,callback: function(key,sObj) {
					if(key =='copy'){
						$.pubGrid(_self.currnetDataGridSelector).copyData();
						return ;
					}

					if(key =='select_column_editor' || key =='select_column_copy'){
						var colInfos= $.pubGrid(_self.currnetDataGridSelector).getSelectionColInfos();

						if(colInfos.length >0){
							var colLabel  = [];
							for(var i=0; i<colInfos.length; i++){
								var colInfo = colInfos[i];
								colLabel.push(colInfo.label);
							}
							var colLabelTxt = colLabel.join(', ');

							if(key =='select_column_copy'){
								copyStringToClipboard(colLabelTxt);
							}else{
								_ui.SQL.addTextToEditorArea(' '+colLabelTxt);
							}
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
	}
	// sql result column typ
	,viewResultColumnType : function (){
		var _self = this;
		var _currnetQueryReusltData = _self._currnetQueryReusltData;
		var columnTypeArr = _currnetQueryReusltData.column;
		if(_currnetQueryReusltData.viewType != 'grid'){
			columnTypeArr = [];
		}

		var gridObj = $.pubGrid(_self.currnetDataGridColumnSelector);

		if(gridObj){
			gridObj.setData(columnTypeArr,'reDraw');
			return ;
		}

		$.pubGrid(_self.currnetDataGridColumnSelector,{
			height:'auto'
			,autoResize : false
			,page :false
			,setting : {
				enabled : true
				,enableSearch : true
			}
			,asideOptions :{
				lineNumber : {enabled : true	,width : 30	,styleCss : 'text-align:right;padding-right:3px;'}
			}
			,tColItem : [
				{label: "NAME", key: "label"}
				,{label: "TYPE", key: "dbType"}
			]
			,tbodyItem :columnTypeArr
			,bodyOptions :{
				cellDblClick : function (rowItem){
					_ui.SQL.addTextToEditorArea(rowItem.item[rowItem.keyItem.key], {type:'column'});
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
						{key : "copy" , "name": "복사"}
					]
				}
			}
		});
	}
	//result message area
	,getResultMsgAreaObj:function(){
		var _self = this;

		if(_self.resultMsgAreaObj==null){
			_self.resultMsgAreaObj = $(_self.options.resultMsgAreaWrap);
		}
		return _self.resultMsgAreaObj;
	}
	,resize : function (dimension){

		if(!dimension.width){
			return ;
		}

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
			    	_self.gridObj.setData(res.items,'reDraw');
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
			this.gridObj.destroy()
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

			    	var items = res.items ||[];

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
			//$(_g_options.hiddenArea).append('<div id=\"data-copy-modal\" title="복사" style="overflow:hidden"><textarea id="data-copy-area" class="wh100"></textarea></div>');
			$(_g_options.hiddenArea).append('<div id=\"data-copy-modal\" title="복사" style="overflow:hidden" class="pretty-view-area"><pre id="data-copy-area" class="user-select-on prettyprint lang-sql" contenteditable="true"></pre><textarea id="data-orgin-area" style="display:none;"></textarea></div>');
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
	if(_g_options.schema != _g_options.param.schema){
		tblName = _g_options.param.schema+'.'+tblName;
	}

	return tblName;
}

/**
 * sql gen
 * @param scriptInfo
 * @returns
 */
function generateSQL(scriptInfo){
	var sqlGenType = scriptInfo.sqlGenType
		,tmpName = scriptInfo.objName
		,data = scriptInfo.item
		,param_yn  = scriptInfo.param_yn;

	tmpName = getTableName(tmpName);

	sqlGenType =sqlGenType.split('|');

	var key =sqlGenType[0]
		,keyMode = sqlGenType[1];

	param_yn = param_yn?param_yn:'N';

	var reval =[];

	var dataArr = data.items, tmpval , item;

	var len = dataArr.length;

	if(key=='selectStar'){ // select 모든것.
		reval.push('select * from '+tmpName);

	}else if(key=='selectCount'){// count query
		reval.push('select count(1) from '+tmpName);
	}
	else if(key=='select'){ // select 컬럼 값
		reval.push('select ');
		for(var i=0; i < len; i++){
			item = dataArr[i];
			reval.push((i==0?'':',')+item[VARSQLCont.tableColKey.NAME]);
		}

		reval.push(' from '+tmpName);

	}
	else if(key=='insert'){ // insert 문
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
	else if(key=='update'){ // update 문
		reval.push('update '+tmpName+VARSQLCont.constants.newline+' set ');

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
					reval.push(',');
				}
				reval.push(item[VARSQLCont.tableColKey.NAME]+ ' = '+ tmpval);
				firstFlag = false;
			}
		}

		if(keyStr.length > 0) reval.push(VARSQLCont.constants.newline+'where '+keyStr.join(' and '));

	}
	else if(key=='delete'){ // delete 문
		reval.push('delete from '+tmpName);

		var item;
		var keyStr = [];
		var firstFlag = true;

		for(var i=0; i < len; i++){
			item = dataArr[i];
			var constraintVal = item[VARSQLCont.tableColKey.CONSTRAINTS] ||'';
			if(constraintVal =='PK' || constraintVal.indexOf('PRIMARY') > -1 ){
				tmpval = queryParameter(param_yn, item, keyMode);

				keyStr.push(item[VARSQLCont.tableColKey.NAME]+ ' = '+ tmpval);
			}
		}

		if(keyStr.length > 0) reval.push(VARSQLCont.constants.newline+'where '+keyStr.join(' and '));

	}
	else if(key=='drop'){ // drop 문
		reval.push('drop table '+tmpName);
	}

	return reval.join('');
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

function milli2str(milliTime, format) {

	var inDate = new Date(milliTime);
    var z = {
        M: inDate.getMonth() + 1,
        d: inDate.getDate(),
        h: inDate.getHours(),
        m: inDate.getMinutes(),
        s: inDate.getSeconds()
    };
    format = format.replace(/(M+|d+|h+|m+|s+)/g, function(v) {
        return ((v.length > 1 ? "0" : "") + eval('z.' + v.slice(-1))).slice(-2)
    });

    return format.replace(/(y+)/g, function(v) {
        return inDate.getFullYear().toString().slice(-v.length)
    });
}

// 컬럼 에디터 넣을때 붙여질 문자.
function addColumnPrefix(chkVal){
	chkVal = chkVal.replace(/^\s*|\s*$/g, '');
	var reval = '{key}';

	if(chkVal==''){
		return reval;
	}

	var lastIdx = chkVal.lastIndexOf('(');
	if(lastIdx > -1){
		var tmpVal = chkVal.substring(lastIdx+1);

		if(tmpVal.replace(/\s/g,'') ==''){
			return reval;
		}
	}
	chkVal = chkVal+'\n';
	// 주석 /**/ 지우기
	chkVal = chkVal.replace(/\/\*(.|[\r\n])*?\\*\//gm,'');

	// 주석 -- 지우기
	chkVal = chkVal.replace(/--.*\n/gm,'');

	// 줄바꿈 ' ' 변경
	chkVal = chkVal.replace(/\s/g,' ');

	// '' 안에 뭍자열 지우기
	chkVal = chkVal.replace(/\'(.*?)\'/g,'');

	// "" 안에 뭍자열 지우기
	chkVal = chkVal.replace(/\"(.*?)\"/g,'');

	// 괄호 () 지우기
	chkVal = chkVal.replace(/\((.|[\r\n])*?\)/gm,'');

	var chkStr = ' ' +chkVal.toLowerCase()+' ';

	var chkReg = [',',' select ',' set ',' from ',' where ',' and ',' or '];

	var regular = /(\s(select|from|where|and|or|on|order|by|group|update|delete|truncate|drop|create)\s)/g;

	var prefixMap = {
		',' :', {key}'
		,'from' : ' where {key}={val}'
		,'and' : ' and {key}={val}'
		,'or' : ' or {key}={val}'
		,'where' : ' and {key}={val}'
		,'select' :' , {key}'
	};


	var dotCheckReg = /(\.)$/; // 마지막 문자 체크.

	for(var i =0 , len = chkReg.length;i <len;i++){
		var chkItem = chkReg[i];
		var lastIdx = chkStr.lastIndexOf(chkItem);

		if(lastIdx > -1){
			chkStr = chkStr.substring(lastIdx+ chkItem.length);

			if(dotCheckReg.test(chkStr.replace(/\s/g,''))) {
				reval = '';
				break;
			}

			if(chkStr.match(regular) ==null){
				if(chkItem ==','){
					if(chkStr.indexOf('=') > -1){
						reval = ', {key}={val}'
						break;
					}
				}

				if(chkStr.replace(/\s/g,'') !=''){
					if(chkStr.indexOf(';') < 0){
						reval = prefixMap[chkItem.replace(/\s/g,'')];
					}
				}

				break;
			}
		}
	}

	return (reval ==''?'{key}':reval);
}

}(jQuery, window, document,VARSQL));