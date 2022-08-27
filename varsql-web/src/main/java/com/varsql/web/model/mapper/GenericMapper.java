package com.varsql.web.model.mapper;

import java.sql.Timestamp;

import org.mapstruct.BeanMapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.vartech.common.utils.DateUtils;

public interface GenericMapper<D, E> {

    D toDto(E e);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDto(D dto, @MappingTarget E entity);
    
    default String stringToTimestamp(Timestamp time) {
        if(time== null) return "";
        return  DateUtils.timestampFormat(time);
    }
}