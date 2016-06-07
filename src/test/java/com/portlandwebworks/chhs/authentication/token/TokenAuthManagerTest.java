package com.portlandwebworks.chhs.authentication.token;

import com.portlandwebworks.chhs.authentication.token.TokenNotFoundException;
import com.portlandwebworks.chhs.authentication.token.TokenAuthManager;
import com.portlandwebworks.chhs.accounts.model.Account;
import com.portlandwebworks.chhs.authentication.AuthenticationDetails;
import com.portlandwebworks.chhs.tokens.model.Token;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.core.Authentication;
import static org.junit.Assert.*;
import static org.easymock.EasyMock.*;

/**
 *
 * @author nick
 */
public class TokenAuthManagerTest {

	private EntityManager em;
	private TokenAuthManager authManager;

	public TokenAuthManagerTest() {
	}

	@Before
	public void setUp() {
		em = createMock(EntityManager.class);
		authManager = new TokenAuthManager();
		authManager.setEm(em);
	}

	@Test
	public void noToken() {
		Authentication a = createMock(Authentication.class);
		expect(a.getPrincipal()).andReturn("TokenValue").times(2);

		TypedQuery<Token> tQuery = createMock(TypedQuery.class);
		expect(em.createNamedQuery(Token.Q_FIND_BY_TOKEN, Token.class)).andReturn(tQuery);
		expect(tQuery.setParameter("token", "TokenValue")).andReturn(tQuery);
		expect(tQuery.getSingleResult()).andThrow(new NoResultException());

		replay(em, tQuery, a);
		try {
			authManager.authenticate(a);
			fail();
		} catch (TokenNotFoundException ex) {
		}

	}

	@Test
	public void validToken() {
		Token token = new Token();
		token.setAccountId(1);
		Authentication a = createMock(Authentication.class);
		expect(a.getPrincipal()).andReturn("TokenValue").times(2);

		TypedQuery<Token> tQuery = createMock(TypedQuery.class);
		expect(em.createNamedQuery(Token.Q_FIND_BY_TOKEN, Token.class)).andReturn(tQuery);
		expect(tQuery.setParameter("token", "TokenValue")).andReturn(tQuery);
		expect(tQuery.getSingleResult()).andReturn(token);

		TypedQuery<String> eQuery = createMock(TypedQuery.class);
		expect(em.createNamedQuery(Account.Q_EMAIL_BY_ID, String.class)).andReturn(eQuery);
		expect(eQuery.setParameter("id", token.getAccountId())).andReturn(eQuery);
		expect(eQuery.getSingleResult()).andReturn("test@email.com");

		replay(em, a, tQuery, eQuery);
		Authentication result = authManager.authenticate(a);
		verify(em, a, tQuery, eQuery);
		assertTrue(result.getPrincipal() instanceof AuthenticationDetails);
		AuthenticationDetails details = (AuthenticationDetails) result.getPrincipal();
		assertEquals("test@email.com", details.getUsername());
		assertEquals(token.getAccountId(), details.getAccountId());
		assertEquals(1, details.getAuthorities().size());
	}

}
