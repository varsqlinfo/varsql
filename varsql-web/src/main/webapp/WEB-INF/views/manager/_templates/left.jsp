<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/include/tagLib.jspf"%>
<ul class="nav left-menu">
    <li class="<c:if test="${selectMenu eq 'userMgmt'}"> active</c:if>">
        <a href="<c:url value="./" />"><i class="fa fa-fw fa-user"></i><spring:message code="manager.menu.usermgmt" /></a>
    </li>
    
    <li class="dropdown<c:if test="${selectMenu eq 'dbGroupMgmt'}"> active</c:if>">
		<a href="#" data-toggle="collapse" data-target="#dbgroup-sub-menu">
			<i class="fa fa-object-group"></i>
			<span class="hidden-xs"><spring:message code="manager.menu.dbgroup" /></span>
			<i class="fa fa-fw fa-caret-down"></i>
		</a>
		<ul id="dbgroup-sub-menu" class="nav sub-menu collapse <c:if test="${selectMenu eq 'dbGroupMgmt'}"> in</c:if>">
			<li>
                <a href="<c:url value="./dbGroupMgmt" />"><spring:message code="manager.menu.dbgroup" /></a>
            </li>
            <li>
				<a href="<c:url value="./dbGroupUserMgmt" />"><spring:message code="manager.menu.dbgroupnuser" /></a>
			</li>
		</ul>
	</li>
	
    <li class="<c:if test="${selectMenu eq 'qnaMgmt'}"> active</c:if>">
        <a href="<c:url value="./qnaMgmt" />"><i class="fa fa-eye"></i><spring:message code="manager.menu.qnamgmt" /></a>
    </li>
    <li class="<c:if test="${selectMenu eq 'glossaryMgmt'}"> active</c:if>">
        <a href="<c:url value="./glossaryMgmt" />"><i class="fa fa-th-list"></i><spring:message code="manager.menu.glossary" /></a>
    </li>
    <li class="<c:if test="${selectMenu eq 'dbCompareMgmt'}"> active</c:if>">
        <a href="<c:url value="./dbCompareMgmt" />"><i class="fa fa-exchange"></i><spring:message code="manager.menu.dbcomparemgmt" /></a>
    </li>
    
    <li class="dropdown<c:if test="${selectMenu eq 'sqlLog'}"> active</c:if>">
		<a href="#" data-toggle="collapse" data-target="#log-sub-menu">
			<i class="fa fa-bar-chart-o"></i>
			<span class="hidden-xs"><spring:message code="manager.menu.logmgmt" /></span>
			<i class="fa fa-fw fa-caret-down"></i>
		</a>
		<ul id="log-sub-menu" class="nav sub-menu collapse <c:if test="${selectMenu eq 'sqlLog'}"> in</c:if>">
			<li>
               <a href="<c:url value="./sqlLogStat" />"><spring:message code="manager.menu.sqllogmgmt" /></a>
            </li>
            <li>
               <a href="<c:url value="./sqlLogHistory" />"><spring:message code="manager.menu.sqllog" /></a>
            </li>
		</ul>
	</li>
    <li class="dropdown<c:if test="${selectMenu eq 'jobMgmt'}"> active</c:if>">
		<a href="#" data-toggle="collapse" data-target="#backup-sub-menu">
			<i class="fa fa-calendar"></i>
			<span class="hidden-xs"><spring:message code="manager.menu.scheduler" text="Scheduler" /></span>
			<i class="fa fa-fw fa-caret-down"></i>
		</a>
		<ul id="backup-sub-menu" class="nav sub-menu collapse <c:if test="${selectMenu eq 'jobMgmt'}"> in</c:if>">
			<li>
               <a href="<c:url value="./dataBackupMgmt" />"><spring:message code="manager.menu.databackupgmgmt" text="데이터 백업 관리" /></a>
            </li>
            <li>
               <a href="<c:url value="./ddlBackupMgmt" />"><spring:message code="manager.menu.ddlbackupmgmt" text="DDL 백업 관리" /></a>
            </li>
            <li>
               <a href="<c:url value="./sqlJobMgmt" />"><spring:message code="manager.menu.sqljobmgmt" text="SQL Job" /></a>
            </li>
		</ul>
	</li>
</ul>