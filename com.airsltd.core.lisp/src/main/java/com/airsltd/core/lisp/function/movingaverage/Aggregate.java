/**
 *
 */
package com.airsltd.core.lisp.function.movingaverage;

import java.util.Arrays;
import java.util.List;

import com.airsltd.core.lisp.function.AbstractFunction;
import com.airsltd.core.lisp.function.EvaluationContext;
import com.airsltd.core.lisp.function.FunctionValue;
import com.airsltd.core.lisp.function.ISimpleValue;
import com.airsltd.core.lisp.function.MovingAverageValue;
import com.airsltd.core.lisp.function.type.Type;

/**
 * Aggregate takes a Function <code>f</code> and applies it to each member of {@link MovingAverageValue} <code>y</code>.
 * <p>
 * The Function will be passed two values.
 * The first value is the current value of the Aggregate.
 * The second value is the iterated value of <code>y</code>.
 * 
 * @author Jon Boley
 *
 */
public class Aggregate extends AbstractFunction {

	public Aggregate() {
		super("AGGREGATE");
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
		final List<ISimpleValue> l_args = translate(Arrays.asList(Type.MOVINGAVERAGE, Type.FUNCTION), p_arguments, p_context);
		return ((MovingAverageValue) l_args.get(0).getValue(p_context)).aggregate((FunctionValue) l_args.get(1),
				p_context);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.airsltd.aga.ranking.core.function.IFunction#getFunctionName()
	 */
	@Override
	public String getFunctionName() {
		return "AGGREGATE";
	}

}
