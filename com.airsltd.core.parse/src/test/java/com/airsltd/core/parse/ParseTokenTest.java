package com.airsltd.core.parse;

import static org.junit.Assert.*;

import java.util.ArrayDeque;
import java.util.Deque;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class ParseTokenTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	private ParseToken f_token;
	private ParseToken f_token2;
	private ParseToken f_token3;

	@Before
	public void setUp() throws Exception {
		f_token = new ParseToken();
		f_token2 = new ParseToken("Hereweare");
		f_token3 = new ParseToken("Andhere", true);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testAddText() {
		// given
		// when
		f_token.addText("test");
		// then
		assertEquals("test", f_token.getText());
	}

	@Test
	public void testCanAdd() {
		// given
		// when
		// then
		assertTrue(f_token.canAdd());
	}

	@Test
	public void testSetText() {
		// given
		// when
		f_token.setText("another");
		// then
		assertEquals("another", f_token.getText());
	}

	@Test
	public void testIsConsume() {
		// given
		// when
		// then
		assertFalse(f_token.isConsume());
		assertFalse(f_token2.isConsume());
		assertTrue(f_token3.isConsume());
	}

	@Test
	public void testSetConsume() {
		// given
		// when
		f_token.setConsume(true);
		// then
		assertTrue(f_token.isConsume());
	}

	@Test
	public void testFromStringString() {
		// given
		// when
		Deque<ParseToken> l_tokens = ParseToken.fromString("test");
		// then
		assertEquals(1, l_tokens.size());
		assertEquals(new ParseToken("test"), l_tokens.peekFirst());
	}

	@Test
	public void testFromStringStringBoolean() {
		// given
		// when
		Deque<ParseToken> l_tokens = ParseToken.fromString("test", true);
		// then
		assertEquals(1, l_tokens.size());
		assertEquals(new ParseToken("test", true), l_tokens.peekFirst());
	}

	@Test
	public void testToStringDequeOfParseToken() {
		// given
		Deque<ParseToken> l_tokens = new ArrayDeque<ParseToken>();
		l_tokens.add(new ParseToken("by"));
		l_tokens.add(new ParseToken("far"));
		// when
		// then
		assertEquals("byfar", ParseToken.toString(l_tokens));
	}

	@Test
	public void testToString() {
		// given
		// when
		// then
		assertEquals(" [f]", f_token.toString());
		assertEquals("Hereweare [f]", f_token2.toString());
		assertEquals("Andhere [t]", f_token3.toString());
	}

	@Test
	public void testHashCode() {
		// given
		// when
		// then
		assertEquals(39308, f_token.hashCode());
		assertEquals(-256724926, f_token2.hashCode());
		assertEquals(802993913, f_token3.hashCode());
	}
	
	@Test
	public void testEquals() {
		// given
		// when
		// then
		assertTrue(f_token.equals(f_token));
		assertFalse(f_token.equals(null));
		assertFalse(f_token.equals(f_token2));
		assertTrue(f_token.equals(new ParseToken()));
		assertFalse(f_token.equals(new ParseToken("", true)));
	}
}
