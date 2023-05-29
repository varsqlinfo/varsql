package com.varsql.core.sql.beans;

import javax.xml.bind.annotation.XmlAttribute;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import lombok.Getter;
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
}

