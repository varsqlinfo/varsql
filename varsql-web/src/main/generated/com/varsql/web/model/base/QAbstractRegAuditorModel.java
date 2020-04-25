package com.varsql.web.model.base;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QAbstractRegAuditorModel is a Querydsl query type for AbstractRegAuditorModel
 */
@Generated("com.querydsl.codegen.SupertypeSerializer")
public class QAbstractRegAuditorModel extends EntityPathBase<AbstractRegAuditorModel> {

    private static final long serialVersionUID = -381677799L;

    public static final QAbstractRegAuditorModel abstractRegAuditorModel = new QAbstractRegAuditorModel("abstractRegAuditorModel");

    public final DateTimePath<java.time.LocalDateTime> regDt = createDateTime("regDt", java.time.LocalDateTime.class);

    public final StringPath regId = createString("regId");

    public QAbstractRegAuditorModel(String variable) {
        super(AbstractRegAuditorModel.class, forVariable(variable));
    }

    public QAbstractRegAuditorModel(Path<? extends AbstractRegAuditorModel> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAbstractRegAuditorModel(PathMetadata metadata) {
        super(AbstractRegAuditorModel.class, metadata);
    }

}

