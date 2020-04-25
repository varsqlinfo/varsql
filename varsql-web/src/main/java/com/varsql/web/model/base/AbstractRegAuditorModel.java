package com.varsql.web.model.base;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Getter;
import lombok.Setter;

/**
 * -----------------------------------------------------------------------------
* @fileName		: BaseAuditorModel.java
* @desc		: model audit model
* @author	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
*2020. 4. 20. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
@Setter
@Getter
@MappedSuperclass
@EntityListeners(value = { AuditingEntityListener.class })
public abstract class AbstractRegAuditorModel {

    @CreatedBy
    @Column(name="REG_ID", nullable = false, updatable = false)
    private String regId;

    @CreatedDate
    @Column(name="REG_DT", nullable = false, updatable = false)
    private LocalDateTime regDt;
}