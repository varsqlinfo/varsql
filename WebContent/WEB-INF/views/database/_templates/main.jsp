<!DOCTYPE html>
<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/include/tagLib.jspf"%>
<html>
<head>
<title>${left_db_object.connInfo.name}::<spring:message code="screen.user" /></title>
<%@ include file="/WEB-INF/include/database-head.jsp"%>

</head>
<body class="database-main">
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
		<div class="main-top-menu-wrapper">
			<div class="col-lg-12">
				<tiles:insertAttribute name="header" />
			</div>
		</div>
		<div id="varsqlBodyWrapper" class="main-body-wrapper"></div>
      </c:otherwise>
</c:choose>

	
<%@ include file="/WEB-INF/views/database/_templates/hiddenElement.jsp"%>
</body>

<script>

$(document).ready(function(){
	var viewConnInfo = ${varsqlfn:objectToJson(left_db_object)};
	var opts = $.extend({param:{conuid:viewConnInfo.conuid},selector:'#dbSchemaList',dbtype:viewConnInfo.type}, viewConnInfo);
	opts.screenSetting = ${database_screen_setting};
	VARSQL.ui.create(opts);
	
	//varsqlMain.init();
}); 

</script>

</html>
	

<%--db object component template --%>
<script id="dbObjectComponentTemplate" type="text/varsql-template">
<div id="dbSchemaObjectArea" class="pos-relative-w-h100">
	<div class="varsql-dbodbject-schema-area">
		<div class="panel-heading">
			<img src="/vsql/webstatic/imgs/Database.gif"/>
			<span id="varsql_schema_name">schema명</span>
			<div class="btn-group pull-right">
				<button type="button" class="btn btn-default btn-xs dropdown-toggle db-schema-list-btn" data-toggle="dropdown" aria-expanded="false">
					<i class="fa fa-chevron-down"></i>
				</button>
				<ul id="dbSchemaList" class="dropdown-menu slidedown">
				</ul>
			</div>
		</div>
	</div>
	<!-- object tab area -->
	<div id="varsqlDbServiceMenu" class="varsql-dbodbject-tab-area"></div>
	<!-- object cont area -->
	<div id="dbServiceMenuContent" class="varsql-dbodbject-cont-area"></div>			
</div>
</script>

<%--meta data 영역  component template --%>
<script id="dbMetadataComponentTemplate" type="text/varsql-template">
<div class="pos-relative-w-h100">
	<div id="metadataTabAreaWrap" class="varsql-pubtab-area"></div>
	
	<div id="metadataContentAreaWrap" class="varsql-pubtab-cont-area"></div>			
</div>
</script>

<%--sql editor component template --%>
<script id="sqlEditorComponentTemplate" type="text/varsql-template">
<div id="sqlEditorComponent" class="pos-relative-w-h100">
	<div id="sqlEditorToolbar" class="varsql-toolbar-area">
		<div class="sql-btn-area">
			<ul>
				<li>
					<a href="javascript:;" class="sql-edit-btn sql-btn-info  sql_execue_btn" title="실행  Ctrl+Enter">
						<span class="fa fa-play"></span>
					</a>
				</li>
				<li class="sql-btn-divider"></li>
				<li>
					<a href="javascript:;" class="sql-edit-btn sql_new_file" title="새 파일  Ctrl+Alt+N">
						<span class="fa fa-file-o"></span>
					</a>
				</li>
				<li>
					<a href="javascript:;" class="sql-edit-btn sql_save_btn" title="저장  Ctrl+Shift+S">
						<span class="fa fa-save"></span>
					</a>
				</li>
				<li class="sql-btn-divider"></li>
				<li>
					<a href="javascript:;" class="sql-edit-btn sql_undo_btn" title="실행취소  Ctrl+Z">
						<span class="fa fa-undo" ></span>
					</a>
				</li>
				<li>
					<a href="javascript:;" class="sql-edit-btn sql_redo_btn" title="다시실행  Ctrl+Y">
						<span class="fa fa-repeat" ></span>
					</a>
				</li>
				<li>
					<a href="javascript:;" class="sql-edit-btn sql-btn-default sql_linewrapper_btn" title="자동 줄 바꿈 ">
						<span class="fa fa-dedent" aria-hidden="true" ></span>
					</a>
				</li>
				<li class="sql-btn-divider"></li>
				<li>
					<a href="javascript:;" class="sql-edit-btn sql_format_btn" title="쿼리 정리 Ctrl+Shift+F">
						<span class="fa fa-align-justify" aria-hidden="true" ></span>
					</a>
				</li>
				<li>
					<a href="javascript:;" class="sql-edit-btn sql_send_btn" title="보내기">
						<span class="fa fa-paper-plane-o"></span>
					</a>
				</li>
			</ul>
		</div>
		<div style="padding-bottom:3px;">
			<div style="width:210px;float:left;padding: 3px 5px 0px;">
				<div class="input-group input-group-sm">
					<input type="hidden" id="sql_id" name="sql_id" value="">
			      	<input type="text" id="saveSqlTitle" name="saveSqlTitle" value="" placeholder="새파일명" style="padding:2px;width:155px;height:23px;">
			      	<span> 
			      		<button class="btn btn-default sql_save_list_btn" bgiframe="true" data-toggle="dropdown" data-target=".sql-save-list-layer" type="button" style="line-height:15px;">
				      		List
				      	</button>
					    <div class="dropdown-menu sql-save-list-layer" role="menu" style="width:250px;">
		                    <div class="panel-success">
		                        <div class="panel-heading">
		                            <input type="text" name="saveSqlSearch" id="saveSqlSearch" style="width:100%;"/>
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
					</span>
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
				<span id="sqlEditerPreloaderArea"><img src="<c:url value="/webstatic/imgs/preloader.gif"/>"><span class="preloader-msg"></span></span>
			</div>
		 	
		 	<div class="pull-right">
			 	<div style="width:50px;display:inline-block;">
					<span style="background:#f7f3f300;background-color:#f7f3f300;border:0px;">
						<span id="sql_parameter_toggle_btn" class="sql-edit-btn" style="cursor:pointer;padding: initial;font-size: inherit;">
							<span class="fa fa-plus-square-o"></span>변수
						</span>
					</span>
			    </div>
			</div>
		</div>
	</div>

	<div id="sql_editor_wrapper" class="varsql-sqleditor-area">
		<div id="sql_editor_area" style="position:relative;height:100%;">
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
</script>

<%--query result component template --%>
<script id="sqlDataComponentTemplate" type="text/varsql-template">
<div id="sqlDataComponent" class="pos-relative-w-h100">
	<div id="data_grid_result_tab_wrap" class="varsql-tab-area">
		<ul id="data_grid_result_tab" class="sql-result-tab">
			<li tab_gubun="result" class="on"><a href="javascript:;">결과</a></li>
			<li tab_gubun="columnType"><a href="javascript:;">컬럼타입</a></li>
			<li tab_gubun="msg"><a href="javascript:;"><span>메시지</span><span class="fa fa-file-o log_clear_btn" style="padding-left:5px;"></span></a></li>
		</ul>
	</div>
	<div id="dataGridAreaWrap" class="varsql-tab-cont-area">
		<div id="dataGridArea" class="sql-result-area on" tab_gubun="result"></div>
		<div id="dataColumnTypeArea" class="sql-result-area on" tab_gubun="columnType"></div>
		<div id="resultMsgAreaWrap"  class="sql-result-area user-select-on" tab_gubun="msg"></div>		
	</div>
</div>
</script>

<%--glossary component template --%>
<script id="glossaryComponentTemplate" type="text/varsql-template">
	<div id="glossaryComponentArea" class="pos-relative-w-h100">
		<div class="glossary-search-area">
			<div>
				검색어<input type="text" id="glossarySearchTxt" value="">
				<button type="button" class="btn btn-default glossary-search-btn" title="조회"><span class="fa fa-retweet"></span></button>
				</div>
			<div>
				<input type="text" id="glossaryConvertTxt" value="">
				<button type="button" class="btn btn-default" title="변환"><span class="fa fa-retweet"></span></button>
				<button type="button" class="btn btn-default" title="지우기"><span class="fa fa-trash-o"></span></button>
			</div>
		</div>

		<div id="glossaryResultArea" class="glossary-result-area"></div>
	</div>
</script>
<%-- history component template --%>
<script id="historyComponentTemplate" type="text/varsql-template">
	<div class="pos-relative-w-h100">
		history
	</div>
</script>



