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
public class ReadListRule extends ParseRule {

	private static final ParseSubstitution PARSESUBSTITUTION = (p_parser, p_parsedSpan, p_originalSpan, p_startString, p_endString) -> {
final Deque<ParseToken> l_retDeque = new ArrayDeque<ParseToken>();
l_retDeque.addLast(new ParseToken(":list"));
if (!p_parsedSpan.isEmpty() && p_parsedSpan.peekLast().getText().isEmpty()) {
	p_parsedSpan.pollLast();
}
l_retDeque.addAll(p_parsedSpan);
l_retDeque.addLast(new ParseToken(":eoList"));
p_parser.setMerging(true);
return l_retDeque;
};

	public ReadListRule() {
		super(LispParser.LISPPRULES, true, "\\(", "\\)\\s*", PARSESUBSTITUTION);
	}

}
