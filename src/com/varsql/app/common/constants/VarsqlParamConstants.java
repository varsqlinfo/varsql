package com.varsql.app.common.constants;

public interface VarsqlParamConstants {
	
	public String VCONNID ="vconnid";
	public String CONN_UUID ="conuid";
	public String SQL ="sql";
	public String LIMIT ="limit";
	public int SQL_MAX_ROW =1000;
	public String SQL_PARAM ="sqlParam";
	
	/**
	 * 페이징 관련 파라미터 정보
	 */
	public String SEARCHVAL ="searchVal";
	public String SEARCH_NO ="page";
	public String SEARCH_FIRST ="first";
	public String SEARCH_START_DATE ="s_date";
	public String SEARCH_END_DATE ="e_date";
	public int SEARCH_DEFAULT_FIRST =0;
	public String SEARCH_ROW ="rows";
	public int SEARCH_DEFAULT_ROW =10;
	
	/**
	 * export 파라미터 
	 */
	public String EXPORT_TYPE ="exportType";
	public String EXPORT_COLUMN_INFO ="columnInfo";
	
	public String DB_TYPE ="dbtype";
	public String DB_SCHEMA ="schema";
	public String DB_OBJECT_NAME ="name";
	public String DB_TABLE ="table";
	
	public String SCREEN_CONFIG_INFO ="screen_config_info";
	public String DATABASE_SCREEN_SETTING ="database_screen_setting";
	public String JSON_REUSLT ="result";
	public String JSON_PAGING ="paging";
}
