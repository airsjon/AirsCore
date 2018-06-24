/**
 *
 */
package com.airsltd.core.lisp.function.math;

import java.util.List;

import com.airsltd.core.lisp.function.AbstractFunction;
import com.airsltd.core.lisp.function.EvaluationContext;
import com.airsltd.core.lisp.function.ISimpleValue;
import com.airsltd.core.lisp.function.exception.InvalidParameters;
import com.airsltd.core.lisp.function.type.Type;

/**
 * Apply a math function to a single integer.
 *
 * @author Jon Boley
 * @see Decrement
 * @see Increment
 *
 */
public abstract class IntegerSingleFunction extends AbstractFunction {

	public IntegerSingleFunction(String p_functionName) {
		super(p_functionName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.airsltd.aga.ranking.core.function.IFunction#evaluate(java.util.List,
	 * com.airsltd.aga.ranking.core.function.EvaluationContext)
	 */
	@Override
	public ISimpleValue evaluate(List<ISimpleValue> p_arguments, EvaluationContext p_context) {
		if (p_arguments.size() != 1) {
			throw new InvalidParameters(getFunctionName() + " needs one parameter.");
		}
		final int l_value = Type.toType(Integer.class, p_arguments.get(0).evaluate(p_context), p_context);
		return Type.functionInteger(modifyInteger(l_value));
	}

	protected abstract int modifyInteger(int p_value);

}
