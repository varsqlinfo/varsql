package com.varsql.web.dto.scheduler;
import java.io.Serializable;
import java.util.LinkedList;

import com.varsql.web.dto.task.TaskExecutionVO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * batch task job 정보 
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
public class TaskJobVO implements Serializable{

	private boolean continueOnError;
	
	private LinkedList<TaskExecutionVO> mappedTaskList;

}