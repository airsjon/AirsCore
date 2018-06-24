/**
 *
 */
package com.airsltd.core.lisp.function.logical;

import java.util.List;

import com.airsltd.core.lisp.function.AbstractFunction;
import com.airsltd.core.lisp.function.EvaluationContext;
import com.airsltd.core.lisp.function.ISimpleValue;
import com.airsltd.core.lisp.function.exception.InvalidParameters;
import com.airsltd.core.lisp.function.type.Type;

/**
 * @author jon_000
 *
 */
public class Not extends AbstractFunction {

	public Not() {
		super("NOT");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.airsltd.aga.ranking.core.function.IFunction#evaluate(java.util.List)
	 */
	@Override
	public ISimpleValue evaluate(List<ISimpleValue> p_arguments, EvaluationContext p_context) {
		if (p_arguments.size() != 1) {
			throw new InvalidParameters("Not needs one argument");
		}
		final List<ISimpleValue> l_translatedValues = translate(Type.BOOLEAN, p_arguments, p_context);
		final boolean l_value = Type.TRUE.equals(l_translatedValues.get(0));
		return l_value ? Type.FALSE : Type.TRUE;
	}

	@Override
	public String getFunctionName() {
		return "Not";
	}

}
