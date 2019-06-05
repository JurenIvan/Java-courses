package hr.fer.zemris.java.servlets;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Class that models a servlet used for updating current vote count. Update
 * affects file where results are stored.
 * 
 * @author juren
 *
 */
@WebServlet(urlPatterns = { "/glasanje-glasaj" })
public class GlasanjeGlasajServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String fileName = req.getServletContext().getRealPath("/WEB-INF/glasanje-rezultati.txt");
		if (!Files.exists(Paths.get(fileName))) {
			Files.createFile(Paths.get(fileName));
		}

		List<Record> records = Record.loader(req);

		String id = req.getParameter("id");
		addOne(records, id);
		updateFile(fileName, records);

		resp.sendRedirect(req.getContextPath() + "/glasanje-rezultati");
	}

	/**
	 * Method that searches collection of records, finds appropriate one and adds
	 * one vote to it's count
	 * 
	 * @param records list of records where record with provided id should be.
	 * @param id      of record whose vote count should be increased
	 */
	private static void addOne(List<Record> records, String id) {
		Objects.requireNonNull(records, "Must provide a list");
		Objects.requireNonNull(id, "Must provide a id");

		Record r = records.get(Record.findIndexOf(id, records));
		r.setVotes(r.getVotes() + 1);
	}

	/**
	 * Method used to update file used for storing results. It rewrites file with
	 * new vote count.
	 * 
	 * @param fileName path to file
	 * @param records  list of records with updated vote count
	 * @throws IOException file can not be written into ( not a regular file)
	 */
	private static void updateFile(String fileName, List<Record> records) throws IOException {
		StringBuilder sb = new StringBuilder();
		for (var l : records) {
			sb.append(l.getId() + "\t" + l.getVotes() + "\r\n");
		}
		Files.writeString(Paths.get(fileName), sb.toString());
	}

}
