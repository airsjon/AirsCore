/**
 *
 */
package com.airsltd.core.data;

import com.airsltd.core.data.annotations.AirsPersistentField;

/**
 * Annotated version of the AbstractPersistentId class.
 * <p>
 * Because we do not know the field name for the AutoIncrementField, we override
 * the generation of field names.
 *
 * @author Jon Boley
 *
 */
public abstract class AbstractPersistedIdData extends PersistedData implements IPersistentId {

	/**
	 * Auto increment field.
	 */
	@AirsPersistentField
	private AutoIncrementField f_internalId;

	public AbstractPersistedIdData(Class<?> p_class) {
		f_internalId = new AutoIncrementField(p_class);
	}

	public AbstractPersistedIdData(Class<?> p_class, String[] p_args) {
		this(p_class);
		fromStringCsv(p_args);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.airsltd.core.data.IPersistentId#getPersistentID()
	 */
	@Override
	public long getPersistentID() {
		if (f_internalId.getId() == AutoIncrementField.TORECORD) {
			addContent();
		}
		return f_internalId.getId();
	}

	/**
	 * @return the id
	 */
	public AutoIncrementField getInternalId() {
		return f_internalId;
	}

	/**
	 * @param p_id
	 *            the id to set
	 */
	public void setInternalId(AutoIncrementField p_id) {
		f_internalId = p_id;
	}

	public long getId() {
		return f_internalId.getId();
	}

	public void setId(long p_id) {
		f_internalId.setId(p_id);
	}

	protected abstract void addContent();

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.airsltd.core.data.AbstractBlockData#dataAdded()
	 */
	@Override
	public void dataAdded() {
		if (f_internalId.getId() == AutoIncrementField.TORECORD) {
			f_internalId.setId(AutoIncrementField.RECORDED);
		}
		super.dataAdded();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.airsltd.core.data.PersistedData#loadFields()
	 */
	@Override
	public void loadFields() {
		super.loadFields();
		getFieldNames()[getIdFieldIndex()] = getIdFieldName();
	}

	protected int getIdFieldIndex() {
		return 0;
	}

	protected abstract String getIdFieldName();

}
