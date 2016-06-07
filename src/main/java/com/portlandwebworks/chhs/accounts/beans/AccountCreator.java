package com.portlandwebworks.chhs.accounts.beans;

import com.portlandwebworks.chhs.accounts.model.Account;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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

	@Transactional
	public void createAccount(Account account) {
		log.info("Creating new user account with email of: {}", account.getEmail());
		verifier.valid(account.getNewPassword(), account.getNewPasswordConfirmation());
		log.debug("Hashing password for: {}", account.getEmail());
		account.setHashedPassword(encoder.encode(account.getNewPassword()));
		log.debug("Persisting account: {}", account.getEmail());
		em.persist(account);
	}
	
	public boolean emailAvailable(String email){
		return em.createQuery("SELECT COUNT(a.id) FROM Account a WHERE a.email = :email", Long.class)
				.setParameter("email", email)
				.getSingleResult() == 0;
	}

	void setEm(EntityManager em) {
		this.em = em;
	}
}
