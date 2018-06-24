package com.airsltd.core;

import java.util.concurrent.Callable;

public interface IExceptionCollector<T> {

	/**
	 * Collect an exception.
	 * <p>
	 * The {@link CollectedException} is used to designate that a collected
	 * exception has been thrown and should be dealt with.
	 *
	 * @param p_message
	 *            can be null, a text message that may give some light to the
	 *            issue
	 * @param p_e
	 *            can be null, a {@link Throwable} that is the cause of the
	 *            current exception chain
	 * @param p_s
	 *            a value, {@link NotificationStatus}, giving the error handler
	 *            a hint on what should be done
	 * @return true if the exception has been handled
	 */
	boolean collect(String p_message, Throwable p_e, NotificationStatus p_s);

	/**
	 * @return the description
	 */
	String getDescription();

	/**
	 * @param p_description
	 *            the description to set
	 */
	void setDescription(String p_description);

	/**
	 * @return the logException
	 */
	boolean isLogException();

	/**
	 * @param p_logException
	 *            the logException to set
	 */
	void setLogException(boolean p_logException);

	T call(Callable<T> p_runnable, T p_fail);

}