<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/include/tagLib.jspf"%>
<!-- Page Heading -->
<div class="row">
    <div class="col-lg-12">
        <h1 class="page-header"><spring:message code="manager.menu.executestat" /></h1>
    </div>
    <!-- /.col-lg-12 -->
</div>
<div class="row display-off" id="epViewArea">
	<div class="col-lg-12">
		<div class="panel panel-default">
			<!-- /.panel-heading -->
			<div class="panel-body">
				<div class="table-responsive">
					<button type="button" class="btn btn-danger" @click="allCancel()">All Cancel</button>
					<div class="pull-right" style=" padding: 5px 0px;">
						
						<span style="color: #ec4343;font-weight: bold;">{{message}}</span>
						<span style="padding-right:10px;">{{refreshTime > 0 ? refreshTime : 'refresh...' }}</span>
						<span class="fa fa-refresh">Refresh Sec</span>
						<label style="margin:0px 0px 0px 5px;">
							<select class="form-control " v-model="refreshSettingTime" @change="changeRefreshTime()">
								<option v-for="(item,index) in refreshSettingArr" :value="item">{{item}}</option>
							</select>
						</label>
						초
						<button type="button" class="btn btn-primary" @click="stopStart()">{{isStart ?'Stop':'Start'}}</button>
					</div>
					<div id="dataTables-example_wrapper"
						class="dataTables_wrapper form-inline" role="grid">
						<table
							class="table table-striped table-bordered table-hover dataTable no-footer"
							id="dataTables-example" style="table-layout:fixed;">
							<colgroup>
								<col style="width: 270px;"> 
								<col style="width: 100px;"> 
								<col style="width: 100px;"> 
								<col style="width: 118px;"> 
								<col> 
								<col style="width: 60px;">
							</colgroup>
							<thead>
								<tr role="row">
									<th class="text-center"><spring:message	code="requid" text="Req Uid" /></th>
									<th class="text-center"><spring:message	code="threadid" text="Thread id" /></th>
									<th class="text-center"><spring:message	code="ip" /></th>
									<th class="text-center"><spring:message	code="std_time" /></th>
									<th class="text-center"><spring:message code="sql" text="Sql" /></th>
									<th class="text-center"></th>
								</tr>
							</thead>
							<tbody class="dataTableContent">
								<tr v-for="(item,index) in gridData" class="gradeA" :class="(item.ERROR_LOG && item.ERROR_LOG != '')?'error-row':(index%2==0?'add':'even')">
									<td><a href="javascript:;" @click="itemView(item)"><div class="text-ellipsis ellipsis10">{{item.reqUid}}</div></a></td>
									<td><div class="text-ellipsis ellipsis5">{{item.threadId}}</div></td>
									<td :title="item.USR_IP"><div class="text-ellipsis ellipsis5">{{item.ip}}</div></td>
									<td>
										{{item.startTime}}
									</td>
									<td>
										<template v-for="(stmtInfo,index) in item.executeStatements">
											<div><pre>{{stmtInfo.sql}}</pre></div>
										</template>
									</td>
									<td>
										<button type="button" class="btn btn-sm btn-default" @click="requestCancel(item)"><spring:message code="cancel"/></button>
									</td>
									
								</tr>
								<tr v-if="gridData.length === 0">
									<td colspan="6"><div class="text-center"><spring:message code="msg.nodata"/></div></td>
								</tr>
							</tbody>
						</table>
					</div>
				</div>
			</div>
			<!-- /.panel-body -->
		</div>
	</div>
</div>
<!-- /.row -->

<script>

VarsqlAPP.vueServiceBean( {
	el: '#epViewArea'
	,data: {
		list_count :10
		,refreshSettingArr : [5,10,15,20,25,30,35,40,45,50,55,60]
		,refreshSettingTime : 5
		,refreshTime : -1
		,searchVal : ''
		,message:''
		,refreshTimer : -1
		,initFlag:false
		,isStart : true
		,gridData :  []
		,detailItem :{}
		,fileViewEditor :{}
	}
	,methods:{
		init : function() {
			
		}
		,stopStart : function (){
			
			this.isStart = !this.isStart; 
			
			if(this.isStart){
				this.changeRefreshTime();
			}else{
				this.refreshTime =0;
				clearInterval(this.refreshTimer);
			}
		}
		// refresh
		,changeRefreshTime: function(){
			var _this =this; 
			
			clearInterval(this.refreshTimer);
			
			this.refreshTime = this.refreshSettingTime;
			
			this.refreshTimer = setInterval(function() {
				--_this.refreshTime;
				
				if(_this.refreshTime < 1){
					_this.search(function (result){
						
						if(result=='error'){
							clearInterval(_this.refreshTimer);
							_this.message = VARSQL.message('msg.refresh.please')
							return ; 
						}
						_this.changeRefreshTime()
					});
				}
			}, 1000);
			
		}
		// 검색
		,search : function(callback){
			var _this = this;

			this.$ajax({
				url : {type:VARSQL.uri.manager, url:'/stats/allExecuteList'}
				,method:'get'
				,success: function(resData) {
					_this.gridData = resData.list || [];
					
					if(VARSQL.isFunction(callback)){
						callback('success');
					}
					
					if(_this.initFlag===false){
						_this.initFlag = true; 
						_this.changeRefreshTime();
					}
				}
			})
		}
		// request cancel
		,requestCancel(item){
			if(!VARSQL.confirmMessage('msg.cancel.confirm')){
				return ;
			}
			VARSQL.databaseRequestCancel(item.reqUid)
		}
		,allCancel(item){
			if(!VARSQL.confirmMessage('['+VARSQL.message('all')+'] '+VARSQL.message('msg.cancel.confirm'))){
				return ;
			}
			VARSQL.databaseRequestCancel('all');
		}
	}
});
</script>
