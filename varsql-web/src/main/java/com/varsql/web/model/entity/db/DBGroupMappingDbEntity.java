package com.varsql.web.model.entity.db;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.varsql.web.model.base.AbstractRegAuditorModel;
import com.varsql.web.model.id.DBGroupIdNVconnIdID;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Getter
@Entity
@NoArgsConstructor
@IdClass(DBGroupIdNVconnIdID.class)
@Table(name = DBGroupMappingDbEntity._TB_NAME)
public class DBGroupMappingDbEntity extends AbstractRegAuditorModel{
	public final static String _TB_NAME="VTDATABASE_GROUP_DB";
	
	@Id
	private String groupId;
	
	@Id
	private String vconnid;
	
	@JsonBackReference
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "GROUP_ID" ,updatable = false, insertable = false)
	private DBGroupEntity groupDbEntity;
	
	// 그룹 커넥션 정보를 얻기 위한 것
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonBackReference
	@JoinColumn(name = DBConnectionEntity.VCONNID, nullable = false , insertable =false , updatable =false)
	private DBConnectionEntity connInfo;

	@Builder
	public DBGroupMappingDbEntity(String groupId, String vconnid) {
		this.groupId = groupId;
		this.vconnid = vconnid;
	}
	
	public final static String JOIN_CONNINFO="connInfo";
	
	public final static String GROUP_ID="groupId";

	public final static String VCONNID="vconnid";
	
}