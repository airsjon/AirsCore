/**
 * Copyright (c) 2012, Jon Boley
 * 432 NE Ravenna Blvd
 * Seattle, WA 98115
 */
package com.airsltd.core;

/**
 * Airs Servers that allow registration use the {@link IRegistrationData}
 * contract to provide a consistent method for registering new users.
 *
 * @author Jon Boley
 *
 */
public interface IRegistrationData {

	/**
	 * return a list of data that needs to be passed for the purposes of
	 * registration. This list must be in the order that is to be displayed in
	 * the registration dialog box
	 *
	 * @return
	 */
	String[] getNames();

	/**
	 * Determine if the current Registration data is valid and can be submitted
	 * to the server
	 * 
	 * @return
	 */
	boolean valid();

	/**
	 * Return the value store for the field <code>p_name</code>
	 * 
	 * @param p_name
	 * @return
	 */
	String getValue(String p_name);

	/**
	 * Return <code>true</code> if the data field <code>p_name</code> is a
	 * password field
	 *
	 * @param p_name
	 * @return
	 */
	boolean isPassword(String p_name);

	/**
	 * Return <code>true</code> if the data field <code>p_name</code> is
	 * optional
	 *
	 * @param p_name
	 * @return
	 */
	boolean isOptional(String p_name);

	/**
	 * Set the data for field <code>p_name</code> to <code>p_text</code>
	 *
	 * @param p_name
	 * @param p_text
	 */
	void setValue(String p_name, String p_text);

	/**
	 * Return a {@link String} to be displayed as the title for this
	 * Registration data
	 *
	 * @return
	 */
	String getRegistrationTitle();

}
