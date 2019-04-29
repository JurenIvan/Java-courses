package hr.fer.zemris.java.hw05.db;

/**
 * Functional interface whose method called "get" gets String stored in
 * StudentRecord. which field is returned depends on implementation of this
 * strategy
 * 
 * @author juren
 *
 */
@FunctionalInterface
public interface IFieldValueGetter {
	/**
	 * Gets String stored in StudentRecord. which field is returned depends on
	 * implementation of this strategy
	 * 
	 * @param record from where string is extracted
	 * @return string extracted from studentRecord
	 */
	public String get(StudentRecord record);
}
