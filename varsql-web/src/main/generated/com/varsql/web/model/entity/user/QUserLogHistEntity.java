package com.varsql.web.model.entity.user;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QUserLogHistEntity is a Querydsl query type for UserLogHistEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QUserLogHistEntity extends EntityPathBase<UserLogHistEntity> {

    private static final long serialVersionUID = -1879449171L;

    public static final QUserLogHistEntity userLogHistEntity = new QUserLogHistEntity("userLogHistEntity");

    public final StringPath browser = createString("browser");

    public final StringPath deviceType = createString("deviceType");

    public final DateTimePath<java.sql.Timestamp> histTime = createDateTime("histTime", java.sql.Timestamp.class);

    public final StringPath histType = createString("histType");

    public final NumberPath<Long> logPk = createNumber("logPk", Long.class);

    public final StringPath platform = createString("platform");

    public final StringPath usrIp = createString("usrIp");

    public final StringPath viewid = createString("viewid");

    public QUserLogHistEntity(String variable) {
        super(UserLogHistEntity.class, forVariable(variable));
    }

    public QUserLogHistEntity(Path<? extends UserLogHistEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUserLogHistEntity(PathMetadata metadata) {
        super(UserLogHistEntity.class, metadata);
    }

}

