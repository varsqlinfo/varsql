package com.varsql.core.db.valueobject;

import org.apache.ibatis.type.Alias;

import com.vartech.common.app.beans.ParamMap;

@Alias("paramMap")
public class ResultTypeMap<K, V> extends ParamMap<K, V>{
	private static final long serialVersionUID = 1L;

}
