<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/include/tagLib.jspf"%>
<div id="dbHiddenArea"></div>

<%--메모 보내기 다이얼로그 --%>
<div id="memoTemplate" style="display:none;overflow: hidden;" title="<spring:message code="msg.sendmsg.title" />">
	<div style="margin: 0px -10px 0px -10px;">
		<div class="col-xs-6">
			<div class="panel panel-default">
				<div class="panel-heading">
					<input type="text" id="recv_user_search" name="recv_user_search" class="form-control" placeholder="<spring:message code="msg.sendmsg.recv.txt.holder" />">
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
			<!-- /.panel-heading -->
			<input type="text" id="memoTitle" name="memoTitle" value="" class="form-control" placeholder="<spring:message code="title" />" style="margin-bottom:5px;">
			<textarea id="memoContent" name="memoContent" class="form-control" style="height: 210px;" placeholder="<spring:message code="content" />"></textarea>
		</div>
	</div>
</div>

<div id="confirmTemplateTemplate" style="display:none;overflow: hidden;" title="Confirm">
	<div style="margin: 0px -10px 0px -10px;">
		<div>저장하고 닫으시겠습니까?</div>
	</div>
</div>

<%--설정 --%>
<div id="preferencesTemplate" style="display:none;margin:0px;padding:0px;" title="<spring:message code="preferences" />">
	<iframe src="" class="frame-wh100"></iframe>
</div>

<%--varsql 정보 --%>
<div id="aboutVarsqlDialog" style="display:none;margin:0px;padding:0px;" title="Varsql정보">
	<div style="position:absolute; width:32px;height:32px;">
		<img src="${pageContextPath}/webstatic/vt/vt32.png">
	</div>
	<div class="user-select-on" style="width:100%; padding-left:40px; height:100%; overflow:auto;">
		<div style="padding-top :10px;">
			<div style="font-weight: bold;font-size: 14pt;">Varsql 정보</div>
			<div style="padding-top:10px;">Version : 0.5</div>
			<!-- div style="padding-top:10px;">email : ytechinfo@gamil.com</div -->
		</div>
	</div>
</div>

<%--db 정보 --%>
<div id="aboutDbInfoDialog" style="display:none;margin:0px;padding:0px;" title="DB Info">
	<div class="user-select-on" style="width:100%; padding-left:40px; height:100%; overflow:auto;">
		<div style="padding-top :10px;">
			<div style="font-weight: bold;font-size: 14pt;">DB info</div>
			<div style="padding-top:10px;" id="epHeaderDialogDbInfo"></div>
			<!-- div style="padding-top:10px;">email : ytechinfo@gamil.com</div -->
		</div>
	</div>
</div>

<%--editor 문자 찾기 다이얼로그. --%>
<div id="editorFindTextDialog" style="display:none;overflow: hidden;" title="찾기">
	<div class="find-text-area">
		<ul class="find-text">
			<li>
				<label class="find-text-label">Find</label>
				<span class="find-text-input-area"><input type="text" id="editorFindText" name="editorFindText"></span>
			</li>
			<li>
				<label class="find-text-label">Replace with</label>
				<span class="find-text-input-area"><input type="text" id="editorReplaceText" name="editorReplaceText"></span>
			</li>
		</ul>
		<div class="rows">
			<div>방향</div>
			<ul class="find-text-option-area">
				<li>
					<label class="checkbox-container">아래
					  <input type="radio" name="find-text-direction" value="down" checked="checked">
					  <span class="radiomark"></span>
					</label>
				</li>
				<li>
					<label class="checkbox-container">위
					  <input type="radio" name="find-text-direction" value="up">
					  <span class="radiomark"></span>
					</label>
				</li>
			</ul>
		</div>
		<div class="rows">
			<div>옵션</div>
			<ul class="find-text-option-area">
				<li>
					<label class="checkbox-container">대소문자
					  <input type="checkbox" name="find-text-option" value="caseSearch">
					  <span class="checkmark"></span>
					</label>
				</li>
				<li>
					<label class="checkbox-container">끝에서 되돌리기
					  <input type="checkbox" name="find-text-option" value="wrapSearch" checked="checked">
					  <span class="checkmark"></span>
					</label>
				</li>
				<li>
					<label class="checkbox-container">정규식
					  <input type="checkbox" name="find-text-option" value="regularSearch">
					  <span class="checkmark"></span>
					</label>
				</li>
			</ul>
		</div>
		<div class="rows">
			<ul class="find-text-button">
				<li><button type="button" class="find_text_btn">찾기</button></li>
				<li><button type="button" class="find_replace_btn">바꾸기</button></li>
				<li><button type="button" class="find_all_replace_btn">모두바꿈</button></li>
				<li><button type="button" class="find_close_btn">닫기</button></li>
			</ul>
		</div>
	</div>
</div>

<%--editor 문자 찾기 다이얼로그. --%>
<div id="editorNewSqlFileDialog" style="display:none;margin:0px;padding:0px;overflow: hidden;" title="Sql File">
	<div class="new-sqlfile-area" style="padding:10px;">
		<div>
			<label class="sqlfile-name-label">File</label>
		</div>
		<div class="sqlfile-name-input-area">
			<input type="hidden" id="editorSqlFileId" name="editorSqlFileId">
			<input type="text" id="editorSqlFileNameText" class="form-control input-sm" placeholder="new file name">
		</div>
	</div>
</div>

<%--ddl view template --%>
<script id="ddlViewTemplate" type="text/varsql-template">
<div class="ddl-view-area">
	<div class="pull-right" style="padding:4px;position:relative;z-index:1;">
		<!-- button type="button" class="btn btn-sm btn-default ddl-copy" data-ddl-copy-mode="copy">띄워보기</button -->
		<button type="button" class="btn btn-sm btn-default ddl-copy" data-ddl-copy-mode="copy">복사</button>
	</div>
	<div  class="wh100-absolute" style="padding-top:30px;z-index:0;">
		<pre class="user-select-on prettyprint lang-sql"></pre>
		<textarea style="display:none;"></textarea>
	</div>
</div>
</script>

<%--data export template --%>
<script id="dataExportTemplate" type="text/varsql-template">
<div id="data-export-modal" title="데이타 내보내기">
	<div class="export-dialog-area">
		<div class="export-column-area">
			<table class="varsql-table border-zero">
				<colgroup>
					<col style="width:50px;">
					<col style="width:150px;">
					<col style="width:*;">
				</colgroup>
				<thead>
					<tr role="row">
						<th class="text-center"><input
							type="checkbox" name="exportColumnCheckBox" value="all">all</th>
						<th>Column</th>
						<th>Desc</th>
					</tr>
				</thead>
				<tbody id="exportColumnInfoArea" class="dataTableContent">
					{{{exportColumnInfo}}}
				</tbody>
			</table>
		</div>
		<div class="export-type-area">
			<div>
				<label class="control-label">LIMIT COUNT</label>
				<input class="" id="exportCount" name="exportCount" value="1000">
			</div>
			<ul class="export-type">
				<li>
					<label class="checkbox-container">CSV
					  <input type="radio" name="exportType" value="csv" checked="checked">
					  <span class="radiomark"></span>
					</label>
				</li>
				<li>
					<label class="checkbox-container">JSON
					  <input type="radio" name="exportType" value="json">
					  <span class="radiomark"></span>
					</label>
				</li>
				<li>
					<label class="checkbox-container">INSERT문
					  <input type="radio" name="exportType" value="insert">
					  <span class="radiomark"></span>
					</label>
				</li>
				<li>
					<label class="checkbox-container">XML
					  <input type="radio" name="exportType" value="xml">
					  <span class="radiomark"></span>
					</label>
				</li>
				<li>
					<label class="checkbox-container">Excel
					  <input type="radio" name="exportType" value="excel">
					  <span class="radiomark"></span>
					</label>
				</li>
			</ul>
		</div>
	</div>
</div>
</script>



