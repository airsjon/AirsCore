/**
 *
 */
package com.airsltd.core.data.converters;

import java.text.ParseException;

import com.airsltd.core.data.BlockData;
import com.airsltd.core.data.IBlockData;
import com.airsltd.core.data.IDatabaseConverter;

/**
 * @author Jon
 *
 */
public class StringSQLConvert implements IDatabaseConverter<IBlockData, String> {

	private final int f_maxLength;

	public StringSQLConvert(int p_maxLength) {
		f_maxLength = p_maxLength;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.airsltd.core.data.IDatabaseConverter#toSql(java.lang.Object)
	 */
	@Override
	public String toSql(IBlockData p_parent, String p_string) {
		String l_retVal = "''";
		if (p_string != null) {
			final String l_string = f_maxLength != -1 && p_string.length() > f_maxLength
					? p_string.substring(0, f_maxLength) : p_string;
			l_retVal = "'" + BlockData.escapeSql(l_string) + "'";
		}
		return l_retVal;
	}

	@Override
	public String fromSql(IBlockData p_parent, String string) throws ParseException {
		return string == null || f_maxLength == -1 || string.length() <= f_maxLength ? string
				: string.substring(0, f_maxLength);
	}

}
