package com.varsql.web.model.entity.app;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.varsql.web.model.entity.user.RegInfoEntity;
import com.varsql.web.model.id.NoteCompositeID;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@IdClass(NoteCompositeID.class)
@Table(name = NoteMappingUserEntity._TB_NAME)
public class NoteMappingUserEntity{
	public final static String _TB_NAME="VTNOTE_USER";
	
	@Id
	private String noteId;
	
	@Id
	private String sendId;
	
	@Id
	private String recvId;

	@Column(name ="VIEW_DT")
	private Timestamp viewDt;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name ="NOTE_ID" ,insertable =false, updatable =false)
	private NoteEntity noteInfo;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "RECV_ID" ,nullable = false, insertable =false , updatable =false)
	private RegInfoEntity recvInfo;

	@Builder
	public NoteMappingUserEntity(String noteId, String sendId, String recvId, Timestamp viewDt) {
		this.noteId = noteId;
		this.sendId = sendId;
		this.recvId = recvId;
		this.viewDt = viewDt;

	}
	public final static String NOTE_ID="noteId";

	public final static String SEND_ID="sendId";

	public final static String RECV_ID="recvId";

	public final static String VIEW_DT="viewDt";

}