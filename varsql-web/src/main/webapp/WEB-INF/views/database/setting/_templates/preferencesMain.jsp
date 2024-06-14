<!DOCTYPE html>
<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/include/tagLib.jspf"%>
<html>
<head>
<title><spring:message code="preferences" /></title>
<%@ include file="/WEB-INF/include/database-preferences.jsp"%>

</head>

<BODY class="preferences-body">
	<div id="preferencesMain" class="preferences display-off">
		<div class="preferences-nav col-xs-3">
			<div class="preferences-menu-search">
				<input type="text" id="preferences_text" value=""/>
			</div>
			<div class="preferences-menu-container">
				<div id="preferences_area"></div>
			</div>
		</div>
		<div class="splitter" data-orientation="vertical" data-prev-min-size="10" data-next-min-size="60"></div>
		<div class="preferences-body padding0 col-xs-9">
			<div class="preferences-body-header">
				{{detailItem.desc}}

				<button type="button" @click="viewHelpPage()" class="pull-right"><spring:message code="help" /></button>
			</div>
			<div class="preferences-body-cont">
				<template v-if="!VARSQL.isUndefined(detailItem.url) && detailItem.url !=''">
					<iframe :src="detailItem.url"	class="preferences-body-iframe"></iframe>
				</template>
			</div>
		</div>
	</div>
</BODY>
</html>

<script>
VarsqlAPP.vueServiceBean({
	el: '#preferencesMain'
	,data: {
		detailItem :{}
	}
	,mounted : function (){

	}
	,methods:{
		init : function (){
			var _this =this;
			
			$.pubSplitter('.splitter',{
				handleSize : 8
				,button : {
					enabled : true
					,toggle : true
				}
			});
			
			var treeItem = [];
			treeItem.push({id:'top'	,pid:''	,name:'preferences-top'});
			/*
			treeItem.push({id:'1',pid:'top',name:'일반',url:'<c:url value="/database/preferences/generalSetting?conuid=${param.conuid}" />'});
			treeItem.push({id:'2',pid:'top',name:'SQL포멧설정',url:'<c:url value="/database/preferences/sqlFormatSetting?conuid=${param.conuid}" />'});
			treeItem.push({id:'3',pid:'top',name:'단축키',url:'<c:url value="/database/preferences/keySetting?conuid=${param.conuid}" />'});
			treeItem.push({id:'4',pid:'top',name:'코드편집기',url:'<c:url value="/database/preferences/codeEditerSetting?conuid=${param.conuid}" />'});
			treeItem.push({id:'4-1',pid:'4',name:'글꼴',url:'<c:url value="/database/preferences/sqlFormatSetting?conuid=${param.conuid}" />'});
			treeItem.push({id:'5',pid:'top',name:'래포트',url:'<c:url value="/database/preferences/sqlFormatSetting?conuid=${param.conuid}" />'});
			treeItem.push({id:'5-1',pid:'5',name:'내보내기 설정',url:'<c:url value="/database/preferences/exportSetting?conuid=${param.conuid}" />'});
			*/
			treeItem.push({
				id:'6'
				, pid:'top'
				, name: VARSQL.message('setting.menu.template.gen','코드 생성설정') 
				, desc : VARSQL.message('msg.setting.code.template.desc','DB table 의 컬럼정보를 가지고 코드를 자동 으로 생성할수있게 템플릿을 작성')
				, url:'<c:url value="/database/preferences/contextMenuSetting?conuid=${param.conuid}" />'
				, help:{
					name : '도울말'
					,helpUrl : '/database/preferences/help'
				}
			});

			$.pubTree("#preferences_area", {
				source : treeItem
				,firstItemClick :true
				,useIcon :{icon : false}
				,click : function (sObj){
					_this.detailItem = sObj.item;
				}
			}); // 트리 객체 네임  div명
		}
		,viewHelpPage : function(){
			if(this.detailItem.help && this.detailItem.help.helpUrl){
				VARSQLUI.popup.open(VARSQL.getContextPathUrl('/webstatic/html/help/contextmenu/index.html'));
			}
		}
	}
});
</script>


