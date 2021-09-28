package com.varsql.web.dto.board;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.varsql.core.common.util.SecurityUtil;
import com.varsql.web.model.entity.board.BoardCommentEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@NoArgsConstructor
public class BoardCommentResponseDTO {

	private long articleId;
	private long commentId;
	private long parentCommentId;
	
	private String contents; 
	private String authorName; 
	
	private boolean delYn;
	
	private boolean isModifyAuth; 
	
	private int indent;
	
	private LocalDateTime regDt; 
	
	private List<BoardFileResponseDTO> fileList; 
	
	public static BoardCommentResponseDTO toDto(BoardCommentEntity comment) {
		BoardCommentResponseDTO brd = new BoardCommentResponseDTO();
		
		brd.setCommentId(comment.getCommentId());
		brd.setArticleId(comment.getArticleId());
		brd.setParentCommentId(comment.getParentCommentId());
		
		if(!comment.isDelYn()) {
			brd.setContents(comment.getContents());
			brd.setIndent(comment.getIndent());
			brd.setAuthorName(comment.getAuthorName());
			brd.setRegDt(comment.getRegDt());
		}
		brd.setDelYn(comment.isDelYn());
		
		brd.setModifyAuth((SecurityUtil.isAdmin() || comment.getRegId().equals(SecurityUtil.userViewId())));
		
		brd.setFileList(comment.getFileList().stream().map(item->{
			return BoardFileResponseDTO.toDto(item);
		}).collect(Collectors.toList()));
		
		return brd;
	}
}