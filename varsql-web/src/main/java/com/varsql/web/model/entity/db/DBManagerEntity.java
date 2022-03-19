package com.varsql.web.model.entity.db;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.varsql.web.model.base.AbstractRegAuditorModel;
import com.varsql.web.model.entity.user.UserEntity;
import com.varsql.web.model.id.DBVconnidNViewIdID;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@IdClass(DBVconnidNViewIdID.class)
@Table(name = DBManagerEntity._TB_NAME)
public class DBManagerEntity extends AbstractRegAuditorModel{
	private static final long serialVersionUID = 1L;
	
	public final static String _TB_NAME="VTDATABASE_MANAGER";

	@Id
	@Column(name =DBConnectionEntity.VCONNID)
	private String vconnid;

	@Id
	@Column(name =UserEntity.VIEWID)
	private String viewid;

	@ManyToOne(fetch = FetchType.LAZY)
	@JsonBackReference
	@JoinColumn(name = "VIEWID", nullable = false, insertable =false , updatable =false)
	private UserEntity user;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonBackReference
	@JoinColumn(name = "VCONNID", nullable = false , insertable =false , updatable =false)
	private DBConnectionEntity dbConnInfo;

	@Builder
	public DBManagerEntity(String vconnid, String viewid, UserEntity user, DBConnectionEntity dbConnInfo) {
		this.vconnid = vconnid;
		this.viewid = viewid;
		this.user = user;
		this.dbConnInfo = dbConnInfo;

	}
	public final static String JOIN_USERINFO="user";
	
	public final static String VCONNID="vconnid";

	public final static String VIEWID="viewid";
	
	// 정렬 정보
	public final static String SORT_USERINFO = JOIN_USERINFO+"."+ UserEntity.UNAME ;
}