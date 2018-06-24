/**
 *
 */
package com.airsltd.core.lisp.function.movingaverage;

import java.util.Arrays;
import java.util.List;

import com.airsltd.core.lisp.function.AbstractFunction;
import com.airsltd.core.lisp.function.EvaluationContext;
import com.airsltd.core.lisp.function.ISimpleValue;
import com.airsltd.core.lisp.function.MovingAverageValue;
import com.airsltd.core.lisp.function.type.Type;

/**
 * @author jon_000
 *
 */
public class Memory extends AbstractFunction {

	public Memory() {
		super("MEMORY");
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
		final List<ISimpleValue> l_args = translate(Arrays.asList(Type.MOVINGAVERAGE, null), p_arguments, p_context);
		((MovingAverageValue) l_args.get(0)).addValue(l_args.get(1));
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.airsltd.aga.ranking.core.function.IFunction#getFunctionName()
	 */
	@Override
	public String getFunctionName() {
		return "Memory";
	}

}
