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
VARSQLCont.init('other' , {});

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
	}
	,methods:{
		init : function(){
			var _this =this;
			
			_this.initGlossary();
			_this.initExcelTableInfo();
			
			this.tableDDLEditor = CodeMirror.fromTextArea(document.getElementById('tableDDLInfo'), {
				mode: 'text/x-sql'
			})
		}
		,initGlossary : function (){
			var _this = this;

			function addColumnItem (sItem, colName){
				var typeAndLength ='';
				var wordType = sItem.wordType;
				if(wordType){
					var typeInfo = VARSQLCont.dataType.getDataTypeInfo(wordType);
					var typeAndLength = wordType;
		        	if(typeInfo.isDate){

		        	}else{
						if(sItem.wordLength){
							typeAndLength = typeAndLength + '('+sItem.wordLength+')';
						}
		        	}
				}

				colName = colName.split(' ').join('_');

	        	_this.tableGridObj.addRow({
	        		name : colName
	        		,typeAndLength : typeAndLength
	        		,nullable : ''
	        		,defaultVal : ''
	        		,comment : sItem['word']
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
		        , setting: {
		            enabled: true,
		            enableSearch: true
		        }
		        , tColItem: [
		        	{label: '컬럼명', key: 'name', width: 80}
		        	, {label: '데이터타입', key: 'typeAndLength'}
		        	, {label: 'Nullable', key: 'nullable'}
		        	, {label: '기본값', key: 'defaultVal', width: 45}
		        	, {label: '설명', key: 'comment', width: 45}
		        ]
		        , tbodyItem: tbodyItems
		    });
		}
		,addRow : function(){
			this.tableGridObj.addRow({
        		name : ''
        		,typeAndLength : ''
        		,nullable : ''
        		,defaultVal : ''
        		,comment : ''
        	});
		}
		,removeRow : function(){
			this.tableGridObj.removeSelectionRow()
		}
		,convertDDL : function(){
			var tableInfoObj = this.tableGridObj;
	        var items = tableInfoObj.options.tbodyItem;
	        var ddlStr = [];
	        var keyCols = [];
	        var firstFlag = true;
	        var tableName = $.trim(this.tableName);
	        ddlStr.push('CREATE TABLE ' + tableName + ' (\n');
	        for (var i = 0; i < items.length; i++) {
	            var item = items[i];
	            var itemName = item.name || '';

	            if (itemName == '') continue;

	            if (item._pubcheckbox === true) { // primary key check
	                keyCols.push(itemName)
	            }

	            //console.log(item);
	            var typeAndLength = item.typeAndLength || '';
	            var defVal = item.defaultVal || '';
	            ddlStr.push('\t' + (firstFlag ? "" : ", ") + itemName);
	            ddlStr.push(' ' + typeAndLength);

	            if (defVal && typeAndLength.toUpperCase().indexOf('CHAR') > -1) {
	                defVal = "'" + defVal + "'";
	            }

	            ddlStr.push(defVal ? (' DEFAULT ' + defVal) : '');
	            ddlStr.push(item.nullable == 'N' ? ' NOT NULL ' : '');
	            ddlStr.push('\n');

	            firstFlag = false;
	        }
	        if (keyCols.length > 0) {
	            ddlStr.push('\t, CONSTRAINT ' + tableName + '_pk' + ' PRIMARY KEY (' + keyCols.join(',') + ')');
	        }
	        ddlStr.push('\n);');
	        
	        this.tableDDLEditor.setValue(ddlStr.join(''))
		}
	}
});
</script>
