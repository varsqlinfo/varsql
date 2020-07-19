<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/include/tagLib.jspf"%>
<!-- Page Heading -->
<div class="row">
    <div class="col-lg-12">
        <h1 class="page-header"><spring:message code="header.menu.tool.utils.genexceltoddl" /></h1>
    </div>
    <!-- /.col-lg-12 -->
</div>
<div class="row display-off" id="varsqlVueArea">
	<div class="col-xs-7">
		<div class="panel panel-default">
			<div class="panel-heading">
				<div class="field-group" style="height: 42px;">
					<label class="col-xs-2 control-label">Table Name : </label>
					<div class="col-xs-7" style="padding:0px 5px 0px 5px;">
						<input class="form-control text required input-md" name="tableName" id="tableName" placeholder="Table Name">
					</div>

					<label class="col-xs-3 control-label"><button @click="convertDDL()" class="varsql-btn-info btn-md w100">Excel ->  DDL</button> </label>
				</div>
			</div>
			<!-- /.panel-heading -->
			<div class="panel-body">
				<div id="tableInfo"></div>
			</div>
			<!-- /.panel-body -->
		</div>
		<!-- /.panel -->
	</div>
	<!-- /.col-lg-4 -->
	<div class="col-xs-5" >
		<div class="panel panel-default detail_area_wrapper" >
			<div class="panel-heading"><spring:message code="admin.form.header" /></div>
			<!-- /.panel-heading -->
			<div class="panel-body">
				<textarea style="height:375px" class="table-ddl" id="tableDDLInfo"></textarea>
			</div>
			<!-- /.panel-body -->
		</div>
		<!-- /.panel -->
	</div>
	<!-- /.col-lg-8 -->
</div>
<!-- /.row -->

<script>
VarsqlAPP.vueServiceBean( {
	el: '#varsqlVueArea'
	,validateCheck : true
	,data: {
		list_count :10
		,detailFlag :false
		,searchVal : ''
		,pageInfo : {}
		,gridData :  []
		,detailItem :{}
		,viewMode : 'view'
		,driverList : []
	}
	,methods:{
		init : function(){
			var _this =this;

			var tbodyItems = [];
		    for (var i = 0; i < 50; i++) {
		        tbodyItems.push({});
		    }
		    $.pubGrid('#tableInfo', {
		        headerOptions: {
		            redraw: false
		        }
		        , height: 350
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
		,convertDDL : function(){
			var tableInfoObj = $.pubGrid('#tableInfo');
	        var items = tableInfoObj.options.tbodyItem;
	        var ddlStr = [];
	        var keyCols = [];
	        var firstFlag = true;
	        var tableName = $.trim($('#tableName').val());
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
	        $('#tableDDLInfo').val(ddlStr.join(''));
		}
	}
});
</script>
