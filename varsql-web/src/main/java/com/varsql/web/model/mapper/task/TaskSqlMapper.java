package com.varsql.web.model.mapper.task;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import com.varsql.web.dto.task.TaskSqlResponseDTO;
import com.varsql.web.model.entity.db.DBConnectionViewEntity;
import com.varsql.web.model.entity.task.TaskSqlEntity;
import com.varsql.web.model.mapper.GenericMapper;
import com.varsql.web.model.mapper.IgnoreUnmappedMapperConfig;

@Mapper(componentModel = "spring", config = IgnoreUnmappedMapperConfig.class)
public interface TaskSqlMapper extends GenericMapper<TaskSqlResponseDTO, TaskSqlEntity> {
	TaskSqlMapper INSTANCE = Mappers.getMapper( TaskSqlMapper.class );
	
	@Mapping(source = "taskSqlEntity.taskDBConnection.vconnid", target = "vconnid")
	@Mapping(source = "taskSqlEntity.taskDBConnection.vname", target = "vname")
	@Mapping(source = "taskSqlEntity.taskDBConnection", target = "useYn", qualifiedByName = "getUseYn")
	TaskSqlResponseDTO toDto(TaskSqlEntity taskSqlEntity);
	
	
	
	@Named("getUseYn")
    static String getUseYn(DBConnectionViewEntity taskDBConnection) {
		return ("N".equalsIgnoreCase(taskDBConnection.getUseYn()) || taskDBConnection.isDelYn()) ?"N":"Y";
	}
}