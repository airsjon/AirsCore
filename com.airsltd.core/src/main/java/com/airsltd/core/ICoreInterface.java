/**
 *
 */
package com.airsltd.core;

import java.sql.Connection;
import java.text.DateFormat;

import org.apache.commons.logging.Log;

/**
 * The Core system interface for AIRS Applications. Provides an exception
 * handler, trace set test and property management
 *
 * @author Jon
 *
 */
public interface ICoreInterface {

	/**
	 * Determine if the User has select to trace <code>traceId</code> for
	 * {@link Class} p_class
	 *
	 * @param p_class
	 * @param p_traceId
	 * @return
	 */
	boolean traceActive(Class<?> p_class, String p_traceId);

	/**
	 * System hook for handling exceptions.
	 * <p>
	 * If p_status has {@link NotificationStatus#BLOCKBIT} set, the handler must
	 * not return. A return value of false, means that the handler was unable to
	 * deal with the exception.
	 *
	 * @param p_message
	 *            can be null, a String value describing the reason for the
	 *            throw
	 * @param p_e
	 *            can be null, exception to be handled
	 * @param p_s
	 *            can be null, suggestion on how the exception should be
	 *            notified
	 * @return true if the exception was handled.
	 */
	boolean handleException(String p_message, Throwable p_e, NotificationStatus p_s);

	/**
	 * System property can be modified with this call.
	 *
	 * @param source
	 * @param property
	 * @param value
	 */
	void loadProperty(Object source, String property, Object value);

	/**
	 * Retrieve the system property's value
	 *
	 * @param p_source  can be null, the source of the property
	 * @param p_property  not null, the name of the property being loaded
	 * @return
	 */
	Object getProperty(Object p_source, String p_property);

	/**
	 * Retrieve the system property's boolean state (not null and not empty)
	 *
	 * @param source
	 * @param property
	 * @return
	 */

	boolean isProperty(String property);

	/**
	 * Get a persistent property.
	 *
	 * @param property
	 *            not null, the property to retrieve
	 * @param defValue
	 *            not null, the default value to be used
	 * @return the property as a String.
	 */
	String getPersistentProperty(String property, String defValue);

	/**
	 * Set a persistent proerty.
	 *
	 * @param property
	 * @param value
	 */
	void setPersistentProperty(String property, String value);

	/**
	 * Get a secure property.
	 *
	 * @param property
	 * @param defValue
	 * @return
	 */
	String getSecurePersistentProperty(String property, String defValue);

	/**
	 * Set a secure property.
	 *
	 * @param property
	 * @param value
	 */
	void setSecurePersistentProperty(String property, String value);

	/**
	 * Get an object that represents the current application.
	 *
	 * @return
	 */
	Object getApplication();

	/**
	 * Return a Product Name for the current product. Implementor(s) may want to
	 * include the version of the Product.
	 *
	 * @return
	 */
	String productName();

	/**
	 * Get the {@link AirsConnection} for SQL i/o
	 *
	 * @return
	 */
	Connection getConnection();

	/**
	 * Tell the interface to play a sound. If an invalid sound is requested
	 * there should be no error generated, however a debug in the log file
	 * should be generated.
	 *
	 * @param p_soundGroup
	 * @param p_soundIndex
	 */
	void playSound(String p_soundGroup, String p_soundIndex);

	/**
	 * get the Date format for displaying dates
	 * 
	 * @return
	 */
	DateFormat getDateFormat();

	/**
	 * return the User Data associated with the current user. This interface
	 * also provides methods for logging in/off and registering to the server
	 *
	 * @return
	 */
	IAirsUserData getUserData();

	/**
	 * return true/false for the persistent property p_string (assumes this
	 * property is a boolean value)
	 * 
	 * @param p_string
	 * @return
	 */
	boolean isPersistentProperty(String p_string);

	/**
	 * Place an IExceptionCollector onto the Exception handling stack.
	 * <p>
	 * When Exceptions are thrown, the {@link CoreInterface} system will check
	 * it's stack to see if a collector can handle it.
	 *
	 * @param p_exceptionCollector
	 * @see ExceptionCollector
	 */
	void pushExceptionCollector(IExceptionCollector<?> p_exceptionCollector);

	/**
	 * Remove an ExceptionCollector from the Exception handling stack.
	 * <p>
	 * This collector is no longer needed and should not interrupt any
	 * exceptions being thrown.
	 * 
	 * @return the ExceptionCollector being removed.
	 */
	IExceptionCollector<?> popExceptionCollector();

	/**
	 * Log a trace on class p_class, group p_ident, of value p_string.
	 * <p>
	 * 
	 * @param p_class
	 * @param p_ident
	 * @param p_string
	 */
	void trace(Class<?> p_class, String p_ident, String p_string);

	/**
	 * Log a debug on class p_class, group p_ident, of value p_string.
	 * 
	 * @param p_class
	 * @param p_ident
	 * @param p_string
	 */
	void debug(Class<?> p_class, String p_ident, String p_string);

	/**
	 * The current task being run has done p_increment worth of work.
	 *
	 * @param p_increment
	 */
	void worked(int p_increment);

	/**
	 * Start a sub task p_taskName
	 * 
	 * @param p_taskName
	 */
	void subTask(String p_taskName);

	/**
	 * Start a sub task, p_taskName, with the expected work of p_taskSize
	 * 
	 * @param p_taskName
	 * @param p_taskSize
	 */
	void beginTask(String p_taskName, int p_taskSize);

	/**
	 * Set the logger for the system.
	 * <p>
	 * Primarily used for Unit testing.
	 * 
	 * @param p_mockLog
	 */
	void setLog(Log p_mockLog);

	/**
	 * Get the logger for the system.
	 * <p>
	 * The system logger logs at {@link CoreInterface}. Classes should create
	 * their own loggers for tracing.
	 * 
	 * @return
	 */
	Log getLog();

	/**
	 * Log the start of an event.
	 * <p>
	 * Used to track time usage in traces.
	 * 
	 * @param p_b
	 * @param p_string
	 * @return
	 */
	Long logTimedEventStart(boolean p_b, String p_string);

	/**
	 *
	 * @param p_b
	 * @param p_string
	 * @param p_startTime
	 * @return
	 */
	Long logTimedEventEnd(boolean p_b, String p_string, long p_startTime);

	/**
	 * Update the password for user p_user.
	 *
	 * @param p_user
	 *            not null, the user being updated
	 * @param p_oldPassword
	 *            not null, the old password for the user
	 * @param p_newPassword
	 *            not null, the new password for the user
	 * @return true of the password was updated.
	 */
	boolean updatePassword(String p_student, String p_string, String p_string2);

	/**
	 * Encrypt the password.
	 *
	 * @param p_object
	 *            not null, the non-encrypted password
	 * @return the password in encrypted text.
	 */
	String convertPassword(String p_object);

	/**
	 * A URI for the root directory of the application.
	 * 
	 * @return
	 */
	String applicationRoot();

}
