package com.varsql.web.model.convert;
public interface DomainMapper {
    
    public <D,E> D convertToDomain(E source,Class<? extends D> classLiteral);
}
 
