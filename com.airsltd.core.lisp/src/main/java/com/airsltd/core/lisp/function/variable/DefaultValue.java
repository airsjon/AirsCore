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
 * Return the current value of the first argument, if one does not exist return
 * the default value.
 * <p>
 * Often used with a let statement to create a named argument interface. (Let (x
 * (defualtValue x 3.2)) ...) would set x to its current value or 3.2 if it does
 * not exist. Major issue is that you can't change the value x that is hidden
 * with code in the let statement.
 *
 * @author Jon Boley
 *
 */
public class DefaultValue extends AbstractFunction {

	private static final int NUMARGUMENTS = 2;

	public DefaultValue() {
		super("DEFAULTVALUE");
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
			throw new InvalidParameters("DefaultValue needs two arguments");
		}
		final ISimpleValue l_arg1 = p_arguments.get(0);
		final String l_variableName = l_arg1 instanceof Variable ? ((Variable) l_arg1).getName()
				: Type.toType(String.class, p_arguments.get(0).evaluate(p_context), p_context);
		ISimpleValue l_retVal = p_context.getValue(l_variableName);
		if (l_retVal == null) {
			l_retVal = p_arguments.get(1).evaluate(p_context);
		}
		return l_retVal;
	}

	@Override
	public String getFunctionName() {
		return "Set";
	}

}
