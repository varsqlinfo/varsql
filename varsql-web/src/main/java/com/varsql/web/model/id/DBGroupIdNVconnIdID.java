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
@EqualsAndHashCode(of= {"groupId","vconnid"})
public class DBGroupIdNVconnIdID implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name ="GROUP_ID")
	private String groupId;

	@Column(name ="VCONNID")
	private String vconnid;

}
