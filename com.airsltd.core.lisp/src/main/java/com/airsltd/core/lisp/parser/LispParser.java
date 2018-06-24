/**
 *
 */
package com.airsltd.core.lisp.parser;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Deque;
import java.util.Map;

import com.airsltd.core.lisp.function.AbstractFunction;
import com.airsltd.core.lisp.function.FunctionCall;
import com.airsltd.core.lisp.function.IFunction;
import com.airsltd.core.lisp.function.ISimpleValue;
import com.airsltd.core.lisp.function.Variable;
import com.airsltd.core.lisp.function.comparison.GreaterThan;
import com.airsltd.core.lisp.function.comparison.GreaterThanEqual;
import com.airsltd.core.lisp.function.comparison.LessThan;
import com.airsltd.core.lisp.function.comparison.LessThanEqual;
import com.airsltd.core.lisp.function.comparison.LispEquals;
import com.airsltd.core.lisp.function.flow.Body;
import com.airsltd.core.lisp.function.flow.Lambda;
import com.airsltd.core.lisp.function.flow.Let;
import com.airsltd.core.lisp.function.logical.And;
import com.airsltd.core.lisp.function.logical.If;
import com.airsltd.core.lisp.function.logical.IsNull;
import com.airsltd.core.lisp.function.logical.Not;
import com.airsltd.core.lisp.function.logical.Or;
import com.airsltd.core.lisp.function.logical.Xor;
import com.airsltd.core.lisp.function.math.Add;
import com.airsltd.core.lisp.function.math.BitwiseAnd;
import com.airsltd.core.lisp.function.math.BitwiseOr;
import com.airsltd.core.lisp.function.math.BitwiseXor;
import com.airsltd.core.lisp.function.math.Decrement;
import com.airsltd.core.lisp.function.math.Divide;
import com.airsltd.core.lisp.function.math.Increment;
import com.airsltd.core.lisp.function.math.Multiply;
import com.airsltd.core.lisp.function.math.Subtract;
import com.airsltd.core.lisp.function.math.Threshold;
import com.airsltd.core.lisp.function.movingaverage.Aggregate;
import com.airsltd.core.lisp.function.movingaverage.Full;
import com.airsltd.core.lisp.function.movingaverage.Memory;
import com.airsltd.core.lisp.function.movingaverage.MovingAverage;
import com.airsltd.core.lisp.function.type.Type;
import com.airsltd.core.lisp.function.variable.DefaultValue;
import com.airsltd.core.lisp.function.variable.Set;
import com.airsltd.core.parse.ParseRule;
import com.airsltd.core.parse.ParseRules;
import com.airsltd.core.parse.ParseToken;
import com.airsltd.core.parse.Parser;

/**
 * @author jon_000
 *
 */
public class LispParser extends Parser {

	private static LispParser s_instance;

	static {
		new Add();
		new Subtract();
		new Divide();
		new Multiply();
		new BitwiseAnd();
		new BitwiseOr();
		new BitwiseXor();
		new Decrement();
		new Increment();
		new Body();
		new Let();
		new LessThan();
		new LessThanEqual();
		new GreaterThan();
		new GreaterThanEqual();
		new And();
		new Set();
		new If();
		new Not();
		new Xor();
		new Or();
		new LispEquals();
		new MovingAverage();
		new Aggregate();
		new Memory();
		new Lambda();
		new Full();
		new DefaultValue();
		new IsNull();
		new Threshold();
	}

	protected static final List<ParseRule> LISPPRULES = Arrays.asList(new ReadListRule(), new TokenRule(),
			ParseRules.STRINGRULE, ParseRules.FLOATRULE, ParseRules.INTEGERRULE, ParseRules.LINECOMMENTPARSE,
			ParseRules.MULTILINECOMMENTPARSE);
	private static Map<String, ISimpleValue> s_constants;

	/**
	 * @param p_rules
	 */
	public LispParser() {
		super(LISPPRULES);
		setInstance(this);
	}

	/**
	 * @param p_parser
	 */
	public LispParser(Parser p_parser) {
		super(p_parser);
	}

	public static ISimpleValue parseString(String p_string) {
		return getInstance().parse(p_string);
	}

	public static LispParser getInstance() {
		if (s_instance == null) {
			new LispParser();
		}
		return s_instance;
	}

	public static void setInstance(LispParser p_parser) {
		s_instance = p_parser;
	}

	private ISimpleValue parse(String p_string) {
		final Deque<ParseToken> l_stack = processTokens(p_string);
		final List<ParseToken> l_list = new ArrayList<ParseToken>(l_stack);
		return new FunctionCall(AbstractFunction.getFunction("BODY"), buildProgram(new ArrayDeque<ParseToken>(l_list)));
	}

	private List<ISimpleValue> buildProgram(Deque<ParseToken> p_stack) {
		final List<ISimpleValue> l_value = new ArrayList<ISimpleValue>();
		while (!p_stack.isEmpty()) {
			l_value.add(processTokens(p_stack.pop(), p_stack, false));
		}
		return l_value;
	}

	private ISimpleValue processTokens(ParseToken p_pop, Deque<ParseToken> p_stack, boolean p_ignoreFunction) {
		ISimpleValue l_retVal = null;
		switch (p_pop.getText()) {
		case ":list":
			l_retVal = parseList(p_stack, p_ignoreFunction);
			break;
		case ":token":
			l_retVal = processTokenText(p_stack.pop().getText());
			break;
		case ":string":
			l_retVal = Type.functionString(p_stack.pop().getText());
			break;
		case ":integer":
			final int l_int = Integer.parseInt(p_stack.pop().getText());
			l_retVal = Type.functionInteger(l_int);
			break;
		case ":float":
			final float l_float = Float.parseFloat(p_stack.pop().getText());
			l_retVal = Type.functionFloat(l_float);
			break;
		default:
		}
		return l_retVal;
	}

	private ISimpleValue processTokenText(String p_text) {
		ISimpleValue l_retVal = null;
		final Map<String, ISimpleValue> l_constants = getConstants();
		if (l_constants.containsKey(p_text)) {
			l_retVal = l_constants.get(p_text);
		} else {
			l_retVal = new Variable(p_text);
		}
		return l_retVal;
	}

	private Map<String, ISimpleValue> getConstants() {
		if (s_constants == null) {
			generateConstants();
		}
		return s_constants;
	}

	private void generateConstants() {
		s_constants = new HashMap<String, ISimpleValue>();
		s_constants.put("null", null);
		s_constants.put("false", Type.FALSE);
		s_constants.put("true", Type.TRUE);
	}

	private ISimpleValue parseList(Deque<ParseToken> p_stack, boolean p_ignoreFunction) {
		ISimpleValue l_retVal = null;
		boolean l_function = false;
		IFunction l_funtionToCall = null;
		if (!p_ignoreFunction && ":token".equals(p_stack.peek().getText())) {
			p_stack.pop();
			l_function = true;
			final String l_functionName = p_stack.pop().getText();
			l_funtionToCall = AbstractFunction.getFunction(l_functionName);
			if (l_funtionToCall == null) {
				throw new UnknownFunctionException("Unable to find function: " + l_functionName);
			}
		}
		final List<ISimpleValue> l_list = new ArrayList<ISimpleValue>();
		int l_index = 0;
		while (!":eoList".equals(p_stack.peek().getText())) {
			l_list.add(processTokens(p_stack.pop(), p_stack,
					l_funtionToCall != null ? l_funtionToCall.ignoreFuntion(l_index++) : false));
		}
		p_stack.pop();
		if (l_function) {
			l_retVal = new FunctionCall(l_funtionToCall, l_list);
		} else {
			l_retVal = Type.functionList(l_list);
		}
		return l_retVal;
	}

}
