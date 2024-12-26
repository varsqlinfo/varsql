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
									<td :title="item.taskName">
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
									<button type="button" class="btn btn-default"  @click="copy()"><spring:message code="copy"/></button>
									<button type="button" class="btn btn-primary"  @click="execute()"><spring:message code="execute"/></button>
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
		,detailItem :{}
		,editor : false
		,viewMode : 'form'
		,selectObj : {}
		,historyItem : {}
		,form : {}
	}
	,methods:{
		init : function(){
			var _self = this; 
			
			this.form = new DaraForm(document.getElementById("addForm"), {
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
				        renderer: {
			        	  mounted: (field, element) => {
			        		  _self.editor = new codeEditor(element.querySelector('#sqlCont'), {
			      				schema: '',
			      				editorOptions: { 
			      					theme: 'vs-light'
			      					,minimap: {enabled: false} 
			      					,contextmenu :false
			      				}
			      			})
			              }
				          ,getValue(){
				            return _self.editor.getValue();
				          }
				          ,setValue(sql){
				        	  _self.editor.setValue(sql);
				          }
				        }
				      }
					, { name : "parameter", label : VARSQL.message('parameter')
						, renderType : "grid"
						,gridOptions: {
				          height: '200px',
				        }
				        , children: [
				          {
				            name: "key",
				            label: VARSQL.message('key'),
				            renderType: "text",
				            required: true,
				            style: {
				              width: '200px'
				            }
				          }
				          , {
				            name: "value",
				            label: VARSQL.message('value'),
				            required: true,
				            renderType: "text"
				          }
				     ]}
					, { name : "description", label : VARSQL.message('desc'), renderType : "textarea"}
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
		// 복사
		,copy : function(){
			var _this = this;
			
			if(!VARSQL.confirmMessage('msg.copy.confirm')){
				return ;
			}
			
			this.$ajax({
				url : {type:VARSQL.uri.manager, url:'/task/sql/copy'}
				,data : {
					taskId : this.detailItem.taskId
				}
				,success:function (resData){
					if(resData.resultCode ==200){
						VARSQL.toastMessage('msg.copy.success');
						_this.search();
						return
					}else{
						VARSQL.alertMessage(resData.messageCode  +'\n'+ resData.message);
					}
				}
			});
		}
		// 상세보기
		,itemView : function(item){
			var _this = this;
			
			if(this.viewMode != 'form'){
				this.viewArea('form');	
			}
			this.setDetailItem(item);
		}
		// 상세 셋팅
		,setDetailItem : function (item){
			this.viewArea('form');
			
			if(item =='init' || VARSQL.isUndefined(item)){
				this.detailFlag = false;
				this.form.resetForm();
			}else{
				if(item.$orginParameter){
					item.parameter = item.parameter;
				}else{
					item.$orginParameter = item.parameter;
					try{
						item.parameter = VARSQL.parseJSON(item.parameter);
					}catch(e){
						item.parameter = [];
					}
				}
				
				this.detailFlag = true;
				this.detailItem = item;
				
				this.form.setValue(item);
			}
		}
		// save
		,save : function (mode){
			var _this = this;
			
			if(!this.form.isValidForm()){
				return ;
			}

			if(!VARSQL.confirmMessage('msg.save.confirm')){
				return ;
			}
			
			this.form.getValue(true).then((formValue) => {
				formValue.parameter = JSON.stringify(formValue.parameter);
				
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
	        }).catch((e) => {
	          alert(e.message);
	        });
		}
		// 정보 삭제 .
		,deleteInfo : function (){
			var _this = this;

			if(typeof this.detailItem.taskId ==='undefined'){
				VARSQL.toastMessage('msg.item.select', VARSQL.message('delete'));
				return ;
			}

			if(!VARSQL.confirmMessage('msg.delete.confirm')){
				return ;
			}

			this.$ajax({
				url : {type:VARSQL.uri.manager, url:'/task/sql/remove'}
				,data: {
					taskId : _this.detailItem.taskId
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
		,execute :function (){
			
			if(this.detailItem.useYn == 'N'){
				VARSQL.toastMessage('msg.execute.db.error');
				return ;
			}
			
			if(!VARSQL.confirmMessage('msg.execute.confirm')){
				return ;
			}
			
			this.$ajax({
				url : {type:VARSQL.uri.manager, url:'/task/sql/execute'}
				,data : {
					taskId : this.detailItem.taskId
				}
				,success:function (resData){
					if(resData.resultCode != 200){
						VARSQL.alertMessage(resData.message);
						return ;
					}

					VARSQL.toastMessage('msg.execute.success');
				}
			});
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
					taskId : this.historyItem.taskId
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
	}
});
</script>
