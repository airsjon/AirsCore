/**
 *
 */
package com.airsltd.core.data.converters;

import java.text.ParseException;

import com.airsltd.core.data.BlockData;
import com.airsltd.core.data.IBlockData;

/**
 * Convert a date to/from sql text.
 * 
 * @author Jon Boley
 *
 */
public class DateConverter extends DatabaseConverter<IBlockData, java.sql.Date> {

	public DateConverter() {
		super(java.sql.Date.class);
	}

	@Override
	public String toSql(IBlockData p_parent, java.sql.Date p_date) {
		return p_date == null ? BlockData.NULLDATESTRING : String.format("'%tF'", p_date );
	}

	@Override
	public java.sql.Date fromSql(IBlockData p_parent, String string) throws ParseException {
		return isNullString(string) ? null : new java.sql.Date(BlockData.SIMPLEDATEFORMAT.parse(string).getTime());
	}

	protected boolean isNullString(String p_string) {
		return p_string == null || p_string.isEmpty() || p_string.equals(BlockData.OUTNULLDATESTRING);
	}

}
