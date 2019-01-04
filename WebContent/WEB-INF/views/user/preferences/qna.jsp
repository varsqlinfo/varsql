<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/include/tagLib.jspf"%>

<div class="row">
    <div class="col-lg-12">
        <h1 class="page-header"><spring:message code="user.menu.edit.qna" /></h1>
    </div>
    <!-- /.col-lg-12 -->
</div>
<div class="row display-off" id="varsqlVueArea">
	<div class="col-xs-5">
		<div class="panel panel-default">
			<div class="panel-heading">
				<div class="input-group">
					<input type="text" value="" v-model="searchVal" class="form-control" @keydown.enter="search()">
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
								<span class="clickItem" >{{item.TITLE}}</span>
							</a>
							<div class="pull-right">
			    				<span style="width:60px;">{{item.CHAR_CRE_DT}}</span>
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
			<div class="panel-heading"><spring:message code="detail.view" /><span style="margin-left:10px;font-weight:bold;">{{detailItem.TITLE}}</span></div>
			<!-- /.panel-heading -->
			<div class="panel-body">
				<form id="addForm" name="addForm" class="form-horizontal" onsubmit="return false;">
					<div class="form-group">
						<div class="col-sm-12">
							<div class="pull-right">
								<button type="button" v-if="isDetailFlag" @click="qnaModify()" class="btn btn-default"><spring:message code="btn.add"/></button>
								<button type="button" @click="save()" class="btn btn-info"><spring:message code="btn.save"/></button>
								<button type="button" v-if="isDetailFlag" @click="deleteInfo()" class="btn btn-default"><spring:message code="btn.delete" /></button>
							</div>
						</div>
					</div>
					
					<div class="form-group" :class="errors.has('TITLE') ? 'has-error' :''">
						<div class="col-xs-12"><label class="control-label"><spring:message code="guest.form.title" /></label></div>
			
						<div class="col-xs-12">
							<input type="text" v-model="detailItem.TITLE"  v-validate="'required'" name="TITLE" class="form-control" />
							<div v-if="errors.has('TITLE')" class="help-block">{{errors.first('TITLE')}}</div>
						</div>
					</div>
					<div class="form-group" :class="errors.has('QUESTION') ? 'has-error' :''">
						<div class="col-xs-12"><label class="control-label"><spring:message code="guest.form.question" /></label></div>
						<div class="col-xs-12">
							<textarea v-model="detailItem.QUESTION" rows="3" v-validate="'required'" name="QUESTION" class="form-control"></textarea>
							<div v-if="errors.has('QUESTION')" class="help-block">{{errors.first('QUESTION')}}</div>
						</div>
					</div>
					<div class="form-group">
						<div class="col-xs-12"><label class="control-label"><spring:message code="guest.form.answer" /></label></div>
						<div class="col-xs-12">
							<pre style="height:200px;overflow:auto;">{{detailItem.ANSWER}}</pre>
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
				url :{type:VARSQL.uri.user, url:'/qnaList'}
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
						url : {type:VARSQL.uri.user, url:'/insQna'}
						,data : param 
						,success:function (resData){
							if(VARSQL.req.validationCheck(resData)){
								if(resData.resultCode != 200){
									alert(resData.message);
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
					TITLE : ''
					,QUESTION :''
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
			
			if(!confirm('<spring:message code="msg.delete.confirm"/>')){
				return ; 	
			}
			
			this.$ajax({
				url : {type:VARSQL.uri.user, url:'/delQna'}
				,data : {
					qnaid : item.QNAID
				}
				,success:function (resData){
					_this.qnaModify();
					_this.search();
				}
			});
		}
		,goMain : function (){
			location.href ='${varsqlLogoutUrl}';
		}
	}
});
</script>
