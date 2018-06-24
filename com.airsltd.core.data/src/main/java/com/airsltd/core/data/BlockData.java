/**
 *
 */
package com.airsltd.core.data;

import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Helper functions for converting to/from SQL to Java data
 *
 * @author Jon Boley
 *
 */
public class BlockData {
	public static final SimpleDateFormat SIMPLEDATEFORMAT = new SimpleDateFormat("yyyy-MM-dd");
	private static final String TRUE = "1";
	private static final String FALSE = "0";

	/**
	 * When a null date is stored in an MySQL Database, this string is used to
	 * represent that value.
	 */
	public static final String NULLDATESTRING = "NULL";
	public static final String OUTNULLDATESTRING = "0000-00-00";


	/**
	 * Standard size of a medium data string
	 */
	public static final int MEDIUMSTRING = 45;
	/**
	 * Standard size of a large data string
	 */
	public static final int LARGESTRING = 256;


	private static final int MINQUOTEDLENGTH = 2;

	private BlockData() {
	}

	public static boolean fromSqlBoolean(String p_string) {
		return TRUE.equals(p_string);
	}

	public static float fromSqlFloat(String p_string) {
		return Float.parseFloat(p_string);
	}

	public static int fromSqlInt(String p_string) {
		return Integer.parseInt(p_string);
	}

	public static long fromSqlLong(String p_string) {
		return Long.parseLong(p_string);
	}

	public static String fromSqlString(String p_string) {
		return p_string != null && p_string.startsWith("'") && p_string.endsWith("'")
				? p_string.substring(1, p_string.length() - 1) : p_string;
	}

	public static Timestamp fromSqlTimestamp(String p_string) throws ParseException {
		Timestamp l_retVal = null;
		if (!OUTNULLDATESTRING.equals(p_string)) {
			try {
				l_retVal = Timestamp.valueOf(p_string);
			} catch (final IllegalArgumentException l_e) {
				final ParseException l_pe = new ParseException("Unable to parse timestamp for: " + p_string, 0);
				l_pe.addSuppressed(l_e);
				throw l_pe;
			}
		}
		return l_retVal;
	}

	/**
	 * Helper function to check equality with null objects being allowed.
	 * 
	 * @param p_object
	 *            can be null, first object to test
	 * @param p_object2
	 *            can be null, second object to test
	 * @return true if the objects are equal or both are null
	 */
	public static boolean objectCompare(Object p_object, Object p_object2) {
		return p_object == null ? p_object2 == null : p_object.equals(p_object2);
	}

	public static boolean stringCompare(String p_name, String p_name2) {
		return p_name == null || p_name.isEmpty() ? p_name2 == null || p_name2.isEmpty() : p_name.equals(p_name2);
	}

	public static <T, V> boolean mapCompare(Map<T, V> p_objectOne, Map<T, V> p_objectTwo) {
		return p_objectOne == null || p_objectOne.isEmpty() ? p_objectTwo == null || p_objectTwo.isEmpty()
				: p_objectOne.equals(p_objectTwo);
	}

	public static boolean stringTrimCompare(String p_name, String p_name2) {
		return stringCompare(p_name == null ? null : p_name.trim(), p_name2 == null ? null : p_name2.trim());
	}

	public static String toSql(boolean p_forfeit) {
		return p_forfeit ? TRUE : FALSE;
	}

	public static String toSql(char p_character) {
		return "'" + p_character + "'";
	}

	public static String toSql(Date p_date) {
		return p_date == null ? NULLDATESTRING : String.format("'%tF'", new Object[] { p_date });
	}

	public static String toSql(float p_rating) {
		return p_rating + "";
	}

	/*
	 * Static functions to create string representations of the various internal
	 * data types
	 */
	public static String toSql(int p_intValue) {
		return p_intValue + "";
	}

	/**
	 * @param IPersistentId
	 * @return
	 */
	public static String toSql(IPersistentId p_persistentId) {
		return p_persistentId == null ? "0" : p_persistentId.getPersistentID() + "";
	}

	public static String toSql(long p_longValue) {
		return p_longValue + "";
	}

	public static String toSql(PasswordString p_password) {
		return toSql(p_password == null ? null : p_password.getPassword());
	}

	/**
	 * This SQL helper method takes a string and makes it ready for storing into
	 * an SQL database. Note: if your SQL data field has a limit on it's string
	 * size, this method will not trim the string to a proper size before
	 * sending it to the SQL database
	 *
	 * @param string
	 *            the string to encode for an SQL query, update, etc.
	 * @return a string properly encoded for sending to an SQL database
	 */

	public static String toSql(String p_string) {
		return p_string == null ? "''" : "'" + escapeSql(p_string) + "'";
	}

	/**
	 * This SQL helper method takes a string, limits it size and makes it ready
	 * for storing into an SQL database
	 *
	 * @param string
	 *            the string to encode for an SQL query, update, etc.
	 * @param maxLength
	 *            maximum length the string can occupy on the database
	 * @return a string properly encoded for sending to an SQL database
	 */
	public static String toSql(String p_string, int p_maxLength) {
		String l_retVal = "''";
		if (p_string != null) {
			final String l_string = p_string.length() > p_maxLength ? p_string.substring(0, p_maxLength) : p_string;
			l_retVal = "'" + escapeSql(l_string) + "'";
		}
		return l_retVal;
	}

	public static String escapeSql(String p_string) {
		return p_string == null ? null : p_string.replace("'", "''");
	}

	public static String toSql(Timestamp p_date) {
		return p_date == null ? NULLDATESTRING : String.format("'%tF %1$tT'", new Object[] { p_date });
	}

	/**
	 * Helper function. Test if <code>p_data</code> is <code>null</code> or an
	 * empty {@link String}.
	 *
	 * @param p_data
	 * @return true if the {@link String} <code>p_data</code> is
	 *         <code>null</code> or an empty {@link String}
	 * @author Jon Boley
	 */
	public static boolean isEmptyString(String p_data) {
		return p_data == null || p_data.isEmpty();
	}

	public static boolean booleanValue(Object p_value) {
		return p_value.equals(Boolean.TRUE) || TRUE.equals(p_value);
	}

	public static boolean validateBoolean(Object p_value) {
		return p_value != null && validateBooleanInternal(p_value);
	}

	private static boolean validateBooleanInternal(Object p_value) {
		return p_value == Boolean.TRUE || p_value == Boolean.FALSE || TRUE.equals(p_value) || FALSE.equals(p_value);
	}

	public static String[] unquote(String[] p_csv) {
		final String[] l_retVal = new String[p_csv.length];
		int l_index = 0;
		for (final String l_string : p_csv) {
			l_retVal[l_index++] = unquote(l_string);
		}
		return l_retVal;
	}

	public static String unquote(String p_string) {
		final String l_retVal = p_string.length() >= MINQUOTEDLENGTH && p_string.startsWith("'")
				&& p_string.endsWith("'") ? p_string.substring(1, p_string.length() - 1) : p_string;
		return l_retVal.replace("''", "'");
	}

	public static <T extends Comparable<T>> String toListStr(List<T> p_seqList) {
		Collections.sort(p_seqList);
		final StringBuilder inList = new StringBuilder("(");
		boolean firstp = true;
		String cRowId = "";
		for (final T sr : p_seqList) {
			final String l_newString = sr.toString();
			if (!cRowId.equals(l_newString)) {
				inList.append((firstp ? "" : ",") + l_newString);
				if (firstp) {
					firstp = false;
				}
			}
			cRowId = l_newString;
		}
		inList.append(")");
		return inList.toString();
	}

	/**
	 * Convert a list of IPersistentId objects into an SQL selector list.
	 * <p>
	 * This selector list will be ordered and of the form ( id1, id2, id3 ...
	 * idn ).
	 * 
	 * @param p_class
	 *            not null, Class of the object being converted
	 * @param p_seqList
	 *            not null, the List of the objects being converted
	 * @return a string that can be used by the SQL Query condition IN.
	 */
	public static <T extends IPersistentId> String toIndexedStr(List<T> p_seqList) {
		final List<Long> l_indexes = new ArrayList<Long>();
		for (final T l_item : p_seqList) {
			l_indexes.add(l_item.getPersistentID());
		}
		Collections.sort(l_indexes);
		final StringBuilder inList = new StringBuilder("(");
		boolean firstp = true;
		String cRowId = "";
		for (final Long sr : l_indexes) {
			final String l_newString = sr.toString();
			if (!cRowId.equals(l_newString)) {
				inList.append((firstp ? "" : ",") + l_newString);
				if (firstp) {
					firstp = false;
				}
			}
			cRowId = l_newString;
		}
		inList.append(")");
		return inList.toString();
	}

	public static <T extends Comparable<? super T>> int objectCompareTo(T p_objectOne, T p_objectTwo) {
		return p_objectOne == null ? p_objectTwo == null ? 0 : -1
				: p_objectTwo == null ? 1 : p_objectOne.compareTo(p_objectTwo);
	}

	public static String toSql(byte[] p_unloadMoves) {
		return toSql(new String(p_unloadMoves, StandardCharsets.UTF_8));
	}

	public static byte[] fromSqlByteArray(String p_string) {
		return p_string.getBytes(StandardCharsets.UTF_8);
	}

}
