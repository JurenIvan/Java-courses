package hr.fer.zemris.java.hw06.shell;

/**
 * ShellIOException is used as universal Exception for Shell input output
 * problems.
 * 
 * @author juren
 *
 */
public class ShellIOException extends RuntimeException {

	private static final long serialVersionUID = -1316278483677184679L;

	/**
	 * Constructs an ShellIOException with the specified detail message.
	 *
	 * @param message the detail message.
	 */
	public ShellIOException(String message) {
		super(message);
	}

	/**
	 * Constructs an ShellIOException with no detail message.
	 */
	public ShellIOException() {
		super();
	}
}
