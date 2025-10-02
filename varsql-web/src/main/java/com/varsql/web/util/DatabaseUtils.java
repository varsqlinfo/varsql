package com.varsql.web.util;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.varsql.core.db.DBVenderType;
import com.varsql.core.db.MetaControlBean;
import com.varsql.core.db.MetaControlFactory;
import com.varsql.core.db.report.VarsqlReportConfig;
import com.varsql.core.db.util.DbMetaUtils;
import com.varsql.core.db.valueobject.DatabaseInfo;
import com.varsql.core.db.valueobject.DatabaseParamInfo;
import com.varsql.web.model.entity.db.DBConnectionEntity;
import com.varsql.web.security.User;
import com.vartech.common.app.beans.EnumMapperValue;

/**
 * -----------------------------------------------------------------------------
* @fileName		: DatabaseUtils.java
* @desc		: 
* @author	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
*2020. 4. 10. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
public final class DatabaseUtils {
	
	private DatabaseUtils(){}
	
	/**
	 * dbConnectionEntity to DatabaseParamInfo
	 *
	 * @method : dbConnectionEntityToDatabaseParamInfo
	 * @param vtConnRVO
	 * @return
	 */
	public static DatabaseParamInfo dbConnectionEntityToDatabaseParamInfo(DBConnectionEntity dbConnectionEntity) {
		return new DatabaseParamInfo(dbConnectionEntityToDatabaseInfo(dbConnectionEntity));
	}
	
	/**
	 * dbConnectionEntity to DatabaseInfo
	 *
	 * @method : dbConnectionEntityToDatabaseInfo
	 * @param dbConnectionEntity
	 * @return
	 */
	public static DatabaseInfo dbConnectionEntityToDatabaseInfo(DBConnectionEntity dbConnectionEntity) {
		return DatabaseInfo.builder()
			.vconnid(dbConnectionEntity.getVconnid())
			.type(dbConnectionEntity.getDbTypeDriverProvider().getDbType())
			.name(dbConnectionEntity.getVname())
			.basetableYn(dbConnectionEntity.getBasetableYn())
			.lazyLoad(dbConnectionEntity.getLazyloadYn())
			.version(dbConnectionEntity.getVdbversion())
			.schemaViewYn(dbConnectionEntity.getSchemaViewYn())
			.maxSelectCount(ConvertUtils.intValue(dbConnectionEntity.getMaxSelectCount()))
			.maxExportCount(ConvertUtils.intValue(dbConnectionEntity.getExportcount()))
			.build();
	}
	
	/**
	 * db connection uid -> vconnid
	 *
	 * @method : getVconnid
	 * @param conuid
	 * @return
	 */
	public static String convertConUidToVconnid(String conuid) {
		User user = SecurityUtil.loginInfo();
		if(user.getDatabaseInfo().containsKey(conuid)) {
			return user.getDatabaseInfo().get(conuid).getVconnid();
		}
		return null;
	}
	
	/**
	 * 스키마 목록.
	 * 
	 * @param conuid
	 * @return
	 * @throws SQLException
	 */
	public static List<String> schemaList(String conuid) throws SQLException {
		DatabaseParamInfo dpi = new DatabaseParamInfo(SecurityUtil.userDBInfo(conuid));
		return schemaList(dpi);
	}
	
	/**
	 * 스키마 목록
	 * @param dpi
	 * @return
	 * @throws SQLException
	 */
	public static List<String> schemaList(DatabaseParamInfo dpi) throws SQLException {
		return DbMetaUtils.schemaList(dpi);
	}
	
}
