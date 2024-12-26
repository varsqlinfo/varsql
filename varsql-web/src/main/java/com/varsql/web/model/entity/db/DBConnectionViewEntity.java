package com.varsql.web.model.entity.db;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.envers.NotAudited;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.varsql.web.model.base.AbstractAuditorModel;
import com.varsql.web.model.converter.BooleanToDelYnConverter;
import com.varsql.web.model.entity.scheduler.JobEntity;
import com.varsql.web.model.entity.task.TaskSqlEntity;
import com.varsql.web.model.entity.task.TaskTransferEntity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@SuppressWarnings("serial")
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = DBConnectionEntity._TB_NAME)
public class DBConnectionViewEntity extends AbstractAuditorModel{

	public final static String _TB_NAME=DBConnectionEntity._TB_NAME;

	@Id
	@Column(name ="VCONNID")
	private String vconnid;

	@Column(name ="VNAME")
	private String vname;

	@Column(name ="VDBSCHEMA")
	private String vdbschema;

	@Column(name ="VURL")
	private String vurl;
	
	@Column(name ="USE_YN")
	private String useYn;

	@Column(name ="DEL_YN")
	@Convert(converter=BooleanToDelYnConverter.class)
	private boolean delYn;

	@NotAudited
	@JsonIgnore
	@JsonManagedReference
	@OneToMany(fetch=FetchType.LAZY, mappedBy = "jobDBConnection")
    private Set<JobEntity> jobEntity;
	
	@NotAudited
	@JsonIgnore
	@JsonManagedReference
	@OneToMany(fetch=FetchType.LAZY, mappedBy = "taskDBConnection")
    private Set<TaskSqlEntity> taskSqlEntity;
	
	@NotAudited
	@JsonIgnore
	@JsonManagedReference
	@OneToMany(fetch=FetchType.LAZY, mappedBy = "sourceDBConnection")
	private Set<TaskTransferEntity> sourceTaskTransferEntity;
	
	@NotAudited
	@JsonIgnore
	@JsonManagedReference
	@OneToMany(fetch=FetchType.LAZY, mappedBy = "targetDBConnection")
	private Set<TaskTransferEntity> targetTaskTransferEntity;
	
	@Builder
	public DBConnectionViewEntity(String vconnid, String vname, String vdbschema, String vurl) {
		this.vconnid = vconnid;
		this.vname = vname;
		this.vdbschema = vdbschema;
		this.vurl = vurl;
	}

	public final static String VCONNID="vconnid";

	public final static String VNAME="vname";

	public final static String VDBSCHEMA="vdbschema";

	public final static String VURL="vurl";
	
}
