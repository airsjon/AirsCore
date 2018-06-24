package com.airsltd.core.lisp.function.type;

import com.airsltd.core.lisp.function.EvaluationContext;
import com.airsltd.core.lisp.function.ISimpleValue;
import com.airsltd.core.lisp.function.SimpleValue;

public class FloatType implements IConvertType {

	@Override
	public ISimpleValue convert(ISimpleValue p_value, EvaluationContext p_context) {
		ISimpleValue l_value = p_value;
		if (p_value == null) {
			l_value = new SimpleValue(0f, Type.FLOAT);
		} else {
			switch (p_value.getType(p_context)) {
			case BOOLEAN:
				l_value = new SimpleValue(Type.TRUE.equals(l_value) ? 1.0f : 0f, Type.FLOAT);
				break;
			case INTEGER:
				l_value = new SimpleValue((float) (int) l_value.getValue(p_context), Type.FLOAT);
				break;
			case LONG:
				l_value = new SimpleValue((float) (long) l_value.getValue(p_context), Type.FLOAT);
				break;
			case DOUBLE:
				l_value = new SimpleValue((float) (double) l_value.getValue(p_context), Type.FLOAT);
				break;
			case STRING:
				try {
					final float l_parsedFloat = Float.parseFloat((String) l_value.getValue(p_context));
					l_value = new SimpleValue(l_parsedFloat, Type.FLOAT);
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
