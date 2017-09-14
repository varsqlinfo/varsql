<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/include/tagLib.jspf"%>

<ul class="db-nav navbar-nav db-header-menu-wrapper">
	<li class="dropdown">
		<a href="#" class="dropdown-toggle" data-toggle="dropdown"><spring:message code="header.menu.file" /></a>
		<ul class="dropdown-menu db-header-menu">
			<li class="header-menu-item" data-menu-item="file_save"><a><spring:message code="header.menu.file.save" /></a></li>
			<li class="divider"></li>
			<li class="header-menu-item" data-menu-item="file_close"><a><spring:message code="header.menu.file.close" /></a></li>
		</ul>
	</li>
	<li class="dropdown">
		<a href="#" class="dropdown-toggle" data-toggle="dropdown"><spring:message code="header.menu.edit" /></a>
		<ul class="dropdown-menu db-header-menu">
			<li class="header-menu-item" data-menu-item="edit_undo"><a><spring:message code="header.menu.edit.undo" /><span class="pull-right">Ctrl+Z</span></a></li>
			<li class="header-menu-item" data-menu-item="edit_redo"><a><spring:message code="header.menu.edit.redo" /><span class="pull-right">Ctrl+Y</span></a></li>
			<li class="header-menu-item" data-menu-item="edit_compare"><a><spring:message code="header.menu.edit.compare" /></a></li>
		</ul>
	</li>
	<li class="dropdown">
		<a href="#" class="dropdown-toggle" data-toggle="dropdown"><spring:message code="header.menu.tool" /></a>
		<ul class="dropdown-menu db-header-menu">
			<li class="header-menu-item" data-menu-item="tool_import"><a><spring:message code="header.menu.tool.import" /></a></li> 
			<li class="header-menu-item" data-menu-item="tool_export"><a><spring:message code="header.menu.tool.export" /></a></li> 
			<li class="header-menu-item" data-menu-item="tool_setting"><a><spring:message code="header.menu.tool.setting" /></a></li>
		</ul>
	</li>
	<li class="dropdown">
		<a href="#" class="dropdown-toggle" data-toggle="dropdown"><spring:message code="header.menu.help" /></a>
		<ul class="dropdown-menu db-header-menu">
			<li>구현중</li>
		</ul>
	</li>
</ul>

<div class="pull-right"><a href="${requestScope['javax.servlet.forward.request_uri']}?${pageContext.request.queryString}" target="_new">_</a></div>
				
