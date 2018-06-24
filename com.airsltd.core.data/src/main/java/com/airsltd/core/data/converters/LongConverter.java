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
public class LongConverter extends DatabaseConverter<IBlockData, Long> {

	public LongConverter() {
		super(Long.class);
		addConverter(long.class);
	}

	@Override
	public String toSql(IBlockData p_parent, Long p_data) {
		return p_data + "";
	}

	@Override
	public Long fromSql(IBlockData p_parent, String string) throws ParseException {
		Long l_retVal = null;
		try {
			l_retVal = Long.parseLong(string);
		} catch (final NumberFormatException e) {
			parseExceptionThrow(e, string, "Long", 0);
		}
		return l_retVal;
	}
}
