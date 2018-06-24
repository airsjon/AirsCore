/**
 *
 */
package com.airsltd.core.lisp.function.flow;

import java.util.List;

import com.airsltd.core.lisp.function.AbstractFunction;
import com.airsltd.core.lisp.function.EvaluationContext;
import com.airsltd.core.lisp.function.ISimpleValue;

/**
 * A simple block of code.
 * <p>
 * The last line returns the value for the block of code.
 *
 * @author Jon Boley
 *
 */
public class Body extends AbstractFunction {

	public Body() {
		super("BODY");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.airsltd.aga.ranking.core.function.IFunction#evaluate(java.util.List)
	 */
	@Override
	public ISimpleValue evaluate(List<ISimpleValue> p_arguments, EvaluationContext p_context) {
		ISimpleValue l_retVal = null;
		for (final ISimpleValue l_currentFunction : p_arguments) {
			l_retVal = l_currentFunction.evaluate(p_context);
		}
		return l_retVal;
	}

	@Override
	public String getFunctionName() {
		return "Body";
	}

}
