package hr.fer.zemris.web.servlets.login;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import hr.fer.zemris.model.BlogUser;

public class LoginFormularForm {
	
	private String nick;
	private String pass;
	
	Map<String, String> errors = new HashMap<>();
	
	public LoginFormularForm() {
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
		this.nick = prepare(req.getParameter("nick"));
		this.pass = prepare(req.getParameter("pass"));
	}
	
	public void fillFromRecord(BlogUser r) {
		this.nick = prepare(r.getNick());
	}
	
	public void fillToRecord(LoginRecord r) {
		r.setNick(this.nick);
		r.setPass(this.pass);
	}

	private String prepare(String s) {
		if(s==null) return "";
		return s.trim();
	}

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

	public void putError(String key, String message) {
		errors.put(key, message);
	}

	public String getNick() {
		return this.nick;
	}
	
	public String getPass() {
		return this.pass;
	}
	
}
