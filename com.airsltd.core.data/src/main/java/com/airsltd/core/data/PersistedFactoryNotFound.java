/**
 *
 */
package com.airsltd.core.data;

/**
 * @author jon
 *
 */
public class PersistedFactoryNotFound extends RuntimeException {

	/**
	 *
	 */
	private static final long serialVersionUID = -2599965807593339735L;
	private Class<?> f_missingClass;

	public PersistedFactoryNotFound(Class<?> p_type) {
		super("Unable to create a field factory for type: " + p_type.getSimpleName());
		setMissingClass(p_type);
	}

	public Class<?> getMissingClass() {
		return f_missingClass;
	}

	public void setMissingClass(Class<?> p_missingClass) {
		f_missingClass = p_missingClass;
	}

}
