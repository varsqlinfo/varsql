package com.varsql.core.sql.type;

import java.util.regex.Pattern;

import com.varsql.core.sql.type.function.SQLCheckFunction;
import com.vartech.common.utils.StringUtils;

public enum SQLCommandType implements SQLCommand {

	// DDL
	CREATE(false, false), DROP(true, false), ALTER(true, false), TRUNCATE(true, false), COMMENT(true, false), RENAME(true, false),
	
	// DML
	SELECT(true, true), INSERT(true, false), DELETE(true, false), UPDATE(true, false), MERGE(true, false), LOCK(true, false), CALL(true, false), EXPLAIN_PLAN(true, false), WITH_SUBQUERY(true, true),
	
	// DCL
	GRANT(true, false), REVOKE(true, false),
	
	// 
	COMMIT(true, false), ROLLBACK(true, false), SAVEPOINT(true, false), SET_TRANSACTION(true, false),
	
	OTHER(true, false);
	
	private SQLCheckFunction sqlCheckFunction;
	private boolean semicolonRemove;
	private boolean selectCommand;
	
	private Pattern pattern = Pattern.compile(";\\s*$");
	
	SQLCommandType(boolean semicolonRemove, boolean selectCommand) {
		this(semicolonRemove, selectCommand, null);
	}
	
	SQLCommandType(boolean semicolonRemove, boolean selectCommand, SQLCheckFunction scf) {
		this.semicolonRemove = semicolonRemove; 
		this.selectCommand = selectCommand; 
		
		if(scf ==null) {
			if(semicolonRemove) {
				this.sqlCheckFunction =(sql)->{
					return pattern.matcher(sql).replaceFirst(""); 
				};
			}else {
				this.sqlCheckFunction =(sql)->{
					return sql; 
				};
			}
		}else {
			this.sqlCheckFunction = scf; 
		}
	}

	public String getCommandName() {
		return this.name();
	}

	@Override
	public String checkSql(String sql) {
		return sqlCheckFunction.checkSQl(sql);
	}
	
	@Override
	public SQLCommandType getCommand() {
		return this;
	}
	
	public boolean isSelectCommand() {
		return selectCommand;
	}

	public static SQLCommand getCommandType(String command) {
		try {
            return valueOf(command.toUpperCase());
        } catch (Exception e) {
            return SQLCommandType.OTHER;
        }
	}
	
	public static SQLCommandType getSQLCommandType(String command) {
		try {
			return valueOf(command.toUpperCase());
		} catch (Exception e) {
			return SQLCommandType.OTHER;
		}
	}
	
	/**
	 * @method  : isUpdateCountCommand
	 * @desc :update count 가 존재하는 command
	 * @author   : ytkim
	 * @date   : 2020. 9. 17.
	 * @param type
	 * @return
	 */
	public static boolean isUpdateCountCommand(SQLCommandType type) {
		if(UPDATE.equals(type) || DELETE.equals(type) || INSERT.equals(type) || MERGE.equals(type)) {
			return true;
		}
		return false;
	}

	public static boolean isUpdateCountCommand(String commandType) {
		if(StringUtils.isBlank(commandType)) return false; 
		
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

