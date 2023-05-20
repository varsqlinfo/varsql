package com.varsql.core.db.valueobject.ddl;

import java.util.List;

import com.varsql.core.db.valueobject.ColumnInfo;
import com.varsql.core.db.valueobject.CommentInfo;
import com.varsql.core.db.valueobject.ConstraintInfo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DDLTemplateParam {
	private String dbType;
	private String schema;
	private String objectName;
	private DDLCreateOption ddlOpt;
	private String sourceText; 
	private List<ColumnInfo> columnList;
	private List<ConstraintInfo> keyList;
	private List<CommentInfo> commentsList;
	private List<?> items;
	private Object item;
}
