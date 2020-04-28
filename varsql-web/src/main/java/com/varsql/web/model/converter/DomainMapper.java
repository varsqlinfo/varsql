package com.varsql.web.model.converter;

public interface DomainMapper {
    
    public <D,E> D convertToDomain(E source,Class<? extends D> classLiteral);
}
 
