package com.varsql.core.sql.executor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.junit.Ignore;
import org.junit.jupiter.api.Test;

import com.varsql.core.connection.beans.ConnectionInfo;
import com.varsql.core.data.writer.SQLWriter;
import com.varsql.core.db.DBType;
import com.varsql.core.db.valueobject.DatabaseInfo;
import com.varsql.core.db.valueobject.SqlStatementInfo;
import com.varsql.core.sql.executor.handler.AbstractSQLExecutorHandler;
import com.varsql.core.sql.executor.handler.SQLHandlerParameter;
import com.varsql.core.test.BaseTest;

class BaseExecutorTest extends BaseTest {

	//@Test
	//@Ignore
	void testUpdate() {
		ConnectionInfo connInfo  = getBaseConnection();

		try {
			DatabaseInfo di= getDatabaseInfo(connInfo);
			SqlStatementInfo sqi = new SqlStatementInfo();
			sqi.setSchema("PUBLIC");

			sqi.setConuid(connInfo.getConnid(), TEST_CON_UID, di);

			SQLExecutor baseExecutor = new SqlUpdateExecutor();

			sqi.setSqlParam("{}");

			String insertQuery = getResourceContent("/db/importdata/insertdata.sql");
			sqi.setSql(insertQuery);

			SQLExecuteResult ser =baseExecutor.execute(sqi, new AbstractSQLExecutorHandler() {

				@Override
				public boolean handle(SQLHandlerParameter handleParam) {
					return true;
				}
			});

			System.out.println("testUpdate ser.getTotalCount(): "+ ser.getTotalCount());
			System.out.println("testUpdate ser.getResult(): "+ ser.getResult());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	//@Ignore
	void testSelect() {
		ConnectionInfo connInfo  = getBaseConnection();

		try {
			DatabaseInfo di= getDatabaseInfo(connInfo);
			SqlStatementInfo sqi = new SqlStatementInfo();
			sqi.setSchema("PUBLIC");

			sqi.setConuid(connInfo.getConnid(), TEST_CON_UID, di);

			SQLExecutor baseExecutor = new SelectExecutor();

			sqi.setSqlParam("{}");
			sqi.setSql("select * from test2222");

			SQLWriter writer = new SQLWriter(new FileOutputStream(new File("c:/zzz/exportInsertQuery.sql")), DBType.getDBType(sqi.getDbType()), "test2222");

			AbstractSQLExecutorHandler handler = new AbstractSQLExecutorHandler() {
				private boolean firstFlag = true;

				@Override
				public boolean handle(SQLHandlerParameter handleParam) {
					if(firstFlag) {
						writer.setColumnInfos(handleParam.getColumnInfoList());
						firstFlag =false;
					}

					try {
						writer.addRow(handleParam.getRowObject());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					return true;
				}

			};

			SQLExecuteResult ser = baseExecutor.execute(sqi, handler);

			writer.write();

			System.out.println("testUpdate ser.getTotalCount(): "+ ser.getTotalCount());
			System.out.println("testSelect ser.getResult(): "+ ser.getResult());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
