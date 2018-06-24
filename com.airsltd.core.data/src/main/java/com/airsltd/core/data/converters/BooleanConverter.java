/**
 *
 */
package com.airsltd.core.data.converters;

import com.airsltd.core.data.IBlockData;

/**
 * @author jon
 *
 */
public class BooleanConverter extends DatabaseConverter<IBlockData, Boolean> {

	private static final String TRUE = "1";
	private static final String FALSE = "0";

	public BooleanConverter() {
		super(Boolean.class);
		addConverter(boolean.class);
	}

	@Override
	public String toSql(IBlockData p_parent, Boolean p_data) {
		return p_data ? TRUE : FALSE;
	}

	@Override
	public Boolean fromSql(IBlockData p_parent, String string) {
		return TRUE.equals(string);
	}
}
