package hr.fer.zemris.java.custom.scripting.tokens;

/**
 * ElementString is a class that inherits {@link Element} class and has one
 * string that it stores.
 * 
 * @author juren
 *
 */
public class ElementString extends Element {
	/**
	 * Read-only property of {@link ElementString} class.
	 */
	private String value;

	/**
	 * Constructor that sets value-property to value of argument
	 * 
	 * @param value
	 */
	public ElementString(String value) {
		this.value = value;
	}

	/**
	 * Method that returns string value stored in class.
	 * 
	 * @return value as string
	 */
	@Override
	public String asText() {
		return value;
	}

	@Override
	public String toString() {
		return "\"" + value + "\"";
	}

}
