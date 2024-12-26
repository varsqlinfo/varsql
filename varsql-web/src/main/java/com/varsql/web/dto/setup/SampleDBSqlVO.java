package com.varsql.web.dto.setup;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class SampleDBSqlVO {
	@JsonProperty("providerInfo")
	private String providerInfo;
	@JsonProperty("providerFileInfo")
	private String providerFileInfo;
	@JsonProperty("connectionInfo")
	private String connectionInfo;
}
