package com.varsql.web.model.mapper.scheduler;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.varsql.web.dto.scheduler.ScheduleHistoryResponseDTO;
import com.varsql.web.model.entity.scheduler.ScheduleHistoryEntity;
import com.varsql.web.model.mapper.GenericMapper;
import com.varsql.web.model.mapper.IgnoreUnmappedMapperConfig;

@Mapper(componentModel = "spring", config = IgnoreUnmappedMapperConfig.class)
public interface ScheduleHistoryMapper extends GenericMapper<ScheduleHistoryResponseDTO, ScheduleHistoryEntity> {
	ScheduleHistoryMapper INSTANCE = Mappers.getMapper( ScheduleHistoryMapper.class );
	
	@Mapping(expression = "java(stringToTimestamp(scheduleHistoryEntity.getStartTime()))", target = "startTime")
	@Mapping(expression = "java(stringToTimestamp(scheduleHistoryEntity.getEndTime()))", target = "endTime")
	ScheduleHistoryResponseDTO toDto(ScheduleHistoryEntity scheduleHistoryEntity);
	
	@Override
	default void updateFromDto(ScheduleHistoryResponseDTO dto, ScheduleHistoryEntity entity) {
		// TODO Auto-generated method stub
		
	}
}