<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/include/tagLib.jspf"%>

<jsp:include page="./otherHead.jsp" flush="true"></jsp:include>
	<div class="col-xs-3">
		<jsp:include page="./otherLeft.jsp" flush="true"></jsp:include>
		<jsp:include page="../dbcomm/dbLeftTemplate.jsp" flush="true"></jsp:include>
	</div>
	
	<div class="col-xs-9 fill">
		<div class="row">
			<div>
				<button class="btn sql-execue-btn">실행</button>
				<button class="btn sql-format-btn">쿼리 정리</button>
				<button class="btn sql-save-btn">저장</button>
			</div>
		</div>
				
		<div id="sqlEditorWrap" class="row fill">
			<table id="editorAreaTable">
				<tr height="*">
					<td>
						<ul class="nav nav-tabs">
							<li><a href="#">Home</a></li>
							<li class="active"><a href="#">Profile</a></li>
							<li><a href="#">Messages</a></li>
						</ul>
							<input type="hidden" id="vconnid" name="vconnid" value="${param.vconnid}">
								LIMIT
								<select id="limitRowCnt" name="limitRowCnt">
									<option value="100" selected>100</option>
									<option value="500">500</option>
									<option value="1000">1000</option>
								</select>
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
			</table>
		</div>
	</div>
	
