package hr.fer.zemris.web.servlets.registration;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.dao.DAOProvider;
import hr.fer.zemris.model.BlogUser;

@WebServlet("/register")
public class Register extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		processRegistrationAttempt(req, resp);
	}

	protected void processRegistrationAttempt(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");

		String method = req.getParameter("method");
		if (!"Register".equals(method)) {
			resp.sendRedirect("/index.html");
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

//		req.getSession().setAttribute("current.user.id", newOne.getId());
//        req.getSession().setAttribute("current.user.fn", newOne.getFirstName());
//        req.getSession().setAttribute("current.user.ln", newOne.getLastName());
//        req.getSession().setAttribute("current.user.nick", newOne.getNick());

		resp.sendRedirect(req.getServletContext().getContextPath() + "/index.html");
	}

}
