package com.varsql.web.model.mapper.scheduler;

import javax.persistence.EntityNotFoundException;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.varsql.web.dto.scheduler.JobResponseDTO;
import com.varsql.web.model.entity.db.DBConnectionViewEntity;
import com.varsql.web.model.entity.scheduler.JobEntity;
import com.varsql.web.model.mapper.GenericMapper;
import com.varsql.web.model.mapper.IgnoreUnmappedMapperConfig;

@Mapper(componentModel = "spring", config = IgnoreUnmappedMapperConfig.class)
public interface JobMapper extends GenericMapper<JobResponseDTO, JobEntity> {
	JobMapper INSTANCE = Mappers.getMapper( JobMapper.class );
	
    default JobResponseDTO toDto(JobEntity e) {
        if ( e == null ) {
            return null;
        }

        JobResponseDTO jobResponseDTO = new JobResponseDTO();

        jobResponseDTO = eJobDBConnectionInfo( e, jobResponseDTO );
        
        jobResponseDTO.setJobUid( e.getJobUid() );
        jobResponseDTO.setJobName( e.getJobName() );
        jobResponseDTO.setJobData( e.getJobData() );
        jobResponseDTO.setJobDescription( e.getJobDescription() );
        jobResponseDTO.setCronExpression( e.getCronExpression() );
        jobResponseDTO.setRegId( e.getRegId() );
        jobResponseDTO.setRegDt( e.getRegDt() );

        return jobResponseDTO;
    }
	
	static JobResponseDTO eJobDBConnectionInfo(JobEntity jobEntity, JobResponseDTO jobResponseDTO) {
        if ( jobEntity == null ) {
            return jobResponseDTO;
        }
        try {
        	DBConnectionViewEntity jobDBConnection = jobEntity.getJobDBConnection();
	        if ( jobDBConnection == null || jobDBConnection.getVconnid()==null) {
	            return jobResponseDTO;
	        }
	        jobResponseDTO.setVconnid(jobDBConnection.getVconnid());
	        jobResponseDTO.setVname(jobDBConnection.getVname());
	        return jobResponseDTO;
        }catch(EntityNotFoundException e) {
        	return jobResponseDTO;
        }
    }

	@Override
	default void updateFromDto(JobResponseDTO dto, JobEntity entity) {
		
	}
	
	
}