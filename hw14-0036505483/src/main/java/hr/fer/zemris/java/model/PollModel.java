package hr.fer.zemris.java.model;

/**
 * Class serving as a datastructure that models poll information
 * 
 * @author juren
 *
 */
public class PollModel {

	/**
	 * variable that stores unique ID number
	 */
	private long id;
	/**
	 * Variable that stores title of poll
	 */
	private String title;
	/**
	 * Variable that stores poll description
	 */
	private String message;

	/**
	 * 
	 * Standard constructor
	 * 
	 * @param id
	 * @param title
	 * @param message
	 */
	public PollModel(long id, String title, String message) {
		super();
		this.id = id;
		setTitle(title);
		setMessage(message);

	}

	/**
	 * Standard getter for id
	 * 
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * Standard setter for id
	 * 
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * Standard getter for title
	 * 
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Standard setter for title
	 * 
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Standard getter for message
	 * 
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Standard setter for message
	 * 
	 * @param message the message to set
	 */
	public void setMessage(String message) {

		this.message = message;
	}

}
