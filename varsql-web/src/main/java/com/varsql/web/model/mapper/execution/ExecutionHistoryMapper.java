package com.varsql.web.model.mapper.execution;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.varsql.web.dto.execution.ExecutionHistoryResponseDTO;
import com.varsql.web.model.entity.execution.ExecutionHistoryEntity;
import com.varsql.web.model.mapper.GenericMapper;
import com.varsql.web.model.mapper.IgnoreUnmappedMapperConfig;

@Mapper(componentModel = "spring", config = IgnoreUnmappedMapperConfig.class)
public interface ExecutionHistoryMapper extends GenericMapper<ExecutionHistoryResponseDTO, ExecutionHistoryEntity> {
	ExecutionHistoryMapper INSTANCE = Mappers.getMapper( ExecutionHistoryMapper.class );
	
	@Mapping(expression = "java(stringToTimestamp(executionHistoryEntity.getStartTime()))", target = "startTime")
	@Mapping(expression = "java(stringToTimestamp(executionHistoryEntity.getEndTime()))", target = "endTime")
	ExecutionHistoryResponseDTO toDto(ExecutionHistoryEntity executionHistoryEntity);
	
	@Override
	default void updateFromDto(ExecutionHistoryResponseDTO dto, ExecutionHistoryEntity entity) {
		
	}
}