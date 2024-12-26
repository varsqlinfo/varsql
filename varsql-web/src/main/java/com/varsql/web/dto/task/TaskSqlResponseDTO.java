package com.varsql.web.dto.task;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * task 
 * @author ytkim
 *
 */
@Getter
@Setter
@NoArgsConstructor
public class TaskSqlResponseDTO extends TaskResponseDTO{
	private String vconnid; 
	private String vname; 
	private String useYn; 
	private String sql; 
	private String parameter; 

}