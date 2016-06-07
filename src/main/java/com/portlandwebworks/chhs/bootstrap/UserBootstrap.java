package com.portlandwebworks.chhs.bootstrap;

import com.portlandwebworks.chhs.accounts.model.Account;
import com.portlandwebworks.chhs.accounts.beans.AccountCreator;
import com.portlandwebworks.chhs.accounts.model.Address;
import com.portlandwebworks.chhs.accounts.beans.PasswordVerifier;
import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ValidationException;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author nick
 */
@Component
public class UserBootstrap {

	private static final Logger log = LoggerFactory.getLogger(UserBootstrap.class);
	@PersistenceContext
	private EntityManager em;

	private final AccountCreator creator;
	private final PasswordVerifier verifier;

	@Autowired
	public UserBootstrap(AccountCreator creator, PasswordVerifier verifier) {
		this.creator = creator;
		this.verifier = verifier;
	}

	@PostConstruct
	public void addCaseWorker() {
		Account defaultAccount = defaultAccount();

		Long existingCount = em.createQuery("SELECT COUNT(a.id) FROM Account a WHERE a.email = :email", Long.class)
				.setParameter("email", defaultAccount.getEmail())
				.getSingleResult();

		if (existingCount == 0) {
			String password = generatePassword();
			log.warn("================================================================");
			log.warn("Creating default demo caseworker account with following password: {}", password);
			log.warn("================================================================");
			defaultAccount.setNewPassword(password);
			defaultAccount.setNewPasswordConfirmation(password);
			creator.createAccount(defaultAccount);
		}
	}

	private String generatePassword() {
		String password = null;
		boolean valid = false;
		while (!valid) {
			try {
				password = RandomStringUtils.random(20, true, true);
				valid = verifier.valid(password, password);
			} catch (ValidationException ex) {
			}
		}
		return password;
	}

	private Account defaultAccount() {
		final Account account = new Account();
		account.setFirstName("Alice");
		account.setLastName("Caseworker");
		account.setEmail("alice@caseworker.com");
		final Address address = new Address();
		address.setCity("Sacremento");
		address.setCountry("USA");
		address.setState("CA");
		address.setPostalCode("95814");
		address.setStreet("10th and L Streets");
		account.setAddress(address);
		return account;
	}

}
