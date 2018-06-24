/**
 *
 */
package com.airsltd.core;

import com.airsltd.core.IRegistrationData;

/**
 * An interface that defines the methods necessary to instantiate a logging
 * server
 *
 * @author Jon
 *
 */
public interface IAirsLogger {

	/**
	 * Are we logged in
	 *
	 * @return true if the user is logged in
	 */
	boolean isLoggedIn();

	/**
	 * Log the user out
	 *
	 * @return true if the user has been logged out
	 */
	boolean logOut();

	/**
	 * Log the user in
	 *
	 * @return true if the user has logged in successfully
	 */
	boolean logIn();

	/**
	 * return the data object which contains the user data
	 *
	 * @return
	 */
	IAirsUserData getUserData();

	/**
	 * register a new user
	 *
	 * @param p_arg0
	 * @return
	 */
	boolean register(IRegistrationData p_arg0);

}
