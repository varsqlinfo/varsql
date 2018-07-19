<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/include/tagLib.jspf"%>
<!-- Page Heading -->
<div class="row">
    <div class="col-lg-12">
        <h1 class="page-header"><spring:message code="manage.menu.glossary" /></h1>
    </div>
    <!-- /.col-lg-12 -->
</div>
<div class="row display-off" id="epViewArea">
	<div class="col-lg-7">
		<div class="panel panel-default">
			<!-- /.panel-heading -->
			<div class="panel-body">
				<div class="row">
					<div class="col-sm-6"></div>
					<div class="col-sm-6">
						<div class="dataTables_filter">
							<label style="float:left; margin-right: 5px;"><select v-model="list_count" @change="search()" class="form-control input-sm"><option
									value="10">10</option>
								<option value="25">25</option>
								<option value="50">50</option>
								<option value="100">100</option></select>
							</label>
							<div class="input-group floatright">
								<input type="text" v-model="searchVal" class="form-control" @keyup="search()">
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
						<table
							class="table table-striped table-bordered table-hover dataTable no-footer"
							id="dataTables-example">
							<thead>
								<tr role="row">
									<th style="width: 10px;"><div
											class="text-center">
											<input type="checkbox" id="allcheck" name="allcheck">
										</div>
									</th>
									<th style="width: 150px;">
										<spring:message	code="manage.glossary.word" />
									</th>
									<th style="width: 150px;">
										<spring:message	code="manage.glossary.word_en" />
									</th>
									<th style="width: 150px;">
										<spring:message	code="manage.glossary.word_abbr" />
									</th>
									<th style="width: 200px;">
										<spring:message	code="manage.glossary.desc" />
									</th>
								</tr>
							</thead>
							<tbody class="dataTableContent">
								<tr v-for="(item,index) in gridData" class="gradeA" :class="(index%2==0?'add':'even')">
									<td><input type="checkbox" :value="item.WORD_IDX" v-model="selectItem"></td>
									<td>{{item.WORD}}</td>
									<td>{{item.WORD_EN}}</td>
									<td>{{item.WORD_ABBR}}</td>
									<td>{{item.DESC}}</td>
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
	</div>
	<!-- /.col-lg-4 -->
	<div class="col-lg-5">
		
	</div>
</div>
<!-- /.row -->

<script>

VarsqlAPP.vueServiceBean( {
	el: '#epViewArea'
	,data: {
		list_count :10
		,searchVal : ''
		,pageInfo : {}
		,gridData :  []
		,detailItem :{}
		,selectItem :[]
	}
	,methods:{
		acceptYn : function(obj){
			var _self = this; 
			var selectItem = _self.selectItem;
			
			if(VARSQL.isDataEmpty(selectItem)){
				VARSQL.alert('<spring:message code="msg.data.select" />');
				return ; 
			}
			
			if(!confirm(obj=='Y'?'<spring:message code="msg.accept.msg" />':'<spring:message code="msg.denial.msg" />')){
				return ; 
			}
			
			var param = {
				acceptyn:obj
				,selectItem:selectItem.join(',')
			};
			
			this.$ajax({
				data:param
				,url : {gubun:VARSQL.uri.manager, url:'/acceptYn'}
				,success:function (response){
					_self.search();
				}
			});
		}
		,search : function(no){
			var _self = this; 
			
			var param = {
				pageNo: (no?no:1)
				,rows: _self.list_count
				,'searchVal':_self.searchVal
			};
			
			this.$ajax({
				url:{gubun:VARSQL.uri.manager, url:'/userList'}
				,data : param
				,success: function(resData) {
					_self.gridData = resData.items;
					_self.pageInfo = resData.page;
				}
			})
		}
		,initPassword :function(sItem){
			this.$ajax({
				url:{gubun:VARSQL.uri.manager, url:'/initPassword'}
				,data : sItem
				,success: function(resData) {
					sItem.INITPW = resData.item;
					
					setTimeout(function (){
						sItem.INITPW ='';
					}, 5000);
					
					alert('변경되었습니다.\n변경된 패스워드는 5초후에 사라집니다.');
				}
			})
		}
	}
});
</script>
