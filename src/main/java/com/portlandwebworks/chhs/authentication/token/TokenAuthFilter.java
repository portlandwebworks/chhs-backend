package com.portlandwebworks.chhs.authentication.token;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.stereotype.Component;

/**
 *
 * @author nick
 */
@Component
public class TokenAuthFilter extends AbstractPreAuthenticatedProcessingFilter {

	private final Logger log = LoggerFactory.getLogger(TokenAuthFilter.class);

	@Autowired
	public TokenAuthFilter(TokenAuthManager authManager) {
		setAuthenticationManager(authManager);
	}

	@Override
	protected Object getPreAuthenticatedPrincipal(HttpServletRequest hsr) {
		log.trace("Checking for pre-auth cookie value.");
		if (hsr.getHeader(TOKEN_NAME) != null) {
			String token = hsr.getHeader(TOKEN_NAME);
			log.debug("Header for pre-auth found {}.", token);
			return token;
		}
		log.trace("No preauth token foundfound.");
		return null;
	}
	private static final String TOKEN_NAME = "Token";

	@Override
	protected Object getPreAuthenticatedCredentials(HttpServletRequest hsr) {
		return null;
	}

}
