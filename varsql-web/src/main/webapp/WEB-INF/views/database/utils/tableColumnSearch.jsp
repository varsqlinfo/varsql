<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/include/tagLib.jspf"%>

<div id="utilMain" class="wh100">
	<div class="menu-tools-body col-xs-12 scroll-y">

		<c:if test="${schemaInfo ne ''}">
			<div style="padding: 5px 0px 0px;">
				<span class="control-label" style="width:100px;font-weight: bold;margin-right:5px;"><spring:message code="db.schema" /> : </span>
				<select v-model="selectSchema" @change="loadColumns()" style="width: calc(100% - 100px);">
					<c:forEach var="item" items="${schemaList}" begin="0" varStatus="status">
						<option value="${item}">${item}</option>
					</c:forEach>
				</select>
			</div>
		</c:if>
		<div id="allColumns" style="height:330px;margin-top:10px;"></div>
		
		<div style="margin-top:10px;text-align:center;">
			<button type="button" class="btn-md" @click="download()"><spring:message code="download" /></button>
		</div>
	</div>
</div>

<script>
VarsqlAPP.vueServiceBean({
	el: '#utilMain'
	,data: {
		selectSchema : ''
		,conuid : '<c:out value="${param.conuid}" escapeXml="true"></c:out>'
		,columnGrid : null
	}
	,methods:{
		init : function (){
			this.selectSchema = '${currentSchemaName}';
			this.initGrid();
			this.loadColumns();
		}
		,initGrid:function (){
			const viewObj = $.pubGrid('#allColumns',{
				asideOptions :{
					lineNumber : {enabled : true, width : 30, align: 'right'}
				}
				,tColItem : [
					{ label: VARSQL.message('column'), key: 'name',width:80 },
					{ label: VARSQL.message('table'), key: 'tableName' , width:80},
					{ label: VARSQL.message('data.type'), key: 'typeAndLength',width:120},
					{ label: VARSQL.message('nullable'), key: 'nullable',width:20},
					{ label: VARSQL.message('constraints.key'), key: 'constraints',width:20},
					{ label: VARSQL.message('desc'), key: 'comment',width:45}]
				,tbodyItem : []
				,toolbar:{
					enabled : true
					,items:[
						{
							search :true,
						}
					]
				}
				,navigation: {
					enablePaging : false
					,status : true
					,height :26
				}
				,rowOptions : {
					contextMenu :{
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
						},
						items: [
							{key : "copy" , "name": "Copy", hotkey :'Ctrl+C'}
						]
					}
				}
			});
			
			this.columnGrid = viewObj;
		}
		,loadColumns: function (){
			const _self = this;
			
			VARSQL.req.ajax({
				url:{type:VARSQL.uri.database, url:'/dbObjectList'}
				,loadSelector : 'body'
				,data : {
					conuid: this.conuid
					,objectType: 'table'
					,schema: this.selectSchema
				}
				,success:function (resData){
					
					const allColumns =[];
					
					for(let tblItem of resData.list){
						var tblName =tblItem.name;
						
						for(let colItem of tblItem.colList){
							colItem.tableName = tblName;
							allColumns.push(colItem)
						}
					}
					_self.columnGrid.setData(allColumns);
					
				}
			});
		}
		,download : function (){
			var selData = this.columnGrid.getData({dataType:'json'});

			var params = {
				conuid: this.conuid
				,schema: this.selectSchema
				,fileName :'TableColumnReport'
				,exportType :'excel'
				,headerInfo : JSON.stringify(selData.header)
				,gridData : JSON.stringify(selData.data)
			}

			VARSQL.req.download({
				type: 'post'
				,url: {type:VARSQL.uri.sql, url:'/base/gridDownload'}
				,params: params
			});
		}
	}
});
</script>



