package hr.fer.zemris.web.servlets.registration;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.model.BlogUser;

@WebServlet("/newUser")
public class NewUser extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		BlogUser r = new BlogUser();
		RegistrationFormularForm f = new RegistrationFormularForm();
		f.fillFromRecord(r);
		
		req.setAttribute("record", f);
		req.getRequestDispatcher("/WEB-INF/pages/Formular.jsp").forward(req, resp);
	}
}
