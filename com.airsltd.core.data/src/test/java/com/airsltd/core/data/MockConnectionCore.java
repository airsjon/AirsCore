/**
 * 
 */
package com.airsltd.core.data;

import java.sql.Connection;
import java.text.DateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import org.apache.commons.logging.Log;

import com.airsltd.core.IAirsUserData;
import com.airsltd.core.ICoreInterface;
import com.airsltd.core.IExceptionCollector;
import com.airsltd.core.NotificationStatus;

/**
 * @author Jon Boley
 *
 */
public class MockConnectionCore implements ICoreInterface {

	private Map<String, Object> l_properties = new HashMap<String, Object>();
	private Connection f_connection;
	private IAirsUserData f_userData;
	private Stack<Tuple3<String,String,String>> f_logData = new Stack<Tuple3<String,String,String>>();
	
	/* (non-Javadoc)
	 * @see com.airsltd.core.ICoreInterface#traceActive(java.lang.Class, java.lang.String)
	 */
	@Override
	public boolean traceActive(Class<?> p_class, String p_traceId) {
		return false;
	}

	/* (non-Javadoc)
	 * @see com.airsltd.core.ICoreInterface#handleException(java.lang.String, java.lang.Throwable, com.airsltd.core.NotificationStatus)
	 */
	@Override
	public boolean handleException(String p_message, Throwable p_e, NotificationStatus p_s) {
		RuntimeException l_re = new RuntimeException(p_e);
		throw l_re;
	}

	/* (non-Javadoc)
	 * @see com.airsltd.core.ICoreInterface#loadProperty(java.lang.Object, java.lang.String, java.lang.Object)
	 */
	@Override
	public void loadProperty(Object p_source, String p_property, Object p_value) {
		l_properties.put(p_property, p_value);
	}

	/* (non-Javadoc)
	 * @see com.airsltd.core.ICoreInterface#getProperty(java.lang.Object, java.lang.String)
	 */
	@Override
	public Object getProperty(Object p_source, String p_property) {
		return l_properties.get(p_property);
	}

	/* (non-Javadoc)
	 * @see com.airsltd.core.ICoreInterface#isProperty(java.lang.String)
	 */
	@Override
	public boolean isProperty(String p_property) {
		return l_properties.containsKey(p_property) && l_properties.get(p_property)!=Boolean.FALSE;
	}

	/* (non-Javadoc)
	 * @see com.airsltd.core.ICoreInterface#getPersistentProperty(java.lang.String, java.lang.String)
	 */
	@Override
	public String getPersistentProperty(String p_property, String p_defValue) {
		return isProperty(p_property)?getProperty(null, p_property).toString():p_defValue;
	}

	/* (non-Javadoc)
	 * @see com.airsltd.core.ICoreInterface#setPersistentProperty(java.lang.String, java.lang.String)
	 */
	@Override
	public void setPersistentProperty(String p_property, String p_value) {
		loadProperty(null, p_property, p_value);
	}

	/* (non-Javadoc)
	 * @see com.airsltd.core.ICoreInterface#getSecurePersistentProperty(java.lang.String, java.lang.String)
	 */
	@Override
	public String getSecurePersistentProperty(String p_property, String p_defValue) {
		return getPersistentProperty(p_property, p_defValue);
	}

	/* (non-Javadoc)
	 * @see com.airsltd.core.ICoreInterface#setSecurePersistentProperty(java.lang.String, java.lang.String)
	 */
	@Override
	public void setSecurePersistentProperty(String p_property, String p_value) {
		loadProperty(null, p_property, p_value);
	}

	/* (non-Javadoc)
	 * @see com.airsltd.core.ICoreInterface#getApplication()
	 */
	@Override
	public Object getApplication() {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.airsltd.core.ICoreInterface#productName()
	 */
	@Override
	public String productName() {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.airsltd.core.ICoreInterface#getConnection()
	 */
	@Override
	public Connection getConnection() {
		return f_connection;
	}
	
	public void setConnection(Connection p_connection) {
		f_connection = p_connection;
	}

	/* (non-Javadoc)
	 * @see com.airsltd.core.ICoreInterface#playSound(java.lang.String, java.lang.String)
	 */
	@Override
	public void playSound(String p_soundGroup, String p_soundIndex) {
	}

	/* (non-Javadoc)
	 * @see com.airsltd.core.ICoreInterface#getDateFormat()
	 */
	@Override
	public DateFormat getDateFormat() {
		return DateFormat.getDateTimeInstance();
	}

	/* (non-Javadoc)
	 * @see com.airsltd.core.ICoreInterface#getUserData()
	 */
	@Override
	public IAirsUserData getUserData() {
		return f_userData;
	}
	
	public void setUserData(IAirsUserData p_userData) {
		f_userData = p_userData;
	}

	/* (non-Javadoc)
	 * @see com.airsltd.core.ICoreInterface#isPersistentProperty(java.lang.String)
	 */
	@Override
	public boolean isPersistentProperty(String p_string) {
		return false;
	}

	/* (non-Javadoc)
	 * @see com.airsltd.core.ICoreInterface#pushExceptionCollector(com.airsltd.core.IExceptionCollector)
	 */
	@Override
	public void pushExceptionCollector(IExceptionCollector<?> p_exceptionCollector) {
	}

	/* (non-Javadoc)
	 * @see com.airsltd.core.ICoreInterface#popExceptionCollector()
	 */
	@Override
	public IExceptionCollector<?> popExceptionCollector() {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.airsltd.core.ICoreInterface#trace(java.lang.Class, java.lang.String, java.lang.String)
	 */
	@Override
	public void trace(Class<?> p_class, String p_ident, String p_string) {
		f_logData.push(new Tuple3<String, String, String>("Trace", p_ident, p_string));
	}

	/* (non-Javadoc)
	 * @see com.airsltd.core.ICoreInterface#debug(java.lang.Class, java.lang.String, java.lang.String)
	 */
	@Override
	public void debug(Class<?> p_class, String p_ident, String p_string) {
		f_logData.push(new Tuple3<String, String, String>("Debug", p_ident, p_string));
	}
	
	/**
	 * Clear the log data.
	 */
	public void clearLogData() {
		f_logData.clear();
	}
	
	/**
	 * Return the top Tuple from the log data.
	 * 
	 * trace and debug push a Tuple3 unto the log data for analysis.
	 * @return
	 */
	public Tuple3<String, String, String> popLogData() {
		return f_logData.pop();
	}

	/* (non-Javadoc)
	 * @see com.airsltd.core.ICoreInterface#worked(int)
	 */
	@Override
	public void worked(int p_increment) {
	}

	/* (non-Javadoc)
	 * @see com.airsltd.core.ICoreInterface#subTask(java.lang.String)
	 */
	@Override
	public void subTask(String p_taskName) {
	}

	/* (non-Javadoc)
	 * @see com.airsltd.core.ICoreInterface#beginTask(java.lang.String, int)
	 */
	@Override
	public void beginTask(String p_taskName, int p_taskSize) {
	}

	/* (non-Javadoc)
	 * @see com.airsltd.core.ICoreInterface#setLog(org.apache.commons.logging.Log)
	 */
	@Override
	public void setLog(Log p_mockLog) {
	}

	/* (non-Javadoc)
	 * @see com.airsltd.core.ICoreInterface#getLog()
	 */
	@Override
	public Log getLog() {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.airsltd.core.ICoreInterface#logTimedEventStart(boolean, java.lang.String)
	 */
	@Override
	public Long logTimedEventStart(boolean p_b, String p_string) {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.airsltd.core.ICoreInterface#logTimedEventEnd(boolean, java.lang.String, long)
	 */
	@Override
	public Long logTimedEventEnd(boolean p_b, String p_string, long p_startTime) {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.airsltd.core.ICoreInterface#updatePassword(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean updatePassword(String p_student, String p_string, String p_string2) {
		return false;
	}

	/* (non-Javadoc)
	 * @see com.airsltd.core.ICoreInterface#convertPassword(java.lang.String)
	 */
	@Override
	public String convertPassword(String p_object) {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.airsltd.core.ICoreInterface#applicationRoot()
	 */
	@Override
	public String applicationRoot() {
		return null;
	}

}
