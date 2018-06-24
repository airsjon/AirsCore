/**
 *
 */
package com.airsltd.core.data;

import java.util.concurrent.Callable;

import com.airsltd.core.IExceptionCollector;
import com.airsltd.core.NotificationStatus;
import com.airsltd.core.data.CoreInterface;

/**
 * An exception collector provides a mechanism for collecting errors at a known
 * point in the program and resuming program control there.
 * <p>
 * During a throw the CoreInterface exception handler will look on it's stack
 * for collectors. Each collector will have it's collect method called. If this
 * method returns true, the Collector has agreed to take control of the program.
 * The collector can also ask that the exception be logged (the default action)
 * be setting the f_logException to true.
 * <p>
 * To facilitate taking control of the program the following code (or a similiar
 * variant) should be used.
 * <p>
 * 
 * <pre>
 * <code>try (ExceptionCollection<T> l_excpetion = new ExceptionCollector<T>()) {
 *   T l_retVal = l_exception.call( -- code to be executed -- , -- fail value -- );
 * }
 * </code>
 * </pre>
 *
 *
 * @author Jon Boley
 *
 */
public abstract class ExceptionCollector<T> implements AutoCloseable, IExceptionCollector<T> {

	/**
	 * A description of the action being performed when the Collector is set up.
	 * This description will be folded into the message that is eventually
	 * returned to aid in debugging.
	 */
	private String f_description;
	/**
	 * When an exception is caught by this {@link ExceptionCollector}, the error
	 * handling system can use this value to determine if the exception is
	 * logged.
	 */
	private boolean f_logException = true;

	/**
	 * Create the ExceptionCollector and place it on the CoreSystem's exception
	 * handler stack.
	 */
	public ExceptionCollector(String p_description) {
		f_description = p_description;
		CoreInterface.getSystem().pushExceptionCollector(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.AutoCloseable#close()
	 */
	/**
	 * Pop the exception handler off the stack and check if an existing handled
	 * condition is still active.
	 * <p>
	 * The collector can handle the error by setting f_collected to true. A
	 * handled error will most likely note a logging of the issue and resumption
	 * of the program.
	 * 
	 */
	@Override
	public void close() {
		CoreInterface.getSystem().popExceptionCollector();
	}

	/* (non-Javadoc)
	 * @see com.airsltd.core.IExtractInterface#collect(java.lang.String, java.lang.Throwable, com.airsltd.core.NotificationStatus)
	 */
	@Override
	public abstract boolean collect(String p_message, Throwable p_e, NotificationStatus p_s);

	/* (non-Javadoc)
	 * @see com.airsltd.core.IExtractInterface#getDescription()
	 */
	@Override
	public String getDescription() {
		return f_description;
	}

	/* (non-Javadoc)
	 * @see com.airsltd.core.IExtractInterface#setDescription(java.lang.String)
	 */
	@Override
	public void setDescription(String p_description) {
		f_description = p_description;
	}

	/* (non-Javadoc)
	 * @see com.airsltd.core.IExtractInterface#isLogException()
	 */
	@Override
	public boolean isLogException() {
		return f_logException;
	}

	/* (non-Javadoc)
	 * @see com.airsltd.core.IExtractInterface#setLogException(boolean)
	 */
	@Override
	public void setLogException(boolean p_logException) {
		f_logException = p_logException;
	}

	/* (non-Javadoc)
	 * @see com.airsltd.core.IExtractInterface#call(java.util.concurrent.Callable, T)
	 */
	@Override
	public T call(Callable<T> p_runnable, T p_fail) {
		T l_retVal = p_fail;
		try {
			l_retVal = p_runnable.call();
		} catch (final PeelExecutionException l_pe) {
			if (l_pe.getCollector() != this) {
				throw l_pe;
			}
		} catch (final Exception l_exception) {
			CoreInterface.getSystem().handleException("Unexpected Error", l_exception, NotificationStatus.LOGDEBUG);
		}
		return l_retVal;
	}

}
