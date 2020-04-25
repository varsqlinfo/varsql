package com.varsql.web.model.base;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QAabstractAuditorModel is a Querydsl query type for AabstractAuditorModel
 */
@Generated("com.querydsl.codegen.SupertypeSerializer")
public class QAabstractAuditorModel extends EntityPathBase<AabstractAuditorModel> {

    private static final long serialVersionUID = 1407254282L;

    public static final QAabstractAuditorModel aabstractAuditorModel = new QAabstractAuditorModel("aabstractAuditorModel");

    public final QAbstractRegAuditorModel _super = new QAbstractRegAuditorModel(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> regDt = _super.regDt;

    //inherited
    public final StringPath regId = _super.regId;

    public final DateTimePath<java.time.LocalDateTime> updDt = createDateTime("updDt", java.time.LocalDateTime.class);

    public final StringPath updId = createString("updId");

    public QAabstractAuditorModel(String variable) {
        super(AabstractAuditorModel.class, forVariable(variable));
    }

    public QAabstractAuditorModel(Path<? extends AabstractAuditorModel> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAabstractAuditorModel(PathMetadata metadata) {
        super(AabstractAuditorModel.class, metadata);
    }

}

