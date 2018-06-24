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
public class Full extends AbstractFunction {

	public Full() {
		super("FULL");
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
		final List<ISimpleValue> l_args = translate(Arrays.asList(Type.MOVINGAVERAGE), p_arguments, p_context);
		return ((MovingAverageValue) l_args.get(0)).full();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.airsltd.aga.ranking.core.function.IFunction#getFunctionName()
	 */
	@Override
	public String getFunctionName() {
		return "Full";
	}

}
