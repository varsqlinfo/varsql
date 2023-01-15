package com.varsql.web.model.entity.scheduler;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;

import com.varsql.web.model.id.ScheduleHistoryLogID;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@DynamicUpdate
@NoArgsConstructor
@Entity
@Table(name = ScheduleHistoryLogEntity._TB_NAME)
@IdClass(ScheduleHistoryLogID.class)
public class ScheduleHistoryLogEntity{

	public final static String _TB_NAME="VTQTZ_HISTORY_LOG";

	@Id
	private long histSeq; 

	@Id
	private String logType; 

	@Column(name ="LOG")
	private String log;

	@Builder
	public ScheduleHistoryLogEntity (long histSeq, String logType, String log) {
		this.histSeq = histSeq;
		this.logType = logType;
		this.log = log;
	}

	public final static String HIST_SEQ="histSeq";
	public final static String LOG_TYPE="logType";
	public final static String LOG="log";
}