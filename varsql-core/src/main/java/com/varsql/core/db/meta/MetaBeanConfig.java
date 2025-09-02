package com.varsql.core.db.meta;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.varsql.core.db.DBVenderType;
import com.varsql.core.db.datatype.DataTypeFactory;
import com.varsql.core.db.ddl.script.DDLScript;
import com.varsql.core.db.report.table.TableReport;
import com.varsql.core.db.report.table.TableReportOTHER;
import com.varsql.core.sql.StatementSetter;
import com.varsql.core.sql.StatementSetterOther;
import com.varsql.core.sql.type.CommandTypeFactory;
import com.varsql.core.sql.type.CommandTypeFactoryOther;
import com.varsql.db.ext.other.DBMetaOTHER;
import com.varsql.db.ext.other.DDLScriptOTHER;
import com.varsql.db.ext.other.DataTypeFactoryOTHER;

/**
 * db vender 별 bean 설정   
 * @author ytkim
 *
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface MetaBeanConfig {
	Class<? extends DBMeta> metaBean()  default DBMetaOTHER.class;

	Class<? extends DDLScript> ddlBean()  default DDLScriptOTHER.class;

	Class<? extends DataTypeFactory> dataTypeBean() default DataTypeFactoryOTHER.class; 

	Class<? extends TableReport> tableReportBean() default TableReportOTHER.class;
	
	Class<? extends CommandTypeFactory> commandTypeBean() default CommandTypeFactoryOther.class;

	Class<? extends StatementSetter> statementSetterBean() default StatementSetterOther.class;

	DBVenderType dbVenderType();

	boolean primary() default false;
}