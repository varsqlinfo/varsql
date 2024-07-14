package com.varsql.core.common.code;

/**
 *
*-----------------------------------------------------------------------------
* @PROJECT	: varsql
* @NAME		: VarsqlFileType.java
* @DESC		: varsql file type
* @AUTHOR	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
* 2019. 4. 18. 			ytkim			최초작성
*-----------------------------------------------------------------------------
 */
public enum VarsqlFileType {

	SQL(-1)
	,CSV(-1)
	,JSON(-1)
	,XML(-1)
	,TEXT(-1)
	,HTML(-1)
	,EXCEL(1048575 ,"xlsx") // 1048576 갯수는 이거 0 부터 시작하기 때문에 1이 빠짐. 
	,ZIP(-1);

	private long limitCount;
	private String extension;

	VarsqlFileType(long limitCount){
		this(limitCount,null);
	}

	VarsqlFileType(long limitCount, String extension){
		this.limitCount = limitCount;

		if(extension == null) {
			extension = this.name();
		}

		this.extension =extension.toLowerCase();
	}

	public long getLimitCount() {
		return limitCount;
	}
	
	public String getExtension() {
		return this.extension;
	}

	public String concatExtension(String fileName) {
		return fileName+"."+this.extension;
	}

	public static VarsqlFileType getFileType(String ext) {
		if(ext != null) {
			for (VarsqlFileType type : values()) {
				if(ext.equalsIgnoreCase(type.name())) {
					return type;
				}
			}
		}

		return SQL;
	}

}
