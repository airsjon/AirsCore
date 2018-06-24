/**
 *
 */
package com.airsltd.core.data;

import java.io.PrintStream;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.sql.Date;
import java.text.ParseException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.airsltd.core.NotificationStatus;
import com.airsltd.core.data.converters.BlockConverters;

/**
 * Provide core code for a Java App that will connect to a database.
 *
 * @author Jon Boley
 *
 */
public class AirsJavaDatabaseApp {

	/**
	 * The number of collections that have occured before the creation of this instance.
	 */
	private long f_initialCollectionCount;
	
	public AirsJavaDatabaseApp() {
		super();
		f_initialCollectionCount = getGcCount();
	}

	private static final int SECONDGROUP = 2;

	/**
	 * Initialize the database and set up the ICoreInterface for the program.
	 *
	 * @param p_sqlConnection
	 */
	protected void initializeDatabase(ISqlConnection p_sqlConnection) {
		new CoreInterface();
		try {
			AirsPooledConnection.getInstance().initialize(p_sqlConnection);

		} catch (final ClassNotFoundException e) {
			CoreInterface.getSystem().handleException("Unable to initialize the Driver and Shared Pool", e,
					NotificationStatus.LOG);
		}
	}

	/**
	 * Determine if p_longId or p_shortId is contained in the array p_args.
	 *
	 * @param p_args
	 *            not null, an array containing all the arguments passed to this
	 *            application.
	 * @param p_longId
	 *            not null, the long id as a String (for example "-remote")
	 * @param p_shortId
	 *            not null, the short id as a String (for example "-r")
	 * @return
	 */
	public static boolean switchExists(String[] p_args, String p_longId, String p_shortId) {
		boolean l_retVal = false;
		if (p_args.length > 0) {
			final List<String> l_args = Arrays.asList(p_args);
			l_retVal = l_args.contains(p_longId) || l_args.contains(p_shortId);
		}
		return l_retVal;
	}

	public static String getArgData(String[] p_args, String p_equalId, String p_nextId) {
		String l_retVal = null;
		int l_index = 0;
		boolean l_break = false;
		for (final String l_current : p_args) {
			if (l_current.startsWith(p_equalId)) {
				l_retVal = l_current.substring(p_equalId.length());
				l_break = true;
			} else if (l_current.equals(p_nextId)) {
				l_retVal = p_args[l_index + 1];
				l_break = true;
			}
			if (l_break) {
				break;
			}
			l_index++;
		}
		return l_retVal;
	}
	
	public static Date getArgDate(String[] p_args, String p_equalId, String p_nextId) throws ParseException {
		String l_value = getArgData(p_args, p_equalId, p_nextId);
		return l_value==null?null:BlockConverters.DATECONVERTER.fromSql(null, l_value);
	}

	public static <T extends Enum<T>> Map<T, String> getArgHash(String p_string, String[] p_args, Class<T> p_class) {
		final Map<T, String> l_retVal = new HashMap<>();
		final Pattern l_pattern = Pattern.compile("([a-zA-Z]*)\\=(.*)");
		for (final String l_arg : p_args) {
			if (l_arg.startsWith(p_string)) {
				final Matcher l_match = l_pattern.matcher(l_arg.substring(p_string.length()));
				if (l_match.matches()) {
					final String l_name = l_match.group(1);
					final T l_enum = findEnum(p_class, l_name);
					l_retVal.put(l_enum, l_match.group(SECONDGROUP));
				}
			}
		}
		return l_retVal;
	}

	public static Map<String, String> getArgHash(String p_string, String[] p_args) {
		final Map<String, String> l_retVal = new HashMap<>();
		final Pattern l_pattern = Pattern.compile("([a-zA-Z]*)\\=(.*)");
		for (final String l_arg : p_args) {
			if (l_arg.startsWith(p_string)) {
				final Matcher l_match = l_pattern.matcher(l_arg.substring(p_string.length()));
				if (l_match.matches()) {
					l_retVal.put(l_match.group(1), l_match.group(SECONDGROUP));
				}
			}
		}
		return l_retVal;
	}

	protected static <T extends Enum<T>> T findEnum(Class<T> p_class, String p_string) {
		T l_retVal = null;
		final String l_string = p_string.toUpperCase();
		for (final T l_value : p_class.getEnumConstants()) {
			if (l_value.name().equals(l_string)) {
				l_retVal = l_value;
				break;
			}
		}
		return l_retVal;
	}

	/**
	 * Get the number of times all GC have been called.
	 * 
	 * @return the sum of all the times every Garbage Collector has been called.
	 */
	public long getGcCount() {
		long l_retVal = 0;
		for (GarbageCollectorMXBean b : ManagementFactory.getGarbageCollectorMXBeans()) {
			long l_count = b.getCollectionCount();
			if (l_count != -1) { l_retVal +=  l_count; }
		}
		return l_retVal;
	}
	
	/**
	 * Return the total memory in use.
	 * <p>
	 * Collect all unused memory and wait till the collection has completed.
	 * Then return the total amount of memory used.
	 * 
	 * @return long value of the memory in use.
	 */
	public long getReallyUsedMemory() {
		long l_before = getGcCount();
		System.gc();
		while (getGcCount() == l_before);
		return getCurrentlyUsedMemory();
	}
	
	/**
	 * Return the amount of memory in use by the jvm.
	 * <p>
	 * Total all the memory used in both Heap and Non Heap storage.
	 * 
	 * @return long value of the total memory usage.
	 */
	public long getCurrentlyUsedMemory() {
		return
				ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getUsed() +
				ManagementFactory.getMemoryMXBean().getNonHeapMemoryUsage().getUsed();
	}
	
	public void dumpStastics(PrintStream p_stream) {
		p_stream.append("Current Memory and GC state");
		p_stream.println();
		long l_memoryUsed = getReallyUsedMemory();
		p_stream.append("Collections: ");
		p_stream.println(getGcCount()-f_initialCollectionCount);
		p_stream.append("Memory Used: ");
		p_stream.format("%.2f MB", l_memoryUsed/1000000f);
		p_stream.println();
	}
}
