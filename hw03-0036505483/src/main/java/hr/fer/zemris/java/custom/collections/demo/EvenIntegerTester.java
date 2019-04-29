package hr.fer.zemris.java.custom.collections.demo;

import hr.fer.zemris.java.custom.collections.Tester;

/**
 * Class containing a simple implementation of {@link Tester} interface with
 * proper test method and a main method that is demonstrating it's use.
 * 
 * @author juren
 *
 */
public class EvenIntegerTester implements Tester {
	/**
	 * test method checks if given object is of type Integer and if it IS integer
	 * whether it is even.
	 */
	@Override
	public boolean test(Object obj) {
		if (!(obj instanceof Integer))
			return false;
		Integer i = (Integer) obj;
		return i % 2 == 0;
	}

	/**
	 * Simple program demonstrating {@link EvenIntegerTester} and test method
	 * implemented in it
	 * 
	 * @param args not used
	 */
	public static void main(String[] args) {

		Tester t = new EvenIntegerTester();
		System.out.println(t.test("Ivo"));
		System.out.println(t.test(22));
		System.out.println(t.test(3));
	}

}
