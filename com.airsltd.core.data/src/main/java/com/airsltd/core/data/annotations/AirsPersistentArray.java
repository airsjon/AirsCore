/**
 *
 */
package com.airsltd.core.data.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A field that is annotated as an Array will replicate the next n fields as an
 * array.
 *
 * @author Jon Boley
 *
 * @see AirsPersistentField
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface AirsPersistentArray {
	/**
	 * The name of the fields in the database.
	 * <p>
	 * If not field names are specified, the name of the field is used to
	 * generate the names.
	 * 
	 * @return
	 */
	String[]fieldNames() default {};

	/**
	 * The number of fields to convert into an array.
	 *
	 * @return
	 */
	int fields() default -1;

}
