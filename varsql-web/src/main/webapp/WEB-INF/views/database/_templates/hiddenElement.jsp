<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/include/tagLib.jspf"%>

<div id="varsqlExtensionsElementArea" style="position:absolute;top:0px;left:0px;width:0px;height:0px;"></div>

<div id="dbHiddenArea"></div>

<%--메모 보내기 다이얼로그 --%>
<div id="noteTemplate" style="display:none;overflow: hidden;" title="<spring:message code="msg.sendmsg.title" />">
	<div>
		<div class="col-xs-6">
			<div class="panel">
				<input type="text" id="recv_user_search" style="margin-bottom:5px;" name="recv_user_search" autocomplete="false" class="form-control" placeholder="<spring:message code="msg.sendmsg.recv.txt.holder" />">
				<div id="recvIdArr"></div>
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
					<textarea id="noteContent" name="noteContent" class="form-control" style="height: 245px;" placeholder="<spring:message code="content" />"></textarea>
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
	<div class="user-select-on" style="width:100%; padding:15px; height:100%; overflow:auto;">
		<div style="padding-top :10px;">
			<div style="font-weight: bold;font-size: 14pt;">DB info</div>
			<div style="padding-top:10px;" id="epHeaderDialogDbInfo"></div>
			<!-- div style="padding-top:10px;">email : ytechinfo@gamil.com</div -->
		</div>
	</div>
</div>

<%--editor 새 파일 다이얼로그. --%>
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

<div id="queryConvertDialog" class="query-convert-dialog" style="display:none;margin:0px;padding:0px;overflow: hidden;" title="<spring:message code="text.convert" text="텍스트 변환"/>">
	<div class="query-convert-header col-xs-12"> 
		<spring:message code="template" text="템플릿" />
		<select id="queryConvertType">
		</select>
		<spring:message code="split.char" text="분리 문자" />
		<select id="queryConvertSplitChar">
			<option value="newline" selected>New Line(\n)</option>
			<option value="tab">Tab(\t)</option>
			<option value="comma">Comma(,)</option>
			<option value="space">Space(\s)</option>
		</select>
		<button class="query_convert_text_btn"><spring:message code="convert" text="변환" /></button>
	</div>
	<div class="query-convert-body">
		<div class="col-xs-6 padding5 h100">
			<div>Data : </div>
			<textarea class="wh100" id="convertSqlText" style="height: calc(100% - 15px);"></textarea>
		</div>
		<div class="col-xs-6 padding5 h100" >
			<div class="h50">
				<div>Template: </div>
				<textarea  id="convertSqlTemplate" style="height: calc(100% - 15px);"></textarea>
			</div>
			<div class="h50">
				<div>Reuslt: </div>
				<textarea  id="convertSqlResult"  style="height: calc(100% - 15px);"></textarea>
			</div>
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
	<div style="padding-bottom: 6px;"><spring:message code="msg.download.history.help" text="다운로드 항목은 환경설정 -> 파일 에서 이력을 조회 할수 있습니다."/></div> 
	<div class="export-dialog-area" style="height:calc(100% - 25px);">
		<div id="data-export-column-list" class="export-column-area">

		</div>
		<div class="export-type-area">
			<table class="w100">
				<colgroup>
					<col style="width:85px">
					<col style="width:*;">
				</colgroup>
				<tr>
					<td><label class="control-label">Object Name</label></td>
					<td>
						<input type="text" id="exportObjectName" name="exportObjectName" value="">
						<input type="hidden" id="exportOrginObjectName" name="exportOrginObjectName" value="">
					</td>
				</tr>
				<tr>
					<td><label class="control-label">Charset</label></td>
					<td>
						<input class="" type="text" id="exportCharset" name="exportCharset" value="utf-8">
					</td>
				</tr>
				<tr>
					<td><label class="control-label">Limit Count</label></td>
					<td>
						<select id="exportCount"  name="exportCount" style="width: 100%;">
							<option value="1000" selected>1000</option>
							<c:forTokens var="item" items="5000,10000,100000,1000000" delims=",">
								<c:if test="${item < maxExportCount}">
		       					<option value="${item}">${item}</option>
								</c:if>
							</c:forTokens>
							<option value="${maxExportCount}">${maxExportCount}</option>
							<c:if test="${varsqlfn:isManager() or varsqlfn:isAdmin()}">
								<option value="-1">ALL</option>
							</c:if>
						</select>
					</td>
				</tr>
				<tr>
					<td><label class="control-label">Alias</label></td>
					<td>
						<select id="exportAliasType">
							<option value="default">기본</option>
							<option value="upper">대문자</option>
							<option value="lower">소문자</option>
							<option value="camel">Camel Case</option>
						</select>
					</td>
				</tr>
				<tr>
					<td class="vertical-top"><label class="control-label">Export Type</label></td>
					<td>
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
								  <input type="radio" name="exportType" value="sql">
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
								<label class="checkbox-container">Excel <span style="font-weight:bold;position: inherit;background: inherit;">(limit 1,048,576)</span>
								  <input type="radio" name="exportType" value="excel">
								  <span class="radiomark"></span>
								</label>
							</li>
						</ul>
					</td>
				</tr>
				<tr>
					<td colspan="2"><button class="btn btn-sm" id="exportAdvancedBtn">Advanced</button></td>
				</tr>
				<tr id="exportConditionQueryArea" class="display-off">
					<td class="vertical-top"><label class="control-label">Condition</label></td>
					<td>
						<div>ex: and column_name = 'test' <br/>
						and column_name1 = 'test2'</div>
						<textarea class="wh100" id="exportConditionQuery" name="conditionQuery" rows="5"></textarea>
					</td>
				</tr>
			</table>
		</div>
	</div>
</div>
</script>



