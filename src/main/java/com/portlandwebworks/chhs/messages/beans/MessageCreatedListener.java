package com.portlandwebworks.chhs.messages.beans;

import com.portlandwebworks.chhs.accounts.model.Account;
import com.portlandwebworks.chhs.messages.MessageCreatedEvent;
import com.portlandwebworks.chhs.messages.MessageDTO;
import com.portlandwebworks.chhs.messages.model.Message;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author nick
 */
@Component
public class MessageCreatedListener {

	private static final Logger log = LoggerFactory.getLogger(MessageCreatedListener.class);
	private static final String DEFAULT_EMAIL = "alice@caseworker.com";

	@PersistenceContext
	private EntityManager em;

	@Async
	@EventListener
	@Transactional
	public void createResponse(MessageCreatedEvent created) {
		log.debug("Message creation event fired for auto responder.");
		Message msg = em.find(Message.class, created.getMsgId());
		Account defaultCaseWorker = em.createQuery("SELECT a FROM Account a WHERE a.email = :email", Account.class)
				.setParameter("email", DEFAULT_EMAIL)
				.getSingleResult();
		//Never respond to yourself...
		if (!msg.getFromUserId().equals(defaultCaseWorker.getId())) {
			//66% change of generating a response
			Random rnd = new Random();
			int willGenerate = rnd.nextInt(3);
			if (willGenerate >= 1) {
				log.info("Generating random response.");
				Message response = new Message();
				response.setFromUserId(defaultCaseWorker.getId());
				response.setToUserId(msg.getFromUserId());
				response.setSubject("RE: " + msg.getSubject());
				response.setContent(randomResp());
				response.setReplyToId(msg.getId());
				em.persist(response);
			}
		}
	}

	private String randomResp() {
		try {
			RestTemplate tmp = new RestTemplate();
			Map<String, Object> params = new HashMap<>();
			params.put("length", new Random().nextInt(5) + 1);
			String rsp = tmp.getForObject("http://loripsum.net/api/{length}/short/plaintext", String.class, params);
			return rsp;
		} catch (Exception ex) {
			log.warn("Could not generate random response. Returning canned response.");
			return "This is the canned response. Not very much fun....";
		}
	}

}
