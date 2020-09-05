package com.varsql.web.model.entity.user;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.data.annotation.Immutable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Immutable
@Entity
@Table(name = RegInfoEntity._TB_NAME)
public class RegInfoEntity implements Serializable{
	
	private static final long serialVersionUID = 9083643697661453334L;

	public final static String _TB_NAME= UserEntity._TB_NAME;

	@Id
	@Column(name ="VIEWID")
	@JsonIgnore
	private String viewid;

	@Column(name ="UID")
	private String uid;
	
	@Column(name ="UNAME")
	private String uname;

	@Column(name ="ORG_NM")
	private String orgNm;

	@Column(name ="DEPT_NM")
	private String deptNm;
	
	@Transient
	@JsonProperty
	private String viewName;
	
	
	public final static String VIEWID="viewid";

	public final static String UID="uid";

	public final static String UNAME="uname";

	public final static String ORG_NM="orgNm";

	public final static String DEPT_NM="deptNm";
	
	public String getViewName() {
		return String.format("%s/%s", this.uid , this.uname);
	}
}
