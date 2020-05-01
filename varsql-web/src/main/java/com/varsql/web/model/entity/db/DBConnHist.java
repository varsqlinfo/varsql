package com.varsql.web.model.entity.db;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = DBConnHist._TB_NAME)
public class DBConnHist{
	public final static String _TB_NAME="VTDATABASE_CONN_HIST";
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name ="LOG_PK")
	private Long logPk;
	
	@Column(name ="VCONNID")
	private String vconnid;

	@Column(name ="VIEWID")
	private String viewid;

	@Column(name ="CONN_TIME")
	private String connTime;

	@Column(name ="REQ_URL")
	private String reqUrl;

	@Builder
	public DBConnHist(String vconnid, String viewid, String connTime, String reqUrl) {
		this.vconnid = vconnid;
		this.viewid = viewid;
		this.connTime = connTime;
		this.reqUrl = reqUrl;

	}
	
	public final static String VCONNID="vconnid";

	public final static String VIEWID="viewid";

	public final static String CONN_TIME="connTime";

	public final static String REQ_URL="reqUrl";

}