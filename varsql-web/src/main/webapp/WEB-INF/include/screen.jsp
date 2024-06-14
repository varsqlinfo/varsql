<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/include/tagLib.jspf"%>

<c:set var="popup_blank" value="${param.popup_yn =='y'?'_blank':''}" />

<c:if test="${fn:length(sessionScope['varsql.user.screen']) > 1}">
	<c:forEach var="screenInfo" items="${sessionScope['varsql.user.screen']}" begin="0" varStatus="status">
		<li>
			<a href="<c:url value="${screenInfo.mainPage}" />" target="${popup_blank}"><spring:message code="${screenInfo.i18N}"/></a>
		</li>       
	</c:forEach>
	<li class="divider"></li>
</c:if>