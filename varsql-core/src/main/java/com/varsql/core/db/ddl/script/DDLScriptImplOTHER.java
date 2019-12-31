package com.varsql.core.db.ddl.script;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.varsql.core.db.MetaControlBean;

public class DDLScriptImplOTHER extends DDLScriptImpl {
	Logger logger = LoggerFactory.getLogger(DDLScriptImplOTHER.class);
	
	public DDLScriptImplOTHER(MetaControlBean dbInstanceFactory){
		super(dbInstanceFactory);
	}
	
}