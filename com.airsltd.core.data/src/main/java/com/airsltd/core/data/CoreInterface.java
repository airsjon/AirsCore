/**

 * Copyright (c) 2012, Jon Boley
 * 432 NE Ravenna Blvd
 * Seattle, WA 98115
 */
package com.airsltd.core.data;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.airsltd.core.IAirsUserData;
import com.airsltd.core.ICoreInterface;
import com.airsltd.core.IExceptionCollector;
import com.airsltd.core.NotificationStatus;

/**
 * @author Jon Boley
 *
 */
public class CoreInterface implements ICoreInterface {

	private static final long ASECONDOFTIME = 1000000;
	private static ICoreInterface s_system;
	public static final String NEWLINE = System.getProperty("line.separator");
	private static String MISSINGMD5ALGORITHM = "257:Missing MD5 Algorithm:Unable to encrypt data:The MD5 Algorithm is part of the Standard Java installation.  This"
			+ " exception should only be thrown if there is either a configuation issue or a problem with your Java Virtual Machine.:"
			+ "Make sure your Java System is 1.7 and a complete install";
	private static final int PASSWORDPADDSIZE = 32;
	private static final int PASSWORDHASHRADIX = 16;
	private static final int PARAMETER2 = 2;
	private static final int PARAMETER3 = 3;
	private static final int PARAMETER1 = 1;

	private final Map<Class<?>, Log> f_log = new HashMap<Class<?>, Log>();

	private final Deque<IExceptionCollector<?>> f_stack = new ArrayDeque<IExceptionCollector<?>>();
	private IAirsUserData f_userData;

	public CoreInterface() {
		setSystem(this);
	}

	/**
	 * @return the system
	 */
	public static ICoreInterface getSystem() {
		if (s_system == null) {
			new CoreInterface();
		}
		return s_system;
	}

	/**
	 * @param p_system
	 *            the system to set
	 */
	public static void setSystem(ICoreInterface p_system) {
		s_system = p_system;
	}

	@Override
	public boolean handleException(String p_message, Throwable p_e, NotificationStatus p_s) {
		boolean l_retVal = false;
		if (!f_stack.isEmpty()) {
			for (final IExceptionCollector<?> l_handler : f_stack) {
				l_retVal = l_handler.collect(p_message, p_e, p_s);
				if (l_retVal) {
					peelExecutionStack(l_handler, p_e);
				}
			}
		}
		if (!l_retVal) {
			l_retVal = lastChanceHandle(p_message, p_e, p_s);
		}
		return l_retVal;
	}

	protected void peelExecutionStack(IExceptionCollector<?> p_handler, Throwable p_e) {
		if (p_handler.isLogException()) {
			getLog().info(p_handler.getDescription(), p_e);
		}
		throw new PeelExecutionException(p_handler);
	}

	protected boolean lastChanceHandle(String p_message, Throwable p_e, NotificationStatus p_s) {
		getLog().error(p_message, p_e);
		return true;
	}

	/**
	 * @return the log
	 */
	public Log getLog(Class<?> p_class) {
		Log l_retVal = f_log.get(p_class);
		if (l_retVal == null) {
			l_retVal = LogFactory.getLog(p_class);
			f_log.put(p_class, l_retVal);
		}
		return l_retVal;
	}

	@Override
	public Long logTimedEventStart(boolean p_warn, String p_startText) {
		Long l_retVal = null;
		final Log l_log = getLog(CoreInterface.class);
		if (p_warn && l_log.isWarnEnabled() || l_log.isTraceEnabled()) {
			final long cTime = System.nanoTime();
			if (p_startText != null && !p_startText.isEmpty()) {
				final String l_startText = String.format("@%,d: %s", cTime / ASECONDOFTIME, p_startText);
				if (p_warn) {
					l_log.warn(l_startText);
				} else {
					l_log.trace(l_startText);
				}
			}
			l_retVal = cTime;
		}
		return l_retVal;
	}

	@Override
	public Long logTimedEventEnd(boolean p_warn, String p_endText, long p_startTime) {
		long rVal = -1;
		final Log l_log = getLog(CoreInterface.class);
		if (p_warn && l_log.isWarnEnabled() || l_log.isTraceEnabled()) {
			final long l_endTime = System.nanoTime();
			if (p_endText != null && !p_endText.isEmpty()) {
				final long l_elapsedTime = l_endTime - p_startTime;
				final String l_startText = String.format("@%,d: %s [%,d elapsed]", l_endTime / ASECONDOFTIME, p_endText,
						l_elapsedTime / ASECONDOFTIME);
				if (p_warn) {
					l_log.warn(l_startText);
				} else {
					l_log.trace(l_startText);
				}
				rVal = l_elapsedTime / ASECONDOFTIME;
			}
		}
		return rVal;
	}

	/**
	 * This static method provides a means for updating the password for a
	 * database system with users. Notice the database that is connected must
	 * respond to the function changePassword with the UserName (id), new
	 * password and the old password
	 *
	 * @param p_student
	 * @param p_nPassword
	 * @param p_oPassword
	 * @return
	 */
	@Override
	public boolean updatePassword(String p_student, String p_nPassword, String p_oPassword) {
		boolean rVal = false;
		final String oldEncode = convertPassword(p_oPassword);
		final String newEncode = convertPassword(p_nPassword);
		try (Connection ac = getSystem().getConnection();
				PreparedStatement l_ps = ac.prepareStatement("SELECT changePassword(?,?,?);")) {
			l_ps.setString(PARAMETER1, p_student);
			l_ps.setString(PARAMETER2, newEncode);
			l_ps.setString(PARAMETER3, oldEncode);
			final ResultSet l_rs = l_ps.executeQuery();
			while (l_rs.next()) {
				rVal = l_rs.getInt(1) != 0;
				break;
			}
		} catch (final SQLException e) {
			handleException("Unable to change Password due to Connection", e, NotificationStatus.BLOCK);
		}
		return rVal;
	}

	@Override
	public String convertPassword(String p_password) {
		String hashtext = "";
		if (p_password != null && !p_password.isEmpty()) {
			MessageDigest m;
			try {
				m = MessageDigest.getInstance("MD5");
				m.reset();
				m.update(p_password.getBytes());
				final byte[] digest = m.digest();
				final BigInteger bigInt = new BigInteger(1, digest);
				hashtext = bigInt.toString(PASSWORDHASHRADIX);
				// Now we need to zero pad it if you actually want the full 32
				// chars.
				while (hashtext.length() < PASSWORDPADDSIZE) {
					hashtext = "0" + hashtext;
				}
			} catch (final NoSuchAlgorithmException e1) {
				handleException(MISSINGMD5ALGORITHM, e1, NotificationStatus.BLOCK);
			}
		}
		return hashtext;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.airsltd.core.ICoreInterface#getConnection()
	 */
	@Override
	public Connection getConnection() {
		Connection l_retVal = null;
		try {
			l_retVal = AirsPooledConnection.getInstance().getConnection();
		} catch (final SQLException e) {
			handleException(null, e, NotificationStatus.BLOCK);
		}
		return l_retVal;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.airsltd.core.ICoreInterface#isProperty(java.lang.String)
	 */
	@Override
	public final boolean isProperty(String p_property) {
		return Boolean.TRUE.equals(getProperty(null, p_property));
	}

	@Override
	public DateFormat getDateFormat() {
		return DateFormat.getDateInstance();
	}

	@Override
	public Object getProperty(Object source, String property) {
		return null;
	}

	@Override
	public String getPersistentProperty(String p_arg0, String p_arg1) {
		return null;
	}

	@Override
	public void setPersistentProperty(String p_arg0, String p_arg1) {
		// Superclass can modify this method to modify persistent properties
	}

	@Override
	public String getSecurePersistentProperty(String p_arg0, String p_arg1) {
		return null;
	}

	@Override
	public void setSecurePersistentProperty(String p_arg0, String p_arg1) {
		// Superclass can modify this method to modify persistent properties
	}

	@Override
	public Object getApplication() {
		return null;
	}

	@Override
	public boolean isPersistentProperty(String p_arg0) {
		return false;
	}

	@Override
	public String productName() {
		return null;
	}

	@Override
	public IAirsUserData getUserData() {
		return f_userData;
	}
	
	public void setUserData(IAirsUserData p_data) {
		f_userData = p_data;
	}

	@Override
	public void loadProperty(Object source, String property, Object value) {
		// Default access to database's does not provide property support
	}

	@Override
	public void playSound(String p_soundGroup, String p_soundIndex) {
		// Default access to a database does not provide sound
	}

	@Override
	public void pushExceptionCollector(IExceptionCollector<?> p_exceptionCollector) {
		f_stack.push(p_exceptionCollector);
	}

	@Override
	public IExceptionCollector<?> popExceptionCollector() {
		return f_stack.pop();
	}

	@Override
	public void trace(Class<?> p_class, String p_ident, String p_string) {
		getLog(p_class).trace(p_string);
	}

	@Override
	public void debug(Class<?> p_class, String p_ident, String p_string) {
		getLog(p_class).debug(p_string);
	}

	@Override
	public boolean traceActive(Class<?> p_class, String p_traceId) {
		final Log l_log = getLog(p_class);
		return p_traceId.startsWith("/debug") ? l_log.isDebugEnabled() : l_log.isTraceEnabled();
	}

	@Override
	public void worked(int p_increment) {
		// Super classes can override this method to note when work has been
		// done
	}

	@Override
	public void subTask(String p_taskName) {
		// Super classes can override this method to note when a sub-task has
		// started
	}

	@Override
	public void beginTask(String p_taskName, int p_taskSize) {
		// Super classes can override this method to note when a task has
		// started
	}

	@Override
	public void setLog(Log p_mockLog) {
		f_log.put(CoreInterface.class, p_mockLog);
	}

	@Override
	public Log getLog() {
		return getLog(CoreInterface.class);
	}

	@Override
	public String applicationRoot() {
		return System.getProperty("user.home");
	}

}
