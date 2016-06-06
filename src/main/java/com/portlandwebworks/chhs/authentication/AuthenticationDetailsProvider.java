package com.portlandwebworks.chhs.authentication;

import com.portlandwebworks.chhs.accounts.Account;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 *
 * @author nick
 */
@Component
public class AuthenticationDetailsProvider {

	@PersistenceContext
	private EntityManager em;

	public AuthenticationDetails details() {
		final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null && auth.isAuthenticated()) {
			return (AuthenticationDetails) auth.getPrincipal();
		} else {
			return null;
		}
	}

	public Account currentAccount() {
		AuthenticationDetails authDetails = details();
		if (authDetails != null) {
			return em.find(Account.class, authDetails.getAccountId());
		} else {
			return null;
		}
	}

}
