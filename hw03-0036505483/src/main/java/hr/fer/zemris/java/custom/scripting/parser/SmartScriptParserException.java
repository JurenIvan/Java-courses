package hr.fer.zemris.java.custom.scripting.parser;

/**
 * RuntimeException thrown to indicate parser error.
 * 
 * @author juren
 *
 */
public class SmartScriptParserException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructs an SmartScriptParserException with the specified detail message.
	 *
	 * @param s the detail message.
	 */
	public SmartScriptParserException(String message) {
		super(message);
	}

	/**
	 * Constructs an SmartScriptParserException with no detail message.
	 */
	public SmartScriptParserException() {
		super();
	}
	
}
