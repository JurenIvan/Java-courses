package hr.fer.zemris.java.hw05.db;

/**
 * Class whose static constant represent implementations of
 * {@link IFieldValueGetter} interface.
 * 
 * @author juren
 *
 */
public class FieldValueGetters {
	/**
	 * Constant used to extract appropriate field (Name describes which field) from
	 * Student Record. Returns String stored in field.
	 */
	public static final IFieldValueGetter FIRST_NAME, LAST_NAME, JMBAG;

	static {
		FIRST_NAME = t -> t.getName();
		LAST_NAME = t -> t.getSurname();
		JMBAG = t -> t.getJmbag();
	}
}
