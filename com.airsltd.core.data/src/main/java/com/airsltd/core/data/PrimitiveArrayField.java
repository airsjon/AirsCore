/**
 *
 */
package com.airsltd.core.data;

import java.text.ParseException;

/**
 * @author Jon Boley
 *
 */
public class PrimitiveArrayField implements IBlockField<Object> {

	private final IBlockData f_data;
	private final PrimitiveArrayFactory f_arrayFactory;

	public PrimitiveArrayField(IBlockData p_blockData, PrimitiveArrayFactory p_primitiveArrayFactory) {
		f_data = p_blockData;
		f_arrayFactory = p_primitiveArrayFactory;
	}

	@Override
	public String toSqlValue() {
		return f_arrayFactory.toSql(getValue());
	}

	@Override
	public Object getValue() {
		return f_arrayFactory.value(f_data);
	}

	@Override
	public void setValue(Object p_newData) {
		f_arrayFactory.modify(f_data, p_newData);
	}

	@Override
	public void fromSqlValue(String p_string) throws ParseException {
		f_arrayFactory.modify(f_data, f_arrayFactory.fromSql(p_string));
	}

	@Override
	public int compareTo(Object p_o) {
		return f_arrayFactory.compareTo(p_o);
	}

}
