/**
 *
 */
package com.airsltd.core.data.converters;

import java.lang.reflect.InvocationTargetException;

import java.text.ParseException;
import java.util.Optional;

import com.airsltd.core.data.IBlockData;
import com.airsltd.core.data.IDatabaseConverter;
import com.airsltd.core.data.annotations.AirsEnumNamed;
import com.airsltd.core.data.annotations.FieldStyle;

/**
 * Convert to/from an Enum data to a MySQL string.
 * <p>
 * {@link Enum} data objects are translated to their ordinal value. This ordinal
 * value will be stored as a string in the database.
 * 
 * @author Jon Boley
 * @param <T>
 *            the Enum being persisted
 *
 */
public class EnumConverter<T extends Enum<?>> implements IDatabaseConverter<com.airsltd.core.data.IBlockData, T> {

	private final Class<T> f_class;
	private FieldStyle f_useNames;

	public EnumConverter(Class<T> p_class) {
		f_class = p_class;
		initialize();
	}

	private void initialize() {
		
		f_useNames = Optional.ofNullable(f_class.getAnnotation(AirsEnumNamed.class)).map(AirsEnumNamed::style).orElse(null);
	}

	public String toSqlInt(IBlockData p_parent, T p_data) {
		return p_data == null ? "-1" : p_data.ordinal() + "";
	}

	@SuppressWarnings("unchecked")
	public T fromSqlInt(IBlockData p_parent, String string) throws ParseException {
		try {
			final Integer l_ordinal = Integer.parseInt(string);
			return l_ordinal == -1 ? null : ((T[]) f_class.getMethod("values").invoke(null))[l_ordinal];
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
				| SecurityException e) {
			final ParseException l_pe = new ParseException("Unable to parse enum from " + string, 0);
			l_pe.addSuppressed(e);
			throw l_pe;
		}
	}

	@Override
	public String toSql(IBlockData p_parent, T p_data) {
		return f_useNames==null?toSqlInt(p_parent, p_data):toSqlNamed(p_parent, p_data);
	}

	protected String toSqlNamed(IBlockData p_parent, T p_data) {
		String l_retVal = p_data==null?"":p_data.name();
		switch (f_useNames) {
		case CAPITALIZED:
			l_retVal = l_retVal.substring(0,1) + l_retVal.substring(1).toLowerCase();
			break;
		case LOWERCASE:
			l_retVal = l_retVal.toLowerCase();
			break;
		case UPPERCASE:
		case ASIS:
		default:
			// everything is as it should be
		}
		return l_retVal;
	}

	@Override
	public T fromSql(IBlockData p_parent, String p_string) throws ParseException {
		return f_useNames==null?fromSqlInt(p_parent, p_string):fromSqlNamed(p_parent, p_string);
	}

	protected T fromSqlNamed(IBlockData p_parent, String p_string) {
		T l_retVal = null;
		String l_compare = p_string.toUpperCase();
		for (T l_enum : f_class.getEnumConstants()) {
			if (l_enum.name().equals(l_compare)) {
				l_retVal = l_enum;
				break;
			}
		}
		return l_retVal;
	}

}
