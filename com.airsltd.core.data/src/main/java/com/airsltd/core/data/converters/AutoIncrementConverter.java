/**
 *
 */
package com.airsltd.core.data.converters;

import java.text.ParseException;

import com.airsltd.core.data.AutoIncrementField;
import com.airsltd.core.data.IBlockData;

/**
 * @author Jon Boley
 *
 */
public class AutoIncrementConverter extends DatabaseConverter<IBlockData, AutoIncrementField> {

	public AutoIncrementConverter() {
		super(AutoIncrementField.class);
	}

	@Override
	public String toSql(IBlockData p_parent, AutoIncrementField p_data) {
		return p_data.getId() + "";
	}

	@Override
	public AutoIncrementField fromSql(IBlockData p_parent, String string) throws ParseException {
		AutoIncrementField l_retVal = null;
		try {
			l_retVal = new AutoIncrementField(p_parent.getClass(), Long.parseLong(string));
		} catch (final NumberFormatException e) {
			parseExceptionThrow(e, string, "Long", 0);
		}
		return l_retVal;
	}
}
