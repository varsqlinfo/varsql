<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/include/tagLib.jspf"%>
<div id="setupContainer" class="container bg-faded">
	<h1 class="text-center">Varsql</h1>
	
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
		            <label for="inputEmail3" class="col-sm-3 control-label"><spring:message code="charset"/></label>
		
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
							<select v-model="appInfo.dbType" class="form-control">
								<option v-for="(item,index) in dbTypes" :value="item.code">{{item.name}}</option>
							</select>
			            </div>
			        </div>
			        
	       			<template v-if="appInfo.dbType != 'H2'">
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
								<input class="form-control text required" value="" autocomplete="off" v-model="dbInfo.id" placeholder="id">
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
   
	<%-- 완료 --%>
	<div class="process-step" :class="currentStep == 3 ? 'active':'hide'">
		<div class="row">
	        <div class="col-xs-12 text-center"><spring:message code="app.settings" /></div>
	    </div>
   </div>
   
   <hr>
	    
    <div class="row">
    	 <div class="col-xs-12 text-center">
    	 	<button v-if="currentStep > step" class="btn btn-default" @click="moveStep(-1)"><spring:message code="step.prev"/></button>
    	 	<button v-if="endStep > currentStep" class="btn btn-default" @click="moveStep(1)"><spring:message code="step.next"/></button>
    	 	<button v-if="endStep == currentStep" class="btn btn-default" @click="complete()"><spring:message code="step.complete"/></button>
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
		 step : 1
		, currentStep: 1
		, endStep : 3
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
			,dbType : 'H2'
		}
		,dbInfo : {
			url: 'UTF-8'
			,id : 30
			,pw : 'H2'
			,confirmPw : 'H2'
		}
	}
	,mounted : function (){
		
	}
	,methods:{
		init : function (){
			var form = new DaraForm("#accountForm", {
				message : "This value is not valid",
				style : {
					position : 'left-right',
					labelWidth : '3'
				},
				message : {
					empty : "{name} 필수 입력사항입니다.",
					string : {
						minLength : "{size} 글자 이상 입력해야합니다.",
						maxLength : "{size} 글자 이상 입력할 수 없습니다.",
					},
					number : {
						minimum : "{size} 보다 커야 합니다",
						miximum : "{size} 보다 커야 합니다",
					},
					regexp : {
						email : "이메일이 유효하지 않습니다.",
						url : "URL이 유효하지 않습니다.",
					},
				},
				fields : [ 
					{ name : "id", label : VARSQL.message('user.id'), renderType : "text", required :true}
					, {	name : "name", label : VARSQL.message('user.name'), renderType : "text", required :true}
					, { name : "email", label : VARSQL.message('email'), renderType : "text", required :true, regexpType : "email",}
					, {	label : VARSQL.message('user.pw'), orientation : 'vertical' , children : [ 
						{name : 'pw', label : 'Passowrd',	required :true, renderType : 'password', regexpType : 'number-char'
							, style : {
								labelHide : true
							},
							rule : {
								minLength : 3
							},
							different : {
								field : 'id',
								message : '아이디와 달라야 합니다.'
							}
						}
						, { name : 'confirmPw', label : 'Confirm password', required :true, renderType : 'password'
							, style : {
								labelHide : true
							},
							identical : {
								field : 'pw',
								message : '비밀번호가 같아야합니다.'
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
				if(!DaraForm.instance('#accountForm').isValidForm()){
					return ;
				}
			}
			
			this.currentStep = currentStep ;
		}
		,userInfoValidation : function (){
			if(VARSQL.isBlank(this.userInfo.id)){
				VARSQL.toastMessage('msg.item.select', VARSQL.message('delete'));
				return ; 
			}
		}
		
		// 완료
		,complete : function (){
			var _self = this;

			var param = {
				userInfo : this.userInfo
			};
			
			console.log(param)
			
			return; 

			VARSQL.req.ajax({
				url : 'setup'
				,data: param
				,loadSelector : '.table-select-area'
				,success:function (resData){
					var list = resData.list;

					
				}
			});
		}
	}
});

</script>