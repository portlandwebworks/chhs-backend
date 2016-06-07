package com.portlandwebworks.chhs.accounts.beans;

import com.portlandwebworks.chhs.accounts.beans.PasswordVerifier;
import javax.validation.ValidationException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author nick
 */
public class PasswordVerifierTest {

	private PasswordVerifier verifier;

	public PasswordVerifierTest() {
	}

	@Before
	public void setUp() {
		verifier = new PasswordVerifier();

	}

	@Test
	public void empty() {
		try {
			verifier.valid("", "");
			fail();
		} catch (ValidationException ex) {
			assertEquals(PasswordVerifier.REQUIRED_MSG, ex.getMessage());
		}
	}

	@Test
	public void mismatch() {
		try {
			verifier.valid("test", "");
			fail();
		} catch (ValidationException ex) {
			assertEquals(PasswordVerifier.DO_NOT_MATCH_MSG, ex.getMessage());
		}
	}

	@Test
	public void badForm() {
		try {
			verifier.valid("test", "test");
			fail();
		} catch (ValidationException ex) {
			assertEquals(PasswordVerifier.BAD_FORMAT_MSG, ex.getMessage());
		}
	}

	@Test
	public void noUppercase() {
		try {
			verifier.valid("test123456", "test123456");
			fail();
		} catch (ValidationException ex) {
			assertEquals(PasswordVerifier.BAD_FORMAT_MSG, ex.getMessage());
		}
	}
	
	@Test
	public void noNumbers() {
		try {
			verifier.valid("Testabcdef", "Testabcdef");
			fail();
		} catch (ValidationException ex) {
			assertEquals(PasswordVerifier.BAD_FORMAT_MSG, ex.getMessage());
		}
	}
	
	@Test
	public void ok() {
		assertTrue(verifier.valid("Test123456", "Test123456"));
	}

}
