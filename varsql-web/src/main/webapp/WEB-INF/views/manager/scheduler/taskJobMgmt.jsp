<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/include/tagLib.jspf"%>

<!-- Page Heading -->
<div class="row">
    <div class="col-lg-12">
        <h1 class="page-header"><spring:message code="manager.menu.taskjobmgmt"/></h1>
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
							<input type="text" v-model="searchVal" class="form-control " @keyup.enter="search()" autofocus="autofocus" placeholder="<spring:message code="search.placeholder" />">
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
								<col style="min-width:50px;width:*;cursor:pointer;">
								<col style="width:100px;">
								<col style="width:145px;">
								<col style="width:75px;">
							</colgroup>
							<thead>
								<tr role="row">
									<th class="text-center"><spring:message	code="name" text="Job Name" /></th>
									<th class="text-center"><spring:message	code="cron.expression" text="Expression" /></th>
									<th class="text-center"><spring:message	code="operation" text="Operation" /></th>
									<th class="text-center"></th>
								</tr>
							</thead>

							<tbody class="dataTableContent">
								<tr v-for="(item,index) in gridData" class="gradeA cursor-pointer" :class="(index%2==0?'add':'even')">
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
			<div class="panel-heading"><spring:message code="detail.view" text="상세보기"/><span style="margin-left:10px;font-weight:bold;">{{detailItem.jobName}}</span></div>
			
			<!-- /.panel-heading -->
			<div class="panel-body">
				<div class="form-group"  style="height: 34px;margin-bottom:10px;">
					<div class="col-sm-12">
						<div class="pull-right">
							<template v-if="viewMode=='form'">
								<button type="button" class="btn btn-default" @click="setDetailItem()"><spring:message code="new"/></button>
								<button type="button" class="btn btn-default" @click="save()"><spring:message code="save"/></button>
	
								<template v-if="detailFlag===true">
									<button type="button" class="btn btn-danger"  @click="deleteInfo()"><spring:message code="delete"/></button>
								</template>
							</template>
							<template v-else>
								<button type="button" class="btn btn-default"  @click="viewArea('form')"><spring:message code="close"/></button>
							</template>
						</div>
					</div>
				</div>

				<div class="view-area" :class="viewMode=='form'?'on':''">
					<form id="addForm" name="addForm" class="form-horizontal" onsubmit="return false;">
					</form>
					
					
					<div style="margin-top:10px;">
						<div>Task Mapping</div>
						<div id="taskMappingId"></div>
					</div>
				</div>
					
				<div class="view-area" :class="viewMode=='log'?'on':''">
					<div class="row">
						<label class="control-label"><spring:message code="history" text="History" /></label> 
						<div id="historyList"></div>
					</div>
				</div>
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
		,viewMode : 'form'
		,historyItem : {}
		,sqlEditor:{}
		,triggerListObj : false
		,taskList :[]
		,mappedTaskList :[]
		,selectObj : null
	}
	,created : function (){
		this.setDetailItem('init');
	}
	,methods:{
		init : function(){
			const _this = this; 
			
			this.form = Daracl.form.create(document.getElementById("addForm"), {
				message : "This value is not valid",
				style : {
					position : 'left-right',
					labelWidth : '3'
				},
				message : {
					empty : VARSQL.message('msg.valid.required'),
					string : {
						minLength : VARSQL.message('msg.valid.string.min'),
						maxLength : VARSQL.message('msg.valid.string.max'),
					},
					number : {
						minimum : VARSQL.message('msg.valid.number.min'),
						miximum : VARSQL.message('msg.valid.number.max'),
					},
					regexp : {
						email : VARSQL.message('msg.valid.regexp.email'),
						url : VARSQL.message('msg.valid.regexp.url'),
					},
				},
				fields : [ 
					 { name : "jobName", label : VARSQL.message('name'), renderType : "text", required :true}
					,{ name : "vconnid", label : 'vconnid', renderType : "hidden", defaultValue : 'task'}
					,{ name : "continueOnError", label : 'continue On Error', renderType : "radio", defaultValue : 'true',
						listItem: {
				          list: [
				            { label: "Y", value: "true", selected: true },
				            { label: "N", value: "false" },
				          ],
				        }
					}
					,{ name : "cronExpression", label : VARSQL.message('cron.expression'), renderType : "text", required :true
							,placeholder:"0 0 3 * * ? "
							,description : `<div>
								<span>ex :0 0 3 * * ? </span> <br/>
								<span>Seconds Minutes Hours Day Month Week Year</span>
							</div>`}
					,{ name : "jobDescription", label : VARSQL.message('desc'), renderType : "textarea", customOptions :{rows:2}}
					,{
				        name: "triggerInfo",
				        style:{position:"top-left"},
				        label: VARSQL.message("trigger.info"),
				        renderType: 'custom',
				        template: '<div id="triggerList" style="width:100%; height:150px;position: relative;"></div>',
				        conditional: {
				        	custom : ()=>{
				        		return this.detailFlag;
				        	}
		                }
				        ,renderer: {
			        	  mounted: (field, element) => {
			              }
				          ,getValue(){
				            return '';
				          }
				          ,setValue(value){
				        	  if(_this.triggerListObj){
				        	  	_this.triggerListObj.setData([]);
				        	  }
				          }
				          ,focus(){
				        	  //_this.sortRowObj.focus();
				          }
				        }
				      }
				 ]
			});
			
			this.selectObj= $.pubMultiselect('#taskMappingId', {
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
				,valueKey : 'taskId'
				,labelKey : 'taskName'
				,render: function (info){
					var item = info.item;
					return (item.taskName +'('+item.taskType+')');
				}
				,source : {
					items : []
					,emptyMessage :'검색해주세요.'
					,search :{
						enable : true
						,callback : function (searchWord){
							_this.getTaskList(1, searchWord);
						}
					}
					,paging :{
						unitPage : _this.unitPage
						,callback : function (clickInfo){
							_this.getTaskList(clickInfo.no, clickInfo.searchword);
						}
					}
				}
				,target : {
					label : "Mapping Task"
					,items : []
					,emptyMessage :VARSQL.message('msg.nodata')
				}
				,footer: {
					enable : true
				}
			});
			
			this.getTaskList();
		}
		// add task
		,addTaskList(item){
			const exists = this.mappedTaskList.some(
			  task => task.taskId === item.taskId
			);
			
			if (!exists) {
				this.mappedTaskList.push(item);
			}
			
		}
		// m
		,removeTaskList(item){
			this.mappedTaskList = this.mappedTaskList.filter(
			  task => task.taskId !== item.taskId
			);
			
		}
		// task list
		,getTaskList : function (no, searchWord){
			
			this.$ajax({
				url : {type:VARSQL.uri.manager, url:'/taskJob/taskList'}
				,data : {
					pageNo: (no?no:1)
					,rows: this.list_count
					,'searchVal':searchWord
				}
				,loadSelector : '#addForm'
				,success:(resData)=> {
					this.selectObj.setSourceItem(resData.list ||[], resData.page);	
				}
			})
		}
		,search : function(no){
			var _this = this;

			var param = {
				pageNo: (no?no:1)
				,rows: _this.list_count
				,'searchVal':_this.searchVal
			};

			this.$ajax({
				url : {type:VARSQL.uri.manager, url:'/taskJob/list'}
				,data : param
				,success: function(resData) {
					_this.gridData = resData.list;
					_this.pageInfo = resData.page;
				}
			})
		}
		//add
		,addParameter : function (){
			this.detailItem.jobData.parameter.push({key:'',value:''});
		}
		// remove
		,removeParameter : function (paramItem, index){
			var idx = this.detailItem.jobData.parameter.indexOf(paramItem);
			if(idx > -1){
				this.detailItem.jobData.parameter.splice(idx, 1);
			}
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
				this.detailFlag = false;
				if(this.form){
					this.form.resetForm();
				}
				this.detailItem = {};
				if(this.selectObj){
					this.selectObj.setTargetItem([])
				}
			}else{
				this.detailFlag = true;
				this.detailItem = item;
				item.vconnid= item.vconnid ?? "task";
				
				
				try{
					const jobData = JSON.parse(item.jobData)
					const mappedTaskList = jobData.mappedTaskList ??[];
					this.selectObj.setTargetItem(mappedTaskList)
					item.continueOnError = jobData.continueOnError?? true;
					
				}catch(e){
					console.log('ignore : ',e);
				}
				
				this.form.setValue(item);
				
				this.setTriggerGrid();
			}
		}
		// save
		,save : function (mode){
			var _this = this;
			
			if(!this.form.isValidForm()){
				return ;
			}
			
			const targetItems = _this.selectObj.getTargetItem();
			
			if(targetItems.length < 1){
				VARSQL.toastMessage('msg.add.param', 'Task');
				return; 
			}

			if(!VARSQL.confirmMessage('msg.save.confirm')){
				return ;
			}
			
			this.form.getValue(true).then((formValue) => {
				
				const jobData ={}; 
				jobData.mappedTaskList = targetItems.map(item=>{
					return {taskName: item.taskName, taskId: item.taskId} 
				});
				
				jobData.continueOnError = formValue.continueOnError;
				formValue.jobData = JSON.stringify(jobData);
				
				_this.$ajax({
					url : {type:VARSQL.uri.manager, url:'/taskJob/save'}
					,data: formValue
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
	        }).catch((e) => {
	          alert(e.message);
	        });
		}
		// 정보 삭제 .
		,deleteInfo : function (){
			var _this = this;

			if(typeof this.detailItem.jobUid ==='undefined'){
				VARSQL.toastMessage('msg.item.select', VARSQL.message('delete'));
				return ;
			}

			if(!VARSQL.confirmMessage('msg.delete.confirm')){
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
		// job control
		,jobCtrl : function (item, mode){
			var _this = this;
			var param = {
				jobUid : item.jobUid
				,mode : mode
			};
			
			if(VARSQL.isBlank(param.jobUid)){
				VARSQLUI.toast.open(mode + ' '+VARSQL.message('msg.item.select'));
				return ;
			}
			
			if(!VARSQL.confirmMessage('msg.confirm.param', mode)){
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
			
			this.triggerListObj = $.pubGrid('#triggerList',{
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
