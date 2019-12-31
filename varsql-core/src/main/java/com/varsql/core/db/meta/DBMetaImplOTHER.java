package com.varsql.core.db.meta;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.varsql.core.db.MetaControlBean;
import com.varsql.core.db.beans.DatabaseParamInfo;
import com.varsql.core.db.beans.TableInfo;

/**
 * 
 * @FileName : DBMetaImplOther.java
 * @작성자 	 : ytkim
 * @Date	 : 2014. 2. 13.
 * @프로그램설명:
 * @변경이력	:
 */
public class DBMetaImplOTHER extends DBMetaImpl{
	
	private static Logger log = LoggerFactory.getLogger(DBMetaImplOTHER.class);
	
	public DBMetaImplOTHER(MetaControlBean dbInstanceFactory){
		super(dbInstanceFactory);
		//serviceMenu.put("Functions", "not visible");
	}
}
