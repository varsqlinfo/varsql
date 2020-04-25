package com.varsql.web.model.entity.db;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QDBTypeDriverEntity is a Querydsl query type for DBTypeDriverEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QDBTypeDriverEntity extends EntityPathBase<DBTypeDriverEntity> {

    private static final long serialVersionUID = 1846521121L;

    public static final QDBTypeDriverEntity dBTypeDriverEntity = new QDBTypeDriverEntity("dBTypeDriverEntity");

    public final StringPath dbdriver = createString("dbdriver");

    public final StringPath dbtype = createString("dbtype");

    public final NumberPath<Integer> defaultPort = createNumber("defaultPort", Integer.class);

    public final StringPath driverDesc = createString("driverDesc");

    public final StringPath driverId = createString("driverId");

    public final StringPath schemaType = createString("schemaType");

    public final StringPath urlFormat = createString("urlFormat");

    public final StringPath validationQuery = createString("validationQuery");

    public QDBTypeDriverEntity(String variable) {
        super(DBTypeDriverEntity.class, forVariable(variable));
    }

    public QDBTypeDriverEntity(Path<? extends DBTypeDriverEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QDBTypeDriverEntity(PathMetadata metadata) {
        super(DBTypeDriverEntity.class, metadata);
    }

}

