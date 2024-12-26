<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/include/tagLib.jspf"%>
<div id="setupContainer" class="container bg-faded">
	<h1 class="text-center">Varsql</h1>
	
	
	<%-- 관리자 계정 설정 --%>
	<div class="process-step" :class="!isInstall && currentStep != 4 ? 'active':'hide'">
	    <div class="row">
	        <div class="col-xs-12 text-center"><spring:message code="msg.welcome" /></div>
	    </div>
	    <hr>
	    <div class="row well">
	    	<div class="col-sm-12 col-centered" style="text-align:center;">
		        <button class="btn btn-default" @click="isInstall=true;"><h5 class="text-center"><spring:message code="install.start" /></h5></button>
		    </div>
	    </div>
	</div>
	
	<div :class="isInstall ||currentStep == 4 ? 'active':'hide'">
		<%-- 관리자 계정 설정 --%>
		<div class="process-step" :class="currentStep == 1 ? 'active':'hide'">
		    <div class="row">
		        <div class="col-xs-12 text-center"><spring:message code="admim.account.settings" /></div>
		    </div>
		    <hr>
		    <div class="row well">
		    	<div class="col-sm-8 col-centered">
			        <form id="accountForm" >
				    </form>
			    </div>
		    </div>
		</div>
	   
		<%-- app 설정 --%>
		<div class="process-step" :class="currentStep == 2 ? 'active':'hide'">
			<div class="row">
		        <div class="col-xs-12 text-center"><spring:message code="app.settings" /></div>
		    </div>
		    <hr>
		    <div class="row">
		        <form name="appForm" class="form-horizontal well" role="form">
			        <div class="form-group">
			            <label class="col-sm-3 control-label"><spring:message code="app.data.path"/></label>
			
			            <div class="col-sm-6 col-md-6">
			            	<input type="text" class="form-control" v-model="appInfo.dataPath" placeholder="<spring:message code="app.data.path"/>"/>
			            </div>
			        </div>
			        <div class="form-group">
			            <label class="col-sm-3 control-label"><spring:message code="charset"/></label>
			
			            <div class="col-sm-6 col-md-6">
			            	<select v-model="appInfo.charset" class="form-control select-charset">
								<option v-for="(item,index) in charsets" :value="item.code">{{item.name}}</option>
							</select>
			            </div>
			        </div>
			        <div class="form-group">
			            <label class="col-sm-3 control-label"><spring:message code="file.retention.period"/></label>
			
			            <div class="col-sm-6 col-md-6">
			                <input type="text" class="form-control" v-model="appInfo.fileRetentionPeriod" placeholder="<spring:message code="file.retention.period"/>"/>
			            </div>
			        </div>
			        <template v-if="dbTypes.length > 1">
				        <div class="form-group">
				            <label class="col-sm-3 control-label"><spring:message code="db.type"/></label>
				
				            <div class="col-sm-6 col-md-6">
								<select v-model="dbInfo.type" class="form-control">
									<option v-for="(item,index) in dbTypes" :value="item.code">{{item.name}}</option>
								</select>
				            </div>
				        </div>
				        
		       			<template v-if="dbInfo.type != 'H2'">
							<div class="form-group">
								<label class="col-sm-3 control-label"><spring:message code="db.vurl" /></label>
								<div class="col-sm-6 col-md-6">
									<input type="text" v-model="dbInfo.url" name="URL" class="form-control" />
									<div>ex : <span class=""></span> </div>
								</div>
							</div>
			
							<div class="form-group">
								<label class="col-sm-3 control-label"><spring:message code="db.vid" /></label>
								<div class="col-sm-6 col-md-6">
									<input class="form-control text required" value="" autocomplete="off" v-model="dbInfo.username" placeholder="id">
								</div>
							</div>
			
							<div class="form-group">
								<label class="col-sm-3 control-label"><spring:message code="db.vpw" /></label>
								<div class="col-sm-6 col-md-6">
									<input type="password" name="password_fake" value="" style="display:none;" autocomplete="new-password"/>
									<input v-model="dbInfo.pw" name="password" type="password" autocomplete="new-password" class="form-control" placeholder="Password" ref="password"  style="margin-bottom:5px;">
									<input v-model="dbInfo.confirmPw" type="password" class="form-control" autocomplete="false" placeholder="Password, Again" data-vv-as="password" ref="password_confirmation">
								</div>
							</div>
						</template>
					</template>
			    </form>
		    </div>
		</div>
		
		<%-- app 설정 --%>
		<div class="process-step" :class="currentStep == 3 ? 'active':'hide'">
			<div class="row">
		        <div class="col-xs-12 text-center"><spring:message code="test.db" text="Test DB" /></div>
		    </div>
		    <hr>
		    <div class="row">
		        <form name="appForm" class="form-horizontal well" role="form">
			        <div class="form-group">
			            <label class="col-sm-3 control-label">Test DB Create</label>
			
			            <div class="col-sm-6 col-md-6">
			            	<div class="col-sm-8" style="margin: 5px 0px;">
								<label><input type="radio" value="Y" v-model="appInfo.useTestDb" checked>Y</label>
								<label><input type="radio" value="N" v-model="appInfo.useTestDb">N</label>
							</div>
			            </div>
			        </div>
			    </form>
		    </div>
		</div>
	   
		<%-- 완료 --%>
		<div class="process-step" :class="currentStep == 4 ? 'active':'hide'">
			<div class="row">
		        <div class="col-xs-12 text-center"><spring:message code="app.settings" /></div>
		        
		        <h2 class="col-xs-12 text-center"><spring:message code="msg.install.end" /></h2>
		    </div>
	   </div>
	   
	   <div class="process-step" :class="currentStep == 5 ? 'active':'hide'">
			<div class="row">
		        <h2 class="col-xs-12 text-center">
		        	<spring:message code="msg.install.complete" />
		        </h2>
		        
		        <div class="col-xs-12 text-center">
		       		<h3>Application Restart</h3>
		        </div>
		    </div>
	   </div>
	   
	   <hr>
		    
	    <div class="row" :class="currentStep == 5 ? 'hide':''">
	    	 <div class="col-xs-12 text-center">
	    	 	<button v-if="currentStep > step" class="btn btn-default" @click="moveStep(-1)"><spring:message code="step.prev"/></button>
	    	 	<button v-if="endStep > currentStep" class="btn btn-default" @click="moveStep(1)"><spring:message code="step.next"/></button>
	    	 	<button v-if="endStep == currentStep" class="btn btn-default" @click="installStart()"><spring:message code="install"/></button>
	    	 </div>
	    </div>
	</div>
</div>

<style>
.col-centered{
    float: none;
    margin: 0 auto;
}
</style>

<script>

VarsqlAPP.vueServiceBean({
	el: '#setupContainer'
	,data: {
		isInstall: false
		, step : 1
		, currentStep: <c:out value="${isInstall?5:1}" escapeXml="true"/>
		, endStep : 4
		, charsets:[
			{name : 'Cp874',code:'Cp874'}
			,{name : 'EUC-CN',code:'EUC-CN'}
			,{name : 'EUC-JP',code:'EUC-JP'}
			,{name : 'EUC-KR',code:'EUC-KR'}
			,{name : 'EUC-TW',code:'EUC-TW'}
			,{name : 'GB2312',code:'GB2312'}
			,{name : 'GB18030',code:'GB18030'}
			,{name : 'GBK',code:'GBK'}
			,{name : 'ISO-8859-1',code:'ISO-8859-1'}
			,{name : 'ISO-8859-2',code:'ISO-8859-2'}
			,{name : 'ISO-8859-3',code:'ISO-8859-3'}
			,{name : 'ISO-8859-4',code:'ISO-8859-4'}
			,{name : 'ISO-8859-5',code:'ISO-8859-5'}
			,{name : 'ISO-8859-6',code:'ISO-8859-6'}
			,{name : 'ISO-8859-7',code:'ISO-8859-7'}
			,{name : 'ISO-8859-8',code:'ISO-8859-8'}
			,{name : 'ISO-8859-9',code:'ISO-8859-9'}
			,{name : 'ISO-8859-13',code:'ISO-8859-13'}
			,{name : 'ISO-8859-15',code:'ISO-8859-15'}
			,{name : 'ISO-8859-15',code:'ISO-8859-15'}
			,{name : 'KSC5601',code:'KSC5601'}
			,{name : 'MS874',code:'MS874'}
			,{name : 'MS932',code:'MS932'}
			,{name : 'MS936',code:'MS936'}
			,{name : 'MS949',code:'MS949'}
			,{name : 'MS950',code:'MS950'}
			,{name : 'MS950-HKSCS',code:'MS950-HKSCS'}
			,{name : 'US-ASCII',code:'US-ASCII'}
			,{name : 'UTF-8',code:'UTF-8'}
			,{name : 'UTF-16',code:'UTF-16'}
			,{name : 'UTF-16BE',code:'UTF-16BE'}
			,{name : 'UTF-16LE',code:'UTF-16LE'}
		]
		,dbTypes : [
			{name : 'H2',code:'H2'}
		]
		, userInfo : {
			id : 'admin'
			,pw : ''
			,confirmPw :''
			,name : '<spring:message code="admim.default.name"/>'
			,email : ''
		}
		,appInfo : {
			charset: 'UTF-8'
			,fileRetentionPeriod : 30
			,useTestDb: 'Y'
			,dataPath: '<c:out value="${installRoot}" escapeXml="true"/>'
		}
		,dbInfo : {
			type : 'H2'
			,url: ''
			,username : ''
			,pw : ''
			,confirmPw : ''
		}
		,form : false
	}
	,created : function (){
		<c:out value="${param.conuid}" escapeXml="true"></c:out>
	}
	,methods:{
		init : function (){
			this.form = new DaraForm(document.getElementById("accountForm"), {
				message : "This value is not valid",
				style : {
					position : 'left-right',
					labelWidth : '3'
				},
				message : {
					empty : VARSQL.message('msg.valid.required'),
					string : {
						minLength : VARSQL.message('msg.valid.string.min'),
						maxLength : VARSQL.message('msg.valid.string.max'),
					},
					number : {
						minimum : VARSQL.message('msg.valid.number.min'),
						miximum : VARSQL.message('msg.valid.number.max'),
					},
					regexp : {
						email : VARSQL.message('msg.valid.regexp.email'),
						url : VARSQL.message('msg.valid.regexp.url'),
					},
				},
				fields : [ 
					{ name : "id", label : VARSQL.message('user.id'), renderType : "text", required :true}
					, {	name : "name", label : VARSQL.message('user.name'), renderType : "text", required :true}
					, { name : "email", label : VARSQL.message('email'), renderType : "text", required :true, regexpType : "email",}
					, {	label : VARSQL.message('user.password'), orientation : 'vertical' , children : [ 
						{name : 'pw', label : 'Passowrd', required :true, renderType : 'password', regexpType : 'number-char'
							, style : {
								labelHide : true
							},
							rule : {
								minLength : 3
							},
							different : {
								field : 'id',
								message : VARSQL.message('msg.valid.id.diffrent')
							}
						}
						, { name : 'confirmPw', label : 'Confirm password', required :true, renderType : 'password'
							, style : {
								labelHide : true
							},
							identical : {
								field : 'pw',
								message : VARSQL.message('msg.valid.password.identical')
							}
						} 
					]
				} ]
			});
			
		}
		//step 선택
		,moveStep : function (step){
			
			var currentStep = this.currentStep;
			
			currentStep += step;
			
			if(currentStep < this.step || this.endStep < currentStep){
				return ; 
			}
			
			if(currentStep == 2){
				if(!this.form.isValidForm()){
					return ;
				}
			}
			
			this.currentStep = currentStep ;
		}
		// 완료
		,installStart : function (){
			var _self = this;
			
			var param = {
				userInfo : this.form.getValue()
				,appInfo : this.appInfo
				,dbInfo :  this.dbInfo
			};
			
			VARSQL.req.ajax({
				url : '/setup/install'
				,ignoreUid : true
				,data: JSON.stringify( param)
				,loadSelector : '#setupContainer'
				,success:function (resData){
					if(resData.resultCode != 200){
						alert(resData.message);
						return; 
					}
					_self.currentStep = _self.endStep+1; 
				}
			});
		}
	}
});

</script>