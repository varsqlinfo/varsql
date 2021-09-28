package com.varsql.web.constants;

public interface VarsqlParamConstants {

	final public String VCONNID ="vconnid";
	final public String CONN_UUID ="conuid";
	final public String SQL ="sql";
	final public String LIMIT ="limit";
	final public String SQL_PARAM ="sqlParam";

	/**
	 * 사용자 관련 파라미터 정보
	 */
	final public String UID ="uid";
	final public String VIEWID ="viewid";
	final public String ROLE ="role";

	/**
	 * 페이징 관련 파라미터 정보
	 */
	final public String SEARCHVAL ="searchVal";
	final public String SEARCH_NO ="page";
	final public String SEARCH_FIRST ="first";
	final public String SEARCH_START_DATE ="s_date";
	final public String SEARCH_END_DATE ="e_date";
	final public int SEARCH_DEFAULT_FIRST =0;
	final public String SEARCH_ROW ="rows";
	final public int SEARCH_DEFAULT_ROW =10;

	/**
	 * export 파라미터
	 */
	final public String EXPORT_TYPE ="exportType";
	final public String EXPORT_COLUMN_INFO ="columnInfo";

	final public String DB_TYPE ="dbtype";
	final public String DB_SCHEMA ="schema";
	final public String DB_OBJECT_NAME ="name";
	final public String DB_TABLE ="table";

	final public String SCREEN_CONFIG_INFO ="screen_config_info";
	final public String DATABASE_SCREEN_SETTING ="database_screen_setting";
	final public String SETTING_INFO ="settingInfo";
	final public String JSON_REUSLT ="result";
	final public String JSON_PAGING ="paging";

	final public String BOARD_CODE = "boardCode";
	final public String DOC_ID = "docId";
}
