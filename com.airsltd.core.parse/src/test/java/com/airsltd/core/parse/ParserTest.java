/**
 * Copyright (c) 2011 Jon Boley, 432 NE Ravenna, Seattle Wa 98115. 
 * All Rights Reserved. 
 */
package com.airsltd.core.parse;

import static org.junit.Assert.*;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Deque;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.airsltd.core.data.CoreInterface;
import com.airsltd.core.parse.ParseRule;
import com.airsltd.core.parse.ParseSubstitution;

/**
 * @author Jon
 *
 */
public class ParserTest {

	static Pattern pattern = Pattern.compile("([0-9a-zA-Z-_]*):([0-9a-zA-Z-_]*)");
	private static List<ParseRule> parseRules = new ArrayList<ParseRule>();
	static Parser peToTest;
	private static List<ParseRule> parseRules2 = new ArrayList<ParseRule>();
	static Parser peToTest2;
	private static List<ParseRule> parseRules3 = new ArrayList<ParseRule>();
	static Parser peToTest3 = new Parser(parseRules3 );
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		parseRules.add(ParseRules.ESCAPERULE);
		parseRules.add(ParseRules.LINECOMMENTPARSE);
		parseRules.add(ParseRules.MULTILINECOMMENTPARSE);
		parseRules.add(ParseRules.MACROVARIABLE);
		parseRules.add(ParseRules.MACROVARIABLEREF);
		parseRules.add(ParseRules.MACROVARIABLEDELAYED);
		parseRules.add(ParseRules.MACROVARIABLEDELAYEDREF);
		ParseRule brRule = new ParseRule(new ArrayList<ParseRule>(), false, "\\{", "\\}", new ParseSubstitution() {
			@Override
			public Deque<ParseToken> substitute(Parser parseEngine, Deque<ParseToken> oldString, String enclosed, String startString,
					String endString) {
				Matcher matcher = pattern.matcher(ParseToken.toString(oldString));
				if (matcher.matches()) {
					return ParseToken.fromString("[ bean reference: " + matcher.group(2) + " member of " + matcher.group(1) + "]");
				} else return null;
			}
		});
		List<ParseRule> subRules = new ArrayList<ParseRule>();
		subRules.add(ParseRules.ESCAPERULE);
		subRules.add(ParseRules.LINECOMMENTPARSE);
		subRules.add(ParseRules.MULTILINECOMMENTPARSE);
		subRules.add(brRule);
		parseRules.add(new ParseRule(subRules, true, "\\#\\{", "\\}",
				new ParseSubstitution() {

			@Override
			public Deque<ParseToken> substitute(Parser parseEngine, Deque<ParseToken> oldString, String enclosed, String startString,
					String endString) {
				Deque<ParseToken> l_retVal = new ArrayDeque<ParseToken>();
				l_retVal.add(new ParseToken("[ javascript code! -> "));
				l_retVal.addAll(oldString);
				l_retVal.add(new ParseToken("]"));
				return l_retVal;
			}
		}));
		parseRules.add(brRule);
		List<ParseRule> subRules2 = new ArrayList<ParseRule>();
		subRules2.add(ParseRules.ESCAPEALLRULE);
		ParseRule lineClone = new ParseRule(subRules2 , true, ParseRules.LINECOMMENTPARSE.getStartPattern(),
				ParseRules.LINECOMMENTPARSE.getEndPattern(),
				ParseRules.LINECOMMENTPARSE.getSubstitute());
		parseRules2.add(lineClone);
		parseRules2.add(new ParseRule(subRules2, false, "\\{","\\}",
				new ParseSubstitution() {

			@Override
			public Deque<ParseToken> substitute(Parser parseEngine, Deque<ParseToken> oldString, String enclosed, String startString,
					String endString) {
				Matcher matcher = pattern.matcher(ParseToken.toString(oldString));
				if (matcher.matches()) {
					return ParseToken.fromString("[ bean reference: " + matcher.group(2) + " member of " + matcher.group(1) + "]");
				} else return null;
			}}
		));
		List<ParseRule> subRules3 = new ArrayList<ParseRule>();
		subRules3.add(new MockParseRule(null, false, "y", null, null));
		parseRules3.add(new MockParseRule(null, false, "x", null, null));
		parseRules3.add(new ParseRule(subRules3 , false, ParseRules.LINECOMMENTPARSE.getStartPattern(),
				ParseRules.LINECOMMENTPARSE.getEndPattern(),
				ParseRules.LINECOMMENTPARSE.getSubstitute()));
		peToTest = new Parser(parseRules );
		peToTest2 = new Parser(parseRules2 );
		peToTest3 = new Parser(parseRules3 );
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
	 * Test method for {@link com.airsltd.core.parse.Parser#parseString(com.airsltd.core.parse.Parser)}.
	 * This test looks to see if an exception if thrown when a parse rule fails to consume the text.
	 */
	public final void testParseStringParserFail() {
		// given
		// when
		peToTest3.setData("and now // we y fail!");
		// then
		assertFalse(peToTest3.parseString());
	}

	/**
	 * Test method for {@link com.airsltd.core.parse.Parser#parseString(com.airsltd.core.parse.Parser)}.
	 * This test looks to see if an exception if thrown when a parse rule fails to consume the text.
	 */
	public final void testParseStringParserFail2() {
		// given
		// when
		peToTest3.setData("and now we x fail!");
		// then
		assertFalse(peToTest3.parseString());
	}
		
	/**
	 * Test method for {@link com.airsltd.core.parse.Parser#parseString(com.airsltd.core.parse.Parser)}.
	 */
	@Test
	public final void testParseStringParser() {
		peToTest.setData("Hello this is a {beanreference:test}. yahoo!");
		assertTrue(peToTest.parseString());
		String retString=peToTest.getTokensAsString();
		assertEquals("Hello this is a [ bean reference: test member of beanreference]. yahoo!", retString);
		peToTest.clear();
		peToTest.setData("Hello #{ javascript code! }. yahoo!");
		assertTrue(peToTest.parseString());
		retString=peToTest.getTokensAsString();
		assertEquals("Hello [ javascript code! ->  javascript code! ]. yahoo!", retString);
		peToTest.clear();
		peToTest.setData("Hello #{javascript {bean:reference}}");
		assertTrue(peToTest.parseString());
		retString=peToTest.getTokensAsString();
		assertEquals("Hello [ javascript code! -> javascript [ bean reference: reference member of bean]]", retString);
		peToTest.clear();
		peToTest.setData("Text #{code // with some comments "+CoreInterface.NEWLINE+"more code/* with "+CoreInterface.NEWLINE+" a lot more "+CoreInterface.NEWLINE+" comments"+CoreInterface.NEWLINE+" */ and even more code}");
		assertTrue(peToTest.parseString());
		retString=peToTest.getTokensAsString();
		assertEquals("Text [ javascript code! -> code "+CoreInterface.NEWLINE+"more code and even more code]",retString);
		peToTest.clear();
		peToTest.setData("Text #{ code that might end \\} soon "+CoreInterface.NEWLINE+" but doesn't it end's here}");
		assertTrue(peToTest.parseString());
		retString=peToTest.getTokensAsString();
		assertEquals("Text [ javascript code! ->  code that might end } soon "+CoreInterface.NEWLINE+" but doesn't it end's here]", retString);
		peToTest.clear();
		peToTest.setData("Fake bean ref {notareference}");
		assertTrue(peToTest.parseString());
		retString=peToTest.getTokensAsString();
		assertEquals("Fake bean ref {notareference}", retString);
		peToTest.clear();
		peToTest.setData(" a line comment with no line end // and here we are!");
		assertTrue(peToTest.parseString());
		retString=peToTest.getTokensAsString();
		assertEquals(" a line comment with no line end ",retString);
		peToTest2.setData(" something // with a comment on \\\n multiple lines ");
		assertTrue(peToTest2.parseString());
		retString=peToTest2.getTokensAsString();
		assertEquals(" something ", retString);
		peToTest2.clear();
		peToTest2.setData(" something {abeanreference}");
		assertTrue(peToTest2.parseString());
		retString=peToTest2.getTokensAsString();
		assertEquals(" something {abeanreference}", retString);
		peToTest.clear();
		peToTest.setData(" nothing #{ to expand \\}");
		assertTrue(peToTest.parseString());
		retString=peToTest.getTokensAsString();
		assertEquals(" nothing #{ to expand }", retString);
		peToTest.clear();
		peToTest.setData(" nothing { to expand");
		assertTrue(peToTest.parseString());
		retString=peToTest.getTokensAsString();
		assertEquals(" nothing { to expand", retString);
		peToTest.clear();
		peToTest.setData(" nothing #{ to expand \\} test");
		assertTrue(peToTest.parseString());
		retString=peToTest.getTokensAsString();
		assertEquals(" nothing #{ to expand } test", retString);
		peToTest.clear();
		peToTest.setData("${testVar}= something "+CoreInterface.NEWLINE+" something simple like ${testVar}");
		assertTrue(peToTest.parseString());
		retString=peToTest.getTokensAsString();
		assertEquals(" something simple like something ", retString);
		peToTest.clear();
		peToTest.setData("${testVar}= something"+CoreInterface.NEWLINE+" something simple like ${testVar}"+CoreInterface.NEWLINE+"" +
				"and lets add ${testVar} some more");
		assertTrue(peToTest.parseString());
		retString=peToTest.getTokensAsString();
		assertEquals(" something simple like something"+CoreInterface.NEWLINE+"and lets add something some more", retString);
		peToTest.clear();
		peToTest.setData(
				"${testvar}= something simple"+CoreInterface.NEWLINE+"" +
						"${testvar2}= here is a ref ${testvar} and ${testVar}code"+CoreInterface.NEWLINE +
						"${testvar3*}= here is a delayed reference ${testvar}"+CoreInterface.NEWLINE +
						"${testvar}= something new and simple"+CoreInterface.NEWLINE +
						"returns: ${testvar2}"+CoreInterface.NEWLINE+"and ${testvar3*}");
		assertTrue(peToTest.parseString());
		retString=peToTest.getTokensAsString();
		assertEquals("returns: here is a ref something simple and code"+CoreInterface.NEWLINE+"and here is a delayed reference something new and simple",
				retString);
		peToTest.clear();
		peToTest.setData(
				"${testvar}= something simple"+CoreInterface.NEWLINE+"" +
						"${testvar2}= here is a ref ${testvar} and ${testVar}"+CoreInterface.NEWLINE +
						"${testvar3*}= here is a delayed reference ${testvar}"+CoreInterface.NEWLINE +
						"${testvar}= something new and simple"+CoreInterface.NEWLINE +
						"returns: ${testvar2}"+CoreInterface.NEWLINE+"and ${testvar3*}");
		assertTrue(peToTest.parseString());
		retString=peToTest.getTokensAsString();
		assertEquals("returns: here is a ref something simple and "+CoreInterface.NEWLINE+"and here is a delayed reference something new and simple",
				retString);
	}

	/**
	 * Test method for {@link com.airsltd.core.parse.Parser#getData()}.
	 */
	@Test
	public final void testGetData() {
		peToTest.setData("test data");
		assertEquals("test data",peToTest.getData());
		peToTest.setData("");
		assertEquals("",peToTest.getData());
	}

	/**
	 * Test method for {@link com.airsltd.core.parse.Parser#setData(java.lang.String)}.
	 */
	@Test
	public final void testSetData() {
		peToTest.setData("test data");
		assertEquals("test data",peToTest.getData());
		peToTest.setData("");
		assertEquals("",peToTest.getData());
	}

	/**
	 * Test method for {@link com.airsltd.core.parse.Parser#getVariables()} and
	 * {@link com.airsltd.core.parse.ParserTest#setVariables()}.
	 */
	@Test
	public final void testGetVariables() {
		Map<String, Deque<ParseToken>> oldVar = peToTest.getVariables();
		peToTest.setVariables(null);
		assertNull(peToTest.getVariables());
		peToTest.setVariables(oldVar);
		assertNotNull(peToTest.getVariables());
		assertEquals(oldVar, peToTest.getVariables());
	}
}
