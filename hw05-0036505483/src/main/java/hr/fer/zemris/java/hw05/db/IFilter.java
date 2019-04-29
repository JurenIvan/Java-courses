package hr.fer.zemris.java.hw05.db;

/**
 * public interface whose method returns boolean which is true if record passed
 * satisfies implementation of accepts.
 * 
 * @author juren
 *
 */
@FunctionalInterface
public interface IFilter {
	/**
	 * returns boolean which is true if record passed satisfies implementation of
	 * accepts.
	 * 
	 * @param record StudentRecord that is tested
	 * @return boolean representing whether the passed argument satisfies its implementation
	 */
	public boolean accepts(StudentRecord record);
}