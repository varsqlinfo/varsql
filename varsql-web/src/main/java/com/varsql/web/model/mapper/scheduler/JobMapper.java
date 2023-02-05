package com.varsql.web.model.mapper.scheduler;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.varsql.web.dto.scheduler.JobResponseDTO;
import com.varsql.web.model.entity.scheduler.JobEntity;
import com.varsql.web.model.mapper.GenericMapper;
import com.varsql.web.model.mapper.IgnoreUnmappedMapperConfig;

@Mapper(componentModel = "spring", config = IgnoreUnmappedMapperConfig.class)
public interface JobMapper extends GenericMapper<JobResponseDTO, JobEntity> {
	JobMapper INSTANCE = Mappers.getMapper( JobMapper.class );
	
	@Mapping(source = "jobDBConnection.vconnid", target = "vconnid")
	@Mapping(source = "jobDBConnection.vname", target = "vname")
	JobResponseDTO toDto(JobEntity e);
	
	@Override
	default void updateFromDto(JobResponseDTO dto, JobEntity entity) {
		
	}
	
	
}