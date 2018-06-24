/**
 *
 */
package com.airsltd.core.data;

import com.airsltd.core.NotificationStatus;

/**
 * Exception collector for the BlockProvider class.
 * <p>
 * When executing complex data stores, this can provide the ability to continue
 * processing when exceptions occur. Current implementation is to simply
 * continue processing if f_continueProcessing is set to true.
 * <p>
 * Further enhancement could include checking the type of exception being thrown
 * and then determinging if we continue.
 * 
 * @author Jon Boley
 *
 */
public class BlockProviderCollector extends ExceptionCollector<Boolean> {

	private final boolean f_continueProcessing;

	public BlockProviderCollector(String p_string, boolean p_continueProcessing) {
		super(p_string);
		f_continueProcessing = p_continueProcessing;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.airsltd.core.ExceptionCollector#collect(java.lang.String,
	 * java.lang.Throwable, int)
	 */
	@Override
	public boolean collect(String p_message, Throwable p_e, NotificationStatus p_s) {
		return f_continueProcessing;
	}

}
