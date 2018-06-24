/**
 *
 */
package com.airsltd.core.lisp.function.type;

import com.airsltd.core.lisp.function.EvaluationContext;
import com.airsltd.core.lisp.function.ISimpleValue;

/**
 * @author jon_000
 *
 */
public class BooleanType implements IConvertType {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.airsltd.aga.ranking.core.function.IType#convert(com.airsltd.aga.
	 * ranking.core.function.ISimpleValue)
	 */
	@Override
	public ISimpleValue convert(ISimpleValue p_value, EvaluationContext p_context) {
		boolean l_value = p_value == null ? false : true;
		if (p_value != null) {
			switch (p_value.getType(p_context)) {
			case INTEGER:
				l_value = !Integer.valueOf(0).equals(p_value.getValue(p_context));
				break;
			case LONG:
				l_value = !Long.valueOf(0).equals(p_value.getValue(p_context));
				break;
			case FLOAT:
				l_value = !Float.valueOf(0f).equals(p_value.getValue(p_context));
				break;
			case DOUBLE:
				l_value = !Double.valueOf(0).equals(p_value.getValue(p_context));
				break;
			case STRING:
				l_value = !"false".equals(p_value.getValue(p_context));
				break;
			case BOOLEAN:
				l_value = p_value.getValue(p_context) == Boolean.TRUE;
				break;
			default:
			}
		}
		return l_value ? Type.TRUE : Type.FALSE;
	}

}
