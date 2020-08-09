<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/include/tagLib.jspf"%>

<div id="varsqlExtensionsElementArea" style="position:absolute;top:0px;left:0px;width:0px;height:0px;"></div>

<div id="dbHiddenArea"></div>

<%--메모 보내기 다이얼로그 --%>
<div id="noteTemplate" style="display:none;overflow: hidden;" title="<spring:message code="msg.sendmsg.title" />">
	<div>
		<div class="col-xs-6">
			<div class="panel">
				<div class="panel-heading">
					<input type="text" id="recv_user_search" style="margin-bottom:5px;" name="recv_user_search" autocomplete="false" class="form-control" placeholder="<spring:message code="msg.sendmsg.recv.txt.holder" />">
					<div id="recv_autocomplete_area" class=""></div>
				</div>
				<!-- /.panel-heading -->
				<div class="panel-body">
					<div class="list-group note-recv-id-cont" id="recvIdArr">
					</div>
				</div>
				<!-- /.panel-body -->
			</div>
			<!-- /.panel -->
		</div>
		<!-- /.col-lg-4 -->
		<div class="col-xs-6">
			<div class="panel">
				<div class="panel-heading">
					<input type="text" id="noteTitle" name="noteTitle" value="" class="form-control" placeholder="<spring:message code="title" />">
				</div>
				<div>
					<textarea id="noteContent" name="noteContent" class="form-control" style="height: 230px;" placeholder="<spring:message code="content" />"></textarea>
				</div>
			</div>
		</div>
	</div>
</div>

<div id="confirmTemplateTemplate" style="display:none;overflow: hidden;" title="Confirm">
	<div>
		<div><spring:message code="msg.saveandclose.confirm" /></div>
	</div>
</div>

<%--설정 --%>
<div id="preferencesTemplate" style="display:none;margin:0px;padding:0px;" title="<spring:message code="preferences" />">
	<iframe src="" class="frame-wh100"></iframe>
</div>

<%--varsql 정보 --%>
<div id="aboutVarsqlDialog" style="display:none;margin:0px;padding:0px;" title="<spring:message code="varsql.info" />">
	<div style="position:absolute; width:32px;height:32px;">
		<img src="${pageContextPath}/webstatic/vt/vt32.png">
	</div>
	<div class="user-select-on" style="width:100%; padding-left:40px; height:100%; overflow:auto;">
		<div style="padding-top :10px;">
			<div style="font-weight: bold;font-size: 14pt;"><varsql:varsqlInfo attribute="name"/></div>
			<div style="padding-top:10px;">Version : <varsql:varsqlInfo attribute="version"/></div>
			<div style="padding-top:10px;"><a href="mailto:<varsql:varsqlInfo attribute="email"/>"><varsql:varsqlInfo attribute="email"/></a></div>
			<div style="padding-top:10px;">Site : <a href="<varsql:varsqlInfo attribute="homepage"/>" target="_blank"><varsql:varsqlInfo attribute="homepage"/></a></div>
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
<div id="editorFindTextDialog" style="display:none;overflow: hidden;" title="<spring:message code="find" />">
	<div class="find-text-area">
		<ul class="find-text">
			<li>
				<label class="find-text-label"><spring:message code="find.word" /></label>
				<span class="find-text-input-area"><input type="text" id="editorFindText" name="editorFindText"></span>
			</li>
			<li>
				<label class="find-text-label"><spring:message code="replace.word" /></label>
				<span class="find-text-input-area"><input type="text" id="editorReplaceText" name="editorReplaceText"></span>
			</li>
		</ul>
		<div class="rows">
			<div><spring:message code="direction" /></div>
			<ul class="find-text-option-area">
				<li>
					<label class="checkbox-container"><spring:message code="down" />
					  <input type="radio" name="find-text-direction" value="down" checked="checked">
					  <span class="radiomark"></span>
					</label>
				</li>
				<li>
					<label class="checkbox-container"><spring:message code="up" />
					  <input type="radio" name="find-text-direction" value="up">
					  <span class="radiomark"></span>
					</label>
				</li>
			</ul>
		</div>
		<div class="rows">
			<div><spring:message code="options" /></div>
			<ul class="find-text-option-area">
				<li>
					<label class="checkbox-container"><spring:message code="label.case.sensitive" />
					  <input type="checkbox" name="find-text-option" value="caseSearch">
					  <span class="checkmark"></span>
					</label>
				</li>
				<li>
					<label class="checkbox-container"><spring:message code="label.wrap.search" />
					  <input type="checkbox" name="find-text-option" value="wrapSearch" checked="checked">
					  <span class="checkmark"></span>
					</label>
				</li>
				<li>
					<label class="checkbox-container"><spring:message code="regular.expression" />
					  <input type="checkbox" name="find-text-option" value="regularSearch">
					  <span class="checkmark"></span>
					</label>
				</li>
			</ul>
		</div>
		<div class="rows">
			<ul class="find-text-button">
				<li><button type="button" class="find_text_btn"><spring:message code="find" /></button></li>
				<li><button type="button" class="find_replace_btn"><spring:message code="replace" /></button></li>
				<li><button type="button" class="find_all_replace_btn"><spring:message code="all.replace" /></button></li>
				<li><button type="button" class="find_close_btn"><spring:message code="close" /></button></li>
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
<div class="pretty-view-area">
	<div class="pull-right" style="position:relative;z-index:1;">
		<!-- button type="button" class="btn btn-sm btn-default ddl-copy" data-ddl-copy-mode="copy">띄워보기</button -->
		<button type="button" class="btn btn-sm btn-default ddl-copy" data-ddl-copy-mode="copy"><spring:message code="copy" /></button>
	</div>
	<div  class="wh100-absolute meta-ddl-view">
		<pre class="user-select-on prettyprint lang-sql" contenteditable="true"></pre>
		<textarea style="display:none;"></textarea>
	</div>
</div>
</script>

<%--data export template --%>
<script id="dataExportTemplate" type="text/varsql-template">
<div id="data-export-modal" title="<spring:message code="data.export" />">
	<div class="export-dialog-area">
		<div id="data-export-column-list" class="export-column-area">

		</div>
		<div class="export-type-area">
			<div style="margin-bottom:5px;">
				<label class="control-label">Export Name</label>
				<input class="" id="exportFileName" name="exportFileName" value="">
			</div>
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
					<label class="checkbox-container">INSERT Query
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



