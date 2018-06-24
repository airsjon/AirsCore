/**
 *
 */
package com.airsltd.core.lisp.function.math;

/**
 * @author jon_000
 *
 */
public class Add extends MathFunction {

	public Add() {
		super("ADD");
	}

	@Override
	protected Integer processInteger(int p_value, int p_next) {
		return p_value + p_next;
	}

	@Override
	protected Float processFloat(float p_value, float p_next) {
		return p_value + p_next;
	}

	@Override
	public String getFunctionName() {
		return "Add";
	}

	@Override
	protected Double processDouble(double p_value, double p_next) {
		return p_value + p_next;
	}

	@Override
	protected Long processLong(long p_value, long p_next) {
		return p_value + p_next;
	}

}
