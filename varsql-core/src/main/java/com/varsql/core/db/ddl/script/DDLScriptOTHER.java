package com.varsql.core.db.ddl.script;

import com.varsql.core.db.DBVenderType;
import com.varsql.core.db.MetaControlBean;

public class DDLScriptOTHER extends AbstractDDLScript {
	public DDLScriptOTHER(MetaControlBean dbInstanceFactory){
		super(dbInstanceFactory, DBVenderType.OTHER);
	}

}