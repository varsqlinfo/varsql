package com.varsql.web.model.entity;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import com.varsql.web.model.base.AbstractRegAuditorModel;
import com.vartech.common.utils.FileUtils;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@MappedSuperclass
public class FileBaseEntity extends AbstractRegAuditorModel{
	private static final long serialVersionUID = 1L;

	@Column(name ="FILE_NAME")
	private String fileName; 

	@Column(name ="FILE_PATH")
	private String filePath; 

	@Column(name ="FILE_EXT")
	private String fileExt; 
	
	@Column(name ="FILE_SIZE")
	private long fileSize;
	
	@Column(name ="FILE_FIELD_NAME")
	private String fileFieldName; 

	public final static String FILE_NAME="fileName";
	public final static String FILE_PATH="filePath";
	public final static String FILE_EXT="fileExt";
	public final static String FILE_SIZE="fileSize";
	public final static String FILE_FIELD_NAME="fileFieldName";
	
	public String getDisplaySize() {
		return FileUtils.displaySize(fileSize);
	}
}