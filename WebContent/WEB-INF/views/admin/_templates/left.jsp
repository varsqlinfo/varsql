<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/include/initvariable.jspf"%>

<ul class="nav left-menu">
    <li <c:if test="${fn:endsWith(originalURL,'/main.do')}">class="active"</c:if>>
        <a href="<c:url value="./main.do" />"><i class="fa fa-fw fa-dashboard"></i><spring:message code="admin.menu.database" /></a>
    </li>
    <li <c:if test="${fn:endsWith(originalURL,'/managerMgmt.do')}">class="active"</c:if>>
        <a href="<c:url value="./managerMgmt.do" />"><i class="fa fa-fw fa-dashboard"></i><spring:message code="admin.menu.managermgmt" /></a>
    </li>
    <li <c:if test="${fn:endsWith(originalURL,'/databaseUserMgmt.do')}">class="active"</c:if>>
        <a href="<c:url value="./databaseUserMgmt.do" />"><i class="fa fa-fw fa-bar-chart-o"></i><spring:message code="admin.menu.databaseusermgmt" /></a>
    </li>
    <li <c:if test="${fn:endsWith(originalURL,'/report.do')}">class="active"</c:if>>
        <a href="<c:url value="./report.do" />"><i class="fa fa-fw fa-bar-chart-o"></i><spring:message code="admin.menu.report" /></a>
    </li>
    <li <c:if test="${fn:endsWith(originalURL,'/userMenuMgmt.do')}">class="active"</c:if>>
        <a href="<c:url value="./userMenuMgmt.do" />"><i class="fa fa-fw fa-bar-chart-o"></i><spring:message code="admin.menu.dbmenumgmt" /></a>
    </li>
    <li>
        <a href="javascript:;" data-toggle="collapse" data-target="#demo"><i class="fa fa-fw fa-arrows-v"></i> Dropdown <i class="fa fa-fw fa-caret-down"></i></a>
        <ul id="demo" class="nav sub-menu collapse">
            <li>
                <a href="#">Dropdown Item</a>
            </li>
            <li>
                <a href="#">Dropdown Item</a>
            </li>
        </ul>
    </li>
</ul>