package com.varsql.web.dto.board;

import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;

import org.springframework.web.multipart.MultipartFile;

import com.varsql.web.model.entity.board.BoardCommentEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class BoardCommentRequestDTO {

	private long commentId;
	
	@NotEmpty
	private String boardCode;
	
	@Positive
	@Min(value = 1)
	private long articleId;
	
	private long parentCommentId;
	private String contents;
	private String authorName;
	
	private String removeFileIds; 
	
	private List<MultipartFile> file;
	
	public BoardCommentEntity toEntity() {
		return BoardCommentEntity.builder()
				.boardCode(boardCode)
				.commentId(commentId)
				.articleId(articleId)
				.parentCommentId(parentCommentId)
				.contents(contents)
				.build();
	}

}