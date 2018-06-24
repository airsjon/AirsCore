package com.airsltd.core.parse;

import static org.junit.Assert.*;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.airsltd.core.data.CoreInterface;
import com.airsltd.core.parse.ParseRule;
import com.airsltd.core.parse.ParseSubstitution;

public class ParseRuleTest {

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
	public final void testParseString() {
	
		Parser parseEngine = new Parser(new ArrayList<ParseRule>());
		parseEngine.setData("// comment "+CoreInterface.NEWLINE+" test!");
		parseEngine.setTokens(new ArrayDeque<ParseToken>());
		ParseRules.LINECOMMENTPARSE.parseString(parseEngine);
		assertEquals(" test!", parseEngine.getData());
		assertEquals(CoreInterface.NEWLINE, ParseToken.toString(parseEngine.getTokens()));
		parseEngine.setData("// comment");
		parseEngine.setTokens(new ArrayDeque<ParseToken>());
		ParseRules.LINECOMMENTPARSE.parseString(parseEngine);
		assertEquals("", parseEngine.getData());
		assertEquals("", ParseToken.toString(parseEngine.getTokens()));
		parseEngine.setData("/* this is a test "+CoreInterface.NEWLINE+" involving several lines of comments "+CoreInterface.NEWLINE+" but it does end */Hello!");
		parseEngine.setTokens(new ArrayDeque<ParseToken>());
		ParseRules.MULTILINECOMMENTPARSE.parseString(parseEngine);
		assertEquals("Hello!", parseEngine.getData());
		assertEquals("", ParseToken.toString(parseEngine.getTokens()));
		parseEngine.setData("\\{ a bracket to test");
		parseEngine.setTokens(new ArrayDeque<ParseToken>());
		ParseRules.ESCAPERULE.parseString(parseEngine);
		assertEquals(" a bracket to test", parseEngine.getData());
		assertEquals("{", ParseToken.toString(parseEngine.getTokens()));
		parseEngine.setData("a bracket to test");
		parseEngine.setTokens(new ArrayDeque<ParseToken>());
		new ParseRule(null,false,"a ","",new ParseSubstitution() {

			@Override
			public Deque<ParseToken> substitute(Parser parseEngine2, Deque<ParseToken> p_parsed, String enclosed, String startString,
					String endString) {
				return ParseToken.fromString("an ");
			}
		}).parseString(parseEngine);
		assertEquals("bracket to test", parseEngine.getData());
		assertEquals("an ", ParseToken.toString(parseEngine.getTokens()));
	}

	@Test
	public final void testMacroVariableRef() {
		// given
		Parser l_parseE = new Parser(Arrays.asList(new ParseRule[] {
				ParseRules.MACROVARIABLE, ParseRules.MACROVARIABLEREF }));
		Deque<ParseToken> l_retVal = null;
		Deque<ParseToken> l_retVal2 = null;
		// when
		l_parseE.setData("here is a variable${testVar}= thatisdefined"+CoreInterface.NEWLINE+
				"${testVar}:${testVar}:${testVar}");
		assertTrue(l_parseE.parseString());
		l_retVal = l_parseE.getTokens();
		l_parseE.setTokens(new ArrayDeque<ParseToken>());
		l_parseE.setData("${testVar2}= three"+CoreInterface.NEWLINE+
				"here is something that looks like a variable but is not ${testVar}= because there is not end line");
		assertTrue(l_parseE.parseString());
		l_retVal2 = l_parseE.getTokens();
		// then
		assertEquals("here is a variablethatisdefined:thatisdefined:thatisdefined", ParseToken.toString(l_retVal));
		assertEquals("here is something that looks like a variable but is not thatisdefined= because there is not end line", ParseToken.toString(l_retVal2));
	}
	
	@Test
	public final void testMacroVariableRefDelayed() {
		// given
		Parser l_parseE = new Parser(Arrays.asList(new ParseRule[] {
				ParseRules.MACROVARIABLE, ParseRules.MACROVARIABLEREF, ParseRules.MACROVARIABLEDELAYED, ParseRules.MACROVARIABLEDELAYEDREF }));
		Deque<ParseToken> l_retVal = null;
		// when
		l_parseE.setData("here is a variable${testVarD*}= thatis${testVar}defined"+CoreInterface.NEWLINE+
				"${testVar}= not"+CoreInterface.NEWLINE + 
				"${testVarD*}"+CoreInterface.NEWLINE+
				"${testVar}= "+CoreInterface.NEWLINE+
				"${testVarD*}");
		assertTrue(l_parseE.parseString());
		l_retVal = l_parseE.getTokens();
		// then
		assertEquals("here is a variablethatisnotdefined"+CoreInterface.NEWLINE+"thatisdefined", ParseToken.toString(l_retVal));
	}
	
	@Test
	public final void testGetStartPattern() {
		ParseRule pr = new ParseRule(null, false, "x","y",null);
		assertEquals("x", pr.getStartPattern());
	}

	@Test
	public final void testSetStartPattern() {
		ParseRule pr = new ParseRule(null, false, null,"y",null);
		assertEquals(null, pr.getStartPattern());
		pr.setStartPattern("x");
		assertEquals("x", pr.getStartPattern());
	}

	@Test
	public final void testGetEndPattern() {
		ParseRule pr = new ParseRule(null, false, "x","y",null);
		assertEquals("y", pr.getEndPattern());
	}

	@Test
	public final void testSetEndPattern() {
		ParseRule pr = new ParseRule(null, false, "x",null,null);
		assertEquals(null, pr.getEndPattern());
		pr.setEndPattern("y");
		assertEquals("y", pr.getEndPattern());
	}

	@Test
	public final void testGetSubstitute() {
		ParseSubstitution ps = new ParseSubstitution() {
			
			@Override
			public Deque<ParseToken> substitute(Parser parseEngine, Deque<ParseToken> oldString, String enclosed, String sString, String eString) {
				return null;
			}
		};
		ParseRule pr = new ParseRule(null,false,"x","y",ps);
		assertEquals(ps, pr.getSubstitute());
	}

	@Test
	public final void testSetSubstitute() {
		ParseRule pr = new ParseRule(null,false,"x","y",null);
		ParseSubstitution ps = new ParseSubstitution() {
			
			@Override
			public Deque<ParseToken> substitute(Parser parseEngine, Deque<ParseToken> oldString, String enclosed, String sString, String eString) {
				return null;
			}
		};
		assertEquals(null, pr.getSubstitute());
		pr.setSubstitute(ps);
		assertEquals(ps, pr.getSubstitute());
	}

	@Test
	public final void testCreateSeperator() {
		// given
		ParseRule pr = ParseRule.createSeparator(":");
		Parser parser = new Parser(Arrays.asList(new ParseRule[] { pr }));
		parser.setData("this is some::data:that is:seperated:by:colons");
		// when
		assertTrue(parser.parseString());
		// then
		Deque<ParseToken> l_tokens = parser.getTokens();
		assertEquals(7, l_tokens.size());
		assertEquals("this is some", l_tokens.pollFirst().getText());
		assertEquals("",l_tokens.pollFirst().getText());
		assertEquals("data", l_tokens.pollFirst().getText());
		assertEquals("that is", l_tokens.pollFirst().getText());
		assertEquals("seperated", l_tokens.pollFirst().getText());
		assertEquals("by", l_tokens.pollFirst().getText());
		assertEquals("colons", l_tokens.pollFirst().getText());
	}

	@Test
	public final void testCreateEscapeSeperator() {
		// given
		ParseRule pr = ParseRule.createEscapeSeparator("%");
		ParseRule pr2 = ParseRule.createSeparator(":");
		Parser parser = new Parser(Arrays.asList(new ParseRule[] { pr, pr2 }));
		parser.setData("this%%is%: some:data:that is:seperated:by:colons%:");
		// when
		assertTrue(parser.parseString());
		// then
		Deque<ParseToken> l_tokens = parser.getTokens();
		assertEquals(6, l_tokens.size());
		assertEquals("this%is: some", l_tokens.pollFirst().getText());
		assertEquals("data", l_tokens.pollFirst().getText());
		assertEquals("that is", l_tokens.pollFirst().getText());
		assertEquals("seperated", l_tokens.pollFirst().getText());
		assertEquals("by", l_tokens.pollFirst().getText());
		assertEquals("colons:", l_tokens.pollFirst().getText());
	}
}
