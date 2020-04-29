package com.varsql.web.model.entity.app;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QExceptionLogEntity is a Querydsl query type for ExceptionLogEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QExceptionLogEntity extends EntityPathBase<ExceptionLogEntity> {

    private static final long serialVersionUID = -212918025L;

    public static final QExceptionLogEntity exceptionLogEntity = new QExceptionLogEntity("exceptionLogEntity");

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

    public QExceptionLogEntity(String variable) {
        super(ExceptionLogEntity.class, forVariable(variable));
    }

    public QExceptionLogEntity(Path<? extends ExceptionLogEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QExceptionLogEntity(PathMetadata metadata) {
        super(ExceptionLogEntity.class, metadata);
    }

}

