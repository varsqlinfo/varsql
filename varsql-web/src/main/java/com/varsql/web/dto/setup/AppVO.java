package com.varsql.web.dto.setup;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * admin VO
 */
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppVO {
	
	// app charset
	@NotBlank
	private String charset;
	
	// export, import 파일 보관 기간 
	private int fileRetentionPeriod;
	
}
