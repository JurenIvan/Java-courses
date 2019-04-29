package hr.fer.zemris.java.custom.collections.demo;

import java.util.ConcurrentModificationException;
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
public class PodZadatak03Demo1 {

	/**
	 * Simple program demonstrating concurrent safety of ElementGetters and
	 * {@link ConcurrentModificationException}
	 * 
	 * @param args not used
	 */
	public static void main(String[] args) {
		Collection col = new ArrayIndexedCollection();
		col.add("Ivo");
		col.add("Ana");
		col.add("Jasna");
		
		ElementsGetter getter = col.createElementsGetter();
		System.out.println("Jedan element: " + getter.getNextElement());
		System.out.println("Jedan element: " + getter.getNextElement());
		col.clear();
		System.out.println("Jedan element: " + getter.getNextElement());
	}

}
