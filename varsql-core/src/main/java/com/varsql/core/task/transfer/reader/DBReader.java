package com.varsql.core.task.transfer.reader;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.varsql.core.common.beans.ProgressInfo;
import com.varsql.core.common.code.VarsqlAppCode;
import com.varsql.core.common.job.AbstractJobHandler;
import com.varsql.core.common.job.JobExecuteResult;
import com.varsql.core.common.job.JobStatus;
import com.varsql.core.connection.ConnectionFactory;
import com.varsql.core.db.DBVenderType;
import com.varsql.core.db.valueobject.ColumnInfo;
import com.varsql.core.db.valueobject.DatabaseInfo;
import com.varsql.core.db.valueobject.SqlStatementInfo;
import com.varsql.core.exception.NotFoundException;
import com.varsql.core.exception.VarsqlRuntimeException;
import com.varsql.core.sql.SQLTemplateCode;
import com.varsql.core.sql.builder.SqlSource;
import com.varsql.core.sql.builder.SqlSourceBuilder;
import com.varsql.core.sql.executor.SelectExecutor;
import com.varsql.core.sql.executor.handler.SelectExecutorHandler;
import com.varsql.core.sql.executor.handler.SelectInfo;
import com.varsql.core.sql.template.SQLTemplateFactory;
import com.varsql.core.sql.util.SQLParamUtils;
import com.varsql.core.sql.util.SQLResultSetUtils;
import com.varsql.core.sql.util.SQLUtils;
import com.varsql.core.task.TaskConstants;
import com.varsql.core.task.TaskConstants.DB_READ_TYPE;
import com.varsql.core.task.TaskConstants.SOURCE_TYPE;
import com.varsql.core.task.transfer.SourceVO;
import com.vartech.common.io.writer.AbstractWriter;
import com.vartech.common.utils.StringUtils;

public class DBReader extends AbstractSourceReader{
	public DBReader(SourceVO sourceVO, AbstractJobHandler handler) {
		super(sourceVO, handler);
	}

	private final Logger logger = LoggerFactory.getLogger(DBReader.class);
	
	
	@Override
	public JobExecuteResult read() throws Exception {
		
		AbstractWriter writer = null;
		
		SqlStatementInfo seDto = new SqlStatementInfo();
		seDto.setLimit(-1);
		
		DatabaseInfo databaseInfo = sourceVO.getTypeInfo();
		
		seDto.setDatabaseInfo(databaseInfo);
		String sql ="";
		if(SOURCE_TYPE.TABLE.equals(sourceVO.getSourceType())) {
			sql = SQLUtils.generateSelectQuery(sourceVO.getSource(), "", DBVenderType.getDBType(databaseInfo.getType()));
		}else if(SOURCE_TYPE.SQL.equals(sourceVO.getSourceType())) {
			sql = sourceVO.getSource();
		}
		
		if(StringUtils.isBlank(sql)) {
			throw new NotFoundException(" sql info not valid : "+sourceVO);
		}
		
		seDto.setSql(sql);
		seDto.setUseColumnAlias(false);
		seDto.setSqlParam("{}");
		
		ProgressInfo progressInfo = sourceVO.getProgressInfo();
		JobExecuteResult jer;
		
		if(DB_READ_TYPE.CURSOR.equals(sourceVO.getReadType())) {
			jer = cursorRead(seDto, progressInfo, handler, writer);
		}else {
			jer = pagingRead(seDto, progressInfo, handler, writer);
		}
		
		return jer; 
		
	}
	
	/**
	 * 데이터를 페이지 단위로 읽어서 처리
	 * 
	 * @param seDto
	 * @param progressInfo
	 * @param handler
	 * @param writer
	 * @return
	 * @throws SQLException 
	 */
	private JobExecuteResult pagingRead(SqlStatementInfo seDto, ProgressInfo progressInfo, AbstractJobHandler handler,
			AbstractWriter writer) throws SQLException {
		
		DBVenderType dbType = DBVenderType.getDBType(seDto.getDatabaseInfo().getType());
		
		String sourceQuery = seDto.getSql();
		Map templateParam = new HashMap();
		
		templateParam.put("selectQuery", sourceQuery);
		StringBuilder sb = new StringBuilder();
		boolean firstFlag = true; 
		if(sourceVO.getSortColumns() != null) {
			for(ColumnInfo item:sourceVO.getSortColumns()) {
				sb.append(firstFlag ? "" : " ,").append(item.getName());
				firstFlag = false;
			}
			templateParam.put("sortColumn", sb.toString());
		}
		
		PreparedStatement stmt;
		ResultSet rs  = null;
		
		try(Connection conn = ConnectionFactory.getInstance().getConnection(seDto.getDatabaseInfo().getVconnid());
				){
			
			String countQuery = SQLTemplateFactory.getInstance().sqlRender(dbType, SQLTemplateCode.TABLE.selectCount, templateParam);
			
			logger.debug("count query : {}", countQuery);
			
			seDto.setSql(countQuery);
			
			// 카운트 구하기
			Map map  = SQLUtils.executeQueryOneItem(seDto, conn);
			
			if(map == null) throw new VarsqlRuntimeException(VarsqlAppCode.EC_TASK, "count data null");
			
			int totalCount = Integer.parseInt(map.getOrDefault("cnt", "0").toString());
			
			int pageSize = TaskConstants.getPageSize(sourceVO.getPageSize());
			
			logger.debug("read total count : {} , fetch count : {}", totalCount, pageSize);
			
			Map queryParam = new HashMap();
			queryParam.put("pageSize", pageSize);
			queryParam.put("totalCount", totalCount);
			queryParam.put("startRow", 0);
			queryParam.put("endRow", pageSize);
			
			handler.setTotal(totalCount);
			
			String pagingQuery = SQLTemplateFactory.getInstance().sqlRender(dbType, SQLTemplateCode.TABLE.selectPaging, templateParam);
			
			int loopCount = totalCount/pageSize + (totalCount%pageSize==0?0:1);
			
			SqlSource sqlSource = SqlSourceBuilder.getSqlSource(pagingQuery, queryParam, dbType);
			
			logger.debug("read query: {}", sqlSource.getQuery());
			
			stmt  = conn.prepareStatement(sqlSource.getQuery());
			
			for(int i =0;i < loopCount; i++) {
				int startIdx = i*pageSize; 
				
				queryParam.put("startRow", startIdx);
				queryParam.put("endRow", startIdx+ pageSize);
				
				logger.debug("loop total {}/{}, pagingInfo : {}", i+1, loopCount, queryParam);
				
				sqlSource.setOrginSqlParam(queryParam);
				
				SQLParamUtils.setSqlParameter(stmt, sqlSource);
				
				rs = stmt.executeQuery();
				
				List<Map> rows = SQLResultSetUtils.resultList(rs, dbType, false);
				
				for(Map row:rows) {
					handler.increaseIndex();
					
					try {
						JobStatus jobStatus = handler.handler(row);
						
						if(JobStatus.COMPLETED.equals(jobStatus)) {
							handler.increaseSuccess();
						}else if(JobStatus.FAIL.equals(jobStatus)) {
							handler.increaseFail();
						}
					} catch (Exception e) {
						handler.increaseFail();
						throw new VarsqlRuntimeException(VarsqlAppCode.EC_TASK, e);
					}
				}
			}
			
		}
		
		return null;
	}
	
	/**
	 * 데이터를 커서를 이동해서 하나씩 read
	 * 
	 * @param seDto
	 * @param progressInfo
	 * @param handler
	 * @param writer
	 * @return
	 * @throws SQLException
	 */
	private JobExecuteResult cursorRead(SqlStatementInfo seDto, ProgressInfo progressInfo, AbstractJobHandler handler, AbstractWriter writer) throws SQLException {
		// 페이징 처리 할것. 
		// cursor 과 페이징.
		
		JobExecuteResult jer = new SelectExecutor().execute(seDto, new SelectExecutorHandler(null) {
			private int rowIdx = 0;

			public boolean handle(SelectInfo handleParam) {

				if(progressInfo != null) {
					progressInfo.setProgressContentLength(++rowIdx);
				}

				Object rowObj = handleParam.getRowObject();
				
				handler.increaseIndex();
				
				try {
					JobStatus jobStatus = handler.handler(rowObj);
					
					if(JobStatus.COMPLETED.equals(jobStatus)) {
						handler.increaseSuccess();
					}else if(JobStatus.FAIL.equals(jobStatus)) {
						handler.increaseFail();
					}
				} catch (Exception e) {
					handler.increaseFail();
					throw new VarsqlRuntimeException(VarsqlAppCode.EC_TASK, e);
				}
				return true;
			}
		});
		
		return jer; 
		
	}

	@Override
	public void close() throws Exception {
		
	}
}
