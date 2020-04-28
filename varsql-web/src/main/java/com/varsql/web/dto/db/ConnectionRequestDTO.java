package com.varsql.web.dto.db;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Range;

import com.varsql.web.dto.valid.ValidUrlDirectYn;
import com.varsql.web.model.entity.db.DBConnectionEntity;
import com.varsql.web.util.ValidateUtils;

import lombok.Getter;
import lombok.Setter;

/**
 *
 *
*-----------------------------------------------------------------------------
* @PROJECT	: varsql
* @NAME		: Vtconnection.java
* @DESC		: 커넥션 정보.
* @AUTHOR	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
*2018. 2. 19. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
@Getter
@Setter
@ValidUrlDirectYn
public class ConnectionRequestDTO{
	@Size(max=5)
	private String vconnid;

	@NotEmpty
	@Size(max=250)
	private String vname;

	@Size(max=45)
	private String vserverip;

	@Size(max=6)
	private String vport;

	@Size(max=250)
	private String vdatabasename;

	@Size(max=250)
	private String vurl;

	@NotEmpty
	@Size(max=100)
	private String vdriver;

	@NotEmpty
	@Size(max=100)
	private String vtype;

	@Size(max=100)
	private String vid;

	@Size(max=500)
	private String vpw;

	@Size(max=500)
	private String confirmPw;

	@Size(max=1)
	private String urlDirectYn;

	private String vdbversion;

	private String userId;

	private String poolInit;

	private String vdbschema;

	@NotEmpty
	@Size(max=1)
	private String useYn;

	@NotEmpty
	@Size(max=1)
	private String basetableYn;

	@NotEmpty
	@Size(max=1)
	private String lazyloadYn;

	@NotEmpty
	@Size(max=1)
	private String schemaViewYn;

	@NotNull
	@Range(min=-1, max=1000)
	private Integer maxActive;

	@NotNull
	@Range(min=-1, max=1000)
	private Integer minIdle;

	@NotNull
	@Range(min=-1, max=1000000)
	private Integer timeout;

	@NotNull
	@Range(min=-1, max=100000000)
	private Integer exportcount;

	@NotNull
	@Range(min=-1, max=100000000)
	private Integer maxSelectCount;

	@Size(max=1000)
	private String vquery;

	public DBConnectionEntity toEntity() {
		return DBConnectionEntity.builder()
				.vconnid(vconnid)
				.vname(vname)
				.vserverip(vserverip)
				.vport(ValidateUtils.longValueOf(vport))
				.vdatabasename(vdatabasename)
				.vurl(vurl)
				.vdriver(vdriver)
				.vtype(vtype)
				.vid(vid)
				.vpw(vpw)
				.urlDirectYn(urlDirectYn)
				.vdbversion(ValidateUtils.longValueOf(vdbversion))
				.vdbschema(vdbschema)
				.useYn(useYn)
				.basetableYn(basetableYn)
				.schemaViewYn(schemaViewYn)
				.lazyloadYn(lazyloadYn)
				.maxActive(ValidateUtils.longValueOf(maxActive))
				.minIdle(ValidateUtils.longValueOf(minIdle))
				.timeout(ValidateUtils.longValueOf(timeout))
				.exportcount(ValidateUtils.longValueOf(exportcount))
				.maxSelectCount(ValidateUtils.longValueOf(maxSelectCount))
				.vquery(vquery).build();
	}
}