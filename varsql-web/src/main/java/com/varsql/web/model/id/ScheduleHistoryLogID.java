package com.varsql.web.model.id;

import java.io.Serializable;

import javax.persistence.Column;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode(of= {"histSeq","logType"})
public class ScheduleHistoryLogID implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name ="HIST_SEQ")
	private long histSeq; 

	@Column(name ="LOG_TYPE")
	private String logType; 
	
	

}
