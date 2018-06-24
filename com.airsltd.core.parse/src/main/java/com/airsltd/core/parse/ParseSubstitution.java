/**
 * Copyright (c) 2011 Jon Boley, 432 NE Ravenna, Seattle Wa 98115.
 * All Rights Reserved.
 */
package com.airsltd.core.parse;

import java.util.Deque;

import org.eclipse.jdt.annotation.Nullable;

/**
 * Interface that provides function to do a textual macro expansion using the
 * old data
 *
 * @author Jon Boley
 *
 */
@FunctionalInterface
public interface ParseSubstitution {

	/**
	 * Replace <i>oldString</i> in the original text. 
	 * <p>
	 * When a ParseRule finds a match, a call to ParseSubstitution will both verify and parse the match.
	 * A ParseRule consists of a starting match and ending match.
	 * When text is found that matches both elements, three values are generated.
	 * The startText, 
	 * This function can return:
	 * <ul>
	 * <li>{@link Deque} of {@link ParseToken} representing the tokens
	 * substituted for the text</li>
	 * <li>Empty {@link Deque} to represent a valid parse, but no tokens
	 * generated</li>
	 * <li>null - incorrect formation for this Element. The system will back up
	 * and continue looking for other possible matches.</li>
	 * </ul>
	 *
	 * @param parser  not null, the Parser in use
	 * @param p_parsedSpan  
	 * @param p_originalSpan
	 * @param startString
	 * @param endString
	 * @return null if not match, otherwise a Deque of ParseTokens that detail the parse.
	 */
	@Nullable Deque<ParseToken> substitute(Parser parser, Deque<ParseToken> p_parsedSpan, String p_originalSpan,
			String startString, String endString);

}
