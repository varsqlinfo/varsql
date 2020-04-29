package com.varsql.web.model.entity.app;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QGlossaryEntity is a Querydsl query type for GlossaryEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QGlossaryEntity extends EntityPathBase<GlossaryEntity> {

    private static final long serialVersionUID = 1869314912L;

    public static final QGlossaryEntity glossaryEntity = new QGlossaryEntity("glossaryEntity");

    public final com.varsql.web.model.base.QAabstractAuditorModel _super = new com.varsql.web.model.base.QAabstractAuditorModel(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> regDt = _super.regDt;

    //inherited
    public final StringPath regId = _super.regId;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updDt = _super.updDt;

    //inherited
    public final StringPath updId = _super.updId;

    public final StringPath word = createString("word");

    public final StringPath wordAbbr = createString("wordAbbr");

    public final StringPath wordDesc = createString("wordDesc");

    public final StringPath wordEn = createString("wordEn");

    public final NumberPath<Long> wordIdx = createNumber("wordIdx", Long.class);

    public QGlossaryEntity(String variable) {
        super(GlossaryEntity.class, forVariable(variable));
    }

    public QGlossaryEntity(Path<? extends GlossaryEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QGlossaryEntity(PathMetadata metadata) {
        super(GlossaryEntity.class, metadata);
    }

}

