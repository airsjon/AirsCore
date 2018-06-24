/**
 *
 */
package com.airsltd.core.data.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This class will be persisted using the Airs Data system.
 *
 * @author Jon Boley
 *
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface AirsPersistentClass {
	/**
	 * Style informs the data system how to convert field names to database
	 * names.
	 * 
	 * @return
	 */
	FieldStyle style() default FieldStyle.LOWERCASE;

	/**
	 * The name of the table the data is stored in.
	 *
	 * @return the SQL table name holding the data.
	 */
	String table();

	/**
	 * The fields that are keys for this data.
	 * <p>
	 * If a bit is set then the nth (where n is the position of the set bit)
	 * field is part of the key for this data.
	 *
	 * @return a long representing the key fields for this data.
	 *
	 */
	long keys();

	/**
	 * The fields that are used to sort this data.
	 *
	 * @return a long array representing which keys are used for sorting.
	 */
	int[]sort() default {};

}
