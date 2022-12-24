package com.varsql.core.connection;

import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.varsql.core.common.beans.FileInfo;
import com.varsql.core.common.constants.VarsqlConstants;
import com.varsql.core.common.util.VarsqlJdbcUtil;
import com.varsql.core.configuration.prop.ValidationProperty;
import com.varsql.core.connection.beans.ConnectionInfo;
import com.varsql.core.connection.beans.JDBCDriverInfo;
import com.varsql.core.connection.beans.JdbcURLFormatParam;
import com.varsql.core.crypto.DBPasswordCryptionFactory;
import com.varsql.core.exception.ConnectionFactoryException;
import com.varsql.core.sql.util.JdbcUtils;
import com.vartech.common.crypto.EncryptDecryptException;
import com.vartech.common.utils.StringUtils;

@ConnectionInfoConfig(beanType = BeanType.JAVA)
public class SimpleConnectionInfoDao implements ConnectionInfoDao {

	private final Logger logger = LoggerFactory.getLogger(SimpleConnectionInfoDao.class);

	@Override
	public ConnectionInfo getConnectionInfo(String connid) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = ConnectionFactory.getInstance().getConnection();
			
			StringBuffer querySb = new StringBuffer();
			querySb.append(" select ");
			querySb.append("  a.*  ");
			querySb.append("  , b.DRIVER_PROVIDER_ID, b.DRIVER_CLASS, b.VALIDATION_QUERY, b.DB_TYPE, b.PATH_TYPE, b.DRIVER_PATH ");
			querySb.append("  , c.URL_FORMAT ");
			querySb.append(" from ");
			querySb.append("  VTCONNECTION a left outer join VTDBTYPE_DRIVER_PROVIDER b on a.VDRIVER = b.DRIVER_PROVIDER_ID ");
			querySb.append("  left outer join VTDBTYPE_DRIVER_TYPE c on b.DRIVER_ID = c.DRIVER_ID  ");
			querySb.append(" where 1=1 ");
			querySb.append(" and a.VCONNID = ? ");
										
			pstmt = conn.prepareStatement(querySb.toString());
			pstmt.setString(1, connid);
			rs = pstmt.executeQuery();
			if (rs == null) {
				throw new ConnectionFactoryException("not valid connection infomation :" + connid);
			}
			
			rs.next();

			ConnectionInfo.ConnectionInfoBuilder builder = ConnectionInfo.builder();
			
			String type =rs.getString("VTYPE").toLowerCase(); 
			
			builder.connid(rs.getString("VCONNID"));
			builder.aliasName(rs.getString("VNAME"));
			builder.type(type);
			String urlDirectYn = rs.getString("URL_DIRECT_YN");
			if ("Y".equals(urlDirectYn)) {
				builder.url(rs.getString("VURL"));
			} else {
				builder.url(VarsqlJdbcUtil.getJdbcUrl(rs.getString("URL_FORMAT"), JdbcURLFormatParam.builder()
					.serverIp(rs.getString("VSERVERIP"))
					.port(Integer.parseInt(rs.getString("VPORT")))
					.databaseName(rs.getString("VDATABASENAME"))
					.build())
				);
			}
			builder.username(rs.getString("VID"));
			
			String vpw = rs.getString("VPW");
			builder.password("");
			if (!StringUtils.isBlank(vpw)) {
				builder.password(DBPasswordCryptionFactory.getInstance().decrypt(rs.getString("VPW")));
			}
			
			builder.useColumnLabel(rs.getString("USE_COLUMN_LABEL"));
			builder.connectionOptions(rs.getString("VCONNOPT"));
			builder.maxActive(NumberUtils.toInt(rs.getString("MAX_ACTIVE"), 10));
			builder.minIdle(NumberUtils.toInt(rs.getString("MIN_IDLE"), 3));
			builder.connectionTimeOut(NumberUtils.toInt(rs.getString("TIMEOUT"), 18000));
			builder.exportCount(NumberUtils.toInt(rs.getString("EXPORTCOUNT"), 1000));
			builder.testWhileIdle("Y".equals(rs.getString("TEST_WHILE_IDLE")));
			builder.enableConnectionPool(!"N".equals(rs.getString("ENABLE_CONNECTION_POOL")));
			
			String conn_query = rs.getString("VQUERY");
			String dbvalidation_query = rs.getString("VALIDATION_QUERY");
			conn_query = (conn_query == null) ? "" : conn_query;
			dbvalidation_query = (dbvalidation_query == null) ? "" : dbvalidation_query;
			String validation_query = !"".equals(conn_query.trim()) ? conn_query
					: (!"".equals(dbvalidation_query.trim()) ? dbvalidation_query
							: ValidationProperty.getInstance().validationQuery(type));
			this.logger.debug("valication_query : {}", validation_query);
			builder.validationQuery(validation_query);
			
			String driverProviderId = rs.getString("DRIVER_PROVIDER_ID");
					
			pstmt.close(); 
			pstmt = conn.prepareStatement(" select FILE_NAME,FILE_PATH,FILE_EXT from VTDBTYPE_DRIVER_FILE where FILE_CONT_ID = ? ");
			pstmt.setString(1, driverProviderId);
			
			rs.close(); 
			rs = pstmt.executeQuery();
			if (rs == null) {
				throw new ConnectionFactoryException("jdbc driver file not found :" + connid);
			}
			
			List<FileInfo> jarFileList = new ArrayList<>();
			while (rs.next()) {
				FileInfo fi = new FileInfo();
				
				fi.setName(rs.getString("FILE_NAME"));
				fi.setPath(Paths.get(VarsqlConstants.UPLOAD_PATH).toAbsolutePath().resolve(rs.getString("FILE_PATH")).normalize().toAbsolutePath().toString());
				fi.setExt(rs.getString("FILE_EXT"));
				
				jarFileList.add(fi);
			}
			
			builder.jdbcDriverInfo(JDBCDriverInfo.builder()
				.driverId(rs.getString("DRIVER_PROVIDER_ID"))
				.driverClass(rs.getString("DRIVER_CLASS"))
				.driverFiles(jarFileList)
				.build()
			);
			
			return builder.build();
		} catch (EncryptDecryptException e) {
			this.logger.error("password decrypt error", e);
			throw new ConnectionFactoryException("password decrypt error", e);
		} catch (Exception e) {
			this.logger.error("empty connection info", e);
			throw new ConnectionFactoryException("empty connection info : [" + connid + "]", e);
		} finally {
			JdbcUtils.close(conn, pstmt, rs);
		}
	}
}
