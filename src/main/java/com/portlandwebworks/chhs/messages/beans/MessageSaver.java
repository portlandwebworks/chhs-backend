package com.portlandwebworks.chhs.messages.beans;

import com.portlandwebworks.chhs.messages.MessageCreatedEvent;
import com.portlandwebworks.chhs.authentication.AuthenticationDetails;
import com.portlandwebworks.chhs.authentication.AuthenticationDetailsProvider;
import com.portlandwebworks.chhs.messages.MessageDTO;
import com.portlandwebworks.chhs.messages.model.Message;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 *
 * @author nick
 */
@Component
public class MessageSaver {

	@PersistenceContext
	private EntityManager em;
	private final AuthenticationDetailsProvider detail;
	private final MessageDTOConverter converter;
	private final ApplicationEventPublisher eventPublisher;

	@Autowired
	public MessageSaver(AuthenticationDetailsProvider detail, MessageDTOConverter converter, ApplicationEventPublisher eventPublisher) {
		this.detail = detail;
		this.converter = converter;
		this.eventPublisher = eventPublisher;
	}

	
	public MessageDTO saveMessage(MessageDTO newMsg) {
		AuthenticationDetails authenticated = detail.authenticated();
		Message msg = new Message();
		msg.setFromUserId(authenticated.getAccountId());
		msg.setToUserId(getAccountIdByEmail(newMsg.getToEmail()));
		msg.setSubject(newMsg.getSubject());
		msg.setContent(newMsg.getContent());
		msg.setReplyToId(newMsg.getInReplyToId());
		em.persist(msg);
		eventPublisher.publishEvent(new MessageCreatedEvent(msg));
		return converter.fromMessage(msg);
	}

	private Integer getAccountIdByEmail(String email) {
		return em.createQuery("SELECT a.id FROM Account a WHERE a.email = :email", Integer.class)
				.setParameter("email", email)
				.getSingleResult();
	}

	void setEm(EntityManager em) {
		this.em = em;
	}

}
