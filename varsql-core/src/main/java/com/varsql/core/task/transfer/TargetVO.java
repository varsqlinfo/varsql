package com.varsql.core.task.transfer;

import java.util.List;

import com.varsql.core.db.valueobject.ColumnInfo;
import com.varsql.core.task.TaskConstants.DATA_WRITE_TYPE;
import com.varsql.core.task.TaskConstants.TRANSFER_TYPE;
import com.vartech.common.constants.EnumCommon;

import lombok.Getter;
import lombok.Setter;


/**
 * data transfer target vo
 * 
 * @author ytkim
 *
 */
@Getter
@Setter
public class TargetVO {
	private String requid;
	
	private TRANSFER_TYPE transferType;
	
	private int commitCount;
	
	private DATA_WRITE_TYPE writeType;
	
	private String target;
	
	private String info;
	
	private List<ColumnInfo> tableRowKey;
	
	private List writeParameter;
	
	private boolean errorIgnore;
	
	private Object typeInfo;
	
	public void setInsertType(String insertType) {
		this.writeType = EnumCommon.getValueOf(DATA_WRITE_TYPE.class, insertType);
	}
	
	public void setTransferType(String transferType) {
		this.transferType = EnumCommon.getValueOf(TRANSFER_TYPE.class, transferType);
	}

	@SuppressWarnings("unchecked")
	public <T> T getTypeInfo() {
        return (T)this.typeInfo;
    }
	
	public void setErrorIgnore(String errorIgnore) {
		this.errorIgnore = "Y".equalsIgnoreCase(errorIgnore);
	}
	
}
