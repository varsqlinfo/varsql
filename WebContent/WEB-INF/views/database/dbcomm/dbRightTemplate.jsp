<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/include/tagLib.jspf"%>
<div class="inner-layout-toolbar-area">
	<div>
		<div class="sql-btn-area">
			<a href="javascript:;" class="sql-edit-btn sql-btn-info  sql_execue_btn" title="실행  Ctrl+Enter">
				<span class="fa fa-play"></span>
			</a>
			
			<a href="javascript:;" class="sql-edit-btn sql_new_file" title="새 파일  Ctrl+Alt+N">
				<span class="fa fa-file-o"></span>
			</a>
			
			<a href="javascript:;" class="sql-edit-btn sql_save_btn" title="저장  Ctrl+Shift+S">
				<span class="fa fa-save"></span>
			</a>
			
			<a href="javascript:;" class="sql-edit-btn sql_undo_btn" title="실행취소  Ctrl+Z">
				<span class="fa fa-undo" ></span>
			</a>
			
			<a href="javascript:;" class="sql-edit-btn sql_redo_btn" title="다시실행  Ctrl+Y">
				<span class="fa fa-repeat" ></span>
			</a>
			
			<a href="javascript:;" class="sql-edit-btn sql_format_btn" title="쿼리 정리 Ctrl+Shift+F">
				<span class="fa fa-align-justify" aria-hidden="true" ></span>
			</a>
			
			<a href="javascript:;" class="sql-edit-btn sql_send_btn" title="보내기">
				<span class="fa fa-paper-plane-o"></span>
			</a>
			<a href="javascript:;" class="sql-edit-btn sql-btn-default sql_linewrapper_btn" title="자동 줄 바꿈 ">
				<span class="fa fa-level-down" aria-hidden="true" ></span>
			</a>
		</div>
		<div style="padding-bottom:3px;">
			<div style="width:200px;float:left;">
				<div class="input-group input-group-sm">
					<input type="hidden" id="sql_id" name="sql_id" value="">
			      	<input type="text" id="saveSqlTitle" name="saveSqlTitle" value="" class="form-control" placeholder="새파일명">
			      	<div class="input-group-btn"> 
			      		<button class="btn btn-default sql_save_list_btn" bgiframe="true" data-toggle="dropdown" data-target=".sql-save-list-layer" type="button">
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
			<div style="float:left;">
				<span style="padding:10px 0px 0px 5px;display:inline-block; vertical-align:bottom;">
					<input type="hidden" id="conuid" name="conuid" value="${param.conuid}">
					LIMIT 
		 
					<select id="limitRowCnt"  name="limitRowCnt" class="selectpicker">
						<option value="100" selected>100</option>
						<option value="500">500</option>
						<option value="1000">1000</option>
					</select>
				</span>
			</div>
		 	<span id="sqlEditerPreloaderArea"><img src="<c:url value="/webstatic/imgs/preloader.gif"/>"><span class="preloader-msg"></span></span>
		 	<div class="pull-right">
			 	<div style="width:220px;display:inline-block;">
					<div class="input-group input-group-sm">
				      	<input type="text" id="sqlFindText" name="sqlFindText" value="" class="form-control" placeholder="검색어">
				      	<div class="input-group-btn"> 
				      		<button class="btn btn-default sql_find_btn">find</button>
						</div>
						
						 <span class="input-group-addon" style="background:#f7f3f300;background-color:#f7f3f300;border:0px;">
						 	<span id="sql_parameter_toggle_btn" class="sql-edit-btn" style="cursor:pointer;padding: initial;font-size: inherit;">
						 		<span class="fa fa-plus-square-o"></span>변수
						 	</span>
						 </span>
				    </div>
			    </div>
			</div>
		</div>
	</div>
</div>
<div id="editorAreaTable" class="inner-layout-sql-editor-area">
	<div id="sqlEditorWrap">
		<div id="sql_editor_area" class="col-xs-12" style="padding: 0px;">
			<textarea rows="10" style="display: none;" id="sqlExecuteArea"></textarea>
		</div>
		<div id="sql_parameter_area" class="sql-parameter-area">
			<table style="width:100%;">
				<colgroup>
					<col width="90px">
					<col width="130px">
					<col width="30px">
			    </colgroup>
				<thead>
					<tr>
						<th>Key</th>
						<th>Value</th>
						<th></th>
					</tr>
				</thead>
				<tbody id="sql_parameter_row_area">
				</tbody>
				<tfoot>
					<tr>
						<td colspan="3" class="text-center">
							<div class="margin-top5">
								<button type="button" class="sql-param-add-btn btn btn-sm btn-default fa fa-plus"></button>
							</div>
						</td>
					</tr>
				</tfoot>
			</table>
		</div>
	</div>
</div>

<div class="inner-layout-result-area">
	<div style="height:100%;width:100%;">
		<div id="data_grid_result_tab_wrap" style="width:100%;height:26px;z-index:1;padding-top:3px;position:absolute;"></div>
		<div id="dataGridAreaWrap" style="width:100%;height:100%;padding-top:25px;"></div>
	</div>
</div>
