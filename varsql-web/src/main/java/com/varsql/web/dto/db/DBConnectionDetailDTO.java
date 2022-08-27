package com.varsql.web.dto.db;

import lombok.Getter;
import lombok.Setter;

/**
 * 커넥션 상세 정보
* 
* @fileName	: DBConnectionDetailDTO.java
* @author	: ytkim
 */
@Getter
@Setter
public class DBConnectionDetailDTO{
	private String vconnid;

	private String vname;

	private String vserverip;

	private int vport;

	private String vdatabasename;

	private String vurl;

	private String vdriver;

	private String vid;

	private String vpw;

	private String confirmPw;

	private String urlDirectYn;

	private String vdbversion;

	private String userId;

	private String poolInit;

	private String vdbschema;

	private String useYn;

	private String basetableYn;

	private String lazyloadYn;

	private String schemaViewYn;

	private int maxActive;

	private int minIdle;

	private int timeout;

	private long exportcount;

	private long maxSelectCount;

	private String useColumnLabel;
	
	private String testWhileIdle;
}