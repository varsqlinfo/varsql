<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/include/tagLib.jspf"%>

<ul class="nav left-menu">
    <li <c:if test="${fn:endsWith(originalURL,'/main')}">class="active"</c:if>>
        <a href="<c:url value="./" />"><i class="fa fa-database"></i><spring:message code="admin.menu.database" /></a>
    </li>
    
    <li <c:if test="${fn:endsWith(originalURL,'/managerMgmt')}">class="active"</c:if>>
        <a href="<c:url value="./managerMgmt" />"><i class="fa fa-user-secret"></i><spring:message code="admin.menu.managermgmt" /></a>
    </li>
    <li <c:if test="${fn:endsWith(originalURL,'/databaseUserMgmt')}">class="active"</c:if>>
        <a href="<c:url value="./databaseUserMgmt" />"><i class="fa fa-drivers-license-o"></i><spring:message code="admin.menu.databaseusermgmt" /></a>
    </li>
    <li <c:if test="${fn:endsWith(originalURL,'/errorlogMgmt')}">class="active"</c:if>>
        <a href="<c:url value="./errorlogMgmt" />"><i class="fa fa-server"></i><spring:message code="admin.menu.errorlog" /></a>
    </li>
</ul>