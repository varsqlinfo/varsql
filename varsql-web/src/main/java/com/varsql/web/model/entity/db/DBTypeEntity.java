package com.varsql.web.model.entity.db;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = DBTypeEntity._TB_NAME)
public class DBTypeEntity implements Serializable{
	public final static String _TB_NAME="VTDBTYPE";

	@Id
	@Column(name ="TYPEID")
	private String typeid;

	@Column(name ="NAME")
	private String name;

	@Column(name ="LANGKEY")
	private String langkey;

	@Column(name ="URLPREFIX")
	private String urlprefix;


	@Builder
	public DBTypeEntity(String typeid, String name, String langkey, String urlprefix) {
		this.typeid = typeid;
		this.name = name;
		this.langkey = langkey;
		this.urlprefix = urlprefix;

	}

	public final static String TYPEID="typeid";

	public final static String NAME="name";

	public final static String LANGKEY="langkey";

	public final static String URLPREFIX="urlprefix";
}