<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/include/tagLib.jspf"%>

<div class="user-connection-list-area">
	<label class="main-logo-area">
	    <img src="${pageContextPath}/webstatic/vt/vt32.png" class="user-main-logo">
	    <span>Connect : </span>
	</label>

	<select id="user_connection_info">
		<option value="">----connection info---</option>
		<c:forEach items="${dblist}" var="tmpInfo" varStatus="status">
			<option value="${tmpInfo.connUUID}" dbtype="${tmpInfo.type}" vname="${tmpInfo.name}">${tmpInfo.name}</option>
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
	        <a href="javascript:;" class="dropdown-toggle ui-note-btn" data-toggle="dropdown" >
	            <i class="fa fa-bell-o fa-fw"></i>
	            <span class="label label-warning alram-count">0</span>
	        </a>
	        <ul id="note_alert_area" class="dropdown-menu dropdown-alerts">
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
					<a href="<c:url value="/user/preferences" />" target="_blank" class="preferences"><i class="fa fa-fw fa-user"></i> <spring:message code="label.user.preferences"/></a>
				</li>
				<li class="divider"></li>
				<li>
					<a href="<c:url value="/logout" />"><i class="fa fa-fw fa-power-off"></i> <spring:message code="btn.logout"/></a>
				</li>
			</ul>
		</li>
	</ul>
</div>

<script>
(function() {

var userHeader = {
	noteDialog : false,
	init : function() {
		var _self = this;
		_self.initEvt();
		_self.messageLoad();
	},
	initEvt : function() {
		var _self = this;
		$('.ui-note-btn').on('click', function(e) {
			_self.messageLoad();
		})

		$('.main-logo-area').on('click', function(e) {
			_self.getConnectionInfo();
		})
	},
	messageLoad : function() {
		var _self = this;
		VARSQL.req.ajax({
			url : {
				type : VARSQL.uri.user,
				url : '/message'
			},
			data : {},
			success : function(res) {
				var items = res.items;
				var strHtm = [], len = items.length;

				if (len > 0) {
					$('.alram-count').addClass('on').html(len);
					for (var i = 0; i < len; i++) {
						var item = items[i];

						strHtm.push('<li>');
						strHtm.push('   <a href="javascript:;" class="note-item" _idx="'+i+'" title="'+item.noteTitle+'"><span class="note-text text-ellipsis"><i class="fa fa-envelope fa-fw"></i><span>'+item.noteTitle+'</span></span>');
						strHtm.push('   <span class="pull-right note-date">'+ item.regDt + '</span></a>');
						strHtm.push(' </li>');
					}
				} else {
					$('.alram-count').removeClass('on');
					strHtm.push('<li class="empty-area">no data</li>')
				}

				$('#note_alert_area').empty().html(strHtm.join(''));
				$('#note_alert_area .note-item').on('click',function() {
					var idx = $(this).attr('_idx');
					_self.noteDetail(items[idx]);
				})

			},
			error : function(data, status, err) {
				VARSQL.log.error(data, status, err);
			}
		});
	}
	,getConnectionInfo : function (){

		if(!confirm('커넥션 정보를 새로고침 하시겠습니까?')){
			return ;
		}
		VARSQL.req.ajax({
			url : {
				type : VARSQL.uri.user,
				url : '/connectionInfo'
			},
			data : {},
			success : function(res) {
				var strHtm = [];

				var items = res.items;

				strHtm.push('<option value="">----connection info---</option>');
				for(var i =0, len = items.length;i <len; i++){
					var item = items[i];
					strHtm.push('<option value="'+item.uuid+'" dbtype="'+item.type+'" vname="'+item.name+'">'+item.name+'</option>');
				}

				$('#user_connection_info').empty().html(strHtm.join(''));
			},
			error : function(data, status, err) {
				VARSQL.log.error(data, status, err);
			}
		});
	}
	,noteDetail : function(item) {
		var _self = this;

		$('#noteSendInfo').text(item.regInfo.viewName);
		$('#note_content').val(item.noteCont);

		if (item.updDt == null) {
			VARSQL.req.ajax({
				url : {
					type : VARSQL.uri.user,
					url : '/updMsgViewDt'
				},
				data : {
					noteId : item.noteId
				},
				success : function(res) {

				},
				error : function(data, status, err) {
					VARSQL.log.error(data, status, err);
				}
			});
		}
		if (_self.noteDialog === false) {
			_self.noteDialog = $('#noteTemplate_view_dialog').dialog({
				height : 350
				,width : 640
				,modal: true
				,close : function() {
					_self.noteDialog.dialog("close");
				}
			});
		}

		_self.noteDialog.dialog("option", "title", item.noteTitle + '('+ item.regDt + ')');
		_self.noteDialog.dialog("open");

	}
}

userHeader.init();

}());
</script>
