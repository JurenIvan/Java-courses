package hr.fer.zemris.java.custom.scripting.elems;

/**
 * ElementVariable is a class that inherits Element, and has a single read-only
 * one String property: name.
 * 
 * @author juren
 *
 */
public class ElementVariable extends Element {
	/**
	 * Read-only property of {@link ElementVariable} class.
	 */
	private String name;

	/**
	 * Constructor that sets name
	 * 
	 * @param name to be stored
	 */
	public ElementVariable(String name) {
		this.name = name;
	}

	/**
	 * method that returns string stored in class
	 * 
	 * @return string
	 */
	@Override
	public String asText() {
		return name;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
