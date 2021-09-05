package com.varsql.web.model.entity.db;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.varsql.web.model.base.AbstractRegAuditorModel;
import com.varsql.web.model.entity.user.UserEntity;
import com.varsql.web.model.id.DBVconnidNViewIdID;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Getter
@NoArgsConstructor
@IdClass(DBVconnidNViewIdID.class)
@Entity
@Table(name = DBBlockingUserEntity._TB_NAME)
public class DBBlockingUserEntity extends AbstractRegAuditorModel{
	public final static String _TB_NAME="VTDATABASE_BLOCK_USER";

	@Id
	@Column(name ="VCONNID")
	private String vconnid;

	@Id
	@Column(name ="VIEWID")
	private String viewid;


	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="VIEWID", insertable = false , updatable = false)
	private UserEntity userEntity;

	@Builder
	public DBBlockingUserEntity(String vconnid, String viewid) {
		this.vconnid = vconnid;
		this.viewid = viewid;
	}

	public final static String VCONNID="vconnid";

	public final static String VIEWID="viewid";

}