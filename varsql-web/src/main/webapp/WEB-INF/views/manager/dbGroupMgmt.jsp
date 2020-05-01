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
									<td :title="item.regId"><div class="text-ellipsis">{{item.regInfo.viewName}}</div></td>
									<td>{{item.regDt}}</td>
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
								<button type="button" class="btn btn-default" :class="(isViewMode?'':'hide')" @click="fieldClear()"><spring:message code="btn.add"/></button>
								<button type="button" class="btn btn-default" @click="saveInfo()"><spring:message code="btn.save"/></button>
								<button type="button" class="btn btn-danger" :class="(isViewMode?'':'hide')" @click="deleteInfo()"><spring:message code="btn.delete"/></button>
							</div>
						</div>
					</div>
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
		
		<div class="panel panel-default" :class="isViewMode ?'' :'hidden'" >
			<div class="panel-heading"><spring:message code="manage.dbgroup.mapping" /></div>
			<!-- /.panel-heading -->
			<div class="panel-body">
				<div class="col-sm-5">
					<ul id="source" class="form-control" style="width:100%;height:200px;">
					  <li><spring:message code="msg.nodata" /></li>
					</ul>
				</div>
				<div class="col-sm-2" style="text-align:center;padding:10px;height:200px;">
					<div style="margin: 0 auto;">
						<a href="javascript:;" class="btn_m mb05 item-move" mode="add">
							<spring:message code="label.add" text="add"/><span class="fa fa-caret-right"></span>
						</a>
						<br/>
						<a href="javascript:;" class="btn_m mb05 item-move" mode="del">
							<span class="fa fa-caret-left"></span><spring:message code="label.delete" text="del" />
						</a>
					</div>
				</div>
				<div class="col-sm-5">
					<ul id="target"  class="form-control" style="width:100%;height:200px;">
					  <li><spring:message code="msg.nodata" /></li>
					</ul>
				</div>
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
		,selectObj : {}
	}
	,beforeMount: function() {
		this.fieldClear();
	}
	,methods:{
		init : function (){
			var _self = this;
			
			this.initDbMappingInfo();
			
			$('.item-move').on('click',function (){
				var moveItem = [];
				var mode = $(this).attr('mode');
				if(mode =='add'){
					moveItem = _self.selectObj.sourceMove();
				}else{
					moveItem = _self.selectObj.targetMove();
				}
			});
			
			_self.selectObj= $.pubMultiselect('#source', {
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
					optVal : 'vconnid'
					,optTxt : 'vname'
					,items : []
				}
				,targetItem : {
					optVal : 'vconnid'
					,optTxt : 'vname'
					,items : []
				}
				,compleateSourceMove : function (moveItem){
					if($.isArray(moveItem)){
						_self.addDbGroupMappingInfo('add', moveItem);
					}
				}
				,compleateTargetMove : function (moveItem){
					if($.isArray(moveItem)){
						_self.addDbGroupMappingInfo('del', moveItem);
					}
				}
			}); 
		}
		// 추가.
		,fieldClear : function (){
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
			
			if(!confirm(VARSQL.messageFormat('varsql.m.0006', _self.detailItem))){
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
		,initDbMappingInfo: function (){
			var _self = this;
			var param = {
				'searchVal':''
			};
			
			this.$ajax({
				url : {type:VARSQL.uri.manager, url:'/comm/dbList'}
				,data : param
				,success: function(resData) {
					_self.selectObj.setItem('source', resData.items);
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
				,url : {type:VARSQL.uri.manager, url:'/dbGroup/dbGroupMappingList'}
				,success:function (resData){
					var result = resData.items;
		    		_self.selectObj.setItem('target', result);
				}
			});
		}
		// 맵핑 정보 추가. 
		,addDbGroupMappingInfo : function (mode, moveItem){
			var _self = this;
			
			if(this.isViewMode ===false) return ; 

			var param ={
				selectItem : moveItem.join(',')
				,groupId : this.detailItem.groupId
				, mode : mode
			};
			
			VARSQL.req.ajax({
				data:param
				,url : {type:VARSQL.uri.manager, url:'/dbGroup/addDbGroupMappingInfo'}
				,success:function (response){
					_self.dbMappingInfo();
				}
			});
		}
	}
});

}());
</script>
