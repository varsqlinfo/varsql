package com.varsql.web.security.rememberme;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.varsql.web.constants.ResourceConfigConstants;
import com.varsql.web.model.entity.user.RememberMeEntity;
import com.varsql.web.security.repository.RememberMeEntityRepository;

@Service
public class RememberMeTokenRepository implements PersistentTokenRepository {

	@Autowired
	private RememberMeEntityRepository rememberMeEntityRepository;

	@Override
	public void createNewToken(PersistentRememberMeToken token) {
		RememberMeEntity me = RememberMeEntity.builder()
				.series(token.getSeries())
				.token(token.getTokenValue())
				.username(token.getUsername())
				.lastUsed(token.getDate())
				.build();

		rememberMeEntityRepository.save(me);
	}

	@Override
	@Transactional(value = ResourceConfigConstants.APP_TRANSMANAGER,rollbackFor=Throwable.class)
	public void updateToken(String series, String tokenValue, Date lastUsed) {
		rememberMeEntityRepository.updateToken(series, tokenValue, lastUsed);
	}

	@Override
	public PersistentRememberMeToken getTokenForSeries(String seriesId) {

		RememberMeEntity rm = rememberMeEntityRepository.findBySeries(seriesId);
		if(rm != null) {
			return new PersistentRememberMeToken(rm.getUsername(), rm.getSeries(), rm.getToken(), rm.getLastUsed());
		} else {
			return null;
		}
	}

	@Override
	@Transactional(value = ResourceConfigConstants.APP_TRANSMANAGER,rollbackFor=Throwable.class)
	public void removeUserTokens(String username) {
		rememberMeEntityRepository.deleteByUsername(username);
	}
}