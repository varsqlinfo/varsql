package com.varsql.web.repository.execution;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import com.varsql.core.common.constants.ExecuteType;
import com.varsql.web.dto.execution.ExecutionHistoryResponseDTO;
import com.varsql.web.model.entity.execution.ExecutionHistoryEntity;
import com.varsql.web.model.entity.execution.ExecutionHistoryLogEntity;
import com.varsql.web.model.entity.execution.QExecutionHistoryEntity;
import com.varsql.web.model.entity.execution.QExecutionHistoryLogEntity;
import com.varsql.web.model.mapper.execution.ExecutionHistoryMapper;
import com.varsql.web.repository.DefaultJpaRepository;

import lombok.Getter;

@Repository
public interface ExecutionHistoryEntityRepository extends DefaultJpaRepository, JpaRepository<ExecutionHistoryEntity, Long>, JpaSpecificationExecutor<ExecutionHistoryEntity>, ExecutionHistoryEntityRepositoryCustom {
	
	ExecutionHistoryEntity findByHistSeq(long histSeq);
	
	public class ExecutionHistoryEntityRepositoryCustomImpl extends QuerydslRepositorySupport implements ExecutionHistoryEntityRepositoryCustom {

		public ExecutionHistoryEntityRepositoryCustomImpl() {
			super(ExecutionHistoryEntity.class);
		}

		@Override
		public Page<ExecutionHistoryResponseDTO> findByTargetId(String targetId, ExecuteType targetType, Pageable pageInfo) {
			final QExecutionHistoryEntity ehe = QExecutionHistoryEntity.executionHistoryEntity;
			final QExecutionHistoryLogEntity ehle = QExecutionHistoryLogEntity.executionHistoryLogEntity;
			
			List<CustomVO> result = getExecutionHistoryList(targetId, pageInfo, targetType, ehe, ehle);
			
			List<ExecutionHistoryResponseDTO> content = new ArrayList<>();
			if(result != null && result.size() > 0) {
				content =  result.stream().map(item ->{ return item.toDto();}).collect(Collectors.toList());
			}
			
			JPQLQuery<Long> countQuery = getExecutionHistoryCount(targetId, pageInfo, ehe, ehle);

		    return PageableExecutionUtils.getPage(content, pageInfo, () -> countQuery.fetchOne());
		}

		private JPQLQuery<Long> getExecutionHistoryCount(String jobUid, Pageable pageInfo, QExecutionHistoryEntity she,
				QExecutionHistoryLogEntity shle) {
			
			JPQLQuery<Long> countQuery = from(she)
					.select(she.count())
					.where(she.targetId.eq(jobUid));
					
			return countQuery;
		}

		private List<CustomVO> getExecutionHistoryList(String targetId, Pageable pageInfo, ExecuteType targetType, QExecutionHistoryEntity ehe, QExecutionHistoryLogEntity eshle) {
			 JPQLQuery<CustomVO> tableQuery;
			
			if(ExecuteType.BATCH.equals(targetType)) {
				tableQuery = from(ehe).leftJoin(eshle).on(ehe.histSeq.eq(eshle.histSeq))
						.select(Projections.constructor(CustomVO.class, ehe, eshle));
			}else {
				tableQuery = from(ehe).select(Projections.constructor(CustomVO.class, ehe));
			}
			
			return tableQuery.where(ehe.targetId.eq(targetId).and(ehe.targetType.eq(targetType)))
					.offset(pageInfo.getOffset())
					.limit(pageInfo.getPageSize())
					.orderBy(ehe.histSeq.desc())
					.fetch();
		}
		
	}
	
	@Getter
	public class CustomVO{
		private ExecutionHistoryEntity ehe;
		private ExecutionHistoryLogEntity ehle;
		
		public CustomVO(ExecutionHistoryEntity ehe) {
			this(ehe, null);
		}
		
		public CustomVO(ExecutionHistoryEntity ehe, ExecutionHistoryLogEntity ehle) {
			this.ehe = ehe; 
			this.ehle = ehle; 
		}
		
		public ExecutionHistoryResponseDTO toDto() {
			String log = ehle == null? "" : ehle.getLog();
			ExecutionHistoryResponseDTO dto= ExecutionHistoryMapper.INSTANCE.toDto(ehe);
			dto.setLog(log);
			return dto; 
		}
	}
	
}

interface ExecutionHistoryEntityRepositoryCustom {
	
	Page<ExecutionHistoryResponseDTO> findByTargetId(String targetId, ExecuteType targetType, Pageable pageInfo);
}