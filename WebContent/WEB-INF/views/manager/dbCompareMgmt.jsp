<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/include/tagLib.jspf"%>

<!-- Page Heading -->
<div class="row">
    <div class="col-lg-12">
        <h1 class="page-header"><spring:message code="manage.menu.dbcomparemgmt" /></h1>
    </div>
    <!-- /.col-lg-12 -->
</div>
	
<div class="row" id="varsqlVueArea">
	<div class="col-xs-12">
		<div class="panel panel-default">
			<div class="panel-heading">
				타켓
				
				<select class="input-sm" v-model="diffItem.target" @change="targetChange(diffItem.target)" style="width:30%">
					<option value="">선택</option>
					<template href="javascript:;" class="list-group-item" v-for="(item,index) in dbList">
						<option :value="item.VCONNID">{{item.VNAME}}</option>
	    			</template>
				</select>
				
				대상
				<select class="input-sm" v-model="diffItem.source" style="width:30%">
					<option value="">선택</option>
					<template href="javascript:;" class="list-group-item" v-for="(item,index) in dbList">
						<option :value="item.VCONNID">{{item.VNAME}}</option>
	    			</template>
				</select>
				오브젝트
				<select class="input-sm" v-model="diffItem.objectType" style="width:10%">
					<option value="">선택</option>
					<template href="javascript:;" class="list-group-item" v-for="(item,index) in objectList">
						<option :value="item.contentid">{{item.name}}</option>
	    			</template>
				</select>
				<button type="button" class="btn btn-sm btn-primary" style="margin-bottom: 3px">
					조회
				</button>
			</div>
			<div class="panel-body">
				<div class="row">
					<div class="col-xs-7">
						<div class="panel panel-default">
							<div class="panel-body">	
								<div class="col-xs-6">
									<div class="row" style="border:1px solid #f1f1f1;">
										asdf
									</div>
								</div>
								<div class="col-xs-6">
									<div class="row" style="border:1px solid #f1f1f1;">
										asdf
									</div>
								</div>
							</div>
						</div>
						<div class="panel panel-default">
							<div class="panel-body">	
								<div class="col-xs-6">
									<div class="row" style="border:1px solid #f1f1f1;">
										asdf
									</div>
								</div>
								<div class="col-xs-6">
									<div class="row" style="border:1px solid #f1f1f1;">
										asdf
									</div>
								</div>
							</div>
						</div>
					</div>
					<div class="col-xs-5">
						<div class="panel panel-default">
							<div class="panel-heading">
								비교 결과
							</div>
							<div class="panel-body">	
								ㅁㄴㅇㄻㄴㅇㄹ
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
		
</div>
<!-- /.row -->


<script>
VarsqlAPP.vueServiceBean( {
	el: '#varsqlVueArea'
	,data: {
		dbList : ${varsqlfn:objectToJson(dbList)}
		,diffItem : {
			target :''
			,source :''
			,objectType : ''
		}
		,objectList : []
	}
	,methods:{
		init : function(){
			
		}
		,search : function(no){
			var _self = this; 
			
			var param = _self.diffItem;
			if(param.target ==''){
				VARSQLUI.toast.open('타켓을 선택하세요.');
				return ;
			}
			if(param.source ==''){
				VARSQLUI.toast.open('대상을 선택하세요.');
				return ;
			}
			
			if(param.objectType ==''){
				VARSQLUI.toast.open('objectType을 선택하세요.');
				return ;
			}
			
			
			this.$ajax({
				url:{gubun:VARSQL.uri.manager, url:'/diff/objectList'}
				,data : param
				,success: function(resData) {
					
				}
			})
		}
		,targetChange : function (val){
			var _self = this; 
			
			this.$ajax({
				url:{gubun:VARSQL.uri.manager, url:'/diff/objectType'}
				,data : {
					vconnid : val
				}
				,success: function(resData) {
					_self.objectList = resData.items;
				}
			})
			
		}
	}
});
</script>