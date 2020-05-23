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
<div class="row display-off" id="varsqlVueArea">
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
							<a href="javascript:;" @click="itemView(item)">
								<span class="clickItem" >{{item.vname}}</span>
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
			<div class="panel-heading"><spring:message code="admin.form.header" /><span id="selectDbInfo" style="margin-left:10px;font-weight:bold;">{{detailItem.vname}}</span></div>
			<!-- /.panel-heading -->
			<div class="panel-body">
				<div class="form-group" style="height: 34px;margin-bottom:10px;">
					<div class="col-sm-12">
						<div class="pull-right">
							<button type="button" class="btn btn-default" @click="setDetailItem()"><spring:message code="btn.add"/></button>
							<button type="button" class="btn btn-default" @click="save()"><spring:message code="btn.save"/></button>
							<button type="button" class="btn btn-default" @click="save('poolInit')" :class="detailFlag===false?'hidden':''"><spring:message code="btn.save.andpoolnit"/></button>
							<button type="button" class="btn btn-primary" @click="connectionClose()" :class="detailFlag===false?'hidden':''"><spring:message code="btn.connnection.close"/></button>
							<button type="button" class="btn btn-primary" @click="connectionCheck()" :class="detailFlag===false?'hidden':''"><spring:message code="btn.connnection.check"/></button>
							<button type="button" class="btn btn-danger"  @click="deleteInfo()" :class="detailFlag===false?'hidden':''"><spring:message code="btn.delete"/></button>
						</div>
					</div>
				</div>

				<form id="addForm" name="addForm" class="form-horizontal" onsubmit="return false;">
					<div id="warningMsgDiv"></div>
					<ul class="nav nav-tabs" style="margin-bottom:10px;margin-top:10px;">
		              <li class="nav-item" :class="viewMode=='view'?'active':''" @click="itemViewMode('view')">
		                <a class="nav-link" data-toggle="tab"><spring:message code="basic.information"/></a>
		              </li>
						<template v-if="detailFlag===true">
							<li class="nav-item" :class="viewMode=='opt'?'active':''" @click="itemViewMode('opt')">
			                 <a class="nav-link" data-toggle="tab"><spring:message code="options"/></a>
			                </li>
		    			</template>
		            </ul>

					<div class="view-area" data-view-mode="view" :class="viewMode=='view'?'on':''">
						<div class="form-group" :class="errors.has('NAME') ? 'has-error' :''">
							<label class="col-sm-4 control-label"><spring:message code="admin.form.db.vname" /></label>
							<div class="col-sm-8">
								<input type="text" v-model="detailItem.vname" v-validate="'required'" name="NAME" class="form-control" />
								<div v-if="errors.has('NAME')" class="help-block">{{ errors.first('NAME') }}</div>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-4 control-label"><spring:message code="admin.form.db.vtype" /></label>
							<div class="col-sm-8">
								<select class="form-control text required" v-model="detailItem.vtype" @change="dbDriverLoad(detailItem.vtype)">
									<c:forEach items="${dbtype}" var="tmpInfo" varStatus="status">
										<option value="${tmpInfo.urlprefix}" i18n="${tmpInfo.langkey}">${tmpInfo.name}</option>
									</c:forEach>
								</select>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-4 control-label"></label>
							<div class="col-sm-8">
								<input type="checkbox" v-model="detailItem.urlDirectYn" true-value="Y" false-value="N" /><spring:message code="admin.form.db.urldirectmsg" />
							</div>
						</div>
						<div v-if="detailItem.urlDirectYn != 'Y'">
							<div class="form-group" :class="errors.has('SERVERIP') ? 'has-error' :''">
								<label class="col-sm-4 control-label"><spring:message code="admin.form.db.serverip" /></label>
								<div class="col-sm-8">
									<input type="text" v-model="detailItem.vserverip" v-validate="'required'" name="SERVERIP" class="form-control" />
									<div v-if="errors.has('SERVERIP')" class="help-block">{{ errors.first('SERVERIP') }}</div>
								</div>
							</div>
							<div class="form-group" :class="errors.has('PORT') ? 'has-error' :''">
								<label class="col-sm-4 control-label"><spring:message code="admin.form.db.port" /></label>
								<div class="col-sm-8">
									<input type="number" v-model="detailItem.vport" name="PORT" class="form-control" />
									<div v-if="errors.has('PORT')" class="help-block">{{ errors.first('PORT') }}</div>
								</div>
							</div>
						</div>
						<div v-else>
							<div class="form-group" :class="errors.has('URL') ? 'has-error' :''">
								<label class="col-sm-4 control-label"><spring:message code="admin.form.db.vurl" /></label>
								<div class="col-sm-8">
									<input type="text" v-model="detailItem.vurl" v-validate="'required'" name="URL" class="form-control" />
									<div v-if="errors.has('URL')" class="help-block">{{ errors.first('URL') }}</div>
								</div>
							</div>
						</div>

						<div class="form-group" :class="errors.has('DBNAME') ? 'has-error' :''">
							<label class="col-sm-4 control-label"><spring:message code="admin.form.db.databasename" /></label>
							<div class="col-sm-8">
								<input type="text" v-model="detailItem.vdatabasename" v-validate="'required'" name="DBNAME" class="form-control" />
								<div v-if="errors.has('DBNAME')" class="help-block">{{ errors.first('DBNAME') }}</div>
							</div>
						</div>

						<div class="form-group">
							<label class="col-sm-4 control-label"><spring:message code="admin.form.db.vid" /></label>
							<div class="col-sm-8">
								<input class="form-control text required" id="vid" name="vid" value="" autocomplete="false" v-model="detailItem.vid">
							</div>
						</div>

						<div class="form-group" :class="errors.has('password') || errors.has('password_confirmation') ? 'has-error' :''">
							<label class="col-sm-4 control-label"><spring:message code="admin.form.db.vpw" /></label>
							<div class="col-sm-8">
								<input v-model="detailItem.vpw" v-validate="'confirmed:password_confirmation'" name="password" type="password" autocomplete="false" class="form-control" placeholder="Password" ref="password" data-vv-as="password_confirmation"  style="margin-bottom:5px;">
								<input v-model="detailItem.CONFIRM_PW" v-validate="" name="password_confirmation" type="password" class="form-control" autocomplete="false" placeholder="Password, Again" data-vv-as="password" ref="password_confirmation">
							    <div class="help-block" v-if="errors.has('password')">
							      {{ errors.first('password') }}
							    </div>
							    <div class="help-block" v-if="errors.has('password_confirmation')">
							      {{ errors.first('password_confirmation') }}
							    </div>
							</div>
						</div>

						<div class="form-group">
							<label class="col-sm-4 control-label"><spring:message code="admin.form.db.vdriver" /></label>
							<div class="col-sm-8">
								<select class="form-control text required" id="vdriver" name="vdriver" v-model="detailItem.vdriver">
									<template href="javascript:;" class="list-group-item" v-for="(item,index) in driverList">
										<option :value="item.driverId" :data-driver="item.dbdriver" selected="{{detailItem.vdriver==item.driverId?true:(detailItem.vdriver==''&& index==0?true:false)}}">{{item.driverDesc}}({{item.dbdriver}})</option>
					    			</template>
								</select>
							</div>
						</div>

						<div class="form-group">
							<label class="col-sm-4 control-label"><spring:message code="admin.form.db.useyn" /></label>
							<div class="col-sm-8">
								<label><input type="radio" name="useyn" value="Y" v-model="detailItem.useYn" checked>Y</label>
								<label><input type="radio" name="useyn" value="N" v-model="detailItem.useYn" >N</label>
							</div>
						</div>

						<div class="form-group">
							<label class="col-sm-4 control-label"><spring:message code="admin.form.db.lazyloadyn" /></label>
							<div class="col-sm-8">
								<label><input type="radio" name="lazyloadyn" value="Y" v-model="detailItem.lazyloadYn" checked>Y</label>
								<label><input type="radio" name="lazyloadyn" value="N" v-model="detailItem.lazyloadYn" >N</label>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-4 control-label"><spring:message code="admin.form.db.basetableyn" /></label>
							<div class="col-sm-8">
								<label><input type="radio" name="basetableyn" value="Y" v-model="detailItem.basetableYn" checked>Y</label>
								<label><input type="radio" name="basetableyn" value="N" v-model="detailItem.basetableYn" >N</label>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-4 control-label"><spring:message code="admin.form.db.schemaviewyn" /></label>
							<div class="col-sm-8">
								<label><input type="radio" name="schemaviewyn" value="Y" v-model="detailItem.schemaViewYn" checked>Y</label>
								<label><input type="radio" name="schemaviewyn" value="N" v-model="detailItem.schemaViewYn" >N</label>
							</div>
						</div>
					</div>
					<div class="view-area" data-view-mode="opt" :class="viewMode=='opt'?'on':''">
						<div class="form-group">
							<label class="col-sm-4 control-label"><spring:message code="admin.form.db.minidle" /></label>
							<div class="col-sm-8">
								<input class="form-control text required" type="number" v-model="detailItem.minIdle">
							</div>
						</div>

						<div class="form-group">
							<label class="col-sm-4 control-label"><spring:message code="admin.form.db.maxactive" /></label>
							<div class="col-sm-8">
								<input class="form-control text required" type="number" v-model="detailItem.maxActive">
							</div>
						</div>

						<div class="form-group">
							<label class="col-sm-4 control-label"><spring:message code="admin.form.db.timeout" /></label>
							<div class="col-sm-8">
								<input class="form-control text required" type="number" v-model="detailItem.timeout">
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-4 control-label"><spring:message code="admin.form.db.exportcount" /></label>
							<div class="col-sm-8">
								<input class="form-control text required" type="number" v-model="detailItem.exportcount">
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-4 control-label"><spring:message code="admin.form.db.max_select_count" /></label>
							<div class="col-sm-8">
								<input class="form-control text required" type="number" v-model="detailItem.maxSelectCount">
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-4 control-label"><spring:message code="admin.form.db.vquery" /></label>
							<div class="col-sm-8">
								<input class="form-control text required" type="text" v-model="detailItem.vquery">
							</div>
						</div>
					</div>
				</form>
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
		,itemViewMode : function (mode){
			this.viewArea(mode);
		}
		// 상세보기
		,itemView : function(item){
			var _self = this;

			var param = {
				vconnid : item.vconnid
			}

			if(_self.detailItem.vconnid == item.vconnid){
				return ;
			}

			_self.errors.clear();

			this.$ajax({
				url : {type:VARSQL.uri.admin, url:'/main/dbDetail'}
				,data : param
				,loadSelector : '.detail_area_wrapper'
				,success: function(resData) {
					var item  =resData.item;

					if(item.vtype != _self.detailItem.vtype){
						_self.dbDriverLoad(item.vtype);
					}

					_self.setDetailItem(item);
				}
			})
		}
		,setUrlDirectInfo : function (){
			this.detailItem.urlDirectYn = (this.detailItem.urlDirectYn=='N'?'Y':'N');
		}
		,setDetailItem : function (item){

			if(VARSQL.isUndefined(item)){
				this.$validator.reset()
				this.viewMode = 'view';
				this.detailFlag = false;
				this.detailItem ={
					exportcount: 1000
					,maxSelectCount : 10000
					,maxActive: 5
					,minIdle: 2
					,timeout: 18000
					,vdbschema: ""
					,vdatabasename: ""
					,vport: ""
					,vdriver: ""
					,useYn: "Y"
					,urlDirectYn:'N'
					,vid: ""
					,vname: ""
					,vpoolopt: ""
					,vpw: ""
					,vquery: ""
					,vtype: ""
					,vserverip: ""
					,vurl: ""
					,basetableYn: 'Y'
					,lazyloadYn: 'N'
					,schemaViewYn: 'N'
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
				}else{
					var errorItem = _this.errors.items[0];

					var viewMode = $('[name="'+errorItem.field+'"]').closest('.view-area').data('view-mode');

					if(_this.viewMode != viewMode){
						alert(errorItem.msg);
						_this.viewMode = _this.viewMode=='opt' ?'view' : 'opt';
					}

					return  ;
				}
			});
		}
		,getParamVal : function (){
			var param = {};
			var saveItem = this.detailItem;
			for(var key in saveItem){
				param[key] = saveItem[key];
			}
			return param;
		}
		// 정보 삭제 .
		,deleteInfo : function (){
			var _this = this;

			if(typeof this.detailItem.vconnid ==='undefined'){
				$('#warningMsgDiv').html('<spring:message code="msg.warning.select" />');
				return ;
			}

			if(!confirm('<spring:message code="msg.delete.confirm"/>')){
				return ;
			}

			this.$ajax({
				url : {type:VARSQL.uri.admin, url:'/main/dbDelete'}
				,data: {
					vconnid : _this.detailItem.vconnid
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

			this.$ajax({
				url : {type:VARSQL.uri.admin, url:'/main/dbConnectionCheck'}
				,loadSelector : 'body'
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
		,connectionClose : function (){
			var param = this.getParamVal();

			this.$ajax({
				url : {type:VARSQL.uri.admin, url:'/main/dbConnectionClose'}
				,loadSelector : 'body'
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

			if(VARSQL.isUndefined(_this.detailItem.vconnid) || _this.detailItem.vconnid==''){
				_this.detailItem.vdriver = '';
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

		    		if(_this.detailItem.vdriver==''){
		    			_this.detailItem.vdriver = resData.items[0].driverId
		    		}

		    		_this.driverList = result;

		    		return ;
				}
			});
		}
	}
});
</script>
