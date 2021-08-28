package com.varsql.web.model.mapper.sql;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.varsql.web.dto.sql.SqlHistoryResponseDTO;
import com.varsql.web.model.entity.sql.SqlHistoryEntity;
import com.varsql.web.model.mapper.base.GenericMapper;
import com.varsql.web.model.mapper.base.IgnoreUnmappedMapperConfig;

@Mapper(componentModel = "spring", config = IgnoreUnmappedMapperConfig.class)
public interface SqlHistoryMapper extends GenericMapper<SqlHistoryResponseDTO, SqlHistoryEntity> {
	SqlHistoryMapper INSTANCE = Mappers.getMapper( SqlHistoryMapper.class );
}