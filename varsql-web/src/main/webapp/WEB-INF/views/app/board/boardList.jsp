<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/include/tagLib.jspf"%>

<div class="display-off" id="varsqlViewArea">
	<h1 style="width: 145px;display: inline-block;">
		<a href="">리스트</a>
	</h1>
	<a href="#" class="fa fa-edit" target="_blank">새창보기</a>
	<div></div>
	<div>
		<div class="form-group" style="width:300px;float:left;">
			<div class="input-group">
				<input class="form-control" v-model="searchVal" placeholder="Search..." @keyup.enter="search()">
				<span class="input-group-btn"> <button class="btn btn-default search-btn" type="button" @click="search()">조회</button>
				</span>
			</div>
		</div>
		<div style="float:right;">
			<a href="<varsql-app:boardUrl addUrl="write"/>" class="btn btn-default"> 새 글쓰기</a>
		</div>
		<div style="clear:both;"></div>
	</div>
	<ul class="list-group">
		<li v-for="(item,index) in gridData" class="list-group-item">
			<div :title="item.title">
				<a href="javascript:;" class="text-ellipsis" @click="viewItem(item)"> {{item.title}} </a>
			</div>
			<div class="text-ellipsis" :title="item.uri">{{item.authorName}} {{item.regDt}}</div>
		</li>
		<li class="list-group-item" v-if="gridData.length === 0">
			<div class="text-center">등록된 게시물이 없습니다.</div>
		</li>
	</ul>
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
		,itemObj:{}
		,pageInfo : {}
		,gridData :  []
		,detailItem :{}
		,detailFlag : false
		,logElement :{}
		,xmlEditor :{}
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
				page:no?no:1
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