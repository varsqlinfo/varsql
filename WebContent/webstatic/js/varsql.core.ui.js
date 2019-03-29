/**
*ytkim
*varsql ui js
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
var _g_cache_store ={}
	,_g_cache_obj_meta_store ={};
var _g_cache = {
	get :function (key){
		return _g_cache_store[key];
	}
	,set : function (key , val){
		_g_cache_store[key] = val;
	}
	// object cache check
	,isSOMetaInitCache : function (objectType){
		return (_g_cache_obj_meta_store[objectType]||{}).initFlag===false ? false :true; 
	}
	,initSOMetaCacheObject : function (){
		var _self = this;
		var serviceObject = _g_options.serviceObject;
		
		for(var i =0; i<serviceObject.length; i++){
			var serviceObj = serviceObject[i];
			var contentid =serviceObj.contentid;
			
			_g_cache_obj_meta_store[contentid] = {initFlag:false}; // 초기화 여부 
		}
	}
	//set  object initflag 
	,setSOMetaInitFlag : function (objectType){
		_g_cache_obj_meta_store[objectType].initFlag=true; 
	}
	// 메타 데이타 케쉬된값 꺼내기
	,getSOMetaCache:function (objectType, objecName, tabKey){
		tabKey =tabKey||'column';
		
		var t =_g_cache_obj_meta_store[objectType][objecName][tabKey]; 
		return t?t:null;
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

VARSQL.ui = VARSQL.ui||{};

var _ui = {};

_ui.utils = {
	copy : function( target, source ) {
		for( var key in source ) {
			target[ key ] = source[ key ];
		}
		return target;
	}
}

_ui.pluginProxy = {
	createScriptSql :function (scriptObj){
		_ui.SQL.addCreateScriptSql(scriptObj);
	}
	,createJavaProgram: function (scriptObj){
		_ui.JAVA.createJavaProgram(scriptObj);
	}
	,getActiveObjectMenu : function (){
		return _ui.dbSchemaObject.selectObjectMenu;
	}
}

//컨텍스트 메뉴 sql 생성 부분 처리 .
_ui.addDbServiceObject = function (objectKey, objectInfo){
	_ui.utils.copy(_ui.dbSchemaObject,objectInfo);
}

_ui.addODbServiceObjectMetadata = function (objectKey, objectInfo){
	_ui.utils.copy(_ui.dbObjectMetadata,objectInfo);
}

_ui.base ={
	mimetype : ''	// editor mime type
	,sqlHints :{}	// sql hints
};

_ui.extension ={
	
}

VARSQL.ui.getTheme = function (){
	return getThemeClass(_g_options.screenSetting.mainTheme); 
}

VARSQL.ui.create = function (_opts){
	
	VARSQLCont.init(_opts.dbtype , _ui.base);
	
	_g_options = VARSQL.util.objectMerge(_g_options, _opts);
	
	_ui.initContextMenu();
	_ui.headerMenu.init();
	_ui.initEditorOpt();
	
	_ui.layout.init(_opts);
	_ui.extension = VARSQL.vender[_opts.dbtype] ||{};
}

_ui.initEditorOpt = function (){
	CodeMirror.keyMap.default["Shift-Tab"] = "indentLess";
	CodeMirror.keyMap.default["Tab"] = "indentMore";
}

var _selectorType ={
	ELEMENT :'element'
	,TEMPLATE :'template'
	,PLUGIN :'plugin'
};

var _selectorSuffix ={
	TAB :'tab'
	,TAB_CONT :'tabCont'
	,CONT :'cont'
};

var _selector = {
	element :{
		'layoutBody' : '#varsqlBodyWrapper'
	}
	,template :{
		'dbObjectComponent' : '#dbObjectComponentTemplate'
		,'dbMetadataComponent' : '#dbMetadataComponentTemplate'
		,'sqlEditorComponent' : '#sqlEditorComponentTemplate'
		,'sqlDataComponent' : '#sqlDataComponentTemplate'
	}
	,plugin :{
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

function _getSelector(key,type, suffixKey){
	type = type|| 'element';
	return _selector[type][key] + (suffixKey ? (_selector['subffix'][suffixKey]||suffixKey):'' ); 
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

_ui.initEvt = function(){
	
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
		}).on('mouseenter',function (e){
			var sEle = $(this);
			var eleOpen = $('.varsql-menu>li.open'); 
			if(eleOpen.length > 0){
				if(eleOpen != sEle){
					eleOpen.removeClass('open');
				}
				sEle.closest('li').addClass('open');
			}
		})
		
		$('.header-dropdown-submenu').on('click.sub.menu', function (){
			var sEle = $(this);
			var dropDownEle = sEle.closest('.header-dropdown-submenu'); 
			if(dropDownEle.hasClass('open')){
				dropDownEle.removeClass('open');
			}else{
				$('.header-dropdown-submenu.open').removeClass('open');
				dropDownEle.addClass('open');
			}
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
						case 'newwin': // 새창 보기.
							var popt = 'width='+screen.width-40+',height='+screen.height-40+',scrollbars=1,resizable=1,status=0,toolbar=0,menubar=0,location=0'; 
							
							var winObj = window.open('', _g_options.conuid,popt);
							
							if(winObj && winObj.VARSQL){
								winObj.focus();
							}else{
								try{
									winObj.location.href = location.href;  
									winObj.focus();
								}catch(e){};
							}
							
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
							_self.openPreferences('설정',VARSQL.getContextPathUrl('/database/preferences/main.vsql?conuid='+_g_options.param.conuid));
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
							if(confirm('초기화 하시면 기본 레이아웃으로 구성되고 새로고침 됩니다.\n초기화 하시겠습니까?')){
								_ui.preferences.save({layoutConfig : ''} , function (){
									location.href = location.href; 
									return ;
								});
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
								url:{type:VARSQL.uri.database, url:'/dbInfo.vsql'}
								,data: param
								,success:function (resData){
									var item = resData.item; 
									
									var strHtm =[];
									
									strHtm.push(' '+ item.VERSIONINFO + '<br/>');
									strHtm.push('<br/><span style="font-weight:bold;">--- more information ---</span><br/>');
									
									for(var key in item){
										if(key != 'VERSIONINFO'){
											//strHtm.push(key+' : '+item[key]+ '<br/>');
											strHtm.push(item[key]+ '<br/>');
										}
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
	//header 메뉴 환경설정처리.
	,openPreferences : function (title , loadUrl){
		var _self = this; 
		
		if(_self.preferencesDialog ==''){
			_self.preferencesDialog = $('#preferencesTemplate').dialog({
				height: 420
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
			_self.openPreferences('명세서 내보내기',VARSQL.getContextPathUrl('/database/tools/export/specMain?conuid='+_g_options.param.conuid));
		}else if(type=='ddl'){
			_self.openPreferences('DDL 내보내기',VARSQL.getContextPathUrl('/database/tools/export/ddlMain?conuid='+_g_options.param.conuid));
		}
	}
	// 테마 설정.
	,setThemeInfo : function (themeName, initFlag){
		var addTheme = getThemeClass(themeName);
		$('html').removeAttr('class').addClass(addTheme);
		
		if(initFlag !== true){
			_ui.preferences.save({mainTheme : themeName});
		}
	}
}

// 환경 설정 관련
_ui.preferences= {
	save : function (prefInfo , callback){
		
		prefInfo = VARSQL.util.objectMerge(_g_options.screenSetting, prefInfo); 
		
		var param = {
			conuid : _g_options.param.conuid
			,prefKey : 'main.database.setting'
			,prefVal : JSON.stringify(prefInfo)
		}
		VARSQL.req.ajax({
			url:{type:VARSQL.uri.database, url:'/preferences/save.vsql'}
			,data: param
			,success:function (resData){

				if(VARSQL.isFunction(callback)){
					callback.call(null, resData);
					return ; 
				}
			}
		});
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
		
		var savedState = _g_options.screenSetting.layoutConfig;
								
		try{
			savedState = JSON.parse( savedState ); 
		}catch(e){
			savedState = '';
		}
			
		var varsqlLayout ={};
		if( !VARSQL.isUndefined(savedState) && '' != savedState) {
			varsqlLayout = new GoldenLayout( savedState ,$(_getSelector('layoutBody')) );
		} else {
			varsqlLayout = new GoldenLayout( config ,$(_getSelector('layoutBody')));
		}
		
		varsqlLayout.registerComponent( 'dbObjectComponent', function( container, componentState ){
		    container.getElement().html($(_getSelector('dbObjectComponent',_selectorType.TEMPLATE)).html());
		    container.$isVarComponentRemove = true;  
			
			var initResize = true; 
			container.on('resize',function() {
				if(initResize === true){
					initResize = false; 
					return ; 
				}
				
				_ui.dbSchemaObject.resizeObjectArea({width : container.width,height : container.height});
				
			});
		});

		varsqlLayout.registerComponent( 'dbMetadataComponent', function( container, componentState ){
		    container.getElement().html($(_getSelector('dbMetadataComponent',_selectorType.TEMPLATE)).html());
		    container.$isVarComponentRemove = true; 

			var initResize = true; 
			container.on('resize',function() {
				if(initResize === true){
					initResize = false; 
					return ; 
				}
				
				_ui.dbObjectMetadata.resizeMetaArea({width : container.width,height : container.height});
			})
		});

		varsqlLayout.registerComponent( 'sqlEditorComponent', function( container, componentState ){
		    container.getElement().html($(_getSelector('sqlEditorComponent',_selectorType.TEMPLATE)).html());
		    container.$isVarComponentRemove = true; 
		    
		    var initResize = true; 
			container.on('resize',function() {
				if(initResize === true){
					initResize = false; 
					return ; 
				}
				
				_ui.SQL.resize({width : container.width,height : container.height});
			});
		});

		varsqlLayout.registerComponent( 'sqlDataComponent', function( container, componentState ){

			container.getElement().html($(_getSelector('sqlDataComponent',_selectorType.TEMPLATE)).html());
			container.$isVarComponentRemove = true; 

		    var initResize = true; 
			container.on('resize',function() {
				if(initResize === true){
					initResize = false; 
					return ; 
				}
				_ui.sqlDataArea.resize({
					width : container.width , height : container.height
				});
			})
		});
		
		// plugin component reg
		varsqlLayout.registerComponent('pluginComponent', function( container, componentInfo ){
			
			var componentObj = _ui.component[componentInfo.key]; 
			container.getElement().html(componentObj.template());
			
			var initResize = true; 
			container.on('resize',function() {
				if(initResize === true){
					initResize = false; 
					return ; 
				}
				var resizeFn = componentObj.resize; 
				if(VARSQL.isFunction(resizeFn)){
					resizeFn.call(componentObj, {
						width : container.width , height : container.height
					});
				}
			})
		});
		
		var initActiveComponentTab = {};
		// component create
		varsqlLayout.on( 'componentCreated', function( component ){
			
			if(component.container.$isVarComponentRemove ===true){
				component.container.tab.closeElement.remove();
			}
			
			var componentInfo = component.config.componentState;
			componentInfo.isComponentInit = true;
			
			var componentName = component.componentName; 
			
			if(componentInfo.isDynamicAdd == true){
				delete componentInfo.isDynamicAdd;
				return ; 
			}
			
			if(component.tab.isActive){
				componentInfo.initFlag = true;
				
				if(!initActiveComponentTab[componentName]){
					initActiveComponentTab[componentName] = [];
				}
				
				initActiveComponentTab[componentName].push(componentInfo)
			}
		});
		
		// item destroy
		varsqlLayout.on('itemDestroyed', function( component ){
			if(component.componentName =='pluginComponent'){
				var componentInfo = component.config.componentState;
				var componentObj = _ui.component[componentInfo.key]; 
				var destroyFn = componentObj.destroy;
				
				if(VARSQL.isFunction(destroyFn)){
					destroyFn.call(componentObj);
				}
			}
		});
		
		varsqlLayout.on( 'stackCreated', function( stack ){
			
			var items = stack.contentItems;
			
			for(var i =0 ;i < items.length; i++){
				var item = items[i];
				item.config.componentState.initFlag = false; 
				item.config.componentState.isComponentInit = false; 
			}
			
		    stack.on( 'activeContentItemChanged', function( contentItem ){
		    	
		    	var componentName = contentItem.componentName; 
		    	var componentInfo = contentItem.config.componentState;
		    	
		    	if(componentInfo.isComponentInit ===true && componentInfo.initFlag !== true){
			    	_self.initComponent(componentName, componentInfo);
		    	}
		    });
		});
		
		varsqlLayout.init();
		
		for(var componentName in initActiveComponentTab){
			var componentInfoArr = initActiveComponentTab[componentName];
			
			for(var i =0 ;i <componentInfoArr.length; i++){
				_self.initComponent(componentName, componentInfoArr[i]);
			}
		}
		
		$(window).resize(function() {
			varsqlLayout.updateSize();
		})
		
		var layoutSaveTimer; 
		
		var firstFlag = true;
		
		var idx = 0;
		varsqlLayout.on( 'stateChanged', function(){
			
			if(firstFlag){
				firstFlag = false; 
				return ; 
			}
			clearTimeout(layoutSaveTimer);
			
			layoutSaveTimer = setTimeout(function() {
				_ui.preferences.save({layoutConfig : JSON.stringify( varsqlLayout.toConfig())});
			}, 300);
		});
		
		_self.mainObj = varsqlLayout;
	}
	,initComponent : function (componentName, componentInfo){
		var _self = this; 
		
		componentInfo.initFlag = true;
		if(componentName =='pluginComponent'){
			_self.initPluginComponent(componentInfo);
		}else if(componentName =='sqlEditorComponent'){
			_ui.SQL.init();
		}else if(componentName =='dbObjectComponent'){
			_ui.dbSchemaObject.init();
		}else if(componentName =='sqlDataComponent'){
			_ui.sqlDataArea.init();
		}else if(componentName =='dbMetadataComponent'){
			_ui.dbObjectMetadata.init();
		}
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
		
		var pluginItem = varsqlLayout.root._$getItemsByProperty('componentName','pluginComponent');
		
		var pluginLen = pluginItem.length;
		
		addItemInfo.isDynamicAdd = true;
		
		if(pluginLen > 0){
			(pluginItem[pluginLen-1].parent).addChild({
				title: addItemInfo.nm
			    ,type: 'component'
			    ,id : addItemInfo.key
			    ,componentName: 'pluginComponent'
			    ,componentState: addItemInfo
			})
		}else{
			varsqlLayout.root.contentItems[0].addChild({
				title: addItemInfo.nm
			    ,type: 'component'
			    ,id : addItemInfo.key
			    ,componentName: 'pluginComponent'
			    ,componentState: addItemInfo
			})
		}
		
		this.initPluginComponent(addItemInfo);
		
		if(pluginLen < 1){
			varsqlLayout.root.getItemsById(addItemInfo.key)[0].container.setSize(250);
		}
	}
	// plugin component 초기화
	,initPluginComponent : function (itemInfo){
		itemInfo.initFlag = true; 
		var componentObj = _ui.component[itemInfo.key]; 
		var initFn = componentObj.init;
		if(VARSQL.isFunction(initFn)){
			initFn.call(componentObj);
		}
	}
}


//추가 component 
_ui.component = {};

// plugin add
_ui.registerPlugin = function ( regItem){
	_ui.utils.copy(_ui.component,regItem);
}

// db schema object 처리.
_ui.dbSchemaObject ={
	initObjectMenu : false
	,selectObjectMenu : ''
	,options :{
		objectTypeTabEleId : _getSelector('schemaObject',_selectorType.PLUGIN, _selectorSuffix.TAB)
		,objectTypeTabContentEleId : _getSelector('schemaObject',_selectorType.PLUGIN, _selectorSuffix.TAB_CONT)
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
		
		var schemaObjEleId = _getSelector('schemaObject',_selectorType.PLUGIN);
		
		$(schemaObjEleId+' .db-schema-list-btn').on('click', function (){
			var viewArea = $(this).closest('.schema-view-btn');
			
			if(viewArea.hasClass('open')){
				viewArea.removeClass('open');
			}else{
				viewArea.addClass('open');
			}
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
			
			$('#varsql_schema_name').html(_g_options.param.schema);
			
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
						if(confirm('새로고침 하시겠습니까?')){
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
	,getObjectTypeData:function(selObj,refresh){
		var _self = this;
		var $contentId = selObj.contentid;
				
		var schemaObjectEleId = _getSelector('schemaObject',_selectorType.PLUGIN, _selectorSuffix.TAB_CONT);
		
		var activeObj = $(schemaObjectEleId+'>#'+$contentId);
		
		if(!activeObj.hasClass('tab-on')){
			// tab 전환.
			$(schemaObjectEleId+'> .tab-on').removeClass('tab-on');
			activeObj.addClass('tab-on');
			
			var serviceGridObj = $.pubGrid(schemaObjectEleId+'>#'+$contentId);
			
			if(serviceGridObj){
				serviceGridObj.resizeDraw({width:$(schemaObjectEleId).width() , height:$(schemaObjectEleId).height()});
			}
		}
		
		_self.getObjectMetadata({'objectType':$contentId, 'visible' :true});

		var objectInitFlag = _g_cache.isSOMetaInitCache($contentId);
		if(refresh !== true && objectInitFlag){
			return ; 
		}
		
		var callMethod = _self.getCallMethod('_'+$contentId);
		
		var param =_getParam({'objectType':$contentId});
		
		VARSQL.req.ajax({      
			loadSelector : _getSelector('schemaObject',_selectorType.PLUGIN)
			,url:{type:VARSQL.uri.database, url:'/dbObjectList.varsql'}
			,data : param 
			,success:function (resData){
				_g_cache.setSOMetaInitFlag($contentId, true);
				callMethod.call(_self,resData);
				_self.getObjectMetadata({'objectType':$contentId, 'initFlag' :true});
			}
		});
		
		if(_g_options.lazyload === true){
			param.custom = {allMetadata : "Y"};
			
			VARSQL.req.ajax({      
				url:{type:VARSQL.uri.database, url:'/dbObjectList.varsql'}
				,data : param 
				,success:function (resData){
					resData.refreshFlag = false; 
					callMethod.call(_self,resData);
				}
			});
		}
	}
	// resize object area
	,resizeObjectArea : function (dimension){
		
		if(dimension){
			dimension.width = dimension.width-2;
			dimension.height = dimension.height - 50; // schema area +  tab area
		}
		
		// tab resize
		var tabObj =$.pubTab(this.options.objectTypeTabEleId);
		if(tabObj){
			tabObj.refresh().setDropHeight(dimension.height-10);
		}
		
		var gridObj = $.pubGrid(this.options.objectTypeTabContentEleId+'>#'+this.selectObjectMenu);
		if(gridObj){
			gridObj.resizeDraw(dimension);
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
			
			if(resData.refreshFlag===false) return ; 
			
			var tableObj = $.pubGrid(_self.options.objectTypeTabContentEleId+'>#'+$$objectType,{
				setting : {
					enabled : true
					,click : false
					,enableSearch : true
					,enableSpeed : true
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
					{key :'name', label:'Table', width:200, sort:true}
					,{key :'remarks', label:'설명', sort:true}
				]
				,tbodyItem :itemArr
				,bodyOptions :{
					cellDblClick : function (rowItem){
						var selKey =rowItem.keyItem.key;
						
						if(selKey == 'name' ){
							_ui.SQL._sqlData('select * from '+ getTableName(rowItem.item[selKey]),false);
						}
					}
				}
				,rowOptions :{
					click : function (idx, item){
						var sObj = $(this);
						
		    			sObj.addClass('active');
		    			
		    			_self.getObjectMetadata({'objectType':$$objectType,'objectName':item.name});
					}
					,contextMenu :{
						beforeSelect :function (){
							$(this).trigger('click');
						}
						/*	//editor 없을대 처리, 경고창으로 대체 함. 
						,disableItemKey : function (items){
							if(!_ui.SQL.getSqlEditorObj()){
								return [
									{key :'sql_create' , depth :0	}
									,{key :'mybatis-sql_create' , depth :0}	
								]; 
							}
							return [];
						}
						*/
						,callback: function(key,sObj) {
							var ele = this.element, sItem = this.gridItem;
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
							
							if(key=='java_column' || key=='java_camel_case_naming'|| key=='java_json' || key =='java_valid'){
								_ui.pluginProxy.createJavaProgram(params);
								return ;
							}
							
							params.sqlGenType = sObj.mode;
							params.param_yn = sObj.param_yn;
							_ui.pluginProxy.createScriptSql(params);
						},
						items: [
							
							{key : "dataview" , "name": "데이타 보기"
								,subMenu: [
									{ key : "dataview_all","name": "데이터" , mode: "selectStar"}
									,{ key : "dataview_count","name": "카운트" ,mode:"selectCount"}
								]
							}
							,{key : "copy" , "name": "복사"}
							,{divider:true}
							,{key : "sql_create", "name": "sql생성" 
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
							,{key : "create_java","name": "java 모델생성" 
								,subMenu:[
									{key : "java_column","name": "컬럼명"}
									,{key : "java_camel_case_naming","name": "Camel case naming"}
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
							,{key :'export', "name": "내보내기" 
								,subMenu:[
									{key : "export_data","name": "데이타 내보내기"}
								]
							}
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
			
			var gridObj = $.pubGrid(_self.options.objectTypeTabContentEleId+'>#'+$$objectType,{
				asideOptions :{
					lineNumber : {enabled : true, width : 30, align: 'right'}
				}
				,tColItem : [
					{key :'name', label:'View', width:200, sort:true}
					,{key :'remarks', label:'설명', sort:true}
				]
				,tbodyItem :itemArr
				,bodyOptions :{
					cellDblClick : function (rowItem){
						var selKey =rowItem.keyItem.key;
						
						if(selKey == 'name' ){
							_ui.SQL._sqlData('select * from '+getTableName(rowItem.item[selKey]),false);
						}
					}
				}
				,rowOptions : {
					click : function (idx, item){
						var sObj = $(this);
		    			sObj.addClass('active');
		    			
		    			_self.getObjectMetadata({'objectType':$$objectType,'objectName':item.name});
					}
					,contextMenu :{
						beforeSelect :function (){
							$(this).trigger('click');
						}
						,callback: function(key,sObj) {
							var ele = this.element, sItem = this.gridItem;
							var tmpName = sItem.name;
							
							if(key =='copy'){
								gridObj.copyData();
								return ; 
							}
							
							var cacheData = _g_cache.getSOMetaCache($$objectType, tmpName);
							
							_ui.pluginProxy.createScriptSql({
								gubunKey : key
								,sqlGenType : sObj.mode
								,objectType : $$objectType
								,objName :  _self.selectMetadata[$$objectType]
								,item : {
									items : cacheData.items
								}
							});
						},
						items: [
							{key : "copy" , "name": "복사", hotkey :'Ctrl+C'}
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
			
			$.each(itemArr , function (_idx, _item){
				var _name =_item.name;
				var colList = _item.colList; 
				_g_cache.setSOMetaCache($$objectType,_name, 'column', {items:colList});
			})
			
			var procedureObj = $.pubGrid(_self.options.objectTypeTabContentEleId+'>#'+$$objectType,{
				asideOptions :{
					lineNumber : {enabled : true	,width : 30, align: 'right'}
				}
				,tColItem : [
					{key :'name', label:'Procedure',width:200, sort:true}
					,{key :'status', label:'상태'}
					,{key :'remarks', label:'설명'}
				]
				,tbodyItem :itemArr
				,rowOptions :{
					click : function (idx, item){
						var sObj = $(this);
		    			
		    			sObj.addClass('active');
		    			
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
			
			$.each(itemArr , function (_idx, _item){
				var _name =_item.name;
				var colList = _item.colList; 
				_g_cache.setSOMetaCache($$objectType,_name, 'column', {items:colList});
			})
		
			var itemArr = resData.items;
    				
			var gridObj = $.pubGrid(_self.options.objectTypeTabContentEleId+'>#'+$$objectType,{
				asideOptions :{
					lineNumber : {enabled : true	,width : 30	,styleCss : 'text-align:right;padding-right:3px;'}				
				}
				,tColItem : [
					{key :'name', label:'Function',width:200, sort:true}
					,{key :'status', label:'상태'}
					,{key :'remarks', label:'설명'}
				]
				,tbodyItem :itemArr
				,rowOptions :{
					click : function (idx, item){
						var sObj = $(this);
						
		    			sObj.addClass('active');
		    			
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
			
			$.each(itemArr , function (_idx, _item){
				var _name =_item.name;
				var colList = _item.colList; 
				_g_cache.setSOMetaCache($$objectType,_name, 'column', {items:colList});
			})
		
			var itemArr = resData.items;
    				
			var indexObj = $.pubGrid(_self.options.objectTypeTabContentEleId+'>#'+$$objectType,{
				asideOptions :{
					lineNumber : {enabled : true	,width : 30	,styleCss : 'text-align:right;padding-right:3px;'}				
				}
				,tColItem : [
					{key :'name', label:'Index',width:200, sort:true}
					,{key :'tblName', label:'테이블명', sort:true}
					,{key :'type', label:'타입',sort:true}
					,{key :'tableSpace', label:'Tablespace',sort:true}
					,{key :'bufferPool', label:'버퍼풀',sort:true}
					,{key :'status', label:'상태' ,sort:true}
				]
				,tbodyItem :itemArr
				,rowOptions :{
					click : function (idx, item){
						var sObj = $(this);
		    			sObj.addClass('active');
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
			
			$.each(itemArr , function (_idx, _item){
				var _name =_item.name;
				var colList = _item.colList; 
				_g_cache.setSOMetaCache($$objectType,_name, {items:colList});
			})
		
			var itemArr = resData.items;
			
			var triggerGridObj = $.pubGrid(_self.options.objectTypeTabContentEleId+'>#'+$$objectType,{
				asideOptions :{
					lineNumber : {enabled : true	,width : 30	,styleCss : 'text-align:right;padding-right:3px;'}				
				}
				,tColItem : [
					{key :'name', label:'Trigger',width:200, sort:true}
					,{key :'tblName', label:'테이블명'}
					,{key :'eventType', label:'타입'}
					,{key :'timing', label:'timing'}
					,{key :'status', label:'상태'}
					,{key :'created', label:'CREATED'}
				]
				,tbodyItem :itemArr
				,rowOptions :{
					click : function (idx, item){
						var sObj = $(this);
		    			
		    			sObj.addClass('active');
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
			
			$.each(itemArr , function (_idx, _item){
				var _name =_item.name;
				var colList = []; 
				
				for(var key in _item){
					colList.push({
						'name' : convertUnderscoreCase(key)
						,'val' : _item[key]
					})
				}
				
				_g_cache.setSOMetaCache($$objectType,_name, 'info', {items:colList});
			})
			
			var itemArr = resData.items;
			
			var triggerGridObj = $.pubGrid(_self.options.objectTypeTabContentEleId+'>#'+$$objectType,{
				asideOptions :{
					lineNumber : {enabled : true	,width : 30	,styleCss : 'text-align:right;padding-right:3px;'}				
				}
				,tColItem : [
					{key :'name', label:'Sequence',width:200, sort:true}
					,{key :'status', label:'상태'}
					,{key :'created', label:'생성일자'}
					,{key :'lastDdlTime', label:'최종수정일'}
					]
				,tbodyItem :itemArr
				,rowOptions :{
					click : function (idx, item){
						var sObj = $(this);
						
						sObj.addClass('active');
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
	,selector : {
		contEleId : _getSelector('objectMeta',_selectorType.PLUGIN, _selectorSuffix.CONT)
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
				copyStringToClipboard('ddlcopy' ,copyTxt);
			}else{
				_ui.SQL.addSqlEditContent(copyTxt , false);
			}
		})
	}
	// 
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
			alert('로드중입니다.');
			return ; 
		}
		
		_ui.layout.setActiveTab('dbMetadata');
		
		if(param.visible===true){
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
						
						if(cacheData && $.isArray(cacheData.items)){
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
			,url:{type:VARSQL.uri.database, url:'/dbObjectMetadataList.varsql'}
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
			url:{type:VARSQL.uri.database, url:'/createDDL.varsql'}
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
		
		$(_getSelector('objectMeta',_selectorType.PLUGIN, _selectorSuffix.CONT)).empty().html(metaStrHtm.join(''));
	}
	// meta 영역 resize
	,resizeMetaArea : function (dimension){
		
		if(dimension){
			dimension.width =dimension.width-2 
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
						if(gridObj.getSelectItem(['name']).length < 1){
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
						
						var cacheData = gridObj.getSelectItem(['name']);
						
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
			gridObj.resizeDraw(dimension);
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
			gridObj.resizeDraw(dimension);
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
	,_functionMetaResize : function (dimension){
		var gridObj = $.pubGrid(this.getTabContEleId('function',"column"));
		
		if(gridObj){
			gridObj.resizeDraw(dimension);
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
			gridObj.resizeDraw(dimension);
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
			gridObj.resizeDraw(dimension);
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
			gridObj.resizeDraw(dimension);
		}
	}
})

/**
 * sql 데이타 그리드
 */
_ui.SQL = {
	currentSqlEditorInfo : null
	,sqlFileTabObj : null	// sql file tab list
	,sqlFileNameDialogEle : null // sql file name dialog
	,allTabSqlEditorObj : {}	 // sql editor  object
	,sqlEditorSelector : '#sql_editor_area' 
	,sqlParameterSelector : '#sql_parameter_area' 
	,sqlEditorEle : null
	,memoDialog : null
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
						_self.sqlFileNameDialogEle.dialog( "close" );
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
					_self.sqlFileNameDialogEle.dialog( "close" );
					return false; 
				}
			});
		}
	}
	// file name 저장.
	,sqlFileNameSave : function (){
		var nameTxt = $('#editorSqlFileNameText').val(); 
		if($.trim(nameTxt)==''){
			VARSQLUI.alert.open('sql명을 입력해주세요.');
			return ;
		}
		
		var sqlFileId = $('#editorSqlFileId').val();
		
		this.saveSqlFile({
			'sqlId' : sqlFileId
			,'sqlTitle' : nameTxt
		}, (sqlFileId =='' ? 'newfile' :'title'))
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
		
		if(this.allTabSqlEditorObj[item.SQL_ID]){
			if(this.allTabSqlEditorObj[item.SQL_ID].editor){
				this.allTabSqlEditorObj[item.SQL_ID].editor.toTextArea();
			}
			
			$('.sql-editor-item[data-editor-id="'+item.SQL_ID+'"]').remove();
			$('.sql-parameter-area[data-parameter-id="'+item.SQL_ID+'"]').remove();
			
			delete this.allTabSqlEditorObj[item.SQL_ID];
		}
		_ui.sqlDataArea.removeDataGridEle(item);
		if(this.sqlFileTabObj.getItemLength() < 1){
			this.currentSqlEditorInfo= null; 
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
			,titleIcon :{
				right :{
					html : '<i class="fa fa-remove"></i>'
					,click : function (item, idx){
						var sqlId =item.SQL_ID; 
						
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
										param.sql = editorObj.editor.getValue();
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
									,"Cancle": function() {
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
				var sqlId =item.SQL_ID; 
				
				_self.loadEditor(item);
				
			}
			,itemKey :{							// item key mapping
				title :'GUERY_TITLE'
				,id : 'SQL_ID'
			}
		})
	}
	,_initEditor : function (){
		var _self = this;
		
		_self.sqlEditorEle = $(_self.sqlEditorSelector);
	}
	,resize : function (dimension){
		if(dimension){
			dimension.width =dimension.width-2;
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
		$.pubContextMenu(_self.sqlEditorSelector +' .sql-editor-item:not([data-editor-id="empty"])', {
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
	
		_self.sqlEditorEle.on('keydown',function (e) {
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
			_self.sqlData(evt);
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
			,itemkey : 'UID'
			,viewAreaSelector:'#recv_autocomplete_area'
			,autoClose:false
			,searchDelay : 100
			,autocompleteTemplate : function (baseHtml){
				return '<div class="">'+baseHtml+'</div>';
			}
			,source : function (request, response){
				var params = { searchVal : request };
				
				VARSQL.req.ajax({      
				    url:{type:VARSQL.uri.user, url:'/searchUserList.varsql'}
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
	}
	,editorFocus : function (){
		this.getSqlEditorObj().focus();
	}
	// editor selection text copy
	,selectionTextCopy: function (){
		copyStringToClipboard('varsqleditor',this.getSql());
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
		if(findOpt.regularSearch===true){
			schTxt = new RegExp(schTxt,'i');
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
			
			isNext = cursor.find(isReverseFlag);
			
			while(isNext){
				replaceCount++;
				_self.getSqlEditorObj().setSelection(cursor.from(), cursor.to());
				_self.getSqlEditorObj().replaceSelection(replaceTxt);
				
				isNext = cursor.find(isReverseFlag)
			}
			
			if(!isNext){
				VARSQLUI.alert.open('일치하는 내용이 '+replaceCount+'회 변경되었습니다.');
			}
			
			return ; 
		}
		
		isNext = cursor.find(isReverseFlag);
		
		if(wrapSearch===true && isNext===false){
			VARSQLUI.alert.open('다음 문자열을 찾을수 없습니다.\n'+orginTxt);
			return ;
		}
		
		if(replaceAllFlag ===true){
			findPos = {line: 0, ch: 0};
		}
		
		if(isNext){
			_self.getSqlEditorObj().setSelection(cursor.from(), cursor.to());
		}else{
			if(findOpt.wrapSearch===true){
				_self.getSqlEditorObj().setCursor(wrapSearchPos);
				_self.searchFindText(orginTxt,replaceTxt,replaceFlag, replaceAllFlag, true);
			}else{
				VARSQLUI.alert.open('다음 문자열을 찾을수 없습니다.\n'+orginTxt);
				return ; 
			}
		}
	}
	,addGridDataToEditArea : function(rowItem){
		var _self = this; 
		
		var startCursor = _self.getSqlEditorObj().getCursor(true);
		
		var cellVal = rowItem.item[rowItem.keyItem.key];
		
		cellVal = cellVal+'';
		
		var addLineArr = cellVal.split(VARSQLCont.constants.newline)
			,addLineCnt =addLineArr.length;
		
		_self.getSqlEditorObj().replaceSelection(cellVal);
		_self.editorFocus();
		
		if(addLineCnt > 1){
			_self.getSqlEditorObj().setCursor({line: startCursor.line+addLineCnt-1, ch:addLineArr[addLineCnt-1].length})
		}else{
			_self.getSqlEditorObj().setCursor({line: startCursor.line, ch: startCursor.ch +cellVal.length})
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
			
			for(var key in data){
				paramHtm.push(Mustache.render(_self.getParamTemplate(true), {key: key , val : data[key]}));
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
			params =VARSQL.util.objectMerge ({},_g_options.param,{
				'sql' :_self.getSqlEditorObj().getValue()
				,'sqlId' : $('#sqlFileId').val()
				,'sqlParam' : JSON.stringify(_self.getSqlParam())
				,'mode' : mode
			});
		}else if(mode =='query_del'){
			params =VARSQL.util.objectMerge ({},_g_options.param,{
				'sql' : item.sql
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
						"SQL_ID":item.sqlId
			    		,"GUERY_TITLE":item.sqlTitle
					}, enabled:false});
				}; 
			}else if('newfile' == mode){
				params.prevTabId = (_self.sqlFileTabObj.getLastItem().SQL_ID ||'');
			}
		}
		
		VARSQL.req.ajax({      
		    loadSelector : (mode=='query' ? '#sql_editor_wrapper' :'')
		    ,url:{type:VARSQL.uri.sql, url:'/base/saveQuery.varsql'}
		    ,data:params 
		    ,success:function (res){
		    	var item = res.item; 
		    	
		    	if(mode=='title' || 'newfile' == mode){
		    		_self.sqlFileList();
		    		if('newfile' == mode){
		    			var  newfileItem = {
		    				"SQL_ID":item.sqlId
		    				,"GUERY_TITLE":params.sqlTitle
		    				,"QUERY_CONT": ''
		    			}
		    			_self.addTabSqlEditorInfo(newfileItem);
		    			_self.loadEditor(newfileItem);
		    		}
		    	}else if(mode=='query'){
		    		var currentEditorInfo = _self.currentSqlEditorInfo;
					currentEditorInfo.item._isChange = false; 
					
					_self.sqlFileTabObj.updateItem({item:{
						"SQL_ID":currentEditorInfo.SQL_ID
			    		,"GUERY_TITLE" : currentEditorInfo.item.GUERY_TITLE
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
				loadSelector : '#sql_editor_wrapper'
			    ,url:{type:VARSQL.uri.sql, url:'/base/saveAllQuery.varsql'}
			    ,data : params
			    ,success:function (resData){
			    	
			    	for(var i = 0 ;i < sqlIdArr.length;i++){
			    		var sqlId = sqlIdArr[i]; 
			    	
			    		var editorObj = allEditorObj[sqlId];
			    		editorObj.item._isChange = false; 
			    		
			    		_self.sqlFileTabObj.updateItem({item:{
							"SQL_ID":sqlId
				    		,"GUERY_TITLE" : editorObj.item.GUERY_TITLE
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
			sqlVal=$.trim(sqlVal);
		}
		
		$('#memoTitle').val(VARSQL.util.dateFormat(new Date(), 'yyyy-mm-dd HH:MM')+'_제목');
		$('#memoContent').val(sqlVal);
		
		if(_self.memoDialog==null){
			_self.memoDialog = $('#memoTemplate').dialog({
				height: 350
				,width: 640
				,modal: true
				,buttons: {
					"Send":function (){
						var recvEle = $('.recv_id_item[_recvid]');
						
						if(recvEle.length < 1) {
							VARSQLUI.alert.open('보낼 사람을 선택하세요.');
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
						    loadSelector : '#sql_editor_wrapper'
						    ,url:{type:VARSQL.uri.user, url:'/sendSql.varsql'}
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
	// sql file tab list
	,sqlFileTabList : function (){
		var _self = this; 
		
		VARSQL.req.ajax({
		    loadSelector : '#sql_editor_wrapper'
		    ,url:{type:VARSQL.uri.sql, url:'/base/sqlFileTab.varsql'}
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
		    			
		    			if(sItem.VIEW_YN=='Y'){
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
		    loadSelector : '#sql_editor_wrapper'
		    ,url:{type:VARSQL.uri.sql, url:'/base/sqlList.varsql'}
		    ,data:params 
		    ,success:function (res){
		    	var items = res.items;
		    	var paging = res.page;
		    	var strHtm = []
		    		,len = items.length;
		    	
		    	if(items.length > 0){
		    		for(var i =0 ;i <len; i++){
		    			var item = items[i];
		    			strHtm.push('<li class="sql-flie-item-area" _idx="'+i+'"><a href="javascript:;" class="sql-flielist-item text-ellipsis" _mode="view" title="'+item.GUERY_TITLE+'">'+item.GUERY_TITLE+'</a>');
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
		    		
		    		var sqlId = sItem.SQL_ID; 
		    		
		    		if(mode=='view'){
		    			
		    			if(_self.sqlFileTabObj.isItem(sqlId)){
		    				_self.loadEditor(sItem);
		    			}else{
		    				params['sqlId'] = sqlId;
		    				VARSQL.req.ajax({
			    			    loadSelector : '#sql_editor_wrapper'
			    			    ,url:{type:VARSQL.uri.sql, url:'/base/sqlFileDetailInfo.varsql'}
			    			    ,data:params
			    			    ,success:function (res){
			    			    	_self.addTabSqlEditorInfo(res.item);
			    			    	_self.loadEditor(res.item);
			    			    }
			    			});
		    			}
		    		}else if(mode=='setting'){
		    			$('#editorSqlFileId').val(sqlId);
						$('#editorSqlFileNameText').val(sItem.GUERY_TITLE);
						
						_self.sqlFileNameDialogEle.dialog("open");
		    		}else{
		    			if(!confirm('['+sItem.GUERY_TITLE + '] 삭제하시겠습니까?')){
		    				return ; 
		    			}
		    			
		    			params['sqlId'] = sqlId;
		    			VARSQL.req.ajax({
		    				loadSelector : '#sql_editor_wrapper'
		    			    ,url:{type:VARSQL.uri.sql, url:'/base/delSqlSaveInfo.varsql'}
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
		
		var sqlId = sItem.SQL_ID;
		
		sItem = (_self.allTabSqlEditorObj[sqlId] ? _self.allTabSqlEditorObj[sqlId].item : sItem);
		var editorEle = $('.sql-editor-item[data-editor-id="'+sqlId+'"]');
		
		if(editorEle.length  < 1){
			$(_self.sqlEditorSelector).append('<div class="sql-editor-item" data-editor-id="'+sqlId+'"><textarea id="ta_'+sqlId+'" name="ta_'+sqlId+'" class="sql-editor-text"></textarea></div>');
			editorEle = $('.sql-editor-item[data-editor-id="'+sqlId+'"]');
			
			$(_self.sqlParameterSelector).append('<div class="sql-parameter-area" data-parameter-id="'+sqlId+'"></div>');
			_ui.sqlDataArea.addDataGridEle(sItem);
		}
		
		if(editorEle.hasClass('active')){
			return ; 
		}
		
		$('#sqlFileId').val(sqlId);
		// editor active
		$('.sql-editor-item.active').removeClass('active');
		editorEle.addClass('active');
		
		// parameter active
		$('.sql-parameter-area.active').removeClass('active');
		$('.sql-parameter-area[data-parameter-id="'+sqlId+'"]').addClass('active');
		
		var isTabItem =_self.sqlFileTabObj.isItem(sqlId); 
		
		if(!isTabItem){
			var lastItem = _self.sqlFileTabObj.getLastItem();
			
			_self.sqlFileTabObj.addItem({item:{
				SQL_ID : sqlId
				,GUERY_TITLE : sItem.GUERY_TITLE
			},enabled:false});
			
			_self.saveSqlFile({
				sqlId : sqlId
				, prevTabId : (lastItem.SQL_ID ||'')
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
		
		if(editorEle.attr('data-load-yn')=='Y'){
			_self.setSelectSqlEditorInfo(sItem);
			return ; 
		}
		
		editorEle.attr('data-load-yn','Y');
		
		$('#ta_'+sqlId).val(sItem.QUERY_CONT);
		
		// tab item setting
		try{
			_self.addParamTemplate('init_data',$.parseJSON(sItem.SQL_PARAM));
		}catch(e){
			_self.addParamTemplate('init_data',{'':''});
		}
		
		var tableHint = {};
		
		var editor= CodeMirror.fromTextArea(document.getElementById('ta_'+sqlId), {
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
			},
			hintOptions: {tables:tableHint}
		});
		
		editor.VARSQL ={
			SQL_ID : sqlId 
		};
		
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
				
				_self.sqlFileTabObj.updateItem({item:{
					"SQL_ID":currentEditorInfo.SQL_ID
		    		,"GUERY_TITLE" : '*' + currentEditorInfo.item.GUERY_TITLE
				}, enabled:false});
			}
			
			if(_g_options.autoSave.enabled !== false){
				clearTimeout(changeTimer);
				autoSave(currentEditorInfo);
			}
			
		})
		
		_self.allTabSqlEditorObj[sqlId].editor = editor;
		_self.setSelectSqlEditorInfo(sItem);
	}
	// set editor
	,setSelectSqlEditorInfo : function(item){
		if(VARSQL.isUndefined(item._isChange)){
			item._isChange = false; 
		}
		
		this.currentSqlEditorInfo = {
			SQL_ID : item.SQL_ID
			,item : item
			,editor : this.allTabSqlEditorObj[item.SQL_ID].editor
		};
	}
	// tab 정보 추가. 
	,addTabSqlEditorInfo : function(item){
		if(VARSQL.isUndefined(this.allTabSqlEditorObj[item.SQL_ID])){
			this.allTabSqlEditorObj[item.SQL_ID] = {item : item , editor : false};
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
		
		var params =VARSQL.util.objectMerge ({}, _g_options.param,{
			'sql' :sqlVal
			,'limit' : $(_self.options.limitCnt).val()
			,sqlParam : JSON.stringify(sqlParam)
		});
		
		VARSQL.req.ajax({      
		    loadSelector : '#sql_editor_wrapper'
		    ,url:{type:VARSQL.uri.sql, url:'/base/sqlData.varsql'}
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
		sqlVal=$.trim(sqlVal);
		
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
		    ,url:{type:VARSQL.uri.sql, url:'/base/sqlFormat.varsql'}
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
		
		var dataArr = data.items;
		var len = dataArr.length;
		
		var strHtm = [];
		
		var item;
		for(var i=0; i < len; i++){
			item = dataArr[i];
			strHtm.push('<tr class="gradeA add">	');
			strHtm.push('	<td class="text-center"><input type="checkbox" name="exportColumnCheckBox" value="'+item[VARSQLCont.tableColKey.NAME]+'" checked="check"></td>	');
			strHtm.push(' 	<td class="">'+item[VARSQLCont.tableColKey.NAME]+'</td>	');
			strHtm.push(' 	<td class="">'+(item[VARSQLCont.tableColKey.COMMENT]||'')+'</td>	');
			strHtm.push('</tr>');
		}
		
		var modalEle = $('#data-export-modal'); 
		if(modalEle.length > 0){
			$('#exportColumnInfoArea').empty().html(strHtm.join(''));
			 $("input:checkbox[name='exportColumnCheckBox'][value='all']").prop('checked',true); 
		}else{
			$(_g_options.hiddenArea).append(Mustache.render($('#dataExportTemplate').html(), {exportColumnInfo:strHtm.join('')}));
			modalEle = $('#data-export-modal');
			
			var checkAllObj = $("input:checkbox[name='exportColumnCheckBox'][value='all']").prop('checked',true); 
			checkAllObj.on('click',function (){
				VARSQL.check.allCheck($(this),"input:checkbox[name='exportColumnCheckBox']");
			});
		}
		
		modalEle.dialog({
			height: 350
			,width: 640
			,modal: true
			,buttons: {
				"Export":function (){
					if(!confirm('내보내기 하시겠습니까?')) return ; 

					var params =VARSQL.util.objectMerge ({}, _g_options.param,{
						exportType : VARSQL.check.radio('input:radio[name="exportType"]')
						,columnInfo : VARSQL.check.getCheckVal("input:checkbox[name='exportColumnCheckBox']:not([value='all'])").join(',')
						,objectName : tmpName
						,limit: $('#exportCount').val()
					});

					VARSQL.req.download({
						type: 'post'
						,url: {type:VARSQL.uri.sql, url:'/base/dataExport.varsql'}
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
			VARSQLUI.alert.open('에디터 창이 없습니다.\n새파일을 클릭후에 추가해주세요.');
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
		this.setGridSelector({SQL_ID : 'empty'});
	}
	// add data grid element
	,addDataGridEle : function(item){
		var addTemplateHtml = '<div class="sql-editor-result active" data-sql-result-id="'+item.SQL_ID+'"><div class="sql-editor-result-grid on" data-grid-type="result"></div><div class="sql-editor-result-grid" data-grid-type="columnType"></div></div>';
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
		$('.sql-editor-result[data-sql-result-id="'+item.SQL_ID+'"]').addClass('active');
		
		this.resize(this.resizeDimension);
	}
	// sql result select
	,getSqlResultSelector : function (item, type){
		var sqlResultSelector;
		if(item =='active'){
			sqlResultSelector = '.sql-editor-result[data-sql-result-id].active';
		}else{
			sqlResultSelector = '.sql-editor-result[data-sql-result-id="'+item.SQL_ID+'"]';
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
		
		if(resultData.resultCode ==500){
			
			var errQuery = resultData.item.query; 
			msgViewFlag =true;
			
			var logValEle = $('<div><div class="error"><span class="log-end-time">'+milli2str(resultData.item.result.endtime,_defaultOptions.dateFormat)+'</span>#resultMsg#</div></div>'.replace('#resultMsg#' , '<span class="error-message">'+resultData.message+'</span><br/>sql line : <span class="error-line">['+resultData.customs.errorLine+']</span> query: <span class="log-query"></span>'));
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
			
		}else{
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
				if(item.resultType=='FAIL' || item.viewType=='msg'){
					msgViewFlag = true;
					
					if(item.resultType=='FAIL'){
    					resultClass = 'error'; 
					}
				}
				
				if(item.viewType=='grid'){
					gridViewFlag = true;
					_self._currnetQueryReusltData =item;
				}
				    				
				resultMsg.push('<div class="'+resultClass+'"><span class="log-end-time">'+milli2str(item.endtime,_defaultOptions.dateFormat)+'</span>#resultMsg#</div>'.replace('#resultMsg#' , tmpMsg));
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
				,enableSpeed : true
				,enableColumnFix : true
				,callback : function (data){
					
				}
			}
			,autoResize : false
			,headerOptions:{
				view:true
				,sort : true
				,resize:{
					enabled : true
				}
			}
			,asideOptions :{
				lineNumber : {enabled : true}				
			}
			,bodyOptions :{
				cellDblClick : function (rowItem){
					_ui.SQL.addGridDataToEditArea(rowItem);
				}
				,valueFilter : function (headerItem, bodyItem){
					if(headerItem.dbType=='CLOB'){
						var reval = bodyItem[headerItem.key]; 
						return (reval||'').substring(0,2000);
					}
					return false; 
				}
			}
			,tColItem : pGridData.column
			,tbodyItem :pGridData.data
		});
				
		if(_self.initDataGridContextFlag===false) { // grid context menu 처리. 
			_self.initDataGridContextFlag= true; 
			var gridContextObj = $.pubContextMenu(_self.options.dataGridSelector, {
				items: [
					{key : "copy" , "name": "복사"}
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
				,callback: function(key,sObj) {
					if(key =='copy'){
						$.pubGrid(_self.currnetDataGridSelector).copyData();
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
							,url: {type:VARSQL.uri.sql, url:'/base/gridDownload.varsql'}
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
			,headerOptions:{
				view:true
				,sort : true
				,resize:{
					enabled : true
				}
			}
			,asideOptions :{
				lineNumber : {enabled : true	,width : 30	,styleCss : 'text-align:right;padding-right:3px;'}				
			}
			,tColItem : [
				{label: "NAME", key: "key"}
				,{label: "TYPE", key: "dbType"}
			]
			,tbodyItem :columnTypeArr
			,bodyOptions :{
				cellDblClick : function (rowItem){
					_ui.SQL.addGridDataToEditArea(rowItem);
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
		
		dimension.width =dimension.width-2
		dimension.height = dimension.height-25; // tab area height;1 

		this.resizeDimension = dimension;
		try{
			$.pubGrid(this.currnetDataGridSelector).resizeDraw(dimension);
		}catch(e){
			//console.log(e)
		}
		
		try{
			$.pubGrid(this.currnetDataGridColumnSelector).resizeDraw(dimension);
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
					{ label: '용어', key: 'WORD',width:80 },
					{ label: '영문명', key: 'WORD_EN' },
					{ label: '약어', key: 'WORD_ABBR', width:45},
					{ label: '설명', key: 'WORD_DESC',width:45},
				]
				,tbodyItem :[]
				,bodyOptions : {
					cellDblClick : function (cellInfo){
						
						var selKey =cellInfo.keyItem.key;
						
						if(selKey != 'WORD' && selKey !='WORD_DESC'){
							
							var variableText = $(_self.selector+' #glossaryConvertTxt').val();
							
							var val =cellInfo.item[selKey]; 
							
							val = val.split(' ').join('_');
							
							if($.trim(variableText)==''){
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
			
			schVal = $.trim(schVal);
			
			if(schVal.length < 1){
				return ; 
			}
			
			var params ={
				keyword : schVal
			}
			
			VARSQL.req.ajax({      
			    loadSelector : _self.selector
			    ,url:{type:VARSQL.uri.plugin, url:'/glossary/search.varsql'}
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
			dimension.height = dimension.height - $(this.selector+' .glossary-search-area-wrapper').height(); 
			this.gridObj.resizeDraw(dimension);
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
					{ label: 'SQL', key: 'LOG_SQL'},
					{ label: '시작시간', key: 'VIEW_STARTDT' },
					{ label: '종료시간', key: 'VIEW_ENDDT', width:45},
					{ label: '걸린시간', key: 'DELAY_TIME',width:45},
				]
				,tbodyItem :[]
				,bodyOptions : {
					cellDblClick : function (cellInfo){
						
						var selKey =cellInfo.keyItem.key;
						
						if(selKey == 'LOG_SQL'){
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
			
			schVal = $.trim(schVal);
			
			if(mode != 'scroll'){
				_self.pageNo = 1;
			}
			
			var params ={
				pageNo: _self.pageNo
				,countPerPage : _self.gridObj.getViewRow()
				,'searchVal':schVal
				,conuid : _g_options.param.conuid
			}
			
			VARSQL.req.ajax({      
			    loadSelector : _self.selector
			    ,url:{type:VARSQL.uri.plugin, url:'/historySearch.varsql'}
			    ,data : params 
			    ,success:function (res){
			    	
			    	var items = res.items ||[]; 
			    	
			    	var itemLen =items.length; 
			    	
			    	if(_self.pageNo ==1){
			    		_self.gridObj.setData(items);
			    	}else{
			    		_self.gridObj.addData(items);
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
			dimension.height = dimension.height - $(this.selector+' .history-search-area-wrapper').height(); 
			this.gridObj.resizeDraw(dimension);
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
		
		var strHtm = [];
		
		if(_this.modalEle === false){
			var modalEle = $('#data-copy-modal');
			//$(_g_options.hiddenArea).append('<div id=\"data-copy-modal\" title="복사" style="overflow:hidden"><textarea id="data-copy-area" class="wh100"></textarea></div>');
			$(_g_options.hiddenArea).append('<div id=\"data-copy-modal\" title="복사" style="overflow:hidden" class="pretty-view-area"><pre id="data-copy-area" class="user-select-on prettyprint lang-sql"></pre><textarea id="data-orgin-area" style="display:none;"></textarea></div>');
			modalEle = $('#data-copy-modal'); 
			
			_this.modalEle = modalEle.dialog({
				height: 350
				,width: 640
				,modal: true
				,buttons: {
					'Copy':function (){
						_this.modalEle.dialog( "close" );
					}
					,'Cancel': function() {
						_this.modalEle.dialog( "close" );
					}
				}
				,close: function() {
					_this.modalEle.dialog( "close" );
				}
			});
			
			$($('[aria-describedby="data-copy-modal"] .ui-dialog-buttonset button.ui-button')[0]).addClass('varsql-copy-btn');
			new Clipboard('.varsql-copy-btn', {
			  text: function(trigger) {
				  return $('#data-orgin-area').val();
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

_ui.JAVA = {
	createJavaProgram : function (scriptInfo){
		var _self = this;
		var key = scriptInfo.gubunKey
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
				var tmpDbType = VARSQLCont.dataType.getDataTypeInfo(item[VARSQLCont.tableColKey.TYPE_NAME])
					,tmpJavaType=tmpDbType.javaType;
				
				var tmpColumnNm,tmpMethodNm; 
				if(createType =='column'){
					tmpColumnNm = item[VARSQLCont.tableColKey.NAME];
					tmpMethodNm = capitalizeFirstLetter(tmpColumnNm);
				}else{
					tmpColumnNm = convertCamel(item[VARSQLCont.tableColKey.NAME]);
					tmpMethodNm = capitalizeFirstLetter(tmpColumnNm);
				}
				
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
				colComment = colComment !='' && colComment != null ?' //'+colComment :'';
				
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
		// java column
		else if(key=='java_column'){
			reval = javaCreate('column');
		}
		_ui.text.copy(reval , 'java');
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
			
			if(item[VARSQLCont.tableColKey.CONSTRAINTS] =='PK'){
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
			
			if(item[VARSQLCont.tableColKey.CONSTRAINTS] == 'PK'){
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

function capitalizeFirstLetter(str) {
    return toUpperCase(str.charAt(0)) + str.slice(1);
}

// theme class
function getThemeClass(themeName){
	return 'varsql-theme-'+themeName +' ' +'pub-theme-'+themeName 
}

}(jQuery, window, document,VARSQL));