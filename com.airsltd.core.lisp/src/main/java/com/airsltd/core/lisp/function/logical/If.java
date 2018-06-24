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
public class If extends AbstractFunction {

	private static final int IFTHENARGS = 2;
	private static final int IFTHENELSEARGS = 3;
	private static final int IFARG = 0;
	private static final int THENARG = 1;
	private static final int ELSEARG = 2;

	public If() {
		super("IF");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.airsltd.aga.ranking.core.function.IFunction#evaluate(java.util.List)
	 */
	@Override
	public ISimpleValue evaluate(List<ISimpleValue> p_arguments, EvaluationContext p_context) {
		if (p_arguments.size() != IFTHENARGS && p_arguments.size() != IFTHENELSEARGS) {
			throw new InvalidParameters("If needs two or three arguments");
		}
		ISimpleValue l_value = null;
		if (Type.toType(Boolean.class, p_arguments.get(IFARG).evaluate(p_context), p_context)) {
			l_value = p_arguments.get(THENARG).evaluate(p_context);
		} else {
			if (p_arguments.size() == IFTHENELSEARGS) {
				l_value = p_arguments.get(ELSEARG).evaluate(p_context);
			}
		}
		return l_value;
	}

	@Override
	public String getFunctionName() {
		return "If";
	}

}
