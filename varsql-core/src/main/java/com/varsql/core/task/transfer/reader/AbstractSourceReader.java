package com.varsql.core.task.transfer.reader;

import com.varsql.core.common.job.AbstractJobHandler;
import com.varsql.core.task.transfer.SourceVO;

/**
 * Abstract source reader
 * 
 * @author ytkim
 *
 */
public abstract class AbstractSourceReader implements SourceReader, AutoCloseable {
	
	protected SourceVO sourceVO;
	
	protected AbstractJobHandler handler;
	
	public AbstractSourceReader(SourceVO sourceVO, AbstractJobHandler handler) {
		this.sourceVO = sourceVO;
		this.handler = handler;
	}
}
