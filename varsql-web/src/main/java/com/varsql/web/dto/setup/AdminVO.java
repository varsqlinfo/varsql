package com.varsql.web.dto.setup;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.varsql.web.dto.valid.FieldMatch;

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
@FieldMatch(first = "pw", second = "confirmPw", message = "msg.valid.password.identical")
public class AdminVO {
	
	// admin id
	@NotBlank
	@Size(min = 2, max = 250)
	private String id;
	// admin name
	@NotBlank
	private String name;
	
	// admin email
	@NotBlank
	private String email;
	
	// admin password
	@NotBlank
	private String pw;
	
	@NotBlank
	private String confirmPw;
}
