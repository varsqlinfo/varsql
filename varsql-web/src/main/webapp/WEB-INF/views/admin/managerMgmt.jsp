<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/include/tagLib.jspf"%>
<!-- Page Heading -->
<div class="row">
    <div class="col-lg-12">
        <h1 class="page-header"><spring:message code="admin.menu.managermgmt" /></h1>
    </div>
    <!-- /.col-lg-12 -->
</div>
<div class="row display-off" id="varsqlVueArea">
	<div class="col-sm-6">
		<div class="panel panel-default">
			<div class="panel-heading"><spring:message code="admin.userlist.search.head" /></div>
			<!-- /.panel-heading -->
			<div class="panel-body">
				<div class="row">
					<div class="col-sm-12" style="padding-bottom: 5px;">
						<div class="dataTables_filter">
							<div class="input-group floatright">
								<input type="text" value="" v-model="userSearchVal" class="form-control" @keydown.enter="userSearch()">
								<span class="input-group-btn">
									<button class="btn btn-default searchBtn" type="button" @click="userSearch()"> <span class="glyphicon glyphicon-search"></span></button>
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
									<th tabindex="0" rowspan="1" colspan="1" style="width: 195px;"><spring:message code="manage.userlist.name" /></th>
									<th tabindex="0" rowspan="1" colspan="1" style="width: 150px;"><spring:message code="manage.userlist.id" /></th>
									<th tabindex="0" rowspan="1" colspan="1" style="width: 179px;"><spring:message code="manage.userlist.dept" /></th>
								</tr>
							</thead>
							<tbody>
				    			<tr v-for="(item,index) in userGridData" class="gradeA " :class="index%2==0?'add':'even'" >
									<td class=""><a href="javascript:;" @click="roleAction(item,'add')">{{item.uname}}</a></td>
									<td class="">{{item.uid}}</td>
									<td class="center">{{item.deptNm}}</td>
								</tr>
				    			<tr v-if="userGridData.length === 0"><td colspan="3"><div class="text-center"><spring:message code="msg.nodata"/></div></td></tr>
							</tbody>
						</table>
						<page-navigation :page-info="userPageInfo" callback="userSearch"></page-navigation>
					</div>
				</div>
			</div>
			<!-- /.panel-body -->
		</div>
		<!-- /.panel -->
	</div>

	<div class="col-sm-6">
		<div class="panel panel-default">
			<div class="panel-heading"><spring:message code="admin.managerlist.head" /></div>
			<!-- /.panel-heading -->
			<div class="panel-body">
				<div class="row">
					<div class="col-sm-12" style="padding-bottom: 5px;">
						<div class="dataTables_filter">
							<div class="input-group floatright">
								<input type="text" value="" v-model="managerSearchVal" class="form-control" @keydown.enter="managerSearch()">
								<span class="input-group-btn">
									<button class="btn btn-default searchBtn" type="button" @click="managerSearch()"> <span class="glyphicon glyphicon-search"></span></button>
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
									<th tabindex="0" rowspan="1" colspan="1" style="width: 195px;"><spring:message code="manage.userlist.name" /></th>
									<th tabindex="0" rowspan="1" colspan="1" style="width: 150px;"><spring:message code="manage.userlist.id" /></th>
									<th tabindex="0" rowspan="1" colspan="1" style="width: 179px;"><spring:message code="manage.userlist.dept" /></th>
								</tr>
							</thead>
							<tbody>
				    			<tr v-for="(item,index) in managerGridData" class="gradeA " :class="index%2==0?'add':'even'" >
									<td class=""><a href="javascript:;" @click="roleAction(item,'remove')">{{item.uname}}</a></td>
									<td class="">{{item.uid}}</td>
									<td class="center">{{item.deptNm}}</td>
								</tr>
				    			<tr v-if="managerGridData.length === 0"><td colspan="3"><div class="text-center"><spring:message code="msg.nodata"/></div></td></tr>
							</tbody>
						</table>
						<page-navigation :page-info="managerPageInfo" callback="managerSearch"></page-navigation>
					</div>
				</div>
			</div>
			<!-- /.panel-body -->
		</div>
		<!-- /.panel -->
	</div>
</div>
<!-- /.row -->


<script>
VarsqlAPP.vueServiceBean( {
	el: '#varsqlVueArea'
	,data: {
		list_count :10
		,userSearchVal : ''
		,userGridData :  []
		,userPageInfo : {}
		,managerSearchVal : ''
		,managerGridData :  []
		,managerPageInfo : {}
	}
	,methods:{
		init : function(){
			this.userSearch();
			this.managerSearch();
		}
		,userSearch : function(no){
			var _self = this;

			var param = {
				pageNo: (no?no:1)
				,'searchVal':_self.userSearchVal
			};

			this.$ajax({
				url : {type:VARSQL.uri.admin, url:'/managerMgmt/userList'}
				,data : param
				,success: function(resData) {
					_self.userGridData = resData.items;
					_self.userPageInfo = resData.page;
				}
			})
		}
		,managerSearch : function(no){
			var _self = this;

			var param = {
				pageNo: (no?no:1)
				,'searchVal':_self.managerSearchVal
			};

			this.$ajax({
				url : {type:VARSQL.uri.admin, url:'/managerMgmt/managerList'}
				,data : param
				,success: function(resData) {
					_self.managerGridData = resData.items;
					_self.managerPageInfo = resData.page;
				}
			})
		}
		,roleAction : function (item,mode){
			var _this = this;

			if(!confirm(VARSQL.messageFormat(mode=='add'?'msg.add.manager.confirm':'msg.del.manager.confirm', {name:item.uname +'('+item.uid+')'}))){
                return ;
            }

            var param ={
                viewid : item.viewid
                ,mode : mode
            };

            _this.$ajax({
                data:param
                ,url : {type:VARSQL.uri.admin, url:'/managerMgmt/managerRoleMgmt'}
                ,success:function (resData){
                	_this.userSearch();
                	_this.managerSearch();
                }
            });
		}
	}
});
</script>
