package com.varsql.core.db.datatype.handler;

public interface MetaDataHandler {
	default long getColumnLength(long length){
		return length;
	}
}

