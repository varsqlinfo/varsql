<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/include/tagLib.jspf"%>
<!-- Page Heading -->
<div class="row page-header-area">
    <div class="col-lg-12">
        <h1 class="page-header">
        	<spring:message code="header.menu.tool.utils.multidbsqlexecute" />
        	<button type="button" class="btn btn-default btn-sm layoutInitBtn"><spring:message code="init.layout" /></button>
        </h1>
    </div>
    <!-- /.col-lg-12 -->
</div>

<div class="wh100" id=epViewArea style="padding-top: 5px;height:calc(100% - 45px)">
	<div id="layoutArea" class="wh100">
	</div>
</div>

<%--db 정보 --%>
<div id="dbSqlEditorTemplate" style="display:none;margin:0px;padding:0px;" title="DB Info">
	<div id="dbSqlEditor" class="wh100">
		<div id="dbConnectionList" class="split-item" style="width:200px; height:100%; overflow:auto;border-right: 1px solid #d5d5d8;">
			<div style="height:100%;overflow:auto;">
				<ul>
					<li v-for="(dbItem,index) in dbList">
						<a href="javascript:" class="db-conn-item">
							<input type="checkbox" @click="addResultItem(dbItem)" v-model="selectItem" :id="'chkId'+dbItem.key" :value="dbItem.key">
							<label :for="'chkId'+dbItem.key">{{dbItem.name}}</label>
						</a>
					</li>
				</ul>
			</div>
		</div>
		<div id="sqlExecuteArea" class="split-item" style="width:calc(100% - 450px); height:100%; overflow:auto;">
			<div style="height:25px;">
				<span>Limit
					<select v-model="limitRowCnt" name="limitRowCnt">
						<option value="100" selected="">100</option>
						<option value="500">500</option>
						<option value="1000">1000</option>
						<option value="5000">5000</option>
						<option value="10000">10000</option>
					</select>
				</span>
				<span>
					<button class="varsql-btn-info" @click="sqlData()" title="<spring:message code="toolbar.execute" /> Ctrl+Enter"><i class="fa fa-play"></i></button>
				</span>
				<span>
					<button type="button" @click="saveSql()" class="sql-edit-btn varsql-btn-trans" title="<spring:message code="toolbar.save" /> Ctrl+S">
						<i class="fa fa-save"></i>
					</button>
				</span>
			</div>
			<div id="editor" style="height:calc(100% - 25px);"></div>
		</div>
		<div id="sqlParamArea" class="split-item" style="width:250px; height:100%; overflow:auto;">
			<div id="sql_parameter_wrapper" class="sql-parameter-wrapper on">
				<div class="sql-param-header">
					<span class="key">Key</span>
					<span class="val">Value</span>
					<span class="remove"><button type="button" class="sql-param-add-btn" @click="addParam()"><i class="fa fa-plus"></i></button></span>
			    </div>
				<div id="sql_parameter_area" class="sql-param-body" data-paramter-id="SQcd471a6aa6d543aea08de19522d29410">
					<div class="sql-param-row" v-for="(paramItem,index) in paramList">	
						<span class="key"><input type="text" class="sql-param-key" name="sql-param-key" v-model="paramItem.key"></span>	
						<span class="val"><textarea class="sql-param-value" name="sql-param-value" rows="2" v-model="paramItem.value"></textarea></span>
						<span class="remove"><button type="button" class="sql-param-del-btn" @click="removeParam(index)"><i class="fa fa-minus"></i></button></span>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>


<varsql:importResources resoures="codeEditor,layout" editorHeight="100%"/>

<style>
.split-item{
	float: left;
}
.db-conn-item{
	display: block;
	padding: 3px 5px;
}
.db-util-body {
	overflow:hidden;
}
.lm_content{
    background: #fbfbfb;
}
</style>
<script>

(function() {
VARSQLCont.init('${dbtype}');
var varsqlLayout;
$('.layoutInitBtn').on('click',function (){
	if(!VARSQL.confirmMessage('msg.layout.restore.confirm')) return ; 
	
	componentCtrl.saveChangeData({layout : {}}, true)	
})

function getResultTabSelector(conuid, selectorType){
	if(selectorType){
		return '[data-result-key="'+conuid+'"] [data-tab-cont-id="'+(selectorType=='grid' ? 'queryData' : 'queryColumn')+'"]';
	}else{
		return '[data-result-key="'+conuid+'"]';
	}
}

var componentCtrl = {
	preferencesData : <c:out value="${settingInfo}" escapeXml="false"></c:out>
	,dbConInfo : ${varsqlfn:objectToJson(dblist)}
	,conuid : '<c:out value="${param.conuid}" escapeXml="true"></c:out>'
	,initAddComponent: function (componentState){
		componentState.initFlag = true; 
		
		var componentId = componentState.key;
		
		if(VARSQL.isUndefined(this.dbConInfo[componentId])){
			$(getResultTabSelector(componentId)).html('<table class="wh100"><tr><td style="text-align: center;"><a href="javascript:;" class="delete-component">'+VARSQL.message('delete')+'</a><br/>'+VARSQL.message('msg.deleted.param', VARSQL.message('component'))+'</td></tr></table>');
			return ; 
		}
		
		if(viewObj){
			viewObj.selectItem = viewObj.selectItem||[];
		
			var idx = viewObj.selectItem.findIndex(function (id){
				return componentId == id;
			});
			
			if(idx<0){
				viewObj.selectItem.push(componentId);
			}
		}
		
		var _this =this; 
		$.pubTab(getResultTabSelector(componentId), {
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
						_this.resultTab.clearTabContent(item);
					}
				}
			}
			,click : function (item){
				var conuid = $(this).closest('[data-result-key]').attr('data-result-key');
				
				if(conuid){
					var itemId = item.id; 
					if(itemId =='queryData' || itemId=='queryColumn'){
						_this.resize(conuid);
						if(itemId=='queryColumn'){
							_this.viewResultColumnType(conuid);
						}
					}
				}
			}
		});
	}
	,viewResultColumnType :function (conuid){
		//console.log(conuid, getResultTabSelector(conuid, 'column'), ((viewObj.allResultData[conuid]||{}).column||[]))
		$.pubGrid(getResultTabSelector(conuid, 'column'),{
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
				{label: VARSQL.message('column'), key: "label"}
				,{label:VARSQL.message('data.type'), key: "dbType"}
			]
			,tbodyItem: ((viewObj.allResultData[conuid]||{}).column||[])
		});
	}
	,saveChangeData : function (data, reloadFlag){
		var _this = this;
		
		this.preferencesData = {
			sql : (data.sql || this.preferencesData.sql)
			,layout : (data.layout || this.preferencesData.layout)
		}
		
		VARSQLApi.preferences.save({
			conuid : this.conuid
			,prefKey : 'multiple.database.sqlexecute'
			,prefVal : JSON.stringify(this.preferencesData)
		}, function (data){
			if(reloadFlag){
				location.href =location.href; 
			}
		});
		
	}
	,resize : function (conuid){
		try{
			$.pubGrid(getResultTabSelector(conuid, 'grid')).resizeDraw();
		}catch(e){
			//console.log(e)
		}
		try{
			$.pubGrid(getResultTabSelector(conuid, 'column')).resizeDraw();
		}catch(e){
			//console.log(e)
		}
	}
	,destroyed : function (conuid, componentInfo){
		
		var contentItem = varsqlLayout.root.contentItems[0].contentItems[1];
		if(contentItem != 'row'){
			varsqlLayout.root.contentItems[0].contentItems[1] = contentItem;
		}
		
		var idx = viewObj.selectItem.findIndex(function (id){
			return conuid == id;
		});
		
		if(idx > -1){
			viewObj.selectItem.splice(idx,1);
		}
	}
	,addComponent : function(dbItem){
		var key = dbItem.key; 
		
		var items = varsqlLayout.root.getItemsById(key);
		if(items.length > 0){
			var contentItem= items[0];
			
			if(contentItem.isInitialised ===false && contentItem.tab && !contentItem.tab.isActive){
				contentItem.tab.header.parent.setActiveContentItem(contentItem);
				contentItem.setSize();
			}
			return true;
		}
		
		var contentItem = {};
		if(varsqlLayout.root.contentItems[0].contentItems[1]){
			contentItem = varsqlLayout.root.contentItems[0].contentItems[1];	
			if(contentItem.config.type != 'row'){
				contentItem = varsqlLayout.root.contentItems[0];	
			} 
		}else{
			contentItem = varsqlLayout.root.contentItems[0];
		}
		
		contentItem.addChild({
			title: dbItem.name
		    ,type: 'component'
		    ,id : key
		    ,componentName: 'Result'
		    ,componentState: dbItem
		});
		
		this.initAddComponent(dbItem);
	}
}

var viewObj = {};
function initVue(){
	viewObj = VarsqlAPP.vueServiceBean( {
		el: '#dbSqlEditor'
		,data: {
			dbList : []
			,selectItem :[]
			,paramList:[]
			,conuid : componentCtrl.conuid
			,limitRowCnt : 100
			,editor : {}
			,allResultData : {}
			,findTextDialog : null
		}
		,computed :{
			selectAllCheck : function (){
				var dbListLen = Object.keys(this.dbList).length; 
				return dbListLen > 0 && dbListLen == this.selectItem.length;
			}
		}
		,created : function (){
			var dbConInfo = componentCtrl.dbConInfo;
			for(var key in dbConInfo){
				var item = dbConInfo[key];
				item.key =key; 
			}
			this.dbList = dbConInfo;
			this.addParam();
		}
		,methods:{
			init : function(){
				var _this = this; 
				
				this.editor = new codeEditor(document.getElementById('editor'), {
					schema: '',
					editorOptions: { 
						theme: 'vs-'+VARSQLUI.theme()
						,minimap: {enabled: true} 
						,fixedOverflowWidgets : true
					}
					,keyEvents: {
				        executeSql: () => {
				        	_this.sqlData();
				        }
				        ,save : ()=>{
				        	_this.saveSql();
				        }
					},
				})
				
				this.editor.setValue(componentCtrl.preferencesData.sql||'');
				
				this.addResultItem(this.dbList[this.conuid])
				this.loadObject();
			}
			,selectAll : function (){
				if(this.selectAllCheck){
					this.selectItem = [];
				}else{
					this.selectItem = [];
					
					for(var key in this.dbList){
						this.selectItem.push(key); 
						this.addResultItem(this.dbList[key])
					}
				}
			}
			,saveSql: function (){
				componentCtrl.saveChangeData({sql : this.editor.getValue()})
			}
			// table 명 힌트추가.
			,loadObject : function (){
				var _this =this; 
				VARSQL.req.ajax({
					url:{type:VARSQL.uri.database, url:'/dbObjectList'}
					,data : {
						conuid: this.conuid
						,objectType: 'table'
					}
					,success:function (resData){
						_this.editor.addSuggestionInfo('', 'table', resData.list);
					}
				});
			}
			,getSql: function (executeSqlFlag){
				var _self = this;
				var executeSql = this.editor.getSelectionValue(); 
				
				if(executeSql.trim() =='' && executeSqlFlag===true){
					var pos = this.editor.getCursorPosition();
					var result = VARSQLUtils.split(this.editor.getValue() ,{findLine : pos.lineNumber, findCharPos : pos.column});
					
					if(result.length >0 ){
						var item = result[0]; 
						this.editor.setSelection({startLineNumber :item.startLine ,startColumn :item.startCharPos-1
							,endLineNumber :item.endLine ,endColumn : item.endCharPos
						});
						executeSql = item.statement;
					}
				}
				return executeSql;
			}
			,sqlData : function (){
				
				var _this = this; 
				var sql = this.getSql(true); 
				
				var sqlParam = VARSQLUtils.getSqlParam(sql);
				
				var paramList = this.paramList;
				
				var addParam = {};
				
				var addFlag = false; 
				
				for(var key in sqlParam){
					var findItem = paramList.find(function (item){
						return item.key ==  key;
					})
					
					if(VARSQL.isUndefined(findItem)){
						addFlag = true; 
						_this.paramList.push({key:key,value:''});
					}
				}
				
				if(addFlag){
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
					
					return ; 
				}
				
				var sqlParamValue = {};
				paramList.forEach(function(item){
					sqlParamValue[item.key] = item.value;
				})
				
				var params = {
					'sql' : sql
					,'limit' : this.limitRowCnt
					,sqlParam : JSON.stringify(sqlParamValue)
					,conuid : ''
				};
				
				this.selectItem.forEach(function (conuid){
					params.conuid = conuid;
					
					var items = varsqlLayout.root.getItemsById(conuid);

					if(items.length > 0){
						var contentItem= items[0];

						if(!contentItem.tab.isActive){
							contentItem.tab.header.parent.setActiveContentItem(contentItem);
							contentItem.setSize();
						}
					}
					
					VARSQL.req.ajax({
					    loadSelector : getResultTabSelector(conuid)
					    ,disableResultCheck : true
					    ,enableLoadSelectorBtn : true 
					    ,url:{type:VARSQL.uri.sql, url:'/base/execute'}
					    ,data:params
					    ,success:function (resData){
					    	_this.viewResult(conuid, resData)
						}
					});
				})
			}
			,viewResult : function (conuid, resultData){
				var _this = this; 
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
						
						_this.setGridData(conuid, resultInfo);
					}else{
						errorMessage = resultData.message;
					}
					
					var logValEle = $('<div><div class="error"><span class="log-end-time">'+VARSQLUtils.millitimeToFormat(msgItemResult.endtime,VARSQLCont.timestampFormat)+' </span>#resultMsg#</div></div>'.replace('#resultMsg#' , '<span class="error-message">'+errorMessage+'</span><br/>sql line : <span class="error-line">['+resultData.customMap.errorLine+']</span> query: <span class="log-query"></span>'));
					logValEle.find('.log-query').text(errQuery);

					resultMsg.push(logValEle.html());
					logValEle.empty();
					logValEle= null;
				}
				var tabObj = $.pubTab(getResultTabSelector(conuid)); 
				if(msgViewFlag){
					tabObj.itemClick({id :'queryLog'});
				}else{
					tabObj.itemClick({id :'queryData'});
				}

				if(gridViewFlag){
					_this.setGridData(conuid, resultInfo);
				}

				var msgAreaEle = $(tabObj.getTabContentSelector('queryLog'));

				msgAreaEle.prepend(resultMsg.join(''));
				msgAreaEle.animate({scrollTop: 0},'fast');
			}
			,setGridData : function (conuid, pGridData){
				this.allResultData[conuid] = pGridData;
				
				$.pubGrid(getResultTabSelector(conuid, 'grid'),{
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
		        		drag:{
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
					,navigation : {
						status :true
						,height :20
						,enableSelectionInfo : true
					}
					,tColItem : pGridData.column
					,tbodyItem :pGridData.data
				});
			}
			,addResultItem: function (dbItem){
				componentCtrl.addComponent(dbItem);
			}
			,addParam: function (){
				this.paramList.push({key:'',value:''})
			}
			,removeParam: function (idx){
				this.paramList.splice(idx,1);
			}
		}
	});
}

//sql editor z-index 변경. 
function sqlEditorStyleChange(sqlContainer){
	sqlContainer = sqlContainer.closest('.lm_item.lm_stack');
	sqlContainer.find('>.lm_header').css('z-index',2);
	sqlContainer.find('>.lm_items').css('z-index',2);
}

function setLayout(){
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
			minimise: 'minimise'
		},
		content: []
	};

	config.content =[{
		type: 'column',
		content: [
			{
				type:'component',
				componentName: 'Sql',
				height :30,
				componentState: { text: 'Component 1' }
			},
			{
				type: 'row'
				,isClosable:false
				,content:[]
			}
		]
	}];
	
	if(varsqlLayout){
		varsqlLayout.destroy();
		$('#layoutArea').empty()
	}
	
	try{
		if(!VARSQL.isUndefined(componentCtrl.preferencesData.layout) && Object.keys(componentCtrl.preferencesData.layout).length > 0){
			varsqlLayout = new GoldenLayout( componentCtrl.preferencesData.layout, $('#layoutArea'));
		}
	}catch(e){
		console.log(e);
	}
	
	if(VARSQL.isUndefined(varsqlLayout)){
		varsqlLayout = new GoldenLayout( config, $('#layoutArea'));
	}
	
	varsqlLayout.registerComponent( 'Sql', function( container, componentState ){
		container.getElement().html($('#dbSqlEditorTemplate').html());
	});

	varsqlLayout.registerComponent( 'Result', function( container, componentState ){
		container.getElement().html('<div data-result-key="'+componentState.key+'" class="wh100"></div>');
		
		var initResize = true;
		container.on('resize',function() {
			if(initResize === true){
				initResize = false;
				return ;
			}
			componentCtrl.resize(componentState.key, {width : container.width,height : container.height});
		});
	});
	
	// component create
	varsqlLayout.on('componentCreated', function( component ){
		var componentName = component.componentName; 
		if(componentName == 'Sql'){
			component.container.tab.closeElement.remove();
			initVue();
		}else if(componentName == 'Result'){
			var componentState = component.config.componentState;
			
			if(componentState.key == componentCtrl.conuid){
				component.container.tab.closeElement.remove();	
			}
			
			if(component.tab && component.tab.isActive){
				componentCtrl.initAddComponent(componentState)
			}
		}
	});

	varsqlLayout.on('initialised', function( contentItem ){
		var firstFlag = true;
		var layoutSaveTimer;

		var layoutSaveTimer;
		var firstFlag = true;

		varsqlLayout.on( 'stateChanged', function(a1){
			if(firstFlag){
				firstFlag = false;
				return ;
			}
			clearTimeout(layoutSaveTimer);
			
			if(!a1 || varsqlLayout._maximisedItem) return ;

			layoutSaveTimer = setTimeout(function() {
				componentCtrl.saveChangeData({layout : varsqlLayout.toConfig()});
			}, 300);
		});
    });
	
	// item destroy
	varsqlLayout.on('itemDestroyed', function( component ){
		if(component.type=='component'){
			var componentInfo = component.config.componentState;

			componentCtrl.destroyed(componentInfo.key, componentInfo);
		}
	})
	
	varsqlLayout.on('stackCreated', function( stack ){
		var items = stack.contentItems;
		for(var i =0 ;i < items.length; i++){
			items[i].config.componentState.initFlag = false;
		}
	});
	
	varsqlLayout.on('activeContentItemChanged', function( contentItem ){

    	var componentName = contentItem.componentName;
    	var componentState = contentItem.config.componentState;
    	
    	if(componentState.initFlag !== true && contentItem.isInitialised ===true){
    		componentCtrl.initAddComponent(componentState);
    	}else{
    		componentCtrl.resize(componentState.key);
    	}
    	
    	if(contentItem.config.componentName=='Sql'){
			sqlEditorStyleChange($(contentItem.element));
		}
    });

	varsqlLayout.init();

	var windowResizeTimer;
	$(window).resize(function() {
		
		clearTimeout(windowResizeTimer);
		windowResizeTimer = setTimeout(function() {
			varsqlLayout.updateSize();
		}, 20);
	})
}

setLayout()
}());


</script>
