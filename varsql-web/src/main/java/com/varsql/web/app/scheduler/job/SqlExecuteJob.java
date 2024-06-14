package com.varsql.web.app.scheduler.job;
import java.sql.Connection;

import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.varsql.core.common.code.VarsqlAppCode;
import com.varsql.core.connection.ConnectionFactory;
import com.varsql.core.db.DBVenderType;
import com.varsql.core.db.valueobject.DatabaseInfo;
import com.varsql.core.exception.VarsqlRuntimeException;
import com.varsql.core.sql.builder.SqlSource;
import com.varsql.core.sql.builder.SqlSourceBuilder;
import com.varsql.core.sql.builder.SqlSourceResultVO;
import com.varsql.core.sql.executor.handler.SelectExecutorHandler;
import com.varsql.core.sql.executor.handler.SelectInfo;
import com.varsql.core.sql.util.JdbcUtils;
import com.varsql.core.sql.util.SQLUtils;
import com.varsql.web.app.scheduler.JobType;
import com.varsql.web.app.scheduler.bean.JobBean;
import com.varsql.web.dto.JobResultVO;
import com.varsql.web.dto.SqlExecuteJobVO;
import com.varsql.web.dto.db.SqlExecuteDTO;
import com.varsql.web.dto.scheduler.JobVO;
import com.varsql.web.repository.db.DBConnectionEntityRepository;
import com.vartech.common.utils.VartechUtils;

@Component
public class SqlExecuteJob extends JobBean {
	private final Logger logger = LoggerFactory.getLogger(SqlExecuteJob.class);
	
	@Autowired
	private DBConnectionEntityRepository dbConnectionEntityRepository; 

	@Override
	public JobResultVO doExecute(JobExecutionContext context, JobVO jsv) throws Exception {
		logger.debug("## sql execute job start : {}", jsv);
		
		String vconnid = jsv.getVconnid(); 
		
		DatabaseInfo databaseInfo = dbConnectionEntityRepository.findDatabaseInfo(vconnid);
		
		if(databaseInfo ==null) {
			throw new VarsqlRuntimeException(VarsqlAppCode.EC_SCHEDULER ,"Sql Execute connection info not found vconnid : "+vconnid);
		}
		
		databaseInfo.setMaxSelectCount(-1);
		
		SqlExecuteJobVO sqlExecuteJobVO = VartechUtils.jsonStringToObject(jsv.getJobData(), SqlExecuteJobVO.class, true);
		
		SqlExecuteDTO seDto = new SqlExecuteDTO();
		seDto.setDatabaseInfo(databaseInfo);
		seDto.setSqlParam(sqlExecuteJobVO.getParameter()== null? "{}" : VartechUtils.objectToJsonString(sqlExecuteJobVO.getParameter()));
		seDto.setSql(sqlExecuteJobVO.getSql());

		Connection conn = null;

		SqlSourceResultVO ssrv = new SqlSourceResultVO();
		
		try {
			conn = ConnectionFactory.getInstance().getConnection(vconnid);
			
			conn.setAutoCommit(false);
			
			SqlSource sqlSource = SqlSourceBuilder.getSqlSource(sqlExecuteJobVO.getSql(), sqlExecuteJobVO.getParameter(), DBVenderType.getDBType( databaseInfo.getType() ));
			
			sqlSource.setResult(ssrv);
			
			SelectExecutorHandler seh = new SelectExecutorHandler(null) {
				@Override
				public boolean handle(SelectInfo sqlResultVO) {
					return false;
				}
			};
			
			ssrv = SQLUtils.getSqlExecute(seDto, conn, sqlSource, true, seh);
			
			ssrv.setTotalCnt(seh.getTotalCount());
				
			conn.commit();
		} catch (Throwable e ) {
			throw new VarsqlRuntimeException(VarsqlAppCode.EC_SCHEDULER ,"sql execute error : ["+ vconnid + "] message : "+ e.getMessage(), e);
		}finally{
			if (conn != null && !conn.isClosed()) {
				conn.setAutoCommit(true);
				JdbcUtils.close(conn);
			}
		}
		
		logger.debug("## sql execute job end ## : {}", ssrv.getResultMessage());
		
		return JobResultVO.builder()
				.jobType(JobType.SQL)
				.log(ssrv.getResultMessage())
				.build(); 
	}
}