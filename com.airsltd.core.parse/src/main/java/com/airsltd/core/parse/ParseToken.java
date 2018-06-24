/**
 * Copyright (c) 2013 Jon Boley
 */
package com.airsltd.core.parse;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * A parsed element.
 * <p>
 * The {@link Parser} will create a {@link Deque} of ParseToken.
 *
 * @author Jon Boley
 *
 */
public class ParseToken {

	private static final int HASHPRIME = 31;
	private static final int TRUEVALUE = 1231;
	private static final int FALSEVALUE = 1237;
	/**
	 * The text that this token represents.
	 */
	private String f_text;
	/**
	 * When set a ParseToken can consume more text.
	 */
	private boolean f_consume;

	/**
	 * Generate an empty ParseToken.
	 */
	public ParseToken() {
		this("", false);
	}

	/**
	 * Create a ParseToken containing text and consume status.
	 * <p>
	 * 
	 * @param p_text  not null, the text this token represents
	 * @param p_consume  boolean, the token allows more text to be appended
	 */
	public ParseToken(String p_text, boolean p_consume) {
		f_text = p_text;
		f_consume = p_consume;
	}

	/**
	 * Constructor that takes text.
	 * <p>
	 * The token is set up to not consume more text.
	 * 
	 * @param p_text  not null, the text to be stored in the token.
	 */
	public ParseToken(String p_text) {
		this(p_text,false);
	}

	/**
	 * Add text to the token.
	 * 
	 * @param p_text  not null, the text to be added
	 */
	public void addText(String p_text) {
		f_text = f_text + p_text;
	}

	/**
	 * Can a token be expanded with more text. Normal tokens can.
	 *
	 * @return true if more text can be added to the token.
	 * @see ParseToken#isConsume()
	 */
	public boolean canAdd() {
		return true;
	}

	/**
	 * Return the text of the ParseToken.
	 * 
	 * @return the text that this represents.
	 */
	public String getText() {
		return f_text;
	}

	/**
	 * @param p_text  non null, the text to set
	 */
	public void setText(String p_text) {
		f_text = p_text;
	}

	/**
	 * Does this token allow for more text to be added to it.
	 * 
	 * @return the true if more text may be added to the token.
	 */
	public boolean isConsume() {
		return f_consume;
	}

	/**
	 * @param p_consume  boolean, the consume to set
	 */
	public void setConsume(boolean p_consume) {
		f_consume = p_consume;
	}

	/**
	 * Helper function to create a single element Deque with ParseToken(p_endString).
	 * 
	 * @param p_endString  not null, the value of the token
	 * @return a Deque containing a single (non-consumable) ParseToken.
	 */
	public static Deque<ParseToken> fromString(String p_endString) {
		final Deque<ParseToken> l_retVal = new ArrayDeque<ParseToken>();
		l_retVal.add(new ParseToken(p_endString));
		return l_retVal;
	}

	/**
	 * Helper function to create a single element Deque with ParseToken(p_string) and p_consume set accordingly.
	 * 
	 * @param p_string  not null, the value of the token
	 * @param p_consume  boolean, true if this token can accept more text
	 * @return a single element Deque with the new ParseToken in it.
	 */
	public static Deque<ParseToken> fromString(String p_string, boolean p_consume) {
		final Deque<ParseToken> l_retVal = new ArrayDeque<ParseToken>();
		l_retVal.add(new ParseToken(p_string, p_consume));
		return l_retVal;
	}

	/**
	 * Convert a Deque of ParseTokens into a string.
	 * 
	 * @param p_tokens  not null, a Deque of the tokens to be converted to a String
	 * @return a String built from the ParseTokens in p_tokens.
	 */
	public static String toString(Deque<ParseToken> p_tokens) {
		final StringBuilder l_sb = new StringBuilder();
		p_tokens.forEach(l_token -> l_sb.append(l_token.getText()));
		return l_sb.toString();
	}

	@Override
	public String toString() {
		return f_text + " [" + (f_consume ? "t" : "f") + "]";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = HASHPRIME;
		int result = 1;
		result = prime * result + (f_consume ? TRUEVALUE : FALSEVALUE);
		result = prime * result + (f_text == null ? 0 : f_text.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		boolean l_retVal = false;
		if (this == obj) {
			l_retVal = true;
		} else if (obj instanceof ParseToken) {
			final ParseToken other = (ParseToken) obj;
			l_retVal = f_consume == other.f_consume && f_text.equals(other.f_text);
		}
		return l_retVal;
	}

}
