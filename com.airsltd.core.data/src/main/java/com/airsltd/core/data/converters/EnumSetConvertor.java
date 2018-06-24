/**
 *
 */
package com.airsltd.core.data.converters;

import java.text.ParseException;
import java.util.EnumSet;
import java.util.Set;

import com.airsltd.core.data.IDatabaseConverter;
import com.airsltd.core.data.PersistedData;

/**
 * Convert an EnumSet to an SQL string and versa.
 *
 * @author Jon Boley
 *
 */
public class EnumSetConvertor<T extends Enum<T>> implements IDatabaseConverter<PersistedData, Set<T>> {

	Class<T> f_class;

	public EnumSetConvertor(Class<T> p_class) {
		super();
		f_class = p_class;
	}

	@Override
	public String toSql(PersistedData p_parent, Set<T> p_data) {
		long l_value = 0;
		for (final T l_element : p_data) {
			l_value += 1 << l_element.ordinal();
		}
		return l_value + "";
	}

	@Override
	public Set<T> fromSql(PersistedData p_parent, String p_string) throws ParseException {
		Long l_inLong = Long.parseLong(p_string);
		final Set<T> l_retVal = EnumSet.allOf(f_class);
		for (final T l_element : l_retVal) {
			if ((l_inLong & 1) == 0) {
				l_retVal.remove(l_element);
			}
			l_inLong = l_inLong >>> 1;
		}
		return l_retVal;
	}

}
