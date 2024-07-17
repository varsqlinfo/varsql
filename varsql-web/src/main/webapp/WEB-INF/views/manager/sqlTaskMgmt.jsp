<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/include/tagLib.jspf"%>

<!-- Page Heading -->
<div class="row">
    <div class="col-lg-12">
        <h1 class="page-header"><spring:message code="manager.menu.sqltaskmgmt"/></h1>
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
								<col style="width:100px">
								<col style="min-width:50px;width:*;cursor:pointer;">
								<col style="width:100px;">
							</colgroup>
							<thead>
								<tr role="row">
									<th class="text-center"><spring:message	code="connection.db" text="Connection"/></th>
									<th class="text-center"><spring:message	code="name" text="Name" /></th>
									<th class="text-center"><spring:message	code="description" text="Description" /></th>
									<th class="text-center"></th>
								</tr>
							</thead>

							<tbody class="dataTableContent">
								<tr v-for="(item,index) in gridData" class="gradeA cursor-pointer" :class="(index%2==0?'add':'even')">
									<td :title="item.vname"><div class="text-ellipsis ellipsis0">{{item.vname}}</div></td>
									<td :title="item.jobName">
										<div class="text-ellipsis ellipsis0"><a href="javascript:;" @click="itemView(item)" :title="item.taskName">{{item.taskName}}</a></div>
										
									</td>
									<td>
										<div class="text-ellipsis ellipsis0">{{item.description}}</div>
									</td>
									<td>
										<button class="btn btn-sm btn-default" @click="historyView(1, item)">History</button>
									</td>
								</tr>
								<tr v-if="gridData.length === 0">
									<td colspan="4"><div class="text-center"><spring:message code="msg.nodata"/></div></td>
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

				<form id="addForm" name="addForm" class="form-horizontal" onsubmit="return false;">
				</form>
			</div>
			<!-- /.panel-body -->
		</div>
		<!-- /.panel -->
	</div>
	<!-- /.col-lg-8 -->

</div>
<!-- /.row -->

<varsql:importResources resoures="codeEditor" editorHeight="200"/>

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
		,selectObj : {}
		,historyItem : {}
		,form : {}
	}
	,created : function (){
		this.setDetailItem('init');
	}
	,methods:{
		init : function(){
			var _self = this; 
			
			this.form = new DaraForm("#addForm", {
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
					, {	name : "taskName", label : VARSQL.message('name'), renderType : "text", required :true}
					,{
				        name: "vconnid",
				        label: VARSQL.message('db'),
				        required: true,
				        renderType: "dropdown",
				        listItem: {
				            labelField: "vname",
				            valueField: "vconnid",
				            list: this.dbList,
				        }
				    }
					, {
				        name: "sql",
				        label: "SQL",
				        renderType: 'custom',
				        template: '<div id="sqlCont" style="width:100%; height:200px;" class="border"></div>',
				        required: true,
				        onChange: (arg) => {
				          console.log(arg);
				        },
				        renderer: {
			        	  mounted: () => {
			        		  _self.fileViewEditor = new codeEditor(document.getElementById('sqlCont'), {
			      				schema: '',
			      				editorOptions: { 
			      					theme: 'vs-light'
			      					,minimap: {enabled: false} 
			      					,contextmenu :false
			      				}
			      			})
			              }
				          ,getValue(){
				            return _self.fileViewEditor.getValue();
				          }
				        }
				      }
					, { name : "parameter", label : VARSQL.message('parameter'), renderType : "text"}
					, { name : "description", label : VARSQL.message('desc'), renderType : "text"}
				 ]
			});
		}
		,search : function(no){
			var _this = this;

			var param = {
				pageNo: (no?no:1)
				,rows: _this.list_count
				,'searchVal':_this.searchVal
			};

			this.$ajax({
				url : {type:VARSQL.uri.manager, url:'/task/sql/list'}
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
			
			this.form.setValue(item);
			
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
                    	sql : ''
         				, parameter:[]
                    }
				}
				
				if(item !='init') this.sqlEditor.setValue('');
				
			}else{
				var jobData = {
					 sql : ''
					, parameter:[{key : '', value : ''}]
				};
				
				try{
					jobData = VARSQL.parseJSON(item.jobData);
					
					var paramMap = jobData.parameter;
					var parameter =[];
					
					for(var key in paramMap){
						parameter.push({key : key , value : paramMap[key]});
					}
					jobData.parameter = parameter;
				}catch(e){
					console.log(e);
				}
				
				item.jobData = jobData;
					
				this.detailFlag = true;
				this.detailItem = item;
				
				this.sqlEditor.setValue(jobData.sql);
				
				this.$nextTick(function (){
					this.setTriggerGrid(item);
				});
			}
		}
		// save
		,save : function (mode){
			var _this = this;
			
			if(!DaraForm.instance('#addForm').isValidForm()){
				return ;
			}

			if(!VARSQL.confirmMessage('msg.save.confirm')){
				return ;
			}
			
			const formValue = DaraForm.instance('#addForm').getValue();
			
			_this.$ajax({
				url : {type:VARSQL.uri.manager, url:'/task/sql/save'}
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
		}
		,getParamVal : function (item){
			return VARSQL.util.objectMerge({}, this.detailItem);
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
				url : {type:VARSQL.uri.manager, url:'/task/sql/remove'}
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
				url : {type:VARSQL.uri.manager, url:'/task/sql/history'}
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
	}
});
</script>
