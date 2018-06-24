package com.airsltd.core.lisp.function.movingaverage;

import java.util.List;

import com.airsltd.core.lisp.function.AbstractFunction;
import com.airsltd.core.lisp.function.EvaluationContext;
import com.airsltd.core.lisp.function.ISimpleValue;
import com.airsltd.core.lisp.function.MovingAverageValue;
import com.airsltd.core.lisp.function.exception.InvalidParameters;
import com.airsltd.core.lisp.function.type.Type;

public class MovingAverage extends AbstractFunction {

	public MovingAverage() {
		super("MOVINGAVERAGE");
	}

	@Override
	public ISimpleValue evaluate(List<ISimpleValue> p_arguments, EvaluationContext p_context) {
		if (p_arguments.size() != 1) {
			throw new InvalidParameters("Moving Average needs an integer argument.");
		}
		final List<ISimpleValue> l_args = translate(Type.INTEGER, p_arguments, p_context);
		return new MovingAverageValue(Type.toType(Integer.class, l_args.get(0), p_context));
	}

	@Override
	public String getFunctionName() {
		return "MovingAverage";
	}

}
