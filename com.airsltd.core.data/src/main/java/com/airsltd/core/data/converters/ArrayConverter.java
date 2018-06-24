/**
 *
 */
package com.airsltd.core.data.converters;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import com.airsltd.core.data.IBlockData;

/**
 * @author Jon Boley
 *
 */
public class ArrayConverter<T> extends DatabaseConverter<IBlockData, T[]> {

	private final Class<?> f_class;

	public ArrayConverter(Class<?> p_class) {
		f_class = p_class;
	}

	public static <T> ArrayConverter<T> getConverter(Class<T> p_class) {
		return new ArrayConverter<T>(p_class);
	}

	@Override
	public String toSql(IBlockData p_parent, T[] p_data) {
		@SuppressWarnings("unchecked")
		final
		DatabaseConverter<IBlockData, T> l_convert = (DatabaseConverter<IBlockData, T>) DatabaseConverter
				.findConverter(IBlockData.class, f_class, null);
		final StringBuilder l_builder = new StringBuilder();
		boolean l_first = true;
		for (final T l_element : p_data) {
			if (l_first) {
				l_first = false;
			} else {
				l_builder.append(",");
			}
			l_builder.append(l_convert.toSql(p_parent, l_element));
		}
		return l_builder.toString();
	}

	@SuppressWarnings("unchecked")
	@Override
	public T[] fromSql(IBlockData p_parent, String p_string) throws ParseException {
		final DatabaseConverter<IBlockData, T> l_convert = (DatabaseConverter<IBlockData, T>) DatabaseConverter
				.findConverter(IBlockData.class, f_class, null);
		final String[] l_elements = breakApartArray(p_string);
		final List<T> l_retList = new ArrayList<T>();
		for (final String l_string : l_elements) {
			l_retList.add(l_convert.fromSql(p_parent, l_string));
		}
		return l_retList.toArray((T[]) Array.newInstance(f_class, 0));
	}

	/**
	 * Split all elements into strings subsections.
	 * <p>
	 * Commas should only be found in quoted data ('). Quotes can also be
	 * escaped (\).
	 *
	 * @param p_string
	 * @return
	 */
	protected String[] breakApartArray(String p_string) {
		return p_string.split(",");
	}

}
