/**
 * (c) 2015, Jon Boley 2703 Wildrose Ct, Bellingham WA 98229
 */
package com.airsltd.core.data.converters;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Properties;

import com.airsltd.core.data.AutoIncrementField;
import com.airsltd.core.data.IBlockData;
import com.airsltd.core.data.IDatabaseConverter;
import com.airsltd.core.data.PasswordString;


/**
 * @author Jon Boley
 *
 */
public class BlockConverters {

	/**
	 * Convert a Long data to a string that MySQL will understand as a Long
	 * number.
	 */
	public static final IDatabaseConverter<IBlockData, Long> LONGCONVERTER = new LongConverter();

	/**
	 * Convert a {@link java.sql.Date} to a string format that MySQL will
	 * understand. <code>null</code> date's are stored as
	 * {@link AbstractBlockData#NULLDATESTRING}
	 */
	public static final IDatabaseConverter<IBlockData, java.sql.Date> DATECONVERTER = new DateConverter();

	/**
	 * Convert an {@link Integer} data to a MySQL string.
	 */
	public static final IDatabaseConverter<IBlockData, Integer> INTEGERCONVERT = new IntegerConverter();
	/**
	 * Convert a {@link Boolean} data to a MySQL string. {@link Boolean#FALSE}
	 * is translated to <code>false</code> and {@link Boolean#TRUE} is
	 * translated to <code>true</code>.
	 */
	public static final IDatabaseConverter<IBlockData, Boolean> BOOLEANCONVERTER = new BooleanConverter();
	/**
	 * Convert a {@link Character} data to a MySQL string. The {@link Character}
	 * will be converted to the string '<code>character</code>'
	 */
	public static final IDatabaseConverter<IBlockData, Character> CHARACTERCONVERTER = new CharacterConverter();

	/**
	 * Convert a {@link Timestamp} to a MySQL string. {@link Timestamp} is
	 * written as the format <code>"'%tF %1$tT'"</code>.
	 */
	public static final IDatabaseConverter<IBlockData, Timestamp> TIMESTAMPCONVERT = new TimestampConverter();

	/**
	 * Encode a Map<String,String> into a string of "<key>"<value>" using : as a
	 * separator between entries
	 */
	public static final IDatabaseConverter<IBlockData, Properties> PROPERTYCONVERT = new PropertyConverter();
	/**
	 * Convert a {@link Float} to a MySQL string. {@link Float} is written
	 * simply as the standard string representation of the {@link Float}.
	 */
	public static final IDatabaseConverter<IBlockData, Float> FLOATCONVERT = new FloatConverter();
	/**
	 * Convert the data to an escaped sql string.
	 * <p>
	 * There is no size check as the database is supposed to handle strings of
	 * any size. This can lead to an exception since there is always a real
	 * upper limit that is unknown.
	 */
	public static final IDatabaseConverter<IBlockData, String> TEXTCONVERT = new StringSQLConvert(-1);

	/**
	 * When a data object is going to be loaded via MySQL "?" argument, use this
	 * helper method.
	 */
	public static final IDatabaseConverter<IBlockData, Object> CALLBACKCONVERT = new IDatabaseConverter<IBlockData, Object>() {

		@Override
		public String toSql(IBlockData p_parent, Object p_data) {
			return "?";
		}

		@Override
		public Object fromSql(IBlockData p_parent, String string) throws ParseException {
			throw new ParseException("Unable to parse call back fields from " + string, 0);
		}

	};

	/**
	 * Convert a {@link PasswordString} to a MySQL string.
	 * {@link PasswordString} is written simply as the standard string
	 * representation of the {@link PasswordString#getPassword()} value (usually
	 * an encoded string).
	 */
	public static final IDatabaseConverter<IBlockData, PasswordString> PASSWORDCONVERT = new PasswordConvert();

	private BlockConverters() {
		// Utility class is not to be instantiated.
	}
	
	/**
	 * Convert a Long data to a string that MySQL will understand as a Long
	 * number.
	 */
	public static IDatabaseConverter<IBlockData, AutoIncrementField> getAutoIncrementConverter(final Class<?> p_class) {

		return new AutoIncrementConverter();

	}

}
