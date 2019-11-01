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
	<div class="col-lg-6">
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
											<input type="checkbox" :checked="selectAllCheck" @click="selectAll()">
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
	<div class="col-lg-6">
		<div class="panel panel-default" >
			<div class="panel-heading"><spring:message code="manage.menu.dbgroup" /><span id="selectItemInfo" style="margin:left:10px;font-weight:bold;"></span></div>
			<!-- /.panel-heading -->
			<div class="panel-body">
				<table class="user-detail-info table table-striped table-bordered table-hover dataTable no-footer">
					<colgroup>
						<col style="width:120px">
						<col style="width:*">
					</colgroup>
					<tbody class="dataTableContent">
						<tr>
							<th><spring:message code="label.username"/></th><td>{{clickItem.UNAME}}</td>
						</tr>
						<tr>
							<th><spring:message code="label.id"/></th><td>{{clickItem.UID}}</td>
						</tr>
						<tr>
							<th><spring:message code="label.email" text="email"/></th><td>{{clickItem.UEMAIL}}</td>
						</tr>
						<tr>
							<th><spring:message code="label.orgnm" text="orgnm"/></th><td>{{clickItem.ORG_NM}}</td>
						</tr>
						<tr>
							<th><spring:message code="label.deptnm" text="orgnm"/></th><td>{{clickItem.DEPT_NM}}</td>
						</tr>
						<tr>
							<th><spring:message code="label.block"/></th>
							<td>
								<template v-if="clickItem.BLOCK_YN">
									<template v-if="clickItem.BLOCK_YN=='N'">
										<button type="button" class="btn btn-xs btn-danger" @click="userBlock('Y')"><spring:message code="block"/></button>
									</template>
									<template v-else>
										<button type="button" class="btn btn-xs btn-info" @click="userBlock('N')"><spring:message code="release"/></button>
									</template>
								</template>
							</td>
						</tr>
						<tr>
							<th valign="top" style="vertical-align: top;"><spring:message code="remarks" text="orgnm"/></th><td><textarea rows="3" style="width:100%;overflow:auto;" disabled="disabled">{{clickItem.DESCRIPTION}}</textarea></td>
						</tr>
						
						<tr>
							<td colspan="2">
								<div class="col-lg-6 padding0" style="padding-right:10px;">
									<div class="tbl-header-fixed-container" style="width: 100%; height: 180px;">
										<div class="tbl-header-fixed-header-bg"></div>
										<div class="tbl-header-fixed-wrapper">
											<table  class="tbl-header-fixed">
												<colgroup>
													<col style="width:65%;">
													<col style="width:35%;">
												</colgroup>
												<thead>
													<tr>
														<th style="width: 100%" colspan="2"><div class="th-text text-center"><spring:message code="label.dbgroup" text="Db group"/></div></th>
													</tr>
												</thead>
												<tbody>
													<tr v-for="(item,index) in dbGroup">
														<td class="text-left">{{item.GROUP_NAME}}</td>
														<td >
															<button type="button" @click="removeDbGroupInfo(item)"><spring:message code="label.remove" text="제거"/></button>
														</td>
													</tr>
													<tr v-if="dbGroup.length === 0">
														<td colspan="2"><div class="text-center"><spring:message code="msg.nodata"/></div></td>
													</tr>
												</tbody>
											</table>
										</div>
									</div>
								</div>
								<div class="col-lg-6 padding0">
									 <div class="tbl-header-fixed-container" style="width: 100%; height: 180px;">
										<div class="tbl-header-fixed-header-bg"></div>
										<div class="tbl-header-fixed-wrapper">
											<table  class="tbl-header-fixed">
												<colgroup>
													<col style="width:65%;">
													<col style="width:35%;">
												</colgroup>
												<thead>
													<tr>
														<th style="width: 100%" colspan="2"><div class="th-text text-center"><spring:message code="msg.auth_db_list" text="db list"/></div></th>
													</tr>
												</thead>
												<tbody>
													<tr v-for="(item,index) in clickItemDbList">
														<td class="text-left">{{item.VNAME}}</td>
														<td >
															<template v-if="(item.MANAGER_DB_CNT > 1)">
																MANAGER
															</template>
															<template v-else>
																<template v-if="(item.MANAGER_CNT > 0 || clickItemCustom.isAdmin)">
																	<template v-if="item.BLOCK_YN == 'N'">
																		<button type="button" class="btn btn-xs btn-danger" @click="dbBlockInfo(item, 'block')"><spring:message code="label.block" text="차단"/></button>
																	</template>
																	<template v-else>
																		<button type="button" class="btn btn-xs btn-primary" @click="dbBlockInfo(item, 'cancel')"><spring:message code="label.release" text="해제"/></button>
																	</template>
																</template>
																<template v-else>{{item.BLOCK_YN=='N'?'Y':'N'}}</template>
															</template>
														</td>
													</tr>
													<tr v-if="clickItemDbList.length === 0">
														<td colspan="2"><div class="text-center"><spring:message code="msg.nodata"/></div></td>
													</tr>
												</tbody>
											</table>
										</div>
									</div>
								</div>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
			<!-- /.panel-body -->
		</div>
	</div>
	<!-- /.col-lg-12 -->
	
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
		,currentItem :{}
		,selectItem :[]
		,dbGroup : []
		,clickItem :{}
		,clickItemDbList :[]
	}
	,computed :{
		selectAllCheck : function (){
			return this.gridData.length > 0 && this.gridData.length == this.selectItem.length; 
		}
	}
	,methods:{
		selectAll : function (){
			if(this.selectAllCheck){
				this.selectItem = []; 
			}else{
				this.selectItem = [];
				
				for(var i =0 ;i <this.gridData.length; i++){
					this.selectItem.push(this.gridData[i].VIEWID)
				}
			}
		}
		,init : function(){
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
				VARSQLUI.alert.open('<spring:message code="msg.data.select" />');
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
				,url : {type:VARSQL.uri.manager, url:'/user/acceptYn'}
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
				,url : {type:VARSQL.uri.manager, url:'/user/blockYn'}
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
				url : {type:VARSQL.uri.manager, url:'/user/userList'}
				,data : param
				,success: function(resData) {
					_self.gridData = resData.items;
					_self.pageInfo = resData.page;
				}
			})
		}
		// remove db group
		,removeDbGroupInfo : function (item){
			var _self = this; 
			var param = item;
			
			if(!confirm(VARSQL.messageFormat('varsql.m.0005', {itemName : item.GROUP_NAME}))){
				return ;; 
			}
			
			this.$ajax({
				url : {type:VARSQL.uri.manager, url:'/user/removeDbGroup'}
				,data : param
				,success: function(resData) {
					_self.detailView(_self.currentItem);
				}
			})
		}
		// 상세보기
		,detailView : function(item){
			var _self = this; 
			_self.currentItem = item; 
			
			this.$ajax({
				url : {type:VARSQL.uri.manager, url:'/user/userDetail'}
				,data : item
				,loadSelector : '#main-content'
				,success: function(resData) {
					_self.clickItem =resData.item;
					_self.clickItemDbList =resData.items;
					_self.clickItemCustom =resData.customs;
					_self.dbGroup =resData.customs.dbGroup;
					
					//_self.detailDialog.dialog("open");
				}
			})
		}
		// pasword 초기화
		,initPassword :function(sItem){
			this.$ajax({
				url : {type:VARSQL.uri.manager, url:'/user/initPassword'}
				,data : sItem
				,success: function(resData) {
					sItem.INITPW = resData.item;
					
					alert(VARSQL.messageFormat('varsql.m.0001'));
					
					setTimeout(function (){
						sItem.INITPW ='';
					}, 5000);
				}
			})
		}
		,dbBlockInfo : function (item, mode){
			
			var confirmMsg =this.clickItem.UNAME +' ['+item.VNAME+']';
			
			if(mode=='block'){
				confirmMsg += VARSQL.messageFormat('varsql.m.0003');
			}else{
				confirmMsg += VARSQL.messageFormat('varsql.m.0004');
			}
			
			if(!confirm(confirmMsg)){
				return ; 
			}
			
			var param = VARSQL.util.objectMerge({},item);
			param.mode = mode; 
			param.VIEWID = this.clickItem.VIEWID;
			this.$ajax({
				url : {type:VARSQL.uri.manager, url:'/user/dbBlockInfo'}
				,data : param
				,success: function(resData) {
					if(resData.item > 0){
						if(mode=='block'){
							item.BLOCK_YN = 'Y';
						}else{
							item.BLOCK_YN = 'N';
						}
					}
				}
			})
		}
	}
});

}());
</script>
