package com.varsql.web.model.mapper.task;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.varsql.web.dto.task.TaskHistoryResponseDTO;
import com.varsql.web.model.entity.task.TaskHistoryEntity;
import com.varsql.web.model.mapper.GenericMapper;
import com.varsql.web.model.mapper.IgnoreUnmappedMapperConfig;

@Mapper(componentModel = "spring", config = IgnoreUnmappedMapperConfig.class)
public interface TaskHistoryMapper extends GenericMapper<TaskHistoryResponseDTO, TaskHistoryEntity> {
	TaskHistoryMapper INSTANCE = Mappers.getMapper( TaskHistoryMapper.class );
	
	@Mapping(expression = "java(stringToTimestamp(taskEntity.getStartTime()))", target = "startTime")
	@Mapping(expression = "java(stringToTimestamp(taskEntity.getEndTime()))", target = "endTime")
	TaskHistoryResponseDTO toDto(TaskHistoryEntity taskEntity);
}