package com.varsql.web.dto.db;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import org.springframework.web.multipart.MultipartFile;

import com.varsql.web.model.entity.db.DBTypeDriverProviderEntity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DBTypeDriverProviderRequestDTO {

	private String driverProviderId;

	@Size(max = 3)
	private String driverId;

	@NotEmpty
	@Size(max = 255)
	private String providerName;

	@NotEmpty
	@Size(max = 50)
	private String dbType;

	@Size(max = 1)
	private String directYn;

	@NotEmpty
	@Size(max = 100)
	private String driverClass;

	@NotEmpty
	@Size(max = 4)
	private String pathType;

	@Size(max = 255)
	private String validationQuery;

	@Size(max = 255)
	private String driverDesc;

	@Size(max = 2000)
	private String driverPath;

	private String removeFileIds;

	private List<MultipartFile> file;

	@Builder
	public DBTypeDriverProviderRequestDTO (String driverProviderId ,String driverId ,String providerName ,String dbType ,String directYn ,String driverClass ,String pathType ,String validationQuery ,String driverDesc ,String driverPath) {
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

	public DBTypeDriverProviderEntity toEntity() {

		return DBTypeDriverProviderEntity.builder()
			.driverProviderId(driverProviderId)
			.driverId(driverId)
			.providerName(providerName)
			.dbType(dbType)
			.directYn(directYn)
			.driverClass(driverClass)
			.pathType(pathType)
			.validationQuery(validationQuery)
			.driverDesc(driverDesc)
			.driverPath(driverPath)
			.build();
	}

}