<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/include/tagLib.jspf"%>

<ul class="nav left-menu">
    <li class="<c:if test="${fn:endsWith(originalURL,'/user/preferences')}"> active</c:if>">
        <a href="<c:url value="/user/preferences?header=${headerview}" />">
        	<spring:message code="user.edit.general" />
        </a>
    </li>
    <li class="<c:if test="${fn:endsWith(originalURL,'/preferences/password')}"> active</c:if>">
        <a href="<c:url value="/user/preferences/password?header=${headerview}" />">
        	<spring:message code="user.edit.pasword" />
        </a>
    </li>
    <li class="<c:if test="${fn:endsWith(originalURL,'/preferences/message')}"> active</c:if>">
        <a href="<c:url value="/user/preferences/message?header=${headerview}" />">
        	<spring:message code="user.edit.message" />
        </a>
    </li>
</ul>