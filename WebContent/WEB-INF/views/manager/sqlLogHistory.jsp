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
								<col style="width:*;">
								<col style="width:120px;">
								<col style="width:100px;">
								<col style="width:130px;">
								<col style="width:50px;">
								<col style="width:40px;">
							</colgroup>
							<thead>
								<tr role="row">
									<th class="text-center"><spring:message	code="manage.log.query" /></th>
									<th class="text-center"><spring:message	code="id" /></th>
									<th class="text-center"><spring:message	code="ip" /></th>
									<th class="text-center"><spring:message	code="std_time" /><br><spring:message code="end_time" /></th>
									<th class="text-center"><spring:message code="result" /></th>
									<th class="text-center"><spring:message	code="manage.log.delay" /></th>
								</tr>
							</thead>
							<tbody class="dataTableContent">
								<tr v-for="(item,index) in gridData" class="gradeA" :class="(item.ERROR_LOG && item.ERROR_LOG != '')?'error-row':(index%2==0?'add':'even')">
									<td :title="item.LOG_SQL"><a href="javascript:;" @click="itemView(item)"><div class="text-ellipsis ellipsis10">{{item.LOG_SQL}}</div></a></td>
									<td :title="item.U_NM_ID"><div class="text-ellipsis ellipsis5">{{item.U_NM_ID}}</div></td>
									<td :title="item.USR_IP"><div class="text-ellipsis ellipsis5">{{item.USR_IP}}</div></td>
									<td>
										{{item.VIEW_STARTDT}}<br>{{item.VIEW_ENDDT}}
									</td>
									<td>
										<template v-if="(item.ERROR_LOG && item.ERROR_LOG != '')">
											<div class="text-center"><i class="fa fa-close"></i></div>
										</template>
										<template v-else>
											<div class="text-center"><i class="fa fa-check"></i></div>
										</template>
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
							IP : {{detailItem.USR_IP}}
						</div>
						<div class="col-lg-12">
							<div style="padding-top:10px;">SQL</div>
							<div style="height:200px;">
								<pre id="epLogSqlArea" class="user-select-on prettyprint lang-sql" style="width:100%;height:100%;"></pre>
							</div>
						</div>
						
						<div class="col-lg-12">
							<div style="padding-top:10px;">ERROR LOG</div>
							<div style="height:200px;">
								<textarea id="epSqlErrorArea" v-model="detailItem.ERROR_LOG" style="width:100%;height:100%;"></textarea>
							</div>
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
			var ele = $('#epLogSqlArea');
			
			this.detailItem = item;
			
			ele.empty().html(item.LOG_SQL);
			ele.removeClass('prettyprinted');
			PR.prettyPrint();
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
				url : {type:VARSQL.uri.manager, url:'/stats/logList'}
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
