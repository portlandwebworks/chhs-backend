package com.portlandwebworks.chhs.messages.beans;

import com.portlandwebworks.chhs.accounts.model.Account;
import com.portlandwebworks.chhs.messages.MessageDTO;
import com.portlandwebworks.chhs.messages.model.Message;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Component;

/**
 *
 * @author nick
 */
@Component
public class MessageDTOConverter {

	@PersistenceContext
	private EntityManager em;

	public MessageDTO fromMessage(Message msg) {
		final MessageDTO dto = new MessageDTO();
		dto.setId(msg.getId());
		dto.setSubject(msg.getSubject());
		dto.setContent(msg.getContent());
		dto.setInReplyToId(msg.getReplyToId());
		dto.setCreatedAt(msg.getCreatedAt());
		dto.setFromEmail(emailForId(msg.getFromUserId()));
		dto.setFromName(nameForId(msg.getFromUserId()));
		dto.setToEmail(emailForId(msg.getToUserId()));
		dto.setToName(emailForId(msg.getToUserId()));
		return dto;
	}

	/**
	 * This and emailForId are not super efficient. Should really cache these
	 * results to not do constant lookups of the same data.
	 *
	 * @param id
	 * @return firstName + ' ' + lastName
	 */
	private String nameForId(Integer id) {
		return em.createQuery("SELECT CONCAT(a.firstName, CONCAT(' ', a.lastName)) FROM Account a WHERE a.id = :id", String.class)
				.setParameter("id", id)
				.getSingleResult();
	}

	private String emailForId(Integer id) {
		return em.createNamedQuery(Account.Q_EMAIL_BY_ID, String.class)
				.setParameter("id", id)
				.getSingleResult();
	}
}
