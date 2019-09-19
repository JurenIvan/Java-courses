package hr.fer.zemris.java.forms;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import hr.fer.zemris.java.model.BlogComment;

/**
 * Class that is use as a datastructure while working with forms.
 * 
 * @author juren
 *
 */
public class BlogCommentForm {

	/**
	 * Variable that stores string resembling eMail
	 */
	private String email;
	/**
	 * Variable that stores string resembling message
	 */
	private String message;

	/**
	 * Storage for all errors that might occur while working with class
	 */
	Map<String, String> errors = new HashMap<>();

	/**
	 * Standard constructor
	 */
	public BlogCommentForm() {
	}

	/**
	 * Standard getter
	 * 
	 * @param name
	 * @return
	 */
	public String getError(String name) {
		return errors.get(name);
	}

	/**
	 * Method that answers whether there were some problems during copy
	 * 
	 * @return
	 */
	public boolean hasErrors() {
		return !errors.isEmpty();
	}

	/**
	 * method that returns boolean dependent on error that we ask for
	 * 
	 * @param name of error
	 * @return
	 */
	public boolean hasError(String name) {
		return errors.containsKey(name);
	}

	/**
	 * Method that fills this dataStructure from {@link HttpServletRequest}
	 * 
	 * @param req request
	 */
	public void fillFromHttpsRequest(HttpServletRequest req) {
		this.email = prepare(req.getParameter("email"));
		this.message = prepare(req.getParameter("message"));

	}

	/**
	 * method that fills this dataStructure from given argument
	 * 
	 * @param r place where we take data from
	 */
	public void fillToRecord(BlogComment r) {
		r.setMessage(this.message);
		r.setUsersEMail(this.email);
	}

	/**
	 * Method that checks whether all predefined conditions are met. Errors are put
	 * in map
	 */
	public void validate() {
		errors.clear();

		if (this.message.isEmpty()) {
			errors.put("message", "Message must be provided!");
			return;
		}
	}

	/**
	 * prepares String by eliminating null pointers
	 * 
	 * @param s string that is transformed
	 * @return
	 */
	private String prepare(String s) {
		if (s == null)
			return "";
		return s.trim();
	}

	/**
	 * Method that puts error into storage of errors
	 * 
	 * @param key
	 * @param message
	 */
	public void putError(String key, String message) {
		errors.put(key, message);
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the errors
	 */
	public Map<String, String> getErrors() {
		return errors;
	}

	/**
	 * @param errors the errors to set
	 */
	public void setErrors(Map<String, String> errors) {
		this.errors = errors;
	}

}
