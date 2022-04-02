package com.varsql.web.dto.board;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.varsql.core.common.constants.VarsqlConstants;
import com.varsql.web.model.entity.board.BoardEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@NoArgsConstructor
public class BoardResponseDTO {

	private String boardCode;
	private long articleId;

	private String title;
	private String contents;
	private long commentCnt;
	private String authorName;
	private char noticeYn;

	private boolean modifyAuth;

	@JsonFormat(shape = Shape.STRING ,pattern=VarsqlConstants.TIMESTAMP_FORMAT)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
	private LocalDateTime regDt;

	private List<BoardFileResponseDTO> fileList;

	public static BoardResponseDTO toDto(BoardEntity board) {
		return toDto(board, false);
	}
	public static BoardResponseDTO toDto(BoardEntity board, boolean fileConvertFlag) {
		BoardResponseDTO brd = new BoardResponseDTO();

		brd.setBoardCode(board.getBoardCode());
		brd.setArticleId(board.getArticleId());
		brd.setTitle(board.getTitle());
		brd.setContents(board.getContents());
		brd.setCommentCnt(board.getCommentCnt());
		brd.setAuthorName(board.getAuthorName());
		brd.setNoticeYn(board.getNoticeYn());
		brd.setRegDt(board.getRegDt());

		if(fileConvertFlag) {
			brd.setFileList(board.getFileList().stream().map(item->{
				return BoardFileResponseDTO.toDto(item);
			}).collect(Collectors.toList()));
		}else {
			brd.setFileList(new ArrayList());
		}


		return brd;
	}
}