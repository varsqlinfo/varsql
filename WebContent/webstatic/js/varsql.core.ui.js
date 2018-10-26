/**
*ytkim
*varsql ui js
 */
;(function($, window, document, VARSQL) {
"use strict";

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

_ui.base ={
	mimetype : ''	// editor mime type
	,sqlHints :{}	// sql hints
};

_ui.extension ={
	
}

var _defaultOptions = {
	tablesConfig :{
		configVal :{
			search :{			// 검색
				field : ''		// 검색 필드
				,val : ''		// 검색어
			}
			,speed :-1			// scroll speed
		}
	}
	,dateFormat :'yyyy-MM-dd hh:mm:ss'
}

var _g_options={
	dbtype:''
	,param:{}
	,hiddenArea : '#dbHiddenArea'
	,downloadForm : '#downloadForm'
	,_opts :{}
};

VARSQL.ui.create = function (_opts){
	
	VARSQLCont.init(_opts.dbtype , _ui.base);
	
	_g_options = VARSQL.util.objectMerge(_g_options, _opts);
	
	_g_options._opts =_opts;
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
		
		_self.initEvt();
	}
	,initEvt : function (){
		var _self = this;
		
		// header menu dropdown  init
		$('.header-menu-top-label').headerDropdown();
		$('.header-menu-top-label').on('mouseenter',function (e){
			var sEle = $(this);
			var eleOpen = $('.db-header-menu-wrapper>li.open'); 
			if(eleOpen.length > 0){
				if(eleOpen != sEle){
					eleOpen.removeClass('open');
				}
				sEle.closest('li').addClass('open');
			}
		})
		
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
						case 'newwin': // 새창 보기.
							var popt = 'width='+screen.width-40+',height='+screen.height-40+',scrollbars=1,resizable=1,status=0,toolbar=0,menubar=0,location=0'; 
							
							var winObj = window.open('', _g_options._opts.conuid,popt);
							
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
							alert('['+menu_mode2+'] 준비중입니다.');
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
							alert('['+menu_mode2+'] 준비중입니다.');
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
								url:{gubun:VARSQL.uri.database, url:'/dbInfo.vsql'}
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
			_self.openPreferences('명세서 내보내기',VARSQL.getContextPathUrl('/database/tools/export/specMain?conuid='+_g_options.param.conuid));
		}else if(type=='ddl'){
			_self.openPreferences('DDL 내보내기',VARSQL.getContextPathUrl('/database/tools/export/ddlMain?conuid='+_g_options.param.conuid));
		}
	}
}

// 환경 설정 관련
_ui.preferences= {
	save : function (prefInfo , callback){
		
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
	,contTabHeight : 28
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
		
		var savedState = _g_options._opts.screenSetting.layoutConfig;
								
		try{
			savedState = JSON.parse( savedState ); 
		}catch(e){
			savedState = '';
		}
			
		var varsqlLayout ={};
		if( !VARSQL.isUndefined(savedState) && '' != savedState) {
			varsqlLayout = new GoldenLayout( savedState ,$('#varsqlBodyWrapper') );
			//varsqlLayout = new GoldenLayout( savedState ,$('#varsqlBodyWrapper') );
		} else {
			varsqlLayout = new GoldenLayout( config ,$('#varsqlBodyWrapper'));
		}
		
		varsqlLayout.registerComponent( 'dbObjectComponent', function( container, componentState ){
		    container.getElement().html($('#dbObjectComponentTemplate').html());
		    container.$isVarComponentRemove = true;  
			
			var initResize = true; 
			container.on('resize',function() {
				if(initResize === true){
					initResize = false; 
					return ; 
				}
				
				var containerW =container.width-2
					, containerH = container.height-60; 
				
				_ui.dbSchemaObjectServiceMenu.resizeObjectArea({width : containerW,height : containerH});
				
			});
		});

		varsqlLayout.registerComponent( 'dbMetadataComponent', function( container, componentState ){
		    container.getElement().html($('#dbMetadataComponentTemplate').html());
		    container.$isVarComponentRemove = true; 

			var initResize = true; 
			container.on('resize',function() {
				if(initResize === true){
					initResize = false; 
					return ; 
				}
				
				var containerW =container.width-2 
					,containerH = container.height-_self.contTabHeight; 
				
				_ui.dbSchemaObjectServiceMenu.resizeMetaArea({width : containerW,height : containerH});
			})
		});

		varsqlLayout.registerComponent( 'sqlEditorComponent', function( container, componentState ){
		    container.getElement().html($('#sqlEditorComponentTemplate').html());
		    container.$isVarComponentRemove = true; 
		    
		    var initResize = true; 
			container.on('resize',function() {
				if(initResize === true){
					initResize = false; 
					return ; 
				}
				
				_ui.SQL.refresh();
			});
		});

		varsqlLayout.registerComponent( 'sqlDataComponent', function( container, componentState ){

			container.getElement().html($('#sqlDataComponentTemplate').html());
			container.$isVarComponentRemove = true; 

		    var initResize = true; 
			container.on('resize',function() {
				if(initResize === true){
					initResize = false; 
					return ; 
				}
				var containerW =container.width-2
					,containerH = container.height-_self.contTabHeight; 
				
				try{
					if($('#dataGridArea .pubGrid-body-container').length > 0){
						$.pubGrid(_ui.sqlDataArea.options.dataGridSelector).resizeDraw({width : containerW,height : containerH});
						
						if(typeof $.pubGrid(_ui.sqlDataArea.options.dataColumnTypeSelector)!=='undefined' && $.isFunction($.pubGrid(_ui.sqlDataArea.options.dataColumnTypeSelector).resizeDraw)){
							$.pubGrid(_ui.sqlDataArea.options.dataColumnTypeSelector).resizeDraw({width: containerW ,height : containerH});
						}
					}
				}catch(e){
					console.log(e)
				}
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
				if(componentName =='pluginComponent'){
					_self.initPluginComponent(componentInfo);
				}else if(componentName =='sqlEditorComponent'){
					componentInfo.initFlag = true; 
					_ui.SQL.init();
				}else if(componentName =='dbObjectComponent'){
					componentInfo.initFlag = true; 
					_ui.dbSchemaObject.init();
				}else if(componentName =='sqlDataComponent'){
					componentInfo.initFlag = true;
					_ui.sqlDataArea.init();
				}
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
			    	if(componentName =='pluginComponent'){
			    		_self.initPluginComponent(componentInfo);
			    	}else if(componentName =='sqlEditorComponent'){
			    		componentInfo.initFlag = true;
						_ui.SQL.init();
					}else if(componentName =='dbObjectComponent'){
						componentInfo.initFlag = true; 
						_ui.dbSchemaObject.init();
					}else if(componentName =='sqlDataComponent'){
						componentInfo.initFlag = true;
						_ui.sqlDataArea.init();
					}
		    	}
		    });
		});
		
		varsqlLayout.init();
		
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
				//console.log( JSON.stringify( varsqlLayout.toConfig() ) );
				//localStorage.setItem( 'varsqlLayoutInfo',  JSON.stringify( varsqlLayout.toConfig() ));
			}, 300);
		});
		
		_self.mainObj = varsqlLayout;
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

//glossary component
_ui.registerPlugin({
	'glossary' : {
		selector :'#glossaryPluginArea'
		,gridObj : false
		,init : function (){
			var _self = this;
			
			_self.initEvt();
			
			_self.gridObj = $.pubGrid('#glossaryResultArea', {
				headerOptions : {redraw : false}
				,asideOptions :{lineNumber : {enable : true	,width : 30}}
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
			    ,url:{gubun:VARSQL.uri.plugin, url:'/glossary/search.varsql'}
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

// history component
_ui.registerPlugin({
	'history' : {
		selector :'#historyPluginArea'
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
				headerOptions : {redraw : false}
				,asideOptions :{lineNumber : {enable : true	,width : 30}}
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
							if(_self.scrollEndFlag !==true && item.position > 80){
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
			    ,url:{gubun:VARSQL.uri.plugin, url:'/historySearch.varsql'}
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

// db schema object 처리.
_ui.dbSchemaObject ={
	initObjectMenu : false
	,options :{
		selector:'#dbSchemaObjectWrap'
		,active: null
		,db_object_list:[]
		,param:{}
	}
	,init :function (){
		var _self = this;
		
		var _opts = _g_options._opts; 
		
		if(!_opts.dbtype) {
			alert('dbtype empty');
			return ;
		}
		
		$.extend(true,_self.options, _opts);
		
		_self._grid();
		_self.initEvt();
		
	}
	// init left event 
	,initEvt : function (){
		var _self = this;
	}
	// db schema 그리기
	,_grid:function (){
		var _self = this;
		
		var data = _self.options.db_object_list;
		var len = data.length; 
	
		if(len < 1) return ; 
	
		var strHtm = [];
		var item; 
		var activeClass = '';
		
		var toUpperSchema = _g_options.schema.toUpperCase(); 
		for (var i=0; i<len ; i++ ){
			item = data[i];
			activeClass = '';
	 		if (toUpperSchema === item.toUpperCase()) {
				activeClass='active';
			}
			strHtm.push('<li><a href="javascript:;" class="db-list-group-item '+activeClass+'" obj_nm="'+item+'">'+item+'</a></li>');
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
			_self._schemaClick(this);
		});
		
		$(_self.options.selector+' .db-list-group-item.active').trigger('click');
		
	}
	// 스키마 클릭. 
	,_schemaClick:function (obj , refreshFlag){
		var _self = this;
		var tmpParam = _self.options.param;
		tmpParam.schema = $(obj).attr('obj_nm');
		
		if(_self.initObjectMenu === false){
			VARSQL.req.ajax({      
			    loadSelector : _ui.dbSchemaObjectServiceMenu.options.dbServiceMenuContentId
			    ,url:{gubun:VARSQL.uri.database, url:'/serviceMenu.varsql'}
			    ,data:tmpParam
			    ,success:function (resData){
			    	_self.initObjectMenu = true; 
			    	
			    	_self.options.objectServiceMenu = VARSQL.util.objectMerge({param:tmpParam} , {menuData: resData.items});
			    	
			    	_ui.dbSchemaObjectServiceMenu.create(_self.options.objectServiceMenu);
				}
			});
		}else{
			_ui.dbSchemaObjectServiceMenu.create(_self.options.objectServiceMenu);
		}
	}
};

/*
 * 왼족 메뉴 셋팅
 * 테이블 , 스키마 , view 등등 
 */
_ui.dbSchemaObjectServiceMenu ={
	initFlag : false
	,metadataCache : {}
	,selectObjectMenu : 'table'
	,options :{
		menuData:[]
		,param:{}
		,serviceMenuTabId:'#varsqlDbServiceMenu'
		,dbServiceMenuContentId:'#dbServiceMenuContent'
		,metadataContentAreaWrapId:'#metadataContentAreaWrap'
		,metadataTabAreaWrapId:'#metadataTabAreaWrap'
		,metadataContentAreaWrapEle:null
		,metadata_content_areaId:'#metadata_content_area'
		,metadata_content_areaIdEle:null
	}
	,_metaCacheGubun : ''
	// 왼쪽 메뉴 생성 . 
	,create: function (options){
		var _self = this; 
		
		_self.options = VARSQL.util.objectMerge(_self.options, options);
		
		_self._initCacheObject();
		_self.initElement();
		
		if(_self.initFlag ===false){
			_self._tabs();
			$.pubTab(_self.options.serviceMenuTabId).itemClick();
			_self.initEvt();
		}else{
			$.pubTab(_self.options.serviceMenuTabId).itemClick(0, {'refresh':'Y'});
		}
		_self.initFlag = true; 
	}
	,initEvt : function (){
		var _self = this; 
		
		// ddl copy 
		$(_self.options.metadataContentAreaWrapId).on('click','.ddl-copy', function (){
			var sEle = $(this)
				,mode = sEle.data('ddl-copy-mode');
			
			var copyTxt = sEle.closest('.ddl-view-area').find('textarea').val();
			
			if('copy'==mode){
				copyStringToClipboard('ddlcopy' ,copyTxt);
			}else{
				_ui.SQL.addSqlEditContent(copyTxt , false);
			}
		})
	}
	,_initCacheObject : function (){
		var _self = this;
		var menuData = this.options.menuData;
		
		for(var i =0; i<menuData.length; i++){
			var serviceObj = menuData[i];
			var serviceNm =serviceObj.contentid;
			
			this.metadataCache[serviceNm] = {};
			
			var seviceGrid  =$.pubGrid(_self.options.dbServiceMenuContentId+'>#'+serviceNm);
			
			if(!VARSQL.isUndefined(seviceGrid)){
				seviceGrid.setData([],'reDraw');
			}
		}
		
		this.selectMetadata = {}; // 선택한 메뉴 
	}
	,initElement :function (){
		var _self = this;
		_self.options.metadataContentAreaWrapEle = $(_self.options.metadataContentAreaWrapId);
		_self.options.metadataTabAreaWrapEle = $(_self.options.metadataTabAreaWrapId);
		
		//_self.options.metadataContentAreaWrapEle.empty();
		//$(_self.options.dbServiceMenuContentId).empty();
	}
	,getMetaContentWrapEle:function (){
		return this.options.metadataContentAreaWrapEle; 
	}
	,getMetadataTabAreaWrapEle:function (){
		return this.options.metadataTabAreaWrapEle; 
	}
	// resize object area
	,resizeObjectArea : function (dimension){
		try{
			// tab resize
			$.pubTab(this.options.serviceMenuTabId).refresh().setDropHeight(dimension.height-10);
		}catch(e){};
		
		try{
			// data resize
			$.pubGrid(this.options.dbServiceMenuContentId+'>#'+this.selectObjectMenu).resizeDraw(dimension);
		}catch(e){};
		
	}
	// meta 영역 resize
	,resizeMetaArea : function (){
		var resizeMethod = this.getCallMethod('_'+this.selectObjectMenu +'MetaResize');
		resizeMethod.call(this);
	}
	// object service 텝 메뉴 그리기
	,_tabs : function (){
		var _self = this; 
	
		var data = _self.options.menuData;
		var len = data.length; 
	
		if(len < 1) return ; 
		var item; 
		
		$(_self.options.dbServiceMenuContentId).empty();
		$(_self.options.serviceMenuTabId).empty();
		
		$.pubTab(_self.options.serviceMenuTabId,{
			items :data
			,width : 'auto'
			,height : 20
			,dropItemHeight : $(_self.options.dbServiceMenuContentId).height() -10
			,activeIcon :{
				position : 'append'		//  활성시 html 추가 위치
				,html : '<i class="fa fa-refresh" style="cursor:pointer;"></i>'		// 활성시 추가할 html
				,click : function (item){
					if(confirm('새로고침 하시겠습니까?')){
						_self._removeMetaCache(item.contentid);
						_self._dbObjectList(item, true);
					}
				}
			}
			,overItemViewMode :'drop'
			,addClass :'service_menu_tab'
			,click : function (item){
				var sObj = $(this);
				
				var refresh = sObj.attr('refresh')=='Y'?true:false;
				sObj.attr('refresh','N');
				
				_self.selectObjectMenu = item.contentid;
				
				if(refresh===true){
					_self._removeMetaCache(item.contentid);
				}
				
				//_self.resizeObjectArea();
				_self._dbObjectList(item, refresh);
			}
		})
	}
	// 클릭시 텝메뉴에 해당하는 메뉴 그리기
	,_dbObjectList:function(selObj,refresh){
		var _self = this;
		var $contentId = selObj.contentid;
		
		var activeObj = $(_self.options.dbServiceMenuContentId+'>#'+$contentId);
		
		$(_self.options.dbServiceMenuContentId+'>'+' .show-display').removeClass('show-display');
		
		// 현재 창 사이즈 구하기.
		var dimension = {width:$(_self.options.dbServiceMenuContentId).width() , height:$(_self.options.dbServiceMenuContentId).height()};
		
		var metaEleId =_self._getMetadataObjectEleId($contentId);
		var tmpEle = $(metaEleId);
		
		$('.varsql-meta-cont-ele.on').removeClass('on');
		if(tmpEle.length < 1){
			_self.getMetaContentWrapEle().append('<div id="'+ (metaEleId).replace('#', '') +'" class="varsql-meta-cont-ele on"></div>');
		}else{
			if(!tmpEle.hasClass('on')){
				tmpEle.addClass('on');
			}
		}
		
		var metaTabId =_self.options.metadataTabAreaWrapId+$contentId;
		var tmpMetaEle =$(metaTabId); 
		$('.varsql-meta-tab-ele.on').removeClass('on');
		
		_ui.layout.setActiveTab('dbMetadata');
		
		if(tmpMetaEle.length < 1){
			_self.getMetadataTabAreaWrapEle().append('<div id="'+ (metaTabId).replace('#', '') +'" class="varsql-meta-tab-ele on"></div>');
		}else{
			if(!tmpMetaEle.hasClass('on')){
				tmpMetaEle.addClass('on');
			}
		}
		
		if(activeObj.length > 0){
			activeObj.addClass('show-display');
			
			var resizeMethod = _self.getCallMethod('_'+$contentId +'MetaResize');
			
			resizeMethod.call(_self);
			
			if(refresh){
				//activeObj.empty();
			}else{
				$.pubGrid(_self.options.dbServiceMenuContentId+'>#'+$contentId).resizeDraw(dimension);
				return ; 
			}
		}else{
			$(_self.options.dbServiceMenuContentId).append('<div id="'+$contentId+'" class="db-metadata-area show-display"></div>');
		}
		
		var callMethod = _self.getCallMethod('_'+$contentId);
		
		VARSQL.req.ajax({      
			loadSelector : '#dbSchemaObjectArea'
			,url:{gubun:VARSQL.uri.database, url:'/dbObjectList.varsql'}
			,data:$.extend(true,_self.options.param,{'gubun':$contentId}) 
			,success:function (resData){
				callMethod.call(_self,resData);
			}
		});
	}
	// 클릭시 텝메뉴에 해당하는 메뉴 그리기
	,_dbObjectMetadataList : function(param,callMethod,refresh){
		var _self = this
			,objType = param.gubun
			,objName = param.objectName; 
		
		_self.selectMetadata[objType] = objName; // 선택한 오브젝트 캐쉬
		
		_ui.layout.setActiveTab('dbMetadata');
		
		var refreshFlag = true; 
		if(!refresh){
			var cacheData = _self._getMetaCache(objType,objName);
			if(cacheData){
				refreshFlag = false; 
			}
		}
		
		var callMethod = _self.getCallMethod(callMethod);
		callMethod.call(_self, _self.options.metadataTabAreaWrapId+objType , param , refreshFlag);
	}
	// meta data 가져오기.
	,_getMetadataInfo : function (param , callbackFn){
		var _self =this; 
		VARSQL.req.ajax({
			loadSelector : _self.options.metadataContentAreaWrapId
			,url:{gubun:VARSQL.uri.database, url:'/dbObjectMetadataList.varsql'}
			,async:false
			,data:param
			,success:function (resData){
				var result = resData.list;
				
				if(result.length > 0){
					var callData=result;
					if(param.gubun=='table' || param.gubun=='view'){
						callData = result[0].colList
					}
					_self._setMetaCache(param.gubun,param.objectName, callData); // data cache
					callbackFn.call(_self,resData, callData);
				}
			}
		});
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
		
		var param =$.extend({},_self.options.param,{'gubun':sObj.gubun ,objectName:sObj.objName});
		
		VARSQL.req.ajax({
			url:{gubun:VARSQL.uri.database, url:'/createDDL.varsql'}
			,data:param
			,success:function (resData){
					
				_self._setMetaCache(param.gubun, param.objectName, 'ddl', resData.item);
				
				if(VARSQL.isFunction(callbackFn)){
					callbackFn.call(_self, resData.item);
				}else{
					if(sObj.gubunKey=='ddl_copy'){
						_ui.text.copy(resData.item);
					}else{
						_ui.SQL.addSqlEditContent(resData.item , false);
					}
				}
			}
		});
	}
	,_getMetadataObjectEleId : function (objectType){
		return this.options.metadataContentAreaWrapId+objectType; 
	}
	// meta element check
	, _getMetadataElement :  function(objectType, eleName, addHtml){
		var objectEleId =this._getMetadataObjectEleId(objectType); 
		var metaEleId = objectEleId+eleName;
		
		var isCreate = false; 
		
		if($(metaEleId).length < 1){
			isCreate = true; 
			$(objectEleId).append('<div id="'+ (metaEleId).replace('#', '') +'" data-meta-tab="'+(eleName)+'"  class="varsql-meta-cont-tab-ele on">'+addHtml+'</div>');
		}
		
		return {
			eleId :metaEleId 
			,isCreate : isCreate
		} 
	}
	// 데이타 내보내기
	,_dataExport : function (exportObj){
		_ui.SQL.exportDataDownload(exportObj);
	}
	// 컨텍스트 메뉴 sql 생성 부분 처리 .
	,_createScriptSql :function (scriptObj){
		_ui.SQL.addCreateScriptSql(scriptObj);
	}
	,_createJavaProgram: function (scriptObj){
		_ui.JAVA.createJavaProgram(scriptObj);
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
		var metaEleInfo = this._getMetadataElement(objectType,eleName,addHtml);
		
		var ele = $(metaEleInfo.eleId);
		
		ele.find('.prettyprint').empty().html(ddlSource).removeClass('prettyprinted');
		ele.find('textarea').val(ddlSource);
		ele.scrollTop(0);
		PR.prettyPrint();
		
	}
	// 메타 데이타 케쉬된값 꺼내기
	,_getMetaCache:function (gubun, objecName, tabKey){
		tabKey =tabKey||'column';
		
		var t =this.metadataCache[gubun][objecName][tabKey]; 
		return t?t:null;
	}
	// 메타 데이타 셋팅하기.
	,_setMetaCache:function (gubun, objecName, tabKey, data){
		if(VARSQL.isUndefined(this.metadataCache[gubun][objecName])){
			var objData = {};
			objData[tabKey] = data; 
			this.metadataCache[gubun][objecName] =objData;
		}else{
			this.metadataCache[gubun][objecName][tabKey]= data;
		}
	}
	,_removeMetaCache:function (gubun, objecName){
		if(typeof gubun !='undefined' && typeof objecName != 'undefined'){
			delete this.metadataCache[gubun][objecName];  
		}else if(typeof gubun !='undefined'){
			this.metadataCache[gubun] ={};
		}else{
			this._initCacheObject();
		}
	}
};

//테이블 정보보기.
_ui.utils.copy(_ui.dbSchemaObjectServiceMenu,{
	_table : function (resData, reqParam){
	
		var _self = this;
		try{
			var $$gubun = 'table';
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
				
				_self._setMetaCache($$gubun, tblName, 'column', {items:colList});
			})
						
			// 테이블 hint;
			VARSQLHints.setTableInfo(tableHint);
			
			var tableObj = $.pubGrid(_self.options.dbServiceMenuContentId+'>#'+$$gubun,{
				setting : {
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
				,tColItem : [
					{key :'name', label:'Table', width:200, sort:true}
					,{key :'remarks', label:'설명', sort:true}
				]
				,tbodyItem :itemArr
				,bodyOptions :{
					cellDblClick : function (rowItem){
						var selKey =rowItem.keyItem.key;
						
						if(selKey == 'name' ){
							_ui.SQL._sqlData('select * from '+rowItem.item[selKey],false);
						}
					}
				}
				,rowOptions :{
					click : function (idx, item){
						var sObj = $(this);
						
		    			var refresh = sObj.attr('refresh')=='Y'?true:false; 
		    			sObj.attr('refresh','N');
		    			
		    			sObj.addClass('active');
		    			
		    			_self._dbObjectMetadataList($.extend({},_self.options.param,{'gubun':$$gubun,'objectName':item.name}), '_'+$$gubun+'TabCtrl', refresh);
					}
					,contextMenu :{
						beforeSelect :function (){
							$(this).trigger('click');
						}
						,callback: function(key,sObj) {
							var ele = this.element, sItem = this.gridItem;
							var gubun= $$gubun
								,tmpName = sItem.name;
							
							if(key=='dataview_all'){
								_ui.SQL._sqlData('select * from '+tmpName,false);
								return ; 
							}else if(key=='dataview_count'){
								_ui.SQL._sqlData('select count(1) CNT from '+tmpName,false);
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
							
							var cacheData = _self._getMetaCache(gubun,tmpName, 'column');
							
							var params ={
								gubun:gubun
								,gubunKey :key
								,objName : tmpName 
								,item : cacheData
							};
							
							if(key=='export_data'){
								_self._dataExport(params);
								return ;
							}
							
							if(key=='java_camel_case_naming'|| key=='java_json' || key =='java_valid'){
								_self._createJavaProgram(params);
								return ;
							}
							
							params.sqlGenType = sObj.mode;
							params.param_yn = sObj.param_yn;
							_self._createScriptSql(params);
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

	// table tab control
	,_tableTabCtrl : function (metaTabId, param , refreshFlag){
		var _self =this; 
		var tabObj = $.pubTab(metaTabId);
		
		if(tabObj){
			tabObj.itemClick();
			return ;
		}
		
		$.pubTab(metaTabId,{
			items : [
				{name: "Column", key: "column"}
				,{name: "DDL", key: "ddl"}
			]
			,width : 'auto'
			,height:20
			,overItemViewMode :'drop'
			,click : function (item){
				var tabEle= $(this)
					,objectName = _self.selectMetadata['table'];
				
				var itemKey = item.key;
				
				var sEle = $(_self._getMetadataObjectEleId('table')+' [data-meta-tab="'+itemKey+'"]');
		
				if(!sEle.hasClass('on')){
					$(_self._getMetadataObjectEleId('table')+' .on[data-meta-tab]').removeClass('on');
					sEle.addClass('on');
				}
				
				var cacheData = _self._getMetaCache('table', objectName, itemKey);
		
				if('column' == itemKey){
					if(cacheData){
						_self._tableColumn(cacheData, param, itemKey, false);
						return ; 
					}else{
						_self._getMetadataInfo(param, function (resData, param){
							_self._tableColumn(resData, param, itemKey, true);
						})
					}
				}else if('ddl' == itemKey){
					if(cacheData){
						_self.metadataDDLView('table',itemKey, cacheData);
						return ; 
					}else{
						_self._createDDL({
							gubun : 'table'
							,objName :  objectName
						}, function (data){
							_self.metadataDDLView('table',itemKey, data);
						});
					}
				}
			}
		}).itemClick();
	}
	//테이블에 대한 메타 정보 보기 .
	,_tableColumn:function (colData ,reqParam, eleName, reloadFlag){
		var _self = this;
		
		var metaEleInfo = _self._getMetadataElement('table',eleName);
		
		var metaEleId = metaEleInfo.eleId; 
		
		var gridObj = $.pubGrid(metaEleId);
		
		if(metaEleInfo.isCreate ===true){
			if(gridObj) gridObj.destroy();
		}
		
		var items = colData.items;
		
		if(reloadFlag===true){ // 데이타 세로 로드시 cache에 추가. 
			var colArr = [];
			$.each(items , function (i , item){
				colArr.push(item.name);
			});
			VARSQLHints.setTableColumns(reqParam.objectName ,colArr);
		}
		
		gridObj = $.pubGrid(metaEleId);
		
		if(gridObj){
			gridObj.setData(items,'reDraw');
			return ;
		}
		
		gridObj = $.pubGrid(metaEleId, {
			headerOptions : {redraw : false}
			,asideOptions :{lineNumber : {enable : true	,width : 30}}
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
						
						_self._createScriptSql({
							gubunKey : key
							,sqlGenType : sObj.mode
							,gubun : 'table'
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
		var gridObj = $.pubGrid(this._getMetadataObjectEleId('table') + "column");
		
		if(gridObj){
			gridObj.resizeDraw();
		}
	}
});

//view 정보 보기.
_ui.utils.copy(_ui.dbSchemaObjectServiceMenu ,{
	_view:function (resData ,reqParam){
		var _self = this;
		try{
			var $$gubun = 'view';	
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
				
				_self._setMetaCache($$gubun,tblName,  'column', {items:colList});
				
			})
						
			// 테이블 hint;
			VARSQLHints.setTableInfo(tableHint);
			
			var gridObj = $.pubGrid(_self.options.dbServiceMenuContentId+'>#'+$$gubun,{
				asideOptions :{
					lineNumber : {enable : true	,width : 30	,styleCss : 'text-align:right;padding-right:3px;'}				
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
							_ui.SQL._sqlData('select * from '+rowItem.item[selKey],false);
						}
					}
				}
				,rowOptions : {
					click : function (idx, item){
						var sObj = $(this);
		    			sObj.addClass('active');
		    			
		    			_self._dbObjectMetadataList($.extend({},_self.options.param,{'gubun':$$gubun,'objectName':item.name}), '_'+$$gubun+'TabCtrl');
					}
					,contextMenu :{
						beforeSelect :function (){
							$(this).trigger('click');
						}
						,callback: function(key,sObj) {
							var ele = this.element, sItem = this.gridItem;
							var gubun=$$gubun
								,tmpName = sItem.name;
							
							if(key =='copy'){
								gridObj.copyData();
								return ; 
							}
							
							var cacheData = _self._getMetaCache(gubun, tmpName);
							
							_self._createScriptSql({
								gubunKey : key
								,sqlGenType : sObj.mode
								,gubun : $$gubun
								,objName :  _self.selectMetadata[$$gubun]
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
	//view tab control
	,_viewTabCtrl : function (metaTabId, param , refreshFlag){
		var _self =this; 
		
		var $objType = 'view';
		var tabObj = $.pubTab(metaTabId);
		
		if(tabObj){
			tabObj.itemClick();
			return ;
		}
		
		$.pubTab(metaTabId,{
			items : [
				{name: "Column", key: "column"}
				,{name: "DDL", key: "ddl"}
			]
			,height:20
			,overItemViewMode :'drop'
			,click : function (item){
				var tabEle= $(this)
					,objectName = _self.selectMetadata[$objType];
				
				var itemKey = item.key;
				
				var sEle = $(_self._getMetadataObjectEleId($objType)+' [data-meta-tab="'+itemKey+'"]');
		
				if(!sEle.hasClass('on')){
					$(_self._getMetadataObjectEleId($objType)+' .on[data-meta-tab]').removeClass('on');
					sEle.addClass('on');
				}
				
				var cacheData = _self._getMetaCache($objType, objectName, itemKey);
		
				if('column' == itemKey){
					if(cacheData){
						_self._viewColumn(cacheData, param, itemKey, false);
						return ; 
					}else{
						_self._getMetadataInfo(param, function (resData, param){
							_self._viewColumn(resData, param, itemKey, true);
						})
					}
				}else if('ddl' == itemKey){
					if(cacheData){
						_self.metadataDDLView($objType,itemKey, cacheData);
						return ; 
					}else{
						_self._createDDL({
							gubun : $objType
							,objName :  objectName
						}, function (data){
							_self.metadataDDLView($objType,itemKey, data);
						});
					}
				}
			}
		}).itemClick();
	}
	// view 메타 데이타 보기.
	,_viewColumn :function (colData ,reqParam, eleName, reloadFlag){
		var _self = this;
		var $objType = 'view';
		
		var metaEleInfo = _self._getMetadataElement($objType,eleName);
		
		var metaEleId = metaEleInfo.eleId; 
		
		var gridObj = $.pubGrid(metaEleId);
		
		if(metaEleInfo.isCreate ===true){
			if(gridObj) gridObj.destroy();
		}
		
		var items = colData.items;
		
		if(reloadFlag===true){ // 데이타 세로 로드시 cache에 추가. 
			var colArr = [];
			$.each(items , function (i , item){
				colArr.push(item.name);
			});
			VARSQLHints.setTableColumns(reqParam.objectName ,colArr);
		}
		
		gridObj = $.pubGrid(metaEleId);
		
		if(gridObj){
			gridObj.setData(items,'reDraw');
			return ;
		}
		
		gridObj = $.pubGrid(metaEleId, {
			headerOptions : {redraw : false}
			,asideOptions :{lineNumber : {enable : true	,width : 30}}
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
						
						_self._createScriptSql({
							gubunKey : key
							,sqlGenType : sObj.mode
							,gubun : $objType
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
	,_viewMetaResize : function (){
		var gridObj = $.pubGrid(this._getMetadataObjectEleId('view') + "column");
		
		if(gridObj){
			gridObj.resizeDraw();
		}
	}
});

// 프로시저 처리.
_ui.utils.copy(_ui.dbSchemaObjectServiceMenu ,{
	_procedure:function (resData ,reqParam){
		var _self = this;
		try{
			var len = resData.items?resData.items.length:0;
    		var $$gubun = 'procedure';
    		
			var itemArr = resData.items;
			
			$.each(itemArr , function (_idx, _item){
				var _name =_item.name;
				var colList = _item.colList; 
				_self._setMetaCache($$gubun,_name, 'column', {items:colList});
			})
			
			var procedureObj = $.pubGrid(_self.options.dbServiceMenuContentId+'>#'+$$gubun,{
				asideOptions :{
					lineNumber : {enable : true	,width : 30	,styleCss : 'text-align:right;padding-right:3px;'}				
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
		    			
		    			_self._dbObjectMetadataList($.extend({},_self.options.param,{'gubun':$$gubun,'objectName':item.name}), '_'+$$gubun+'TabCtrl');
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
	//procedure tab control
	,_procedureTabCtrl : function (metaTabId, param , refreshFlag){
		var _self =this; 
		var tabObj = $.pubTab(metaTabId);
		var $objType = 'procedure';
		
		if(tabObj){
			tabObj.itemClick();
			return ;
		}
		
		$.pubTab(metaTabId,{
			items : [
				{name: "Column", key: "column"}
				,{name: "DDL", key: "ddl"}
			]
			,width : 'auto'
			,height:20
			,overItemViewMode :'drop'
			,click : function (item){
				var tabEle= $(this)
					,objectName = _self.selectMetadata[$objType];
				
				var itemKey = item.key;
				
				var sEle = $(_self._getMetadataObjectEleId($objType)+' [data-meta-tab="'+itemKey+'"]');
		
				if(!sEle.hasClass('on')){
					$(_self._getMetadataObjectEleId($objType)+' .on[data-meta-tab]').removeClass('on');
					sEle.addClass('on');
				}
				
				var cacheData = _self._getMetaCache($objType, objectName, itemKey);
				
				if('column' == itemKey){
					if(cacheData){
						_self._procedureColumn(cacheData, param, itemKey, false);
						return ; 
					}else{
						_self._getMetadataInfo(param, function (resData, param){
							_self._procedureColumn(resData, param, itemKey, true);
						})
					}
				}else if('ddl' == itemKey){
					if(cacheData){
						_self.metadataDDLView($objType,itemKey, cacheData);
						return ; 
					}else{
						_self._createDDL({
							gubun : $objType
							,objName :  objectName
						}, function (data){
							_self.metadataDDLView($objType,itemKey, data);
						});
					}
				}
			}
		}).itemClick();
	}
	//procedure 대한 메타 정보 보기 .
	,_procedureColumn :function (colData ,reqParam, eleName, reloadFlag){
		var _self = this;
 		
 		var $objType = 'procedure';
		
		var metaEleInfo = _self._getMetadataElement($objType,eleName);
		
		var metaEleId = metaEleInfo.eleId; 
		
		var gridObj = $.pubGrid(metaEleId);
		
		if(metaEleInfo.isCreate ===true){
			if(gridObj) gridObj.destroy();
		}
		
		var items = colData.items;
		
		gridObj = $.pubGrid(metaEleId);
		
		if(gridObj){
			gridObj.setData(items,'reDraw');
			return ;
		}
		
		gridObj = $.pubGrid(metaEleId, {
			headerOptions : {redraw : false}
			,asideOptions :{lineNumber : {enable : true	,width : 30}}
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
	,_procedureMetaResize : function (){
		var gridObj = $.pubGrid(this._getMetadataObjectEleId('procedure') + "column");
		
		if(gridObj){
			gridObj.resizeDraw();
		}
	}
})

// function 정보 처리.
_ui.utils.copy(_ui.dbSchemaObjectServiceMenu ,{
	_function : function (resData ,reqParam){
		var _self = this;
		try{
			var len = resData.items?resData.items.length:0;
    		var $$gubun = 'function';
    		
			var itemArr = resData.items;
			
			$.each(itemArr , function (_idx, _item){
				var _name =_item.name;
				var colList = _item.colList; 
				_self._setMetaCache($$gubun,_name, 'column', {items:colList});
			})
		
			var itemArr = resData.items;
    				
			var gridObj = $.pubGrid(_self.options.dbServiceMenuContentId+'>#function',{
				asideOptions :{
					lineNumber : {enable : true	,width : 30	,styleCss : 'text-align:right;padding-right:3px;'}				
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
		    			
		    			_self._dbObjectMetadataList($.extend({},_self.options.param,{'gubun':$$gubun,'objectName':item.name}), '_'+$$gubun+'TabCtrl');
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
	//functio tab control
	,_functionTabCtrl : function (metaTabId, param , refreshFlag){
		var _self =this; 
		var tabObj = $.pubTab(metaTabId);
		var $objType = 'function';
		
		if(tabObj){
			tabObj.itemClick();
			return ;
		}
		
		$.pubTab(metaTabId,{
			items : [
				{name: "Column", key: "column"}
				,{name: "DDL", key: "ddl"}
			]
			,height:20
			,overItemViewMode :'drop'
			,click : function (item){
				var tabEle= $(this)
					,objectName = _self.selectMetadata[$objType];
				
				var itemKey = item.key;
				
				var sEle = $(_self._getMetadataObjectEleId($objType)+' [data-meta-tab="'+itemKey+'"]');
		
				if(!sEle.hasClass('on')){
					$(_self._getMetadataObjectEleId($objType)+' .on[data-meta-tab]').removeClass('on');
					sEle.addClass('on');
				}
				
				var cacheData = _self._getMetaCache($objType, objectName, itemKey);
				
				if('column' == itemKey){
					if(cacheData){
						_self._functionColumn(cacheData, param, itemKey, false);
						return ; 
					}else{
						_self._getMetadataInfo(param, function (resData, param){
							_self._functionColumn(resData, param, itemKey, true);
						})
					}
				}else if('ddl' == itemKey){
					if(cacheData){
						_self.metadataDDLView($objType,itemKey, cacheData);
						return ; 
					}else{
						_self._createDDL({
							gubun : $objType
							,objName :  objectName
						}, function (data){
							_self.metadataDDLView($objType,itemKey, data);
						});
					}
				}
			}
		}).itemClick();
	}
	//functio 대한 메타 정보 보기 .
	,_functionColumn :function (colData ,reqParam, eleName, reloadFlag){
		var _self = this;
		
 		var $objType = 'function';
		
		var metaEleInfo = _self._getMetadataElement($objType,eleName);
		
		var metaEleId = metaEleInfo.eleId; 
		
		var gridObj = $.pubGrid(metaEleId);
		
		if(metaEleInfo.isCreate ===true){
			if(gridObj) gridObj.destroy();
		}
		
		var items = colData.items;
		
		gridObj = $.pubGrid(metaEleId);
		
		if(gridObj){
			gridObj.setData(items,'reDraw');
			return ;
		}
		
		gridObj = $.pubGrid(metaEleId, {
			headerOptions : {redraw : false}
			,asideOptions :{lineNumber : {enable : true	,width : 30}}
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
	,_functionMetaResize : function (){
		var gridObj = $.pubGrid(this._getMetadataObjectEleId('function') + "column");
		
		if(gridObj){
			gridObj.resizeDraw();
		}
	}
})

// index 처리.
_ui.utils.copy(_ui.dbSchemaObjectServiceMenu ,{
	_index : function (resData ,reqParam){
		var _self = this;
		try{
			var len = resData.items?resData.items.length:0;
    		var $$gubun = 'index';
    		
			var itemArr = resData.items;
			
			$.each(itemArr , function (_idx, _item){
				var _name =_item.name;
				var colList = _item.colList; 
				_self._setMetaCache($$gubun,_name, 'column', {items:colList});
			})
		
			var itemArr = resData.items;
    				
			var indexObj = $.pubGrid(_self.options.dbServiceMenuContentId+'>#'+$$gubun,{
				asideOptions :{
					lineNumber : {enable : true	,width : 30	,styleCss : 'text-align:right;padding-right:3px;'}				
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
		    			
		    			_self._dbObjectMetadataList($.extend({},_self.options.param,{'gubun':$$gubun,'objectName':item.name}), '_'+$$gubun+'TabCtrl');
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
	//index tab control
	,_indexTabCtrl : function (metaTabId, param , refreshFlag){
		var _self =this; 
		var tabObj = $.pubTab(metaTabId);
		var $objType = 'index';
		
		if(tabObj){
			tabObj.itemClick();
			return ;
		}
		
		$.pubTab(metaTabId,{
			items : [
				{name: "Column", key: "column"}
				,{name: "DDL", key: "ddl"}
			]
			,height:20
			,overItemViewMode :'drop'
			,click : function (item){
				var tabEle= $(this)
					,objectName = _self.selectMetadata[$objType];
				
				var itemKey = item.key;
				
				var sEle = $(_self._getMetadataObjectEleId($objType)+' [data-meta-tab="'+itemKey+'"]');
		
				if(!sEle.hasClass('on')){
					$(_self._getMetadataObjectEleId($objType)+' .on[data-meta-tab]').removeClass('on');
					sEle.addClass('on');
				}
				
				var cacheData = _self._getMetaCache($objType, objectName, itemKey);
				
				if('column' == itemKey){
					if(cacheData){
						_self._indexColumn(cacheData, param, itemKey, false);
						return ; 
					}else{
						_self._indexColumn(param, function (resData, param){
							_self._functionColumn(resData, param, itemKey, true);
						})
					}
				}else if('ddl' == itemKey){
					if(cacheData){
						_self.metadataDDLView($objType,itemKey, cacheData);
						return ; 
					}else{
						_self._createDDL({
							gubun : $objType
							,objName :  objectName
						}, function (data){
							_self.metadataDDLView($objType,itemKey, data);
						});
					}
				}
			}
		}).itemClick();
	}
	// index column 정보.
	,_indexColumn : function (colData ,reqParam, eleName, reloadFlag){
		var _self = this;
		
 		var $objType = 'index';
		
		var metaEleInfo = _self._getMetadataElement($objType,eleName);
		
		var metaEleId = metaEleInfo.eleId; 
		
		var gridObj = $.pubGrid(metaEleId);
		
		if(metaEleInfo.isCreate ===true){
			if(gridObj) gridObj.destroy();
		}
		
		var items = colData.items;
		
		gridObj = $.pubGrid(metaEleId);
		
		if(gridObj){
			gridObj.setData(items,'reDraw');
			return ;
		}
		
		gridObj = $.pubGrid(metaEleId, {
			headerOptions : {redraw : false}
			,asideOptions :{lineNumber : {enable : true	,width : 30}}
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
	,_indexMetaResize : function (){
		var gridObj = $.pubGrid(this._getMetadataObjectEleId('index') + "column");
		
		if(gridObj){
			gridObj.resizeDraw();
		}
	}
})

// trigger 처리.
_ui.utils.copy(_ui.dbSchemaObjectServiceMenu ,{
	_trigger : function (resData ,reqParam){
		var _self = this;
		try{
			var len = resData.items?resData.items.length:0;
    		var $$gubun = 'trigger';
    		
			var itemArr = resData.items;
			
			$.each(itemArr , function (_idx, _item){
				var _name =_item.name;
				var colList = _item.colList; 
				_self._setMetaCache($$gubun,_name, {items:colList});
			})
		
			var itemArr = resData.items;
			
			var triggerGridObj = $.pubGrid(_self.options.dbServiceMenuContentId+'>#'+$$gubun,{
				asideOptions :{
					lineNumber : {enable : true	,width : 30	,styleCss : 'text-align:right;padding-right:3px;'}				
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
		    			
		    			_self._dbObjectMetadataList($.extend({},_self.options.param,{'gubun':$$gubun,'objectName':item.name}), '_'+$$gubun+'TabCtrl');
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
	//trigger tab control
	,_triggerTabCtrl : function (metaTabId, param , refreshFlag){
		var _self =this; 
		var tabObj = $.pubTab(metaTabId);
		var $objType = 'trigger';
		
		if(tabObj){
			tabObj.itemClick();
			return ;
		}
		
		$.pubTab(metaTabId,{
			items : [
				{name: "DDL", key: "ddl"}
			]
			,height:20
			,click : function (item){
				var tabEle= $(this)
					,objectName = _self.selectMetadata[$objType];
				
				var itemKey = item.key;
				
				var sEle = $(_self._getMetadataObjectEleId($objType)+' [data-meta-tab="'+itemKey+'"]');
		
				if(!sEle.hasClass('on')){
					$(_self._getMetadataObjectEleId($objType)+' .on[data-meta-tab]').removeClass('on');
					sEle.addClass('on');
				}
				
				var cacheData = _self._getMetaCache($objType, objectName, itemKey);
				
				if(cacheData){
					_self.metadataDDLView($objType,itemKey, cacheData);
					return ; 
				}else{
					_self._createDDL({
						gubun : $objType
						,objName :  objectName
					}, function (data){
						_self.metadataDDLView($objType,itemKey, data);
					});
				}
			}
		}).itemClick();
	}
	,_triggerMetaResize : function (){
		var gridObj = $.pubGrid(this._getMetadataObjectEleId('trigger') + "column");
		
		if(gridObj){
			gridObj.resizeDraw();
		}
	}
})

// sequence 처리.
_ui.utils.copy(_ui.dbSchemaObjectServiceMenu ,{
	_sequence : function (resData ,reqParam){
		var _self = this;
		try{
			var len = resData.items?resData.items.length:0;
			var $$gubun = 'sequence';
			
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
				
				_self._setMetaCache($$gubun,_name, 'info', {items:colList});
			})
			
			var itemArr = resData.items;
			
			var triggerGridObj = $.pubGrid(_self.options.dbServiceMenuContentId+'>#'+$$gubun,{
				asideOptions :{
					lineNumber : {enable : true	,width : 30	,styleCss : 'text-align:right;padding-right:3px;'}				
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
						
						_self._dbObjectMetadataList($.extend({},_self.options.param,{'gubun':$$gubun,'objectName':item.name}), '_'+$$gubun+'TabCtrl');
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
	//sequence tab control
	,_sequenceTabCtrl : function (metaTabId, param , refreshFlag){
		var _self =this; 
		var tabObj = $.pubTab(metaTabId);
		var $objType = 'sequence';
		
		if(tabObj){
			tabObj.itemClick();
			return ;
		}
		
		$.pubTab(metaTabId,{
			items : [
				{name: "info", key: "info"}
				,{name: "DDL", key: "ddl"}
			]
			,height:20
			,click : function (item){
				var tabEle= $(this)
				,objectName = _self.selectMetadata[$objType];
				
				var itemKey = item.key;
				
				var sEle = $(_self._getMetadataObjectEleId($objType)+' [data-meta-tab="'+itemKey+'"]');
				
				if(!sEle.hasClass('on')){
					$(_self._getMetadataObjectEleId($objType)+' .on[data-meta-tab]').removeClass('on');
					sEle.addClass('on');
				}
				
				var cacheData = _self._getMetaCache($objType, objectName, itemKey);
				
				if('info' == itemKey){
					_self._sequenceInfo(cacheData, param, itemKey, false);
					return ; 
				}else if('ddl' == itemKey){
					if(cacheData){
						_self.metadataDDLView($objType,itemKey, cacheData);
						return ; 
					}else{
						_self._createDDL({
							gubun : $objType
							,objName :  objectName
						}, function (data){
							_self.metadataDDLView($objType,itemKey, data);
						});
					}
				}
			}
		}).itemClick();
	}
	// sequence 정보보기.
	,_sequenceInfo : function (colData ,reqParam, eleName, reloadFlag){
		var _self = this;
		
 		var $objType = 'sequence';
		
		var metaEleInfo = _self._getMetadataElement($objType,eleName);
		
		var metaEleId = metaEleInfo.eleId; 
		
		var gridObj = $.pubGrid(metaEleId);
		
		if(metaEleInfo.isCreate ===true){
			if(gridObj) gridObj.destroy();
		}
		
		var items = colData.items;
		
		gridObj = $.pubGrid(metaEleId);
		
		if(gridObj){
			gridObj.setData(items,'reDraw');
			return ;
		}
		
		gridObj = $.pubGrid(metaEleId, {
			headerOptions : {redraw : false}
			,asideOptions :{lineNumber : {enable : true	,width : 30}}
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
	,_sequenceMetaResize : function (){
		var gridObj = $.pubGrid(this._getMetadataObjectEleId('sequence') + "info");
		
		if(gridObj){
			gridObj.resizeDraw();
		}
	}
})

/**
 * sql 데이타 그리드
 */
_ui.SQL = {
	sqlTextAreaObj:null
	,sqlEditorSelector : '#sql_editor_area .CodeMirror.cm-s-default'
	,sqlEditorEle:null
	,memoDialog : null
	,findTextDialog : null
	,currentSqlData :''
	,options :{
		selector:'#sqlExecuteArea'
		,preloaderArea :'#sqlEditerPreloaderArea'
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
		
		var options ={dbtype:_g_options._opts.dbtype}; 
		if(!options.dbtype) {
			alert('dbtype empty');
			return ;
		}
		
		$.extend(true,_self.options, options);
		
		_self._initEditor();
		_self._initEvent();
		_self._userSettingInfo();
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
		
		_self.sqlEditorEle = $(_self.sqlEditorSelector);
	}
	,refresh : function (){
		var _self = this;
		try{
			_self.sqlTextAreaObj.refresh();
		}catch(e){
			console.log('editor refresh error')
		}
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
		
		_self.sqlEditorEle.on('click', function(e){
			$('#sql_parameter_area').removeClass('on');
		})
		
		// 에디터 mouse focus 이동.
		_self.sqlEditorEle.on('mouseenter', function(e){
			_self.sqlTextAreaObj.focus();
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
					_self.getTextAreaObj().replaceSelection(replaceTxt);
				}
			}
		}
		
		var cursor =_self.sqlTextAreaObj.getSearchCursor(schTxt, findPos , {
			caseFold : !findOpt.caseSearch
		})
		
		if(replaceAllFlag ===true){
			var replaceCount =0; 
			
			var isNext = cursor.find(isReverseFlag);
			
			while(isNext){
				replaceCount++;
				_self.sqlTextAreaObj.setSelection(cursor.from(), cursor.to());
				_self.getTextAreaObj().replaceSelection(replaceTxt);
				
				isNext = cursor.find(isReverseFlag)
			}
			
			if(!isNext){
				alert('일치하는 내용이 '+replaceCount+'회 변경되었습니다.');
			}
			
			return ; 
		}
		
		var isNext = cursor.find(isReverseFlag);
		
		if(wrapSearch===true && isNext===false){
			alert('다음 문자열을 찾을수 없습니다.\n'+orginTxt);
			return ;
		}
		
		if(replaceAllFlag ===true){
			findPos = {line: 0, ch: 0};
		}
		
		if(isNext){
			_self.sqlTextAreaObj.setSelection(cursor.from(), cursor.to());
		}else{
			if(findOpt.wrapSearch===true){
				_self.getTextAreaObj().setCursor(wrapSearchPos);
				_self.searchFindText(orginTxt,replaceTxt,replaceFlag, replaceAllFlag, true);
			}else{
				alert('다음 문자열을 찾을수 없습니다.\n'+orginTxt);
				return ; 
			}
		}
	}
	,addGridDataToEditArea : function(rowItem){
		var _self = this; 
		
		var startCursor = _self.getTextAreaObj().getCursor(true);
		
		var cellVal = rowItem.item[rowItem.keyItem.key];
		
		cellVal = cellVal+'';
		
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
		    loadSelector : '#sql_editor_wrapper'
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
						    loadSelector : '#sql_editor_wrapper'
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
		    loadSelector : '#sql_editor_wrapper'
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
		    			    ,loadSelector : '#sql_editor_wrapper'
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
	,getSelectionPosition : function(endFlag){
		var std = this.sqlTextAreaObj.listSelections()[0].anchor
		,end = this.sqlTextAreaObj.listSelections()[0].head;
		
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
		
		var params =VARSQL.util.objectMerge (_g_options.param,{
			'sql' :sqlVal
			,'limit' : $(_self.options.limitCnt).val()
			,sqlParam : JSON.stringify(sqlParam)
		});
		
		VARSQL.req.ajax({      
		    loadSelector : '#sql_editor_wrapper'
		    ,url:{gubun:VARSQL.uri.sql, url:'/base/sqlData.varsql'}
		    ,data:params 
		    ,success:function (resData){
		    	_ui.sqlDataArea.viewResult(resData)             
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
			startSelection = _self.getSelectionPosition();
		}
		
		if(''== sqlVal) return ; 
		
		var params =VARSQL.util.objectMerge (_g_options.param,{
			'sql' :sqlVal
		});
		
		params.formatType =formatType; 
		
		VARSQL.req.ajax({      
		    loadSelector : '#sql_editor_wrapper'
		    ,url:{gubun:VARSQL.uri.sql, url:'/base/sqlFormat.varsql'}
		    ,data:params 
		    ,success:function (res){
		    	var formatSql = res.item; 
		    	formatSql = VARSQL.str.trim(formatSql)
		    	
		    	var linecnt = VARSQL.matchCount(formatSql,VARSQLCont.constants.newline)+1;
	    		tmpEditor.replaceSelection(formatSql);
	    		
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
				"내보내기":function (){
					if(!confirm('내보내기 하시겠습니까?')) return ; 

					var params =VARSQL.util.objectMerge (_g_options.param,{
						exportType : VARSQL.check.radio('input:radio[name="exportType"]')
						,columnInfo : VARSQL.check.getCheckVal("input:checkbox[name='exportColumnCheckBox']:not([value='all'])").join(',')
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
				
		var editObj =_self.getTextAreaObj()
			,insLine = editObj.lastLine()+1; 
				
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
	,resultMsgAreaObj:null
	,initDataGridContextFlag : false // data grid context 초기화 여부
	,options :{
		dataGridSelector:'#dataGridArea'
		,dataColumnTypeSelector:'#dataColumnTypeArea'
		,dataGridSelectorWrap:'#dataGridAreaWrap'
		,resultMsgAreaWrap:'#resultMsgAreaWrap'
		,dataGridResultTabWrap:'#data_grid_result_tab_wrap'
		,active: null
		,delay: 0
	}
	,init : function (){
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
			
			// data grid araea
			$(_self.options.dataGridSelectorWrap +' [tab_gubun]').removeClass('on');
			$(_self.options.dataGridSelectorWrap +' [tab_gubun='+tab_gubun+']').addClass('on');
			
			if(tab_gubun == 'columnType'){
				_self.viewResultColumnType();
			}
		});
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
			
			var logValEle = $('<div><div class="error-log-area"><span class="log-end-time">'+milli2str(resultData.item.result.endtime,_defaultOptions.dateFormat)+'</span>#resultMsg#</div></div>'.replace('#resultMsg#' , '<span class="error-message">'+resultData.message+'</span><br/>sql line : <span class="error-line">['+resultData.customs.errorLine+']</span> query: <span class="log-query"></span>'));
			logValEle.find('.log-query').text(errQuery);
			
			resultMsg.push(logValEle.html());
			logValEle.empty();
			logValEle= null; 
			
			var stdPos = _ui.SQL.getSelectionPosition();
			
			var cursor =_ui.SQL.sqlTextAreaObj.getSearchCursor(errQuery, stdPos);
			
			if(cursor.findNext()){
				_ui.SQL.sqlTextAreaObj.setSelection(cursor.from(), cursor.to());
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
		}
		
		_self.getResultMsgAreaObj().prepend(resultMsg.join(''));
		_self.getResultMsgAreaObj().animate({scrollTop: 0},'fast');
	}
	// sql result column typ
	,viewResultColumnType : function (){
		var _self = this; 
		var _currnetQueryReusltData = _self._currnetQueryReusltData;
		var columnTypeArr = _currnetQueryReusltData.column; 
		if(_currnetQueryReusltData.viewType != 'grid'){
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
					_ui.SQL.addGridDataToEditArea(rowItem);
				}
			}
			,tColItem : pGridData.column
			,tbodyItem :pGridData.data
		});
		
		
		if(_self.initDataGridContextFlag===false) { // grid context menu 처리. 
			_self.initDataGridContextFlag= true; 
			var gridContextObj = $.pubContextMenu(_self.options.dataGridSelector+' .pubGrid-body-container', {
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
						$.pubGrid(_self.options.dataGridSelector).copyData();
						return ; 
					}
					
					if(key.indexOf('download_') > -1){
						var sqlGridResultSelect = gridContextObj.getCheckBoxId('sqlGridResultSelect');
						var isSelect = $("#"+sqlGridResultSelect).is(":checked");
						
						var selData = $.pubGrid(_self.options.dataGridSelector).getData({isSelect:isSelect, dataType:'json'});
						var mode = sObj.mode; 
						
						var params =VARSQL.util.objectMerge (_g_options.param,{
							exportType :mode 
							,headerInfo : JSON.stringify(selData.header)
							,gridData : JSON.stringify(selData.data)
						});
						
						VARSQL.req.download({
							type: 'post'
							,url: {gubun:VARSQL.uri.sql, url:'/base/gridDownload.varsql'}
							,params: params
						});
						return 
					}
				}
			});
		}
	}
	//result message area
	,getResultMsgAreaObj:function(){
		var _self = this; 
		
		if(_self.resultMsgAreaObj==null){
			_self.resultMsgAreaObj = $(_self.options.resultMsgAreaWrap);
		}
		return _self.resultMsgAreaObj; 
	}
}

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
	,copy :function (copyString, copyType){
		var _this = this; 
		
		var strHtm = [];
		
		if(_this.modalEle === false){
			var modalEle = $('#data-copy-modal');
			//$(_g_options.hiddenArea).append('<div id=\"data-copy-modal\" title="복사" style="overflow:hidden"><textarea id="data-copy-area" class="width-height100"></textarea></div>');
			$(_g_options.hiddenArea).append('<div id=\"data-copy-modal\" title="복사" style="overflow:hidden"><pre id="data-copy-area" class="user-select-on prettyprint lang-sql width-height100"></pre><textarea id="data-orgin-area" style="display:none;"></textarea></div>');
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
				var tmpDbType = VARSQLCont.dataType.getDataTypeInfo(item[VARSQLCont.tableColKey.TYPE_NAME])
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
		_ui.text.copy(reval , 'java');
	}
}

/**
 * sql gen
 * @param scriptInfo
 * @returns
 */
function generateSQL(scriptInfo){
	var sqlGenType = scriptInfo.sqlGenType
		,gubun = scriptInfo.gubun
		,tmpName = scriptInfo.objName
		,data = scriptInfo.item
		,param_yn  = scriptInfo.param_yn;
	
	if(_g_options.schema != _g_options.param.schema){
		tmpName = _g_options.param.schema+'.'+tmpName;
	}
			
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

}(jQuery, window, document,VARSQL));