/**
 * Copyright (c) 2012, Jon Boley
 * 432 NE Ravenna Blvd
 * Seattle, WA 98115
 */
package com.airsltd.core;

/**
 * A status message for Airs code will have the following format:
 *
 * [id]text[explanation]action[support]
 * 
 * @author Jon Boley
 *
 */
public interface IAirsStatusMessage {

	/**
	 * return the full message as a text
	 *
	 * @return
	 */
	String toMessage();

	/**
	 * return the unique ID for the message
	 *
	 * @return
	 */
	long getId();

	/**
	 * return the description of the message
	 *
	 * @return
	 */
	String toDescription();

	/**
	 * return the explanation of the message
	 *
	 * @return
	 */
	String toExplanation();

	/**
	 * return the suggest action for the message
	 *
	 * @return
	 */
	String toAction();

	/**
	 * returns true if this message has a support component
	 *
	 * @return
	 */
	boolean isSupport();

	/**
	 * returns the support message associated with this component
	 *
	 * @return
	 */
	String toSupport();
	
	/**
	 * create a string designed to be read by users
	 * 
	 * @return
	 */
	String niceString();
	

}
