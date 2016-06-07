package com.portlandwebworks.chhs.accounts.beans;

import com.portlandwebworks.chhs.accounts.beans.AccountCreator;
import com.portlandwebworks.chhs.accounts.beans.PasswordVerifier;
import com.portlandwebworks.chhs.accounts.model.Account;
import javax.persistence.EntityManager;
import javax.validation.ValidationException;
import javax.ws.rs.WebApplicationException;
import org.junit.Before;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.junit.Assert.*;
import static org.easymock.EasyMock.*;
import org.junit.Test;

/**
 *
 * @author nick
 */
public class AccountCreatorTest {

	private EntityManager em;
	private PasswordEncoder encoder;
	private PasswordVerifier verifier;
	private AccountCreator creator;

	public AccountCreatorTest() {
	}

	@Before
	public void setUp() {
		em = createMock(EntityManager.class);
		encoder = createMock(PasswordEncoder.class);
		verifier = createMock(PasswordVerifier.class); 
		creator = new AccountCreator(encoder, verifier);
		creator.setEm(em);
	}


	@Test
	public void valid() {
		Account acc = new Account();
		acc.setNewPassword("newPassword");
		acc.setNewPasswordConfirmation("newPassword");
		expect(verifier.valid(acc.getNewPassword(), acc.getNewPasswordConfirmation())).andReturn(true);
		expect(encoder.encode("newPassword")).andReturn("hashed");
		em.persist(acc);
		expectLastCall().once();

		replay(em, encoder, verifier);
		creator.createAccount(acc);
		verify(em, encoder, verifier);
		assertEquals("hashed", acc.getHashedPassword());
	}

}
