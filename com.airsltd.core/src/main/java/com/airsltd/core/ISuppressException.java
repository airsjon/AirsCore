/**
 *
 */
package com.airsltd.core;

/**
 * Interface defining a method to suppress a Throwable.
 *
 * @author Jon Boley
 *
 */
public interface ISuppressException {

	/**
	 * Return the new Throwable that is suppressing p_throwable.
	 *
	 * @param p_message
	 *            can be null, a message to give meaning to the suppression (or
	 *            the real issue being generated)
	 * @param p_throwable
	 *            not null, the Throwable that is being suppressed
	 * @param p_status
	 *            an integer that gives hints to the Status Reporter on how to
	 *            behave
	 * @return a new Throwable that is suppressing the original.
	 * @see NotificationStatus
	 */
	Throwable suppress(String p_message, Throwable p_throwable, int p_status);

}
