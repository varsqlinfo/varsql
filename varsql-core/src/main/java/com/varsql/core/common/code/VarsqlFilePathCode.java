package com.varsql.core.common.code;

/**
 *
*-----------------------------------------------------------------------------
* @PROJECT	: varsql
* @NAME		: VarsqlFilePathCode.java
* @DESC		: varsql File Path
* @AUTHOR	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
* 2019. 4. 18. 			ytkim			최초작성
*-----------------------------------------------------------------------------
 */
public enum VarsqlFilePathCode {

	EXPORT_PATH("exportFile")
	,IMPORT("fileImport")
	,UPLOAD("appFile")
	,OTHER("appOther");

	private String div;

	VarsqlFilePathCode(String div){
		this.div = div;
	}

	public static VarsqlFilePathCode getFileType(String div) {
		if(div != null) {
			for (VarsqlFilePathCode type : values()) {
				if(div.equalsIgnoreCase(type.name())) {
					return type;
				}
			}
		}

		return OTHER;
	}


	public String getDiv() {
		return div;
	}

}
