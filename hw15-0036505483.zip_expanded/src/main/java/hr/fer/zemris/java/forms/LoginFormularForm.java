package hr.fer.zemris.java.forms;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import hr.fer.zemris.java.model.BlogUser;

/**
 * Class that is use as a datastructure while working with forms.
 * 
 * @author juren
 *
 */
public class LoginFormularForm {
	
	/**
	 * Variable that stores string resembling nickname
	 */
	private String nick;
	/**
	 * Variable that stores string resembling password
	 */
	private String pass;
	
	/**
	 * Storage for all errors that might occur while working with class
	 */
	Map<String, String> errors = new HashMap<>();
	
	/**
	 * Standard constructor
	 */
	public LoginFormularForm() {
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
		this.nick = prepare(req.getParameter("nick"));
		this.pass = prepare(req.getParameter("pass"));
	}
	
	/**
	 * method that fills this dataStructure from given argument
	 * 
	 * @param r place where we take data from
	 */
	public void fillFromRecord(BlogUser r) {
		this.nick = prepare(r.getNick());
	}
	
	/**
	 * Method that fills this dataStructure from {@link HttpServletRequest}
	 * 
	 * @param req request
	 */
	public void fillToRecord(LoginRecord r) {
		r.setNick(this.nick);
		r.setPass(this.pass);
	}

	/**
	 * method that fills this dataStructure from given argument
	 * 
	 * @param r place where we take data from
	 */
	private String prepare(String s) {
		if(s==null) return "";
		return s.trim();
	}

	/**
	 * Method that checks whether all predefined conditions are met. Errors are put
	 * in map
	 */
	public void validate() {
		errors.clear();
		
		if(this.nick.isEmpty()) {
			errors.put("nick", "Nick must be provided!");
			return;
		}
		
		if(this.pass.isEmpty()) {
			errors.put("pass", "Password must be provided!");
			return;
		}
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

	public String getNick() {
		return this.nick;
	}
	
	public String getPass() {
		return this.pass;
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

	/**
	 * @param nick the nick to set
	 */
	public void setNick(String nick) {
		this.nick = nick;
	}

	/**
	 * @param pass the pass to set
	 */
	public void setPass(String pass) {
		this.pass = pass;
	}
	
	
	
}
