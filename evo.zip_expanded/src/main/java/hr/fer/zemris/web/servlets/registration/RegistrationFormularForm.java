package hr.fer.zemris.web.servlets.registration;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import hr.fer.zemris.model.BlogUser;
import hr.fer.zemris.web.servlets.EncriptingUtil;

public class RegistrationFormularForm {

	private String firstName;
	private String lastName;
	private String email;
	private String nick;
	private String pass;

	Map<String, String> errors = new HashMap<>();

	public RegistrationFormularForm() {
	}

	public String getError(String name) {
		return errors.get(name);
	}

	public boolean hasErrors() {
		return !errors.isEmpty();
	}

	public boolean hasError(String name) {
		return errors.containsKey(name);
	}

	public void fillFromHttpsRequest(HttpServletRequest req) {
		this.firstName = prepare(req.getParameter("firstName"));
		this.lastName = prepare(req.getParameter("lastName"));
		this.email = prepare(req.getParameter("email"));
		this.nick = prepare(req.getParameter("nick"));
		this.pass = prepare(req.getParameter("pass"));
	}

	public void fillFromRecord(BlogUser r) {
		this.firstName = prepare(r.getFirstName());
		this.lastName = prepare(r.getLastName());
		this.email = prepare(r.getEmail());
		this.nick = prepare(r.getNick());
	}

	

	public void fillToRecord(BlogUser r) {
		r.setEmail(this.email);
		r.setFirstName(this.firstName);
		r.setLastName(this.lastName);
		r.setNick(this.nick);
		r.setPasswordHash(EncriptingUtil.calculateSha(this.pass));
	}

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

	private String prepare(String s) {
		if (s == null)
			return "";
		return s.trim();
	}

	public void putError(String key, String message) {
		errors.put(key, message);
	}
}
