package com.varsql.core.db.mybatis;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.SqlSessionUtils;

import com.querydsl.sql.SQLTemplates;
import com.varsql.core.connection.ConnectionFactory;
import com.varsql.core.connection.beans.ConnectionInfo;
import com.varsql.core.db.DBType;
import com.varsql.core.db.MetaControlFactory;
import com.varsql.core.db.valueobject.DatabaseInfo;
import com.varsql.core.db.valueobject.DatabaseParamInfo;
import com.varsql.core.db.valueobject.TableInfo;
import com.varsql.core.exception.ConnectionFactoryException;
import com.vartech.common.utils.VartechReflectionUtils;

class SQLManagerTest {

	@Test
	void test() {

		ConnectionInfo connInfo  = new ConnectionInfo();

		String testUid = "test222";

		connInfo.setConnid(testUid);

		connInfo.setAliasName(testUid);
		
		connInfo.setType(DBType.H2.name());
		connInfo.setDriver("org.h2.Driver");
		connInfo.setUrl("jdbc:h2:file:C:/zzz/resources/varsql");
		connInfo.setUsername("sa");
		connInfo.setPassword("sa");
		
		connInfo.setValidation_query("select 1");
		connInfo.setPool_opt("max_idle=5;min_idle=5;");
		connInfo.setConnection_opt("sslConnection=true");
		connInfo.setTimebetweenevictionrunsmillis(60000);
		connInfo.setTest_while_idle("true");

		connInfo.setMax_active(2);
		connInfo.setMin_idle(1);

		try {
			ConnectionFactory.getInstance().createPool(connInfo);
			DatabaseInfo di = new DatabaseInfo(testUid, testUid , connInfo.getType(), connInfo.getAliasName(), ""
					, "Y","N",1, "Y", -1);

			DatabaseParamInfo dpi = new DatabaseParamInfo();
			dpi.setSchema("PUBLIC");

			dpi.setConuid(connInfo.getConnid(), testUid, di);

			for (int i = 0; i < 10000; i++) {
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

		} catch (ConnectionFactoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
