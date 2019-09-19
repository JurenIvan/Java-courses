package hr.fer.zemris.java.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.dao.DAOProvider;
import hr.fer.zemris.java.model.BlogUser;

/**
 * Servlet used as a starting point of a webapp
 * 
 * @author juren
 *
 */
@WebServlet("/servleti/main")
public class MainServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		List<BlogUser> blogUsers = DAOProvider.getDAO().getUsers();
		req.setAttribute("blogUsers", blogUsers);
		req.getRequestDispatcher("/WEB-INF/pages/WelcomePage.jsp").forward(req, resp);
	}
}
