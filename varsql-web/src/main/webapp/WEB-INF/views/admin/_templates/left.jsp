<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/include/tagLib.jspf"%>

<ul class="nav left-menu">
    <li <c:if test="${selectMenu eq 'databaseMgmt'}">class="active"</c:if>>
        <a href="<varsql:url type="admin" suffix="databaseMgmt" />"><i class="fa fa-database"></i><spring:message code="admin.menu.database" /></a>
    </li>
    
    <li <c:if test="${selectMenu eq 'managerMgmt'}">class="active"</c:if>>
        <a href="<varsql:url type="admin" suffix="managerMgmt" />"><i class="fa fa-user-secret"></i><spring:message code="admin.menu.managermgmt" /></a>
    </li>
    <li <c:if test="${selectMenu eq 'databaseManagerMgmt'}">class="active"</c:if>>
        <a href="<varsql:url type="admin" suffix="databaseManagerMgmt" />"><i class="fa fa-drivers-license-o"></i><spring:message code="admin.menu.databasemanagermgmt" /></a>
    </li>
    <li <c:if test="${selectMenu eq 'errorlogMgmt'}">class="active"</c:if>>
        <a href="<varsql:url type="admin" suffix="errorlogMgmt" />"><i class="fa fa-server"></i><spring:message code="admin.menu.errorlog" /></a>
    </li>
    <li <c:if test="${selectMenu eq 'driverMgmt'}">class="active"</c:if>>
        <a href="<varsql:url type="admin" suffix="driverMgmt" />"><i class="fa fa-ticket"></i><spring:message code="admin.menu.jdbcprovidermgmt"/></a>
    </li>
    
    <li class="dropdown<c:if test="${selectMenu eq 'env'}"> active</c:if>">
		<a href="#" data-toggle="collapse" data-target="#dbgroup-sub-menu">
			<i class="fa fa-info"></i>
			<span class="hidden-xs"><spring:message code="admin.menu.env" /></span>
			<i class="fa fa-fw fa-caret-down"></i>
		</a>
		<ul id="dbgroup-sub-menu" class="nav sub-menu collapse <c:if test="${selectMenu eq 'env'}"> in</c:if>">
			<li>
                <a href="<varsql:url type="admin" suffix="appEnv" />"><spring:message code="admin.menu.appEnv" /></a>
            </li>
            <li>
				<a href="<varsql:url type="admin" suffix="systemInfo" />"><spring:message code="admin.menu.systeminfo" /></a>
			</li>
		</ul>
	</li>
</ul>