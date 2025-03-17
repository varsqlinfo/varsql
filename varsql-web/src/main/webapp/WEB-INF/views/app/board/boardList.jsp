<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/include/tagLib.jspf"%>

<div class="display-off" id="varsqlViewArea">
	<h1 style="width: 145px;display: inline-block;">
		<a href=""><spring:message code="list" text="목록"/></a>
	</h1>
	<a href="#" class="fa fa-edit" target="_blank"><spring:message code="newwin.view" text="새창보기"/></a>
	<div></div>
	<div>
		<div class="form-group" style="width:300px;float:left;">
			<div class="input-group">
				<input class="form-control" v-model="searchVal" placeholder="<spring:message code="search.placeholder" />" @keyup.enter="search()">
				<span class="input-group-btn"> <button class="btn btn-default search-btn" type="button" @click="search()"><spring:message code="search" text="검색"/></button>
				</span>
			</div>
		</div>
		<div style="float:right;">
			<a href="<varsql-app:boardUrl addUrl="write"/>" class="btn btn-default"><spring:message code="write" text="작성"/></a>
		</div>
		<div style="clear:both;"></div>
	</div>
	<div class="list-group">
		<a href="javascript:;" v-for="(item,index) in gridData" class="list-group-item" @click="viewItem(item)">
			<div :title="item.title">
				<span class="text-ellipsis">
					<span v-if="item.noticeYn=='Y'" style="font-weight:bold;">[<spring:message code="notice" text="공지사항"/>] </span>{{item.title}}
				</span>
			</div>
			<div class="text-ellipsis" >{{item.authorName}} {{item.regDt}} <template v-if="item.commentCnt>0"><i class="fa fa-comment"></i>({{item.commentCnt}})</template></div>
		</a>
		<a class="list-group-item" v-if="gridData.length === 0">
			<div class="text-center"><spring:message code="msg.nodata" text=">데이터가 없습니다."/></div>
		</a>
	</div>
	<div>
		<page-navigation :page-info="pageInfo" callback="search"></page-navigation>
	</div>
</div>

<script>

VarsqlAPP.vueServiceBean({
	el: '#varsqlViewArea'
	,data: {
		list_count :10
		,searchVal : ''
		,pageInfo : {}
		,gridData :  []
	}
	,methods:{
		init : function (){
			var _this =this;

		}
		,viewItem : function(item){
			location.href='<varsql-app:boardUrl addUrl="view"/>?articleId='+item.articleId;
		}
		,search : function(no){
			var _this = this;

			var param = {
				pageNo:no?no:1
				,rows: _this.list_count
				,'searchVal':_this.searchVal
			};

			this.$ajax({
				url: '<varsql-app:boardUrl addUrl="list" contextPath="false"/>'
				,data: param
				,success: function(resData) {
					var items = resData.list;

					_this.gridData = resData.list;
					_this.pageInfo = resData.page;
				}
			})
		}
	}
});
</script>