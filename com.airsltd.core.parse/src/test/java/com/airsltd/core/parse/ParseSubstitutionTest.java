package com.airsltd.core.parse;

import static org.junit.Assert.*;

import java.util.Deque;

import org.junit.Test;

import com.airsltd.core.parse.ParseSubstitution;

public class ParseSubstitutionTest {

	@Test
	public final void testSubstitute() {
		ParseSubstitution ps = new ParseSubstitution() {
			
			@Override
			public Deque<ParseToken> substitute(Parser parseEngine, Deque<ParseToken> oldString, String enclosed, String startString, String endString) {
				return ParseToken.fromString("***-"+ParseToken.toString(oldString)+"-***");
			}
		};
		assertEquals("***-worked-***", ParseToken.toString(ps.substitute(null,ParseToken.fromString("worked"),"worked","","")));
	}

}
