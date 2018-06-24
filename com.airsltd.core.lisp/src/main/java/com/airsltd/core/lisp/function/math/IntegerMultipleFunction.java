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
 * @author jon_000
 *
 */
public abstract class IntegerMultipleFunction extends AbstractFunction {

	private static final int MINARGUMENTS = 2;

	/**
	 * @param p_functionName
	 */
	public IntegerMultipleFunction(String p_functionName) {
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
		if (p_arguments.size() < MINARGUMENTS) {
			throw new InvalidParameters(getFunctionName() + " needs at least one parameter.");
		}
		int l_value = Type.toType(Integer.class, p_arguments.get(0).evaluate(p_context), p_context);
		for (final ISimpleValue l_nextValue : p_arguments.subList(1, p_arguments.size())) {
			l_value = modifyInteger(l_value, Type.toType(Integer.class, l_nextValue, p_context));
		}
		return Type.functionInteger(l_value);
	}

	protected abstract int modifyInteger(int p_value, int p_nextValue);

}
