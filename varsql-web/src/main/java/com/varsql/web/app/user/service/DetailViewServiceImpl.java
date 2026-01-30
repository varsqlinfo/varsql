package com.varsql.web.app.user.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.varsql.core.exception.NotFoundException;
import com.varsql.web.common.service.AbstractService;
import com.varsql.web.model.entity.execution.ExecutionHistoryEntity;
import com.varsql.web.model.mapper.execution.ExecutionHistoryMapper;
import com.varsql.web.repository.execution.ExecutionHistoryEntityRepository;
import com.varsql.web.util.VarsqlUtils;
import com.vartech.common.app.beans.ResponseResult;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DetailViewServiceImpl extends AbstractService{
	private final Logger logger = LoggerFactory.getLogger(DetailViewServiceImpl.class);

	private final ExecutionHistoryEntityRepository executionHistoryEntityRepository;
	
	/**
	 * batch 상세 보기
	 * @param histSeq
	 * @return
	 */
	public ResponseResult executionHistoryDetail(long histSeq) {
		ExecutionHistoryEntity ehe = executionHistoryEntityRepository.findById(histSeq).orElse(null);
		
		if(ehe ==null) {
			logger.warn("execution history not found histseq: {}", histSeq);
			throw new NotFoundException("execution history not found histseq: "+ histSeq);
		}
		return VarsqlUtils.getResponseResultItemOne(ExecutionHistoryMapper.INSTANCE.toDto(ehe));
	}
}