package com.varsql.web.model.entity.db;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.varsql.web.model.base.AbstractAuditorModel;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@DynamicUpdate
@NoArgsConstructor
@Entity
@Table(name = DBTypeDriverProviderEntity._TB_NAME)
public class DBTypeDriverProviderEntity extends AbstractAuditorModel {
	private static final long serialVersionUID = 1L;

	public final static String _TB_NAME = "VTDBTYPE_DRIVER_PROVIDER";

	@Id
	@GenericGenerator(name = "driverProviderIdGenerator", strategy = "com.varsql.web.model.id.generator.AppUUIDGenerator", parameters = {
			@Parameter(name = "prefix", value = "") })
	@GeneratedValue(generator = "driverProviderIdGenerator")
	@Column(name = "DRIVER_PROVIDER_ID")
	private String driverProviderId;
	
	@Column(name = "DRIVER_ID")
	private String driverId;

	@Column(name = "PROVIDER_NAME")
	private String providerName;

	@Column(name = "DB_TYPE")
	private String dbType;

	@Column(name = "DIRECT_YN")
	private String directYn;

	@Column(name = "DRIVER_CLASS")
	private String driverClass;

	@Column(name = "PATH_TYPE")
	private String pathType;

	@Column(name = "VALIDATION_QUERY")
	private String validationQuery;

	@Column(name = "DRIVER_DESC")
	private String driverDesc;

	@Column(name = "DRIVER_PATH")
	private String driverPath;

	@OneToMany(mappedBy = "driverProviderFiles", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<DBTypeDriverFileEntity> driverFiles = new ArrayList<>();
	
	@OneToMany(mappedBy = "dbTypeDriverProvider", fetch = FetchType.LAZY)
	private List<DBConnectionEntity> driverDbConnection = new ArrayList<>();

	@Builder
	public DBTypeDriverProviderEntity(String driverProviderId, String driverId, String providerName, String dbType,
			String directYn, String driverClass, String pathType, String validationQuery, String driverDesc,
			String driverPath) {
		this.driverProviderId = driverProviderId;
		this.driverId = driverId;
		this.providerName = providerName;
		this.dbType = dbType;
		this.directYn = directYn;
		this.driverClass = driverClass;
		this.pathType = pathType;
		this.validationQuery = validationQuery;
		this.driverDesc = driverDesc;
		this.driverPath = driverPath;
	}

	public final static String DRIVER_PROVIDER_ID = "driverProviderId";
	public final static String DRIVER_ID = "driverId";
	public final static String PROVIDER_NAME = "providerName";
	public final static String DB_TYPE = "dbType";
	public final static String DIRECT_YN = "directYn";
	public final static String DRIVER_CLASS = "driverClass";
	public final static String PATH_TYPE = "pathType";
	public final static String VALIDATION_QUERY = "validationQuery";
	public final static String DRIVER_DESC = "driverDesc";
	public final static String DRIVER_PATH = "driverPath";
}