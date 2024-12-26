package com.varsql.core.sql.beans;

import javax.xml.bind.annotation.XmlAttribute;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.varsql.core.common.constants.ColumnJavaType;
import com.varsql.core.db.valueobject.ColumnInfo;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @FileName  : ExportColumnInfo.java
 * @프로그램 설명 : 컬럼 정보를 담기위한 bean.
 * @Date      : 2017. 10. 31.
 * @작성자      : ytkim
 * @변경이력 :
 */

@Getter
@Setter
@JacksonXmlRootElement(localName = "column")
@NoArgsConstructor
public class ExportColumnInfo {
	// column name
	@XmlAttribute
	@JacksonXmlProperty(isAttribute = true)
	private String name;
	
	@XmlAttribute
	@JacksonXmlProperty(isAttribute = true)
	private String alias;

	// column type
	@XmlAttribute
	@JacksonXmlProperty(isAttribute = true)
	private String type;
	
	// column type code
	@XmlAttribute
	@JacksonXmlProperty(isAttribute = true)
	private int typeCode;

	// 숫자 여부.
	@XmlAttribute
	@JacksonXmlProperty(isAttribute = true)
	private boolean number;

	// lob type 여부
	@XmlAttribute
	@JacksonXmlProperty(isAttribute = true)
	private boolean lob;
	
	@Builder
	public ExportColumnInfo(String name, String alias, String type,
			int typeCode, boolean number, boolean lob) {
		this.name = name; 
		this.alias = alias; 
		this.type = type; 
		this.typeCode = typeCode; 
		this.number = number; 
		this.lob = lob; 
	}

	@Override
	public String toString() {
		return new StringBuffer()
				.append("name : ").append(name)
				.append(" alias : ").append(alias)
				.append(" type : ").append(type)
				.append(" typeCode : ").append(typeCode)
				.append(" number : ").append(number)
				.toString();
	}
	
	public static ExportColumnInfo toColumnInfo(ColumnInfo columnInfo) {
		ColumnJavaType columnJavaType = ColumnJavaType.getType(columnInfo.getTypeCode()); 
		return ExportColumnInfo.builder()
		.name(columnInfo.getName())
		.alias(columnInfo.getName())
		.type(columnInfo.getTypeName())
		.typeCode(columnInfo.getTypeCode())
		.lob(columnJavaType.isLob())
		.number(columnJavaType.isNumber())
		.build();
	}
}

