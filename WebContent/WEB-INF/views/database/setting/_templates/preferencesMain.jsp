<!DOCTYPE html>
<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/include/tagLib.jspf"%>
<html>
<head>
<title><spring:message code="database.preferences" /></title>
<%@ include file="/WEB-INF/include/head-preferences.jsp"%>

</head>

<BODY class="preferences-body">
	<div class="preferences">
		<div class="navi-area col">
			<div class="preferences-left-search">
				<input type="text" id="preferences_text" value=""/>
			</div>
			<div class="preferences-left-body">
				<div id="preferences_area"></div>
			</div>
		</div>
		<div class="content-area-frame col scroll-y">
			<iframe id="view_iframe" name="view_iframe"	class="content-view-iframe"></iframe>
		</div>
	</div>
</BODY>
</html>

<script>
$('.pub-main-body').on('selectstart', function (e){
	return false; 	
})

$(document).ready(function (){
	init();
})

function init(){
	var treeItem = [];
	treeItem.push({id:'top'	,pid:''	,name:'preferences-top'});
	treeItem.push({id:'1',pid:'top',name:'일반',url:'<c:url value="/database/preferences/generalSetting.vsql?conuid=${param.conuid}" />'});
	treeItem.push({id:'2',pid:'top',name:'SQL포멧설정',url:'<c:url value="/database/preferences/sqlFormatSetting.vsql?conuid=${param.conuid}" />'});	
	treeItem.push({id:'3',pid:'top',name:'단축키',url:'<c:url value="/database/preferences/keySetting.vsql?conuid=${param.conuid}" />'});	
	treeItem.push({id:'4',pid:'top',name:'코드편집기',url:'<c:url value="/database/preferences/codeEditerSetting.vsql?conuid=${param.conuid}" />'});	
	treeItem.push({id:'4-1',pid:'4',name:'글꼴',url:'<c:url value="/database/preferences/sqlFormatSetting.vsql?conuid=${param.conuid}" />'});	
	treeItem.push({id:'5',pid:'top',name:'래포트',url:'<c:url value="/database/preferences/sqlFormatSetting.vsql?conuid=${param.conuid}" />'});	
	treeItem.push({id:'5-1',pid:'5',name:'내보내기 설정',url:'<c:url value="/database/preferences/exportSetting.vsql?conuid=${param.conuid}" />'});
	treeItem.push({id:'6',pid:'top',name:'코드 생성설정',url:'<c:url value="/database/preferences/keySetting.vsql?conuid=${param.conuid}" />'});

	$.pubTree("#preferences_area", {
		source : treeItem
		,useIcon :{icon : false}
		,click : function (sObj){
			$('#view_iframe').attr('src',sObj.item.url);
		}
	}); // 트리 객체 네임  div명

}
</script>


