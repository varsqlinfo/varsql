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
			<tiles:insertAttribute name="initLeft" />
			<tiles:insertAttribute name="left" />
		</div>
	
		<div class="ui-layout-center-area">
			<tiles:insertAttribute name="rightContent" />
			<!-- div class="inner-layout-sql-editor-area">sql editor</div>
			<div class="inner-layout-result-area">grid area</div -->
		</div>
		<div class="ui-layout-footer-area">ytechinfo copy right</div>
		<script>
		var varsqlMainLayout, varsqlLeftLayout,  varsqlRightLayout;
		
		$(document).ready(function(){
		
			varsqlMainLayout = $('body').layout({
				center__paneSelector:	".ui-layout-center-area"
				, west__paneSelector:	".ui-layout-left-area"
				, north__paneSelector: ".ui-layout-header-area"
				, south__paneSelector : ".ui-layout-footer-area"
				, west__size:				300 
				, spacing_open:			5 // ALL panes
				, spacing_closed:   8 // ALL panes
				, north__spacing_open: 0
				, south__spacing_open: 0
				, resizerDblClickToggle: false
				, center__onresize:  "varsqlRightLayout.resizeAll"
				, west__onresize:  "varsqlLeftLayout.resizeAll"
			});
			
			varsqlLeftLayout = $('div.ui-layout-left-area').layout({
				north__paneSelector: ".ui-layout-left-top-area"
				, center__paneSelector: ".ui-layout-left-middle-area"
				, south__paneSelector: ".ui-layout-left-bottom-area"
				, north__size:    125
				, south__size:    150
				, spacing_open:   5  // ALL panes  //0 일경우 버튼 사라짐.
				, spacing_closed:   8  // ALL panes
				, resizerDblClickToggle: false
				, center__onresize_end:  function (obj1, obj2 ,obj3 ,obj4 ,obj5){
					if($('.db-metadata-area.show-display').length > 0){
						$.pubGrid('#left_service_menu_content>#'+$('.db-metadata-area.show-display').attr('id')).resizeDraw({width:obj3.layoutWidth,height:obj3.layoutHeight-24});
					}
				}
				,south__onresize_end :  function (obj1, obj2 ,obj3 ,obj4 ,obj5){
					if($('.varsql-meta-cont-ele.on').length > 0){
						$.pubGrid('#'+$('.varsql-meta-cont-ele.on').attr('id')).resizeDraw({width:obj3.resizerLength,height:obj3.layoutHeight});
					}
				}
			});
		
			varsqlRightLayout = $('div.ui-layout-center-area').layout({
				north__paneSelector: ".inner-layout-toolbar-area"
				, center__paneSelector: ".inner-layout-sql-editor-area"
				, south__paneSelector: ".inner-layout-result-area"
				, north__size:    65
				, north__resizable: false
				, south__size:    100 
				, spacing_open:   5  // ALL panes  //0 일경우 버튼 사라짐.
				, spacing_closed:   8  // ALL panes
				, north__spacing_open: 0
				, resizerDblClickToggle: false
				, center__onresize:  function (obj1, obj2 ,obj3 ,obj4 ,obj5){
					$('.CodeMirror.cm-s-default').css('height' ,obj3.layoutHeight);
				}
				,south__onresize_end :  function (obj1, obj2 ,obj3 ,obj4 ,obj5){
					try{
						if($('#dataGridArea .pubGrid-body-container').length > 0){
							$.pubGrid('#dataGridArea').resizeDraw({width:obj3.resizerLength,height:obj3.css.height-25});
						}
					}catch(e){
						console.log(e)
					}
				}
			});
			
			$('.CodeMirror.cm-s-default').css('height' ,$('#editorAreaTable').height());
			VARSQL.ui.SQL.sqlTextAreaObj.refresh();
		}); 
		</script>
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
						<input type="text" id="recvUserSearch" name="recvUserSearch" class="form-control" placeholder="보낼사용자검색">
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
</body>
</html>