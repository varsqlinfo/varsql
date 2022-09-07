package com.varsql.web.model.mapper.scheduler;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.varsql.web.dto.scheduler.JobScheduleResponseDTO;
import com.varsql.web.model.entity.scheduler.JobScheduleEntity;
import com.varsql.web.model.mapper.GenericMapper;
import com.varsql.web.model.mapper.IgnoreUnmappedMapperConfig;

@Mapper(componentModel = "spring", config = IgnoreUnmappedMapperConfig.class)
public interface JobScheduleMapper extends GenericMapper<JobScheduleResponseDTO, JobScheduleEntity> {
	JobScheduleMapper INSTANCE = Mappers.getMapper( JobScheduleMapper.class );
	
	@Mapping(source = "scheduleDBConnection.vconnid", target = "vconnid")
	@Mapping(source = "scheduleDBConnection.vname", target = "vname")
	JobScheduleResponseDTO toDto(JobScheduleEntity e);
	
	@Override
	default void updateFromDto(JobScheduleResponseDTO dto, JobScheduleEntity entity) {
		
	}
	
	
}