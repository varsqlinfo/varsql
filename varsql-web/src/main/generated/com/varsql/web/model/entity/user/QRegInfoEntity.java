package com.varsql.web.model.entity.user;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QRegInfoEntity is a Querydsl query type for RegInfoEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QRegInfoEntity extends EntityPathBase<RegInfoEntity> {

    private static final long serialVersionUID = -569186028L;

    public static final QRegInfoEntity regInfoEntity = new QRegInfoEntity("regInfoEntity");

    public final StringPath deptNm = createString("deptNm");

    public final StringPath orgNm = createString("orgNm");

    public final StringPath uid = createString("uid");

    public final StringPath uname = createString("uname");

    public final StringPath viewid = createString("viewid");

    public QRegInfoEntity(String variable) {
        super(RegInfoEntity.class, forVariable(variable));
    }

    public QRegInfoEntity(Path<? extends RegInfoEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QRegInfoEntity(PathMetadata metadata) {
        super(RegInfoEntity.class, metadata);
    }

}

