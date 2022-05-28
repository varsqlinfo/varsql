package com.varsql.web.model.entity.app;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;

import com.varsql.web.model.base.AbstractAuditorModel;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@DynamicUpdate
@Entity
@Table(name = GlossaryEntity._TB_NAME)
public class GlossaryEntity extends AbstractAuditorModel{

	private static final long serialVersionUID = 1L;

	public final static String _TB_NAME="VTGLOSSARY";

	@Id
	@GeneratedValue(strategy = GenerationType. AUTO)
	@Column(name ="WORD_IDX")
	private Long wordIdx;

	@Column(name ="WORD")
	private String word;

	@Column(name ="WORD_EN")
	private String wordEn;

	@Column(name ="WORD_ABBR")
	private String wordAbbr;

	@Column(name ="WORD_DESC")
	private String wordDesc;

	@Column(name ="WORD_TYPE")
	private String wordType;

	@Column(name ="WORD_LENGTH")
	private String wordLength;

	@Builder
	public GlossaryEntity(Long wordIdx, String word, String wordEn, String wordAbbr, String wordDesc, String wordType, String wordLength) {
		this.wordIdx = wordIdx;
		this.word = word;
		this.wordEn = wordEn;
		this.wordAbbr = wordAbbr;
		this.wordDesc = wordDesc;
		this.wordType = wordType;
		this.wordLength = wordLength;
	}
	public final static String WORD_IDX="wordIdx";

	public final static String WORD="word";

	public final static String WORD_EN="wordEn";

	public final static String WORD_ABBR="wordAbbr";

	public final static String WORD_TYPE="wordType";

	public final static String WORD_LENGTH="wordLength";

	public final static String WORD_DESC="wordDesc";
}