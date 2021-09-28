package com.varsql.web.model.entity.board;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.varsql.web.constants.VarsqlJpaConstants;
import com.varsql.web.model.base.AbstractAuditorModel;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@DynamicUpdate
@NoArgsConstructor
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property="articleId")
@Table(name = BoardEntity._TB_NAME)
public class BoardEntity extends AbstractAuditorModel {

	private static final long serialVersionUID = 1L;

	public final static String _TB_NAME = "VTBOARD";

	@Id
	@TableGenerator(
		name = "articleIdGenerator"
		,table= VarsqlJpaConstants.TABLE_SEQUENCE_NAME
		,pkColumnValue = _TB_NAME
		,allocationSize = 1
	)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "articleIdGenerator")
	@Column(name ="ARTICLE_ID", nullable = false)
	private long articleId;
	
	@Column(name = "BOARD_CODE")
	private String boardCode;

	@Column(name = "TITLE")
	private String title;

	@Column(name = "CONTENTS")
	private String contents;

	@Column(name = "COMMENT_CNT")
	private long commentCnt;

	@Column(name = "AUTHOR_NAME")
	private String authorName;

	@Column(name = "NOTICE_YN")
	private char noticeYn;
	
	@OneToMany(mappedBy = "article", fetch = FetchType.LAZY , cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@Where(clause = "CONT_TYPE = 'board' ")	
	private List<BoardFileEntity> fileList = new ArrayList<>(); 

	@Builder
	public BoardEntity(String boardCode, long articleId, String title, String contents, long commentCnt,
			String authorName, char noticeYn) {
		this.boardCode = boardCode;
		this.articleId = articleId;
		this.title = title;
		this.contents = contents;
		this.commentCnt = commentCnt;
		this.authorName = authorName;
		this.noticeYn = noticeYn;
	}
	
	public final static String BOARD_CODE = "boardCode";
	public final static String ARTICLE_ID = "articleId";
	public final static String TITLE = "title";
	public final static String CONTENTS = "contents";
	public final static String COMMENT_CNT = "commentCnt";
	public final static String AUTHOR_NAME = "authorName";
	public final static String NOTICE_YN = "noticeYn";
	
}