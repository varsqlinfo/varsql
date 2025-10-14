<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/include/tagLib.jspf"%>

<div class="row">
	<div class="col-lg-12">
		<h1 class="page-header"><spring:message code="admin.menu.appEnv" /></h1>
	</div>
</div>

<div class="row display-off" id="appViewArea">
	<div class="col-lg-12">
		<div class="panel panel-default">
			<div class="panel-body" >
				<div class="list-group">
					<table
						class="table table-striped table-bordered table-hover dataTable no-footer"
						id="dataTables-example" style="table-layout:fixed;width:100%;">
						<thead>
							<tr role="row">
								<th style="width: 80px;">
									key
								</th>
								<th style="width: 180px;">
									value
								</th>
							</tr>
						</thead>
						<tbody>
							<tr v-for="(item,index) in configArr" class="gradeA">
								<td>
									<div class="text-ellipsis" :title="item.key">
										{{item.key}}
									</div>
								</td>
								<td class="center">
									<template v-if="item.isChild">
										<table class="table table-striped table-bordered table-hover">
											<tr v-for="(childItem, index) in item.value" class="gradeA">
												<td :title="childItem.key">{{childItem.key}}</td>
												<td :title="childItem.value">{{childItem.value}}</td>
											</tr>
										</table>
									</template>
									<template v-else>
										<div class="text-ellipsis" :title="item.value">{{item.value}}</div>
									</template>
								</td>
							</tr>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
</div>
<script>

function getConfigArr(configInfo){
	var configArr = [];
	
	for(var key in configInfo){
		var val = configInfo[key]; 
		
		var isChild = VARSQL.isObject(val); 
		
		configArr.push({
			key : key
			, isChild : isChild
			, value : isChild ? getConfigArr(val)  :val
		})
	}
	
	return configArr; 
}

VarsqlAPP.vueServiceBean({
	el: '#appViewArea'
	,data: {
		configInfo : ${configInfo}
		,configArr : []
	}
	, created: function () {
		this.configArr = getConfigArr(this.configInfo); 
	}
	,methods:{
		
	}
});


</script>