<!DOCTYPE html>
<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/include/tagLib.jspf"%>
<html>
<head>
<title>${vname}::<spring:message code="screen.user" /></title>
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

<style>

.varql-sqlfile-tab{
	display:block;
	position: relative;
    height: 20px;
	float:left;
	margin-left: 5px;
	width:calc(100% - 200px);
}

.varsql-sqleditor-area .varsql-file-save-list{
	display:none;
	width:200px;
	position:relative;
	height:100%;
	padding: 5px;
	float:left;
	overflow: auto;
	border-right: 1px solid #cccccc;
}
.varsql-sqleditor-area .varsql-file-save-list ul{
	margin:0px;
	padding:0px;	
}
.varsql-sqleditor-area.sql-flielist-active .varsql-file-save-list{
	display:block;
}

.varsql-sqleditor-area.sql-flielist-active #sql_editor_area{
	margin-left:200px
}

.varsql-sqleditor-area .sql-editor-text{
	display:none;
}

.varsql-sqleditor-area .sql-editor-item{
	z-index:0;
	position: absolute;
    width: 100%;
    height: 100%;
    top: 0px;
    left: 0px;
}

.varsql-sqleditor-area .sql-editor-item.active{
	z-index:1;
}

.varsql-toolbar-area .pubTab-wrapper {
	background: inherit;
}

.varsql-toolbar-area .pubTab {
	border-bottom: 0px solid #bfb8b8
}
    
.varsql-toolbar-area .pubTab-item.active .pubTab-item-cont{
	background:#ffffff;
}

.text-ellipsis.sql-flielist-item{
	max-width: calc(100% - 50px)
} 

</style>
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
		<div class="varql-sqlfile-list-wrapper" style="padding-top:3px;">
			<span style="float:left;">
				<span style="padding:0px 5px 0px 0px;display:inline-block; vertical-align:bottom;">
					<input type="hidden" id="conuid" name="conuid" value="${param.conuid}">
					LIMIT 
					<select id="limitRowCnt"  name="limitRowCnt" class="selectpicker">
						<option value="100" selected>100</option>
						<option value="500">500</option>
						<option value="1000">1000</option>
					</select>
				</span>
				<button type="button" id="sql_filelist_view_btn" class="btn btn-default" style="line-height:15px;">SQL</button>
			</span>
			<div id="varsqlSqlFileTab" class="varql-sqlfile-tab">
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
		<div class="varsql-file-save-list">
			<div style="padding:5px;">
				<input type="hidden" id="sqlFileId">
				<input type="text" id="sqlFileSearchTxt" class="form-control input-sm">
			</div>
			<ul id="sql_filelist_area"></ul>
		</div>
				
		<div id="sql_editor_area" style="position:relative;height:100%;">
			<div class="sql-editor-item" data-editor-id="empty"><textarea id="sqlEmptyEditor" name="sqlEmptyEditor" class="sql-editor-text"></textarea></div>
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
	<div id="glossaryPluginArea" class="pos-relative-w-h100">
		<div class="glossary-search-area-wrapper">
			<div class="glossary-search-area">
				<span>검색</span>
				<input type="text" id="glossarySearchTxt" class="input-text" placeholder="Search...">
				<button type="button" class="btn btn-default glossary-search-btn" title="조회"><span class="fa fa-search"></span></button>
			</div>
			<div class="glossary-convert-area">
				<button type="button" class="btn btn-default glossary-convert-camelcase" title="변환"><span class="fa fa-retweet"></span></button>
				<button type="button" class="btn btn-default glossary-convert-clear" title="지우기"><span class="fa fa-trash-o"></span></button>
				<input type="text" id="glossaryConvertTxt" class="input-text">
			</div>
		</div>

		<div class="glossary-result-area">
			<div id="glossaryResultArea" class="pos-relative-w-h100"></div>
		</div>
	</div>
</script>
<%-- history component template --%>
<script id="historyPluginAreaTemplate" type="text/varsql-template">
	<div id="historyPluginArea" class="pos-relative-w-h100">
		<div class="history-search-area-wrapper">
			<div class="history-search-area">
				<span>검색</span>
				<input type="text" id="historySearchTxt" class="input-text" placeholder="Search...">
				<button type="button" class="btn btn-default history-search-btn" title="조회"><span class="fa fa-search"></span></button>
			</div>
		</div>
		<div class="history-result-area">
			<div id="historyResultArea" class="pos-relative-w-h100"></div>
		</div>
	</div>
</script>



