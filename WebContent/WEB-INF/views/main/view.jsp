<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/include/initvariable.jspf"%>
<!doctype html>
<html>
<head>
<title>user List form</title>
<%@ include file="/WEB-INF/include/head.jsp"%>


<script>
	
</script>
</head>
<body>
	<form name="joinForm" action="./joinAction.html" method="post">
		로그인 성공. <br/>
		<a href="<c:url value='/user/main' />">user main</a><br /> 
		<a href="<c:url value='/manage/main' />">manage main</a><br />
		<a href="<c:url value='/admin/main' />">admin main</a><br />
		<table>
			<tr>
				<td>name</td>
				<td>${pageContext.request.userPrincipal.name} <sec:authorize
						access="hasRole('USER')">
					ROLE_USER-=============== <br>
				</sec:authorize> <sec:authorize access="hasAnyAuthority'USER','ADMIN')">
					ROLE_ADMIN-===============<br>
					ROLE_USER-===============
				</sec:authorize> <sec:authorize access="hasAnyAuthority'MANAGE')">
					ROLE_MANAGE-===============<br>
				</sec:authorize> <br /> Your password is <sec:authentication property="credentials" />

					<br /> <!-- Roles display --> <sec:authentication
						property="authorities" var="roles" scope="page" /> Your roles
					are:
					<ul>
						<c:forEach var="role" items="${roles}">
							<li>${role}</li>
						</c:forEach>
					</ul> <br /> <!-- Username display --> Your username is <sec:authentication
						property="principal.username" /> <br />
				</td>
			</tr>
			<tr>
				<td>로그아웃</td>
				<td><input type="button" value="logout"
					onclick="location.href='<c:url value="/logout" />'"></td>
			</tr>
		</table>
	</form>
</body>
</html>
