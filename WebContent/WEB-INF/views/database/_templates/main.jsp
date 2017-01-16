<!DOCTYPE html>
<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/include/tagLib.jspf"%>
<html>
<head>
<%@ include file="/WEB-INF/include/initvariable.jspf"%>
<title><spring:message code="screen.user" /></title>
<%@ include file="/WEB-INF/include/user-head.jsp"%>

</head>
<body>
<c:set var="pageType" value="custom9"></c:set>
<c:choose>
	<%-- 패이지 공통으로 쓰이는게 아닐경우 custom으로 해서 처리 할것.  --%> 
	<c:when test="${pageType=='custom'}">
		<div id="wrapper">
			<!-- Page Heading -->
			<div id="db-header">
				<div class="col-lg-12">
					<tiles:insertAttribute name="header" />
				</div>
			</div>
			<div id="db-page-wrapper" style="height:750px;"><!-- to do 수정할것 필히. -->
				<div class="container-fluid fill row" style="margin-right: 0px; ">
					<div class="row fill">
						<tiles:insertAttribute name="body" />
					</div>
				</div>
			</div>
		</div>
	</c:when>
	<c:otherwise>
        <div class="ui-layout-header-area">
			<div id="db-header" style="z-index:10;">
				<div class="col-lg-12">
					<tiles:insertAttribute name="header" />
				</div>
			</div>
		</div>
	
		<div class="ui-layout-left-area">
			<tiles:insertAttribute name="left" />
		</div>
	
		<div class="ui-layout-center-area">
			<tiles:insertAttribute name="rightContent" />
		</div>
		<div class="ui-layout-footer-area">ytechinfo copy right</div>
      </c:otherwise>
</c:choose>

	
	<div id="dbHiddenArea"></div>
	
	<form name="downloadForm" id="downloadForm"  style="display:none;" target="hiddenIframe"></form>
	<iframe name="hiddenIframe" id="hiddenIframe"  style="width:0px;height:px;display:none;"></iframe>
	
	<div id="memoTemplate" style="display:none;" title="메시지 보내기">
		<div style="margin: 0px -10px 0px -10px;">
			<div class="col-xs-6">
				<div class="panel panel-default">
					<div class="panel-heading">
						<input type="text" id="recv_user_search" name="recv_user_search" class="form-control" placeholder="보낼사용자검색">
						<div id="recv_autocomplete_area" class=""></div>
					</div>
					<!-- /.panel-heading -->
					<div class="panel-body">
						<div class="list-group memo-recv-id-cont" id="recvIdArr">
						</div>
					</div>
					<!-- /.panel-body -->
				</div>
				<!-- /.panel -->
			</div>
			<!-- /.col-lg-4 -->
			<div class="col-xs-6">
				<div class="panel panel-default">
					<!-- /.panel-heading -->
					<div class="panel-body">
						<input type="text" id="memoTitle" name="memoTitle" value="" class="form-control" placeholder="제목" style="margin-bottom:5px;">
						<textarea id="memoContent" name="memoContent" class="form-control" rows="7" placeholder="내용"></textarea>
					</div>
					<!-- /.panel-body -->
				</div>
				<!-- /.panel -->
			</div>
			<!-- /.col-lg-8 -->
		</div>
	</div>
	
	<div id="preferencesTemplate" style="display:none;margin:0px;padding:0px;" title="환경설정">
		<div style="margin: 0px 0px 0px 0px;">
			<iframe src="<c:url value="/preferences/main.vsql?vconnid=${param.vconnid}" />" style="border:0px;width:100%;height:400px;"></iframe>
		</div>
	</div>
</body>

<script>
$(document).ready(function(){
	var viewConnInfo = ${left_db_object}; 
	VARSQL.ui.create($.extend({}, {param:{vconnid:'${param.vconnid}'},selector:'#leftDBList',dbtype:viewConnInfo.connInfo.type}, viewConnInfo));
	
	//varsqlMain.init();
}); 

</script>

</html>




