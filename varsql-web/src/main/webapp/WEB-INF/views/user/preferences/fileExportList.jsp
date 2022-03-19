<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/include/tagLib.jspf"%>

<div class="row">
    <div class="col-lg-12">
        <h1 class="page-header"><spring:message code="user.prefernces.menu.fileexportlist" text="File Export List" /></h1>
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
								<select v-model="vconnid" class="form-control ">
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
									<td>
										<a href="javascript:;" @click="download(item)" style="margin-right:10px;"><i class="fa fa-download"></i></a>
										<a href="javascript:;" @click="detail(item)">{{item.fileName}}</a>
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
						최대 5000개 까지 보이며 더 많은 내용은 다운로드 후 확인 바랍니다.
					</div>
					<div class="form-group">
						<div class="col-xs-12"><label class="control-label"><spring:message code="user.preferences.sqlcont" /></label></div>
						<div class="col-xs-12">
							<div id="fileList" style="height:300px;" class="form-control input-init-type"></div>
							
							<br/>
							최대 5000 라인 까지 확인 가능
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

<varsql:editorResource/>

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
		,vconnid : 'ALL'
		,isDetailFlag :false
		,fileListGridObj : {}
		,fileViewEditor : {}
	}
	,computed :{
		selectAllCheck : function (){
			return this.gridData.length > 0 && this.gridData.length == this.selectItem.length;
		}
	}
	,methods:{
		init : function() {
			var _this = this;
			
			$(this.$el).removeClass('display-off')
			
			this.fileListGridObj = $.pubGrid('#fileList', {
				asideOptions :{lineNumber : {enabled : true	,width : 30}}
				,tColItem : [
					{ label: 'Name', key: 'fileName',width:80},
					{ label: 'Size', key: 'fileSize', align:'right', width:45},
					{ label: 'Compress Size', key: 'compressFileSize', align:'right', width:45},
					{ label: 'UPD DT', key: 'updDt',width:50},
					{ label: '보기', key: 'viewCont',width:85, align:'center' 
						,"renderer": { 
							type : "button" 
							,label : '내용보기'
							,click : function (info){
								_this.$ajax({
									url : {type:VARSQL.uri.user, url:'/preferences/file/zipDetail'}
									,loadSelector : '#main-content'
									,data : {
										fileId : _this.detailItem.fileId
										,fileName : info.rowItem.fileName
									}
									,success:function (resData){
										_this.fileViewEditor.setValue(resData.item||'');
										_this.fileViewEditor.setHistory({done:[],undone:[]});
									}
								});
							}
						}	
					}
				]
				,rowOptions :{
					height : 30
				}
				,tbodyItem :[]
			});
			
			this.fileViewEditor = CodeMirror.fromTextArea(document.getElementById('sqlFileViewer'), {
				mode: 'text/plain',
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
					this.selectItem.push(this.gridData[i].sqlId)
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
				,fileType : 'export'
			};

			this.$ajax({
				url :{type:VARSQL.uri.user, url:'/preferences/file/fileList'}
				,data : param
				,success: function(resData) {
					_self.gridData = resData.items;
					_self.pageInfo = resData.page;
				}
			})
		}
		,detail : function (item){
			var _this = this;
			
			this.detailItem = item;
			
			_this.isDetailFlag = true;
			this.$ajax({
				url : {type:VARSQL.uri.user, url:'/preferences/file/detail'}
				,loadSelector : '#main-content'
				,data : {
					fileId : item.fileId
				}
				,success:function (resData){
					_this.fileListGridObj.setData(resData.items);
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
	}
});
</script>
