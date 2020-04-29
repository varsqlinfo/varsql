package com.varsql.web.model.entity.db;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QDBManagerEntity is a Querydsl query type for DBManagerEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QDBManagerEntity extends EntityPathBase<DBManagerEntity> {

    private static final long serialVersionUID = -647272236L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QDBManagerEntity dBManagerEntity = new QDBManagerEntity("dBManagerEntity");

    public final com.varsql.web.model.base.QAbstractRegAuditorModel _super = new com.varsql.web.model.base.QAbstractRegAuditorModel(this);

    public final QDBConnectionEntity dbConnInfo;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> regDt = _super.regDt;

    //inherited
    public final StringPath regId = _super.regId;

    public final com.varsql.web.model.entity.user.QUserEntity user;

    public final StringPath vconnid = createString("vconnid");

    public final StringPath viewid = createString("viewid");

    public QDBManagerEntity(String variable) {
        this(DBManagerEntity.class, forVariable(variable), INITS);
    }

    public QDBManagerEntity(Path<? extends DBManagerEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QDBManagerEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QDBManagerEntity(PathMetadata metadata, PathInits inits) {
        this(DBManagerEntity.class, metadata, inits);
    }

    public QDBManagerEntity(Class<? extends DBManagerEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.dbConnInfo = inits.isInitialized("dbConnInfo") ? new QDBConnectionEntity(forProperty("dbConnInfo")) : null;
        this.user = inits.isInitialized("user") ? new com.varsql.web.model.entity.user.QUserEntity(forProperty("user")) : null;
    }

}

