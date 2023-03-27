package com.varsql.web.model.id;

import java.io.Serializable;

import javax.persistence.Column;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode(of= {"noteId","sendId","recvId"})
public class NoteCompositeID implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name ="NOTE_ID")
	private String noteId;
	
	@Column(name ="SEND_ID")
	private String sendId;
	
	@Column(name ="RECV_ID")
	private String recvId;


}
