<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/include/tagLib.jspf"%>
<%@ page import=" java.util.*, java.io.*" %>
<!doctype html>
<HTML>
<head>
<title><spring:message code="page.title.varsql"/></title>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<%@ include file="/WEB-INF/include/initvariable.jspf"%>

<script src="${pageContextPath}/webstatic/js/jquery-1.10.2.min.js"></script>
<script src="${pageContextPath}/webstatic/js/varsql.web.js"></script>

<script src="${pageContextPath}/webstatic/js/plugins/polyfill/polyfill.min.js"></script>


<script src="${pageContextPath}/webstatic/js/vue.min.js"></script>
<script src="${pageContextPath}/webstatic/js/plugins/vue/vee-validate.min.js"></script>
<script src="${pageContextPath}/webstatic/js/vue.varsql.js"></script>

<!-- Bootstrap Core CSS -->
<link href="${pageContextPath}/webstatic/css/bootstrap.min.css" rel="stylesheet">

<link href="${pageContextPath}/webstatic/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">

<link href="${pageContextPath}/webstatic/css/varsql.common.css" rel="stylesheet" type="text/css">

</head>
<body>
	<header class="navbar navbar-default">
	    <div class="container">
	        <div class="navbar-header">
	            <a class="navbar-brand" href="javascript:;"><spring:message code="guest.title"/></a>
	        </div>
	    </div>
	</header>

	<div class="container" id="varsqlVueArea">
		<h3 class="page-header">
			<sec:authentication property="principal.fullname" />
			<spring:message code="guest.message" />
		</h3>
		<!-- form start -->
		<form class="form-horizontal" onsubmit="return false; ">
			<div class="form-group" :class="errors.has('TITLE') ? 'has-error' :''">
				<label><spring:message code="guest.form.title" /></label> 
				<input type="text" v-model="detailItem.TITLE"  v-validate="'required'" name="TITLE" class="form-control" />
				<div v-if="errors.has('TITLE')" class="help-block">{{errors.first('TITLE')}}</div>
			</div>
			<div class="form-group" :class="errors.has('QUESTION') ? 'has-error' :''">
				<label><spring:message code="guest.form.question" /></label>
				<textarea v-model="detailItem.QUESTION" rows="3" v-validate="'required'" name="QUESTION" class="form-control"></textarea>
				<div v-if="errors.has('QUESTION')" class="help-block">{{errors.first('QUESTION')}}</div>
			</div>

			<div class="form-group">
				<div class="col-sm-12 text-center">
					<button type="button" v-if="isDetailFlag" @click="qnaModify()" class="btn btn-default"><spring:message code="btn.add"/></button>
					<button type="button" @click="save()" class="btn btn-info"><spring:message code="btn.save"/></button>
					<button type="button" @click="goMain()" class="btn btn-default"><spring:message code="btn.login.screen" /></button>
				</div>
			</div>
		</form>
		<!--/form-->

		<div class="row panel panel-default">
			<div class="panel-heading">
				<spring:message code="guest.form.question" />
				<div class="input-group">
					<input type="text" v-model="searchVal" @keydown.enter="search()" class="form-control input-sm" placeholder="<spring:message code="msg.search.placeholder" />"> 
					<span class="input-group-btn"  class="form-control">
						<button type="button" class="btn btn-warning btn-sm searchBtn"  @click="search()">
							<spring:message code="btn.search" />
						</button>
					</span>
				</div>
			</div>
			<!-- /.panel-heading -->
			<div class="panel-body">
				<div v-for="(item,index) in gridData">
					
					<hr v-if="index!=0" class="dotline" />
	    			
					<strong class="primary-font">{{item.TITLE}}</strong> 
	    			<div class="btn-group pull-right" v-if="item.ANSWER==''">
		    			<button type="button" class="btn btn-default btn-xs" @click="qnaModify(item)">
		    			    <i class="fa fa-edit"></i>
		    			</button>
		    			
		    			<button type="button" class="btn btn-primary btn-xs" style="margin-left:10px" @click="deleteInfo(item)">
		    			    <i class="fa fa-trash-o"></i>
		    			</button>
	    			</div>
	    			<div>{{item.CHAR_CRE_DT}}</div>
	    			<p>{{item.QUESTION}}</p>
	    			
	    			<template v-if="item.ANSWER">
		    			<div class="replymargin30">
							<strong class="primary-font"><spring:message code="guest.form.answer"/></strong> 
							<small class="pull-right text-muted">
								<i class="fa fa-clock-o fa-fw"></i>{{item.CHAR_UPD_DT}}
							</small>
							<p>{{ item.ANSWER}}</p>
		    			</div>
	    			</template>
    			</div>
    			
    			<div class="text-center" v-if="gridData.length === 0"><spring:message code="msg.nodata"/></div>
				
				<page-navigation :page-info="pageInfo" callback="search"></page-navigation>
			</div>
		</div>
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
				url :{type:VARSQL.uri.guest, url:'/qnaList'}
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
						url : {type:VARSQL.uri.guest, url:'/insQna'}
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
		,deleteInfo : function(item){
			var _this = this;
			
			if(!confirm('<spring:message code="msg.delete.confirm"/>')){
				return ; 	
			}
			
			this.$ajax({
				url : {type:VARSQL.uri.guest, url:'/delQna'}
				,data : {
					qnaid : item.QNAID
				}
				,success:function (resData){
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
</body>
</html>


