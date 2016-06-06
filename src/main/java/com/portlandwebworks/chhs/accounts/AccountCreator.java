package com.portlandwebworks.chhs.accounts;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ValidationException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 *
 * @author nick
 */
@Component
public class AccountCreator {

	private final static Logger log = LoggerFactory.getLogger(AccountCreator.class);
	@PersistenceContext
	private EntityManager em;
	private final PasswordEncoder encoder;
	private final PasswordVerifier verifier;

	@Autowired
	public AccountCreator(PasswordEncoder encoder, PasswordVerifier verifier) {
		this.encoder = encoder;
		this.verifier = verifier;
	}

	public void createAccount(Account account) {
		log.info("Creating new user account with email of: {}", account.getEmail());
		verifier.valid(account.getNewPassword(), account.getNewPasswordConfirmation());
		log.debug("Hashing password for: {}", account.getEmail());
		account.setHashedPassword(encoder.encode(account.getNewPassword()));
		log.debug("Persisting account: {}", account.getEmail());
		em.persist(account);
	}

	void setEm(EntityManager em) {
		this.em = em;
	}
}
