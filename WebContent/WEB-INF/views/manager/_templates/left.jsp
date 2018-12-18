<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/include/tagLib.jspf"%>

<ul class="nav left-menu">
    <li class="<c:if test="${fn:endsWith(originalURL,'/main')}"> active</c:if>">
        <a href="<c:url value="./main" />"><i class="fa fa-fw fa-user"></i><spring:message code="manage.menu.usermgmt" /></a>
    </li>
    <li class="<c:if test="${fn:endsWith(originalURL,'/dbUserMgmt')}"> active</c:if>">
        <a href="<c:url value="./dbUserMgmt" />"><i class="fa fa-address-card-o"></i><spring:message code="manage.menu.dbnuser" /></a>
    </li>
    <li class="<c:if test="${fn:endsWith(originalURL,'/qnaMgmt')}"> active</c:if>">
        <a href="<c:url value="./qnaMgmt" />"><i class="fa fa-eye"></i><spring:message code="manage.menu.qnamgmt" /></a>
    </li>
    <li class="<c:if test="${fn:endsWith(originalURL,'/glossaryMgmt')}"> active</c:if>">
        <a href="<c:url value="./glossaryMgmt" />"><i class="fa fa-th-list"></i><spring:message code="manage.menu.glossary" /></a>
    </li>
    <li class="<c:if test="${fn:endsWith(originalURL,'/dbCompareMgmt')}"> active</c:if>">
        <a href="<c:url value="./dbCompareMgmt" />"><i class="fa fa-exchange"></i><spring:message code="manage.menu.dbcomparemgmt" /></a>
    </li>
    
    <li class="dropdown<c:if test="${fn:endsWith(originalURL,'/report')}"> active</c:if>">
		<a href="#" data-toggle="collapse" data-target="#log-sub-menu">
			<i class="fa fa-bar-chart-o"></i>
			<span class="hidden-xs"><spring:message code="manage.menu.logmgmt" /></span>
			<i class="fa fa-fw fa-caret-down"></i>
		</a>
		<ul id="log-sub-menu" class="nav sub-menu collapse">
			<li>
               <a href="<c:url value="./sqlLogStat" />"><spring:message code="manage.menu.sqllogmgmt" /></a>
            </li>
            <li>
               <a href="<c:url value="./sqlLogHistory" />"><spring:message code="manage.menu.sqllog" /></a>
            </li>
		</ul>
	</li>
</ul>