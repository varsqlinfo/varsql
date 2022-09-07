package com.varsql.web.util;

import com.varsql.core.auth.User;
import com.varsql.core.common.util.SecurityUtil;
import com.varsql.core.db.valueobject.DatabaseInfo;
import com.varsql.core.db.valueobject.DatabaseParamInfo;
import com.varsql.web.model.entity.db.DBConnectionEntity;

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
			.schema(dbConnectionEntity.getVdbschema())
			.basetableYn(dbConnectionEntity.getBasetableYn())
			.lazyLoad(dbConnectionEntity.getLazyloadYn())
			.version(ConvertUtils.longValueOf(dbConnectionEntity.getVdbversion()))
			.schemaViewYn(dbConnectionEntity.getSchemaViewYn())
			.maxSelectCount(ConvertUtils.intValue(dbConnectionEntity.getMaxSelectCount()))
			.useColumnLabel(dbConnectionEntity.getUseColumnLabel())
			.databaseName(dbConnectionEntity.getVdatabasename())
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
	
}
