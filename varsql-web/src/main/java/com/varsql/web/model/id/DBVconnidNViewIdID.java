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
@EqualsAndHashCode(of= {"vconnid","viewid"})
public class DBVconnidNViewIdID implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name ="VCONNID")
	private String vconnid;

	@Column(name ="VIEWID")
	private String viewid;

}
