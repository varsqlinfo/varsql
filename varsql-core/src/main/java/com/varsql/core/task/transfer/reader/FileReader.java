package com.varsql.core.task.transfer.reader;

import com.varsql.core.common.job.AbstractJobHandler;
import com.varsql.core.common.job.JobExecuteResult;
import com.varsql.core.task.transfer.SourceVO;

public class FileReader extends AbstractSourceReader{
	
	public FileReader(SourceVO sourceVO, AbstractJobHandler handler) {
		super(sourceVO, handler);
	}
	
	@Override
	public JobExecuteResult read() throws Exception {
		return null;
	}

	@Override
	public void close() throws Exception {
		
	}
}
