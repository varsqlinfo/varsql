package com.varsql.web.model.mapper.task;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.varsql.web.dto.task.TaskResponseDTO;
import com.varsql.web.model.entity.task.TaskEntity;
import com.varsql.web.model.mapper.GenericMapper;
import com.varsql.web.model.mapper.IgnoreUnmappedMapperConfig;

@Mapper(componentModel = "spring", config = IgnoreUnmappedMapperConfig.class)
public interface TaskMapper extends GenericMapper<TaskResponseDTO, TaskEntity> {
	TaskMapper INSTANCE = Mappers.getMapper( TaskMapper.class );
	
	TaskResponseDTO toDto(TaskEntity taskEntity);
}