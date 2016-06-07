package com.portlandwebworks.chhs.accounts.beans;

import com.portlandwebworks.chhs.accounts.model.Account;
import com.portlandwebworks.chhs.authentication.AuthenticationDetails;
import com.portlandwebworks.chhs.authentication.AuthenticationDetailsProvider;
import java.util.Arrays;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author nick
 */
@Component
public class AccountUpdater {
	
	private static final Logger log = LoggerFactory.getLogger(AccountUpdater.class);
	private static final List<String> ACCOUNT_UPDATABLE_PROPS = Arrays.asList("firstName", "lastName");
	private static final List<String> ADDRESS_UPDATABLE_PROPS = Arrays.asList("city", "state", "street", "country", "postalCode");
	
	@PersistenceContext
	private EntityManager em;
	
	private final AuthenticationDetailsProvider authProvider;

	@Autowired
	public AccountUpdater(AuthenticationDetailsProvider authProvider) {
		this.authProvider = authProvider;
	}

	@Transactional
	public void updateAccount(Account newDetails){
		AuthenticationDetails authenticated = authProvider.authenticated();
		Account existing = em.find(Account.class, authenticated.getAccountId());
		log.info("Updating account {}", existing.getId());
		BeanWrapper accountTarget = PropertyAccessorFactory.forBeanPropertyAccess(existing);
		BeanWrapper accountSource = PropertyAccessorFactory.forBeanPropertyAccess(newDetails);
		ACCOUNT_UPDATABLE_PROPS.stream().forEach(prop -> accountTarget.setPropertyValue(prop, accountSource.getPropertyValue(prop)));
		
		BeanWrapper addressTarget = PropertyAccessorFactory.forBeanPropertyAccess(existing.getAddress());
		BeanWrapper addressSource = PropertyAccessorFactory.forBeanPropertyAccess(newDetails.getAddress());
		ADDRESS_UPDATABLE_PROPS.stream().forEach(prop -> addressTarget.setPropertyValue(prop, addressSource.getPropertyValue(prop)));
	}

	void setEm(EntityManager em) {
		this.em = em;
	}
	
}
