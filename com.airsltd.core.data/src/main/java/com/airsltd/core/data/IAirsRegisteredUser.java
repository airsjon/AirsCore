/**
 * Copyright (c) 2013 Jon Boley
 */
package com.airsltd.core.data;

import com.airsltd.core.IUserIdProvider;
import com.airsltd.core.RegisterState;

/**
 * @author Jon Boley
 *
 */
public interface IAirsRegisteredUser extends INamed, IBlockData, IUserIdProvider {

	String getTokenVerify();

	String getBestName();

	void setVerified(boolean p_b);

	void setTokenVerify(String p_string);

	void setPasswordClear(String p_convertPassword);

	boolean isActive();

	boolean isBanned();

	String getEmail();

	void setResetMailed(boolean p_b);

	boolean isExpired();

	RegisterState getCurrentState();

	boolean isResetMailed();

	int getUserId();

}
