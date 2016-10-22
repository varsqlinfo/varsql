<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/include/tagLib.jspf"%>
<div class="inner-layout-toolbar-area">
	<div>
		<div>
			<button type="button" class="btn btn-default btn-sm sql-new-file" title="새 파일  Ctrl+alt+n">
				<span class="glyphicon glyphicon-file"></span>
			</button>
			<button type="button" class="btn btn-default btn-sm sql-save-btn" title="저장  Ctrl+shift+s">
				<span class="glyphicon glyphicon-save"></span>
			</button>
			<button type="button" class="btn btn-default btn-sm sql-execue-btn" title="실행  Ctrl+Enter">
				<span class="glyphicon glyphicon-play"></span>
			</button>
			<button type="button" class="btn btn-default btn-sm sql-format-btn" title="쿼리 정리 Ctrl+Shift+f">
				<span class="glyphicon glyphicon-align-left" aria-hidden="true" ></span>
			</button>
			<button type="button" class="btn btn-default btn-sm sql-send-btn" title="보내기">
				<span class="glyphicon glyphicon-send"></span>
			</button>
		</div>
		<div style="padding-bottom:3px;">
			<div style="width:200px;float:left;">
				<div class="input-group input-group-sm">
					<input type="hidden" id="sql_id" name="sql_id" value="">
			      	<input type="text" id="saveSqlTitle" name="saveSqlTitle" value="" class="form-control" placeholder="새파일명">
			      	<div class="input-group-btn"> 
			      		<button class="btn btn-default sql-save-list-btn" bgiframe="true" data-toggle="dropdown" data-target=".sql-save-list-layer" type="button">
				      		List
				      	</button>
					    <div class="dropdown-menu sql-save-list-layer" role="menu" style="width:250px;">
		                    <div class="panel-success">
		                        <div class="panel-heading">
		                            <input type="text" name="saveSqlSearch" id="saveSqlSearch"/>
		                        </div>
		                        <div class="save-sql-list-wrapper">
		                            <ul id="saveSqlList" class="list-unstyled save-sql-list">
		                            </ul>
		                        </div>
		                        <div class="panel-footer">
		                        	<div>
			                            <input type="number" id="sql-save-list-no" name="sql-save-list-no" min="1" max="10000" size="2" value="1">/<span id="sql-save-list-pagecnt"></span>(<span id="sql-save-list-totalcnt"></span>)
			                            <span style="padding-left:10px;">
				                            <a href="javascript:;" class="sql-list-move-btn" _mode="p"><span class="glyphicon glyphicon-chevron-left" aria-hidden="true"></span></a>
				                            <a href="javascript:;" class="sql-list-move-btn" _mode="n"><span class="glyphicon glyphicon-chevron-right"></span></a>
			                            </span>
		                            </div>
		                        </div>
		                    </div>
					    </div> 
					</div>
			    </div>		
			</div>
			<div style="float:left;padding-left:10px;">
				<input type="hidden" id="vconnid" name="vconnid" value="${param.vconnid}">
				LIMIT 
	 
				<select id="limitRowCnt"  name="limitRowCnt" class="selectpicker">
					<option value="100" selected>100</option>
					<option value="500">500</option>
					<option value="1000">1000</option>
				</select>
				<span class="glyphicon glyphicon-cog btn-sm" title="설정"></span>
			</div>
		 	<span id="sqlEditerPreloaderArea"><img src="<c:url value="/webstatic/imgs/preloader.gif"/>"><span class="preloader-msg"></span></span>
		</div>
	</div>
</div>
<div id="editorAreaTable" class="inner-layout-sql-editor-area">
	<div id="sqlEditorWrap">
		<div class="col-md-12" style="padding: 0px;">
			<textarea rows="10" style="display: none;" id="sqlExecuteArea"></textarea>
		</div>
	</div>
</div>

<div class="inner-layout-result-area">
	<div style="height:100%; display:table;width:100%">
		<div id="data_grid_result_tab_wrap" style="display:table-row;width:100%;height:1px"></div>
		<div id="dataGridAreaWrap" style="display:table-row;width:100%;height:100%"></div>
	</div>
</div>
