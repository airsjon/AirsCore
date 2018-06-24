/**
 *
 */
package com.airsltd.core.lisp.function.type;

import com.airsltd.core.lisp.function.EvaluationContext;
import com.airsltd.core.lisp.function.ISimpleValue;
import com.airsltd.core.lisp.function.SimpleValue;

/**
 * @author Jon Boley
 *
 */
public class DoubleType implements IConvertType {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.airsltd.aga.ranking.core.function.type.IConvertType#convert(com.
	 * airsltd.aga.ranking.core.function.ISimpleValue,
	 * com.airsltd.aga.ranking.core.function.EvaluationContext)
	 */
	@Override
	public ISimpleValue convert(ISimpleValue p_value, EvaluationContext p_context) {
		ISimpleValue l_value = p_value;
		if (p_value == null) {
			l_value = new SimpleValue(0f, Type.DOUBLE);
		} else {
			switch (p_value.getType(p_context)) {
			case BOOLEAN:
				l_value = new SimpleValue(Type.TRUE.equals(l_value) ? 1.0d : 0d, Type.DOUBLE);
				break;
			case INTEGER:
				l_value = new SimpleValue((double) (int) l_value.getValue(p_context), Type.DOUBLE);
				break;
			case LONG:
				l_value = new SimpleValue((double) (long) l_value.getValue(p_context), Type.DOUBLE);
				break;
			case FLOAT:
				l_value = new SimpleValue((double) (float) l_value.getValue(p_context), Type.DOUBLE);
				break;
			case STRING:
				try {
					final double l_parsedFloat = Double.parseDouble((String) l_value.getValue(p_context));
					l_value = new SimpleValue(l_parsedFloat, Type.DOUBLE);
				} catch (final NumberFormatException lfe) {
					// Unable to parse the Floating point number so we leave the
					// value as is
				}
				break;
			default:
			}
		}
		return l_value;

	}

}
