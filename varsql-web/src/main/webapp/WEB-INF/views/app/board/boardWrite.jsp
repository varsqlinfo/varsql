<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/include/tagLib.jspf"%>
<div class="display-off" id="vueArea">
	<h1>
		<spring:message code="writing" text="글쓰기"/>
	</h1>
	<div class="pull-right">
		<a @click="save()" class="btn btn-default"><spring:message code="save"/></a>
		<a @click="cancel()" class="btn btn-default"><spring:message code="cancel"/></a>
	</div>
	<div style="clear:both;padding-top: 15px;">

		<form id="writeForm" name="writeForm" role="form" class="bv-form">
			<div class="form-group">
				<input v-model="articleInfo.title" placeholder="<spring:message code="title"/>" class="form-control">
			</div>
			<div class="form-group">
				<label for="noticeId"><input type="checkbox" id="noticeId" v-model="articleInfo.noticeYn" true-value="Y" false-value="N">
					<spring:message code="notice" text="공지사항"/>
				</label>
			</div>
			<div class="form-group">
				<textarea id="fileDropArea" v-model="articleInfo.contents" rows="5" style="width:100%"></textarea>
			</div>
		</form>
	</div>

	<div id="fileUploadPreview" class="file-upload-area"></div>
</div>

<script>
VARSQL.loadResource(['fileupload']);
VarsqlAPP.vueServiceBean({
	el: '#vueArea'
	,data: {
		list_count :10
		,searchVal : ''
		,articleInfo : VARSQL.util.objectMerge({
				title : ''
				,contents : ''
				,noticeYn : 'N'
				,removeFiles:[]
				,fileList : []
			},${articleInfo}
		)
		,fileUploadObj : {}
	}
	,methods:{
		init : function (){
			var _this =this;

			var files = [];
			this.articleInfo.fileList.forEach(function (item){
				files.push({
					name : item.fileName
					,size : item.fileSize
					,fileId : item.fileId
				})
			})

			this.fileUploadObj = VARSQLUI.file.create('#fileDropArea',{
				files: files
				,options : {
					url : '<varsql-app:boardUrl addUrl="save"/>'
					,params : {
						div : 'board'
						, fileExtensions : ''
						, contGroupId : '<varsql-app:boardCode/>'
					}
					,previewsContainer :'#fileUploadPreview'
				}
				,callback : {
					complete : function (file, resp){
						_this.listPage();
					}
					,removeFile : function (file){
						_this.articleInfo.removeFiles.push(file.fileId);
					}
				}
			});

		}
		,save : function(){

			var saveInfo = VARSQL.util.objectMerge({}, this.articleInfo);

			if(VARSQL.isBlank(saveInfo.title)){
				VARSQL.toastMessage('msg.content.enter.param',VARSQL.message('title'));
				return ;
			}

			var fileUploadObj = this.fileUploadObj;

			var rejectedFiles = fileUploadObj.getRejectedFiles();

			if(rejectedFiles.length > 0){
				VARSQLUI.toast.open({text : 'rejected files : '+JSON.stringify(rejectedFiles)});
				return ;
			}

			saveInfo.removeFileIds = saveInfo.removeFiles.join(',');

			fileUploadObj.save(saveInfo);
		}
		,cancel :function (){
			if(VARSQL.isBlank(this.articleInfo.articleId)){
				location.href='<varsql-app:boardUrl />';
			}else{
				location.href='<varsql-app:boardUrl addUrl="view"/>?articleId='+this.articleInfo.articleId;
			}
		}
		,listPage : function(){
			location.href='<varsql-app:boardUrl />';
		}
	}
});


</script>
