/**
 *
 */
package com.airsltd.core.data;

import com.airsltd.core.IExceptionCollector;

/**
 * This exception is used to send execution back to the ExceptionCollector for
 * resumption of the program there.
 *
 * @author Jon Boley
 *
 */
public class PeelExecutionException extends RuntimeException {

	/**
	 *
	 */
	private static final long serialVersionUID = -367006427924885183L;

	private final IExceptionCollector<?> f_collector;

	public PeelExecutionException(IExceptionCollector<?> p_collector) {
		super();
		f_collector = p_collector;
	}

	/**
	 * @return the collector
	 */
	public IExceptionCollector<?> getCollector() {
		return f_collector;
	}

}
