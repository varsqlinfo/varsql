<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/include/tagLib.jspf"%>

<!-- Page Heading -->
<div class="row">
    <div class="col-lg-12">
        <h1 class="page-header"><spring:message code="manager.menu.boardmgmt" /></h1>
    </div>
    <!-- /.col-lg-12 -->
</div>

<div class="row display-off" id="varsqlVueArea" style="height:calc(100% - 76px);">
	<div class="col-xs-4">
		<div>
			<div class="margin-bottom5">
				<label>DB</label>
				<div>
					<select class="form-control" v-model="boardCode" @change="search(1)">
						<option value="all"><spring:message code="all" text="전체"/></option>
						<option v-for="(item,index) in dbList" :value="item.vconnid">{{item.vname}}</option>
					</select>
				</div>
			</div>
								
			<div class="margin-bottom5">
				<div class="input-group">
					<input class="form-control" v-model="searchVal" placeholder="<spring:message code="search.placeholder" />" @keyup.enter="search()">
					<span class="input-group-btn"> <button class="btn btn-default search-btn" type="button" @click="search()"><spring:message code="search" text="검색"/></button>
					</span>
				</div>
			</div>
			<div class="margin-bottom5" style="float:right;">
				<button type="button" @click="viewWritePage()" class="btn btn-default"><spring:message code="write" text="작성"/></button>
			</div>
			<div style="clear:both;"></div>
		</div>
		<ul class="list-group">
			<li v-for="(item,index) in gridData" class="list-group-item">
				<div :title="item.title">
					<a href="javascript:;" class="text-ellipsis" @click="viewItem(item)">
						<span v-if="item.noticeYn=='Y'" style="font-weight:bold;">[<spring:message code="notice" text="공지사항"/>] </span>{{item.title}}
					</a>
				</div>
				<div class="text-ellipsis">
					{{item.authorName}} {{item.regDt}}
					<template v-if="item.commentCnt>0">
						<a href="javascript:;" @click="commentViewItem(item)"><i class="fa fa-comment"></i>({{item.commentCnt}})</a>
					</template>
				</div>
				<div class="pull-right">
					<button type="button" @click="deleteItem(item)" class="btn btn-xs btn-warning"><spring:message code="delete" text="삭제"/></button>
				</div>
			</li>
			<li class="list-group-item" v-if="gridData.length === 0">
				<div class="text-center"><spring:message code="msg.nodata" text=">데이터가 없습니다."/></div>
			</li>
		</ul>
		<div>
			<page-navigation :page-info="pageInfo" callback="search"></page-navigation>
		</div>
	</div>
	<div class="col-xs-8 h100">
		<iframe id="contentViewArea" :src="contentViewUrl" style="height: 99%;width:100%;min-height:760px;border:0px;" ></iframe>
	</div>
</div>
<!-- /.row -->

<style>
.list-group-item{
	padding: 3px 10px 3px 10px;
}
.list-group-item .text-center{
	height: 60px;
    padding: 20px;
}

</style>

<script>
(function (){
	const boardMgmtBean = VarsqlAPP.vueServiceBean({
		el: '#varsqlVueArea'
		,data: {
			list_count :10
			,dbList : ${varsqlfn:objectToJson(dbList)}
			,searchVal : ''
			,pageInfo : {}
			,gridData :  []
			,boardCode : 'all'
			,contentViewUrl:'about:blank;'
		}
		,methods:{
			init : function (){
				var _this =this;
				
				this.viewUrl(VARSQL.url(VARSQL.uri.manager,'/boardMgmt/select'));

			}
			,viewUrl: function (url){
				document.getElementById('contentViewArea').src = url;
			}
			,viewWritePage:function (){
				
				if(this.boardCode == 'all'){
					VARSQL.toastMessage('msg.select.param','DB');
					return ; 
				}
				
				this.viewUrl(VARSQL.url(VARSQL.uri.manager,'/boardMgmt/write?boardCode=#boardCode#',{boardCode:this.boardCode}));
				
			}
			,viewItem : function(item){
				this.viewUrl(VARSQL.url(VARSQL.uri.manager,'/boardMgmt/view?boardCode=#boardCode#&articleId=#articleId#',{boardCode:item.boardCode,articleId:item.articleId}));
			}
			,commentViewItem : function(item){
				this.viewUrl(VARSQL.url(VARSQL.uri.manager,'/boardMgmt/view?boardCode=#boardCode#&articleId=#articleId#',{boardCode:item.boardCode,articleId:item.articleId})+"#comments");
			}
			,deleteItem : function(item){

				if(!VARSQL.confirmMessage('msg.brd.article.delete','게시글이 삭제되면 복구할 수 없습니다. 그래도 삭제하시겠습니까?')) return ;

				const param = {
					'articleId' : item.articleId
					,boardCode : item.boardCode
				};
				
				this.$ajax({
					url: {type:VARSQL.uri.manager, url:'/boardMgmt/delete'}
					,method :'delete'
					,data: param
					,success: (resData) => {
						VARSQL.toastMessage('msg.delete.success');
						this.search(1);						
					}
				})
			}
			,search : function(no){

				const param = {
					pageNo:no?no:1
					,rows: this.list_count
					,boardCode: this.boardCode
					,searchVal:this.searchVal
				};

				this.$ajax({
					url:  {type:VARSQL.uri.manager, url:'/boardMgmt/list'}
					,data: param
					,loadSelector:'#varsqlVueArea'
					,success: (resData) =>{
						this.gridData = resData.list;
						this.pageInfo = resData.page;
					}
				})
			}
		}
	});
	
	window.boardMainSearch =(disableSelectView)=>{
		boardMgmtBean.search();
		if(disableSelectView !== true){
			boardMgmtBean.viewUrl(VARSQL.url(VARSQL.uri.manager,'/boardMgmt/select'));
		}
	}
})();

</script>