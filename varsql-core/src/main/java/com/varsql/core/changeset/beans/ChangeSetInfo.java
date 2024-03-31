package com.varsql.core.changeset.beans;

import java.util.List;

import org.springframework.core.io.Resource;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ChangeSetInfo {
	private String type; 
	private int version;
	
	private Resource resource;
	
	private List<ChangeSql> applySqls;
	
	private List<ChangeSql> revertSqls;
	
}
