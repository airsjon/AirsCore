/**
 *
 */
package com.airsltd.core.lisp.function.variable;

import java.util.List;

import com.airsltd.core.lisp.function.AbstractFunction;
import com.airsltd.core.lisp.function.EvaluationContext;
import com.airsltd.core.lisp.function.ISimpleValue;
import com.airsltd.core.lisp.function.Variable;
import com.airsltd.core.lisp.function.exception.InvalidParameters;
import com.airsltd.core.lisp.function.type.Type;

/**
 * @author jon_000
 *
 */
public class Set extends AbstractFunction {

	private static final int NUMARGUMENTS = 2;

	public Set() {
		super("SET");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.airsltd.aga.ranking.core.function.IFunction#evaluate(java.util.List)
	 */
	@Override
	public ISimpleValue evaluate(List<ISimpleValue> p_arguments, EvaluationContext p_context) {
		if (p_arguments.size() != NUMARGUMENTS) {
			throw new InvalidParameters("Set Variable needs two arguments");
		}
		final ISimpleValue l_arg1 = p_arguments.get(0);
		final String l_variableName = l_arg1 instanceof Variable ? ((Variable) l_arg1).getName()
				: Type.toType(String.class, p_arguments.get(0).evaluate(p_context), p_context);
		final ISimpleValue l_newValue = p_arguments.get(1).evaluate(p_context);
		p_context.setValue(l_variableName, l_newValue);
		return l_newValue;
	}

	@Override
	public String getFunctionName() {
		return "Set";
	}

}
