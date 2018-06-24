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
public class DoubleConverter extends DatabaseConverter<IBlockData, Double> {

	public DoubleConverter() {
		super(Double.class);
		addConverter(double.class);
	}

	@Override
	public String toSql(IBlockData p_parent, Double p_data) {
		return p_data + "";
	}

	@Override
	public Double fromSql(IBlockData p_parent, String string) throws ParseException {
		Double l_retVal = null;
		try {
			if (string != null) {
				l_retVal = Double.parseDouble(string);
			}
		} catch (final NumberFormatException e) {
			parseExceptionThrow(e, string, "Float", 0);
		}
		return l_retVal;
	}
}
