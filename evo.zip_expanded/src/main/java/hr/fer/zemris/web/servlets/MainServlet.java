package hr.fer.zemris.web.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.dao.jpa.JPAEMProvider;
import hr.fer.zemris.model.BlogUser;

@WebServlet("/servleti/main")
public class MainServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		List<BlogUser> blogUsers = JPAEMProvider.getEntityManager().createNamedQuery("getAllUsers",BlogUser.class).getResultList();
		req.setAttribute("blogUsers", blogUsers);

		req.getRequestDispatcher("/WEB-INF/pages/WelcomePage.jsp").forward(req, resp);
	}

}
