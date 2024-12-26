package com.varsql.core.sql;

import com.vartech.common.utils.StringUtils;

/**
 *
 * @FileName  : SQLTemplateCode.java
 * @프로그램 설명 : SQL template
 * @Date      : 2020. 11. 26.
 * @작성자      : ytkim
 * @변경이력 :
 */
public class SQLTemplateCode {
	
	public enum TABLE implements TemplateEnum {
		select, selectCount, selectPaging, insert, update, delete,
		selectInsert, merge, createSelect;

		private String templateId;
		
		TABLE() {
			templateId = this.getClass().getSimpleName().toLowerCase()+StringUtils.capitalize(this.name());
		}
		
		@Override
		public String getTemplateId() {
			return templateId;
		}
	}
}
