package hr.fer.zemris.java.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Class that models a servlet used for preparing data(color) for index.jsp.
 * Default color is white. If illegal hex code gets sent, color will be set to
 * white.
 * 
 * @author juren
 *
 */
@WebServlet(urlPatterns = { "/setcolor" })
public class SetColorServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String color = req.getParameter("color");

		if (color == null) {
			color = "FFFFFF";
		}

		req.getSession().setAttribute("pickedBgColor", color);
		req.getRequestDispatcher("/index.jsp").forward(req, resp);
	}

}
