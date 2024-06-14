package com.varsql.web.dto.setup;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SetupConfigDTO {
	
	@NotNull
	private AdminVO userInfo;
	
	@NotNull
	private AppVO appInfo;
	
	@NotNull
	private DatabaseVO dbInfo;
}
