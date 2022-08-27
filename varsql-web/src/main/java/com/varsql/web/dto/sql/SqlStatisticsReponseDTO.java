package com.varsql.web.dto.sql;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SqlStatisticsReponseDTO {
	private LocalDateTime startTime;
	private String viewDt;
	private Long cnt;
	
	private Integer sMm;
	private Integer sDd;
	
	@JsonProperty("xCol")
	private String xCol;
	@JsonProperty("yCol")
	private Long yCol;
	
	public SqlStatisticsReponseDTO(LocalDateTime startTime, Integer sMm, Integer sDd, Long yCol) {
		this.startTime = startTime; 
		this.yCol = yCol; 
		this.sMm = sMm; 
		this.sDd = sDd; 
	}
	
	public SqlStatisticsReponseDTO(String xCol, Long yCol) {
		this.xCol = xCol; 
		this.yCol = yCol; 
	}
	
}

