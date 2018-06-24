/**
 *
 */
package com.airsltd.core.lisp.function.math;

import java.text.ParseException;
import java.util.List;

import com.airsltd.core.data.converters.BlockConverters;
import com.airsltd.core.lisp.function.AbstractFunction;
import com.airsltd.core.lisp.function.EvaluationContext;
import com.airsltd.core.lisp.function.ISimpleValue;
import com.airsltd.core.lisp.function.SimpleValue;
import com.airsltd.core.lisp.function.exception.InvalidArgument;
import com.airsltd.core.lisp.function.exception.InvalidParameters;
import com.airsltd.core.lisp.function.type.Type;

/**
 * @author jon_000
 *
 */
public abstract class MathFunction extends AbstractFunction {

	protected Type f_type;
	protected Object f_value;

	/**
	 * @param p_functionName
	 */
	public MathFunction(String p_functionName) {
		super(p_functionName);
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
		if (p_arguments.isEmpty()) {
			throw new InvalidParameters(getFunctionName() + " needs at least one parameter.");
		}
		f_type = Type.INTEGER;
		f_value = 0;
		final ISimpleValue l_value = p_arguments.get(0).evaluate(p_context);
		f_value = bestClass(l_value, p_context);
		return loopFunction(p_arguments, p_context);
	}

	protected ISimpleValue loopFunction(List<ISimpleValue> p_arguments, EvaluationContext p_context) {
		for (final ISimpleValue l_currentFunction : p_arguments.subList(1, p_arguments.size())) {
			final ISimpleValue l_testValue = l_currentFunction.evaluate(p_context);
			final Object l_next = bestClass(l_testValue, p_context);
			switch (f_type) {
			case INTEGER:
				f_value = processInteger((int) f_value, (int) l_next);
				break;
			case FLOAT:
				f_value = processFloat((float) f_value, (float) l_next);
				break;
			case LONG:
				f_value = processLong((long) f_value, (long) l_next);
				break;
			case DOUBLE:
				f_value = processDouble((double) f_value, (double) l_next);
				break;
			default:
				throw new UnsupportedOperationException("Unable to process math functions on non-numeric values");
			}
		}
		return new SimpleValue(f_value, f_type);
	}

	protected abstract Integer processInteger(int p_value, int p_next);

	protected abstract Float processFloat(float p_value, float p_next);

	protected abstract Double processDouble(double p_value, double p_next);

	protected abstract Long processLong(long p_value, long p_next);

	/**
	 * Compare the current type (f_type) with p_value's type and determine the
	 * best class
	 *
	 * @param p_value
	 * @param p_context
	 * @return
	 * @throws InvalidParameters
	 * @throws InvalidArgument
	 */
	protected Object bestClass(ISimpleValue p_value, EvaluationContext p_context) {
		Object l_retVal;
		final Type l_newType = f_type.bestMathType(closestType(p_value, p_context));
		if (l_newType != f_type) {
			f_value = l_newType.convert(new SimpleValue(f_value, f_type), p_context).getValue(p_context);
			f_type = l_newType;
		}
		l_retVal = f_type.convert(p_value, p_context).getValue(p_context);
		return l_retVal;
	}

	/**
	 * For string types we need to get best math type for that string.
	 *
	 * @param p_value
	 * @param p_context
	 * @return
	 */
	protected Type closestType(ISimpleValue p_value, EvaluationContext p_context) {
		Type l_type = p_value == null ? Type.INTEGER : p_value.getType(p_context);
		if (l_type == Type.STRING) {
			final String l_value = (String) p_value.getValue(p_context);
			try {
				BlockConverters.DATECONVERTER.fromSql(null, l_value);
				l_type = Type.DATE;
			} catch (final ParseException e) {
				try {
					final Double l_d = Double.valueOf(l_value);
					l_type = Type.DOUBLE;
					final Float l_f = Float.valueOf(l_value);
					l_type = infinityCheck(l_d, l_f);
					Long.parseLong(l_value);
					l_type = Type.LONG;
					Integer.parseInt(l_value);
					l_type = Type.INTEGER;
				} catch (final NumberFormatException l_nfe) {
				}
			}
		}
		return l_type;
	}

	private Type infinityCheck(Double p_d, Float p_f) {
		return p_d.isInfinite() || !p_f.isInfinite() ? Type.FLOAT : Type.DOUBLE;
	}

}
