/**
 *
 */
package com.airsltd.core.lisp.function;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.airsltd.core.lisp.function.exception.InvalidParameters;
import com.airsltd.core.lisp.function.type.Type;

/**
 * @author jon_000
 *
 */
public abstract class AbstractFunction implements IFunction {

	private static Map<String, IFunction> s_functionList = new HashMap<>();

	private final String f_functionName;

	public AbstractFunction(String p_functionName) {
		f_functionName = p_functionName;
		s_functionList.put(p_functionName, this);
	}

	public static IFunction getFunction(String p_functionName) {
		return s_functionList.get(p_functionName.toUpperCase());
	}

	/**
	 * Return the p_arguments as a list of elements converted to p_type.
	 * 
	 * @param p_parameters
	 * @param p_arguments
	 * @param p_context
	 * @return
	 */
	protected List<ISimpleValue> translate(List<Type> p_parameters, List<ISimpleValue> p_arguments,
			EvaluationContext p_context) {
		if (p_parameters.size() != p_arguments.size()) {
			throw new InvalidParameters("Functions requires " + p_arguments.size() + " arguments.");
		}
		final List<ISimpleValue> l_retVal = new ArrayList<ISimpleValue>();
		int l_index = 0;
		for (final ISimpleValue l_value : p_arguments) {
			final ISimpleValue l_argVal = l_value.evaluate(p_context);
			final IType l_typeWanted = p_parameters.get(l_index++);
			l_retVal.add(l_typeWanted != null ? l_typeWanted.convert(l_argVal, p_context) : l_argVal);
		}
		return l_retVal;
	}

	protected List<ISimpleValue> translate(IType p_type, List<ISimpleValue> p_arguments, EvaluationContext p_context) {
		final List<ISimpleValue> l_retVal = new ArrayList<ISimpleValue>();
		for (final ISimpleValue l_value : p_arguments) {
			l_retVal.add(p_type.convert(l_value.evaluate(p_context), p_context));
		}
		return l_retVal;
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

	@Override
	public String toString() {
		return f_functionName;
	}

}
