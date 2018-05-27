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