package com.varsql.web.dto.db;

import javax.persistence.Transient;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.varsql.web.dto.valid.ValidUrlDirectYn;
import com.varsql.web.model.entity.db.DBConnectionEntity;
import com.varsql.web.model.entity.db.DBTypeDriverProviderEntity;
import com.varsql.web.util.ConvertUtils;
import com.vartech.common.utils.StringUtils;

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
public class DBConnectionRequestDTO{
	@Size(max=5)
	private String vconnid;

	@NotEmpty
	@Size(max=250)
	private String vname;

	@Size(max=45)
	private String vserverip;

	@NotNull
	@Min(value=-1)
	@Max(value=65535)
	private int vport;

	@Size(max=250)
	private String vdatabasename;

	@Size(max=250)
	private String vurl;

	@NotEmpty
	@Size(max=100)
	private String vdriver;

	@Size(max=100)
	private String vid;

	@Transient
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
	@Min(value=-1)
	@Max(value=1000)
	private int maxActive;

	@NotNull
	@Min(value=-1)
	@Max(value=1000)
	private int minIdle;

	@NotNull
	@Min(value=-1)
	@Max(value=1000000)
	private int timeout;

	@NotNull
	@Min(value=-1)
	@Max(value=100000000)
	private long exportcount;

	@NotNull
	@Min(value=-1)
	@Max(value=100000000)
	private long maxSelectCount;

	@NotEmpty
	@Size(max=1)
	private String useColumnLabel;
	
	@NotEmpty
	@Size(max=1)
	private String testWhileIdle;
	
	@Size(max=1)
	private String enableConnectionPool;

	private boolean passwordChange;

	public void setPasswordChange(String passwordChange) {
		this.passwordChange = Boolean.parseBoolean(passwordChange);
	}

	public DBConnectionEntity toEntity() {
		return DBConnectionEntity.builder()
				.vconnid(vconnid)
				.vname(vname)
				.vserverip(vserverip)
				.vport(vport)
				.vdatabasename(vdatabasename)
				.vurl(StringUtils.trim(vurl) )
				.dbTypeDriverProvider(DBTypeDriverProviderEntity.builder().driverProviderId(vdriver).build())
				.vid(vid)
				.vpw(vpw)
				.urlDirectYn(urlDirectYn)
				.vdbversion(ConvertUtils.longValueOf(vdbversion))
				.vdbschema(vdbschema)
				.useYn(useYn)
				.basetableYn(basetableYn)
				.schemaViewYn(schemaViewYn)
				.lazyloadYn(lazyloadYn)
				.maxActive(maxActive)
				.minIdle(minIdle)
				.timeout(timeout)
				.exportcount(exportcount)
				.maxSelectCount(maxSelectCount)
				.useColumnLabel(useColumnLabel)
				.testWhileIdle(testWhileIdle)
				.enableConnectionPool(enableConnectionPool)
				.build();
	}
}