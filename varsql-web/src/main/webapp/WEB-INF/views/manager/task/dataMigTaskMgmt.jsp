<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/include/tagLib.jspf"%>

<!-- Page Heading -->
<div class="row">
    <div class="col-lg-12">
        <h1 class="page-header"><spring:message code="manager.menu.datamigtaskmgmt"/></h1>
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
								<col style="width:100px">
								<col style="width:100px;">
								<col style="width:100px;">
								<col style="width:100px;">
							</colgroup>
							<thead>
								<tr role="row">
									<th class="text-center"><spring:message	code="name" text="Name" /></th>
									<th class="text-center"><spring:message	code="source" text="Source"/></th>
									<th class="text-center"><spring:message	code="target" text="Target" /></th>
									<th class="text-center"><spring:message	code="description" text="Desc" /></th>
									<th class="text-center"></th>
								</tr>
							</thead>

							<tbody class="dataTableContent">
								<tr v-for="(item,index) in gridData" class="gradeA cursor-pointer" :class="(index%2==0?'add':'even')">
									<td :title="item.taskName">
										<div class="text-ellipsis ellipsis0"><a href="javascript:;" @click="itemView(item)" :title="item.taskName">{{item.taskName}}</a></div>
									</td>
									<td :title="item.sourceVname"><div class="text-ellipsis ellipsis0">{{item.sourceVname}}</div></td>
									<td :title="item.targetVname"><div class="text-ellipsis ellipsis0">{{item.targetVname}}</div></td>
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
		,transferTypes : ${varsqlfn:objectToJson(transferTypes)}
		,sourceTypes : ${varsqlfn:objectToJson(sourceTypes)}
		,sourceReadTypes : ${varsqlfn:objectToJson(sourceReadTypes)}
		,targetTypes : ${varsqlfn:objectToJson(targetTypes)}
		,detailFlag :false
		,searchVal : ''
		,pageInfo : {}
		,gridData :  []
		,detailItem :{}
		,readEditor : false
		,writeEditor : false
		,viewMode : 'form'
		,selectObj : {}
		,historyItem : {}
		,form : {}
		,allTableInfo:{}
		,writeTableObj:false
		,sortRowObj:false
	}
	,methods:{
		init(){
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
					 {	name : "taskName", label : VARSQL.message('name'), renderType : "text", required :true}
					, { name : "description", label : VARSQL.message('desc'), renderType : "textarea" ,customOptions:{rows:2}}
					,{
						renderType: 'group'
					    , orientation: "horizontal"
					   	, children: [
					   	  {
				            renderType: 'group'
				            , orientation: "vertical"
				            , label: VARSQL.message('source')
				            , style: {
				              width: '6',
				              position: 'top-center'
				            }
				            , children: [
				            	{
							        name: "sourceVconnid",
							        label: VARSQL.message('db'),
							        required: true,
							        renderType: "dropdown",
							        style: {
						              position: 'top-left'
						            }, 
							        listItem: {
							            labelField: "vname",
							            valueField: "vconnid",
							            list: this.dbList,
							        }
						            ,onChange(changeInfo){
						            	_self.tableList('source', changeInfo.value);
						            }
							    }
				            	,{
							        name: "sourceType",
							        label: 'Source Type',
							        required: true,
							        renderType: "radio",
							        style: {
						              position: 'top-left'
						            }, 
							        listItem: {
							        	labelField: "name",
							            valueField: "code",
							            list: this.sourceTypes,
							        }
						            ,customOptions:{disableDefaultOption:true}
							    }
				            	,{
							        name: "source",
							        label: VARSQL.message('Table'),
							        required: true,
							        renderType: "dropdown",
							        style: {
						              position: 'top-left'
						            }, 
							        listItem: {
							            labelField: "name",
							            valueField: "name",
							            list:[],
							        }
						            ,conditional: {
					                  show: false,
					                  field: "sourceType",
					                  eq: "table",
					                }
							        ,onChange :(changeInfo)=>{
							        	if(changeInfo.value ==''){
							        		_self.sortRowObj.setData([]);
							        		return 
							        	}
							        	// 이전 키 셋팅 한거 셋팅
							        	if(changeInfo.valueItems && changeInfo.valueItems.length > 0){
							        		
							        		var constraint = false; 
							        		var chkRow = [];
							        		if(VARSQL.isUndefined(_self.detailItem.$sortColumns)){
							        			constraint = true; 
							        		}else{
							        			chkRow = _self.detailItem.$sortColumns;
							        		}
							        		
							        		changeInfo.valueItems[0].colList.forEach((rowItem)=>{
							        		if((constraint  && !VARSQL.isBlank(rowItem.constraints)) 
							        			||(_self.detailItem.source == changeInfo.value && chkRow.includes(rowItem.name))){
							  						rowItem['_pubcheckbox'] = true; 
							  					}
							        		})
							        		_self.sortRowObj.setData(changeInfo.valueItems[0].colList)
							        	}
							        }
							    }
				            	,{
							        name: "readType",
							        label: 'Data Read Type',
							        defaultValue : 'page',
							        renderType: "radio",
							        style: {
						              position: 'top-left'
						            }, 
							        listItem: {
							        	labelField: "name",
							            valueField: "code",
							            list: this.sourceReadTypes,
							        }
						            ,customOptions:{disableDefaultOption:true}
							    }
				            	, {	name : "pageSize", label : VARSQL.message('page.size'), renderType : "number", defaultValue:1000, required :true
				            		,conditional: {
					                  show: false,
					                  field: "readType",
					                  eq: "page",
					                }
				            	  }
				            	,{
							        name: "sortColumns",
							        label: "Sort Column",
							        renderType: 'custom',
							        template: '<div id="tableSortRow" style="width:100%; height:150px;position: relative;"></div>',
							        required: false,
							        conditional: {
					                  show: true,
					                  field: "sourceType",
					                  eq: "table",
					                }
							        ,renderer: {
						        	  mounted: (field, element) => {
						        		  
						              }
							          ,getValue(){
							            return _self.sortRowObj.getCheckItems();
							          }
							          ,setValue(value){
							        	  if(_self.sortRowObj){
							        	  	_self.sortRowObj.setData([]);
							        	  }
							          }
							          ,focus(){
							        	  //_self.sortRowObj.focus();
							          }
							        }
							      }
				            	,{
							        name: "readSql",
							        label: "SQL",
							        renderType: 'custom',
							        description: '<div style="color: #f21111;">'+VARSQL.message('msg.sql.create.warning.msg')+'</div>',
							        template: '<div id="readSqlCont" style="width:100%; height:200px;" class="border"></div>',
							        required: true ,
							        conditional: {
					                  show: false,
					                  field: "sourceType",
					                  eq: "sql",
					                }
							        ,renderer: {
						        	  mounted: (field, element) => {
						        		  _self.readEditor = new codeEditor(element.querySelector('#readSqlCont'), {
						      				schema: '',
						      				editorOptions: { 
						      					theme: 'vs-light'
						      					,minimap: {enabled: false} 
						      					,contextmenu :false
						      				}
						      			})
						              }
							          ,getValue(){
							            return _self.readEditor.getValue();
							          }
							          ,setValue(sql){
							        	  _self.readEditor.setValue(sql);
							          }
							          ,focus(){
							        	  _self.readEditor.focus();
							          }
							        }
							      }
								, { name : "readParameter", label : VARSQL.message('parameter')
									, renderType : "grid"
									, conditional: {
					                  show: false,
					                  field: "sourceType",
					                  eq: "sql",
					                }
									,gridOptions: {
							          height: '100px',
							        }
							        , children: [
							          {
							            name: "key",
							            label: VARSQL.message('key'),
							            renderType: "text",
							            required: true,
							            style: {
							              width: '45%'
							            }
							          }
							          , {
							            name: "value",
							            label: VARSQL.message('value'),
							            required: true,
							            renderType: "text"
							          }
							     ]
							  }
				            ]
				          }
					   	 ,{
				            renderType: 'group'
				            , orientation: "vertical"
				            , label: VARSQL.message('target')
				            , style: {
				              width: '6',
				              position: 'top-center'
				            }
				            , children: [
				            	{
							        name: "targetVconnid",
							        label: VARSQL.message('db'),
							        required: true,
							        renderType: "dropdown",
							        style: {
						              position: 'top-left'
						            },
							        listItem: {
							            labelField: "vname",
							            valueField: "vconnid",
							            list: this.dbList,
							        }
						            ,onChange(changeInfo){
						            	_self.tableList('target', changeInfo.value);
						           }
							    }
				            	,{
							        name: "transferType",
							        label: VARSQL.message('type'),
							        required: true,
							        renderType: "dropdown",
							        defaultValue : '2',
							        listItem: {
							            labelField: "name",
							            valueField: "code",
							            list: this.transferTypes,
							        }
							    }
								, {	name : "commitCount", label : VARSQL.message('commit.count'), renderType : "number", defaultValue:1000, required :true}
								, {	name : "errorIgnore", label : VARSQL.message('error.ignore'), renderType : "checkbox"}
				            	,{
							        name: "writeType",
							        label: 'Data Insert Type',
							        required: true,
							        renderType: "radio",
							        style: {
						              position: 'top-left'
						            }, 
							        listItem: {
							        	labelField: "name",
							            valueField: "code",
							            list: this.targetTypes,
							        }
						            ,customOptions:{disableDefaultOption:true}
							    }
				            	,{
							        name: "target",
							        label: VARSQL.message('Table'),
							        required: true,
							        renderType: "dropdown",
							        style: {
						              position: 'top-left'
						            }, 
							        listItem: {
							            labelField: "name",
							            valueField: "name",
							            list:[],
							        },
							        onChange :(changeInfo)=>{
							        	
							        	if(changeInfo.value ==''){
							        		_self.sortRowObj.setData([]);
							        		return 
							        	}
							        	// 이전 키 셋팅 한거 셋팅
							        	if(changeInfo.valueItems && changeInfo.valueItems.length > 0){
							        		
							        		var constraint = false; 
							        		var chkRow = [];
							        		if(VARSQL.isUndefined(_self.detailItem.$tableRowKey)){
							        			constraint = true; 
							        		}else{
							        			chkRow = _self.detailItem.$tableRowKey;
							        		}
							        		
							        		changeInfo.valueItems[0].colList.forEach((rowItem)=>{
							        		if((constraint && !VARSQL.isBlank(rowItem.constraints)) 
							        			||(_self.detailItem.target == changeInfo.value && chkRow.includes(rowItem.name))){
							  						rowItem['_pubcheckbox'] = true; 
							  					}
							        		})
							        		_self.writeTableObj.setData(changeInfo.valueItems[0].colList)
							        	}
							        }
							    }
				            	,{
							        name: "tableRowKey",
							        label: "Key Column",
							        renderType: 'custom',
							        template: '<div id="tableColumnGrid" style="width:100%; height:150px;position: relative;"></div>',
							        required: true,
							        renderer: {
						        	  mounted: (field, element) => {
						        		  
						              }
							          ,getValue(){
							            return _self.writeTableObj.getCheckItems();
							          }
							          ,setValue(value){
							        	  if(_self.writeTableObj){
							        	  	_self.writeTableObj.setData([]);
							        	  }
							          }
							          ,focus(){
							        	  //_self.writeTableObj.focus();
							          }
							        }
							      }
				            ]
					   	 }
					  ]
					}
				 ]
			});
			
  		  	_self.sortRowObj = $.pubGrid('#tableSortRow',{
			  	height:150,
  				asideOptions :{
  					lineNumber : {enabled : true, width : 30, align: 'right'}
	        		,rowSelector: {
		                enabled: true,
		                key: 'checkbox',
		                name: 'Sort',
		                width: 30
		            }
  				}
  				,tColItem : [
  					{ label: VARSQL.message('column'), key: 'name',width:30 },
  				]
  				,tbodyItem : []
  			});
  		  	
  		  	_self.writeTableObj = $.pubGrid('#tableColumnGrid',{
			  	height:150,
  				asideOptions :{
  					lineNumber : {enabled : true, width : 30, align: 'right'}
	        		,rowSelector: {
		                enabled: true,
		                key: 'checkbox',
		                name: 'Key',
		                width: 25
		            }
  				}
  				,tColItem : [
  					{ label: VARSQL.message('column'), key: 'name',width:30 },
  				]
  				,tbodyItem : []
  			});
		}
		,tableList(fieldName, vconnid){
			
			if(!VARSQL.isUndefined(this.allTableInfo[vconnid])){
				this.form.setFieldItems(fieldName, this.allTableInfo[vconnid]);
				return ;
			}
			
			this.$ajax({
				url : {type:VARSQL.uri.manager, url:'/comm/objectMetaList'}
				,loadSelector : '#sourceObjectMeta'
				,disableResultCheck : true
				,loadSelector : '#addForm'
				,data :  {
					vconnid : vconnid
					,objectType : 'table'
					,schema : ''
				}
				,success: (resData)=> {
					
					if(resData.status == 500){
						VARSQL.alertMessage('Message code : '+resData.messageCode +"\nMessage : "+ resData.message);
						return ; 
					}
					
					this.allTableInfo[vconnid] = resData.list;
					
					this.form.setFieldItems(fieldName, resData.list);
				}
			})
		}
		,search(no){

			var param = {
				pageNo: (no?no:1)
				,rows: this.list_count
				,'searchVal':this.searchVal
			};

			this.$ajax({
				url : {type:VARSQL.uri.manager, url:'/task/dataMig/list'}
				,data : param
				,success: (resData)=> {
					this.gridData = resData.list;
					this.pageInfo = resData.page;
				}
			})
		}
		// 복사
		,copy(){
			var _this = this;
			
			if(!VARSQL.confirmMessage('msg.copy.confirm')){
				return ;
			}
			
			this.$ajax({
				url : {type:VARSQL.uri.manager, url:'/task/dataMig/copy'}
				,data : {
					taskId : this.detailItem.taskId
				}
				,success:(resData)=>{
					if(resData.resultCode ==200){
						VARSQL.toastMessage('msg.copy.success');
						this.search();
						return
					}else{
						VARSQL.alertMessage(resData.messageCode  +'\n'+ resData.message);
					}
				}
			});
		}
		// 상세보기
		,itemView(item){
			var _this = this;
			
			if(this.viewMode != 'form'){
				this.viewArea('form');	
			}
			this.setDetailItem(item);
		}
		// 상세 셋팅
		,setDetailItem (item){
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
				
				item=this.setSourceConfigFormValue(item);
				item=this.setTargetConfigFormValue(item);
				
				this.detailFlag = true;
				this.detailItem = item;
				this.form.setValue(item);
			}
		}
		// set source config value
		,setSourceConfigFormValue(item){
			var config = VARSQL.parseJSON(item.sourceConfig)||{};
			
			if(VARSQL.isArray(config.sortColumns)){
				config.$sortColumns = [];
				config.sortColumns.forEach((rowItem)=>{
					config.$sortColumns.push(rowItem.name);
        		})
			}
			
			delete config.sortColumns;
			
			item = VARSQL.util.objectMerge(item, config);
			
			if(item.sourceType =='sql'){
				item.readSql = config.source||'';
				item.readParameter = config.readParameter;
			}
			return item;
		}
		// source 값 얻기
		,getSourceConfig(formValue){
			var config = {};
			
			config.sourceType = formValue.sourceType;
			config.source = formValue.source;
			config.readType = formValue.readType;
			config.pageSize = formValue.pageSize;
			config.sortColumns = formValue.sortColumns
			
			if(formValue.sourceType =='sql'){
				config.source = formValue.readSql||'';
				config.readParameter = formValue.readParameter;
			}
			
			formValue.sourceConfig = JSON.stringify(config);
			
			return formValue; 
		}
		// set target config value 
		,setTargetConfigFormValue(item){
			var config = VARSQL.parseJSON(item.targetConfig)||{};
			
			if(VARSQL.isArray(config.tableRowKey)){
				config.$tableRowKey = [];
				config.tableRowKey.forEach((rowItem)=>{
					config.$tableRowKey.push(rowItem.name);
        		})
			}
			
			delete config.tableRowKey;
			
			item = VARSQL.util.objectMerge(item, config);
			
			return item ;
		}
		// target 값 얻기
		,getTargetConfig(formValue){
			var config = {};
			
			config.writeType = formValue.writeType;
			config.target = formValue.target;
			config.transferType = formValue.transferType;
			config.commitCount = formValue.commitCount;
			config.errorIgnore = formValue.errorIgnore;
			config.tableRowKey = formValue.tableRowKey;
			
			formValue.targetConfig = JSON.stringify(config);
			
			return formValue; 
		}
		// save
		,save (mode){
			var _this = this;
			
			if(!this.form.isValidForm()){
				return ;
			}

			if(!VARSQL.confirmMessage('msg.save.confirm')){
				return ;
			}
			
			this.form.getValue(true).then((formValue) => {
				_this.getSourceConfig(formValue)
				_this.getTargetConfig(formValue);
				
				_this.$ajax({
					url : {type:VARSQL.uri.manager, url:'/task/dataMig/save'}
					,data: formValue
					,success:function (resData){
						if(VARSQL.req.validationCheck(resData)){
							if(resData.resultCode != 200){
								VARSQL.alertMessage(resData.message);
								return ;
							}
							
							VARSQL.toastMessage('msg.save.success');
							//_this.setDetailItem();
							_this.search();
						}
					}
				});
	        }).catch((e) => {
	          alert(e.message);
	        });
		}
		// 정보 삭제 .
		,deleteInfo (){
			var _this = this;

			if(typeof this.detailItem.taskId ==='undefined'){
				VARSQL.toastMessage('msg.item.select', VARSQL.message('delete'));
				return ;
			}

			if(!VARSQL.confirmMessage('msg.delete.confirm')){
				return ;
			}

			this.$ajax({
				url : {type:VARSQL.uri.manager, url:'/task/dataMig/remove'}
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
			
			if(this.detailItem.sourceUseYn == 'N' || this.detailItem.targetUseYn == 'N'){
				VARSQL.toastMessage('[' + VARSQL.message(this.detailItem.sourceUseYn == 'N'?'source':'target')+'] '+ VARSQL.message('msg.execute.db.error'));
				return ;
			}
			
			if(!VARSQL.confirmMessage('msg.execute.confirm')){
				return ;
			}
			
			var param = {
				taskId : this.detailItem.taskId
				,progressUid : VARSQL.generateUUID()
			};
			
			this.$ajax({
				url : {type:VARSQL.uri.manager, url:'/task/dataMig/execute'}
				,loadSelector : 'body'
				,data : param
				,success:function (resData){
					if(resData.resultCode != 200){
						VARSQL.alertMessage(resData.message);
						VARSQL.req.stopProgress();
						return ;
					}

					VARSQL.toastMessage('msg.execute.success');
				}
			});
			
			var progressEle = $('body .center-loading-centent'); 
			progressEle.append('<div id="progressItemImport"></div><div id="progressItemContent"></div>');
			
			var progressItemImportEle = $('#progressItemImport');
			var progressItemContentEle = $('#progressItemContent');
			VARSQL.req.progressInfo({
				progressUid : param.progressUid
				,callback : function (resData){
					var item = resData.item; 
					
					if(item == 'fail'){
						progressEle.text('fail');
					}else if(item == 'complete'){
						progressEle.text('complete');
					}else{
						if(item != null){
							
							var taskResult = item.customInfo;
							
							if(taskResult.readTotal){
								progressItemImportEle.text('total : '+VARSQL.util.numberFormat(taskResult.readTotal));
							}
							
							var progressText = '';
							
							if(taskResult.writeTotal && taskResult.writeTotal > 0){
								progressText = 'idx :  ' + VARSQL.util.numberFormat(taskResult.readIdx)
								+ ', fail : '+ VARSQL.util.numberFormat(taskResult.writeFail)
								+ ', insert : '+ VARSQL.util.numberFormat(taskResult.writeInsert)
								+ ', update : '+ VARSQL.util.numberFormat(taskResult.writeUpdate)
								+ ', success : '+ VARSQL.util.numberFormat(taskResult.writeTotal)
							}
							
							progressItemContentEle.html(progressText);
						}
					}
				} 
			});
			
		}
		,historyView (no, item){
			var _this = this;
			this.viewArea('log');
			
			if(item){
				this.historyItem = item;	
			}
			
			this.$ajax({
				url : {type:VARSQL.uri.manager, url:'/task/dataMig/history'}
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
