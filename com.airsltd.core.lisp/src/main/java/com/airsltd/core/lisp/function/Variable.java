/**
 *
 */
package com.airsltd.core.lisp.function;

/**
 * @author jon_000
 *
 */
public class Variable extends SimpleValue {

	private String f_name;

	public Variable(String p_name) {
		f_name = p_name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.airsltd.aga.ranking.core.function.SimpleValue#evaluate(com.airsltd.
	 * aga.ranking.core.function.EvaluationContext)
	 */
	@Override
	public ISimpleValue evaluate(EvaluationContext p_context) {
		final ISimpleValue l_retVal = p_context.getValue(f_name);
		if (FunctionCall.getTracing()) {
			FunctionCall.getLog().info(f_name + ": " + l_retVal);
		}
		return l_retVal;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return f_name;
	}

	/**
	 * @param p_name
	 *            the name to set
	 */
	public void setName(String p_name) {
		f_name = p_name;
	}

	@Override
	public String toString() {
		return f_name;
	}

}
