package hr.fer.zemris.java.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.dao.DAOProvider;
import hr.fer.zemris.java.model.PollModel;
import hr.fer.zemris.java.model.PollOptionModel;

/**
 * Class that models a servlet used for setting up data for glasanjeIndex jsp
 * page. Data that is set is list of all bands that are defined in underlaying
 * database .
 * 
 * @author juren
 *
 */
@WebServlet(urlPatterns = { "/servleti/glasanje" })
public class GlasanjeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		long arg = Long.parseLong(req.getParameter("pollID"));
		List<PollOptionModel> choises = DAOProvider.getDao().getResultsData(arg);
		PollModel desciptor = DAOProvider.getDao().getDescription(arg);

		req.setAttribute("options", choises);
		req.setAttribute("descipton", desciptor);

		req.getRequestDispatcher("/WEB-INF/pages/glasanjeIndex.jsp").forward(req, resp);
	}

}
