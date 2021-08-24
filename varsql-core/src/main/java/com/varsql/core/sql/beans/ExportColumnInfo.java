package com.varsql.core.sql.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

/**
 *
 * @FileName  : ExportColumnInfo.java
 * @프로그램 설명 : 컬럼 정보를 담기위한 bean.
 * @Date      : 2017. 10. 31.
 * @작성자      : ytkim
 * @변경이력 :
 */
@JacksonXmlRootElement(localName = "column")
public class ExportColumnInfo {
	// column name
	@XmlAttribute
	@JacksonXmlProperty(isAttribute = true)
	private String name;

	// column type
	@XmlAttribute
	@JacksonXmlProperty(isAttribute = true)
	private String type;

	// 숫자 여부.
	@XmlAttribute
	@JacksonXmlProperty(isAttribute = true)
	private boolean number;

	// lob type 여부
	@XmlAttribute
	@JacksonXmlProperty(isAttribute = true)
	private boolean lob;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isNumber() {
		return number;
	}

	public void setNumber(boolean number) {
		this.number = number;
	}

	public boolean isLob() {
		return lob;
	}

	public void setLob(boolean lob) {
		this.lob = lob;
	}

	@Override
	public String toString() {

		return new StringBuffer()
				.append("name : ").append(name)
				.append(" type : ").append(type)
				.append(" number : ").append(number)
				.toString();
	}
}

