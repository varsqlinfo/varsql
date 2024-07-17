package com.varsql.web.repository.task;


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
import com.varsql.web.dto.task.TaskSqlResponseDTO;
import com.varsql.web.model.entity.task.QTaskEntity;
import com.varsql.web.model.entity.task.QTaskSqlEntity;
import com.varsql.web.model.entity.task.TaskEntity;
import com.varsql.web.model.entity.task.TaskSqlEntity;
import com.varsql.web.model.mapper.task.TaskSqlMapper;
import com.varsql.web.repository.DefaultJpaRepository;

import lombok.Getter;

@Repository
public interface TaskSqlRepository extends DefaultJpaRepository, JpaRepository<TaskSqlEntity, Long>, JpaSpecificationExecutor<TaskSqlEntity>, TaskSqlEntityCustom  {

	TaskSqlEntity findByTaskId(String taskId);
	
	@Transactional(readOnly = true , value=ResourceConfigConstants.APP_TRANSMANAGER)
	public class TaskSqlEntityCustomImpl extends QuerydslRepositorySupport implements TaskSqlEntityCustom {

		public TaskSqlEntityCustomImpl() {
			super(TaskSqlEntity.class);
		}
		
		@Override
		public Page<TaskSqlResponseDTO> findAllByNameContaining(String keyword, Pageable convertSearchInfoToPage) {
			//final QTaskEntity taskEntity = QTaskEntity.taskEntity;
			final QTaskSqlEntity taskSqlEntity = QTaskSqlEntity.taskSqlEntity;
			
			JPQLQuery<TaskSqlEntityCustomResultVO> query = from(taskSqlEntity)
					.select(Projections.constructor(TaskSqlEntityCustomResultVO.class, taskSqlEntity))
					.where(taskSqlEntity.taskName.contains(keyword));
			
			
			long totalCount = query.fetchCount();
			
			List<TaskSqlEntityCustomResultVO> results = getQuerydsl().applyPagination(convertSearchInfoToPage, query).fetch();
			
			return new PageImpl<>(results.stream().map(item->{
				return TaskSqlMapper.INSTANCE.toDto(item.getTaskSqlEntity());
			}).collect(Collectors.toList()), convertSearchInfoToPage, totalCount);
		}
		
		public Page<TaskSqlResponseDTO> findAllByNameContaining1(String keyword, Pageable convertSearchInfoToPage) {
			final QTaskEntity taskEntity = QTaskEntity.taskEntity;
			final QTaskSqlEntity taskSqlEntity = QTaskSqlEntity.taskSqlEntity;
			
			JPQLQuery<TaskSqlEntityCustomResultVO> query = from(taskEntity).innerJoin(taskSqlEntity).on(taskEntity.taskId.eq(taskSqlEntity.taskId))
			.select(Projections.constructor(TaskSqlEntityCustomResultVO.class, taskEntity, taskSqlEntity))
			.where(taskEntity.taskName.contains(keyword));
			
			
			long totalCount = query.fetchCount();
			
			List<TaskSqlEntityCustomResultVO> results = getQuerydsl().applyPagination(convertSearchInfoToPage, query).fetch();
			
			return new PageImpl<>(results.stream().map(item->{
				TaskSqlResponseDTO dto = new TaskSqlResponseDTO();
				
				dto.setTaskId(item.getTaskEntity().getTaskId());
				dto.setTaskName(item.getTaskEntity().getTaskName());
				dto.setTaskType(item.getTaskEntity().getTaskType());
				dto.setUseYn(item.getTaskEntity().isUseYn());
				dto.setDescription(item.getTaskEntity().getDescription());
				
				dto.setSql(item.getTaskSqlEntity().getSql());
				dto.setVconnid(item.getTaskSqlEntity().getVconnid());
				dto.setParameter(item.getTaskSqlEntity().getParameter());
				
				return dto;
			}).collect(Collectors.toList()), convertSearchInfoToPage, totalCount);
		}

		@Override
		public TaskSqlResponseDTO findTaskInfo(String taskId) {
			final QTaskEntity taskEntity = QTaskEntity.taskEntity;
			final QTaskSqlEntity taskSqlEntity = QTaskSqlEntity.taskSqlEntity;
			
			TaskSqlEntityCustomResultVO customVo = from(taskEntity).innerJoin(taskSqlEntity).on(taskEntity.taskId.eq(taskSqlEntity.taskId))
			.select(Projections.constructor(TaskSqlEntityCustomResultVO.class, taskEntity, taskSqlEntity))
			.where(taskEntity.taskId.contains(taskId)).fetchOne();
			
			TaskSqlResponseDTO dto = new TaskSqlResponseDTO();
			
			dto.setTaskId(customVo.getTaskEntity().getTaskId());
			dto.setTaskName(customVo.getTaskEntity().getTaskName());
			dto.setTaskType(customVo.getTaskEntity().getTaskType());
			dto.setUseYn(customVo.getTaskEntity().isUseYn());
			dto.setDescription(customVo.getTaskEntity().getDescription());
			
			dto.setSql(customVo.getTaskSqlEntity().getSql());
			dto.setVconnid(customVo.getTaskSqlEntity().getVconnid());
			dto.setParameter(customVo.getTaskSqlEntity().getParameter());
			
			return dto;
		}

	}
	
	@Getter
	public class TaskSqlEntityCustomResultVO{
		private TaskEntity taskEntity;
		private TaskSqlEntity taskSqlEntity;
		
		public TaskSqlEntityCustomResultVO( TaskSqlEntity taskSqlEntity) {
			this.taskSqlEntity = taskSqlEntity; 
		}
		
		public TaskSqlEntityCustomResultVO(TaskEntity taskEntity, TaskSqlEntity taskSqlEntity) {
			this.taskEntity = taskEntity; 
			this.taskSqlEntity = taskSqlEntity; 
		}
	}
}

interface TaskSqlEntityCustom {
	Page<TaskSqlResponseDTO> findAllByNameContaining(String keyword, Pageable convertSearchInfoToPage);
	TaskSqlResponseDTO findTaskInfo(String taskID);
}