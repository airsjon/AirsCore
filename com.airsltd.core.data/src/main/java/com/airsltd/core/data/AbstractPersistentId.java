/**
 *
 */
package com.airsltd.core.data;

/**
 * Deprecated. Use {@link AbstractPersistedIdData}.
 *
 * @author Jon Boley
 *
 */
public abstract class AbstractPersistentId extends AbstractBlockData implements IPersistentId {

	/**
	 * Auto increment field.
	 */
	private AutoIncrementField f_id;

	public AbstractPersistentId(Class<?> p_class) {
		f_id = new AutoIncrementField(p_class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.airsltd.core.data.IPersistentId#getPersistentID()
	 */
	@Override
	public long getPersistentID() {
		if (f_id.getId() == AutoIncrementField.TORECORD) {
			addContent();
		}
		return f_id.getId();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.airsltd.core.data.IBlockData#autoIncrementField(int, long)
	 */
	@Override
	public void autoIncrementField(int p_key, long p_id) {
		if (p_key == 0) {
			f_id.setId(p_id);
		}
	}

	/**
	 * @return the id
	 */
	public AutoIncrementField getId() {
		return f_id;
	}

	/**
	 * @param p_id
	 *            the id to set
	 */
	public void setId(AutoIncrementField p_id) {
		f_id = p_id;
	}

	protected abstract void addContent();

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.airsltd.core.data.AbstractBlockData#dataAdded()
	 */
	@Override
	public void dataAdded() {
		if (f_id.getId() == AutoIncrementField.TORECORD) {
			f_id.setId(AutoIncrementField.RECORDED);
		}
		super.dataAdded();
	}

}
