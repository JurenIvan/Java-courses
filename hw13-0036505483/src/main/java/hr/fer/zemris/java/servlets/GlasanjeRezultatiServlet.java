package hr.fer.zemris.java.servlets;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Class that models a servlet used for setting up data for
 * glasanje-rezultati.jsp page. Data is read from files, sorted based on number
 * of votes.
 *
 * Scriplet sets up two lists. first one is list of all records with appropriate
 * vote count, second one is list of current leaders (records with most votes)
 * 
 * @author juren
 *
 */
@WebServlet(urlPatterns = { "/glasanje-rezultati" })
public class GlasanjeRezultatiServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String fileName = req.getServletContext().getRealPath("\\WEB-INF\\glasanje-rezultati.txt");
		if (!Files.exists(Paths.get(fileName))) {
			Files.createFile(Paths.get(fileName));
		}

		List<Record> records = Record.loader(req);

		records.sort((f, s) -> Integer.compare(s.getVotes(), f.getVotes()));
		int max = records.get(0).getVotes();
		List<Record> win = records.stream().filter(t -> t.getVotes() == max).collect(Collectors.toList());

		req.setAttribute("list", records);
		req.setAttribute("win", win);

		req.getRequestDispatcher("\\WEB-INF\\pages\\glasanjeRez.jsp").forward(req, resp);
	}

}
