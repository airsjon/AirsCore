/**
 *
 */
package com.airsltd.core.lisp.function.comparison;

import java.util.List;

import com.airsltd.core.lisp.function.EvaluationContext;
import com.airsltd.core.lisp.function.ISimpleValue;
import com.airsltd.core.lisp.function.math.MathFunction;
import com.airsltd.core.lisp.function.type.Type;

/**
 * @author jon_000
 *
 */
public abstract class CompareFunction extends MathFunction {

	public CompareFunction(String p_functionName) {
		super(p_functionName);
	}

	@Override
	protected ISimpleValue loopFunction(List<ISimpleValue> p_arguments, EvaluationContext p_context) {
		for (final ISimpleValue l_currentFunction : p_arguments.subList(1, p_arguments.size())) {
			final ISimpleValue l_testValue = l_currentFunction.evaluate(p_context);
			final Object l_next = bestClass(l_testValue, p_context);
			switch (f_type) {
			case INTEGER:
				f_value = processInteger((int) f_value, (int) l_next);
				break;
			case FLOAT:
				f_value = processFloat((float) f_value, (float) l_next);
				break;
			case LONG:
				f_value = processLong((long) f_value, (long) l_next);
				break;
			case DOUBLE:
				f_value = processDouble((double) f_value, (double) l_next);
				break;
			default:
				throw new UnsupportedOperationException("Unable to process math functions on non-numeric values");
			}
			if (f_value == null) {
				break;
			}
		}
		return f_value != null ? Type.TRUE : Type.FALSE;
	}

}
