package com.varsql.web.model.mapper.task;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.varsql.web.dto.task.TaskSqlResponseDTO;
import com.varsql.web.model.entity.task.TaskSqlEntity;
import com.varsql.web.model.mapper.GenericMapper;
import com.varsql.web.model.mapper.IgnoreUnmappedMapperConfig;

@Mapper(componentModel = "spring", config = IgnoreUnmappedMapperConfig.class)
public interface TaskSqlMapper extends GenericMapper<TaskSqlResponseDTO, TaskSqlEntity> {
	TaskSqlMapper INSTANCE = Mappers.getMapper( TaskSqlMapper.class );
}