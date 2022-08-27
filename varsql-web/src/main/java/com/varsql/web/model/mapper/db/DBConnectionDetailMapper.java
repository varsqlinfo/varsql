package com.varsql.web.model.mapper.db;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.varsql.web.dto.db.DBConnectionDetailDTO;
import com.varsql.web.model.entity.db.DBConnectionEntity;
import com.varsql.web.model.mapper.GenericMapper;
import com.varsql.web.model.mapper.IgnoreUnmappedMapperConfig;

@Mapper(componentModel = "spring", config = IgnoreUnmappedMapperConfig.class)
public interface DBConnectionDetailMapper extends GenericMapper<DBConnectionDetailDTO, DBConnectionEntity> {
	DBConnectionDetailMapper INSTANCE = Mappers.getMapper( DBConnectionDetailMapper.class );
	
	@Mapping(source = "dbTypeDriverProvider.driverProviderId", target = "vdriver")
	DBConnectionDetailDTO toDto(DBConnectionEntity e);
}