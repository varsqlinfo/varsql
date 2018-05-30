<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<div id="dbHiddenArea"></div>

<%--메모 보내기 다이얼로그 --%>
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

<%--설정 --%>
<div id="preferencesTemplate" style="display:none;margin:0px;padding:0px;" title="환경설정">
	<iframe src="" class="preferences-frame"></iframe>
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

<%--editor 문자 찾기 다이얼로그. --%>
<div id="editorFindTextDialog" style="display:none;margin:0px;padding:0px;overflow: hidden;" title="찾기">
	<div class="find-text-area">
		<div>
			<div>
				<label class="find-text-label">Find</label>
				<span class="find-text-input-area"><input type="text" id="editorFindText" name="editorFindText"></span>
			</div>
			</div>
				<label class="find-text-label">Replace with</label>
				<span class="find-text-input-area"><input type="text" id="editorReplaceText" name="editorReplaceText"></span>
			<div>
		</div>
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

<%--ddl view template --%>
<script id="ddlViewTemplate" type="text/varsql-template">
<div class="ddl-view-area">
	<div class="pull-right" style="padding:4px;position:relative;z-index:1;">
		<!-- button type="button" class="btn btn-sm btn-default ddl-copy" data-ddl-copy-mode="copy">띄워보기</button -->
		<button type="button" class="btn btn-sm btn-default ddl-copy" data-ddl-copy-mode="paste">editor 붙여넣기</button>
	</div>
	<div  class="absolute-wh100per" style="padding-top:30px;z-index:0;">
		<pre class="user-select-on prettyprint lang-sql width-height100"></pre>
		<textarea style="display:none;"></textarea>
	</div>
</div>
</script>

<style>
.export-dialog-area{
	width:100%;
	height:100%;
}
.export-dialog-area>div{
	float:left; 
}

.export-dialog-area .export-column-area{
	width:60%;
	height: 100%;
    overflow: auto;
	border: 1px solid #d0d0d8;
}

.export-dialog-area .export-type-area{
	width: calc(40% - 15px);
    height: 100%;
    overflow: auto;
    padding: 5px 10px;
    margin-left: 10px;
    border: 1px solid #c5caca;
}

.export-dialog-area .export-type{
	margin: 10px 3px;
	padding:0px;
	list-style: none;
}

.varsql-table {
	width:100%;
	height:100%;
	border-collapse: collapse;
	border: 1px solid #d0d0d8;
}
.varsql-table.border-zero{
	border: 0px solid #d0d0d8;
}
.varsql-table th{
	border: 1px solid #d0d0d8;
	border-top: 0px solid #d0d0d8;
	margin: 0;
    padding: 0;
    color: #000;
	height:25px;
    background-color: #f1f1f1;
}

.varsql-table td{
	border: 1px solid #d0d0d8;
	border-top: 0px solid #d0d0d8;
	margin: 0;
    padding: 2px 3px;
    color: #000;
	height:25px;
}

.varsql-table tr:even(odd){
    background: #f3f6fa;
}
.varsql-table tr:hover{
	background-color: #f1f1f1;
}
</style>

<%--data export template --%>
<script id="dataExportTemplate" type="text/varsql-template">
<div id="data-export-modal" title="데이타 내보내기">
	<div class="export-dialog-area">
		<div class="export-column-area">
			<table class="varsql-table border-zero">
				<thead>
					<tr role="row">
						<th tabindex="0" rowspan="1" colspan="1" style="width: 50px;"><input
							type="checkbox" name="exportColumnCheckBox" value="all">all</th>
						<th tabindex="0" rowspan="1" colspan="1" style="width: 100px;">Column</th>
						<th tabindex="0" rowspan="1" colspan="1" style="width: 100px;">Desc</th>
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



