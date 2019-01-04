<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/include/tagLib.jspf"%>

<ul class="nav left-menu">
    <li class="<c:if test="${fn:endsWith(originalURL,'/user/preferences')}"> active</c:if>">
        <a href="<c:url value="/user/preferences?header=${headerview}" />">
        	<spring:message code="user.menu.edit.general" />
        </a>
    </li>
    <li class="<c:if test="${fn:endsWith(originalURL,'/preferences/password')}"> active</c:if>">
        <a href="<c:url value="/user/preferences/password?header=${headerview}" />">
        	<spring:message code="user.menu.edit.pasword" />
        </a>
    </li>
    <li class="<c:if test="${fn:endsWith(originalURL,'/preferences/message')}"> active</c:if>">
        <a href="<c:url value="/user/preferences/message?header=${headerview}" />">
        	<spring:message code="user.menu.edit.message" />
        </a>
    </li>
    <li class="<c:if test="${fn:endsWith(originalURL,'/preferences/qna')}"> active</c:if>">
        <a href="<c:url value="/user/preferences/qna?header=${headerview}" />">
        	<spring:message code="user.menu.edit.qna" />
        </a>
    </li>
</ul>