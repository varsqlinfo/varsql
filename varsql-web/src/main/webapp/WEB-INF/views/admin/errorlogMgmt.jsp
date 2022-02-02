<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/include/tagLib.jspf"%>
<!-- Page Heading -->
<div class="row">
    <div class="col-lg-12">
        <h1 class="page-header"><spring:message code="admin.menu.errorlog" /></h1>
    </div>
    <!-- /.col-lg-12 -->
</div>
<div class="row display-off" id="epViewArea">
	<div class="col-lg-5">
		<div class="panel panel-default">
			<!-- /.panel-heading -->
			<div class="panel-body">
				<div class="pull-right search-area" style="width:350px;">
					<div class="dataTables_filter">
						<label style="float:left; margin-right: 5px;">
							<select v-model="list_count" @change="search()" class="form-control ">
								<option	value="10">10</option>
								<option value="25">25</option>
								<option value="50">50</option>
								<option value="100">100</option>
							</select>
						</label>
						<label style="float:left; margin-right: 5px;">
							<select v-model="searchCatg" class="form-control ">
								<option	value="title"><spring:message code="admin.errorlog.title" /></option>
								<option value="cont"><spring:message code="admin.errorlog.cont" /></option>
								<option value="type"><spring:message code="admin.errorlog.type" /></option>
								<option	value="server"><spring:message code="admin.errorlog.server" /></option>
							</select>
						</label>
						<div class="input-group floatright">
							<input type="text" v-model="searchVal" class="form-control " @keyup.enter="search()" autofocus="autofocus" placeholder="Search...">
							<span class="input-group-btn">
								<button class="btn btn-default" @click="search()" type="button">
									<span class="glyphicon glyphicon-search"></span>
								</button>
							</span>
						</div>
					</div>
				</div>
				<div class="table-responsive">
					<div id="dataTables-example_wrapper"
						class="dataTables_wrapper form-inline" role="grid">
						<table
							class="table table-striped table-bordered table-hover dataTable no-footer"
							id="dataTables-example" style="table-layout:fixed;">
							<colgroup>
								<col style="width:*;">
								<col style="width:80px;">
								<col style="width:160px;">
								<col style="width:85px;">
							</colgroup>
							<thead>
								<tr role="row">
									<th class="text-center"><spring:message	code="admin.errorlog.title" /></th>
									<th class="text-center"><spring:message	code="admin.errorlog.server" /></th>
									<th class="text-center"><spring:message	code="admin.errorlog.type" /></th>
									<th class="text-center"><spring:message	code="reg_dt" /></th>
								</tr>
							</thead>

							<tbody class="dataTableContent">
								<tr v-for="(item,index) in gridData" class="gradeA cursor-pointer" :class="(index%2==0?'add':'even')" @click="itemView(item)">
									<td :title="item.excpTitle"><div class="text-ellipsis ellipsis0">{{item.excpTitle}}</div></td>
									<td :title="item.serverId"><div class="text-ellipsis ellipsis0">{{item.serverId}}</div></td>
									<td :title="item.excpType"><div class="text-ellipsis ellipsis0">{{item.excpType}}</div></td>
									<td>{{item.regDt}}</td>
								</tr>
								<tr v-if="gridData.length === 0">
									<td colspan="4"><div class="text-center"><spring:message code="msg.nodata"/></div></td>
								</tr>
							</tbody>
						</table>

						<page-navigation :page-info="pageInfo" callback="search"></page-navigation>
					</div>
				</div>
			</div>
			<!-- /.panel-body -->
		</div>
	</div>
	<!-- /.col-lg-4 -->
	<div class="col-lg-7">
		<div class="panel panel-default" >
			<div class="panel-heading"><spring:message code="manage.menu.glossary" /><span id="selectItemInfo" style="margin:left:10px;font-weight:bold;"></span></div>
			<!-- /.panel-heading -->
			<div class="panel-body">
				<form id="addForm" name="addForm" class="form-horizontal" >
					<div class="form-group">
						<label class="col-xs-2 control-label"><spring:message code="admin.errorlog.title" /></label>
						<div class="col-xs-10">
							<pre style="min-height:90px;">{{detailItem.excpTitle}}</pre>
						</div>
					</div>
					<div class="form-group">
						<label class="col-xs-2 control-label"><spring:message code="admin.errorlog.type" /></label>
						<div class="col-xs-10">
							<input class="form-control text required" v-model="detailItem.excpType" disabled="disabled">
						</div>
					</div>
					<div class="form-group">
						<label class="col-xs-2 control-label"><spring:message code="admin.errorlog.server" /></label>
						<div class="col-xs-10">
							<input class="form-control text required" v-model="detailItem.serverId" disabled="disabled">
						</div>
					</div>
					<div class="form-group">
						<label class="col-xs-2 control-label"><spring:message code="admin.errorlog.cont" /></label>
						<div class="col-xs-10">
							<textarea class="form-control text" rows="20" v-model="detailItem.excpCont" style="width:100%;" disabled="disabled"></textarea>
						</div>
					</div>
				</form>
			</div>
			<!-- /.panel-body -->
		</div>
	</div>
</div>
<!-- /.row -->

<script>

VarsqlAPP.vueServiceBean( {
	el: '#epViewArea'
	,data: {
		list_count :10
		,searchVal : ''
		,searchCatg : ''
		,pageInfo : {}
		,gridData :  []
		,detailItem :{}
	}
	,methods:{
		// 상세
		itemView : function (item){
			this.detailItem = item;
		}
		// 검색
		,search : function(no){
			var _self = this;

			var param = {
				pageNo: (no?no:1)
				,countPerPage : _self.list_count
				,'searchVal':_self.searchVal
				,search_category : _self.searchCatg
			};

			this.$ajax({
				url : {type:VARSQL.uri.admin, url:'/errorlogMgmt/list'}
				,data : param
				,success: function(resData) {
					_self.gridData = resData.items || [];
					_self.pageInfo = resData.page;
				}
			})
		}
	}
});
</script>