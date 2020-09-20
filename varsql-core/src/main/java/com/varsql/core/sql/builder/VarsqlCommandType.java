package com.varsql.core.sql.builder;


/**
 *
 * @FileName : SqlCommandType.java
 * @프로그램 설명 :
 * @Date : 2015. 4. 8.
 * @작성자 : ytkim
 * @변경이력 :
 */
public enum VarsqlCommandType {
	SELECT("select"), UPDATE("update"), INSERT("insert"), DELETE("delete"), CREATE("create"), ALTER("alter"),DROP("drop"), TRUNCATE("truncate"), MERGE("merge"), REPLACE("replace"),CALL("call"), UNKNOWN("unknown");

	private String type;

	VarsqlCommandType(String _type) {
		this.type = _type;
	}

	public String val() {
		return this.type;
	}

	/**
	 * @method  : isUpdateCountCommand
	 * @desc :update count 가 존재하는 command
	 * @author   : ytkim
	 * @date   : 2020. 9. 17.
	 * @param type
	 * @return
	 */
	public static boolean isUpdateCountCommand(VarsqlCommandType type) {
		if(UPDATE.equals(type) || DELETE.equals(type) || INSERT.equals(type) || MERGE.equals(type)) {
			return true;
		}
		return false;
	}

	public static boolean isUpdateCountCommand(String commandType) {
		if(commandType.startsWith(UPDATE.name())
			||commandType.startsWith(DELETE.name())
			||commandType.startsWith(INSERT.name())
			||commandType.startsWith(MERGE.name())
				) {
			return true;
		}
		return false;
	}
}
