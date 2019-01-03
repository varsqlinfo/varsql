<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/include/tagLib.jspf"%>
<style>
.view-area{
	display:none;
}

.view-area.on{
	display:block;
}
.list-group-item a{
	color : #000;
}

</style>
<!-- Page Heading -->
<div class="row">
    <div class="col-lg-12">
        <h1 class="page-header"><spring:message code="admin.menu.database" /></h1>
    </div>
    <!-- /.col-lg-12 -->
</div>
<div class="row" id="varsqlVueArea">
	<div class="col-xs-5">
		<div class="panel panel-default">
			<div class="panel-heading">
				<div class="input-group">
					<input type="text" value="" v-model="searchVal" class="form-control" @keydown.enter="search()">
					<span class="input-group-btn">
						<button class="btn btn-default searchBtn" type="button" @click="search()"> <span class="glyphicon glyphicon-search"></span></button>
					</span>
				</div>
			</div>
			<!-- /.panel-heading -->
			<div class="panel-body">
				<div class="list-group" id="dbinfolist">
					<template v-for="(item,index) in gridData">
						<div class="list-group-item">
							<a href="javascript:;" @click="itemView(item,'view')">
								<span class="clickItem" >{{item.VNAME}}</span>
							</a>
							<a href="javascript:;" @click="itemView(item,'opt')" style="float:right;">
			    				<span class="clickItem pull-right glyphicon glyphicon-pencil" style="width:60px;">옵션</span>
			    			</a>
		    			</div>
	    			</template>
	    			<div class="text-center" v-if="gridData.length === 0"><spring:message code="msg.nodata"/></div>
				</div>
				
				<page-navigation :page-info="pageInfo" callback="search"></page-navigation>
			</div>
			<!-- /.panel-body -->
		</div>
		<!-- /.panel -->
	</div>
	<!-- /.col-lg-4 -->
	<div class="col-xs-7" >
		<div class="panel panel-default detail_area_wrapper" >
			<div class="panel-heading"><spring:message code="admin.form.header" /><span id="selectDbInfo" style="margin:left:10px;font-weight:bold;">{{detailItem.VNAME}}</span></div>
			<!-- /.panel-heading -->
			<div class="panel-body">
				<div class="view-area" :class="viewMode=='view'?'on':''">
					<form id="addForm" name="addForm" class="form-horizontal" >
						<div class="form-group">
							<div class="col-sm-12">
								<div class="pull-right">
									<button type="button" class="btn btn-default" @click="setDetailItem()"><spring:message code="btn.add"/></button>
									<button type="button" class="btn btn-default" @click="save()"><spring:message code="btn.save"/></button>
									<button type="button" class="btn btn-default" @click="save('poolInit')" :class="detailFlag===false?'hidden':''"><spring:message code="btn.save.andpoolnit"/></button>
									<button type="button" class="btn btn-primary" @click="connectionCheck()" :class="detailFlag===false?'hidden':''"><spring:message code="btn.connnection.check"/></button>
									<button type="button" class="btn btn-danger"  @click="deleteInfo()" :class="detailFlag===false?'hidden':''"><spring:message code="btn.delete"/></button>
								</div>
							</div>
						</div>
						<div id="warningMsgDiv"></div>
						<div class="form-group" :class="errors.has('NAME') ? 'has-error' :''">
							<label class="col-sm-4 control-label"><spring:message code="admin.form.db.vname" /></label>
							<div class="col-sm-8">
								<input type="text" v-model="detailItem.VNAME" v-validate="'required'" name="NAME" class="form-control" />
								<div v-if="errors.has('NAME')" class="help-block">{{ errors.first('NAME') }}</div>
							</div>
						</div>
						<div class="form-group" :class="errors.has('SCHEMA') ? 'has-error' :''">
							<label class="col-sm-4 control-label"><spring:message code="admin.form.db.databasename" /></label>
							<div class="col-sm-8">
								<input type="text" v-model="detailItem.VDBSCHEMA" v-validate="'required'" name="SCHEMA" class="form-control" />
								<div v-if="errors.has('SCHEMA')" class="help-block">{{ errors.first('SCHEMA') }}</div>
							</div>
						</div>
						<div class="form-group" :class="errors.has('URL') ? 'has-error' :''">
							<label class="col-sm-4 control-label"><spring:message code="admin.form.db.vurl" /></label>
							<div class="col-sm-8">
								<input type="text" v-model="detailItem.VURL" v-validate="'required'" name="URL" class="form-control" />
								<div v-if="errors.has('URL')" class="help-block">{{ errors.first('URL') }}</div>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-4 control-label"><spring:message code="admin.form.db.vid" /></label>
							<div class="col-sm-8">
								<input class="form-control text required" id="vid" name="vid" value="" v-model="detailItem.VID">
							</div>
						</div>
						
						<div class="form-group" :class="errors.has('password') || errors.has('password_confirmation') ? 'has-error' :''">
							<label class="col-sm-4 control-label"><spring:message code="admin.form.db.vpw" /></label>
							<div class="col-sm-8">
								<input v-model="detailItem.VPW" v-validate="'confirmed:password_confirmation'" name="password" type="password" class="form-control" placeholder="Password" ref="password" data-vv-as="password_confirmation"  style="margin-bottom:5px;">
								<input v-validate="" name="password_confirmation" type="password" class="form-control" placeholder="Password, Again" data-vv-as="password" ref="password_confirmation">
							    <div class="help-block" v-if="errors.has('password')">
							      {{ errors.first('password') }}
							    </div>
							    <div class="help-block" v-if="errors.has('password_confirmation')">
							      {{ errors.first('password_confirmation') }}
							    </div>
							</div>
						</div>
						
						<div class="form-group">
							<label class="col-sm-4 control-label"><spring:message code="admin.form.db.useyn" /></label>
							<div class="col-sm-8">
								<label><input type="radio" name="useyn" value="Y" v-model="detailItem.USE_YN" checked>Y</label>
								<label><input type="radio" name="useyn" value="N" v-model="detailItem.USE_YN" >N</label>
							</div>
						</div>
						
						<div class="form-group">
							<label class="col-sm-4 control-label"><spring:message code="admin.form.db.vtype" /></label>
							<div class="col-sm-8">
								<select class="form-control text required" v-model="detailItem.VTYPE" @change="dbDriverLoad(detailItem.VTYPE)">
									<c:forEach items="${dbtype}" var="tmpInfo" varStatus="status">
										<option value="${tmpInfo.URLPREFIX}" i18n="${tmpInfo.LANGKEY}">${tmpInfo.NAME}</option>
									</c:forEach>
								</select>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-4 control-label"><spring:message code="admin.form.db.vdriver" /></label>
							<div class="col-sm-8">
								<select class="form-control text required" id="vdriver" name="vdriver" v-model="detailItem.VDRIVER">
									<template href="javascript:;" class="list-group-item" v-for="(item,index) in driverList">
										<option :value="item.DRIVER_ID" :data-driver="item.DBDRIVER" selected="{{detailItem.VDRIVER==item.DRIVER_ID?true:(detailItem.VDRIVER==''&& index==0?true:false)}}">{{item.DRIVER_DESC}}({{item.DBDRIVER}})</option>
					    			</template>
								</select>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-4 control-label"><spring:message code="admin.form.db.lazyloadyn" /></label>
							<div class="col-sm-8">
								<label><input type="radio" name="lazyloadyn" value="Y" v-model="detailItem.LAZYLOAD_YN" checked>Y</label>
								<label><input type="radio" name="lazyloadyn" value="N" v-model="detailItem.LAZYLOAD_YN" >N</label>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-4 control-label"><spring:message code="admin.form.db.basetableyn" /></label>
							<div class="col-sm-8">
								<label><input type="radio" name="basetableyn" value="Y" v-model="detailItem.BASETABLE_YN" checked>Y</label>
								<label><input type="radio" name="basetableyn" value="N" v-model="detailItem.BASETABLE_YN" >N</label>
							</div>
						</div>
					</form>
				</div>
				<div class="view-area"  :class="viewMode=='opt'?'on':''">
					<form id="optionsForm" name="optionsForm" class="form-horizontal" onsubmit="return false;">
						<div class="form-group">
							<div class="col-sm-12">
								<div class="pull-right">
									<button type="button" class="btn btn-default" @click="optionSave()"><spring:message code="btn.save"/></button>
									<button type="button" class="btn btn-default" @click="optionSave('poolInit')"><spring:message code="btn.save.andpoolnit"/></button>
									<button type="button" class="btn btn-default" @click="viewArea('view')"><spring:message code="btn.close"/></button>
								</div>
							</div>
						</div>
						<div id="optWarningMsgDiv"></div>
						
						<div class="form-group">
							<label class="col-sm-4 control-label"><spring:message code="admin.form.db.minidle" /></label>
							<div class="col-sm-8">
								<input class="form-control text required" type="number" v-model="detailItem.MIN_IDLE">
							</div>
						</div>
						
						<div class="form-group">
							<label class="col-sm-4 control-label"><spring:message code="admin.form.db.maxactive" /></label>
							<div class="col-sm-8">
								<input class="form-control text required" type="number" v-model="detailItem.MAX_ACTIVE">
							</div>
						</div>
						
						<div class="form-group">
							<label class="col-sm-4 control-label"><spring:message code="admin.form.db.timeout" /></label>
							<div class="col-sm-8">
								<input class="form-control text required" type="number" v-model="detailItem.TIMEOUT">
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-4 control-label"><spring:message code="admin.form.db.exportcount" /></label>
							<div class="col-sm-8">
								<input class="form-control text required" type="number" v-model="detailItem.EXPORTCOUNT">
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-4 control-label"><spring:message code="admin.form.db.vquery" /></label>
							<div class="col-sm-8">
								<input class="form-control text required" type="text" v-model="detailItem.VQUERY">
							</div>
						</div>
					</form>
				</div>
			</div>
			<!-- /.panel-body -->
		</div>
		<!-- /.panel -->
	</div>
	<!-- /.col-lg-8 -->
</div>
<!-- /.row -->

<script>
VarsqlAPP.vueServiceBean( {
	el: '#varsqlVueArea'
	,validateCheck : true 
	,data: {
		list_count :10
		,detailFlag :false
		,searchVal : ''
		,pageInfo : {}
		,gridData :  []
		,detailItem :{}
		,viewMode : 'view'
		,driverList : []
	}
	,methods:{
		init : function(){
			this.setDetailItem();
		}
		,search : function(no){
			var _self = this; 
			
			var param = {
				pageNo: (no?no:1)
				,rows: _self.list_count
				,'searchVal':_self.searchVal
			};
			
			this.$ajax({
				url : {type:VARSQL.uri.admin, url:'/main/dblist'}
				,data : param
				,success: function(resData) {
					_self.gridData = resData.items;
					_self.pageInfo = resData.page;
				}
			})
		}
		// 상세보기
		,itemView : function(item, mode){
			var _self = this;
			
			var param = {
				vconnid : item.VCONNID
			}
			
			_self.viewArea(mode);
			
			if(_self.detailItem.VCONNID == item.VCONNID){
				return ; 
			}
			
			this.$ajax({
				url : {type:VARSQL.uri.admin, url:'/main/dbDetail'}
				,data : param
				,loadSelector : '.detail_area_wrapper'
				,success: function(resData) {
					var item  =resData.item; 
					
					if(item.VTYPE != _self.detailItem.VTYPE){
						_self.dbDriverLoad(item.VTYPE);
					}
					
					_self.setDetailItem(item);
				}
			})
		}
		,setDetailItem : function (item){
			
			if(VARSQL.isUndefined(item)){
				this.viewMode = 'view';
				this.detailFlag = false;
				this.detailItem ={
					EXPORTCOUNT: 1000
					,MAX_ACTIVE: 5
					,MIN_IDLE: 2
					,TIMEOUT: 18000
					,VDBSCHEMA: ""
					,VDRIVER: ""
					,USE_YN: "Y"
					,VID: ""
					,VNAME: ""
					,VPOOLOPT: ""
					,VPW: ""
					,VQUERY: ""
					,VTYPE: ""
					,VURL: ""
					,BASETABLE_YN: 'Y'
					,LAZYLOAD_YN: 'N'
				}
			}else{
				this.detailFlag = true; 
				this.detailItem = item;	
			}
			 
		}
		,save : function (mode){
			var _this = this; 
			
			this.$validator.validateAll().then(function (result){
				if(result){
					var poolInitVal = 'N';
					if(mode=='poolInit'){
						if(!confirm('<spring:message code="msg.saveAndpoolInit.confirm"/>')) return ; 
						poolInitVal = 'Y';
					}
					
					var param = _this.getParamVal();
					param.poolInit = poolInitVal;
					
					_this.$ajax({
						url : {type:VARSQL.uri.admin, url:'/main/dbSave'}
						,data : param 
						,success:function (resData){
							if(VARSQL.req.validationCheck(resData)){
								if(resData.resultCode != 200){
									alert(resData.message);
									return ; 
								}
								_this.search();
								_this.setDetailItem();
							}
						}
					});
				}
			});
		}
		// option save
		,optionSave : function (mode){
			var _this = this; 
			
			this.$validator.validateAll().then(function (result){
				if(result){
					var poolInitVal = 'N';
					if(mode=='poolInit'){
						if(!confirm('<spring:message code="msg.saveAndpoolInit.confirm"/>')) return ; 
						poolInitVal = 'Y';
					}
					
					var param = _this.getParamVal();
					
					param.poolInit = poolInitVal;
					
					_this.$ajax({
						url : {type:VARSQL.uri.admin, url:'/main/dbOptSave'}
						,data : param 
						,success:function (resData){
							if(VARSQL.req.validationCheck(resData)){
								_this.search();
								_this.setDetailItem();
							}
						}
					});
				}
			});
		}
		,getParamVal : function (){
			var param = {};
			var saveItem = this.detailItem; 
			for(var key in saveItem){
				param[VARSQL.util.convertCamel(key)] = saveItem[key];
			}
			return param; 
		}
		// 정보 삭제 .
		,deleteInfo : function (){
			var _this = this; 
			
			if(typeof this.detailItem.VCONNID ==='undefined'){
				$('#warningMsgDiv').html('<spring:message code="msg.warning.select" />');
				return ; 
			}
			
			if(!confirm('<spring:message code="msg.delete.confirm"/>')){
				return ; 
			}
			
			this.$ajax({
				url : {type:VARSQL.uri.admin, url:'/main/dbDelete'}
				,data: {
					vconnid : _this.detailItem.VCONNID
				}
				,success:function (resData){
					_this.setDetailItem();
					_this.search();
				}
			});
		}
		// edit element, options 처리.
		,viewArea :function (mode){
			this.viewMode = mode;
		}
		,connectionCheck : function (){
			var param = this.getParamVal();
			
			param.vdriver = $('#vdriver option:selected').attr('data-driver');
			
			this.$ajax({
				url : {type:VARSQL.uri.admin, url:'/main/dbConnectionCheck'}
				,data:param
				,success:function (resData){
					if(VARSQL.req.validationCheck(resData)){
						if(resData.messageCode =='success'){
							alert('<spring:message code="msg.success" />');
							return 
						}else{
							alert(resData.messageCode  +'\n'+ resData.message);
						}
					}
				}
			});
		}
		// db driver list
		,dbDriverLoad : function (val){
			var _this = this; 
			var param = {
				dbtype :val
			}; 
			
			if(VARSQL.isUndefined(_this.detailItem.VCONNID) || _this.detailItem.VCONNID==''){
				_this.detailItem.VDRIVER = '';
			}
			
			this.$ajax({
				url : {type:VARSQL.uri.admin, url:'/main/dbDriver'}
				,data : param 
				,success:function (resData){
					
					var result = resData.items;
		    		var resultLen = result.length;
		    		
		    		if(resultLen==0){
		    			_this.driverList = [];
		    			return ; 
		    		}
		    		
		    		if(_this.detailItem.VDRIVER==''){
		    			_this.detailItem.VDRIVER = resData.items[0].DRIVER_ID
		    		}
		    		
		    		_this.driverList = result;
		    		
		    		return ; 
				}
			});
		}
	}
});
</script>