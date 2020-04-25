package com.varsql.web.model.entity.sql;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QSqlExceptionLogEntity is a Querydsl query type for SqlExceptionLogEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QSqlExceptionLogEntity extends EntityPathBase<SqlExceptionLogEntity> {

    private static final long serialVersionUID = -114406796L;

    public static final QSqlExceptionLogEntity sqlExceptionLogEntity = new QSqlExceptionLogEntity("sqlExceptionLogEntity");

    public final com.varsql.web.model.base.QAbstractRegAuditorModel _super = new com.varsql.web.model.base.QAbstractRegAuditorModel(this);

    public final StringPath excpCont = createString("excpCont");

    public final StringPath excpId = createString("excpId");

    public final StringPath excpTitle = createString("excpTitle");

    public final StringPath excpType = createString("excpType");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> regDt = _super.regDt;

    //inherited
    public final StringPath regId = _super.regId;

    public final StringPath serverId = createString("serverId");

    public QSqlExceptionLogEntity(String variable) {
        super(SqlExceptionLogEntity.class, forVariable(variable));
    }

    public QSqlExceptionLogEntity(Path<? extends SqlExceptionLogEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSqlExceptionLogEntity(PathMetadata metadata) {
        super(SqlExceptionLogEntity.class, metadata);
    }

}

