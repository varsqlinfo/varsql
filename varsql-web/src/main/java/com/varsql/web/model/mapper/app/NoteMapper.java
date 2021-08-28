package com.varsql.web.model.mapper.app;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.varsql.web.dto.user.NoteResponseDTO;
import com.varsql.web.model.entity.app.NoteEntity;
import com.varsql.web.model.mapper.base.GenericMapper;
import com.varsql.web.model.mapper.base.IgnoreUnmappedMapperConfig;

@Mapper(componentModel = "spring", config = IgnoreUnmappedMapperConfig.class)
public interface NoteMapper extends GenericMapper<NoteResponseDTO, NoteEntity> {
	NoteMapper INSTANCE = Mappers.getMapper( NoteMapper.class );
}