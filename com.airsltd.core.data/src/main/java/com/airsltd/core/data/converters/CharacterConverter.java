/**
 *
 */
package com.airsltd.core.data.converters;

import com.airsltd.core.data.IBlockData;

/**
 * @author jon
 *
 */
public class CharacterConverter extends DatabaseConverter<IBlockData, Character> {

	public CharacterConverter() {
		super(Character.class);
		addConverter(char.class);
	}

	@Override
	public String toSql(IBlockData p_parent, Character p_data) {
		return "'" + p_data + "'";
	}

	@Override
	public Character fromSql(IBlockData p_parent, String string) {
		return string != null && string.length() > 0 ? string.charAt(0) : null;
	}

}
