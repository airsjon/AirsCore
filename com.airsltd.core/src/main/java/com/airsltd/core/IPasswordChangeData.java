/**
 * Copyright (c) 2012, Jon Boley
 * 432 NE Ravenna Blvd
 * Seattle, WA 98115
 */
package com.airsltd.core;

import java.util.List;

/**
 * Classes that implement this interface must be able to store a new password,
 * its verification, and the old password. Also, the class must be able to tell
 * if the new password and old password are valid.
 *
 * @author Jon Boley
 *
 */
public interface IPasswordChangeData {
	/**
	 * The new password that will be set for the account
	 *
	 * @param p_password
	 */
	void setNewPassword(String p_password);

	/**
	 * The old password that was set for the account. Used to verify that the
	 * individual trying to change the password has the authority (based on
	 * their knowledge of the previous password).
	 *
	 * @param p_password
	 */
	void setOldPassword(String p_password);

	/**
	 * The verification input entered by the user. This value should be
	 * <code>equal</code> to the new password.
	 * 
	 * @param p_password
	 */
	void setVerifiedPassword(String p_password);

	/**
	 * Determine if the values for new password, old password and verification
	 * represent a valid password change set.
	 * 
	 * @return a list of all the reasons the password change was invalid. If
	 *         valid return null
	 */
	List<IAirsStatusMessage> validInput();
}
