package com.portlandwebworks.chhs.messages.beans;

import com.portlandwebworks.chhs.authentication.AuthenticationDetails;
import com.portlandwebworks.chhs.authentication.AuthenticationDetailsProvider;
import com.portlandwebworks.chhs.messages.MessageCreatedEvent;
import com.portlandwebworks.chhs.messages.MessageDTO;
import com.portlandwebworks.chhs.messages.model.Message;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import org.easymock.Capture;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.easymock.EasyMock.*;
import org.springframework.context.ApplicationEventPublisher;

/**
 *
 * @author nick
 */
public class MessageSaverTest {

	private EntityManager em;
	private AuthenticationDetailsProvider provider;
	private MessageDTOConverter converter;
	private ApplicationEventPublisher publisher;
	private MessageSaver saver;

	public MessageSaverTest() {
	}

	@Before
	public void setUp() {
		em = createMock(EntityManager.class);
		provider = createMock(AuthenticationDetailsProvider.class);
		converter = createMock(MessageDTOConverter.class);
		publisher = createMock(ApplicationEventPublisher.class);
		saver = new MessageSaver(provider, converter, publisher);
		saver.setEm(em);
	}

	@Test
	public void testSaveMessage() {
		AuthenticationDetails details = new AuthenticationDetails(1, "test", "", null);
		MessageDTO dto = new MessageDTO();
		dto.setContent("content");
		dto.setSubject("subject");
		dto.setToEmail("test-to");

		expect(provider.authenticated()).andReturn(details);

		TypedQuery<Integer> iQuery = createMock(TypedQuery.class);
		expect(em.createQuery("SELECT a.id FROM Account a WHERE a.email = :email", Integer.class)).andReturn(iQuery);
		expect(iQuery.setParameter("email", dto.getToEmail())).andReturn(iQuery);
		expect(iQuery.getSingleResult()).andReturn(2);
		publisher.publishEvent(isA(MessageCreatedEvent.class));
		expectLastCall().once();

		Capture<Message> capMsg = Capture.newInstance();
		em.persist(capture(capMsg));
		expectLastCall().once();
		expect(converter.fromMessage(isA(Message.class))).andReturn(dto);

		replay(em, provider, converter, iQuery);
		saver.saveMessage(dto);
		verify(em, provider, converter, iQuery);

		Message capped = capMsg.getValue();
		assertNotNull(capped);
		assertEquals(details.getAccountId(), capped.getFromUserId());
		assertEquals(new Integer(2), capped.getToUserId());
		assertEquals(dto.getContent(), capped.getContent());
		assertEquals(dto.getSubject(), capped.getSubject());
	}
}
