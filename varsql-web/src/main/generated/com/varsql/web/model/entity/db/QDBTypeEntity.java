package com.varsql.web.model.entity.db;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QDBTypeEntity is a Querydsl query type for DBTypeEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QDBTypeEntity extends EntityPathBase<DBTypeEntity> {

    private static final long serialVersionUID = 1095358297L;

    public static final QDBTypeEntity dBTypeEntity = new QDBTypeEntity("dBTypeEntity");

    public final StringPath langkey = createString("langkey");

    public final StringPath name = createString("name");

    public final StringPath typeid = createString("typeid");

    public final StringPath urlprefix = createString("urlprefix");

    public QDBTypeEntity(String variable) {
        super(DBTypeEntity.class, forVariable(variable));
    }

    public QDBTypeEntity(Path<? extends DBTypeEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QDBTypeEntity(PathMetadata metadata) {
        super(DBTypeEntity.class, metadata);
    }

}

