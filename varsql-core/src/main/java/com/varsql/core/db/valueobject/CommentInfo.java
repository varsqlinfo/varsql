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
@Alias("commentInfo")
public class CommentInfo {
	// Comment Type
	private String type;
	
	// 스키마
	private String schema;
	
	// 코멘트 컬럼 or table명 등등
	private String name;
	
	// 코멘트 내용
	private String comment;
	
	// 코멘트 생성할 item 정보
	private Object item;
}
