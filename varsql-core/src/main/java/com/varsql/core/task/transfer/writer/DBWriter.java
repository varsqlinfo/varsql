package com.varsql.core.task.transfer.writer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.varsql.core.common.code.VarsqlAppCode;
import com.varsql.core.connection.ConnectionFactory;
import com.varsql.core.db.MetaControlBean;
import com.varsql.core.db.MetaControlFactory;
import com.varsql.core.db.datatype.DataType;
import com.varsql.core.db.datatype.DataTypeFactory;
import com.varsql.core.db.servicemenu.DBObjectType;
import com.varsql.core.db.util.DataTypeUtils;
import com.varsql.core.db.valueobject.DatabaseInfo;
import com.varsql.core.db.valueobject.DatabaseParamInfo;
import com.varsql.core.db.valueobject.TableInfo;
import com.varsql.core.exception.BatchException;
import com.varsql.core.exception.NotFoundException;
import com.varsql.core.exception.VarsqlRuntimeException;
import com.varsql.core.sql.beans.ExportColumnInfo;
import com.varsql.core.sql.beans.GenQueryInfo;
import com.varsql.core.sql.util.SQLUtils;
import com.varsql.core.task.TaskConstants.DATA_WRITE_TYPE;
import com.varsql.core.task.TaskConstants.TRANSFER_TYPE;
import com.varsql.core.task.transfer.TargetVO;

/**
 * db writer
 * @author ytkim
 *
 */
public class DBWriter extends AbstractTargetWriter {
	private final Logger logger = LoggerFactory.getLogger(DBWriter.class);

	private Connection conn;
	private DatabaseInfo databaseInfo;
	
	private PreparedStatement insertStatement;
	private PreparedStatement updateStatement;
	private PreparedStatement deleteStatement;

	private ExportColumnInfo[] columnArr = null;
	private DataType[] dataTypeArr = null;

	private ExportColumnInfo[] updateColumnArr = null;
	private DataType[] updateDataTypeArr = null;
	
	private int batchCount;
	
	private TRANSFER_TYPE transferType;
	
	private boolean autoCommitFlag = false;

	private String sql;
	
	private boolean initFlag = false; 

	public DBWriter(TargetVO targetVO) {
		super(targetVO);
	}
	

	private void init() {
		this.databaseInfo = targetVO.getTypeInfo();
		this.conn = ConnectionFactory.getInstance().getConnection(this.databaseInfo.getVconnid());
		this.batchCount = targetVO.getCommitCount();

		MetaControlBean metaControlBean = MetaControlFactory.getDbInstanceFactory(databaseInfo.getType());
		DataTypeFactory dataTypeFactory = metaControlBean.getDataTypeImpl();
		
		
		DatabaseParamInfo dpi = new DatabaseParamInfo(databaseInfo);
		dpi.setObjectType(DBObjectType.TABLE.getObjectTypeId());
		dpi.setSchema(sql);

		List<TableInfo> tableInfo = metaControlBean.getDBObjectMeta(DBObjectType.TABLE.getObjectTypeId(), dpi, targetVO.getTarget());
		
		if(tableInfo.size() < 1) {
			throw new NotFoundException("table not found : " +  targetVO.getTarget());
		}
		
		List<ExportColumnInfo> columnList = new LinkedList<ExportColumnInfo>();
		tableInfo.get(0).getColList().forEach(columnInfo->{
			columnList.add(ExportColumnInfo.toColumnInfo(columnInfo));
		});
		
		GenQueryInfo insertQueryInfo = SQLUtils.generateInsertQuery(targetVO.getTarget(), columnList, dataTypeFactory);
		this.columnArr = insertQueryInfo.getColumns().toArray(new ExportColumnInfo[0]);
		
		if (DATA_WRITE_TYPE.TABLE.equals(targetVO.getWriteType())) {
			this.sql = insertQueryInfo.getSql();
		} else {
			this.sql = targetVO.getInfo();
		}
		
		this.dataTypeArr = DataTypeUtils.getExportColumnInfoToDataTypes(this.columnArr, dataTypeFactory);
		this.transferType = targetVO.getTransferType();
		
		logger.debug("transfer start type :{}", this.transferType.name());
		try {
			
			try {
				this.autoCommitFlag = this.conn.getAutoCommit();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			this.conn.setAutoCommit(false);
			
			if(TRANSFER_TYPE.CLEAR_INSERT.equals(this.transferType)) {
				//String query = SQLUtils.generateDeleteQuery(targetVO.getTarget(), dataTypeFactory);
				String query = SQLUtils.generateTruncateQuery(targetVO.getTarget(), dataTypeFactory);
				logger.debug("data clear query start  :{}", query);
				this.deleteStatement = this.conn.prepareStatement(query);
				this.deleteStatement.execute();
				logger.debug("data clear success");
				this.deleteStatement.close();
				
			}else if(TRANSFER_TYPE.INSERT_UPDATE.equals(this.transferType)) {
				
				if(targetVO.getTableRowKey().size() < 1) {
					throw new VarsqlRuntimeException(VarsqlAppCode.EC_TASK, "table row key empty : "+ targetVO.getTableRowKey());
				}
				
				GenQueryInfo queryInfo = SQLUtils.generateUpdateConditionQuery(targetVO.getTarget(), columnList, targetVO.getTableRowKey(), dataTypeFactory);
				
				this.updateColumnArr = queryInfo.getColumns().toArray(new ExportColumnInfo[0]);
				this.updateDataTypeArr = DataTypeUtils.getExportColumnInfoToDataTypes(this.updateColumnArr, dataTypeFactory);
				
				this.updateStatement = this.conn.prepareStatement(queryInfo.getSql());
				logger.debug("update query : {}", queryInfo.getSql());
			}
			
			this.insertStatement = this.conn.prepareStatement(this.sql);
		} catch (SQLException e) {
			logger.error("create statement error :{}", e.getMessage(), e);
			throw new VarsqlRuntimeException(VarsqlAppCode.EC_TASK, "create statement error : "+ e.getMessage());
		}
	}

	@Override
	public void update(Map rowInfo) throws SQLException {
		if(!initFlag) {
			initFlag = true; 
			this.init();
		}
		
		increaseTotalCount();
		
		if (getTotalCount() % batchCount == 0) {
			this.conn.commit();
		}
		
		if(TRANSFER_TYPE.INSERT_UPDATE.equals(this.transferType)) {
			
			try {
				for (int i = 0; i < updateColumnArr.length; i++) {
					SQLUtils.setStatementParameter(this.updateStatement, updateDataTypeArr[i], i+1, rowInfo, updateColumnArr[i].getName());
				}
				
				int cnt = this.updateStatement.executeUpdate();
				
				if(cnt > 0) {
					increaseUpdateCount();
					logger.debug("update cnt : {},  info : {}", cnt,  rowInfo);
					return ; 
				}
			}catch(SQLException e) {
				logger.error("update statement error :{}, rowInfo :{} ", e.getMessage(), rowInfo);
				if(!this.targetVO.isErrorIgnore()) {
					this.conn.rollback();
					throw e;
				}
			}
		}
		
		logger.debug("insert info : {}", rowInfo);
		for (int i = 0; i < columnArr.length; i++) {
			SQLUtils.setStatementParameter(this.insertStatement, dataTypeArr[i], i+1, rowInfo, columnArr[i].getName());
		}
		
		increaseInsertCount();

		insertStatement.addBatch();
		insertStatement.clearParameters();

		if (getInsertCount() % batchCount == 0) {
			try {
				SQLUtils.executeBatch(insertStatement, getTotalCount(), batchCount);
			}catch(SQLException e) {
				increaseFailCount();
				logger.error("insert batch error :{}, rowInfo :{} ", e.getMessage(), rowInfo);
				if(!this.targetVO.isErrorIgnore()) {
					this.conn.rollback();
					throw e;
				}
			}
			
			this.conn.commit();
		}
	}

	@Override
	public void close() throws Exception {
		if (this.conn != null && !this.conn.isClosed()) {
			
			if (getInsertCount() % batchCount != 0) {
				try {
					SQLUtils.executeBatch(insertStatement, getTotalCount(), batchCount);
					this.conn.commit();
				}catch(BatchException e) {
					addFailCount(e.getFailCount());
					if(!this.targetVO.isErrorIgnore()) {
						this.conn.rollback();
						throw e;
					}
				}catch(SQLException e) {
					increaseFailCount();
					if(!this.targetVO.isErrorIgnore()) {
						this.conn.rollback();
						throw e;
					}
				}
			}
			
			this.conn.setAutoCommit(autoCommitFlag);
			
			if(this.deleteStatement != null && !this.deleteStatement.isClosed()) this.deleteStatement.close();
			if(this.updateStatement != null && !this.updateStatement.isClosed()) this.updateStatement.close();
			if(this.insertStatement != null && !this.insertStatement.isClosed()) this.insertStatement.close();
			this.conn.close();
		}

	}

}
