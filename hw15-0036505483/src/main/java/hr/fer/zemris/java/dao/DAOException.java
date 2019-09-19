package hr.fer.zemris.java.dao;

/**
 * Exception that is thrown when error occures during communication with
 * underlaying database layer.
 * 
 * @author juren
 *
 */
public class DAOException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor
	 */
	public DAOException() {
	}

	/**
	 * 
	 * Standard constructor for exception
	 * 
	 * @param message            that describes exception
	 * @param cause              wrapped exception
	 * @param enableSuppression  flag that enables suppression
	 * @param writableStackTrace flag that enables writeableStackTrae
	 */
	public DAOException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	/**
	 * 
	 * Standard constructor for exception
	 * 
	 * @param message that describes
	 * @param cause   wrapped exception
	 */
	public DAOException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * 
	 * Standard constructor for exception
	 * 
	 * @param message that describes
	 */
	public DAOException(String message) {
		super(message);
	}

	/**
	 * 
	 * Standard constructor for exception
	 * 
	 * @param cause exception that is wrapped
	 */
	public DAOException(Throwable cause) {
		super(cause);
	}
}