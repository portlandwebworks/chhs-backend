package com.portlandwebworks.chhs.accounts.beans;

import com.portlandwebworks.chhs.accounts.beans.AccountUpdater;
import com.portlandwebworks.chhs.accounts.model.Address;
import com.portlandwebworks.chhs.accounts.model.Account;
import com.portlandwebworks.chhs.authentication.AuthenticationDetails;
import com.portlandwebworks.chhs.authentication.AuthenticationDetailsProvider;
import javax.persistence.EntityManager;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.easymock.EasyMock.*;
/**
 *
 * @author nick
 */
public class AccountUpdaterTest {

	private EntityManager em;
	private AuthenticationDetailsProvider provider;
	private AccountUpdater updater;
	
	public AccountUpdaterTest() {
	}
	
	@Before
	public void setUp() {
		em = createMock(EntityManager.class);
		provider = createMock(AuthenticationDetailsProvider.class);
		updater = new AccountUpdater(provider);
		updater.setEm(em);
	}

	@Test
	public void testUpdateAccount() {
		AuthenticationDetails details = new AuthenticationDetails(1, "test@test.com", "", null);
		Account existing = new Account();
		existing.setId(1);
		existing.setEmail("test@test.com");
		existing.setCreatedBy("Test");
		existing.setAddress(new Address());
		
		expect(provider.authenticated()).andReturn(details);
		expect(em.find(Account.class, details.getAccountId())).andReturn(existing);
		
		Account update = new Account();
		update.setId(2);
		update.setEmail("NOTSET");
		update.setFirstName("NewName");
		update.setLastName("NewLastName");
		final Address newAddress = new Address();
		newAddress.setCity("NewCity");
		newAddress.setState("NewState");
		newAddress.setPostalCode("NewCode");
		newAddress.setCountry("NewCountry");
		update.setAddress(newAddress);
		
		replay(em,provider);
		updater.updateAccount(update);
		verify(em,provider);
		
		assertEquals(update.getFirstName(), existing.getFirstName());
		assertEquals(update.getLastName(), existing.getLastName());
		assertEquals("Test", existing.getCreatedBy());
		
		final Address existingAddress = existing.getAddress();
		assertEquals(newAddress.getCity(), existingAddress.getCity());
		assertEquals(newAddress.getState(), existingAddress.getState());
		assertEquals(newAddress.getStreet(), existingAddress.getStreet());
		assertEquals(newAddress.getCountry(), existingAddress.getCountry());
		assertEquals(newAddress.getPostalCode(), existingAddress.getPostalCode());
	}
}
