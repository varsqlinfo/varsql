package com.varsql.core.db.meta;

import com.varsql.core.db.MetaControlBean;

/**
 *
 * @FileName : DBMetaOTHER.java
 * @작성자 	 : ytkim
 * @Date	 : 2014. 2. 13.
 * @프로그램설명:
 * @변경이력	:
 */
public class DBMetaOTHER extends AbstractDBMeta{

	public DBMetaOTHER(MetaControlBean dbInstanceFactory){
		super(dbInstanceFactory, null);
		//serviceMenu.put("Functions", "not visible");
	}
}
