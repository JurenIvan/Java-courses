package hr.fer.zemris.java.custom.scripting.tokens;

/**
 * ElementConstantInteger is a class that inherits {@link Element} class and has
 * one int value that it stores.
 * 
 * @author juren
 *
 */
public class ElementConstantInteger extends Element {
	/**
	 * Read-only property of {@link ElementConstantInteger} class.
	 */
	private int value;

	public ElementConstantInteger(int value) {
		this.value = value;
	}

	/**
	 * Method that returns value(int) stored in class.
	 * 
	 * @return value(int) as string
	 */
	@Override
	public String asText() {
		return Integer.toString(value);
	}
	
	@Override
	public String toString() {
		return Integer.toString(value);
	}
}
