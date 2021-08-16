<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/include/tagLib.jspf"%>
<!-- Page Heading -->
<div class="row page-header-area">
    <div class="col-lg-12">
        <h1 class="page-header"><spring:message code="header.menu.tool.utils.genexceltoddl" /></h1>
    </div>
    <!-- /.col-lg-12 -->
</div>
<div class="row display-off page-content" id="varsqlVueArea">
	<div class="col-xs-4 h100">
		<div class="panel panel-default">
			<div class="panel-heading">
				<div class="field-group" style="height: 42px;">
					<label class="col-xs-2 control-label"><spring:message code="manage.glossary.word" /></label>
					<div class="col-xs-10">
						<input class="form-control text required input-md" v-model="glossarySearchTxt" @keydown.enter="searchGlossary()" style="margin:0px 5px 0px 5px;width: calc(100% - 120px);" placeholder="<spring:message code="manage.glossary.word" />">
						<button @click="searchGlossary()" class="varsql-btn-info btn-md" style="padding: 3px 10px;width: 100px;vertical-align: top;"><spring:message code="btn.search" /></button>
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
	<div class="col-xs-5 h100">
		<div class="panel panel-default">
			<div class="panel-heading">
				<div class="field-group" style="height: 42px;">
					<div class="col-xs-12">
						<input class="form-control text required input-md" v-model="tableName" style="margin:0px 5px 0px 5px;width: calc(100% - 228px);" placeholder="Table Name">
						<button @click="addRow()" class="varsql-btn-info btn-md" style="padding: 3px 10px;width: 50px;vertical-align: top;">Add</button>
						<button @click="removeRow()" class="varsql-btn-info btn-md" style="padding: 3px 10px;width: 50px;vertical-align: top;">Del</button>
						<button @click="convertDDL()" class="varsql-btn-info btn-md" style="padding: 3px 10px;width: 100px;vertical-align: top;">Excel ->  DDL</button>
					</div>

				</div>
			</div>
			<!-- /.panel-heading -->
			<div class="panel-body">
				<div id="tableInfo" class="wh100"></div>
			</div>
			<!-- /.panel-body -->
		</div>
		<!-- /.panel -->
	</div>
	<div class="col-xs-3 h100" >
		<div class="panel panel-default detail_area_wrapper" >
			<div class="panel-heading">DDL</div>
			<!-- /.panel-heading -->
			<div class="panel-body">
				<textarea class="table-ddl wh100" id="tableDDLInfo"></textarea>
			</div>
			<!-- /.panel-body -->
		</div>
		<!-- /.panel -->
	</div>
	<!-- /.col-lg-8 -->
</div>
<!-- /.row -->

<varsql:editorResource editorHeight="100%"/>

<script>
VARSQLCont.init('${dbtype}' , {});

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
	}
	,methods:{
		init : function(){
			var _this =this;

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
	        		,COLUMN_SIZE : (sItem.wordLength ||'')
	        		,NULLABLE : ''
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
			var schVal = $.trim(_this.glossarySearchTxt);

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
		        	pasteAfter : function (e){
						_this.convertDDL();
		        	}
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
		        	, {label: '데이터타입', key: 'DATA_TYPE', width: 80, editor :{
		        		type : 'select'
		        		,itemKey : {code : 'name', label : 'name'}
			        	,items : VARSQLCont.allDataType()
					}}
		        	, {label: '길이', key: 'COLUMN_SIZE'}
		        	, {label: 'Nullable', key: 'NULLABLE',editor :{
		        		type : 'select'
		        		,items :[{CODE : 'Y' , LABEL :'Y'}, {CODE : 'N' , LABEL :'N'}]
		        	}}
		        	, {label: '기본값', key: 'COLUMN_DEF', width: 45}
		        	, {label: '설명', key: 'COMMENT', width: 45}
		        ]
		        , tbodyItem: tbodyItems
		    });
		}
		,addRow : function(){
			this.tableGridObj.addRow({
        		COLUMN_NAME : ''
        		,DATA_TYPE : 'VARCHAR'
        		,COLUMN_SIZE : ''
        		,NULLABLE : ''
        		,COLUMN_DEF : ''
        		,COMMENT : ''
        	});
		}
		,removeRow : function(){
			this.tableGridObj.removeSelectionRow()
		}
		,convertDDL : function(){
			var tableInfoObj = this.tableGridObj;
	        var items = tableInfoObj.options.tbodyItem;
	        var tableName = $.trim(this.tableName);

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
				,"objectName" : tableName
				,"keyList":keyList
				,"columnList": columnList
				,"commentsList": commentList
				,"ddlOpt":{"addDropClause":true,"addLastSemicolon":true}
			};

			var obj = Handlebars.compile(this.ddlTemplate);
	        this.tableDDLEditor.setValue(obj(param))
		}
	}
});
</script>
