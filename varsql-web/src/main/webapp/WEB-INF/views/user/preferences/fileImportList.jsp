<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/include/tagLib.jspf"%>

<div class="row">
    <div class="col-lg-12">
        <h1 class="page-header"><spring:message code="user.prefernces.menu.fileimportlist" text="File Import List" /></h1>
    </div>
    <!-- /.col-lg-12 -->
</div>
<div class="row display-off" id="varsqlVueArea">
	<div class="col-xs-5">
		<div class="panel panel-default">
			<!-- /.panel-heading -->
			<div class="panel-body">
				<div class="row search-area">
					<div class="col-sm-4">
						<label>
							<button @click="deleteInfo()" type="button" class="btn btn-xs btn-danger"><spring:message code="btn.delete" /></button>
						</label>
					</div>
					<div class="col-sm-8">
						<div class="dataTables_filter">
							<label style="float:left; margin-right: 5px;">
								<select v-model="vconnid" class="form-control" @change="search()">
									<option value="ALL"><spring:message code="all"/></option>
									<c:forEach items="${dblist}" var="tmpInfo" varStatus="status">
										<option value="${tmpInfo.vconnid}" vname="${tmpInfo.name}">${tmpInfo.name}</option>
									</c:forEach>
								</select>
							</label>
							<div class="input-group floatright">
								<input type="text" value="" v-model="searchVal" class=" form-control" @keydown.enter="search()">
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
											<input type="checkbox" :checked="selectAllCheck" @click="selectAll()" />
										</div>
									</th>
									<th style="width: 195px;">
										<spring:message	code="sql_file_name" />
									</th>
									<th style="width: 120px;">
										<spring:message	code="size" />
									</th>
									<th style="width: 170px;">
										<spring:message	code="reg_dt" />
									</th>
								</tr>
							</thead>
							<tbody class="dataTableContent">
								<tr v-for="(item,index) in gridData" class="gradeA" :class="(index%2==0?'add':'even')">
									<td><input type="checkbox" :value="item.fileId" v-model="selectItem"></td>
									<td> 
										<a href="javascript:;" @click="download(item)" title="download" style="margin-right:10px;"><i class="fa fa-download"></i></a>
										<a href="javascript:;" @click="detail(item)" :title="item.fileName">{{item.fileName}}</a>
									</td>
									<td class="center">{{item.fileSize}}</td>
									<td class="center">{{item.regDt}}</td>
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
			<div class="panel-heading"><spring:message code="detail.view" /></div>
			<!-- /.panel-heading -->
			<div class="panel-body">
				<form id="addForm" name="addForm" class="form-horizontal" onsubmit="return false;">
					<div class="col-xs-12">
						최대 5000 라인 까지 보이며 더 많은 내용은 다운로드 후 확인 바랍니다.
					</div>
					<div class="pull-right" v-if="detailItem.content" @click="download(currentItem)">
						<button type="button" class="btn btn-sm btn-default">다운로드</button>
					</div>
					<div class="form-group">
						<div class="col-xs-12"><label class="control-label"><spring:message code="user.preferences.sqlcont" /></label></div>
						<div class="col-xs-12">
							<textarea id="sqlFileViewer" rows="10" class="form-control input-init-type"></textarea>
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

<varsql:importResources resoures="codeEditor"/>

<style>
.CodeMirror {
    width: 100%;
    height: 500px;
    border: 1px solid #c5bbbb;
}
</style>
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
		,currentItem : {}
		,vconnid : 'ALL'
		,isDetailFlag :false
		,fileViewEditor : {}
		,fileMode :{
			xml : 'application/xml'
			,json : 'application/json'
			,sql : 'text/x-sql'
			,other : 'text/plain'
		}
	}
	,computed :{
		selectAllCheck : function (){
			return this.gridData.length > 0 && this.gridData.length == this.selectItem.length;
		}
	}
	,methods:{
		init : function() {

			$(this.$el).removeClass('display-off')

			this.fileViewEditor = CodeMirror.fromTextArea(document.getElementById('sqlFileViewer'), {
				mode: 'text/x-sql',
				indentWithTabs: true,
				smartIndent: true,
				autoCloseBrackets: true,
				indentUnit : 4,
				lineNumbers: true,
				height:500,
				lineWrapping: false,
				matchBrackets : true,
				theme: "eclipse",
				readOnly:true
			});
		}
		,selectAll : function (){
			if(this.selectAllCheck){
				this.selectItem = [];
			}else{
				this.selectItem = [];

				for(var i =0 ;i <this.gridData.length; i++){
					this.selectItem.push(this.gridData[i].fileId)
				}
			}
		}
		,search : function(no){
			var _self = this;

			var param = {
				pageNo: (no?no:1)
				,rows: _self.list_count
				,'searchVal':_self.searchVal
				,vconnid : _self.vconnid
				,fileType : 'import'
			};

			this.$ajax({
				url :{type:VARSQL.uri.user, url:'/preferences/file/fileList'}
				,data : param
				,success: function(resData) {
					_self.gridData = resData.list;
					_self.pageInfo = resData.page;
				}
			})
		}
		,detail : function (item){
			var _this = this;
			this.currentItem = item; 

			_this.isDetailFlag = true;
			this.$ajax({
				url : {type:VARSQL.uri.user, url:'/preferences/file/detail'}
				,loadSelector : '#main-content'
				,data : {
					fileId : item.fileId
				}
				,success:function (resData){
					_this.detailItem = resData.item;
					
					_this.fileViewEditor.setOption('mode',_this.fileMode[item.ext] || _this.fileMode.other);
					_this.fileViewEditor.setValue(_this.detailItem.content||'');
					_this.fileViewEditor.setHistory({done:[],undone:[]});
				}
			});
		}
		,download : function (item){
			
			VARSQL.req.download({
				type: 'post'
				,url: '/file/download'
				,params: {
					fileId : item.fileId
					,contId : ''
				}
			});
		}
		//delete
		,deleteInfo : function(){
			var _this = this;

			var item = _this.detailItem;

			var selectItem = _this.selectItem;

			if(VARSQL.isDataEmpty(selectItem)){
				VARSQLUI.alert.open(VARSQL.messageFormat('varsql.0006'));
				return ;
			}

			if(!confirm(VARSQL.messageFormat('varsql.0016'))){
				return ;
			}

			this.$ajax({
				url : {type:VARSQL.uri.user, url:'/preferences/file/delete'}
				,data : {
					selectItem : selectItem.join(',')
				}
				,success:function (resData){
					VARSQLUI.toast.open(VARSQL.messageFormat('varsql.0017'));
					_this.search();
				}
			});
		}
	}
});
</script>
