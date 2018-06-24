/**
 *
 */
package com.airsltd.core.lisp.function;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A Function Call.
 * <p>
 * Lisp format: (<function> <arg> ...) Each argument is evaluated before the
 * function is called.
 *
 * @author Jon Boley
 *
 */
public class FunctionCall extends SimpleValue {

	private static Boolean s_tracing = false;
	private static final Log LOGGER = LogFactory.getLog(FunctionCall.class);

	private IFunction f_function;
	private List<ISimpleValue> f_arguments;

	public FunctionCall(IFunction p_function, List<ISimpleValue> p_arguments) {
		super();
		f_function = p_function;
		f_arguments = p_arguments;
	}

	public FunctionCall(FunctionValue p_function, List<ISimpleValue> p_arguments) {
		this(p_function.getFunction(), p_arguments);
	}

	public FunctionCall(IFunction p_function, ISimpleValue... p_arguments) {
		this(p_function, Arrays.asList(p_arguments));
	}

	public FunctionCall(FunctionValue p_function, ISimpleValue... p_arguments) {
		this(p_function.getFunction(), Arrays.asList(p_arguments));
	}

	/**
	 * @return the tracing
	 */
	public static Boolean getTracing() {
		return s_tracing;
	}

	/**
	 * @param p_tracing
	 *            the tracing to set
	 */
	public static void setTracing(Boolean p_tracing) {
		s_tracing = p_tracing;
	}

	/**
	 * @return the log
	 */
	public static Log getLog() {
		return LOGGER;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.airsltd.aga.ranking.core.function.ISimpleValue#evaluate()
	 */
	@Override
	public ISimpleValue evaluate(EvaluationContext p_context) {
		if (s_tracing) {
			LOGGER.info("Function call " + this);
		}
		final ISimpleValue l_retVal = f_function.evaluate(f_arguments, p_context);
		if (l_retVal == null) {
			setValue(null);
			setType(null);
		} else {
			setValue(l_retVal.getValue(p_context));
			setType(l_retVal.getType(p_context));
		}
		if (s_tracing) {
			LOGGER.info("Function call " + this + " returns: " + l_retVal);
		}
		return l_retVal;
	}

	@Override
	public String toString() {
		String l_retVal = "(" + f_function;
		for (final ISimpleValue l_arg : f_arguments) {
			l_retVal += " " + l_arg;
		}
		return l_retVal + ")";
	}

	/**
	 * @return the function
	 */
	public IFunction getFunction() {
		return f_function;
	}

	/**
	 * @param p_function
	 *            the function to set
	 */
	public void setFunction(IFunction p_function) {
		f_function = p_function;
	}

	/**
	 * @return the arguments
	 */
	public List<ISimpleValue> getArguments() {
		return f_arguments;
	}

	/**
	 * @param p_arguments
	 *            the arguments to set
	 */
	public void setArguments(List<ISimpleValue> p_arguments) {
		f_arguments = p_arguments;
	}

}
