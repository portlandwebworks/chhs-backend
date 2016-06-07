package com.portlandwebworks.chhs.messages.beans;

import com.portlandwebworks.chhs.accounts.model.Account;
import com.portlandwebworks.chhs.authentication.AuthenticationDetails;
import com.portlandwebworks.chhs.authentication.AuthenticationDetailsProvider;
import com.portlandwebworks.chhs.messages.MessageDTO;
import com.portlandwebworks.chhs.messages.model.Message;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author nick
 */
@Component
public class MessageFinder {

	@PersistenceContext
	private EntityManager em;
	private final AuthenticationDetailsProvider authProvider;
	private final MessageDTOConverter converter;

	@Autowired
	public MessageFinder(AuthenticationDetailsProvider authProvider, MessageDTOConverter converter) {
		this.authProvider = authProvider;
		this.converter = converter;
	}

	public List<MessageDTO> getMessages() {
		AuthenticationDetails authenticated = authProvider.authenticated();
		return em.createQuery("SELECT m FROM Message m WHERE (m.fromUserId = :fromId OR m.toUserId = :toId) AND m.deleted = false ORDER BY m.id", Message.class)
				.setParameter("fromId", authenticated.getAccountId())
				.setParameter("toId", authenticated.getAccountId())
				.getResultList().stream()
				.map(converter::fromMessage)
				.collect(Collectors.toList());
	}

	void setEm(EntityManager em) {
		this.em = em;
	}

}
