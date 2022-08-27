package com.varsql.web.model.entity.board;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;

import com.varsql.web.constants.VarsqlJpaConstants;
import com.varsql.web.model.base.AbstractAuditorModel;
import com.varsql.web.model.converter.BooleanToDelYnConverter;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@DynamicUpdate
@NoArgsConstructor
@Entity
@ToString
@Table(name = BoardCommentEntity._TB_NAME)
public class BoardCommentEntity extends AbstractAuditorModel{
	
	private static final long serialVersionUID = 1L;

	public final static String _TB_NAME = "VTBOARD_COMMENT";
	
	@Id
	@TableGenerator(
		name = "commentIdGenerator"
		,table= VarsqlJpaConstants.TABLE_SEQUENCE_NAME
		,pkColumnValue = _TB_NAME
		,allocationSize = 1
	)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "commentIdGenerator")
	@Column(name = "COMMENT_ID", nullable = false)
	private long commentId;
	
	@Column(name = "ARTICLE_ID", nullable = false)
	private long articleId;
	
	@Column(name = "BOARD_CODE", nullable = false)
	private String boardCode;
	
	@Column(name = "PARENT_COMMENT_ID")
	private long parentCommentId;
	
	@Column(name ="GRP_COMMENT_ID")
	private long grpCommentId; 
	
	@Column(name ="GRP_SEQ")
	private long grpSeq; 
	
	@Column(name ="INDENT")
	private int indent; 

	@Column(name = "CONTENTS")
	private String contents;

	@Column(name = "AUTHOR_NAME")
	private String authorName;
	
	@Column(name = "DEL_YN")
	@Convert(converter = BooleanToDelYnConverter.class)
	private boolean delYn;
	
	@OneToMany(mappedBy = "comment", fetch = FetchType.LAZY , cascade = CascadeType.ALL, orphanRemoval = true)
	@Where(clause = "CONT_TYPE = 'comment' ")
	private List<BoardFileEntity> fileList = new ArrayList<>(); 
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="PARENT_COMMENT_ID", insertable = false, updatable = false)
	private BoardCommentEntity parent; 
	
	@OneToMany(fetch=FetchType.LAZY, mappedBy = "parent")
	private List<BoardCommentEntity> children;
	
	@Builder
	public BoardCommentEntity(long commentId, long articleId, String boardCode, long parentCommentId, long grpCommentId, long grpSeq,
			String contents, String authorName, int indent, boolean delYn) {
		this.commentId = commentId;
		this.articleId = articleId;
		this.boardCode = boardCode;
		this.parentCommentId = parentCommentId;
		this.grpCommentId = grpCommentId;
		this.grpSeq = grpSeq;
		this.contents = contents;
		this.authorName = authorName;
		this.indent = indent;
		this.delYn = delYn;
	}

	public final static String COMMENT_ID = "commentId";
	public final static String ARTICLE_ID = "articleId";
	public final static String BOARD_CODE = "boardCode";
	public final static String PARENT_COMMENT_ID = "parentCommentId";
	public final static String GRP_COMMENT_ID="grpCommentId";
	public final static String GRP_SEQ="grpSeq";
	public final static String CONTENTS = "contents";
	public final static String AUTHOR_NAME = "authorName";
	public final static String INDENT = "indent";
	public final static String DEL_YN = "delYn";
}