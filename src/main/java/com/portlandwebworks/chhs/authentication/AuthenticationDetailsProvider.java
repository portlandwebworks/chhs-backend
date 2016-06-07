package com.portlandwebworks.chhs.authentication;

import com.portlandwebworks.chhs.accounts.model.Account;
import java.util.Arrays;
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

	public AuthenticationDetails authenticated() {
		final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null && auth.isAuthenticated() && auth.getPrincipal() instanceof AuthenticationDetails) {
			return (AuthenticationDetails) auth.getPrincipal();
		} else {
			return null;
		}
	}

	public Account currentAccount() {
		AuthenticationDetails authDetails = authenticated();
		if (authDetails != null) {
			return em.find(Account.class, authDetails.getAccountId());
		} else {
			return null;
		}
	}

	public AuthenticationDetails forToken(String token) {
		Account a = em.createQuery("SELECT a FROM Account a WHERE a.id = (SELECT t.accountId FROM Token t WHERE t.token = :token)", Account.class)
				.setParameter("token", token)
				.getSingleResult();
		return new AuthenticationDetails(a.getId(), a.getEmail(), token, Arrays.asList("ROLE_USER"));
	}

}
