/**
 *
 */
package com.airsltd.core.lisp.function.flow;

import java.util.List;

import com.airsltd.core.lisp.function.AbstractFunction;
import com.airsltd.core.lisp.function.EvaluationContext;
import com.airsltd.core.lisp.function.IFunction;
import com.airsltd.core.lisp.function.ISimpleValue;
import com.airsltd.core.lisp.function.Variable;

/**
 * @author jon_000
 *
 */
public class LambdaClosure implements IFunction {

	private final EvaluationContext f_context;
	private final List<ISimpleValue> f_body;
	private final List<Variable> f_parameters;

	public LambdaClosure(List<Variable> p_parameters, EvaluationContext p_context, List<ISimpleValue> p_subList) {
		f_context = p_context;
		f_body = p_subList;
		f_parameters = p_parameters;
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
		final EvaluationContext l_context = new EvaluationContext(f_context);
		int l_argIndex = 0;
		for (final Variable l_variable : f_parameters) {
			if (l_argIndex < p_arguments.size()) {
				l_context.createValue(l_variable.getName(), p_arguments.get(l_argIndex++));
			} else {
				l_context.createValue(l_variable.getName(), null);
			}
		}
		return AbstractFunction.getFunction("BODY").evaluate(f_body, l_context);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.airsltd.aga.ranking.core.function.IFunction#ignoreFuntion(int)
	 */
	@Override
	public boolean ignoreFuntion(int p_i) {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.airsltd.aga.ranking.core.function.IFunction#getFunctionName()
	 */
	@Override
	public String getFunctionName() {
		return "Lambda";
	}

}
