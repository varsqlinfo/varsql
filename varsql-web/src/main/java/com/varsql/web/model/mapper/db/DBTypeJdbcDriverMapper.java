package com.varsql.web.model.mapper.db;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.varsql.web.dto.db.DBTypeDriverProviderResponseDTO;
import com.varsql.web.model.entity.db.DBTypeDriverProviderEntity;
import com.varsql.web.model.mapper.GenericMapper;
import com.varsql.web.model.mapper.IgnoreUnmappedMapperConfig;

@Mapper(componentModel = "spring", config = IgnoreUnmappedMapperConfig.class)
public interface DBTypeJdbcDriverMapper extends GenericMapper<DBTypeDriverProviderResponseDTO, DBTypeDriverProviderEntity> {
  public static final DBTypeJdbcDriverMapper INSTANCE = Mappers.getMapper(DBTypeJdbcDriverMapper.class);
}
