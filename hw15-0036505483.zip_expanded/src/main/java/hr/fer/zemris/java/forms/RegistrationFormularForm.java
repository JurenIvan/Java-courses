package hr.fer.zemris.java.forms;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import hr.fer.zemris.java.model.BlogUser;
import hr.fer.zemris.java.servlets.EncriptingUtil;

/**
 * Class that is use as a datastructure while working with forms.
 * 
 * @author juren
 *
 */
public class RegistrationFormularForm {

	/**
	 * Variable that stores string resembling first name
	 */
	private String firstName;
	/**
	 * Variable that stores string resembling last name
	 */
	private String lastName;
	/**
	 * Variable that stores string resembling email
	 */
	private String email;
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
	public RegistrationFormularForm() {
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
		this.firstName = prepare(req.getParameter("firstName"));
		this.lastName = prepare(req.getParameter("lastName"));
		this.email = prepare(req.getParameter("email"));
		this.nick = prepare(req.getParameter("nick"));
		this.pass = prepare(req.getParameter("pass"));
	}

	/**
	 * method that fills this dataStructure from given argument
	 * 
	 * @param r place where we take data from
	 */
	public void fillFromRecord(BlogUser r) {
		this.firstName = prepare(r.getFirstName());
		this.lastName = prepare(r.getLastName());
		this.email = prepare(r.getEmail());
		this.nick = prepare(r.getNick());
	}

	/**
	 * Method that fills this dataStructure from {@link HttpServletRequest}
	 * 
	 * @param req request
	 */
	public void fillToRecord(BlogUser r) {
		r.setEmail(this.email);
		r.setFirstName(this.firstName);
		r.setLastName(this.lastName);
		r.setNick(this.nick);
		r.setPasswordHash(EncriptingUtil.calculateSha(this.pass));
	}

	/**
	 * Method that checks whether all predefined conditions are met. Errors are put
	 * in map
	 */
	public void validate() {
		errors.clear();

		if (this.firstName.isEmpty()) {
			errors.put("firstName", "First name must be provided!");
			return;
		}
		if (this.lastName.isEmpty()) {
			errors.put("lastName", "Last Name must be provided!");
			return;
		}

		if (this.email.isEmpty()) {
			errors.put("email", "Email must be provided!");
			return;
		}

		if (this.nick.isEmpty()) {
			errors.put("nick", "Nick must be provided!");
			return;
		}

		if (this.pass.isEmpty()) {
			errors.put("pass", "Password must be provided!");
			return;
		}

		if (this.email.isEmpty()) {
			errors.put("email", "Email must be provided!");
		} else {
			int l = email.length();
			int p = email.indexOf('@');
			if (l < 3 || p == -1 || p == 0 || p == l - 1) {
				errors.put("email", "Email is not properly formatted!");
			}
		}

	}

	/**
	 * method that fills this dataStructure from given argument
	 * 
	 * @param r place where we take data from
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
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @return the nick
	 */
	public String getNick() {
		return nick;
	}

	/**
	 * @return the pass
	 */
	public String getPass() {
		return pass;
	}

	/**
	 * @return the errors
	 */
	public Map<String, String> getErrors() {
		return errors;
	}

}
