/**
 *
 */
package com.airsltd.core.data.converters;

import java.sql.Timestamp;
import java.text.ParseException;

import com.airsltd.core.data.BlockData;
import com.airsltd.core.data.IBlockData;

/**
 * @author jon
 *
 */
public class TimestampConverter extends DatabaseConverter<IBlockData, Timestamp> {

	public TimestampConverter() {
		super(Timestamp.class);
	}

	@Override
	public String toSql(IBlockData p_parent, Timestamp p_date) {
		return p_date == null ? BlockData.NULLDATESTRING : "'" + p_date.toString() + "'";
	}

	@Override
	public Timestamp fromSql(IBlockData p_parent, String string) throws ParseException {
		return string == null || BlockData.OUTNULLDATESTRING.equals(string) ? null : Timestamp.valueOf(string);
	}
}