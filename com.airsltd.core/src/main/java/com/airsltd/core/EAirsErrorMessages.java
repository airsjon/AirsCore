/**
 * Copyright (c) 2013 Jon Boley
 */
package com.airsltd.core;

/**
 * @author Jon Boley
 *
 */
public enum EAirsErrorMessages {

	PASSWORDINVALIDLENGTHID(
			"1000:Invalid Password:Your new password must be at least 7 characters long"), PASSWORDSDONOTMATCHID(
					"1001:Password Input Error:Your new password does not match your verified password");

	private final String f_description;
	private static final int ERRORIDSTART = 1000;

	private EAirsErrorMessages(String p_description) {
		f_description = p_description;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return ERRORIDSTART + ordinal();
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return f_description;
	}

}
