package com.varsql.web.model.id;

import java.io.Serializable;

import javax.persistence.Column;

import com.varsql.web.model.entity.db.DBConnectionEntity;
import com.varsql.web.model.entity.user.UserEntity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode(of= {"vconnid","viewid"})
public class DBManagerId implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name =DBConnectionEntity.VCONNID)
	private String vconnid;

	@Column(name =UserEntity.VIEWID)
	private String viewid;

}
