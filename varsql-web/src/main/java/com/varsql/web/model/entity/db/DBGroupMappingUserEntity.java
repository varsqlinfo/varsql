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
	private String groupId;
	
	@Id
	private String viewid;
	
	@JsonBackReference
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "GROUP_ID" ,updatable = false, insertable = false)
	private DBGroupEntity groupUserEntity;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonBackReference
	@JoinColumn(name = "VIEWID", nullable = false, insertable =false , updatable =false)
	private UserEntity userInfo;

	@Builder
	public DBGroupMappingUserEntity(String groupId, String viewid, DBGroupEntity groupUserEntity, UserEntity userInfo) {
		this.groupId = groupId;
		this.viewid = viewid;
		this.groupUserEntity = groupUserEntity;
		this.userInfo = userInfo;

	}
	
	public final static String JOIN_USERINFO="userInfo";
	
	public final static String GROUP_ID="groupId";

	public final static String VIEWID="viewid";
	
	// 정렬 정보
	public final static String SORT_USERINFO = JOIN_USERINFO+"."+ UserEntity.UNAME ;

}