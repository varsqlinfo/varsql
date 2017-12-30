<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/include/tagLib.jspf"%>
<!-- Brand and toggle get grouped for better mobile display -->

<div id="logo" class="navbar-header col-xs-12 col-sm-2">
	<a id="logo-title" href="<c:url value="./" />">
		<spring:message	code="label.user.preferences" />
	</a>
</div>
<!-- Top Menu Items -->
<div id="top-panel" class="col-xs-12 col-sm-10">
	<div class="pull-right">
		<ul class="nav navbar-right top-nav">
			<li class="dropdown">
				<a href="#" class="dropdown-toggle"	data-toggle="dropdown">
					<sec:authentication property="principal.fullname" /> <b class="caret"></b>
				</a>
				<ul class="top-setting-menu dropdown-menu">
					<%@ include file="/WEB-INF/include/screen.jspf"%>
					<li>
						<a href="<c:url value="/logout" />">
							<i class="fa fa-fw fa-power-off"></i>
							<spring:message	code="btn.logout" />
						</a>
					</li>
				</ul>
			</li>
		</ul>
	</div>
</div>