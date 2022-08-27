<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/include/tagLib.jspf"%>
<style>
.db-util-body .page-content .panel .panel-heading{
	height :35px;
}

.db-util-body .page-content .panel .panel-body {
    height: calc(100% - 40px);
}
.field-group{
	padding: 7px 0px;
}
</style>
<!-- Page Heading -->
<div class="row page-header-area">
    <div class="col-lg-12">
        <h1 class="page-header"><spring:message code="header.menu.tool.utils.gentable" /></h1>
    </div>
    <!-- /.col-lg-12 -->
</div>

<div class="row display-off page-content" id="varsqlVueArea">
	<div class="wh100">
		<div class="col-xs-5 h100">
			<div class="panel panel-default">
				<div class="panel-heading">
					<div class="field-group" style="height: 42px;">
						<label class="col-xs-2 control-label"><spring:message code="manager.glossary.word" /></label>
						<div class="col-xs-10">
							<input class="form-control text required input-md" v-model="glossarySearchTxt" @keydown.enter="searchGlossary()" style="margin:0px 5px 0px 5px;width: calc(100% - 120px);" placeholder="<spring:message code="manager.glossary.word" />">
							<button @click="searchGlossary()" class="varsql-btn-default  btn-md" style="padding: 3px 10px;width: 100px;vertical-align: top;"><spring:message code="btn.search" /></button>
						</div>
	
					</div>
				</div>
				<!-- /.panel-heading -->
				<div class="panel-body">
					<div id="glossaryInfo" class="wh100"></div>
				</div>
				<!-- /.panel-body -->
			</div>
			<!-- /.panel -->
		</div>
		<div class="splitter" data-orientation="vertical"></div>
		<div class="col-xs-7 h100">
			<div class="panel panel-default"  style="height: calc(100% - 125px);">
				<div class="panel-heading">
					<div class="field-group" style="height: 42px;">
						<div class="col-xs-12">
							<input class="form-control text required input-md" v-model="tableName" style="margin:0px 5px 0px 5px;width: calc(100% - 123px);" placeholder="Table Name">
							<button @click="addRow()" class="varsql-btn-default  btn-md" style="padding: 3px 10px;width: 50px;vertical-align: top;">Add</button>
							<button @click="removeRow()" class="varsql-btn-default  btn-md" style="padding: 3px 10px;width: 50px;vertical-align: top;">Del</button>
						</div>
					</div>
				</div>
				<!-- /.panel-heading -->
				<div class="panel-body"  style="height: calc(100% - 35px); padding-bottom:5px;">
					<div id="tableInfo" class="wh100"></div>
				</div>
			</div>
			
			<div class="splitter" data-orientation="horizontal" data-prev-min-size="20" data-next-min-size="20"></div>
			<div class="panel-footer" style="height: 125px;">
				<div class="pull-right">
					<button type="button" @click="ddlView()">DDL View</button>
					<button type="button" @click="createTable()">Create</button>
				</div>
				<div class="log-area clearboth" style="padding-top: 5px;height:calc(100% - 25px)">
					<pre class="wh100" style="overflow:auto;">{{resultMessage}}</pre>
				</div>
			</div>
		</div>
	</div>
	
	<div id="ddlDialog" title="DDL View" class="clearboth" style="display:none;">
		<div style="height:350px">
			<textarea class="wh100" id="tableDDLInfo"></textarea>
		</div>
	</div>
</div>

<varsql:editorResource editorHeight="100%"/>

<script>
VARSQLCont.init('${dbtype}');

VarsqlAPP.vueServiceBean( {
	el: '#varsqlVueArea'
	,validateCheck : true
	,data: {
		list_count :10
		,detailFlag :false
		,glossarySearchTxt : ''
		,pageInfo : {}
		,gridData :  []
		,detailItem :{}
		,viewMode : 'view'
		,driverList : []
		,tableName : ''
		,glossaryGridObj : {}
		,tableGridObj : {}
		,tableDDLEditor:{}
		,ddlTemplate :''
		,dbType : '${dbtype}'
		,schema : '${schema}'
		,resultMessage : ''
		,ddlDialog : ''
		,addColumnIdx: 1
	}
	,methods:{
		init : function(){
			var _this =this;
						
			$.pubSplitter('.splitter',{
				handleSize : 5
				,button : {
					enabled : false
				}
				,minSize: {
					size : 5
				}
				,stop: function (splitterEle, splitterConf, moveData){
					_this.glossaryGridObj.resizeDraw();
					_this.tableGridObj.resizeDraw();
				}
			});
			
			_this.ddlDialog = VARSQLUI.dialog.open('#ddlDialog', {
				height: 400
				,width: 450
				,modal: true
				,autoOpen :false
				,close: function() {
					_this.ddlDialog.dialog( "close" );
				}
			});
			
			_this.initGlossary();
			_this.initExcelTableInfo();

			VARSQLApi.sqlTemplate.load({dbType : this.dbType, conuid :'${conuid}', 'templateType' : 'TABLE'}, function(resData){
				_this.ddlTemplate = resData.item;
			});

			this.tableDDLEditor = CodeMirror.fromTextArea(document.getElementById('tableDDLInfo'), {
				mode: 'text/x-sql'
			})
		}
		,initGlossary : function (){
			var _this = this;

			function addColumnItem (sItem, colName){
				var typeAndLength ='';
				var wordType = sItem.wordType ||'VARCHAR';

				colName = colName.split(' ').join('_');

	        	_this.tableGridObj.addRow({
	        		COLUMN_NAME : colName
	        		,DATA_TYPE : wordType
	        		,COLUMN_SIZE : (sItem.wordLength ||255)
	        		,NULLABLE : 'Y'
	        		,COLUMN_DEF : ''
	        		,COMMENT : sItem.word
	        	});
				return ;
			}

			_this.glossaryGridObj = $.pubGrid('#glossaryInfo', {
				asideOptions :{lineNumber : {enabled : false	,width : 30}}
				,tColItem : [
					{ label: 'WORD', key: 'word',width:80 },
					{ label: 'EN', key: 'wordEn' },
					{ label: 'ABBR', key: 'wordAbbr', width:45},
					{ label: 'TYPE', key: 'wordType',width:45},
					{ label: 'DESC', key: 'wordDesc',width:45},
				]
				,tbodyItem :[]
				,rowOptions :{
					contextMenu :{
						callback: function(key,sObj) {
							var sItem = this.gridItem;

							if(key =='addItem'){
								addColumnItem(sItem, sItem['wordAbbr']);
							}
						},
						items: [
							{key : "addItem", "name": "추가"	}
						]
					}
				}
				,bodyOptions : {
					cellDblClick : function (cellInfo){

						var selKey =cellInfo.keyItem.key;

			        	var sItem = cellInfo.item;

			        	var colName = sItem['wordAbbr'];

			        	if(selKey == 'wordEn'){
			        		colName = sItem['wordEn'];
			        	}
			        	addColumnItem (sItem, colName);
					}
				}
			});
		}
		,searchGlossary :  function (){
			var _this = this;
			var schVal = VARSQL.str.trim(_this.glossarySearchTxt);

			if(schVal.length < 1){
				return ;
			}

			var params ={
				searchVal : schVal
			}

			VARSQL.req.ajax({
			    loadSelector : '#glossaryInfo'
			    ,url:{type:VARSQL.uri.plugin, url:'/glossary/search'}
			    ,data : params
			    ,success:function (res){
			    	_this.glossaryGridObj.setData(res.items,'reDraw');
				}
			});
		}
		,initExcelTableInfo : function(){
			var _this =this;
			var tbodyItems = [];

		    _this.tableGridObj = $.pubGrid('#tableInfo', {
		        headerOptions: {
		            redraw: false
		        }
		        ,selectionMode : 'multiple-row'
		        , asideOptions: {
		            lineNumber: {
		                enabled: true,
		                width: 30
		            },
		            rowSelector: {
		                enabled: true,
		                key: 'checkbox',
		                name: 'Key',
		                width: 25
		            }
		        }
		        ,rowOptions :{
		        	height : 30
		        	,contextMenu :{
						callback: function(key,sObj) {
							var sItem = this.gridItem;

							if(key =='selectionDelete'){
								_this.tableGridObj.removeSelectionRow();
								return ;
							}
						},
						items: [
							{key : "selectionDelete", "name": "선택 삭제"	}
						]
					}
		        }
		        ,editable: true
		        , tColItem: [
		        	{label: '컬럼명', key: 'COLUMN_NAME', width: 80}
		        	, {label: '데이터타입', key: 'DATA_TYPE', width: 80, renderer :{
		        		type : 'dropdown'
	        			,labelField : 'name'
	        			,valueField : 'name'
			        	,list : VARSQLCont.allDataType()
					}}
		        	, {label: '길이', key: 'COLUMN_SIZE'}
		        	, {label: 'Nullable', key: 'NULLABLE', renderer :{
		        		type : 'dropdown'
		        		,list : [
							'Y'
							,'N'
						]
		        	}}
		        	, {label: '기본값', key: 'COLUMN_DEF', width: 45}
		        	, {label: '설명', key: 'COMMENT', width: 45}
		        ]
		        , tbodyItem: tbodyItems
		    });
		}
		,addRow : function(){
			this.tableGridObj.addRow({
        		COLUMN_NAME : 'COLUMN_'+(this.addColumnIdx++)
        		,DATA_TYPE : 'VARCHAR'
        		,COLUMN_SIZE : 255
        		,NULLABLE : 'Y'
        		,COLUMN_DEF : ''
        		,COMMENT : ''
        	});
		}
		,removeRow : function(){
			this.tableGridObj.removeSelectionRow()
		}
		,generateDDL : function(){
			var tableInfoObj = this.tableGridObj;
	        var items = tableInfoObj.options.tbodyItem;
	        var tableName = VARSQL.str.trim(this.tableName);

	        var columnList = [];
	        var commentList = [];
	        var keyList = [];
	        for(var i =0; i<items.length; i++){
	        	var item = items[i];

	        	var itemName = item.COLUMN_NAME || '';

	            if (itemName == '') continue;

	            columnList.push(item);

	            if (item._pubcheckbox === true) { // primary key check
	            	keyList.push({"COLUMN_NAME":itemName, "CONSTRAINT_NAME": "pk_"+tableName, TYPE:"PK"})
	            }

	        	if(!VARSQL.isBlank(item.COMMENT)){
	        		commentList.push({TYPE: 'COL', NAME: item.COLUMN_NAME, COMMENT: item.COMMENT});
				}
	        }

			var param = {
				"dbType" : this.dbType
				,"schema" : this.schema
				,"objectName" : tableName
				,"keyList":keyList
				,"columnList": columnList
				,"commentsList": commentList
				,"ddlOpt":{"addDropClause":true,"addLastSemicolon":true}
			};
			
			if(VARSQL.isBlank(tableName)){
				VARSQLUI.toast.open({text :'테이블명을 입력해주세요.'})
				return false; 
			}
			
			if(columnList.length < 1){
				VARSQLUI.toast.open({text :'컬럼 정보를 입력해주세요.'})
				return false; 
			}

			var obj = Handlebars.compile(this.ddlTemplate);
			var ddl = obj(param); 
			
	        return ddl;
		}
		,ddlView : function (){
			var ddl = this.generateDDL();
			
			if(ddl==false) return ; 
			
			this.ddlDialog.dialog('open');
			
			this.tableDDLEditor.setValue(ddl);
		}
		,createTable: function (){
			var ddl = this.generateDDL();
			
			if(ddl==false) return ; 
			
			if(!confirm('테이블을 생성하시겠습니까?')){
				return ;
			}
			var _this =this;
			
			VARSQLApi.sql.execute({sql : ddl}, function (resData){
				if(resData.resultCode==200){
					_this.resultMessage = 'Success'; 
				}else{
					_this.resultMessage = VARSQL.str.trim(ddl) + '\n' + resData.message;
				}
			})
		}
	}
});
</script>
