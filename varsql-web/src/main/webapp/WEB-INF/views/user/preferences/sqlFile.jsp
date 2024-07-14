<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/include/tagLib.jspf"%>

<div class="row">
    <div class="col-lg-12">
        <h1 class="page-header"><spring:message code="user.prefernces.menu.sqlfile" /></h1>
    </div>
    <!-- /.col-lg-12 -->
</div>
<div class="row display-off" id="varsqlVueArea">
	<div class="col-xs-5">
		<div class="panel panel-default">
			<!-- /.panel-heading -->
			<div class="panel-body">
				<div class="row search-area">
					<div class="col-sm-4">
						<label>
							<button @click="deleteInfo()" type="button" class="btn btn-xs btn-danger"><spring:message code="delete" /></button>
						</label>
					</div>
					<div class="col-sm-8">
						<div class="dataTables_filter">
							<label style="float:left; margin-right: 5px;">
								<select v-model="vconnid" class="form-control ">
									<option value="ALL"><spring:message code="all"/></option>
									<c:forEach items="${dblist}" var="tmpInfo" varStatus="status">
										<option value="${tmpInfo.vconnid}" vname="${tmpInfo.name}">${tmpInfo.name}</option>
									</c:forEach>
								</select>
							</label>
							<div class="input-group floatright">
								<input type="text" value="" v-model="searchVal" class="form-control" @keydown.enter="search()" placeholder="<spring:message code="search.placeholder" />">
								<span class="input-group-btn">
									<button class="btn btn-default searchBtn" type="button" @click="search()"> <span class="glyphicon glyphicon-search"></span></button>
								</span>
							</div>
						</div>
					</div>
				</div>


				<div class="table-responsive">
					<div id="dataTables-example_wrapper"
						class="dataTables_wrapper form-inline" role="grid">
						<table
							class="table table-striped table-bordered table-hover dataTable no-footer"
							id="dataTables-example">
							<thead>
								<tr role="row">
									<th  style="width: 10px;"><div
											class="text-center">
											<input type="checkbox" :checked="selectAllCheck" @click="selectAll()" />
										</div>
									</th>
									<th style="width: 195px;">
										<spring:message	code="sql.file.name" />
									</th>
									<th style="width: 120px;">
										<spring:message	code="database" />
									</th>
									<th style="width: 170px;">
										<spring:message	code="reg_dt" />
									</th>
								</tr>
							</thead>
							<tbody class="dataTableContent">
								<tr v-for="(item,index) in gridData" class="gradeA" :class="(index%2==0?'add':'even')">
									<td><input type="checkbox" :value="item.sqlId" v-model="selectItem"></td>
									<td><a href="javascript:;" @click="detail(item)">{{item.sqlTitle}}</a></td>
									<td class="center">{{item.vname}}</td>
									<td class="center">{{item.regDt}}</td>
								</tr>
								<tr v-if="gridData.length === 0">
									<td colspan="10"><div class="text-center"><spring:message code="msg.nodata"/></div></td>
								</tr>
							</tbody>
						</table>

						<page-navigation :page-info="pageInfo" callback="search"></page-navigation>
					</div>
				</div>
			</div>
			<!-- /.panel-body -->
		</div>
		<!-- /.panel -->
	</div>
	<!-- /.col-lg-4 -->
	<div class="col-xs-7" >
		<div class="panel panel-default detail_area_wrapper" >
			<div class="panel-heading"><spring:message code="detail.view" /></div>
			<!-- /.panel-heading -->
			<div class="panel-body">
				<form id="addForm" name="addForm" class="form-horizontal" onsubmit="return false;">
					<div class="form-group">
						<div class="col-xs-12"><label class="control-label"><spring:message code="sql.file.name" /></label></div>

						<div class="col-xs-12">
							{{detailItem.sqlTitle}}&nbsp;
						</div>
					</div>
					<div class="form-group">
						<div class="col-xs-12"><label class="control-label"><spring:message code="content" /></label></div>
						<div class="col-xs-12">
							<div id="sqlFileViewer" class="sql-code-editor" style="height:500px;"></div>
						</div>
					</div>
				</form>
			</div>
			<!-- /.panel-body -->
		</div>
		<!-- /.panel -->
	</div>
	<!-- /.col-lg-8 -->
</div>

<varsql:importResources resoures="codeEditor"/>

<script>
VarsqlAPP.vueServiceBean({
	el: '#varsqlVueArea'
	,validateCheck : true
	,data: {
		searchVal : ''
		,selectItem :[]
		,pageInfo : {}
		,gridData :  []
		,detailItem : {}
		,vconnid : 'ALL'
		,isDetailFlag :false
		,fileViewEditor : {}
	}
	,computed :{
		selectAllCheck : function (){
			return this.gridData.length > 0 && this.gridData.length == this.selectItem.length;
		}
	}
	,methods:{
		init : function() {

			$(this.$el).removeClass('display-off');
			
			this.fileViewEditor = new codeEditor(document.getElementById('sqlFileViewer'), {
				schema: '',
				editorOptions: { 
					theme: 'vs-light'
					,minimap: {enabled: true} 
					,readOnly: true
					,contextmenu :false
				}
			})
			
		}
		,selectAll : function (){
			if(this.selectAllCheck){
				this.selectItem = [];
			}else{
				this.selectItem = [];

				for(var i =0 ;i <this.gridData.length; i++){
					this.selectItem.push(this.gridData[i].sqlId)
				}
			}
		}
		,search : function(no){
			var _self = this;

			var param = {
				pageNo: (no?no:1)
				,rows: _self.list_count
				,'searchVal':_self.searchVal
				,vconnid : _self.vconnid
			};

			this.$ajax({
				url :{type:VARSQL.uri.user, url:'/preferences/sqlFile/list'}
				,data : param
				,success: function(resData) {
					_self.gridData = resData.list;
					_self.pageInfo = resData.page;
				}
			})
		}
		,detail : function (item){
			var _this = this;

			_this.isDetailFlag = true;
			this.$ajax({
				url : {type:VARSQL.uri.user, url:'/preferences/sqlFile/detail'}
				,loadSelector : '#main-content'
				,data : {
					sqlId : item.sqlId
				}
				,success:function (resData){
					_this.detailItem = resData.item;
					_this.fileViewEditor.setValue(_this.detailItem.sqlCont||'');
				}
			});
		}
		//delete
		,deleteInfo : function(){
			var _this = this;

			var item = _this.detailItem;

			var selectItem = _this.selectItem;

			if(VARSQL.isDataEmpty(selectItem)){
				VARSQL.alertMessage('msg.item.select');
				return ;
			}

			if(!VARSQL.confirmMessage('msg.delete.confirm')){
				return ;
			}

			this.$ajax({
				url : {type:VARSQL.uri.user, url:'/preferences/sqlFile/delete'}
				,data : {
					selectItem : selectItem.join(',')
				}
				,success:function (resData){
					VARSQL.toastMessage('msg.delete.success');
					_this.search();
				}
			});
		}
	}
});
</script>
