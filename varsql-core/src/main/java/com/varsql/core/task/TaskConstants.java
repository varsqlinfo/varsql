package com.varsql.core.task;

import com.varsql.core.common.job.AbstractJobHandler;
import com.varsql.core.task.transfer.SourceVO;
import com.varsql.core.task.transfer.TargetVO;
import com.varsql.core.task.transfer.reader.AbstractSourceReader;
import com.varsql.core.task.transfer.reader.DBReader;
import com.varsql.core.task.transfer.writer.AbstractTargetWriter;
import com.varsql.core.task.transfer.writer.DBWriter;
import com.vartech.common.constants.EnumCommon;

import lombok.Getter;

/**
 * task 
 * 
 * @author ytkim
 *
 */
public interface TaskConstants{
	
	static final int READ_PAGE_SIZE = 1000;
	
	public static int getPageSize(int size) {
		return size > 0 ? size : READ_PAGE_SIZE;
	}
	
	/**
	 * 데이터 마이그 타입  
	 * 
	 * @author ytkim
	 *
	 */
	@Getter
	enum TRANSFER_TYPE implements EnumCommon{
		INSERT("1")		// 무조건 등록.
		,INSERT_UPDATE("2") // update 없으면 insert 
		,CLEAR_INSERT("3");// 전체 삭제후 등록.
		
		private String code;
		
		private boolean enabled;

		TRANSFER_TYPE(String code) {
			this(code, true);
		}
		
		TRANSFER_TYPE(String code, boolean enabled) {
			this.code = code;
			this.enabled = enabled; 
		}
	}
	
	@Getter
	enum DB_READ_TYPE implements EnumCommon{
		PAGING("page"), CURSOR("cursor");

		private String code;
		
		DB_READ_TYPE(String code) {
			this.code = code; 
		}
	}
	
	/**
	 * data select type
	 * 
	 * @author ytkim
	 *
	 */
	@Getter
	enum SOURCE_TYPE implements EnumCommon, Reader{
		TABLE("table",true){
			public AbstractSourceReader getSourceReader(SourceVO source, AbstractJobHandler handler) {
				return new DBReader(source, handler); 
			}
		}, 
		SQL("sql",true){
			public AbstractSourceReader getSourceReader(SourceVO source, AbstractJobHandler handler) {
				return new DBReader(source, handler); 
			}
		}, 
		CSV("csv", false), 
		JSON("json", false), 
		XML("xml", false), 
		OTHER("other", false);
		
		private String code;

		private boolean enabled;

		SOURCE_TYPE(String code) {
			this(code, true);
		}
		
		SOURCE_TYPE(String code, boolean enabled) {
			this.code = code;
			this.enabled = enabled;
		}
	}
	
	/**
	 * data transfer target 타입
	 * 
	 * @author ytkim
	 *
	 */
	@Getter
	enum DATA_WRITE_TYPE implements EnumCommon, Writer{
		TABLE("table"){
			public AbstractTargetWriter getTargetWriter(TargetVO target) {
				return new DBWriter(target); 
			}
		}, 
		FILE("file",false), 
		OTHER("other", false);
		
		private String code;

		private boolean enabled;

		DATA_WRITE_TYPE(String code) {
			this(code, true);
		}
		
		DATA_WRITE_TYPE(String code, boolean enabled) {
			this.code = code;
			this.enabled = enabled;
		}
	}
}

interface Reader {
	default AbstractSourceReader getSourceReader(SourceVO source, AbstractJobHandler handler) {
		return null; 
	}
}

interface Writer {
	default AbstractTargetWriter getTargetWriter(TargetVO target) {
		return null; 
	}
}
