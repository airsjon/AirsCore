package com.airsltd.core.lisp.function;

import java.util.Arrays;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;

import com.airsltd.core.lisp.function.type.Type;

public class MovingAverageValue extends LispValue implements Iterable<ISimpleValue> {

	private Deque<ISimpleValue> f_series = new LinkedList<>();
	private int f_size;

	public MovingAverageValue(Integer p_type) {
		f_size = p_type;
	}

	@Override
	public Type getType(EvaluationContext p_context) {
		return Type.MOVINGAVERAGE;
	}

	public void addValue(ISimpleValue p_value) {
		if (f_series.size() == f_size) {
			f_series.pollFirst();
		}
		f_series.addLast(p_value);
	}

	public ISimpleValue aggregate(FunctionValue p_function, EvaluationContext p_context) {
		ISimpleValue l_retVal = Type.functionInteger(1);
		for (final ISimpleValue l_value : f_series) {
			final FunctionCall l_functionCall = new FunctionCall(p_function, Arrays.asList(l_retVal, l_value));
			l_retVal = l_functionCall.evaluate(p_context);
		}
		return l_retVal;
	}

	public ISimpleValue full() {
		return f_size == f_series.size() ? Type.TRUE : Type.FALSE;
	}

	/**
	 * @return the series
	 */
	public Deque<ISimpleValue> getSeries() {
		return f_series;
	}

	/**
	 * @param p_series
	 *            the series to set
	 */
	public void setSeries(Deque<ISimpleValue> p_series) {
		f_series = p_series;
	}

	/**
	 * @return the size
	 */
	public int getSize() {
		return f_size;
	}

	/**
	 * @param p_size
	 *            the size to set
	 */
	public void setSize(int p_size) {
		f_size = p_size;
	}

	public void put(int p_index, ISimpleValue p_value) {
		if (p_index < f_series.size()) {
			((LinkedList<ISimpleValue>) f_series).set(p_index, p_value);
		}
	}

	@Override
	public Iterator<ISimpleValue> iterator() {
		return f_series.iterator();
	}

}
