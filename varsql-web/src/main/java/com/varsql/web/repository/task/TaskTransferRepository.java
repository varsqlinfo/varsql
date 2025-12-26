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

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import com.varsql.web.constants.ResourceConfigConstants;
import com.varsql.web.dto.task.TaskTransferResponseDTO;
import com.varsql.web.model.entity.db.QDBConnectionViewEntity;
import com.varsql.web.model.entity.task.QTaskTransferEntity;
import com.varsql.web.model.entity.task.TaskEntity;
import com.varsql.web.model.entity.task.TaskTransferEntity;
import com.varsql.web.model.mapper.task.TaskTransferMapper;
import com.varsql.web.repository.DefaultJpaRepository;

import lombok.Getter;

@Repository
public interface TaskTransferRepository extends DefaultJpaRepository, JpaRepository<TaskTransferEntity, String>, JpaSpecificationExecutor<TaskTransferEntity>, TaskTransferEntityCustom  {

	TaskTransferEntity findByTaskId(String taskId);
	
	@Transactional(readOnly = true , value=ResourceConfigConstants.APP_TRANSMANAGER)
	public class TaskTransferEntityCustomImpl extends QuerydslRepositorySupport implements TaskTransferEntityCustom {

		public TaskTransferEntityCustomImpl() {
			super(TaskTransferEntity.class);
		}
		
		@Override
		public Page<TaskTransferResponseDTO> findAllByNameContaining(String keyword, Pageable convertSearchInfoToPage) {
			final QTaskTransferEntity taskTransferEntity = QTaskTransferEntity.taskTransferEntity;
			
			JPQLQuery<TaskTransferEntityResultVO> query = from(taskTransferEntity)
					.select(Projections.constructor(TaskTransferEntityResultVO.class, taskTransferEntity))
					.where(taskTransferEntity.taskName.contains(keyword));
			
			long totalCount = query.fetchCount();
			
			if(totalCount  < 1) {
				return new PageImpl<>(Collections.emptyList(), convertSearchInfoToPage, totalCount);
			}
			
			List<TaskTransferEntityResultVO> results = getQuerydsl().applyPagination(convertSearchInfoToPage, query).fetch();
			
			return new PageImpl<>(results.stream().map(item->{
				return TaskTransferMapper.INSTANCE.toDto(item.getTaskTransferEntity());
			}).collect(Collectors.toList()), convertSearchInfoToPage, totalCount);
		}
		
		@Override
		public TaskTransferResponseDTO findTaskInfo(String taskId) {
			final QTaskTransferEntity taskTransferEntity = QTaskTransferEntity.taskTransferEntity;
			
			TaskTransferEntityResultVO customVo = from(taskTransferEntity)
			.select(Projections.constructor(TaskTransferEntityResultVO.class, taskTransferEntity))
			.where(taskTransferEntity.taskId.contains(taskId)).fetchOne();
			
			return TaskTransferMapper.INSTANCE.toDto(customVo.getTaskTransferEntity());
		}

	}
	
	@Getter
	public class TaskTransferEntityResultVO{
		private TaskEntity taskEntity;
		private TaskTransferEntity taskTransferEntity;
		
		public TaskTransferEntityResultVO( TaskTransferEntity taskTransferEntity) {
			this.taskTransferEntity = taskTransferEntity; 
		}
	}
}

interface TaskTransferEntityCustom {
	Page<TaskTransferResponseDTO> findAllByNameContaining(String keyword, Pageable convertSearchInfoToPage);
	TaskTransferResponseDTO findTaskInfo(String taskID);
}