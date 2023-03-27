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
import com.varsql.web.dto.scheduler.JobHistoryResponseDTO;
import com.varsql.web.model.entity.scheduler.JobHistoryEntity;
import com.varsql.web.model.entity.scheduler.JobHistoryLogEntity;
import com.varsql.web.model.entity.scheduler.QJobHistoryEntity;
import com.varsql.web.model.entity.scheduler.QJobHistoryLogEntity;
import com.varsql.web.model.mapper.scheduler.JobHistoryMapper;
import com.varsql.web.repository.DefaultJpaRepository;

import lombok.Getter;

@Repository
public interface JobHistoryEntityRepository extends DefaultJpaRepository, JpaRepository<JobHistoryEntity, Long>, JpaSpecificationExecutor<JobHistoryEntity>, JobHistoryEntityRepositoryCustom {
	
	JobHistoryEntity findByHistSeq(long histSeq);
	
	public class JobHistoryEntityRepositoryCustomImpl extends QuerydslRepositorySupport implements JobHistoryEntityRepositoryCustom {

		public JobHistoryEntityRepositoryCustomImpl() {
			super(JobHistoryEntity.class);
		}

		@Override
		public Page<JobHistoryResponseDTO> findByJobUid(String jobUid, Pageable pageInfo) {
			final QJobHistoryEntity jhe = QJobHistoryEntity.jobHistoryEntity;
			final QJobHistoryLogEntity jhle = QJobHistoryLogEntity.jobHistoryLogEntity;
			
			List<CustomVO> result = getJobHistoryList(jobUid, pageInfo, jhe, jhle);
			
			List<JobHistoryResponseDTO> content = new ArrayList<>();
			if(result != null && result.size() > 0) {
				content =  result.stream().map(item ->{ return item.toDto();}).collect(Collectors.toList());
			}
			
			JPQLQuery<Long> countQuery = getJobHistoryCount(jobUid, pageInfo, jhe, jhle);

		    return PageableExecutionUtils.getPage(content, pageInfo, () -> countQuery.fetchOne());
		}

		private JPQLQuery<Long> getJobHistoryCount(String jobUid, Pageable pageInfo, QJobHistoryEntity she,
				QJobHistoryLogEntity shle) {
			
			JPQLQuery<Long> countQuery = from(she)
					.select(she.count())
					.where(she.jobUid.eq(jobUid));
					
			return countQuery;
		}

		private List<CustomVO> getJobHistoryList(String jobUid, Pageable pageInfo, QJobHistoryEntity jhe, QJobHistoryLogEntity jshle) {
			return from(jhe).leftJoin(jshle).on(jhe.histSeq.eq(jshle.histSeq))
					.select(Projections.constructor(CustomVO.class, jhe, jshle))
					.where(jhe.jobUid.eq(jobUid))
					.offset(pageInfo.getOffset())
					.limit(pageInfo.getPageSize())
					.orderBy(jhe.histSeq.desc())
					.fetch();
		}
		
	}
	
	@Getter
	public class CustomVO{
		private JobHistoryEntity jhe;
		private JobHistoryLogEntity jhle;
		
		public CustomVO(JobHistoryEntity jhe, JobHistoryLogEntity jhle) {
			this.jhe = jhe; 
			this.jhle = jhle; 
		}
		
		public JobHistoryResponseDTO toDto() {
			String log = jhle == null? "" : jhle.getLog();
			JobHistoryResponseDTO dto= JobHistoryMapper.INSTANCE.toDto(jhe);
			dto.setLog(log);
			return dto; 
		}
	}
	
}

interface JobHistoryEntityRepositoryCustom {
	
	Page<JobHistoryResponseDTO> findByJobUid(String jobUid, Pageable pageInfo);
}