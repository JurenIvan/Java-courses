package hr.fer.zemris.java.custom.scripting.lexer;
/**
 * RuntimeException thrown to indicate Lexer error.
 * 
 * @author juren
 *
 */
public class LexerException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructs an LexerException with the specified detail message.
	 *
	 * @param s the detail message.
	 */
	public LexerException(String message) {
		super(message);
	}

	/**
	 * Constructs an LexerException with no detail message.
	 */
	public LexerException() {
		super();
	}
	
}
