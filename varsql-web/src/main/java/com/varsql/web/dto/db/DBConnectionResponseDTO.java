package com.varsql.web.dto.db;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DBConnectionResponseDTO{
	private String vconnid;

	private String vname;

	private String vdbschema;

	@JsonIgnore
	private String vurl;

	private LocalDateTime regDt;

	private String regId;

}