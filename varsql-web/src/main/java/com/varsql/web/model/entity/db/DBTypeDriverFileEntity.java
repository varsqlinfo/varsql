package com.varsql.web.model.entity.db;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;

import com.varsql.web.model.entity.FileBaseEntity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@DynamicUpdate
@NoArgsConstructor
@Entity
@Table(name = DBTypeDriverFileEntity._TB_NAME)
public class DBTypeDriverFileEntity extends FileBaseEntity {
	private static final long serialVersionUID = 1L;

	public static final String _TB_NAME = "VTDBTYPE_DRIVER_FILE";

	@Id
	@Column(name = "FILE_ID")
	private String fileId;

	@Column(name = "FILE_DIV")
	private String fileDiv;

	@Column(name = "FILE_CONT_ID")
	private String fileContId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FILE_CONT_ID", referencedColumnName = "DRIVER_PROVIDER_ID", insertable = false, updatable = false)
	private DBTypeDriverProviderEntity driverProviderFiles;

	@Builder
	public DBTypeDriverFileEntity(String fileId, String fileDiv, String fileContId, String fileName, String filePath, String fileExt, long fileSize) {
		setFileId(fileId);
		setFileDiv(fileDiv);
		setFileContId(fileContId);
		setFileName(fileName);
		setFilePath(filePath);
		setFileExt(fileExt);
		setFileSize(fileSize);
	}

	public static final String FILE_ID = "fileId";

	public static final String FILE_DIV = "fileDiv";

	public static final String FILE_CONT_ID = "fileContId";
	
	public static final String FILE_SIZE = "fileSize";
}
