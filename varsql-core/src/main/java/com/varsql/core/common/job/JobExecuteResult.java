package com.varsql.core.common.job;

import com.varsql.core.common.code.VarsqlAppCode;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * job execute result
 * @author ytkim
 *
 */
@Getter
@Setter
@ToString(exclude = "result")
public class JobExecuteResult {
	private long startTime;
	private long endTime;
	private String message;
	private VarsqlAppCode resultCode;
	private long totalCount;
	private long executeCount;
	private long failCount;
	private Object result;

	@SuppressWarnings("unchecked")
	public <T>T getResult() {
		return (T)result;
	}

	public void setResult(Object result) {
		this.result = result;
	}
}
