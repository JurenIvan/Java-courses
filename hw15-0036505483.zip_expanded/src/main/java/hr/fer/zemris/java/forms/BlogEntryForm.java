package hr.fer.zemris.java.forms;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import hr.fer.zemris.java.model.BlogEntry;

/**
 * Class that is use as a datastructure while working with forms.
 * 
 * @author juren
 *
 */
public class BlogEntryForm {
	/**
	 * Variable that stores string resembling title
	 */
	private String title;
	/**
	 * Variable that stores string resembling text
	 */
	private String text;

	/**
	 * Storage for all errors that might occur while working with class
	 */
	Map<String, String> errors = new HashMap<>();

	/**
	 * Standard constructor
	 */
	public BlogEntryForm() {
	}

	/**
	 * Standard getter for errors in error map
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
		this.title = prepare(req.getParameter("title"));
		this.text = prepare(req.getParameter("text"));

	}

	/**
	 * method that fills this dataStructure from given argument
	 * 
	 * @param r place where we take data from
	 */
	public void fillFromRecord(BlogEntry r) {
		this.title = prepare(r.getTitle());
		this.text = prepare(r.getText());
	}

	/**
	 * method that fills this dataStructure from given argument
	 * 
	 * @param r place where we take data from
	 */
	public void fillToRecord(BlogEntry r) {
		r.setText(this.text);
		r.setTitle(this.title);
	}

	/**
	 * Method that checks whether all predefined conditions are met. Errors are put
	 * in map
	 */
	public void validate() {
		errors.clear();

		if (this.title.isEmpty()) {
			errors.put("title", "Title must be provided!");
			return;
		}
		if (this.text.isEmpty()) {
			errors.put("text", "Text must be provided!");
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
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
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
