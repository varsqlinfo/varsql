package com.varsql.web.model.entity.db;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QDBConnectionEntity is a Querydsl query type for DBConnectionEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QDBConnectionEntity extends EntityPathBase<DBConnectionEntity> {

    private static final long serialVersionUID = 118380285L;

    public static final QDBConnectionEntity dBConnectionEntity = new QDBConnectionEntity("dBConnectionEntity");

    public final com.varsql.web.model.base.QAabstractAuditorModel _super = new com.varsql.web.model.base.QAabstractAuditorModel(this);

    public final StringPath basetableYn = createString("basetableYn");

    public final BooleanPath delYn = createBoolean("delYn");

    public final NumberPath<Long> exportcount = createNumber("exportcount", Long.class);

    public final StringPath lazyloadYn = createString("lazyloadYn");

    public final SetPath<DBManagerEntity, QDBManagerEntity> managerList = this.<DBManagerEntity, QDBManagerEntity>createSet("managerList", DBManagerEntity.class, QDBManagerEntity.class, PathInits.DIRECT2);

    public final NumberPath<Long> maxActive = createNumber("maxActive", Long.class);

    public final NumberPath<Long> maxSelectCount = createNumber("maxSelectCount", Long.class);

    public final NumberPath<Long> minIdle = createNumber("minIdle", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> regDt = _super.regDt;

    //inherited
    public final StringPath regId = _super.regId;

    public final StringPath schemaViewYn = createString("schemaViewYn");

    public final NumberPath<Long> timeout = createNumber("timeout", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updDt = _super.updDt;

    //inherited
    public final StringPath updId = _super.updId;

    public final StringPath urlDirectYn = createString("urlDirectYn");

    public final StringPath useYn = createString("useYn");

    public final StringPath vconnid = createString("vconnid");

    public final StringPath vconnopt = createString("vconnopt");

    public final StringPath vdatabasename = createString("vdatabasename");

    public final StringPath vdbschema = createString("vdbschema");

    public final NumberPath<Long> vdbversion = createNumber("vdbversion", Long.class);

    public final StringPath vdriver = createString("vdriver");

    public final StringPath vid = createString("vid");

    public final StringPath vname = createString("vname");

    public final StringPath vpoolopt = createString("vpoolopt");

    public final NumberPath<Long> vport = createNumber("vport", Long.class);

    public final StringPath vpw = createString("vpw");

    public final StringPath vquery = createString("vquery");

    public final StringPath vserverip = createString("vserverip");

    public final StringPath vtype = createString("vtype");

    public final StringPath vurl = createString("vurl");

    public QDBConnectionEntity(String variable) {
        super(DBConnectionEntity.class, forVariable(variable));
    }

    public QDBConnectionEntity(Path<? extends DBConnectionEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QDBConnectionEntity(PathMetadata metadata) {
        super(DBConnectionEntity.class, metadata);
    }

}

