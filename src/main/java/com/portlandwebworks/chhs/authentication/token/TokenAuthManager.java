/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.portlandwebworks.chhs.authentication.token;

import com.portlandwebworks.chhs.accounts.model.Account;
import com.portlandwebworks.chhs.authentication.AuthenticationDetails;
import com.portlandwebworks.chhs.tokens.model.Token;
import java.util.Arrays;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

/**
 *
 * @author nick
 */
@Component
public class TokenAuthManager implements AuthenticationManager {

	private Logger log = LoggerFactory.getLogger(TokenAuthManager.class);

	@PersistenceContext
	private EntityManager em;

	@Override
	public Authentication authenticate(Authentication a) throws AuthenticationException {
		try {
			log.debug("Trying to determine if token {} is valid.", a.getPrincipal());
			Token t = em.createNamedQuery(Token.Q_FIND_BY_TOKEN, Token.class)
					.setParameter("token", a.getPrincipal())
					.getSingleResult();
			
			//TODO: Check if token expired
			log.trace("Found token, determing if still valid.");
			
			log.debug("Token valid, returning user info.");
			String email = em.createNamedQuery(Account.Q_EMAIL_BY_ID, String.class)
					.setParameter("id", t.getAccountId())
					.getSingleResult();
			//TODO: Verify user account still valid and active.

			//TODO: Load roles
			AuthenticationDetails details = new AuthenticationDetails(t.getAccountId(), email, t.getToken(), Arrays.asList("ROLES_USER"));
			return new UsernamePasswordAuthenticationToken(details, null, details.getAuthorities());
		} catch (NoResultException ex) {
			throw new TokenNotFoundException("Invalid token given.");
		}
	}

	void setEm(EntityManager em) {
		this.em = em;
	}
}
