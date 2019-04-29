package hr.fer.zemris.java.custom.collections.demo;

import hr.fer.zemris.java.custom.collections.ArrayIndexedCollection;
import hr.fer.zemris.java.custom.collections.Collection;
import hr.fer.zemris.java.custom.collections.ElementsGetter;

/**
 * Class containing a main method with a simple demo of capabilities of
 * collections that implement List interface.
 * 
 * @author juren
 *
 */
public class Podzadatak02Demo4 {

	/**
	 * Simple program demonstrating that collection can have multiple ElementGetters
	 * without them interfering
	 * 
	 * @param args not used
	 */
	public static void main(String[] args) {
		Collection col = new ArrayIndexedCollection();
		// Collection col= new LinkedListIndexedCollection();

		col.add("Ivo");
		col.add("Ana");
		col.add("Jasna");
		ElementsGetter getter1 = col.createElementsGetter();
		ElementsGetter getter2 = col.createElementsGetter();
		System.out.println("Jedan element: " + getter1.getNextElement());
		System.out.println("Jedan element: " + getter1.getNextElement());
		System.out.println("Jedan element: " + getter2.getNextElement());
		System.out.println("Jedan element: " + getter1.getNextElement());
		System.out.println("Jedan element: " + getter2.getNextElement());
	}

}
