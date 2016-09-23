<!DOCTYPE html>
<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/include/initvariable.jspf"%>

<html>
<head>
<title><spring:message code="manage.home.title" /></title>
<%@ include file="/WEB-INF/include/user-head.jsp"%>
</head>
<body>
	<div id="wrapper">
		<!-- Page Heading -->
		<div id="db-header" class="row">
			<div class="col-lg-12">
			</div>
		</div>
		<div id="db-page-wrapper">
			<div class="container-fluid fill">
				<div class="row fill">
				
				</div>
			</div>
		</div>
		<div id="db-hidden-area">
			<div id="data-export-modal">
				<table style="width:700px; border: 1px solid gray;" >
					<colgroup>
						<col width="260px" />
						<col width="20px" />
						<col width="*" />
					</colgroup>
					<tr>
						<td>
							<table style="vertical-align: text-top;" class="table table-striped table-bordered table-hover dataTable no-footer" id="dataTables-example">
								<thead>
									<tr role="row">
										<th tabindex="0" rowspan="1" colspan="1" style="width: 20px;"><input type="checkbox" name="columnCheck" value="all">all</th>
										<th tabindex="0" rowspan="1" colspan="1" style="width: 150px;">Column</th>
									</tr>
								</thead>
								<tbody class="dataTableContent1">
									<tr class="gradeA add">	
										<td class=""><input type="checkbox" name="columnCheck"></td>	
										<td class="">abcde10@naver.com</td>	
									</tr>
								</tbody>
							</table>
						</td>
						<td></td>
						<td style="vertical-align: text-top;">
							<div>
								<label class="control-label">LIMIT COUNT</label>
								<input class="" id="exportCount" name="exportCount">
							</div>
							<ul>
								<li><span><input type="radio" name="exportType" value="xml"></span>XML</li>
								<li><span><input type="radio" name="exportType" value="json"></span>JSON</li>
								<li><span><input type="radio" name="exportType" value="insert"></span>INSERT문</li>
							</ul>
						</td>
					</tr>
					<tr>
						<td colspan="3" style="text-align: right;">
							<button type="button" class="btn btn-default dataExportBtn">내보내기</button>
						</td>
					</tr>
				</table>
			</div>
		</div>
	</div>
</body>
</html>

