package com.varsql.db.ext.other;

import com.varsql.core.db.DBVenderType;
import com.varsql.core.db.MetaControlBean;
import com.varsql.core.db.ddl.script.AbstractDDLScript;

public class DDLScriptOTHER extends AbstractDDLScript {
	public DDLScriptOTHER(MetaControlBean dbInstanceFactory){
		super(dbInstanceFactory, DBVenderType.OTHER);
	}

}