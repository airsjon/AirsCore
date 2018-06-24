/**
 *
 */
package com.airsltd.core.data.converters;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.text.ParseException;
import java.util.Properties;

import com.airsltd.core.NotificationStatus;
import com.airsltd.core.data.BlockData;
import com.airsltd.core.data.CoreInterface;
import com.airsltd.core.data.IBlockData;

/**
 * @author jon_000
 *
 */
public class PropertyConverter extends DatabaseConverter<IBlockData, Properties> {

	public PropertyConverter() {
		super(Properties.class);
	}

	@Override
	public String toSql(IBlockData p_parent, Properties p_data) {
		final StringWriter l_sw = new StringWriter();
		try {
			p_data.store(l_sw, "");
		} catch (final IOException e) {
			CoreInterface.getSystem().handleException("Unable to store property list: " + p_data, e,
					NotificationStatus.BLOCK);
		}
		return "'" + BlockData.escapeSql(l_sw.toString()) + "'";
	}

	@Override
	public Properties fromSql(IBlockData p_parent, String p_string) throws ParseException {
		final Properties l_retVal = new Properties();
		if (p_string != null) {
			final StringReader l_sr = new StringReader(p_string);
			try {
				l_retVal.load(l_sr);
			} catch (final IOException e) {
				final ParseException l_pe = new ParseException(p_string, 0);
				l_pe.addSuppressed(e);
				throw l_pe;
			}
		}
		return l_retVal;
	}
}
