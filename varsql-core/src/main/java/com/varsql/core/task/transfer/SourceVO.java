package com.varsql.core.task.transfer;

import java.util.ArrayList;
import java.util.List;

import com.varsql.core.common.beans.ProgressInfo;
import com.varsql.core.db.valueobject.ColumnInfo;
import com.varsql.core.task.TaskConstants;
import com.varsql.core.task.TaskConstants.DB_READ_TYPE;
import com.varsql.core.task.TaskConstants.SOURCE_TYPE;
import com.vartech.common.constants.EnumCommon;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * data transfer source vo
 *  
 * @author ytkim
 *
 */
@Getter
@Setter
@ToString
public class SourceVO {
	private SOURCE_TYPE sourceType;
	
	private DB_READ_TYPE readType;
	
	private String source;
	
	private int pageSize;
	
	private ArrayList readParameter;
	
	private List<ColumnInfo> sortColumns;
	
	private Object typeInfo;
	
	private ProgressInfo progressInfo;
	
	public void setSourceType(String sourceType) {
		this.sourceType = EnumCommon.getValueOf(SOURCE_TYPE.class, sourceType);
	}
	
	public void setReadType(String readType) {
		this.readType = EnumCommon.getValueOf(DB_READ_TYPE.class, readType);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getTypeInfo() {
        return (T)this.typeInfo;
    }
	
	public void setPageSize(String pageSize) {
		try {
			this.pageSize = Integer.parseInt(pageSize);
		}catch(NumberFormatException e) {
			this.pageSize = TaskConstants.READ_PAGE_SIZE;
		}
	}
}
