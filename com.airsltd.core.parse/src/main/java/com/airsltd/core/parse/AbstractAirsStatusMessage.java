/**
 * Copyright (c) 2012, Jon Boley
 * 432 NE Ravenna Blvd
 * Seattle, WA 98115
 */
package com.airsltd.core.parse;

import java.util.Arrays;

import com.airsltd.core.IAirsStatusMessage;
import com.airsltd.core.IPrettyObject;

/**
 * This provides for extended messaging by Airs components. A message is written
 * in the following format:
 *
 * [id]message[description]explanation[action]support
 *
 * @author Jon
 *
 */
public class AbstractAirsStatusMessage implements IAirsStatusMessage, IPrettyObject {

	private static final int INDEXID = 0;
	private static final int INDEXMESSAGE = 1;
	private static final int INDEXDESCIPTION = 2;
	private static final int INDEXEXPLANATION = 3;
	private static final int INDEXACTION = 4;
	private static final int INDEXSUPPORT = 5;
	private static final Parser PARSER = new Parser(
			Arrays.asList(new ParseRule[] { ParseRule.createEscapeSeparator("%"), ParseRule.createSeparator(":") }));

	private long f_id;
	private String f_message = "";
	private String f_description = "";
	private String f_explanation = "";
	private String f_action = "";
	private String f_support = "";

	public AbstractAirsStatusMessage(String[] p_messageTokens, Throwable p_exception) {

		final int l_size = p_messageTokens.length;
		switch (l_size) {
		case 0:
			f_message = "System Error";
			break;
		case 1:
			f_message = p_messageTokens[0];
			break;
		default:
			loadMessage(p_messageTokens);
		}
		if (p_exception != null && f_description.isEmpty()) {
			String l_exceptionMessage = p_exception.getClass().getSimpleName();
			if (l_exceptionMessage.contains("$")) {
				l_exceptionMessage = l_exceptionMessage.substring(0, l_exceptionMessage.indexOf('$'));
			}
			f_description = l_exceptionMessage += " '" + p_exception.getLocalizedMessage() + "'";
		}
	}

	/**
	 * Generate an AbstractAirsStatusMessage from a message. Messages that are
	 * well formed (match messagePattern) will be parsed. Otherwise, the Status
	 * will be built from the exception and complete message
	 *
	 * @param f_string
	 * @param p_exception
	 * @return
	 */
	public static AbstractAirsStatusMessage parseString(String p_string, Throwable p_exception) {
		final String[] l_messageTokens = PARSER.getTokensAsStrings(p_string == null ? "" : p_string);
		return new AbstractAirsStatusMessage(l_messageTokens, p_exception);
	}

	private void loadMessage(String[] p_messageTokens) {
		int l_fieldOffset = 0;
		try {
			f_id = Integer.parseInt(p_messageTokens[INDEXID]);
		} catch (final NumberFormatException e) {
			l_fieldOffset = -1;
		}
		f_message = getFieldValue(p_messageTokens, INDEXMESSAGE + l_fieldOffset);
		f_description = getFieldValue(p_messageTokens, INDEXDESCIPTION + l_fieldOffset);
		f_explanation = getFieldValue(p_messageTokens, INDEXEXPLANATION + l_fieldOffset);
		f_action = getFieldValue(p_messageTokens, INDEXACTION + l_fieldOffset);
		f_support = getFieldValue(p_messageTokens, INDEXSUPPORT + l_fieldOffset);
	}

	private String getFieldValue(String[] p_messageTokens, int p_i) {
		return p_messageTokens.length > p_i ? p_messageTokens[p_i] : "";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.airsltd.core.IAirsStatusMessage#toMessage()
	 */
	@Override
	public String toString() {
		return f_id + ":" + escaped(f_message) + ":" + escaped(f_description) + ":" + escaped(f_explanation) + ":"
				+ escaped(f_action) + ":" + escaped(f_support);
	}

	private String escaped(String p_explanation) {
		return p_explanation.replaceAll("%", "%%").replaceAll(":", "%:");
	}

	@Override
	public String niceString() {
		return "[" + f_id + "] " + f_message + " - " + f_description;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.airsltd.core.IAirsStatusMessage#getId()
	 */
	@Override
	public long getId() {
		return f_id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.airsltd.core.IAirsStatusMessage#toDescription()
	 */
	@Override
	public String toDescription() {
		return f_description;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.airsltd.core.IAirsStatusMessage#toExplanation()
	 */
	@Override
	public String toExplanation() {
		return f_explanation;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.airsltd.core.IAirsStatusMessage#toAction()
	 */
	@Override
	public String toAction() {
		return f_action;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.airsltd.core.IAirsStatusMessage#isSupport()
	 */
	@Override
	public boolean isSupport() {
		return !f_explanation.isEmpty() || !f_action.isEmpty() || !f_support.isEmpty();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.airsltd.core.IAirsStatusMessage#toSupport()
	 */
	@Override
	public String toSupport() {
		return f_support;
	}

	@Override
	public String toMessage() {
		return f_message;
	}

}
