<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/include/tagLib.jspf"%>

<div class="preferences">
	<div class="navi-area col">
		<div class="preferences-search">
			<input type="text" id="preferences_text" value=""/>
		</div>
		<div class="preferences-body scroll-y">
			<div id="preferences_area" style="padding-left:5px;"></div>
		</div>
	</div>
	<div class="content-area col scroll-y">
		<iframe id="view_iframe" name="view_iframe"	class="content-view-iframe"></iframe>
	</div>
</div>

<script>

$(document).ready(function (){
	init();
})

function init(){
	var treeItem = [];
	treeItem.push({id:'top'	,pid:''	,name:'preferences-top'});
	treeItem.push({id:'1',pid:'top',name:'일반',url:'<c:url value="/preferences/generalSetting.vsql?vconnid=${param.vconnid}" />'});
	treeItem.push({id:'2',pid:'top',name:'SQL포멧설정',url:'<c:url value="/preferences/sqlFormatSetting.vsql?vconnid=${param.vconnid}" />'});	
	treeItem.push({id:'3',pid:'top',name:'단축키',url:'<c:url value="/preferences/keySetting.vsql?vconnid=${param.vconnid}" />'});	
	treeItem.push({id:'4',pid:'top',name:'코드편집기',url:'<c:url value="/preferences/codeEditerSetting.vsql?vconnid=${param.vconnid}" />'});	
	treeItem.push({id:'4-1',pid:'4',name:'글꼴',url:'<c:url value="/preferences/sqlFormatSetting.vsql?vconnid=${param.vconnid}" />'});	
	treeItem.push({id:'5',pid:'top',name:'래포트',url:'<c:url value="/preferences/sqlFormatSetting.vsql?vconnid=${param.vconnid}" />'});	
	treeItem.push({id:'5-1',pid:'5',name:'내보내기 설정',url:'<c:url value="/preferences/exportSetting.vsql?vconnid=${param.vconnid}" />'});	

	$.pubTree("#preferences_area", {
		source : treeItem
		,useIcon :{icon : false}
		,click : function (sObj){
			$('#view_iframe').attr('src',sObj.item.url);
		}
	}); // 트리 객체 네임  div명

}
</script>