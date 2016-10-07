<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/include/tagLib.jspf"%>
<div class="col-xs-9 fill">
	<div class="row">
		<div>
			<button type="button" class="btn btn-default btn-sm sql-new-file" title="새 파일">
				<span class="glyphicon glyphicon-file"></span>
			</button>
			<button type="button" class="btn btn-default btn-sm sql-execue-btn" title="실행  Ctrl+Enter">
				<span class="glyphicon glyphicon-play"></span>
			</button>
			<button type="button" class="btn btn-default btn-sm sql-format-btn" title="쿼리 정리 Ctrl+Shift+f">
				<span class="glyphicon glyphicon-align-left" aria-hidden="true" ></span>
			</button>
			<button type="button" class="btn btn-default btn-sm sql-save-btn" title="저장">
				<span class="glyphicon glyphicon-save"></span>
			</button>
		</div>
	</div>

	<div id="sqlEditorWrap" class="row fill">
		<table id="editorAreaTable">
			<tr height="*">
				<td>
					<div>
						<input type="hidden" id="vconnid" name="vconnid" value="${param.vconnid}">
						<input type="hidden" id="sql_id" name="sql_id" value="">
						<input type="text" id="saveSqlTitle" name="saveSqlTitle" value="" placeholder="새파일명">
						LIMIT 
						<select id="limitRowCnt" name="limitRowCnt">
							<option value="100" selected>100</option>
							<option value="500">500</option>
							<option value="1000">1000</option>
						</select>
						<span class="glyphicon glyphicon-cog btn-sm" title="설정"></span>
						
						<span id="sqlEditerPreloaderArea"><img src="<c:url value="/webstatic/imgs/preloader.gif"/>"><span class="preloader-msg"></span></span>
					</div>
					<div>
						<textarea rows="10" style="display: none;" id="sqlExecuteArea"></textarea>
					</div>
				</td>
			</tr>
			<tr height="80%" class="tbl-valign-top">
				<td class="tbl-valign-top">
					<div id="data_grid_result_tab_wrap"></div>
					<div id="dataGridAreaWrap"></div>
				</td>
			</tr>
		</table>
	</div>
</div>
