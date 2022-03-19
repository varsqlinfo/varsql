<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/include/tagLib.jspf"%>
<!-- Page Heading -->
<div class="row">
    <div class="col-lg-12">
        <h1 class="page-header"><spring:message code="manage.menu.dbgroupnuser" /></h1>
    </div>
    <!-- /.col-lg-12 -->
</div>
<div class="row display-off" id="epViewArea">
	<div class="col-xs-4">
		<div class="panel panel-default">
			<!-- /.panel-heading -->
			<div class="panel-body">
				<div class="row search-area">
					<div class="col-sm-4"></div>
					<div class="col-sm-8">
						<div class="dataTables_filter">
							<label style="float:left; margin-right: 5px;"><select v-model="list_count" @change="search()" class="form-control "><option
									value="10">10</option>
								<option value="25">25</option>
								<option value="50">50</option>
								<option value="100">100</option></select>
							</label>
							<div class="input-group floatright">
								<input type="text" v-model="searchVal" class=" form-control" @keyup.enter="search()" autofocus="autofocus" placeholder="Search...">
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
							</colgroup>
							<thead>
								<tr role="row">
									<th class="text-center"><spring:message	code="manage.dbgroup.nm" /></th>
								</tr>
							</thead>
							<tbody class="dataTableContent">
								<tr v-for="(item,index) in gridData" class="gradeA" :class="(index%2==0?'add':'even')">
									<td :title="item.groupName"><a href="javascript:;" @click="itemView(item)"> {{item.groupName}}</a></td>
								</tr>
								<tr v-if="gridData.length === 0">
									<td><div class="text-center"><spring:message code="msg.nodata"/></div></td>
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
	<div class="col-xs-8">
		<div class="panel panel-default">
			<div class="panel-heading">
				<spring:message code="manage.dbgroup.usermapping" />
				<template v-if="detailItem.groupName">
					[[<b>{{detailItem.groupName}}</b>]]
				</template>
			</div>

			<div class="panel-body">
				<div class="col-sm-12">
					<div id="source" style="width:100%;height:400px;"></div>
				</div>
			</div>
		</div>
		<!-- /.panel -->
	</div>
	<!-- /.col-lg-8 -->
</div>
<!-- /.row -->


<script>
(function() {

VarsqlAPP.vueServiceBean( {
	el: '#epViewArea'
	,data: {
		list_count :10
		,searchVal : ''
		,pageInfo : {}
		,gridData :  []
		,detailItem : {}
		,isViewMode : false
		,selectObj : {}
	}
	,methods:{
		init : function (){
			var _self = this;

			this.initDbMappingInfo();

			_self.selectObj= $.pubMultiselect('#source', {
				valueKey : 'viewid'	
				,labelKey : 'uname'
				,render: function (info){	// 아이템 추가될 템플릿.
					var item = info.item; 
					return (item.uname+'('+item.uid+')')
				}
				,source : {
					items : []
					,completeMove : function (moveItem){
						if($.isArray(moveItem)){
							_self.dbGroupUserMappingInfo('add', moveItem);
						}
						
						return false; 
					}
				}
				,target : {
					items : []
					,completeMove : function (moveItem){
						if($.isArray(moveItem)){
							_self.dbGroupUserMappingInfo('del', moveItem);
						}
						return false; 
					}
				}
			});
		}
		// 상세
		,itemView : function (item){
			this.isViewMode = true;
			this.detailItem = item;
			this.dbMappingInfo();
		}
		// 검색
		,search : function(no){
			var _self = this;

			var param = {
				pageNo: (no?no:1)
				,countPerPage : _self.list_count
				,'searchVal':_self.searchVal
			};

			this.$ajax({
				url : {type:VARSQL.uri.manager, url:'/dbGroup/list'}
				,data : param
				,success: function(resData) {
					_self.gridData = resData.items || [];
					_self.pageInfo = resData.page;
				}
			})
		}
		,initDbMappingInfo: function (){
			var _self = this;
			var param = {
				'searchVal':''
			};

			this.$ajax({
				url : {type:VARSQL.uri.manager, url:'/comm/userList'}
				,data : param
				,success: function(resData) {
					_self.selectObj.setSourceItem( resData.items);
				}
			})
		}
		// db mapping info
		,dbMappingInfo: function (){
			var _self = this;

			if(this.isViewMode ===false) return ;

			var param = {
				groupId : this.detailItem.groupId
			};

			VARSQL.req.ajax({
				data:param
				,loadSelector: '#main-content'
				,url : {type:VARSQL.uri.manager, url:'/dbGroup/dbGroupUserMappingList'}
				,success:function (resData){
					var result = resData.items;
		    		_self.selectObj.setTargetItem(result);
				}
			});
		}
		// 맵핑 정보 추가.
		,dbGroupUserMappingInfo : function (mode, moveItem){
			var _self = this;

			if(!_self.detailItem.groupId){
				alert(VARSQL.messageFormat('varsql.0003'));
				return false;
			}

			if(this.isViewMode ===false) return ;

			var param ={
				selectItem : moveItem.join(',')
				,groupId : this.detailItem.groupId
				, mode : mode
			};

			VARSQL.req.ajax({
				data:param
				,url : {type:VARSQL.uri.manager, url:'/dbGroup/dbGroupUserMapping'}
				,success:function (res){
					console.log(res);
					_self.selectObj.setTargetItem(res.items)
				}
			});
		}
	}
});

}());
</script>