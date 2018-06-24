/**
 * Copyright (c) 2012, Jon Boley
 * 432 NE Ravenna Blvd
 * Seattle, WA 98115
 */
package com.airsltd.core;

/**
 * This represents the data for the current logged on user. Closely related to
 * {@link IAirsRegisteredUser}, the main difference is that
 * {@link IAirsRegisteredUser} stores the persistent information related to the
 * user while IAirsUserData provides access to data related to the current
 * logged on user.
 *
 * @author Jon
 *
 */
public interface IAirsUserData extends IUserIdProvider {

	/**
	 * Save the password in a local persistent store
	 *
	 * @return true if the user would like to save their password for future log
	 *         ons.
	 */
	boolean isSavePassword();

	/**
	 * When starting attempt to login with current credentials
	 *
	 * @return true if the user would like to automatically log in when the
	 *         product starts.
	 */
	boolean isAutoLogin();

	/**
	 * returns the password that the user specified when logging on. If
	 * <code>savePassword</code> is set this can be a password that was
	 * specified in the past
	 *
	 * @return the encoded password as a <code>String</code>
	 */
	String getPassword();

	/**
	 * set the user name to <code>userName</code>
	 * 
	 * @param userName
	 *            String representing the logon name of the user
	 */
	void setUserName(String userName);

	/**
	 * modify the password for the current user. If the password is
	 * <code>null</code> then the password is not modified.
	 *
	 * @param password
	 *            plain text <code>String</code> representation of the password
	 *            for the current account. The call Implementing this Interface
	 *            must encode the password before saving it.
	 */
	void setPassword(String password);

	/**
	 * set the state of the user to <code>loggedIn</code>
	 *
	 * @param loggedIn
	 *            <code>boolean</code> representing if the user is logged in
	 *            (true) or off (false)
	 */
	void setSavePassword(boolean savePassword);

	/**
	 * is the user logged in
	 * 
	 * @return
	 */
	boolean isLoggedIn();

	/**
	 * set the logged in state of the user to <code>loggedIn</code>
	 *
	 * @param loggedIn
	 *            <code>boolean</code> representing if the user is logged in
	 *            (true) or off (false)
	 */
	void setLoggedIn(boolean loggedIn);

	boolean logOut();

	boolean logIn();

	void setAutoLogin(boolean p_selection);

	/**
	 * is the current user an admin
	 * 
	 * @return true if the current user is an admin
	 */
	boolean isAdmin();

	/**
	 * return application specific data for the current logged in user
	 * 
	 * @return
	 */
	Object getLogSession();

	/**
	 * Attempt to log in via an interactive method
	 *
	 * @return
	 */
	LoginStatus logInInteractive();

	/**
	 * Register a new user
	 *
	 * @param p_arg0
	 * @return
	 */
	boolean register(IRegistrationData p_arg0);

	/**
	 * Return the registration data for this user
	 *
	 * @return
	 */
	IRegistrationData getRegistrationData();

	/**
	 * Establish a connection between the UserData and the Logger (where the log
	 * on/off methods reside.)
	 *
	 */
	void setLifeCycle(IAirsLogger p_lifeCycle);

	/**
	 * get the id of the current user
	 *
	 * @return the <code>int</code> value of the current user
	 */
	long getId();

	/**
	 * Set the current users student id.
	 *
	 * @param studentId
	 */
	void setId(long studentId);

}
