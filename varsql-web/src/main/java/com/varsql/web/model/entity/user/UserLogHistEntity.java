package com.varsql.web.model.entity.user;

import java.sql.Timestamp;

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
@Table(name = UserLogHistEntity._TB_NAME)
public class UserLogHistEntity{
	public final static String _TB_NAME="VTUSER_LOG_HIST";

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name ="LOG_PK")
	private Long logPk;

	@Column(name ="VIEWID")
	private String viewid;

	@Column(name ="HIST_TIME")
	private Timestamp histTime;

	@Column(name ="HIST_TYPE", length=6)
	private String histType;

	@Column(name ="USR_IP", length=45)
	private String usrIp;

	@Column(name ="BROWSER", length=250)
	private String browser;

	@Column(name ="DEVICE_TYPE", length=10)
	private String deviceType;

	@Column(name ="PLATFORM", length=10)
	private String platform;

	@Builder
	public UserLogHistEntity(String viewid, Timestamp histTime, String histType, String usrIp, String browser, String deviceType, String platform) {
		this.viewid = viewid;
		this.histTime = histTime;
		this.histType = histType;
		this.usrIp = usrIp;
		this.browser = browser;
		this.deviceType = deviceType;
		this.platform = platform;

	}

	public final static String VIEWID="viewid";

	public final static String HIST_TIME="histTime";

	public final static String HIST_TYPE="histType";

	public final static String USR_IP="usrIp";

	public final static String BROWSER="browser";

	public final static String DEVICE_TYPE="deviceType";

	public final static String PLATFORM="platform";


}