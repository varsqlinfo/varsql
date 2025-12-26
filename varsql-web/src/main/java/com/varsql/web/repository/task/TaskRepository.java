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
import com.varsql.web.dto.task.TaskResponseDTO;
import com.varsql.web.model.entity.task.QTaskEntity;
import com.varsql.web.model.entity.task.TaskEntity;
import com.varsql.web.model.mapper.task.TaskMapper;
import com.varsql.web.repository.DefaultJpaRepository;

@Repository
public interface TaskRepository extends DefaultJpaRepository, JpaRepository<TaskEntity, Long>, JpaSpecificationExecutor<TaskEntity>, TaskEntityCustom {
	
	TaskEntity findByTaskId(String taskId);
	
	@Transactional(readOnly = true , value=ResourceConfigConstants.APP_TRANSMANAGER)
	public class TaskEntityCustomImpl extends QuerydslRepositorySupport implements TaskEntityCustom {

		public TaskEntityCustomImpl() {
			super(TaskEntity.class);
		}
		
		@Override
		public Page<TaskResponseDTO> findAllByNameContaining(String keyword, Pageable convertSearchInfoToPage) {
			final QTaskEntity taskEntity = QTaskEntity.taskEntity;
			
			JPQLQuery<TaskEntity> query = from(taskEntity)
					.select(taskEntity)
					.where(taskEntity.taskName.contains(keyword));
			
			long totalCount = query.fetchCount();
			
			if(totalCount  < 1) {
				return new PageImpl<TaskResponseDTO>(Collections.emptyList(), convertSearchInfoToPage, totalCount);
			}
			
			List<TaskEntity> results = getQuerydsl().applyPagination(convertSearchInfoToPage, query).fetch();
			
			return new PageImpl<>(results.stream().map(item->{
				return TaskMapper.INSTANCE.toDto(item);
			}).collect(Collectors.toList()), convertSearchInfoToPage, totalCount);
		}
	}
}

interface TaskEntityCustom {
	Page<TaskResponseDTO> findAllByNameContaining(String keyword, Pageable convertSearchInfoToPage);
}