package hr.fer.zemris.java.web.servlets.registration;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.dao.DAOProvider;
import hr.fer.zemris.java.forms.RegistrationFormularForm;
import hr.fer.zemris.java.model.BlogUser;

/**
 * Servlet used for user registration
 * 
 * @author juren
 *
 */
@WebServlet("/servleti/register")
public class Register extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");

		String method = req.getParameter("method");
		if (!"Register".equals(method)) {
			resp.sendRedirect("main");
			// req.getRequestDispatcher("/WEB-INF/pages/WelcomePage.jsp").forward(req,
			// resp);
			return;
		}

		RegistrationFormularForm f = new RegistrationFormularForm();
		f.fillFromHttpsRequest(req);
		f.validate();

		if (f.hasErrors()) {
			req.setAttribute("record", f);
			req.getRequestDispatcher("/WEB-INF/pages/Formular.jsp").forward(req, resp);
			return;
		}

		BlogUser newOne = new BlogUser();
		f.fillToRecord(newOne);
		BlogUser user = DAOProvider.getDAO().getUser(newOne.getNick());

		if (user != null) {
			f.putError("nick", "Nick already in use! Pick another one");
			req.setAttribute("record", f);
			req.getRequestDispatcher("/WEB-INF/pages/Formular.jsp").forward(req, resp);
			return;
		}
		DAOProvider.getDAO().commitBlogUser(newOne);

		resp.sendRedirect("main");
	}

}
