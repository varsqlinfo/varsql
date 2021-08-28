package com.varsql.web.model.mapper.base;

import org.mapstruct.MapperConfig;
import org.mapstruct.ReportingPolicy;

//@MapperConfig(unmappedTargetPolicy = ReportingPolicy.IGNORE,unmappedSourcePolicy = ReportingPolicy.IGNORE)
@MapperConfig(unmappedTargetPolicy = ReportingPolicy.IGNORE,unmappedSourcePolicy = ReportingPolicy.IGNORE, uses = {DateMapper.class})
public interface IgnoreUnmappedMapperConfig {
}