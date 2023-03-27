package com.varsql.web.model.mapper.sql;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.varsql.web.dto.sql.SqlFileResponseDTO;
import com.varsql.web.model.entity.sql.SqlFileEntity;
import com.varsql.web.model.mapper.GenericMapper;
import com.varsql.web.model.mapper.IgnoreUnmappedMapperConfig;

@Mapper(componentModel = "spring", config = IgnoreUnmappedMapperConfig.class)
public interface SqlFileMapper extends GenericMapper<SqlFileResponseDTO, SqlFileEntity> {
	SqlFileMapper INSTANCE = Mappers.getMapper( SqlFileMapper.class );
}