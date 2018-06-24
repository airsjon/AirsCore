/**
 *
 */
package com.airsltd.core;

/**
 * An interface to allow handling of internal exceptions that are going to be
 * dealt with, but might be of interest to the calling code
 *
 * @author Jon Boley
 *
 */
public interface ExceptionHandler {

	/**
	 * Without a handler, the code will dump a stack trace to the console.
	 *
	 * @param e
	 *            Exception that was thrown
	 */
	void handle(Exception e);

}
