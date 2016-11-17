<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/include/tagLib.jspf"%>
<style>
.left-panel {
	margin-bottom: 5px;
}

.left-panel.padding2{
	padding :2px 2px;
}

</style>

<div class="ui-layout-left-top-area">
	<div class="panel panel-default left-panel">
		<div class="panel-default left-panel padding2">
			<div id="leftDBList">
			</div>
		</div>
	</div>
</div>


<div class="ui-layout-left-middle-area">
	<div style="height:100%;width:100%;display:table;">
		<div class="ui-tabs ui-widget" style="width:100%;display:table-row;">
			<ul id="leftServiceMenu" style="margin-right: 2px;" class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-tl ui-corner-tr" role="tablist">
				<li>db를 선택하시오.</li>
			</ul>
		</div>
		<div id="left_service_menu_content" style="width:100%;height:100%;display:table-row;"></div>
	</div>
</div>
<div class="ui-layout-left-bottom-area">
	<div id="metadata_content_area_wrap"></div>
</div>

