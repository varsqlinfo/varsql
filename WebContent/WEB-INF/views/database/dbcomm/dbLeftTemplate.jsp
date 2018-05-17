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
<!-- 	<div class="panel panel-default left-panel"> -->
<!-- 		<div class="panel-default left-panel padding2"> -->
<!-- 			<div id="dbSchemaList"> -->
<!-- 			</div> -->
<!-- 		</div> -->
<!-- 	</div> -->
</div>


<div class="ui-layout-left-middle-area">
	<div style="height:100%;width:100%;display:table;">
		<div class="ui-tabs ui-widget" style="width:100%;display:table-row;">
			<div id="varsqlDbServiceMenu"></div>
		</div>
		<div id="dbServiceMenuContent" style="width:100%;height:100%;display:table-row;"></div>
	</div>
</div>
<div class="ui-layout-left-bottom-area">
	<div id="metadataContentAreaWrap"></div>
</div>

