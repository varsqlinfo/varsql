package com.varsql.web.model.entity.db;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import com.varsql.web.model.base.AbstractRegAuditorModel;
import com.varsql.web.model.converter.BooleanToYnConverter;
import com.varsql.web.model.id.DBVconnidNViewIdID;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@IdClass(DBVconnidNViewIdID.class)
@Table(name = DBConnTabEntity._TB_NAME)
public class DBConnTabEntity extends AbstractRegAuditorModel{

	private static final long serialVersionUID = 1L;

	public final static String _TB_NAME="VTDATABASE_CONN_TAB";

	@Id
	@Column(name ="VIEWID")
	private String viewid;

	@Id
	@Column(name ="VCONNID")
	private String vconnid;

	@Column(name ="PREV_VCONNID")
	private String prevVconnid;

	@Column(name ="VIEW_YN")
	@Convert(converter = BooleanToYnConverter.class)
	private boolean viewYn;

	@Builder
	public DBConnTabEntity(String viewid, String vconnid, String prevVconnid, boolean viewYn) {
		this.viewid = viewid;
		this.vconnid = vconnid;
		this.prevVconnid = prevVconnid;
		this.viewYn = viewYn;

	}
	public final static String VIEWID="viewid";

	public final static String VCONNID="vconnid";

	public final static String PREV_VCONNID="prevVconnid";

}