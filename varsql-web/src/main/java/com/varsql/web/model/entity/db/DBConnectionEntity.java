package com.varsql.web.model.entity.db;

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
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.varsql.web.model.base.AabstractAuditorModel;
import com.varsql.web.model.converter.BooleanToDelYnConverter;
import com.varsql.web.model.converter.DbPasswordEncodeConverter;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@DynamicUpdate
@Audited
@Table(name = DBConnectionEntity._TB_NAME)
public class DBConnectionEntity extends AabstractAuditorModel{
	private static final Long serialVersionUID = 1L;
	
	public final static String _TB_NAME="VTCONNECTION";

	@Id
	@GenericGenerator(name = "vconnidGenerator", strategy = "com.varsql.web.model.id.generator.VconnIDGenerator")
    @GeneratedValue(generator = "vconnidGenerator")
	@Column(name ="VCONNID")
	private String vconnid;

	@Column(name ="VNAME")
	private String vname;

	@Column(name ="VDBSCHEMA")
	private String vdbschema;

	@Column(name ="VURL")
	private String vurl;

	@Column(name ="VDRIVER")
	private String vdriver;

	@Column(name ="VTYPE")
	private String vtype;

	@Column(name ="VQUERY")
	private String vquery;

	@Column(name ="VID")
	private String vid;

	@Column(name ="VPW")
	@JsonIgnore
	@Convert(converter = DbPasswordEncodeConverter.class)
	private String vpw;

	@Column(name ="MAX_ACTIVE")
	private Long maxActive;

	@Column(name ="MIN_IDLE")
	private Long minIdle;

	@Column(name ="TIMEOUT")
	private Long timeout;

	@Column(name ="EXPORTCOUNT")
	private Long exportcount;

	@Column(name ="VCONNOPT")
	private String vconnopt;

	@Column(name ="VPOOLOPT")
	private String vpoolopt;

	@Column(name ="VDBVERSION")
	private Long vdbversion;

	@Column(name ="USE_YN")
	private String useYn;

	@Column(name ="SCHEMA_VIEW_YN")
	private String schemaViewYn;

	@Column(name ="DEL_YN")
	@Convert(converter=BooleanToDelYnConverter.class)
	private boolean delYn;

	@Column(name ="BASETABLE_YN")
	private String basetableYn;

	@Column(name ="LAZYLOAD_YN")
	private String lazyloadYn;

	@Column(name ="URL_DIRECT_YN")
	private String urlDirectYn;

	@Column(name ="VSERVERIP")
	private String vserverip;

	@Column(name ="VDATABASENAME")
	private String vdatabasename;

	@Column(name ="VPORT")
	private Long vport;

	@Column(name ="MAX_SELECT_COUNT")
	private Long maxSelectCount;

	@NotAudited
	@JsonManagedReference
	@OneToMany(mappedBy = "dbConnInfo",cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private Set<DBManagerEntity> managerList;
	
//	@NotAudited
//	@JsonManagedReference
//	@OneToMany(mappedBy = "groupConnInfo",cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
//	private Set<DBGroupMappingDbEntity> dbGroupList;
	
	@Builder
	public DBConnectionEntity(String vconnid, String vname, String vdbschema, String vurl, String vdriver, String vtype, String vquery, String vid, String vpw, Long maxActive, Long minIdle, Long timeout, Long exportcount, String vconnopt, String vpoolopt, Long vdbversion, String useYn, String schemaViewYn, boolean delYn, String basetableYn, String lazyloadYn, String urlDirectYn, String vserverip, String vdatabasename, Long vport, Long maxSelectCount) {
		this.vconnid = vconnid;
		this.vname = vname;
		this.vdbschema = vdbschema;
		this.vurl = vurl;
		this.vdriver = vdriver;
		this.vtype = vtype;
		this.vquery = vquery;
		this.vid = vid;
		this.vpw = vpw;
		this.maxActive = maxActive;
		this.minIdle = minIdle;
		this.timeout = timeout;
		this.exportcount = exportcount;
		this.vconnopt = vconnopt;
		this.vpoolopt = vpoolopt;
		this.vdbversion = vdbversion;
		this.useYn = useYn;
		this.schemaViewYn = schemaViewYn;
		this.delYn = delYn;
		this.basetableYn = basetableYn;
		this.lazyloadYn = lazyloadYn;
		this.urlDirectYn = urlDirectYn;
		this.vserverip = vserverip;
		this.vdatabasename = vdatabasename;
		this.vport = vport;
		this.maxSelectCount = maxSelectCount;

	}
	
	public static final String JOIN_MANAGER_LIST = "managerList";
	
	public static final String JOIN_GROUPLIST = "dbGroupList";

	public final static String VCONNID="vconnid";

	public final static String VNAME="vname";

	public final static String VDBSCHEMA="vdbschema";

	public final static String VURL="vurl";

	public final static String VDRIVER="vdriver";

	public final static String VTYPE="vtype";

	public final static String VQUERY="vquery";

	public final static String VID="vid";

	public final static String VPW="vpw";

	public final static String MAX_ACTIVE="maxActive";

	public final static String MIN_IDLE="minIdle";

	public final static String TIMEOUT="timeout";

	public final static String EXPORTCOUNT="exportcount";

	public final static String VCONNOPT="vconnopt";

	public final static String VPOOLOPT="vpoolopt";

	public final static String VDBVERSION="vdbversion";

	public final static String USE_YN="useYn";

	public final static String SCHEMA_VIEW_YN="schemaViewYn";

	public final static String DEL_YN="delYn";

	public final static String BASETABLE_YN="basetableYn";

	public final static String LAZYLOAD_YN="lazyloadYn";

	public final static String URL_DIRECT_YN="urlDirectYn";

	public final static String VSERVERIP="vserverip";

	public final static String VDATABASENAME="vdatabasename";

	public final static String VPORT="vport";

	public final static String MAX_SELECT_COUNT="maxSelectCount";
}
