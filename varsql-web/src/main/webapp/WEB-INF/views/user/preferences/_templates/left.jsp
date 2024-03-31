<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/include/tagLib.jspf"%>

<ul class="nav left-menu">
	<li>
        <a href="<c:url value="/user/" />" style="text-align:center;">
        	<i class="fa fa-home"></i><spring:message code="screen.main" text="메인화면 가기"/>
        </a>
    </li>
    <li class="<c:if test="${fn:endsWith(originalURL,'/user/preferences')}"> active</c:if>">
        <a href="<c:url value="/user/preferences?header=${headerview}" />">
        	<spring:message code="user.prefernces.menu.general" />
        </a>
    </li>
    <li class="<c:if test="${fn:endsWith(originalURL,'/preferences/password')}"> active</c:if>">
        <a href="<c:url value="/user/preferences/password?header=${headerview}" />">
        	<spring:message code="user.prefernces.menu.pasword" />
        </a>
    </li>
    <li class="<c:if test="${fn:endsWith(originalURL,'/preferences/message')}"> active</c:if>">
        <a href="<c:url value="/user/preferences/message?header=${headerview}" />">
        	<spring:message code="user.prefernces.menu.message" />
        </a>
    </li>
    <li class="<c:if test="${fn:endsWith(originalURL,'/preferences/qna')}"> active</c:if>">
        <a href="<c:url value="/user/preferences/qna?header=${headerview}" />">
        	<spring:message code="user.prefernces.menu.qna" />
        </a>
    </li>
    <li class="<c:if test="${fn:endsWith(originalURL,'/preferences/sqlfile')}"> active</c:if>">
        <a href="<c:url value="/user/preferences/sqlFile?header=${headerview}" />">
        	<spring:message code="user.prefernces.menu.sqlfile" />
        </a>
    </li>
    
    <li class="dropdown<c:if test="${selectMenu eq 'fileList'}"> active</c:if>">
		<a href="#" data-toggle="collapse" data-target="#dbgroup-sub-menu">
			<i class="fa fa-object-group"></i>
			<span class="hidden-xs"><spring:message code="user.prefernces.menu.file" text="File"/></span>
			<i class="fa fa-fw fa-caret-down"></i>
		</a>
		<ul id="dbgroup-sub-menu" class="nav sub-menu collapse <c:if test="${selectMenu eq 'fileList'}"> in</c:if>">
			<li>
                <a href="<c:url value="/user/preferences/fileImportList?header=${headerview}" />"><spring:message code="user.prefernces.menu.fileimportlist" /></a>
            </li>
            <li>
				<a href="<c:url value="/user/preferences/fileExportList?header=${headerview}" />"><spring:message code="user.prefernces.menu.fileexportlist" /></a>
			</li>
		</ul>
	</li>
</ul>