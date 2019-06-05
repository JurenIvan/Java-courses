package hr.fer.zemris.java.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Class that models a servlet used for setting up data for glasanjeIndex jsp
 * page. Data that is set is list of all bands that are defined in appropriate
 * txt file.
 * 
 * @author juren
 *
 */
@WebServlet(urlPatterns = { "/glasanje" })
public class GlasanjeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		List<Record> listOfRecords = Record.loader(req);

		req.setAttribute("records", listOfRecords);
		req.getRequestDispatcher("\\WEB-INF\\pages\\glasanjeIndex.jsp").forward(req, resp);
	}

}
