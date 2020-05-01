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
@EqualsAndHashCode(of= {"groupId","viewid"})
public class DBGroupIdNViewIdID implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name ="GROUP_ID")
	private String groupId;

	@Column(name ="VIEWID")
	private String viewid;

}
