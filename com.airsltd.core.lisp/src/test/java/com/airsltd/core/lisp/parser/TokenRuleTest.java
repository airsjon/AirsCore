package com.airsltd.core.lisp.parser;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.*;

import java.util.Deque;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.airsltd.core.lisp.function.ISimpleValue;
import com.airsltd.core.parse.ParseSubstitution;
import com.airsltd.core.parse.ParseToken;
import com.airsltd.core.parse.Parser;

public class TokenRuleTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testTokenRule() {
		// given
		TokenRule l_tokenRule = new TokenRule();
		ParseSubstitution l_substitution = l_tokenRule.getSubstitute();
		Parser m_parser = mock(Parser.class);
		@SuppressWarnings("unchecked")
		Deque<ParseToken> m_span = mock(Deque.class);
		// when
		Deque<ParseToken> l_retVal = l_substitution.substitute(m_parser, m_span, "something", "something", "");
		// then
		assertEquals("[:token [f], something [f]]", l_retVal.toString());
	}
	
	@Test
	public void testTokenRuleFromString() {
		// given
		// when
		ISimpleValue l_tokens = LispParser.parseString("atoken");
		// then
		assertEquals("(BODY atoken)", l_tokens.toString());
		
	}

}
