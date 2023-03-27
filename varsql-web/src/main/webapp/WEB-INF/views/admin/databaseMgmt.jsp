<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/include/tagLib.jspf"%>
<!-- Page Heading -->
<div class="row">
    <div class="col-lg-12">
        <h1 class="page-header"><spring:message code="admin.menu.database" /></h1>
        <input type="password" autocomplete="new-password" style="display:none" >
    </div>
    <!-- /.col-lg-12 -->
</div>
<div class="row display-off" id="varsqlVueArea">
	<div class="col-xs-5">
		<div class="panel panel-default">
			<div class="panel-heading">
				<div class="input-group">
					<input type="text" value="" v-model="searchVal" class="form-control" @keydown.enter="search()" autocomplete="off">
					<span class="input-group-btn">
						<button class="btn btn-default searchBtn" type="button" @click="search()"> <span class="glyphicon glyphicon-search"></span></button>
					</span>
				</div>
			</div>
			<!-- /.panel-heading -->
			<div class="panel-body">
				<div class="list-group" id="dbinfolist" style="padding:0px 15px;">
					<template v-for="(item,index) in gridData">
						<div class="list-group-item row">
							<div class="col-sm-5 padding0">
								<span class="status-circle big" :class="item.statusStyle" :title="item.status"></span>
								<span class="clickItem" @click="itemView(item)"><a href="javascript:;">{{item.vname}}</a></span>
							</div>
							<div class="col-sm-7 padding0">
								<span class="pull-right">
									<button class="btn btn-xs btn-primary" @click="viewPasswordDialog(item)"><spring:message code="btn.password.view"/></button>
									<button class="btn btn-xs btn-primary" @click="connectionReset(item)"><spring:message code="btn.pool.init"/></button>
									<button class="btn btn-xs btn-default" @click="connectionClose(item)"><spring:message code="btn.pool.close"/></button>
								</span>
							</div>
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

							<template v-if="detailFlag===true">
								<button type="button" class="btn btn-default" @click="copy()"><spring:message code="btn.copy"/></button>
								<button type="button" class="btn btn-primary" @click="connectionCheck()"><spring:message code="btn.connnection.check"/></button>
								<button type="button" class="btn btn-danger"  @click="deleteInfo()"><spring:message code="btn.delete"/></button>
							</template>
						</div>
					</div>
				</div>

				<form id="addForm" name="addForm" class="form-horizontal" onsubmit="return false;">
					<div id="warningMsgDiv"></div>
					<ul class="nav nav-tabs" style="margin-bottom:10px;margin-top:10px;">
						<li class="nav-item" :class="viewMode=='view'?'active':''" @click="itemViewMode('view')">
							<a class="nav-link" data-toggle="tab"><spring:message code="basic.information"/></a>
						</li>
						<li v-if="detailFlag===true" class="nav-item" :class="viewMode=='opt'?'active':''" @click="itemViewMode('opt')">
							<a class="nav-link" data-toggle="tab"><spring:message code="options"/></a>
						</li>
		            </ul>

					<div class="view-area" :class="viewMode=='view'?'on':''">
						<div class="form-group" :class="errors.has('NAME') ? 'has-error' :''">
							<label class="col-sm-4 control-label"><spring:message code="admin.form.db.vname" /></label>
							<div class="col-sm-8">
								<input type="text" v-model="detailItem.vname" v-validate="'required'" name="NAME" class="form-control" />
								<div v-if="errors.has('NAME')" class="help-block">{{ errors.first('NAME') }}</div>
							</div>
						</div>

						<div class="form-group">
							<label class="col-sm-4 control-label">JDBC Provider</label>
							<div class="col-sm-8">
								<div v-if="jdbcProviderList.length > 0">
									<select class="form-control text required" id="vdriver" name="vdriver" v-model="detailItem.vdriver" @change="changeProvider($event)">
										<option value="" disabled="disabled">선택</option>
										<option v-for="(item,index) in jdbcProviderList" :value="item.driverProviderId" :data-driver="item.driverProviderId" selected="{{detailItem.vdriver==item.driverProviderId?true:(detailItem.vdriver==''&& index==0?true:false)}}">
											{{item.dbType}}({{item.providerName}})
										</option>
									</select>
									
									<div><a href="<c:url value="/admin/driverMgmt"/>"><spring:message code="jdbc.provider.shortcuts" /></a></div>
								</div>
								<div v-else>
									<a href="<c:url value="/admin/driverMgmt"/>"><spring:message code="jdbc.provider.msg" /></a>
								</div>
							</div>
						</div>
						
						<div class="form-group" v-if="selectJdbcProvider.versionList.length > 0">
							<label class="col-sm-4 control-label">Version</label>
							<div class="col-sm-8">
								<div>
									<select class="form-control text required" id="vdbversion" name="vdbversion" v-model="detailItem.vdbversion">
										<option value="" disabled="disabled">선택</option>
										<option v-for="(item,index) in selectJdbcProvider.versionList" :value="item.version" selected="{{detailItem.vdbversion==item.version?true:false}}">
											{{item.version}}
										</option>
									</select>
								</div>
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
									<div>ex : <span class="">{{jdbcUrlFormat[selectJdbcProvider.driverId]}}</span> </div>
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
								<input class="form-control text required" id="vid" name="vid" value="" autocomplete="off" v-model="detailItem.vid" placeholder="id">
							</div>
						</div>

						<div class="form-group" :class="errors.has('password') || errors.has('password_confirmation') ? 'has-error' :''">
							<label class="col-sm-4 control-label"><spring:message code="admin.form.db.vpw" /></label>
							<div class="col-sm-8">
								<template v-if="detailFlag===true">
									<input type="checkbox" v-model="detailItem.passwordChange" />
								</template>
								<template v-if="detailFlag===false || (detailFlag===true && detailItem.passwordChange===true)">
									<input type="password" name="password_fake" value="" style="display:none;" autocomplete="new-password"/>
									<input v-model="detailItem.vpw" v-validate="'confirmed:password_confirmation'" name="password" type="password" autocomplete="new-password" class="form-control" placeholder="Password" ref="password" data-vv-as="password_confirmation"  style="margin-bottom:5px;">
									<input v-model="detailItem.CONFIRM_PW" v-validate="" name="password_confirmation" type="password" class="form-control" autocomplete="false" placeholder="Password, Again" data-vv-as="password" ref="password_confirmation">
								    <div class="help-block" v-if="errors.has('password')">
								      {{ errors.first('password') }}
								    </div>
								    <div class="help-block" v-if="errors.has('password_confirmation')">
								      {{ errors.first('password_confirmation') }}
								    </div>
							    </template>
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
							<label class="col-sm-4 control-label"><spring:message code="admin.form.db.enableConnectionPool" /></label>
							<div class="col-sm-8">
								<label><input type="radio" name="enableConnectionPool" value="Y" v-model="detailItem.enableConnectionPool" checked>Y</label>
								<label><input type="radio" name="enableConnectionPool" value="N" v-model="detailItem.enableConnectionPool">N</label>
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
						<div class="form-group">
							<label class="col-sm-4 control-label"><spring:message code="admin.form.db.usecolumnlabel" /></label>
							<div class="col-sm-8">
								<label><input type="radio" name="useColumnLabel" value="Y" v-model="detailItem.useColumnLabel" checked>Y</label>
								<label><input type="radio" name="useColumnLabel" value="N" v-model="detailItem.useColumnLabel" >N</label>
							</div>
						</div>
					</div>
					<div class="view-area" :class="viewMode=='opt'?'on':''">
						
						<div class="form-group">
							<label class="col-sm-4 control-label"><spring:message code="admin.form.db.test_while_idle" /></label>
							<div class="col-sm-8">
								<label><input type="radio" name="testWhileIdle" value="Y" v-model="detailItem.testWhileIdle" >Y</label>
								<label><input type="radio" name="testWhileIdle" value="N" v-model="detailItem.testWhileIdle" checked>N</label>
							</div>
						</div>
						
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
					</div>
				</form>
			</div>
			<!-- /.panel-body -->
		</div>
		<!-- /.panel -->
	</div>
	<!-- /.col-lg-8 -->

	<div id="passwordViewTemplate" title="Password view">
		<div>
			<div>Login password : <input v-model="userPw" type="password" @keyup.enter="passwordView()" class="form-control text"> </div>
			<div style="padding: 5px 0px;" class="pull-right"><button type="button" @click="passwordView()" class="db btn btn-default">Password view</button> </div>
			<div style="clear:both;">Password : <input v-model="dbPw" type="text" class="form-control text" disabled="disabled" style="width: calc(100% - 70px);display: inline-block;"></div>
		</div>
	</div>
</div>
<!-- /.row -->

<style>

.status-circle {
    border-radius: 50%;
    display: inline-block
}

.status-circle.big {
    width: 12px;
    height: 12px
}

.status-circle.small {
    width: 8px;
    height: 8px
}

.status-circle.green {
    background-color: #4FCE67;
    border: 2px solid #4FCE67
}

.status-circle.orange {
    background-color: #F7A443;
    border: 2px solid #F7A443
}

.status-circle.red {
    background-color: #FF5858;
    border: 2px solid #FF5858
}
</style>


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
		,jdbcProviderList : []
		,dbPwViewItem :{}
		,userPw : ''
		,dbPw : ''
		,selectJdbcProvider : {versionList:[]}
		,jdbcUrlFormat : ${jdbcUrlFormat}
	}
	,methods:{
		init : function(){
			this.setDetailItem();

			$('#passwordViewTemplate').dialog({
				height: 210
				,width: 400
				,modal: true
				,autoOpen :false
				,close: function() {
					$( this ).dialog( "close" );
				}
			});

			this.getJdbcProvider();
		}
		,search : function(no){
			var _this = this;

			var param = {
				pageNo: (no?no:1)
				,rows: _this.list_count
				,'searchVal':_this.searchVal
			};

			this.$ajax({
				url : {type:VARSQL.uri.admin, url:'/databaseMgmt/dblist'}
				,data : param
				,success: function(resData) {
					var list = resData.list;
					
					list.forEach(item=>{
						if(item.status=='STOP'){
							item.statusStyle = 'red';
						}else if(item.status=='SHUTDOWN'){
							item.statusStyle = 'orange';
						}else{
							item.statusStyle = 'green';	
						}
					})
					
					_this.gridData = list;
					
					_this.pageInfo = resData.page;
				}
			})
		}
		,itemViewMode : function (mode){
			this.viewArea(mode);
		}
		,changeProvider : function (evt){
			this.setProviderInfo(evt.target.value)
		}
		,setProviderInfo : function (selectVal){
			this.selectJdbcProvider = {};
			
			for(var i =0 ;i < this.jdbcProviderList.length; i++){
				var item = this.jdbcProviderList[i];

				if(item.driverProviderId == selectVal){
					this.selectJdbcProvider = item;
					break; 
				}
			}
			var versionList = this.selectJdbcProvider.versionList ||[];
			
			if(versionList.length > 0){
				if(this.detailItem.vdbversion ==''){
					this.detailItem.vdbversion = versionList[versionList.length-1].version;
				}
			}
			
			this.selectJdbcProvider.versionList = versionList;
		}
		// 상세보기
		,itemView : function(item){
			var _this = this;

			var param = {
				vconnid : item.vconnid
			}

			if(_this.detailItem.vconnid == item.vconnid){
				return ;
			}

			_this.errors.clear();

			this.$ajax({
				url : {type:VARSQL.uri.admin, url:'/databaseMgmt/dbDetail'}
				,data : param
				,loadSelector : '.detail_area_wrapper'
				,success: function(resData) {
					var item  =resData.item;
					_this.setDetailItem(item);
				}
			})
		}
		,viewPasswordDialog : function (item){
			this.dbPwViewItem= item;
			this.userPw = '';
			this.dbPw = '';
			$('#passwordViewTemplate').dialog('open');
		}
		,passwordView : function (){
			this.userPw = VARSQL.str.trim(this.userPw);

			if(this.userPw == ''){
				VARSQLUI.toast.open(VARSQL.messageFormat('varsql.0023'));
				return ;
			}

			var _this=this;

			var param =  {
				vconnid : _this.dbPwViewItem.vconnid
				,userPw : this.userPw
			}

			_this.$ajax({
				url : {type:VARSQL.uri.admin, url:'/databaseMgmt/dbPwView'}
				,data : param
				,success:function (resData){
					if(resData.resultCode != 200){
						alert(resData.message);
						return ;
					}else{
						_this.dbPw = resData.item;
					}
				}
			});
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
					,vport: 0
					,vdriver: ""
					,vdbversion: ""
					,useYn: "Y"
					,urlDirectYn:'N'
					,vid: ""
					,vname: ""
					,vpoolopt: ""
					,vpw: ""
					,vserverip: ""
					,vurl: ""
					,basetableYn: 'Y'
					,lazyloadYn: 'N'
					,schemaViewYn: 'N'
					,useColumnLabel: 'Y'
					,testWhileIdle: 'N'
					,enableConnectionPool:'Y'
				}
			}else{
				this.detailFlag = true;
				item.passwordChange = false;
				this.detailItem = item;
			}

			this.setProviderInfo(this.detailItem.vdriver);

		}
		,save : function (mode){
			var _this = this;

			this.$validator.validateAll().then(function (result){
				if(result){
					var param = _this.getParamVal();

					if(param.passwordChange==true && !confirm(VARSQL.messageFormat('varsql.a.0004'))){
						return ;
					}

					_this.$ajax({
						url : {type:VARSQL.uri.admin, url:'/databaseMgmt/dbSave'}
						,data : param
						,success:function (resData){
							if(VARSQL.req.validationCheck(resData)){
								if(resData.resultCode != 200){
									alert(resData.message);
									return ;
								}else{
									VARSQLUI.toast.open(VARSQL.messageFormat('varsql.a.0005'));
								}
								_this.search();
								_this.setDetailItem();
							}
						}
					});
				}else{
					var errorItem = _this.errors.items[0];
					
					if(!$('[name="'+errorItem.field+'"]').closest('.view-area').hasClass('on')){
						alert(errorItem.msg);
						_this.viewMode = _this.viewMode=='opt' ?'view' : 'opt';
					}

					return  ;
				}
			});
		}
		,getParamVal : function (item){
			var param = {};
			var saveItem = item || this.detailItem;
			for(var key in saveItem){
				param[key] = saveItem[key];
			}
			return param;
		}
		// 정보 삭제 .
		,deleteInfo : function (){
			var _this = this;

			if(typeof this.detailItem.vconnid ==='undefined'){
				$('#warningMsgDiv').html(VARSQL.messageFormat('varsql.0004'));
				return ;
			}

			if(!confirm(VARSQL.messageFormat('varsql.0016'))){
				return ;
			}

			this.$ajax({
				url : {type:VARSQL.uri.admin, url:'/databaseMgmt/dbDelete'}
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
				url : {type:VARSQL.uri.admin, url:'/databaseMgmt/dbConnectionCheck'}
				,loadSelector : 'body'
				,data:param
				,success:function (resData){
					if(VARSQL.req.validationCheck(resData)){
						if(resData.resultCode == 200){
							VARSQLUI.toast.open(VARSQL.messageFormat('success'));
							return
						}else{
							alert(resData.messageCode  +'\n'+ resData.message);
						}
					}
				}
			});
		}
		,connectionClose : function (item){
			if(!confirm(VARSQL.messageFormat('varsql.a.0001'))){
				return ;
			}

			var _this = this;
			
			this.$ajax({
				url : {type:VARSQL.uri.admin, url:'/databaseMgmt/dbConnectionClose'}
				,loadSelector : 'body'
				,data : {
					vconnid : item.vconnid
				}
				,success:function (resData){
					if(VARSQL.req.validationCheck(resData)){
						if(resData.resultCode ==200){
							alert(VARSQL.messageFormat('varsql.a.0002'));
							_this.search();
							return
						}else{
							alert(resData.messageCode  +'\n'+ resData.message);
						}
					}
				}
			});
		}
		,copy : function (){
			var _this = this;
			var param = this.getParamVal();

			this.$ajax({
				url : {type:VARSQL.uri.admin, url:'/databaseMgmt/dbConnectionCopy'}
				,loadSelector : 'body'
				,data:param
				,success:function (resData){
					if(resData.resultCode ==200){
						VARSQLUI.toast.open(VARSQL.messageFormat('varsql.0027'));
						_this.search();

						_this.itemView({vconnid : resData.item});
						return
					}else{
						alert(resData.messageCode  +'\n'+ resData.message);
					}
				}
			});
		}
		,connectionReset : function (item){
			if(!confirm(VARSQL.messageFormat('varsql.a.0003'))){
				return ;
			}

			var _this = this;
			
			this.$ajax({
				url : {type:VARSQL.uri.admin, url:'/databaseMgmt/dbConnectionReset'}
				,loadSelector : 'body'
				,data : {
					vconnid : item.vconnid
				}
				,success:function (resData){
					if(VARSQL.req.validationCheck(resData)){
						if(resData.resultCode ==200){
							VARSQLUI.toast.open(VARSQL.messageFormat('success'));
							_this.search();
							return
						}else{
							alert(resData.messageCode  +'\n'+ resData.message);
						}
					}
				}
			});
		}
		// db driver list
		,getJdbcProvider : function (val){
			var _this = this;
			var param = {
				dbtype :val
			};

			this.$ajax({
				url : {type:VARSQL.uri.admin, url:'/databaseMgmt/jdbcProviderList'}
				,data : param
				,success:function (resData){

					var result = resData.list;

		    		if(result.length==0){
		    			_this.jdbcProviderList = [];
		    			return ;
		    		}

		    		_this.jdbcProviderList = result;

		    		return ;
				}
			});
		}
	}
});
</script>
