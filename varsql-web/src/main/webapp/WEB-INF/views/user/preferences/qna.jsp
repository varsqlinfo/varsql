<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/include/tagLib.jspf"%>

<div class="row">
    <div class="col-lg-12">
        <h1 class="page-header"><spring:message code="user.prefernces.menu.qna" /></h1>
    </div>
    <!-- /.col-lg-12 -->
</div>
<div class="row display-off" id="varsqlVueArea">
	<div class="col-xs-5">
		<div class="panel panel-default">
			<div class="panel-heading">
				<div class="input-group">
					<input type="text" v-model="searchVal" class="form-control" @keydown.enter="search()" placeholder="<spring:message code="search.placeholder" />">
					<span class="input-group-btn">
						<button class="btn btn-default searchBtn" type="button" @click="search()"> <span class="glyphicon glyphicon-search"></span></button>
					</span>
				</div>
			</div>
			<!-- /.panel-heading -->
			<div class="panel-body">
				<div class="list-group" id="dbinfolist">
					<template v-for="(item,index) in gridData">
						<div class="list-group-item">
							<a href="javascript:;" @click="qnaModify(item)">
								<span class="clickItem" >{{item.title}}</span>
							</a>
							<div class="pull-right">
			    				<span style="width:60px;">{{item.regDt}}</span>
			    			</div>
		    			</div>
	    			</template>
	    			<div class="text-center" v-if="gridData.length === 0"><spring:message code="msg.nodata"/></div>
				</div>

				<page-navigation :page-info="pageInfo" callback="search"></page-navigation>
			</div>
			<!-- /.panel-body -->
		</div>
		<!-- /.panel -->
	</div>
	<!-- /.col-lg-4 -->
	<div class="col-xs-7" >
		<div class="panel panel-default detail_area_wrapper" >
			<div class="panel-heading"><spring:message code="detail.view" /><span style="margin-left:10px;font-weight:bold;">{{detailItem.title}}</span></div>
			<!-- /.panel-heading -->
			<div class="panel-body">
				<form id="addForm" name="addForm" class="form-horizontal" onsubmit="return false;">
					<div class="form-group">
						<div class="col-sm-12">
							<div class="pull-right">
								<button type="button" v-if="isDetailFlag" @click="qnaModify()" class="btn btn-default"><spring:message code="new"/></button>
								<button type="button" @click="save()" class="btn btn-info"><spring:message code="save"/></button>
								<button type="button" v-if="isDetailFlag && detailItem.answerYn == 'N'" @click="deleteInfo()" class="btn btn-primary"><spring:message code="delete" /></button>
							</div>
						</div>
					</div>

					<div class="form-group" :class="errors.has('Title') ? 'has-error' :''">
						<div class="col-xs-12"><label class="control-label"><spring:message code="title" /></label></div>

						<div class="col-xs-12">
							<input type="text" v-model="detailItem.title"  v-validate="'required'" name="Title" class="form-control" />
							<div v-if="errors.has('Title')" class="help-block">{{errors.first('Title')}}</div>
						</div>
					</div>
					<div class="form-group" :class="errors.has('Question') ? 'has-error' :''">
						<div class="col-xs-12"><label class="control-label"><spring:message code="question" /></label></div>
						<div class="col-xs-12">
							<textarea v-model="detailItem.question" rows="3" v-validate="'required'" name="Question" class="form-control"></textarea>
							<div v-if="errors.has('Question')" class="help-block">{{errors.first('Question')}}</div>
						</div>
					</div>
					<div class="form-group">
						<div class="col-xs-12">
							<label class="control-label"><spring:message code="answer" /></label>
							<span v-if="detailItem.answerYn != 'N'">({{detailItem.answerDt}})</span>
						</div>
						<div class="col-xs-12">
							<pre style="height:200px;overflow:auto;">{{detailItem.answer}}</pre>
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
<script>
VarsqlAPP.vueServiceBean({
	el: '#varsqlVueArea'
	,validateCheck : true
	,data: {
		searchVal : ''
		,pageInfo : {}
		,gridData :  []
		,detailItem : {}
		,isDetailFlag :false
	}
	,methods:{
		init : function(){
			this.qnaModify();
		}
		,search : function(no){
			var _self = this;

			var param = {
				pageNo: (no?no:1)
				,rows: _self.list_count
				,'searchVal':_self.searchVal
			};

			this.$ajax({
				url :{type:VARSQL.uri.user, url:'/preferences/qnaList'}
				,data : param
				,success: function(resData) {
					_self.gridData = resData.list;
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

					_this.$ajax({
						url : {type:VARSQL.uri.user, url:'/preferences/insQna'}
						,data : saveItem
						,success:function (resData){
							if(VARSQL.req.validationCheck(resData)){
								if(resData.resultCode != 200){
									VARSQL.alertMessage(resData.message);
									return ;
								}
								_this.qnaModify();
								_this.search();
							}
						}
					});
				}
			});
		}
		,qnaModify : function (item){

			if(VARSQL.isUndefined(item)){
				this.detailItem = {
					title : ''
					,question :''
				}
				this.isDetailFlag = false;
			}else{
				this.isDetailFlag = true;
				this.detailItem = item;
			}
		}
		,deleteInfo : function(){
			var _this = this;

			var item = this.detailItem;

			if(!VARSQL.confirmMessage('msg.delete.confirm')){
				return ;
			}

			this.$ajax({
				url : {type:VARSQL.uri.user, url:'/preferences/delQna'}
				,data : {
					qnaid : item.qnaid
				}
				,success:function (resData){
					_this.qnaModify();
					_this.search();
				}
			});
		}
	}
});
</script>
