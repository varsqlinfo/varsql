package com.varsql.web.model.mapper.scheduler;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.varsql.web.dto.scheduler.JobHistoryResponseDTO;
import com.varsql.web.model.entity.scheduler.JobHistoryEntity;
import com.varsql.web.model.mapper.GenericMapper;
import com.varsql.web.model.mapper.IgnoreUnmappedMapperConfig;

@Mapper(componentModel = "spring", config = IgnoreUnmappedMapperConfig.class)
public interface JobHistoryMapper extends GenericMapper<JobHistoryResponseDTO, JobHistoryEntity> {
	JobHistoryMapper INSTANCE = Mappers.getMapper( JobHistoryMapper.class );
	
	@Mapping(expression = "java(stringToTimestamp(jobHistoryEntity.getStartTime()))", target = "startTime")
	@Mapping(expression = "java(stringToTimestamp(jobHistoryEntity.getEndTime()))", target = "endTime")
	JobHistoryResponseDTO toDto(JobHistoryEntity jobHistoryEntity);
	
	@Override
	default void updateFromDto(JobHistoryResponseDTO dto, JobHistoryEntity entity) {
		
	}
}