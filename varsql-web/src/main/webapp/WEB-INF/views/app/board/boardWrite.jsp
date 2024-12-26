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
			<div id="articleWrapper">
				<div id="fileUploadPreview" class="file-upload-area"></div>
				
				<div class="form-group">
					<div id="articleContent" style="width:100%;margin-top:10px;"></div>
				</div>
			</div>
		</form>
	</div>

</div>

<script>
VARSQL.loadResource(['fileupload',"toast.editor"]);
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
		,editor : false
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
			
			setTimeout(()=>{
				this.editor = new toastui.Editor({
		            el: document.querySelector('#articleContent'), // 에디터를 적용할 요소 (컨테이너)
		            height: '500px',                        // 에디터 영역의 높이 값 (OOOpx || auto)
		            initialEditType: 'markdown',            // 최초로 보여줄 에디터 타입 (markdown || wysiwyg)
		            initialValue: '',     // 내용의 초기 값으로, 반드시 마크다운 문자열 형태여야 함
		            //previewStyle: 'vertical'                // 마크다운 프리뷰 스타일 (tab || vertical)
		            plugins: [
	                    [toastui.Editor.plugin.chart],
	                    [toastui.Editor.plugin.codeSyntaxHighlight, { highlighter: Prism }],
	                    toastui.Editor.plugin.tableMergedCell,
	                    toastui.Editor.plugin.colorSyntax,
	                    [
	                        toastui.Editor.plugin.uml,
	                        { rendererURL: "http://www.plantuml.com/plantuml/svg/" }
	                    ]
	                ],
		            hooks: {
		                async addImageBlobHook(blob, callback) { // 이미지 업로드 로직 커스텀
		                    try {
		                        const formData = new FormData();
		                        formData.append('image', blob);

		                        const response = await fetch(VARSQL.url('/file/imageUpload'), {
		                            method : 'POST',
		                            body : formData,
		                        });

		                        const responseJson = await response.json();
		                        const fileInfo = responseJson.item;
		                        const fileContId = fileInfo.fileContId;
		                        
		                        // 4. addImageBlobHook의 callback 함수를 통해, 디스크에 저장된 이미지를 에디터에 렌더링
		                        const imageUrl = VARSQL.url( '/imageView/'+fileContId);
		                        callback(imageUrl, fileInfo.fileName || 'image');

		                    } catch (error) {
		                        console.error('업로드 실패 : ', error);
		                    }
		                }
		            }
		        });
				
				this.editor.setHTML(this.articleInfo.contents)
			},100); 

			this.fileUploadObj = VARSQLUI.file.create('#articleWrapper',{
				files: files
				,btn :'top'
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
			
			var contents = this.editor.getMarkdown(); 
			if (contents.length < 1) {
			    VARSQL.toastMessage('msg.content.enter.param',VARSQL.message('content'));
			    return ; 
			}
						
			saveInfo.contents = this.editor.getHTML();

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
