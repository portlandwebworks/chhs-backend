package com.portlandwebworks.chhs.tokens.beans;

import com.portlandwebworks.chhs.tokens.model.Token;
import java.util.Date;
import java.util.UUID;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Component;

/**
 *
 * @author nick
 */
@Component
public class TokenGenerator {

	@PersistenceContext
	private EntityManager em;
	
	public Token generateTokenFor(String email){
		Integer userId = em.createQuery("SELECT a.id FROM Account a WHERE a.email = :email", Integer.class)
				.setParameter("email", email)
				.getSingleResult();
		
		em.createQuery("DELETE FROM Token t WHERE t.accountId = :accountId")
				.setParameter("accountId", userId)
				.executeUpdate();
		Token t = new Token();
		t.setAccountId(userId);
		t.setToken(UUID.randomUUID().toString());
		t.setExpiresAt(new Date());
		em.persist(t);
		return t;
	}
	
}
