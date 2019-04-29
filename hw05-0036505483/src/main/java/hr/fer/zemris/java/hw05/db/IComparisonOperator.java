package hr.fer.zemris.java.hw05.db;

/**
 * Public interface containing method satisfied that takes 2 strings and
 * returns boolean if they satisfy its purpose.
 * 
 * @author juren
 *
 */
@FunctionalInterface
public interface IComparisonOperator {

	/**
	 * Takes 2 Strings and looks whether they satisfy implementation purpose of
	 * method. Returns boolean.
	 * 
	 * @param value1 value passed into method.
	 * @param value2 second value passed into method
	 * @return boolean representing whether two arguments satisfy implementation.
	 */
	public boolean satisfied(String value1, String value2);
}
