package hr.fer.zemris.java.custom.scripting.tokens;

/**
 * ElementConstantDouble is a class that inherits {@link Element} class and has one double value that it stores.
 * @author juren
 *
 */
public class ElementConstantDouble extends Element {
	/**
	 * Read-only property of {@link ElementConstantDouble} class.
	 */
	private double value;
	
	/**
	 * Constructor that sets value.
	 * @param value to be stored
	 */
	public ElementConstantDouble(double value) {
		this.value=value;
	}
	
	/**
	 * Method that returns value(double) stored in class.
	 * 
	 * @return double as string 
	 */
	@Override
	public String asText() {
		return Double.toString(value);
	}
	
	@Override
	public String toString() {
		return Double.toString(value);
	}
}
