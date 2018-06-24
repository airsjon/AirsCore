/**
 *
 */
package com.airsltd.core.data.converters;

import java.text.ParseException;

import com.airsltd.core.data.IBlockData;

/**
 * @author Jon Boley
 *
 */
public class FloatConverter extends DatabaseConverter<IBlockData, Float> {

	public FloatConverter() {
		super(Float.class);
		addConverter(float.class);
	}

	@Override
	public String toSql(IBlockData p_parent, Float p_data) {
		return p_data + "";
	}

	@Override
	public Float fromSql(IBlockData p_parent, String string) throws ParseException {
		Float l_retVal = null;
		try {
			l_retVal = Float.parseFloat(string);
		} catch (final NumberFormatException e) {
			parseExceptionThrow(e, string, "Float", 0);
		}
		return l_retVal;
	}
}
