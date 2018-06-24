/**
 *
 */
package com.airsltd.core.lisp.function.flow;

import java.util.Iterator;
import java.util.List;

import com.airsltd.core.lisp.function.AbstractFunction;
import com.airsltd.core.lisp.function.EvaluationContext;
import com.airsltd.core.lisp.function.ISimpleValue;
import com.airsltd.core.lisp.function.Variable;
import com.airsltd.core.lisp.function.exception.InvalidArgument;
import com.airsltd.core.lisp.function.exception.InvalidParameters;
import com.airsltd.core.lisp.function.type.Type;

/**
 * @author jon_000
 *
 */
public class Let extends AbstractFunction {

	private static final int MINARGUMENTS = 2;

	public Let() {
		super("LET");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.airsltd.aga.ranking.core.function.IFunction#evaluate(java.util.List)
	 */
	@Override
	public ISimpleValue evaluate(List<ISimpleValue> p_arguments, EvaluationContext p_context) {
		if (p_arguments.size() < MINARGUMENTS) {
			throw new InvalidParameters("Let needs a set of variables to set and a body.");
		}
		final EvaluationContext l_context = new EvaluationContext(p_context);
		@SuppressWarnings("unchecked")
		final
		List<ISimpleValue> l_variables = Type.toType(List.class, p_arguments.get(0), p_context);
		if ((l_variables.size() & 1) != 0) {
			throw new InvalidParameters("All Variables defined in a Let must have an initial value defined.");
		}
		final Iterator<ISimpleValue> l_iterator = l_variables.iterator();
		while (l_iterator.hasNext()) {
			final ISimpleValue l_variable = l_iterator.next();
			if (!(l_variable instanceof Variable)) {
				throw new InvalidArgument("Let works only on variables");
			}
			final String l_variableName = ((Variable) l_variable).getName();
			final ISimpleValue l_initial = l_iterator.next().evaluate(p_context);
			l_context.createValue(l_variableName, l_initial);
		}
		return AbstractFunction.getFunction("BODY").evaluate(p_arguments.subList(1, p_arguments.size()), l_context);
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

	@Override
	public String getFunctionName() {
		return "Let";
	}

}
