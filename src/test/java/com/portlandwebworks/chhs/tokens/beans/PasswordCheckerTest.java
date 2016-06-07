package com.portlandwebworks.chhs.tokens.beans;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.ws.rs.WebApplicationException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.easymock.EasyMock.*;

/**
 *
 * @author nick
 */
public class PasswordCheckerTest {

	private EntityManager em;
	private PasswordEncoder encoder;
	private PasswordChecker checker;

	public PasswordCheckerTest() {
	}

	@Before
	public void setUp() {
		em = createMock(EntityManager.class);
		encoder = createMock(PasswordEncoder.class);
		checker = new PasswordChecker(encoder);
		checker.setEm(em);
	}

	@Test
	public void valid() {
		TypedQuery<String> pQuery = createMock(TypedQuery.class);
		final String email = "email";
		final String password = "password";
		expect(em.createQuery("SELECT a.hashedPassword FROM Account a WHERE a.email = :email", String.class)).andReturn(pQuery);
		expect(pQuery.setParameter("email", email)).andReturn(pQuery);
		expect(pQuery.getSingleResult()).andReturn(password);
		expect(encoder.matches(password, password)).andReturn(true);

		replay(em, encoder, pQuery);
		boolean match = checker.passwordMatches(email, password);
		verify(em, encoder, pQuery);
		assertTrue(match);
	}

	@Test
	public void invalid() {
		TypedQuery<String> pQuery = createMock(TypedQuery.class);
		final String email = "email";
		final String password = "password";
		expect(em.createQuery("SELECT a.hashedPassword FROM Account a WHERE a.email = :email", String.class)).andReturn(pQuery);
		expect(pQuery.setParameter("email", email)).andReturn(pQuery);
		expect(pQuery.getSingleResult()).andReturn(password);
		expect(encoder.matches("badPassword", password)).andReturn(false);

		replay(em, encoder, pQuery);
		boolean match = checker.passwordMatches(email, "badPassword");
		verify(em, encoder, pQuery);
		assertFalse(match);
	}

	@Test
	public void noUser() {
		TypedQuery<String> pQuery = createMock(TypedQuery.class);
		final String email = "email";
		expect(em.createQuery("SELECT a.hashedPassword FROM Account a WHERE a.email = :email", String.class)).andReturn(pQuery);
		expect(pQuery.setParameter("email", email)).andReturn(pQuery);
		expect(pQuery.getSingleResult()).andThrow(new NoResultException());
		replay(em, pQuery);
		try {
			checker.passwordMatches(email, "password");
			fail();
		} catch (WebApplicationException ex) {
			assertEquals(400, ex.getResponse().getStatus());
		}
		verify(em, pQuery);
	}
}
