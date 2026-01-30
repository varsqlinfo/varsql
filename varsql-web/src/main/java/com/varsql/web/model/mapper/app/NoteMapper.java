package com.varsql.web.model.mapper.app;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import com.varsql.web.constants.SecurityConstants;
import com.varsql.web.dto.user.NoteResponseDTO;
import com.varsql.web.model.entity.app.NoteEntity;
import com.varsql.web.model.entity.user.RegInfoEntity;
import com.varsql.web.model.mapper.GenericMapper;
import com.varsql.web.model.mapper.IgnoreUnmappedMapperConfig;

@Mapper(componentModel = "spring", config = IgnoreUnmappedMapperConfig.class)
public interface NoteMapper extends GenericMapper<NoteResponseDTO, NoteEntity> {
	NoteMapper INSTANCE = Mappers.getMapper( NoteMapper.class );
	
	@Mapping(target = "noteType", expression = "java(source.getNoteType() != null ? source.getNoteType().name() : null)")
    NoteResponseDTO toDto(NoteEntity source);
	
	@AfterMapping
    default void handleRegInfoDefault(NoteEntity source, @MappingTarget NoteResponseDTO target) {
        if (source.getRegInfo() == null) {
            target.setRegUserInfo(RegInfoEntity.ofNullable(null, source.getRegId()).getUname());
        } else {
        	String uid = source.getRegInfo().getUid(); 
        	if(SecurityConstants.SYSTEM_ID.equals(uid)) {
        		target.setRegUserInfo(source.getRegInfo().getUname());
        	}else {
        		target.setRegUserInfo(source.getRegInfo().getUname()+"("+uid+")");
        	}
        	
        }
    }
}