/**
 *
 */
package com.airsltd.core.lisp.function.type;

import java.util.Date;

import com.airsltd.core.lisp.function.EvaluationContext;
import com.airsltd.core.lisp.function.ISimpleValue;
import com.airsltd.core.lisp.function.SimpleValue;

/**
 * @author Jon Boley
 *
 */
public class IntegerType implements IConvertType {

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
			l_value = Type.functionInteger(0);
		} else {
			switch (p_value.getType(p_context)) {
			case BOOLEAN:
				l_value = new SimpleValue(Type.TRUE.equals(l_value) ? 1 : 0, Type.INTEGER);
				break;
			case LONG:
				l_value = new SimpleValue((int) (long) l_value.getValue(p_context), Type.INTEGER);
				break;
			case FLOAT:
				l_value = new SimpleValue((int) (float) l_value.getValue(p_context), Type.INTEGER);
				break;
			case DOUBLE:
				l_value = new SimpleValue((int) (double) l_value.getValue(p_context), Type.INTEGER);
				break;
			case DATE:
				l_value = new SimpleValue(((Date) l_value.getValue(p_context)).getTime(), Type.INTEGER);
				break;
			case STRING:
				try {
					final int l_parsedInt = Integer.parseInt((String) l_value.getValue(p_context));
					l_value = new SimpleValue(l_parsedInt, Type.INTEGER);
				} catch (final NumberFormatException lfe) {
					// Unable to parse the integer so we leave the value as
					// input
				}
				break;
			default:
			}
		}
		return l_value;
	}

	public static boolean isInteger(ISimpleValue p_value) {
		return p_value instanceof SimpleValue && ((SimpleValue) p_value).getType(null) == Type.INTEGER;
	}

	public static Integer toInteger(ISimpleValue p_value) {
		if (!isInteger(p_value)) {
			throw new UnsupportedOperationException("Can not cast to Integer - evaluate first?");
		}
		return (Integer) ((SimpleValue) p_value).getValue(null);
	}
}
