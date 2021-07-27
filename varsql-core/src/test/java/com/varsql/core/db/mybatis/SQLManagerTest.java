package com.varsql.core.db.mybatis;

import java.util.List;

import org.junit.Ignore;
import org.junit.jupiter.api.Test;

import com.varsql.core.connection.beans.ConnectionInfo;
import com.varsql.core.db.DBType;
import com.varsql.core.db.MetaControlFactory;
import com.varsql.core.db.valueobject.DatabaseInfo;
import com.varsql.core.db.valueobject.DatabaseParamInfo;
import com.varsql.core.db.valueobject.TableInfo;
import com.varsql.core.test.BaseTest;

class SQLManagerTest extends BaseTest {
	
	@Ignore
	@Test
	void test() {

		ConnectionInfo connInfo  = getBaseConnection();

		try {
			DatabaseInfo di= getDatabaseInfo(connInfo);
			DatabaseParamInfo dpi = new DatabaseParamInfo();
			dpi.setSchema("PUBLIC");

			dpi.setConuid(connInfo.getConnid(), TEST_CON_UID, di);

			for (int i = 0; i < 10; i++) {
				//System.out.println(MetaControlFactory.getDbInstanceFactory(DBType.H2).getSchemas(dpi) + "  :: " +MetaControlFactory.getDbInstanceFactory(DBType.OTHER).getSchemas(dpi));
				
				List<TableInfo> tblList=MetaControlFactory.getDbInstanceFactory(DBType.OTHER).getDBObjectMeta("table", dpi);
				
				
				System.out.println(i+" : tblList.size() : "+ tblList.size());
				
//				for (TableInfo info :  tblList) {
//					System.out.println(VartechReflectionUtils.reflectionToString(info));
//				}

				if(i%1000==0) {
					System.out.println("111111111111");
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} 

	}

}
