<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/include/initvariable.jspf"%>
<style>
.left-panel {
	margin-bottom: 5px;
}

.left-panel.padding2{
	padding :2px 2px;
}

</style>

<div class="panel panel-default left-panel" style="min-height:600px;">
	<div class="panel-default left-panel padding2">
		<div id="leftDBList">
		</div>
	</div>

	<div class="ui-tabs ui-widget">
		<ul id="leftServiceMenu" style="margin-right: 2px;" class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-tl ui-corner-tr" role="tablist">
			<li>db를 선택하시오.</li>
		</ul>
		<div id="leftServiceMenuContent" style="height:180px;"></div>
		<div id="metadataContentAreaWrap"><div id="metadataContentArea"></div></div>
	</div>
</div>

