package hr.fer.zemris.java.custom.collections;

/**
 * Interface with declared test method which takes an object and checks whether
 * the given object passes the criteria given/implemented in test method.
 * 
 * @author juren
 */
public interface Tester {
	/**
	 * Method that is called to check whether an object can pass the test.
	 * 
	 * @param obj test subject
	 * @return boolean representing a object passed a test defined in function
	 */
	boolean test(Object obj);
}
