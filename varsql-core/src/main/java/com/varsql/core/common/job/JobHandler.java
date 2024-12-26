package com.varsql.core.common.job;

public interface JobHandler {
	public JobStatus handler(Object obj) throws Exception;
}
