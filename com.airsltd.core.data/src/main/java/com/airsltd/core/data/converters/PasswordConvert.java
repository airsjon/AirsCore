/**
 *
 */
package com.airsltd.core.data.converters;

import com.airsltd.core.data.BlockData;
import com.airsltd.core.data.IBlockData;
import com.airsltd.core.data.PasswordString;

/**
 * @author Jon Boley
 *
 */
public class PasswordConvert extends DatabaseConverter<IBlockData, PasswordString> {

	public PasswordConvert() {
		super(PasswordString.class);
	}

	@Override
	public String toSql(IBlockData p_parent, PasswordString p_data) {
		return BlockData.toSql(p_data == null ? null : p_data.getPassword());
	}

	@Override
	public PasswordString fromSql(IBlockData p_parent, String string) {
		return new PasswordString(string);
	}
}