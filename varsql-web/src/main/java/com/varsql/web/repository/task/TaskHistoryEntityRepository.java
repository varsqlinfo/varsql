package com.varsql.web.repository.task;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.jpa.JPQLQuery;
import com.varsql.web.constants.ResourceConfigConstants;
import com.varsql.web.dto.task.TaskHistoryResponseDTO;
import com.varsql.web.model.entity.task.QTaskHistoryEntity;
import com.varsql.web.model.entity.task.TaskHistoryEntity;
import com.varsql.web.model.mapper.task.TaskHistoryMapper;
import com.varsql.web.repository.DefaultJpaRepository;

/**
 * task history repository
 */
@Repository
public interface TaskHistoryEntityRepository extends DefaultJpaRepository, JpaRepository<TaskHistoryEntity, Long>, JpaSpecificationExecutor<TaskHistoryEntity>, TaskHistoryEntityCustom {
	
	TaskHistoryEntity findByHistSeq(long histSeq);
	
	@Transactional(readOnly = true , value=ResourceConfigConstants.APP_TRANSMANAGER)
	public class TaskHistoryEntityCustomImpl extends QuerydslRepositorySupport implements TaskHistoryEntityCustom {

		public TaskHistoryEntityCustomImpl() {
			super(TaskHistoryEntity.class);
		}
		
		@Override
		public Page<TaskHistoryResponseDTO> findByTaskId(String taskId, Pageable convertSearchInfoToPage) {
			final QTaskHistoryEntity taskHistoryEntity = QTaskHistoryEntity.taskHistoryEntity;
			
			JPQLQuery<TaskHistoryEntity> query = from(taskHistoryEntity)
					.select(taskHistoryEntity)	
					.where(taskHistoryEntity.taskId.eq(taskId));
			
			long totalCount = query.fetchCount();
			
			if(totalCount  < 1) {
				return new PageImpl<TaskHistoryResponseDTO>(Collections.emptyList(), convertSearchInfoToPage, totalCount);
			}
			
			List<TaskHistoryEntity> results = getQuerydsl().applyPagination(convertSearchInfoToPage, query).fetch();
			
			return new PageImpl<>(results.stream().map(item->{
				return TaskHistoryMapper.INSTANCE.toDto(item);
			}).collect(Collectors.toList()), convertSearchInfoToPage, totalCount);
		}
	}
}

interface TaskHistoryEntityCustom {
	Page<TaskHistoryResponseDTO> findByTaskId(String taskId, Pageable pageInfo);
}