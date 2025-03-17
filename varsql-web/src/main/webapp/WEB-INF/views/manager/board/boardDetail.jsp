<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/include/tagLib.jspf"%>

<div class="display-off" id="vueArea">
	<h1 style="display: inline-block;">
		<a href=""><spring:message code="detail.view" text="상세보기"/></a>
	</h1>
	<div class="pull-right">
		<template v-if="articleInfo.modifyAuth">
			<a @click="modifyInfo()"  class="btn btn-primary"><spring:message code="modify" text="수정"/></a>
			<a href="javascript:;" @click="deleteItem()" class="btn btn-warning"><spring:message code="delete" text="삭제"/></a>
		</template>
	</div>
	<div style="clear:both;padding-top: 15px;"></div>
	<div class="article-area">
		<div class="title">
			<h3>{{articleInfo.title}}</h3>
			<div>
				{{articleInfo.authorName}} {{articleInfo.regDt}}
			</div>
		</div>
		<div class="file-list-area" v-if="articleInfo.fileList.length > 0">
			<div><spring:message code="attach.file" text="첨부파일"/></div>
			<ul class="file-list">
				<li v-for="(item, index) in articleInfo.fileList" class="file-list-item">
					<a href="javascript:;" @click="download(item)" :title="item.fileName" class="text-ellipsis">
						<i class="fa fa-download"></i>{{item.fileName}}({{item.displaySize}})
					</a>
				</li>
			</ul>
		</div>

		<div id="contentViewer" style="width: 100%;padding: 5px;"></div>
	</div>

	<div style="margin-top: 15px;border-top: 1px solid rgb(221, 221, 221);padding: 15px 0px 0px 10px;">
		<strong class="comment-label"><spring:message code="comment" text="댓글"/></strong>
	</div>
	<div>
		<div id="mainCommentContainer">
			<div class="form-group">
				<div id="commentContents" style="width:100%;margin-top:10px;"></div>
			</div>
			<div id="commentFileUploadPreview" class="file-upload-area"></div>
		</div>
		<div class="pull-right">
			<a @click="commentSave()" class="btn btn-success"><spring:message code="save" text="저장"/></a>
		</div>
		<div style="clear:both;padding-top:5px;"></div>
	</div>
	<div class="comment-area">
		<ul class="list-group list-group-flush">
			<li v-for="(commentItem, index) in commentInfos" class="list-group-item">
				<div class="comment-item">
					<div class="comment-meta-info" v-if="!commentItem.delYn">
						 <span class="text-ellipsis" :title="commentItem.fileName">{{commentItem.authorName}}({{commentItem.regDt}})</span>
						 <span class="pull-right" v-if="commentItem.modifyAuth">
							<button @click="commentModify(commentItem)" class="btn btn-sm btn-default"><i class="fa fa-edit"></i><spring:message code="modify" text="수정"/></button>
							<button @click="commentDelete(commentItem)" class="btn btn-sm btn-warning"><i class="fa fa-trash"></i><spring:message code="delete" text="삭제"/></button>
						</span>
					</div>

					<template v-if="commentItem.commentId != modifyComment.commentId">
						<div class="clearboth">
							<div class="comment-content">
								<template v-if="commentItem.delYn">
									<spring:message code="delete.warning.msg" text="삭제된 메시지 입니다."/>
								</template>
								<template v-else>
									<div class="toastui-editor-contents" v-html="commentItem.contents"></div>
								</template>
							</div>
							<template v-if="commentItem.indent == 0">
								<ul class="list-group list-group-flush" v-if="commentItem.fileList.length > 0">
									<li v-for="(fileItem, index) in commentItem.fileList" class="list-group-item">
										<div class="text-ellipsis" :title="fileItem.fileName">
											<a href="javascript:;" @click="download(fileItem)">
												<i class="fa fa-download"></i> {{fileItem.fileName}}({{fileItem.displaySize}})
											</a>
										</div>
									</li>
								</ul>
								<div style="text-align:right;">
									<button class="btn btn-sm btn-default" type="button" @click="reComment(commentItem)"><spring:message code="re.comment" text="대댓글"/></button>
								</div>

								<div class="form-group re-comment-btn" v-if="reCommentParent == commentItem">
									<div class="input-group">
										<input v-model="reCommentParent.reCommentText"  placeholder="<spring:message code="input.placeholder" text="입력..."/>" class="form-control">
										<span class="input-group-btn">
											<button type="button" class="btn btn-default search-btn" @click="comment('re')"><spring:message code="save" text="저장"/></button>
										</span>
									</div>
								</div>

								<div class="re-comment-item" v-for="(reCommentItem, index) in commentItem.children">
									<div class="re-comment-meta-info">
										 <span class="text-ellipsis" :title="commentItem.fileName">{{reCommentItem.authorName}}({{reCommentItem.regDt}})</span>
										 <span class="pull-right" v-if="reCommentItem.modifyAuth">
							
										 	<button @click="reCommentModify(reCommentItem)" title="<spring:message code="modify" text="수정"/>" class="btn btn-sm btn-default"><i class="fa fa-edit"></i></button>
											<button @click="commentDelete(reCommentItem)" title="<spring:message code="delete" text="삭제"/>" class="btn btn-sm btn-warning"><i class="fa fa-trash"></i></button>
										</span>
									</div>
									<div class="re-comment-modify input-group" v-if="reModifyCommentInfo.commentId == reCommentItem.commentId">
										<textarea v-model="reModifyCommentInfo.reCommentText" rows="2" placeholder="입력..." class="form-control"></textarea>
										<span class="input-group-btn">
											<button type="button" class="btn btn-success" @click="commentSave('re','modify')"><spring:message code="save" text="저장"/></button>
											<button type="button" class="btn btn-default" @click="reCommentModify({})"><spring:message code="cancel" text="취소"/></button>
										</span>
									</div>
									<div class="re-comment-content" v-else><pre>{{reCommentItem.contents}}</pre></div>
								</div>
							</template>
						</div>
					</template>
					<template v-else>
						
						<div id="modifyCommentContainer">
							<div class="form-group">
								<div id="modifyContents" style="width:100%;margin-top:10px;"></div>
							</div>
							<div id="modifyCommentFileUploadPreview" class="file-upload-area"></div>
						</div>
					
						<div class="pull-right">
							<a @click="commentModify('cancel')"  class="btn btn-default"><spring:message code="cancel" text="취소"/></a>
							<a @click="commentSave('modify')"  class="btn btn-success"><spring:message code="save" text="저장"/></a>
						</div>
						<div style="clear:both;padding-bottom:5px;"></div>
					</template>
				</div>
			</li>
		</ul>
	</div>
</div>

<script>
VARSQL.loadResource(['fileupload',"toast.editor"]);
VarsqlAPP.vueServiceBean({
	el: '#vueArea'
	,data: {
		articleInfo :${articleInfo}
		,comment : {}
		,commentInfos : []
		,modifyComment : {}
		,commentFileUploadObj : {}
		,modifyCommentFileUploadObj : {}
		,reCommentParent : {}
		,reModifyCommentInfo : {}
		,commentEditor: {}
		,modifyCommentEditor: {}

	}
	,methods:{
		commentReset : function(){
			this.comment ={contents : '', removeFiles :[], fileList :[]};
		}
		,init : function (){
			var _this =this;
			
			const viewer  = new toastui.Editor.factory({
	            el: document.querySelector('#contentViewer'), 
	            height: '500px',  
	            linkAttributes:{target:"_blank"},
	            viewer: true,
	            initialValue : this.articleInfo.contents,
	            plugins: [
                    [toastui.Editor.plugin.chart],
                    toastui.Editor.plugin.colorSyntax,
                   // [toastui.Editor.plugin.codeSyntaxHighlight, { highlighter: Prism }],
                    toastui.Editor.plugin.tableMergedCell,
                    [
                        toastui.Editor.plugin.uml,
                        { rendererURL: "http://www.plantuml.com/plantuml/svg/" }
                    ]
                ],
	        });
			
			setTimeout(()=>{
				this.commentEditor = new toastui.Editor({
		            el: document.querySelector('#commentContents'),
		            height: '200px',                        
		            initialEditType: 'markdown',
		            autofocus:false,
		            initialValue: '',
		            linkAttributes:{target:"_blank"},
		            //previewStyle: 'vertical'
		            plugins: [
	                    [toastui.Editor.plugin.chart],
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
		                        
		                        // 4. addImageBlobHook의 callback 함수를 통해, 디스크에 저장된 이미지를 에디터에 추가
		                        const imageUrl = VARSQL.url( '/imageView/'+fileContId);
		                        callback(imageUrl, fileInfo.fileName || 'image');

		                    } catch (error) {
		                        console.error('upload fail : ', error);
		                    }
		                }
		            }
		        });
			},100); 
			

			this.commentList();

			this.commentFileUploadObj = VARSQLUI.file.create('#mainCommentContainer',{
				options : {
					url : VARSQL.url(VARSQL.uri.manager,'/boardMgmt/commentSave')
					,params : {
						div : 'board'
						, contGroupId : _this.articleInfo.boardCode
					}
					,previewsContainer :'#commentFileUploadPreview'
				}
				,callback : {
					complete : function (file, resp){
						
						_this.commentEditor.setHTML('');
						_this.commentList();
					}
				}
			});
		}
		,modifyInfo : function (){
			location.href= VARSQL.url(VARSQL.uri.manager,'/boardMgmt/modify?boardCode=#boardCode#&articleId=#articleId#',this.articleInfo);
		}
		,download : function (item){
			VARSQL.req.download({
				url : {
					type: VARSQL.uri.manager
					,url : VARSQL.util.replaceParam('/boardMgmt/file?boardCode=#boardCode#&articleId=#fileContId#&fileId=#fileId#',item)
				}
			});
		}
		,deleteItem : function(){

			if(!VARSQL.confirmMessage('msg.brd.article.delete','게시글이 삭제되면 복구할 수 없습니다. 그래도 삭제하시겠습니까?')) return ;

			const param = {
				'articleId' : this.articleInfo.articleId
				,boardCode : this.articleInfo.boardCode
			};
			
			this.$ajax({
				url: {type:VARSQL.uri.manager, url:'/boardMgmt/delete'}
				,method :'delete'
				,data: param
				,success: (resData) => {
					VARSQL.toastMessage('msg.delete.success');
					
					setTimeout(()=>{
						if(parent && parent.boardMainSearch){
							parent.boardMainSearch();
						}
					},1200); 
					
				}
			})
		}
		,commentSave : function (mode, subMode){

			var saveInfo;
			var fileUploadObj;

			if(mode == 're'){ // 대댓글
				if(subMode == 'modify'){ //수정
					saveInfo = VARSQL.util.objectMerge(this.reModifyCommentInfo);
					saveInfo.contents = saveInfo.reCommentText;
				}else{ // 작성.
					saveInfo = {};
					saveInfo.parentCommentId = this.reCommentParent.commentId;
					saveInfo.contents = this.reCommentParent.reCommentText;
				}
			}else{
				if(mode == 'modify'){
					saveInfo = VARSQL.util.objectMerge(this.modifyComment);
					saveInfo.contents = this.modifyCommentEditor.getHTML();
					saveInfo.removeFileIds = saveInfo.removeFiles.join(',');

					fileUploadObj = this.modifyCommentFileUploadObj;
				}else{
					saveInfo = this.comment;
					saveInfo.contents = this.commentEditor.getHTML();
					fileUploadObj = this.commentFileUploadObj;
				}
			}

			if(VARSQL.isBlank(saveInfo.contents)){
				VARSQL.toastMessage('msg.content.enter.param',VARSQL.message('content'));
				return ;
			}

			saveInfo.articleId = this.articleInfo.articleId;
			saveInfo.boardCode = this.articleInfo.boardCode;

			if(mode == 're'){ // 대댓글 파일없이 저장.
				this.reModifyCommentInfo = {};
				this.commentFileUploadObj.emptyFileSave(saveInfo);
				return ;
			}

			var rejectedFiles = fileUploadObj.getRejectedFiles();

			if(rejectedFiles.length > 0){
				VARSQLUI.toast.open({text : 'rejected files : '+JSON.stringify(rejectedFiles)});
				return ;
			}

			fileUploadObj.save(saveInfo, (mode == 'modify' ? true : false));

		}
		,commentList : function (){

			this.commentReset();

			const param = {
				articleId : this.articleInfo.articleId
				,boardCode: this.articleInfo.boardCode
			};

			this.$ajax({
				url:  {type:VARSQL.uri.manager, url:'/boardMgmt/commentList'}
				,data: param
				,success: (resData)=> {
					var list = resData.list;

					var commentInfos = [];
					var commentMap = {};

					for(var i =0 ;i < list.length; i++){
						var item = list[i];

						if(item.indent == 0){
							
							item.contents = (item.contents||'').replace(/&amp;amp;/g,'&');
							
							commentInfos.push(item);
							item.children = [];
							commentMap[item.commentId] = item;
						}else{
							if(commentMap[item.parentCommentId]){
								commentMap[item.parentCommentId].children.push(item);
							}
						}
					}
					this.commentInfos = commentInfos;
				}
			})
		}
		,commentModify : function(item){
			var _this = this;

			if(item=='cancel' || this.modifyComment == item){
				this.modifyComment = {};
				this.modifyCommentFileUploadObj.destroy();
				return ;
			}

			if(VARSQL.isUndefined(item.modifyContents)){
				item.modifyContents = item.contents;
				item.removeFiles = [];

				var files = [];
				item.fileList.forEach(function (item){
					files.push({
						name : item.fileName
						,size : item.fileSize
						,fileId : item.fileId
					})
				})
				item.modifyFiles = files;
			}

			this.modifyComment = item;
			
			if(_this.modifyCommentEditor && _this.modifyCommentEditor.destroy) _this.modifyCommentEditor.destroy();

			this.$nextTick(()=>{

				if(VARSQL.isUndefined(_this.modifyComment.commentId)) return ;
				
				setTimeout(()=>{
					
					_this.modifyCommentEditor = new toastui.Editor({
			            el: document.querySelector('#modifyContents'),
			            height: 'auto',                        
			            initialEditType: 'markdown',
			            autofocus:false,
			            initialValue: '',
			            linkAttributes:{target:"_blank"},
			            //previewStyle: 'vertical'
			            plugins: [
		                    [toastui.Editor.plugin.chart],
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
			                        
			                        // 4. addImageBlobHook의 callback 함수를 통해, 디스크에 저장된 이미지를 에디터에 추가
			                        const imageUrl = VARSQL.url( '/imageView/'+fileContId);
			                        callback(imageUrl, fileInfo.fileName || 'image');

			                    } catch (error) {
			                        console.error('upload fail : ', error);
			                    }
			                }
			            }
			        });
					
					_this.modifyCommentEditor.setHTML(item.contents);
				},100); 
				
				this.modifyCommentFileUploadObj = VARSQLUI.file.create('#modifyCommentContainer',{
					files: _this.modifyComment.modifyFiles
					,options : {
						url : VARSQL.url(VARSQL.uri.manager,'/boardMgmt/commentSave')
						,params : {
							div : 'board'
							, contGroupId : this.articleInfo.boardCode
						}
						,previewsContainer :'#modifyCommentFileUploadPreview'
					}
					,callback : {
						complete : function (file, resp){
							_this.commentList();
							_this.commentModify('cancel');
						}
						,removeFile : function (file){
							var modifyFiles = _this.modifyComment.modifyFiles;
							for(var j=0; j < modifyFiles.length; j++){
								var fileItem = modifyFiles[j];

								if(fileItem.fileId == file.fileId){
									_this.modifyComment.modifyFiles.splice(j, 1);
									break;
								}
							}

							_this.modifyComment.removeFiles.push(file.fileId);
						}
					}
				}, true);
			})
		}
		,commentDelete : function(item){
			var _this = this;

			if(!VARSQL.confirmMessage('msg.brd.comment.delete','댓글이 삭제되면 복구할 수 없습니다. 그래도 삭제하시겠습니까?')) return ;

			var param = {
				articleId : this.articleInfo.articleId
				,commentId : item.commentId
				,boardCode : this.articleInfo.boardCode
			};

			this.$ajax({
				url: {type:VARSQL.uri.manager, url:'/boardMgmt/commentDelete'}
				,data: param
				,method :'delete'
				,success: function(resData) {
					_this.commentList();
				}
			})
		}
		,reComment : function (pComment){
			if(this.reCommentParent == pComment){
				this.reCommentParent = {};
				this.reCommentText = '';
			}else{
				pComment.reCommentText = pComment.reCommentText || ''
				this.reCommentParent = pComment;
			}
		}
		,reCommentModify : function (commentInfo){
			if(this.reModifyCommentInfo == commentInfo){
				this.reModifyCommentInfo = {};
			}else{
				commentInfo.reCommentText = commentInfo.reCommentText || commentInfo.contents;
				this.reModifyCommentInfo = commentInfo;
			}
		}
	}
});
</script>
