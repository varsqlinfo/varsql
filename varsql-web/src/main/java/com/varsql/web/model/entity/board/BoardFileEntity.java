package com.varsql.web.model.entity.board;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import org.hibernate.annotations.DynamicUpdate;

import com.varsql.web.constants.VarsqlJpaConstants;
import com.varsql.web.model.entity.FileBaseEntity;
import com.varsql.web.model.entity.app.FileInfoEntity;
import com.varsql.web.util.NumberUtils;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@DynamicUpdate
@NoArgsConstructor
@Entity
@Table(name = BoardFileEntity._TB_NAME)
public class BoardFileEntity extends FileBaseEntity{
	private static final long serialVersionUID = 1L;

	public final static String _TB_NAME="VTBOARD_FILE";

	@Id
	@TableGenerator(
		name = "boardFileSeqGenerator"
		,table= VarsqlJpaConstants.TABLE_SEQUENCE_NAME
		,pkColumnValue = _TB_NAME
		,allocationSize = 1
	)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "boardFileSeqGenerator")
	@Column(name ="FILE_ID", nullable = false)
	private long fileId;
	
	@Column(name ="BOARD_CODE", nullable = false)
	private String boardCode; 
	
	@Column(name ="CONT_TYPE", nullable = false)
	private String contType; 
	
	@Column(name ="CONT_ID", nullable = false)
	private long contId; 
	
	@Column(name ="FILE_FIELD_NAME")
	private String fileFieldName; 
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name ="CONT_ID", referencedColumnName = "COMMENT_ID", insertable = false, updatable = false)
	private BoardCommentEntity comment; 

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name ="CONT_ID", referencedColumnName = "ARTICLE_ID", insertable = false, updatable = false)
	private BoardEntity article;
	
	@Builder
	public BoardFileEntity (long fileId, String boardCode, long contId, String contType, String fileFieldName, String fileName, String filePath, String fileExt, long fileSize) {
		setFileId(fileId);
		setContId(contId);
		setContType(contType);
		setBoardCode(boardCode);
		setFileFieldName(fileFieldName);
		setFileName(fileName);
		setFilePath(filePath);
		setFileExt(fileExt);
		setFileSize(fileSize);
	}
	
	public final static String FILE_ID="fileId";
	public final static String BOARD_CODE="boardCode";
	public final static String CONT_TYPE="CONT_TYPE";
	public final static String FILE_FIELD_NAME="fileFieldName";
	
	public static BoardFileEntity toBoardFileEntity(FileInfoEntity item) {
		return BoardFileEntity.builder()
			.boardCode(item.getContGroupId())
			.contId(Long.parseLong(item.getFileContId()))
			.fileFieldName(item.getFileFieldName())
			.fileName(item.getFileName())
			.filePath(item.getFilePath())
			.fileExt(item.getFileExt())
			.fileSize(item.getFileSize())
			.build();
	}
	
	
	@PrePersist
	public void setContId () {
		if(NumberUtils.isNullOrZero(contId)) {
			if("board".equals(this.contType)) {
				this.contId = this.article.getArticleId();
			}else {
				this.contId = this.comment.getCommentId();
			}
		}
	}
}