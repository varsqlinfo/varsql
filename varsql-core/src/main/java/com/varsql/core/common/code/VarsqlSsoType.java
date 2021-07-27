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
public enum VarsqlSsoType {

	URL
	,ALWAYS
	,NEVER;

	public static VarsqlSsoType of(String ssoType) {
		if(ssoType != null) {
			for (VarsqlSsoType type : values()) {
				if(ssoType.equalsIgnoreCase(type.name())) {
					return type;
				}
			}
		}

		return NEVER;
	}

}
