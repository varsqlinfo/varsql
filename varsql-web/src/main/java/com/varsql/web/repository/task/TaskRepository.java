package com.varsql.web.repository.task;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.varsql.web.dto.task.TaskResponseDTO;
import com.varsql.web.model.entity.task.TaskEntity;
import com.varsql.web.repository.DefaultJpaRepository;

@Repository
public interface TaskRepository extends DefaultJpaRepository, JpaRepository<TaskEntity, Long>, JpaSpecificationExecutor<TaskEntity>  {
	
	TaskEntity findByTaskId(String taskId);
	
	@Query(value = "select new com.varsql.web.dto.task.TaskResponseDTO("+TaskEntity.TASK_ID+", "+TaskEntity.TASK_NAME+", "+TaskEntity.TASK_TYPE+", "+TaskEntity.USE_YN+", "+TaskEntity.DESCRIPTION+") from TaskEntity m")
	List<TaskResponseDTO> findAllByNameContaining(Sort sort);
}
