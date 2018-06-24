/**
 * (c) 2015, Jon Boley 2703 Wildrose Ct, Bellingham WA 98229
 */
package com.airsltd.core.parse;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

/**
 * Defined ParseRules.
 * 
 * @author Jon Boley
 *
 */
public class ParseRules {
	
	public static final String NEWLINE = System.getProperty("line.separator");
	
	public static final ParseRule LINECOMMENTPARSE = new ParseRule(null, false, "^//|[^:]//", NEWLINE + "|$",
			(parser, oldString, enclosed, startString, endString) -> {
				final Deque<ParseToken> l_retDeque = new ArrayDeque<ParseToken>();
				if (startString.length() > 2) {
					l_retDeque.add(new ParseToken(startString.charAt(0) + ""));
				}
				l_retDeque.add(new ParseToken(endString));
				return l_retDeque;
			});

	public static final ParseRule MULTILINECOMMENTPARSE = new ParseRule(null, false, "/\\*", "\\*/",
			(parser, oldString, enclosed, startString, endString) -> new ArrayDeque<ParseToken>());

	public static final ParseRule ESCAPERULE = new ParseRule(null, false, "\\\\.", null,
			(parser, oldString, enclosed, startString, endString) -> ParseToken.fromString(startString.substring(1)));

	/**
	 * Take any text that is escaped and send the escaped character thru.
	 * <p>
	 * \<char> -> char will be sent to the 
	 */
	public static final ParseRule ESCAPEALLRULE = new ParseRule(null, false, "(?s)\\\\.", null,
			(parser, oldString, enclosed, startString, endString) -> ParseToken.fromString(startString.substring(1)));

	protected static final int MACROSIZE = 3;

	/**
	 * Load a variable for substitution. This variable (expressed as
	 * ${([alpah-numeric_-])+} ) will be set to the contents of the remainder of
	 * the current line. This value will then be substitued directly for the
	 * rest of the parsing.
	 */
	public static final ParseRule MACROVARIABLE = new ParseRule(null, true, "\\$\\{[0-9a-zA-Z_-]+\\}= ", NEWLINE,
			(parser, oldString, enclosed, startString, endString) -> {
				final String varName = startString.substring(MACROSIZE - 1, startString.length() - MACROSIZE);
				parser.addVariable(varName, new ArrayDeque<ParseToken>(oldString));
				return new ArrayDeque<ParseToken>();
			});

	/**
	 * Substitute the current value of the reference variable into the resultent
	 * string
	 */
	public static final ParseRule MACROVARIABLEREF = new ParseRule(null, false, "\\$\\{[0-9a-zA-Z_-]+\\}", null,
			(parser, oldString, enclosed, startString, endString) -> {
				final String varName = startString.substring(2, startString.length() - 1);
				return parser.getVariable(varName);
			});

	/**
	 * This rule does the same thing as {@link ParseRule#MACROVARIABLE} but the
	 * resultant string will be re-parsed when it is substituted. Note:
	 * currently this string will not be spliced in an the remainder parsed but
	 * the substring itself.
	 */
	public static final ParseRule MACROVARIABLEDELAYED = new ParseRule(null, true, "\\$\\{[0-9a-zA-Z_-]+\\*\\}= ",
			NEWLINE, (parser, oldString, enclosed, startString, endString) -> {
				final String varName = startString.substring(MACROSIZE - 1, startString.length() - MACROSIZE);
				parser.addVariable(varName, ParseToken.fromString(enclosed));
				return new ArrayDeque<ParseToken>();
			});

	/**
	 * Substitute the current value for the variable and then parse this result
	 * using the current Parse engine.
	 */
	public static final ParseRule MACROVARIABLEDELAYEDREF = new ParseRule(null, false, "\\$\\{[0-9a-zA-Z_-]+\\*\\}",
			null, (parser, oldString, enclosed, startString, endString) -> {
				final String varName = startString.substring(2, startString.length() - 1);
				final Parser l_parser = new Parser(parser);
				l_parser.setData(ParseToken.toString(parser.getVariable(varName)));
				return l_parser.parseString() ? l_parser.getTokens() : new ArrayDeque<ParseToken>();
			});

	public static final ParseRule STRINGRULE = new ParseRule(Arrays.asList(ESCAPERULE), false, "\"", "\"\\s*",
			(parser, oldString, enclosed, startString, endString) -> {
				parser.setMerging(true);
				final Deque<ParseToken> l_retVal = new ArrayDeque<ParseToken>();
				l_retVal.addAll(Arrays.asList(new ParseToken(":string"), new ParseToken(enclosed)));
				return l_retVal;
			});

	/**
	 * Note if the INTERGERRULE is preceded by FLOATRULE then (?!^\\.) is not
	 * needed.
	 *
	 */
	public static final ParseRule INTEGERRULE = new ParseRule(null, false, "[+-]*[0-9]+(?!^\\.)", "\\s*",
			(parser, oldString, enclosed, startString, endString) -> {
				parser.setMerging(true);
				final Deque<ParseToken> l_retVal = new ArrayDeque<ParseToken>();
				l_retVal.addAll(Arrays.asList(new ParseToken(":integer"), new ParseToken(startString)));
				return l_retVal;
			});

	public static final ParseRule FLOATRULE = new ParseRule(null, false, "[+-]*[0-9]+(\\.[0-9]*f*|f)", "\\s*",
			(parser, oldString, enclosed, startString, endString) -> {
				parser.setMerging(true);
				final Deque<ParseToken> l_retVal = new ArrayDeque<ParseToken>();
				l_retVal.addAll(Arrays.asList(new ParseToken(":float"), new ParseToken(startString)));
				return l_retVal;
			});

	/**
	 * Not to be instantiated.
	 */
	private ParseRules() {
		// Never created.
	}
}
