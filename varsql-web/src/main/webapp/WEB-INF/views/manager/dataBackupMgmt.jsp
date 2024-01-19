<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/include/tagLib.jspf"%>

<!-- Page Heading -->
<div class="row">
    <div class="col-lg-12">
        <h1 class="page-header"><spring:message code="manager.menu.databackupgmgmt"/></h1>
    </div>
    <!-- /.col-lg-12 -->
</div>
<div class="row display-off" id="varsqlVueArea">
	<div class="col-lg-6">
		<div class="panel panel-default">
			<!-- /.panel-heading -->
			<div class="panel-body">
				<div class="pull-right search-area">
					<div class="dataTables_filter">
						<div class="input-group floatright">
							<input type="text" v-model="searchVal" class="form-control " @keyup.enter="search()" autofocus="autofocus" placeholder="Search...">
							<span class="input-group-btn">
								<button class="btn btn-default" @click="search()" type="button">
									<span class="glyphicon glyphicon-search"></span>
								</button>
							</span>
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
								<col style="width:100px">
								<col style="min-width:50px;width:*;">
								<col style="width:100px;">
								<col style="width:145px;">
								<col style="width:75px;">
							</colgroup>
							<thead>
								<tr role="row">
									<th class="text-center"><spring:message	code="manager.backupmgmt.connection" text="Connection"/></th>
									<th class="text-center"><spring:message	code="manager.backupmgmt.jobname" text="Job Name" /></th>
									<th class="text-center"><spring:message	code="manager.backupmgmt.expression" text="Expression" /></th>
									<th class="text-center"><spring:message	code="operation" text="Operation" /></th>
									<th class="text-center"></th>
								</tr>
							</thead>

							<tbody class="dataTableContent">
								<tr v-for="(item,index) in gridData" class="gradeA cursor-pointer" :class="(index%2==0?'add':'even')">
									<td :title="item.vname"><div class="text-ellipsis ellipsis0">{{item.vname}}</div></td>
									<td :title="item.jobName">
										<div class="text-ellipsis ellipsis0"><a href="javascript:;" @click="itemView(item)">{{item.jobName}}</a></div><br/>
										<div class="text-ellipsis ellipsis0">{{item.jobDescription}}</div>
									</td>
									<td :title="item.cronExpression"><div class="text-ellipsis ellipsis0">{{item.cronExpression}}</div></td>
									<td>
										<div class="margin-bottom5 text-right"><button class="btn btn-sm btn-info" @click="jobCtrl(item, 'RUN')">Run</button></div>
										<div>
											<button class="btn btn-sm btn-default" @click="jobCtrl(item, 'PAUSE')">Pause</button>
											<button class="btn btn-sm btn-default" @click="jobCtrl(item, 'RESUME')">Resume</button>
										</div>
									</td>
									<td>
										<button class="btn btn-sm btn-default" @click="historyView(1, item)">History</button>
									</td>
								</tr>
								<tr v-if="gridData.length === 0">
									<td colspan="5"><div class="text-center"><spring:message code="msg.nodata"/></div></td>
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
	<div class="col-lg-6" >
		<div class="panel panel-default detail_area_wrapper" >
			<div class="panel-heading"><spring:message code="manager.backupmgmt.form.header" text="Job 관리"/><span style="margin-left:10px;font-weight:bold;">{{detailItem.jobName}}</span></div>
			
			<!-- /.panel-heading -->
			<div class="panel-body">
				<div class="form-group"  style="height: 34px;margin-bottom:10px;">
					<div class="col-sm-12">
						<div class="pull-right">
							<template v-if="viewMode=='form'">
								<button type="button" class="btn btn-default" @click="setDetailItem()"><spring:message code="btn.add"/></button>
								<button type="button" class="btn btn-default" @click="save()"><spring:message code="btn.save"/></button>
	
								<template v-if="detailFlag===true">
									<button type="button" class="btn btn-danger"  @click="deleteInfo()"><spring:message code="btn.delete"/></button>
								</template>
							</template>
							<template v-else>
								<button type="button" class="btn btn-default"  @click="viewArea('form')"><spring:message code="btn.close"/></button>
							</template>
						</div>
					</div>
				</div>

				<form id="addForm" name="addForm" class="form-horizontal" onsubmit="return false;">
					<div class="view-area" :class="viewMode=='form'?'on':''">
						<div class="form-group" :class="errors.has('NAME') ? 'has-error' :''">
							<label class="col-sm-3 control-label"><spring:message code="manager.backupmgmt.jobname"  /></label>
							<div class="col-sm-9">
								<input type="text" v-model="detailItem.jobName" v-validate="'required'" name="NAME" class="form-control" />
								<div v-if="errors.has('NAME')" class="help-block">{{ errors.first('NAME') }}</div>
							</div>
						</div>
						
						<div class="form-group" :class="errors.has('EXPRESSION') ? 'has-error' :''">
							<label class="col-sm-3 control-label"><spring:message code="manager.backupmgmt.expression"  /></label>
							<div class="col-sm-9">
								<input type="text" v-model="detailItem.cronExpression" v-validate="'required'" name="EXPRESSION" placeholder="0 0 3 * * ? " class="form-control" />
								<div>
									<span>ex :0 0 3 * * ? </span> <br/>
									<span>Seconds Minutes Hours Day Month Week Year</span>
								</div>
								<div v-if="errors.has('EXPRESSION')" class="help-block">{{ errors.first('EXPRESSION') }}</div>
							</div>
						</div>
						
						<div class="form-group" :class="errors.has('NAME') ? 'has-error' :''">
							<label class="col-sm-3 control-label"><spring:message code="export.info" /></label>
							<div class="col-sm-9">
								<div class="margin-bottom5">
									<label><spring:message code="charset"  /></label>
									<input type="text" v-model="detailItem.jobData.charset" class="form-control" />
								</div>
								
								<div class="margin-bottom5">
									<label><spring:message code="type"  /></label>
									<select class="form-control" v-model="detailItem.jobData.exportType">
										<option v-for="(val,key) in exportTypeList" :value="key">{{val}}</option>
									</select>
								</div>
								
								<div class="margin-bottom5">
									<label><spring:message code="manager.backupmgmt.connection"  /></label>
									<div>
										<select class="form-control" v-model="detailItem.vconnid" @change="getSchemaInfo(detailItem.vconnid)">
											<option value=""><spring:message code="select" text="선택"/></option>
											<option v-for="(item,index) in dbList" :value="item.vconnid">{{item.vname}}</option>
										</select>
									</div>
								</div>
								
								<div class="margin-bottom5">
									<label>
										<spring:message code="schema" text="Schema" />
										
										<input type="checkbox" v-model="detailItem.jobData.isAllData" /> <spring:message code="all.table" text="All Table" />
									</label>
									<select id="schemaList" class="form-control" v-model="detailItem.jobData.schema" @change="getObjectInfo(detailItem.vconnid, detailItem.jobData.schema)">
										<option v-for="(val,key) in schemaList" :value="val">{{val}}</option>
									</select>
									
									<div id="backupList" style="height: 200px;padding:10px 0px;" v-show="!detailItem.jobData.isAllData"></div>
									<div v-show="detailItem.jobData.isAllData" style="height: 35px;padding: 10px 0px;text-align: center;vertical-align: middle;background: #ededed;margin: 10px 0px;">
										<spring:message code="all.table" text="All Data" />
									</div>
								</div>
							</div>
						</div>
						
						<div class="form-group">
							<label class="col-sm-3 control-label"><spring:message code="desc" /></label>
							<div class="col-sm-9">
								<textarea v-model="detailItem.jobDescription" rows="3" class="form-control" /></textarea>
							</div>
						</div>
						
						<div v-show="detailFlag">
							<div class="row">
								<label class=" control-label"><spring:message code="trigger.info" text="Trigger Info" /></label> 
								<div id="triggerList"></div>
							</div>
						</div>
					</div>
					
					<div class="view-area" :class="viewMode=='log'?'on':''">
						<div class="row">
							<label class=" control-label"><spring:message code="history" text="History" /></label> 
							<div id="historyList"></div>
						</div>
					</div>
				</form>
			</div>
			<!-- /.panel-body -->
		</div>
		<!-- /.panel -->
	</div>
	<!-- /.col-lg-8 -->

</div>
<!-- /.row -->

<script>

VarsqlAPP.vueServiceBean( {
	el: '#varsqlVueArea'
	,validateCheck : true
	,data: {
		dbList : ${varsqlfn:objectToJson(dbList)}
		,detailFlag :false
		,searchVal : ''
		,pageInfo : {}
		,gridData :  []
		,detailItem :{jobData:{}}
		,schemaList :[]
		,viewMode : 'form'
		,exportTypeList : {
			'xml' : 'XML'
			,'json' : 'JSON'
			,'csv' : 'CSV'
			,'sql' : 'Insert Query'
		}
		,selectObj : {}
		,historyItem : {}
	}
	,methods:{
		init : function(){
			var _self = this; 
			
			this.selectObj= $.pubMultiselect('#backupList', {
				duplicateCheck : true
				,header : {
					enableSourceLabel : true 	// source header label 보일지 여부
					,enableTargetLabel : false 	// target header label 보일지 여부
				}
				,message :{
					duplicate: VARSQL.message('varsql.0018')
				}
				,valueKey : 'name'	
				,labelKey : 'name'
				,source : {
					items : []
					,search :{
						enable : true
					}
				}
				,target : {
					items : []
				}
			});
			
			
			this.setDetailItem('init');
		}
		,search : function(no){
			var _this = this;

			var param = {
				pageNo: (no?no:1)
				,rows: _this.list_count
				,'searchVal':_this.searchVal
			};

			this.$ajax({
				url : {type:VARSQL.uri.manager, url:'/dataBackup/list'}
				,data : param
				,success: function(resData) {
					_this.gridData = resData.list;
					_this.pageInfo = resData.page;
				}
			})
		}
		// 상세보기
		,itemView : function(item){
			var _this = this;
			
			if(this.viewMode != 'form'){
				this.viewArea('form');	
			}
			
			if(_this.detailItem.jobUid == item.jobUid){
				return ;
			}

			var param = {
				jobUid : item.jobUid
			}

			_this.errors.clear();

			this.$ajax({
				url : {type:VARSQL.uri.manager, url:'/schedulerMgmt/detail'}
				,data : param
				,loadSelector : '#addForm'
				,success: function(resData) {
					var resItem  = resData.item;
					
					_this.setDetailItem(resItem);
				}
			})
		}
		// 상세 셋팅
		,setDetailItem : function (item){
			this.viewArea('form');

			if(item =='init' || VARSQL.isUndefined(item)){
				this.$validator.reset()
				this.detailFlag = false;
				this.detailItem ={
					jobUid : ''
					,jobName : ''
                    ,cronExpression : ''
                    ,vconnid :''
                    ,jobDescription : ''
                    ,jobData :{
                    	charset : 'utf-8'
                    	, exportType : 'xml'
                    	, schema : ''
                    	, isAllData : false
                    	, exportItems : []
                    }
				}
				this.getObjectInfo('','');
			}else{
				
				var jobData = {
                   	charset : 'utf-8'
                 	, exportType : 'xml'
                 	, exportItems : []
				};
				
				try{
					jobData = VARSQL.parseJSON(item.jobData);
				}catch(e){
					console.log(e);
				}
				
				jobData.isAllData = VARSQL.isUndefined(jobData.isAllData) ? false : jobData.isAllData; 
				
				item.jobData = jobData;
					
				this.detailFlag = true;
				this.detailItem = item;
				
				this.$nextTick(function (){
					this.setTriggerGrid(item);
				});
				
				this.getSchemaInfo(item.vconnid);
				this.getObjectInfo(item.vconnid, item.jobData.schema);

                this.selectObj.setTargetItem(item.jobData.exportItems);
			}
		}
		// save
		,save : function (mode){
			var _this = this;

			this.$validator.validateAll().then(function (result){
				if(result){
					var item = _this.getParamVal();
					
					if(VARSQL.isBlank(item.vconnid)){
						VARSQL.toastMessage('item.select.message', '<spring:message code="manager.backupmgmt.connection"  />');
						return ;
					}
					
					if(item.jobData.isAllData){
						item.jobData.exportItems = [];
					}else{
						var backupItems = _this.selectObj.getTargetItem(); 
						
						if(backupItems.length < 1){
							VARSQL.toastMessage('varsql.0034', 'Data Backup');
							return ;
						}
						
						item.jobData.exportItems = backupItems.map(item=>{
							return {'name' : item.name} 
						})
					}
					
					item.jobData =  JSON.stringify(item.jobData);
					
					if(!VARSQL.confirmMessage('varsql.0019')){
						return ;
					}

					_this.$ajax({
						url : {type:VARSQL.uri.manager, url:'/dataBackup/save'}
						,data: item
						,success:function (resData){
							
							if(VARSQL.req.validationCheck(resData)){
								if(resData.resultCode != 200){
									VARSQL.alertMessage(resData.message);
									return ;
								}
								
								_this.setDetailItem();
								_this.search();
							}
						}
					});
				}
			});
		}
		,getParamVal : function (item){
			return VARSQL.util.objectMerge({}, this.detailItem);
		}
		// 정보 삭제 .
		,deleteInfo : function (){
			var _this = this;

			if(typeof this.detailItem.jobUid ==='undefined'){
				VARSQL.toastMessage('varsql.0004');
				return ;
			}

			if(!VARSQL.confirmMessage('varsql.0016')){
				return ;
			}

			this.$ajax({
				url : {type:VARSQL.uri.manager, url:'/schedulerMgmt/delete'}
				,data: {
					jobUid : _this.detailItem.jobUid
				}
				,success:function (resData){
					_this.setDetailItem();
					_this.search();
				}
			});
		}
		// edit form, log
		,viewArea :function (mode){
			this.viewMode = mode;
		}
		,historyView : function (no, item){
			var _this = this;
			this.viewArea('log');
			
			if(item){
				this.historyItem = item;	
			}
			
			this.$ajax({
				url : {type:VARSQL.uri.manager, url:'/schedulerMgmt/history'}
				,data : {
					jobUid : this.historyItem.jobUid
					,countPerPage : 15
					,pageNo: no
				}
				,success:function (resData){
					_this.$nextTick(function (){
						_this.setHistoryGrid(resData);
					});
					
				}
			});
			
		}
		// schema info
		,getSchemaInfo : function (id){
			var _self = this;
			var param = {
				vconnid : id
			};
			
			if(VARSQL.isBlank(id)){
				_self.selectObj.setSourceItem([]);
				_self.selectObj.setTargetItem([]);
				return ;
			}

			this.$ajax({
				url : {type:VARSQL.uri.manager, url:'/dataBackup/schemaList'}
				,loadSelector : '#schemaList'
				,data : param
				,success:function (resData){
		    		_self.schemaList = resData.list;
				}
			});
		}
		// get backup object info
		,getObjectInfo : function (id, schema){
			var _self = this;
			
			if(VARSQL.isBlank(id)){
				_self.selectObj.setSourceItem([]);
				_self.selectObj.setTargetItem([]);
				return ;
			}
			
			if(VARSQL.isBlank(schema)){
				VARSQLUI.toast.open(VARSQL.message('Select schema'));
				return ;
			}
			
			var param = {
				vconnid : id
				,schema : schema
			};

			this.$ajax({
				url : {type:VARSQL.uri.manager, url:'/dataBackup/dataObjectList'}
				,loadSelector : '#backupList'
				,data : param
				,success:function (resData){
		    		_self.selectObj.setSourceItem(resData.list);
				}
			});
		}
		// job control
		,jobCtrl : function (item, mode){
			var _this = this;
			var param = {
				jobUid : item.jobUid
				,mode : mode
			};
			
			if(VARSQL.isBlank(param.jobUid)){
				VARSQLUI.toast.open(mode + ' '+VARSQL.message('varsql.0006'));
				return ;
			}
			
			if(!VARSQL.confirmMessage('varsql.0035', mode)){
				return ; 
			}
			
			this.$ajax({
				url : {type:VARSQL.uri.manager, url:'/schedulerMgmt/jobCtrl'}
				,data : param
				,success:function (resData){
					if(_this.detailItem.jobUid == item.jobUid){
						$.pubGrid('#triggerList').setData(resData.item.triggerList);
					}
				}
			});
		}
		,setHistoryGrid : function (resData){
			var _this = this;
			
			if($.pubGrid('#historyList')){
				$.pubGrid('#historyList').resizeDraw();
				$.pubGrid('#historyList').setData({
					items : resData.list
					,paging : resData.page
				});
				return ; 
			}
			
			$.pubGrid('#historyList',{
				tColItem : [
					{key :'instanceId', label:'InstanceId', width:70}
					,{key :'status', label:'Status', width:70}
					,{key :'runType', label:'Type', width:50}
					,{key :'startTime', label:'Start Time',align:'center', width:150}
					,{key :'endTime', label:'End Time', width:150}
					,{key :'runTime', label:'Run Time', width:50, formatter : function (cellInfo){
						var val = cellInfo.item[cellInfo.colInfo.key]; 
						return (val < 1 ? 0: val)+' sec'; 
					}}
					,{key :'resultCount', label:'Result Count', width:50}
					,{key :'failCount', label:'Fail Count', width:50}
					,{key :'message', label:'Message', width:70}
					,{key :'log', label:'log', width:70}
				]
				,rowOptions:{	// 로우 옵션.
					height: 30	// cell 높이
				}
				,valueFilter : function (colInfo, objValue){
					var val = objValue[colInfo.key];
					if(colInfo.key=='log' && !VARSQL.isBlank(val) && val.length > 1000){
							return val.substring(0, 1000)+'...';
					}else{
						return val;
					}
				}
				,tbodyItem : resData.list
				,height : 520
				,navigation: {
					enablePaging : true
					,callback : function (no){
						_this.historyView(no);
					}
				}
				,paging : resData.page
			})
		}
		,setTriggerGrid : function (){
			
			if($.pubGrid('#triggerList')){
				$.pubGrid('#triggerList').resizeDraw();
				$.pubGrid('#triggerList').setData(this.detailItem.triggerList);
				return ; 
			}
			
			$.pubGrid('#triggerList',{
				tColItem : [
					{key :'triggerName', label:'triggerName', width:70}
					,{key :'triggerState', label:'State',align:'center', width:50}
					,{key :'triggerType', label:'Type', width:50}
					,{key :'startTime', label:'Start Time', width:70, formatter : function (cellInfo){
						var val = cellInfo.item[cellInfo.colInfo.key]; 
						if(val > 0){
							return VARSQL.util.dateFormat(val, $varsqlConfig.dateFormat.yyyyMMddHHmmSS)
						}
						return val; 
					}}
					,{key :'endTime', label:'End Time', width:70, formatter : function (cellInfo){
						var val = cellInfo.item[cellInfo.colInfo.key]; 
						if(val > 0){
							return VARSQL.util.dateFormat(val, $varsqlConfig.dateFormat.yyyyMMddHHmmSS)
						}
						return val; 
					}}
					,{key :'nextFireTime', label:'Next Fire Time', width:70, formatter : function (cellInfo){
						var val = cellInfo.item[cellInfo.colInfo.key]; 
						if(val > 0){
							return VARSQL.util.dateFormat(val, $varsqlConfig.dateFormat.yyyyMMddHHmmSS)
						}
						return val; 
					}}
					,{key :'priority', label:'Priority', width:70}
					,{key :'prevFireTime', label:'Prev Fire Time', width:70, formatter : function (cellInfo){
						var val = cellInfo.item[cellInfo.colInfo.key]; 
						if(val > 0){
							return VARSQL.util.dateFormat(val, $varsqlConfig.dateFormat.yyyyMMddHHmmSS)
						}
						return val; 
					}}
					,{key :'description', label:'설명', width:70}
					
				]
				,tbodyItem : this.detailItem.triggerList
				,height : 150
			})
		}
	}
});
</script>
