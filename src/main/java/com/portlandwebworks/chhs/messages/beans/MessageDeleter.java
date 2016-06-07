package com.portlandwebworks.chhs.messages.beans;

import com.portlandwebworks.chhs.authentication.AuthenticationDetails;
import com.portlandwebworks.chhs.authentication.AuthenticationDetailsProvider;
import com.portlandwebworks.chhs.messages.model.DeletedMessage;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author nick
 */
@Component
public class MessageDeleter {
	
	@PersistenceContext
	private EntityManager em;
	private final AuthenticationDetailsProvider detailsProvider;

	@Autowired
	public MessageDeleter(AuthenticationDetailsProvider detailsProvider) {
		this.detailsProvider = detailsProvider;
	}

	public void deleteMessage(Integer messageId){
		AuthenticationDetails authenticated = detailsProvider.authenticated();
		DeletedMessage del = new DeletedMessage();
		del.setForUserId(authenticated.getAccountId());
		del.setMessageId(messageId);
		em.persist(del);
	}
	
}
