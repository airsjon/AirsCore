/**
 * Copyright (c) 2011 Jon Boley, 432 NE Ravenna, Seattle Wa 98115. 
 * All Rights Reserved. 
 */
package com.airsltd.core.parse;

import java.util.List;

/**
 * Class {@code MockParseRule} is created with the intention of parsing a text string and doing nothing.
 * Normal ParseRule should always consume something from the data of the PairEngine.
 * 
 * @author Jon Boley
 *
 */
public class MockParseRule extends ParseRule {

	public MockParseRule(List<ParseRule> subRules, boolean parseInner, String startPattern,
			String endPattern, ParseSubstitution substitute) {
		super(subRules, parseInner, startPattern, endPattern, substitute);
	}

	/* (non-Javadoc)
	 * @see com.airsltd.core.parse.ParseRule#parseString(com.airsltd.core.parse.ParseEngine, int)
	 */
	@Override
	public int parseString(Parser parseEngine) {
		// pretend like we did something but do nothing.
		return 0;
	}
	
}
