<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/include/tagLib.jspf"%>
<!-- Page Heading -->
<div class="row">
    <div class="col-lg-12">
        <h1 class="page-header"><spring:message code="manager.menu.dbgroupnuser" /></h1>
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
									<th class="text-center"><spring:message	code="manager.dbgroup.nm" /></th>
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
				<spring:message code="manager.dbgroup.usermapping" />
				<template v-if="detailItem.groupName">
					[<b>{{detailItem.groupName}}</b>]
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
		,unitPage : 5
		,mappingUserSearchKeyword :''
	}
	,methods:{
		init : function (){
			var _self = this;

			this.userList(1,'');
			
			this.selectObj= $.pubMultiselect('#source', {
				header : {
					enableSourceLabel : true 	// source header label 보일지 여부
					,enableTargetLabel : true 	// target header label 보일지 여부
				}
				,body : {
					enableItemEvtBtn : true // 추가,삭제 버튼 보이기
				}
				,i18 : {
					add : 'Add'
					,remove : 'Del'
				}
				,height : 350
				,enableAddItemCheck :false
				,valueKey : 'viewid'
				,labelKey : 'uname'
				,render: function (info){
					var item = info.item;
					return (item.uname +'('+item.uid+')');
				}
				,source : {
					items : []
					,emptyMessage :'검색해주세요.'
					,search :{
						enable : true
						,callback : function (searchWord){
							_self.userList(1, searchWord);
						}
					}
					,completeMove : function (moveItem){
						if($.isArray(moveItem)){
							_self.dbGroupUserMappingInfo('add', moveItem);
						}
						
						return true; 
					}
					,paging :{
						unitPage : _self.unitPage
						,callback : function (clickInfo){
							_self.userList(clickInfo.no, clickInfo.searchword);
						}
					}
				}
				,target : {
					items : []
					,emptyMessage :'데이터가 존재하지 않습니다.'
					,search :{
						enable : true
						,callback : function (searchWord){
							_self.mappingUserSearchKeyword = searchWord;
							_self.dbMappingInfo(1, searchWord);
						}
					}
					,completeMove : function (moveItem){
						if($.isArray(moveItem)){
							_self.dbGroupUserMappingInfo('del', moveItem);
						}
						return true;
					}
					,paging :{
						unitPage : _self.unitPage
						,callback : function (clickInfo){
							_self.dbMappingInfo(clickInfo.no, clickInfo.searchword);
						}
					}
				}
			});
		}
		// 상세
		,itemView : function (item){
			this.isViewMode = true;
			this.detailItem = item;
			this.dbMappingInfo(1,this.mappingUserSearchKeyword);
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
					_self.gridData = resData.list || [];
					_self.pageInfo = resData.page;
				}
			})
		}
		,userList: function (no, searchWord){
			var _self = this;

			this.$ajax({
				url : {type:VARSQL.uri.manager, url:'/comm/userList'}
				,data : {
					pageNo: (no?no:1)
					,countPerPage : 10
					,unitPage : _self.unitPage
					,searchVal : searchWord
				}
				,success: function(resData) {
					_self.selectObj.setSourceItem(resData.list ||[], resData.page);	
				}
			})
		}
		// db mapping info
		,dbMappingInfo: function(no, searchWord){
			var _self = this;

			if(this.isViewMode ===false) return ;

			VARSQL.req.ajax({
				url : {type:VARSQL.uri.manager, url:'/dbGroup/dbGroupUserMappingList'}
				,data : {
					pageNo: (no?no:1)
					,countPerPage : 10
					,unitPage : _self.unitPage
					,groupId : this.detailItem.groupId
					,searchVal : searchWord
				}
				,loadSelector: '#source [data-item-type="source"]'
				,success:function (resData){
		    		_self.selectObj.setTargetItem(resData.list ||[], resData.page);
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
				,loadSelector: '#source'
				,success:function (res){
					_self.selectObj.setTargetItem(res.list)
				}
			});
		}
	}
});

}());
</script>