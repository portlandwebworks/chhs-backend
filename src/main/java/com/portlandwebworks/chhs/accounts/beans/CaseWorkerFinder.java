package com.portlandwebworks.chhs.accounts.beans;

import com.portlandwebworks.chhs.accounts.model.Account;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Component;

/**
 * Looks up the caseworker for the current authenticated account.
 * 
 * @author nick
 */
@Component
public class CaseWorkerFinder {
	
	@PersistenceContext
	private EntityManager em;
	
	/**
	 * Dummy method right now, always looks up the same, static, case worker. 
	 * Normally this would use the AuthenticationDetailsProvider class to get the
	 * user and perform a lookup based of the settings for the account.
	 * 
	 * @return Default case worker account created during bootstrap process.
	 */
	public Account findCaseWorker(){
		return em.createQuery("SELECT a FROM Account a WHERE a.email = :email", Account.class)
				.setParameter("email", "alice@caseworker.com")
				.getSingleResult();
	}
}
