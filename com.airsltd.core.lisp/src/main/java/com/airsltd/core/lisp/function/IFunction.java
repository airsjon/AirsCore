/**
 *
 */
package com.airsltd.core.lisp.function;

import java.util.List;

/**
 * @author jon_000
 *
 */
public interface IFunction {

	ISimpleValue evaluate(List<ISimpleValue> p_arguments, EvaluationContext p_context);

	boolean ignoreFuntion(int p_i);

	String getFunctionName();

}
