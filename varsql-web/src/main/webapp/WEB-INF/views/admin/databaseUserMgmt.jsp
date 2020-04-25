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
					<ul id="source" class="form-control" style="width:100%;height:200px;">
					  <li><spring:message code="msg.nodata" /></li>
					</ul>
				</div>
				<div class="col-sm-12" style="text-align:center;padding:10px;">
					<a href="javascript:;" class="btn_m mb05 item-move" mode="down"><span class="glyphicon glyphicon-chevron-down"></span><spring:message code="label.add"/></a>
					<a href="javascript:;" class="btn_m mb05 item-move" mode="up"><span class="glyphicon glyphicon-chevron-up"></span><spring:message code="label.delete"/></a>
				</div>
				<div class="col-sm-12">
					<ul id="target"  class="form-control" style="width:100%;height:200px;">
					  <li><spring:message code="msg.nodata" /></li>
					</ul>
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

			$('.item-move').on('click',function (){
				var moveItem = [];
				var mode = $(this).attr('mode');
				if(mode =='up'){
					moveItem = _self.selectObj.targetMove()
				}else{
					moveItem = _self.selectObj.sourceMove();
				}
			});

			this.selectObj= $.pubMultiselect('#source', {
				targetSelector : '#target'
				,addItemClass:'text_selected'
				,useMultiSelect : true
				,useDragMove : false
				,useDragSort : false
				,duplicateCheck : true
				,message :{
					duplicate: VARSQL.messageFormat('varsql.0018')
				}
				,sourceItem : {
					optVal : 'viewid'
					,optTxt : 'uname'
					,items : []
				}
				,targetItem : {
					optVal : 'viewid'
					,optTxt : 'uname'
					,items : []
				}
				,compleateSourceMove : function (moveItem){
					if($.isArray(moveItem)){
						_self.addDbManager('down', moveItem);
					}
				}
				,compleateTargetMove : function (moveItem){
					if($.isArray(moveItem)){
						_self.addDbManager('up', moveItem);
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
				url : {type:VARSQL.uri.admin, url:'/main/dblist'}
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
					_self.selectObj.setItem('source', _self.getItemFormatter(resData.items));
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
					var result = resData.items;
		    		var resultLen = result.length;

					if(resultLen==0){
		    			$('#target').html('<spring:message code="msg.nodata" />');
		    			return ;
		    		}

		    		_self.selectObj.setItem('target', _self.getItemFormatter(result));
				}
			})
		}
		,getItemFormatter : function (result){
			var resultItem = [];
    		var item;
    		for(var i = 0 ;i < result.length; i ++){
    			item = result[i];
    			item.uname =item.uname+'('+item.uemail +')';
    			resultItem.push(item);
    		}
			return resultItem;
		}
		,addDbManager : function (mode, moveItem){
			var _self = this;

			if(!_self.detailItem.vconnid){
				alert(VARSQL.messageFormat('varsql.0003'));
				return ;
			}

			var param ={
				selectItem : moveItem.join(',')
				,vconnid : _self.detailItem.vconnid
				, mode : mode =='up'? 'del' : 'add'
			};

			_self.$ajax({
				data:param
				,url : {type:VARSQL.uri.admin, url:'/managerMgmt/addDbManager'}
				,success:function (resData){
					_self.dbManagerList(_self.detailItem);
				}
			});
		}
	}
});
</script>
