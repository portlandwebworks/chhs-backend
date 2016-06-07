/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.portlandwebworks.chhs.authentication.token;

import com.portlandwebworks.chhs.authentication.token.TokenAuthFilter;
import javax.servlet.http.HttpServletRequest;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.easymock.EasyMock.*;

/**
 *
 * @author nick
 */
public class TokenAuthFilterTest {
	
	private TokenAuthFilter instance;
	public TokenAuthFilterTest() {
	}
	
	@Before
	public void setUp() {
		instance = new TokenAuthFilter(null);
	}

	@Test
	public void hasHeader() {
		HttpServletRequest hsr = EasyMock.createMock(HttpServletRequest.class);
		expect(hsr.getHeader("Token")).andReturn("TokenValue").times(2);
		replay(hsr);
		Object result = instance.getPreAuthenticatedPrincipal(hsr);
		verify(hsr);
		assertEquals("TokenValue", result);
	}
	
	@Test
	public void noHeader() {
		HttpServletRequest hsr = EasyMock.createMock(HttpServletRequest.class);
		expect(hsr.getHeader("Token")).andReturn(null).times(1);
		replay(hsr);
		Object result = instance.getPreAuthenticatedPrincipal(hsr);
		verify(hsr);
		assertNull(result);
	}
}
