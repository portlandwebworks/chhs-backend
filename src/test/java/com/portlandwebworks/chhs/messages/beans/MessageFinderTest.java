package com.portlandwebworks.chhs.messages.beans;

import com.portlandwebworks.chhs.authentication.AuthenticationDetails;
import com.portlandwebworks.chhs.authentication.AuthenticationDetailsProvider;
import com.portlandwebworks.chhs.messages.MessageDTO;
import com.portlandwebworks.chhs.messages.model.Message;
import java.util.Arrays;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.easymock.EasyMock.*;

/**
 *
 * @author nick
 */
public class MessageFinderTest {
	
	private EntityManager em;
	private AuthenticationDetailsProvider provider;
	private MessageDTOConverter converter;
	private MessageFinder finder;
	
	public MessageFinderTest() {
	}
	
	@Before
	public void setUp() {
		em = createMock(EntityManager.class);
		provider = createMock(AuthenticationDetailsProvider.class);
		converter = createMock(MessageDTOConverter.class);
		finder = new MessageFinder(provider, converter);
		finder.setEm(em);
	}

	@Test
	public void testGetMessages() {
		AuthenticationDetails details = new AuthenticationDetails(1, "t@t.com", "", null);
		expect(provider.authenticated()).andReturn(details);
		TypedQuery<Message> mQuery = createMock(TypedQuery.class);
		expect(em.createQuery("SELECT m FROM Message m WHERE (m.fromUserId = :fromId OR m.toUserId = :toId) AND m.id NOT IN (SELECT d.messageId FROM DeletedMessage d WHERE d.forUserId = :deletedUserId) ORDER BY m.id", Message.class))
				.andReturn(mQuery);
		expect(mQuery.setParameter("toId", details.getAccountId())).andReturn(mQuery);
		expect(mQuery.setParameter("fromId", details.getAccountId())).andReturn(mQuery);
		expect(mQuery.setParameter("deletedUserId", details.getAccountId())).andReturn(mQuery);
		
		final Message msg = new Message();
		final MessageDTO dto = new MessageDTO();
		List<Message> msgs = Arrays.asList(msg);
		expect(mQuery.getResultList()).andReturn(msgs);
		expect(converter.fromMessage(msg)).andReturn(dto);
		
		replay(em,provider,converter,mQuery);
		List<MessageDTO> results = finder.getMessages();
		verify(em,provider,converter,mQuery);
		assertEquals(1, results.size());
		assertTrue(results.contains(dto));
	}
	
}
