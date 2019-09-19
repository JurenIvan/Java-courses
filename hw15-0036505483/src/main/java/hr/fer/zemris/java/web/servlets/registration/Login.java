package hr.fer.zemris.java.web.servlets.registration;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.dao.DAOProvider;
import hr.fer.zemris.java.forms.LoginFormularForm;
import hr.fer.zemris.java.model.BlogUser;
import hr.fer.zemris.java.servlets.EncriptingUtil;

/**
 * Servlet used for Login. calculates and compares sha1 has values of paswords
 * 
 * @author juren
 *
 */
@WebServlet("/servleti/login")
public class Login extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		List<BlogUser> blogUsers = DAOProvider.getDAO().getUsers();
		req.setAttribute("blogUsers", blogUsers);

		LoginFormularForm f = new LoginFormularForm();
		f.fillFromHttpsRequest(req);
		f.validate();

		if (f.hasErrors()) {
			req.setAttribute("loginEntry", f);
			req.getRequestDispatcher("/WEB-INF/pages/WelcomePage.jsp").forward(req, resp);
			return;
		}

		BlogUser bu = DAOProvider.getDAO().getUser(f.getNick());
		if (bu == null) {
			f.putError("nick", "No such user exists");
			req.setAttribute("loginEntry", f);
			req.getRequestDispatcher("/WEB-INF/pages/WelcomePage.jsp").forward(req, resp);
			return;
		}

		if (!bu.getPasswordHash().equals(EncriptingUtil.calculateSha(f.getPass()))) {
			f.putError("pass", "Wrong password for provided username.");
			req.setAttribute("loginEntry", f);
			req.getRequestDispatcher("/WEB-INF/pages/WelcomePage.jsp").forward(req, resp);
			return;
		}

		req.getSession().setAttribute("current.user.id", bu.getId());
		req.getSession().setAttribute("current.user.fn", bu.getFirstName());
		req.getSession().setAttribute("current.user.nick", bu.getNick());
		req.getSession().setAttribute("current.user.email", bu.getEmail());
		req.getSession().setAttribute("current.user.ln", bu.getLastName());
		

		resp.sendRedirect("main");
	}
}
