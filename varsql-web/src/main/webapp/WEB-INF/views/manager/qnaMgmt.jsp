<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/include/tagLib.jspf"%>

<!-- Page Heading -->
<div class="row">
    <div class="col-lg-12">
        <h1 class="page-header"><spring:message code="manager.menu.qnamgmt" /></h1>
    </div>
    <!-- /.col-lg-12 -->
</div>
<div class="row display-off" id="varsqlVueArea">
	<div class="col-lg-12">
		<div class="panel panel-default">
			<!-- /.panel-heading -->
			<div class="panel-body">
				<div class="row search-area">
					<div class="col-sm-6">
						<label> <select v-model="list_count"
							aria-controls="dataTables-example" class="form-control "><option
									value="10">10</option>
								<option value="25">25</option>
								<option value="50">50</option>
								<option value="100">100</option></select>
						</label>
						<label class="radio-inline"><spring:message code="answer.yn" text="답변여부" /></label>
						<label class="radio-inline">
							<input type="radio"	name="answerYn" v-model="answerYn" value="ALL" checked>ALL
						</label>
						<label class="radio-inline">
							<input type="radio"	name="answerYn" v-model="answerYn" value="Y">Y
						</label>
						<label class="radio-inline">
							<input type="radio" name="answerYn" v-model="answerYn" value="N">N
						</label>
					</div>
					<div class="col-sm-6">
						<div class="dataTables_filter">
							<div class="input-group floatright">

								<input type="text" value="" v-model="searchVal" class="form-control" @keydown.enter="search()" placeholder="<spring:message code="search.placeholder" />">
								<span class="input-group-btn">
									<button class="btn btn-default searchBtn" type="button" @click="search()"> <span class="glyphicon glyphicon-search"></span></button>
								</span>
							</div>
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-sm-12">
						<template v-for="(item,index) in gridData">

		    				<div v-if="index != 0"><hr class="dotline"></div>

							<strong class="primary-font">{{ item.title}}</strong>
							<div class="btn-group pull-right">
							</div>
							<div><a>{{item.regInfo.viewName}}</a>&nbsp;{{item.regDt}}</div>
							<div><pre>{{ item.question}}</pre></div>

							<div class="form-group">
								<label>Answer ({{item.answerDt}})</label>
								<textarea v-model="item.answer" class="form-control answerTextArea" rows="3"></textarea>
							</div>
							<div class="text-right"><button type="button" class="btn btn-xs btn-primary" @click="save(item)">Save</button></div>
		    			</template>
		    			<div class="text-center" v-if="gridData.length === 0"><spring:message code="msg.nodata"/></div>
					</div>
					<page-navigation :page-info="pageInfo" callback="search"></page-navigation>
				</div>
			</div>
			<!-- /.panel-body -->
		</div>
		<!-- /.panel -->
	</div>
</div>
<!-- /.row -->


<script>
VarsqlAPP.vueServiceBean({
	el: '#varsqlVueArea'
	,validateCheck : true
	,data: {
		searchVal : ''
		,list_count :10
		,answerYn :'ALL'
		,pageInfo : {}
		,gridData :  []
		,detailItem : {}
		,isDetailFlag :false
	}
	,watch : {
		answerYn : function (val){
			this.search();
		}
	}
	,methods:{
		search : function(no){
			var _self = this;

			var param = {
				pageNo: (no?no:1)
				,rows: _self.list_count
				,'searchVal' : _self.searchVal
				,search_category : _self.answerYn
			};

			this.$ajax({
				url :{type:VARSQL.uri.manager, url:'/qnaMgmtList'}
				,data : param
				,success: function(resData) {
					_self.gridData = resData.list;
					_self.pageInfo = resData.page;
				}
			})
		}
		,save : function (item){
			var _this = this;
			var param ={
				qnaid : item.qnaid
				,title : "update"
				,question : "update"
				,answer : item.answer
			};

			_this.$ajax({
				url : {type:VARSQL.uri.manager, url:'/updQna'}
				,data : param
				,success:function (resData){
					if(VARSQL.req.validationCheck(resData)){
						if(resData.resultCode != 200){
							VARSQL.alertMessage(resData.message);
							return ;
						}

						if(resData.item > 0){
							VARSQL.toastMessage('msg.save.success');
							return ;
						}else{
							VARSQL.toastMessage('msg.save.success');
							return ;
						}
					}
				}
			});
		}
	}
});
</script>
