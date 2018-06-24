/**
 *
 */
package com.airsltd.core.lisp.parser;

import java.util.ArrayDeque;
import java.util.Deque;

import com.airsltd.core.parse.ParseRule;
import com.airsltd.core.parse.ParseSubstitution;
import com.airsltd.core.parse.ParseToken;

/**
 * @author jon_000
 *
 */
public class TokenRule extends ParseRule {

	private static final ParseSubstitution PARSESUBSTITUTION = (p_parser, p_parsedSpan, p_originalSpan, p_startString, p_endString) -> {
final Deque<ParseToken> l_retStack = new ArrayDeque<ParseToken>();
l_retStack.addLast(new ParseToken(":token"));
l_retStack.addLast(new ParseToken(p_startString));
p_parser.setMerging(true);
return l_retStack;
};

	public TokenRule() {
		super(null, false, "[a-zA-Z][a-zA-Z0-9_]*", "\\s*", PARSESUBSTITUTION);
	}

}
