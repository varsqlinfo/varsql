package com.varsql.web.repository.scheduler;

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
import com.varsql.web.dto.scheduler.ScheduleHistoryResponseDTO;
import com.varsql.web.model.entity.scheduler.QScheduleHistoryEntity;
import com.varsql.web.model.entity.scheduler.QScheduleHistoryLogEntity;
import com.varsql.web.model.entity.scheduler.ScheduleHistoryEntity;
import com.varsql.web.model.entity.scheduler.ScheduleHistoryLogEntity;
import com.varsql.web.model.mapper.scheduler.ScheduleHistoryMapper;
import com.varsql.web.repository.DefaultJpaRepository;

import lombok.Getter;

@Repository
public interface ScheduleHistoryEntityRepository extends DefaultJpaRepository, JpaRepository<ScheduleHistoryEntity, Long>, JpaSpecificationExecutor<ScheduleHistoryEntity>, ScheduleHistoryEntityRepositoryCustom {
	
	ScheduleHistoryEntity findByHistSeq(long histSeq);
	
	public class ScheduleHistoryEntityRepositoryCustomImpl extends QuerydslRepositorySupport implements ScheduleHistoryEntityRepositoryCustom {

		public ScheduleHistoryEntityRepositoryCustomImpl() {
			super(ScheduleHistoryEntity.class);
		}

		@Override
		public Page<ScheduleHistoryResponseDTO> findByJobUid(String jobUid, Pageable pageInfo) {
			final QScheduleHistoryEntity she = QScheduleHistoryEntity.scheduleHistoryEntity;
			final QScheduleHistoryLogEntity shle = QScheduleHistoryLogEntity.scheduleHistoryLogEntity;
			
			List<CustomVO> result = getJobHistoryList(jobUid, pageInfo, she, shle);
			
			List<ScheduleHistoryResponseDTO> content = new ArrayList<>();
			if(result != null && result.size() > 0) {
				content =  result.stream().map(item ->{ return item.toDto();}).collect(Collectors.toList());
			}
			
			JPQLQuery<Long> countQuery = getJobHistoryCount(jobUid, pageInfo, she, shle);

		    return PageableExecutionUtils.getPage(content, pageInfo, () -> countQuery.fetchOne());
		}

		private JPQLQuery<Long> getJobHistoryCount(String jobUid, Pageable pageInfo, QScheduleHistoryEntity she,
				QScheduleHistoryLogEntity shle) {
			
			JPQLQuery<Long> countQuery = from(she)
					.select(she.count())
					.where(she.jobUid.eq(jobUid));
					
			return countQuery;
		}

		private List<CustomVO> getJobHistoryList(String jobUid, Pageable pageInfo, QScheduleHistoryEntity she, QScheduleHistoryLogEntity shle) {
			return from(she).leftJoin(shle).on(she.histSeq.eq(shle.histSeq))
					.select(Projections.constructor(CustomVO.class, she, shle))
					.where(she.jobUid.eq(jobUid))
					.offset(pageInfo.getOffset())
					.limit(pageInfo.getPageSize())
					.orderBy(she.histSeq.desc())
					.fetch();
		}
		
	}
	
	@Getter
	public class CustomVO{
		private ScheduleHistoryEntity she;
		private ScheduleHistoryLogEntity shle;
		
		public CustomVO(ScheduleHistoryEntity she, ScheduleHistoryLogEntity shle) {
			this.she = she; 
			this.shle = shle; 
		}
		
		public ScheduleHistoryResponseDTO toDto() {
			String log = shle == null? "" : shle.getLog();
			ScheduleHistoryResponseDTO dto= ScheduleHistoryMapper.INSTANCE.toDto(she);
			dto.setLog(log);
			return dto; 
		}
	}
	
}

interface ScheduleHistoryEntityRepositoryCustom {
	
	Page<ScheduleHistoryResponseDTO> findByJobUid(String jobUid, Pageable pageInfo);
}