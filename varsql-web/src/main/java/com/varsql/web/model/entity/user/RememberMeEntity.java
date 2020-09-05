package com.varsql.web.model.entity.user;

import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = RememberMeEntity._TB_NAME)
public class RememberMeEntity {
	public final static String _TB_NAME= "VTUSER_REMEMBERME";

    @Id
    @Column(name = "SERIES")
    private String series;

    @Column(name = "USERNAME", nullable = false)
    private String username;

    @Column(name = "TOKEN",nullable = false)
    private String token;

    @Column(name = "LAST_USED")
    private Date lastUsed;

    @Builder
    public RememberMeEntity(String series, String username, String token, Date lastUsed) {
    	this.series = series;
    	this.username = username;
    	this.token = token;
    	this.lastUsed = lastUsed;
    }
}