/**
 *
 */
package com.airsltd.core.lisp.function;

import com.airsltd.core.lisp.function.type.Type;

/**
 * @author Jon Boley
 *
 */
public class FunctionValue extends LispValue {

	private final IFunction f_function;

	public FunctionValue(IFunction p_function) {
		f_function = p_function;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.airsltd.aga.ranking.core.function.ISimpleValue#getType(com.airsltd.
	 * aga.ranking.core.function.EvaluationContext)
	 */
	@Override
	public Type getType(EvaluationContext p_context) {
		return Type.FUNCTION;
	}

	public IFunction getFunction() {
		return f_function;
	}
}
