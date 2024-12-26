package com.varsql.web.model.mapper.task;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import com.varsql.web.dto.task.TaskTransferResponseDTO;
import com.varsql.web.model.entity.db.DBConnectionViewEntity;
import com.varsql.web.model.entity.task.TaskTransferEntity;
import com.varsql.web.model.mapper.GenericMapper;
import com.varsql.web.model.mapper.IgnoreUnmappedMapperConfig;

@Mapper(componentModel = "spring", config = IgnoreUnmappedMapperConfig.class)
public interface TaskTransferMapper extends GenericMapper<TaskTransferResponseDTO, TaskTransferEntity> {
	TaskTransferMapper INSTANCE = Mappers.getMapper( TaskTransferMapper.class );
	
	@Mapping(source = "taskTransferEntity.sourceDBConnection.vname", target = "sourceVname")
	@Mapping(source = "taskTransferEntity.sourceDBConnection", target = "sourceUseYn", qualifiedByName = "getUseYn")
	@Mapping(source = "taskTransferEntity.targetDBConnection.vname", target = "targetVname")
	@Mapping(source = "taskTransferEntity.targetDBConnection", target = "targetUseYn", qualifiedByName = "getUseYn")
	TaskTransferResponseDTO toDto(TaskTransferEntity taskTransferEntity);
	
	
	@Named("getUseYn")
    static String getUseYn(DBConnectionViewEntity taskDBConnection) {
		return ("N".equalsIgnoreCase(taskDBConnection.getUseYn()) || taskDBConnection.isDelYn()) ?"N":"Y";
	}
}