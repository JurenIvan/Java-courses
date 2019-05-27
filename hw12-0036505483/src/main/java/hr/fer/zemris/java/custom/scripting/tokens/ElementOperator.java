package hr.fer.zemris.java.custom.scripting.tokens;

/**
 * ElementOperator is a class that inherits {@link Element} class and has one
 * string that it stores.
 * 
 * @author juren
 *
 */
public class ElementOperator extends Element {
	/**
	 * Read-only property of {@link ElementOperator} class.
	 */
	private String symbol;

	public ElementOperator(String symbol) {
		this.symbol = symbol;
	}

	/**
	 * Method that returns symbol string stored in class.
	 * 
	 * @return symbol as string
	 */
	@Override
	public String asText() {
		return symbol;
	}
	
	@Override
	public String toString() {
		return symbol;
	}
}
