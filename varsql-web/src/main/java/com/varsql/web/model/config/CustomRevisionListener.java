package com.varsql.web.model.config;

import org.hibernate.envers.RevisionListener;

import com.varsql.web.util.SecurityUtil;

public class CustomRevisionListener implements RevisionListener {

	@Override
	public void newRevision(Object revisionEntity) {
		CustomRevisionEntity revision = (CustomRevisionEntity) revisionEntity;
		revision.setUserName(SecurityUtil.loginInfo().getUsername());
	}

}