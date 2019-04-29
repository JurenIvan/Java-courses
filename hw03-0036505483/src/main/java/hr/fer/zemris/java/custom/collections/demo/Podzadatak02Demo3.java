package hr.fer.zemris.java.custom.collections.demo;

import hr.fer.zemris.java.custom.collections.ArrayIndexedCollection;
import hr.fer.zemris.java.custom.collections.Collection;
import hr.fer.zemris.java.custom.collections.ElementsGetter;

/**
 * Class containing a main method with a simple demo of capabilities of
 * collections that implement List interface.
 * 
 * @author juren
 */
public class Podzadatak02Demo3 {

	/**
	 * Simple program demonstrating that user is not obligated to call for
	 * hasNextElement before calling getNextElement
	 * 
	 * @param args not used
	 */
	public static void main(String[] args) {
		Collection col = new ArrayIndexedCollection();
		// Collection col= new LinkedListIndexedCollection();

		col.add("Ivo");
		col.add("Ana");
		col.add("Jasna");

		ElementsGetter getter = col.createElementsGetter();
		System.out.println("Jedan element: " + getter.getNextElement());
		System.out.println("Jedan element: " + getter.getNextElement());
		System.out.println("Jedan element: " + getter.getNextElement());
		System.out.println("Jedan element: " + getter.getNextElement());
	}

}
