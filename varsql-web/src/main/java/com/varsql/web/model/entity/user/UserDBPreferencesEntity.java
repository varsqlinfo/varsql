package com.varsql.web.model.entity.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import com.varsql.web.model.base.AabstractAuditorModel;
import com.varsql.web.model.id.UserDBPreferencesID;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@SuppressWarnings("serial")
@Getter
@Setter
@NoArgsConstructor
@Entity
@IdClass(UserDBPreferencesID.class)
@Table(name = UserDBPreferencesEntity._TB_NAME)
public class UserDBPreferencesEntity extends AabstractAuditorModel{
	
	public final static String _TB_NAME= "VTPREFERENCES";
	
	@Id
	private String vconnid;
	
	@Id
	private String viewid;
	
	@Id
	private String prefKey;

	@Column(name ="PREF_VAL")
	private String prefVal;
	
	@Builder
	public UserDBPreferencesEntity(String vconnid, String viewid, String prefKey, String prefVal) {
		this.vconnid  = vconnid; 
		this.viewid  = viewid; 
		this.prefKey  = prefKey; 
		this.prefVal  = prefVal; 
	}
	
	public final static String VCONNID= "vconnid";
	public final static String VIEWID= "viewid";
	public final static String PREF_KEY= "prefKey";
	public final static String PREF_VAL= "prefVal";
}