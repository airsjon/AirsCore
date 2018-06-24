/**
 *
 */
package com.airsltd.core.lisp.function.flow;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.airsltd.core.lisp.function.AbstractFunction;
import com.airsltd.core.lisp.function.EvaluationContext;
import com.airsltd.core.lisp.function.FunctionValue;
import com.airsltd.core.lisp.function.ISimpleValue;
import com.airsltd.core.lisp.function.Variable;
import com.airsltd.core.lisp.function.exception.InvalidArgument;
import com.airsltd.core.lisp.function.type.Type;

/**
 * @author jon_000
 *
 */
public class Lambda extends AbstractFunction {

	public Lambda() {
		super("LAMBDA");
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
		final List<Variable> l_variables = new ArrayList<>();
		@SuppressWarnings("unchecked")
		final
		List<ISimpleValue> l_variableList = Type.toType(List.class, p_arguments.get(0), p_context);
		final Iterator<ISimpleValue> l_iterator = l_variableList.iterator();
		while (l_iterator.hasNext()) {
			final ISimpleValue l_variable = l_iterator.next();
			if (!(l_variable instanceof Variable)) {
				throw new InvalidArgument("Let works only on variables");
			}
			l_variables.add((Variable) l_variable);
		}

		return new FunctionValue(new LambdaClosure(l_variables, p_context, p_arguments.subList(1, p_arguments.size())));
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.airsltd.aga.ranking.core.function.AbstractFunction#ignoreFuntion(int)
	 */
	@Override
	public boolean ignoreFuntion(int p_i) {
		return p_i == 0;
	}

}
