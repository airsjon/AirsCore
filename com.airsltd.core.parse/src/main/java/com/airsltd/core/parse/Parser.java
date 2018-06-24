/**
 * Copyright (c) 2011 Jon Boley, 432 NE Ravenna, Seattle Wa 98115.
 * All Rights Reserved.
 */
package com.airsltd.core.parse;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jdt.annotation.Nullable;

/**
 * The core of the Parse package. 
 * <p>
 * The parser iterates through all the parse rules and applies them as they match.
 *
 * @author Jon Boley
 *
 */
public class Parser {

	/**
	 * Variable substitution parser.
	 */
	public static final Parser VARIABLESUBSTITUTION = new Parser(
			Arrays.asList(new ParseRule[] { ParseRules.MACROVARIABLE, ParseRules.MACROVARIABLEREF }));

	/**
	 * List of rules to be applied to any input
	 */
	private final List<ParseRule> f_rules = new ArrayList<ParseRule>();
	private Deque<ParseToken> f_tokens = new ArrayDeque<ParseToken>();
	private String f_data;
	private Map<String, Deque<ParseToken>> f_variables = new HashMap<String, Deque<ParseToken>>();
	private boolean f_merging;

	@Nullable private Parser f_parent;

	/**
	 * Create a new parser with the rules <code>p_rules</code>
	 *
	 * @param p_rules
	 */
	public Parser(List<ParseRule> p_rules) {
		setRules(p_rules);
	}

	/**
	 * Create a parse whose parent is p_parser.
	 * 
	 * @param p_parser  nullable, the parser that is the parent for this parser.
	 */
	public Parser(Parser p_parser) {
		f_parent = p_parser;
	}

	/**
	 * <p>
	 * Method {@code parseString} applies the {@code rules} to the current
	 * string for the next {@code maxLook} characters. If {@code maxLook} == 0
	 * then the string is parsed in it's entirety.
	 * </p>
	 * <p>
	 * Each parse rule is iterated through and the Parser tracks the next point
	 * that a Rule would be applied. If any of the rules is valid right away
	 * (local variable parseMin is equal to 0), it is applied right then. This
	 * means that the order of the rules is important and the {@link ParseRule}s
	 * at the front of the {@link List} have priority.
	 * </p>
	 *
	 * @return a successful parse
	 * @throws ParseNothingParsed
	 */
	public boolean parseString() {
		return parseString(null);
	}

	public boolean parseString(Pattern p_end) {
		int l_nextCheck;
		boolean l_retVal = true;
		while (l_retVal && (l_nextCheck = processMore(p_end)) != 0) {
			boolean ruleApplied = false;
			for (final ParseRule parseRule : getRules()) {
				final int l_ruleCheck = applyRule(parseRule, l_nextCheck);
				if (l_ruleCheck == -1) {
					ruleApplied = true;
					break;
				} else {
					l_nextCheck = l_ruleCheck;
				}
			}
			noRuleCheck(ruleApplied, l_nextCheck);
		}
		if (l_retVal && p_end != null) {
			final Matcher l_match = p_end.matcher(f_data);
			l_retVal = l_match.find() && l_match.start() == 0;
		}
		return l_retVal;
	}

	protected int applyRule(ParseRule p_parseRule, int p_nextCheck) {
		int l_retVal = p_nextCheck;
		final int l_checkLength = f_data.length();
		final int parseMin = p_parseRule.parseString(this);
		if (parseMin == 0) {
			if (f_data.length() == l_checkLength) {
				// match failed, no idea on next potential start so need to
				// check at next spot
				l_retVal = Integer.max(1,l_retVal);
			} else {
				l_retVal = -1;
			}
		} else {
			if (parseMin > 0 && parseMin < l_retVal) {
				l_retVal = parseMin;
			}
		}
		return l_retVal;
	}

	/**
	 * Return zero if no more to process
	 *
	 * @param p_end
	 * @return
	 */
	private int processMore(Pattern p_end) {
		int l_retVal = 0;
		if (p_end == null) {
			l_retVal = f_data.length();
		} else {
			final Matcher l_match = p_end.matcher(f_data);
			l_retVal = l_match.find() ? l_match.start() : 0;
		}
		return l_retVal;
	}

	/**
	 * After apply a rule or not, then next segment of code must be added to the
	 * current result text.
	 *
	 * @param p_ruleApplied
	 * @param p_parseEngine
	 * @param p_minTillRule
	 * @param p_data
	 */
	private void noRuleCheck(boolean p_ruleApplied, int p_minTillRule) {
		if (!p_ruleApplied) {
			final String l_currentSplice = f_data.substring(0, p_minTillRule);
			final boolean l_oldMerging = f_merging;
			try {
				f_merging = true;
				addToken(l_currentSplice);
			} finally {
				f_merging = l_oldMerging;
			}
			if (p_minTillRule != 0) {
				f_data = f_data.substring(p_minTillRule);
			}
		} else {
			addToken("");
		}
	}

	/**
	 * Add a string with the value p_currentSplice on to the token Deque.
	 * <p>
	 * If the last token on the Deque is able to splice in the token, the string is added to that token.
	 * Otherwise, the string is converted into a {@link ParseToken} and added to the Deque.
	 * 
	 * @param p_currentSplice  not null, the string to add to the token {@link Deque}
	 */
	public void addToken(String p_currentSplice) {
		final ParseToken l_last = f_tokens.peekLast();
		if (l_last != null && l_last.canAdd() && (f_merging || l_last.isConsume())) {
			l_last.addText(p_currentSplice);
			if (!p_currentSplice.isEmpty()) {
				l_last.setConsume(false);
			}
		} else {
			f_tokens.add(new ParseToken(p_currentSplice));
			if (f_merging) {
				f_merging = false;
			}
		}
	}

	/**
	 * Add a ParseToken to the token Deque.
	 * <p>
	 * If the last token on the Deque can be merged then p_currentSplice is merged into the token.
	 * Otherwise, p_currentSplice is added to the Deque.
	 * 
	 * @param p_currentSplice  not null, the {@link ParseToken} to add to the token {@link Deque}
	 */
	public void addToken(ParseToken p_currentSplice) {
		final ParseToken l_last = f_tokens.peekLast();
		if (l_last != null && l_last.canAdd() && (f_merging || l_last.isConsume())) {
			l_last.addText(p_currentSplice.getText());
			l_last.setConsume(p_currentSplice.isConsume());
			f_merging = false;
		} else {
			f_tokens.addLast(p_currentSplice);
			if (f_merging) {
				f_merging = false;
			}
		}
	}

	/**
	 * Return the rules the current parse acts with.
	 * <p>
	 * The rules include the parent's rules ...
	 *  
	 * @return the rules as a {@link List} of {@link ParseRule}.
	 */
	public List<ParseRule> getRules() {
		return getRules(new ArrayList<ParseRule>());
	}

	private List<ParseRule> getRules(ArrayList<ParseRule> p_arrayList) {
		p_arrayList.addAll(f_rules);
		if (f_parent!=null) {
			f_parent.getRules(p_arrayList);
		}
		return p_arrayList;
	}

	/**
	 * @param p_rules  not null, the rules to set
	 */
	public final void setRules(List<ParseRule> p_rules) {
		if (p_rules != null) {
			f_rules.addAll(p_rules);
		}
	}

	/**
	 * @return the tokens
	 */
	public Deque<ParseToken> getTokens() {
		return f_tokens;
	}

	/**
	 * @param p_tokens
	 *            the tokens to set
	 */
	public void setTokens(Deque<ParseToken> p_tokens) {
		f_tokens = p_tokens;
	}

	/**
	 * @return the data
	 */
	public String getData() {
		return f_data;
	}

	public String getTokensAsString() {
		return ParseToken.toString(f_tokens);
	}

	/**
	 * @param p_data
	 *            the data to set
	 */
	public void setData(String p_data) {
		f_data = p_data;
	}

	/**
	 * @return the variables
	 */
	public Map<String, Deque<ParseToken>> getVariables() {
		return f_variables;
	}

	/**
	 * @param p_variables
	 *            the variables to set
	 */
	public void setVariables(Map<String, Deque<ParseToken>> p_variables) {
		f_variables = p_variables;
	}

	public void append(Deque<ParseToken> p_deque) {
		for (final ParseToken p_token : p_deque) {
			addToken(p_token);
		}
	}

	public void append(String p_string) {
		f_tokens.peekLast().addText(p_string);
	}

	public void addVariable(String varName, Deque<ParseToken> vString) {
		f_variables.put(varName, vString);
	}

	public Deque<ParseToken> getVariable(String varName) {
		Deque<ParseToken> rval = f_variables.get(varName);
		if (rval == null && f_parent != null) {
			rval = f_parent.getVariable(varName);
		}
		return rval == null ? new ArrayDeque<ParseToken>() : rval;
	}

	public void clearVariables() {
		f_variables.clear();
	}

	public void clear() {
		f_tokens.clear();
		clearVariables();
		f_data = "";
	}

	public void setMerging(boolean p_merging) {
		f_merging = p_merging;
	}

	/**
	 * @return the merging
	 */
	public boolean isMerging() {
		return f_merging;
	}

	/**
	 * Copy Parser's state.
	 * 
	 * @param p_parser  not null, the Parse to copy
	 */
	public void applyParser(Parser p_parser) {
		f_variables.putAll(p_parser.getVariables());
		f_merging = p_parser.isMerging();
	}

	/**
	 * Helper function to reset parser, load the parse string and return the
	 * tokens as their string values.
	 *
	 * @param p_string
	 * @return
	 */
	public String[] getTokensAsStrings(String p_string) {
		final List<String> l_stringList = new ArrayList<String>();
		for (final ParseToken l_token : processTokens(p_string)) {
			l_stringList.add(l_token.getText());
		}
		return l_stringList.toArray(new String[] {});
	}

	/**
	 * Turn a String into a Deque of ParseToken.
	 * 
	 * @param p_string  not null, String to be parsed
	 * @return a {@link Deque} of {@link ParseToken}s
	 */
	public Deque<ParseToken> processTokens(String p_string) {
		Deque<ParseToken> l_retVal;
		clear();
		setData(p_string);
		boolean l_validParse = false;
		l_validParse = parseString();
		if (l_validParse) {
			l_retVal = getTokens();
			if (!l_retVal.isEmpty()) {
				while (l_retVal.peekLast().getText().trim().isEmpty()) {
					l_retVal.pollLast();
				}
			}
		} else {
			l_retVal = new ArrayDeque<ParseToken>();
		}
		return l_retVal;
	}

	/**
	 * Helper function to reset parser, load the parse string and return a
	 * string with a global set of variables. Very useful with the static parser
	 * {@link Parser#VARIABLESUBSTITUTION}
	 *
	 * @param p_string
	 * @return
	 */
	public String getTokensAsString(String p_string, Properties p_variables) {
		clear();
		if (p_variables != null) {
			for (final Entry<Object, Object> l_property : p_variables.entrySet()) {
				final String l_key = (String) l_property.getKey();
				final String l_value = l_property.getValue().toString();
				f_variables.put(l_key, ParseToken.fromString(l_value));
			}
		}
		setData(p_string);
		return parseString() ? getTokensAsString() : null;
	}

	/**
	 * Add a variable into the parser.
	 * 
	 * @param p_varName  not null, the name of the variable
	 * @param p_value  not null, the value of the variable
	 */
	public void addVariable(String p_varName, String p_value) {
		addVariable(p_varName, ParseToken.fromString(p_value));
	}

}
