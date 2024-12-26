package com.varsql.core.task.transfer.reader;

import com.varsql.core.common.job.JobExecuteResult;

public interface SourceReader {
	public JobExecuteResult read() throws Exception;
}
