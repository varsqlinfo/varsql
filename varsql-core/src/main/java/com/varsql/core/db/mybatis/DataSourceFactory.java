package com.varsql.core.db.mybatis;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.ibatis.datasource.unpooled.UnpooledDataSourceFactory;

/**
 *
 * @FileName  : SQLManager.java
 * @프로그램 설명 : mybatis manager
 * @Date      : 2018. 4. 12.
 * @작성자      : ytkim
 * @변경이력 :
 */
public final class DataSourceFactory extends UnpooledDataSourceFactory {

	public DataSourceFactory() {
		this.dataSource = new BasicDataSource();
	}
}