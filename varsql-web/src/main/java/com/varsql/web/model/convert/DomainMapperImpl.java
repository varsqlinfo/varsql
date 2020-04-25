package com.varsql.web.model.convert;

import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration.AccessLevel;
import org.springframework.stereotype.Component;

@Component
public class DomainMapperImpl implements DomainMapper{

    private ModelMapper modelMapper;

    public DomainMapperImpl(ModelMapper modelMapper) {
        this.modelMapper=modelMapper;

        this.modelMapper.getConfiguration()
        .setFieldMatchingEnabled(true)
        .setFieldAccessLevel(AccessLevel.PRIVATE)
        .setMethodAccessLevel(AccessLevel.PRIVATE);
    }

    /*
     * 공통 매퍼
     */
    @Override
    public <D, E> D convertToDomain(E source, Class<? extends D> classLiteral) {
        return modelMapper.map(source, classLiteral);
    }
}


