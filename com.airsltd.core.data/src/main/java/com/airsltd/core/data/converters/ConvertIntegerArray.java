/**
 *
 */
package com.airsltd.core.data.converters;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.airsltd.core.data.IBlockData;
import com.airsltd.core.data.IDatabaseConverter;

/**
 * @author Jon Boley
 *
 */
public class ConvertIntegerArray implements IDatabaseConverter<IBlockData, int[]> {

	private static final int MINVECTORSIZE = 2;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.airsltd.core.data.IDatabaseConverter#toSql(com.airsltd.core.data.
	 * IBlockData, java.lang.Object)
	 */
	@Override
	public String toSql(IBlockData p_parent, int[] p_data) {
		return p_data == null ? "[]" : Arrays.toString(p_data);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.airsltd.core.data.IDatabaseConverter#fromSql(com.airsltd.core.data.
	 * IBlockData, java.lang.String)
	 */
	@Override
	public int[] fromSql(IBlockData p_parent, String p_string) throws ParseException {
		final List<Integer> l_parsed = new ArrayList<Integer>();
		final String l_string = p_string.trim();
		if (l_string.length() < MINVECTORSIZE || l_string.charAt(0) != '['
				|| l_string.charAt(l_string.length() - 1) != ']') {
			throw new ParseException("Looking for an integer array of the form '[ 0x??, ...]'", 0);
		}
		final String[] l_values = l_string.substring(1, l_string.length() - 1).split(",");
		int l_index = 0;
		for (final String l_val : l_values) {
			try {
				final Integer l_intVal = Integer.parseInt(l_val.trim());
				l_parsed.add(l_intVal);
			} catch (final NumberFormatException nfe) {
				throw new ParseException("Invalid integer in array", l_index);
			}
			l_index += l_val.length();
		}
		final int[] l_retVal = new int[l_parsed.size()];
		l_index = 0;
		for (final Integer l_value : l_parsed) {
			l_retVal[l_index++] = l_value;
		}
		return l_retVal;
	}

}
