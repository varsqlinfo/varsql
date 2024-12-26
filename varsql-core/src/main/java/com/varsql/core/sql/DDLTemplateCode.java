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
public class DDLTemplateCode {
	
	public enum TABLE implements TemplateEnum {
		create, createConversion, rename,
		addComment,	modifyComment,	dropComment,
		columnAdd ,columnRename ,columnModify ,columnDrop ,columnAddComment ,columnModifyComment ,columnDropComment, constraintKey;

		private String templateId; 
		
		TABLE() {
			templateId = this.getClass().getSimpleName().toLowerCase()+StringUtils.capitalize(this.name());
		}
		
		@Override
		public String getTemplateId() {
			return templateId;
		}
	}
	
	public enum VIEW implements TemplateEnum {
		create, rename,	addComment,	modifyComment,	dropComment;

		private String templateId; 
		
		VIEW() {
			templateId = this.getClass().getSimpleName().toLowerCase()+StringUtils.capitalize(this.name());
		}
		@Override
		public String getTemplateId() {
			return templateId;
		}
	}
	
	public enum FUNCTION implements TemplateEnum {
		create, rename,	addComment,	modifyComment,	dropComment;

		private String templateId; 
		
		FUNCTION() {
			templateId = this.getClass().getSimpleName().toLowerCase()+StringUtils.capitalize(this.name());
		}
		@Override
		public String getTemplateId() {
			return templateId;
		}
	}
	
	public enum PROCEDURE implements TemplateEnum {
		create, rename,	addComment,	modifyComment,	dropComment;

		private String templateId; 
		
		PROCEDURE() {
			templateId = this.getClass().getSimpleName().toLowerCase()+StringUtils.capitalize(this.name());
		}
		@Override
		public String getTemplateId() {
			return templateId;
		}
	}
	
	public enum PACKAGE implements TemplateEnum {
		create, rename,	addComment,	modifyComment,	dropComment;

		private String templateId; 
		
		PACKAGE() {
			templateId = this.getClass().getSimpleName().toLowerCase()+StringUtils.capitalize(this.name());
		}
		@Override
		public String getTemplateId() {
			return templateId;
		}
	}
	
	public enum TRIGGER implements TemplateEnum {
		create, rename,	addComment,	modifyComment,	dropComment;

		private String templateId; 
		
		TRIGGER() {
			templateId = this.getClass().getSimpleName().toLowerCase()+StringUtils.capitalize(this.name());
		}
		@Override
		public String getTemplateId() {
			return templateId;
		}
	}
	
	public enum INDEX implements TemplateEnum {
		create, rename,	addComment,	modifyComment,	dropComment;

		private String templateId; 
		
		INDEX() {
			templateId = this.getClass().getSimpleName().toLowerCase()+StringUtils.capitalize(this.name());
		}
		@Override
		public String getTemplateId() {
			return templateId;
		}
	}
	
	public enum SEQUENCE implements TemplateEnum {
		create, rename,	addComment,	modifyComment,	dropComment;

		private String templateId; 
		
		SEQUENCE() {
			templateId = this.getClass().getSimpleName().toLowerCase()+StringUtils.capitalize(this.name());
		}
		@Override
		public String getTemplateId() {
			return templateId;
		}
	}
}
