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
public class TaskTransferResponseDTO extends TaskResponseDTO{
	
	private String transferType; 

	private int commitCount; 
	
	private String sourceVconnid; 
	private String sourceVname; 
	private String sourceConfig; 
	private String sourceUseYn; 

	private String targetVconnid; 
	private String targetVname; 
	private String targetConfig; 
	private String targetUseYn; 
}