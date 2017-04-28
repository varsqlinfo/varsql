<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/include/tagLib.jspf"%>
<script>
$(document).ready(function (){
	qnaMgmt.init();
});

var qnaMgmt ={
	init:function (){
		var _self = this; 
		_self.initEvt();
		_self.search();
	}
	,initEvt:function (){
		var _self = this; 
		//search
		$('.searchBtn').click(function() {
			_self.search();
		});
		
		$('#searchval').keydown(function() {
			if(event.keyCode =='13') _self.search();
		});
		
		$('#rowDataSize').change(function() {
			_self.search();
		});
		
		$('input:radio[name=answerYn]').on('click',function (){
			_self.search();
		})
	}
	,search : function (no){
		var _self = this; 
		var param = {
			page:no?no:1
			,rows:$('#rowDataSize').val()
			,'searchval':$('#searchval').val()
			,'answerYn' : $('input:radio[name=answerYn]:checked').val()
		};
		
		VARSQL.req.ajax({
			type:'POST'
			,data:param
			,url : {gubun:VARSQL.uri.manager, url:'/qnaMgmtList'}
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
		    				strHtm.push('<div><hr class="dotline"></div>');
		    			}
		    			
		    			strHtm.push('<strong class="primary-font">'+ item.TITLE+'</strong>');
		    			strHtm.push('<div class="btn-group pull-right">');
		    			strHtm.push('</div>');
		    			strHtm.push('<div><a>'+item.UNAME+'</a>&nbsp;'+item.CHAR_CRE_DT+'</div>');
		    			strHtm.push('<div><p>'+ item.QUESTION+'</p></div>');
		    			
	    				strHtm.push('<div class="form-group">');
	    				strHtm.push('	<label>Answer</label>');
	    				strHtm.push('	<textarea class="form-control answerTextArea '+item.QNAID+'" rows="3">'+(answerflag?item.ANSWER:'')+'</textarea>');
	    				strHtm.push('</div>');
	    				strHtm.push('<div class="text-right"><button type="button" class="btn btn-xs btn-primary btnReply" qnaid="'+item.QNAID+'">Save</button></div>');
	    				
		    		}
		    		
		    		$('#contentArea').html(strHtm.join(''));
		    		
		    		qnaMgmt.btnReplyEvent();
		    		
		    		$('.pageNavigation').pagingNav(response.paging,qnaMgmt.search);
		    		
				}catch(e){
					$('#dataViewAreaTd').attr('color','red');
					$("#dataViewAreaTd").val("errorMsg : "+e+"\nargs : " + resultMsg);  
				}
			}
		});
	}
	,btnReplyEvent : function(){
		
		$('.btnReply').click(function (){
			var tmpQnaId = $(this).attr('qnaid');
			var param ={
				qnaid:tmpQnaId
				,answer:$('.'+tmpQnaId).val()
			};
			VARSQL.req.ajax({
				type:'POST'
				,data:param
				,url : {gubun:VARSQL.uri.manager, url:'/updQna'}
				,dataType:'JSON'
				,success:function (response){
					if(response.result){
						alert('<spring:message code="msg.save.success" />');
						return ; 
					}else{
						alert('<spring:message code="msg.save.fail" />');
						return ; 
					}
				}
			});
		});
	}
}	
</script>
<!-- Page Heading -->
<div class="row">
    <div class="col-lg-12">
        <h1 class="page-header"><spring:message code="manage.menu.qnamgmt" /></h1>
    </div>
    <!-- /.col-lg-12 -->
</div>
<div class="row">
	<div class="col-lg-12">
		<div class="panel panel-default">
			<!-- /.panel-heading -->
			<div class="panel-body">
				<div class="row">
					<div class="col-sm-6">
						<label> <select name="rowDataSize" id="rowDataSize"
							aria-controls="dataTables-example" class="form-control input-sm"><option
									value="10">10</option>
								<option value="25">25</option>
								<option value="50">50</option>
								<option value="100">100</option></select>
						</label> 
						<label class="radio-inline">답변여부</label> 
						<label class="radio-inline"> 
							<input type="radio"	name="answerYn" value="ALL" checked>ALL
						</label> 
						<label class="radio-inline"> 
							<input type="radio"	name="answerYn" value="Y">Y
						</label> 
						<label class="radio-inline"> 
							<input type="radio" name="answerYn" value="N">N
						</label>
					</div>
					<div class="col-sm-6">
						<div class="dataTables_filter">
							<div class="input-group floatright">
								<input type="text" value="" id="searchval" name="searchval"
									class="form-control"
									onkeydown="" placeholder="<spring:message code="msg.search.placeholder" />"> <span
									class="input-group-btn">
									<button class="btn btn-default searchBtn" type="button">
										<span class="glyphicon glyphicon-search"></span>
									</button>
								</span>
							</div>
						</div>
					</div>
				</div>
				<div class="row">
					<div id="contentArea" class="col-sm-12"></div>
					<div class="pageNavigation"></div>
				</div>
			</div>
			<!-- /.panel-body -->
		</div>
		<!-- /.panel -->
	</div>
</div>
<!-- /.row -->
