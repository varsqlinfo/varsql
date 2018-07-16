<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/include/tagLib.jspf"%>
<ul class="db-header-menu-wrapper">
	<li class="dropdown">
		<a href="javascript:;" class="header-menu-top-label" data-toggle="header-dropdown"><spring:message code="header.menu.file" /></a>
		<ul class="db-header-menu">
			<li class="header-menu-item" data-menu-item="file_new">
				<a>
					<span class="header-menu-item-text"><spring:message code="header.menu.file.new" /></span>
					<span class="header-menu-item-key">Ctrl+Shift+N</span>
				</a>
			</li>
			<li class="header-menu-item" data-menu-item="file_save">
				<a>
					<span class="header-menu-item-text"><spring:message code="header.menu.file.save" /></span>
					<span class="header-menu-item-key">Ctrl+Shift+S</span>
				</a>
			</li>
			<li class="divider"></li>
			<li class="header-menu-item" data-menu-item="file_newwin">
				<a>
					<span class="header-menu-item-text"><spring:message code="header.menu.file.newwin" /></span>
				</a>
			</li>
			<li class="divider"></li>
			<li class="header-menu-item" data-menu-item="file_close">
				<a>
					<span class="header-menu-item-text"><spring:message code="header.menu.file.close" /></span>
				</a>
			</li>
		</ul>
	</li>
	<li class="dropdown">
		<a href="javascript:;" class="header-menu-top-label" data-toggle="header-dropdown"><spring:message code="header.menu.edit" /></a>
		<ul class="db-header-menu">
			<li class="header-menu-item" data-menu-item="edit_undo">
				<a>
					<span class="header-menu-item-text"><spring:message code="header.menu.edit.undo" /></span>
					<span class="header-menu-item-key">Ctrl+Z</span>
				</a>
			</li>
			<li class="header-menu-item" data-menu-item="edit_redo">
				<a>
					<span class="header-menu-item-text"><spring:message code="header.menu.edit.redo" /></span>
					<span class="header-menu-item-key">Ctrl+Y</span>
				</a>
			</li>
			<li class="header-menu-item" data-menu-item="edit_find">
				<a>
					<span class="header-menu-item-text"><spring:message code="header.menu.edit.find" /></span>
					<span class="header-menu-item-key">Ctrl+F</span>
				</a>
			</li>
			<li class="header-menu-item" data-menu-item="edit_compare">
				<a>
					<span class="header-menu-item-text"><spring:message code="header.menu.edit.compare" /></span>
				</a>
			</li>
		</ul>
	</li>
	<li class="dropdown">
		<a href="javascript:;" class="header-menu-top-label" data-toggle="header-dropdown"><spring:message code="header.menu.tool" /></a>
		<ul class="db-header-menu">
			<%-- <!-- 가져오기 -->
			<li class="header-menu-item" data-menu-item="tool_import">
				<a>
					<span class="header-menu-item-text"><spring:message code="header.menu.tool.import" /></span>
				</a>
			</li> 
			 --%>
			<li class="header-dropdown-submenu">
				<a href="javascript:;">
					<span class="header-menu-item-text"><spring:message code="header.menu.tool.export" /></span>
				</a>
				<ul class="dropdown-menu">
					<li class="header-menu-item" data-menu-item="tool_export_spec">
						<a><span class="header-menu-item-text"><spring:message code="header.menu.tool.export.specification" /></span></a>
					</li>
					<!-- li class="header-menu-item" data-menu-item="tool_export_data">
						<a><span class="header-menu-item-text"><spring:message code="header.menu.tool.export.data" /></span></a>
					</li -->
				</ul>
			</li>
			<li class="header-menu-item" data-menu-item="tool_setting">
				<a><span class="header-menu-item-text"><spring:message code="header.menu.tool.setting" /></span></a>
			</li>
			<li class="divider"></li>
			<li class="header-dropdown-submenu">
				<a href="javascript:;">
					<span class="header-menu-item-text"><spring:message code="header.menu.tool.show.plugin" /></span>
				</a>
				<ul class="dropdown-menu">
					<li class="header-menu-item" data-menu-item="tool_show_glossary">
						<a><span class="header-menu-item-text"><spring:message code="header.menu.tool.show.glossary" /></span></a>
					</li>
					<li class="header-menu-item" data-menu-item="tool_show_history">
						<a><span class="header-menu-item-text"><spring:message code="header.menu.tool.show.history" /></span></a>
					</li>
					<!-- li class="header-menu-item" data-menu-item="tool_export_data">
						<a><span class="header-menu-item-text"><spring:message code="header.menu.tool.export.data" /></span></a>
					</li -->
				</ul>
			</li>
			<li class="divider"></li>
			<li class="header-menu-item" data-menu-item="tool_layout">
				<a>
					<span class="header-menu-item-text"><spring:message code="header.menu.tool.initlayout" /></span>
				</a>
			</li>
		</ul>
	</li>
	<li class="dropdown">
		<a href="javascript:;" class="header-menu-top-label" data-toggle="header-dropdown"><spring:message code="header.menu.help" /></a>
		<ul class="db-header-menu">
			<li class="header-menu-item" data-menu-item="help_help">
				<a><span class="header-menu-item-text"><spring:message code="header.menu.help.help" /></span></a>
			</li>
			<li class="header-menu-item" data-menu-item="help_info">
				<a><span class="header-menu-item-text"><spring:message code="header.menu.help.info" /></span></a>
			</li>
		</ul>
	</li>
</ul>
