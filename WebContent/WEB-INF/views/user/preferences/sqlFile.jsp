<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/include/tagLib.jspf"%>
<%@ include file="/WEB-INF/include/initvariable.jspf"%>

<div class="row">
    <div class="col-lg-12">
        <h1 class="page-header"><spring:message code="user.prefernces.menu.sqlfile" /></h1>
    </div>
    <!-- /.col-lg-12 -->
</div>
<div class="row display-off" id="varsqlVueArea">
	<div class="col-xs-5">
		<div class="panel panel-default">
			<!-- /.panel-heading -->
			<div class="panel-body">
				<div class="row">
					<div class="col-sm-4"></div>
					<div class="col-sm-8">
						<div class="dataTables_filter">
							<label style="float:left; margin-right: 5px;">
								<select v-model="vconnid" class="form-control input-sm">
									<option value="ALL"><spring:message code="all"/></option>
									<c:forEach items="${dblist}" var="tmpInfo" varStatus="status">
										<option value="${tmpInfo.vconnid}" vname="${tmpInfo.name}">${tmpInfo.name}</option>
									</c:forEach>
								</select>
							</label>
							<div class="input-group floatright">
								<input type="text" value="" v-model="searchVal" class="form-control" @keydown.enter="search()">
								<span class="input-group-btn">
									<button class="btn btn-default searchBtn" type="button" @click="search()"> <span class="glyphicon glyphicon-search"></span></button>
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
										<spring:message	code="sql_file_name" />
									</th>
									<th style="width: 120px;">
										<spring:message	code="label.database" />
									</th>
									<th style="width: 170px;">
										<spring:message	code="reg_dt" />
									</th>
								</tr>
							</thead>
							<tbody class="dataTableContent">
								<tr v-for="(item,index) in gridData" class="gradeA" :class="(index%2==0?'add':'even')">
									<td><input type="checkbox" :value="item.VCONNID" v-model="selectItem"></td>
									<td><a href="javascript:;" @click="modify(item)">{{item.GUERY_TITLE}}</a></td>
									<td class="center">{{item.VNAME}}</td>
									<td class="center">{{item.CHAR_REG_DT}}</td>
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
	<!-- /.col-lg-4 -->
	<div class="col-xs-7" >
		<div class="panel panel-default detail_area_wrapper" >
			<div class="panel-heading"><spring:message code="detail.view" /><span style="margin-left:10px;font-weight:bold;">{{detailItem.TITLE}}</span></div>
			<!-- /.panel-heading -->
			<div class="panel-body">
				<form id="addForm" name="addForm" class="form-horizontal" onsubmit="return false;">
					<div class="form-group">
						<div class="col-sm-12">
							<div class="pull-right">
								<button type="button" v-if="isDetailFlag" @click="deleteInfo()" class="btn btn-default"><spring:message code="btn.delete" /></button>
							</div>
						</div>
					</div>
					
					<div class="form-group">
						<div class="col-xs-12"><label class="control-label"><spring:message code="user.preferences.sqlfile" /></label></div>
			
						<div class="col-xs-12">
							<input type="text" :value="detailItem.GUERY_TITLE" class="form-control" />
						</div>
					</div>
					<div class="form-group">
						<div class="col-xs-12"><label class="control-label"><spring:message code="user.preferences.sqlcont" /></label></div>
						<div class="col-xs-12">
							<pre id="prettyprintArea" class="user-select-on prettyprint lang-sql prettyprinted" contenteditable="true" style="width:100%;height:370px;">
							</pre>
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

<link href="${pageContextPath}/webstatic/css/prettify/prettify.css?version=${prettify_ver}" rel="stylesheet" type="text/css">
<script src="${pageContextPath}/webstatic/js/plugins/prettify/prettify.js?version=${prettify_ver}"></script>
<script src="${pageContextPath}/webstatic/js/plugins/prettify/lang-sql.js?version=${prettify_ver}"></script>

<script>
VarsqlAPP.vueServiceBean({
	el: '#varsqlVueArea'
	,validateCheck : true
	,data: {
		searchVal : ''
		,selectItem :[]
		,pageInfo : {}
		,gridData :  []
		,detailItem : {}
		,vconnid : 'ALL'
		,isDetailFlag :false
	}
	,methods:{
		init : function(){
			
		}
		,search : function(no){
			var _self = this; 
			
			var param = {
				pageNo: (no?no:1)
				,rows: _self.list_count
				,'searchVal':_self.searchVal
				,vconnid : _self.vconnid
			};
			
			this.$ajax({
				url :{type:VARSQL.uri.user, url:'/preferences/sqlFile/list'}
				,data : param
				,success: function(resData) {
					_self.gridData = resData.items;
					_self.pageInfo = resData.page;
				}
			})
		}
		,save : function (mode){
			var _this = this;
			
			this.$validator.validateAll().then(function (result){
				if(result){
					
					var param ={};
					var saveItem = _this.detailItem;
					
					for(var key in saveItem){
						param[VARSQL.util.convertCamel(key)] = saveItem[key];
					}
					
					_this.$ajax({
						url : {type:VARSQL.uri.user, url:'/preferences/sqlFile/ins'}
						,data : param 
						,success:function (resData){
							if(VARSQL.req.validationCheck(resData)){
								if(resData.resultCode != 200){
									alert(resData.message);
									return ; 
								}
								_this.modify();
								_this.search();
							}
						}
					});
				}
			});
		}
		,modify : function (item){
			var _this = this; 
		
			_this.isDetailFlag = true; 
			this.$ajax({
				url : {type:VARSQL.uri.user, url:'/preferences/sqlFile/detail'}
				,loadSelector : '#main-content'
				,data : {
					sqlid : item.SQL_ID
				}
				,success:function (resData){
					_this.detailItem = resData.item;
					
					var ele = $('#prettyprintArea');
					
					ele.empty().html(_this.detailItem.QUERY_CONT).removeClass('prettyprinted');
					ele.scrollTop(0);
					PR.prettyPrint();
				}
			});
			
		}
		,deleteInfo : function(){
			var _this = this;
			
			var item = _this.detailItem;
			
			var selectItem = _this.selectItem;
			
			if(VARSQL.isDataEmpty(selectItem)){
				VARSQLUI.alert.open('<spring:message code="msg.data.select" />');
				return ; 
			}
			
			if(!confirm('<spring:message code="msg.delete.confirm"/>')){
				return ; 	
			}
			
			this.$ajax({
				url : {type:VARSQL.uri.user, url:'/preferences/sqlFile/del'}
				,data : {
					selectItem : selectItem.join(',')
				}
				,success:function (resData){
					_this.modify();
					_this.search();
				}
			});
		}
	}
});
</script>
