/**
 * Copyright (c) 2012, Jon Boley
 * 432 NE Ravenna Blvd
 * Seattle, WA 98115
 */
package com.airsltd.core;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.airsltd.core.parse.AbstractAirsStatusMessage;

/**
 * @author Jon
 *
 */
public class AbstractAirsStatusMessageTest {

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link com.airsltd.core.parse.AbstractAirsStatusMessage#parseString(java.lang.String, java.lang.Throwable)}.
	 */
	@Test
	public final void testParseString() {
		// given
		AbstractAirsStatusMessage message = AbstractAirsStatusMessage.parseString("An error", new RuntimeException("blarg"));
		// when
		// then
		assertEquals("0:An error:RuntimeException 'blarg':::",message.toString());
		assertEquals(0l,message.getId());
		assertEquals("",message.toAction());
		assertEquals("RuntimeException 'blarg'",message.toDescription());
		assertEquals("",message.toExplanation());
		assertEquals("An error",message.toMessage());
		assertFalse(message.isSupport());
		assertEquals("",message.toSupport());
		
		// given
		message = AbstractAirsStatusMessage.parseString("12:An error%: Runtime:Stipulated:Could fix:Been 100%% fixed:Look here", new RuntimeException("blarg"));
		// when
		// then
		assertEquals("12:An error%: Runtime:Stipulated:Could fix:Been 100%% fixed:Look here",message.toString());
		assertEquals(12l,message.getId());
		assertEquals("Been 100% fixed",message.toAction());
		assertEquals("Stipulated",message.toDescription());
		assertEquals("Could fix",message.toExplanation());
		assertEquals("An error: Runtime",message.toMessage());
		assertEquals("Look here",message.toSupport());
		assertTrue(message.isSupport());

		// given
		message = AbstractAirsStatusMessage.parseString(null, new RuntimeException("blarg"));
		// when
		// then
		assertEquals("0:System Error:RuntimeException 'blarg':::",message.toString());
		assertEquals(0l,message.getId());
		
		// given
		// when
		message = AbstractAirsStatusMessage.parseString("1024:Registration Successful!:Thank you for registering with ATP.  "
				+ "You will recieve and email soon to complete the registration process.  The email will include a html link "
				+ "that you can click to finish your registration.:::", null);
		// then
		assertEquals(1024, message.getId());
		assertEquals("Registration Successful!", message.toMessage());
		assertEquals("Thank you for registering with ATP.  "
				+ "You will recieve and email soon to complete the registration process.  The email will include a html link "
				+ "that you can click to finish your registration.", message.toDescription());
	}
	
	@Test
	public final void testPrettyPrint() {
		// given
		AbstractAirsStatusMessage message = AbstractAirsStatusMessage.parseString("An error", new RuntimeException("blarg"));
		// when
		// then
		assertEquals("[0] An error - RuntimeException 'blarg'",message.niceString());
	}

}
