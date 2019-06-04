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

@WebServlet(urlPatterns = { "/glasanje-rezultati" })
public class GlasanjeRezultatiServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String fileName = req.getServletContext().getRealPath("\\WEB-INF\\glasanje-rezultati.txt");
		if (!Files.exists(Paths.get(fileName))) {
			Files.createFile(Paths.get(fileName));
		}

		
		List<Record> records = Record.readDefinition(req);
		Record.updateRecords(records, fileName);
		
		
		records.sort((f, s) -> Integer.compare(s.getVotes(), f.getVotes()));
		int max = records.get(0).getVotes();
		List<Record> win = records.stream().filter(t -> t.getVotes() == max).collect(Collectors.toList());

		req.setAttribute("list", records);
		req.setAttribute("win", win);

		req.getRequestDispatcher("\\WEB-INF\\pages\\glasanjeRez.jsp").forward(req, resp);
	}

}
