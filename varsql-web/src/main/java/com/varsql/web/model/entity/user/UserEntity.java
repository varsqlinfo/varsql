package com.varsql.web.model.entity.user;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.envers.NotAudited;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.varsql.web.model.base.AbstractAuditorModel;
import com.varsql.web.model.converter.BooleanToYnConverter;
import com.varsql.web.model.converter.PasswordEncodeConverter;
import com.varsql.web.model.entity.db.DBBlockingUserEntity;
import com.varsql.web.model.entity.db.DBManagerEntity;
import com.varsql.web.model.id.generator.AppUUIDGenerator;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@DynamicUpdate
@NoArgsConstructor
@Entity
@Table(name = UserEntity._TB_NAME)
public class UserEntity extends AbstractAuditorModel{
	private static final long serialVersionUID = 1L;

	public final static String _TB_NAME="VTUSER";

	@Id
	@GenericGenerator(name = "viewidGenerator"
		, strategy = "com.varsql.web.model.id.generator.AppUUIDGenerator"
		, parameters = @Parameter(
            name = AppUUIDGenerator.PREFIX_PARAMETER,
            value = "U_"
		)
	)
    @GeneratedValue(generator = "viewidGenerator")
	@Column(name ="VIEWID")
	private String viewid;

	@Column(name ="UID")
	private String uid;

	@JsonIgnore
	@Convert(converter = PasswordEncodeConverter.class)
	@Column(name ="UPW")
	private String upw;

	@Column(name ="UNAME")
	private String uname;

	@Column(name ="ORG_NM")
	private String orgNm;

	@Column(name ="DEPT_NM")
	private String deptNm;

	@Column(name ="LANG")
	private String lang;

	@Column(name ="UEMAIL")
	private String uemail;

	@Column(name ="USER_ROLE")
	private String userRole;

	@Column(name ="DESCRIPTION")
	private String description;

	@Column(name ="ACCEPT_YN")
	@Convert(converter = BooleanToYnConverter.class)
	private boolean acceptYn;

	@Column(name ="BLOCK_YN")
	@Convert(converter = BooleanToYnConverter.class)
	private boolean blockYn;

	@NotAudited
	@JsonManagedReference
	@OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private Set<DBManagerEntity> dbList;

	@NotAudited
	@JsonManagedReference
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "userEntity")
	private Set<DBBlockingUserEntity> dbBlockingUserEntity;

	@Builder
	public UserEntity(String viewid, String uid, String upw, String uname, String orgNm, String deptNm, String lang, String uemail, String userRole, String description, boolean acceptYn, boolean blockYn) {
		this.viewid = viewid;
		this.uid = uid;
		this.upw = upw;
		this.uname = uname;
		this.orgNm = orgNm;
		this.deptNm = deptNm;
		this.lang = lang;
		this.uemail = uemail;
		this.userRole = userRole;
		this.description = description;
		this.acceptYn = acceptYn;
		this.blockYn = blockYn;

	}

	public final static String VIEWID="viewid";

	public final static String UID="uid";

	public final static String UPW="upw";

	public final static String UNAME="uname";

	public final static String ORG_NM="orgNm";

	public final static String DEPT_NM="deptNm";

	public final static String LANG="lang";

	public final static String UEMAIL="uemail";

	public final static String USER_ROLE="userRole";

	public final static String DESCRIPTION="description";

	public final static String ACCEPT_YN="acceptYn";

	public final static String BLOCK_YN="blockYn";
	
	public final static String SORT_UNAME= UNAME;
}
