<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/include/tagLib.jspf"%>
<ul class="varsql-menu">
	<li class="top-menu-button">
		<a href="javascript:;" class="varsql-top-menu-label" data-toggle="header-dropdown"><spring:message code="header.menu.file" /></a>
		<ul class="varsql-top-menu">
			<li class="varsql-menu-item" data-menu-item="file_new">
				<a href="javascript:;">
					<span class="varsql-menu-item-text"><spring:message code="header.menu.file.new" /></span>
					<span class="varsql-menu-item-key">Ctrl+Shift+N</span>
				</a>
			</li>

			<li class="varsql-menu-item" data-menu-item="file_save" data-sql-editor-menu="y">
				<a href="javascript:;">
					<span class="varsql-menu-item-text"><spring:message code="header.menu.file.save" /></span>
					<span class="varsql-menu-item-key">Ctrl+S</span>
				</a>
			</li>
			<li class="varsql-menu-item" data-menu-item="file_allsave" data-sql-editor-menu="y">
				<a href="javascript:;">
					<span class="varsql-menu-item-text"><spring:message code="header.menu.file.allsave" /></span>
					<span class="varsql-menu-item-key">Ctrl+Shift+S</span>
				</a>
			</li>
			<li class="divider"></li>
			<li class="varsql-menu-item" data-menu-item="file_import-export">
				<a href="javascript:;">
					<span class="varsql-menu-item-text"><spring:message code="header.menu.file.import_export" /></span>
				</a>
			</li>
			<li class="divider"></li>
			<li class="varsql-menu-item" data-menu-item="file_newwin">
				<a href="javascript:;">
					<span class="varsql-menu-item-text"><spring:message code="header.menu.file.newwin" /></span>
				</a>
			</li>
			<li class="divider"></li>
			<li class="varsql-menu-item" data-menu-item="file_close">
				<a href="javascript:;">
					<span class="varsql-menu-item-text"><spring:message code="header.menu.file.close" /></span>
				</a>
			</li>
		</ul>
	</li>
	<li class="top-menu-button">
		<a href="javascript:;" class="varsql-top-menu-label" data-toggle="header-dropdown"><spring:message code="header.menu.edit" /></a>
		<ul class="varsql-top-menu">
			<li class="varsql-menu-item" data-menu-item="edit_undo" data-sql-editor-menu="y">
				<a href="javascript:;">
					<span class="varsql-menu-item-text"><spring:message code="header.menu.edit.undo" /></span>
					<span class="varsql-menu-item-key">Ctrl+Z</span>
				</a>
			</li>
			<li class="varsql-menu-item" data-menu-item="edit_redo" data-sql-editor-menu="y">
				<a href="javascript:;">
					<span class="varsql-menu-item-text"><spring:message code="header.menu.edit.redo" /></span>
					<span class="varsql-menu-item-key">Ctrl+Y</span>
				</a>
			</li>
			<%--
			<li class="varsql-menu-item" data-menu-item="edit_compare">
				<a href="javascript:;">
					<span class="varsql-menu-item-text"><spring:message code="header.menu.edit.compare" /></span>
				</a>
			</li>
			--%>
		</ul>
	</li>
	<li class="top-menu-button">
		<a href="javascript:;" class="varsql-top-menu-label" data-toggle="header-dropdown"><spring:message code="header.menu.search" /></a>
		<ul class="varsql-top-menu">
			<li class="varsql-menu-item" data-menu-item="search_find" data-sql-editor-menu="y">
				<a href="javascript:;">
					<span class="varsql-menu-item-text"><spring:message code="header.menu.search.find" /></span>
					<span class="varsql-menu-item-key">Ctrl+F</span>
				</a>
			</li>
		</ul>
	</li>
	<li class="top-menu-button">
		<a href="javascript:;" class="varsql-top-menu-label" data-toggle="header-dropdown"><spring:message code="header.menu.tool" /></a>
		<ul class="varsql-top-menu">
			<%-- <!-- 가져오기 -->
			<li class="varsql-menu-item" data-menu-item="tool_import">
				<a href="javascript:;">
					<span class="varsql-menu-item-text"><spring:message code="header.menu.tool.import" /></span>
				</a>
			</li>
			 --%>
			<li class="header-dropdown-submenu">
				<a href="javascript:;">
					<span class="varsql-menu-item-text"><spring:message code="header.menu.tool.export" /></span>
				</a>
				<ul class="varsql-sub-menu">
					<li class="varsql-menu-item" data-menu-item="tool_export_spec">
						<a href="javascript:;"><span class="varsql-menu-item-text"><spring:message code="header.menu.tool.export.specification" /></span></a>
					</li>
					<li class="varsql-menu-item" data-menu-item="tool_export_ddl">
						<a href="javascript:;"><span class="varsql-menu-item-text"><spring:message code="header.menu.tool.export.ddl" /></span></a>
					</li>
				</ul>
			</li>

			<li class="divider"></li>
			<li class="header-dropdown-submenu">
				<a href="javascript:;">
					<span class="varsql-menu-item-text"><spring:message code="header.menu.tool.show.plugin" /></span>
				</a>
				<ul class="varsql-sub-menu">
					<li class="varsql-menu-item" data-menu-item="tool_show_glossary">
						<a href="javascript:;"><span class="varsql-menu-item-text"><spring:message code="header.menu.tool.show.glossary" /></span></a>
					</li>
					<li class="varsql-menu-item" data-menu-item="tool_show_history">
						<a href="javascript:;"><span class="varsql-menu-item-text"><spring:message code="header.menu.tool.show.history" /></span></a>
					</li>
					<!-- li class="varsql-menu-item" data-menu-item="tool_export_data">
						<a><span class="varsql-menu-item-text"><spring:message code="header.menu.tool.export.data" /></span></a>
					</li -->
				</ul>
			</li>
			<li class="divider"></li>
			<li class="header-dropdown-submenu">
				<a href="javascript:;">
					<span class="varsql-menu-item-text"><spring:message code="header.menu.tool.theme" /></span>
				</a>
				<ul class="varsql-sub-menu">
					<li class="varsql-menu-item" data-menu-item="tool_theme_light">
						<a href="javascript:;"><span class="varsql-menu-item-text"><spring:message code="header.menu.tool.theme.light" /></span></a>
					</li>
					<li class="varsql-menu-item" data-menu-item="tool_theme_dark">
						<a href="javascript:;"><span class="varsql-menu-item-text"><spring:message code="header.menu.tool.theme.dark" /></span></a>
					</li>
				</ul>
			</li>
			<li class="divider"></li>
			<li class="varsql-menu-item" data-menu-item="tool_layout">
				<a href="javascript:;">
					<span class="varsql-menu-item-text"><spring:message code="header.menu.tool.initlayout" /></span>
				</a>
			</li>
			<li class="divider"></li>
			<li class="header-dropdown-submenu">
				<a href="javascript:;">
					<span class="varsql-menu-item-text"><spring:message code="header.menu.tool.utils" /></span>
				</a>
				<ul class="varsql-sub-menu">
					<li class="varsql-menu-item" data-menu-item="tool_utils_genexceltoddl">
						<a href="javascript:;"><span class="varsql-menu-item-text"><spring:message code="header.menu.tool.utils.genexceltoddl" /></span></a>
					</li>
				</ul>
			</li>
			<li class="divider"></li>
			<li class="varsql-menu-item" data-menu-item="tool_setting">
				<a href="javascript:;"><span class="varsql-menu-item-text"><spring:message code="header.menu.tool.setting" /></span></a>
			</li>
		</ul>
	</li>
	<li class="top-menu-button">
		<a href="javascript:;" class="varsql-top-menu-label" data-toggle="header-dropdown"><spring:message code="header.menu.help" /></a>
		<ul class="varsql-top-menu">
			<!--
			<li class="varsql-menu-item" data-menu-item="help_help">
				<a href="javascript:;"><span class="varsql-menu-item-text"><spring:message code="header.menu.help.help" /></span></a>
			</li>
			 -->
			<li class="varsql-menu-item" data-menu-item="help_dbinfo">
				<a href="javascript:;"><span class="varsql-menu-item-text"><spring:message code="header.menu.help.dbinfo" /></span></a>
			</li>
			<li class="varsql-menu-item" data-menu-item="help_info">
				<a href="javascript:;"><span class="varsql-menu-item-text"><spring:message code="header.menu.help.info" /></span></a>
			</li>
		</ul>
	</li>
</ul>
