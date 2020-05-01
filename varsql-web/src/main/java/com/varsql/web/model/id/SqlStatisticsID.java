package com.varsql.web.model.id;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;

import com.varsql.web.model.entity.db.DBConnectionEntity;
import com.varsql.web.model.entity.user.UserEntity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode(of= {"vconnid","viewid","startTime"})
public class SqlStatisticsID implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name ="VCONNID")
	private String vconnid;
	
	@Column(name ="VIEWID")
	private String viewid;
	
	@Column(name ="START_TIME")
	private LocalDateTime startTime;

}
