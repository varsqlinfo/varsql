package com.varsql.web.model.entity.user;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;

import com.varsql.web.model.base.AbstractRegAuditorModel;
import com.varsql.web.model.id.EmailTokenID;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@DynamicUpdate
@NoArgsConstructor
@Entity
@IdClass(EmailTokenID.class)
@Table(name = EmailTokenEntity._TB_NAME)
public class EmailTokenEntity extends AbstractRegAuditorModel{
	private static final long serialVersionUID = 1L;

	public final static String _TB_NAME="VTEMAIL_TOKEN";
	
	@Id
	private String token; 

	@Id
	private String viewid; 

	@Column(name ="TOKEN_TYPE")
	private String tokenType; 

	@Builder
	public EmailTokenEntity (String token, String viewid, String tokenType) {
		this.token = token;
		this.viewid = viewid;
		this.tokenType = tokenType;
	}

	public final static String TOKEN="token";
	public final static String VIEWID="viewid";
	public final static String TOKEN_TYPE="tokenType";
}