package com.varsql.core.db.valueobject;

import org.apache.ibatis.type.Alias;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Alias("constraintInfo")
public class ConstraintInfo {
	private int seq;
	private String tableName;
	private String constraintName;
	private String type;
	private String columnName;
	private String ascOrDesc;
	
	// 참조 테이블
	private String referencedTableName;
	// 참보 컬럼
	private String referencedColumnName;
	
	
}
