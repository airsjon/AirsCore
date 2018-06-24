/**
 *
 */
package com.airsltd.core.lisp.function.type;

import java.text.ParseException;
import java.util.Date;

import com.airsltd.core.data.converters.BlockConverters;
import com.airsltd.core.lisp.function.EvaluationContext;
import com.airsltd.core.lisp.function.ISimpleValue;

/**
 * @author Jon Boley
 *
 */
public class DateType implements IConvertType {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.airsltd.aga.ranking.core.function.IType#convert(com.airsltd.aga.
	 * ranking.core.function.ISimpleValue)
	 */
	@Override
	public ISimpleValue convert(ISimpleValue p_value, EvaluationContext p_context) {
		ISimpleValue l_value = p_value;
		if (p_value == null) {
			l_value = Type.functionDate(new Date());
		} else {
			switch (p_value.getType(p_context)) {
			case INTEGER:
				l_value = fromLong((int) l_value.getValue(p_context));
				break;
			case LONG:
				l_value = fromLong((long) l_value.getValue(p_context));
				break;
			case FLOAT:
				l_value = fromLong((long) (float) l_value.getValue(p_context));
				break;
			case DOUBLE:
				l_value = fromLong((long) (double) l_value.getValue(p_context));
				break;
			case STRING:
				try {
					l_value = Type
							.functionDate(BlockConverters.DATECONVERTER.fromSql(null, (String) p_value.getValue(p_context)));
				} catch (final ParseException e) {
					// not able to convert, default back to original value
				}
				break;
			default:
			}
		}
		return l_value;
	}

	private ISimpleValue fromLong(long p_value) {
		final Date l_date = new Date();
		l_date.setTime(p_value);
		return Type.functionDate(l_date);
	}

}
