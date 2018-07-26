<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/include/tagLib.jspf"%>
<!-- Page Heading -->
<div class="row">
    <div class="col-lg-12">
        <h1 class="page-header"><spring:message code="manage.menu.sqllog" /></h1>
    </div>
    <!-- /.col-lg-12 -->
</div>
<div class="row display-off" id="epViewArea">
	<div class="col-lg-7">
		<div class="panel panel-default">
			<!-- /.panel-heading -->
			<div class="panel-body">
				<div class="row">
					<div class="col-sm-6">
						<select id="dbinfolist" class="form-control input-sm">
							<c:forEach items="${dbList}" var="tmpInfo" varStatus="status">
								<option value="${tmpInfo.VCONNID}">${tmpInfo.VNAME}</option>
							</c:forEach>
						</select>
					</div>
					<div class="col-sm-6">
						<div class="dataTables_filter">
							<label style="float:left; margin-right: 5px;"><select v-model="list_count" @change="search()" class="form-control input-sm"><option
									value="10">10</option>
								<option value="25">25</option>
								<option value="50">50</option>
								<option value="100">100</option></select>
							</label>
							<div class="input-group floatright">
								<input type="text" v-model="searchVal" class="form-control" @keyup.enter="search()" autofocus="autofocus" placeholder="Search...">
								<span class="input-group-btn">
									<button class="btn btn-default" @click="search()" type="button">
										<span class="glyphicon glyphicon-search"></span>
									</button>
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
							id="dataTables-example" style="table-layout:fixed;">
							<colgroup>
								<col style="width:75px;">
								<col style="width:*;">
								<col style="width:70px;">
								<col style="width:100px;">
								<col style="width:130px;">
								<col style="width:50px;">
							</colgroup>
							<thead>
								<tr role="row">
									<th class="text-center"><spring:message	code="manage.log.command" /></th>
									<th class="text-center"><spring:message	code="manage.log.query" /></th>
									<th class="text-center"><spring:message	code="id" /></th>
									<th class="text-center"><spring:message	code="ip" /></th>
									<th class="text-center"><spring:message	code="std_time" /><br><spring:message code="end_time" /></th>
									<th class="text-center"><spring:message	code="manage.log.delay" /></th>
								</tr>
							</thead>
							<tbody class="dataTableContent">
								<tr v-for="(item,index) in gridData" class="gradeA" :class="(index%2==0?'add':'even')">
									<td :title="item.COMMAND_TYPE"> {{item.COMMAND_TYPE}}</a></td>
									<td :title="item.LOG_SQL"><a href="javascript:;" @click="itemView(item)"><div class="text-ellipsis">{{item.LOG_SQL}}</div></a></td>
									<td :title="item.VIEWID">{{item.VIEWID}}</td>
									<td :title="item.USR_IP"><div class="text-ellipsis">{{item.USR_IP}}</div></td>
									<td>
										{{item.VIEW_STARTDT}}<br>{{item.VIEW_ENDDT}}
									</td>
									<td>{{item.DELAY_TIME}}</td>
								</tr>
								<tr v-if="gridData.length === 0">
									<td colspan="7"><div class="text-center"><spring:message code="msg.nodata"/></div></td>
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
	<div class="col-lg-5">
		<div class="panel panel-default" >
			<div class="panel-heading"><spring:message code="detail.view" /><span id="selectItemInfo" style="margin:left:10px;font-weight:bold;"></span></div>
			<!-- /.panel-heading -->
			<div class="panel-body">
				<form id="addForm" name="addForm" class="form-horizontal" >
					<div class="form-group">
						<div class="col-lg-12">
							<textarea class="form-control text" rows="10" v-model="detailItem.LOG_SQL" style="width:100%;"></textarea>
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
		,pageInfo : {}
		,gridData :  []
		,detailItem :{}
	}
	,methods:{
		// 추가.
		fieldClear : function (){
			this.detailItem = {
				wordIdx:''
				, word :''
				, wordEn :''
				, wordAbbr :''
				, wordDesc : '' 
			};
		}
		// 상세
		,itemView : function (item){
			this.detailItem = item;
		}
		// 검색
		,search : function(no){
			var _self = this;
			
			var param = {
				pageNo: (no?no:1)
				,countPerPage : _self.list_count
				,'searchVal':_self.searchVal
				,vconnid : $('#dbinfolist').val()
			};
			
			this.$ajax({
				url:{gubun:VARSQL.uri.manager, url:'/stats/logList'}
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
