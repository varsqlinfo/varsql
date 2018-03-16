<!DOCTYPE html>
<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/include/tagLib.jspf"%>
<html>
<head>
<title>${left_db_object.connInfo.name}::<spring:message code="screen.user" /></title>
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
      </c:otherwise>
</c:choose>

	
	<div id="dbHiddenArea"></div>
	
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
		<iframe src="" class="preferences-frame"></iframe>
	</div>
</body>



<script>
$(document).ready(function(){
	var viewConnInfo = ${varsqlfn:objectToJson(left_db_object)};
	var opts = $.extend({param:{conuid:viewConnInfo.conuid},selector:'#leftDBList',dbtype:viewConnInfo.type}, viewConnInfo);
	opts.screenSetting = ${database_screen_setting};
	VARSQL.ui.create(opts);
	
	//varsqlMain.init();
}); 

</script>

</html>




