package hr.fer.zemris.java.custom.scripting.tokens;

/**
 * ElementConstantInteger is a class that inherits {@link Element} class and has one String value (name).
 * @author juren
 *
 */
public class ElementFunction extends Element {
	/**
	 * Read-only property of {@link ElementFunction} class.
	 */
	private String name;

	/**
	 * Constructor that sets name to input argument.
	 * 
	 * @param name String to be stored as name
	 */
	public ElementFunction(String name) {
		this.name = name;
	}

	/**
	 * Method that returns string name stored in class
	 * 
	 * @return string name
	 */
	@Override
	public String asText() {
		return  name;
	}
	
	@Override
	public String toString() {
		return  "@"+name;
	}
	
	
}
