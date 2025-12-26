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
import com.varsql.web.dto.task.TaskSqlResponseDTO;
import com.varsql.web.model.entity.db.QDBConnectionViewEntity;
import com.varsql.web.model.entity.task.QTaskSqlEntity;
import com.varsql.web.model.entity.task.TaskEntity;
import com.varsql.web.model.entity.task.TaskSqlEntity;
import com.varsql.web.model.mapper.task.TaskSqlMapper;
import com.varsql.web.repository.DefaultJpaRepository;

import lombok.Getter;

/**
 * task sql repository
 * @author ytkim
 *
 */
@Repository
public interface TaskSqlRepository extends DefaultJpaRepository, JpaRepository<TaskSqlEntity, String>, JpaSpecificationExecutor<TaskSqlEntity>, TaskSqlEntityCustom  {

	TaskSqlEntity findByTaskId(String taskId);
	
	@Transactional(readOnly = true , value=ResourceConfigConstants.APP_TRANSMANAGER)
	public class TaskSqlEntityCustomImpl extends QuerydslRepositorySupport implements TaskSqlEntityCustom {

		public TaskSqlEntityCustomImpl() {
			super(TaskSqlEntity.class);
		}
		
		/**
		 * 목록 
		 */
		@Override
		public Page<TaskSqlResponseDTO> findAllByNameContaining(String keyword, Pageable convertSearchInfoToPage) {
			final QTaskSqlEntity taskSqlEntity = QTaskSqlEntity.taskSqlEntity;
			final QDBConnectionViewEntity dbConnectionViewEntity = QDBConnectionViewEntity.dBConnectionViewEntity; 
			
			JPQLQuery<TaskSqlEntityCustomResultVO> query = from(taskSqlEntity)
					.innerJoin(dbConnectionViewEntity).on(taskSqlEntity.vconnid.eq(dbConnectionViewEntity.vconnid))
					.select(Projections.constructor(TaskSqlEntityCustomResultVO.class, taskSqlEntity))
					.where(taskSqlEntity.taskName.contains(keyword));
			
			long totalCount = query.fetchCount();
			
			if(totalCount  < 1) {
				return new PageImpl<>(Collections.emptyList(), convertSearchInfoToPage, totalCount);
			}
			
			List<TaskSqlEntityCustomResultVO> results = getQuerydsl().applyPagination(convertSearchInfoToPage, query).fetch();
			
			return new PageImpl<>(results.stream().map(item->{
				return TaskSqlMapper.INSTANCE.toDto(item.getTaskSqlEntity());
			}).collect(Collectors.toList()), convertSearchInfoToPage, totalCount);
		}
		
		/**
		 * task 정보 얻기
		 */
		@Override
		public TaskSqlResponseDTO findTaskInfo(String taskId) {
			final QTaskSqlEntity taskSqlEntity = QTaskSqlEntity.taskSqlEntity;
			
			TaskSqlEntityCustomResultVO customVo = from(taskSqlEntity)
			.select(Projections.constructor(TaskSqlEntityCustomResultVO.class, taskSqlEntity))
			.where(taskSqlEntity.taskId.contains(taskId)).fetchOne();
			
			return TaskSqlMapper.INSTANCE.toDto(customVo.getTaskSqlEntity());
		}
	}
	
	@Getter
	public class TaskSqlEntityCustomResultVO{
		private TaskEntity taskEntity;
		private TaskSqlEntity taskSqlEntity;
		
		public TaskSqlEntityCustomResultVO( TaskSqlEntity taskSqlEntity) {
			this.taskSqlEntity = taskSqlEntity; 
		}
	}
}

interface TaskSqlEntityCustom {
	Page<TaskSqlResponseDTO> findAllByNameContaining(String keyword, Pageable convertSearchInfoToPage);
	TaskSqlResponseDTO findTaskInfo(String taskID);
}