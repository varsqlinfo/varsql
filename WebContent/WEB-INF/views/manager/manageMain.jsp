<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/include/tagLib.jspf"%>
<!-- Page Heading -->
<div class="row">
    <div class="col-lg-12">
        <h1 class="page-header"><spring:message code="manage.menu.usermgmt" /></h1>
    </div>
    <!-- /.col-lg-12 -->
</div>
<div class="row display-off" id="epViewArea">
	<div class="col-lg-12">
		<div class="panel panel-default">
			<!-- /.panel-heading -->
			<div class="panel-body">
				<div class="row">
					<div class="col-sm-6">
						<label> 
							<button type="button" class="btn btn-xs btn-primary" @click="acceptYn('Y')"><spring:message code="btn.accept" /></button>
						</label>
						<label> 
							<button type="button" class="btn btn-xs btn-danger" @click="acceptYn('N')"><spring:message code="btn.denial" /></button>
						</label>
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
								<input type="text" v-model="searchVal" class="form-control" @keyup="search()">
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
							id="dataTables-example">
							<thead>
								<tr role="row">
									<th  style="width: 10px;"><div
											class="text-center">
											<input type="checkbox" id="allcheck" name="allcheck">
										</div>
									</th>
									<th style="width: 195px;">
										<spring:message	code="manage.userlist.name" />
									</th>
									<th style="width: 150px;">
										<spring:message	code="manage.userlist.id" />
									</th>
									<th style="width: 179px;">
										<spring:message	code="manage.userlist.date" />
									</th>
									<th style="width: 50px;">
										<spring:message	code="manage.userlist.access" />
									</th>
									<th style="width: 150px;">
										<spring:message	code="manage.userlist.init.password" />
									</th>
								</tr>
							</thead>
							<tbody class="dataTableContent">
								<tr v-for="(item,index) in gridData" class="gradeA" :class="(index%2==0?'add':'even')">
									<td><input type="checkbox" :value="item.VIEWID" v-model="selectItem"></td>
									<td><a href="javascript:;" @click="detailView(item)">{{item.UNAME}}</a></td>
									<td>{{item.UID}}</td>
									<td class="center">{{item.CHAR_CRE_DT}}</td>
									<td class="center">{{item.ACCEPT_YN}}</td>
									<td class="center">
										<button class="btn btn-xs btn-default" @click="initPassword(item)" >초기화</button>
										<span>{{item.INITPW}}</span>
									</td>
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
	<!-- /.col-lg-12 -->
	
	<div id="detailInfo" class="display-off" title="상세보기">
		<table class="user-detail-info table table-striped table-bordered table-hover dataTable no-footer">
			<colgroup>
				<col style="width:120px">
				<col style="width:*">
			</colgroup>
			<tbody class="dataTableContent">
				<tr>
					<th>ID</th><td>{{clickItem.UNAME}}</td>
				</tr>
				<tr>
					<th>UID</th><td>{{clickItem.UID}}</td>
				</tr>
				<tr>
					<th>EMAIL</th><td>{{clickItem.UEMAIL}}</td>
				</tr>
				<tr>
					<th>ORG</th><td>{{clickItem.ORG_NM}}</td>
				</tr>
				<tr>
					<th>DEPT</th><td>{{clickItem.DEPT_NM}}</td>
				</tr>
				<tr>
					<th>DESC</th><td>{{clickItem.DESCRIPTION}}</td>
				</tr>
				<tr>
					<th>차단</th>
					<td>
						<template v-if="clickItem.BLOCK_YN=='N'">
							<button type="button" class="btn btn-xs btn-danger" @click="userBlock('Y')"><spring:message code="block"/></button>
						</template>
						<template v-else>
							<button type="button" class="btn btn-xs btn-info" @click="userBlock('N')"><spring:message code="release"/></button>
						</template>
					</td>
				</tr>
				<tr>
					<th valign="top" style="vertical-align: top;">
						<div>권한 있는 DB 목록</div>
					</th>
					<td style="padding:0px;">
						<div>
							<table class="fixed_headers" style="background:#ffffff;">
								<thead>
									<tr>
										<th>DB</th>
										<th>사용자 </th>
										<th>매니저</th>
									</tr>
								</thead>
								<tbody>
									<tr v-for="(item,index) in clickItemDbList">
										<td><div class="text-ellipsis ellipsis5" style="width:150px;">{{item.VNAME}}</div></td>
										<td class="align-center">
											<template v-if="item.USER_CHK > 0 && (item.MANAGER_CNT > 0 || clickItemCustom.isAdmin) ">
												<button type="button" @click="removeAuth(item, 'U')">제거</button>
											</template>
											<template v-else-if="item.USER_CHK > 0">
												Y
											</template>
											<template v-else>N</template>
										</td>
										<td class="align-center">
											<template v-if="clickItemCustom.isAdmin && item.MSG_CHK > 0">
												<button type="button" @click="removeAuth(item, 'M')">제거</button>
											</template>
											<template v-else-if="item.MSG_CHK> 0">
												Y
											</template>
											<template v-else>N</template>
										</td>
									</tr>
									<tr v-if="clickItemDbList.length === 0">
										<td colspan="10" style="width:100%;min-width: 345px;border: 0px;"><div class="text-center"><spring:message code="msg.nodata"/></div></td>
									</tr>
								</tbody>
							</table>
						</div>
					</td>
				</tr>
			</tbody>
		</table>
	</div>
</div>
<!-- /.row -->
<style>
.ui-front { z-index: 2000}

.user-detail-info {
    width: 100%;
    max-width: 100%;
    margin-bottom: 0px;
}
#detailInfo{
	overflow:hidden;
}

.fixed_headers {
	width: 350px;
	table-layout: fixed;
}
.fixed_headers th {
	text-align:center;
}
.fixed_headers th,
.fixed_headers td {
	padding: 5px;
	border :1px solid #dddddd;
}
.fixed_headers td:nth-child(1),
.fixed_headers th:nth-child(1) {
	min-width: 160px;
}
.fixed_headers td:nth-child(2),
.fixed_headers th:nth-child(2) {
	min-width: 95px;
}
.fixed_headers td:nth-child(3),
.fixed_headers th:nth-child(3) {
	width: 95px;
}
.fixed_headers thead {
	background-color: #e1e1e1;
    color: #090909;
}
.fixed_headers thead tr {
	display: block;
	position: relative;
}
.fixed_headers tbody {
	display: block;
	overflow: auto;
	height: 100px;
}

</style>
<script>

VarsqlAPP.vueServiceBean( {
	el: '#epViewArea'
	,data: {
		list_count :10
		,searchVal : ''
		,pageInfo : {}
		,gridData :  []
		,detailItem :{}
		,selectItem :[]
		,clickItem :{}
		,clickItemDbList :[]
	}
	
	,methods:{
		init : function(){
			var _self =this; 
			_self.detailDialog = $('#detailInfo').dialog({
				height: 490
				,width: 500
				,modal: true
				,autoOpen :false
				,buttons: {
					Cancel: function() {
						_self.detailDialog.dialog( "close" );
					}
				}
				,close: function() {
					_self.detailDialog.dialog( "close" );
				}
			});
		}
		,acceptYn : function(obj){
			var _self = this; 
			var selectItem = _self.selectItem;
			
			if(VARSQL.isDataEmpty(selectItem)){
				VARSQL.alert('<spring:message code="msg.data.select" />');
				return ; 
			}
			
			if(!confirm(obj=='Y'?'<spring:message code="msg.accept.msg" />':'<spring:message code="msg.denial.msg" />')){
				return ; 
			}
			
			var param = {
				acceptyn:obj
				,selectItem:selectItem.join(',')
			};
			
			this.$ajax({
				data:param
				,url : {gubun:VARSQL.uri.manager, url:'/user/acceptYn'}
				,success:function (response){
					_self.search();
				}
			});
		}
		,userBlock : function(mode){
			var _self = this; 
			var clickItem = _self.clickItem;
			
			if(!confirm(mode=='Y'?'<spring:message code="msg.confirm.block.y" />':'<spring:message code="msg.confirm.block.n" />')){
				return ; 
			}
			
			var param = {
				userid : clickItem.VIEWID
				,blockYn: mode
			}
			
			this.$ajax({
				data:param
				,url : {gubun:VARSQL.uri.manager, url:'/user/blockYn'}
				,success:function (resData){
					
					if(resData.item > 0){
						clickItem.BLOCK_YN = mode; 
					}
				}
			});
		}
		,search : function(no){
			var _self = this; 
			
			var param = {
				pageNo: (no?no:1)
				,rows: _self.list_count
				,'searchVal':_self.searchVal
			};
			
			this.$ajax({
				url:{gubun:VARSQL.uri.manager, url:'/user/userList'}
				,data : param
				,success: function(resData) {
					_self.gridData = resData.items;
					_self.pageInfo = resData.page;
				}
			})
		}
		// 상세보기
		,detailView : function(item){
			var _self = this; 
			
			this.$ajax({
				url:{gubun:VARSQL.uri.manager, url:'/user/userDetail'}
				,data : item
				,loadSelector : '#main-content'
				,success: function(resData) {
					_self.clickItem =resData.item;
					_self.clickItemDbList =resData.items;
					_self.clickItemCustom =resData.customs;
					
					_self.detailDialog.dialog("open");
				}
			})
		}
		// pasword 초기화
		,initPassword :function(sItem){
			this.$ajax({
				url:{gubun:VARSQL.uri.manager, url:'/user/initPassword'}
				,data : sItem
				,success: function(resData) {
					sItem.INITPW = resData.item;
					
					setTimeout(function (){
						sItem.INITPW ='';
					}, 5000);
					
					alert('변경되었습니다.\n변경된 패스워드는 5초후에 사라집니다.');
				}
			})
		}
		,removeAuth : function (item, mode){
			var confirmMsg =this.clickItem.UNAME +'님의 ['+item.VNAME+']'; 
			if(mode=='M'){
				confirmMsg+=' 매니저 권한을 삭제 하시겠습니까?';
			}else{
				confirmMsg+=' 사용자 권한을 삭제 하시겠습니까?';
			}
			
			if(!confirm(confirmMsg)){
				return ; 
			}
			
			var param = VARSQL.util.objectMerge({},item);
			param.mode = mode; 
			this.$ajax({
				url:{gubun:VARSQL.uri.manager, url:'/user/removeAuth'}
				,data : param
				,success: function(resData) {
					if(resData.item > 0){
						if(mode=='M'){
							item.MSG_CHK = 0;
						}else{
							item.USER_CHK = 0;
						}
					}
				}
			})
		}
	}
});
</script>
