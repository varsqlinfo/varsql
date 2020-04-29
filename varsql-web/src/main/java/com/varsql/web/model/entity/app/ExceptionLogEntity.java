package com.varsql.web.model.entity.app;

import java.util.Arrays;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.varsql.web.model.base.AbstractRegAuditorModel;
import com.varsql.web.model.id.generator.AppUUIDGenerator;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = ExceptionLogEntity._TB_NAME)
public class ExceptionLogEntity extends AbstractRegAuditorModel{
	
	private static final long serialVersionUID = 1L;
	
	public final static String _TB_NAME="VTSQL_EXCEPTIONLOG";

	@Id
	@GenericGenerator(name = "excpIdGenerator"
		, strategy = "com.varsql.web.model.id.generator.AppUUIDGenerator"
		, parameters = @Parameter(
            name = AppUUIDGenerator.PREFIX_PARAMETER,
            value = ""
		)
	)
    @GeneratedValue(generator = "excpIdGenerator")
	@Column(name ="EXCP_ID")
	private String excpId;

	@Column(name ="SERVER_ID")
	private String serverId;

	@Column(name ="EXCP_TYPE")
	private String excpType;

	@Column(name ="EXCP_TITLE")
	private String excpTitle;

	@Column(name ="EXCP_CONT")
	private String excpCont;
	
	@Builder
	public ExceptionLogEntity(String excpId, String serverId, String excpType, String excpTitle, String excpCont) {
		this.excpId = excpId;
		this.serverId = serverId;
		this.excpType = excpType;
		this.excpTitle = excpTitle;
		this.excpCont = excpCont;

	}

	public final static String EXCP_ID="excpId";

	public final static String SERVER_ID="serverId";

	public final static String EXCP_TYPE="excpType";

	public final static String EXCP_TITLE="excpTitle";

	public final static String EXCP_CONT="excpCont";

	public final static String REG_ID="regId";

	public final static String REG_DT="regDt";

}