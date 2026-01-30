package com.varsql.web.app.user.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.varsql.core.exception.PermissionDeniedException;
import com.varsql.web.common.service.FileUploadService;
import com.varsql.web.constants.ResourceConfigConstants;
import com.varsql.web.constants.UploadFileType;
import com.varsql.web.dto.board.BoardCommentRequestDTO;
import com.varsql.web.dto.board.BoardCommentResponseDTO;
import com.varsql.web.dto.board.BoardRequestDTO;
import com.varsql.web.dto.board.BoardResponseDTO;
import com.varsql.web.exception.BoardNotFoundException;
import com.varsql.web.exception.BoardPermissionException;
import com.varsql.web.model.entity.board.BoardCommentEntity;
import com.varsql.web.model.entity.board.BoardEntity;
import com.varsql.web.model.entity.board.BoardFileEntity;
import com.varsql.web.repository.board.BoardCommentRepository;
import com.varsql.web.repository.board.BoardFileRepository;
import com.varsql.web.repository.board.BoardRepository;
import com.varsql.web.repository.spec.BoardSpec;
import com.varsql.web.util.MarkdownXssUtils;
import com.varsql.web.util.NumberUtils;
import com.varsql.web.util.SecurityUtil;
import com.varsql.web.util.VarsqlUtils;
import com.vartech.common.app.beans.ResponseResult;
import com.vartech.common.app.beans.SearchParameter;
import com.vartech.common.utils.StringUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardService{
	private final static Logger logger = LoggerFactory.getLogger(BoardService.class);

	private final BoardRepository boardEntityRepository;

	private final BoardCommentRepository boardCommentEntityRepository;

	private final BoardFileRepository boardFileEntityRepository;

	private final FileUploadService fileUploadService;

	private final String BOARD_CONT_TYPE = "board";
	private final String COMMENT_CONT_TYPE = "comment";

	public ResponseResult list(String boardCode, SearchParameter searchParameter) {
		Sort sort =Sort.by(Sort.Direction.DESC, BoardEntity.NOTICE_YN).and(Sort.by(Sort.Direction.DESC, BoardEntity.REG_DT));

		Page<BoardEntity> result = boardEntityRepository.findAll(
			BoardSpec.boardSearch(boardCode, searchParameter)
			, VarsqlUtils.convertSearchInfoToPage(searchParameter, sort)
		);

		return VarsqlUtils.getResponseResult(result.getContent().stream().map(item->{
			 return BoardResponseDTO.toDto(item);
		}).collect(Collectors.toList()), result.getTotalElements(), searchParameter);
	}

	/**
	 *
	 * @Method Name  : saveBoardInfo
	 * @Method 설명 : 게시물 정보 저장.
	 * @작성자   : ytkim
	 * @작성일   : 2021. 7. 1.
	 * @변경이력  :
	 * @param boardRequestDTO
	 * @return
	 */
	@Transactional(transactionManager=ResourceConfigConstants.APP_TRANSMANAGER, rollbackFor=Throwable.class)
	public ResponseResult saveBoardInfo(BoardRequestDTO boardRequestDTO) {

		BoardEntity boardEntity;

		if(NumberUtils.isNullOrZero(boardRequestDTO.getArticleId())) {
			boardEntity = boardRequestDTO.toEntity();
			boardEntity.setAuthorName(SecurityUtil.loginInfo().getFullname());
			boardEntity.setFileList(new ArrayList<BoardFileEntity>());
		}else {
			boardEntity = boardEntityRepository.findByArticleId(boardRequestDTO.getArticleId());

			if(!isModify(boardEntity)) {
				throw new BoardPermissionException("no permission");
			}

			boardEntity.setTitle(boardRequestDTO.getTitle());
			boardEntity.setContents(boardRequestDTO.getContents());

			String fileIds = boardRequestDTO.getRemoveFileIds();

			if(!StringUtils.isBlank(fileIds)) {
				boardFileEntityRepository.deleteByIdInQuery(Arrays.asList(fileIds.split(",")).stream().map(item->{
					return Long.parseLong(item);
				}).collect(Collectors.toList()));
			}
		}

		if(boardRequestDTO.getFile() !=null && boardRequestDTO.getFile().size() > 0) {
			List<BoardFileEntity> boardFileList= boardEntity.getFileList();
			fileUploadService.uploadFiles(UploadFileType.BOARD, boardRequestDTO.getFile(), boardRequestDTO.getArticleId()+"", boardRequestDTO.getBoardCode(),"file", false, false).forEach(item->{
				BoardFileEntity entity= BoardFileEntity.toBoardFileEntity(item);
				entity.setArticle(boardEntity);
				entity.setContType(BOARD_CONT_TYPE);
				boardFileList.add(entity);
			});
			boardEntity.setFileList(boardFileList);
		}
		
		//boardEntity.setContents(MarkdownXssUtils.sanitizeAndSerializeMarkdown(boardEntity.getContents()));

		boardEntityRepository.save(boardEntity);

		ResponseResult result = VarsqlUtils.getResponseResultItemList(new ArrayList());
		result.setItemOne(1);

		return result;
	}

	/**
	 *
	 * @Method Name  : deleteBoardInfo
	 * @Method 설명 : 글 삭제.
	 * @작성자   : ytkim
	 * @작성일   : 2021. 7. 8.
	 * @변경이력  :
	 * @param boardCode
	 * @param articleId
	 * @return
	 */
	@Transactional(transactionManager=ResourceConfigConstants.APP_TRANSMANAGER, rollbackFor=Throwable.class)
	public ResponseResult deleteBoardInfo(String boardCode, long articleId) {

		logger.debug("deleteBoardInfo boardCode : {} , articleId:{} ", boardCode, articleId);

		BoardEntity board = boardEntityRepository.findByArticleId(articleId);

		if(board == null || !isModify(board)) {
			throw new PermissionDeniedException("no permission");
		}

		boardFileEntityRepository.deleteByContIdQuery(board.getArticleId());
		boardFileEntityRepository.deleteAllCommnetFileQuery(board.getArticleId());
		boardCommentEntityRepository.deleteByArticleIdQuery(board.getArticleId());
		boardEntityRepository.delete(board);

		return VarsqlUtils.getResponseResultItemOne(1);
	}

	/**
	 *
	 * @Method Name  : viewBoardInfo
	 * @Method 설명 : 글 상세보기.
	 * @작성자   : ytkim
	 * @작성일   : 2021. 7. 8.
	 * @변경이력  :
	 * @param boardCode
	 * @param articleId
	 * @return
	 */
	public BoardResponseDTO viewBoardInfo(String boardCode, long articleId) {
		BoardEntity boardEntity = boardEntityRepository.findByArticleId(articleId);

		if(boardEntity == null) {
			throw new BoardNotFoundException("article id not found : "+ articleId);
		}
		
		BoardResponseDTO  dto = BoardResponseDTO.toDto(boardEntity, true);
		dto.setModifyAuth(isModify(boardEntity));

		return dto;
	}

	/**
	 *
	 * @Method Name  : commentSave
	 * @Method 설명 : 코멘트 저장.
	 * @작성자   : ytkim
	 * @작성일   : 2021. 7. 8.
	 * @변경이력  :
	 * @param articleId
	 * @param boardCommentRequestDTO
	 * @return
	 */
	@Transactional(transactionManager=ResourceConfigConstants.APP_TRANSMANAGER, rollbackFor=Throwable.class)
	public ResponseResult commentSave(long articleId, BoardCommentRequestDTO boardCommentRequestDTO) {
		
		BoardEntity boardEntity = boardEntityRepository.findByArticleId(articleId);
		
		if(boardEntity == null) {
			throw new BoardNotFoundException("article id not found : "+ articleId);
		}
		
		BoardCommentEntity boardCommentEntity;
		boolean isNew = NumberUtils.isNullOrZero(boardCommentRequestDTO.getCommentId());
		boolean isReComment = false;

		if(isNew) {

			boardCommentEntity = boardCommentRequestDTO.toEntity();
			boardCommentEntity.setArticleId(articleId);
			boardCommentEntity.setAuthorName(SecurityUtil.loginInfo().getFullname());
			boardCommentEntity.setFileList(new ArrayList<BoardFileEntity>());

			isReComment = !NumberUtils.isNullOrZero(boardCommentRequestDTO.getParentCommentId()); // 대댓글
			if(isReComment) {

				BoardCommentEntity parentBoardCommentEntity = boardCommentEntityRepository.findByArticleIdAndCommentId(articleId, boardCommentRequestDTO.getParentCommentId());

				if(parentBoardCommentEntity== null) {
					throw new BoardNotFoundException("parent comments not found : "+ boardCommentRequestDTO.getParentCommentId());
				}
				boardCommentEntity.setIndent(parentBoardCommentEntity.getIndent() +1);
				boardCommentEntity.setGrpCommentId(parentBoardCommentEntity.getGrpCommentId());
				boardCommentEntity.setGrpSeq(boardCommentEntityRepository.findByGrpSeqMaxQuery(parentBoardCommentEntity.getGrpCommentId()));
			}else {

			}
		}else {
			boardCommentEntity = boardCommentEntityRepository.findByArticleIdAndCommentId(articleId, boardCommentRequestDTO.getCommentId());

			if( !isCommentModify(boardCommentEntity)){
				throw new BoardPermissionException("no permission");
			}

			boardCommentEntity.setContents(boardCommentRequestDTO.getContents());

			String fileIds = boardCommentRequestDTO.getRemoveFileIds();

			if(!StringUtils.isBlank(fileIds)) {
				boardFileEntityRepository.deleteByIdInQuery(Arrays.asList(fileIds.split(",")).stream().map(item->{
					return Long.parseLong(item);
				}).collect(Collectors.toList()));
			}
		}

		if(boardCommentRequestDTO.getFile().size() > 0) {
			List<BoardFileEntity> boardFileList= boardCommentEntity.getFileList();

			fileUploadService.uploadFiles(UploadFileType.BOARD, boardCommentRequestDTO.getFile(), boardCommentRequestDTO.getCommentId()+"", boardCommentRequestDTO.getBoardCode(), "file", false, false).forEach(item->{
				BoardFileEntity entity= BoardFileEntity.toBoardFileEntity(item);
				entity.setComment(boardCommentEntity);
				entity.setContType(COMMENT_CONT_TYPE);
				boardFileList.add(entity);
			});
			boardCommentEntity.setFileList(boardFileList);
		}
		
		boardCommentEntity.setContents(MarkdownXssUtils.sanitizeAndSerializeHTML(boardCommentEntity.getContents()));

		BoardCommentEntity saveEntity = boardCommentEntityRepository.save(boardCommentEntity);

		if(isNew) {
			
			if(boardEntity.getCommentCnt() > 0) {
				boardEntity.setCommentCnt(boardEntity.getCommentCnt() +1);
			}else {
				boardEntity.setCommentCnt(1);
			}
			
			boardEntityRepository.save(boardEntity);
			
			if(!isReComment){
				saveEntity.setGrpCommentId(saveEntity.getCommentId());
				boardCommentEntityRepository.save(saveEntity);
			}else {
				boardCommentEntityRepository.updateGrpSeqQuery(saveEntity.getArticleId(), saveEntity.getGrpCommentId(), saveEntity.getCommentId(), saveEntity.getGrpSeq());
			}
		}
		
		ResponseResult result = VarsqlUtils.getResponseResultItemList(new ArrayList());
		result.setItemOne(1);

		return result;
	}

	/**
	 *
	 * @Method Name  : commentList
	 * @Method 설명 : 댓글 목록.
	 * @작성자   : ytkim
	 * @작성일   : 2021. 7. 8.
	 * @변경이력  :
	 * @param boardCode
	 * @param acticleId
	 * @param searchParameter
	 * @return
	 */
	public ResponseResult commentList(String boardCode, long acticleId, SearchParameter searchParameter) {
		Sort sort =Sort.by(Sort.Direction.ASC, BoardCommentEntity.GRP_COMMENT_ID).and(Sort.by(Sort.Direction.ASC, BoardCommentEntity.GRP_SEQ));

		return VarsqlUtils.getResponseResultItemList(boardCommentEntityRepository.findByArticleId(acticleId, sort).stream().map(item->{
			return BoardCommentResponseDTO.toDto(item);
		}).collect(Collectors.toList()));
	}

	/**
	 *
	 * @Method Name  : commentDelete
	 * @Method 설명 : 댓글 삭제.
	 * @작성자   : ytkim
	 * @작성일   : 2021. 7. 8.
	 * @변경이력  :
	 * @param articleId
	 * @param commentId
	 * @return
	 */
	public ResponseResult commentDelete(long articleId, long commentId) {
		
		BoardEntity boardEntity = boardEntityRepository.findByArticleId(articleId);
		
		if(boardEntity == null) {
			throw new BoardNotFoundException("article id not found : "+ articleId);
		}

		BoardCommentEntity boardCommentEntity = boardCommentEntityRepository.findByArticleIdAndCommentId(articleId, commentId);

		if(boardCommentEntity == null || !isCommentModify(boardCommentEntity)) {
			throw new PermissionDeniedException("no permission");
		}

		if(boardCommentEntity.getChildren().size() > 0) {
			boardCommentEntity.setDelYn(true);
			boardCommentEntityRepository.save(boardCommentEntity);
		}else {
			boardCommentEntityRepository.delete(boardCommentEntity);

			BoardCommentEntity parentCommentEntity = boardCommentEntity.getParent();
			// 상위 comment 삭제 처리
			if(parentCommentEntity != null && parentCommentEntity.isDelYn() && parentCommentEntity.getChildren().size() ==1 && parentCommentEntity.getChildren().get(0).getCommentId() ==boardCommentEntity.getCommentId() ) {
				boardCommentEntityRepository.delete(parentCommentEntity);
			}
		}
		
		if(boardEntity.getCommentCnt() > 0) {
			boardEntity.setCommentCnt(boardEntity.getCommentCnt() - 1);
		}else {
			boardEntity.setCommentCnt(0);
		}
		
		boardEntityRepository.save(boardEntity);

		return VarsqlUtils.getResponseResultItemOne(1);
	}

	/**
	 *
	 * @Method Name  : findFileList
	 * @Method 설명 : 파일 목록.
	 * @작성자   : ytkim
	 * @작성일   : 2021. 7. 8.
	 * @변경이력  :
	 * @param articleId
	 * @param fileId
	 * @return
	 */
	public List<BoardFileEntity> findFileList(long articleId, long fileId) {
		return boardFileEntityRepository.findAllByArticleAndFileId(BoardEntity.builder().articleId(articleId).build(), fileId);
	}

	private boolean isModify(BoardEntity boardEntity) {
		return SecurityUtil.isAdmin() || SecurityUtil.isManager() || boardEntity.getRegId().equals(SecurityUtil.userViewId());
	}

	private boolean isCommentModify(BoardCommentEntity boardCommentEntity) {
		return SecurityUtil.isAdmin() || SecurityUtil.isManager() || boardCommentEntity.getRegId().equals(SecurityUtil.userViewId());
	}

}
