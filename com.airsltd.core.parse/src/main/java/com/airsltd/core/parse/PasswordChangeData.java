/**
 * Copyright (c) 2012, Jon Boley
 * 432 NE Ravenna Blvd
 * Seattle, WA 98115
 */
package com.airsltd.core.parse;

import java.util.ArrayList;
import java.util.List;

import com.airsltd.core.EAirsErrorMessages;import com.airsltd.core.IAirsStatusMessage;
import com.airsltd.core.IPasswordChangeData;

/**
 * This object contains the data transmitted to and form the
 * PasswordChangeDialog
 *
 * @author Jon
 *
 */
public class PasswordChangeData implements IPasswordChangeData {

	private static final int MINPASSWORDLENGTH = 7;
	private String f_user;
	private String f_oldPassword;
	private String f_newPassword;
	private String f_verifiedPassword;

	public PasswordChangeData(String p_user) {
		super();
		f_user = p_user;
	}

	/**
	 * @return the user
	 */
	public String getUser() {
		return f_user;
	}

	/**
	 * @param p_user
	 *            the user to set
	 */
	public void setUser(String p_user) {
		f_user = p_user;
	}

	/**
	 * @return the oldPassword
	 */
	public String getOldPassword() {
		return f_oldPassword;
	}

	/**
	 * @param p_oldPassword
	 *            the oldPassword to set
	 */
	@Override
	public void setOldPassword(String p_oldPassword) {
		f_oldPassword = p_oldPassword;
	}

	/**
	 * @return the newPassword
	 */
	public String getNewPassword() {
		return f_newPassword;
	}

	/**
	 * @param p_newPassword
	 *            the newPassword to set
	 */
	@Override
	public void setNewPassword(String p_newPassword) {
		f_newPassword = p_newPassword;
	}

	/**
	 * @return the verifiedPassword
	 */
	public String getVerifiedPassword() {
		return f_verifiedPassword;
	}

	/**
	 * @param p_verifiedPassword
	 *            the verifiedPassword to set
	 */
	@Override
	public void setVerifiedPassword(String p_verifiedPassword) {
		f_verifiedPassword = p_verifiedPassword;
	}

	@Override
	public List<IAirsStatusMessage> validInput() {
		final List<IAirsStatusMessage> l_retVal = new ArrayList<IAirsStatusMessage>();
		if (f_newPassword == null || f_newPassword.length() < MINPASSWORDLENGTH) {
			l_retVal.add(AbstractAirsStatusMessage
					.parseString(EAirsErrorMessages.PASSWORDINVALIDLENGTHID.getDescription(), null));
		}
		if (f_newPassword == null ? f_verifiedPassword != null : !f_newPassword.equals(f_verifiedPassword)) {
			l_retVal.add(AbstractAirsStatusMessage
					.parseString(EAirsErrorMessages.PASSWORDSDONOTMATCHID.getDescription(), null));
		}
		return l_retVal.isEmpty() ? null : l_retVal;
	}

}
