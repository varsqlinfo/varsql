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
@EqualsAndHashCode(of= {"schedName","triggerName","triggerGroup"})
public class TriggersID implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name ="SCHED_NAME")
	private String schedName; 

	@Column(name ="TRIGGER_NAME")
	private String triggerName; 

	@Column(name ="TRIGGER_GROUP")
	private String triggerGroup; 

}
