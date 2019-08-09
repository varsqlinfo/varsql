<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/include/tagLib.jspf"%>
<!-- Page Heading -->
<div class="row">
    <div class="col-lg-12">
        <h1 class="page-header"><spring:message code="manage.menu.dbgroup" /></h1>
    </div>
    <!-- /.col-lg-12 -->
</div>
<div class="row display-off" id="epViewArea">
	<div class="col-lg-5">
		<div class="panel panel-default">
			<!-- /.panel-heading -->
			<div class="panel-body">
				<div class="row">
					<div class="col-sm-12">
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
								<col style="width:140px;">
								<col style="width:140px;">
							</colgroup>
							<thead>
								<tr role="row">
									<th class="text-center"><spring:message	code="manage.dbgroup.nm" /></th>
									<th class="text-center"><spring:message	code="reg_user" /></th>
									<th class="text-center"><spring:message	code="reg_dt" /></th>
								</tr>
							</thead>
							<tbody class="dataTableContent">
								<tr v-for="(item,index) in gridData" class="gradeA" :class="(index%2==0?'add':'even')">
									<td :title="item.groupName"><a href="javascript:;" @click="itemView(item)"> {{item.groupName}}</a></td>
									<td :title="item.regId"><div class="text-ellipsis">{{item.regId}}</div></td>
									<td>{{item.charCreDt}}</td>
								</tr>
								<tr v-if="gridData.length === 0">
									<td colspan="3"><div class="text-center"><spring:message code="msg.nodata"/></div></td>
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
			<div class="panel-heading"><spring:message code="manage.menu.dbgroup" /><span id="selectItemInfo" style="margin:left:10px;font-weight:bold;"></span></div>
			<!-- /.panel-heading -->
			<div class="panel-body">
				<input type="hidden" v-model="detailItem.wordIdx">
				<form id="addForm" name="addForm" class="form-horizontal" >
					<div class="form-group">
						<div class="col-sm-12">
							<div class="pull-right">
								<button type="button" class="btn btn-default" @click="fieldClear()"><spring:message code="btn.add"/></button>
								<button type="button" class="btn btn-default" @click="saveInfo()"><spring:message code="btn.save"/></button>
								<button type="button" class="btn btn-danger" :class="(isViewMode?'':'hide')"  @click="deleteInfo()"><spring:message code="btn.delete"/></button>
							</div>
						</div>
					</div>
					<div id="warningMsgDiv"></div>
					<div class="form-group">
						<label class="col-sm-4 control-label"><spring:message code="manage.dbgroup.nm" /></label>
						<div class="col-sm-8">
							<input class="form-control text required" v-model="detailItem.groupName">
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-4 control-label"><spring:message code="desc" /></label>
						<div class="col-sm-8">
							<textarea class="form-control text" rows="3" v-model="detailItem.groupDesc" style="width:100%;"></textarea>
						</div>
					</div>
				</form>
			</div>
			<!-- /.panel-body -->
		</div>
	</div>
</div>


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
	}
	,beforeMount: function() {
		this.fieldClear();
	}
	,methods:{
		// 추가.
		fieldClear : function (){
			this.isViewMode = false;
			this.detailItem = {
				groupId:''
				, groupName:''
				, groupDesc :''
			};
		}
		// 상세
		,itemView : function (item){
			this.isViewMode = true;
			this.detailItem = item;
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
		// 저장
		,saveInfo : function (){
			var _self = this;
			
			var param = this.detailItem;
			
			_self.$ajax({
				url : {type:VARSQL.uri.manager, url:'/dbGroup/save'}
				,data : param
				,success: function(resData) {
					if(resData.resultCode != 200){
						if(resData.messageCode=='valid'){
							var items = resData.items;
							objLen = items.length;
							if(objLen >0){
								var item;
								for(var i=0; i <objLen; i++){
									item = items[i];
									alert(item.field + "\n"+ item.defaultMessage)
									return ; 
								}
							}
						}else{
							var message = resData.messageCode; 
							alert(resData.messageCode +'\n'+ resData.message);
							return ; 
						}
					}
					
					_self.fieldClear();
					_self.search();
				}
			})
		}
		// 삭제.
		,deleteInfo : function(){
			var _self = this; 
			
			if(!confirm('['+_self.detailItem.groupName +'] 삭제하시겠습니까?')){
				return ; 
			}
			
			var param = {
				groupId : _self.detailItem.groupId
			};
			
			this.$ajax({
				data:param
				,url : {type:VARSQL.uri.manager, url:'/dbGroup/delete'}
				,success:function (response){
					_self.fieldClear();
					_self.search();
				}
			});
		}
	}
});

}());
</script>
