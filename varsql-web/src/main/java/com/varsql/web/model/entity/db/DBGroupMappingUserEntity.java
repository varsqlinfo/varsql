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
import com.varsql.web.model.id.DBGroupIdNViewIdID;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Getter
@Entity
@NoArgsConstructor
@IdClass(DBGroupIdNViewIdID.class)
@Table(name = DBGroupMappingUserEntity._TB_NAME)
public class DBGroupMappingUserEntity extends AbstractRegAuditorModel{
	public final static String _TB_NAME="VTDATABASE_GROUP_USER";
	
	@Id
	@Column(name ="GROUP_ID")
	private String groupId;
	
	@Id
	@Column(name ="VIEWID")
	private String viewid;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonBackReference
	@JoinColumn(name = UserEntity.VIEWID, nullable = false, insertable =false , updatable =false)
	private UserEntity userInfo;

	@Builder
	public DBGroupMappingUserEntity(String groupId, String viewid) {
		this.groupId = groupId;
		this.viewid = viewid;

	}
	public final static String GROUP_ID="groupId";

	public final static String VIEWID="viewid";

}