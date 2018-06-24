/**
 *
 */
package com.airsltd.core;

/**
 * {@link Enum} representing the state of the current login attempt.
 *
 * @author Jon Boley
 *
 */
public enum LoginStatus {
	/**
	 * The server has been logged on to correctly.
	 */
	SUCCESSFUL, 
	/**
	 * The user has canceled the current attempt to log on. It is up
	 * to the server to decide if that user can have minimized
	 * access to the server.
	 */
	CANCELED, 
	/**
	 * The user was unable to log on to the server (invalid
	 * user/incorrect password).
	 */
	FAIL;

	/**
	 * Default field name for user name data
	 */
	public static final String USERNAME = "User";
	/**
	 * Default field name for password data
	 */
	public static final String PASSWORD = "Password";
	/**
	 * Default field name for password verify data
	 */
	public static final String PASSWORDVERIFY = "PasswordVerify";
	/**
	 * Default field name for email data
	 */
	public static final String EMAIL = "Email";

}
