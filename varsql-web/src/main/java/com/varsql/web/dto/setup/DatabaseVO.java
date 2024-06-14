package com.varsql.web.dto.setup;

import javax.validation.constraints.NotBlank;

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
public class DatabaseVO {
	
	// main db type
	@NotBlank
	private String type;
	
	private String driverClass;
	
	// db jdbc url
	private String url;
	
	// db username 
	private String username;
	
	// db password
	private String pw;
	private String confirmPw;
	
	
}
