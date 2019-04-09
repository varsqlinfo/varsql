<!DOCTYPE html>
<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/include/tagLib.jspf"%>
<html>
<head>
<title>${vname}::<spring:message code="screen.user" /></title>
<%@ include file="/WEB-INF/include/database-head.jsp"%>

</head>
<body class="varsql-main">
<div class="varsql-menu-wrapper">
	<tiles:insertAttribute name="header" />
</div>
<div id="varsqlBodyWrapper" class="varsql-body-wrapper"></div>

	
<%@ include file="/WEB-INF/views/database/_templates/hiddenElement.jsp"%>
</body>

<c:set var="screenConfigInfo" value="${screen_config_info}"/>
<script>

$(document).ready(function(){
	var viewConnInfo = ${varsqlfn:objectToJson(screenConfigInfo)};
	var opts = VARSQL.util.objectMerge({param:{conuid:viewConnInfo.conuid, schema:viewConnInfo.schema},selector:'#dbSchemaList',dbtype:viewConnInfo.type}, viewConnInfo);
	opts.screenSetting = ${database_screen_setting};
	VARSQL.ui.create(opts);
	
	//varsqlMain.init();
}); 

</script>

</html>
<%--db object component template --%>
<script id="dbObjectComponentTemplate" type="text/varsql-template">
<div id="pluginSchemaObject" class="varsql-plugin-wrapper">
	<div class="db-schema">
		<img src="/vsql/webstatic/imgs/Database.gif"/>
		<input type="text" id="varsqlSschemaName" value="${screenConfigInfo.schema}" class="schema-name-text" disabled="">
		
		<div class="schema-view-btn pull-right varsql-widget-layer">
			<c:if test="${fn:length(screenConfigInfo.schemaList) > 1}">
				<button type="button" class="varsql-btn-default db-schema-list-btn">
					<i class="fa fa-chevron-down"></i>
				</button>
				<ul id="dbSchemaList" class="dropdown-scheam-list">
					<c:forEach var="item" items="${screenConfigInfo.schemaList}" varStatus="status">
						<li><a href="javascript:;" class="db-schema-item ${screenConfigInfo.schema == item ? 'active' :''}" obj_nm="${item}">${item}</a></li>
					</c:forEach>
				</ul>
			</c:if>
		</div>
	</div>
	<!-- object tab area -->
	<div id="pluginSchemaObjectTab" class="db-object-tab"></div>
	<!-- object cont area -->
	<div id="pluginSchemaObjectTabContent" class="db-object-tab-content"></div>			
</div>
</script>

<%--meta data 영역  component template --%>
<script id="dbMetadataComponentTemplate" type="text/varsql-template">
<div id="pluginObjectMeta" class="varsql-plugin-wrapper">
	<div id="pluginObjectMetaContent" class="wh100"></div>	
</div>
</script>

<%--sql editor component template --%>
<script id="sqlEditorComponentTemplate" type="text/varsql-template">
<div id="pluginSqlEditor" class="varsql-plugin-wrapper">
	<div class="sql-editor-toolbar">
		<ul>
			<li>
				<button type="button" class="sql-edit-btn varsql-btn-trans varsql-btn-info sql_toolbar_execute_btn" data-sql-editor-menu="y" title="<spring:message code="btn.toolbar.execute" /> Ctrl+Enter">
					<i class="fa fa-play"></i>
				</button>
			</li>
			<li class="sql-btn-divider"></li>
			<li>
				<button type="button" class="sql-edit-btn varsql-btn-trans sql_toolbar_new_file" title="<spring:message code="btn.toolbar.newfile" /> Ctrl+Alt+N">
					<i class="fa fa-file-o"></i>
				</button>
			</li>
			<li>
				<button type="button" class="sql-edit-btn varsql-btn-trans sql_toolbar_save_btn" data-sql-editor-menu="y" title="<spring:message code="btn.toolbar.save" /> Ctrl+S">
					<i class="fa fa-save"></i>
				</button>
			</li>
			<li>
				<button type="button" class="sql-edit-btn varsql-btn-trans sql_toolbar_allsave_btn" data-sql-editor-menu="y" title="<spring:message code="btn.toolbar.allsave" /> Ctrl+Shift+S">
					<i class="fa fa-save-all"></i>
				</button>
			</li>
			<li class="sql-btn-divider"></li>
			<li>	
				<button type="button" class="sql-edit-btn varsql-btn-trans sql_toolbar_cut_btn" data-sql-editor-menu="y" title="<spring:message code="btn.toolbar.cut" /> Ctrl+X">
					<i class="fa fa-scissors"></i>
				</button>
			</li>
			<li>	
				<button type="button" class="sql-edit-btn varsql-btn-trans sql_toolbar_copy_btn" data-sql-editor-menu="y" title="<spring:message code="btn.toolbar.copy" /> Ctrl+C">
					<i class="fa fa-copy"></i>
				</button>
			</li>	
			<li>	
				<button type="button" class="sql-edit-btn varsql-btn-trans sql_toolbar_delete_btn" data-sql-editor-menu="y" title="<spring:message code="btn.toolbar.eraser" />">
					<i class="fa fa-eraser"></i>
				</button>
			</li>	
			<li class="sql-btn-divider"></li>
			<li>
				<button type="button" class="sql-edit-btn varsql-btn-trans sql_toolbar_undo_btn" data-sql-editor-menu="y" title="<spring:message code="btn.toolbar.undo" /> Ctrl+Z">
					<i class="fa fa-undo" ></i>
				</button>
			</li>
			<li>
				<button type="button" class="sql-edit-btn varsql-btn-trans sql_toolbar_redo_btn" data-sql-editor-menu="y" title="<spring:message code="btn.toolbar.redo" /> Ctrl+Y">
					<i class="fa fa-repeat" ></i>
				</button>
			</li>
			<li>
				<button type="button" class="sql-edit-btn varsql-btn-trans sql-btn-default sql_toolbar_linewrapper_btn" data-sql-editor-menu="y" title="<spring:message code="btn.toolbar.linewrapper" />">
					<i class="fa fa-dedent" aria-hidden="true" ></i>
				</button>
			</li>
			<li class="sql-btn-divider"></li>
			<li>
				<button type="button" class="sql-edit-btn varsql-btn-trans sql_toolbar_format_btn" data-sql-editor-menu="y" title="<spring:message code="btn.toolbar.format" /> Ctrl+Shift+F">
					<i class="fa fa-align-justify" aria-hidden="true" ></i>
				</button>
			</li>
			<li>
				<button type="button" class="sql-edit-btn varsql-btn-trans sql_toolbar_send_btn" title="<spring:message code="btn.toolbar.send" />">
					<i class="fa fa-paper-plane-o"></i>
				</button>
			</li>
		</ul>
	</div>
	
	<div class="sql-editor-file">
		<div class="left-cont">
			<span class="sql-limit-count">
				<input type="hidden" id="conuid" name="conuid" value="${param.conuid}">
				LIMIT 
				<select id="limitRowCnt"  name="limitRowCnt">
					<option value="100" selected>100</option>
					<option value="500">500</option>
					<option value="1000">1000</option>
					<option value="5000">5000</option>
					<option value="10000">10000</option>
				</select>
			</span>
			<button type="button" id="sql_filelist_view_btn" class="varsql-btn-default sql-filelist-view-btn"><i class="fa fa-bars" style="margin-right:3px;"></i>파일</button></button>
		</div>

		<div id="varsqlSqlFileTab" class="sqlfile-tab"></div>

	 	<div class="float-right">
			<button id="sql_parameter_toggle_btn" data-sql-editor-menu="y" class="sql-edit-btn sql-parameter-btn disable">
				<span class="fa fa-plus-square-o"></span><spring:message code="btn.toolbar.parameter"/>
			</button>
		</div>
	</div>

	<div id="sql_editor_wrapper" class="sql-editor">
		<div class="varsql-file-save-list">
			<div style="padding:5px;">
				<input type="hidden" id="sqlFileId">
				<input type="text" id="sqlFileSearchTxt">
			</div>
			<div class="scroll-area">
				<ul id="sql_filelist_area"></ul>
			</div>
		</div>
		<div id="sql_editor_area" class="sql-editor-tab-content">
			<div class="sql-editor-item" data-editor-id="empty">
	 			<p class="msg-text">
					<a href="javascript:;" class="sql_new_file">
						<button type="button" class=""><span class="fa fa-file-o"></span></button><spring:message code="msg.editor.newfile" /> 
					</a>
					<br><spring:message code="msg.editor.info" />
				</p>
			</div>
		</div>
		<div id="sql_parameter_wrapper" class="sql-parameter-wrapper">
			<div class="sql-param-header">
				<span class="key">Key</span>
				<span class="val">Value</span>
				<span class="remove"><button type="button" class="sql-param-add-btn"><i class="fa fa-plus"></i></button></span>
		    </div>
			<div id="sql_parameter_area" class="sql-param-body">

			</div>
		</div>
	</div>
</div>
</script>

<%--query result component template --%>
<script id="sqlDataComponentTemplate" type="text/varsql-template">
<div id="pluginSqlResult" class="varsql-plugin-wrapper">
	<div id="data_grid_result_tab_wrap" class="sql-result-tab-wrapper">
		<ul id="data_grid_result_tab" class="sql-result-tab">
			<li tab_gubun="result" class="on"><a href="javascript:;"><spring:message code="btn.resultarea.tab.grid"/></a></li>
			<li tab_gubun="columnType"><a href="javascript:;"><spring:message code="btn.resultarea.tab.column"/></a></li>
			<li tab_gubun="msg"><a href="javascript:;"><span><spring:message code="btn.resultarea.tab.log"/></span><span class="fa fa-file-o log_clear_btn" style="padding-left:5px;"></span></a></li>
		</ul>
	</div>
	
	<div id="dataGridAreaWrap" class="sql-result-tab-content">
		<div id="dataGridArea" class="varsql-tab-content tab-on" tab_gubun="result">
			<div class="sql-editor-result active" data-sql-result-id="empty">
				<div class="sql-editor-result-grid on" data-grid-type="result"></div>
				<div class="sql-editor-result-grid" data-grid-type="columnType"></div>
			</div>
		</div>
		<div id="resultMsgAreaWrap"  class="varsql-tab-content user-select-on varsql-log-area" tab_gubun="msg"></div>		
	</div>
</div>
</script>

<%--glossary component template --%>
<script id="glossaryComponentTemplate" type="text/varsql-template">
	<div id="pluginGlossary" class="varsql-plugin-wrapper">
		<div class="glossary-search-area-wrapper">
			<div class="glossary-search-area">
				<input type="text" id="glossarySearchTxt" class="input-text" placeholder="Search...">
				<button type="button" class="varsql-btn-default glossary-search-btn" title="<spring:message code="btn.search"/>"><span class="fa fa-search"></span></button>
			</div>
			<div class="glossary-convert-area">
				<button type="button" class="varsql-btn-default glossary-convert-camelcase" title="<spring:message code="btn.glossary.convert"/>"><span class="fa fa-retweet"></span></button>
				<button type="button" class="varsql-btn-default glossary-convert-clear" title="<spring:message code="btn.glossary.remove"/>"><span class="fa fa-trash-o"></span></button>
				<input type="text" id="glossaryConvertTxt" class="input-text">
			</div>
		</div>

		<div class="glossary-result-area">
			<div id="glossaryResultArea" class="wh100-relative"></div>
		</div>
	</div>
</script>
<%-- history component template --%>
<script id="historyPluginAreaTemplate" type="text/varsql-template">
	<div id="pluginHistory" class="varsql-plugin-wrapper">
		<div class="history-search-area-wrapper">
			<div class="history-search-area">
				<input type="text" id="historySearchTxt" class="input-text" placeholder="Search...">
				<button type="button" class="varsql-btn-default history-search-btn" title="<spring:message code="btn.search"/>"><span class="fa fa-search"></span></button>
			</div>
		</div>
		<div class="history-result-area">
			<div id="historyResultArea" class="wh100-relative"></div>
		</div>
	</div>
</script>



