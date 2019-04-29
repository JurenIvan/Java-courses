package hr.fer.zemris.java.custom.collections.demo;

import hr.fer.zemris.java.custom.collections.ArrayIndexedCollection;
import hr.fer.zemris.java.custom.collections.Collection;
import hr.fer.zemris.java.custom.collections.LinkedListIndexedCollection;

/**
 * Class containing a main method with a simple demo of capabilities of
 * collections that implement List interface.
 * 
 * @author juren
 */
public class Podzadatak05Demo1 {

	/**
	 * Simple program demonstrating use of {@link EvenIntegerTester} and
	 * addAllSatisfying method
	 * 
	 * @param args not used
	 */
	public static void main(String[] args) {
		Collection col1 = new LinkedListIndexedCollection();
		Collection col2 = new ArrayIndexedCollection();

		col1.add(2);
		col1.add(3);
		col1.add(4);
		col1.add(5);
		col1.add(6);
		col2.add(12);

		col2.addAllSatisfying(col1, new EvenIntegerTester());
		col2.forEach(System.out::println);
	}

}
