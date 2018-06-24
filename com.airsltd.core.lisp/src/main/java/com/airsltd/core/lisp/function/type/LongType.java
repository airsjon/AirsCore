/**
 *
 */
package com.airsltd.core.lisp.function.type;

import java.text.ParseException;
import java.util.Date;

import com.airsltd.core.data.converters.BlockConverters;
import com.airsltd.core.lisp.function.EvaluationContext;
import com.airsltd.core.lisp.function.ISimpleValue;
import com.airsltd.core.lisp.function.SimpleValue;

/**
 * @author Jon Boley
 *
 */
public class LongType implements IConvertType {

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
			l_value = Type.functionLong(0);
		} else {
			switch (p_value.getType(p_context)) {
			case BOOLEAN:
				l_value = new SimpleValue(Type.TRUE.equals(l_value) ? 1l : 0l, Type.LONG);
				break;
			case INTEGER:
				l_value = new SimpleValue((long) (int) l_value.getValue(p_context), Type.LONG);
				break;
			case FLOAT:
				l_value = new SimpleValue((long) (float) l_value.getValue(p_context), Type.LONG);
				break;
			case DOUBLE:
				l_value = new SimpleValue((long) (double) l_value.getValue(p_context), Type.LONG);
				break;
			case DATE:
				l_value = new SimpleValue(((Date) l_value.getValue(p_context)).getTime(), Type.LONG);
				break;
			case STRING:
				final String l_string = (String) l_value.getValue(p_context);
				try {
					final long l_parsedInt = Long.parseLong(l_string);
					l_value = new SimpleValue(l_parsedInt, Type.LONG);
				} catch (final NumberFormatException lfe) {
					// might be a Date
					try {
						l_value = new SimpleValue(BlockConverters.DATECONVERTER.fromSql(null, l_string).getTime(), Type.LONG);
					} catch (final ParseException e) {
						// Unable to parse the integer so we leave the value as
						// input
					}
				}
				break;
			default:
			}
		}
		return l_value;
	}

	public static boolean isLong(ISimpleValue p_value) {
		return p_value instanceof SimpleValue && ((SimpleValue) p_value).getType(null) == Type.LONG;
	}

	public static Long toLong(ISimpleValue p_value) {
		if (!isLong(p_value)) {
			throw new UnsupportedOperationException("Can not cast to Long - evaluate first?");
		}
		return (Long) ((SimpleValue) p_value).getValue(null);
	}

}
