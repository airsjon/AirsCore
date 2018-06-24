/**
 *
 */
package com.airsltd.core.lisp.function.type;

import java.util.Date;
import java.util.List;

import com.airsltd.core.lisp.function.EvaluationContext;
import com.airsltd.core.lisp.function.FunctionValue;
import com.airsltd.core.lisp.function.IFunction;
import com.airsltd.core.lisp.function.ISimpleValue;
import com.airsltd.core.lisp.function.IType;
import com.airsltd.core.lisp.function.SimpleValue;

/**
 * All Lisp types are represented by a Type.
 *
 * @author Jon Boley
 *
 */
public enum Type implements IType {

	STRING(new StringType(), String.class), LIST(new ListType(),
			List.class), MOVINGAVERAGE(new MovingAverageType()), FUNCTION(new FunctionType()), POJO(new PojoType(),
					Object.class), BOOLEAN(new BooleanType(), Boolean.class), INTEGER(new IntegerType(),
							Integer.class), DATE(new DateType(), Date.class), LONG(new LongType(), Long.class), FLOAT(
									new FloatType(), Float.class), DOUBLE(new DoubleType(), Double.class);

	private Class<?> f_javaType;
	private IConvertType f_convertMethod;

	Type(IConvertType p_convertMethod) {
		f_convertMethod = p_convertMethod;
	}

	Type(IConvertType p_convertMethod, Class<?> p_class) {
		this(p_convertMethod);
		f_javaType = p_class;
	}

	/**
	 * Lisp TRUE value
	 */
	public static final ISimpleValue TRUE = functionBoolean(true);
	/**
	 * Lisp FALSE value
	 */
	public static final ISimpleValue FALSE = functionBoolean(false);

	public static ISimpleValue functionInteger(int p_value) {
		return new SimpleValue(p_value, Type.INTEGER);
	}

	public static ISimpleValue functionLong(long p_value) {
		return new SimpleValue(p_value, Type.LONG);
	}

	public static ISimpleValue functionDate(Date p_date) {
		return new SimpleValue(p_date, Type.DATE);
	}

	public static ISimpleValue functionString(String p_value) {
		return new SimpleValue(p_value, Type.STRING);
	}

	public static ISimpleValue functionFloat(float p_value) {
		return new SimpleValue(p_value, Type.FLOAT);
	}

	public static ISimpleValue functionDouble(double p_value) {
		return new SimpleValue(p_value, Type.DOUBLE);
	}

	public static ISimpleValue functionBoolean(boolean p_value) {
		return new SimpleValue(p_value, Type.BOOLEAN);
	}

	public static ISimpleValue functionList(List<ISimpleValue> p_list) {
		return new SimpleValue(p_list, Type.LIST);
	}

	public static ISimpleValue function(IFunction p_function) {
		return new FunctionValue(p_function);
	}

	public static ISimpleValue functionPojo(Object p_pojo) {
		return new SimpleValue(p_pojo, Type.POJO);
	}

	@SuppressWarnings("unchecked")
	public static <T> T toType(Class<T> p_class, ISimpleValue p_value, EvaluationContext p_context) {
		T l_retVal = null;
		for (final Type l_type : Type.values()) {
			if (l_type.f_javaType == p_class) {
				l_retVal = (T) l_type.convert(p_value, p_context).getValue(p_context);
				break;
			}
		}
		return l_retVal;
	}

	@Override
	public ISimpleValue convert(ISimpleValue p_value, EvaluationContext p_context) {
		return f_convertMethod.convert(p_value, p_context);
	}

	public Type bestMathType(Type p_type) {
		Type l_retVal = this;
		if (p_type.ordinal() > ordinal()) {
			l_retVal = p_type;
		}
		if (l_retVal == DATE) {
			l_retVal = LONG;
		} else if (l_retVal.ordinal() < INTEGER.ordinal()) {
			l_retVal = INTEGER;
		}
		return l_retVal;
	}

}
