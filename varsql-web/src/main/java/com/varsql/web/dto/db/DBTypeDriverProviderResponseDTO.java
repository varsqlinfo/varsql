package com.varsql.web.dto.db;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.varsql.web.model.entity.db.DBTypeDriverProviderEntity;
import com.varsql.web.model.mapper.db.DBTypeJdbcDriverMapper;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DBTypeDriverProviderResponseDTO {

	private String driverProviderId;
	private String driverId;
	private String providerName;
	private String dbType;
	private String directYn;
	private String driverClass;
	private String pathType;
	private String validationQuery;
	private String driverDesc;
	private String driverPath;

	private List<DBTypeDriverFileDTO> fileList = new ArrayList<>();

	public DBTypeDriverProviderResponseDTO(String driverProviderId, String providerName, String dbType, String driverClass, String pathType) {
		this.driverProviderId = driverProviderId;
		this.providerName = providerName;
		this.dbType = dbType;
		this.driverClass = driverClass;
		this.pathType = pathType;
	}

	public static DBTypeDriverProviderResponseDTO detailDto(DBTypeDriverProviderEntity entity) {
		DBTypeDriverProviderResponseDTO dto = DBTypeJdbcDriverMapper.INSTANCE.toDto(entity);

		dto.setFileList(entity.getDriverFiles().stream().map(item->{
			return DBTypeDriverFileDTO.toDto(item);
		}).collect(Collectors.toList()));

		return dto;
	}
}