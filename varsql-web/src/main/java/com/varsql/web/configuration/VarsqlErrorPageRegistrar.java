package com.varsql.web.configuration;

import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.ErrorPageRegistrar;
import org.springframework.boot.web.server.ErrorPageRegistry;
import org.springframework.http.HttpStatus;

/**
 * -----------------------------------------------------------------------------
* @fileName		: VarsqlErrorPageRegistrar.java
* @desc		: error page 설정.
* @author	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
*2020. 4. 21. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
public class VarsqlErrorPageRegistrar implements ErrorPageRegistrar {

	@Override
	public void registerErrorPages(ErrorPageRegistry registry) {
		registry.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND, "/error/error404")
			, new ErrorPage(HttpStatus.FORBIDDEN, "/error/error403")
			, new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/error/error500")
			, new ErrorPage(Throwable.class, "/error/error500")
			, new ErrorPage(NullPointerException.class, "/error/error500")
		);
	}
}