package hr.fer.zemris.java.custom.collections;

/**
 * RuntimeException thrown to indicate error when empty stack gets request to
 * give something stored in it.
 * 
 * @author juren
 *
 */
public class EmptyStackException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructs an EmptyStackException with the specified detail message.
	 *
	 * @param s the detail message.
	 */
	public EmptyStackException(String message) {
		super(message);
	}

	/**
	 * Constructs an EmptyStackException with no detail message.
	 */
	public EmptyStackException() {
		super();
	}

}
