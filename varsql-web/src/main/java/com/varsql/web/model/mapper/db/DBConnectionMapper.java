package com.varsql.web.model.mapper.db;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.varsql.web.dto.db.DBConnectionResponseDTO;
import com.varsql.web.model.entity.db.DBConnectionEntity;
import com.varsql.web.model.mapper.base.GenericMapper;
import com.varsql.web.model.mapper.base.IgnoreUnmappedMapperConfig;

@Mapper(componentModel = "spring", config = IgnoreUnmappedMapperConfig.class)
public interface DBConnectionMapper extends GenericMapper<DBConnectionResponseDTO, DBConnectionEntity> {
	DBConnectionMapper INSTANCE = Mappers.getMapper( DBConnectionMapper.class );
}