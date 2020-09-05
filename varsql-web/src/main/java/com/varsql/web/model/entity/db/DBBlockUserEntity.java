package com.varsql.web.model.entity.db;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import com.varsql.web.model.base.AbstractRegAuditorModel;
import com.varsql.web.model.id.DBVconnidNViewIdID;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Getter
@NoArgsConstructor
@IdClass(DBVconnidNViewIdID.class)
@Entity
@Table(name = DBBlockUserEntity._TB_NAME)
public class DBBlockUserEntity extends AbstractRegAuditorModel{
	public final static String _TB_NAME="VTDATABASE_BLOCK_USER";

	@Id
	@Column(name ="VCONNID")
	private String vconnid;

	@Id
	@Column(name ="VIEWID")
	private String viewid;

	@Builder
	public DBBlockUserEntity(String vconnid, String viewid) {
		this.vconnid = vconnid;
		this.viewid = viewid;
	}

	public final static String VCONNID="vconnid";

	public final static String VIEWID="viewid";

}