/**
 *
 */
package com.airsltd.core.data.converters;

import java.text.ParseException;

import com.airsltd.core.data.IBlockData;

/**
 * @author jon
 *
 */
public class IntegerConverter extends DatabaseConverter<IBlockData, Integer> {

	public IntegerConverter() {
		super(Integer.class);
		addConverter(int.class);
	}

	@Override
	public String toSql(IBlockData p_parent, Integer p_data) {
		return p_data + "";
	}

	@Override
	public Integer fromSql(IBlockData p_parent, String p_string) throws ParseException {
		Integer l_retVal = null;
		try {
			l_retVal = p_string==null?0:Integer.parseInt(p_string);
		} catch (final NumberFormatException e) {
			parseExceptionThrow(e, p_string, "Integer", 0);
		}
		return l_retVal;
	}
}
