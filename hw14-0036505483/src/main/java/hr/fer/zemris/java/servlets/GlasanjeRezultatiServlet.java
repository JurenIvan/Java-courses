package hr.fer.zemris.java.servlets;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.dao.DAOProvider;
import hr.fer.zemris.java.model.PollOptionModel;

/**
 * Class that models a servlet used for setting up data for
 * glasanje-rezultati.jsp page. Data is read from database, sorted based on number
 * of votes.
 *
 * Scriplet sets up two lists. first one is list of all records with appropriate
 * vote count, second one is list of current leaders (records with most votes)
 * 
 * @author juren
 *
 */
@WebServlet(urlPatterns = { "/servleti/glasanje-rezultati" })
public class GlasanjeRezultatiServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
			
		long pollIDNum = Long.parseLong(req.getParameter("pollID"));
		List<PollOptionModel> results=DAOProvider.getDao().getResultsData(pollIDNum);
		
		results.sort((f, s) -> Long.compare(s.getVoteCount(), f.getVoteCount()));
		long max = results.get(0).getVoteCount();
		List<PollOptionModel> win = results.stream().filter(t -> t.getVoteCount() == max).collect(Collectors.toList());

		req.setAttribute("list", results);
		req.setAttribute("pollID", results.get(0).getPollID());
		req.setAttribute("win", win);

		req.getRequestDispatcher("/WEB-INF/pages/glasanjeRez.jsp").forward(req, resp);
	}

}
