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
<script src="${pageContextPath}/webstatic/js/bootstrap.min.js" type="text/javascript"></script>
<script src="${pageContextPath}/webstatic/js/bootstrapValidator.js" type="text/javascript"></script>
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
		<form name="guestForm" id="guestForm" action="<c:url value="/guest/insQna" />" method="post" class="form-horizontal well" role="form">
								
			<div class="form-group"  :class="errors.has('TITLE') ? 'has-error' :''">
				<label><spring:message code="guest.form.title" /></label> 
				<input type="text" class="form-control" v-model="detailItem.TITLE" placeholder="<spring:message code="guest.form.title" />" v-validate="'required'" name="TITLE" class="form-control" />
				<div v-if="errors.has('TITLE')" class="help-block">{{errors.first('TITLE')}}</div>
			</div>
			<div class="form-group" :class="errors.has('QUESTION') ? 'has-error' :''">
				<label><spring:message code="guest.form.question" /></label>
				<textarea v-model="detailItem.QUESTION" class="form-control" rows="3" placeholder="<spring:message code="guest.form.question"/>" v-validate="'required'" name="QUESTION"></textarea>
				<div v-if="errors.has('QUESTION')" class="help-block">{{errors.first('QUESTION')}}</div>
			</div>

			<div class="form-group">
				<div class="col-sm-12 text-center">
					<button type="button" @click="save()" class="btn btn-info"><spring:message code="btn.save"/></button>
					<button type="button" @click="goMain()" class="btn btn-default">
						<spring:message code="btn.login.screen" />
					</button>
				</div>
			</div>
		</form>
		<!--/form-->

		<div class="chat-panel panel panel-default">
			<div class="panel-heading">
				<spring:message code="guest.form.question" />
				<div class="input-group">
					<input type="text" class="form-control input-sm"
						placeholder="<spring:message code="msg.search.placeholder" />"> <span
						class="input-group-btn" v-model="searchVal" class="form-control" @keydown.enter="search()">
						<button type="button" class="btn btn-warning btn-sm searchBtn"  @click="search()">
							<spring:message code="btn.search" />
						</button>
					</span>
				</div>
			</div>
			<!-- /.panel-heading -->
			<div class="panel-body">
				<div v-for="(item,index) in gridData">
					
					<hr v-if="index!=0" class="dotline">
	    			
					<strong class="primary-font">{{item.TITLE}}</strong> 
	    			<div class="btn-group pull-right" qnaid="{{item.QNAID}}">
	    			<button type="button" class="btn btn-default btn-xs dropdown-toggle" data-toggle="dropdown">
	    			    <i class="fa fa-chevron-down"></i>
	    			</button>
	    			<ul class="dropdown-menu slidedown">
	    				<li v-if="item.ANSWER==''"><a href="javascript:;" class="modifyBtn" @click="qnaModify(item)">Modify</a></li>
	    			    <li><a href="javascript:;"  class="deleteBtn" @click="deleteInfo(item)">Delete</a></li>
	    			</ul>
	    			</div>
	    			<div>{{item.CHAR_CRE_DT}}</div>
	    			<p>{{item.QUESTION}}</p>
	    			
	    			<template v-if="item.ANSWER">
		    			<div class="replymargin30">
							<strong class="primary-font"><spring:message code="guest.form.answer"/></strong> 
							<small class="pull-right text-muted">
								<i class="fa fa-clock-o fa-fw"></i> {{item.CHAR_UPD_DT);
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
</body>
</html>


<script>
VarsqlAPP.vueServiceBean( {
	el: '#varsqlVueArea'
	,validateCheck : true 
	,data: {
		list_count :10
		,searchVal : ''
		,pageInfo : {}
		,gridData :  []
		,detailItem :{}
	}
	,methods:{
		init : function(){
			//this.setDetailItem();
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
					
					var param = _this.detailItem
					
					_this.$ajax({
						url : {type:VARSQL.uri.guest, url:'/insQna'}
						,data : param 
						,success:function (resData){
							if(VARSQL.req.validationCheck(resData)){
								if(resData.resultCode != 200){
									alert(resData.message);
									return ; 
								}
								_this.search();
							}
						}
					});
				}
			});
		}
		,qnaModify : function (item){
			this.detailItem = item; 
		}
		,deleteInfo : function(){
			
			if(!confirm('<spring:message code="msg.delete.confirm"/>')){
				return ; 	
			}
			$('#qnaid').val($('.open').attr('qnaid'));
			
			$('#guestForm').attr('action','<c:url value="/guest/delQna" />');
			
			document.guestForm.submit();
		}
		,goMain : function (){
			location.href ='${varsqlLogoutUrl}';
		}
	}
});
</script>