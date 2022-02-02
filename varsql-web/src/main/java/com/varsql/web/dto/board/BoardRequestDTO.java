package com.varsql.web.dto.board;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import org.springframework.web.multipart.MultipartFile;

import com.varsql.web.model.entity.board.BoardEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@NoArgsConstructor
public class BoardRequestDTO {

	private String boardCode;
	private long articleId;

	@NotEmpty
	@Size(max=250)
	private String title;

	private String contents;
	private long commentCnt;
	private String authorName;
	private char noticeYn;

	private String removeFileIds;

	private List<MultipartFile> file;

	public BoardEntity toEntity() {
		return BoardEntity.builder()
				.boardCode(boardCode)
				.articleId(articleId)
				.title(title)
				.contents(contents)
				.commentCnt(0)
				.noticeYn('Y'==noticeYn?'Y':'N')
				.build();
	}
}