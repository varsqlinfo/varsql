package com.varsql.web.model.entity.user;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QQnAEntity is a Querydsl query type for QnAEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QQnAEntity extends EntityPathBase<QnAEntity> {

    private static final long serialVersionUID = 1603114358L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QQnAEntity qnAEntity = new QQnAEntity("qnAEntity");

    public final com.varsql.web.model.base.QAabstractAuditorModel _super = new com.varsql.web.model.base.QAabstractAuditorModel(this);

    public final StringPath answer = createString("answer");

    public final DateTimePath<java.sql.Timestamp> answerDt = createDateTime("answerDt", java.sql.Timestamp.class);

    public final StringPath answerId = createString("answerId");

    public final QUserEntity author;

    public final StringPath delYn = createString("delYn");

    public final StringPath qnaid = createString("qnaid");

    public final StringPath question = createString("question");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> regDt = _super.regDt;

    //inherited
    public final StringPath regId = _super.regId;

    public final StringPath title = createString("title");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updDt = _super.updDt;

    //inherited
    public final StringPath updId = _super.updId;

    public QQnAEntity(String variable) {
        this(QnAEntity.class, forVariable(variable), INITS);
    }

    public QQnAEntity(Path<? extends QnAEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QQnAEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QQnAEntity(PathMetadata metadata, PathInits inits) {
        this(QnAEntity.class, metadata, inits);
    }

    public QQnAEntity(Class<? extends QnAEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.author = inits.isInitialized("author") ? new QUserEntity(forProperty("author")) : null;
    }

}

