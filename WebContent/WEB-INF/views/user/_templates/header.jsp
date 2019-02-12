<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/include/tagLib.jspf"%>
<!-- Brand and toggle get grouped for better mobile display -->


	<div class="user-connection-list-area">
		<label>Connect to : </label> 
		
		<select id="user_connection_info">
			<option value="">----connection info---</option>
			<c:forEach items="${dblist}" var="tmpInfo" varStatus="status">
				<option value="${tmpInfo.connUUID}" dbtype="${tmpInfo.type}" vname="${tmpInfo.name}">${tmpInfo.name}</option>
			</c:forEach>
		</select>
	</div>
	
	<div class="connection-tab-area">
		<ul class="connection-tab" id="user_connection_info_tab">
		</ul>
	</div>
	
	<div class="user-right-menu-area">
		<!-- Top Menu Items -->
		<ul class="user-right-menu">
			<li class="dropdown">
		        <a href="javascript:;" class="dropdown-toggle ui-memo-btn" data-toggle="dropdown" >
		            <i class="fa fa-bell-o fa-fw"></i>
		            <span class="label label-warning alram-count">0</span>
		        </a>
		        <ul id="memo_alert_area" class="dropdown-menu dropdown-alerts">
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
						<a href="javascript:;" class="preferences"><i class="fa fa-fw fa-user"></i> <spring:message code="label.user.preferences"/></a>
					</li>
					<li class="divider"></li>
					<li>
						<a href="<c:url value="/logout" />"><i class="fa fa-fw fa-power-off"></i> <spring:message code="btn.logout"/></a>
					</li>
				</ul>
			</li>
		</ul>
	</div>
</div>
 
<script>
	var userHeader = {
		memoDialog : false,
		init : function() {
			var _self = this;
			_self.initEvt();
			_self.messageLoad();
		},
		initEvt : function() {
			var _self = this;
			$('.ui-memo-btn').on('click', function(e) {
				_self.messageLoad();
			})
			$('.preferences').on('click', function(e) {
				userMain.tabCntl({
					id : 'preferences'
					,name : '환경설정'
					,url : '<c:url value="/user/preferences?header=N" />'
				});
			})
		},
		messageLoad : function() {
			var _self = this;
			VARSQL.req.ajax({
				type : "POST",
				url : {
					type : VARSQL.uri.user,
					url : '/message.vsql'
				},
				dataType : 'json',
				data : {},
				success : function(res) {
					var items = res.items;
					var paging = res.paging;
					var strHtm = [], len = items.length;
					if(len > 0){
						$('.alram-count').addClass('on').html(len);
					}else{
						$('.alram-count').removeClass('on');
					}
					if (len > 0) {
						for (var i = 0; i < len; i++) {
							var item = items[i];

							strHtm.push('<li>');
							strHtm.push('   <a href="javascript:;" class="memo-item" _idx="'+i+'" title="'+item.MEMO_TITLE+'"><span class="memo-text text-ellipsis"><i class="fa fa-envelope fa-fw"></i><span>'+item.MEMO_TITLE+'</span></span>');
							strHtm.push('   <span class="pull-right memo-date">'+ item.REG_DT + '</span></a>');
							strHtm.push(' </li>');
						}
					} else {
						strHtm.push('<li class="empty-area">no data</li>')
					}

					$('#memo_alert_area').empty().html(strHtm.join(''));
					$('#memo_alert_area .memo-item').on('click',
							function() {
								var idx = $(this).attr('_idx');
								_self.memoDetail(items[idx]);
							})

				},
				error : function(data, status, err) {
					VARSQL.log.error(data, status, err);
				}
			});
		},
		memoDetail : function(item) {
			var _self = this;

			$('#memo_content').val(item.MEMO_CONT);

			if (item.UPD_DT == null) {
				VARSQL.req.ajax({
					type : "POST",
					url : {
						type : VARSQL.uri.user,
						url : '/updMsgViewDt.vsql'
					},
					dataType : 'json',
					data : {
						memo_id : item.MEMO_ID
					},
					success : function(res) {

					},
					error : function(data, status, err) {
						VARSQL.log.error(data, status, err);
					}
				});
			}
			if (_self.memoDialog === false) {
				_self.memoDialog = $('#memoTemplate_view_dialog').dialog({
					height : 350
					,width : 640
					,modal: true
					,buttons : {
						"닫기" : function() {
							_self.memoDialog.dialog("close");
						}
					}
					,close : function() {
						_self.memoDialog.dialog("close");
					}
				});
			}

			_self.memoDialog.dialog("option", "title", item.MEMO_TITLE + '('
					+ item.REG_DT + ')');
			_self.memoDialog.dialog("open");

		}
	}

	$(document).ready(function() {
		userHeader.init();
	})
</script>