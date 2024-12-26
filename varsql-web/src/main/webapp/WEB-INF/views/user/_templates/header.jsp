<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/include/tagLib.jspf"%>

<div id="userTopArea" class="display-off">
	<div class="user-connection-list-area">
		<label @click="getConnectionInfo()" class="main-logo-area" title="<spring:message code="msg.refresh.connnection.info"/>">
		    <img src="${pageContextPath}/webstatic/vt/vt32.png" class="user-main-logo">
		    <span>Connect : </span>
		</label>

		<select id="user_connection_info">
			<option value="">----connection info---</option>
			<c:forEach items="${dblist}" var="tmpInfo" varStatus="status">
				<option value="${tmpInfo.key}">${tmpInfo.value.name}</option>
			</c:forEach>
		</select>
	</div>

	<div class="connection-tab-area">
		<div id="connection_tab">
		</div>
	</div>

	<div class="user-right-menu-area">
		<!-- Top Menu Items -->
		<ul class="user-right-menu">
			<li class="dropdown">
		        <a href="javascript:;"  @click="messageLoad()" class="ui-note-btn" :class="alarmFlag ? 'on' :''">
		            <i class="fa fa-fw" :class="alarmFlag ? 'fa-bell' :'fa-bell-o'"></i>
		        </a>
		        <ul class="alarm-msg-area" @click="messageLoad()" :class="alarmMsgAeraCls">
		        	<li class="text-center"><spring:message code="msg.note.arrived"/></li>
	       		</ul>
		    </li>
			<li class="dropdown">
				<a href="#" class="dropdown-toggle user-profile text-ellipsis"	data-toggle="dropdown">
					<sec:authentication	property="principal.fullname" /> <b class="caret"></b>
				</a>
				<ul class="dropdown-menu">

			        <jsp:include page="/WEB-INF/include/screen.jsp" flush="false">
						<jsp:param name="popup_yn" value="y" />
					</jsp:include>

					<li>
						<a href="<c:url value="/user/preferences" />" target="_blank" class="preferences"><i class="fa fa-fw fa-user"></i> <spring:message code="user.preferences"/></a>
					</li>
					<li class="divider"></li>
					<li>
						<a href="${varsqlfn:logoutUrl(pageContext.request)}"><i class="fa fa-fw fa-power-off"></i> <spring:message code="btn.logout"/></a>
					</li>
				</ul>
			</li>
		</ul>
	</div>

	<div id="noteTemplate_view_dialog" style="display:none;">
		<div class="note-view-area">
		    <div class="col-xs-5 note-list-area">
		    	<div class="note-list-tab">
		    		<ul class="nav nav-tabs">
					  <li :class="tabInfo =='new' ?'active' :''"><a href="javascript:;" @click="tabView('new')"><spring:message code="new.note" text="신규 쪽지"/></a></li>
					  <li :class="tabInfo =='old' ?'active' :''"><a href="javascript:;" @click="tabView('old')"><spring:message code="note.box" text="쪽지함"/></a></li>
					</ul>
					<div class="pull-right"><a href="javascript:;" @click="moreNote()"><i class="fa fa-plus"></i><spring:message code="more" text="더보기"/></a></div>
		    	</div>
		    	<div style="clear:both;"></div>
		    	<div class="note-list">
			    	<ul id="userNoteDataList" class="">
			    		<li v-for="(item,index) in noteItems">
							<a href="javascript:;" @click="noteDetail(item ,index)" class="note-item" :class="item.noteId == detailItem.noteId ? 'active' :''" :title="item.noteTitle"><span class="note-text text-ellipsis"><i class="fa fa-envelope fa-fw"></i>{{item.noteTitle}}</span>
							<span class="note-date">{{item.regDt}}</span></a>
						</li>
						<li v-if="noteItems.length === 0">
							<div class="text-center"><spring:message code="msg.nodata"/></div>
						</li>
			    	</ul>
		    	</div>
		    </div>
		    <div class="col-xs-7 h100">
		        <div style="height: 50%;" :style="tabInfo=='old'?'height:100%':'height:50%'">
		        	<div style="padding: 0px 0px 5px;">
			        	<div style="width: calc(50% - 5px);display: inline-block;"><spring:message code="send.user" text="보낸사람"/> : {{detailItem.regInfo.uname}} </div>
			        </div>
		        	<textarea style="height: calc(100% - 21px);" :value="detailItem.noteCont" disabled="disabled"></textarea>
		        </div>
		        <div style="height:50%;" v-if="tabInfo != 'old'">
		        	<div style="padding: 5px 0px;">
			        	<div style="width: calc(50% - 5px);display: inline-block;"><spring:message code="content" text="내용"/> </div>
			        	<span style="width: 50%;text-align: right;display: inline-block;"><button type="button" :disabled="tabInfo=='old'?true:false" @click="resendNote()"><spring:message code="reply" text="답장"/></button></span>
			        </div>
		        	<textarea v-model="detailItem.reNoteCont" style="height: calc(100% - 34px);"></textarea>
		        </div>
		    </div>
		</div>
	</div>
</div>
<script>
(function() {

var userTopObj = VarsqlAPP.vueServiceBean( {
	el: '#userTopArea'
	,data: {
		noteDialog : false
		,noteItems :[]
		,newNoteItems : []
		,pastNoteItems : false
		,allNoteIds :[]
		,detailItem : {}
		,alarmFlag :false
		,tabInfo :'new'
		,alarmTimer :''
		,alarmMsgAeraCls : ''
	}
	,created : function (){
		this.setDetailItem();
	}
	,watch : {
		alarmFlag : function (){
			var _this  = this;
			if(this.alarmFlag == true){
				this.alarmMsgAeraCls = 'ani-fade-in';
				clearTimeout(this.alarmTimer);
				this.alarmTimer = setTimeout(function() {
					if(_this.alarmMsgAeraCls != ''){
						_this.alarmMsgAeraCls = 'ani-fade-out';
					}
				}, 3000);

			}else{
				_this.alarmMsgAeraCls = '';
			}
		}
	}
	,methods:{
		init : function() {
			this.messageLoad(true);
		}
		,messageLoad : function(initFlag) {
			var _this = this;

			_this.alarmMsgAeraCls = '';
			this.tabInfo = 'new';
			_this.allNoteIds = [];
			this.$ajax({
				url : {type : VARSQL.uri.user, url : '/message'},
				data :{
					messageType : 'new'
				},
				success : function(res) {
					var items = res.list;
					var len = items.length;

					_this.noteItems = items;
					_this.newNoteItems = items;

					_this.alarmFlag = len > 0?true :false;

					if (_this.alarmFlag) {
						for(var i =0; i<len; i++){
							_this.allNoteIds.push(items[i].noteId)
						}
					}

					if(!initFlag){
						_this.noteOpen();
					}
				},
				error : function(data, status, err) {
					VARSQL.log.error(data, status, err);
				}
			});
		}
		// more 더보기
		,moreNote : function(){
			VARSQLUI.popup.open(VARSQL.getContextPathUrl('/user/preferences/message'));
		}
		// note tab click
		,tabView : function (tabInfo){

			if(this.tabInfo == tabInfo) return ;

			this.setDetailItem();

			this.tabInfo = tabInfo;

			if(tabInfo =='old'){
				if(this.pastNoteItems == false){
					this.searchNoteList();
				}else{
					this.noteItems = this.pastNoteItems;
				}
			}else{
				this.noteItems = this.newNoteItems;
			}
		}
		,getConnectionInfo : function (msgFlag){

			if(msgFlag !== false){
				if(!VARSQL.confirmMessage('msg.refresh.connnection.info.confirm')){
					return ;
				}
			}

			VARSQL.req.ajax({
				url : {type : VARSQL.uri.user, url : '/connectionInfo'},
				data : {},
				success : function(res) {
					var strHtm = [];

					var items = res.list;

					strHtm.push('<option value="">----connection info---</option>');
					for(var i =0, len = items.length;i <len; i++){
						var item = items[i];
						strHtm.push('<option value="'+item.uuid+'">'+item.name+'</option>');
					}

					$('#user_connection_info').empty().html(strHtm.join(''));
				},
				error : function(data, status, err) {
					VARSQL.log.error(data, status, err);
				}
			});
		}
		//set detail item
		,setDetailItem : function (item){
			if(VARSQL.isUndefined(item)){
				this.detailItem = {regInfo:{}};
			}else{
				this.detailItem = item;
			}
		}
		// note view
		,noteDetail : function(item, idx) {
			var _this = this;

			_this.setDetailItem(item);

			if(_this.tabInfo =='old'){
				return ;
			}

			if(VARSQL.isUndefined(item.reNoteCont)){
				item.reNoteCont ='';
			}

			_this.allNoteIds.splice(VARSQL.inArray(_this.allNoteIds,item.noteId),1);

			_this.alarmFlag = _this.allNoteIds.length > 0?true :false;

			if (item.updDt == null) {
				item.updDt = new Date();
				VARSQL.req.ajax({
					url : {type : VARSQL.uri.user,url : '/updMsgViewDt'},
					data : {noteId : item.noteId},
					success : function(res) {

					},
					error : function(data, status, err) {
						VARSQL.log.error(data, status, err);
					}
				});
			}
		}
		// dialog open
		,noteOpen : function (){
			var _this = this;

			if (_this.noteDialog === false) {
				_this.noteDialog = $('#noteTemplate_view_dialog').dialog({
					height : 450
					,width : 730
					,modal: true
					,close : function() {
						_this.noteDialog.dialog("close");
					}
				});
			}

			this.setDetailItem();

			_this.noteDialog.dialog("option", "title", "Note");
			_this.noteDialog.dialog("open");
		}
		,resendNote  : function (){
			var _this =this;

			if(VARSQL.isBlank(this.detailItem.noteId)){
				VARSQL.toastMessage('msg.item.select');
				return ;
			}else if(VARSQL.isBlank(this.detailItem.reNoteCont)){
				VARSQL.toastMessage('msg.content.enter.param',VARSQL.message('content'));
				return ;
			}

			if(!VARSQL.confirmMessage('msg.send.confirm')){
				return ;
			}

			var params = VARSQL.util.copyObject(this.detailItem);

			params.recvId = 'resend';

			this.$ajax({
			    url:{type:VARSQL.uri.user, url:'/resendNote'}
			    ,data:params
			    ,success:function (resData){
			    	VARSQL.toastMessage('msg.save.success');
				}
			});
		}
		,searchNoteList : function (){
			var _this = this;

			var param = {
				pageNo : 1
				,messageType : 'recv'
				,rows: 20
				,'searchVal':''
			};

			this.$ajax({
				url : {type:VARSQL.uri.user, url:'/message'}
				,data : param
				,success: function(resData) {
					_this.noteItems = resData.list;
					_this.pastNoteItems = resData.list;
				}
			})
		}
	}
});

function userConnect(){
	VARSQL.socket.connect('topic', {uid : $varsqlConfig.viewId}, function (data){
		var msgType = data.type;

		if(msgType== 'NOTE'){
			userTopObj.alarmFlag =true;
		}else if(msgType == 'USER_BLOCK'){
			location.href=VARSQL.getContextPathUrl("/logout?viewPage=/error/blockingUser");
		}else if(msgType == 'USER_DB_BLOCK'){
			userMain.blockTab(data.item);
			userTopObj.getConnectionInfo(false);
		}
	});	
}
userConnect();

window.mainSocketConnect = function (){
	if(VARSQL.socket.isConnect() ===false){
		userConnect();
	};
}; 

}());

</script>
