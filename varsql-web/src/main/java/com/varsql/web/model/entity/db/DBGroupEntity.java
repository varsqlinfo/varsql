package com.varsql.web.model.entity.db;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.varsql.web.model.base.AabstractAuditorModel;
import com.varsql.web.model.entity.user.RegInfoEntity;
import com.varsql.web.model.id.generator.AppUUIDGenerator;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@SuppressWarnings("serial")
@Getter
@Setter
@NoArgsConstructor
@DynamicUpdate
@Entity
@Table(name = DBGroupEntity._TB_NAME)
public class DBGroupEntity extends AabstractAuditorModel{

	public final static String _TB_NAME="VTDATABASE_GROUP";

	@Id
	@GenericGenerator(name = "groupIdGenerator"
		, strategy = "com.varsql.web.model.id.generator.AppUUIDGenerator"
		, parameters = @Parameter(
            name = AppUUIDGenerator.PREFIX_PARAMETER,
            value = "GR_"
		)
	)
	@GeneratedValue(generator = "groupIdGenerator")
	@Column(name ="GROUP_ID")
	private String groupId;

	@Column(name ="GROUP_NAME")
	private String groupName;

	@Column(name ="GROUP_DESC")
	private String groupDesc;
	
	@OneToOne
	@JoinColumn(name = "REG_ID", referencedColumnName = RegInfoEntity.VIEWID ,nullable = false, insertable =false , updatable =false)
	private RegInfoEntity regInfo;
	
	@JsonManagedReference
	@OneToMany(mappedBy = "userInfo", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private Set<DBGroupMappingUserEntity> userInfos;
	
	@JsonManagedReference
	@OneToMany(mappedBy = "connInfo", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
	private Set<DBGroupMappingDbEntity> connInfos;

	@Builder
	public DBGroupEntity(String groupId, String groupName, String groupDesc) {
		this.groupId = groupId;
		this.groupName = groupName;
		this.groupDesc = groupDesc;
	}
	
	public final static String GROUP_ID="groupId";

	public final static String GROUP_NAME="groupName";

	public final static String GROUP_DESC="groupDesc";

}