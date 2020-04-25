package com.varsql.web.model.config;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QCustomRevisionEntity is a Querydsl query type for CustomRevisionEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QCustomRevisionEntity extends EntityPathBase<CustomRevisionEntity> {

    private static final long serialVersionUID = 2065961794L;

    public static final QCustomRevisionEntity customRevisionEntity = new QCustomRevisionEntity("customRevisionEntity");

    public final NumberPath<Long> revId = createNumber("revId", Long.class);

    public final NumberPath<Long> timestamp = createNumber("timestamp", Long.class);

    public final StringPath userName = createString("userName");

    public QCustomRevisionEntity(String variable) {
        super(CustomRevisionEntity.class, forVariable(variable));
    }

    public QCustomRevisionEntity(Path<? extends CustomRevisionEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCustomRevisionEntity(PathMetadata metadata) {
        super(CustomRevisionEntity.class, metadata);
    }

}

