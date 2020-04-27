package com.varsql.web.model.entity.user;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.varsql.web.model.base.AabstractAuditorModel;
import com.varsql.web.model.id.generator.AppUUIDGenerator;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@DynamicUpdate
@Entity
@Table(name = QnAEntity._TB_NAME)
public class QnAEntity extends AabstractAuditorModel{
	public final static String _TB_NAME="VTQNA";
	
	@Id
	@GenericGenerator(name = "qnaidGenerator", strategy = "com.varsql.web.model.id.generator.AppUUIDGenerator"
		, parameters = @Parameter(
            name = AppUUIDGenerator.PREFIX_PARAMETER,
            value = "Q_"
		)
	)
    @GeneratedValue(generator = "qnaidGenerator")
	@Column(name ="QNAID")
	private String qnaid;

	@Column(name ="TITLE")
	private String title;

	@Column(name ="QUESTION")
	private String question;

	@Column(name ="ANSWER")
	private String answer;

	@Column(name ="ANSWER_ID")
	private String answerId;

	@Column(name ="ANSWER_DT")
	private Timestamp answerDt;

	@Column(name ="DEL_YN")
	private String delYn;
	
	@OneToOne
	@JoinColumn(name = "REG_ID" , referencedColumnName = UserEntity.VIEWID ,nullable = false, insertable =false , updatable =false)
	private UserEntity author;

	@Builder
	public QnAEntity(String qnaid, String title, String question, String answer, String answerId, Timestamp answerDt, String delYn) {
		this.qnaid = qnaid;
		this.title = title;
		this.question = question;
		this.answer = answer;
		this.answerId = answerId;
		this.answerDt = answerDt;
		this.delYn = delYn;

	}
	public final static String QNAID="qnaid";

	public final static String TITLE="title";

	public final static String QUESTION="question";

	public final static String ANSWER="answer";

	public final static String ANSWER_ID="answerId";

	public final static String ANSWER_DT="answerDt";

	public final static String DEL_YN="delYn";

	public final static String REG_ID="regId";

	public final static String REG_DT="regDt";

	public final static String UPD_ID="updId";
	
	public final static String UPD_DT="updDt";

}