package hr.fer.zemris.web.servlets.login;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.dao.DAOProvider;
import hr.fer.zemris.model.BlogUser;
import hr.fer.zemris.web.servlets.EncriptingUtil;

/**
 * Servlet koji obavlja akciju /login. Pogledajte slideove za opis: tamo
 * odgovara akciji /update.
 * 
 * @author Juren
 */
@WebServlet("/login")
public class Login extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		processLoginAttempt(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		processLoginAttempt(req, resp);
	}

	protected void processLoginAttempt(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");

		String method = req.getParameter("method");
		if (!"Log in".equals(method)) {
			resp.sendRedirect(req.getServletContext().getContextPath() + "/index.html");
			return;
		}

		LoginFormularForm f = new LoginFormularForm();
		f.fillFromHttpsRequest(req);
		f.validate();

		if (!f.hasErrors()) {
			req.setAttribute("loginEntry", f);
			req.getRequestDispatcher("/index.html").forward(req, resp);
			return;
		}

		BlogUser bu = DAOProvider.getDAO().getUser(f.getNick());
		if (bu == null) {
			f.putError("nick", "No such user exists");
			req.setAttribute("loginEntry", f);
			req.getRequestDispatcher("/index.html").forward(req, resp);
			return;
		}

		if (!bu.getPasswordHash().equals(EncriptingUtil.calculateSha(f.getPass()))) {
			f.putError("pass", "Wrong password for provided username.");
			req.setAttribute("loginEntry", f);
			req.getRequestDispatcher("/index.html").forward(req, resp);
			return;
		}

		req.getSession().setAttribute("current.user.id", bu.getId());
		req.getSession().setAttribute("current.user.fn", bu.getFirstName());
		req.getSession().setAttribute("current.user.ln", bu.getLastName());
		req.getSession().setAttribute("current.user.nick", bu.getNick());

		resp.sendRedirect(req.getServletContext().getContextPath() + "/index.html");
	}
}
