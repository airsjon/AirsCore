/**
 * Copyright (c) 2011 Jon Boley, 432 NE Ravenna, Seattle Wa 98115.
 * All Rights Reserved.
 */
package com.airsltd.core.parse;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Deque;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Definition of a rule for parsing.
 * <p>
 * {@code startPattern} represents the start of the element.
 * {@code endPattern} represents the end of the element.
 * {@code substitute} specifies how the element should be modified.
 * <p>
 * If no end pattern is supplied, then the complete data for parsing the Rule
 * must be contained in the matched text from the start pattern.
 * 
 * @author Jon Boley
 *
 */
public class ParseRule {

	// bean members
	private String f_endPattern;
	private ParseSubstitution f_substitute;
	private String f_startPattern;

	// internal members
	private Pattern f_startInternal;
	private Pattern f_endInternal;
	private final List<ParseRule> f_subRules;

	private final boolean f_internalParse;

	/**
	 * @param subRules  Rules to be applied to determine if this is valid pattern
	 * @param p_parseSpan
	 * @param startPattern  match for the start of the pattern
	 * @param endPattern  match for the end of the pattern
	 * @param substitute  what to substitute
	 */
	public ParseRule(List<ParseRule> subRules, boolean p_parseSpan, String startPattern, String endPattern,
			ParseSubstitution substitute) {
		f_subRules = subRules;
		setStartPattern(startPattern);
		setEndPattern(endPattern);
		f_substitute = substitute;
		f_internalParse = p_parseSpan;
	}

	/**
	 * Helper function to create a ParseRule that separtes on a String (p_separator).
	 * 
	 * @param p_separator  not null, the String to split the text with
	 * @return a ParseRule that splits text
	 */
	public static ParseRule createSeparator(String p_separator) {
		return new ParseRule(null, false, p_separator, null,
				(p_parser, p_parsedSpan, p_originalSpan, p_startString, p_endString) -> new ArrayDeque<ParseToken>());
	}

	/**
	 * Helper function to create a ParseRule for escaping characters based on a String (p_escape).
	 * 
	 * @param p_escape  not null, the String to use for escaping characters
	 * @return a ParseRule that will create a consumable ParseToken of the excaped character.
	 */
	public static ParseRule createEscapeSeparator(final String p_escape) {
		return new ParseRule(null, false, p_escape + ".", null,
				(p_parser, p_parsedSpan, p_originalSpan, p_startString, p_endString) -> {
					p_parser.setMerging(true);
					return ParseToken.fromString(p_startString.substring(1), true);
				});
	}

	/**
	 * @return the substitute
	 */
	public ParseSubstitution getSubstitute() {
		return f_substitute;
	}

	/**
	 * @return the startPattern
	 */
	public String getStartPattern() {
		return f_startPattern;
	}

	/**
	 * @param startPattern  the startPattern to set
	 */
	public final void setStartPattern(String startPattern) {
		f_startPattern = startPattern;
		if (startPattern != null) {
			f_startInternal = Pattern.compile(startPattern, Pattern.MULTILINE);
		} else {
			f_startInternal = null;
		}
	}

	/**
	 * @return the endPattern
	 */
	public String getEndPattern() {
		return f_endPattern;
	}

	/**
	 * @param endPattern  the endPattern to set
	 */
	public final void setEndPattern(String endPattern) {
		f_endPattern = endPattern;
		if (endPattern != null) {
			f_endInternal = Pattern.compile(endPattern, Pattern.MULTILINE);
		} else {
			f_endInternal = null;
		}
	}

	/**
	 * @param substitute
	 *            the substitute to set
	 */
	public void setSubstitute(ParseSubstitution substitute) {
		f_substitute = substitute;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.airsltd.core.parse.Parser#parseString(com.airsltd.core.parse.Parser)
	 */
	public int parseString(Parser parser) {
		int l_retVal = -1;
		/*
		 * is this a match?
		 */
		final String data = parser.getData();
		final Matcher matcher = f_startInternal.matcher(data);
		if (matcher.find()) {
			if (matcher.start() == 0) {
				/*
				 * we have a potential match. note if we get to the end of the
				 * data without finding the end patter we back everything up and
				 * have no match
				 */
				l_retVal = parsePotentialMap(parser, matcher, data);
			} else {
				l_retVal = matcher.start();
			}
		}
		return l_retVal;
	}

	protected int parsePotentialMap(Parser parser, Matcher matcher, String data) {
		int l_retVal = 0;
		final String restData = data.substring(matcher.end());
		if (f_endPattern == null || f_endPattern.isEmpty()) {
			if (parser.isMerging()) {
				parser.setMerging(false);
			}
			parser.append(f_substitute.substitute(parser, null, restData, matcher.group(), null));
			parser.setData(data.substring(matcher.end()));
		} else {
			if (f_internalParse) {
				l_retVal = internalParse(parser, restData, matcher);
			} else {
				l_retVal = externalParse(parser, restData, matcher);
			}
		}
		return l_retVal;
	}

	protected int externalParse(Parser p_parser, String p_restData, Matcher p_matcher) {
		int l_retVal = 0;
		final Parser l_parser = getSubParser(p_parser);
		final Matcher endMatcher = f_endInternal.matcher(p_restData);
		if (endMatcher.find()) {
			final String l_enclosed = p_restData.substring(0, endMatcher.start());
			final Deque<ParseToken> nTokens = f_substitute.substitute(l_parser, ParseToken.fromString(l_enclosed),
					l_enclosed, p_matcher.group(), endMatcher.group());
			if (nTokens == null) {
				l_retVal = -1;
			} else {
				p_parser.applyParser(l_parser);
				p_parser.append(nTokens);
				p_parser.setData(p_restData.substring(endMatcher.end()));
			}
		}
		return l_retVal;
	}

	protected int internalParse(Parser p_parser, String p_restData, Matcher p_matcher) {
		int l_retVal = 0;
		final Parser l_parser = getSubParser(p_parser);
		l_parser.setData(p_restData);
		if (l_parser.parseString(f_endInternal)) {
			final Matcher endMatcher = f_endInternal.matcher(l_parser.getData());
			if (endMatcher.find()) {
				l_parser.setData(l_parser.getData().substring(endMatcher.end()));
				final Deque<ParseToken> nTokens = f_substitute.substitute(l_parser, l_parser.getTokens(),
						p_restData.substring(0, p_restData.indexOf(endMatcher.group())), p_matcher.group(),
						endMatcher.group());
				if (nTokens == null) {
					l_retVal = -1;
				} else {
					p_parser.applyParser(l_parser);
					p_parser.append(nTokens);
					p_parser.setData(l_parser.getData());
				}
			} else {
				l_retVal = -1;
			}
		} else {
			l_retVal = -1;
		}
		return l_retVal;
	}

	private Parser getSubParser(Parser p_parser) {
		final Parser l_retVal = new Parser(p_parser);
		if (f_subRules != null) {
			l_retVal.setRules(f_subRules);
		}
		return l_retVal;
	}

}
