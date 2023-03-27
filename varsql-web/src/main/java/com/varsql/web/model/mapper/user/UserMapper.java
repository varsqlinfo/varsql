package com.varsql.web.model.mapper.user;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.varsql.web.dto.user.UserResponseDTO;
import com.varsql.web.model.entity.user.UserEntity;
import com.varsql.web.model.mapper.GenericMapper;
import com.varsql.web.model.mapper.IgnoreUnmappedMapperConfig;

@Mapper(componentModel = "spring", config = IgnoreUnmappedMapperConfig.class)
public interface UserMapper extends GenericMapper<UserResponseDTO, UserEntity> {
	UserMapper INSTANCE = Mappers.getMapper( UserMapper.class );
}