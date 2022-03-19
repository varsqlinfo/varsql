<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/include/tagLib.jspf"%>

<!-- Page Heading -->
<div class="row">
    <div class="col-lg-12">
        <h1 class="page-header"><spring:message code="admin.menu.databaseusermgmt" /></h1>
    </div>
    <!-- /.col-lg-12 -->
</div>
<div class="row display-off" id="varsqlVueArea">
	<div class="col-xs-6">
		<div class="panel panel-default">
			<div class="panel-heading">
				<div class="input-group">
					<input type="text" value="" v-model="searchVal" class="form-control" @keydown.enter="search()">
					<span class="input-group-btn">
						<button class="btn btn-default searchBtn" type="button" @click="search()"> <span class="glyphicon glyphicon-search"></span></button>
					</span>
				</div>
			</div>
			<!-- /.panel-heading -->
			<div class="panel-body">
				<input type="hidden" id="vconnid" name="vconnid" value="">
				<div class="list-group" >
					<template v-for="(item,index) in searchData">
		    			<a href="javascript:;" class="list-group-item" @click="dbManagerList(item)">{{item.vname}}</a>
	    			</template>
	    			<div v-if="searchData.length === 0" class="text-center"><spring:message code="msg.nodata"/></div>
				</div>
				<page-navigation :page-info="pageInfo" callback="search"></page-navigation>
			</div>
			<!-- /.panel-body -->
		</div>
		<!-- /.panel -->
	</div>

	<div class="col-xs-6">
		<div class="panel panel-default">
			<div class="panel-heading">
				<span v-if="detailItem.vname">[{{detailItem.vname}}]</span> <spring:message code="admin.managerlist.dbuser" />
			</div>
			<!-- /.panel-heading -->
			<div class="panel-body manage-user-detail">
				<div class="col-sm-12">
					<div id="source" style="width:100%;height:430px;"></div>
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
		,searchVal : ''
		,searchData :  []
		,pageInfo : {}
		,selectObj : {}
		,detailItem : {}
	}
	,methods:{
		init : function(){
			this.initPubMultiselect();
			this.allManager();
		}
		, initPubMultiselect : function (){
			var _self = this;
			
			this.selectObj= $.pubMultiselect('#source', {
				orientation : 'y'
				,duplicateCheck : true
				,message :{
					duplicate: VARSQL.messageFormat('varsql.0018')
				}
				,valueKey : 'viewid'	
				,labelKey : 'name'
				,render: function (info){	// 아이템 추가될 템플릿.
					var item = info.item; 
					return (item.uname+'('+item.uid+')')
				}
				,source : {
					items : []
					,completeMove : function (moveItem){
						if($.isArray(moveItem)){
							_self.dbManagerMapping('add', moveItem);
						}
						return false; 
					}
				}
				,target : {
					items : []
					,completeMove : function (moveItem){
						if($.isArray(moveItem)){
							_self.dbManagerMapping('del', moveItem);
						}
						return false; 
					}
				}
			});
		}
		// db list
		,search : function(no){
			var _self = this;

			var param = {
				pageNo: (no?no:1)
				,'searchVal':_self.searchVal
			};

			this.$ajax({
				url : {type:VARSQL.uri.admin, url:'/databaseMgmt/dbNameSearch'}
				,data : param
				,success: function(resData) {
					_self.searchData = resData.items;
					_self.pageInfo = resData.page;
				}
			})
		}
		// get all manager
		,allManager : function (){
			var _self = this;

			var param = {
				rows : 10000
				,'searchVal':_self.managerSearchVal
			};

			this.$ajax({
				url : {type:VARSQL.uri.admin, url:'/managerMgmt/managerList'}
				,data : param
				,success: function(resData) {
					_self.managerGridData = resData.items;
					
		    		if( _self.managerGridData.length > 0){
		    			_self.selectObj.setSourceItem(resData.items);
		    		}
				}
			})
		}
		// db managet list
		,dbManagerList : function(item){
			var _self = this;

			this.detailItem = item;

			var param = {
				vconnid: item.vconnid
			};

			this.$ajax({
				url : {type:VARSQL.uri.admin, url:'/managerMgmt/dbManagerList'}
				,loadSelector: '.manage-user-detail'
				,data : param
				,success: function(resData) {
		    		_self.selectObj.setTargetItem(resData.items);
				}
			})
		}
		,dbManagerMapping : function (mode, moveItem){
			var _self = this;

			if(!_self.detailItem.vconnid){
				alert(VARSQL.messageFormat('varsql.0003'));
				return false;
			}

			var param ={
				selectItem : moveItem.join(',')
				,vconnid : _self.detailItem.vconnid
				, mode : mode =='del'? 'del' : 'add'
			};

			_self.$ajax({
				data:param
				,url : {type:VARSQL.uri.admin, url:'/managerMgmt/dbManagerMapping'}
				,success:function (resData){
					_self.selectObj.setTargetItem(resData.items);
				}
			});
		}
	}
});
</script>
