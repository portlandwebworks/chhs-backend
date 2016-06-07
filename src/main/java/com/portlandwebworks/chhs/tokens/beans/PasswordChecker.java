package com.portlandwebworks.chhs.tokens.beans;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 *
 * @author nick
 */
@Component
public class PasswordChecker {

	private final static Logger log = LoggerFactory.getLogger(PasswordChecker.class);

	@PersistenceContext
	private EntityManager em;
	private final PasswordEncoder encoder;

	@Autowired
	public PasswordChecker(PasswordEncoder encoder) {
		this.encoder = encoder;
	}

	public boolean passwordMatches(String email, String password) {
		try {
			String hashedPassword = em.createQuery("SELECT a.hashedPassword FROM Account a WHERE a.email = :email", String.class)
					.setParameter("email", email)
					.getSingleResult();
			if (encoder.matches(password, hashedPassword)) {
				return true;
			} else {
				return false;
			}
		} catch (NoResultException ex) {
			log.info("Could not find valid user for account.");
			throw new WebApplicationException(Response.Status.BAD_REQUEST);
		}
	}

	void setEm(EntityManager em) {
		this.em = em;
	}
}
