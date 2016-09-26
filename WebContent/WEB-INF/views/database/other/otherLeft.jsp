<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/include/tagLib.jspf"%>

<script>
$(document).ready(function (){
	otherLeft.init();
});

var otherLeft = {
	init:function (){
		VARSQL.ui.leftDbObject.create($.extend({}, {param:{vconnid:'${param.vconnid}'},selector:'#leftDBList',dbtype:'other'}, ${left_db_object}));
		//VARSQL.ui.leftDbObjectServiceMenu.create($.extend({}, {param:{vconnid:'${param.vconnid}'},selector:'#leftTopMenu',dbtype:'db2',delay:10}, ${left_menu}));
	}
}
</script>
