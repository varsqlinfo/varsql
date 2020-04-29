package com.varsql.web.model.entity.user;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUserEntity is a Querydsl query type for UserEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QUserEntity extends EntityPathBase<UserEntity> {

    private static final long serialVersionUID = -360883521L;

    public static final QUserEntity userEntity = new QUserEntity("userEntity");

    public final com.varsql.web.model.base.QAabstractAuditorModel _super = new com.varsql.web.model.base.QAabstractAuditorModel(this);

    public final StringPath acceptYn = createString("acceptYn");

    public final StringPath blockYn = createString("blockYn");

    public final SetPath<com.varsql.web.model.entity.db.DBManagerEntity, com.varsql.web.model.entity.db.QDBManagerEntity> dbList = this.<com.varsql.web.model.entity.db.DBManagerEntity, com.varsql.web.model.entity.db.QDBManagerEntity>createSet("dbList", com.varsql.web.model.entity.db.DBManagerEntity.class, com.varsql.web.model.entity.db.QDBManagerEntity.class, PathInits.DIRECT2);

    public final StringPath deptNm = createString("deptNm");

    public final StringPath description = createString("description");

    public final StringPath lang = createString("lang");

    public final StringPath orgNm = createString("orgNm");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> regDt = _super.regDt;

    //inherited
    public final StringPath regId = _super.regId;

    public final StringPath uemail = createString("uemail");

    public final StringPath uid = createString("uid");

    public final StringPath uname = createString("uname");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updDt = _super.updDt;

    //inherited
    public final StringPath updId = _super.updId;

    public final StringPath upw = createString("upw");

    public final StringPath userRole = createString("userRole");

    public final StringPath viewid = createString("viewid");

    public QUserEntity(String variable) {
        super(UserEntity.class, forVariable(variable));
    }

    public QUserEntity(Path<? extends UserEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUserEntity(PathMetadata metadata) {
        super(UserEntity.class, metadata);
    }

}

