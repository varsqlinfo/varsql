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
	
	public enum TABLE implements SQLTemplateEnum {
		create, rename,
		addComment,	modifyComment,	dropComment,
		select,	insert,	update,	delete,
		selectInsert, insertUpdate, createSelect,
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
	
	public enum VIEW implements SQLTemplateEnum {
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
	
	public enum FUNCTION implements SQLTemplateEnum {
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
	
	public enum PROCEDURE implements SQLTemplateEnum {
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
	
	public enum PACKAGE implements SQLTemplateEnum {
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
	
	public enum TRIGGER implements SQLTemplateEnum {
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
	
	public enum INDEX implements SQLTemplateEnum {
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
	
	public enum SEQUENCE implements SQLTemplateEnum {
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
