<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/include/tagLib.jspf"%>

<!-- Page Heading -->
<div class="row">
    <div class="col-lg-12">
        <h1 class="page-header"><spring:message code="user.menu.edit.message" /></h1>
    </div>
    <!-- /.col-lg-12 -->
</div>
<div class="row display-off" id="epViewArea">
	<div class="col-xs-6">
		<div class="panel panel-default">
			<div class="panel-heading">
				<label>
					<input type="radio" value="recv" v-model="message_type" @change="search()"><spring:message code="label.recv.msg" text="받은메시지" />
				</label>
				<label>
					<input type="radio" value="send" v-model="message_type" @change="search()"><spring:message code="label.send.msg" text="보낸메시지"/>
				</label>
			</div>
			<!-- /.panel-heading -->
			<div class="panel-body">
				
				<div class="row">
					<div class="col-xs-12">
						
					</div>
				</div>
				<div class="row">
					<div class="col-sm-6">
						<label> 
							<button type="button" class="btn btn-sm btn-primary" @click="deleteMsg()"><spring:message code="label.delete" /></button>
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
								<input type="text" v-model="searchVal"	class="form-control">
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
						<table :class="(message_type=='recv'?'view':'hidden')"
							class="table table-striped table-bordered table-hover dataTable no-footer"
							id="dataTables-example">
							<thead>
								<tr role="row">
									<th  style="width: 10px;">
										<!-- <div class="text-center"><input type="checkbox" @click="allCheck(this)"></div> -->
									</th>
									<th style="width: 195px;">
										<spring:message	code="user.edit.msgtitle" />
									</th>
									<th style="width: 150px;">
										<spring:message	code="user.edit.send_id" />
									</th>
									<th style="width: 150px;">
										<spring:message	code="reg_dt" />
									</th>
									<th style="width: 179px;">
										<spring:message	code="view_dt" />
									</th>
								</tr>
							</thead>
							<tbody class="dataTableContent">
								<tr v-for="(item,index) in gridData" class="gradeA" :class="(index%2==0?'add':'even')">
									<td><input type="checkbox" :value="item.MEMO_ID" v-model="selectItem"></td>
									<td><a href="javascript:;" @click="viewItem(item)"> {{item.MEMO_TITLE}} </a></td>
									<td class="center">{{item.SEND_NM}}</td>
									<td class="center">{{item.REG_DT}}</td>
									<td class="center">{{item.VIEW_DT}}</td>
								</tr>
								<tr v-if="gridData.length === 0">
									<td colspan="10"><div class="text-center"><spring:message code="msg.nodata"/></div></td>
								</tr>
							</tbody>
						</table>
						
						<table :class="(message_type=='send'?'view':'hidden')"
							class="table table-striped table-bordered table-hover dataTable no-footer"
							id="dataTables-example">
							<thead>
								<tr role="row">
									<th  style="width: 10px;">
										<!-- <div class="text-center"><input type="checkbox" @click="allCheck(this)"></div> -->
									</th>
									<th style="width: 195px;">
										<spring:message	code="user.edit.msgtitle" />
									</th>
									<th style="width: 150px;">
										<spring:message	code="reg_dt" />
									</th>
								</tr>
							</thead>
							<tbody class="dataTableContent">
								<tr v-for="(item,index) in gridData" class="gradeA" :class="(index%2==0?'add':'even')">
									<td><input type="checkbox" :value="item.MEMO_ID" v-model="selectItem"></td>
									<td><a href="javascript:;" @click="viewItem(item)"> {{item.MEMO_TITLE}} </a></td>
									<td class="center">{{item.REG_DT}}</td>
								</tr>
								<tr v-if="gridData.length === 0">
									<td colspan="10"><div class="text-center"><spring:message code="msg.nodata"/></div></td>
								</tr>
							</tbody>
						</table>
						
						<page-navigation :page-info="pageInfo" callback="goPage"></page-navigation>
					</div>
				</div>
			</div>
			<!-- /.panel-body -->
		</div>
		<!-- /.panel -->
	</div>
	<div class="col-xs-6">
		<div class="panel panel-default">
			<div class="panel-heading"><spring:message code="detail.view" /></div>
			<!-- /.panel-heading -->
			<div class="panel-body">
				<form id="addForm" name="addForm" class="form-horizontal" onsubmit="return false;">
					<div class="form-group">
						<div class="col-sm-12">
							<div class="form-control text required">{{detailItem.MEMO_TITLE}}</div>
						</div>
					</div>
					<div class="form-group">
						<div class="col-sm-12">
							<textarea class="form-control text required" rows="5" readonly="readonly">{{detailItem.MEMO_CONT}}</textarea>
						</div>
					</div>
					
					<div v-if="message_type=='send'">
					 	<div><spring:message code="label.recv.user" text="받는사람"/></div>
						<div class="form-group">
							<div class="col-sm-12">
								<div style="height:100px;overflow:auto;border:1px solid #ddd;">
								<div v-for="(item,index) in detailItem.RECV_USER">
									 {{item.RECV_NM}} <span>(</span> {{item.UID}}<span>)</span>
								</div>
								</div>
							</div>
						</div>
					</div>
					<div v-else>
						<div class="pull-right" style="margin-bottom:10px;">
							<button type="button" class="btn btn-sm btn-primary" @click="resendMemo()"><spring:message code="reply" text="답장"/></button>
						</div>
						<textarea class="form-control text required" rows="5" v-model="detailItem.RE_MEMO_CONT"></textarea>
						
						<div style="margin-top:10px;">
							<table 
								class="table table-striped table-bordered table-hover dataTable no-footer"
								id="dataTables-example" style="table-layout:fixed;">
								<colgroup>
									<col style="width:calc(100% - 140px);">
									<col style="width:140px;">
									
								</colgroup>
								<thead>
									<tr role="row">
										<th>
											<spring:message	code="content" />
										</th>
										<th>
											<spring:message	code="reg_dt" />
										</th>
									</tr>
								</thead>
								<tbody class="dataTableContent">
									<tr v-for="(item,index) in replyList" class="gradeA" :class="(index%2==0?'add':'even')">
										<td>
											<a href="javascript:;" @click="replyViewItem(item)" class="text-ellipsis">{{item.MEMO_CONT}}</a>
											<textarea rows="5" class="wh100" :class="item._visible==true?'view':'hidden'">{{item.MEMO_CONT}}</textarea>
										</td>
										<td class="center">{{item.REG_DT}}</td>
									</tr>
									<tr v-if="replyList.length === 0">
										<td colspan="2"><div class="text-center"><spring:message code="msg.nodata"/></div></td>
									</tr>
								</tbody>
							</table>
						</div>
					</div>
				</form>
			</div>
			<!-- /.panel-body -->
		</div>
	</div>
	<!-- /.col-lg-12 -->
</div>
<!-- /.row -->

<script>

VarsqlAPP.vueServiceBean( {
	el: '#epViewArea'
	,data: {
		list_count :10
		,message_type : 'recv'
		,searchVal : ''
		,pageInfo : {}
		,gridData :  []
		,detailItem :{}
		,selectItem :[]
		,replyList :[]
		,reMemoItem :{}
	}
	,methods:{
		deleteMsg : function(){
			var _self = this; 
			var selectItem = _self.selectItem;
			
			if(VARSQL.isDataEmpty(selectItem)){
				VARSQLUI.alert.open('<spring:message code="msg.data.select" />');
				return ; 
			}
			
			if(!confirm('삭제 하시겠습니까?')){
				return ; 
			}
			
			var param = {
				message_type : _self.message_type
				,selectItem:selectItem.join(',')
			};
			
			this.$ajax({
				data:param
				,url : {type:VARSQL.uri.user, url:'/preferences/deleteMsg'}
				,success:function (response){
					_self.search();
				}
			});
		}
		,viewItem : function (item){
			var _self =this; 
			item.RE_MEMO_CONT ='';
			this.detailItem = item;
			this.replyList = [];
			
			this.$ajax({
			    url:{type:VARSQL.uri.user, url:'/preferences/msgReplyList.varsql'}
			    ,data : VARSQL.util.getConvertCamelObject(item) 
			    ,success:function (resData){
			    	_self.replyList = resData.items;
				}
			});
		}
		,allCheck : function (sEle){
			console.log($(sEle));
		}
		,resendMemo  : function (){
			var _this =this; 
			
			var params = VARSQL.util.getConvertCamelObject(this.detailItem);
			params.recvId = 'resend';
			
			this.$ajax({
			    url:{type:VARSQL.uri.user, url:'/resendMemo.varsql'}
			    ,data:params 
			    ,success:function (resData){
			    	VARSQLUI.toast.open('저장 되었습니다.');
			    	
			    	_this.viewItem(_this.detailItem)
				}
			});
		}
		,replyViewItem : function (item){
			this.reMemoItem = item; 
			
			Vue.set(item, '_visible',  !item._visible)
		}
		,search : function(no){
			var _self = this; 
			
			this.detailItem = {};
			this.replyList = [];
			
			var param = {
				pageNo : (no?no:1)
				,message_type : _self.message_type
				,rows: _self.list_count
				,'searchVal':_self.searchVal
			};
			
			this.$ajax({
				url : {type:VARSQL.uri.user, url:'/preferences/listMsg'}
				,data : param
				,success: function(resData) {
					_self.gridData = resData.items;
					_self.pageInfo = resData.page;
				}
			})
		}
	}
});
</script>
