package com.varsql.web.model.entity.db;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = DBTypeDriverEntity._TB_NAME)
public class DBTypeDriverEntity implements Serializable{
	public final static String _TB_NAME="VTDBTYPE_DRIVER";

	@Id
	@Column(name ="DRIVER_ID")
	private String driverId;

	@Column(name ="DBTYPE")
	private String dbtype;

	@Column(name ="DBDRIVER")
	private String dbdriver;

	@Column(name ="VALIDATION_QUERY")
	private String validationQuery;

	@Column(name ="URL_FORMAT")
	private String urlFormat;

	@Column(name ="DEFAULT_PORT")
	private int defaultPort;

	@Column(name ="SCHEMA_TYPE")
	private String schemaType;

	@Column(name ="DRIVER_DESC")
	private String driverDesc;


	@Builder
	public DBTypeDriverEntity(String driverId, String dbtype, String dbdriver, String validationQuery, String urlFormat, int defaultPort, String schemaType, String driverDesc) {
		this.driverId = driverId;
		this.dbtype = dbtype;
		this.dbdriver = dbdriver;
		this.validationQuery = validationQuery;
		this.urlFormat = urlFormat;
		this.defaultPort = defaultPort;
		this.schemaType = schemaType;
		this.driverDesc = driverDesc;

	}

	public final static String DRIVER_ID="driverId";

	public final static String DBTYPE="dbtype";

	public final static String DBDRIVER="dbdriver";

	public final static String VALIDATION_QUERY="validationQuery";

	public final static String URL_FORMAT="urlFormat";

	public final static String DEFAULT_PORT="defaultPort";

	public final static String SCHEMA_TYPE="schemaType";

	public final static String DRIVER_DESC="driverDesc";
}