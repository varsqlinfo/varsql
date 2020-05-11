package com.varsql.web.model.entity.app;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.varsql.web.model.base.AbstractRegAuditorModel;
import com.varsql.web.model.converter.BooleanToDelYnConverter;
import com.varsql.web.model.id.generator.AppUUIDGenerator;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@SuppressWarnings("serial")
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = NoteEntity._TB_NAME)
public class NoteEntity extends AbstractRegAuditorModel{
	public final static String _TB_NAME="VTNOTE";
	
	@Id
	@GenericGenerator(name = "noteIdGenerator"
		, strategy = "com.varsql.web.model.id.generator.AppUUIDGenerator"
		, parameters = @Parameter(
            name = AppUUIDGenerator.PREFIX_PARAMETER,
            value = ""
		)
	)
    @GeneratedValue(generator = "noteIdGenerator")
	@Column(name ="NOTE_ID")
	private String noteId;

	@Column(name ="PARENT_NOTE_ID")
	private String parentNoteId;

	@Column(name ="NOTE_TITLE")
	private String noteTitle;

	@Column(name ="NOTE_CONT")
	private String noteCont;
	
	@Column(name ="DEL_YN")
	@Convert(converter=BooleanToDelYnConverter.class)
	private boolean delYn;
	
	@OneToMany(mappedBy="noteInfo")
	private Set<NoteMappingUserEntity> recvList; 

	@Builder
	public NoteEntity(String noteId, String parentNoteId, String noteTitle, String noteCont, boolean delYn) {
		this.noteId = noteId;
		this.parentNoteId = parentNoteId;
		this.noteTitle = noteTitle;
		this.noteCont = noteCont;
		this.delYn = delYn;
	}
	
	public static final String JOIN_RECV_LIST = "recvList";
	
	public final static String NOTE_ID="noteId";

	public final static String PARENT_NOTE_ID="parentNoteId";

	public final static String NOTE_TITLE="noteTitle";

	public final static String NOTE_CONT="noteCont";

	public final static String DEL_YN="delYn";
}