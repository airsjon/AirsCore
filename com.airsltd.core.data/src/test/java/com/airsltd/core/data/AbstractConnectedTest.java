/**
 * (c) 2015, Jon Boley 2703 Wildrose Ct, Bellingham WA 98229
 */
package com.airsltd.core.data;

import java.sql.Connection;

import com.airsltd.core.NotificationStatus;
import com.airsltd.core.data.CoreInterface;

/**
 * @author Jon Boley
 *
 */
public abstract class AbstractConnectedTest {

	public void setUp() throws Exception {
		
		new CoreInterface() {
			@Override
			public Connection getConnection() {
				return getTestConnection();
			}

			/* (non-Javadoc)
			 * @see com.airsltd.core.CoreInterface#lastChanceHandle(java.lang.String, java.lang.Throwable, com.airsltd.core.NotificationStatus)
			 */
			@Override
			protected boolean lastChanceHandle(String p_message, Throwable p_e,
					NotificationStatus p_s) {
				RuntimeException l_retVal = new RuntimeException(p_message, p_e);
				if (p_e != null) {
					p_e.printStackTrace();
				} else {
					l_retVal.printStackTrace();
				}
				throw l_retVal;
			}

		};
		
	}

	protected abstract Connection getTestConnection();

}
