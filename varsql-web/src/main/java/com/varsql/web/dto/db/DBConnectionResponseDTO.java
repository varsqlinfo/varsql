package com.varsql.web.dto.db;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DBConnectionResponseDTO{
	private String vconnid;

	private String vname;

	private String vdbschema;
	
	private String status;

	@JsonIgnore
	private String vurl;

	private LocalDateTime regDt;

	private String regId;

}