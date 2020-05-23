package com.varsql.web.app.database.service;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.varsql.web.app.database.dao.SQLDAO;
import com.varsql.web.constants.ResourceConfigConstants;
import com.varsql.web.dto.sql.SqlFileRequestDTO;
import com.varsql.web.dto.sql.SqlFileResponseDTO;
import com.varsql.web.model.entity.sql.SqlFileEntity;
import com.varsql.web.model.entity.sql.SqlFileTabEntity;
import com.varsql.web.repository.spec.SqlFileSpec;
import com.varsql.web.repository.sql.SqlFileEntityRepository;
import com.varsql.web.repository.sql.SqlFileTabEntityRepository;
import com.varsql.web.util.VarsqlUtils;
import com.vartech.common.app.beans.ResponseResult;

/**
 *
 * @FileName  : SQLFileServiceImpl.java
 * @Date      : 2014. 8. 18.
 * @작성자      : ytkim
 * @변경이력 :
 * @프로그램 설명 :
 */
@Service
public class SQLFileServiceImpl{
	private static final Logger logger = LoggerFactory.getLogger(SQLFileServiceImpl.class);

	@Autowired
	private SQLDAO sqlDAO;
	
	@Autowired
	private SqlFileEntityRepository sqlFileEntityRepository; 
	
	@Autowired
	private SqlFileTabEntityRepository sqlFileTabEntityRepository; 

	/**
	 * 쿼리 저장.
	 * @param sqlFileRequestDTO
	 */
	public ResponseResult saveQuery(SqlFileRequestDTO sqlFileRequestDTO) {
		ResponseResult result = new ResponseResult();
		
		SqlFileEntity sqlFileInfo = sqlFileRequestDTO.toEntity();
		
		if("".equals(sqlFileRequestDTO.getSqlId())){
			sqlFileTabEntityRepository.updateSqlFileTabDisable(sqlFileRequestDTO.getVconnid(), sqlFileRequestDTO.getViewid());
			
			sqlFileEntityRepository.save(sqlFileInfo);
		}else{
			String mode = String.valueOf(sqlFileRequestDTO.getCustom().get("mode"));

			if("addTab".equals(mode)){
				sqlFileTabEntityRepository.updateSqlFileTabDisable(sqlFileRequestDTO.getVconnid(), sqlFileRequestDTO.getViewid());
				sqlDAO.insertSqlFileTabInfo(sqlFileRequestDTO); // 이전 활성 view mode  N으로 변경.
				
				sqlFileTabEntityRepository.save(SqlFileTabEntity.builder()
					.vconnid(sqlFileRequestDTO.getVconnid())
					.viewid(sqlFileRequestDTO.getViewid())
					.sqlId(sqlFileRequestDTO.getSqlId())
					.prevSqlId(sqlFileRequestDTO.getPrevSqlId())
					.build()
				);
			}else if("delTab".equals(mode)){
				sqlFileTabEntityRepository.deleteTabInfo(sqlFileRequestDTO.getVconnid(), sqlFileRequestDTO.getViewid(), sqlFileRequestDTO.getSqlId());
			}else if("viewTab".equals(mode)){
				sqlFileTabEntityRepository.updateSqlFileTabDisable(sqlFileRequestDTO.getVconnid(), sqlFileRequestDTO.getViewid());
				sqlFileTabEntityRepository.updateSqlFileTabEnable(sqlFileRequestDTO.getVconnid(), sqlFileRequestDTO.getViewid(), sqlFileRequestDTO.getSqlId());
			}else{
				sqlFileEntityRepository.save(sqlFileInfo);

				if("query_del".equals(mode)){
					sqlFileTabEntityRepository.deleteTabInfo(sqlFileRequestDTO.getVconnid(), sqlFileRequestDTO.getViewid(), sqlFileRequestDTO.getSqlId());
				}
			}
		}

		result.setItemOne(sqlFileRequestDTO);

		return result;
	}

	private void deleteSqlFileTabInfo(SqlFileRequestDTO sqlFileRequestDTO) {
		try{
			int tabLen = -1;
			try{
				tabLen = Integer.parseInt(String.valueOf(sqlFileRequestDTO.getCustom().get("len")));
			}catch(Exception e){
				tabLen = -1;
			}

			if(tabLen ==0){
				sqlFileTabEntityRepository.deleteAllSqlFileTabInfo(sqlFileRequestDTO.getVconnid(), sqlFileRequestDTO.getViewid());
			}else{
				sqlFileTabEntityRepository.deleteTabInfo(sqlFileRequestDTO.getVconnid(), sqlFileRequestDTO.getViewid(), sqlFileRequestDTO.getSqlId());
			}
		}catch(Exception e){
			logger.error("deleteSqlFileTabInfo" ,e);
		}

	}

	/**
	 *
	 * @Method Name  : saveAllQuery
	 * @Method 설명 : sql 파일 모두저장.
	 * @작성자   : ytkim
	 * @작성일   : 2018. 11. 26.
	 * @변경이력  :
	 * @param sqlParamInfo
	 * @return
	 */
	public ResponseResult saveAllQuery(SqlFileRequestDTO sqlParamInfo) {
		Map<String,Object> customParam = (Map<String,Object>)sqlParamInfo.getCustom();

		String sqlIdStr = String.valueOf(customParam.get("sqlIdArr"));

		String[] sqlIdArr= sqlIdStr.split(";");
		
		List<SqlFileEntity> sqlFileInfos = new ArrayList<>();
		for (int i = 0; i < sqlIdArr.length; i++) {
			String sqlId = sqlIdArr[i];

			sqlParamInfo.setSqlId(sqlId);
			sqlParamInfo.setSqlParam(String.valueOf(customParam.get(sqlId+"_param")));
			sqlParamInfo.setSql(String.valueOf(customParam.get(sqlId)));
			
			sqlFileInfos.add(sqlParamInfo.toEntity());
		}
		
		sqlFileEntityRepository.saveAll(sqlFileInfos);

		return VarsqlUtils.getResponseResultItemOne(1);
	}

	/**
	 * 사용자 sql 목록 보기.
	 * @param sqlParamInfo
	 * @return
	 */
	public ResponseResult selectSqlFileList(SqlFileRequestDTO sqlParamInfo) {
		
		List<SqlFileResponseDTO> files = new ArrayList<>();
		sqlFileEntityRepository.findAll(SqlFileSpec.searchVconnSqlFile(sqlParamInfo.getVconnid(), String.valueOf(sqlParamInfo.getCustom().get("searchVal")))).forEach(item ->{
			files.add(SqlFileResponseDTO.builder()
				.sqlId(item.getSqlId())
				.sqlTitle(item.getSqlTitle())
				.build()
			);
		});
		
		return VarsqlUtils.getResponseResultItemList(files);
	}

	/**
	 * sql 저장 정보 삭제 .
	 * @param sqlParamInfo
	 * @return
	 */
	@Transactional(value = ResourceConfigConstants.APP_TRANSMANAGER,rollbackFor=Throwable.class)
	public ResponseResult deleteSqlSaveInfo(SqlFileRequestDTO sqlFileRequestDTO) {
		sqlFileEntityRepository.deleteSqlFileInfo(sqlFileRequestDTO.getVconnid(), sqlFileRequestDTO.getSqlId());
		sqlFileTabEntityRepository.deleteTabInfo(sqlFileRequestDTO.getVconnid(), sqlFileRequestDTO.getViewid(), sqlFileRequestDTO.getSqlId());
		
		return VarsqlUtils.getResponseResultItemOne(1);
	}

	/**
	 *
	 * @Method Name  : selectSqlFileTabList
	 * @Method 설명 : sql file tab list
	 * @작성자   : ytkim
	 * @작성일   : 2018. 11. 7.
	 * @변경이력  :
	 * @param sqlParamInfo
	 * @return
	 */
	public ResponseResult selectSqlFileTabList(SqlFileRequestDTO sqlParamInfo) {
		ResponseResult result = new ResponseResult();
		result.setItemList(sqlDAO.selectSqlFileTabList(sqlParamInfo));
		return result;
	}

	/**
	 *
	 * @Method Name  : sqlFileDetailInfo
	 * @Method 설명 : sql file 상세보기.
	 * @작성자   : ytkim
	 * @작성일   : 2018. 11. 26.
	 * @변경이력  :
	 * @param sqlParamInfo
	 * @return
	 */
	public ResponseResult sqlFileDetailInfo(SqlFileRequestDTO sqlParamInfo) {
		return VarsqlUtils.getResponseResultItemOne(sqlFileEntityRepository.findOne(SqlFileSpec.detailSqlFile(sqlParamInfo.getVconnid(), sqlParamInfo.getSqlId())).orElse(null));
	}
}