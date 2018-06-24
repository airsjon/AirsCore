/**
 *
 */
package com.airsltd.core.lisp.function.logical;

import java.util.List;

import com.airsltd.core.lisp.function.AbstractFunction;
import com.airsltd.core.lisp.function.EvaluationContext;
import com.airsltd.core.lisp.function.ISimpleValue;
import com.airsltd.core.lisp.function.type.Type;

/**
 * @author jon_000
 *
 */
public class And extends AbstractFunction {

	public And() {
		super("AND");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.airsltd.aga.ranking.core.function.IFunction#evaluate(java.util.List)
	 */
	@Override
	public ISimpleValue evaluate(List<ISimpleValue> p_arguments, EvaluationContext p_context) {
		boolean l_value = true;
		for (final ISimpleValue l_currentFunction : p_arguments) {
			if (!Type.toType(Boolean.class, l_currentFunction.evaluate(p_context), p_context)) {
				l_value = false;
				break;
			}
		}
		return l_value ? Type.TRUE : Type.FALSE;
	}

	@Override
	public String getFunctionName() {
		return "And";
	}

}
