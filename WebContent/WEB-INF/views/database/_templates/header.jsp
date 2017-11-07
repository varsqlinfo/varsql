<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/include/tagLib.jspf"%>
<style>
.dropdown-submenu{
	position:relative;
}

.dropdown-submenu .dropdown-menu{
	top:0px;
	left :100%;
	margin-top:-1px;
}

</style>
<ul class="db-nav navbar-nav db-header-menu-wrapper">
	<li class="dropdown">
		<a href="#" class="dropdown-toggle" data-toggle="dropdown"><spring:message code="header.menu.file" /></a>
		<ul class="dropdown-menu db-header-menu">
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
			<li class="header-menu-item" data-menu-item="file_close">
				<a>
					<span class="header-menu-item-text"><spring:message code="header.menu.file.close" /></span>
				</a>
			</li>
		</ul>
	</li>
	<li class="dropdown">
		<a href="#" class="dropdown-toggle" data-toggle="dropdown"><spring:message code="header.menu.edit" /></a>
		<ul class="dropdown-menu db-header-menu">
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
			<li class="header-menu-item" data-menu-item="edit_compare">
				<a>
					<span class="header-menu-item-text"><spring:message code="header.menu.edit.compare" /></span>
				</a>
			</li>
		</ul>
	</li>
	<li class="dropdown">
		<a href="#" class="dropdown-toggle" data-toggle="dropdown"><spring:message code="header.menu.tool" /></a>
		<ul class="dropdown-menu db-header-menu">
			<li class="header-menu-item" data-menu-item="tool_import">
				<a>
					<span class="header-menu-item-text"><spring:message code="header.menu.tool.import" /></span>
				</a>
			</li> 
			<li class="dropdown-submenu">
				<a href="javascript:;" onclick="return false;">
					<span class="header-menu-item-text"><spring:message code="header.menu.tool.export" /></span>
				</a>
				<ul class="dropdown-menu">
					<li class="header-menu-item" data-menu-item="tool_export">
						<a><span class="header-menu-item-text"><spring:message code="header.menu.tool.export" /></span></a>
					</li>
					<li class="header-menu-item" data-menu-item="tool_export">
						<a><span class="header-menu-item-text"><spring:message code="header.menu.tool.export" /></span></a>
					</li>
				</ul>
			</li> 
			<li class="header-menu-item" data-menu-item="tool_setting">
				<a>
					<span class="header-menu-item-text"><spring:message code="header.menu.tool.setting" /></span>
				</a>
				
			</li>
		</ul>
	</li>
	<li class="dropdown">
		<a href="#" class="dropdown-toggle" data-toggle="dropdown"><spring:message code="header.menu.help" /></a>
		<ul class="dropdown-menu db-header-menu">
			<li>구현중</li>
		</ul>
	</li>
</ul>

<div class="pull-right"><a href="${requestScope['javax.servlet.forward.request_uri']}?${pageContext.request.queryString}" target="_new"><span class="glyphicon glyphicon-new-window"></span></a></div>
				
