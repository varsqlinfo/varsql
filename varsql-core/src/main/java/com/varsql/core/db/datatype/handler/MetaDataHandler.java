package com.varsql.core.db.datatype.handler;

public interface MetaDataHandler {
	default int getColumnLength(int length){
		return length;
	}
}

