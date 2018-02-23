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

<!-- Bootstrap Core CSS -->
<link href="${pageContextPath}/webstatic/css/bootstrap.min.css" rel="stylesheet">

<link href="${pageContextPath}/webstatic/font-awesome-4.1.0/css/font-awesome.min.css" rel="stylesheet" type="text/css">

<link href="${pageContextPath}/webstatic/css/varsql.main.css" rel="stylesheet" type="text/css">

<script>
$(document).ready(function (){
	guestMain.init();
});

var guestMain ={
	init:function (){
		var _self = this; 
		_self.initEvt();
		_self.search();
	}
	,initEvt : function (){
		var _self = this; 
		$('.btnMain').click(function (){
			location.href ='${varsqlLogoutUrl}';
		});
		
		//search
		$('.searchBtn').click(function() {
			_self.search();
		});
		
		$('#searchVal').keydown(function() {
			if(event.keyCode =='13') _self.search();
		});
		
		$('#guestForm').bootstrapValidator({
			message: 'This value is not valid',
			feedbackIcons: {
				valid: 'glyphicon glyphicon-ok',
				invalid: 'glyphicon glyphicon-remove',
				validating: 'glyphicon glyphicon-refresh'
			}
		});
	}
	,initQnaBtn : function (){
		var _self = this; 
		$('.modifyBtn').click(function (){
			_self.modifyInfo();
		});
		
		$('.deleteBtn').click(function (){
			_self.deleteInfo();
		});
	}
	,modifyInfo : function(){
		
		var param = {
			qnaid:$('.open').attr('qnaid')
		};
			
		VARSQL.req.ajax({
			type:'POST'
			,data:param
			,url : {gubun:VARSQL.uri.guest, url:'/detailQna'}
			,dataType:'JSON'
			,success:function (response){
				var item = response.result?response.result:{};
				
				$('#title').val(item.TITLE);
				$('#question').val(item.QUESTION);
				$('#title').focus();
				$('#qnaid').val(item.QNAID);
				
				$('#guestForm').attr('action','<c:url value="/guest/updQna" />');
				
			}
		});
	}
	,deleteInfo : function(){
		
		if(!confirm('<spring:message code="msg.delete.confirm"/>')){
			return ; 	
		}
		$('#qnaid').val($('.open').attr('qnaid'));
		
		$('#guestForm').attr('action','<c:url value="/guest/delQna" />');
		
		document.guestForm.submit();
	}
	,search : function (no){
		var _self = guestMain; 
		var param = {
			page:no?no:1
			,'searchVal':$('#searchVal').val()
		};
		
		VARSQL.req.ajax({
			type:'POST'
			,data:param
			,url :{gubun:VARSQL.uri.guest, url:'/qnaList'}
			,dataType:'JSON'
			,success:function (response){
				try{
					
		    		var resultLen = response.result?response.result.length:0;
		    		
		    		if(resultLen==0){
		    			$('#contentArea').html('<div class="text-center"><spring:message code="msg.nodata"/></div>');
		    			$('.pageNavigation').pagingNav();
		    			return ; 
		    		}
		    		var result = response.result;
		    		
		    		var strHtm = new Array();
		    		var item; 
		    		var answerflag; 
		    		for(var i = 0 ;i < resultLen; i ++){
		    			item = result[i];
		    			answerflag = item.ANSWER; 
		    			
		    			if(i!=0){
		    				strHtm.push('<hr class="dotline">');
		    			}
		    			
		    			strHtm.push(' <strong class="primary-font">'+ item.TITLE+'</strong> ');
		    			strHtm.push('<div class="btn-group pull-right" qnaid="'+item.QNAID+'">');
		    			strHtm.push('<button type="button" class="btn btn-default btn-xs dropdown-toggle" data-toggle="dropdown">');
		    			strHtm.push('    <i class="fa fa-chevron-down"></i>');
		    			strHtm.push('</button>');
		    			strHtm.push('<ul class="dropdown-menu slidedown">');
		    			if(!answerflag){
		    				strHtm.push(' <li><a href="javascript:;" class="modifyBtn">Modify</a></li>');
		    			}
		    			strHtm.push('    <li><a href="javascript:;"  class="deleteBtn">Delete</a></li>');
		    			strHtm.push('</ul>');
		    			strHtm.push('</div>');
		    			strHtm.push('<div>'+item.CHAR_CRE_DT+'</div>');
		    			strHtm.push('<p>'+ item.QUESTION+'</p>');
		    			
		    			if(answerflag){
			    			strHtm.push('	<div class="replymargin30">');
			    			strHtm.push(' <strong class="primary-font"><spring:message code="guest.form.answer"/></strong> ');
			    			strHtm.push(' <small class="pull-right text-muted">');
			    			strHtm.push('     <i class="fa fa-clock-o fa-fw"></i> '+item.CHAR_UPD_DT);
			    			strHtm.push(' </small>');
			    			strHtm.push('     <p>'+ item.ANSWER+'</p>');
			    			strHtm.push('	</div>');
		    			}
		    		}
		    		
		    		$('#contentArea').html(strHtm.join(''));
		    		
		    		_self.initQnaBtn();
		    		
		    		$('.pageNavigation').pagingNav(response.paging,_self.search);
		    		
				}catch(e){
					$('#dataViewAreaTd').attr('color','red');
					$("#dataViewAreaTd").val("errorMsg : "+e+"\nargs : " + resultMsg);  
				}
			}
		});
	}
}
</script>	
</head>
<body>

<header class="navbar navbar-default">
    <div class="container">
        <div class="navbar-header">
            <a class="navbar-brand" href="#"><spring:message code="guest.title"/></a>
        </div>
    </div>
</header>
<!--// header -->

	<div class="container">
		<h3 class="page-header">
			<sec:authentication property="principal.fullname" />
			<spring:message code="guest.message" />
		</h3>
		<!-- form start -->
		<form name="guestForm" id="guestForm"
			action="<c:url value="/guest/insQna" />" method="post"
			class="form-horizontal well" role="form">
			<input type="hidden" name="qnaid" id="qnaid" value="">
			<div class="form-group">
				<label for="inputEmail3"><spring:message
						code="guest.form.title" /></label> <input type="text" class="form-control"
					id="title" name="title"
					placeholder="<spring:message code="guest.form.title" />" required
					data-bv-notempty-message="<spring:message code="msg.title.empty" />" />
			</div>
			<div class="form-group">
				<label for="nickname"><spring:message
						code="guest.form.question" /></label>
				<textarea id="question" name="question" class="form-control"
					rows="3" placeholder="<spring:message code="guest.form.question"/>"
					required
					data-bv-notempty-message="<spring:message code="msg.contactus.empty" />"></textarea>
			</div>

			<div class="form-group">
				<div class="col-sm-12 text-center">
					<button type="submit" class="btn btn-info">
						<spring:message code="btn.contact" />
					</button>
					<button type="button" class="btn btn-default btnMain">
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
					<input id="searchVal" type="text" class="form-control input-sm"
						placeholder="<spring:message code="msg.search.placeholder" />"> <span
						class="input-group-btn">
						<button class="btn btn-warning btn-sm searchBtn">
							<spring:message code="btn.search" />
						</button>
					</span>
				</div>
			</div>
			<!-- /.panel-heading -->
			<div class="panel-body">
				<div id="contentArea"></div>
				<div class="pageNavigation"></div>
			</div>
		</div>
	</div>
</body>
</html>
